package com.sc.aftds.unit;

import com.googlecode.cqengine.attribute.Attribute;
import com.sc.aftds.cmd.Command;
import com.sc.aftds.excel.Sex;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;


public class UnitService implements IUnitService {
    private static final Logger logger = LogManager.getLogger(UnitService.class);

    private final IUnitEngine<UnitModel> _unitEngine;
    private final Command _command;

    public UnitService(IUnitEngine<UnitModel> unitEngine, Command command) {
        _unitEngine = unitEngine;
        _command = command;
    }

    public Map<Integer, UnitModel> findAllUnitsWithBirthDateUndefined() {
        var query = equal(UnitModel.UNIT_BIRTH_DATE, LocalDate.EPOCH);
        try (var resultSet = _unitEngine.retrieve(query)) {
            return resultSet.stream().collect(Collectors.toMap(u -> u.id, u -> u));
        }
    }

    public Option<UnitModel> findUnitById(int parentId) {
        var query = equal(UnitModel.UNIT_ID, parentId);
        try (var resultSet = _unitEngine.retrieve(query)) {
            return Option.ofOptional(resultSet.stream().findFirst());
        }
    }

    public List<UnitModel> findParentsOfChild(UnitModel child) {
        var query = or(
                and(equal(UnitModel.UNIT_ID, child.fatherId), not(equal(UnitModel.UNIT_BIRTH_DATE, LocalDate.EPOCH))),
                and(equal(UnitModel.UNIT_ID, child.motherId), not(equal(UnitModel.UNIT_BIRTH_DATE, LocalDate.EPOCH)))
        );
        var order = orderBy((descending(UnitModel.UNIT_BIRTH_DATE)));
        try (var resultSet = _unitEngine.retrieve(query, queryOptions(order))) {
            return List.ofAll(resultSet.stream());
        }
    }

    public List<UnitModel> findChildrenWithDefinedBirthDates(Attribute<UnitModel, Integer> attribute, int unitId) {
        var query = and(equal(attribute, unitId), not(equal(UnitModel.UNIT_BIRTH_DATE, LocalDate.EPOCH)));
        var order = orderBy((ascending(UnitModel.UNIT_BIRTH_DATE)));
        try (var resultSet = _unitEngine.retrieve(query, queryOptions(order))) {
            return List.ofAll(resultSet.stream());
        }
    }

    @Override
    public void process() {
        var unitsWithBirthDateUndefined = findAllUnitsWithBirthDateUndefined();

        while (unitsWithBirthDateUndefined.entrySet().iterator().hasNext()) {
            var unit = unitsWithBirthDateUndefined.entrySet().iterator().next().getValue();
            var parents = findParentsOfChild(unit);
            var unitOptionByParents = defineBirthDateByParents(parents, unit);

            unitOptionByParents.peek(u -> {
                replaceUnitWithNewOneAndReduce(unit, u, unitsWithBirthDateUndefined);
            }).onEmpty(() -> {
                var children = unit.sex == Sex.Female ?
                        findChildrenWithDefinedBirthDates(UnitModel.UNIT_MOTHER_ID, unit.id) :
                        findChildrenWithDefinedBirthDates(UnitModel.UNIT_FATHER_ID, unit.id);

                var unitOptionByChildren = defineBirthDateByOldestChild(children, unit);

                unitOptionByChildren.peek(u -> {
                    replaceUnitWithNewOneAndReduce(unit, u, unitsWithBirthDateUndefined);
                }).onEmpty(() -> {
                    unitsWithBirthDateUndefined.remove(unit.id);
                    logger.warn("Failed to define birth date for " + unit);
                });
            });
        }
    }

    public Option<UnitModel> defineBirthDateByParents(List<UnitModel> parents, UnitModel unit) {
        if (parents.isEmpty()) {
            return Option.none();
        }
        var computedBirthDate = setBirthDateByParent(parents.head().birthDate);
        return Option.of(unit.copy(computedBirthDate, true));
    }

    public Option<UnitModel> defineBirthDateByOldestChild(List<UnitModel> children, UnitModel unit) {
        if (children.isEmpty()) {
            return Option.none();
        }
        var computedBirthDate = setBirthDateByChild(children.head().birthDate);
        return Option.of(unit.copy(computedBirthDate, true));
    }

    public LocalDate setBirthDateByParent(LocalDate birthDate) {
        return birthDate.plusMonths(_command.getTimeDistance());
    }

    public LocalDate setBirthDateByChild(LocalDate birthDate) {
        return birthDate.minusMonths(_command.getTimeDistance());
    }

    private void replaceUnitWithNewOneAndReduce(UnitModel oldUnit, UnitModel newUnit, Map<Integer, UnitModel> unitMap) {
        _unitEngine.remove(oldUnit);
        unitMap.remove(oldUnit.id);
        _unitEngine.add(newUnit);
    }
}

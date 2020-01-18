package com.sc.aftds.unit;

import com.googlecode.cqengine.attribute.Attribute;
import com.sc.aftds.excel.Sex;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;


public class UnitService implements IUnitService {
    private static final Logger logger = LogManager.getLogger(UnitService.class);

    private final IUnitEngine<UnitModel> unitEngine;

    public UnitService(IUnitEngine<UnitModel> unitEngine) {
        this.unitEngine = unitEngine;
    }

    public Map<Integer, UnitModel> findAllUnitsWithBirthDateUndefined() {
        var query = equal(UnitModel.UNIT_BIRTH_DATE, LocalDate.EPOCH);
        try (var resultSet = unitEngine.retrieve(query)) {
            return resultSet.stream().collect(Collectors.toMap(u -> u.id, u -> u));
        }
    }

    public Option<UnitModel> findUnitById(int parentId) {
        var query = equal(UnitModel.UNIT_ID, parentId);
        try (var resultSet = unitEngine.retrieve(query)) {
            return Option.ofOptional(resultSet.stream().findFirst());
        }
    }

    public Set<UnitModel> findParentsOfChild(UnitModel child) {
        var query = or(equal(UnitModel.UNIT_ID, child.fatherId), equal(UnitModel.UNIT_ID, child.motherId));
        try (var resultSet = unitEngine.retrieve(query)) {
            return resultSet.stream().collect(Collectors.toUnmodifiableSet());
        }
    }

    public List<UnitModel> findChildrenWithDefinedBirthDates(Attribute<UnitModel, Integer> attribute, int unitId) {
        var query = and(equal(attribute, unitId), not(equal(UnitModel.UNIT_BIRTH_DATE, LocalDate.EPOCH)));
        var options = orderBy((ascending(UnitModel.UNIT_BIRTH_DATE)));
        try (var resultSet = unitEngine.retrieve(query, queryOptions(options))) {
            var result = List.ofAll(resultSet.stream());
            return result;
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
                unitEngine.remove(unit);
                unitEngine.add(u);
                unitsWithBirthDateUndefined.remove(u.id);
            }).onEmpty(() -> {
                logger.warn("Parents of " + unit + " have birth dates undefined");

                var children = unit.sex == Sex.Female ?
                        findChildrenWithDefinedBirthDates(UnitModel.UNIT_MOTHER_ID, unit.id) :
                        findChildrenWithDefinedBirthDates(UnitModel.UNIT_FATHER_ID, unit.id);

                if (parents.isEmpty() && children.isEmpty()) {
                    unitsWithBirthDateUndefined.remove(unit.id);
                    logger.error("Missing parents and children for " + unit);
                }

                var unitOptionByChildren = defineBirthDateByOldestChild(unit, children);

                unitOptionByChildren.peek(u -> {
                    unitEngine.remove(unit);
                    unitEngine.add(u);
                    unitsWithBirthDateUndefined.remove(u.id);
                }).onEmpty(() -> logger.warn("Failed to define birth date for " + unit));
            });
        }
    }

    public Option<UnitModel> defineBirthDateByParents(Set<UnitModel> parents, UnitModel unit) {
        var mother = parents.stream().filter(u -> u.id == unit.motherId).findFirst();
        var father = parents.stream().filter(u -> u.id == unit.fatherId).findFirst();

        if (mother.isPresent()) {
            var computedBirthDate = setBirthDateByParent(mother.get().birthDate);
            return Option.of(unit.copy(computedBirthDate));
        }

        if (father.isPresent()) {
            var computedBirthDate = setBirthDateByParent(father.get().birthDate);
            return Option.of(unit.copy(computedBirthDate));
        }

        return Option.none();
    }

    public Option<UnitModel> defineBirthDateByOldestChild(UnitModel unit, List<UnitModel> children) {
        if (children.isEmpty()) {
            return Option.none();
        }
        var computedBirthDate = setBirthDateByChild(children.head().birthDate);
        return Option.of(unit.copy(computedBirthDate));
    }

    public LocalDate setBirthDateByParent(LocalDate birthDate) {
        return birthDate.plusMonths(4);
    }

    public LocalDate setBirthDateByChild(LocalDate birthDate) {
        return birthDate.minusMonths(4);
    }
}

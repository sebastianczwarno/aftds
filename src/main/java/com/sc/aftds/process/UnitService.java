package com.sc.aftds.process;

import com.googlecode.cqengine.attribute.Attribute;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.googlecode.cqengine.query.QueryFactory.*;


public class UnitService {
    private final UnitEngine unitEngine;

    public UnitService(UnitEngine unitEngine) {
        this.unitEngine = unitEngine;
    }

    public List<Unit> findAllUnitWithBirthDateUndefined() {
        var query = equal(Unit.UNIT_BIRTH_DATE, LocalDate.EPOCH);
        try (var resultSet = unitEngine.transactionalIndexedCollection.retrieve(query)) {
            return resultSet.stream().collect(Collectors.toUnmodifiableList());
        }
    }

    public Optional<Unit> findParentOfAChild(int parentId) {
        var query = equal(Unit.UNIT_ID, parentId);
        try (var resultSet = unitEngine.transactionalIndexedCollection.retrieve(query)) {
            return resultSet.stream().findFirst();
        }
    }

    public List<Unit> findChildrenWithDefinedBirthDates(Attribute<Unit, Integer> attribute, int unitId) {
        var query = and(equal(attribute, unitId), not(equal(Unit.UNIT_BIRTH_DATE, LocalDate.EPOCH)));
        try (var resultSet = unitEngine.transactionalIndexedCollection.retrieve(query)) {
            return resultSet.stream().collect(Collectors.toUnmodifiableList());
        }
    }
}

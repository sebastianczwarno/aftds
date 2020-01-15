package com.sc.aftds.process;

import com.googlecode.cqengine.attribute.Attribute;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.time.LocalDate;

import static com.googlecode.cqengine.query.QueryFactory.*;


public class UnitService {
    private final UnitEngine unitEngine;

    public UnitService(UnitEngine unitEngine) {
        this.unitEngine = unitEngine;
    }

    public List<Unit> findAllUnitsWithBirthDateUndefined() {
        var query = equal(Unit.UNIT_BIRTH_DATE, LocalDate.EPOCH);
        try (var resultSet = unitEngine.transactionalIndexedCollection.retrieve(query)) {
            return List.ofAll(resultSet.stream());
        }
    }

    public Option<Unit> findParentById(int parentId) {
        var query = equal(Unit.UNIT_ID, parentId);
        try (var resultSet = unitEngine.transactionalIndexedCollection.retrieve(query)) {
            return Option.ofOptional(resultSet.stream().findFirst());
        }
    }

    public Tuple2<Unit,Unit> findParentsOfChild(Unit child) {
        var query = or(equal(Unit.UNIT_ID, child.fatherId), equal(Unit.UNIT_ID, child.motherId));
        try(var resultSet = unitEngine.transactionalIndexedCollection.retrieve(query)) {
            var result = List.ofAll(resultSet.stream());
            return Tuple.of(result.head(), result.last());
        }
    }

    public List<Unit> findChildrenWithDefinedBirthDates(Attribute<Unit, Integer> attribute, int unitId) {
        var query = and(equal(attribute, unitId), not(equal(Unit.UNIT_BIRTH_DATE, LocalDate.EPOCH)));
        try (var resultSet = unitEngine.transactionalIndexedCollection.retrieve(query)) {
            return List.ofAll(resultSet.stream());
        }
    }
}

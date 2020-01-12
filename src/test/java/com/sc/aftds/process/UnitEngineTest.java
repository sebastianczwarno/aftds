package com.sc.aftds.process;

import com.sc.aftds.excel.ExcelSheetPosition;
import com.sc.aftds.excel.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static com.googlecode.cqengine.query.QueryFactory.and;
import static com.googlecode.cqengine.query.QueryFactory.equal;
import static com.googlecode.cqengine.query.QueryFactory.not;

public class UnitEngineTest {

    private final UnitEngine unitEngine;

    public UnitEngineTest() throws IOException {
        var file = new File("/home/sc/Documents/Baza_koty_30_12_2019.xlsx");
        unitEngine = new UnitEngine();
        var fileLoader = new FileLoader(new ExcelSheetPosition(3), unitEngine);
        fileLoader.loadIntoEngine(file);
    }

    @Test
    public void testAndQueryWithTwoParameters() {
        var query = and(equal(Unit.UNIT_FATHER_ID, 89041), equal(Unit.UNIT_BIRTH_DATE, LocalDate.EPOCH));
        try (var resultSet = unitEngine.transactionalIndexedCollection.retrieve(query)) {
            Assertions.assertEquals(8, resultSet.size());
            for (var unit: resultSet) {
                System.out.println(unit);
            }
        }
    }

    @Test
    public void testAndQueryWithTwoParametersAndNot() {
        var query = and(equal(Unit.UNIT_FATHER_ID, 89041), not(equal(Unit.UNIT_BIRTH_DATE, LocalDate.EPOCH)));
        try (var resultSet = unitEngine.transactionalIndexedCollection.retrieve(query)) {
            Assertions.assertEquals(8, resultSet.size());
            for (var unit: resultSet) {
                System.out.println(unit);
            }
        }
    }
}

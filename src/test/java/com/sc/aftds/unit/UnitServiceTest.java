package com.sc.aftds.unit;

import com.sc.aftds.excel.ExcelSheetPosition;
import com.sc.aftds.excel.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class UnitServiceTest {

    private static UnitService unitService;

    @BeforeAll
    public static void setup() throws IOException {
        var file = new File("/home/sc/Documents/Baza_koty_30_12_2019.xlsx");
        var unitEngine = new UnitEngine();
        var fileLoader = new FileLoader(new ExcelSheetPosition(3), unitEngine);
        fileLoader.loadIntoEngine(file);
        unitService = new UnitService(unitEngine);
    }

    @Test
    public void test_find_children_with_defined_birth_dates() {
        var children = unitService.findChildrenWithDefinedBirthDates(UnitModel.UNIT_FATHER_ID, 89041);
        Assertions.assertFalse(children.isEmpty());
    }

    @Test
    public void test_find_unit_by_id() {
        var children = unitService.findChildrenWithDefinedBirthDates(UnitModel.UNIT_FATHER_ID, 89041);
        unitService.findUnitById(children.last().fatherId).peek(p ->
                Assertions.assertEquals(89041, p.id)
        );
    }

    @Test
    public void test_find_all_units_with_birth_date_undefined() {
        var list = unitService.findAllUnitsWithBirthDateUndefined();
        Assertions.assertFalse(list.isEmpty());
    }

    @Test
    public void test_find_parents_of_child() {
        var children = unitService.findChildrenWithDefinedBirthDates(UnitModel.UNIT_FATHER_ID, 89041);
        var result = unitService.findParentsOfChild(children.last());
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void test_process() {
        var before = unitService.findAllUnitsWithBirthDateUndefined().size();
        unitService.process();
        var after = unitService.findAllUnitsWithBirthDateUndefined().size();
        Assertions.assertTrue(before > after);
    }
}

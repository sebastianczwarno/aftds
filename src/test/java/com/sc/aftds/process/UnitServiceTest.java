package com.sc.aftds.process;

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
    public void testFindChildrenWithDefinedBirthDates() {
        var children = unitService.findChildrenWithDefinedBirthDates(Unit.UNIT_FATHER_ID, 89041);
        Assertions.assertFalse(children.isEmpty());
    }

    @Test
    public void testFindParentById() {
        var children = unitService.findChildrenWithDefinedBirthDates(Unit.UNIT_FATHER_ID, 89041);
        unitService.findParentById(children.last().fatherId).peek(p ->
            Assertions.assertEquals(89041, p.id)
        );
    }

    @Test
    public void testFindAllUnitsWithBirthDateUndefined() {
        var list = unitService.findAllUnitsWithBirthDateUndefined();
        Assertions.assertFalse(list.isEmpty());
    }

    @Test
    public void testFindParentsOfChild() {
        var children = unitService.findChildrenWithDefinedBirthDates(Unit.UNIT_FATHER_ID, 89041);
        var result = unitService.findParentsOfChild(children.last());
        Assertions.assertNotNull(result._1);
        Assertions.assertNotNull(result._2);
    }
}

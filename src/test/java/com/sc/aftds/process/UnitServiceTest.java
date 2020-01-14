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
    public void testFindParentOfAChild() {
        var children = unitService.findChildrenWithDefinedBirthDates(Unit.UNIT_FATHER_ID, 89041);
        var child = children.stream().findFirst();
        child.ifPresent(c -> unitService.findParentOfAChild(c.fatherId).ifPresent(p ->
            Assertions.assertEquals(89041, p.id)
        ));
    }

    @Test
    public void testFindAllUnitWithBirthDateUndefined() {
        var list = unitService.findAllUnitWithBirthDateUndefined();
        Assertions.assertFalse(list.isEmpty());
    }
}

package com.sc.aftds.unit;

import com.sc.aftds.cmd.Command;
import com.sc.aftds.excel.FileLoader;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class UnitServiceTest {

    private static UnitService unitService;

    @BeforeAll
    public static void setup() throws IOException, ParseException {
        var command = new Command(new String[]{"-f /home/sc/Documents/Baza_koty_30_12_2019.xlsx", "-p 3"});
        Assertions.assertTrue(command.getFile().exists());
        IUnitEngine<UnitModel> unitEngine = new UnitEngine(UnitModel.class);
        var fileLoader = new FileLoader(command, unitEngine);
        fileLoader.loadIntoEngine();
        unitService = new UnitService(unitEngine, command);
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

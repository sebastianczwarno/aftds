package com.sc.aftds.excel;

import com.sc.aftds.cmd.Command;
import com.sc.aftds.unit.IUnitEngine;
import com.sc.aftds.unit.UnitEngine;
import com.sc.aftds.unit.UnitModel;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FileLoaderTest {

    @Test
    public void test_load() throws IOException, ParseException {
        var command = new Command(new String[]{"-f /home/sc/Documents/Baza_koty_30_12_2019.xlsx", "-p 3"});
        Assertions.assertTrue(command.getFile().exists());
        IUnitEngine<UnitModel> unitEngine = new UnitEngine(UnitModel.class);
        var fileLoader = new FileLoader(command, unitEngine);
        fileLoader.loadIntoEngine();
        Assertions.assertFalse(unitEngine.isEmpty());
    }
}

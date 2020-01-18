package com.sc.aftds.excel;

import com.sc.aftds.unit.IUnitEngine;
import com.sc.aftds.unit.UnitEngine;
import com.sc.aftds.unit.UnitModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class FileLoaderTest {

    @Test
    public void test_load() throws IOException {
        var file = new File("/home/sc/Documents/Baza_koty_30_12_2019.xlsx");
        Assertions.assertTrue(file.exists());
        IUnitEngine<UnitModel> unitEngine = new UnitEngine(UnitModel.class);
        var fileLoader = new FileLoader(new ExcelSheetPosition(3), unitEngine);
        fileLoader.loadIntoEngine(file);
        Assertions.assertFalse(unitEngine.isEmpty());
    }
}

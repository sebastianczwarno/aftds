package com.sc.aftds.excel;

import com.sc.aftds.process.UnitEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class FileLoaderTest {

    @Test
    public void testLoad() throws IOException {
        var file = new File("/home/sc/Documents/Baza_koty_30_12_2019.xlsx");
        Assertions.assertTrue(file.exists());
        var unitEngine = new UnitEngine();
        var fileLoader = new FileLoader(new ExcelSheetPosition(3), unitEngine);
        fileLoader.loadIntoEngine(file);
        Assertions.assertFalse(unitEngine.transactionalIndexedCollection.isEmpty());
    }
}

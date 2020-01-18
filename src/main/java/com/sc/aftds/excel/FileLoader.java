package com.sc.aftds.excel;

import com.sc.aftds.unit.IUnitEngine;
import com.sc.aftds.unit.UnitModel;

import com.sc.aftds.unit.UnitEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FileLoader {
    private static final Logger logger = LogManager.getLogger(FileLoader.class);

    private final Set<Integer> idSet = new HashSet<>();
    private final ExcelSheetPosition _excelSheetPosition;
    private final IUnitEngine<UnitModel> _unitEngine;

    public FileLoader(ExcelSheetPosition excelSheetPosition, IUnitEngine<UnitModel> unitEngine) {
        _excelSheetPosition = excelSheetPosition;
        _unitEngine = unitEngine;
    }

    public void loadIntoEngine(File source) throws IOException {
        try (var fileInputStream = new FileInputStream(source)) {
            var wb = new XSSFWorkbook(fileInputStream);
            var sheet = wb.getSheetAt(_excelSheetPosition.Number);
            var titleRow = sheet.getRow(0);
            sheet.removeRow(titleRow);
            sheet.forEach(row -> {
                var unit = UnitModel.createOptionalUnitFromExcelRow(row);
                var rowNum = row.getRowNum() + 1;
                unit.peek(u -> {
                    if (idSet.contains(u.id)) {
                        logger.error("Duplicated id detected " + u.id);
                    } else {
                        idSet.add(u.id);
                        _unitEngine.add(u);
                    }
                }).onEmpty(() -> logger.error("Incorrect data on row number " + rowNum));
            });
        }
        logger.debug("Loaded " + idSet.size() + " items.");
    }
}

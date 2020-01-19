package com.sc.aftds.excel;

import com.sc.aftds.unit.UnitModel;
import io.vavr.collection.List;
import io.vavr.control.Try;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static io.vavr.Predicates.*;
import static io.vavr.API.*;

public class ExcelFileWriter implements IExcelFileWriter {
    private static final Logger logger = LogManager.getLogger(ExcelFileWriter.class);
    private static String fileName = "database.xlsx";

    private final List<UnitModel> _unitModels;
    private final DateTimeFormatter _dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ExcelFileWriter(List<UnitModel> unitModels) {
        _unitModels = unitModels;
    }

    @Override
    public void write() {
        logger.info("Begin writing information into " + fileName);
        var workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("database");
        createHeaderRow(sheet);
        createRecordsInSheet(sheet);
        commit(workbook);
        logger.info("Completed writing information into " + fileName);
    }

    public void createHeaderRow(Sheet sheet) {
        var headerRow = sheet.createRow(0);
        for (var value : Column.values()) {
            var cell = headerRow.createCell(value.PositionWrite);
            cell.setCellValue(value.Alias);
        }
    }

    public void createRecordsInSheet(Sheet sheet) {
        for (int i = 0; i < _unitModels.size(); i++) {
            var unitModel = _unitModels.get(i);
            var rowPosition = i + 1;
            var row = sheet.createRow(rowPosition);
            writeUnitModelIntoRow(row, unitModel);
        }
    }

    public Row writeUnitModelIntoRow(Row row, UnitModel unitModel) {
        row.createCell(Column.Id.PositionWrite).setCellValue(unitModel.id);
        row.createCell(Column.BirthDate.PositionWrite).setCellValue(_dateTimeFormatter.format(unitModel.birthDate));
        row.createCell(Column.FatherId.PositionWrite).setCellValue(unitModel.fatherId);
        row.createCell(Column.MotherId.PositionWrite).setCellValue(unitModel.motherId);
        row.createCell(Column.Name.PositionWrite).setCellValue(unitModel.name);
        row.createCell(Column.Sex.PositionWrite).setCellValue(unitModel.sex.name());
        row.createCell(Column.Ems.PositionWrite).setCellValue(unitModel.ems);
        row.createCell(Column.PawPeds.PositionWrite).setCellValue(unitModel.pawPeds);
        row.createCell(Column.Pl.PositionWrite).setCellValue(unitModel.pl);
        row.createCell(Column.Ru.PositionWrite).setCellValue(unitModel.ru);
        return row;
    }

    public Try<Boolean> commit(Workbook workbook) {
        var result = Try.of(() -> {
            try (workbook) {
                try (var file = new FileOutputStream(fileName)) {
                    workbook.write(file);
                }
            }
            return true;
        }).recover(x -> Match(x).of(
                Case($(instanceOf(FileNotFoundException.class)), this::logMessageAndReturnFalse),
                Case($(instanceOf(IOException.class)), this::logMessageAndReturnFalse)
        ));

        return result;
    }

    private boolean logMessageAndReturnFalse(Exception e) {
        logger.error(e.getMessage());
        return false;
    }
}

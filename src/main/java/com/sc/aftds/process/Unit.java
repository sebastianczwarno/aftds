package com.sc.aftds.process;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.sc.aftds.excel.Column;
import com.sc.aftds.excel.RowCell;
import com.sc.aftds.excel.Sex;
import io.vavr.control.Option;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Unit {
    private static final Logger logger = LogManager.getLogger(Unit.class);

    public final int id;
    public final int fatherId;
    public final int motherId;
    public final LocalDate birthDate;
    public final String name;
    public final Sex sex;
    public final String ems;
    public final String pawPeds;
    public final boolean pl;
    public final boolean ru;

    public Unit(
            int id,
            int fatherId,
            int motherId,
            LocalDate birthDate,
            String name,
            Sex sex,
            String ems,
            String pawPeds,
            boolean pl,
            boolean ru) {
        this.id = id;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.birthDate = birthDate;
        this.name = name;
        this.sex = sex;
        this.ems = ems;
        this.pawPeds = pawPeds;
        this.pl = pl;
        this.ru = ru;
    }

    @Override
    public String toString() {
        return "Unit{id=" + id + ", fatherId=" + fatherId + ", motherId=" + motherId + ", birthDate=" + birthDate + "}";
    }

    public static Unit copy(Unit unit, LocalDate birthDate) {
        return new Unit(
                unit.id,
                unit.fatherId,
                unit.motherId,
                birthDate,
                unit.name,
                unit.sex,
                unit.ems,
                unit.pawPeds,
                unit.pl,
                unit.ru
        );
    }

    public static Option<Unit> createOptionalUnitFromExcelRow(Row row) {
        var optionalId = RowCell.get(Column.Id, row, Cell::getNumericCellValue).map(Double::intValue);

        if (optionalId.isEmpty()) {
            var rowNumber = row.getRowNum() + 1;
            logger.error("Missing id on excel row=" + rowNumber);
            return Option.none();
        }

        var optionalFatherId = RowCell.get(Column.FatherId, row, Unit::getCellNumericValue).map(Double::intValue);
        var optionalMotherId = RowCell.get(Column.MotherId, row, Unit::getCellNumericValue).map(Double::intValue);
        var optionalSex = RowCell.get(Column.Sex, row, (cell) ->
                cell.getNumericCellValue() == Sex.Male.Val ? Sex.Male : Sex.Female);
        var optionalName = RowCell.get(Column.Name, row, Cell::getStringCellValue);
        var optionalBirthDate = RowCell.get(Column.BirthDate, row, (cell) -> {
            var result = LocalDate.EPOCH;
            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            switch (cell.getCellType()) {
                case NUMERIC:
                    result = cell.getLocalDateTimeCellValue().toLocalDate();
                    break;
                default:
                    result = StringUtils.isBlank(cell.getStringCellValue()) ?
                            result : LocalDate.parse(cell.getStringCellValue(), formatter);
            }
            return result;
        });
        var optionalEms = RowCell.get(Column.Ems, row, Cell::getStringCellValue);
        var optionalPawPeds = RowCell.get(Column.PawPeds, row, (cell) -> {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return Double.toString(cell.getNumericCellValue());
                default:
                    return cell.getStringCellValue();
            }
        });
        var optionalPl = RowCell.get(Column.Pl, row, (cell) -> cell.getNumericCellValue() == 1);
        var optionalRu = RowCell.get(Column.Ru, row, (cell) -> cell.getNumericCellValue() == 1);



        return Option.of(new Unit(
                optionalId.getOrElse(0),
                optionalFatherId.getOrElse(0),
                optionalMotherId.getOrElse(0),
                optionalBirthDate.getOrElse(LocalDate.EPOCH),
                optionalName.getOrElse(StringUtils.EMPTY),
                optionalSex.getOrElse(Sex.Female),
                optionalEms.getOrElse(StringUtils.EMPTY),
                optionalPawPeds.getOrElse(StringUtils.EMPTY),
                optionalPl.getOrElse(false),
                optionalRu.getOrElse(false)));
    }

    public static final Attribute<Unit, Integer> UNIT_ID = new SimpleAttribute<Unit, Integer>("id") {
        public Integer getValue(Unit unit, QueryOptions queryOptions) {
            return unit.id;
        }
    };

    public static final Attribute<Unit, Integer> UNIT_FATHER_ID = new SimpleAttribute<Unit, Integer>("fatherId") {
        public Integer getValue(Unit unit, QueryOptions queryOptions) {
            return unit.fatherId;
        }
    };

    public static final Attribute<Unit, Integer> UNIT_MOTHER_ID = new SimpleAttribute<Unit, Integer>("motherId") {
        public Integer getValue(Unit unit, QueryOptions queryOptions) {
            return unit.motherId;
        }
    };

    public static final Attribute<Unit, LocalDate> UNIT_BIRTH_DATE = new SimpleAttribute<Unit, LocalDate>("birthDate") {
        public LocalDate getValue(Unit unit, QueryOptions queryOptions) {
            return unit.birthDate;
        }
    };

    private static Double getCellNumericValue(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            default:
                return Double.NaN;
        }
    }
}

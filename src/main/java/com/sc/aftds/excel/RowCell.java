package com.sc.aftds.excel;

import io.vavr.control.Option;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.function.Function;

public final class RowCell {
    public static <T> Option<T> get(Column column, Row row, Function<Cell, T> function) {
        var cell = row.getCell(column.Val);
        var optional = Option.of(cell);
        return optional.map(function);
    }
}

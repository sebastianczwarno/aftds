package com.sc.aftds.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Optional;
import java.util.function.Function;

public final class RowCell {
    public static <T> Optional<T> get(Column column, Row row, Function<Cell, T> function) {
        var cell = row.getCell(column.Val);
        var optional = Optional.ofNullable(cell);
        return optional.map(function);
    }
}

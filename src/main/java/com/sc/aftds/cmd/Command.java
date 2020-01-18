package com.sc.aftds.cmd;

import com.sc.aftds.excel.ExcelSheetPosition;
import io.vavr.control.Try;
import org.apache.commons.cli.*;

import java.io.File;

public final class Command {
    private static final Options _options = new Options();
    private static final CommandLineParser _commCommandLineParser = new DefaultParser();

    private static final Option FileOption = new Option("f", "file", true, "Path to file");
    private static final Option ExcelSheetPositionOption = new Option("p", "sheetPos", true, "Excel sheet position");
    private static Option TimeDistanceOption = new Option("t", "timeDistance", true, "Time distance between parent animal and child in months.");

    private final CommandLine _commCommandLine;

    public Command(String[] args) throws ParseException {
        FileOption.setRequired(true);
        FileOption.setType(File.class);
        ExcelSheetPositionOption.setRequired(false);
        ExcelSheetPositionOption.setType(int.class);
        TimeDistanceOption.setRequired(false);
        TimeDistanceOption.setType(int.class);

        _options.addOption(FileOption);
        _options.addOption(ExcelSheetPositionOption);
        _options.addOption(TimeDistanceOption);

        _commCommandLine = _commCommandLineParser.parse(_options, args);
    }

    public ExcelSheetPosition getExcelSheetPosition() {
        return Try.of(() ->
                new ExcelSheetPosition(Integer.parseInt(_commCommandLine.getOptionValue(ExcelSheetPositionOption.getOpt()).trim()))
        ).getOrElse(new ExcelSheetPosition(0));
    }

    public int getTimeDistance() {
        return Try.of(() -> Integer.parseInt(_commCommandLine.getOptionValue(TimeDistanceOption.getOpt()).trim())).getOrElse(36);
    }

    public File getFile() {
        return new File(_commCommandLine.getOptionValue(FileOption.getOpt()).trim());
    }
}

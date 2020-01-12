package com.sc.aftds.cmd;

import org.apache.commons.cli.*;

public final class Command {
    private final Options _options = new Options();
    private final CommandLineParser _commCommandLineParser = new DefaultParser();
    private final HelpFormatter _helpHelpFormatter = new HelpFormatter();

    public final Option FileOption = new Option("f", "file", true, "Path to file");
    public final Option ExcelSheetPosition = new Option("p", "sheetPos", true, "Excel sheet position");
    public final Option TimeDistance = new Option("t", "timeDistance", true, "Time distance between parent animal and child.");

    public Command() {
        FileOption.setRequired(true);
        ExcelSheetPosition.setRequired(false);
        TimeDistance.setRequired(true);
        _options.addOption(FileOption);
        _options.addOption(ExcelSheetPosition);
        _options.addOption(TimeDistance);
    }

    public CommandLine parse(String[] args) throws ParseException {
        return _commCommandLineParser.parse(_options, args);
    }
}

package com.sc.aftds.cmd;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandTest {

    @Test
    public void test_parse() throws ParseException {
        var cmd = new Command();
        var input = new String[]{"-f /home/sc/Documents/Baza_koty_30_12_2019.xlsx", "-p 5", "-t 4"};
        var result = cmd.parse(input);
        Assertions.assertEquals("/home/sc/Documents/Baza_koty_30_12_2019.xlsx", result.getOptionValue(cmd.FileOption.getOpt()).trim());
        Assertions.assertEquals(5, Integer.parseInt(result.getOptionValue(cmd.ExcelSheetPosition.getOpt()).trim()));
        Assertions.assertEquals(4, Integer.parseInt(result.getOptionValue(cmd.TimeDistance.getOpt()).trim()));
    }
}

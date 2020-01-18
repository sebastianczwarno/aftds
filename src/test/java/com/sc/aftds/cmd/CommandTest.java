package com.sc.aftds.cmd;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandTest {

    @Test
    public void test_parse() throws ParseException {
        var input = new String[]{"-f /home/sc/Documents/Baza_koty_30_12_2019.xlsx", "-p 3", "-t 4"};
        var cmd = new Command(input);
        Assertions.assertTrue(cmd.getFile().exists());
        Assertions.assertEquals(3, cmd.getExcelSheetPosition().Number);
        Assertions.assertEquals(4, cmd.getTimeDistance());
    }

    @Test
    public void test_parse_fail() throws ParseException {
        var input = new String[]{"-f srdr345efsg,l,", "-p dupa", "-t &&&"};
        var cmd = new Command(input);
        Assertions.assertFalse(cmd.getFile().exists());
        Assertions.assertEquals(0, cmd.getExcelSheetPosition().Number);
        Assertions.assertEquals(36, cmd.getTimeDistance());
    }
}

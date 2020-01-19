package com.sc.aftds;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class AftdsApplicationTest {

    @Test
    void main() throws IOException, ParseException {
        var args = new String[]{"-f /home/sc/Documents/Baza_koty_30_12_2019.xlsx", "-p 3"};
        AftdsApplication.main(args);
    }
}
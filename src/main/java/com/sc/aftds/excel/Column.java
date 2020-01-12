package com.sc.aftds.excel;

public enum Column {
    Id(0),
    Name(1),
    Sex(2),
    BirthDate(3),
    Ems(4),
    FatherId(5),
    MotherId(7),
    PawPeds(9),
    Pl(10),
    Ru(11);

    public final int Val;

    Column(int val) {
        Val = val;
    }
}

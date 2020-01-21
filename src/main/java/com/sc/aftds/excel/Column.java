package com.sc.aftds.excel;

public enum Column {
    Id(0,0, "ID"),
    Name(1,1, "Imię"),
    BirthDate(3,2, "Data ur"),
    FatherId(5, 3,"Id_Ojciec"),
    FatherBirthDate(-1, 4,"Data Urodzenia Ojca"),
    MotherId(7,5, "Id_Matka"),
    MotherBirthDate(-1,6, "Data Urodzenia Matki"),
    Sex(2,7,"Płeć"),
    Ems(4,8, "EMS"),
    PawPeds(9,9, "PawPeds"),
    Pl(10,10, "PL"),
    Ru(11,11, "RU"),
    ModifiedByProgram(-1, 12, "Zmodyfikowane Przez Program");

    public final int PositionRead;
    public final String Alias;
    public final int PositionWrite;

    Column(int positionRead, int positionWrite, String alias) {
        PositionRead = positionRead;
        PositionWrite = positionWrite;
        Alias = alias;
    }
}

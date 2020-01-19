package com.sc.aftds.excel;

public enum Column {
    Id(0,0, "ID"),
    Name(1,4, "Imię"),
    Sex(2,5,"Płeć"),
    BirthDate(3,1, "Data ur"),
    Ems(4,6, "EMS"),
    FatherId(5, 2,"Id_Ojciec"),
    MotherId(7,3, "Id_Matka"),
    PawPeds(9,7, "PawPeds"),
    Pl(10,8, "PL"),
    Ru(11,9, "RU");

    public final int PositionRead;
    public final String Alias;
    public final int PositionWrite;

    Column(int positionRead, int positionWrite, String alias) {
        PositionRead = positionRead;
        PositionWrite = positionWrite;
        Alias = alias;
    }
}

package com.sc.aftds.excel;

public enum Sex {
    Male(1.0),
    Female(2.0),
    Undefined(3.0);

    public final double Val;

    Sex(double val) {
        Val = val;
    }
}

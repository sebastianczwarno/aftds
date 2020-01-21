package com.sc.aftds.unit;

import io.vavr.control.Option;

public interface IUnitService {
    void process();
    Option<UnitModel> findUnitById(int parentId);
}

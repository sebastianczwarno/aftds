package com.sc.aftds.unit;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;

public interface IUnitEngine<O extends UnitModel> {
    ResultSet<O> retrieve(Query<O> query);
    ResultSet<O> retrieve(Query<O> query, QueryOptions queryOptions);
    boolean add(O o);
    boolean remove(O o);
    boolean isEmpty();
}

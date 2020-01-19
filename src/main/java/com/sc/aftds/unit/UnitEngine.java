package com.sc.aftds.unit;

import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import io.vavr.collection.List;

public class UnitEngine implements IUnitEngine<UnitModel> {
    private final TransactionalIndexedCollection<UnitModel> transactionalIndexedCollection;

    public UnitEngine() {
        transactionalIndexedCollection = new TransactionalIndexedCollection<>(UnitModel.class);
        transactionalIndexedCollection.addIndex(NavigableIndex.onAttribute(UnitModel.UNIT_FATHER_ID));
        transactionalIndexedCollection.addIndex(NavigableIndex.onAttribute(UnitModel.UNIT_MOTHER_ID));
    }

    @Override
    public ResultSet<UnitModel> retrieve(Query<UnitModel> query) {
        return transactionalIndexedCollection.retrieve(query);
    }

    @Override
    public ResultSet<UnitModel> retrieve(Query<UnitModel> query, QueryOptions queryOptions) {
        return transactionalIndexedCollection.retrieve(query, queryOptions);
    }

    @Override
    public boolean add(UnitModel o) {
        return transactionalIndexedCollection.add(o);
    }

    @Override
    public boolean remove(UnitModel o) {
        return transactionalIndexedCollection.remove(o);
    }

    @Override
    public boolean isEmpty() {
        return transactionalIndexedCollection.isEmpty();
    }

    @Override
    public List<UnitModel> getAllUnitModels() {
        return List.ofAll(transactionalIndexedCollection.stream());
    }
}

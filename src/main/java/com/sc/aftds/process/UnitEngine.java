package com.sc.aftds.process;

import com.googlecode.cqengine.TransactionalIndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;

public class UnitEngine {
    public final TransactionalIndexedCollection transactionalIndexedCollection;

    public UnitEngine() {
        transactionalIndexedCollection = new TransactionalIndexedCollection<>(Unit.class);
        transactionalIndexedCollection.addIndex(NavigableIndex.onAttribute(Unit.UNIT_FATHER_ID));
        transactionalIndexedCollection.addIndex(NavigableIndex.onAttribute(Unit.UNIT_MOTHER_ID));
    }
}

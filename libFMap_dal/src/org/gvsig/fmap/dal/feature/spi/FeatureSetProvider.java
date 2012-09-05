package org.gvsig.fmap.dal.feature.spi;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;

public interface FeatureSetProvider {

	boolean canFilter();

	boolean canOrder();

	boolean canIterateFromIndex();

	long getSize() throws DataException;

	boolean isEmpty() throws DataException;

	DisposableIterator iterator() throws DataException;

	DisposableIterator iterator(long index) throws DataException;

	DisposableIterator fastIterator(long index) throws DataException;

	DisposableIterator fastIterator() throws DataException;

	void dispose();

}

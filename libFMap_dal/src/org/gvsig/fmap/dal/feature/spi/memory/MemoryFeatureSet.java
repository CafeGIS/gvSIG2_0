package org.gvsig.fmap.dal.feature.spi.memory;

import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.DefaultFeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;

public class MemoryFeatureSet implements FeatureSetProvider {

	AbstractMemoryStoreProvider store;
	FeatureType type;
	protected List data;

	public MemoryFeatureSet(AbstractMemoryStoreProvider store,
			FeatureQuery query, FeatureType featureType, List data) {
		this.store = store;
		this.data = data;
		/*
		 * Nota: Como no sabe filtrar ni ordenar ignora el query.
		 */

		/*
		 * Comproabar si los attributos solicitados son distintos
		 * a los originales.
		 */

		this.type = null;
		if (!featureType.equals(store.getStoreServices()
				.getProviderFeatureType(featureType.getId()))) {
			this.type = featureType;
		}
	}

	public boolean canFilter() {
		return false;
	}

	public boolean canOrder() {
		return false;
	}

	public boolean canIterateFromIndex() {
		return true;
	}

	public DisposableIterator fastIterator(long index) throws DataException {
		return iterator(0);
	}

	public DisposableIterator fastIterator() throws DataException {
		return iterator();
	}

	public long getSize() throws DataException {
		return data.size();
	}

	public boolean isEmpty() throws DataException {
		return data.size() < 1;
	}

	public DisposableIterator iterator() throws DataException {
		if (type == null) {
			return new DelegatedDisposableIterator(this.data.iterator());
		}
		return new DelegatedDisposableIterator(this.data.iterator(), type);
	}

	public DisposableIterator iterator(long index) throws DataException {
		if (type == null) {
			return new DelegatedDisposableIterator(this.data
					.listIterator((int) index));
		}
		return new DelegatedDisposableIterator(this.data
				.listIterator((int) index), type);
	}

	public void dispose() {
		store = null;
	}




	protected class DelegatedDisposableIterator implements DisposableIterator {
		private Iterator delegated;
		private FeatureType fType;

		public DelegatedDisposableIterator(Iterator it) {
			this(it, null);
		}

		public DelegatedDisposableIterator(Iterator it, FeatureType featureType) {
			this.delegated = it;
			this.fType = featureType;

		}

		public void dispose() {
			this.delegated = null;
		}

		public boolean hasNext() {
			return delegated.hasNext();
		}

		public Object next() {
			if (fType == null) {
				return delegated.next();
			} else {
				return new MemoryFeatureProviderAttributeMapper(
						(DefaultFeatureProvider) delegated.next(),
						this.fType);
			}

		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}

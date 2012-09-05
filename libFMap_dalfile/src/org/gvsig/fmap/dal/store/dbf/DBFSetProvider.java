/**
 *
 */
package org.gvsig.fmap.dal.store.dbf;

import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadRuntimeException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;

/**
 * @author jjdelcerro
 *
 */
public class DBFSetProvider implements FeatureSetProvider {

	DBFStoreProvider store;
	FeatureQuery query;
	FeatureType featureType;

	public DBFSetProvider(DBFStoreProvider store, FeatureQuery query,
			FeatureType featureType)
			throws DataException {
		this.store = store;
		this.query = query;
		this.featureType = featureType;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#canFilter()
	 */
	public boolean canFilter() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#canIterateFromIndex()
	 */
	public boolean canIterateFromIndex() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#canOrder()
	 */
	public boolean canOrder() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#fastIterator(long)
	 */
	public DisposableIterator fastIterator(long index) throws DataException {
		return new FastDBFIterartor(this.store, this.featureType,
				index);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#fastIterator()
	 */
	public DisposableIterator fastIterator() throws DataException {
		return this.fastIterator(0);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#getSize()
	 */
	public long getSize() throws DataException {
		return this.store.getFeatureCount();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#isEmpty()
	 */
	public boolean isEmpty() throws DataException {
		return this.store.getFeatureCount() == 0;
	}

	public void dispose() {
		this.store = null;
		this.featureType = null;
		this.query = null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#iterator()
	 */
	public DisposableIterator iterator() throws DataException {
		return this.iterator(0);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#iterator(long)
	 */
	public DisposableIterator iterator(long index) throws DataException {
		return new DBFIterartor(this.store, this.featureType, index);
	}

	protected class DBFIterartor implements DisposableIterator {
		protected long index;
		protected DBFStoreProvider store;
		protected FeatureType type;
		protected long count;

		public DBFIterartor(DBFStoreProvider store, FeatureType type,
				long startOn) throws DataException {
			this.store = store;
			this.index = startOn;
			this.type = type;
			this.count = this.store.getFeatureCount();
		}

		public boolean hasNext() {
			return this.count > index;
		}

		public Object next() {
			if (index >= this.count) {
				throw new NoSuchElementException();
			}
			try {

				FeatureProvider ret = this.store.getFeatureProviderByIndex(index,
						this.type);
				index++;
				return ret;
			} catch (DataException e) {
				throw new ReadRuntimeException(this.store.getName(), e);
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void dispose() {
			store = null;
			type = null;
		}
	}

	protected class FastDBFIterartor extends DBFIterartor {
		protected FeatureProvider data;

		public FastDBFIterartor(DBFStoreProvider store, FeatureType type,
				long startOn) throws DataException {
			super(store, type, startOn);
			this.data = this.store.createFeatureProvider(type);
		}

		public Object next() {
			if (index >= this.count) {
				throw new NoSuchElementException();
			}

			try {
				this.store.initFeatureProviderByIndex(this.data, index, type);
			} catch (DataException e) {
				throw new ReadRuntimeException("next", e);
			}
			index++;
			return this.data;
		}

		public void dispose() {
			super.dispose();
			data = null;
		}

	}
}

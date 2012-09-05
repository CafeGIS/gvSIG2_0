/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2008 {{Company}}   {{Task}}
*/


package org.gvsig.fmap.dal.feature.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadRuntimeException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProviderServices;
import org.gvsig.fmap.dal.feature.spi.LongList;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProviderServices;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.visitor.Visitor;


public class IndexFeatureSet implements FeatureSet, FeatureSetProvider {

	LongList featureReferences = null;
	FeatureStoreProvider storeProvider = null;
	FeatureStoreProviderServices store = null;
	FeatureIndexProviderServices index = null;
	List featureTypes = null;

	public class IndexIterator implements DisposableIterator {
		Iterator it = null;

		public IndexIterator(Iterator it) {
			this.it = it;
		}

		public boolean hasNext() {
			return it.hasNext();
		}

		public Object next() {
			Object oid = it.next();
			FeatureReference ref = new DefaultFeatureReference(store
					.getFeatureStore(), oid);
			try {
				return store.getFeatureStore().getFeatureByReference(ref);
			} catch (DataException e) {
				throw new ReadRuntimeException(store.getName(), e);
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void dispose() {
			this.it = null;
		}
	}

	public class FastIndexIterator implements DisposableIterator {
		Iterator it = null;
		DefaultFeature feature = null;

		public FastIndexIterator(Iterator it) throws DataException {
			this.it = it;
			feature = (DefaultFeature) store.createFeature(storeProvider
					.createFeatureProvider(index.getFeatureType()));
		}

		public boolean hasNext() {
			return it.hasNext();
		}

		public Object next() {
			Object oid = it.next();
			try {
				//				Long longer=new Long(((Integer)oid).longValue());
				FeatureReference ref = new DefaultFeatureReference(store
						.getFeatureStore(), oid);
				FeatureProvider data = storeProvider
						.getFeatureProviderByReference((FeatureReferenceProviderServices) ref);

				feature.setData(data);

				return feature;
			} catch (DataException e) {
				throw new ReadRuntimeException(store.getName(), e);
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public void dispose() {
			this.it = null;
			this.feature = null;

		}
	}

	public IndexFeatureSet(FeatureIndexProviderServices index, LongList featureReferences) {
		this.featureReferences = featureReferences;
		this.store = index.getFeatureStoreProviderServices();
		this.storeProvider = store.getProvider();
		this.index = index;
	}

	public boolean canFilter() {
		return false;
	}

	public boolean canIterateFromIndex() {
		return true;
	}

	public boolean canOrder() {
		return false;
	}

	public DisposableIterator fastIterator(long index) throws DataException {
		if (store.getFeatureStore().isEditing()) {
			return this.iterator(index);
		}
		return new FastIndexIterator(this.featureReferences.iterator(index));
	}

	public DisposableIterator fastIterator() throws DataException {
		if (store.getFeatureStore().isEditing()) {
			return this.iterator();
		}
		return new FastIndexIterator(this.featureReferences.iterator());
	}

	public long getSize() throws DataException {
		return featureReferences.getSize();
	}

	public boolean isEmpty() throws DataException {
		return featureReferences.isEmpty();
	}

	public DisposableIterator iterator() throws DataException {
		return new IndexIterator(this.featureReferences.iterator());
	}

	public DisposableIterator iterator(long index) throws DataException {
		return new IndexIterator(this.featureReferences.iterator(index));
	}

	public void delete(Feature feature) throws DataException {
		index.delete(feature);
		store.getFeatureStore().delete(feature);
	}

	public FeatureType getDefaultFeatureType() {
		return index.getFeatureType();
	}

	public List getFeatureTypes() {
		List types = new ArrayList();
		types.add(index.getFeatureType());
		return Collections.unmodifiableList(types);
	}

	public void insert(EditableFeature feature) throws DataException {
		index.insert(feature);
		store.getFeatureStore().insert(feature);
	}

	public void update(EditableFeature feature) throws DataException {
		store.getFeatureStore().update(feature);
		// we need to re-index the feature, since its shape might have changed
		index.delete(feature);
		index.insert(feature);
	}

	public void dispose() {

	}

	public boolean isFromStore(DataStore store) {
		return this.store.equals(store);
	}

	public void accept(Visitor visitor) throws BaseException {
		DisposableIterator iterator = iterator();

		while (iterator.hasNext()) {
			Feature feature = (Feature) iterator.next();
			visitor.visit(feature);
		}
		iterator.dispose();
	}

}


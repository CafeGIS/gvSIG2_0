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
 * 2009 {Iver T.I.}   {Task}
 */

package org.gvsig.fmap.dal.store.gpe;

import java.util.HashMap;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.fmap.dal.store.gpe.handlers.FmapContentHandler;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class GPESetProvider implements FeatureSetProvider {
	private GPEStoreProvider store;
	private FeatureQuery query;
	private FeatureType featureType;

	public GPESetProvider(GPEStoreProvider store,
			FeatureQuery query) {
		this.store = store;
		this.query = query;		
	}

	public GPESetProvider(GPEStoreProvider store,
			FeatureQuery query, FeatureType featureType) {
		this.store = store;
		this.query = query;
		this.featureType = featureType;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#canFilter()
	 */
	public boolean canFilter() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#canIterateFromIndex()
	 */
	public boolean canIterateFromIndex() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#canOrder()
	 */
	public boolean canOrder() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#fastIterator(long)
	 */
	public DisposableIterator fastIterator(long index) throws DataException {
		return new GPEFastIterator(this.store, this.featureType);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#fastIterator()
	 */
	public DisposableIterator fastIterator() throws DataException {
		return fastIterator(0);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#getSize()
	 */
	public long getSize() throws DataException {
		return store.getFeatureCount();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#isEmpty()
	 */
	public boolean isEmpty() throws DataException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#iterator()
	 */
	public DisposableIterator iterator() throws DataException {
		return this.iterator(0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#iterator(long)
	 */
	public DisposableIterator iterator(long index) throws DataException {
		return new GPEIterator(this.store, this.featureType);
	}

	protected class GPEIterator implements DisposableIterator {
		protected GPEStoreProvider store;
		protected FeatureType type;
		Iterator keys = null;
		HashMap features = null;

		public GPEIterator(GPEStoreProvider store, FeatureType type) throws DataException {
			this.store = store;
			this.type = type;			
			features = ((FmapContentHandler)store.getParser().getContentHandler())
					.getFeatureSet();
			keys = features.keySet().iterator();
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			return keys.hasNext();
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		public Object next() {
			return ((DefaultFeature)features.get(keys.next())).getData();			
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/* (non-Javadoc)
		 * @see org.gvsig.fmap.dal.feature.DisposableIterator#dispose()
		 */
		public void dispose() {
			
		}
	}
	
	protected class GPEFastIterator extends GPEIterator {

		public GPEFastIterator(GPEStoreProvider store, FeatureType type) throws DataException {
			super(store, type);
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}


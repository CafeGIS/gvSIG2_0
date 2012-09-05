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
* 2009 IVER T.I   {{Task}}
*/

package org.gvsig.fmap.dal.store.jdbc;

import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;

/**
 * @author jmvivo
 *
 */
public class JDBCIterator implements DisposableIterator {

	protected int resultSetID;
	protected Boolean hasNext = null;
	protected JDBCStoreProvider store;
	protected FeatureType featureType;
	protected JDBCSetProvider set;

	protected JDBCIterator(JDBCStoreProvider store, JDBCSetProvider set,
			FeatureType featureType,
			int resulsetID) throws DataException {
		this.resultSetID = resulsetID;
		this.featureType = featureType;
		this.store = store;
		this.set = set;
		this.hasNext = null;
		this.set.addResulsetReference(resulsetID);
	}

	public boolean hasNext() {
		if (hasNext == null) {
			try {
				if (store.resulsetNext(resultSetID)) {
					hasNext = Boolean.TRUE;
				} else {
					hasNext = Boolean.FALSE;
					this.close();
				}
			} catch (DataException e) {
				// FIXME Exception
				throw new RuntimeException(e);
			}
		}
		return hasNext.booleanValue();
	}

	public Object next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		FeatureProvider data;
		try {
			data = getFeatureProvider();
		} catch (DataException e) {
			// FIXME Exception
			throw new RuntimeException(e);
		}
		hasNext = null;
		return data;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	protected FeatureProvider createFeatureProvider() throws DataException {
		return store.createFeatureProvider(featureType);
	}

	protected FeatureProvider getFeatureProvider() throws DataException {
		FeatureProvider data = createFeatureProvider();
		store.loadFeatureProvider(data, resultSetID);
		return data;
	}

	public void dispose() {
		if (resultSetID >= 0){
			this.close();
		}
		this.store = null;
		this.featureType = null;

	}

	protected void close() {
		try {
			this.set.removeResulsetReference(resultSetID);
			this.store.closeResulset(resultSetID);
			resultSetID = -1;
		} catch (DataException e) {
			throw new RuntimeException(e);
		}
	}

}

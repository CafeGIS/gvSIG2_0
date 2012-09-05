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

/**
 *
 */
package org.gvsig.fmap.dal.store.jdbc;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;

/**
 * @author jmvivo
 *
 */
public class JDBCFastIterator extends JDBCIterator {

	protected FeatureProvider featureProvider;

	protected JDBCFastIterator(JDBCStoreProvider store, JDBCSetProvider set,
			FeatureType featureType, int resultsetID) throws DataException {
		super(store, set, featureType, resultsetID);
		this.featureProvider = super.createFeatureProvider();
	}

	protected FeatureProvider createFeatureProvider() throws DataException {
		return this.featureProvider;
	}

	public void dispose() {
		super.dispose();
		featureProvider = null;
	}

}

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
package org.gvsig.fmap.dal.store.postgresql;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.store.jdbc.JDBCSetProvider;
import org.gvsig.fmap.dal.store.jdbc.JDBCStoreProvider;

/**
 * @author jmvivo
 *
 */
public class PostgreSQLSetProvider extends JDBCSetProvider {

	public PostgreSQLSetProvider(JDBCStoreProvider store, FeatureQuery query,
			FeatureType featureType) throws DataException {
		super(store, query, featureType);
		// TODO Auto-generated constructor stub
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#canFilter()
	 */
	public boolean canFilter() {
		// TODO more checks
		if (!super.canFilter()) {
			return false;
		}
		return true;

	}

}

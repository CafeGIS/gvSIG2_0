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
* 2008 IVER T.I. S.A.   {{Task}}
*/

package org.gvsig.fmap.data.feature.db.jdbc.h2;

import java.sql.Connection;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.expressionevaluator.Filter;
import org.gvsig.fmap.dal.feature.impl.FeatureManager;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.jdbc.AbstractJDBCIterator;

public abstract class AbstractH2DBIterator extends AbstractJDBCIterator {


	public AbstractH2DBIterator(H2Store store, DBFeatureType featureType,
            String sql, int fetchSize, FeatureManager featureManager,
            Filter featureFilter) throws ReadException {
        super(store, featureType, sql, fetchSize, featureManager, featureFilter);
    }

    public AbstractH2DBIterator(H2Store store, DBFeatureType featureType,
            String sql, int fetchSize, FeatureManager featureManager,
            Filter featureFilter, int initialPosition) throws ReadException {
        super(store, featureType, sql, fetchSize, featureManager,
                featureFilter, initialPosition);
    }

	protected String getFinalResulsetSQL() {
		return H2Utils.addLimitsToSQL(this.sql, this.fetchSize,
				this.page);
	}

	protected Connection getConnection() throws ReadException {
		((H2Store) this.store).open();
		return ((H2Store) this.store).getConnection();
	}

	protected Feature createFeatureFromTheResulset() throws ReadException {
		return ((H2Store) this.store).createFeatureFromResulset(
				this.rs, this.featureType);
	}


}

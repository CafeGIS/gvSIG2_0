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

package org.gvsig.fmap.data.feature.db.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.expressionevaluator.Filter;
import org.gvsig.fmap.dal.feature.impl.FeatureManager;
import org.gvsig.fmap.data.feature.db.DBFeatureType;

public abstract class AbstractJDBCIterator implements Iterator {

	protected boolean nextChecked = false;
	protected Feature feature;
	protected ResultSet rs;
	protected JDBCStore store;
	protected DBFeatureType featureType;
	protected boolean rsEOF = false;
	protected boolean memoryEOF = false;
	protected int memoryPosition = 0;
	protected int fetchSize;
	protected String sql;
	protected int page;
	protected int index;
	protected FeatureManager featureManager = null;
	protected Filter featureFilter = null;
	
	private int initialPosition = 1;

    public AbstractJDBCIterator(JDBCStore store, DBFeatureType featureType,
            String sql, int fetchSize, FeatureManager featureManager,
            Filter featureFilter, int initialPosition) throws ReadException {
        this.store = store;
        this.featureType = featureType;
        this.fetchSize = fetchSize;
        this.sql = sql;
        this.page = 0;
        this.index = 0;
        this.featureManager = featureManager;
        this.featureFilter = featureFilter;
        this.initialPosition = initialPosition;
        this.createResulset();
    }

	public AbstractJDBCIterator(JDBCStore store, DBFeatureType featureType,
            String sql, int fetchSize, FeatureManager featureManager,
            Filter featureFilter) throws ReadException {
	    this(store, featureType, sql, fetchSize, featureManager, featureFilter,
                1);
    }

	protected Feature nextDBFeature() {
		Feature feature = null;
		while (true) {
			try {
				if (rs.next()) {
					feature = this.createFeatureFromTheResulset();
					this.index++;
					return feature;
				} else {
					if (this.index == this.fetchSize) {
						this.index = 0;
						this.page++;
						this.createResulset();
						continue;
					} else {
						return null;
					}

				}
			} catch (java.sql.SQLException e) {
				throw new RuntimeException(new ReadException(this.store
						.getName(), e));
			} catch (ReadException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected Feature nextMemoryFeature() {
		Feature feature = null;
		while (true) {
			if (this.memoryPosition < this.featureManager.getNum()) {
				try {
					feature = featureManager.get(this.memoryPosition,
							store, this.featureType);
					this.memoryPosition++;
					if (this.featureFilter == null) {
						break;
					}
					if (!this.featureFilter.evaluate(feature)) {
						continue;
					}
				} catch (ReadException e) {
					throw new RuntimeException(new ReadException(this.store
							.getName(), e));
				}
			} else {
				this.memoryEOF = true;
				break;
			}
		}
		return feature;
	}

	protected abstract Feature createFeatureFromTheResulset()
			throws ReadException;

	protected abstract void checkModified();

	protected abstract Connection getConnection() throws ReadException;

	protected abstract String getFinalResulsetSQL();

	protected void createResulset() throws ReadException {
		String mSql = this.getFinalResulsetSQL();
		Connection conn = this.getConnection();
		try {
			if (this.rs != null) {
				this.rs.close();
			}

			Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			this.rs = st.executeQuery(mSql);
			
			if (initialPosition > 1) {
                rs.absolute(initialPosition);
            }

		} catch (java.sql.SQLException e) {
			throw new SQLException(mSql, this.store.getName(), e);
		}

	}


	public boolean hasNext() {
		checkModified();

		if (nextChecked) {
			return this.feature != null;
		}
		Feature feature = null;
		nextChecked = true;
		if (this.rsEOF && this.memoryEOF) {
			return false;
		}
		while (true) {
			try {
				if (!this.rsEOF) {
					feature = nextDBFeature();
					if (feature == null) {
						this.close();
						continue;
					}
					if (this.featureManager != null
							&& this.featureManager.isDeleted(feature)) {
						continue;
					}
					this.feature = feature;
					return true;
				} else {
					if (this.featureManager == null) {
						this.memoryEOF = true;
						return false;
					}
					this.feature = nextMemoryFeature();
					return this.feature != null;
				}
			} catch (java.sql.SQLException e) {
				throw new RuntimeException(new ReadException(this.store
						.getName(), e));
			}
		}
	}

	public Object next() {
		checkModified();
		if (!nextChecked) {
			hasNext();
		}
		if (this.feature == null) {
			throw new NoSuchElementException();
		}
		nextChecked = false;
		Feature feature = this.feature;
		this.feature = null;
		return feature;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	protected void close() throws java.sql.SQLException {
		if (rs == null) {
			return;
		}
		rs.close();
		this.rsEOF = true;
	}
}

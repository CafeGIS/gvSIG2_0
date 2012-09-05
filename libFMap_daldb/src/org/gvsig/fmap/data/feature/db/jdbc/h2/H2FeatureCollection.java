package org.gvsig.fmap.data.feature.db.jdbc.h2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.data.feature.db.DBDataFeatureCollection;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.jdbc.AbstractJDBCIterator;
import org.gvsig.fmap.data.feature.db.jdbc.SQLException;
import org.gvsig.tools.exception.BaseException;

public class H2FeatureCollection extends DBDataFeatureCollection {
	protected DBFeatureType featureType;
	protected String filter;
	protected String totalFilter;
	protected H2Store store;
	private String order;
	private int numReg=-1;
	private String sql;
	private String sqlCount;
	private String totalOrder;
	private int fetchSize = 5000;

	H2FeatureCollection(H2Store store,DBFeatureType type, String filter, String order) {
		this.store=store;
		this.featureType=type;
		this.filter=filter;
		this.order=order;
		this.calculateWhere();
		this.calculateOrder();

		if (store.isUseSqlSource()){
			this.sql = store.getSqlSource();
			this.sqlCount = null;
		} else {
			this.sql = this.store.getSqlSelectPart();
			this.sqlCount = "Select count(*) From "+ ((H2StoreParameters)this.store.getParameters()).tableID();
			if (!isStringEmpty(this.totalFilter)){
				this.sql= this.sql + " Where " + this.totalFilter;
				this.sqlCount= this.sqlCount + " Where " + this.totalFilter;
			}
			if (!isStringEmpty(this.totalOrder)){
				this.sql= this.sql + " Order by " + this.totalOrder;
			}
		}
	}

	private ResultSet getNewResulset(String aSql) throws ReadException{
		this.store.open();

		Connection conn = this.store.getConnection();
		try {
			Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
			return st.executeQuery(aSql);

		} catch (java.sql.SQLException e) {
			throw new SQLException(aSql,this.store.getName(),e);
		}

	}


	private void calculateWhere(){
		if (isStringEmpty(this.store.getBaseWhereClause())){
			this.totalFilter = this.filter;
		} else {
			this.totalFilter = "(" + this.store.getBaseWhereClause() + ") and " +this.filter;
		}
	}

	private void calculateOrder(){
		if (isStringEmpty(this.store.getBaseOrder())){
			this.totalOrder = this.order;
		} else {
			this.totalOrder = this.store.getBaseOrder() + ", " +this.order;
		}

	}

	public int size() {
		checkModified();
		try {
			if (this.numReg < 0){
				ResultSet r=null;
				try {
					if (this.sqlCount != null){
						r = this.getNewResulset(this.sqlCount);
						r.next();
						numReg = r.getInt(1);
					} else{
						this.numReg = 0;
						r = this.getNewResulset(this.sql);
						while (r.next()){
							this.numReg++;
						}
					}
				} catch (java.sql.SQLException e) {
					throw new ReadException(this.store.getName(),e);

				} finally{
					try {
						if (r != null) {
							r.close();
						}
					} catch (java.sql.SQLException e) {
						throw new ReadException(this.store.getName(),e);
					}
				}
			}
			return numReg;
		} catch (BaseException e){
			throw new RuntimeException(e);
		}

	}
	
	protected Iterator internalIterator(int index) {
        checkModified();
        AbstractJDBCIterator dbfIter = null;
        try {
            dbfIter = new H2Iterator(this.store, this.featureType, this.sql,
                    this.fetchSize, index + 1);
        } catch (ReadException e) {
            throw new RuntimeException(e);
        }
        return dbfIter;
    }

	public class H2Iterator extends AbstractH2DBIterator {

		public H2Iterator(H2Store store, DBFeatureType featureType, String sql,
                int fetchSize, int initialPosition) throws ReadException {
            super(store, featureType, sql, fetchSize, null, null,
                    initialPosition);
        }

		protected void checkModified() {
			if (modified) {
				throw new ConcurrentModificationException(
						"FeatureCollection modified");
			}
		}

		protected Feature createFeatureFromTheResulset() throws ReadException {
			return ((H2Store) this.store).createFeatureFromResulset(this.rs,
					this.featureType);
		}

	}

	public void dispose() {
		this.store.deleteObserver(this);
		this.store=null;
		this.featureType=null;

	}

	public boolean isFromStore(DataStore store) {
		return this.store.equals(store);
	}

	public FeatureType getFeatureType() {
		return this.featureType;
	}
}

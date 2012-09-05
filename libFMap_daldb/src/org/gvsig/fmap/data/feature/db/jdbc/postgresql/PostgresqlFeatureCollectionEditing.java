package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.ReadException;
import org.gvsig.fmap.dal.feature.FeatureManager;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.data.feature.db.DBDataFeatureCollection;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.jdbc.SQLException;
import org.gvsig.tools.exception.BaseException;

public class PostgresqlFeatureCollectionEditing extends DBDataFeatureCollection {
	protected DBFeatureType featureType;
	protected String totalFilter;
	protected PostgresqlStore store;
	private int numReg=-1;
	private String sql;
	private String sqlCount;
	private String totalOrder;
	private int fetchSize=5000;
	private FeatureManager featureManager;


	PostgresqlFeatureCollectionEditing(FeatureManager fm,PostgresqlStore store,FeatureType type) {
		this.featureManager=fm;
		this.store=store;
		this.featureType=(DBFeatureType)type;

		this.totalFilter =store.getBaseWhereClause();
		this.totalOrder = store.getBaseOrder();

		this.sql = this.store.getSqlSelectPart();
		this.sqlCount = "Select count(*) From "+ ((PostgresqlStoreParameters)this.store.getParameters()).tableID();
		if (!isStringEmpty(this.totalFilter)){
			this.sql= this.sql + " Where " + this.totalFilter;
			this.sqlCount= this.sqlCount + " Where " + this.totalFilter;
		}
		if (!isStringEmpty(this.totalOrder)){
			this.sql= this.sql + " Order by " + this.totalOrder;
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

	public int size() {
		checkModified();
		try {
			if (this.numReg < 0){
				ResultSet r=null;
				r = this.getNewResulset(this.sqlCount);
				try {
					r.next();
					numReg = r.getInt(1);
				} catch (java.sql.SQLException e) {
					throw new ReadException(this.store.getName(),e);
				} finally{
					try {
						r.close();
					} catch (java.sql.SQLException e) {
						throw new ReadException(this.store.getName(),e);
					}
				}
				numReg = numReg+featureManager.getNum();
			}
			return numReg;
		} catch (BaseException e){
			throw new RuntimeException(e);
		}

	}

    protected Iterator internalIterator(int index) {
        checkModified();
        try {
            ResultSet r = null;
            r = this.getNewResulset(this.sql);
            PostgresIterator dbIter = new PostgresIterator(this.store,
                    this.featureType, this.sql, this.fetchSize,
                    this.featureManager, index + 1);
            return dbIter;
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }
    }
	
	protected class PostgresIterator extends AbstractPostgresqlDBIterator {

		public PostgresIterator(PostgresqlStore store,
				DBFeatureType featureType, String sql, int fetchSize,
				FeatureManager featureManager, int initialPosition)
                throws ReadException {
            super(store, featureType, sql, fetchSize, featureManager, null,
                    initialPosition);
		}

		protected void checkModified() {
			if (modified) {
				throw new ConcurrentModificationException(
						"FeatureCollection modified");
			}
		}
	}

	public void dispose() {
		this.store.deleteObserver(this);
		this.store = null;
		this.featureManager = null;
		this.featureType = null;
	}


	public boolean isFromStore(DataStore store) {
		return this.store.equals(store);
	}

	public FeatureType getFeatureType() {
		return this.featureType;
	}
}

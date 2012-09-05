package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.IsNotFeatureSettingException;
import org.gvsig.fmap.data.feature.db.DBDataFeatureCollection;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.DBStore;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStore;
import org.gvsig.fmap.data.feature.db.jdbc.SQLException;
import org.gvsig.tools.exception.BaseException;

public class PostgresqlFeatureCollection extends DBDataFeatureCollection {
	protected DBFeatureType featureType;
	protected String filter;
	protected String totalFilter;
	protected JDBCStore store;
	private String order;
	private int numReg=-1;
	private String sql;
	private String sqlCount;
	private String totalOrder;
	private int fetchSize=5000;

	PostgresqlFeatureCollection(DBStore store,FeatureType type, String filter, String order) {
		this.store=(JDBCStore)store;
		this.featureType=(DBFeatureType)type;
		this.filter=filter;
		this.order=order;
		this.calculateWhere();
		this.calculateOrder();

		if (this.store.isUseSqlSource()){
			this.sql = this.store.getSqlSource();
			this.sqlCount = null;
		} else {
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
	}

	private ResultSet getNewResulset(String aSql) throws ReadException, IsNotFeatureSettingException{
		this.store.open();

		Connection conn = ((PostgresqlStore)this.store).getConnection();
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
        PostgresqlIterator dbIter;
        try {
            dbIter = new PostgresqlIterator((PostgresqlStore) this.store,
                    this.featureType, this.sql, this.fetchSize, index + 1);
        } catch (ReadException e) {
            throw new RuntimeException(e);
        }

        return dbIter;
    }

	protected class PostgresqlIterator extends AbstractPostgresqlDBIterator {
		public PostgresqlIterator(PostgresqlStore store,
                DBFeatureType featureType, String sql, int fetchSize,
                int initialPosition) throws ReadException {
            super(store, featureType, sql, fetchSize, null, null,
                    initialPosition);
		}

		protected void createResulset() throws ReadException{

			((PostgresqlStore) this.store).open();

			String mSql = PostgresqlStoreUtils.addLimitsToSQL(
					this.sql,
					this.fetchSize,
					this.page);
			Connection conn = ((PostgresqlStore) this.store).getConnection();
			try {
				if (this.rs != null){
					this.rs.close();
//					this.rs.getStatement().close();
				}

				Statement st = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
//				System.out.println(mSql);
				this.rs = st.executeQuery(mSql);

			} catch (java.sql.SQLException e) {
				throw new SQLException(mSql,this.store.getName(),e);
			}


		}

		protected Feature createFeatureFromTheResulset() throws ReadException {
			return ((PostgresqlStore) this.store).createFeatureFromResulset(
					this.rs, this.featureType);
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

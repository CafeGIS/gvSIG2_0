package org.gvsig.fmap.data.feature.db.jdbc.postgresqlbin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.data.feature.db.DBDataFeatureCollection;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.jdbc.AbstractJDBCIterator;
import org.gvsig.fmap.data.feature.db.jdbc.SQLException;
import org.gvsig.tools.exception.BaseException;

public class PostgresqlBinFeatureCollection extends DBDataFeatureCollection {
	protected DBFeatureType featureType;
	protected String filter;
	protected String totalFilter;
	protected PostgresqlBinStore store;
	private String order;
	private int numReg=-1;
	private String sql;
	private String sqlCount;
	private String totalOrder;
	private int fetchSize=5000;

	public PostgresqlBinFeatureCollection(PostgresqlBinStore store,FeatureType type, String filter, String order) {
		this.store=store;
		this.featureType=(DBFeatureType)type;
		this.filter=filter;
		this.order=order;
		this.calculateWhere();
		this.calculateOrder();

		if (store.isUseSqlSource()){
			this.sql = store.getSqlSource();
			this.sqlCount = null;
		} else {
			this.sql = this.store.getSqlSelectPart();

			this.sqlCount = "Select count(*) From "+ ((PostgresqlBinStoreParameters)this.store.getParameters()).tableID();
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
        PostgresqlIterator dbfIter;
        try {
            dbfIter = new PostgresqlIterator(this.store, this.featureType,
                    this.sql, this.fetchSize, index + 1);
        } catch (ReadException e) {
            throw new RuntimeException(e);
        }

        return dbfIter;
    }

	protected class PostgresqlIterator extends AbstractJDBCIterator {
		private String cursorName = null;
		public PostgresqlIterator(PostgresqlBinStore store,
				DBFeatureType featureType, String sql, int fetchSize,
                int initialPosition)
				throws ReadException {
			super(store, featureType, sql, fetchSize, null, null,
                    initialPosition);

		}
		protected void createResulset() throws ReadException {
			if (this.cursorName == null) {
				this.createResulset();
			}
			super.createResulset();
		}

		protected void createCursor() throws ReadException {
			//Comprobar que no se ha inicializado
			((PostgresqlBinStore) this.store).open();
			Connection conn = ((PostgresqlBinStore) this.store).getConnection();
			String mSql= null;

			try {

				Statement st = conn.createStatement();

				this.cursorName = PostgresqlBinStoreUtils.createCursorName();
				mSql="BEGIN";
				st.execute(mSql);
				mSql="declare " + this.cursorName + " binary cursor for " + this.sql;
				st.execute("msql");
//				System.out.println(mSql);


			} catch (java.sql.SQLException e) {
				throw new SQLException(mSql,this.store.getName(),e);
			}


		}


		protected Feature createFeatureFromTheResulset() throws ReadException {
			return ((PostgresqlBinStore) this.store).createFeatureFromResulset(
					this.rs, this.featureType);
		}

		protected void close() throws java.sql.SQLException {
			if (rs == null) {
				return;
			}
			try {
				super.close();
			} finally{
				Statement st;
				try {
					Connection conn = ((PostgresqlBinStore) this.store)
							.getConnection();
					st = conn.createStatement();
					st.execute("Rollback");
				} catch (java.sql.SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ReadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


		}
		protected void checkModified() {
			if (modified) {
				throw new ConcurrentModificationException(
						"FeatureCollection modified");
			}
		}

		protected String getFinalResulsetSQL() {
			return "fetch forward " + this.fetchSize + " in " + this.cursorName;
		}

		protected Connection getConnection() throws ReadException {
			((PostgresqlBinStore) this.store).open();
			return ((PostgresqlBinStore) this.store).getConnection();
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

package org.gvsig.fmap.data.feature.db.jdbc.postgresql;


import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.ReadException;
import org.gvsig.fmap.dal.feature.FeatureManager;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.expressionevaluator.Filter;
import org.gvsig.fmap.data.feature.db.DBDataFeatureCollection;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.tools.exception.BaseException;

public class PostgresqlFeatureCollectionEditingFiltered extends DBDataFeatureCollection {
	protected DBFeatureType featureType;
	protected String totalFilter;
	protected String filter;
	protected PostgresqlStore store;
	private int numReg=-1;
	private String sql;
	private String sqlCount;
	private String totalOrder;
	private int fetchSize=5000;
	private FeatureManager featureManager;
	private Filter parser;


	PostgresqlFeatureCollectionEditingFiltered(FeatureManager fm,
			PostgresqlStore store, FeatureType type, String filter)
			throws ReadException {
		this.featureManager=fm;
		this.store=store;
		this.featureType=(DBFeatureType)type;

		this.filter = filter;

		this.calculateWhere();
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

		if (this.filter!=null) {
			parser = DataManager.getManager().getExpressionParser()
					.parseFilter(filter);
		}

	}

	private void calculateWhere(){
		if (isStringEmpty(this.store.getBaseWhereClause())){
			this.totalFilter = this.filter;
		} else {
			this.totalFilter = "(" + this.store.getBaseWhereClause() + ") and " +this.filter;
		}
	}

	public int size() {
		checkModified();
		if (this.numReg < 0){
			this.numReg =0;
			try{
				Iterator iter = this.iterator();
				while (true){
					iter.next();
					this.numReg++;
				}
			} catch (NoSuchElementException e){
				//Normal condition exit
			}
		}
		return this.numReg;

	}


    protected Iterator internalIterator(int index) {
        checkModified();
        try {
            PostgresIterator dbIter = new PostgresIterator(this.store,
                    this.featureType, this.sql, this.fetchSize,
                    this.featureManager, this.parser, index + 1);
            return dbIter;
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }
    }

	protected class PostgresIterator extends AbstractPostgresqlDBIterator {
		public PostgresIterator(PostgresqlStore store,
				DBFeatureType featureType, String sql, int fetchSize,
				FeatureManager featureManager, Filter featureFilter,
                int initialPosition)
				throws ReadException {
			super(store, featureType, sql, fetchSize, featureManager,
					featureFilter, initialPosition);
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
		this.parser= null;

	}


	public boolean isFromStore(DataStore store) {
		return this.store.equals(store);
	}

	public FeatureType getFeatureType() {
		return this.featureType;
	}
}

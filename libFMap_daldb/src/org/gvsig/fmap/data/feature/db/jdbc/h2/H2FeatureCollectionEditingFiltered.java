package org.gvsig.fmap.data.feature.db.jdbc.h2;


import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureManager;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.expressionevaluator.Filter;
import org.gvsig.fmap.data.feature.db.DBDataFeatureCollection;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.tools.exception.BaseException;

public class H2FeatureCollectionEditingFiltered extends DBDataFeatureCollection {
	protected DBFeatureType featureType;
	protected String totalFilter;
	protected String filter;
	protected H2Store store;
	private int numReg=-1;
	private String sql;
	private String sqlCount;
	private String totalOrder;
	private FeatureManager featureManager;
	private Filter parser;
	private int fetchSize = 5000;


	H2FeatureCollectionEditingFiltered(FeatureManager fm, H2Store store,
			FeatureType type, String filter) throws ReadException {
		this.featureManager=fm;
		this.store=store;
		this.featureType=(DBFeatureType)type;

		this.filter = filter;

		this.calculateWhere();
		this.totalOrder = store.getBaseOrder();

		this.sql = this.store.getSqlSelectPart();
		this.sqlCount = "Select count(*) From "+ ((H2StoreParameters)this.store.getParameters()).tableID();
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
			this.numReg=0;
			try{
				Iterator iter =this.iterator();
				while (true){
					iter.next();
					numReg++;
				}
			} catch (NoSuchElementException e){
				//Normal condition exit
			}
		}
		return numReg;

	}

    protected Iterator internalIterator(int index) {
		checkModified();
		try{
			H2Iterator dbIter = new H2Iterator(this.store, this.featureType,
					this.sql, this.fetchSize, this.featureManager, this.parser,
                    index + 1);
			return dbIter;
		} catch (BaseException e){
			throw new RuntimeException(e);
		}
	}

	protected class H2Iterator extends AbstractH2DBIterator {

		public H2Iterator(H2Store store, DBFeatureType featureType,
				String sql,
				int fetchSize, FeatureManager featureManager,
				Filter featureFilter, int initialPosition)
				throws ReadException {
			super(store, featureType, sql, fetchSize, featureManager,
					featureFilter, initialPosition);
		}

		protected Feature createFeatureFromTheResulset() throws ReadException {
			return ((H2Store) this.store).createFeatureFromResulset(this.rs,
					this.featureType);
		}

		protected void checkModified() {
			if (modified) {
				throw new ConcurrentModificationException(
						"FeatureCollection modified");
			}
		}

	}


	protected class H2IteratorMemory implements Iterator{
		protected long position=0;
		private boolean nextChecked=false;
		private Feature feature;
		private FeatureManager featureManager;
		private FeatureType featureType;

		public H2IteratorMemory(DBFeatureType featureType, FeatureManager featureManager){
			this.featureType = featureType;
			position=0;
			this.featureManager=featureManager;
		}

		protected void checkModified(){
			if (modified) {
				throw new ConcurrentModificationException("FeatureCollection modified");
			}
		}

		public boolean hasNext(){
			checkModified();


			if (nextChecked){
				return this.feature != null;
			}
			Feature feature=null;
			nextChecked=true;
			while (true){
				if (position<featureManager.getNum()){
					try {
						feature=featureManager.get((int)position,store,this.featureType);
					} catch (ReadException e) {
						throw new RuntimeException(
								new ReadException(store.getName(),e)
						);
					}
					position++;

				}else{
					this.feature = null;
					return false;
				}

				if(featureManager.isDeleted(feature)) {
					continue;
				}

				if (filter == null) {
					this.feature=feature;
					return true;

				} else {
					try {
						if (parser.evaluate(feature)) {
							this.feature=feature;
							return true;
						}else{
							continue;
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}

		public Object next() {
			checkModified();
			if (!nextChecked){
				hasNext();
			}
			if (this.feature == null) {
				throw new NoSuchElementException();
			}
			nextChecked=false;
			Feature feature = this.feature;
			this.feature = null;
			return feature;
		}

		public void remove() {
			throw new UnsupportedOperationException();
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

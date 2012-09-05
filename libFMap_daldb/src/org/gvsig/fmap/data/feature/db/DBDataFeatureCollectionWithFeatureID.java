package org.gvsig.fmap.data.feature.db;

import java.util.*;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.expressionevaluator.Filter;
import org.gvsig.fmap.dal.feature.impl.AbstractFeatureIDIterator;
import org.gvsig.fmap.dal.feature.impl.FeatureManager;
import org.gvsig.fmap.dal.impl.DefaultDataManager;

public abstract class DBDataFeatureCollectionWithFeatureID extends
		DBDataFeatureCollection {

	protected DBStore store;
	protected Collection featureIDs;
	protected FeatureType featureType;

	protected abstract FeatureSet newBaseDataFeatureCollection(DBStore store,FeatureType featureType,String filter,String order);

	protected void init(FeatureManager fm, DBStore store, FeatureType type, String filter, String order) throws ReadException {
		this.store=store;
		this.featureType= type;

		DefaultDataManager manager = DefaultDataManager.getManager();

		Filter parser = null;
		Feature feature;
		Collection tmpFeatures;
		if (order == null){
			if (filter == null){
				tmpFeatures = new ArrayList(); // Si queremos optimizar deberiamos inicializar
				// el tamaño
			} else {
				tmpFeatures = new ArrayList();
			}
		} else {
			Comparator comparator = manager.getExpressionParser()
					.parseComparator(order);
			tmpFeatures = new TreeSet(comparator);
		}

		if (filter != null && filter != ""){
			parser = manager.getExpressionParser().parseFilter(filter);
		}
		for (int i=0;i<fm.getNum();i++){
			feature = fm.get(i,store,this.featureType);
			if (fm.isDeleted(feature)){
				continue;
			}
			if (parser == null || parser.evaluate(feature)) {
				if (order != null){
					tmpFeatures.add(feature);
				} else {
					tmpFeatures.add(feature.getReference());
				}
			}


		}

		FeatureSet coll = this.newBaseDataFeatureCollection(store, featureType, filter, order);
		Iterator iter = coll.iterator();
		while (iter.hasNext()){
			feature =(Feature)iter.next();
			if (!fm.isDeleted(feature)){
				if (order != null){
					tmpFeatures.add(feature);
				} else {
					tmpFeatures.add(feature.getReference());
				}
			}
		}

		if (order != null){
			featureIDs = new ArrayList();
			iter = tmpFeatures.iterator();
			while (iter.hasNext()){
				featureIDs.add(((Feature)iter.next()).getReference());
			}
		} else {
			featureIDs = tmpFeatures;
		}


	}

	public int size() {
		checkModified();
		return featureIDs.size();
	}

	public Iterator iterator() {
		checkModified();
		FIDIterator dbfIter = new FIDIterator(featureIDs.iterator(),
				this.featureType);
		return dbfIter;
	}
	
	protected Iterator internalIterator(int index) {
        checkModified();
        FIDIterator dbfIter;


        if (featureIDs instanceof List) {
            dbfIter = new FIDIterator(((List) featureIDs).listIterator(index),
                    featureType);
        } else {
            // TODO: If featureIDs is not a Collection of type List (actually,
            // only when ordering is applied), we are not able
            // to get a listIterator with an index, so we have to move the
            // iterator to the required position. This option will be probably
            // a lot slower, so an option would be to order with a List
            // instead of a TreeSet.
            Iterator iter = featureIDs.iterator();
            for (int i = 0; i < index; i++) {
                iter.next();
            }

            dbfIter = new FIDIterator(iter, featureType);
        }

        return dbfIter;
	}

	public class FIDIterator extends AbstractFeatureIDIterator {

		public FIDIterator(Iterator iter, FeatureType featureType) {
			super(iter, featureType);
		}

		protected void checkModified() {
			DBDataFeatureCollectionWithFeatureID.this.checkModified();
		}

	}

	public void dispose(){
		this.store.deleteObserver(this);
		this.store = null;
		this.featureType = null;
	}


	public boolean isFromStore(DataStore store) {
		return this.store.equals(store);
	}

	public FeatureType getDefaultFeatureType() {
		return this.featureType;
	}
}

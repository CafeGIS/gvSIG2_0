package org.gvsig.fmap.data.feature.db.jdbc.h2;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.FeatureManager;
import org.gvsig.fmap.data.feature.db.DBDataFeatureCollectionWithFeatureID;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.DBStore;


public class H2FeatureCollectionWithFeatureID extends DBDataFeatureCollectionWithFeatureID {
	H2FeatureCollectionWithFeatureID(FeatureManager fm,DBStore store,FeatureType type, String filter,String order) throws ReadException {
		init(fm, store, type,filter, order);
	}

	public FeatureSet newBaseDataFeatureCollection(DBStore store, FeatureType featureType, String filter, String order) {
		return new H2FeatureCollection((H2Store)store,(DBFeatureType)this.featureType,filter,order);
	}

}

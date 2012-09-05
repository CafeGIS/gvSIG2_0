package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.FeatureManager;
import org.gvsig.fmap.data.feature.db.DBDataFeatureCollectionWithFeatureID;
import org.gvsig.fmap.data.feature.db.DBStore;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStore;


public class PostgresqlFeatureCollectionWithFeatureID extends DBDataFeatureCollectionWithFeatureID {

	PostgresqlFeatureCollectionWithFeatureID(FeatureManager fm,JDBCStore store,FeatureType type, String filter,String order) throws ReadException {
		init(fm, store, type, filter, order);
	}

	public FeatureSet newBaseDataFeatureCollection(DBStore store, FeatureType featureType, String filter, String order) {
		return new PostgresqlFeatureCollection((DBStore)store,this.featureType,filter,order);
	}

}

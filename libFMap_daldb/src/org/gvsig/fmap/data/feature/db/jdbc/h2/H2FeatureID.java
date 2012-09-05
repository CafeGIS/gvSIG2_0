package org.gvsig.fmap.data.feature.db.jdbc.h2;

import org.gvsig.fmap.data.feature.db.DBFeatureID;
import org.gvsig.fmap.data.feature.db.DBStore;

public class H2FeatureID extends DBFeatureID{

	protected H2FeatureID(DBStore store, Object[] featureKey) {
		super(store, featureKey);
	}


}

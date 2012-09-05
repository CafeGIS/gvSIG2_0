package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import org.gvsig.fmap.data.feature.db.DBFeatureID;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStore;

public class PostgresqlFeatureID extends DBFeatureID{

	protected PostgresqlFeatureID(JDBCStore store, Object[] featureKey) {
		super(store, featureKey);
	}


}

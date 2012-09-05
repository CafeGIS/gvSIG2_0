package org.gvsig.fmap.data.feature.db.jdbc.postgresqlbin;

import org.gvsig.fmap.data.feature.db.DBFeatureID;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStore;

public class PostgresqlBinFeatureID extends DBFeatureID{

	protected PostgresqlBinFeatureID(JDBCStore store, Object[] featureKey) {
		super(store, featureKey);
	}


}

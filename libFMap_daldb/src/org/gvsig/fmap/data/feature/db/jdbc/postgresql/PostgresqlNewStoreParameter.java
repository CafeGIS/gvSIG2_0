package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.DBNewStoreParameter;

public class PostgresqlNewStoreParameter extends DBNewStoreParameter {

	PostgresqlNewStoreParameter(DataStoreParameters dsp) {
		this.init(dsp);
	}

	public PostgresqlStoreParameters getPostgresqlParameters() {
		return (PostgresqlStoreParameters) this.dsparameters;
	}

	public DBFeatureType getDBFeatureType(){
		return (DBFeatureType)this.featureType;
	}

	public String getDataStoreName() {
		return PostgresqlStore.DATASTORE_NAME;
	}

	public boolean isValid() {
		if (!super.isValid()){
			return false;
		}
		if (!(this.dsparameters instanceof PostgresqlStoreParameters)) {
			return false;
		}
		if (this.getDBFeatureType().getFieldsId().length != 1){
			return false;
		}
		return true;
	}



}

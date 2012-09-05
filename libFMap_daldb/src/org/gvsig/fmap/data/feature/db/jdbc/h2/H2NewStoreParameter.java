package org.gvsig.fmap.data.feature.db.jdbc.h2;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.DBNewStoreParameter;
import org.h2.server.web.DbTableOrView;

public class H2NewStoreParameter extends DBNewStoreParameter {

	H2NewStoreParameter(DataStoreParameters dsp) {
		this.init(dsp);
	}

	public H2StoreParameters getH2Parameters(){
		return (H2StoreParameters)this.dsparameters;
	}

	public DBFeatureType getDBFeatureType(){
		return (DBFeatureType)this.featureType;
	}

	public String getDataStoreName() {
		return H2Store.DATASTORE_NAME;
	}

	public boolean isValid() {
		if (!super.isValid()){
			return false;
		}
		if (!(this.dsparameters instanceof H2StoreParameters)){
			return false;
		}
		if (this.getDBFeatureType().getFieldsId().length != 1){
			return false;
		}
		return true;
	}



}

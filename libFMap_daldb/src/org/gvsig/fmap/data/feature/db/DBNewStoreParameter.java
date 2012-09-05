package org.gvsig.fmap.data.feature.db;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.feature.impl.AbstractNewFeatureStoreParameter;

public abstract class DBNewStoreParameter extends AbstractNewFeatureStoreParameter {
	public void init(DataStoreParameters dsp) {
		super.init(dsp);
	}

	public boolean isValid() {
		if (!(this.dsparameters instanceof DBStoreParameters)) {
			return false;
		}
		DBStoreParameters params = (DBStoreParameters)this.dsparameters;
		if (!(this.featureType instanceof DBFeatureType)){
			return false;
		}
		if (params.tableID() == null){
			return false;
		}
		if (params.tableID().length() == 0){
			return false;
		}
		DBFeatureType ftype = (DBFeatureType)this.featureType;
		if (ftype.getFieldsId() == null){
			return false;
		}
		if (ftype.size() == 0 || ftype.size() < ftype.getFieldsId().length){
			return false;
		}
		if (ftype.getFieldsId().length == 0){
			return false;
		}
		if (ftype.getFieldsId().length != params.getFieldsId().length){
			return false;
		}
		return true;
	}

}

package org.gvsig.fmap.dal.feature;

import org.gvsig.fmap.dal.NewDataStoreParameters;

public interface NewFeatureStoreParameters extends NewDataStoreParameters {

	public void setDefaultFeatureType(FeatureType defaultFeatureType);

	public FeatureType getDefaultFeatureType();
}

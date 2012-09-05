package org.gvsig.fmap.data.feature.db;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.AbstractFeature;
import org.gvsig.fmap.dal.feature.FeatureType;

public abstract class DBFeature extends AbstractFeature{

	public DBFeature(FeatureType featureType) throws ReadException {
		super(featureType);
	}

}

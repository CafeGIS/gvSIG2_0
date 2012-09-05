package org.gvsig.normalization;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.AbstractFeatureStoreTransform;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

public class Normalizer  extends AbstractFeatureStoreTransform{

	public void applyTransform(Feature source, EditableFeature target)
			throws DataException {
		// TODO Auto-generated method stub
		
	}

	public FeatureType getSourceFeatureTypeFrom(FeatureType targetFeatureType) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isTransformsOriginalValues() {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadFromState(PersistentState state)
			throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	public void saveToState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

}

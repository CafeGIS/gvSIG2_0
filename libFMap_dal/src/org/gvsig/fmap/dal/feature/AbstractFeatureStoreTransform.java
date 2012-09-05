package org.gvsig.fmap.dal.feature;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;

/**
 * Abstract feature store transform intended for giving a partial default implementation 
 * of the {@link FeatureStoreTransform} interface to other transform implementations. It is recommended
 * to extend this class when implementing new {@link FeatureStoreTransform}s.
 * 
 */
public abstract class AbstractFeatureStoreTransform implements
		FeatureStoreTransform {

	private FeatureStore store;

	private FeatureType defaultFeatureType = null;
	private List featureTypes = new ArrayList();

	public FeatureType getDefaultFeatureType() throws DataException {
		return defaultFeatureType;
	}

	public List getFeatureTypes() throws DataException {
		return featureTypes;
	}

	public void setFeatureStore(FeatureStore store) {
		this.store = store;
	}

	public FeatureStore getFeatureStore() {
		return store;
	}

	protected void setFeatureTypes(List types, FeatureType defaultType) {
		this.featureTypes.clear();
		this.featureTypes.addAll(types);
		this.defaultFeatureType = defaultType;
	}
	/*
    public void loadState(PersistentState state) throws PersistenceException {
    	state.set("mainStore", store.getId());
    	state.set("defaultFeatureType", defaultFeatureType);
    	state.set("featureTypes", featureTypes);
    }

    public void loadFromState(PersistentState state) throws PersistenceException {
    	this.store = DALLocator.getDataManager().getStore(state.get("mainStore"));
    	this.defaultFeatureType = (FeatureType) state.get("defaultFeatureType");
    	this.featureTypes = (List) state.get("featureTypes");
    }
    */
}

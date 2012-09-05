package org.gvsig.fmap.data.feature.swing.table;

import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;

public interface FeatureTypeChangeListener {

	void change(FeatureStore featureStore, FeatureType featureType, boolean isDoubleBuffered);

}

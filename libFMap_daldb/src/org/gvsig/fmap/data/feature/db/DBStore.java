package org.gvsig.fmap.data.feature.db;


import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.AbstractFeatureStore;
import org.gvsig.fmap.dal.index.IndexException;

public abstract class DBStore extends AbstractFeatureStore{
	public abstract Feature getFeatureByID(FeatureType featureType2, Object[] featureKey) throws ReadException;
    /* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#isEditable()
	 */
	public boolean allowWrite() {
		return !((DBFeatureType)this.getDefaultFeatureType()).isReadOnly();
	}
}

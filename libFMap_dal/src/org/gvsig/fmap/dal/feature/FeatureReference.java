package org.gvsig.fmap.dal.feature;

import org.gvsig.fmap.dal.exception.DataException;

/**
 * A FeatureReference is a lightweight unique identifier for a
 * {@link Feature}. It is used to keep references on features
 * without maintaining the whole feature in memory, thus improving
 * memory needs.
 * 
 */
public interface FeatureReference {

	/**
	 * Returns the referenced {@link Feature}
	 * 
	 * @return the {@link Feature} identified by this {@link FeatureReference}
	 * 
	 * @throws DataException
	 */
	public Feature getFeature() throws DataException;

	/**
	 * Returns the referenced {@link Feature}
	 * 
	 * @param featureType
	 *            The {@link FeatureType} to which the referenced
	 *            {@link Feature} belongs.
	 * 
	 * @return the {@link Feature} identified by this {@link FeatureReference}
	 * 
	 * @throws DataException
	 */
	public Feature getFeature(FeatureType featureType) throws DataException;
}

package org.gvsig.fmap.dal.feature;

import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;

/**
 * This interface represents a container for store transforms.
 *
 * Provides access to transforms by index and by iterator, and also allows
 * adding and removing transforms.
 *
 */
public interface FeatureStoreTransforms {

	/**
	 * Returns the FeatureStoreTransform given its index.
	 *
	 * @param index
	 * 			a position in this FeatureStoreTransforms
	 *
	 * @return
	 * 		the FeatureStoreTransform that is stored in the given index.
	 */
	public FeatureStoreTransform getTransform(int index);

	/**
	 * Adds a FeatureStoreTransform to this container.
	 *
	 * @param transform
	 * 			the FeatureStoreTransform to add
	 *
	 * @return
	 * 		the added FeatureStoreTransform
	 *
	 * @throws DataException
	 */
	public FeatureStoreTransform add(FeatureStoreTransform transform)
			throws DataException;

	/**
	 * Returns the number of FeatureStoreTransforms stored in this container
	 *
	 * @return
	 * 		number of FeatureStoreTransforms stored in this container
	 */
	public int size();

	/**
	 * Indicates whether this container is empty.
	 *
	 * @return
	 * 		true if this container is empty, false if not
	 */
	public boolean isEmpty();

	/**
	 * Returns an iterator over this container elements.
	 *
	 * @return
	 * 		an iterator over this container elements.
	 */
	public Iterator iterator();

	/**
	 * Empties this container.
	 *
	 */
	public void clear();

	/**
	 * Removes the {@link FeatureStoreTransform} given its index.
	 *
	 * @param index
	 * 			the position of the {@link FeatureStoreTransform} to remove.
	 * @return
	 * 		the removed object
	 */
	public Object remove(int index);

	/**
	 * Removes the given {@link FeatureStoreTransform}.
	 *
	 * @param transform
	 * 				{@link FeatureStoreTransform} to remove
	 * @return
	 * 		true if the transform was successfully removed, false if not.
	 */
	public boolean remove(FeatureStoreTransform transform);

	/**
	 * Retruns true if any {@link FeatureStoreTransform} make changes of any
	 * previous attributes values.<br>
	 *
	 * If all {@link FeatureStoreTransform} affects only to attributes defition,
	 * the {@link FeatureSet} performance can be better.
	 *
	 * @return
	 */
	public boolean isTransformsOriginalValues();

}

package org.gvsig.fmap.dal.feature.spi;

import java.util.List;

import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.resource.Resource;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;

public interface FeatureStoreProviderServices extends
		DataStoreProviderServices {

	/**
	 * Call this to send a notification to observers of this store
	 *
	 * @param notification
	 */
	public void notifyChange(String notification);

	/**
	 * Call this to send a notification to observers of this store relative to
	 * Resources
	 *
	 * @param notification
	 */
	public void notifyChange(String notification, Resource resource);

	/**
	 * Create a new instance of default implementation of a
	 * {@link FeatureSelection}
	 *
	 * @return new {@link FeatureSelection}
	 * @throws DataException
	 */
	public FeatureSelection createDefaultFeatureSelection()
			throws DataException;

	/**
	 * Create a new instance of default implementation of a {@link FeatureProvider}
	 *
	 * @return new {@link FeatureProvider}
	 * @throws DataException
	 */
	public FeatureProvider createDefaultFeatureProvider(FeatureType type)
			throws DataException;

	/**
	 * Sets {@link FeatureType} available from this store.<br>
	 *
	 * <strong>Note:</strong> <code>defaultType</code> must be in
	 * <code>types</code>
	 *
	 * @param list
	 *            of all {@link FeatureType} available
	 * @param {@link FeatureType} used in
	 *        {@link FeatureStore#getDefaultFeatureType()}
	 */
	public void setFeatureTypes(List types, FeatureType defaultType);

	/**
	 * Reaturn {@link DataManager} instance.
	 *
	 * @return
	 */
	public DataManager getManager();

	/**
	 * Create a {@link Feature} instance for {@link FeatureProvider}
	 * <code>data</code>
	 *
	 * <br>
	 * <br>
	 * <strong>NOTE:</strong> Normaly <strong> Providers must use</strong>
	 * {@link FeatureProvider} instances instead Feature
	 *
	 * @param data
	 * @return a {@link Feature}
	 * @throws DataException
	 */
	public Feature createFeature(FeatureProvider data)
	throws DataException;

	/**
	 * Creates a new instance of EditableFeatureType. Uses 'default' as
	 * identifier.
	 *
	 * @return
	 */
	public EditableFeatureType createFeatureType();

	/**
	 * Creates a new instance of EditableFeatureType. Uses 'id' as identifier.
	 *
	 * @return
	 */
	public EditableFeatureType createFeatureType(String id);

	/**
	 * Return the instance of {@link FeatureStoreProvider} for this store.
	 *
	 * @return
	 */
	public FeatureStoreProvider getProvider();


	/**
	 * Return original {@link FeatureType} of {@link FeatureStoreProvider}.
	 *
	 * @param id
	 *            of the {@link FeatureType}
	 * @return
	 * @throws DataException
	 */
	public FeatureType getProviderFeatureType(String featureTypeId);

	/**
	 * Extract {@link FeatureProvider} from a {@link Feature} instance.
	 *
	 * @param feature
	 * @return
	 */
	public FeatureProvider getFeatureProviderFromFeature(Feature feature);

	/**
	 * Return current FeatureStore
	 *
	 * @return
	 */
	public FeatureStore getFeatureStore();

	/**
	 * Return current FeatureStore name
	 *
	 * @return
	 */
	public String getName();

	/**
	 * Return default {@link FeatureType} of the store
	 *
	 * @return
	 */
	public FeatureType getDefaultFeatureType() throws DataException;

}

package org.gvsig.fmap.dal.spi;

import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProviderServices;

public interface DataManagerProviderServices extends DataManager {

	public void registerStoreProvider(String name,
			Class dataStoreProviderClass,
			Class parametersClass);


	public void registerExplorerProvider(String name,
			Class dataSourceClass, Class parametersClass);


    /**
     * Registers a new feature index provider.
     *
     * @param name
     * 			provider's name
     *
     * @param description
     * 			provider's description
     *
     * @param clazz
     * 			a custom FeatureIndexProvider implementation
     *
     * @param dataType
     * 			one of the constants in {@link DataTypes}. This means that this provider
     * 			can build indexes based on attributes of this type.
     */
	public void registerFeatureIndexProvider(String name, String description, Class clazz, int dataType);

	/**
	 * Returns a DataIndexProvider compatible with the attribute data type.
	 * @param store associated FeatureStore
	 * @param type associated FeatureType
	 * @param attr associated FeatureAttributeDescriptor
	 * @param providerNames array of strings containing one or more preferred providers
	 * @return empty DataIndexProvider, initialized and ready to use
	 */
	public FeatureIndexProviderServices createFeatureIndexProvider(String name, FeatureStore store, FeatureType type,
			String indexName,
			FeatureAttributeDescriptor attr) throws InitializeException,
			ProviderNotRegisteredException;


}
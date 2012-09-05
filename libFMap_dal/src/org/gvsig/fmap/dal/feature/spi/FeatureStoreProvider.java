package org.gvsig.fmap.dal.feature.spi;


import java.util.Iterator;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureLocks;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.spi.DataStoreProvider;
import org.gvsig.fmap.geom.primitive.Envelope;

public interface FeatureStoreProvider extends DataStoreProvider {

	/**
	 * Return the name of the provider.
	 *
	 * @return
	 */
	public String getName();

	/**
	 * Return a new OID valid for a new feature.
	 *
	 * @return a new OID
	 * @see {@link FeatureStoreProvider#getOIDType()}
	 */
	public Object createNewOID();

	/**
	 * Return OID data type (from {@link DataTypes}) of this store.
	 *
	 * @return OID data type
	 * @see {@link FeatureStoreProvider#createNewOID()} {@link DataTypes}
	 */
	public int getOIDType();

	/**
	 * Factory of {@link FeatureProvider}. Create a new {@link FeatureProvider} instance
	 * valid for this Store.
	 *
	 * @param {@link FeatureType} of the {@link FeatureProvider}
	 * @return
	 * @throws DataException
	 */
	public FeatureProvider createFeatureProvider(FeatureType type) throws DataException;

	/**
	 * Factory of {@link FeatureSelection}. Create a new
	 * {@link FeatureSelection} instance valid for this Store.
	 *
	 * @return
	 * @throws DataException
	 */
	public FeatureSelection createFeatureSelection() throws DataException;

	/**
	 * Factory of {@link FeatureLocks}. Create a new {@link FeatureLocks}
	 * instance valid for this Store.
	 *
	 *
	 * @return {@link FeatureLocks} or <code>null</code> if not
	 *         {@link FeatureStoreProvider#isLocksSupported()}
	 * @throws DataException
	 */
	public FeatureLocks createFeatureLocks() throws DataException;

	/**
	 * Factory of {@link FeatureSetProvider}. Create a new
	 * {@link FeatureSetProvider} that represents result of {@link FeatureQuery}
	 * .
	 *
	 * @param query
	 *            (never will be null)
	 * @param featureType
	 *            (never will be null)
	 * @return
	 * @throws DataException
	 */
	public FeatureSetProvider createSet(FeatureQuery query,
			FeatureType featureType) throws DataException;

	/**
	 * Return {@link FeatureProvider} from a
	 * {@link FeatureReferenceProviderServices} using
	 * {@link FeatureStore#getDefaultFeatureType()} as {@link FeatureType}
	 *
	 * @param reference
	 * @return
	 * @throws DataException
	 */
	public FeatureProvider getFeatureProviderByReference(FeatureReferenceProviderServices reference)
			throws DataException;

	/**
	 * Return {@link FeatureProvider} from a
	 * {@link FeatureReferenceProviderServices} using <code>featureType</code>
	 * as {@link FeatureType}
	 *
	 * @param reference
	 * @return
	 * @throws DataException
	 */
	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference, FeatureType featureType)
			throws DataException;
	/**
	 * Informs that store supports write.
	 *
	 * @return <true> if write is supported
	 */
	public boolean allowWrite();

	/**
	 *Informs that store supports write a geometry.
	 *
	 * @param geometryType
	 * @param geometrySubType
	 * @return
	 * @throws DataException
	 */
	public boolean canWriteGeometry(int geometryType, int geometrySubType) throws DataException;


	public interface FeatureTypeChanged {
		FeatureType getSource();

		FeatureType getTarget();
	}

	/**
	 * Perform changes on store.
	 *
	 * @param deleteds
	 *            iterator of {@link FeatureReferenceProviderServices}
	 * @param inserteds
	 *            iterator of {@link FeatureProvider}
	 * @param updateds
	 *            iterator of {@link FeatureProvider}
	 * @param featureTypesChanged
	 *            iterator of {@link FeatureTypeChanged}
	 *
	 * @throws DataException
	 */
	public void performChanges(Iterator deleteds, Iterator inserteds, Iterator updateds, Iterator featureTypesChanged) throws DataException;

	/**
	 * Returns this store's total envelope (extent).
	 *
	 * @return this store's total envelope (extent) or <code>null</code> if
	 *         store not have geometry information
	 */
	public Envelope getEnvelope() throws DataException;

	/**
	 * Informs if store supports locks
	 *
	 * @return
	 */
	public boolean isLocksSupported();

	/**
	 * Return {@link FeatureStoreProviderServices} for this store
	 *
	 * @return
	 */
	public FeatureStoreProviderServices getStoreServices();

	/**
	 * Inform if the store provider supports automatic values for attributues
	 * (autonumeric)
	 *
	 * @return <code>true</code> if supported
	 */
	public boolean allowAutomaticValues();

	/**
	 * Returns total feature count of this store.
	 *
	 * @return
	 * @throws DataException
	 */
	public long getFeatureCount() throws DataException;


	public boolean supportsAppendMode();

	public void beginAppend() throws DataException;

	public void endAppend() throws DataException;

	public void append(FeatureProvider featureProvider) throws DataException;



}

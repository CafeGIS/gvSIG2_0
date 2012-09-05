package org.gvsig.fmap.dal.feature.spi.memory;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.PerformEditingException;
import org.gvsig.fmap.dal.feature.spi.AbstractFeatureStoreProvider;
import org.gvsig.fmap.dal.feature.spi.DefaultFeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.tools.dynobject.DynObject;

public abstract class AbstractMemoryStoreProvider extends
		AbstractFeatureStoreProvider {

	protected ArrayList data;

	protected AbstractMemoryStoreProvider(DataStoreParameters params,
			DataStoreProviderServices storeServices, DynObject metadata) {
		super(params, storeServices, metadata);
	}

	protected AbstractMemoryStoreProvider(DataStoreParameters params,
			DataStoreProviderServices storeServices) {
		super(params, storeServices);
	}

	public void performChanges(Iterator deleteds, Iterator inserteds, Iterator updateds, Iterator originalFeatureTypesUpdated) throws PerformEditingException {
		throw new UnsupportedOperationException();
	}

	public void addFeatureProvider(FeatureProvider data) {
		data.setOID(this.createNewOID());
		this.data.add(data);
	}



	public long getDataSize() throws DataException {
		this.open();
		return this.data.size();
	}

	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference)
			throws DataException {
		int oid = ((Long) reference.getOID()).intValue();
		return (FeatureProvider) this.data.get(oid);
	}

	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference,
			FeatureType featureType) throws DataException {
		int oid = ((Long) reference.getOID()).intValue();
		return new MemoryFeatureProviderAttributeMapper((DefaultFeatureProvider) this.data
				.get(oid),
				featureType);
	}

	public FeatureSetProvider createSet(FeatureQuery query, FeatureType featureType)
			throws DataException {
		this.open();
		return new MemoryFeatureSet(this, query, featureType, this.data);
	}

	public FeatureProvider createFeatureProvider(FeatureType featureType)throws DataException  {
		this.open();
		return new DefaultFeatureProvider(featureType);
	}


	public long getFeatureCount() throws DataException {
		return data.size();
	}
}

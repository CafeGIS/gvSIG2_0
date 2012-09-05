package org.gvsig.fmap.data.feature.db;

import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.feature.exception.InitializeWriterException;
import org.gvsig.fmap.dal.feature.impl.AbstractFeatureExplorer;
import org.gvsig.fmap.dal.impl.DefaultDataManager;

public abstract class DBExplorer extends AbstractFeatureExplorer {

	public boolean canAdd() {
		return false;
	}

	public DataStoreParameters add(NewFeatureStoreParameters ndsp) throws InitializeWriterException, InitializeException {
		throw new UnsupportedOperationException();
	}

	public DataStoreParameters add(NewDataStoreParameters ndsp) throws InitializeWriterException, InitializeException {
		return this.add((NewFeatureStoreParameters)ndsp, false);
	}

	public void remove(DataStoreParameters dsp) throws ReadException {
		throw new UnsupportedOperationException();

	}

	public NewDataStoreParameters createNewDataStoreParameter()
			throws InitializeException {
		throw new UnsupportedOperationException();
	}

	public DataStore createDataStore(DataStoreParameters dsp) throws InitializeException, InitializeWriterException {
		DataManager manager = DefaultDataManager.getManager();
		return manager.createStore(dsp);
	}

}
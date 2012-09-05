package org.gvsig.fmap.dal.store.lidar;

import java.io.File;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.BaseTestFeatureStore;
import org.gvsig.fmap.geom.GeometryLibrary;

public class TestLiDAR extends BaseTestFeatureStore {

	protected boolean testLiDARInitialized = false;

	public static final File file_prueba = new File(TestLiDAR.class
			.getResource(
			"data/Duerna_000004.las").getFile()); // FIXME

	protected void setUp() throws Exception {
		super.setUp();

		if (testLiDARInitialized) {
			return;
		}

		DALFileLibrary libFile = new DALFileLibrary();
		libFile.initialize();
		libFile.postInitialize();

		GeometryLibrary geoLib = new GeometryLibrary();
		geoLib.initialize();
		geoLib.postInitialize();

		LiDARLibrary lib = new LiDARLibrary();
		lib.initialize();
		lib.postInitialize();

		testLiDARInitialized = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.feature.BaseTestFeatureStore#getDefaultDataStoreParameters
	 * ()
	 */
	public DataStoreParameters getDefaultDataStoreParameters()
			throws DataException {
		LiDARStoreParameters parameters = null;

		parameters = (LiDARStoreParameters) dataManager
				.createStoreParameters(LiDARStoreProvider.NAME);

		parameters.setFileName(file_prueba.getAbsolutePath());
		parameters.setSRSID("EPSG:23030");
		return parameters;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#hasExplorer()
	 */
	public boolean hasExplorer() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean usesResources() {
		return true;
	}


}

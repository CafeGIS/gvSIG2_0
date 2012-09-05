/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
* AUTHORS (In addition to CIT):
* 2008 IVER T.I. S.A.   {{Task}}
*/

package org.gvsig.fmap.dal.store.dxf;

import java.io.File;

import org.cresques.ProjectionLibrary;
import org.cresques.impl.CresquesCtsLibrary;
import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.BaseTestFeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.index.spatial.jts.JTSIndexLibrary;
import org.gvsig.fmap.dal.store.dxf.legend.DXFLegendLibrary;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;

public class TestDXF extends BaseTestFeatureStore {

	protected boolean testDXFInitialized = false;

	public static final File file_prueba = new File(TestDXF.class.getResource(
			"data/prueba.dxf").getFile());


	protected void setUp() throws Exception {
		super.setUp();

		if (testDXFInitialized) {
			return;
		}


		DALFileLibrary libFile = new DALFileLibrary();
		libFile.initialize();

		DefaultGeometryLibrary defGeomLib = new DefaultGeometryLibrary();
		defGeomLib.initialize();

		ProjectionLibrary projLib = new ProjectionLibrary();
		projLib.initialize();

		CresquesCtsLibrary cresquesLib = new CresquesCtsLibrary();
		cresquesLib.initialize();

		DXFLibrary dxfLib = new DXFLibrary();
		dxfLib.initialize();

		DXFLegendLibrary dxfLegendLib = new DXFLegendLibrary();
		dxfLegendLib.initialize();

		JTSIndexLibrary jtsIndex = new JTSIndexLibrary();
		jtsIndex.initialize();

		defGeomLib.postInitialize();
		libFile.postInitialize();
		projLib.postInitialize();
		cresquesLib.postInitialize();
		dxfLib.postInitialize();
		dxfLegendLib.postInitialize();
		jtsIndex.postInitialize();


		testDXFInitialized = true;
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
		DXFStoreParameters dxfParameters = null;

		dxfParameters = (DXFStoreParameters) dataManager
				.createStoreParameters(DXFStoreProvider.NAME);

		dxfParameters.setFileName(file_prueba.getAbsolutePath());
		dxfParameters.setSRSID("EPSG:23030");
		return dxfParameters;
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


	public void testLegendAndLabeling() throws Exception {
		FeatureStore store = (FeatureStore) dataManager
				.createStore(getDefaultDataStoreParameters());

		assertNotNull(store.invokeDynMethod("getLegend", null));
		assertNotNull(store.invokeDynMethod("getLabeling", null));
		store.dispose();
	}
}

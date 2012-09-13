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
 * 2009 {Iver T.I.}   {Task}
 */

package org.gvsig.fmap.dal.store.gpe;

import java.io.File;

import org.gvsig.compat.se.SECompatLibrary;
import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.BaseTestEditableFeatureStore;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.gpe.GPELocator;
import org.gvsig.gpe.GPEManager;
import org.gvsig.gpe.gml.impl.DefaultGmlLibrary;
import org.gvsig.gpe.impl.DefaultGPELibrary;
import org.gvsig.gpe.kml.impl.DefaultKmlLibrary;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class GPETest extends BaseTestEditableFeatureStore {
	public static final File file_prueba = new File("testdata/GML-points.gml");
	//public static final File dbf_prueba_destino= new File(GPETest.class.getResource("data").getFile()+"/testTemp.gpe");

	private FilesystemServerExplorer myExplorer = null;
	private GPEManager gpeManager = GPELocator.getGPEManager();

	protected void setUp() throws Exception {
		super.setUp();

		DALFileLibrary libFile = new DALFileLibrary();
		libFile.initialize();
		libFile.postInitialize();

		DefaultGeometryLibrary geoLib = new DefaultGeometryLibrary();
		geoLib.initialize();
		geoLib.postInitialize();

		SECompatLibrary compatLibrary = new SECompatLibrary();
		compatLibrary.initialize();
		compatLibrary.postInitialize();

		DALGPELibrary dalLib = new DALGPELibrary();
		dalLib.initialize();
		dalLib.postInitialize();

		DefaultGPELibrary lib = new DefaultGPELibrary();
		lib.initialize();
		lib.postInitialize();

		DefaultGmlLibrary gmlLib = new DefaultGmlLibrary();
		gmlLib.initialize();
		gmlLib.postInitialize();

		DefaultKmlLibrary kmlLib = new DefaultKmlLibrary();
		kmlLib.initialize();
		kmlLib.postInitialize();
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
		GPEStoreParameters parameters = null;

		parameters = (GPEStoreParameters) dataManager
		.createStoreParameters(GPEStoreProvider.NAME);

		parameters.setFileName(file_prueba.getAbsolutePath());
		return parameters;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#hasExplorer()
	 */
	public boolean hasExplorer() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.BaseTestEditableFeatureStore#getDefaultNewDataStoreParameters()
	 */
	public NewFeatureStoreParameters getDefaultNewDataStoreParameters()
	throws Exception {
		return null;
//		if (this.myExplorer == null) {
//			GPEStoreParameters gpeStoreParameters = (GPEStoreParameters) this
//			.getDefaultDataStoreParameters();
//			FeatureStore store = (FeatureStore) dataManager
//			.createStore(gpeStoreParameters);
//			myExplorer = (FilesystemServerExplorer) store.getExplorer();
//			store.dispose();
//		}
//
//		return (NewFeatureStoreParameters) myExplorer
//		.getAddParameters(dbf_prueba_destino);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.BaseTestEditableFeatureStore#usesResources()
	 */
	public boolean usesResources() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean resourcesNotifyChanges() {
		return true;
	}


}


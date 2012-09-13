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
 
package org.gvsig.fmap.dal.store.wfs;

import java.io.File;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.BaseTestFeatureStore;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.gpe.GPELocator;
import org.gvsig.gpe.GPEManager;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WFSTest extends BaseTestFeatureStore {
	private String url = "http://inspire.cop.gva.es/geoserver/wfs";
	private String version = "1.0.0";
	private String featureType = "comunic_lin_300k";
	private String namespace = "http://inspire.cop.gva.es";
	private String namespacePrefix = "cit";	

	private String parsersFile = "config" + File.separatorChar + "parser.properties";	
	private GPEManager gpeManager = GPELocator.getGPEManager();
	
	protected void setUp() throws Exception {
		super.setUp();
			
		gpeManager.addParsersFile(new File(parsersFile));
		
		DALFileLibrary libFile = new DALFileLibrary();
		libFile.initialize();
		libFile.postInitialize();

		DefaultGeometryLibrary geoLib = new DefaultGeometryLibrary();
		geoLib.initialize();
		geoLib.postInitialize();
		
//		GPELibraryTest lib = new GPELibraryTest();
//		lib.initialize();
//		lib.postInitialize();
//		
//		GmlLibrary gmlLib = new GmlLibrary();
//		gmlLib.initialize();
//		gmlLib.postInitialize();
//		
//		XMLLibrary xmlLibrary = new XMLLibrary();
//		xmlLibrary.initialize();
//		xmlLibrary.postInitialize();
		
		WFSLibrary wfsLib = new WFSLibrary();
		wfsLib.initialize();
		wfsLib.postInitialize();
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
		WFSStoreParameters parameters = null;

		parameters = (WFSStoreParameters) dataManager
				.createStoreParameters(WFSStoreProvider.NAME);

		parameters.setUrl(url);
		parameters.setFeatureType(namespacePrefix, namespace, featureType);
		parameters.setVersion(version);
		//parameters.setFields(fields);
		return parameters;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#hasExplorer()
	 */
	public boolean hasExplorer() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#usesResources()
	 */
	@Override
	public boolean usesResources() {
		// TODO Auto-generated method stub
		return false;
	}


}



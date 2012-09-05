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
* 2009 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.store.jdbc;

import org.cresques.ProjectionLibrary;
import org.cresques.impl.CresquesCtsLibrary;
import org.gvsig.fmap.dal.DALDbLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.BaseTestFeatureStore;
import org.gvsig.fmap.dal.index.spatial.jts.JTSIndexLibrary;
import org.gvsig.fmap.dal.store.db.DBStoreLibrary;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;

import com.mysql.jdbc.Driver;

/**
 * @author jmvivo
 *
 */
public class JDBCTest extends BaseTestFeatureStore {

	protected void setUp() throws Exception {
		super.setUp();
		ToolsLocator.registerDefaultPersistenceManager(XMLEntityManager.class);

		DefaultGeometryLibrary defGeomLib = new DefaultGeometryLibrary();
		defGeomLib.initialize();

		ProjectionLibrary projLib = new ProjectionLibrary();
		projLib.initialize();

		CresquesCtsLibrary cresquesLib = new CresquesCtsLibrary();
		cresquesLib.initialize();

		DALDbLibrary libDb = new DALDbLibrary();
		libDb.initialize();

		DBStoreLibrary libDbStore = new DBStoreLibrary();
		libDbStore.initialize();

		JDBCLibrary libJDBC = new JDBCLibrary();
		libJDBC.initialize();

		JTSIndexLibrary jtsIndex = new JTSIndexLibrary();
		jtsIndex.initialize();

		projLib.postInitialize();
		cresquesLib.postInitialize();
		defGeomLib.postInitialize();
		libDb.postInitialize();
		libDbStore.postInitialize();
		libJDBC.postInitialize();
		jtsIndex.postInitialize();
	}

	public DataStoreParameters getDefaultDataStoreParameters()
			throws DataException {
		JDBCStoreParameters parameters = null;
		parameters = (JDBCStoreParameters) dataManager
				.createStoreParameters(JDBCStoreProvider.NAME);


		parameters.setHost("localhost");
		parameters.setPort(3306);
		parameters.setUser("test");
		parameters.setPassword("test");
		parameters.setDBName("gis");
		parameters.setTable("alfanum");
		parameters.setUrl("jdbc:mysql://" + parameters.getHost() + ":"
				+ parameters.getPort() + "/" + parameters.getDBName());
		parameters.setJDBCDriverClassName(Driver.class.getName());

		return parameters;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#hasExplorer()
	 */
	@Override
	public boolean hasExplorer() {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#usesResources()
	 */
	@Override
	public boolean usesResources() {
		// TODO Auto-generated method stub
		return true;
	}

}

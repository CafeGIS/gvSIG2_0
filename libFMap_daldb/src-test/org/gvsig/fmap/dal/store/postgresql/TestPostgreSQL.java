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
package org.gvsig.fmap.dal.store.postgresql;

import org.cresques.ProjectionLibrary;
import org.cresques.impl.CresquesCtsLibrary;
import org.gvsig.fmap.dal.DALDbLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.BaseTestFeatureStore;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.index.spatial.jts.JTSIndexLibrary;
import org.gvsig.fmap.dal.store.db.DBStoreLibrary;
import org.gvsig.fmap.dal.store.jdbc.JDBCLibrary;
import org.gvsig.fmap.dal.store.postgresql.PostgreSQLLibrary;
import org.gvsig.fmap.dal.store.postgresql.PostgreSQLStoreParameters;
import org.gvsig.fmap.dal.store.postgresql.PostgreSQLStoreProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;
import org.gvsig.tools.evaluator.EvaluatorFieldsInfo;
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;



/**
 * @author jmvivo
 *
 */
// public class TestPostgreSQL extends BaseTestEditableFeatureStore {
public class TestPostgreSQL extends BaseTestFeatureStore {


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

		PostgreSQLLibrary pgLib = new PostgreSQLLibrary();
		pgLib.initialize();

		JTSIndexLibrary jtsIndex = new JTSIndexLibrary();
		jtsIndex.initialize();

		projLib.postInitialize();
		cresquesLib.postInitialize();
		defGeomLib.postInitialize();
		libDb.postInitialize();
		libDbStore.postInitialize();
		libJDBC.postInitialize();
		pgLib.postInitialize();
		jtsIndex.postInitialize();
	}

	public DataStoreParameters getDefaultDataStoreParameters()
			throws DataException {
		PostgreSQLStoreParameters pgParameters = null;
		pgParameters = (PostgreSQLStoreParameters) dataManager
				.createStoreParameters(PostgreSQLStoreProvider.NAME);

		pgParameters.setHost("localhost");
		pgParameters.setUser("postgres");
		pgParameters.setPassword("postgres");
		pgParameters.setDBName("gis");
		pgParameters.setTable("muni10000_peq");

		return pgParameters;
	}

	public boolean hasExplorer() {
		return true;
	}

	public void testLoadMetadata() throws Exception {
		DataStoreParameters params = this.getDefaultDataStoreParameters();

		FeatureStore store = null;
		store = (FeatureStore) dataManager.createStore(params);
		FeatureType fType = store.getDefaultFeatureType();
		FeatureAttributeDescriptor geomAttr;



		if (fType.getDefaultGeometryAttributeIndex() >= 0) {
			assertNotNull(store.getEnvelope());
			geomAttr = fType.getAttributeDescriptor(fType
					.getDefaultGeometryAttributeIndex());
			assertTrue(geomAttr.getGeometryType() == Geometry.TYPES.MULTISURFACE);
			assertTrue(geomAttr.getGeometrySubType() == Geometry.SUBTYPES.GEOM2D);
			assertNotNull(store.getDynValue("DefaultSRS"));

		}

	}

	public void testCloserConnection() throws Exception {

		DataStoreParameters params = this.getDefaultDataStoreParameters();

		FeatureStore store = null;
		store = (FeatureStore) dataManager.createStore(params);

		FeatureQuery query = store.createFeatureQuery();

		query.getOrder().add("gid", true);

		query.setFilter(new Evaluator() {

			public Object evaluate(EvaluatorData data)
					throws EvaluatorException {
				// TODO Auto-generated method stub
				return Boolean.TRUE;
			}

			public String getCQL() {
				return "true = true";
			}

			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}

			public String getName() {
				return "AlwaysTrue";
			}

			public EvaluatorFieldsInfo getFieldsInfo() {
				// TODO Auto-generated method stub
				return null;
			}

		});

		FeatureStoreTransform transform = new StringsToLowerTransform();
		transform.setFeatureStore(store);

		store.getTransforms().add(transform);

		transform = new StringsToLowerTransform();
		transform.setFeatureStore(store);

		store.getTransforms().add(transform);

		transform = new StringsToLowerTransform();
		transform.setFeatureStore(store);

		store.getTransforms().add(transform);

		for (int i = 0; i < 30; i++) {
			// this.fullStoreIteratorTest(store);

			this.testIterationFastAndStandart(store, query);
		}


		store.dispose();

	}

	public boolean usesResources() {
		return true;
	}

	public NewFeatureStoreParameters getDefaultNewDataStoreParameters()
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

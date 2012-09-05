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

import java.util.Iterator;

import org.cresques.ProjectionLibrary;
import org.cresques.impl.CresquesCtsLibrary;
import org.gvsig.fmap.dal.DALDbLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.BaseTestEditableFeatureStore;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.index.spatial.jts.JTSIndexLibrary;
import org.gvsig.fmap.dal.store.db.DBStoreLibrary;
import org.gvsig.fmap.dal.store.jdbc.JDBCLibrary;
import org.gvsig.fmap.dal.store.jdbc.JDBCServerExplorer;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.fmap.geom.operation.towkt.ToWKT;
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
public class TestPostgreSQLWrite extends BaseTestEditableFeatureStore {


	private JDBCServerExplorer myExplorer;
	private PostgreSQLNewStoreParameters newParams;

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

		// Initialize wkt geom operation
		int wktCode = ToWKT.CODE;

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
		PostgreSQLStoreParameters parameters = (PostgreSQLStoreParameters) this
				.getDefaultDataStoreParameters();

		FeatureStore store = null;

		if (this.myExplorer == null) {
			store = (FeatureStore) dataManager.createStore(parameters);
			myExplorer = (JDBCServerExplorer) store.getExplorer();
		}
		if (this.newParams == null) {
			if (store == null){
				store = (FeatureStore) dataManager.createStore(parameters);
			}

			newParams = (PostgreSQLNewStoreParameters) myExplorer
					.getAddParameters();

			newParams.setTable(parameters.getTable() + "_test");
			FeatureType ftOrg = store.getDefaultFeatureType();
			EditableFeatureType ftTrg = (EditableFeatureType) newParams
					.getDefaultFeatureType();
			FeatureAttributeDescriptor org;
			EditableFeatureAttributeDescriptor trg;
			Iterator iter = ftOrg.iterator();
			while (iter.hasNext()) {
				org = (FeatureAttributeDescriptor) iter.next();
				trg = ftTrg.add(org.getName(), org.getDataType());
				trg.setAllowNull(org.allowNull());
				trg.setDefaultValue(org.getDefaultValue());
				trg.setGeometrySubType(org.getGeometrySubType());
				trg.setGeometryType(org.getGeometryType());
				trg.setIsAutomatic(org.isAutomatic());
				trg.setIsPrimaryKey(org.isPrimaryKey());
				trg.setIsReadOnly(org.isReadOnly());
				trg.setMaximumOccurrences(org.getMaximumOccurrences());
				trg.setMinimumOccurrences(org.getMinimumOccurrences());
				trg.setPrecision(org.getPrecision());
				trg.setSize(org.getSize());
				trg.setSRS(org.getSRS());
			}
			ftTrg.setDefaultGeometryAttributeName(ftOrg
					.getDefaultGeometryAttributeName());
			ftTrg.setHasOID(ftOrg.hasOID());

		}
		if (store != null){
			store.dispose();
		}

		return this.newParams;
	}

	public boolean resourcesNotifyChanges() {
		return false;
	}
}

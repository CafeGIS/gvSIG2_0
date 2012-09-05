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

package org.gvsig.fmap.dal.store.mysql;

import org.cresques.ProjectionLibrary;
import org.cresques.impl.CresquesCtsLibrary;
import org.gvsig.fmap.dal.DALDbLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.BaseTestEditableFeatureStore;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.index.spatial.jts.JTSIndexLibrary;
import org.gvsig.fmap.dal.store.db.DBStoreLibrary;
import org.gvsig.fmap.dal.store.jdbc.JDBCLibrary;
import org.gvsig.fmap.dal.store.jdbc.JDBCStoreParameters;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;


public class MySQLTestWrite extends BaseTestEditableFeatureStore {
	private MySQLNewStoreParameters newParams;
	private MySQLServerExplorer myExplorer;


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

		MySQLLibrary libMySQL = new MySQLLibrary();
		libMySQL.initialize();

		JTSIndexLibrary jtsIndex = new JTSIndexLibrary();
		jtsIndex.initialize();

		projLib.postInitialize();
		cresquesLib.postInitialize();
		defGeomLib.postInitialize();
		libDb.postInitialize();
		libDbStore.postInitialize();
		libJDBC.postInitialize();
		libMySQL.postInitialize();
		jtsIndex.postInitialize();
	}

	public DataStoreParameters getDefaultDataStoreParameters()
			throws DataException {
		MySQLStoreParameters parameters = null;
		parameters = (MySQLStoreParameters) dataManager
				.createStoreParameters(MySQLStoreProvider.NAME);

		parameters.setHost("localhost");
		parameters.setPort(3306);
		parameters.setUser("test");
		parameters.setPassword("test");
		parameters.setDBName("gis");
		// parameters.setTable("alfanum_autoid");
		parameters.setTable("comunic_lin_300k");

		return parameters;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#hasExplorer()
	 */
	@Override
	public boolean hasExplorer() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#usesResources()
	 */
	@Override
	public boolean usesResources() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	public void testImportDB() throws Exception {
		JDBCNewStoreParameters newParasm = (JDBCNewStoreParameters) getDefaultNewDataStoreParameters();
		JDBCServerExplorerParameters seParams = (JDBCServerExplorerParameters) dataManager
				.createServerExplorerParameters(JDBCServerExplorer.NAME);

		assertTrue(myExplorer.add(newParasm, true));

		FeatureStore store = (FeatureStore) dataManager
				.createStore(getDefaultDataStoreParameters());

		FeatureStore newstore = (FeatureStore) dataManager
				.createStore(newParasm);



		newstore.edit(FeatureStore.MODE_APPEND);
		FeatureSet set = store.getFeatureSet();
		DisposableIterator iter = set.iterator();
		Feature org;
		EditableFeature trg;
		Iterator attrIter;
		FeatureAttributeDescriptor attr;
		while (iter.hasNext()) {
			org = (Feature) iter.next();
			trg = newstore.createNewFeature(false);
			attrIter = trg.getType().iterator();
			while (attrIter.hasNext()) {
				attr = (FeatureAttributeDescriptor) attrIter.next();
				trg.set(attr.getName(), org.get(attr.getName()));
			}
			newstore.insert(trg);
		}
		newstore.finishEditing();

		iter.dispose();
		set.dispose();
		store.dispose();
		newstore.dispose();


	}
	*/

	@Override
	public NewFeatureStoreParameters getDefaultNewDataStoreParameters()
			throws Exception {


		JDBCStoreParameters params = (JDBCStoreParameters) getDefaultDataStoreParameters();

		if (this.myExplorer == null) {

			MySQLServerExplorerParameters seParameters = null;
			seParameters = (MySQLServerExplorerParameters) dataManager
					.createServerExplorerParameters(MySQLServerExplorer.NAME);

			seParameters.setHost("localhost");
			seParameters.setPort(3306);
			seParameters.setUser("test");
			seParameters.setPassword("test");
			seParameters.setDBName("gis");


			myExplorer = (MySQLServerExplorer) dataManager
					.createServerExplorer(seParameters);
		}
		if (this.newParams == null) {
			FeatureStore store = (FeatureStore) dataManager.createStore(params);

			newParams = (MySQLNewStoreParameters) myExplorer
					.getAddParameters();


			newParams.setTable(params.getTable() + "_test");
			FeatureType ftOrg = store.getDefaultFeatureType();
			EditableFeatureType ftTrg = ftOrg.getEditable();

//			EditableFeatureType ftTrg = (EditableFeatureType) newParams
//					.getDefaultFeatureType();
			newParams.setDefaultFeatureType(ftTrg);

//			FeatureAttributeDescriptor org;
//			EditableFeatureAttributeDescriptor trg;
//			Iterator iter = ftOrg.iterator();
//			while (iter.hasNext()) {
//				org = (FeatureAttributeDescriptor) iter.next();
//				trg = ftTrg.add(org.getName(), org.getDataType());
//				trg.setAllowNull(org.allowNull());
//				trg.setDefaultValue(org.getDefaultValue());
//				trg.setGeometrySubType(org.getGeometrySubType());
//				trg.setGeometryType(org.getGeometryType());
//				trg.setIsAutomatic(org.isAutomatic());
//				trg.setIsPrimaryKey(org.isPrimaryKey());
//				trg.setIsReadOnly(org.isReadOnly());
//				trg.setMaximumOccurrences(org.getMaximumOccurrences());
//				trg.setMinimumOccurrences(org.getMinimumOccurrences());
//				trg.setPrecision(org.getPrecision());
//				trg.setSize(org.getSize());
//				trg.setSRS(org.getSRS());
//				trg.setAdditionalInfo("SQLType", org
//						.getAdditionalInfo("SQLType"));
//				trg.setAdditionalInfo("SQLTypeName", org
//						.getAdditionalInfo("SQLTypeName"));
//
//			}
//			ftTrg.setDefaultGeometryAttributeName(ftOrg
//					.getDefaultGeometryAttributeName());
//			ftTrg.setHasOID(ftOrg.hasOID());

			store.dispose();
		}

		return this.newParams;
	}

	@Override
	public boolean resourcesNotifyChanges() {
		// TODO Auto-generated method stub
		return false;
	}

}

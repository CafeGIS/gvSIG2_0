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

package org.gvsig.fmap.dal.store.dbf;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.BaseTestEditableFeatureStore;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;

public class TestDBF extends BaseTestEditableFeatureStore {

	protected boolean testDBFInitialized = false;


	public static final File dbf_prueba = new File(TestDBF.class.getResource(
			"data/prueba.dbf").getFile());
	public static final File dbf_prueba_destino= new File(TestDBF.class.getResource(
			"data").getFile()+"/pruebaTemp.dbf");
	public static final File dbf_pruebaNull = new File(TestDBF.class
			.getResource("data/pruebaNull.dbf").getFile());

	private FilesystemServerExplorer myExplorer = null;

	public TestDBF() {
		super();
	}

	/**
	 * @param dataManager
	 */
	public TestDBF(boolean initializeBase, boolean initializeDBF) {
		this.baseTestInitialized = !initializeBase;
		this.testDBFInitialized = !initializeDBF;
	}

	protected void setUp() throws Exception {
		super.setUp();
		if (testDBFInitialized) {
			return;
		}
		ToolsLocator.registerDefaultPersistenceManager(XMLEntityManager.class);

		DALFileLibrary libFile = new DALFileLibrary();
		libFile.initialize();

		DBFLibrary dbfLib = new DBFLibrary();
		dbfLib.initialize();

		libFile.postInitialize();
		dbfLib.postInitialize();
		testDBFInitialized = true;
	}


	public DataStoreParameters getDefaultDataStoreParameters() throws DataException {
		DBFStoreParameters dbfParameters = null;
		dbfParameters = (DBFStoreParameters) dataManager
				.createStoreParameters(DBFStoreProvider.NAME);

		dbfParameters.setFile(dbf_prueba);

		return dbfParameters;
	}

	public void testExplorerList() throws Exception {
		this.testExplorerList(2);
	}

	public void testJoin() throws Exception {
		DBFStoreParameters dbfParameters = null;
		FeatureStore store1, store2;

		dbfParameters = (DBFStoreParameters) dataManager
				.createStoreParameters(DBFStoreProvider.NAME);

		dbfParameters.setFile(dbf_prueba);

		store1 = (FeatureStore) dataManager.createStore(dbfParameters);
		store2 = (FeatureStore) dataManager.createStore(dbfParameters);

		JoinTransform join = new JoinTransform();

		ArrayList names = new ArrayList();// <String>
		Iterator iter = store2.getDefaultFeatureType().iterator();// <FeatureAttributeDescriptor>
		while (iter.hasNext()) {
			names.add(((FeatureAttributeDescriptor) iter.next()).getName());
		}

		join.initialize(store1, store2, "NOMBRE", "NOMBRE", null, null,
				(String[]) names.toArray(new String[0]));
		store1.getTransforms().add(join);

		this.testSimpleIteration(store1);

		FeatureQuery query = store1.createFeatureQuery();
		FeatureType ftFinal = store1.getDefaultFeatureType();
		String[] queryNames = new String[] {
				ftFinal.getAttributeDescriptor(0).getName(),
				ftFinal.getAttributeDescriptor(1).getName(),
				ftFinal.getAttributeDescriptor(ftFinal.size() - 1).getName() };
		query.setAttributeNames(queryNames);

		this.testSimpleIteration(store1, query);

		queryNames = new String[] {
				ftFinal.getAttributeDescriptor(ftFinal.size() - 1).getName(),
				ftFinal.getAttributeDescriptor(1).getName(),
				ftFinal.getAttributeDescriptor(0).getName(),
				ftFinal.getAttributeDescriptor(ftFinal.size() - 2).getName() };

		query.setAttributeNames(queryNames);
		this.testSimpleIteration(store1, query);

		store1.dispose();
		store2.dispose();




	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.BaseTestFeatureStore#hasExplorer()
	 */
	public boolean hasExplorer() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.gvsig.fmap.dal.feature.BaseTestEditableFeatureStore#
	 * getDefaultNewDataStoreParameters()
	 */
	public NewFeatureStoreParameters getDefaultNewDataStoreParameters()
			throws Exception {
		if (this.myExplorer == null) {
			DBFStoreParameters dbfParameters = (DBFStoreParameters) this
					.getDefaultDataStoreParameters();
			FeatureStore store = (FeatureStore) dataManager
					.createStore(dbfParameters);
			myExplorer = (FilesystemServerExplorer) store.getExplorer();
			store.dispose();
		}

		return (NewFeatureStoreParameters) myExplorer
				.getAddParameters(dbf_prueba_destino);
	}

	public boolean usesResources() {
		return true;
	}

	@Override
	public boolean resourcesNotifyChanges() {
		return true;
	}

}

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

package org.gvsig.fmap.dal.store.shp;

import java.io.File;
import java.util.Iterator;

import org.cresques.ProjectionLibrary;
import org.cresques.impl.CresquesCtsLibrary;
import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.BaseTestEditableFeatureStore;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.index.spatial.jts.JTSIndexLibrary;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreProvider;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;

public class TestSHP extends BaseTestEditableFeatureStore {

	protected boolean testSHPInitialized = false;


	private FilesystemServerExplorer myExplorer;


	public static final File file_prueba = new File(TestSHP.class.getResource(
			"data/prueba.shp").getFile());
	public static final File file_prueba_destino = new File(file_prueba
			.getParent()
			+ File.separatorChar + "pruebaTemp.shp");
	public static final File file_pruebaNull = new File(TestSHP.class
			.getResource("data/pruebaNull.shp").getFile());

	public static final File file_poly_valencia = new File(TestSHP.class
			.getResource("data/poly-valencia.shp").getFile());


	protected void setUp() throws Exception {
		super.setUp();

		if (testSHPInitialized) {
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

		SHPLibrary shpLib = new SHPLibrary();
		shpLib.initialize();


		JTSIndexLibrary jtsIndex = new JTSIndexLibrary();
		jtsIndex.initialize();

		defGeomLib.postInitialize();
		projLib.postInitialize();
		cresquesLib.postInitialize();
		libFile.postInitialize();
		shpLib.postInitialize();
		jtsIndex.postInitialize();

		testSHPInitialized = true;


	}

	public void testSimpleIteration() throws Exception {

		FeatureStore store = null;
		SHPStoreParameters shpParameters = null;

		shpParameters = (SHPStoreParameters) dataManager
				.createStoreParameters(SHPStoreProvider.NAME);

		// shpParameters.setFile(file_poly_valencia);
		shpParameters.setFile(file_prueba);

		shpParameters.setSRSID("EPSG:23030");

		store = (FeatureStore) dataManager.createStore(shpParameters);
		FeatureSet set;
		FeatureType type = store.getDefaultFeatureType();
		set = store.getFeatureSet();

		System.out.println("Num:" + set.getSize());
		DisposableIterator it = set.iterator();
		Iterator ftIt;
		FeatureAttributeDescriptor desc;

		int i = 0;
		Feature feature;
		while (it.hasNext()) {
			ftIt = type.iterator();

			feature = (Feature) it.next();
			// while (ftIt.hasNext()) {
			// desc = (FeatureAttributeDescriptor) ftIt.next();
			// System.out.println(desc.getName() + ":"
			// + feature.get(desc.getIndex()));
			//
			// }
			// System.out.println(feature.get("NOMBRE"));
			System.out.print(feature.getDefaultEnvelope() + "\t");
			// System.out.print(feature.getDefaultGeometry() + "\t");
			// System.out.println(feature.get("NOMBRE"));
			System.out.println("row:" + i);
			i++;


		}

		it.dispose();
		set.dispose();

		store.dispose();

	}

	public void testEditing(Object x) throws Exception {
		FeatureStore store = null;
		SHPStoreParameters shpParameters = null;

		shpParameters = (SHPStoreParameters) dataManager
				.createStoreParameters(SHPStoreProvider.NAME);

		shpParameters.setFile(file_poly_valencia);

		store = (FeatureStore) dataManager.createStore(shpParameters);
		FeatureSet set;
		FeatureType type = store.getDefaultFeatureType();
		System.err.println("Antes de la edición");
		set = store.getFeatureSet();
		System.out.println("Num:" + set.getSize());
		DisposableIterator it = set.iterator();
		Iterator ftIt;
		FeatureAttributeDescriptor desc;

		int i = 0;
		Feature feature;
		while (it.hasNext()) {
			ftIt = type.iterator();

			feature = (Feature) it.next();
			// while (ftIt.hasNext()) {
			// desc = (FeatureAttributeDescriptor) ftIt.next();
			// System.out.println(desc.getName() + ":"
			// + feature.get(desc.getIndex()));
			//
			// }
			// System.out.println(feature.get("NOMBRE"));
			System.out.print(feature.getDefaultEnvelope() + "\t");
			// System.out.print(feature.getDefaultGeometry() + "\t");
			// System.out.println(feature.get("NOMBRE"));
			System.out.println("row:" + i);
			i++;


		}

		it.dispose();
		set.dispose();
		store.edit();
		EditableFeature ef = store.createNewFeature();
		store.insert(ef);
		store.finishEditing();
		System.err.println("Después de la edición");
		set = store.getFeatureSet();
		System.out.println("Num:" + set.getSize());
		it = set.iterator();

		i = 0;
		while (it.hasNext()) {
			ftIt = type.iterator();

			feature = (Feature) it.next();
			// while (ftIt.hasNext()) {
			// desc = (FeatureAttributeDescriptor) ftIt.next();
			// System.out.println(desc.getName() + ":"
			// + feature.get(desc.getIndex()));
			//
			// }
			// System.out.println(feature.get("NOMBRE"));
			System.out.print(feature.getDefaultEnvelope() + "\t");
			// System.out.print(feature.getDefaultGeometry() + "\t");
			// System.out.println(feature.get("NOMBRE"));
			System.out.println("row:" + i);
			i++;


		}

		it.dispose();
		set.dispose();

		store.dispose();

	}

	public void testExport(Object x) throws Exception {
		DBFStoreParameters dbfParameters = null;

		dbfParameters = (DBFStoreParameters) dataManager
				.createStoreParameters(DBFStoreProvider.NAME);

		dbfParameters.setFile(file_prueba);

		FeatureStore store = (FeatureStore) dataManager
				.createStore(dbfParameters);
		FilesystemServerExplorerParameters explorerParams = (FilesystemServerExplorerParameters) dataManager
				.createServerExplorerParameters(FilesystemServerExplorerParameters.DYNCLASS_NAME);
		explorerParams.setRoot(file_prueba.getParent());

		FilesystemServerExplorer explorer = (FilesystemServerExplorer) dataManager
				.createServerExplorer(explorerParams);

		NewFeatureStoreParameters newParams = (NewFeatureStoreParameters) explorer
				.getAddParameters(file_prueba_destino);

		store.export(explorer, newParams);

		FeatureStore result = (FeatureStore) dataManager.createStore(newParams);

		FeatureSet set = result.getFeatureSet();
		FeatureSet originalSet = store.getFeatureSet();
		assertEquals(set.getSize(), originalSet.getSize());

		DisposableIterator originalIter = originalSet.iterator();
		DisposableIterator iter = set.iterator();
		while (iter.hasNext()) {
			assertEquals(originalIter.next(), iter.next());
		}

		originalIter.dispose();
		iter.dispose();
		result.dispose();
		explorer.remove(newParams);

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
		SHPStoreParameters shpParameters = (SHPStoreParameters) dataManager
				.createStoreParameters(SHPStoreProvider.NAME);

		//			shpParameters.setFile(file_poly_valencia);
		shpParameters.setFile(file_prueba);
		shpParameters.setSRSID("EPSG:23030");
		return shpParameters;
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
		SHPStoreParameters parameters = (SHPStoreParameters) this
				.getDefaultDataStoreParameters();

		if (this.myExplorer == null) {
			FeatureStore store = (FeatureStore) dataManager
					.createStore(parameters);
			myExplorer = (FilesystemServerExplorer) store.getExplorer();
			store.dispose();
		}

		SHPNewStoreParameters result = (SHPNewStoreParameters) myExplorer
				.getAddParameters(file_prueba_destino);

		result.setSRS(parameters.getSRS());
		return result;
	}

	public boolean usesResources() {
		return true;
	}

	public boolean resourcesNotifyChanges() {
		return true;
	}

}

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

package org.gvsig.fmap.dal.explorer.filesystem;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreProvider;
import org.gvsig.fmap.dal.store.dbf.TestDBF;

public class FileSystemExplorerTest extends TestCase {

	private static DataManager dataManager = null;
	public static final File dbf_data = new File(TestDBF.class.getResource(
			"data/prueba.dbf").getFile()).getParentFile();


	protected void setUp() throws Exception {
		super.setUp();

		if (dataManager != null) {
			return;
		}
		DALLibrary lib = new DALLibrary();
		lib.initialize();
		lib.postInitialize();

		DALFileLibrary libFile = new DALFileLibrary();
		libFile.initialize();
		libFile.postInitialize();

		dataManager = DALLocator.getDataManager();

	}

	public void testList() throws Exception {
		FilesystemServerExplorerParameters exParam = (FilesystemServerExplorerParameters) dataManager
				.createServerExplorerParameters(FilesystemServerExplorer.NAME);

		exParam.setRoot(dbf_data.getPath());

		DataServerExplorer ex = dataManager.createServerExplorer(exParam);

		List list = ex.list();

		assertEquals(2, list.size());

		if (((FilesystemStoreParameters) list.get(0)).getFile().getPath()
				.equals(TestDBF.dbf_prueba.getPath())) {
			assertEquals(((FilesystemStoreParameters) list.get(1)).getFile()
					.getPath(), TestDBF.dbf_pruebaNull.getPath());

		} else {
			assertEquals(((FilesystemStoreParameters) list.get(0)).getFile()
					.getPath(), TestDBF.dbf_pruebaNull.getPath());
			assertEquals(((FilesystemStoreParameters) list.get(1)).getFile()
					.getPath(), TestDBF.dbf_prueba.getPath());
		}

		Iterator iter = list.iterator();
		TestDBF testDBF = new TestDBF(false, true);
		while (iter.hasNext()) {
			testDBF.testSimpleIteration((DBFStoreParameters) iter.next());
		}



	}

	private boolean deleteDir(File dir) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				if (!deleteDir(files[i])) {
					return false;
				}
			}
			if (!files[i].delete()) {
				return false;
			}
		}
		return dir.delete();
	}

	public void testAddRemove() throws Exception {
		File tmpDir = new File("_FilesystemExplorer.tmp");

		if (tmpDir.exists()) {
			if (!deleteDir(tmpDir)) {
				fail("Clean tmp dir:" + tmpDir.getAbsolutePath());
			}
		}

		tmpDir.mkdir();

		FilesystemServerExplorerParameters exParam = (FilesystemServerExplorerParameters) dataManager
				.createServerExplorerParameters(FilesystemServerExplorer.NAME);

		exParam.setRoot(tmpDir.getAbsolutePath());

		DataServerExplorer ex = dataManager.createServerExplorer(exParam);

		assertTrue(ex.canAdd());

		assertTrue(ex.canAdd(DBFStoreProvider.NAME));

		assertEquals(0, ex.list().size());

		NewDataStoreParameters newParams = ex
				.getAddParameters(DBFStoreProvider.NAME);

		((FilesystemStoreParameters) newParams).setFile(new File(tmpDir,
				"tmp1.dbf"));
		EditableFeatureType fType;
		if (((NewFeatureStoreParameters) newParams).getDefaultFeatureType() instanceof EditableFeatureType) {
			fType = (EditableFeatureType) ((NewFeatureStoreParameters) newParams)
					.getDefaultFeatureType();
		} else {
			fType = ((NewFeatureStoreParameters) newParams)
					.getDefaultFeatureType().getEditable();
		}
		fType.add("campo1", DataTypes.STRING).setSize(10);
		((NewFeatureStoreParameters) newParams).setDefaultFeatureType(fType);
		ex.add(newParams, false);

		List list = ex.list();
		assertEquals(1, list.size());

		Iterator iter = list.iterator();
		TestDBF testDBF = new TestDBF(false, true);
		while (iter.hasNext()) {
			testDBF.testSimpleIteration((DBFStoreParameters) iter.next());
		}

		ex.remove((DataStoreParameters) list.get(0));

		assertEquals(0, ex.list().size());


	}
}

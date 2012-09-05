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
 * 2008 Prodevelop S.L  main development
 */

package org.gvsig.geocoding.update;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import junit.framework.TestCase;

import org.cresques.impl.CresquesCtsLibrary;
import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.index.spatial.jts.JTSIndexLibrary;
import org.gvsig.fmap.dal.index.spatial.spatialindex.SPTLIBIndexLibrary;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPStoreProvider;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.locator.Library;
import org.gvsig.tools.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class TestUpdateFeature extends TestCase {

	private static final Logger log = LoggerFactory
			.getLogger(TestUpdateFeature.class);

	private DataManager dataManager = null;
	private FeatureStore streetsStore = null;

	/**
	 * Constructor
	 */
	public TestUpdateFeature() {

		try {

			DefaultGeometryLibrary defGeomLib = new DefaultGeometryLibrary();
			defGeomLib.initialize();
			defGeomLib.postInitialize();

			ToolsLibrary tools = new ToolsLibrary();
			tools.initialize();
			tools.postInitialize();

			Library dlib = new DALLibrary();
			dlib.initialize();
			dlib.postInitialize();

			Library libFile = new DALFileLibrary();
			libFile.initialize();
			libFile.postInitialize();

			Library shpLib = new SHPLibrary();
			shpLib.initialize();
			shpLib.postInitialize();

			Library proj = new CresquesCtsLibrary();
			proj.initialize();
			proj.postInitialize();
			
			Library index = new SPTLIBIndexLibrary();
			index.initialize();
			index.postInitialize();
			
			Library indexjts = new JTSIndexLibrary();
			indexjts.initialize();
			indexjts.postInitialize();

		} catch (Exception e) {
			e.printStackTrace();
		}

		dataManager = DALLocator.getDataManager();

		try {
			// streets file
			File streets = new File("./test-data/geocoder/streets.shp");
			assertTrue(streets.exists());
			SHPStoreParameters strParam = (SHPStoreParameters) dataManager
					.createStoreParameters(SHPStoreProvider.NAME);
			strParam.setFile(streets);
			strParam.setSRSID("EPSG:23030");
			streetsStore = (FeatureStore) dataManager.createStore(strParam);
			assertNotNull(streetsStore);

		} catch (Exception e) {
			log.error("Getting streets.shp store", e);
		}

	}

	/**
	 * setUP
	 */
	public void setUp() {

	}

	/**
	 * tearDown
	 */
	public void tearDown() {

	}

	/**
	 * geocoding Massive
	 * 
	 * @throws IOException
	 * @throws PersistenceException
	 * @throws DataException
	 * 
	 * @throws DataException
	 */
	public void testUpdateFeature() throws IOException, PersistenceException,
			DataException {
		
		streetsStore.edit(FeatureStore.MODE_FULLEDIT);

		FeatureSet selfeats = (FeatureSet) streetsStore.getDataSet();		
		int size = (int)selfeats.getSize();
		if (size > 0) {
			Iterator<Feature> it = selfeats.iterator(0);
			EditableFeature shpfeat = (EditableFeature) it.next().getEditable();
			shpfeat.set("MUNI", "yyy");
			shpfeat.set("PROV", "yyy");
			shpfeat.set("PAIS", "yyy");
			selfeats.update(shpfeat);
			selfeats.dispose();
		}
		streetsStore.finishEditing();
		
		FeatureSet set = (FeatureSet)streetsStore.getDataSet();
		Iterator<Feature> it2 = set.iterator(0);
		Feature feat = it2.next();
		String muni = (String)feat.get("MUNI");
		String prov = (String)feat.get("PROV");
		String pais = (String)feat.get("PAIS");
		
		assertEquals("yyy", muni);
		assertEquals("yyy", prov);
		assertEquals("yyy", pais);
		
		
		

	}

}

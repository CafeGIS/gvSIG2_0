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

package org.gvsig.geocoding.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import junit.framework.TestCase;

import org.cresques.impl.CresquesCtsLibrary;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPStoreProvider;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.impl.DefaultLiteral;
import org.gvsig.geocoding.address.impl.DefaultRelationsComponent;
import org.gvsig.geocoding.pattern.GeocodingSource;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.pattern.impl.DefaultGeocodingSource;
import org.gvsig.geocoding.pattern.impl.DefaultPatterngeocoding;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.geocoding.styles.impl.SimpleCentroid;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.evaluator.sqljep.SQLJEPEvaluator;
import org.gvsig.tools.locator.Library;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceManager;
import org.gvsig.tools.persistence.PersistentState;
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class TestPersistence extends TestCase {

	private static final Logger log = LoggerFactory
			.getLogger(TestPersistence.class);

	private DataManager dataManager = null;
	private DataStore store = null;

	private Patterngeocoding pat = null;

	/**
	 * Constructor
	 */
	public TestPersistence() {

		try {

			DefaultGeometryLibrary defGeomLib = new DefaultGeometryLibrary();
			defGeomLib.initialize();
			defGeomLib.postInitialize();

			ToolsLibrary tools = new ToolsLibrary();
			tools.initialize();
			ToolsLocator.registerPersistenceManager(XMLEntityManager.class);
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

		} catch (Exception e) {
			e.printStackTrace();
		}

		dataManager = DALLocator.getDataManager();

		try {
			// streets file
			File fileStreets = new File("./test-data/geocoder/streets.shp");
			assertTrue(fileStreets.exists());
			SHPStoreParameters strParam = (SHPStoreParameters) dataManager
					.createStoreParameters(SHPStoreProvider.NAME);
			strParam.setSRSID("EPSG:23030");
			strParam.setSRS(CRSFactory.getCRS("EPSG:23030"));
			strParam.setFile(fileStreets);
						
			store = dataManager.createStore(strParam);
			assertNotNull(store);

		} catch (Exception e) {
			log.error("Getting streets.shp store", e);
		}

		// Get pattern
		pat = getPattern();
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
	 * 
	 * @throws DataException
	 */
	public void testPersistPattern() throws IOException, PersistenceException {

		File file = new File("c:/pat.xml");
		// File file = File.createTempFile("pat", "xml");

		PersistenceManager manager = ToolsLocator.getPersistenceManager();
		Writer writer = new FileWriter(file);
		PersistentState state = manager.getState(pat);
		state.save(writer);
		writer.close();

	}

	/**
	 * get pattern
	 * 
	 * @return
	 */
	private Patterngeocoding getPattern() {

		assertNotNull(dataManager);
		dataManager.registerDefaultEvaluator(SQLJEPEvaluator.class);

		Patterngeocoding pat = new DefaultPatterngeocoding();

		try {
			pat.setPatternName("SimpleCentroidLines");

			GeocodingSource source = new DefaultGeocodingSource();
			source.setLayerSource(store);

			AbstractGeocodingStyle style = new SimpleCentroid();
			FeatureType ftype = ((FeatureStore) store)
					.getDefaultFeatureType();
			Literal relations = new DefaultLiteral();
			relations.add(new DefaultRelationsComponent("Calle", "STREET_NAM"));

			style.setRelationsLiteral(relations);
			source.setStyle(style);

			pat.setSource(source);

		} catch (Exception e) {
			log.error("ERROR Building a pattern", e);
		}

		return pat;
	}

}

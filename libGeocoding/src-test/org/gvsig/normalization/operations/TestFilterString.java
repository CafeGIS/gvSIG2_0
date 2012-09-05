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
 * 2008 Prodevelop S.L  vsanjaime   programador
 */

package org.gvsig.normalization.operations;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.normalization.NormalizationTransform;
import org.gvsig.normalization.algorithm.NormalizationAlgorithm;
import org.gvsig.normalization.algorithm.impl.DefaultNormalizationAlgorithm;
import org.gvsig.normalization.pattern.Patternnormalization;
import org.gvsig.normalization.pattern.impl.DefaultPatternnormalization;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.locator.Library;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceManager;
import org.gvsig.tools.persistence.PersistentState;
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class TestFilterString extends TestCase {

	private static final Logger log = LoggerFactory
			.getLogger(TestFilterString.class);

	/**
	 * Constructor
	 */
	public TestFilterString() {
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
	}

	/**
	 * @throws IOException
	 * @throws PersistenceException
	 * @throws DataException
	 * 
	 */
	public void testTransformFeatureStore() throws PersistenceException,
			IOException, DataException {

		// store
		List<String> chains = new ArrayList<String>();
		chains.add(",XXX;9393;33.25;337.22E2;1/1/8");
		FeatureStore store = null;

		// Pattern
		File filePattern = new File(
				"../test-data/normalization/PATNORM_TEST.xml");
		assertTrue(filePattern.exists());
		assertNotNull(filePattern);
		Patternnormalization pat = parserPat(filePattern);
		assertNotNull(pat);

		// Algorithm
		NormalizationAlgorithm algorithm = new DefaultNormalizationAlgorithm(
				pat);

		// Normalization transform
		NormalizationTransform transform = new NormalizationTransform();
		transform.initialize(store, pat, 0, algorithm);

		// Add transform
		store.getTransforms().add(transform);

		//	
		//
		//		
		// int nFields = -1;
		// long nRows = -1;
		// String val00 = null;
		// String val01 = null;
		// String val02 = null;
		// String val03 = null;
		// String val04 = null;
		// String val05 = null;
		//
		// try {
		// test.open(outputFile);
		// nFields = test.getFieldCount();
		// nRows = test.getRowCount();
		// val00 = test.getFieldValue(0, 0).toString().trim();
		// val01 = test.getFieldValue(0, 1).toString().trim();
		// val02 = test.getFieldValue(0, 2).toString().trim();
		// val03 = test.getFieldValue(0, 3).toString().trim();
		// val04 = test.getFieldValue(0, 4).toString().trim();
		// val05 = test.getFieldValue(0, 5).toString().trim();
		//
		// test.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// assertEquals(nFields, 6);
		// assertEquals(nRows, 1);
		//
		// assertEquals(val00, "");
		// assertEquals(val01, "XXX");
		// assertEquals(val02, "9393");
		// assertEquals(val03, "0.0");
		// assertEquals(val04, "33722.0");
		// assertEquals(val05, "01-ene-0008");

		log.info("Test finished");
	}

	/**
	 * 
	 * @param f
	 * @return
	 * @throws PersistenceException
	 * @throws IOException
	 */
	private Patternnormalization parserPat(File f) throws PersistenceException,
			IOException {

		PersistenceManager manager = ToolsLocator.getPersistenceManager();
		Patternnormalization pat = new DefaultPatternnormalization();
		Reader reader = new FileReader(f);
		PersistentState state3 = manager.loadState(reader);
		pat.loadFromState(state3);
		reader.close();
		return pat;
	}

}

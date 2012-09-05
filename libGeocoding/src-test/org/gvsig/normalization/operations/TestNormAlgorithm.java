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

package org.gvsig.normalization.operations;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
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

public class TestNormAlgorithm extends TestCase {

	private static final Logger log = LoggerFactory
			.getLogger(TestNormAlgorithm.class);

	/**
	 * Constructor
	 */
	public TestNormAlgorithm() {

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
	 * 
	 * @throws PersistenceException
	 * @throws IOException
	 */
	public void testSplitChain() throws PersistenceException, IOException {

		log.debug("TestNormAlgorithm. Test splits strings");

		log.debug("SubTest 1");
		String c1 = ";aaa,,bbb;;;ccc/ddd@eee##;";

		File f1 = new File(
				"./src-test/org/gvsig/normalization/testdata/patSplitChain.xml");

		assertTrue(f1.exists());
		assertNotNull(f1);

		List<String> r1 = parser(f1, c1);
		assertNotNull(r1);

		assertEquals("", (String) r1.get(0));
		assertEquals("aaa", (String) r1.get(1));
		assertEquals("", (String) r1.get(2));
		assertEquals("bbb", (String) r1.get(3));
		assertEquals("", (String) r1.get(4));
		assertEquals("", (String) r1.get(5));
		assertEquals("ccc", (String) r1.get(6));
		assertEquals("ddd", (String) r1.get(7));
		assertEquals("eee", (String) r1.get(8));
		assertEquals("", (String) r1.get(9));
		assertEquals(";", (String) r1.get(10));

		log.debug("SubTest 2.");
		String c2 = "aaa bbb ccc ddd,76 %";

		File f2 = new File(
				"./src-test/org/gvsig/normalization/testdata/patSplitChain2.xml");
		assertTrue(f2.exists());
		assertNotNull(f2);

		List<String> r2 = parser(f2, c2);
		assertNotNull(r2);

		assertEquals("aaa", (String) r2.get(0));
		assertEquals("bbb", (String) r2.get(1));
		assertEquals("ccc", (String) r2.get(2));
		assertEquals("ddd", (String) r2.get(3));
		assertEquals("76", (String) r2.get(4));
		assertEquals("%", (String) r2.get(5));

		log.debug("SubTest 3.");
		String c3 = "Av;Germanias;25;3;Moncada;Valencia";

		File f3 = new File(
				"./src-test/org/gvsig/normalization/testdata/patSplitChain3.xml");
		assertTrue(f3.exists());
		assertNotNull(f3);

		List<String> r3 = parser(f3, c3);
		assertNotNull(r3);

		assertEquals("Av", (String) r3.get(0));
		assertEquals("Germanias", (String) r3.get(1));
		assertEquals(25, Integer.parseInt((String) r3.get(2)));
		assertEquals(3, Integer.parseInt((String) r3.get(3)));
		assertEquals("Moncada", (String) r3.get(4));
		assertEquals("Valencia", (String) r3.get(5));

		log.debug("SubTest 4.");
		String c4 = "Av. Germanias      15  2   Moncada   Valencia    ";

		File f4 = new File(
				"./src-test/org/gvsig/normalization/testdata/patSplitChain4.xml");
		assertTrue(f4.exists());
		assertNotNull(f4);

		List<String> r4 = parser(f4, c4);
		assertNotNull(r4);

		assertEquals("Av.", ((String) r4.get(0)).trim());
		assertEquals("Germanias", ((String) r4.get(1)).trim());
		assertEquals(15, Integer.parseInt(((String) r4.get(2)).trim()));
		assertEquals(2, Integer.parseInt(((String) r4.get(3)).trim()));
		assertEquals("Moncada", ((String) r4.get(4)).trim());
		assertEquals("Valencia", ((String) r4.get(5)).trim());

		log.debug("SubTest 5.");
		String c5 = "Av;,;Germanias;15;;2;Moncada;Valencia";

		File f5 = new File(
				"./src-test/org/gvsig/normalization/testdata/patSplitChain5.xml");
		assertTrue(f5.exists());
		assertNotNull(f5);

		List<String> r5 = parser(f5, c5);
		assertNotNull(r5);

		assertEquals("Av", (String) r5.get(0));
		assertEquals("Germanias", (String) r5.get(1));
		assertEquals(15, Integer.parseInt((String) r5.get(2)));
		assertEquals(2, Integer.parseInt((String) r5.get(3)));
		assertEquals("Moncada", (String) r5.get(4));
		assertEquals("Valencia", (String) r5.get(5));

		log.debug("SubTest 6.");
		String c6 = "Av. Germanias 15-2 Moncada (Valencia)";

		File f6 = new File(
				"./src-test/org/gvsig/normalization/testdata/patSplitChain6.xml");
		assertTrue(f6.exists());
		assertNotNull(f6);

		List<String> r6 = parser(f6, c6);
		assertNotNull(r6);

		assertEquals("Av.", ((String) r6.get(0)).trim());
		assertEquals("Germanias", ((String) r6.get(1)).trim());
		assertEquals(15, Integer.parseInt(((String) r6.get(2)).trim()));
		assertEquals(2, Integer.parseInt(((String) r6.get(3)).trim()));
		assertEquals("Moncada", ((String) r6.get(4)).trim());
		assertEquals("Valencia", ((String) r6.get(5)).trim());

		log.debug("SubTest 7.");
		String c7 = "Juana Aguirre;Piedras;No 623;Piso2;Dto.4;C1070AAM;Capital Federal;ARGENTINA";

		File f7 = new File(
				"./src-test/org/gvsig/normalization/testdata/patSplitChain7.xml");
		assertTrue(f7.exists());
		assertNotNull(f7);

		List<String> r7 = parser(f7, c7);
		assertNotNull(r7);

		assertEquals("Juana Aguirre", ((String) r7.get(0)).trim());
		assertEquals("Piedras", ((String) r7.get(1)).trim());
		assertEquals("No 623", ((String) r7.get(2)).trim());
		assertEquals("Piso2", ((String) r7.get(3)).trim());
		assertEquals("Dto.4", ((String) r7.get(4)).trim());
		assertEquals("C1070AAM", ((String) r7.get(5)).trim());
		assertEquals("Capital Federal", ((String) r7.get(6)).trim());
		assertEquals("ARGENTINA", ((String) r7.get(7)).trim());

		log.debug("SubTest 8.");
		String c8 = "5.548\t5478.254\t0.24578457\t256.21450045";

		File f8 = new File(
				"./src-test/org/gvsig/normalization/testdata/patSplitChain8.xml");
		assertTrue(f8.exists());
		assertNotNull(f8);

		List<String> r8 = parser(f8, c8);
		assertNotNull(r8);

		assertEquals(5.548d, Double.parseDouble(((String) r8.get(0)).trim()), 0);
		assertEquals(5478.254, Double.parseDouble(((String) r8.get(1)).trim()),
				0);
		assertEquals(0.24578457, Double
				.parseDouble(((String) r8.get(2)).trim()), 0);
		assertEquals(256.21450045, Double.parseDouble(((String) r8.get(3))
				.trim()), 0);

		log.debug("TestNormAlgorithm. Test finished");
	}

	/**
	 * 
	 */
	public void testSplitFixed() {

		log.debug("TestNormAlgorithm. Test tokens fixed");

		String chain = "esto;/es;;; una_prueba;  de un/   split de una cadena_de texto";

		int parts = 4;
		boolean join = true;
		String[] separators = { " " };
		List<String> result = DefaultNormalizationAlgorithm
				.splitChainBySeparators(chain, parts, separators, join);
		log.debug("Cadena inicial: " + chain);
		for (int i = 0; i < result.size(); i++) {
			log.debug("Subcadena" + i + ": " + (String) result.get(i));
		}
		assertEquals("esto;/es;;;", result.get(0));
		assertEquals("una_prueba;", result.get(1));
		assertEquals("de", result.get(2));
		assertEquals("un/   split de una cadena_de texto", result.get(3));

		log.debug("TestNormAlgorithm. Test tokens fixed finished");

	}

	/**
	 * 
	 */
	public void testSplitSeparators() {

		log.debug("TestNormAlgorithm. Test tokens with separators");

		String chain = "esto;/es;;; una_prueba;  de un/   split de una cadena_de texto";

		int parts = 4;
		boolean join = true;
		String[] separators = { " " };
		List<String> result = DefaultNormalizationAlgorithm
				.splitChainBySeparators(chain, parts, separators, join);
		System.out.println("Cadena inicial: " + chain);
		for (int i = 0; i < result.size(); i++) {
			System.out.println("Subcadena" + i + ": " + (String) result.get(i));
		}
		assertEquals("esto;/es;;;", result.get(0));
		assertEquals("una_prueba;", result.get(1));
		assertEquals("de", result.get(2));
		assertEquals("un/   split de una cadena_de texto", result.get(3));

		log.debug("TestNormAlgorithm. Test tokens with separators finished");

	}

	/**
	 * 
	 * @param f
	 * @param chain
	 * @return
	 * @throws PersistenceException
	 * @throws IOException
	 */
	private List<String> parser(File f, String chain)
			throws PersistenceException, IOException {

		PersistenceManager manager = ToolsLocator.getPersistenceManager();

		Patternnormalization pat = new DefaultPatternnormalization();
		FileReader reader = new FileReader(f);
		PersistentState state = manager.loadState(reader);
		pat.loadFromState(state);
		reader.close();

		NormalizationAlgorithm na = new DefaultNormalizationAlgorithm(pat);

		List<String> result = na.splitChain(chain);
		return result;

	}

}

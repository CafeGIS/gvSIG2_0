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
 * 2008 Prodevelop S.L. main development
 */

package org.gvsig.normalization.patterns;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.normalization.pattern.Element;
import org.gvsig.normalization.pattern.Fieldseparator;
import org.gvsig.normalization.pattern.Fieldtype;
import org.gvsig.normalization.pattern.Infieldseparators;
import org.gvsig.normalization.pattern.Patternnormalization;
import org.gvsig.normalization.pattern.Stringvalue;
import org.gvsig.normalization.pattern.impl.DefaultElement;
import org.gvsig.normalization.pattern.impl.DefaultFieldseparator;
import org.gvsig.normalization.pattern.impl.DefaultFieldtype;
import org.gvsig.normalization.pattern.impl.DefaultInfieldseparators;
import org.gvsig.normalization.pattern.impl.DefaultPatternnormalization;
import org.gvsig.normalization.pattern.impl.DefaultStringvalue;
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
 * test persistence
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */
public class TestPersistence extends TestCase {

	private static final Logger log = LoggerFactory
			.getLogger(TestPersistence.class);
	private File tempfile = null;

	/**
	 * Constructor
	 */
	public TestPersistence() {		

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

	public void setUp() {
		tempfile.delete();
		try {
			tempfile = File.createTempFile("pruebaParseXML", "xml");
		} catch (Exception e) {
			log.error("Creating temporal file", e);
		}
	}

	public void testSerializing() throws PersistenceException, IOException {

		PersistenceManager manager = ToolsLocator.getPersistenceManager();

		Patternnormalization pat = new DefaultPatternnormalization();
		pat.setPatternname("pruebaParseXML.xml");
		pat.setNofirstrows(0);

		List<Element> elems = new ArrayList<Element>();

		Element elem1 = new DefaultElement();
		elem1.setFieldname("campo1");

		Fieldtype tipo = new DefaultFieldtype();
		Stringvalue strval = new DefaultStringvalue();
		strval.setStringvaluewidth(50);
		tipo.setStringvalue(strval);
		elem1.setFieldtype(tipo);

		elem1.setFieldwidth(30);

		Fieldseparator fsep = new DefaultFieldseparator();
		fsep.setSemicolonsep(true);
		fsep.setJoinsep(false);
		elem1.setFieldseparator(fsep);

		Infieldseparators infsep = new DefaultInfieldseparators();
		infsep.setThousandseparator(",");
		infsep.setDecimalseparator(".");
		infsep.setTextseparator("\"");
		elem1.setInfieldseparators(infsep);

		elem1.setImportfield(true);

		elems.add(elem1);

		Element elem2 = new DefaultElement();
		elem2.setFieldname("campo2");

		Fieldtype tipo2 = new DefaultFieldtype();
		Stringvalue strval2 = new DefaultStringvalue();
		strval2.setStringvaluewidth(50);
		tipo2.setStringvalue(strval2);
		elem2.setFieldtype(tipo2);

		elem2.setFieldwidth(30);

		Fieldseparator fsep2 = new DefaultFieldseparator();
		fsep2.setSemicolonsep(true);
		fsep2.setJoinsep(false);
		elem2.setFieldseparator(fsep2);

		Infieldseparators infsep2 = new DefaultInfieldseparators();
		infsep2.setThousandseparator(",");
		infsep2.setDecimalseparator(".");
		infsep2.setTextseparator("\"");
		elem2.setInfieldseparators(infsep2);

		elem2.setImportfield(true);

		elems.add(elem2);

		pat.setElements(elems);

		FileReader reader = new FileReader(tempfile);
		PersistentState state = manager.loadState(reader);
		pat.loadFromState(state);
		reader.close();
	}

	public void testParsing() throws IOException, PersistenceException {

		PersistenceManager manager = ToolsLocator.getPersistenceManager();

		Patternnormalization pat = new DefaultPatternnormalization();
		pat.setPatternname("pruebaParseXML.xml");
		pat.setNofirstrows(0);

		List<Element> elems = new ArrayList<Element>();

		Element elem1 = new DefaultElement();
		elem1.setFieldname("campo1");

		Fieldtype tipo = new DefaultFieldtype();
		Stringvalue strval = new DefaultStringvalue();
		strval.setStringvaluewidth(50);
		tipo.setStringvalue(strval);
		elem1.setFieldtype(tipo);

		elem1.setFieldwidth(30);

		Fieldseparator fsep = new DefaultFieldseparator();
		fsep.setSemicolonsep(true);
		fsep.setJoinsep(false);
		elem1.setFieldseparator(fsep);

		Infieldseparators infsep = new DefaultInfieldseparators();
		infsep.setThousandseparator(",");
		infsep.setDecimalseparator(".");
		infsep.setTextseparator("\"");
		elem1.setInfieldseparators(infsep);

		elem1.setImportfield(true);

		elems.add(elem1);

		Element elem2 = new DefaultElement();
		elem2.setFieldname("campo2");

		Fieldtype tipo2 = new DefaultFieldtype();
		Stringvalue strval2 = new DefaultStringvalue();
		strval2.setStringvaluewidth(50);
		tipo2.setStringvalue(strval2);
		elem2.setFieldtype(tipo2);

		elem2.setFieldwidth(30);

		Fieldseparator fsep2 = new DefaultFieldseparator();
		fsep2.setSemicolonsep(true);
		fsep2.setJoinsep(false);
		elem2.setFieldseparator(fsep2);

		Infieldseparators infsep2 = new DefaultInfieldseparators();
		infsep2.setThousandseparator(",");
		infsep2.setDecimalseparator(".");
		infsep2.setTextseparator("\"");
		elem2.setInfieldseparators(infsep2);

		elem2.setImportfield(true);

		elems.add(elem2);

		pat.setElements(elems);

		Writer writer = new FileWriter(tempfile);
		PersistentState state = manager.getState(pat);
		state.save(writer);
		writer.close();

		Patternnormalization pat2 = new DefaultPatternnormalization();
		FileReader reader = new FileReader(tempfile);
		PersistentState state2 = manager.loadState(reader);
		pat2.loadFromState(state2);
		reader.close();

		assertEquals(0, pat2.getNofirstrows());

		assertEquals("pruebaParseXML.xml", pat2.getPatternname());
		assertEquals(0, pat2.getNofirstrows());

		Element elem0 = ((Element) pat2.getElements().get(0));

		assertEquals("campo1", elem0.getFieldname());
		assertEquals(true, elem0.getImportfield());
	}
	
	public void testParserSerializer() throws IOException, PersistenceException {

		PersistenceManager manager = ToolsLocator.getPersistenceManager();

		Patternnormalization pat = new DefaultPatternnormalization();

		File file = new File("../test-data/normalization/patSplitChain.xml");

		assertTrue(file.exists());

		// PARSER
		FileReader reader = new FileReader(file);
		PersistentState state2 = manager.loadState(reader);
		pat.loadFromState(state2);
		reader.close();

		assertNotNull(pat);
		assertEquals(11, pat.getElements().size());
		assertEquals(0, pat.getNofirstrows());

		Element elem1 = pat.getElements().get(0);

		assertNotNull(elem1);
		assertEquals("NewField", elem1.getFieldname());

		// SERIALIZER
		File ftemp = File.createTempFile("tem", "txt");
		Writer writer2 = new FileWriter(ftemp);

		PersistentState state = manager.getState(pat);
		state.save(writer2);
		writer2.close();

		// PARSER
		Patternnormalization pat3 = new DefaultPatternnormalization();

		FileReader reader3 = new FileReader(ftemp);
		PersistentState state3 = manager.loadState(reader3);
		pat3.loadFromState(state3);
		reader3.close();

		Element elem2 = (Element) pat3.getElements().get(0);

		assertNotNull(elem2);

		assertEquals(elem1.getImportfield(), elem2.getImportfield());
		assertEquals(elem1.getFieldwidth(), elem2.getFieldwidth());
		assertEquals(elem1.getFieldname(), elem2.getFieldname());
		assertEquals(elem1.getInfieldseparators().getDecimalseparator(), elem2
				.getInfieldseparators().getDecimalseparator());
		
		ftemp.delete();
		log.info("Test finished.....");

	}
}

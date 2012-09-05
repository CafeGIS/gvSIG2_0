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
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.fmap.dal.feature.FeatureStoreTransforms;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreProvider;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.normalization.NormalizationTransform;
import org.gvsig.normalization.algorithm.NormalizationAlgorithm;
import org.gvsig.normalization.algorithm.impl.DefaultNormalizationAlgorithm;
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
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */
public class TestNormalizeFeatureData extends TestCase {

	private static final Logger log = LoggerFactory
			.getLogger(TestNormalizeFeatureData.class);

	Patternnormalization pat = null;
	File file = null;

	/**
	 * Constructor
	 */
	public TestNormalizeFeatureData() {

		ToolsLibrary tools = new ToolsLibrary();
		tools.initialize();
		ToolsLocator.registerPersistenceManager(XMLEntityManager.class);
		tools.postInitialize();

		Library dallib = new DALLibrary();
		dallib.initialize();
		dallib.postInitialize();

		Library libFile = new DALFileLibrary();
		libFile.initialize();
		libFile.postInitialize();

		Library shpLib = new SHPLibrary();
		shpLib.initialize();
		shpLib.postInitialize();
	}

	public void setUp() {
	}

	public void testNormalizationTransform()
			throws ValidateDataParametersException, DataException {

		DataManager manager = DALLocator.getDataManager();

		// PATTERN
		pat = getPattern();

		// DATASTORE
		file = new File("test-data\\normalization\\pro.dbf");		
		assertTrue(file.exists());
		DBFStoreParameters params = (DBFStoreParameters) manager
				.createStoreParameters(DBFStoreProvider.NAME);
		params.setDBFFile(file);
		FeatureStore store = (FeatureStore) manager.createStore(params);

		// ALGORITHM
		NormalizationAlgorithm algorithm = new DefaultNormalizationAlgorithm(
				pat);

		// TRANSFORM
		NormalizationTransform transform = new NormalizationTransform();
		transform.initialize(store, pat, 5, algorithm);
		// Add transform
		FeatureStoreTransforms trans = store.getTransforms();
		trans.add((FeatureStoreTransform)transform);

	}

	public void tearDown() {
		log.debug("TEST FINISHED");
	}

	private Patternnormalization getPattern() {

		// Create object
		Patternnormalization pat = new DefaultPatternnormalization();

		// Field Separators
		Fieldseparator filsep1 = new DefaultFieldseparator();

		filsep1.setSemicolonsep(true);
		filsep1.setJoinsep(false);
		filsep1.setColonsep(false);
		filsep1.setSpacesep(false);
		filsep1.setTabsep(false);

		// Field Separators
		Fieldseparator filsep2 = new DefaultFieldseparator();

		filsep2.setSemicolonsep(true);
		filsep2.setJoinsep(false);
		filsep2.setColonsep(false);
		filsep2.setSpacesep(false);
		filsep2.setTabsep(false);

		// In Field Separators
		Locale loc = Locale.getDefault();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(loc);
		Infieldseparators infilsep1 = new DefaultInfieldseparators();

		infilsep1.setDecimalseparator(Character.toString(dfs
				.getDecimalSeparator()));
		infilsep1.setTextseparator("\"");
		infilsep1.setThousandseparator(Character.toString(dfs
				.getGroupingSeparator()));

		// In Field Separators

		Infieldseparators infilsep2 = new DefaultInfieldseparators();

		infilsep2.setDecimalseparator(Character.toString(dfs
				.getDecimalSeparator()));
		infilsep2.setTextseparator("\"");
		infilsep2.setThousandseparator(Character.toString(dfs
				.getGroupingSeparator()));

		// Main attributes
		pat.setPatternname("thePattern");
		pat.setNofirstrows(0);

		// Create the first Address Element ////////////////////
		List<Element> elems = new ArrayList<Element>();
		Element elem1 = new DefaultElement();

		// Field 1 Field 1 Field 1 Field 1
		elem1.setFieldname("campo1");

		elem1.setFieldseparator(filsep1);

		elem1.setInfieldseparators(infilsep1);

		// Field type
		Fieldtype newtype = new DefaultFieldtype();
		Stringvalue strval = new DefaultStringvalue();
		strval.setStringvaluewidth(50);
		newtype.setStringvalue(strval);

		elem1.setFieldtype(newtype);
		elem1.setFieldwidth(0);
		elem1.setImportfield(true);

		elems.add(elem1);

		// Create the second Address Element ////////////////////

		Element elem2 = new DefaultElement();

		// Field 2 Field 2 Field 2 Field 2
		elem2.setFieldname("campo2");

		elem2.setFieldseparator(filsep2);

		elem2.setInfieldseparators(infilsep2);

		// Field type
		Fieldtype newtype2 = new DefaultFieldtype();
		Stringvalue strval2 = new DefaultStringvalue();
		strval2.setStringvaluewidth(50);
		newtype2.setStringvalue(strval2);

		elem2.setFieldtype(newtype2);
		elem2.setFieldwidth(0);
		elem2.setImportfield(true);

		elems.add(elem2);

		pat.setElements(elems);

		return pat;
	}

}

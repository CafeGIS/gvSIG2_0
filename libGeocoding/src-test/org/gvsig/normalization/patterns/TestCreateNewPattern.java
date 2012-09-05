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

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.gvsig.normalization.pattern.Decimalvalue;
import org.gvsig.normalization.pattern.Element;
import org.gvsig.normalization.pattern.Fieldseparator;
import org.gvsig.normalization.pattern.Fieldtype;
import org.gvsig.normalization.pattern.Infieldseparators;
import org.gvsig.normalization.pattern.Patternnormalization;
import org.gvsig.normalization.pattern.Stringvalue;
import org.gvsig.normalization.pattern.impl.DefaultDecimalvalue;
import org.gvsig.normalization.pattern.impl.DefaultElement;
import org.gvsig.normalization.pattern.impl.DefaultFieldseparator;
import org.gvsig.normalization.pattern.impl.DefaultFieldtype;
import org.gvsig.normalization.pattern.impl.DefaultInfieldseparators;
import org.gvsig.normalization.pattern.impl.DefaultPatternnormalization;
import org.gvsig.normalization.pattern.impl.DefaultStringvalue;

import junit.framework.TestCase;



/*
 * This test validates a new pattern generated
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class TestCreateNewPattern extends TestCase {

	public void getUp() {
	}

	public void testCreateNewPattern() {
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

		// Field Name
		elem1.setFieldname("StringField");

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

		// Field Name
		elem2.setFieldname("DecimalField");

		elem2.setFieldseparator(filsep2);

		elem2.setInfieldseparators(infilsep2);

		// Field type
		Fieldtype newtype2 = new DefaultFieldtype();
		Decimalvalue decval = new DefaultDecimalvalue();
		decval.setDecimalvalueint(5);
		decval.setDecimalvaluedec(5);
		newtype2.setDecimalvalue(decval);

		elem2.setFieldtype(newtype2);
		elem2.setFieldwidth(0);
		elem2.setImportfield(true);

		elems.add(elem2);

		pat.setElements(elems);
		System.out.println("load pattern");

		assertNotNull(pat);
		assertNotNull(elem1);
		assertNotNull(elem2);
		assertEquals(((Element) pat.getElements().get(0)).getImportfield(),
				true);
		assertEquals(((Element) pat.getElements().get(0)).getFieldwidth(), 0);
		assertEquals(((Stringvalue) ((Element) pat.getElements().get(0))
				.getFieldtype().getStringvalue()).getStringvaluewidth(), 50);
		assertEquals(((Element) pat.getElements().get(0)).getFieldseparator()
				.getSemicolonsep(), true);
		assertEquals(((Element) pat.getElements().get(1)).getImportfield(),
				true);
		assertEquals(((Element) pat.getElements().get(1)).getFieldwidth(), 0);
		assertEquals(((Element) pat.getElements().get(1)).getFieldtype()
				.getDecimalvalue().getDecimalvaluedec(), 5);
		assertEquals(((Element) pat.getElements().get(1)).getFieldseparator()
				.getSemicolonsep(), true);

	}

	public void tearDown() {
	}

}

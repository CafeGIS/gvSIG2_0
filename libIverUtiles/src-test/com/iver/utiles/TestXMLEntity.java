/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/* CVS MESSAGES:
*
* $Id: TestXMLEntity.java 28076 2009-04-21 12:39:58Z cmartinez $
* $Log$
* Revision 1.7  2007-03-05 11:15:43  jaume
* *** empty log message ***
*
* Revision 1.6  2007/03/05 10:03:12  jaume
* *** empty log message ***
*
* Revision 1.5  2007/03/05 09:00:11  jaume
* *** empty log message ***
*
* Revision 1.4  2007/03/02 13:35:56  jaume
* *** empty log message ***
*
* Revision 1.3  2007/03/02 13:27:32  jaume
* *** empty log message ***
*
* Revision 1.2  2007/03/02 13:24:53  jaume
* *** empty log message ***
*
* Revision 1.1  2007/03/02 13:23:50  jaume
* *** empty log message ***
*
* Revision 1.1  2006/08/29 06:18:17  jaume
* *** empty log message ***
*
*
*/
package com.iver.utiles;

import java.io.File;
import java.io.FileReader;

import org.gvsig.tools.IverUtilesLibrary;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.locator.Library;

import junit.framework.TestCase;

import com.iver.utiles.xmlEntity.generate.XmlTag;

public class TestXMLEntity extends TestCase {
	private XMLEntity x1, x2, x3, x4;
	private XMLEntity gvp1;
	private XMLEntity gvp2;

	public void setUp() {
		Library toolsLib = new ToolsLibrary();
		toolsLib.initialize();
		
		Library utilsLib = new IverUtilesLibrary();
		utilsLib.initialize();
		
		toolsLib.postInitialize();
		utilsLib.postInitialize();
		

		x1 = new XMLEntity();
		x2 = new XMLEntity();
		x3 = new XMLEntity();

		
		final Object obj1 = new XMLEntity();
		final Object obj2 = new XMLEntity();
		final Object obj3 = "StringClass";

		
		x1.putProperty("boolean1", false);
		x1.putProperty("boolean2", true);

		x1.putProperty("integer1", Integer.parseInt("123"));
		x1.putProperty("integer2", Integer.parseInt("321"));

		x1.putProperty("long1", Long.parseLong("123123"));
		x1.putProperty("long2", Long.parseLong("321321"));

		x1.putProperty("float1", Float.parseFloat("1234.1234"));
		x1.putProperty("float2", Float.parseFloat("4321.4321"));

		x1.putProperty("double1", Double.parseDouble("12341234.12341234"));
		x1.putProperty("double2", Double.parseDouble("43214321.43214321"));

		x1.putProperty("string1", "String1");
		x1.putProperty("string2", "String2");

		x1.putProperty("object1", obj1);
		x1.putProperty("object2", obj2);


		//----- x1 == x2
		x2.putProperty("boolean1", false);
		x2.putProperty("boolean2", true);

		x2.putProperty("integer1", Integer.parseInt("123"));
		x2.putProperty("integer2", Integer.parseInt("321"));

		x2.putProperty("long1", Long.parseLong("123123"));
		x2.putProperty("long2", Long.parseLong("321321"));

		x2.putProperty("float1", Float.parseFloat("1234.1234"));
		x2.putProperty("float2", Float.parseFloat("4321.4321"));

		x2.putProperty("double1", Double.parseDouble("12341234.12341234"));
		x2.putProperty("double2", Double.parseDouble("43214321.43214321"));

		x2.putProperty("string1", "String1");
		x2.putProperty("string2", "String2");

		x2.putProperty("object1", obj1);
		x2.putProperty("object2", obj2);

		
		//----- x1 != x3

		x3.putProperty("boolean1", false);
		x3.putProperty("boolean2", true);

		x3.putProperty("integer1", Integer.parseInt("123"));
		x3.putProperty("integer2", Integer.parseInt("321"));

		x3.putProperty("long1", Long.parseLong("123123"));
		x3.putProperty("long2", Long.parseLong("321321"));

		x3.putProperty("float1", Float.parseFloat("1234.1234"));
		x3.putProperty("float2", Float.parseFloat("4321.4321"));

		x3.putProperty("double1", Double.parseDouble("12341234.12341234"));
		x3.putProperty("double2", Double.parseDouble("43214321.43214324"));

		x3.putProperty("string1", "String1");
		x3.putProperty("string2", "String2");

		x3.putProperty("object1", obj1);
		x3.putProperty("object2", obj3);
		
		
		XmlTag tag;
		try {
			tag = (XmlTag) XmlTag.unmarshal(
					new FileReader(new File("testdata/sample.gvp")));
			gvp1 = new XMLEntity(tag);
			
			tag = (XmlTag) XmlTag.unmarshal(
					new FileReader(new File("testdata/sample2.gvp")));
			gvp2 = new XMLEntity(tag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 
	}

	public void testEquality() {
		assertEquals(x1, x2);

		assertFalse(x1.equals(x3));
		assertFalse(x2.equals(x3));
	}

	
	public void testHash() {
		final long x1Hash = x1.hash();
		final long x2Hash = x2.hash();
		final long x3Hash = x3.hash();
		
		assertTrue("x1 should be equals to x2", x1Hash == x2Hash);
		assertTrue("x1 should be distinct to x3", x1Hash != x3Hash);
	}
	
	
	public void testSkippedProperties() {
		x1.putProperty("skipped1", true, false);
		x1.putProperty("skipped2", 1, false);
		x1.putProperty("skipped3", 1D, false);
		x1.putProperty("skipped4", 1F, false);
		x1.putProperty("skipped5", 1L, false);
		x1.putProperty("skipped6", "bla bla", false);
		
		x2.putProperty("skipped1", false, false);
		x2.putProperty("skipped2", 2, false);
		x2.putProperty("skipped3", 2D, false);
		x2.putProperty("skipped4", 2F, false);
		x2.putProperty("skipped5", 2L, false);
		x2.putProperty("skipped6", "bla bla bleitor", false);
		
		x3.putProperty("skipped1", false, false);
		x3.putProperty("skipped2", 2, false);
		x3.putProperty("skipped3", 2D, false);
		x3.putProperty("skipped4", 2F, false);
		x3.putProperty("skipped5", 2L, false);
		x3.putProperty("skipped6", "bla bla bleitor", false);

		
		
		final long x1Hash = x1.hash();
		final long x2Hash = x2.hash();
		final long x3Hash = x3.hash();
		
		assertTrue("x1 should be equals to x2", x1Hash == x2Hash);
		assertTrue("x1 should be distinct to x3", x1Hash != x3Hash);
		assertTrue("x2 should be distinct to x3", x2Hash != x3Hash);
		
		x1.remove("skipped1");
		x1.remove("skipped2");
		x1.remove("skipped3");
		x1.remove("skipped4");
		x1.remove("skipped5");
		x1.remove("skipped6");
		
		x2.remove("skipped1");
		x2.remove("skipped2");
		x2.remove("skipped3");
		x2.remove("skipped4");
		x2.remove("skipped5");
		x2.remove("skipped6");
		
		x3.remove("skipped1");
		x3.remove("skipped2");
		x3.remove("skipped3");
		x3.remove("skipped4");
		x3.remove("skipped5");
		x3.remove("skipped6");
	}
	
	

	public void testNonSkippedProperties() {
		x1.putProperty("non_skipped1", true, true);
		x1.putProperty("non_skipped2", 1, true);
		x1.putProperty("non_skipped3", 1D, true);
		x1.putProperty("non_skipped4", 1F, true);
		x1.putProperty("non_skipped5", 1L, true);
		x1.putProperty("non_skipped6", "bla bla", true);
		
		x3.putProperty("non_skipped1", false, true);
		x3.putProperty("non_skipped2", 2, true);
		x3.putProperty("non_skipped3", 2D, true);
		x3.putProperty("non_skipped4", 2F, true);
		x3.putProperty("non_skipped5", 2L, true);
		x3.putProperty("non_skipped6", "bla bla bleitor", true);

		x2.putProperty("non_skipped1", false, true);
		x2.putProperty("non_skipped2", 2, true);
		x2.putProperty("non_skipped3", 2D, true);
		x2.putProperty("non_skipped4", 2F, true);
		x2.putProperty("non_skipped5", 2L, true);
		x2.putProperty("non_skipped6", "bla bla bleitor", true);
		
		final long x1Hash = x1.hash();
		final long x2Hash = x2.hash();
		final long x3Hash = x3.hash();
		
		assertFalse("x1 should be distinct to x2", x1Hash == x2Hash);
		assertFalse("x1 should be distinct to x3", x1Hash == x3Hash);
		assertFalse("x2 should be distinct to x3", x2Hash == x3Hash);
		
		
		// restore the XMEntities to the initial state and ensure that
		// passes the tests again
		x1.remove("non_skipped1");
		x1.remove("non_skipped2");
		x1.remove("non_skipped3");
		x1.remove("non_skipped4");
		x1.remove("non_skipped5");
		x1.remove("non_skipped6");
		
		x2.remove("non_skipped1");
		x2.remove("non_skipped2");
		x2.remove("non_skipped3");
		x2.remove("non_skipped4");
		x2.remove("non_skipped5");
		x2.remove("non_skipped6");
		
		x3.remove("non_skipped1");
		x3.remove("non_skipped2");
		x3.remove("non_skipped3");
		x3.remove("non_skipped4");
		x3.remove("non_skipped5");
		x3.remove("non_skipped6");
		
		testHash();
		testEquality();
	}
	

	public void testEquivalentProjects() {
		final long x1Hash = gvp1.hash();
		final long x2Hash = gvp2.hash();

		assertTrue("x1 should be equals to x2", x1Hash == x2Hash);
	}
	
	public void testBenchMark() {
		long t_ini, t_unmarshal, t_buildXMLEntity, t_computeHash ;
		t_ini = System.currentTimeMillis();
		XmlTag tag;
		try {
			File f= new File("testdata/big.gvp");
			tag = (XmlTag) XmlTag.unmarshal(
					new FileReader(f));
			t_unmarshal = System.currentTimeMillis() - t_ini;
			
			gvp1 = new XMLEntity(tag);
			t_buildXMLEntity = System.currentTimeMillis() - t_unmarshal- t_ini;

			long hash = gvp1.hash();
			t_computeHash = System.currentTimeMillis() - t_buildXMLEntity- t_ini;
			
			assertTrue("BenchMark ("+f.getAbsolutePath()+" "+f.length()/1024+"Kb):" +
					"\t-unmarshal time: "+t_unmarshal+" milliseconds\n" +
					"\t-build XMLEntity time: "+t_buildXMLEntity+" milliseconds\n" +
					"\t-compute hash time: "+t_computeHash+" milliseconds",t_computeHash < 500);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

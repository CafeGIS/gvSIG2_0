package com.iver.utiles.search;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.iver.utiles.CompareLists;
import com.iver.utiles.StringComparator;

import junit.framework.TestCase;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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

/**
 * Tests the static methods of BinarySearchUsingFirstCharacters
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestBinarySearchUsingFirstCharacters extends TestCase {
	private static String text;
	private static Vector v1;
	private static Vector v2;
	private static List list;
	private static List results;
	private static StringComparator stringComparator;
//	private static StringComparatorForItems stringComparatorForItems;
	
	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		// Initialize the text
		text = "";
		
		// Initialize the StringComparator
		stringComparator = new StringComparator();

		// Initialize the StringComparatorForItems
//		stringComparatorForItems = new StringComparatorForItems();

		// Initialize the vector of Items (for test methods without the parameter Comparator)
		v1 = new Vector();
		v1.add(new Item("extWMS"));
		v1.add(new Item("libUI"));
		v1.add(new Item("extWMS"));
		v1.add(new Item("libRemoteServices"));
		v1.add(new Item("extWFS2"));
		v1.add(new Item("libNomenclatorIGN"));
		v1.add(new Item("libNomenclatorIGN_GUI"));
		v1.add(new Item("libIverUtiles"));
		v1.add(new Item("extWFS2"));
		v1.add(new Item("EXTWFS2"));
		v1.add(new Item("extWFS2"));
		v1.add(new Item("libGDBMS"));
		v1.add(new Item("libInternationalization"));
		v1.add(new Item("libFMap"));
		v1.add(new Item("libuiDownCase"));
		v1.add(new Item("6"));
		v1.add(new Item("4"));
		v1.add(new Item("3"));
		v1.add(new Item("5"));
		v1.add(new Item("2"));
		v1.add(new Item("1"));
		v1.add(new Item("extWFS2"));
		v1.add(new Item("libExceptions"));
		v1.add(new Item("libDriverManager"));
		v1.add(new Item("libCq CMS for java"));
		v1.add(new Item("libCorePlugin"));
		v1.add(new Item("extWFS2"));
		v1.add(new Item("extAddIDEELayers"));
		v1.add(new Item("extAlertCClient"));
		v1.add(new Item("extCAD"));
		v1.add(new Item("extWFS2"));
		v1.add(new Item("ÑandÚ"));
		v1.add(new Item("ñandú"));
		v1.add(new Item("extJDBC"));
		v1.add(new Item("extMyPlugin"));
		v1.add(new Item("extRasterTools"));
		v1.add(new Item("extScripting"));
		v1.add(new Item("extWCS"));
		v1.add(new Item("extWFS2"));
		v1.add(new Item("extwfs2"));
		v1.add(new Item("extWMS"));
		v1.add(new Item("extWMS"));
		v1.add(new Item("extWCS"));
		v1.add(new Item("7"));
		v1.add(new Item("9"));
		v1.add(new Item("8"));
		v1.add(new Item("0"));
		v1.add(new Item("EXTWCS"));
		v1.add(new Item("Ñandú"));
		v1.add(new Item("extensión"));
		v1.add(new Item("ÑANDÚ"));
		v1.add(new Item("_fwAndami"));
		v1.add(new Item("abcdefghijklmnñopqrstuvwxyz"));
		v1.add(new Item("ç"));
		v1.add(new Item("appgvSIG"));
		v1.add(new Item("la casa"));
		v1.add(new Item("la montaña"));
		v1.add(new Item("la colina"));
		v1.add(new Item("las abejas"));
		v1.add(new Item("las nutrias"));
		v1.add(new Item("las ballenas"));
		v1.add(new Item("lasaña"));
		v1.add(new Item("Vector"));
		v1.add(new Item("Çe"));

		// Initialize the vector of Strings (for test methods with the parameter Comparator)
		v2 = new Vector();
		v2.add("extWMS");
		v2.add("libUI");
		v2.add("extWMS");
		v2.add("libRemoteServices");
		v2.add("extWFS2");
		v2.add("libNomenclatorIGN");
		v2.add("libNomenclatorIGN_GUI");
		v2.add("libIverUtiles");
		v2.add("extWFS2");
		v2.add("EXTWFS2");
		v2.add("extWFS2");
		v2.add("libGDBMS");
		v2.add("libInternationalization");
		v2.add("libFMap");
		v2.add("libuiDownCase");
		v2.add("6");
		v2.add("4");
		v2.add("3");
		v2.add("5");
		v2.add("2");
		v2.add("1");
		v2.add("extWFS2");
		v2.add("libExceptions");
		v2.add("libDriverManager");
		v2.add("libCq CMS for java");
		v2.add("libCorePlugin");
		v2.add("extWFS2");
		v2.add("extAddIDEELayers");
		v2.add("extAlertCClient");
		v2.add("extCAD");
		v2.add("extWFS2");
		v2.add("ÑandÚ");
		v2.add("ñandú");
		v2.add("extJDBC");
		v2.add("extMyPlugin");
		v2.add("extRasterTools");
		v2.add("extScripting");
		v2.add("extWCS");
		v2.add("extWFS2");
		v2.add("extwfs2");
		v2.add("extWMS");
		v2.add("extWMS");
		v2.add("extWCS");
		v2.add("7");
		v2.add("9");
		v2.add("8");
		v2.add("0");
		v2.add("EXTWCS");
		v2.add("Ñandú");
		v2.add("extensión");
		v2.add("ÑANDÚ");
		v2.add("_fwAndami");
		v2.add("abcdefghijklmnñopqrstuvwxyz");
		v2.add("ç");
		v2.add("appgvSIG");
		v2.add("la casa");
		v2.add("la montaña");
		v2.add("la colina");
		v2.add("las abejas");
		v2.add("las nutrias");
		v2.add("las ballenas");
		v2.add("lasaña");
		v2.add("Vector");
		v2.add("Çe");
		
		// Sort items of the two vectors
		Collator collator = Collator.getInstance(new Locale("es_ES"));
//		stringComparatorForItems.setLocaleRules(stringComparatorForItems.new LocaleRules(true, collator));
//		Collections.sort(v1.subList(0, v1.size()), stringComparatorForItems);
		
		// The algorithm of sort is the default used by Java (that makes some subsets: one for numbers, one for words that starts in big letters,
		//   another that stats in small letters, another with 'special' characters, etc)
		Collections.sort(v1.subList(0, v1.size())); 
		
		stringComparator.setLocaleRules(stringComparator.new LocaleRules(true, collator));
		Collections.sort(v2.subList(0, v2.size()), stringComparator);
	}

	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	///// TEST OPERATION: doSearchConsideringCaseSensitive(String, Vector) /////
	
	/**
	 * A test
	 */
	public void test1() {
		try {
			text = "";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v1);
			
			// List of results
			results = v1.subList(0, v1.size());
			
			System.out.println("Test 1:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test2() {
		try {
			text = "ext";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive("ext", v1);
			
			// List of results
			results = new ArrayList();
			results.add(new Item("extAddIDEELayers"));
			results.add(new Item("extAlertCClient"));
			results.add(new Item("extCAD"));
			results.add(new Item("extJDBC"));
			results.add(new Item("extMyPlugin"));
			results.add(new Item("extRasterTools"));
			results.add(new Item("extScripting"));
			results.add(new Item("extWCS"));
			results.add(new Item("extWCS"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWMS"));
			results.add(new Item("extWMS"));
			results.add(new Item("extWMS"));
			results.add(new Item("extWMS"));
			results.add(new Item("extensión"));
			results.add(new Item("extwfs2"));

			System.out.println("\nTest 2:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A test
	 */
	public void test3() {
		try {
			text = "libUI";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v1);
			
			// List of results
			results = new ArrayList();
			results.add(new Item("libUI"));
			
			System.out.println("\nTest 3:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test4() {
		try {
			text = "extWFS2";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v1);
			
			// List 4 of results
			results = new ArrayList();
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));		
			
			System.out.println("\nTest 4:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test5() {
		try {
			text = "extWFS2a";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v1);
			
			// List of results (without elements)
			results = null;
			
			System.out.println("\nTest 5:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test6() {
		try {
			text = "ñandú";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v1);
			
			// List of results
			results = new ArrayList();
			results.add(new Item("ñandú"));
			
			System.out.println("\nTest 6:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test7() {
		try {
			text = "ÑANDÚ";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v1);
			
			// List of results
			results = new ArrayList();
			results.add(new Item("ÑANDÚ"));
			
			System.out.println("\nTest 7:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test8() {
		try {
			text = "la ";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v1);
	
			// List of results
			results = new ArrayList();
			results.add(new Item("la casa"));
			results.add(new Item("la colina"));
			results.add(new Item("la montaña"));
			
			System.out.println("\nTest 8:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test9() {
		try {
			text = "livUI";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v1);
			
			// List of results (without elements)
			results = null;
			
			System.out.println("\nTest 9:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test9_1() {
		try {
			text = "libui";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v1);
			
			// List of results (without elements)
			results = new ArrayList();
			results.add(new Item("libuiDownCase"));
			
			System.out.println("\nTest 9_1:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test10() {
		try {
			text = "ç";
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v1);
			
			// List of results (without elements)
			results = new ArrayList();
			results.add(new Item("ç"));
			
			System.out.println("\nTest 10:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	///// END TEST OPERATION: doSearchConsideringCaseSensitive(String, Vector) /////
	
	///// TEST OPERATION: doSearchWithoutConsideringCaseSensitive(String, Vector) /////
	
	/**
	 * A test
	 */
	public void test11() {
		try {
			text = "";
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v1);
			
			// List of results
			results = v1.subList(0, v1.size());
				
			System.out.println("\nTest 11:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results)) {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test12() {
		try {
			text = "ext";
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v1);
			
			// List of results
			results = new ArrayList();
			results.add(new Item("extAddIDEELayers"));
			results.add(new Item("extAlertCClient"));
			results.add(new Item("extCAD"));
			results.add(new Item("extJDBC"));
			results.add(new Item("extMyPlugin"));
			results.add(new Item("extRasterTools"));
			results.add(new Item("extScripting"));
			results.add(new Item("extWCS"));
			results.add(new Item("extWCS"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWMS"));
			results.add(new Item("extWMS"));
			results.add(new Item("extWMS"));
			results.add(new Item("extWMS"));
			results.add(new Item("extensión"));
			results.add(new Item("extwfs2"));
							
			System.out.println("Test 12:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results)) {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test13() {
		try {
			text = "libUI";
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v1);

			// List of results
			results = new ArrayList();
			results.add(new Item("libUI"));
			results.add(new Item("libuiDownCase"));
					
			System.out.println("Test 13:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results)) {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test14() {
		try {
			text = "extWFS2";
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v1);
			
			// List of results
			results = new ArrayList();
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extWFS2"));
			results.add(new Item("extwfs2"));

			System.out.println("Test 14:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results)) {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test15() {
		try {
			text = "extWFS2a";
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v1);
			
			// List of results (without elements)
			results = null;
							
			System.out.println("Test 15:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results)) {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test16() {
		try {
			text = "ç";
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v1);
			
			// List of results
			results = new ArrayList();
			results.add(new Item("Çe"));
			results.add(new Item("ç"));
					
			System.out.println("Test 16:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results)) {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test17() {
		try {
			text = "La ";
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v1);
			
			// List of results
			results = new ArrayList();
			results.add(new Item("la casa"));
			results.add(new Item("la colina"));
			results.add(new Item("la montaña"));

			System.out.println("Test 17:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results)) {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test18() {
		try {
			text = "livUI";
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v1);
			
			// List of results
			results = null;

			System.out.println("Test 18:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results)) {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test18_1() {
		try {
			text = "libui";
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v1);
			
			// List of results
			results = new ArrayList();
			results.add(new Item("libUI"));
			results.add(new Item("libuiDownCase"));

			System.out.println("Test 18_1:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results)) {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test19() {
		try {
			text = "ÑANDÚ";
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v1);
			
			// List of results
			results = new ArrayList();
			results.add(new Item("ÑANDÚ"));
			results.add(new Item("ÑandÚ"));
			results.add(new Item("Ñandú"));
			results.add(new Item("ñandú"));

			System.out.println("Test 19:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results)) {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> OK");
			} else {
				System.out.println("Op: doSearchIgnoringCaseSensitive(String, Vector) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	///// END TEST OPERATION: doSearchWithoutConsideringCaseSensitive(String, Vector) /////
	
	///// TEST OPERATION: doSearchConsideringCaseSensitive(String, Vector, Comparator) /////
	/**
	 * A test
	 */
	public void test21() {
		try {
			text = "";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = v2.subList(0, v2.size());

			System.out.println("Test 21:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test22() {
		try {
			text = "ext";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = new ArrayList();
			results.add("extAddIDEELayers");
			results.add("extAlertCClient");
			results.add("extCAD");
			results.add("extensión");
			results.add("extJDBC");
			results.add("extMyPlugin");
			results.add("extRasterTools");
			results.add("extScripting");
			results.add("extWCS");
			results.add("extWCS");
			results.add("extwfs2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWMS");
			results.add("extWMS");
			results.add("extWMS");
			results.add("extWMS");

						
			System.out.println("\nTest 22:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A test
	 */
	public void test23() {
		try {
			text = "libUI";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = new ArrayList();
			results.add("libUI");
						
			System.out.println("\nTest 23:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test24() {
		try {
			text = "extWFS2";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator);
			
			// List 4 of results
			results = new ArrayList();
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");		
						
			System.out.println("\nTest 24:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test25() {
		try {
			text = "extWFS2a";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator);
			
			// List of results (without elements)
			results = null;
						
			System.out.println("\nTest 25:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test26() {
		try {
			text = "ñandú";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = new ArrayList();
			results.add("ñandú");
						
			System.out.println("\nTest 26:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test27() {
		try {
			text = "ÑANDÚ";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = new ArrayList();
			results.add("ÑANDÚ");
						
			System.out.println("\nTest 27:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test28() {
		try {
			text = "la ";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = new ArrayList();
			results.add("la casa");
			results.add("la colina");
			results.add("la montaña");

			System.out.println("\nTest 28:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test29() {
		try {
			text = "livUI";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator);
			
			// List of results (without elements)
			results = null;
						
			System.out.println("\nTest 29:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test29_1() {
		try {
			text = "libui";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator); //<- Esta búsqueda
			
			// List of results (without elements)
			results = new ArrayList();
			results.add("libuiDownCase");
						
			System.out.println("\nTest 29_1:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test30() {
		try {
			text = "ç";
			stringComparator.setCaseSensitive(true);
			list = BinarySearchUsingFirstCharacters.doSearchConsideringCaseSensitive(text, v2, stringComparator);
			
			// List of results (without elements)
			results = new ArrayList();
			results.add("ç");
						
			System.out.println("\nTest 30:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compare(list, results, stringComparator)) {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		 } catch (Exception e) {
			e.printStackTrace();
		}
	}
	///// END TEST OPERATION: doSearchConsideringCaseSensitive(String, Vector, Comparator) /////

	///// TEST OPERATION: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) /////
	
	/**
	 * A test
	 */
	public void test31() {
		try {
			text = "";
			stringComparator.setCaseSensitive(false);
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = v2.subList(0, v2.size());

			System.out.println("\nTest 31:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
			
			if (CompareLists.compareIgnoringCaseSensitive(list, results, stringComparator)) {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test32() {
		try {
			text = "ext";		
			stringComparator.setCaseSensitive(false);
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v2, stringComparator);
		
			// List of results
			results = new ArrayList();
			results.add("extAddIDEELayers");
			results.add("extAlertCClient");
			results.add("extCAD");
			results.add("extensión");
			results.add("extJDBC");
			results.add("extMyPlugin");
			results.add("extRasterTools");
			results.add("extScripting");
			results.add("extWCS");
			results.add("extWCS");
			results.add("EXTWCS");
			results.add("extwfs2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("EXTWFS2");
			results.add("extWMS");
			results.add("extWMS");
			results.add("extWMS");
			results.add("extWMS");
							
			System.out.println("Test 32:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results, stringComparator)) {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test33() {
		try {
			text = "LIBui";		
			stringComparator.setCaseSensitive(false);
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v2, stringComparator);
				
			// List of results
			results = new ArrayList();
			results.add("libUI");
			results.add("libuiDownCase");
			
			System.out.println("Test 33:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results, stringComparator)) {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test34() {
		try {
			text = "extWFS2";		
			stringComparator.setCaseSensitive(false);
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = new ArrayList();
			results.add("extwfs2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("extWFS2");
			results.add("EXTWFS2");

			System.out.println("Test 34:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results, stringComparator)) {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test35() {
		try {
			text = "extWFS2a";		
			stringComparator.setCaseSensitive(false);
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v2, stringComparator);
			
			// List of results (without elements)
			results = null;
							
			System.out.println("Test 35:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results, stringComparator)) {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test36() {
		try {
			text = "ÑaNdÚ";		
			stringComparator.setCaseSensitive(false);
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = new ArrayList();
			results.add("ñandú");
			results.add("Ñandú");
			results.add("ÑandÚ");
			results.add("ÑANDÚ");
					
			System.out.println("Test 36:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results, stringComparator)) {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test37() {
		try {
			text = "La ";		
			stringComparator.setCaseSensitive(false);
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = new ArrayList();
			results.add("la casa");
			results.add("la colina");
			results.add("la montaña");

			System.out.println("Test 37:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results, stringComparator)) {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test38() {
		try {
			text = "livUI";		
			stringComparator.setCaseSensitive(false);
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = null;

			System.out.println("Test 38:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results, stringComparator)) {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test38_1() {
		try {
			text = "libui";		
			stringComparator.setCaseSensitive(false);
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = new ArrayList();
			results.add("libUI");
			results.add("libuiDownCase");

			System.out.println("Test 38_1:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results, stringComparator)) {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A test
	 */
	public void test39() {
		try {
			text = "ç";		
			stringComparator.setCaseSensitive(false);
			list = BinarySearchUsingFirstCharacters.doSearchIgnoringCaseSensitive(text, v2, stringComparator);
			
			// List of results
			results = new ArrayList();
			results.add("ç");
			results.add("Çe");

			System.out.println("Test 38:\nText: " + text + "\nResults of the search: " + list + "\nResults expected: " + results);
				
			if (CompareLists.compareIgnoringCaseSensitive(list, results, stringComparator)) {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> OK");
			} else {
				System.out.println("Op: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) -> FAILED");
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	///// END TEST OPERATION: doSearchWithoutConsideringCaseSensitive(String, Vector, Comparator) /////

	///// AUXILIAR CLASS THAT REPRESENTS AN ITEM WITH AN STRING VALUE AND IMPLEMENTING THE COMPARABLE INTERFACE /////
	
	/**
	 * This class is used for represent an item, with an string value, and implementing the interface Comparable for allow searches.
	 * 
	 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
	 */
	private static class Item implements Comparable {
		String value;

		/**
		 * Default contructor with 1 parameter
		 * 
		 * @param v1 Text value
		 */
		public Item (String v) {
			value = v;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return value;
		}

		/*
		 *  (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object o) {
			if (!(o instanceof Item))
				throw new ClassCastException("Se esperaba un objeto de tipo Item.");

			return this.value.compareTo(o.toString());
		}
	}
	
	///// AUXILIAR CLASS THAT REPRESENTS AN ITEM WITH AN STRING VALUE AND IMPLEMENTING THE COMPARABLE INTERFACE /////
	
    ///// END AUXILIAR CLASS THAT HAS A METHOD FOR COMPARE TWO ITEMS /////

	/**
	 * Compares two items (with String value) alphabetically
	 *
	 * @author Pablo Piqueras Bartolomé
	 */
	public class StringComparatorForItems implements Comparator {
		private boolean caseSensitive = true;
		private LocaleRules localeRules = null;

	    /**
	     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	     */
	    public int compare(Object o1, Object o2) {
	        String s1 = ((Item) o1).toString();
	        String s2 = ((Item) o2).toString();

	        // If localeRules is null -> use the default rules
	        if (localeRules == null) {
	        	if (caseSensitive) {
	        		return s1.compareTo(s2);
	        	}
	        	else {
	        		return s1.compareToIgnoreCase(s2);
	        	}
	        }
	        else {
	        	if (localeRules.isUseLocaleRules()) {
	        		Collator collator = localeRules.getCollator();
	        		
	        		if (caseSensitive) {
	        			return collator.compare(s1, s2);
	        		}
	        		else {
	        			return collator.compare(s1.toLowerCase(), s2.toLowerCase());
	        		}
	        	}
	        	else {
	            	if (caseSensitive) {
	            		return s1.compareTo(s2);
	            	}
	            	else {
	            		return s1.compareToIgnoreCase(s2);
	            	}
	        	}
	        }
	    }

	    /**
	     * Returns if the comparator is sensitive to small and big letters
	     *
	     * @return
	     */
	    public boolean isCaseSensitive() {
	        return caseSensitive;
	    }

	    /**
	     * Establece la sensibilidad del comparador a las mayusculas y minusculas
	     *
	     * @param b
	     */
	    public void setCaseSensitive(boolean b) {
	        caseSensitive = b;
	    }
	    
	    /**
	     * Gets an object with the information for use the locale rules in comparation between strings. <br>
	     * <ul>
	     * <li>A boolean value -> if want or not use the locale rules</li>
	     * <li>A reference to the locale rules</li>
	     * </ul>
	     * 
	     * @return @see LocaleRules
	     */
	    public LocaleRules getLocaleRules() {
	    	return localeRules;
	    }    
	    /**
	     * Sets an object with the information for use the locale rules in comparation between strings. <br>
	     * <ul>
	     * <li>A boolean value -> if want or not use the locale rules</li>
	     * <li>A reference to the locale rules</li>
	     * </ul>
	     * 
	     * @param @see LocaleRules
	     */
	    public void setLocaleRules(LocaleRules locRules) {
	    	localeRules = locRules;
	    }
	    
	    /**
	     * Represents the information needed by <i>StringComparator</i> for use or not locale-sensitive String comparison-rules in the <b><i>compare</i></b> method
	     * 
	     * @author Pablo Piqueras Bartolomé
	     */
	    public class LocaleRules {
	    	 private boolean useLocaleRules;
	    	 private Collator _collator;
	    	 
	    	 /**
	    	  * Default constructor without parameters
	    	  */
	    	 public LocaleRules() {
	    		 useLocaleRules = false;
	    		 _collator = null;
	    	 }
	    	 
	    	 /**
	    	  * Default constructor with two parameters
	    	  * 
	    	  * @param b Use locale rules
	    	  * @param collator A reference to an object configurated for locale-sensitive String comparison
	    	  */
	    	 public LocaleRules(boolean b, Collator collator) {
	    		 useLocaleRules = b;
	    		 _collator = collator;
	    	 }
	    	 
	 		/**
	 		 * Gets the value of the inner attribute <i>_collator</i>
	 		 * 
	 		 * @return Returns A reference to an object configurated for locale-sensitive String comparison
	 		 */
	 		public Collator getCollator() {
	 			return _collator;
	 		}

	 		/**
	 		 * Sets a value to the inner attribute <i>_collator</i>
	 		 * 
	 		 * @param collator A reference to an object configurated for locale-sensitive String comparison
	 		 */
	 		public void setCollator(Collator collator) {
	 			this._collator = collator;
	 		}

			/**
			 * Gets the value of the inner attribute <i>useLocaleRules</i>
			 * 
			 * @return Returns the useLocaleRules.
			 */
			public boolean isUseLocaleRules() {
				return useLocaleRules;
			}

			/**
			 * Sets a value to the inner attribute <i>useLocaleRules</i>
			 * 
			 * @param useLocaleRules The useLocaleRules to set.
			 */
			public void setUseLocaleRules(boolean useLocaleRules) {
				this.useLocaleRules = useLocaleRules;
			}
	    }
	}
	///// END AUXILIAR CLASS THAT HAS A METHOD FOR COMPARE TWO ITEMS /////
}

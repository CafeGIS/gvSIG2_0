package com.iver.utiles.vectorUtilities;

import java.text.Collator;
import java.util.Locale;
import java.util.Vector;

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
 * Tests the methods of {@link VectorUtilities}
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestVectorUtilities extends TestCase {
	private static String obj;
	private static Vector v1 = new Vector();
	private static Vector v2 = new Vector();
	private static StringComparator stringComparator;
	
	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		// Initialize the StringComparator
		stringComparator = new StringComparator();

	    // Set spanish rules and with case sensitive
		Collator collator = Collator.getInstance(new Locale("es_ES"));		
		stringComparator.setLocaleRules(stringComparator.new LocaleRules(true, collator));
		stringComparator.setCaseSensitive(false);
	}

	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	///// TEST OPERATION: addAlphabeticallyOrdered(Vector, Object) /////

	/**
	 * A test
	 */
	public void test1() {
		try {
			// Insert at the beginning
			obj = new String("First");
			
			VectorUtilities.addAlphabeticallyOrdered(v1, obj);
			
			System.out.println("Test 1:\nObj: " + obj + "\nResults: " + v1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test2() {
		try {
			// Insert at the end
			obj = new String("ZZÑÑÇÇ");
			
			VectorUtilities.addAlphabeticallyOrdered(v1, obj);
			
			System.out.println("Test 2:\nObj: " + obj + "\nResults: " + v1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test3() {
		try {
			// An insertion
			obj = new String("Ñandú");
			
			VectorUtilities.addAlphabeticallyOrdered(v1, obj);
			
			System.out.println("Test 3:\nObj: " + obj + "\nResults: " + v1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test4() {
		try {
			// An insertion
			obj = new String("ÑANDÚ");
			
			VectorUtilities.addAlphabeticallyOrdered(v1, obj);
			
			System.out.println("Test 4:\nObj: " + obj + "\nResults: " + v1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test5() {
		try {
			// An insertion
			obj = new String("appgvSIG");
			
			VectorUtilities.addAlphabeticallyOrdered(v1, obj);
			
			System.out.println("Test 5:\nObj: " + obj + "\nResults: " + v1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test6() {
		try {
			// An insertion
			obj = new String("libIverUtiles");
			
			VectorUtilities.addAlphabeticallyOrdered(v1, obj);
			
			System.out.println("Test 6:\nObj: " + obj + "\nResults: " + v1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A test
	 */
	public void test7() {
		try {
			// An insertion
			obj = new String("libUI");
			
			VectorUtilities.addAlphabeticallyOrdered(v1, obj);
			
			System.out.println("Test 7:\nObj: " + obj + "\nResults: " + v1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test8() {
		try {
			// An insertion
			obj = new String("libIverUtiles");
			
			VectorUtilities.addAlphabeticallyOrdered(v1, obj);
			
			System.out.println("Test 8:\nObj: " + obj + "\nResults: " + v1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * A test
	 */
	public void test9() {
		try {
			// An insertion
			obj = new String("extWFS");
			
			VectorUtilities.addAlphabeticallyOrdered(v1, obj);
			
			System.out.println("Test 9:\nObj: " + obj + "\nResults: " + v1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	///// END TEST OPERATION: addAlphabeticallyOrdered(Vector, Object) /////
	
	///// TEST OPERATION: addAlphabeticallyOrdered(Vector, Object, Comparator) /////
	
	/**
	 * A test
	 */
	public void test11() {
		try {
			// Insert at the beginning
			obj = new String("First");
			
			VectorUtilities.addAlphabeticallyOrdered(v2, obj, stringComparator);
			
			System.out.println("Test 11:\nObj: " + obj + "\nResults: " + v2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test12() {
		try {
			// Insert at the end
			obj = new String("ZZÑÑÇÇ");
			
			VectorUtilities.addAlphabeticallyOrdered(v2, obj, stringComparator);
			
			System.out.println("Test 12:\nObj: " + obj + "\nResults: " + v2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test13() {
		try {
			// An insertion
			obj = new String("Ñandú");
			
			VectorUtilities.addAlphabeticallyOrdered(v2, obj, stringComparator);
			
			System.out.println("Test 13:\nObj: " + obj + "\nResults: " + v2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test14() {
		try {
			// An insertion
			obj = new String("ÑANDÚ");
			
			VectorUtilities.addAlphabeticallyOrdered(v2, obj, stringComparator);
			
			System.out.println("Test 14:\nObj: " + obj + "\nResults: " + v2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test15() {
		try {
			// An insertion
			obj = new String("appgvSIG");
			
			VectorUtilities.addAlphabeticallyOrdered(v2, obj, stringComparator);
			
			System.out.println("Test 15:\nObj: " + obj + "\nResults: " + v2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test16() {
		try {
			// An insertion
			obj = new String("libIverUtiles");
			
			VectorUtilities.addAlphabeticallyOrdered(v2, obj, stringComparator);
			
			System.out.println("Test 16:\nObj: " + obj + "\nResults: " + v2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * A test
	 */
	public void test17() {
		try {
			// An insertion
			obj = new String("libUI");
			
			VectorUtilities.addAlphabeticallyOrdered(v2, obj, stringComparator);
			
			System.out.println("Test 17:\nObj: " + obj + "\nResults: " + v2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A test
	 */
	public void test18() {
		try {
			// An insertion
			obj = new String("libIverUtiles");
			
			VectorUtilities.addAlphabeticallyOrdered(v2, obj, stringComparator);
			
			System.out.println("Test 18:\nObj: " + obj + "\nResults: " + v2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * A test
	 */
	public void test19() {
		try {
			// An insertion
			obj = new String("extWFS");
			
			VectorUtilities.addAlphabeticallyOrdered(v2, obj, stringComparator);
			
			System.out.println("Test 19:\nObj: " + obj + "\nResults: " + v2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	///// END TEST OPERATION: addAlphabeticallyOrdered(Vector, Object, Comparator) /////
}

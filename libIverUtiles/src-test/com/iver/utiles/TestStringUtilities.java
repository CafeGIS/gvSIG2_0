package com.iver.utiles;

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
 * This class tests some methods of the 'StringUtilities' class
 * (Now only tests the methods 'numberOfOccurrencesOfSubStringInString' and 'numberOfOccurrencesOfSubStringInStringUpToPosition')
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestStringUtilities extends TestCase{
	private String word_String;
	private String word_subString;
	private boolean case_Sensitive;
	private int start_position;
	private int end_position;
	
	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		word_String = new String();
		word_subString = new String();
		case_Sensitive = true; // By default, case sensitive		
	}

	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * A test
	 */
	public void test1() {
		word_String = "la casa es grande";
		word_subString = "a";
		case_Sensitive = true;

		System.out.print("Test1: El número de ocurrencias");
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInString(word_String, word_subString, case_Sensitive));
	}
	
	/**
	 * A test
	 */
	public void test2() {
		word_String = "la casa es grande";
		word_subString = "a";
		case_Sensitive = false;

		System.out.print("Test2: El número de ocurrencias");
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInString(word_String, word_subString, case_Sensitive));
	}

	/**
	 * A test
	 */
	public void test3() {
		word_String = "la casa es grande";
		word_subString = "A";
		case_Sensitive = true;

		System.out.print("Test3: El número de ocurrencias");
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInString(word_String, word_subString, case_Sensitive));
	}

	/**
	 * A test
	 */
	public void test4() {
		word_String = "la casa es grande";
		word_subString = "A";
		case_Sensitive = false;

		System.out.print("Test4: El número de ocurrencias");
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInString(word_String, word_subString, case_Sensitive));
	}

	/**
	 * A test
	 */
	public void test5() {
		word_String = "la casa es grande";
		word_subString = "z";
		case_Sensitive = true;

		System.out.print("Test5: El número de ocurrencias");
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInString(word_String, word_subString, case_Sensitive));
	}

	/**
	 * A test
	 */
	public void test6() {
		word_String = "la casa es grande";
		word_subString = "z";
		case_Sensitive = false;

		System.out.print("Test6: El número de ocurrencias");
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInString(word_String, word_subString, case_Sensitive));
	}
	
	/**
	 * A test
	 */
	public void test7() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos dos";
		case_Sensitive = true;

		System.out.print("Test7: El número de ocurrencias");
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInString(word_String, word_subString, case_Sensitive));
	}
	
	/**
	 * A test
	 */
	public void test8() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos Dos";
		case_Sensitive = true;

		System.out.print("Test8: El número de ocurrencias");
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInString(word_String, word_subString, case_Sensitive));
	}
	
	/**
	 * A test
	 */
	public void test9() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos dos";
		case_Sensitive = false;

		System.out.print("Test9: El número de ocurrencias");
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInString(word_String, word_subString, case_Sensitive));
	}
	
	/**
	 * A test
	 */
	public void test10() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos Dos";
		case_Sensitive = false;

		System.out.print("Test10: El número de ocurrencias");
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInString(word_String, word_subString, case_Sensitive));
	}
	
	/**
	 * A test
	 */
	public void test11() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos Dos";
		case_Sensitive = false;
		start_position = 0;
		end_position = word_String.length();

		System.out.print("Test11: El número de ocurrencias entre posiciones " + start_position + " y " + end_position);
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInStringBetweenPositions(word_String, word_subString, case_Sensitive, start_position, end_position));
	}
	
	/**
	 * A test
	 */
	public void test12() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos dos";
		case_Sensitive = true;
		start_position = 20;
		end_position = 72;

		System.out.print("Test12: El número de ocurrencias entre posiciones " + start_position + " y " + end_position);
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInStringBetweenPositions(word_String, word_subString, case_Sensitive, start_position, end_position));
	}

	/**
	 * A test
	 */
	public void test13() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos Dos";
		case_Sensitive = true;
		start_position = 20;
		end_position = 72;

		System.out.print("Test13: El número de ocurrencias entre posiciones " + start_position + " y " + end_position);
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInStringBetweenPositions(word_String, word_subString, case_Sensitive, start_position, end_position));
	}
	
	/**
	 * A test
	 */
	public void test14() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos dos";
		case_Sensitive = false;
		start_position = 20;
		end_position = 72;

		System.out.print("Test14: El número de ocurrencias entre posiciones " + start_position + " y " + end_position);
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInStringBetweenPositions(word_String, word_subString, case_Sensitive, start_position, end_position));
	}
	
	/**
	 * A test
	 */
	public void test15() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos Dos";
		case_Sensitive = false;
		start_position = 20;
		end_position = 72;

		System.out.print("Test15: El número de ocurrencias entre posiciones " + start_position + " y " + end_position);
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInStringBetweenPositions(word_String, word_subString, case_Sensitive, start_position, end_position));
	}
	
	/**
	 * A test
	 */
	public void test16() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos dos";
		case_Sensitive = true;
		start_position = 21;
		end_position = 72;

		System.out.print("Test16: El número de ocurrencias entre posiciones " + start_position + " y " + end_position);
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInStringBetweenPositions(word_String, word_subString, case_Sensitive, start_position, end_position));
	}

	/**
	 * A test
	 */
	public void test17() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos Dos";
		case_Sensitive = true;
		start_position = 21;
		end_position = 72;

		System.out.print("Test17: El número de ocurrencias entre posiciones " + start_position + " y " + end_position);
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInStringBetweenPositions(word_String, word_subString, case_Sensitive, start_position, end_position));
	}
	
	/**
	 * A test
	 */
	public void test18() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos dos";
		case_Sensitive = false;
		start_position = 21;
		end_position = 72;

		System.out.print("Test18: El número de ocurrencias entre posiciones " + start_position + " y " + end_position);
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInStringBetweenPositions(word_String, word_subString, case_Sensitive, start_position, end_position));
	}
	
	/**
	 * A test
	 */
	public void test19() {
		word_String = "Frasecica: tres mas cuAtro menos dos no es lo mismo que cuatro menos dos mas cinco ni que cuatro menos dos mas dos ni que cuatro menos cuatro mas diez. ¿Cierto no?";
		word_subString = "cuatro menos Dos";
		case_Sensitive = false;
		start_position = 21;
		end_position = 72;

		System.out.print("Test19: El número de ocurrencias entre posiciones " + start_position + " y " + end_position);
		
		if (case_Sensitive)
			System.out.print(" con distinción minúsculas/mayúsculas ");
		else
			System.out.print(" sin distinción minúsculas/mayúsculas ");
		
		System.out.println("de '" + word_subString + "' en '" + word_String + " es " + StringUtilities.numberOfOccurrencesOfSubStringInStringBetweenPositions(word_String, word_subString, case_Sensitive, start_position, end_position));
	}
}

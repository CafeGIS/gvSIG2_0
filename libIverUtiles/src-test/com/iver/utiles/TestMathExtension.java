package com.iver.utiles;
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
 * Tests for the MathExtension class
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestMathExtension {
	/**
	 * Test method for the TestMathExtension class
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Test 1
		System.out.println("Test 1 -> log2(1)");
		showResultState(0, MathExtension.log2(1), 1);

		// Test 2
		System.out.println("Test 2 -> log2(16)");
		showResultState(4, MathExtension.log2(16), 2);

		// Test 3
		System.out.println("Test 3 -> log2Integer(15)");
		showResultState(3, MathExtension.log2Integer(15), 3);
		
		// Test 4
		System.out.println("Test 4 -> log2Integer(64)");
		showResultState(6, MathExtension.log2Integer(64), 4);
		
		// Test 5
		System.out.println("Test 5 -> logX(e2, e6)");
		showResultState(3, MathExtension.logX(Math.pow(Math.E, 2), Math.pow(Math.E, 6)), 5);
		
		// Test 6
		System.out.println("Test 6 -> logXInteger(5, 137.43)");
		showResultState(3, MathExtension.logXInteger(5, 137.43), 6);

		
		// There might be more tests
		
	}
	
	/**
	 * Indicates if the result of the current test has been successfull or not
	 * 
	 * @param correct The correct result
	 * @param result The result of the operation
	 * @param testNumber The numer of the current test
	 */
	private static void showResultState(double correct, double result, int testNumber) {
		if (correct == result)
			System.out.println("Test " + testNumber + " OK    (Result: " + result + ")");
		else
			System.out.println("Test " + testNumber + " FAILED    (Result: " + result + ")");
	}
}

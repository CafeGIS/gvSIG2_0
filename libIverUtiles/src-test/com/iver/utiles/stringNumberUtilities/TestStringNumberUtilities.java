package com.iver.utiles.stringNumberUtilities;

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
 * Tests the methods of the class StringNumberUtilities
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestStringNumberUtilities extends TestCase{
	private String word;
	
	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		word = new String();
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
		word = "2.3";
		ShowText.showIsNumberText(word);

		if (StringNumberUtilities.isNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test2() {
		word = "a as wz rwa";
		ShowText.showIsNumberText(word);

		if (StringNumberUtilities.isNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test3() {
		word = "2";
		ShowText.showIsNumberText(word);

		if (StringNumberUtilities.isNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test4() {
		word = "";
		ShowText.showIsNumberText(word);

		if (StringNumberUtilities.isNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test5() {
		word = "-2.3";
		ShowText.showIsNumberText(word);

		if (StringNumberUtilities.isNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test6() {
		word = "-300";
		ShowText.showIsNumberText(word);

		if (StringNumberUtilities.isNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test7() {
		word = "2.3";
		ShowText.showIsNaturalText(word);

		if (StringNumberUtilities.isNaturalNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test8() {
		word = "334";
		ShowText.showIsNaturalText(word);

		if (StringNumberUtilities.isNaturalNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test9() {
		word = "-2a3";
		ShowText.showIsIntegerText(word);

		if (StringNumberUtilities.isIntegerNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test10() {
		word = "-23";
		ShowText.showIsIntegerText(word);

		if (StringNumberUtilities.isIntegerNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test11() {
		word = "7";
		ShowText.showIsIntegerText(word);

		if (StringNumberUtilities.isIntegerNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test12() {
		word = "2.3";
		ShowText.showIsIntegerText(word);

		if (StringNumberUtilities.isIntegerNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test13() {
		word = "2.3";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test14() {
		word = "37";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test15() {
		word = "-4.6";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test16() {
		word = "-8000";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test17() {
		word = "-80k00";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
		
	/**
	 * A test
	 */
	public void test18() {
		word = ".6";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test19() {
		word = "-.6";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test20() {
		word = "b-.6";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	
	/**
	 * A test
	 */
	public void test21() {
		word = "-c.6";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	
	/**
	 * A test
	 */
	public void test22() {
		word = "-.d6";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test23() {
		word = "-.6e";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test24() {
		word = "+.6";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test25() {
		word = "+.6";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	
	/**
	 * A test
	 */
	public void test26() {
		word = ".6";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	
	/**
	 * A test
	 */
	public void test27() {
		word = "-.6";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	
	/**
	 * A test
	 */
	public void test28() {
		word = "+6";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	
	/**
	 * A test
	 */
	public void test29() {
		word = "-6";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	
	/**
	 * A test
	 */
	public void test30() {
		word = "6";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test31() {
		word = "136.13";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	
	/**
	 * A test
	 */
	public void test32() {
		word = "+13.42";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test33() {
		word = "-246.24";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test34() {
		word = "146.134E+13";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test35() {
		word = "614.425E-67";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test36() {
		word = "-8945.1201e48.141";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test37() {
		word = "-8945.1201e48.141.";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test38() {
		word = "8945.1201ee48.141";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
		
	/**
	 * A test
	 */
	public void test39() {
		word = "8945.1201E";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test40() {
		word = "8945.1201e+";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test41() {
		word = "8945.1201e+1";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	
	/**
	 * A test
	 */
	public void test42() {
		word = "8945.1201e+.32";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test43() {
		word = "+.6";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	
	/**
	 * A test
	 */
	public void test44() {
		word = ".6";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	
	/**
	 * A test
	 */
	public void test45() {
		word = "-.6";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	
	/**
	 * A test
	 */
	public void test46() {
		word = "+6";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	
	/**
	 * A test
	 */
	public void test47() {
		word = "-6";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	
	/**
	 * A test
	 */
	public void test48() {
		word = "6";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test49() {
		word = "136.13";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	
	/**
	 * A test
	 */
	public void test50() {
		word = "+13.42";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test51() {
		word = "-246.24";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test52() {
		word = "146.134E+13";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test53() {
		word = "614.425E-67";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test54() {
		word = "-8945.1201e48.141";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test55() {
		word = "-8945.1201e48.141.";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test56() {
		word = "8945.1201ee48.141";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
		
	/**
	 * A test
	 */
	public void test57() {
		word = "8945.1201E";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test58() {
		word = "8945.1201e+";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test59() {
		word = "8945.1201e+1";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test60() {
		word = "8945.1201e+.32";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test61() {
		word = "-";
		ShowText.showIsIntegerText(word);

		if (StringNumberUtilities.isIntegerNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test62() {
		word = "-";
		ShowText.showIsNaturalText(word);

		if (StringNumberUtilities.isNaturalNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test63() {
		word = "-";
		ShowText.showIsNumberText(word);

		if (StringNumberUtilities.isNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test64() {
		word = "-";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test65() {
		word = "-";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test66() {
		word = "-";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * A test
	 */
	public void test67() {
		word = "+";
		ShowText.showIsIntegerText(word);

		if (StringNumberUtilities.isIntegerNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test68() {
		word = "+";
		ShowText.showIsNaturalText(word);

		if (StringNumberUtilities.isNaturalNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test69() {
		word = "+";
		ShowText.showIsNumberText(word);

		if (StringNumberUtilities.isNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test70() {
		word = "+";
		ShowText.showIsRealText(word);

		if (StringNumberUtilities.isRealNumber(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test71() {
		word = "+";
		ShowText.showIsRealWithIntegerExponentText(word);

		if (StringNumberUtilities.isRealNumberWithIntegerExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}
	
	/**
	 * A test
	 */
	public void test72() {
		word = "+";
		ShowText.showIsRealWithRealExponentText(word);

		if (StringNumberUtilities.isRealNumberWithRealExponent(word)) {
			ShowText.showYes();
		}
		else {
			ShowText.showNo();
			fail();
		}
	}

	/**
	 * Shows a text sentence
	 * 
	 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
	 */
	private static class ShowText {
		/**
		 * Shows the text: Is number?
		 * 
		 * @param word An String
		 */
		public static void showIsNumberText(String word) {
			System.out.print("¿ Es número \'" + word + "\' ? ");
		}

		/**
		 * Shows the text: Is natural number?
		 * 
		 * @param word An String
		 */
		public static void showIsNaturalText(String word) {
			System.out.print("¿ Es número natural \'" + word + "\' ? ");
		}

		/**
		 * Shows the text: Is integer number?
		 * 
		 * @param word An String
		 */
		public static void showIsIntegerText(String word) {
			System.out.print("¿ Es número entero \'" + word + "\' ? ");
		}

		/**
		 * Shows the text: Is rational number?
		 * 
		 * @param word An String
		 */
		public static void showIsRealText(String word) {
			System.out.print("¿ Es número real \'" + word + "\' ? ");
		}
		
		/**
		 * Shows the text: Is integer exponent rational number?
		 * 
		 * @param word An String
		 */
		public static void showIsRealWithIntegerExponentText(String word) {
			System.out.print("¿ Es número real con exponente entero \'" + word + "\' ? ");
		}
		
		/**
		 * Shows the text: Is rational exponent rational number?
		 * 
		 * @param word An String
		 */
		public static void showIsRealWithRealExponentText(String word) {
			System.out.print("¿ Es número real con exponente real \'" + word + "\' ? ");
		}

		/**
		 * Shows the text: Yes
		 * 
		 * @param word An String
		 */
		public static void showYes() {
			System.out.println("Si.");
		}
		
		/**
		 * Shows the text: No
		 * 
		 * @param word An String
		 */
		public static void showNo() {
			System.out.println("No.");
		}
	}
}

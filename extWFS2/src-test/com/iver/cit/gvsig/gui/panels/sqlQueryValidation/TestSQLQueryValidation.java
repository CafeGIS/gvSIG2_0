package com.iver.cit.gvsig.gui.panels.sqlQueryValidation;

import junit.framework.TestCase;

import com.iver.cit.gvsig.gui.panels.sqlqueryvalidation.SQLQueryValidation;

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
 * Tests the methods of the class SQLQueryValidation
 *    (This class is made without inner static methods for don't create problems to Zql, in this way,
 *     it's possible to add more tests, with or without long queries.)
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
 public class TestSQLQueryValidation extends TestCase{
	
	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * A test (Correct)
	 */
	public void test1() {
		String query = "SELECT r.name, f.id FROM room r, flat f WHERE (r.user_name LIKE 'P%') AND (r.flat = f.id) AND (r.color_wall LIKE 'white') AND (r.height < 2.20);";
		
		System.out.println("¿Es válida '" + query + "' ?");
		SQLQueryValidation sqlQueryValidation = new SQLQueryValidation(query, false);

		if (sqlQueryValidation.validateQuery()) {
			System.out.println("Yes.");
		}
		else {
			System.out.println("No.");
			System.out.println(sqlQueryValidation.getErrorPositionAsMessage());
			System.out.println(sqlQueryValidation.getErrorMessage());
			fail();
		}
	}
	
	/**
	 * A test (Correct)
	 */
	public void test2() {
		String query = "SELECT * FROM House;";

		System.out.println("¿Es válida '" + query + "' ?");
		SQLQueryValidation sqlQueryValidation = new SQLQueryValidation(query, false);

		if (sqlQueryValidation.validateQuery()) {
			System.out.println("Yes.");
		}
		else {
			System.out.println("No.");
			System.out.println(sqlQueryValidation.getErrorPositionAsMessage());
			System.out.println(sqlQueryValidation.getErrorMessage());
			fail();
		}
	}

	/**
	 * A test (Incorrect)
	 */
	public void test3() {
		String query = "SELECT a* FROM House;";

		System.out.println("¿Es válida '" + query + "' ?");
		SQLQueryValidation sqlQueryValidation = new SQLQueryValidation(query, false);

		if (sqlQueryValidation.validateQuery()) {
			System.out.println("Yes.");
		}
		else {
			System.out.println("No.");
			System.out.println(sqlQueryValidation.getErrorPositionAsMessage());
			System.out.println(sqlQueryValidation.getErrorMessage());
			fail();
		}
	}
	
	/**
	 * A test (Correct)
	 */
	public void test4() {
		String query = "SELECT * FROM House;";

		System.out.println("¿Es válida '" + query + "' ?");
		SQLQueryValidation sqlQueryValidation = new SQLQueryValidation(query, false);

		if (sqlQueryValidation.validateQuery()) {
			System.out.println("Yes.");
		}
		else {
			System.out.println("No.");
			System.out.println(sqlQueryValidation.getErrorPositionAsMessage());
			System.out.println(sqlQueryValidation.getErrorMessage());
			fail();
		}
	}

	/**
	 * A test (Correct)
	 */
	public void test5() {
		String query = "r.level = f.level AND r.user_name LIKE \'P%\';";

		System.out.println("¿Es válida '" + query + "' ?");
		SQLQueryValidation sqlQueryValidation = new SQLQueryValidation(query, true);

		if (sqlQueryValidation.validateQuery()) {
			System.out.println("Yes.");
		}
		else {
			System.out.println("No.");
			System.out.println(sqlQueryValidation.getErrorPositionAsMessage());
			System.out.println(sqlQueryValidation.getErrorMessage());
			fail();
		}
	}

	/**
	 * A test (Incorrect)
	 */
	public void test6() {
		String query = "r.level = f.level a e3 w 	q3 	 º32	9'}97AND r.user_name LIKE \'P%\';";

		System.out.println("¿Es válida '" + query + "' ?");
		SQLQueryValidation sqlQueryValidation = new SQLQueryValidation(query, true);

		if (sqlQueryValidation.validateQuery()) {
			System.out.println("Yes.");
		}
		else {
			System.out.println("No.");
			System.out.println(sqlQueryValidation.getErrorPositionAsMessage());
			System.out.println(sqlQueryValidation.getErrorMessage());			
			fail();
		}
	}
 }
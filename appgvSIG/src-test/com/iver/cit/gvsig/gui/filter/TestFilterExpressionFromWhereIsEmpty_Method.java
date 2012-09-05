package com.iver.cit.gvsig.gui.filter;

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
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestFilterExpressionFromWhereIsEmpty_Method extends TestCase {
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
	 * Test 1 (valid)
	 */
	public void test1() {
		String expression = new String("select * from 'gdbms144426c_10fc90fa1aa__7c18' where ;");
		
		System.out.println("¿ Es vacío el filtro en: " + expression + " ? ");

		if (this.filterExpressionFromWhereIsEmpty(expression))
			System.out.println("Si.");
		else
			System.out.println("No.");
	}
	
	/**
	 * Test 2 (invalid)
	 */
	public void test2() {
		String expression = new String("select * from 'gdbms158fd70_10fc92ee61e__7c18' where layer < '61';");
		
		System.out.println("¿ Es vacío el filtro en: " + expression + " ? ");

		if (this.filterExpressionFromWhereIsEmpty(expression))
			System.out.println("Si.");
		else
			System.out.println("No.");
	}	
	
	/**
	 * Returns true if the WHERE subconsultation of the filterExpression is empty ("")
	 * 
	 * @param expression An string
	 * @return A boolean value 
	 */
	private boolean filterExpressionFromWhereIsEmpty(String expression) {
		String subExpression = expression.trim();
		int pos;	
		
		// Remove last ';' if exists
		if (subExpression.charAt(subExpression.length() -1) == ';')
			subExpression = subExpression.substring(0, subExpression.length() -1).trim();
		
		// If there is no 'where' clause
		if ((pos = subExpression.indexOf("where")) == -1)
			return false;
		
		// If there is no subexpression in the WHERE clause -> true
		subExpression = subExpression.substring(pos + 5, subExpression.length()).trim(); // + 5 is the length of 'where'
		if ( subExpression.length() == 0 )
			return true;
		else
			return false;
	}
}

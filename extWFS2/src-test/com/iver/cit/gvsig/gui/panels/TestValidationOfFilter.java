package com.iver.cit.gvsig.gui.panels;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import junit.framework.TestCase;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.sqlQueryValidation.SQLQueryValidation;
import com.iver.utiles.stringNumberUtilities.StringNumberUtilities;

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
 * This class tests a simply version of the method of validation a filter
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestValidationOfFilter extends TestCase{
	Map allFieldsAndValuesKnownOfCurrentLayer;
	Set _operatorSymbols = null;
	String query;
	
	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
				
		// Add some fields as example
		allFieldsAndValuesKnownOfCurrentLayer = new HashMap();

		allFieldsAndValuesKnownOfCurrentLayer.put("gid", new HashMap());
		allFieldsAndValuesKnownOfCurrentLayer.put("entity", new HashMap());
		allFieldsAndValuesKnownOfCurrentLayer.put("layer", new HashMap());
		allFieldsAndValuesKnownOfCurrentLayer.put("elevation", new HashMap());
		allFieldsAndValuesKnownOfCurrentLayer.put("color", new HashMap());
		allFieldsAndValuesKnownOfCurrentLayer.put("codigo", new HashMap());
		allFieldsAndValuesKnownOfCurrentLayer.put("tipo", new HashMap());
		allFieldsAndValuesKnownOfCurrentLayer.put("fecha", new HashMap());
		allFieldsAndValuesKnownOfCurrentLayer.put("id", new HashMap());
		
		// At beginning: no query
		query = new String();
	}

	/*
	 *  (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	///// TESTS /////
	
	/**
	 * A test (Correct)
	 */
	public void test1() {
		query = new String("\"codigo\" = 'Canal d'Elx'");
		
		System.out.println("Test 1: \n¿ Es válida '" + query + "' ? ");
		
		if (validateExpression(query))
			System.out.println("Si.");
		else
			System.out.println("No.");
	}
	
	/**
	 * A test (Correct)
	 */
	public void test2() {
		query = new String("\"codigo\" = 'Río de Buñol'");
		
		System.out.println("Test 2: \n¿ Es válida '" + query + "' ? ");
		
		if (validateExpression(query))
			System.out.println("Si.");
		else
			System.out.println("No.");
	}
	
	/**
	 * A test (Correct)
	 */
	public void test3() {
		query = new String("\"codigo\" = 'els Banys de la Tia Joana'");
		
		System.out.println("Test 3: \n¿ Es válida '" + query + "' ? ");
		
		if (validateExpression(query))
			System.out.println("Si.");
		else
			System.out.println("No.");
	}
	
	/**
	 * A test (Correct)
	 */
	public void test4() {
		query = new String("\"codigo\" = 'Barranc d'Adell'");
		
		System.out.println("Test 4: \n¿ Es válida '" + query + "' ? ");
		
		if (validateExpression(query))
			System.out.println("Si.");
		else
			System.out.println("No.");
	}
	
	/**
	 * A test (Correct)
	 */
	public void test5() {
		query = new String("\"codigo\" = 'els Banys de la Tia Joana' or \"fecha\" = Date(25-ene-2007) or \"fecha\" = Date(03-mar-2008) or \"codigo\" = 'Barranc d'Adell'");
		
		System.out.println("Test 5: \n¿ Es válida '" + query + "' ? ");
		
		if (validateExpression(query))
			System.out.println("Si.");
		else
			System.out.println("No.");
	}
	
	/**
	 * A test (Correct)
	 */
	public void test6() {
		query = new String(""); // Without query
		
		System.out.println("Test 6: \n¿ Es válida '" + query + "' ? ");
		
		if (validateExpression(query))
			System.out.println("Si.");
		else
			System.out.println("No.");
	}
	
	/**
	 * A test (Correct)
	 */
	public void test7() {
		query = new String("\"id\" = '354' and \"id\" > '697'");
		
		System.out.println("Test 7: \n¿ Es válida '" + query + "' ? ");
		
		if (validateExpression(query))
			System.out.println("Si.");
		else
			System.out.println("No.");
	}
		
	/**
	 * A test (Correct)
	 */
	public void test8() {
		query = new String("\"elevation\" = 354 and \"elevation\" > 697");
		
		System.out.println("Test 8: \n¿ Es válida '" + query + "' ? ");
		
		if (validateExpression(query))
			System.out.println("Si.");
		else
			System.out.println("No.");
	}
	
	/**
	 * A test (Correct)
	 */
	public void test9() {
		String query = "SELECT r.name, f.id FROM room r, flat f WHERE (r.user_name LIKE 'P%') AND (r.flat = f.id) AND (r.color_wall LIKE 'white') AND (r.height < 2.20)";
		
		System.out.println("Test 9: \n¿Es válida '" + query + "' ?");
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
	public void test10() {
		String query = "SELECT * FROM House";

		System.out.println("Test 10: \n¿Es válida '" + query + "' ?");
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
	public void test11() {
		String query = "SELECT a* FROM House";

		System.out.println("Test 11: \n¿Es válida '" + query + "' ?");
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
	public void test12() {
		String query = "SELECT * FROM House";

		System.out.println("Test 12: \n¿Es válida '" + query + "' ?");
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
	public void test13() {
		String query = "r.level = f.level AND r.user_name LIKE \'P%\'";

		System.out.println("Test 13: \n¿Es válida '" + query + "' ?");
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
	public void test14() {
		String query = "r.level = f.level a e3 w 	q3 	 º32	9'}97AND r.user_name LIKE \'P%\'";

		System.out.println("Test 15: \n¿Es válida '" + query + "' ?");
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
	public void test15() {
		String query = "r.level = ";

		System.out.println("Test 15: \n¿Es válida '" + query + "' ?");
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
	 * A test (Correct)
	 */
	public void test16() {
		String query = "r.level = 'el fondo d'Elx'";

		System.out.println("Test 16: \n¿Es válida '" + query + "' ?");
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
	 * A test (Correct)
	 */
	public void test17() {
		String query = "r.level = 'el fondo dElx'";

		System.out.println("Test 17: \n¿Es válida '" + query + "' ?");
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
	///// END TESTS /////
	
	///// METHODS RELATED WITH THE VALIDATION OF THE QUERY /////
	
	/**
	 * Checks the filter expression if it's correct
	 * 
	 * @param query The query expression to analyze
	 * @return True if it's valid or false if not
	 */	
	private boolean validateExpression(String query) {
		// If it's an empty query -> ok 
		if (query.trim().length() == 0)
			return true;
		
		// Replace all Date(dd-mmm-yyyy) format to ddd-mmm-yyyy (characters will replaced to spaces)
		int index = 0;
		String query_copy = new String(query);
		while ((index = query_copy.indexOf("Date(", index)) != -1) {
			if (index > 0) {
				if ((query_copy.charAt(index-1) != ' ') && (query_copy.charAt(index-1) != '('))
					break;
			}
			
			if (((index + 16) < query_copy.length()) && (query_copy.charAt(index + 16) == ')')) { // +17 is the length of Date(dd-mmm-yyyy)
				if ((index + 17) < query_copy.length()) {
					query_copy = query_copy.substring(0, index) + "     " + query_copy.substring(index+6, index+16) + " " + query_copy.substring(index+17);
				}
				else {
					query_copy = query_copy.substring(0, index) + "     " + query_copy.substring(index+6, index+16);
				}
			}
		}
		
		SQLQueryValidation sQLQueryValidation = new SQLQueryValidation(query_copy, true);

		// Tries to validate the query, and if fails shows a message
		if (!sQLQueryValidation.validateQuery()) {
			JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "finded") + " " + sQLQueryValidation.getTokenThatProducedTheSyntacticError() + " " + PluginServices.getText(null, "in")  + " " + sQLQueryValidation.getErrorPositionAsMessage() + ".", PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else {
			// Analyzes tokens in query
			StringTokenizer tokens = new StringTokenizer(query, " ");
			String token, token_aux;
			boolean finish = false;

			while (tokens.hasMoreTokens()) {
				token = tokens.nextToken().trim();
				
				if (token.charAt(0) == '\'') {
					if (token.charAt(token.length() -1) != '\'') {
						while (!finish) {
							if (!tokens.hasMoreTokens()) {
								JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "the_token") + " " + token + " " + PluginServices.getText(null, "has_bad_format"), PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
								return false;
							}
							else {
								token_aux = tokens.nextToken().trim();
								token += " " + token_aux;
								
								if (token_aux.charAt(token_aux.length() -1) == '\'')
									finish = true;
							}
						}
						
						finish = false;
					}
				}
				
				if (token.charAt(0) == '\"') {
					if (token.charAt(token.length() -1) != '\"') {
						while (!finish) {
							if (!tokens.hasMoreTokens()) {
								JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "the_token") + " " + token + " " + PluginServices.getText(null, "has_bad_format"), PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
								return false;
							}
							else {
								token_aux = tokens.nextToken().trim();
								token += " " + token_aux;
								
								if (token_aux.charAt(token_aux.length() -1) == '\"')
									finish = true;
							}
						}
						
						finish = false;
					}
				}

				// Tries to find an invalid token
				if (token.length() > 0) {
					// Validates if a supposed field exists
					if ( (token.length() > 2) && (token.charAt(0) == '\"') && (token.charAt(token.length()-1) == '\"') ) {
						if (! this.isAField(token.substring(1, token.length()-1))) {
							JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "the_token") + " " + token + " " + PluginServices.getText(null, "isnt_a_field_of_layer"), PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
							return false;
						}
					}
					else {
						// If it's an string -> ignore
						if (! ((token.charAt(0) == token.charAt(token.length() - 1)) && (token.charAt(0) == '\''))) {
							
							// If it's a date -> ignore
							int returnValue = validateDate(token);
							
							if (returnValue == 1) {
								JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "incorrect_format_on_date") + " " + token.substring(5, 16) + " .", PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
								return false;
							}
							
							if (returnValue == 2) {								
								// Else -> Checks if the current token is a valid number or symbol
								if ((! StringNumberUtilities.isNumber(token)) && (! this.isAnOperatorNameOrSymbol(token, getAllOperatorSymbols()))) {
									JOptionPane.showMessageDialog(null, PluginServices.getText(null, "filter_with_an_incorrect_format") + ": " + PluginServices.getText(null, "not_valid_token") + ": " + token, PluginServices.getText(null, "error_validating_filter_query"), JOptionPane.ERROR_MESSAGE);
									return false;
								}
							}
						}
					}
				}
			}

			// If has validate all tokens -> query validated
			return true;
		}
	}
	
	/**
	 * Returns true if there is a field with the same name as 'text'
	 * 
	 * @param text An string
	 * @return A boolean value
	 */
	private boolean isAField(String text) {
		return this.allFieldsAndValuesKnownOfCurrentLayer.containsKey(text);		
	}
	

	/**
	 * Returns true if there is the 'text' is a symbol or a operator name
	 * 
	 * @param text An string
	 * @return A boolean value
	 */
	private boolean isAnOperatorNameOrSymbol(String text, Set operatorNamesAndSymbols) {
		// We will ignore the case sensitive (all the rest of operators are in lower case)
		return operatorNamesAndSymbols.contains(text.toLowerCase());
	}
	
	
	/**
	 * Validates if a text has a correct date format as Date(dd-mmm-yyyy)  (Ex. Date(03-feb-2004) )
	 * 
	 * @param text
	 * @return 0 -> if has a date format; 1 -> if it's a date that has a but format; 2 -> if it isn't a date
	 */
	private int validateDate(String text) {
		// If it's a date -> check if format is correct (Ex.  Date(01-feb-2004) )
		if ( ((text.length() == 17) && (text.startsWith("Date(")) && (text.endsWith(")"))) && (text.charAt(7) == '-') && (text.charAt(11) == '-') ) {
			if ( (StringNumberUtilities.isNaturalNumber(text.substring(5, 7))) && (StringNumberUtilities.isNaturalNumber(text.substring(12, 16))) ) {
				try {
					// If can parse the date -> date with a correct format 
					DateFormat.getDateInstance().parse(text.substring(5, 16));
					return 0;
				} catch (ParseException e) {
					// If can't parse the date -> date with an incorrect format 
					return 1;
				}
			}
			else {
				return 1;
			}
		}
		
		return 2;
	}	
	
	///// END METHODS RELATED WITH THE VALIDATION OF THE QUERY /////
	
	///// OTHER METHOD AUXILIARS /////
	private Set getAllOperatorSymbols() {
		if (_operatorSymbols == null) {
			// Add some operations as example
			_operatorSymbols = new HashSet();
			_operatorSymbols.add("and");
//			operatorSymbols.add("Date");
			_operatorSymbols.add("<>"); // In SQL this is the formal operator
			_operatorSymbols.add("!="); // This operator is also supported
			_operatorSymbols.add("=");
			_operatorSymbols.add(">=");
			_operatorSymbols.add("<=");
			_operatorSymbols.add(">");
			_operatorSymbols.add("not");
			_operatorSymbols.add("or");
			_operatorSymbols.add("(");
			_operatorSymbols.add(")");
			_operatorSymbols.add("<");
		}
		
		return _operatorSymbols;
	}

	///// END OTHER METHOD AUXILIARS /////
}

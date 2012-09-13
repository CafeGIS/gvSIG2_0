package com.iver.cit.gvsig.gui.panels.sqlqueryvalidation;

import java.io.ByteArrayInputStream;

import Zql.ZqlParser;

import com.iver.andami.PluginServices;

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
 * Class for validate a complete query or the sentence of a query from WHERE 
 *
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class SQLQueryValidation {
	private String query;
	private boolean onlyWhereStatement;
	private int[] errorPosition; //[0] -> line, [1] -> column        ( (-1, -1) -> no error ) ( (-2, -2) -> error: query = null)
	private String errorMessage;
	private String errorPositionAsMessage;
	private final String preQuery = "SELECT a FROM b WHERE ";
	private final int preQueryLenght = preQuery.length();
	
	/**
	 * Default constructor with a parameter
	 * 
	 * @param _query The query to validate
	 * @param _onlyWhereStatement If the query is only the part of WHERE ... in a query
	 */
	public SQLQueryValidation(String _query, boolean _onlyWhereStatement) {
		query = new String(_query);
		onlyWhereStatement = _onlyWhereStatement;
		
		// By default no error position
		errorPosition = new int[2];
		errorPosition[0] = -1;
		errorPosition[1] = -1;
		
		errorPositionAsMessage = null;
		
		// By default no error message
		errorMessage = null;
	}
		
	/**
	 * Validates a query
	 * 
	 * Returns 'null' if there has been some error
	 */
	public boolean validateQuery() {
		String completeQuery = new String();
		
		// If the query is null -> error 
		if (query == null)	{
			errorPosition[0] = -2;
			errorPosition[1] = -2;
			defineErrorPositionAsMessageAttribute(errorPosition);
			errorMessage = new String( PluginServices.getText(null, "queryIsNull"));
			
			return false;
		}
		
		// If no query itsn't considered as a error
		if ((query.compareTo("")) == 0) {
			errorPosition[0] = -1;
			errorPosition[1] = -1;
			defineErrorPositionAsMessageAttribute(errorPosition);
			errorMessage = null;
			
			return true;
		}
		
		if (onlyWhereStatement)
			completeQuery = preQuery + query.trim().replaceAll("\"", "");
		else
			completeQuery = query.trim().replaceAll("\"", "");
		
		if ((completeQuery.length() > 0) && (completeQuery.charAt(completeQuery.length() - 1) != ';'))
			completeQuery += ";";

		try {
			// Starts the parser
			ZqlParser p = new ZqlParser();
			p.initParser(new ByteArrayInputStream(completeQuery.getBytes()));
	      
			// Analyzes the query
			p.readStatement();
			
			// If arrives here -> there has been no errors
			errorPosition[0] = -1;
			errorPosition[1] = -1;
			defineErrorPositionAsMessageAttribute(errorPosition);
			errorMessage = null;
			return true;
		} catch (Exception e) {
			// Get the line an column where the syntax error starts
			String line = new String(e.getMessage().substring(e.getMessage().indexOf("line"), e.getMessage().indexOf(',')));
			line = line.substring(line.indexOf(' ')+1, line.length());
			
			String column = new String(e.getMessage().substring(e.getMessage().indexOf("column"), e.getMessage().indexOf('.')));
			column = column.substring(column.indexOf(' ')+1, column.length());			

			// Get the line an column of the error
			errorPosition[0] = Integer.valueOf(line.trim()).intValue();			
			errorPosition[1] = Integer.valueOf(column.trim()).intValue();
			
			if (onlyWhereStatement) {
				this.errorPosition[1] -= this.preQueryLenght; // Substract the lenght of the pre-query added
			}
			
			defineErrorPositionAsMessageAttribute(this.errorPosition);
			
			// Defines the error message
			errorMessage = e.getMessage();

			return false;
		}
	}
	
	/**
	 * Returns an string with an error message if there has been an error, or 'null' if there hasn't been
	 * 
	 * @return An string or null
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	/**
	 * Returns an string with a text describing the (first) position of the error
	 * 
	 * @return An string or null
	 */
	public String getErrorPositionAsMessage() {
		return this.errorPositionAsMessage;
	}
	
	/**
	 * Returns the error position (line, column) or (-1, -1) if there hasn't been any error
	 * 
	 * @return An array of 2 integer values (first: line, second: column) 
	 */
	public int[] getErrorPosition() {
		return errorPosition;
	}
	
	/**
	 * Creates a message with information about the message
	 * 
	 * @param position An array with 2 elements: (row, column)
	 */
	private void defineErrorPositionAsMessageAttribute(int position[]) {
		
		// Defines the error message
		errorPositionAsMessage = new String( PluginServices.getText(null, "line") + ": " + errorPosition[0] + ", " + PluginServices.getText(null, "column") + ": " + errorPosition[1] );
	}
}
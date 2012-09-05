
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
package org.gvsig.catalog.languages;
import java.util.Iterator;

import org.gvsig.catalog.querys.Coordinates;


/**
 * This class implements the Filter Encoding Language. It is used to
 * create queries in this language
 * @author Jorge Piera Llodra (jorge.piera@iver.es)
 * @see http://portal.opengeospatial.org/files/?artifact_id=8340
 */
public class FilterEncoding extends AbstractGeneralLanguage {
	//Properties
	public static final String PROPERTY_IS_LIKE = "PropertyIsLike";
	public static final String PROPERTY_IS_LESS = "PropertyIsLess";
	public static final String PROPERTY_IS_GREATER = "PropertyIsGreater";
	public static final String PROPERTY_IS_GREATER_THAN = "PropertyIsGreaterThan";
	public static final String PROPERTY_IS_LESS_THAN = "PropertyIsLessThan";
	public static final String PROPERTY_IS_EQUALS_TO = "PropertyIsEqualTo";
	//Type options
	public static final String TYPE_LITERAL = "Literal";
	public static final String TYPE_TWO_PROPERTIES = "PropertyName";	
	//Default values
	public static final String DEFAULT_PREFIX = "ogc";
	public static final String DEFAULT_WILDCARD = "*";
	public static final String DEFAULT_SINGLECHAR = "?";
	public static final String DEFAULT_ESCAPE = "\\";
	public static final String DEFAULT_NAMESPACE = "xmlns:ogc=\"http://www.opengis.net/ogc\"";
	//Private labels
	private static final String FILTER = "Filter"; 

	private String prefix = null;
	private String wildCard = null;
	private String singleChar = null;
	private String escape = null;
	private String namespace = null;
	private String wildCardLabel = "wildCard";
	private String escapeCharLabel = "escapeChar";
	private String singleCharLabel = "singleChar";

	/**
	 * Create a new Filter Encoding Parser
	 * @param prefix Prefix of the labels (if its necessary). 
	 * Typically "ogc".
	 * @param wildCard It Depends of the server
	 * @param singleChar It Depends of the server
	 * @param escape It Depends of the server
	 */
	public  FilterEncoding(String prefix, String wildCard, String singleChar, String escape) {        
		this.prefix = prefix + ":";
		this.wildCard = wildCard;
		this.singleChar = singleChar;
		this.escape = escape;		
	} 

	/**
	 * Create a new Filter Encoding Parser with the 
	 * deafault values
	 */
	public  FilterEncoding() {        
		this.prefix = DEFAULT_PREFIX + ":";
		this.wildCard = DEFAULT_WILDCARD;
		this.singleChar = DEFAULT_SINGLECHAR;
		this.escape = DEFAULT_ESCAPE;		
	} 

	/**
	 * It Adds a new clause of the query
	 * @param propertyName The property name
	 * @param propertyValue The property value
	 * @param concordancia "E" (Exact phrase), "A" (All words)
	 * or "Y" (anY word).
	 * @param relationship PropertyIsLike, PropertyIsLess, PropertyIsGreater,... See the File encoding
	 * Documentation.
	 * @param type Values: "P" (to comparate two propertyes) or "L" (to comparate one property
	 * and one literal value)
	 * @param operator "And" or "Or". Operator between fields
	 */
	public void addClauses(String propertyName, String propertyValue, 
			String concordancia, String relationship, String type, 
			String operator) {        
		currentClause = null;
		//Seperating the words
		Iterator values = parseValues(propertyValue, concordancia, relationship, wildCard);
		//Filling the words
		addClauses(propertyName, values, concordancia, relationship, type, operator);
	} 

	/**
	 * It Adds a new clause of the query
	 * @param propertyName The property name
	 * @param propertyValue The property value
	 * @param concordancia "E" (Exact phrase), "A" (All words)
	 * or "Y" (anY word).
	 */
	public void addClauses(String propertyName, String propertyValue, 
			String concordancia) {   	
		addClauses(propertyName, propertyValue, concordancia,
				FilterEncoding.PROPERTY_IS_LIKE, 
				FilterEncoding.TYPE_LITERAL,
				FilterEncoding.AND);
	} 

	/**
	 * It Adds a new clause of the query
	 * @param propertyName The property name
	 * @param propertyValues The property value separated by blank spaces
	 * @param concordancia "E" (Exact phrase), "A" (All words)
	 * or "Y" (anY word).
	 * @param relationship PropertyIsLike, PropertyIsLess, PropertyIsGreater,... See the File encoding
	 * Documentation.
	 * @param type Values: "P" (to comparate two propertyes) or "L" (to comparate one property
	 * and one literal value)
	 * @param operator "And" or "Or". Operator between fields
	 */
	public void addClauses(String propertyName, Iterator propertyValues, 
			String concordancia, String relationship, String type,
			String operator) {        
		while (propertyValues.hasNext())
			addTerm(propertyName, (String) propertyValues.next(), concordancia,
					relationship, type);
		addCurrentClauseQuery(operator);
	} 

	/**
	 * It adds a new term to the full query
	 * @param propertyName The property name
	 * @param propertyValue The property value
	 * @param concordancia "E" (Exact phrase), "A" (All words) or "Y" (anY word).
	 * @param relationship PropertyIsLike, PropertyIsLess, PropertyIsGreater,... See the File encoding
	 * Documentation.
	 * @param type Values: "P" (to comparate two propertyes) or "L" (to comparate one property
	 * and one literal value)
	 */
	private void addTerm(String propertyName, String propertyValue, 
			String concordancia, String relationship, String type) {        
		StringBuffer term = new StringBuffer();
		term.append(propertyIsXXX(relationship, propertyName, propertyValue, type));
		if (currentClause == null) {
			currentClause = term.toString();
		} else {
			currentClause = currentClause + term.toString();
			currentClause = enterLabel(currentClause, getOperator(concordancia));
		}
	} 

	/**
	 * It adds the "and" label to join different operations
	 * @param operator 
	 */
	protected void addCurrentClauseQuery(String operator) {        
		if (currentClause != null) {
			if (currentQuery == null) {
				currentQuery = currentClause;
			} else {
				currentQuery = currentQuery + currentClause;
				currentQuery = enterLabel(currentQuery, operator);
			}
		}
	} 

	/**
	 * It returns the encoded query
	 * @return 
	 */
	public String toString() {        
		return enterLabel(currentQuery, FILTER);
	} 

	/**
	 * Involves a query with a label
	 * @param query Query to involve
	 * @param label Label name
	 * @return a filter encoding query
	 */
	private String enterLabel(String query, String label) {        
		if (label.equals(FILTER) && (this.namespace != null)) {
			return "<" + prefix + label + " " + this.namespace + ">" +
			query + "</" + prefix + label + ">";
		} else {
			return "<" + prefix + label + ">" + query + "</" + prefix +
			label + ">";
		}
	} 

	/**
	 * It writes a "PropertyIsXXX" part of a filter encoding query
	 * @param relationship Possible Values: PropertIsLike, PropertyIsLess,
	 * PropertyIsGreater,... See the Filter Encoding documentation
	 * @param propertyName The property name
	 * @param propertyValue The property value
	 * @param type Values: "P" (to comparate two propertyes) or "L" (to comparate one property
	 * and one literal value)
	 * @return The part of the query
	 */
	private String propertyIsXXX(String relationship, String propertyName,
			String propertyValue, String type) {        
		String cadena = "";
		cadena = "<" + prefix + relationship;
		if (relationship.equals("PropertyIsLike")) {
			if (this.wildCard != null) {
				cadena = cadena + " " + getWildCardLabel() + "=\"" + this.wildCard + "\"";
			}
			if (this.singleChar != null) {
				cadena = cadena + " " + getSingleCharLabel() + "=\"" + this.singleChar + "\"";
			}
			if (this.escape != null) {
				cadena = cadena + " " + getEscapeCharLabel() + "=\"" + this.escape + "\"";
			}
		}
		cadena = cadena + ">" + enterLabel(propertyName, TYPE_TWO_PROPERTIES);
		cadena = cadena + enterLabel(propertyValue, type);		
		return cadena + "</" + prefix + relationship + ">";
	} 

	/**
	 * It Adds a Bounding Box query
	 * 
	 * 
	 * @param coordinates Coordinates to find
	 * @param propertyName Property that contains the geom field
	 * @param not If we have to envolve the query with the "NOT" tag.
	 */
	public void addBoundingBox(Coordinates coordinates, String propertyName, boolean not) {        
		// sNorth -> Uly();
		// sWest -> Ulx();
		// sSouth -> Bry();
		// sEast -> Brx();
		String bbox = "<ogc:BBOX>" + "<ogc:PropertyName>" + propertyName +
		"</ogc:PropertyName>" + "<gml:Box>" + "<gml:coord>" + "<gml:X>" +
		coordinates.ulx + "</gml:X>" + "<gml:Y>" + coordinates.bry +
		"</gml:Y>" + "</gml:coord>" + "<gml:coord>" + "<gml:X>" +
		coordinates.brx + "</gml:X>" + "<gml:Y>" + coordinates.uly +
		"</gml:Y>" + "</gml:coord>" + "</gml:Box>" + "</ogc:BBOX>";
		if (not){
			bbox = "<ogc:Not>" + bbox + "</ogc:Not>"; 
		}
		if (currentQuery == null) {
			currentQuery = bbox;
		} else {
			currentQuery = currentQuery + bbox;
			currentQuery = enterLabel(currentQuery, "And");
		}
	}

	/**
	 * @return the wildCard
	 */
	public String getWildCard() {
		return wildCard;
	}

	/**
	 * @return the wildCardLabel
	 */
	public String getWildCardLabel() {
		return wildCardLabel;
	}

	/**
	 * @param wildCardLabel the wildCardLabel to set
	 */
	public void setWildCardLabel(String wildCardLabel) {
		this.wildCardLabel = wildCardLabel;
	}

	/**
	 * @return the escapeCharLabel
	 */
	public String getEscapeCharLabel() {
		return escapeCharLabel;
	}

	/**
	 * @param escapeCharLabel the escapeCharLabel to set
	 */
	public void setEscapeCharLabel(String escapeCharLabel) {
		this.escapeCharLabel = escapeCharLabel;
	}

	/**
	 * @return the singleCharLabel
	 */
	public String getSingleCharLabel() {
		return singleCharLabel;
	}

	/**
	 * @param singleCharLabel the singleCharLabel to set
	 */
	public void setSingleCharLabel(String singleCharLabel) {
		this.singleCharLabel = singleCharLabel;
	} 
}

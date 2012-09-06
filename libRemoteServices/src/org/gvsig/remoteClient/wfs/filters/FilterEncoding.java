
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
package org.gvsig.remoteClient.wfs.filters;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.filters.BinaryTree.Node;

/**
 * This class implements the Filter Encoding Language. It is used to
 * create querys in this language
 * 
 * Name: OpenGIS® Filter Encoding Implementation Specification
 * Version: 1.1.0
 * Project Document: OGC 04-095 
 * @see http://portal.opengeospatial.org/files/?artifact_id=8340
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class FilterEncoding extends AFilter {
	public static final int RELATIONSHIP_PROPERTY = 0;
	public static final int RELATIONSHIP_VAUES = 1;

	private StringBuffer currentQuery = null;
	//Operation types
	private static final int OPERATION_PROPERTYNAME = 0;
	private static final int OPERATION_LITERAL = 1;
	//Filter namespace. (typically "ogc")
	private String namepacePrefix = null;
	//If the Filter can have blanckSpaces
	private boolean hasBlankSpaces = true;
	private String defaultBlankSpace = "%20";
	//This character must be replaced by any set of characters (typically "*")
	private String wildCardChar = null;
	//This character must be replaced by one character (typically "?")
	private String singleChar = null;
	//Escape character (typically "\")
	private String escapeChar = null;
	//Default values
	public static final String DEFAULT_NAMESPACE_PREFIX = "ogc";
	public static final String DEFAULT_WILDCARD = "*";
	public static final String DEFAULT_SINGLECHAR = "?";
	public static final String DEFAULT_ESCAPE = "\\";
	public static final String DEFAULT_NAMESPACE = "xmlns:ogc=\"http://www.opengis.net/ogc\"";

	private Hashtable filterAttributes = new Hashtable();

	/**
	 * If the namespace XML is qualified
	 */
	private boolean isQualified = false;

	/**
	 * Create a new Filter Encoding Parser
	 * 
	 * 
	 * @param namepacePrefix
	 * Filter namespace. (typically "ogc")
	 * @param wildCardChar 
	 * This character must be replaced by any set of characters (typically "*")
	 * @param singleChar 
	 * This character must be replaced by one character (typically "?")
	 * @param escape 
	 * Escape character
	 * @param filterAttribute Sometimes, "Field" label needs an attribute.
	 */
	public FilterEncoding(ISQLExpressionFormat formatter,String namesPacePrefix, String wildCard, String singleChar, String escape, Hashtable filterAttributes) {        
		super(formatter);
		if (namesPacePrefix == null){
			setQualified(false);
		}else{
			setQualified(true);
		}
		this.wildCardChar = wildCard;
		this.singleChar = singleChar;
		this.escapeChar = escape;
		this.filterAttributes = filterAttributes;
	} 


	/**
	 * Create a new Filter Encoding Parser
	 * @param formatter
	 */
	public FilterEncoding(ISQLExpressionFormat formatter) {        
		this(formatter,null,DEFAULT_WILDCARD,DEFAULT_SINGLECHAR,
				DEFAULT_ESCAPE,new Hashtable());
	} 

	/**
	 * Create a new Filter Encoding Parser
	 */
	public FilterEncoding() {        
		this(new DefaultSQLExpressionFormat());		
	} 	

	/**
	 * Create a new Filter Encoding Parser
	 */
	public FilterEncoding(WFSStatus status) {        
		this();	
		FilterEncoding filterEncoding = new FilterEncoding();	
		filterEncoding.setQueryByAttribute(status.getFilterByAttribute());
		filterEncoding.setArea(status.getFilterByArea());		
	} 

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.filterEncoding.QueryLanguage#toString(org.gvsig.remoteClient.filterEncoding.BinaryTree)
	 */
	protected String toString(BinaryTree tree) {
		//If is a filter by ids...
		StringBuffer idQuery = null;
		if (getIds() != null){
			idQuery = new StringBuffer();
			ArrayList ids = getIds();
			for (int i=0 ; i<ids.size() ; i++){
				if (ids.get(i) != null){
					Hashtable attributes = new Hashtable();
					attributes.put("fid","\"" + ids.get(i).toString() + "\"");
					idQuery.append(setTag("FeatureId", attributes, null));
				}
			}
			return enclosesWithFilterTag(idQuery.toString());
		}
		//If is a filter by attributes...
		String filterQuery = null;
		if ((tree.getRoot() == null) && (getArea() == null)){
			return null;
		}
		if (tree.getRoot() != null){
			currentQuery = new StringBuffer();
			filterQuery = getFilterNode(tree.getRoot());
			if (getArea() == null){
				return enclosesWithFilterTag(filterQuery);
			}
		}
		//If is a filter by area
		String bboxQuery = null;
		if (getArea() != null){
			FEIntersectsQuery intersects = new FEIntersectsQuery(getArea());			
//			bboxQuery = enterLabel(getAreaPropertyName(), "PropertyName");
//			bboxQuery = bboxQuery + new GMLBBox().createBBOX(getArea());
//			bboxQuery = enterLabel(bboxQuery, "Within");
			bboxQuery = intersects.getFilterEncoding();
			if (tree.getRoot() == null){
				return enclosesWithFilterTag(bboxQuery);
			}
		}		
		return enclosesWithFilterTag(filterQuery + bboxQuery);
	}

	/**
	 * Gets the filter code from a node
	 * @param node
	 */
	private String getFilterNode(Node node){
		if (node.isField()){
			return getExpression(node.getValue());
		}else{
			String left = "";
			String rigth = "";
			if (node.getLeftNode() != null){
				left = getFilterNode(node.getLeftNode());
			}
			if (node.getRigthNode() != null){
				rigth = getFilterNode(node.getRigthNode());
			}
			int operationCode = getLogicalOperator(node.getValue());
			String operation = getLogicalOperator(operationCode);
			return enterLabel(left+rigth, operation);
		}		
	}   

	/**
	 * Parses a expresion like 'A op B' and returns the
	 * expresion using the filter encoding language
	 * @param expression
	 * @return
	 */
	private String getExpression(String expression){
		String[] words = expression.split(" ");
		//Param
		String param = words[0];
		if (param.charAt(0) == '"'){
			param = param.substring(1,param.length());
		}
		if (param.charAt(param.length()-1) == '"'){
			param = param.substring(0,param.length()-1);
		}
		//Operator
		String operator = words[1];
		//Value
		String value = words[2];		
		for (int i=3 ; i<words.length ; i++){
			value = value + " " + words[i];
		}
		if (value.charAt(0) == '\''){
			value = value.substring(1,value.length());
		}
		if (value.charAt(value.length()-1) == '\''){
			value = value.substring(0,value.length()-1);
		}
		int operatorCode = getRelationalOperator(operator);
		operator = getRelationalOperator(operatorCode);
		return createExpression(operator,param,value);
	}

	/**
	 * It writes a "PropertyIsXXX" part of a filtyer encoding query
	 * 
	 * 
	 * @return The part of the query
	 * @param property Possible Values: PropertIsLike, PropertyIsLess, PropertyIsGreater,... See
	 * the Filter Encoding documentation
	 * @param parameter Parameter name
	 * @param value Parameter value
	 * @param type Values: "P" (to comparate two propertyes) or "L" (to comparate one property
	 * and one literal value)
	 */
	private String createExpression(String property, String parameter, String value) {        
		String cadena = "";
		cadena = "<" + namepacePrefix + property;
		if (property.equals("PropertyIsLike")) {
			if (wildCardChar != null) {
				cadena = cadena + " wildCard=\"" + this.wildCardChar + "\"";
			}
			if (singleChar != null) {
				cadena = cadena + " singleChar=\"" + this.singleChar + "\"";
			}
			if (escapeChar != null) {
				cadena = cadena + " escape=\"" + this.escapeChar + "\"";
			}
		}
		cadena = cadena + ">" + enterLabel(parameter, "PropertyName");
		cadena = cadena + enterLabel(value, "Literal");
		return cadena + "</" + namepacePrefix + property + ">";
	} 	

	/**
	 * Envuelve a una pregunta con una etiqueta
	 * 
	 * 
	 * @return String : parte de la query en el lenguaje soportado
	 * @param pregunta Pregunta a envolver
	 * @param etiqueta Nombre de la etiqueta
	 */
	private String enterLabel(String value, String tagName) {        
		if (tagName.equals("Filter") && (!(filterAttributes.isEmpty()))) {
			return setTag(tagName,filterAttributes,value);
		} else {
			return setTag(tagName,null,value);
		}
	} 

	/**
	 * Envolves a value with an XML tag
	 * 
	 * @return String
	 * XML tag with its value
	 * @param tagName 
	 * XML tag name
	 * @param attributes
	 * XML tag attributes
	 * @param value 
	 * Tag value
	 */
	public String setTag(String tagName, Hashtable attributes, String value) {        
		StringBuffer tag = new StringBuffer();

		tag.append("<");
		tag.append(namepacePrefix);
		tag.append(tagName);    	
		if (attributes != null){
			Set keys = attributes.keySet();
			if (attributes.size() > 0){
				Iterator it = keys.iterator();
				while (it.hasNext()){
					String key = (String)it.next();
					if (hasBlankSpaces){
						tag.append(" ");
					}else{
						tag.append(defaultBlankSpace);
					}
					tag.append(key + "=" + (String)attributes.get(key));

				}
			}
		}
		if (value == null){
			tag.append("/>");
		}else{
			tag.append(">" + value);
			tag.append("</");
			tag.append(namepacePrefix);
			tag.append(tagName);
			tag.append(">");
		}
		return tag.toString();
	}


	/**
	 * Encloses a query with the filter tag
	 * @param query
	 * @return
	 */
	private String enclosesWithFilterTag(String query){
		StringBuffer filter = new StringBuffer();
		filter.append("<" + namepacePrefix + "Filter");
		if (!isQualified){
			filter.append("%20");
			filter.append("xmlns:ogc=\"http://www.opengis.net/ogc\"%20");
			filter.append("xmlns:gml=\"http://www.opengis.net/gml\"%20"); 
			filter.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"%20");
			filter.append("xsi:schemaLocation=\"http://www.opengis.net/ogc%20../filter/1.0.0/filter.xsd%20http://www.opengis.net/gml%20../gml/2.1.2/geometry.xsd\"");
		}
		filter.append(">");
		filter.append(query);
		filter.append("</" + namepacePrefix + "Filter>");
		return filter.toString();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.filterEncoding.AQueryLanguage#getLogicOperator(int)
	 */
	public String getLogicalOperator(int operator) {
		switch (operator){
		case LOGICAL_OPERATOR_AND:
			return "And";
		case LOGICAL_OPERATOR_OR:
			return "Or";
		case LOGICAL_OPERATOR_NOT:
			return "Not";
		default:
			return "And";
		}    
	} 

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.filterEncoding.AQueryLanguage#getRelationalOperator(int)
	 */
	public String getRelationalOperator(int operator) {
		switch (operator){
		case RELATIONAL_OPERATOR_IS_EQUALS_TO:
			return "PropertyIsEqualTo";
		case RELATIONAL_OPERATOR_IS_NOT_EQUALS_TO:
			return "PropertyIsNotEqualTo";
		case RELATIONAL_OPERATOR_IS_LESS_THAN:
			return "PropertyIsLessThan";
		case RELATIONAL_OPERATOR_IS_GREATER_THAN:
			return "PropertyIsGreaterThan";
		case RELATIONAL_OPERATOR_IS_LESS_THAN_OR_EQUAL_TO:
			return "PropertyIsLessThanOrEqualTo";
		case RELATIONAL_OPERATOR_IS_GREATER_THAN_OR_EQUAL_TO:
			return "PropertyIsGreaterThanOrEqualTo";
		case RELATIONAL_OPERATOR_IS_LIKE:
			return "PropertyIsLike";
		case RELATIONAL_OPERATOR_IS_NULL:
			return "PropertyIsNull";
		case RELATIONAL_OPERATOR_IS_BETWEEN:
			return "PropertyIsBetween";
		default:
			return "PropertyIsLike";
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.filterEncoding.AQueryLanguage#getGeometricOperator(int)
	 */
	public String getGeometricOperator(int operator) {
		switch (operator){
		case GEOMETRIC_OPERATOR_EQUALS:
			return "Equals";
		case GEOMETRIC_OPERATOR_DISJOINT:
			return "Disjoint";
		case GEOMETRIC_OPERATOR_TOUCHES:
			return "Touches";
		case GEOMETRIC_OPERATOR_WITHIN:
			return "Within";
		case GEOMETRIC_OPERATOR_OVERLAPS:
			return "Overlaps";
		case GEOMETRIC_OPERATOR_CROSSES:
			return "Crosses";
		case GEOMETRIC_OPERATOR_INTERSECT:
			return "Intersect";
		case GEOMETRIC_OPERATOR_CONTAINS:
			return "Contains";
		case GEOMETRIC_OPERATOR_DWITHIN:
			return "Dwithin";
		case GEOMETRIC_OPERATOR_BEYOND:
			return "Beyond";
		case GEOMETRIC_OPERATOR_BBOX:
			return "BBOX";
		default:
			return "Equals";
		} 

	}

	public String getSeparator(int separator) {
		return null;
	}

	/**
	 * @param isQualified the isQualified to set
	 */
	public void setQualified(boolean isQualified) {
		this.isQualified = isQualified;
		if (isQualified){
			namepacePrefix = DEFAULT_NAMESPACE_PREFIX + ":";
		}else{
			namepacePrefix = "";
		}
	}


	/**
	 * @param namepacePrefix the namepacePrefix to set
	 */
	public void setNamepacePrefix(String namepacePrefix) {
		if ((namepacePrefix == null) || (namepacePrefix.equals(""))){
			this.namepacePrefix = "";
		}else{
			this.namepacePrefix = namepacePrefix + ":";
		}
	}


	/**
	 * @param hasBlankSpaces the hasBlankSpaces to set
	 */
	public void setHasBlankSpaces(boolean hasBlankSpaces) {
		this.hasBlankSpaces = hasBlankSpaces;
	}
}

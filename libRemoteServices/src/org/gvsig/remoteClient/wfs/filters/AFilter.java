
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
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.remoteClient.wfs.WFSGeometryOperation;

/**
 * All classes that implement a "Query Language" must to inherit of 
 * this class
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public abstract class AFilter {
	private BinaryTree root;
	private String currentClause;
	private ISQLExpressionFormat formatter;
	private WFSGeometryOperation area = null;
	private String areaPropertyName = null;
	private String srs = null;
	private int areaOption = AFilter.GEOMETRIC_OPERATOR_INTERSECT;
	private ArrayList ids = null;
	//Concordancia values: Between 0 and 9
    public static final int CONCORDANCIA_EXACT = 0;
    public static final int CONCORDANCIA_ANY_WORD = 1;
    public static final int CONCORDANCIA_ALL_WORDS = 2;
    //Locgical operators: Between 10 and 19
    public static final int LOGICAL_OPERATOR_AND = 10;
    public static final int LOGICAL_OPERATOR_OR = 11;
    public static final int LOGICAL_OPERATOR_NOT = 12;
    //Geometric operators: Between 20 and 39
    public static final int GEOMETRIC_OPERATOR_EQUALS = 21;
    public static final int GEOMETRIC_OPERATOR_DISJOINT = 22;
    public static final int GEOMETRIC_OPERATOR_TOUCHES = 23;
    public static final int GEOMETRIC_OPERATOR_WITHIN = 24;
	public static final int GEOMETRIC_OPERATOR_OVERLAPS = 25;
	public static final int GEOMETRIC_OPERATOR_CROSSES = 26;
	public static final int GEOMETRIC_OPERATOR_INTERSECT = 27;
	public static final int GEOMETRIC_OPERATOR_CONTAINS = 28;
	public static final int GEOMETRIC_OPERATOR_DWITHIN = 29;
	public static final int GEOMETRIC_OPERATOR_BEYOND = 30;
	public static final int GEOMETRIC_OPERATOR_BBOX = 31;
	//Relational operator: Between 40 and 60
	public static final int RELATIONAL_OPERATOR_IS_EQUALS_TO = 40;
	public static final int RELATIONAL_OPERATOR_IS_NOT_EQUALS_TO = 41;
	public static final int RELATIONAL_OPERATOR_IS_LESS_THAN = 42;
	public static final int RELATIONAL_OPERATOR_IS_GREATER_THAN = 43;
	public static final int RELATIONAL_OPERATOR_IS_LESS_THAN_OR_EQUAL_TO = 44;
	public static final int RELATIONAL_OPERATOR_IS_GREATER_THAN_OR_EQUAL_TO = 45;
	public static final int RELATIONAL_OPERATOR_IS_LIKE = 46;
	public static final int RELATIONAL_OPERATOR_IS_NULL = 47;
	public static final int RELATIONAL_OPERATOR_IS_BETWEEN = 48;
	//Separators: Between 60 and 69
	public static final int SEPARATOR_OPENED = 60;
	public static final int SEPARATOR_CLOSED = 61; 
	//Other: greater that 70
	public static final int STRING_VALUE = 70;
	//BBOX
	public static final int BBOX_ENCLOSES = 81;
	
	public AFilter(ISQLExpressionFormat formatter){
		root = new BinaryTree();	
		this.formatter = formatter;
	}
	
	/**
	 * Adds a feature id
	 * @param id
	 * The feature id
	 */
	public void addFeatureById(Object id){
		if (ids == null){
			ids = new ArrayList();
		}
		ids.add(id);
	}
		
	/**
	 * @return the ids
	 */
	protected ArrayList getIds() {
		return ids;
	}

	public void setQueryByAttribute(String query){
		if (query != null){			
			ByteArrayInputStream is = new ByteArrayInputStream(query.getBytes());
			String sql = formatter.format(query);
			if (sql != null){
				ParseExpressions expressions = new ParseExpressions();
				ArrayList tokens = expressions.parseExpression(sql);

				for (int i=0 ; i<tokens.size() ; i++){
					String token = (String)tokens.get(i);
					root.addTerm(token);				
				}			
			}	
		}
	}
	
	/**
	 * It adds a new property and value using the AND
	 * operation 
	 * @param propertyName
	 * @param propertyValue
	 */
	public void addAndClause(String propertyName, String propertyValue){
		root.addTerm(propertyName + " = " + propertyValue,
				getLogicalOperator(LOGICAL_OPERATOR_AND));
	}
	
	public void setArea(WFSGeometryOperation geometryOperation){
		this.area = geometryOperation;
	}
	
	public void setArea(Geometry filterByArea, String attributeName, String srs, int operation) {
		this.area = new WFSGeometryOperation(filterByArea, operation, attributeName, srs);
	}
	
	public void addClause(String value){
		if (currentClause == null){
			currentClause = new String("");
		}
		currentClause = currentClause + value;
	}
	
	public String toString(){
		if (currentClause != null){
			setQueryByAttribute(currentClause);
		}
		return toString(root);
	}
	
    /**
     * It returns the Query like a String
     */
    protected abstract String toString(BinaryTree tree);
    
    /**
     * returns the String that represents the logic
     * operator in this query language
     * @param operator
     * Logic operator
     * @return
     */
    public abstract String getLogicalOperator(int operator);
    
    /**
     * returns the String that represents the relational
     * operator in this query language
     * @param operator
     * Logic operator
     * @return
     */
    public abstract String getRelationalOperator(int operator);
    
    /**
     * returns the String that represents the geometric
     * operator in this query language
     * @param operator
     * Logic operator
     * @return
     */
    public abstract String getGeometricOperator(int operator);

    /**
     * returns the String that represents the separator
     * operator in this query language
     * @param separator
     * LSeparator "(" or ")" 
     * @return
     */
    public abstract String getSeparator(int separator);
    
    /**
     * Return true if the token is a operator
     * @param operator
     * @return
     */
    public String getOperator(int operator){
    	if (isLogical(operator)){
    		return getLogicalOperator(operator);
    	}else if(isRelational(operator)){
    		return getRelationalOperator(operator);
    	}else if(isGeometric(operator)){
    		return getGeometricOperator(operator);
    	}
    	return String.valueOf(operator);
    }	
    
    public int getRelationalOperator(String operator){
    	if (operator.equals("=")){
    		return RELATIONAL_OPERATOR_IS_EQUALS_TO;
    	}else if(operator.equals("!=")){
    		return RELATIONAL_OPERATOR_IS_NOT_EQUALS_TO;
    	}else if(operator.equals("<>")){ // This is another way to tell not-equal
    		return RELATIONAL_OPERATOR_IS_NOT_EQUALS_TO;
    	}else if(operator.equals(">")){
    		return RELATIONAL_OPERATOR_IS_GREATER_THAN;
    	}else if(operator.equals(">=")){
    		return RELATIONAL_OPERATOR_IS_GREATER_THAN_OR_EQUAL_TO;
    	}else if(operator.equals("<")){
    		return RELATIONAL_OPERATOR_IS_LESS_THAN;
    	}else if(operator.equals("<=")){
    		return RELATIONAL_OPERATOR_IS_LESS_THAN_OR_EQUAL_TO;
    	}else if(operator.toUpperCase().equals("LIKE")){
    		return RELATIONAL_OPERATOR_IS_LIKE;
    	}
    	return RELATIONAL_OPERATOR_IS_EQUALS_TO;
    }
    
    public int getLogicalOperator(String operator){
    	if (operator.toUpperCase().equals("AND")){
    		return LOGICAL_OPERATOR_AND;
    	}else if(operator.toUpperCase().equals("NOT")){
    		return LOGICAL_OPERATOR_NOT;
    	}else if(operator.toUpperCase().equals("OR")){
    		return LOGICAL_OPERATOR_OR;
    	}
    	return LOGICAL_OPERATOR_AND;
    }
    
	/**
     * Return true if is a geometric operator
     * @param type
     * @return
     */
    private boolean isGeometric(int type) {
       	if ((type > 19) && (type < 40)){
       		return true;
       	}
        return false;
	}

	/**
     * Return true if is a relational operator
     * @param type
     * @return
     */
    private boolean isRelational(int type) {
    	if ((type > 39) && (type < 60)){
       		return true;
       	}
        return false;
	}

	/**
     * Return true if is a logical operator
     * @param type
     * @return
     */
    private boolean isLogical(int type){
     	if ((type > 9) && (type < 20)){
       		return true;
       	}
        return false;
    }
    
   
    
    /**
     * Return true if is a seperator
     * @param type
     * @return
     */
    private boolean isSeparator(int type){
    	if ((type > 59) && (type < 70)){
       		return true;
       	}
        return false;
    }      
    
    /**
     * Divide a line in a set of words
     * @param line
     * Line to divide
     * @param option
     * If the option is EXACT it returns the same line 
     * @return Iteraror
     * A set of words
     */
    public Iterator parseValues(String line, int option) {        
        Vector values = new Vector();
            
        if (option == CONCORDANCIA_EXACT) {
            values.add(line);
            return values.iterator();
        }
        
        StringTokenizer doubleQuotesTokenizer = new StringTokenizer(line, "\"",
                true);
        boolean inside = false;
        while (doubleQuotesTokenizer.hasMoreTokens()) {
            String token = doubleQuotesTokenizer.nextToken();
            if (token.equals("\"")) {
                inside = !inside;
            } else if (inside) {
                values.add(token);
            } else {
                StringTokenizer spaceTokenizer = new StringTokenizer(token, " ");
                while (spaceTokenizer.hasMoreTokens()) {
                    String value = spaceTokenizer.nextToken();
                    values.add(value);
                }
            }
        }
        return values.iterator();
    }     
  
	/**
	 * @return Returns the bbox.
	 */
	public WFSGeometryOperation getArea() {
		return area;
	}

	/**
	 * @return Returns the bboxOption.
	 */
	public int getAreaOption() {
		return areaOption;
	}

	public String getAreaPropertyName() {
		return areaPropertyName;
	}

	public void setAreaPropertyName(String areaPropertyName) {
		this.areaPropertyName = areaPropertyName;
	}

	public String getSrs() {
		return srs;
	}

	public void setSrs(String srs) {
		this.srs = srs;
	}  
  
   
}

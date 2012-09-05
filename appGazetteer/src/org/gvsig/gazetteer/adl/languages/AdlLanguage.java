
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
package org.gvsig.gazetteer.adl.languages;

import java.util.Iterator;

import org.gvsig.catalog.languages.AbstractGeneralLanguage;

/**
 * This class is used to create a query in the language supported
 * by the ADL protocol
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 * @see http://www.alexandria.ucsb.edu/gazetteer/protocol/
 */
public class AdlLanguage extends AbstractGeneralLanguage {

/**
 * It Adds a new clause of the query
 * 
 * @param value Parameter value
 * 
 * @param parameter Parameter name
 * @param line 
 * @param concordancia "E" (Exact phrase), "A" (All words) or "Y" (anY word).
 * @param option operation option
 */
    public void addClauses(String parameter, String line, String concordancia, String option) {        
        currentClause = null;
      
        Iterator values = parseValues(line, concordancia);
        addClauses(parameter, values, concordancia,option);
    } 

/**
 * 
 * 
 * 
 * @param parameter 
 * @param values 
 * @param concordancia 
 * @param operator 
 */
    public void addClauses(String parameter, Iterator values, String concordancia, String operator) {        
           while (values.hasNext())
               addTerm(parameter, (String) values.next(), concordancia);
           addCurrentClauseQuery(operator);
    } 

/**
 * It adds a new term to the full query
 * 
 * 
 * @param parameter Parameter name
 * @param value Parameter Value
 * @param concordancia "E" (Exact phrase), "A" (All words) or "Y" (anY word).
 */
    private void addTerm(String parameter, String value, String concordancia) {        
       StringBuffer term = new StringBuffer();
       term.append(switchOperation(parameter,value));
       if (currentClause == null) {
           currentClause = term.toString();
       } else {
           currentClause = currentClause + term.toString();
           currentClause = enterLabel(currentClause, getOperator(concordancia));
       }
    } 

/**
 * It adds the "and" label to join different operations
 * 
 * 
 * @param operator 
 */
    protected void addCurrentClauseQuery(String operator) {        
       if (currentClause != null) {
           if (currentQuery == null) {
               currentQuery = currentClause;
           } else {
               currentQuery = currentQuery + currentClause;
               currentQuery = enterLabel(currentQuery,operator);
           }
       }
    } 

/**
 * Envuelve a una pregunta con una etiqueta
 * 
 * 
 * @return String : parte de la query en el lenguaje soportado
 * @param pregunta Pregunta a envolver
 * @param etiqueta Nombre de la etiqueta
 */
    public String enterLabel(String pregunta, String etiqueta) {        
           return "<" + etiqueta + ">" + pregunta + "</" + etiqueta + ">";
       
    } 

/**
 * It returns the encoded query
 * 
 * 
 * @return 
 */
    public String toString() {        
            return "<query-request>" +
                    "<gazetteer-query>" + 
            		currentQuery +
            		"</gazetteer-query>" +
            	    "<report-format>standard</report-format>" +
            	    "</query-request>";
               
    } 

/**
 * it is used to choose the opeartion.
 * 
 * 
 * @return 
 * @param parameter Parameter name
 * @param value Parameter value
 */
    public String switchOperation(String parameter, String value) {        
            if (parameter.toLowerCase().equals("title")){
                return containsPhrase(value);
            }
            if (parameter.toLowerCase().equals("thesaurus")){
                return classQuery(value);
            }
            if (parameter.toLowerCase().equals("coordinates")){
                return addBoundingBox(value);
            }
            return "";
    } 

/**
 * It writes a contains-phase operator of the ADL language
 * 
 * 
 * @return 
 * @param value Toponim value
 */
    public String containsPhrase(String value) {        
             return "<name-query operator=\"contains-phrase\" "
             	+ "text=\"" + value + "\"/>";
    } 

/**
 * It writes a class-query operator of the ADL language
 * 
 * 
 * @return 
 * @param value Thesaurus value
 */
    public String classQuery(String value) {        
            return "<class-query " +
            	"thesaurus=\"ADL Feature Type Thesaurus\" " +
            	"term=\"" + value + "\"/>";
    } 

/**
 * It Adds a Bounding Box query
 * 
 * 
 * @return 
 * @param coordinates Coordinates to find
 * @param option 
 */
    public String addBoundingBox(String coordinates) {        
            return "<footprint-query operator=\"overlaps\">" +
            		"<gml:Box>" +
            		"<gml:coordinates>" + coordinates + "</gml:coordinates>" +
            		"</gml:Box>" +
            		"</footprint-query>";
        
    } 

/**
 * Return logic operators
 * 
 * 
 * @return Or or And
 * @param titleKeys E,A o Y --> Exact, All, anY
 */
    public String getOperator(String titleKeys) {        
             if (titleKeys.equals("Y")) {
                return "or";
            } else {
                return "and";
            }
    } 
 }

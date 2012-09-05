
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

/**
 * This class is used to create a Basic Encoding Rules (BER) query.
 * See the specification for more information.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 * @see http://www.loc.gov/z3950/agency/
 */
public class BasicEncodingRules extends AbstractGeneralLanguage {

/**
 * It adds a new clause to the query
 * 
 * 
 * @param use It is a number that represent an attribute (4=Title,62=abstract,...)
 * @param structure It defines the attribute type (1=Phrase,2=wors,...)
 * @param relation Relation between the attribute and the query (1=LessThan,3=equal,...)
 * @param line String with the user introduced value
 * @param concordancia Relationship between different words of the same field (more than one words)
 * E,A o Y --> Exact, All, anY
 * @param operator Relationship between fields (title, abstract)
 * 'and' or 'or'
 */
    public void addClauses(String use, String structure, String relation, String line, String concordancia, String operator) {        
        currentClause = null;
        //Cut the words
        Iterator values = parseValues(line, concordancia);
        addClauses(use, structure, relation, values, concordancia,operator);
    } 

/**
 * It realize the same function than the "addClauses(String use, String structure
 * String relation,String line, String concordancia)" function, but the words
 * to find are in a vector.
 * 
 * 
 * @param use 
 * @param structure 
 * @param relation 
 * @param values 
 * @param concordancia 
 * @param operator 
 */
    public void addClauses(String use, String structure, String relation, Iterator values, String concordancia, String operator) {        
        while (values.hasNext())
            addTerm(use, structure, relation, (String) values.next(),
                getOperator(concordancia));
        addCurrentClauseQuery(operator);
    } 

/**
 * Add a new serch field
 * 
 * 
 * @param use BER use
 * @param structure BER structure
 * @param relation BER relation
 * @param value Filed value
 * @param operator "and" or "or"
 */
    private void addTerm(String use, String structure, String relation, String value, String operator) {        
        StringBuffer term = new StringBuffer();
        if (use != null) {
            term.append("@attr 1=" + use + " ");
        }
        if (structure != null) {
            term.append("@attr 4=" + structure + " ");
        }
        if (relation != null) {
            term.append("@attr 2=" + relation + " ");
        }
        term.append("\"" + value + "\" ");
        if (currentClause == null) {
            currentClause = term.toString();
        } else {
            currentClause = "@" + operator + " " + currentClause + " " + term;
        }
    } 

/**
 * It adds a new query to the current query.
 * 
 * 
 * @param operator 'and' or 'or'. Relation between fields
 */
    protected void addCurrentClauseQuery(String operator) {        
        if (currentClause != null) {
            if (currentQuery == null) {
                currentQuery = currentClause;
            } else {
                currentQuery = "@" + operator + " " + currentQuery + " " + currentClause;
            }
        }
    } 

/**
 * It returns the complete BER query
 * 
 * 
 * @return 
 */
    public String toString(String database) {        
        if ((database == null) || (database.equals(""))){
        	database = "geo";
        }
    	return "@attrset bib-1 " + currentQuery;
    } 
 }

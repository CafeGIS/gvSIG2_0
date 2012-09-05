
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
import java.util.StringTokenizer;

/**
 * This class is used to create a Common Query Language query.
 * See the specification for more information.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 * @see http://www.loc.gov/z3950/agency/zing/cql/
 */
public class CommonQueryLanguage extends AbstractGeneralLanguage {

/**
 * 
 * 
 * 
 * @param parameter 
 * @param line 
 * @param concordancia 
 */
    public void addClauses(String parameter, String line, String concordancia,String operator) {        
        currentClause = null;
        
        Iterator values = parseValues(line, concordancia);
        addClauses(parameter, values, concordancia,operator);
    } 

/**
 * 
 * 
 * 
 * @param parameter 
 * @param values 
 * @param concordancia 
 */
    public void addClauses(String parameter, Iterator values, String concordancia, String operator) {        
        while (values.hasNext())
            addTerm(parameter, (String) values.next(), concordancia);
        addCurrentClauseQuery(operator);
    } 

/**
 * It adds a new search field to the string
 * 
 * 
 * @param parameter 
 * @param value 
 * @param concordancia 
 */
    private void addTerm(String parameter, String value, String concordancia) {        
        StringBuffer term = new StringBuffer();
        term.append(parameter + "=" + cutWord(value, concordancia));
        if (currentClause == null) {
            currentClause = term.toString();
        } else {
            currentClause = "(" + currentClause + " " +
                getOperator(concordancia) + " " + term.toString() + ")";
            
        }          
    } 

/**
 * 
 * 
 */
    protected void addCurrentClauseQuery(String operator) {        
        if (currentClause != null) {
            if (currentQuery == null) {
                currentQuery = currentClause;
            } else {
                currentQuery = "(" + currentQuery + " " +
                operator  + " " + currentClause + ")";
            }
        }
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public String toString() {        
        return currentQuery;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param line 
 * @param titleKeys 
 */
    public String cutWord(String line, String titleKeys) {        
        
        if (titleKeys.equals("E")) {
            StringTokenizer sti = new StringTokenizer(line, " ", true);
            boolean first = true;
            String token = "";
            while (sti.hasMoreTokens()) {
                String currentToken = sti.nextToken();
                if (first) {
                    token = currentToken;
                    first = !first;
                } else if (!(currentToken.equals(" "))) {
                    token = "(" + token + " and " + currentToken + ")";
                }
            }
            return token;
        }
        return line;
    } 
 }

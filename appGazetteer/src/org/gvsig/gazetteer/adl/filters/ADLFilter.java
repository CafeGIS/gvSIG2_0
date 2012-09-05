
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
package org.gvsig.gazetteer.adl.filters;
import java.util.Vector;

import org.gvsig.catalog.utils.Strings;
import org.gvsig.gazetteer.adl.languages.AdlLanguage;
import org.gvsig.gazetteer.filters.AbstractFilter;
import org.gvsig.gazetteer.querys.GazetteerQuery;


/**
 * This class create a query using the gazetteer query language used by
 * ADL
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 * @see http://www.alexandria.ucsb.edu/gazetteer/protocol/
 */
public class ADLFilter extends AbstractFilter {
	private boolean withAccents = true;
/**
 * 
 * 
 */
    public  ADLFilter(boolean withAccents) {        
        super();
        this.withAccents = withAccents;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param query 
 */
    public String getQuery(GazetteerQuery query) {        
        AdlLanguage filter = new AdlLanguage();
                
        if (query.getName() != null) {
        	if (query.getOptions().getSearch().isWithAccents()){
        		Vector v = Strings.allWordForms(query.getName(),withAccents);
        		for (int i=0 ; i<v.size() ; i++){
        			String str = (String)v.get(i);
        			filter.addClauses("title",str,query.getNameFilter(),"or");
        		}        		
        	}else{
        		filter.addClauses("title",query.getName(),query.getNameFilter(),"and");
        	}        	
        }  
                
        if ((query.getFeatures() != null) && (query.getFeatures().length != 0)){
            if (!(query.getFeatures()[0].getName().equals("ThesaurusRoot")))
                filter.addClauses("thesaurus",query.getFeatures()[0].getName(),"E","and");
        }
        
        if ((query.getCoordinates() != null) && (query.isCoordinatesClicked())){
            String sNorth = query.getCoordinates().getUly();
            String sWest = query.getCoordinates().getUlx();
            String sSouth = query.getCoordinates().getBry();
            String sEast = query.getCoordinates().getBrx();
            String sCoordinates = sWest + "," + sSouth + " " + sEast + "," + sNorth;
            
            filter.addClauses("coordinates",sCoordinates,"E",getRelation(query.getCoordinatesFilter()));
        }
              
        return filter.toString();
    } 

/**
 * Return a value for the option of the 'Coordenates Filter'
 * 
 * @param relation String in the combo. Possible values:
 * encloses
 * overlaps
 * fullyOutsideOf
 * equals
 * 
 * @return String
 * Possible values:
 * overlaps
 * within
 * contains
 * @param relacion 
 */
    private static String getRelation(String relacion) {        
        if (relacion.equals("igual")) {
            return "contains";
        }
        if (relacion.equals("contiene")) {
            return "contains";
        }
        if (relacion.equals("incluye")) {
            return "within";
            
        } else {
            return "overlaps";
        }
    } 
 }

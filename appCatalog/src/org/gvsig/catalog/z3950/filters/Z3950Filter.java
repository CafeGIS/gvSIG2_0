
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
package org.gvsig.catalog.z3950.filters;
import org.gvsig.catalog.filters.AbstractFilter;
import org.gvsig.catalog.languages.BasicEncodingRules;
import org.gvsig.i18n.Messages;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class Z3950Filter extends AbstractFilter {
	private String database = null; 
	
    public  Z3950Filter(String database) {        
        super();
        this.database = database;
    } 


    /*
     * (non-Javadoc)
     * @see es.gva.cit.catalog.filters.IFilter#getQuery(es.gva.cit.catalog.querys.CatalogQuery)
     */
    public String getQuery(org.gvsig.catalog.querys.CatalogQuery query) {        
        // Create a RemoteBooleanQuery
        BasicEncodingRules filter = new BasicEncodingRules();
       
        //Title (title, 4), Word List (6)
        if (query.getTitle() != null) {
            filter.addClauses("4", "6", "3", query.getTitle(),
                    query.getTitleFilter(),"and");
        }
        
             
        if (query.isMinimized()){
            if (query.getAbstract() != null) {
                filter.addClauses("62", "6", "3", query.getAbstract(), "E", "or");
            }
        }else{
            if (query.getAbstract() != null) {
                filter.addClauses("62", "6", "3", query.getAbstract(), "E", "and");
            }         
                //  Theme Keyword (themekey, 2002), Word List (6)
            if (query.getThemeKey() != null) {
                filter.addClauses("2002", "6", "3", query.getThemeKey(), "Y","and");
            }
            //  Source Scale Denominator (srcscale, 1024), Numeric String (109)
            if (query.getScale() != null) {
                if (query.getMinScale() != null){
                    filter.addClauses("1024", "109", "4", query.getMinScale(),"E","and"); 
                }
                if (query.getMaxScale() != null){
                    filter.addClauses("1024", "109", "2", query.getMaxScale(),"E","and"); 
                }           
            }
        
            if (query.getProvider() != null) {
                filter.addClauses("1005", "6", "3", query.getProvider(), "E","and");
            }
        //     Beginning Date  (begdate, 1012), Date(210)
            if (query.getDateFrom() != null) {
                filter.addClauses("1012", "210", "18", query.getDateFrom(), "E","and");
            }
            //  Ending Date (date, 1012 ), Date(210)		
            if (query.getDateTo() != null) {
                filter.addClauses("1012", "210", "14", query.getDateTo(), "E","and");
            }
        }
        
        if ((query.getCoordenates() != null) && (query.isCoordinatesClicked())){
                String sNorth = query.getCoordenates().getUly();
                String sWest = query.getCoordenates().getUlx();
                String sSouth = query.getCoordenates().getBry();
                String sEast = query.getCoordenates().getBrx();
                String sBounding = sNorth + " " + sWest + " " + sSouth + " " +
                    sEast;
                String sRelation = getRelation(query.getCoordenatesFilter());
            //  Bounding Coordinates (bounding, 2060), Coordinate String (201)
            filter.addClauses("2060", "201", sRelation, sBounding, "E","and");
        }
        //return query
        return filter.toString(database);
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
 * 9 -> encloses
 * 7 -> overlaps
 * 10 -> fullyOutsideOf
 * 3 -> equals
 * @param translator 
 * @param relacion 
 */
    private String getRelation(String relacion) {        
        if (relacion.equals(Messages.getText("coordinatesEqual"))) {
            return "3";
        }
        if (relacion.equals(Messages.getText("coordinatesContains"))) {
            return "7";
        }
        if (relacion.equals(Messages.getText("coordinatesEnclose"))) {
            return "9";            
        } else {
            return "10";
        }
    } 
 }

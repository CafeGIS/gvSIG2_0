
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
package org.gvsig.catalog.srw.filters;
import org.gvsig.catalog.filters.AbstractFilter;
import org.gvsig.catalog.languages.CommonQueryLanguage;

/**
 * This class create que query in the language suported by the SRW protocol
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class SRWFilter extends AbstractFilter {

/**
 * 
 * 
 */
    public  SRWFilter() {        
        super();
    } 

/**
 * Devuelve un query en el lenguaje que soporte el protocolo SRW
 * 
 * 
 * @param query 
 * @return String : query en el lenguaje soportado
 * @param profile 
 */
    public String getQuery(org.gvsig.catalog.querys.CatalogQuery query) {        
        String pregunta = null;
        // Construimos una RemoteBooleanQuery
        CommonQueryLanguage filter = new CommonQueryLanguage();
        
        if (query.getTitle() != null) {
            filter.addClauses("dc.title", query.getTitle(), query.getTitleFilter(),"And");
        }
        
        if (query.isMinimized()){
        	if (query.getAbstract() != null) {
        		filter.addClauses("dc.subject", query.getAbstract(), "E", "Or");
        	}
        }else{
        	if (query.getAbstract() != null) {
        		filter.addClauses("dc.subject", query.getAbstract(), "E", "And");
        	}
        	
        	if (query.getProvider() != null) {
                filter.addClauses("dc.creator", query.getProvider(), "E","And");
            }
        }        	
        	
        //if (this.getThemeKey() != null) 
        //query.addClauses("", this.getThemeKey(),"Y");
        //if (this.getScale() != null) 
        //query.addClauses("map_scale", this.getScale(),"E");
        
        //if (this.getDateFrom() != null) 
        //query.addClauses("2072", "210", "4",this.getDateFrom(),"E");
        //if (this.getDateTo() != null) 
        //query.addClauses("2073", "210", "2",this.getDateTo(),"E");
        pregunta = filter.toString();
        System.out.println(pregunta);
        return pregunta;
    } 
 }


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
package org.gvsig.gazetteer.wfsg.filters;
import java.util.Vector;

import org.gvsig.catalog.languages.FilterEncoding;
import org.gvsig.catalog.utils.Strings;
import org.gvsig.gazetteer.filters.AbstractFilter;
import org.gvsig.gazetteer.querys.GazetteerQuery;
import org.gvsig.i18n.Messages;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class WFSGFilter extends AbstractFilter {

	public  WFSGFilter() {        
		super();
	} 


	/**
	 * @return 
	 * @param query 
	 */
	public String getQuery(GazetteerQuery query) {        
		String pregunta = null;
		FilterEncoding filter = new FilterEncoding();
		filter.setEscapeCharLabel("escape");
		
		if (query.getName() != null) {
			if (query.getOptions().getSearch().isWithAccents()){
				Vector v = Strings.allWordForms(query.getName(),true);
				for (int i=0 ; i<v.size() ; i++){
					String str = (String)v.get(i);
					filter.addClauses( query.getFieldAttribute(), formatName(str,query.getNameFilter()), query.getNameFilter(),
							FilterEncoding.PROPERTY_IS_LIKE, 
							FilterEncoding.TYPE_LITERAL,
							FilterEncoding.OR);
				}        		
			}else{
				filter.addClauses( query.getFieldAttribute(), formatName(query.getName(),query.getNameFilter()), query.getNameFilter(),
						FilterEncoding.PROPERTY_IS_LIKE, 
						FilterEncoding.TYPE_LITERAL,
						FilterEncoding.AND);
			}       	


		}

		/*
        if (this.getFeature() != null) {
            query.addClauses(equiv.getAbstract(), "*" + getAbstract() + "*",
                "Y", "PropertyIsLike", "L");
        }
		 */

		if ((query.getCoordinates() != null) && (query.isCoordinatesClicked())){
			filter.addBoundingBox(query.getCoordinates(), "position" , getCoordinatesOption(query.getCoordinatesFilter()));
		}

		pregunta = filter.toString();
		return pregunta;
	} 

	/**
	 * This function returns true only when the user has choosen the
	 * "Fully Outside Of" of the coordinates option.
	 * 
	 * 
	 * @return 
	 * @param translator 
	 * @param coordinatesOption 
	 */
	public boolean getCoordinatesOption(String coordinatesOption) {        
		if (coordinatesOption.equals(Messages.getText("coordinatesContains")))
			return false;

		return true; 
	} 
}

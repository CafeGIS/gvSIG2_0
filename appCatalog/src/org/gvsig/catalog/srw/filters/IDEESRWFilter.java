
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
import org.gvsig.catalog.languages.FilterEncoding;
import org.gvsig.catalog.querys.CatalogQuery;
import org.gvsig.catalog.utils.Strings;
import org.gvsig.i18n.Messages;


/**
 * This class creates a RSW query for the IDEE server 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class IDEESRWFilter extends AbstractFilter {
	
	public  IDEESRWFilter() {        
		super();
	} 

	/**
	 * Return a CQL-Text query
	 * @param query 
	 * @return String
	 * @param profile 
	 */
	public String getQuery(CatalogQuery query) {        
		String pregunta = null;

		FilterEncoding filter = new FilterEncoding("", "*", "?", "\\");    

		if (query.getTitle() != null) {
			filter.addClauses("title", query.getTitle(), query.getTitleFilter(),
					"PropertyIsLike", "L", "And");
		}     

		if (query.isMinimized()){
			if (query.getAbstract() != null) {
				filter.addClauses("subject", Strings.addAsteriscsFromAnArray(query.getAbstract()),
						"Y", "PropertyIsLike", "L", "Or");
			}
		}else{
			if (query.getAbstract() != null) {
				filter.addClauses("subject", Strings.addAsteriscsFromAnArray(query.getAbstract()),
						"Y", "PropertyIsLike", "L", "And");
			}


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
		if ((coordinatesOption.equals(Messages.getText("coordinatesEqual"))) ||
				(coordinatesOption.equals(Messages.getText("coordinatesContains"))) ||
				(coordinatesOption.equals(Messages.getText("coordinatesEnclose"))))
			return false;

		return true; 
	} 

}

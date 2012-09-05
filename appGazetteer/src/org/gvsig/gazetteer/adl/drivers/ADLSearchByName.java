
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
package org.gvsig.gazetteer.adl.drivers;
import java.net.URI;
import java.net.URISyntaxException;

import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.GazetteerQuery;


/**
 * This class is used to find and return the coordinates for a place.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ADLSearchByName extends ADLGazetteerServiceDriver {
	public String serverURL = "http://testbed.alexandria.ucsb.edu:8080/gaz/adl_gaz2/dispatch";

	/**
	 * This class needs the server URL to work
	 * @param url Server URL
	 */
	public  ADLSearchByName(String uri) {        
		super();
		setServerURI(uri);
	} 

	/**
	 * Constructor witout fileds
	 * 
	 */
	public  ADLSearchByName() {        
		super();
	} 

	/**
	 * It searches and returns an array of features.
	 * 
	 * 
	 * @return 
	 * @param placeName String with the place name to find.
	 */
	public Feature[] search(String placeName) {        
		GazetteerQuery query = new GazetteerQuery(placeName,"populated places");
		try {
			return getFeature(new URI(getServerURI()),query);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the serverURL.
	 */
	public String getServerURI() {        
		return serverURL;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param serverURL The serverURL to set.
	 */
	public void setServerURI(String serverURL) {        
		this.serverURL = serverURL;
	} 
}


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
package org.gvsig.gazetteer;
import org.gvsig.catalog.DiscoveryServiceClient;
import org.gvsig.gazetteer.drivers.GazetteerCapabilities;
import org.gvsig.gazetteer.drivers.IGazetteerServiceDriver;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.FeatureType;
import org.gvsig.gazetteer.querys.FeatureTypeAttribute;
import org.gvsig.gazetteer.querys.GazetteerQuery;


/**
 * This class represents a gazetteer client. It must be created to
 * use the gazetteer service 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class GazetteerClient extends DiscoveryServiceClient{
	
	public GazetteerClient(String uri, IGazetteerServiceDriver driver) {
		super(uri, driver);		
	}

	/**
	 * @return the feature types
	 * @throws Exception 
	 */
	public FeatureType[] getFeatureTypes() throws Exception {
		return ((GazetteerCapabilities)getCapabilities()).getFeatureTypes();
	}

	/**
	 * This method makes the describeFeatureType
	 * operation
	 * @param featureName
	 * Feature name
	 * @return
	 * @throws Exception 
	 */
	public FeatureTypeAttribute[] describeFeatureType(String featureName) throws Exception{
		return ((IGazetteerServiceDriver)getDriver()).describeFeatureType(getUri(), featureName);
	}
	
	/**
	 * @return if the describeFeatureType operation is
	 * supported by the driver
	 */
	public boolean isDescribeFeatureTypeNeeded(){
		return ((IGazetteerServiceDriver)getDriver()).isDescribeFeatureTypeNeeded();
	}

	/**
	 * This method implements the getFeature operation
	 * @return 
	 * A result collection
	 * @param query 
	 * The results query
	 * @throws Exception 
	 */
	public Feature[] getFeature(GazetteerQuery query) throws Exception {        
		return ((IGazetteerServiceDriver)getDriver()).getFeature(getUri(), query);
	} 	

	/**
	 * This method is used to create a new Query
	 * @return 
	 */
	public GazetteerQuery createNewQuery() {        
		return new GazetteerQuery();   
	} 	
	
	/**
	 * @return the projection
	 */
	public String getProjection() {
		return ((IGazetteerServiceDriver)getDriver()).getProjection();
	}

	/**
	 * @param projection the projection to set
	 */
	public void setProjection(String projection) {
		((IGazetteerServiceDriver)getDriver()).setProjection(projection);
	} 
}

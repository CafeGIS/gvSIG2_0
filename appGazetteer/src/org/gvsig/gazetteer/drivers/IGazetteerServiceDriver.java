
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
package org.gvsig.gazetteer.drivers;
import java.net.URI;

import org.gvsig.catalog.drivers.IDiscoveryServiceDriver;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.FeatureTypeAttribute;
import org.gvsig.gazetteer.querys.GazetteerQuery;

/**
 * This interface contains the common methods that have to be
 * implemented by all the gazetteer drivers.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public interface IGazetteerServiceDriver extends IDiscoveryServiceDriver{
	/**
	 * It returns feature properties
	 * @param uri Server URI
	 * @param feature Feature name
	 * @return Array of the attributes of a feature
	 */
	public FeatureTypeAttribute[] describeFeatureType(URI uri, String featureType) throws Exception;
	
	/**
	 * There are protocols that need to invoke the describeFeatureType
	 * operation before to do a getFeature.
	 * @return if the describeFeatureType operation is needed. 
	 */
	public boolean isDescribeFeatureTypeNeeded();
	
	/**
	 * It returns the foubd records
	 * @param uri Server URI
	 * @param query Query with the search parameters
	 * @return The features
	 */	
	public Feature[] getFeature(URI uri, GazetteerQuery query) throws Exception;
		
	/**
	 * @return the projection
	 */
	public String getProjection();

	/**
	 * @param projection the projection to set
	 */
	public void setProjection(String projection);

}



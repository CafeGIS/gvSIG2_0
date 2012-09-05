
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

import org.gvsig.catalog.drivers.AbstractDiscoveryServiceDriver;
import org.gvsig.catalog.querys.DiscoveryServiceQuery;
import org.gvsig.gazetteer.querys.FeatureType;
import org.gvsig.gazetteer.querys.FeatureTypeAttribute;
import org.gvsig.gazetteer.querys.GazetteerQuery;


/**
 * This class must be inherited by all the gazetteer drivers. It contains
 * the common attributes. 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public abstract class AbstractGazetteerServiceDriver extends AbstractDiscoveryServiceDriver implements IGazetteerServiceDriver {
	private FeatureType[] featureTypes;
	private GazetteerQuery query;
	private String projection = null;	

	/**
	 * @return Returns the feature.
	 */
	public FeatureType[] getFeatureTypes() {        
		return featureTypes;
	} 

	/**
	 * @param feature The feature to set.
	 */
	public void setFeatureTypes(FeatureType[] featureTypes) {        
		this.featureTypes = featureTypes;
	} 	

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.IDiscoveryServiceDriver#createQuery()
	 */
	public DiscoveryServiceQuery createQuery(){
		return new GazetteerQuery();
	}
	
	/**
	 * @return Returns the query.
	 */
	public GazetteerQuery getQuery() {        
		return query;
	} 

	/**
	 * @param query The query to set.
	 */
	public void setQuery(GazetteerQuery query) {        
		this.query = query;
	}	

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#describeFeatureType(java.net.URI, java.lang.String)
	 */
	public FeatureTypeAttribute[] describeFeatureType(URI uri, String feature) {        
		return null;
	} 	

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#isDescribeFeatureTypeNeeded()
	 */
	public boolean isDescribeFeatureTypeNeeded()  {        
		return false;
	} 

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getProjection()
	 */	 
	public String getProjection(){
		return projection;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#setProjection(java.lang.String)
	 */
	public void setProjection(String projection){
		this.projection = projection;
	}
}

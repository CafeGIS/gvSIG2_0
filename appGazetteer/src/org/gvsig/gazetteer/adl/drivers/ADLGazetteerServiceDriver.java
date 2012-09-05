
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.protocols.HTTPPostProtocol;
import org.gvsig.gazetteer.adl.filters.ADLFilter;
import org.gvsig.gazetteer.adl.parsers.AdlCapabilitiesParser;
import org.gvsig.gazetteer.adl.parsers.AdlFeatureParser;
import org.gvsig.gazetteer.drivers.AbstractGazetteerServiceDriver;
import org.gvsig.gazetteer.drivers.GazetteerCapabilities;
import org.gvsig.gazetteer.drivers.IGazetteerServiceDriver;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.GazetteerQuery;

import com.iver.utiles.swing.jcomboServer.ServerData;


/**
 * This class implements the driver to connect with a
 * ADL gazetteer service
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @see http://alexandria.sdc.ucsb.edu/
 */
public class ADLGazetteerServiceDriver extends AbstractGazetteerServiceDriver implements IGazetteerServiceDriver {

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getCapabilities(java.net.URI)
	 */
	public DiscoveryServiceCapabilities getCapabilities(URI uri) {        
		GazetteerCapabilities capabilities = new GazetteerCapabilities();
		Collection nodes = new java.util.ArrayList();  
		URL url = null;
			try {
				url = uri.toURL();
			} catch (MalformedURLException e) {
				capabilities.setServerMessage("errorServerNotFound");
				capabilities.setAvailable(false);
				return capabilities;
			}        
		try {
			nodes = new HTTPPostProtocol().doQuery(uri.toURL(),
					getPOSTMessageCapabilities(), 0);
		} catch (MalformedURLException e) {
			capabilities.setServerMessage(e.getMessage());
			capabilities.setAvailable(false);
			return capabilities;
		}		
		AdlCapabilitiesParser parser = new AdlCapabilitiesParser(this, capabilities);
		parser.parse(nodes);
		return capabilities;
	} 
	
	/**
	 * It creates the XML for the getCapabilities request
	 * @return Name-value pair with a XML request
	 */
	private String getPOSTMessageCapabilities() {        
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
		"<gazetteer-service " +
		"xmlns=\"http://www.alexandria.ucsb.edu/gazetteer\" " +
		"version=\"1.2\">" +
		"<get-capabilities-request/>" +
		"</gazetteer-service>";        
	} 	
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getFeature(java.net.URI, es.gva.cit.gazetteer.querys.Query)
	 */
	public Feature[] getFeature(URI uri, GazetteerQuery query) {        
		Collection nodes = new java.util.ArrayList();
		setQuery(query);
		URL url = null;
		try {
			url = uri.toURL();
		} catch (MalformedURLException e) {
			setServerAnswerReady("errorServerNotFound");
			return null;
		}    
		System.out.println("**************POST*************");
		System.out.println(getPOSTGetFeature(getQuery(),true));
		nodes = new HTTPPostProtocol().doQuery(url,
				getPOSTGetFeature(getQuery(),true), 0);

		if (nodes == null){
			System.out.println(getPOSTGetFeature(getQuery(),false));
			nodes = new HTTPPostProtocol().doQuery(url,
					getPOSTGetFeature(getQuery(),false), 0);
		}
		if ((nodes != null) && (nodes.size() == 1)){
			return AdlFeatureParser.parse((XMLNode)nodes.toArray()[0]);
		}else{
			return null;
		}
	} 

	/**
	 * It creates the XML for the getFeature request
	 * @return Name-value pair with a XML request
	 * @param query 
	 */
	private String getPOSTGetFeature(GazetteerQuery query,boolean withAccents) {        
		return  "<gazetteer-service " +
		"xmlns=\"http://www.alexandria.ucsb.edu/gazetteer\" " +
		"xmlns:gml=\"http://www.opengis.net/gml\" " +
		"version=\"1.2\">" +
		new ADLFilter(withAccents).getQuery(query) +
		"</gazetteer-service>" ;
	} 
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#isProtocolSupported(java.net.URI)
	 */
	public boolean isProtocolSupported(URI uri) {        
		// TODO Auto-generated method stub
		return true;
	} 	

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getDefaultPort()
	 */
	public int getDefaultPort() {
		return 80;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getDefaultSchema()
	 */
	public String getDefaultSchema() {
		return "http";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getServiceName()
	 */
	public String getServiceName() {
		return ServerData.SERVER_SUBTYPE_GAZETTEER_ADL;
	}
}

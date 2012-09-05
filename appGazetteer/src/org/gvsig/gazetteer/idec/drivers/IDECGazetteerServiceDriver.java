
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
package org.gvsig.gazetteer.idec.drivers;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.httpclient.NameValuePair;
import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.protocols.HTTPGetProtocol;
import org.gvsig.catalog.protocols.HTTPPostProtocol;
import org.gvsig.catalog.utils.Strings;
import org.gvsig.gazetteer.drivers.AbstractGazetteerServiceDriver;
import org.gvsig.gazetteer.idec.parsers.IdecCapabilitiesParser;
import org.gvsig.gazetteer.idec.parsers.IdecFeatureParser;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.GazetteerQuery;

import com.iver.utiles.swing.jcomboServer.ServerData;


/**
 * This class connects with the IDEC gazetteer service
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class IDECGazetteerServiceDriver extends AbstractGazetteerServiceDriver {

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.IDiscoveryServiceDriver#getCapabilities(java.net.URI)
	 */
	public DiscoveryServiceCapabilities getCapabilities(URI uri) {        
		Collection nodes = new java.util.ArrayList();
		URL url = null;
		try {
			url = uri.toURL();
		} catch (MalformedURLException e) {
			setServerAnswerReady("errorServerNotFound");
			return null;
		}       
		nodes = new HTTPGetProtocol().doQuery(url, getMessageCapabilities(), 0);
		IdecCapabilitiesParser parser  = new IdecCapabilitiesParser(this);
		parser.parse(nodes);
		return parser.getCapabilities();	
	} 

	/**
	 * It creates the name-value pairs for the getCapabilities request
	 * @return Name-Value pair array
	 */
	private NameValuePair[] getMessageCapabilities() {        
		NameValuePair nvp1 = new NameValuePair("wsdl", "");
		return new NameValuePair[] { nvp1 };
	} 	
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getFeature(java.net.URI, es.gva.cit.gazetteer.querys.Query)
	 */
	public Feature[] getFeature(URI uri, GazetteerQuery query) {        
		Collection nodes = new java.util.ArrayList();
		  URL url = null;
			try {
				url = uri.toURL();
			} catch (MalformedURLException e) {
				setServerAnswerReady("errorServerNotFound");
				return null;
			}       
		setQuery(query);
		System.out.println(getPOSTGetFeature(query));
		nodes = new HTTPPostProtocol().doQuery(url,getPOSTGetFeature(query),0);
		if ((nodes != null) && (nodes.size() == 1)){
			return IdecFeatureParser.parse((XMLNode)nodes.toArray()[0]);
		}else{
			return null;
		}
	} 

	/**
	 * It creates the XML for the getFeature request
	 * @return Name-value pair with a XML request
	 * @param query 
	 */
	private String getPOSTGetFeature(GazetteerQuery query) {        
		String name = query.getName();
		if (query.getOptions().getSearch().isWithAccents()){
			name = Strings.removeAccents(name);
		}    	
		return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
		"<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:si=\"http://soapinterop.org/xsd\" xmlns:tns=\"urn:idecwsdl\">" +
		"<SOAP-ENV:Body><tns:getCoordenadesUTM xmlns:tns=\"urn:idecwsdl\">" +
		"<key xsi:type=\"xsd:string\">CV81HP6</key>" +
		"<toponim xsi:type=\"xsd:string\">" + name + "</toponim>" +
		"</tns:getCoordenadesUTM>" +
		"</SOAP-ENV:Body>" +
		"</SOAP-ENV:Envelope>";
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
		return ServerData.SERVER_SUBTYPE_GAZETTEER_IDEC;
	} 
}

package org.gvsig.gazetteer.wfsg.drivers;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.protocols.HTTPPostProtocol;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.GazetteerQuery;
import org.gvsig.gazetteer.wfs.drivers.WFSServiceDriver;
import org.gvsig.gazetteer.wfsg.parsers.WfsgCapabilitiesParser;
import org.gvsig.gazetteer.wfsg.parsers.WfsgFeatureParser;

import com.iver.utiles.swing.jcomboServer.ServerData;


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
/* CVS MESSAGES:
 *
 * $Id: WFSGServiceDriver.java 539 2007-07-26 11:21:38 +0000 (Thu, 26 Jul 2007) jpiera $
 * $Log$
 * Revision 1.1.2.2  2007/07/13 12:00:35  jorpiell
 * Add the posibility to add a new panel
 *
 * Revision 1.1.2.1  2007/07/10 11:18:04  jorpiell
 * Added the registers
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class WFSGServiceDriver extends WFSServiceDriver {
	//TODO use the download manager instead of this
	private static final String tempDirectoryPath = System.getProperty("java.io.tmpdir")+"/tmp-andami";
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.wfs.drivers.WFSServiceDriver#getCapabilities(java.net.URI)
	 */
	public DiscoveryServiceCapabilities getCapabilities(URI uri) {
		URL url = null;
		try {
			url = uri.toURL();
		} catch (MalformedURLException e) {
			setServerAnswerReady("errorServerNotFound");
			return null;
		}
		Collection nodes = new HTTPPostProtocol().doQuery(url,
				WFSGMessages.getHTTPPOSTCapabilities(), 0);
		
		WfsgCapabilitiesParser parser = new WfsgCapabilitiesParser(this);
		parser.parse(nodes);
		return parser.getCapabilities();			
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.wfs.drivers.WFSServiceDriver#getFeature(java.net.URI, es.gva.cit.gazetteer.querys.Query)
	 */
	 public Feature[] getFeature(URI uri, GazetteerQuery query) {        
		 URL url = null;
			try {
				url = uri.toURL();
			} catch (MalformedURLException e) {
				setServerAnswerReady("errorServerNotFound");
				return null;
			}
			query.setFieldAttribute("geographicIdentifier");
			Collection nodes = new HTTPPostProtocol().doQuery(url,
					WFSGMessages.getHTTPPOSTFeature(query,0),
					0);			
			
			return new WfsgFeatureParser().parse((XMLNode)nodes.toArray()[0]);
					 
	 }

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.AsbtractGazetteerServiceDriver#isDescribeFeatureTypeNeeded()
	 */
	public boolean isDescribeFeatureTypeNeeded(){
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.drivers.IGazetteerServiceDriver#getServiceName()
	 */
	public String getServiceName() {
		return ServerData.SERVER_SUBTYPE_GAZETTEER_WFSG;
	}	
}

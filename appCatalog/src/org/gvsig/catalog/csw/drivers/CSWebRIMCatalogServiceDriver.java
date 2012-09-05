package org.gvsig.catalog.csw.drivers;

import java.net.URL;

import org.apache.commons.httpclient.NameValuePair;
import org.gvsig.catalog.csw.drivers.profiles.CSWebRIMProfile;
import org.gvsig.catalog.csw.parsers.CSWConstants;
import org.gvsig.catalog.drivers.profiles.IProfile;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.protocols.HTTPGetProtocol;


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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class CSWebRIMCatalogServiceDriver extends CSWCatalogServiceDriver{
	public static String SERVICE_NAME = "CSW/ebRIM";
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getServiceName()
	 */
	public String getServiceName() {
		return SERVICE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.drivers.AbstractCatalogServiceDriver#getProfile()
	 */
	public IProfile getProfile() {
		return new CSWebRIMProfile();
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.CSWCatalogServiceDriver#retrieveResults(es.gva.cit.catalog.metadataxml.XMLNode)
	 */
	protected XMLNode[] retrieveResults(XMLNode root) {
		XMLNode resultNode = root.searchNode(CSWConstants.SEARCH_RESULTS);
		if (resultNode == null){
			return new XMLNode[0];
		}
		XMLNode[] results = new XMLNode[resultNode.getNumSubNodes()];
		for (int i=0 ; i<resultNode.getNumSubNodes() ; i++){
			results[i] = resultNode.getSubNode(i);
		}
		return getEbRIMNodes(results);
	}
	
	/**
	 * This function retrieve the nodes for one ebRIM answer
	 * @return Medatada Nodes.
	 * @param nodes Server URL
	 * @param url 
	 */
	private XMLNode[] getEbRIMNodes(XMLNode[] nodes) {        
		//if the getExtrinsincContentOperation is not supported
		if (capabilities.getOperations().getGetExtrinsicContent().size() == 0){
			return nodes;			
		}
		XMLNode[] auxNodes = new XMLNode[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			String id = nodes[i].searchAtribute(CSWConstants.ID);
			URL url = (URL)capabilities.getOperations().getGetExtrinsicContent().get(CSWConstants.GET);
			auxNodes[i] = (XMLNode)new HTTPGetProtocol().doQuery(url,
				getEbRIMRequestParameters(id), 0).toArray()[0];						
		}
		return auxNodes;
	} 
	
	/**
	 * It Returns the parameters needed by getExtrinsicContent
	 * @return 
	 * @param id Record id
	 */
	private NameValuePair[] getEbRIMRequestParameters(String id) {        
		NameValuePair nvp1 = new NameValuePair(CSWConstants.REQUEST, CSWConstants.EXTRISIC_CONTENT);
		NameValuePair nvp2 = new NameValuePair(CSWConstants.ID, id);
		return new NameValuePair[] { nvp1, nvp2 };
	} 
}

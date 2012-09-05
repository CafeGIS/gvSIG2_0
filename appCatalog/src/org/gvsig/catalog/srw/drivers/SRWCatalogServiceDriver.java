
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
package org.gvsig.catalog.srw.drivers;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.StringTokenizer;

import org.gvsig.catalog.drivers.AbstractCatalogServiceDriver;
import org.gvsig.catalog.drivers.CatalogCapabilities;
import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.catalog.drivers.GetRecordsReply;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.catalog.protocols.HTTPGetProtocol;
import org.gvsig.catalog.protocols.SOAPProtocol;
import org.gvsig.catalog.querys.CatalogQuery;
import org.gvsig.catalog.srw.parsers.SrwCapabilitiesParser;

import com.iver.utiles.swing.jcomboServer.ServerData;


/**
 * This class implements the CSW protocol.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 * @see http://www.loc.gov/z3950/agency/zing/srw/
 */
public class SRWCatalogServiceDriver extends AbstractCatalogServiceDriver {
	private String version = "1.1";
	private String recordXPath;
	private String resultSetTTL;

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getCapabilities(java.net.URI)
	 */
	public DiscoveryServiceCapabilities getCapabilities(URI uri) {        
		URL url = null;
		try {
			url = uri.toURL();
		} catch (MalformedURLException e) {
			setServerAnswerReady("errorServerNotFound");
			return null;
		}        
		SRWMessages messages = new SRWMessages(this);
		Collection nodes = new HTTPGetProtocol().doQuery(url,
				messages.getHTTPGETCapabilities(true), 0);

		nodes = new SOAPProtocol().doQuery(url, messages.getSOAPCapabilities(), 0);
		new SrwCapabilitiesParser(this).parse((XMLNode)nodes.toArray()[0]);
		CatalogCapabilities capabilities = new CatalogCapabilities();
		capabilities.setVersion(version);
		capabilities.setServerMessage(getServerAnswerReady());
		return capabilities;
	} 

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getRecords(java.net.URI, es.gva.cit.catalogClient.querys.Query, int)
	 */
	public GetRecordsReply getRecords(URI uri, CatalogQuery query, int firstRecord) {        
		URL url = null;
		try {
			url = uri.toURL();
		} catch (MalformedURLException e) {
			setServerAnswerReady("errorServerNotFound");
			return null;
		}        
		setQuery(query);
		SRWMessages messages = new SRWMessages(this);
		Collection nodes = new java.util.ArrayList();

		//TODO remove this comparation
		if (url.getHost().equals("idee.unizar.es")){
			try {
				url = new URL("http://idee.unizar.es/SRW/servlet/search/SRW");
			} catch (MalformedURLException e) {
				// It will never throws
			}
		}
	      
		XMLNode root = null;
		nodes = new SOAPProtocol().doQuery(url,
				messages.getSOAPRecords(getQuery(), firstRecord), firstRecord);
		root = (XMLNode)nodes.toArray()[0];
		root = root.getSubNode(0).getSubNode(0);

		String prefix = new StringTokenizer(root.getName(), ":").nextToken();
		if (prefix.equals(root.getName())) {
			prefix = "";
		} else {
			prefix = prefix + ":";
		}        
		int numberOfRecords = getNumberOfRecords(root,
				prefix + "numberOfRecords",
				null);

		if (numberOfRecords == -1) {
			return null;
		}   
		GetRecordsReply recordsReply = new GetRecordsReply(numberOfRecords);

		parseRecords(root,recordsReply,uri,prefix,numberOfRecords,firstRecord);		
		return recordsReply;
	} 

	/**
	 * Parser the XML
	 * @param node
	 * @param recordsReply
	 * @param uri
	 * @param prefix
	 * @param numberOfRecords
	 * @param firstRecord
	 */
	private void parseRecords(XMLNode node, GetRecordsReply recordsReply, URI uri, String prefix, int numberOfRecords, int firstRecord) {        

		XMLNode[] auxNodes = XMLTree.searchMultipleNode(node,
				prefix + "records->" + prefix + "record");
		for (int i = 1;	(i <= numberOfRecords) && (i <= 10) &&	(i <= (numberOfRecords - firstRecord + 1)); i++){
			recordsReply.addRecord(uri, auxNodes[i - 1]);
		}

	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the recordXPath.
	 */
	public String getRecordXPath() {        
		return recordXPath;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param recordXPath The recordXPath to set.
	 */
	public void setRecordXPath(String recordXPath) {        
		this.recordXPath = recordXPath;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the resultSetTTL.
	 */
	public String getResultSetTTL() {        
		return resultSetTTL;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param resultSetTTL The resultSetTTL to set.
	 */
	public void setResultSetTTL(String resultSetTTL) {        
		this.resultSetTTL = resultSetTTL;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the version.
	 */
	public String getVersion() {        
		return version;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param version The version to set.
	 */
	public void setVersion(String version) {        
		this.version = version;
	} 

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#isProtocolSupported(java.net.URI)
	 */
	public boolean isProtocolSupported(URI uri) {        
		return SOAPProtocol.isProtocolSupported(null);
	} 

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getServiceName()
	 */
	public String getServiceName() {
		return ServerData.SERVER_SUBTYPE_CATALOG_SRW;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getDefaultPort()
	 */
	public int getDefaultPort() {		
		return 80;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#getDefaultSchema()
	 */
	public String getDefaultSchema() {
		return "http";
	} 
}


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
package org.gvsig.catalog.csw.drivers;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

import org.gvsig.catalog.csw.drivers.profiles.CSWAbstractProfile;
import org.gvsig.catalog.csw.messages.CSWAbstractMessages;
import org.gvsig.catalog.csw.messages.CSWMessagesFactory;
import org.gvsig.catalog.csw.parsers.CSWCapabilitiesParser;
import org.gvsig.catalog.csw.parsers.CSWDescribeRecordParser;
import org.gvsig.catalog.drivers.AbstractCatalogServiceDriver;
import org.gvsig.catalog.drivers.DiscoveryServiceCapabilities;
import org.gvsig.catalog.drivers.GetRecordsReply;
import org.gvsig.catalog.exceptions.NotSupportedVersionException;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.protocols.HTTPGetProtocol;
import org.gvsig.catalog.protocols.HTTPPostProtocol;
import org.gvsig.catalog.querys.CatalogQuery;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;


/**
 * This class implements the CSW protocol.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 * @see http://portal.opengeospatial.org/files/?artifact_id=5929&version=1
 */
public abstract class CSWCatalogServiceDriver extends AbstractCatalogServiceDriver {
	protected CSWCapabilities capabilities = null;
	protected CSWAbstractProfile profile = null;
	private static Hashtable servers = new Hashtable();
	
	/**
	 * @return 
	 * @param url 
	 * @throws NotSupportedVersionException 
	 */
	public DiscoveryServiceCapabilities getCapabilities(URI uri) throws NotSupportedVersionException {        
		URL url = null;
		try {
			url = uri.toURL();
		} catch (MalformedURLException e) {
			setServerAnswerReady("errorServerNotFound");
			return null;
		}        
		Collection nodes = new java.util.ArrayList();
		nodes = new HTTPGetProtocol().doQuery(url, CSWAbstractMessages.getHTTPGETCapabilities(), 0);
		CSWCapabilitiesParser parser = new CSWCapabilitiesParser(url, this);
		capabilities = parser.parse((XMLNode)nodes.toArray()[0]);
		//There are servers that need the getCapabilities operation (instead of GetCapabilities)
		if (!(capabilities.isAvailable()) && (capabilities.getException() != null)){
			CSWException exception = capabilities.getException();
			if ((exception.getCode().equals(CSWException.CODE_ERROR)) &&
					(exception.getSubcode().equals(CSWException.SUBCODE_ERROR))){
				nodes = new HTTPGetProtocol().doQuery(url, CSWAbstractMessages.getHTTPGETCapabilitiesLower(), 0);
				capabilities = parser.parse((XMLNode)nodes.toArray()[0]);
			}
		}
		//If the version can be retrieved the CSWAbstractMessages object
		//cant be created
		setMessages(uri,url);
		return capabilities;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.drivers.ICatalogServiceDriver#getRecords(java.net.URI, es.gva.cit.catalog.querys.CatalogQuery, int)
	 */
	public GetRecordsReply getRecords(URI uri, CatalogQuery query,
			int firstRecord) {		        
		URL url = capabilities.getOperations().getGetRecordsURLPost();	       
		setQuery(query);
		Collection nodes = new java.util.ArrayList();
		try {
			System.out.println(getMessages(uri).getHTTPPostGetRecordsMessage(capabilities, getQuery(), getServerData(),  firstRecord));
			nodes = new HTTPPostProtocol().doQuery(url,
					getMessages(uri).getHTTPPostGetRecordsMessage(capabilities, getQuery(), getServerData(), firstRecord),
					firstRecord);
		} catch (NotSupportedVersionException e) {
			//This exception never will be thrown
		}           	
		
		if (nodes == null) 
			return null;

		XMLNode root = (XMLNode)nodes.toArray()[0];
		int numberOfRecords = getNumberOfRecords(root);
		if (numberOfRecords == -1) {
			return null;
		}
		
		getRecordsReply().setNumRecords(numberOfRecords);
		XMLNode[] results = retrieveResults(root);
		for (int i=0 ; i<results.length ; i++){
			getRecordsReply().addRecord(uri, results[i]);
		}
		return getRecordsReply();
	}
	
	/**
	 * Retrieve and parse the results
	 * @param root
	 * XML root node
	 * @return
	 */
	protected abstract XMLNode[] retrieveResults(XMLNode root);

	/**
	 * This function returns the number of records that have been retrieved.
	 * It Reads a Node value.
	 * 
	 * 
	 * @return The number of records
	 * @param node The answer tree
	 */
	protected int getNumberOfRecords(XMLNode node) {        
		int numberOfRecords = getNumberOfRecords(node,"csw:SearchResults","numberOfRecordsMatched");
		if (numberOfRecords == -1)
			numberOfRecords = getNumberOfRecords(node,"SearchResults","numberOfRecordsMatched");

		return numberOfRecords;
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

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalogClient.drivers.ICatalogServiceDriver#isProtocolSupported(java.net.URI)
	 */
	public boolean isProtocolSupported(URI uri) {
		return true;
	} 
	
	/**
	 * @return the class to generate the messages. It depends
	 * of the server version
	 * @throws NotSupportedVersionException 
	 */
	private CSWAbstractMessages getMessages(URI uri) throws NotSupportedVersionException{
		if (servers.containsKey(uri)){
			return (CSWAbstractMessages)servers.get(uri);
		}
		CSWAbstractMessages messages = CSWMessagesFactory.getMessages(capabilities.getVersion(),
				(CSWAbstractProfile)getProfile());
		servers.put(uri, messages);
		return messages;
	}
	
	/**
	 * Create and sets the Messages obejct if it is possible
	 * @param uri
	 * Server URI
	 * @param url
	 * Server url
	 * @throws NotSupportedVersionException
	 */
	private void setMessages(URI uri,URL url)throws NotSupportedVersionException{
		Collection nodes = new java.util.ArrayList();
		nodes = new HTTPGetProtocol().doQuery(url, CSWAbstractMessages.getHTTPGETDescribeRecord(capabilities.getVersion()), 0);
		CSWDescribeRecordParser parser = new CSWDescribeRecordParser(url, this);
		parser.parse((XMLNode)nodes.toArray()[0]);
	}	
}

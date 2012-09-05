
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
package org.gvsig.catalog.csw.parsers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * This class is use like a structure. It saves what protocol is
 * supported by each CSW operation. Each attribute is an array that
 * can contain next values: GET, POST or SOAP
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class CSWSupportedProtocolOperations {
	private HashMap getCapabilities = null;;
	private HashMap describeRecords = null;
	private HashMap getDomain = null;
	private HashMap getRecords = null;
	private HashMap getRecordsById = null;
	private HashMap transaction = null;
	private HashMap harvest = null;
	private HashMap getExtrinsicContent = null;
	private URL url = null;

	public CSWSupportedProtocolOperations(URL url){
		this.url = url;
		getCapabilities = new HashMap();
		describeRecords = new HashMap();
		getDomain = new HashMap();
		getRecords = new HashMap();
		getRecordsById = new HashMap();
		transaction = new HashMap();
		harvest = new HashMap();
		getExtrinsicContent = new HashMap();
	}	
	
	/**
	 * Add a new URL
	 * @param operation
	 * Table of the opeartion
	 * @param protocol
	 * Protocol
	 * @param sUrl
	 * URL to parse
	 * @throws MalformedURLException 
	 */
	private void addOperation(HashMap operation, String protocol, String sUrl) throws MalformedURLException{
		URL url = new URL(sUrl);
		operation.put(protocol, url);
	}
	
	/**
	 * @return Returns the describeRecords.
	 */
	public HashMap getDescribeRecords() {        
		return describeRecords;
	} 

	/**
	 * @param describeRecords The describeRecords to set.
	 * @throws MalformedURLException 
	 */
	public void addDescribeRecord(String protocol, String url) throws MalformedURLException {        
		addOperation(describeRecords,protocol, url);
	} 

	/**
	 * @return Returns the getCapabilities.
	 */
	public HashMap getGetCapabilities() {        
		return getCapabilities;
	} 

	/**
	 * @param getCapabilities The getCapabilities to set.
	 */
	public void addGetCapabilities(String protocol, String url) throws MalformedURLException {        
		addOperation(getCapabilities,protocol, url);
	} 

	/**
	 * @return Returns the getDomain.
	 */
	public HashMap getGetDomain() {        
		return getDomain;
	} 

	/**
	 * @param getDomain The getDomain to set.
	 */
	public void addGetDomain(String protocol, String url) throws MalformedURLException {        
		addOperation(getDomain,protocol, url);
	} 
	
	/**
	 * @return Returns the getRecords.
	 */
	public HashMap getGetRecords() {        
		return getRecords;
	} 

	/**
	 * @param getRecords The getRecords to set.
	 */
	public void addGetRecords(String protocol, String url) throws MalformedURLException {        
		addOperation(getRecords,protocol, url);
	} 

	/**
	 * @return Returns the getRecordsById.
	 */
	public HashMap getGetRecordsById() {        
		return getRecordsById;
	} 

	/**
	 * @param getRecordsById The getRecordsById to set.
	 */
	public void addGetRecordsById(String protocol, String url) throws MalformedURLException {        
		addOperation(getRecordsById,protocol, url);
	} 
	/**
	 * @return Returns the harvest.
	 */
	public HashMap getHarvest() {        
		return harvest;
	} 

	/**
	 * @param harvest The harvest to set.
	 */
	public void addHarvest(String protocol, String url) throws MalformedURLException {        
		addOperation(harvest,protocol, url);
	} 

	/**
	 * @return Returns the transaction.
	 */
	public HashMap getTransaction() {        
		return transaction;
	} 

	/**
	 * @param transaction The transaction to set.
	 */
	public void setTransaction(String protocol, String url) throws MalformedURLException {        
		addOperation(transaction,protocol, url);
	}

	/**
	 * @return the URL for the getRecords operation
	 * using the HTTP Post method
	 */
	public URL getGetRecordsURLPost() {
		Object obj = getRecords.get(CSWConstants.POST);
		if (obj != null){
			return (URL)obj;
		}
		return url;
	}

	/**
	 * @param getCapabilities the getCapabilities to set
	 */
	public void setGetCapabilities(HashMap getCapabilities) {
		this.getCapabilities = getCapabilities;
	}

	/**
	 * @param describeRecords the describeRecords to set
	 */
	public void setDescribeRecords(HashMap describeRecords) {
		this.describeRecords = describeRecords;
	}

	/**
	 * @param getDomain the getDomain to set
	 */
	public void setGetDomain(HashMap getDomain) {
		this.getDomain = getDomain;
	}

	/**
	 * @param getRecords the getRecords to set
	 */
	public void setGetRecords(HashMap getRecords) {
		this.getRecords = getRecords;
	}

	/**
	 * @param getRecordsById the getRecordsById to set
	 */
	public void setGetRecordsById(HashMap getRecordsById) {
		this.getRecordsById = getRecordsById;
	}

	/**
	 * @param transaction the transaction to set
	 */
	public void setTransaction(HashMap transaction) {
		this.transaction = transaction;
	}

	/**
	 * @param harvest the harvest to set
	 */
	public void setHarvest(HashMap harvest) {
		this.harvest = harvest;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/**
	 * @return the getExtrinsicContent
	 */
	public HashMap getGetExtrinsicContent() {
		return getExtrinsicContent;
	}

	/**
	 * @param getExtrinsicContent the getExtrinsicContent to set
	 */
	public void setGetExtrinsicContent(HashMap getExtrinsicContent) {
		this.getExtrinsicContent = getExtrinsicContent;
	} 
	
	/**
	 * @param getDomain The getDomain to set.
	 */
	public void addGetExtrinsicContent(String protocol, String url) throws MalformedURLException {        
		addOperation(getExtrinsicContent,protocol, url);
	} 
}

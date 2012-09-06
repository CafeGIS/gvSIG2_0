package org.gvsig.remoteClient.wfs;

import java.io.File;
import java.net.URL;
import java.util.Hashtable;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.gvsig.remoteClient.ogc.OGCProtocolHandler;
import org.gvsig.remoteClient.ogc.OGCServiceInformation;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.utils.Utilities;
import org.gvsig.remoteClient.wfs.edition.WFSTTransaction;
import org.gvsig.remoteClient.wfs.exceptions.WFSException;
import org.gvsig.remoteClient.wfs.request.WFSDescribeFeatureTypeRequest;
import org.gvsig.remoteClient.wfs.request.WFSGetFeatureRequest;
import org.gvsig.remoteClient.wfs.request.WFSTLockFeatureRequest;
import org.gvsig.remoteClient.wfs.schema.GMLTags;
import org.gvsig.remoteClient.wms.ICancellable;
import org.kxml2.io.KXmlParser;

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
 * $Id: WFSProtocolHandler.java 27881 2009-04-07 14:47:27Z jpiera $
 * $Log$
 * Revision 1.11  2007-09-20 09:30:12  jaume
 * removed unnecessary imports
 *
 * Revision 1.10  2007/02/09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 * Revision 1.9  2007/01/12 13:09:42  jorpiell
 * added searches by area
 *
 * Revision 1.8  2006/10/10 12:52:28  jorpiell
 * Soporte para features complejas.
 *
 * Revision 1.7  2006/06/21 12:53:03  jorpiell
 * Se tienen en cuanta el número de features
 *
 * Revision 1.6  2006/06/14 08:46:07  jorpiell
 * Se tiene en cuanta la opcion para refrescar las capabilities
 *
 * Revision 1.5  2006/06/14 07:54:18  jorpiell
 * Se parsea el online resource que antes se ignoraba
 *
 * Revision 1.4  2006/05/30 13:58:03  jaume
 * cancelable downloads
 *
 * Revision 1.3  2006/05/23 13:23:13  jorpiell
 * Se ha cambiado el final del bucle de parseado y se tiene en cuenta el online resource
 *
 * Revision 1.2  2006/04/20 16:39:16  jorpiell
 * Añadida la operacion de describeFeatureType y el parser correspondiente.
 *
 * Revision 1.1  2006/04/19 12:51:35  jorpiell
 * Añadidas algunas de las clases del servicio WFS
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public abstract class WFSProtocolHandler extends OGCProtocolHandler {
	/**
	 * WFS metadata
	 */
	protected WFSServiceInformation serviceInfo = new WFSServiceInformation();
	protected Hashtable features = new Hashtable();
	protected String currentFeature = null;
	private int numberOfErrors = 0;

	/**
	 * Clear the connection
	 */
	private void clear() {
		features.clear();
		serviceInfo.clear();
	}

	/**
	 * <p>Builds a GetCapabilities request that is sent to the WFS
	 * the response will be parse to extract the data needed by the
	 * WFS client</p>
	 */
	public void getCapabilities(WFSStatus status,boolean override, ICancellable cancel) throws WFSException {
		URL request = null;
		try {
			request = new URL(buildCapabilitiesRequest(status));
			if (override){
				Utilities.removeURL(request);
			}
			File f = Utilities.downloadFile(request,"wfs_capabilities.xml", cancel);
			if (f == null)
				return;
			clear();
			parseCapabilities(f);
		} catch (Exception e){
			throw new WFSException(e);
		}
	}    

	/**
	 * @return
	 */
	private String buildCapabilitiesRequest(WFSStatus status) {
		StringBuffer req = new StringBuffer();
		String symbol = null;

		String onlineResource;
		if (status == null || status.getOnlineResource() == null)
			onlineResource = getHost();
		else 
			onlineResource = status.getOnlineResource();
		symbol = getSymbol(onlineResource);

		req.append(onlineResource).append(symbol).append("REQUEST=GetCapabilities&SERVICE=WFS&");
		req.append("VERSION=").append(getVersion()).append("&EXCEPTIONS=XML");
		return req.toString();
	}   

	/**
	 * Builds the GetCapabilitiesRequest according to the OGC WFS Specifications
	 * without a VERSION, to get the highest version than a WFS supports.
	 */
	public static String buildCapabilitiesSuitableVersionRequest(String _host, String _version)
	{
int index = _host.indexOf('?');
		
		if (index > -1) {
			String host = _host.substring(0, index + 1);
			String query = _host.substring(index + 1, _host.length());
			
			StringTokenizer tokens = new StringTokenizer(query, "&");
			String newQuery = "", token;

			// If there is a field or a value with spaces, (and then it's on different tokens) -> unify them
			while (tokens.hasMoreTokens()) {
				token = tokens.nextToken().trim();

				if (token.toUpperCase().compareTo("REQUEST=GETCAPABILITIES") == 0)
					continue;

				if (token.toUpperCase().compareTo("SERVICE=WFS") == 0)
					continue;

				if ((_version != null) && (_version.length() > 0)) {
    				if (token.toUpperCase().compareTo("VERSION=" + _version) == 0)
    					continue;
				}

				if (token.toUpperCase().compareTo("EXCEPTIONS=XML") == 0)
					continue;

				newQuery += token + "&";
			}

        	_host = host + newQuery;
		}
		else {
			_host += "?";
		}

    	if ((_version != null) && (_version.compareTo("") != 0))
    		_host += "REQUEST=GetCapabilities&SERVICE=WFS&VERSION=" + _version + "&EXCEPTIONS=XML";
    	else
    		_host += "REQUEST=GetCapabilities&SERVICE=WFS&EXCEPTIONS=XML";

    	return _host;    
	}

	/**
	 * <p>Builds a describeFeatureType request that is sent to the WFS
	 * the response will be parse to extract the data needed by the
	 * WFS client</p>
	 * @param status
	 * WFS client status. Contains all the information to create
	 * the query. In this case, the only the feature name is needed.
	 */
	public void describeFeatureType(WFSStatus status,boolean override, ICancellable cancel)throws WFSException {
		this.currentFeature = status.getFeatureName();
		//sets the namespace URI and the namespace prefix
		int index = currentFeature.indexOf(":");
		if (index > 0){
			String namespacePrefix = currentFeature.substring(0, index);
			String namespaceURI = serviceInfo.getNamespace(namespacePrefix);
			if (namespaceURI != null){
				status.setNamespace(namespaceURI);
				status.setNamespacePrefix(namespacePrefix);
			}
		}
		try {
			WFSDescribeFeatureTypeRequest request = createDescribeFeatureTypeRequest(status);
			File f = request.sendRequest();
			parseDescribeFeatureType(f,status.getNamespacePrefix());
		} catch (Exception e){
			throw new WFSException(e);
		}
	}     	

	/**
	 * parses the data retrieved by the DescribeCoverage XML document
	 */
	protected abstract boolean parseDescribeFeatureType(File f,String nameSpace);

	/**
	 * <p>Builds a getFeature request that is sent to the WFS
	 * the response will be parse to extract the data needed by the
	 * WFS client</p>
	 * @param status
	 * WFS client status. Contains all the information to create
	 * the query. 
	 * @return File
	 * GML file
	 */    
	public File getFeature(WFSStatus status,boolean override, ICancellable cancel) throws WFSException{
		try{		
			WFSGetFeatureRequest request = createGetFeatureRequest(status);
			File f = request.sendRequest();
			parseGetFeature(f,status.getNamespacePrefix());
			return f;
		} catch (Exception e){
			throw new WFSException(e);
		}		
	}

	/**
	 * parses the data retrieved by the GetFeature XML document. It is
	 * used just to find errors
	 */
	protected abstract boolean parseGetFeature(File f,String nameSpace) throws WFSException;

	
	/**
	 * <p>Builds a transaction request that is sent to the WFS
	 * the response will be parse to extract the data needed by the
	 * WFS client</p>
	 * @param status
	 * WFS client status. Contains all the information to create
	 * the query. 
	 */   
	public void transaction(WFSStatus status,boolean override, ICancellable cancel) throws WFSException {
		URL request = null;
		try {
			request = new URL(buildTransactionRequest(status));
			for (int i=0 ; i<status.getTransactionsSize() ; i++){
				WFSTTransaction transaction = status.getTransactionAt(i);
				if (transaction.getStatus() == WFSTTransaction.STATUS_NO_EXECUTED){
					System.out.println(transaction.getWFSTRequest());
					File f = Utilities.downloadFile(request, 
							transaction.getWFSTRequest(),
							"wfs_transaction.xml", null);
					parseTransaction(f,status.getNamespacePrefix(),transaction);	
					//If the transaction has been executed with success,
					//it is necessary to remove the getFeature file from the
					//downloader manager. 
					if (transaction.getStatus() == WFSTTransaction.STATUS_SUCCESS){
						WFSGetFeatureRequest requestAux = createGetFeatureRequest(status);
						requestAux.setDeleted(true);
						requestAux.sendRequest();
					}
				}
			}			
		} catch (Exception e){
			throw new WFSException(e);
		}
	}

	/**
	 * <p>Builds a transaction request that is sent to the WFS
	 * the response will be parse to extract the data needed by the
	 * WFS client</p>
	 * @param status
	 * WFS client status. Contains all the information to create
	 * the query. 
	 */
	private String buildTransactionRequest(WFSStatus status){
		StringBuffer req = new StringBuffer();
		String symbol = null;

		String onlineResource;
		if(serviceInfo.getOnlineResource(CapabilitiesTags.WFS_TRANSACTION) != null){
			onlineResource = serviceInfo.getOnlineResource(CapabilitiesTags.WFS_TRANSACTION);
		}else {
			onlineResource = getHost();
		}
		symbol = getSymbol(onlineResource);

		req.append(onlineResource);
		return req.toString();
	}

	/**
	 * parses the data retrieved by the transaction operation
	 * @param f
	 * The retrieved file
	 * @param nameSpace
	 * The namespace
	 * @param transaction
	 * The current WFSTransaction
	 */
	protected abstract boolean parseTransaction(File f,String nameSpace, WFSTTransaction transaction) throws WFSException;

	/**
	 * <p>Builds a lockFeature request that is sent to the WFS
	 * the response will be parse to extract the data needed by the
	 * WFS client</p>
	 * @param status
	 * WFS client status. Contains all the information to create
	 * the query. 
	 */  
	public void lockFeature(WFSStatus status,boolean override, ICancellable cancel) throws WFSException {
		try {
			WFSTLockFeatureRequest request = createLockFeatureRequest(status);
			File f = request.sendRequest();
			parseLockFeature(f,status.getNamespacePrefix(),status);
		} catch(WFSException e) {
			throw e;
		} catch (Exception e){
			throw new WFSException(e);
		}		
	}	

	/**
	 * parses the data retrieved by the LockFeature operation
	 */
	protected abstract boolean parseLockFeature(File f,String nameSpace, WFSStatus status) throws WFSException;

	/**
	 * @return Returns the features.
	 */
	public Hashtable getFeatures() {
		return features;
	}

	/**
	 * @return Returns the currentFeature.
	 */
	public String getCurrentFeature() {
		return currentFeature;
	}

	/**
	 * Sets the fields of the current feature
	 * @param fields
	 */
	public void setFields(Vector fields){
		WFSFeature feature = (WFSFeature) features.get(currentFeature);
		feature.setFields(fields);
		features.put(feature.getName(),feature);		
	}

	/**
	 * Sets the fields of the current feature
	 * @param fields
	 */
	public void setFields(Hashtable fields){
		WFSFeature feature = (WFSFeature) features.get(currentFeature);
		Vector vFields = new Vector();
		Set keys = fields.keySet();
		for (int i=0 ; i<keys.size() ; i++){
			vFields.add(fields.get(keys.toArray()[i]));
		}
		feature.setFields(vFields);
		features.put(feature.getName(),feature);		
	}


	/**
	 * @param currentFeature The currentFeature to set.
	 */
	public void setCurrentFeature(String currentFeature) {
		this.currentFeature = currentFeature;
	}    
	
	/**
	 * It parses the attributes of the current KXMLParser 
	 * and add the new namespaces to the service information
	 * @param parser
	 * The KXML parser
	 */
	protected void parseNamespaces(KXmlParser parser){
		for (int i=0 ; i<parser.getAttributeCount() ; i++){
			String attName = parser.getAttributeName(i);
			if (attName.startsWith(GMLTags.XML_NAMESPACE)){
				int index = attName.indexOf(":");
				if (index > 0){
					serviceInfo.addNamespace(attName.substring(index+1, attName.length()), 
							parser.getAttributeValue(i));
				}
			}
		}			
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.remoteClient.ogc.OGCProtocolHandler#getServiceInformation()
	 */
	public OGCServiceInformation getServiceInformation() {
		return serviceInfo;
	}	

	/**
	 * @param status
	 * The WFS status
	 * @param protocolHandler
	 * The handler to parse the requests
	 * @return an object to send the DescribeFeatureType requests
	 */
	protected abstract WFSDescribeFeatureTypeRequest createDescribeFeatureTypeRequest(WFSStatus status);

	/**
	 * @param status
	 * The WFS status
	 * @param protocolHandler
	 * The handler to parse the requests
	 * @return an object to send the GetFeature requests
	 */
	protected abstract WFSGetFeatureRequest createGetFeatureRequest(WFSStatus status);

	/**
	 * @param status
	 * The WFS status
	 * @param protocolHandler
	 * The handler to parse the requests
	 * @return an object to send the LockFeature requests
	 */
	protected abstract WFSTLockFeatureRequest createLockFeatureRequest(WFSStatus status);
}

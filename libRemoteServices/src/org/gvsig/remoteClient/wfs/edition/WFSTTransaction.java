package org.gvsig.remoteClient.wfs.edition;

import java.util.ArrayList;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.wfs.WFSOperation;
import org.gvsig.remoteClient.wfs.filters.FilterEncoding;

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
public abstract class WFSTTransaction {
	private ArrayList operations = null;
	private String typename = null;
	private String namespace = null;
	private String namespaceprefix = null;
	
	//Features Locked
	private ArrayList featuresLocked = null;	
	
	//Transaction message
	public String message = null;
	
	//Status
	public static final int STATUS_NO_EXECUTED = 0;
	public static final int STATUS_FAILED = 1;
	public static final int STATUS_SUCCESS = 2;
	private int status = 0;
		
	WFSTTransaction(){
		status = STATUS_NO_EXECUTED;	
		operations = new ArrayList();	
		featuresLocked = new ArrayList();
	}
	
	WFSTTransaction(String typename, String namespaceprefix, String namespace, ArrayList featuresLocked){
		status = STATUS_NO_EXECUTED;	
		operations = new ArrayList();
		this.typename = typename;
		this.namespaceprefix = namespaceprefix;
		this.namespace = namespace;
		this.featuresLocked = featuresLocked;
	}
	
	/**
	 * Adds a delete operation
	 * @param ids
	 * The identifiers of the features to delete
	 */
	public void addDeleteOperation(ArrayList ids){
		FilterEncoding fe = new FilterEncoding();
		fe.setQualified(true);
		for (int i=0 ; i<ids.size() ; i++){
			fe.addFeatureById(ids.get(i));
		}
		operations.add(new WFSTDeleteOperation(typename, fe.toString()));		
	}
	
	/**
	 * Adds a delete operation
	 * @param id
	 * The identifies of the feature to delete
	 */
	public void addDeleteOperation(String id){
		ArrayList ids = new ArrayList();
		ids.add(id);
		addDeleteOperation(ids);		
	}
	
	/**
	 * Adds a delete operation
	  * @param gml
	 * The new Feature
	 */
	public void addInsertOperation(String gml){
		operations.add(new WFSTInsertOperation(typename, gml));		
	}
	
	/**
	 * Adds a update operation
	 * @param ids
	 * The identifiers of the features to update
	 * @param gml
	 * The update operation
	 */
	public void addUpdateOperation(ArrayList ids, String gml){
		FilterEncoding fe = new FilterEncoding();
		fe.setQualified(true);
		for (int i=0 ; i<ids.size() ; i++){
			fe.addFeatureById(ids.get(i));
		}
		operations.add(new WFSTUpdateOperation(typename, fe.toString(), gml));		
	}
	
	/**
	 * Adds a update operation
	 * @param ids
	 * The identifier of the feature to update
	 * @param gml
	 * The update operation
	 */
	public void addUpdateOperation(String id, String gml){
		ArrayList ids = new ArrayList();
		ids.add(id);
		addUpdateOperation(ids,gml);			
	}

	/**
	 * @return the WFS version
	 */
	protected abstract String getVersion();
	
	/**
	 * @return the transaction schema location
	 */
	protected abstract String getSchemaLocation();
	
	/**
	 * @return the WFS-T request to execute
	 * the transaction
	 */
	public String getWFSTRequest(){
		StringBuffer request = new StringBuffer();
		request.append(getWFSTRequestStartHeader());
		request.append(getWFSTRequestLockID());
		for (int i=0 ; i<getOperationSize() ; i++){
			WFSTOperation operation = getOperationAt(i);
			request.append(operation.getRequest());
		}
		request.append(getWFSTRequestEndHeader());
		return request.toString();
	}	
	
	/**
	 * Create the lockID request
	 * @return
	 */
	private Object getWFSTRequestLockID() {
		StringBuffer request = new StringBuffer();
//		for (int i=0 ; i<featuresLocked.size() ; i++){
//			request.append("<" + WFSTTags.WFS_NAMESPACE_PREFIX + ":" + WFSTTags.WFST_LOCKID + ">" );
//			request.append(featuresLocked.get(i));
//			request.append("</" + WFSTTags.WFS_NAMESPACE_PREFIX + ":" + WFSTTags.WFST_LOCKID + ">" );
//		}
		return request.toString();
	}

	/**
	 * @return the XML header of the WFS Transaction 
	 * request
	 */
	private String getWFSTRequestStartHeader(){
		StringBuffer request = new StringBuffer();
		request.append(WFSTTags.XML_ROOT);
		request.append("<" + WFSTTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(CapabilitiesTags.WFS_TRANSACTION + " ");
		request.append(CapabilitiesTags.VERSION + "=\"" + getVersion() + "\" ");
		request.append(WFSTTags.WFST_RELEASEACTION + "=\"ALL\" ");
		request.append(WFSTTags.WFST_SERVICE + "=\"WFS\" ");
		request.append(WFSTTags.XMLNS + ":" + WFSTTags.OGC_NAMESPACE_PREFIX);
		request.append("=\"" + WFSTTags.OGC_NAMESPACE + "\" ");
		request.append(WFSTTags.XMLNS + ":" + WFSTTags.WFS_NAMESPACE_PREFIX);
		request.append("=\"" + WFSTTags.WFS_NAMESPACE + "\" ");
		request.append(WFSTTags.XMLNS + ":" + WFSTTags.XML_NAMESPACE_PREFIX);
		request.append("=\"" + WFSTTags.XML_NAMESPACE + "\" ");
		request.append(WFSTTags.XMLNS + ":" + WFSTTags.GML_NAMESPACE_PREFIX);
		request.append("=\"" + WFSTTags.GML_NAMESPACE + "\" ");
		request.append(WFSTTags.XMLNS + ":" + namespaceprefix);
		request.append("=\"" + namespace + "\" ");
		request.append(WFSTTags.XML_NAMESPACE_PREFIX + ":" + WFSTTags.XML_SCHEMALOCATION);
		request.append("=\"" + WFSTTags.WFS_NAMESPACE + " ");
		request.append(getSchemaLocation());
		request.append("\">");
		return request.toString();
	}	
	
	/**
	 * @return the end of the WFS Transaction request header
	 */
	private String getWFSTRequestEndHeader(){
		StringBuffer request = new StringBuffer();
		request.append("</" + WFSTTags.WFS_NAMESPACE_PREFIX + ":");
		request.append(CapabilitiesTags.WFS_TRANSACTION + ">");
		return request.toString();
	}	
	
	/**
	 * @return the operation size
	 */
	public int getOperationSize() {
		return operations.size();
	}

	/**
	 * Gets an operation
	 * @param i
	 * Operation position
	 * @return
	 * A operation
	 */
	public WFSTOperation getOperationAt(int i){
		if (i>getOperationSize()){
			return null;
		}
		return (WFSTOperation)operations.get(i);
	}
	
	/**
	 * Adds a new operation
	 * @param operation the operation to add
	 */
	public void addOperation(WFSOperation operation) {
		operations.add(operation);
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}	
}

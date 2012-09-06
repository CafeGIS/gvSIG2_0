package org.gvsig.remoteClient.wfs;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.remoteClient.RemoteClientStatus;
import org.gvsig.remoteClient.wfs.edition.WFSTTransaction;
import org.gvsig.remoteClient.wfs.edition.WFSTransactionFactory;
import org.gvsig.remoteClient.wfs.filters.AFilter;
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
 * $Id: WFSStatus.java 29247 2009-06-08 13:21:33Z jpiera $
 * $Log$
 * Revision 1.7  2007-09-20 09:30:12  jaume
 * removed unnecessary imports
 *
 * Revision 1.6  2007/02/09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 * Revision 1.5  2006/12/11 11:02:24  ppiqueras
 * Corregido bug -> que se mantenga la frase de filtrado
 *
 * Revision 1.4  2006/10/10 12:52:28  jorpiell
 * Soporte para features complejas.
 *
 * Revision 1.3  2006/06/14 07:54:18  jorpiell
 * Se parsea el online resource que antes se ignoraba
 *
 * Revision 1.2  2006/05/23 13:23:13  jorpiell
 * Se ha cambiado el final del bucle de parseado y se tiene en cuenta el online resource
 *
 * Revision 1.1  2006/04/19 12:51:35  jorpiell
 * Añadidas algunas de las clases del servicio WFS
 *
 *
 */
/**
 * The status of the current WFS connection
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSStatus extends RemoteClientStatus{
	static final String LOCKACTION_ALL = "ALL"; 
	static final String LOCKACTION_SOME = "SOME"; 
	//WFS attributes
	private String featureName = null;
	private String namespacePrefix = null;
	private String namespace = null;
	private String[] fields = null;
	private String onlineResource = null;
	private Integer timeout = 10000;
	private Integer buffer = 100;	
	private String filterByAttribute = null;
	private WFSGeometryOperation filterByArea = null;

	//Geometry manager
	private static GeometryManager geometryManager = GeometryLocator.getGeometryManager();
	
	//WFS-T LockFeature attributes
	private String userName = null;
	private String password = null;
	private ArrayList featuresToLock = new ArrayList();
	private ArrayList featuresToLockPropertieName = new ArrayList();
	private ArrayList featuresToLockPropertieValue = new ArrayList();
	private ArrayList featuresLocked = new ArrayList();
	private int expiry = -1;
	private String lockAction = null;
	private Rectangle2D lockedArea = null;
	private String lockedAreaProperty = null;
	//WFS-T Transaction
	private ArrayList transactions = null;

	public WFSStatus(String featureName){
		this(featureName, null);
	}

	public WFSStatus(String featureName, String namespace){
		this.featureName = featureName;
		this.namespace = namespace;
		if (featureName != null){
			int index = featureName.indexOf(":");
			if (index>0){
				namespacePrefix = featureName.substring(0, index);
			}
		}
		lockAction = LOCKACTION_ALL;
		transactions = new ArrayList();
		featuresLocked = new ArrayList();
		featuresLocked = new ArrayList();
		featuresToLockPropertieName = new ArrayList();
		featuresToLockPropertieValue = new ArrayList();
	}

	/**
	 * @return Returns the buffer.
	 */
	public Integer getBuffer() {
		return buffer;
	}


	/**
	 * @param buffer The buffer to set.
	 */
	public void setBuffer(Integer buffer) {
		if (buffer != null){
			this.buffer = buffer;
		}
	}


	/**
	 * @return Returns the featureName.
	 */
	public String getFeatureName() {
		return featureName;
	}


	/**
	 * @param featureName The featureName to set.
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	/**
	 * @return Returns the fields.
	 */
	public String[] getFields() {
		if (fields == null){
			fields = new String[0];
		}
		return fields;
	}


	/**
	 * @param fields The fields to set.
	 */
	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public void setFields(String fields) {
		if (fields == null){
			return;
		}
		this.fields = fields.split(",");		
	}


	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * @return Returns the timeout.
	 */
	public Integer getTimeout() {
		return timeout;
	}


	/**
	 * @param timeout The timeout to set.
	 */
	public void setTimeout(Integer timeout) {
		if (timeout != null){
			this.timeout = timeout;
		}
	}


	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}


	/**
	 * @param userName The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOnlineResource() {
		return onlineResource;
	}


	public void setOnlineResource(String url) {
		onlineResource = url;
	}

	/**
	 * @return Returns the filterQuery.
	 */
	public String getFilterByAttribute() {
		return filterByAttribute;
	}

	/**
	 * @param filterQuery The filterQuery to set.
	 */
	public void setFilterByAttribute(String filterQuery) {
		this.filterByAttribute = filterQuery;
	}	

	/**
	 * @param filterByArea 
	 * the filterByArea to set
	 * @param operation
	 * The geometric operation to apply. It has to be a value from 
	 * {@link AFilter}
	 */
	public void setFilterByArea(Geometry filterByArea, String attributeName, String srs, int operation) {
		this.filterByArea = new WFSGeometryOperation(filterByArea,
				operation,
				attributeName, 
				srs);
				
	}	
	
	/**
	 * @return the filterByArea
	 */
	public WFSGeometryOperation getFilterByArea() {
		return filterByArea;
	}

	/**
	 * @return the filterQueryLocked
	 */
	public String getFilterQueryLocked() {
		FilterEncoding filter = new FilterEncoding();
		filter.setQualified(true);
		filter.setNamepacePrefix(null);
		filter.setHasBlankSpaces(false);
		return getFilterQueryLocked(filter);	
	}

	/**
	 * @return the filterQueryLocked
	 */
	public String getFilterQueryLockedPost() {
		FilterEncoding filter = new FilterEncoding();
		filter.setQualified(true);
		return getFilterQueryLocked(filter);
	}

	/**
	 * Create a filter encoding request
	 * @param filter
	 * @return
	 */
	private String getFilterQueryLocked(FilterEncoding filter){
		if ((featuresToLock.size() == 0) && 
				(getLockedArea() == null) &&
				(featuresToLockPropertieName.size() == 0)){
			return null;
		}
		for (int i=0 ; i<featuresToLock.size() ; i++){
			filter.addFeatureById(featuresToLock.get(i));
		}
		if (featuresToLockPropertieName.size() > 0){
			for (int i=0 ; i<featuresToLockPropertieName.size() ; i++){
				filter.addAndClause((String)featuresToLockPropertieName.get(i),
						(String)featuresToLockPropertieValue.get(i));						
			}				
		}
		if (lockedArea != null){
			GeneralPathX generalPath = new GeneralPathX();
			generalPath.moveTo(lockedArea.getMinX(), lockedArea.getMinY());
			generalPath.lineTo(lockedArea.getMaxX(), lockedArea.getMinY());
			generalPath.lineTo(lockedArea.getMaxX(), lockedArea.getMaxY());
			generalPath.lineTo(lockedArea.getMinX(), lockedArea.getMaxY());
			generalPath.lineTo(lockedArea.getMinX(), lockedArea.getMinY());
			try {
				filter.setArea(geometryManager.createSurface(generalPath, SUBTYPES.GEOM2D),
						lockedAreaProperty, 
						getSrs(), 
						AFilter.BBOX_ENCLOSES);
			} catch (CreateGeometryException e) {
				return null;
			}
			
		}
		return filter.toString();	
	}

	/**
	 * @return the expiry
	 */
	public int getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry the expiry to set
	 */
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

	/**
	 * @return the lockAction
	 */
	public String getLockAction() {
		return lockAction;
	}

	/**
	 * Set the lock action to all
	 */
	public void setLockActionToAll() {
		lockAction = LOCKACTION_ALL;
	}

	/**
	 * Set teh lock action to some
	 */
	public void setLockActionToSome() {
		lockAction = LOCKACTION_SOME;
	}

	/**
	 * @return the transaction size
	 */
	public int getTransactionsSize() {
		return transactions.size();
	}

	/**
	 * Gets an transaction
	 * @param i
	 * Transaction position
	 * @return
	 * A transaction
	 */
	public WFSTTransaction getTransactionAt(int i){
		if (i>getTransactionsSize()){
			return null;
		}
		return (WFSTTransaction)transactions.get(i);
	}

	/**
	 * Adds a new transaction
	 * @param transactions the transactions to add
	 */
	public WFSTTransaction createTransaction(String version) {
		WFSTTransaction transaction = WFSTransactionFactory.createTransaction(version,
				getFeatureName(),
				getNamespacePrefix(),
				getNamespace(),
				featuresLocked);
		transactions.add(transaction);
		return transaction;
	}

	/**
	 * @return the neumber of features blocked
	 */
	public int getFeaturesToLockSize() {
		return featuresToLock.size();
	}

	/**
	 * Gets an identifier of a feature that is blocked
	 * @param i
	 * The id position
	 * @return
	 * The Id
	 */
	public String getFeatureToLockAt(int i){
		if (i>featuresToLock.size()){
			return null;
		}
		return (String)featuresToLock.get(i);
	}

	/**
	 * Remove all the features to lock
	 */
	public void removeFeaturesToLock() {
		featuresToLock.clear();
		featuresLocked.clear();
		featuresToLockPropertieName.clear();
		featuresToLockPropertieValue.clear();
	}

	/**
	 * Adds a new feature locked
	 * @param idFeature
	 * the feature id
	 */
	public void addFeatureToLock(String idFeature) {
		featuresToLock.add(idFeature);
	}

	/**
	 * Adds a new feature locked
	 * @param idFeature
	 * the feature id
	 */
	public void addFeatureToLock(String propertyName, String propertyValue){
		featuresToLockPropertieName.add(propertyName);
		featuresToLockPropertieValue.add(propertyValue);
	}

	/**
	 * @return the idsLocked size
	 */
	public int getFeaturesLocked() {
		return featuresLocked.size();
	}

	/**
	 * Gets an identifier tha is blocked
	 * @param i
	 * The id position
	 * @return
	 * The Id
	 */
	public String getFeatureLockedAt(int i){
		if (i>featuresLocked.size()){
			return null;
		}
		return (String)featuresLocked.get(i);
	}

	/**
	 * Adds a new Id
	 * @param idLocked
	 * the idLocked to add
	 */
	public void addFeatureLocked(String lockId) {
		featuresLocked.add(lockId);
	}

	/**
	 * @return the lockedArea
	 */
	public Rectangle2D getLockedArea() {
		return lockedArea;
	}

	/**
	 * @param lockedArea the lockedArea to set
	 */
	public void setLockedArea(Rectangle2D lockedArea, String lockedAreaProperty) {
		this.lockedArea = lockedArea;
		this.lockedAreaProperty = lockedAreaProperty;
	}

	/**
	 * @return the namespace prefix
	 */
	public String getNamespacePrefix(){
		return namespacePrefix;
	}

	/**
	 * @return the namespace URL
	 */
	public String getNamespace(){
		return namespace;
	}

	/**
	 * @param namespaceprefix the namespaceprefix to set
	 */
	public void setNamespacePrefix(String namespacePrefix) {
		this.namespacePrefix = namespacePrefix;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;		
	}
	
	private class AreaStatus{
		
	}
}
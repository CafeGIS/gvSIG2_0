/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.fmap.dal.store.wfs;

import org.gvsig.fmap.dal.DataParameters;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WFSStoreParameters extends AbstractDataParameters implements
 DataStoreParameters{
	protected static DynClass DYNCLASS = null;
	public static final String DYNCLASS_NAME = "WFSStoreParameters";
	
	public static final String DYNFIELDNAME_URL = "url";
	public static final String DYNFIELDNAME_VERSION = "version";
	public static final String DYNFIELDNAME_TYPENAME = "typeName";
	public static final String DYNFIELDNAME_NAMESPACE = "namespace";
	public static final String DYNFIELDNAME_NAMESPACEPREFIX = "namespacePrefix";
	public static final String DYNFIELDNAME_FIELDS = "fields";
	public static final String DYNFIELDNAME_FILTERENCODING = "filterEncoding";
	public static final String DYNFIELDNAME_MAXFEATURES = "maxFeatures";
	public static final String DYNFIELDNAME_TIMEOUT = "timeOut";
	public static final String DYNFIELDNAME_USER = "user";
	public static final String DYNFIELDNAME_PASSWORD = "password";
		
	public WFSStoreParameters() {
		super();
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
		.getDynObjectManager().createDynObject(
			WFSStoreParameters.DYNCLASS);
	}		
	
	public WFSStoreParameters(DataParameters dataParameters) throws InitializeException, ProviderNotRegisteredException{
		this();			
		setUrl((String)dataParameters.getDynValue(WFSStoreParameters.DYNFIELDNAME_URL));
		String namespace = null;
		String namespacePrefix = null;
		if (dataParameters.hasDynValue(WFSStoreParameters.DYNFIELDNAME_NAMESPACE)){
			namespace = (String)dataParameters.getDynValue(WFSStoreParameters.DYNFIELDNAME_NAMESPACE);
		}
		if (dataParameters.hasDynValue(WFSStoreParameters.DYNFIELDNAME_NAMESPACEPREFIX)){
			namespacePrefix = (String)dataParameters.getDynValue(WFSStoreParameters.DYNFIELDNAME_NAMESPACEPREFIX);
		}
		setFeatureType(namespacePrefix, namespace,
				(String)dataParameters.getDynValue(WFSStoreParameters.DYNFIELDNAME_TYPENAME));
//		setVersion((String)dataParameters.getDynValue(WFSStoreParameters.DYNFIELDNAME_VERSION));
//		setUser((String)dataParameters.getDynValue(WFSStoreParameters.DYNFIELDNAME_USER));
//		setPassword((String)dataParameters.getDynValue(WFSStoreParameters.DYNFIELDNAME_PASSWORD));
	}
	
	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME);
			field = dynClass.addDynField(DYNFIELDNAME_URL);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("URL of the WFS server");
			field.setType(DataTypes.STRING);
			field.setMandatory(true);
			
			field = dynClass.addDynField(DYNFIELDNAME_VERSION);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Version of the WFS server");
			field.setType(DataTypes.STRING);
			field.setMandatory(false);
			
			field = dynClass.addDynField(DYNFIELDNAME_TYPENAME);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Feature type to retrieve");
			field.setType(DataTypes.STRING);
			field.setMandatory(true);
			
			field = dynClass.addDynField(DYNFIELDNAME_NAMESPACE);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Namespace of the feature type to retrieve");
			field.setType(DataTypes.STRING);
			field.setMandatory(false);
			
			field = dynClass.addDynField(DYNFIELDNAME_NAMESPACEPREFIX);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Prefix of the namespace of the feature type to retrieve");
			field.setType(DataTypes.STRING);
			field.setMandatory(false);
			
			field = dynClass.addDynField(DYNFIELDNAME_FIELDS);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Fields to retrieve separated by ','");
			field.setType(DataTypes.STRING);
			field.setMandatory(false);
			
			field = dynClass.addDynField(DYNFIELDNAME_FILTERENCODING);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Filter encoding request");
			field.setType(DataTypes.STRING);
			field.setMandatory(false);
			
			field = dynClass.addDynField(DYNFIELDNAME_MAXFEATURES);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Number of features to retrieve");
			field.setType(DataTypes.INT);
			field.setMandatory(false);
			
			field = dynClass.addDynField(DYNFIELDNAME_TIMEOUT);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Timeout");
			field.setType(DataTypes.INT);
			field.setMandatory(false);
			
			field = dynClass.addDynField(DYNFIELDNAME_USER);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("User name (not used at this moment)");
			field.setType(DataTypes.STRING);
			field.setMandatory(false);
			
			field = dynClass.addDynField(DYNFIELDNAME_PASSWORD);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("Passwrod (not used at this moment");
			field.setType(DataTypes.STRING);
			field.setMandatory(false);
			
			DYNCLASS = dynClass;
		}
	}	

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStoreParameters#getDataStoreName()
	 */
	public String getDataStoreName() {
		return WFSStoreProvider.NAME;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStoreParameters#getDescription()
	 */
	public String getDescription() {
		return WFSStoreProvider.DESCRIPTION;
	}	

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStoreParameters#isValid()
	 */
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getUrl(){
		return (String) this.getDynValue(DYNFIELDNAME_URL);
	}
	
	public void setUrl(String url){
		this.setDynValue(DYNFIELDNAME_URL, url);
	}
	
	public String getVersion(){
		return (String) this.getDynValue(DYNFIELDNAME_VERSION);
	}
	
	public void setVersion(String version){
		this.setDynValue(DYNFIELDNAME_VERSION, version);
	}
	
	public String getFeatureType(){
		return (String) this.getDynValue(DYNFIELDNAME_TYPENAME);
	}
	
	public void setFeatureType(String featureType){
		this.setDynValue(DYNFIELDNAME_TYPENAME, featureType);
	}
	
	public void setFeatureType(String namespace, String featureType){
		this.setDynValue(DYNFIELDNAME_NAMESPACE, namespace);
		this.setDynValue(DYNFIELDNAME_TYPENAME, featureType);
	}
	
	public void setFeatureType(String prefix, String namespace, String featureType){
		this.setDynValue(DYNFIELDNAME_NAMESPACEPREFIX, prefix);
		this.setDynValue(DYNFIELDNAME_NAMESPACE, namespace);
		this.setDynValue(DYNFIELDNAME_TYPENAME, featureType);
	}
	
	public String getFeatureNamespace(){
		return (String) this.getDynValue(DYNFIELDNAME_NAMESPACE);
	}
	
	public String getFeaturePrefix(){
		return (String) this.getDynValue(DYNFIELDNAME_NAMESPACEPREFIX);
	}
	
	public String getFields(){
		return (String) this.getDynValue(DYNFIELDNAME_FIELDS);
	}
	
	public void setFields(String fields){
		this.setDynValue(DYNFIELDNAME_FIELDS, fields);
	}
	
	public String getFilterEncoding(){
		return (String) this.getDynValue(DYNFIELDNAME_FILTERENCODING);
	}
	
	public void setFilterEncoding(String filter){
		this.setDynValue(DYNFIELDNAME_FILTERENCODING, filter);
	}
	
	public Integer getMaxFeatures(){
		return (Integer) this.getDynValue(DYNFIELDNAME_MAXFEATURES);
	}
	
	public void setMaxFeatures(Integer maxFeatures){
		this.setDynValue(DYNFIELDNAME_MAXFEATURES, maxFeatures);
	}
	
	public Integer getTimeOut(){
		return (Integer) this.getDynValue(DYNFIELDNAME_TIMEOUT);
	}
	
	public void setTimeOut(Integer timeOut){
		this.setDynValue(DYNFIELDNAME_TIMEOUT, timeOut);
	}	
	
	public String getUser(){
		return (String) this.getDynValue(DYNFIELDNAME_USER);
	}
	
	public void setUser(String user){
		this.setDynValue(DYNFIELDNAME_USER, user);
	}
	
	public String getPassword(){
		return (String) this.getDynValue(DYNFIELDNAME_PASSWORD);
	}
	
	public void setPassword(String password){
		this.setDynValue(DYNFIELDNAME_PASSWORD, password);
	}
}


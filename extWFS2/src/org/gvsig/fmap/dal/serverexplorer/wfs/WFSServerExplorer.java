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

package org.gvsig.fmap.dal.serverexplorer.wfs;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.spi.DataServerExplorerProvider;
import org.gvsig.fmap.dal.spi.DataServerExplorerProviderServices;
import org.gvsig.fmap.dal.store.wfs.WFSStoreParameters;
import org.gvsig.fmap.dal.store.wfs.WFSStoreProvider;
import org.gvsig.remoteClient.wfs.WFSClient;
import org.gvsig.remoteClient.wfs.WFSFeature;
import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.exceptions.WFSException;
import org.gvsig.remoteClient.wms.ICancellable;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class WFSServerExplorer implements DataServerExplorerProvider {
	public static final String NAME = "WFSServerExplorer";
	private WFSServerExplorerParameters parameters = null;
	private DataManager dataManager = DALLocator.getDataManager();
	private String url = null;
	
	//WFS Parameters
	private WFSStatus status = null;
	private WFSClient wfsClient = null;

	/**
	 * @param parameters
	 */
	public WFSServerExplorer(WFSServerExplorerParameters parameters) throws InitializeException{
		super();
		this.parameters = parameters;
		this.url = parameters.getUrl();
		if (wfsClient == null){
			try {
				wfsClient = new WFSClient(url);
				if (status == null){
					status = new WFSStatus(null);					
				}
				wfsClient.getCapabilities(status, false, null);
			} catch (ConnectException e) {
				throw new InitializeException("Not possible to connect with " + url, e);
			} catch (IOException e) {
				throw new InitializeException("Not possible to connect with " + url, e);
			} catch (WFSException e) {
				throw new InitializeException("Not possible to connect with " + url, e);
			}
		}
	}
	
	/**
	 * Returns all the feature information retrieved using a
	 * describeFeatureTypeOpearion
	 * @param layerName
	 * Feature name
	 * @return
	 * @throws WFSException 
	 */
	public WFSFeature getFeatureInfo(String nameSpace, String layerName) throws WFSException{
		describeFeatureType(layerName, nameSpace, null);
		return (WFSFeature) wfsClient.getFeatures().get(layerName);			
	}
	
	private void describeFeatureType(String featureType, String nameSpace, ICancellable cancel) throws WFSException {
		status = new WFSStatus(featureType, nameSpace);
		wfsClient.describeFeatureType(status, false, cancel);
	}
	
	/**
	 * Returns an array of WFSLayerNode's with the descriptors of
	 * all features (retrieved using the getCapabilities operation)
	 * @return WFSLayerNode[]
	 */
	public Hashtable getFeatures(){
		return wfsClient.getFeatures();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#add(org.gvsig.fmap.dal.NewDataStoreParameters, boolean)
	 */
	public boolean add(NewDataStoreParameters parameters, boolean overwrite)
	throws DataException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#canAdd()
	 */
	public boolean canAdd() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#canAdd(java.lang.String)
	 */
	public boolean canAdd(String storeName) throws DataException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#dispose()
	 */
	public void dispose() throws DataException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#getAddParameters(java.lang.String)
	 */
	public NewDataStoreParameters getAddParameters(String storeName)
	throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#getName()
	 */
	public String getName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#getParameters()
	 */
	public DataServerExplorerParameters getParameters() {
		return parameters;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#list()
	 */
	public List list() throws DataException {
		ArrayList list = new ArrayList();
		Hashtable features = wfsClient.getFeatures();
		Iterator it = features.keySet().iterator();
		DataStoreParameters dsp = null;
		while (it.hasNext()){
			String key = (String)it.next();
			WFSFeature feature = (WFSFeature)features.get(key);
			list.add(getParametersFor(feature));
		}
		return list;
	}

	public DataStoreParameters getParametersFor(WFSFeature feature)
	throws DataException {
		WFSStoreParameters params = (WFSStoreParameters)dataManager
		.createStoreParameters(WFSStoreProvider.NAME);
		params.setUrl(url);
		params.setFeatureType(feature.getNamespace().getName(), 
				feature.getNamespace().getLocation(),
				feature.getName());
		return params;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#list(int)
	 */
	public List list(int mode) throws DataException {
		return list();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataServerExplorer#remove(org.gvsig.fmap.dal.DataStoreParameters)
	 */
	public void remove(DataStoreParameters parameters) throws DataException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.spi.DataServerExplorerProvider#getServerExplorerProviderServices()
	 */
	public DataServerExplorerProviderServices getServerExplorerProviderServices() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.spi.DataServerExplorerProvider#initialize(org.gvsig.fmap.dal.spi.DataServerExplorerProviderServices)
	 */
	public void initialize(
			DataServerExplorerProviderServices dataServerExplorerProviderServices) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return
	 */
	public String getTitle() {
		String title = wfsClient.getServiceInformation().title;
		if (title == null){
			return "None";
		}
		return title;		
	}

	/**
	 * @return
	 */
	public String getAbstract() {
		String _abstract = wfsClient.getServiceInformation().abstr;
		if (_abstract == null){
			return "None";
		}
		return _abstract;
	}

	/**
	 * @return
	 */
	public String getServerType() {
		String serverVersion = wfsClient.getVersion();
		if (serverVersion == null) {
			return "WFS";
		}
		return "WFS "+ serverVersion;
	}

	/**
	 * @return
	 */
	public String getUrl() {
		return wfsClient.getHost();
	}

	/**
	 * @return
	 */
	public int getBuffer() {
		return status.getBuffer();
	}

	/**
	 * @return
	 */
	public int getTimeOut() {
		return status.getTimeout();
	}

	/**
	 * @param userName
	 */
	public void setUserName(String userName) {
		status.setUserName(userName);		
	}

	/**
	 * @param buffer
	 */
	public void setBuffer(int buffer) {
		status.setBuffer(buffer);
	}

	/**
	 * @param timeout
	 */
	public void setTimeOut(int timeout) {
		status.setTimeout(timeout);		
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return wfsClient.getVersion();
	}	
}
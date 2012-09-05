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
package org.gvsig.catalog.loaders;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.gvsig.catalog.schemas.Resource;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.store.catalog.CatalogStoreParameters;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.i18n.Messages;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.extensionPointsOld.ExtensionPointsSingleton;


/**
 * This class is used to load a WFS layer in gvSIG
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class WFSLayerLoader extends GvSigLayerLoader{
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
		
	public WFSLayerLoader(Resource resource) {
		super(resource);
	}
	
	/**
	 * This function loads a WFS resource 
	 * @param host
	 * URL where the server is located
	 * @param layer
	 * Layer name
	 * @throws LayerLoaderException 
	 */
	
	public void loadLayer() throws LayerLoaderException {
		DataStoreParameters storeParameters = null;
		String host = getResource().getLinkage();
		String featureType = getResource().getName();
		try {
			storeParameters = createWFSStoreParameters(host, featureType);
			addLayerToView(featureType, storeParameters);
		} catch (MalformedURLException e) {
			throw new LayerLoaderException(e.getMessage(),getWindowMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LayerLoaderException(e.getMessage(),getWindowMessage());
		}		
	}
	
	private DataStoreParameters createWFSStoreParameters(String host, String featureType) throws Exception{
		CatalogStoreParameters dataStoreParameters = new CatalogStoreParameters();
		dataStoreParameters.setDynValue(DYNFIELDNAME_URL, host);
		int index = featureType.indexOf(":");
		if (index > 0){
			dataStoreParameters.setDynValue(DYNFIELDNAME_NAMESPACEPREFIX, featureType.substring(0, index));
		}
		dataStoreParameters.setDynValue(DYNFIELDNAME_TYPENAME, featureType);
		dataStoreParameters.setDynValue(DYNFIELDNAME_USER, "");
		dataStoreParameters.setDynValue(DYNFIELDNAME_PASSWORD, "");
		
		ExtensionPointManager extensionPointManager = ToolsLocator
		.getExtensionPointManager();
		ExtensionPoint extensionPoint = extensionPointManager.add("CatalogDataStoreParameters");
		Object[] args = new Object[1];
		args[0] = dataStoreParameters;
		
		try {
			return (DataStoreParameters)extensionPoint.create("OGC:WFS", args);
		} catch(Exception e) {
			e.printStackTrace();
			throw new LayerLoaderException(getErrorMessage(),getWindowMessage());
		}		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see es.gva.cit.gvsig.catalogClient.loaders.LayerLoader#getErrorMessage()
	 */
	protected String getErrorMessage() {
		return Messages.getText("wfsError") + ".\n" +
		Messages.getText("server") + ": " + 
		getResource().getLinkage() + "\n" +
		Messages.getText("layer") + ": " +
		getResource().getName();		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see es.gva.cit.gvsig.catalogClient.loaders.LayerLoader#getWindowMessage()
	 */
	protected String getWindowMessage() {
		return Messages.getText("wfsLoad");
	}
	
	
}

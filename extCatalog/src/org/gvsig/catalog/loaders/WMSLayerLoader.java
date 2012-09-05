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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;

import org.exolab.castor.xml.ValidationException;
import org.gvsig.catalog.schemas.Resource;
import org.gvsig.fmap.dal.exception.UnsupportedVersionException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.i18n.Messages;
import org.gvsig.remoteClient.exceptions.WMSException;
import org.gvsig.tools.extensionpoint.ExtensionPoint;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.extensionPointsOld.ExtensionPointsSingleton;

/**
 * This class is used to load a WMS layer in gvSIG
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class WMSLayerLoader extends GvSigLayerLoader{
	
	
	public WMSLayerLoader(Resource resource) {
		super(resource);
		//load(getResource().getLinkage(),getResource().getName());
	}
	
	/**
	 * This function loads a WMS resource 
	 * @param host
	 * URL where the server is located
	 * @param sLayer
	 * Layer name
	 * @param crs
	 * Coordenates System
	 * @throws LayerLoaderException 
	 */
	
	public void loadLayer() throws LayerLoaderException {
		String host = getResource().getLinkage();
		String sLayer = getResource().getName();
		
		FLayer flayer;
		
		try {
			flayer = createWMSLayer(host, sLayer);
			addLayerToView(flayer);
		} catch (Exception e) {
			throw new LayerLoaderException(e.getMessage(),getWindowMessage());
		}   		    
		
	}
	
	/**
	 * It retrieves a getCapabilities file and loads the layer in gvSIG
	 * @param host
	 * WMS URL
	 * @param sLayer
	 * Layer Name
	 * @return
	 * FLayer
	 * @throws ServerOutOfOrderException
	 * @throws ProtocolException
	 * @throws MalformedURLException
	 * @throws WMSException
	 * @throws IOException
	 * @throws UnsupportedVersionException 
	 * @throws ValidationException 
	 * @throws IllegalStateException 
	 * @throws LayerLoaderException 
	 */
	private FLayer createWMSLayer(String host, String sLayer) throws  LayerLoaderException{
		ExtensionPoint extensionPoint = (ExtensionPoint)ExtensionPointsSingleton.getInstance().get("CatalogLayers");
		Map args = new HashMap();
		args.put("host",host);
		args.put("layer",sLayer);
		BaseView activeView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();
		args.put("SRS",activeView.getProjection().getAbrev());
		try {
			return (FLayer)extensionPoint.create("OGC:WMS", args  );
		} catch(Exception e) {
			throw new LayerLoaderException(getErrorMessage(),getWindowMessage());
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see es.gva.cit.gvsig.catalogClient.loaders.LayerLoader#getErrorMessage()
	 */
	protected String getErrorMessage() {
		return Messages.getText("wmsError") + ".\n" +
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
		return Messages.getText("loadWMS");
	}	
}

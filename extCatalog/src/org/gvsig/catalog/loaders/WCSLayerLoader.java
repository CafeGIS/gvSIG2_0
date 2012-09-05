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
import java.util.Map;
import java.util.TreeMap;

import org.gvsig.catalog.schemas.Resource;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.i18n.Messages;
import org.gvsig.tools.extensionpoint.ExtensionPoint;

import com.iver.utiles.extensionPointsOld.ExtensionPointsSingleton;

/**
 * This class is used to load a WCS  layer in gvSIG
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class WCSLayerLoader extends GvSigLayerLoader{
	
	
	public WCSLayerLoader(Resource resource) {
		super(resource);
	}
	
	/**
	 * This method loads a WSC layer from a full URL
	 * @param link
	 * @throws LayerLoaderException 
	 */
	public void loadLayer() throws LayerLoaderException {
		String link = getResource().getLinkage();
		String name = getResource().getName();
		try {
			FLayer flayer = createWCSLayer(link,name);
			addLayerToView(flayer);
		} catch (IOException e) {
			throw new LayerLoaderException(e.getMessage(),getWindowMessage());
		} 	
	}   	
	
	private FLayer createWCSLayer(String link,String name) throws IOException, LayerLoaderException {
		Map args = initFromQueryString(link,name);
		ExtensionPoint extensionPoint = (ExtensionPoint)ExtensionPointsSingleton.getInstance().get("CatalogLayers");
		try {
			return (FLayer)extensionPoint.create("OGC:WCS", args  );
		} catch(Exception e) {
			throw new LayerLoaderException(getErrorMessage(),getWindowMessage());
		}		
	}
	
	/**
	 * Builds a coverage starting from a full GetCoverage URL.
	 * (Using this is not a regular function)
	 */
	private TreeMap initFromQueryString(String link,String name){
		String host = null;
		String queryString = null;
		if (link.compareTo("?")==0){
			host = link.substring(0,link.indexOf('?'));
			queryString = link.substring(link.indexOf('?')+1);
		}else{
			host = link;
			queryString = "";
		}
		queryString = link.substring(link.indexOf('?')+1);
		
		TreeMap map = new TreeMap(); 
		String[] params = queryString.split("&");
		for (int i = 0; i < params.length; i++) {
			if (params[i]!= null){
				String[] nameValue = params[i].split("=");
				if (nameValue.length == 1){
					map.put(nameValue[0].toUpperCase(), "");
				}else if(nameValue.length == 2){
					map.put(nameValue[0].toUpperCase(), nameValue[1]);
				}
			}
		}
		map.put("HOST",link);
		if ((name != null) && (!name.equals(""))){
			map.put("COVERAGE",name);
		}
		return map;
	}	
	
	/*
	 *  (non-Javadoc)
	 * @see es.gva.cit.gvsig.catalogClient.loaders.LayerLoader#getErrorMessage()
	 */
	protected String getErrorMessage() {
		return Messages.getText("wcsError") + ".\n" +
		Messages.getText("link") + ": " + 
		getResource().getLinkage();		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see es.gva.cit.gvsig.catalogClient.loaders.LayerLoader#getWindowMessage()
	 */
	protected String getWindowMessage() {
		return Messages.getText("wcsLoad");
	}
	
	
	
}

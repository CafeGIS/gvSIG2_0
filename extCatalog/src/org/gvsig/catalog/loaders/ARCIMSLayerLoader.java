package org.gvsig.catalog.loaders;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.catalog.schemas.Resource;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.i18n.Messages;
import org.gvsig.tools.extensionpoint.ExtensionPoint;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.extensionPointsOld.ExtensionPointsSingleton;

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
 * $Id: ARCIMSLayerLoader.java 505 2007-07-23 07:14:25Z jorpiell $
 * $Log$
 * Revision 1.1.2.4.4.2  2007/07/23 07:14:25  jorpiell
 * Catalog refactoring
 *
 * Revision 1.1.2.4.4.1  2007/07/13 12:00:36  jorpiell
 * Add the posibility to add a new panel
 *
 * Revision 1.1.2.4  2007/01/08 12:16:30  jcampos
 * Revert changes
 *
 * Revision 1.1.2.2  2006/11/15 00:08:16  jjdelcerro
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/02 08:29:06  jorpiell
 * Modificados los cambios del Branch 10 al head
 *
 * Revision 1.1.2.1  2006/09/20 12:01:36  jorpiell
 * Se ha cambiado la versión de la jzkit, se ha incorporado la funcionalidad de cargar arcims
 *
 * Revision 1.1  2006/09/20 11:23:50  jorpiell
 * Se ha cambiado la versión de la librería jzkit de la 1 a la 2.0. Además se ha modificado lo del document
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class ARCIMSLayerLoader extends GvSigLayerLoader {
	
	public ARCIMSLayerLoader(Resource resource) {
		super(resource);		
	}
	
	/**
	 * This function loads a ArcIms resource 
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
		String protocol = getResource().getProtocol();
		BaseView activeView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();
		String srs = activeView.getProjection().getAbrev();
		FLayer flayer = null;
				
		Map args = new HashMap();
		args.put("host",host);
		args.put("service_name",sLayer);
		args.put("layer_name",sLayer);
		args.put("srs",srs);
		
		if (protocol.toUpperCase().indexOf(Resource.ARCIMS_IMAGE) >= 0){
			flayer = createArcImsImageLayer(args);
		}else if (protocol.toUpperCase().indexOf(Resource.ARCIMS_VECTORIAL) >= 0){
			flayer = createArcImsVectorialLayer(args);
		}
		
		try {
			addLayerToView(flayer);
		} catch (Exception e) {
			throw new LayerLoaderException(e.getMessage(),getWindowMessage());
		}   		    
		
	}	

	private FLayer createArcImsImageLayer(Map args) throws  LayerLoaderException{
		ExtensionPoint extensionPoint = (ExtensionPoint)ExtensionPointsSingleton.getInstance().get("CatalogLayers");

		try {
			return (FLayer)extensionPoint.create("arcims_raster", args  );
		} catch(Exception e) {
			throw new LayerLoaderException(getErrorMessage(),getWindowMessage());
		}
	}
	
	private FLayer createArcImsVectorialLayer(Map args) throws  LayerLoaderException{
		ExtensionPoint extensionPoint = (ExtensionPoint)ExtensionPointsSingleton.getInstance().get("CatalogLayers");
		
		try {			
			return (FLayer)extensionPoint.create("arcims_vectorial", args);
		} catch(Exception e) {
			throw new LayerLoaderException(getErrorMessage(),getWindowMessage());
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see es.gva.cit.gvsig.catalogClient.loaders.LayerLoader#getErrorMessage()
	 */
	protected String getErrorMessage() {
		return Messages.getText("arcims_server_error") + ".\n" +
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
		return Messages.getText("arcims_load");
	}	
}


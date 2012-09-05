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

import org.gvsig.catalog.schemas.Resource;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

/**
 * This class has to be inherited by all the classes
 * that have to load a layer in the current view
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public abstract class GvSigLayerLoader extends LayerLoader{
	
	/**
	 * @param resource
	 */
	public GvSigLayerLoader(Resource resource) {
		super(resource);		
	}

	/**
     * It adds a new layer to the current view
     * @param lyr
     * Layer lo load
     */
    protected void addLayerToView(FLayer lyr) {
		BaseView theView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();
				
		if (lyr != null) {			
			theView.getMapControl().getMapContext().beginAtomicEvent();
			theView.getMapControl().getMapContext().getLayers().addLayer(lyr);
			theView.getMapControl().getMapContext().endAtomicEvent();
			PluginServices.getMainFrame().enableControls();
		}
	}	
    
    /**
     * It adds a new layer to the current view
     * @param lyr
     * Layer lo load
     * @throws LoadLayerException 
     */
    protected void addLayerToView(String layerName, DataStoreParameters storeParameters) throws LoadLayerException {
		BaseView theView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();
			
		FLayer layer = LayerFactory.getInstance().createLayer(layerName, storeParameters);
		
		if (layer != null) {			
			theView.getMapControl().getMapContext().beginAtomicEvent();
			theView.getMapControl().getMapContext().getLayers().addLayer(layer);
			theView.getMapControl().getMapContext().endAtomicEvent();
			PluginServices.getMainFrame().enableControls();
		}
	}	
}

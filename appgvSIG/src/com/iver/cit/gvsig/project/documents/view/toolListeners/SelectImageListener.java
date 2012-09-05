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
package com.iver.cit.gvsig.project.documents.view.toolListeners;

import java.awt.geom.Point2D;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.SelectImageListenerImpl;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;

import com.iver.andami.PluginServices;


/**
 * <p>Listener to select the upper layer with raster data that has information in the associated <code>MapControl</code> object, down
 *  the position selected by a single click of any button of the mouse.</p>
 *
 * <p>The layer selected will be set as <i>active</i>, and the available controls for managing the data will be updated, enabling and
 *  disabling the necessary.</p>
 *
 * <p>The status of the other layers will be changed to <i>not active</i>.</p>
 *
 * @deprecated
 * @author Nacho Brodin <brodin_ign@gva.es>
 */
public class SelectImageListener extends SelectImageListenerImpl {

	//FLyrRaster 			layer = null;

	/**
	 * Extent of the layer selected.
	 */
	Envelope extentLayer = null;

	/**
	 * <p>Creates a new <code>SelectImageListener</code> object.</p>
	 *
	 * @param mapCtrl the <code>MapControl</code> where are stored the layers
	 */
	public SelectImageListener(MapControl mapCtrl) {
		super(mapCtrl);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PointListener#point(com.iver.cit.gvsig.fmap.tools.Events.PointEvent)
	 */
	public void point(PointEvent event) {
		super.point(event);

		Point2D pointSelect = event.getPoint();

		if (PluginServices.getMainFrame() != null) {
			PluginServices.getMainFrame().enableControls();
		}

		ViewPort vp = mapCtrl.getMapContext().getViewPort();

		wcPoint = vp.toMapPoint((int)pointSelect.getX(), (int)pointSelect.getY());

		FLayers layers = mapCtrl.getMapContext().getLayers();
		for(int i=0;i<layers.getLayersCount();i++) {
			layers.getLayer(i).setActive(false);
		}

		for(int i=layers.getLayersCount()-1;i>=0;i--){
			if (select(layers.getLayer(i))) {
				break;
			}
		}
	}

	/**
	 * Determines if the <code>layer</code> is selected at a position starting by the end.
	 *
	 * @param layer kind of layer
	 * @param pos number of positions starting by the end
	 *
	 * @return <code>true</code> if the <code>layer</code> is selected at a position starting by the end, otherwise <code>false</code>
	 */
	private boolean select(FLayer layer){
		if (layer instanceof FLayers){
			FLayers laux=(FLayers)layer;
			for (int j=laux.getLayersCount()-1;j>=0;j--){
				if (select(laux.getLayer(j))){
					return true;
				}
			}
		}else{
			try{
				extentLayer = layer.getFullEnvelope();
			}catch(ReadException exc){
				exc.printStackTrace();
			}
		}
		return false;
	}
}
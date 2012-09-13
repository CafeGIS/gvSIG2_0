/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.rastertools.geolocation.listener;


import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Events.MoveEvent;
import org.gvsig.fmap.raster.layers.GeorefPanListenerImpl;

import com.iver.andami.PluginServices;


/**
 * Extensión de la clase PanListenerImpl de FMap para poder llamar a métodos de
 * andami o de gvSIG.
 * 
 * @version 12/07/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class GeorefPanListener extends GeorefPanListenerImpl {

	/**
	 * Constructor. 
	 * Asigna el MapControl
	 * @param mapCtrl
	 */
	public GeorefPanListener(MapControl mapCtrl) {
		super(mapCtrl);
	}

	public void move(MoveEvent event) {
		super.move(event);
		PluginServices.getMainFrame().enableControls();
	}
}

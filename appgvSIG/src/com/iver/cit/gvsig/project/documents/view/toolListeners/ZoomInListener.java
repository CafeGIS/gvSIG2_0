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

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.ZoomInListenerImpl;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;

import com.iver.andami.PluginServices;


/**
 * <p>Inherits {@link ZoomInListenerImpl ZoomInListenerImpl} enabling/disabling special
 *  controls for managing the data selected.</p>
 *
 * @see ZoomInListenerImpl
 *
 * @author Vicente Caballero Navarro
 */
public class ZoomInListener extends ZoomInListenerImpl {
	/**
	 * <p>Creates a new <code>ZoomInListener</code> object.</p>
	 * 
	 * @param mapCtrl the <code>MapControl</code> where be applied the changes
	 */
	public ZoomInListener(MapControl mapCtrl) {
		super(mapCtrl);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.ZoomInListenerImpl#rectangle(com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent)
	 */
	public void rectangle(EnvelopeEvent event) {
		super.rectangle(event);
		if (PluginServices.getMainFrame() != null)
		    PluginServices.getMainFrame().enableControls();
	}
}

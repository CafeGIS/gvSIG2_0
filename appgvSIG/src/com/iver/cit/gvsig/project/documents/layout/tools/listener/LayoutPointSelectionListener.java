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
package com.iver.cit.gvsig.project.documents.layout.tools.listener;

import java.awt.Image;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Implementación de la interfaz LayoutPointListener como herramienta para realizar
 * una selección por punto.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutPointSelectionListener implements LayoutPointListener {
//	private final Image img = new ImageIcon(MapControl.class.getResource(
//				"images/PointSelectCursor.gif")).getImage();
	private final Image img = PluginServices.getIconTheme().get("point-select-cursor").getImage();
	protected Layout layout;

	/**
	 * Crea un nuevo AreaListenerImpl.
	 *
	 * @param mc MapControl.
	 */
	public LayoutPointSelectionListener(Layout l) {
		this.layout = l;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.PointListener#point(org.gvsig.fmap.mapcontrol.tools.Events.PointEvent)
	 */
	public void point(PointEvent event) throws BehaviorException {

	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return img;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return false;
	}

	public void pointDoubleClick(PointEvent event) throws BehaviorException {

	}


}

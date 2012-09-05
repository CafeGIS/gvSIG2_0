/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
package com.iver.cit.gvsig.project.documents.layout.tools;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutRectangleListener;


/**
 * Implementaci�n de la interfaz LayoutPointListener como herramienta para realizar
 * un zoom menos.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutZoomOutListenerImpl implements LayoutRectangleListener {
	 private static final Image iLayoutzoomout = PluginServices.getIconTheme()
		.get("layout-zoom-out-cursor").getImage();
	private Layout layout;

	/**
	 * Crea un nuevo LayoutZoomOutListenerImpl.
	 *
	 * @param mapControl MapControl.
	 */
	public LayoutZoomOutListenerImpl(Layout l) {
		this.layout = l;
	}


	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return iLayoutzoomout;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
	    System.out.println("cancelDrawing del ZoomOutListenerImpl");
		return true;
	}

	public void pointDoubleClick(PointEvent event) {
		// TODO Auto-generated method stub

	}

	public void rectangle(EnvelopeEvent event) throws BehaviorException {
		if (event.getEvent().getButton() == MouseEvent.BUTTON1) {
			Point p2 = new Point(event.getEvent().getX(), event.getEvent().getY());
			layout.getLayoutControl().getLayoutZooms().setZoomOut(p2);
        	layout.getLayoutControl().refresh();
		}

	}
}

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

import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutRectangleListener;


/**
 * Implementaci�n de la interfaz LayoutRectangleListener como herramienta para
 * realizar un zoom m�s.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutZoomInListenerImpl implements LayoutRectangleListener {
	private static final Image iLayoutzoomin = PluginServices.getIconTheme()
		.get("layout-zoom-in-cursor").getImage();
	private Layout layout;

	/**
	 * Crea un nuevo LayoutRectangleListenerImpl.
	 *
	 * @param s Layout.
	 */
	public LayoutZoomInListenerImpl(Layout l) {
		this.layout = l;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.RectangleListener#rectangle(org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent)
	 */
	public void rectangle(EnvelopeEvent event) {
		if (event.getEvent().getButton() != MouseEvent.BUTTON3) {
            layout.getLayoutControl().setLastPoint();
        }

        if (event.getEvent().getButton() == MouseEvent.BUTTON1) {
            Point p1 = layout.getLayoutControl().getFirstPoint();
            Point p2 = new Point(event.getEvent().getX(), event.getEvent().getY());

            layout.getLayoutControl().getLayoutZooms().setZoomIn(p1, p2);
            layout.getLayoutControl().refresh();
        }
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return iLayoutzoomin;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
	    System.out.println("cancelDrawing del ZoomOutListenerImpl");
		return true;
	}
}

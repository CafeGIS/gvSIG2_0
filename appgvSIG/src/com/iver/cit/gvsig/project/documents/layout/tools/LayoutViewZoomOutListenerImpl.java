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

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutMoveListener;


/**
 * Implementaci�n de la interfaz LayoutPanListener como herramienta para realizar el
 * zoom menos sobre una vista seleccionada.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutViewZoomOutListenerImpl implements LayoutMoveListener {
	 public static final Image izoomout = PluginServices.getIconTheme()
	 	.get("zoom-out-cursor").getImage();
	private Layout layout;
	/**
	 * Crea un nuevo LayoutViewZoomInListenerImpl.
	 *
	 * @param l Layout.
	 */
	public LayoutViewZoomOutListenerImpl(Layout l) {
		this.layout = l;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.PanListener#move(java.awt.geom.Point2D,
	 * 		java.awt.geom.Point2D)
	 */
	public void drag(PointEvent event) {

	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return izoomout;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return true;
	}

	public void press(PointEvent event) throws BehaviorException {
		// TODO Auto-generated method stub
	}

	public void release(PointEvent event) throws BehaviorException {
		layout.getLayoutControl().setLastPoint();
		layout.getLayoutControl().setPointAnt();
        Point p2 = event.getEvent().getPoint();
        layout.getLayoutControl().getLayoutZooms().setViewZoomOut(p2);
		layout.getLayoutControl().refresh();
	}

	public void move(PointEvent event) throws BehaviorException {
		// TODO Auto-generated method stub

	}

	public void click(PointEvent event) throws BehaviorException {
		// TODO Auto-generated method stub

	}

}

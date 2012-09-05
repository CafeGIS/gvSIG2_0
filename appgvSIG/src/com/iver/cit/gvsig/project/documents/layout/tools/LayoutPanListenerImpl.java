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
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutMoveListener;


/**
 * Implementaci�n de la interfaz LayoutPanListener como herramienta para realizar el
 * Pan.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutPanListenerImpl implements LayoutMoveListener {
	public static final Image iLayoutpan = PluginServices.getIconTheme()
	 	.get("layout-hand-icon").getImage();
	private Layout layout;
	/**
	 * Crea un nuevo RectangleListenerImpl.
	 *
	 * @param mapControl MapControl.
	 */
	public LayoutPanListenerImpl(Layout l) {
		this.layout = l;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.PanListener#move(java.awt.geom.Point2D,
	 * 		java.awt.geom.Point2D)
	 */
	public void drag(PointEvent event) {
		Point pLast=layout.getLayoutControl().getLastPoint();
		Point pAnt=layout.getLayoutControl().getPointAnt();
		Point origin=layout.getLayoutControl().getRectOrigin();
		layout.getLayoutControl().getRect().x=origin.getX()+pLast.getX()-pAnt.getX();
		layout.getLayoutControl().getRect().y=origin.getY()+pLast.getY()-pAnt.getY();
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return iLayoutpan;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return true;
	}

	public void press(PointEvent event) throws BehaviorException {
		layout.getLayoutControl().getRectOrigin().setLocation(layout.getLayoutControl().getRect().x,
                 layout.getLayoutControl().getRect().y);

	}

	public void release(PointEvent event) throws BehaviorException {
		Point p1;
		Point p2;

		if (event.getEvent().getButton() == MouseEvent.BUTTON1) {
			p1 = layout.getLayoutControl().getFirstPoint();
	        p2 = event.getEvent().getPoint();
			layout.getLayoutControl().getLayoutZooms().setPan(p1,p2);
			layout.getLayoutControl().refresh();
	     	PluginServices.getMainFrame().enableControls();
		}

		layout.getLayoutControl().setFirstPoint();
		layout.getLayoutControl().setLastPoint();
		layout.getLayoutControl().setPointAnt();
		layout.getLayoutControl().getRectOrigin().setLocation(layout.getLayoutControl().getRect().x,
               layout.getLayoutControl().getRect().y);

	}

	public void move(PointEvent event) throws BehaviorException {
		// TODO Auto-generated method stub

	}

	public void click(PointEvent event) throws BehaviorException {
		// TODO Auto-generated method stub

	}

}

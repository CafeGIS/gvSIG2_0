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
package com.iver.cit.gvsig.project.documents.layout.tools.behavior;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;

import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutPointListener;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutToolListener;


/**
 * LayoutBehaviour que espera un listener de tipo LayoutPointListener.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutPointBehavior extends LayoutBehavior{
	private LayoutPointListener listener;
	private boolean doubleClick=false;
	/**
	 * Crea un nuevo LayoutPointBehavior.
	 *
	 * @param l listener.
	 */
	public LayoutPointBehavior(LayoutPointListener l) {
		listener = l;
	}

	/**
	 * Reimplementación del método mousePressed de Behavior.
	 *
	 * @param e MouseEvent
	 * @throws BehaviorException
	 */
	public void mousePressed(MouseEvent e) throws BehaviorException {
		super.mousePressed(e);
		if (listener.cancelDrawing()) {
		//	getMapControl().cancelDrawing();
		}
		if (e.getClickCount()==2){
			doubleClick=true;
		}
	}
	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	/**
	 * Reimplementación del método mouseReleased de Behavior.
	 *
	 * @param e MouseEvent
	 *
	 * @throws BehaviorException Excepción lanzada cuando el Maptool.
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException {
		super.mouseReleased(e);
		PointEvent event = new PointEvent(e.getPoint(), e);
		listener.point(event);
		if (doubleClick){
			listener.pointDoubleClick(event);
			doubleClick=false;
		}
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior#setListener(com.iver.cit.gvsig.fmap.tools.ToolListener)
	 */
	public void setListener(ToolListener listener) {
		this.listener = (LayoutPointListener) listener;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior#getListener()
	 */
	public LayoutToolListener getListener() {
		return listener;
	}

	public void mouseMoved(MouseEvent e) throws BehaviorException {
		super.mouseMoved(e);
		 getLayoutControl().setGeometryAdapterPoinPosition();
	}

	public boolean isAdjustable() {
		return true;
	}
}

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
package org.gvsig.fmap.mapcontrol.tools.Behavior;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.MoveEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PanListener;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;



/**
 * <p>Behavior that permits user to drag the image of the associated
 *  <code>MapControl</code> using a {@link PanListener PanListener}.</p>
 *
 * @author Vicente Caballero Navarro
  * @author Pablo Piqueras Bartolomé
 */
public class DraggerBehavior extends Behavior {
	/**
	 * First point of the path in image coordinates.
	 */
	private Point2D m_FirstPoint;

	/**
	 * Tool listener used to work with the <code>MapControl</code> object.
	 *
	 * @see #getListener()
	 * @see #setListener(ToolListener)
	 */
	private PanListener listener;

	/**
 	 * <p>Creates a new behavior for dragging the mouse.</p>
 	 *
	 * @param pli listener used to permit this object to work with the associated <code>MapControl</code>
	 */
	public DraggerBehavior(PanListener pli) {
		listener = pli;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			m_FirstPoint = e.getPoint();
		}

		if (listener.cancelDrawing()) {
			getMapControl().cancelDrawing();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException {
		m_FirstPoint = null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) throws BehaviorException {
		MoveEvent event = new MoveEvent(m_FirstPoint, e.getPoint(), e);
		listener.move(event);
		getMapControl().repaint();
	}

	/**
	 * <p>Sets a tool listener to work with the <code>MapControl</code> using this behavior.</p>
	 *
	 * @param listener a <code>PanListener</code> object for this behavior
	 */
	public void setListener(ToolListener listener) {
		this.listener = (PanListener) listener;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getListener()
	 */
	public ToolListener getListener() {
		return listener;
	}
}

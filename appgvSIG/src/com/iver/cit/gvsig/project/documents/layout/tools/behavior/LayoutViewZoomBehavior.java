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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;

import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutMoveListener;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutToolListener;


/**
 * Behaviour que espera un listener de tipo MoveListener.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutViewZoomBehavior extends LayoutBehavior {
	private LayoutMoveListener listener;
	private boolean dragged=false;
	/**
	 * Crea un nuevo MoveBehavior.
	 *
	 * @param pli listener.
	 */
	public LayoutViewZoomBehavior(LayoutMoveListener lpl) {
		listener = lpl;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		BufferedImage img = getLayoutControl().getImage();
		BufferedImage imgRuler=getLayoutControl().getImgRuler();
		g.drawImage(img, 0, 0, getLayoutControl());
		g.drawImage(imgRuler, 0, 0, getLayoutControl());
		g.setColor(Color.black);
		g.setXORMode(Color.white);

		// Borramos el anterior
		Rectangle r = new Rectangle();

		// Dibujamos el actual
		if (dragged && (getLayoutControl().getFirstPoint() != null) && (getLayoutControl().getLastPoint() != null)) {
			r.setFrameFromDiagonal(getLayoutControl().getFirstPoint(), getLayoutControl().getLastPoint());
			g.drawRect(r.x, r.y, r.width, r.height);
		}
		 IFFrame[] frames = getLayoutControl().getLayoutContext().getFFrameSelected();
	        for (int i = 0; i < frames.length; i++) {
	            g.setColor(Color.black);
	            frames[i].drawHandlers((Graphics2D) g);
	        }
		g.setPaintMode();
	}

	/**
	 * @throws BehaviorException
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) throws BehaviorException {
		super.mousePressed(e);
		PointEvent event = new PointEvent(e.getPoint(), e);
		listener.press(event);
	}

	/**
	 * Reimplementación del método mouseReleased de Behavior.
	 *
	 * @param e MouseEvent
	 *
	 * @throws BehaviorException Excepción lanzada cuando el Behavior.
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException {
		super.mouseReleased(e);
		PointEvent event = new PointEvent(e.getPoint(), e);
		listener.release(event);
		dragged=false;
	}

	/**
	 * Reimplementación del método mouseDragged de Behavior.
	 *
	 * @param e MouseEvent
	 * @throws BehaviorException
	 */
	public void mouseDragged(MouseEvent e) throws BehaviorException {
		super.mouseDragged(e);
		PointEvent event = new PointEvent(e.getPoint(), e);
		listener.drag(event);
		dragged=true;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior#setListener(com.iver.cit.gvsig.fmap.tools.ToolListener)
	 */
	public void setListener(LayoutToolListener listener) {
		this.listener = (LayoutMoveListener) listener;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior#getListener()
	 */
	public LayoutToolListener getListener() {
		return listener;
	}
}

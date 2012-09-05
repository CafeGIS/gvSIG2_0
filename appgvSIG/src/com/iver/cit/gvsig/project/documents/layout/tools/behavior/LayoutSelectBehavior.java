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
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.PointEvent;

import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutMoveListener;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutToolListener;


/**
 * Behaviour que espera un listener de tipo LayoutMoveListener.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutSelectBehavior extends LayoutBehavior {
	private LayoutMoveListener listener;
	private boolean dragged=false;

	/**
	 * Crea un nuevo LayoutSelectBehavior.
	 *
	 * @param pli listener.
	 */
	public LayoutSelectBehavior(LayoutMoveListener lpl) {
		listener = lpl;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
	    getLayoutControl().getLayoutDraw().drawRectangle((Graphics2D) g);

        g.drawImage(getLayoutControl().getImage(), 0, 0, getLayoutControl());

        //getLayout().getLayoutDraw().drawHandlers((Graphics2D) g, Color.black);
        if (getLayoutControl().isReSel()) {
        	Rectangle reSel=getLayoutControl().getReSel();
        	reSel=new Rectangle();
        	reSel.setFrameFromDiagonal(getLayoutControl().getFirstPoint(), getLayoutControl().getLastPoint());
            g.drawRect(reSel.x, reSel.y, reSel.width, reSel.height);
        }
        IFFrame[] frames = getLayoutControl().getLayoutContext().getFFrameSelected();
        for (int i = 0; i < frames.length; i++) {
            g.setColor(Color.black);
            frames[i].drawHandlers((Graphics2D) g);
            int difx = (getLayoutControl().getLastPoint().x - getLayoutControl().getFirstPoint().x);
            int dify = (getLayoutControl().getLastPoint().y - getLayoutControl().getFirstPoint().y);
            if ((Math.abs(difx) > 3) || (Math.abs(dify) > 3)) {
                Rectangle2D rectangle = frames[i].getMovieRect(difx, dify);
                if (rectangle == null)
                    return;
                ((Graphics2D) g).rotate(Math.toRadians(frames[i]
                        .getRotation()), rectangle.getX()
                        + (rectangle.getWidth() / 2), rectangle.getY()
                        + (rectangle.getHeight() / 2));

                if (rectangle != null && dragged && !getLayoutControl().isReSel()) {
                    g.drawRect((int) rectangle.getMinX(), (int) rectangle
                            .getMinY(), (int) rectangle.getWidth(),
                            (int) rectangle.getHeight());
                }

                ((Graphics2D) g).rotate(Math.toRadians(-frames[i]
                        .getRotation()), rectangle.getX()
                        + (rectangle.getWidth() / 2), rectangle.getY()
                        + (rectangle.getHeight() / 2));

            }
        }

        //g.setClip(rClip);
        g.drawImage(getLayoutControl().getImgRuler(), 0, 0, getLayoutControl());
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

	public void mouseClicked(MouseEvent e) throws BehaviorException {
		super.mouseClicked(e);
		PointEvent event = new PointEvent(e.getPoint(), e);
		listener.click(event);
	}

	public void mouseMoved(MouseEvent e) throws BehaviorException {
		super.mouseMoved(e);
		PointEvent event = new PointEvent(e.getPoint(), e);
		listener.move(event);
	}
}

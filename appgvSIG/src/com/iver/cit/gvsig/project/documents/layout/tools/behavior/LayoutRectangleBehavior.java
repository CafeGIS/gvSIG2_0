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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.cit.gvsig.gui.styling.SymbolSelector;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutRectangleListener;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutToolListener;


/**
 * Behaviour que espera un listener de tipo LayoutRectangleListener.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutRectangleBehavior extends LayoutBehavior {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(LayoutRectangleBehavior.class);
	private LayoutRectangleListener listener;
	private boolean dragged=false;

	/**
	 * Crea un nuevo RectangleBehavior.
	 *
	 * @param zili listener.
	 */
	public LayoutRectangleBehavior(LayoutRectangleListener lrl) {
		listener = lrl;
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
	 * Reimplementación del método mousePressed de Behavior.
	 *
	 * @param e MouseEvent
	 * @throws BehaviorException
	 */
	public void mousePressed(MouseEvent e) throws BehaviorException {
		super.mousePressed(e);
		if (listener.cancelDrawing()) {
			//getLayout().cancelDrawing();
		}
		getLayoutControl().repaint();
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
	    dragged=false;
		if (getLayoutControl().getFirstPoint() == null) return;
		Point2D p1;
		Point2D p2;
		Point pScreen = getLayoutControl().getLastPoint();

		AffineTransform at = getLayoutControl().getAT();

		p1 = FLayoutUtilities.toSheetPoint(getLayoutControl().getFirstPoint(), at);
		p2 = FLayoutUtilities.toSheetPoint(pScreen, at);

		if (e.getButton() == MouseEvent.BUTTON1) {
			//	Fijamos el nuevo extent
//			Rectangle2D.Double r = new Rectangle2D.Double();
//			r.setFrameFromDiagonal(p1, p2);
			Envelope r;
			try {
				r = geomManager.createEnvelope(p1.getX(),p1.getY(),p2.getX(),p2.getY(), SUBTYPES.GEOM2D);
				Rectangle2D rectPixel = new Rectangle();
				rectPixel.setFrameFromDiagonal(getLayoutControl().getFirstPoint(), pScreen);

				EnvelopeEvent event = new EnvelopeEvent(r, e, rectPixel);
				listener.rectangle(event);
			} catch (CreateEnvelopeException e1) {
				logger.error("Error creating the envelope", e);
			}			
		}

	}

	/**
	 * Reimplementación del método mouseDragged de Behavior.
	 *
	 * @param e MouseEvent
	 * @throws BehaviorException
	 */
	public void mouseDragged(MouseEvent e) throws BehaviorException {
		super.mouseDragged(e);
		dragged=true;
		getLayoutControl().repaint();
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior#setListener(com.iver.cit.gvsig.fmap.tools.ToolListener)
	 */
	public void setListener(LayoutToolListener listener) {
		this.listener = (LayoutRectangleListener) listener;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior#getListener()
	 */
	public LayoutToolListener getListener() {
		return listener;
	}

	public boolean isAdjustable() {
		return true;
	}
}

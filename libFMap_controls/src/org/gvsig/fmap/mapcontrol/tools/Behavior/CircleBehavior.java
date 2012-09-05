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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.MeasureEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.CircleListener;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;



/**
 * <p>Behavior that allows user to draw a circle on the image of the associated
 *  <code>MapControl</code> using a {@link PolylineListener PolylineListener}.</p>
 *
 * @author Laura
 * @author Pablo Piqueras Bartolomé
 */
public class CircleBehavior extends Behavior {
	/**
	 * First point set, that represents the center of the circle.
	 */
	protected Point2D m_FirstPoint;

	/**
	 * Second point set, that permits calculate the radius of the circle.
	 */
	protected Point2D m_LastPoint;

	/**
	 * Tool listener used to work with the <code>MapControl</code> object.
	 *
	 * @see #getListener()
	 * @see #setListener(ToolListener)
	 */
	private CircleListener listener;

	/**
	 * Determines if user setting the radius of the circle (with one click of the button 1 of the mouse), or not.
	 */
	protected boolean isClicked = false;

	/**
 	 * <p>Creates a new behavior for selecting circle areas.</p>
 	 *
	 * @param zili listener used to permit this object to work with the associated <code>MapControl</code>
	 */
	public CircleBehavior(CircleListener zili) {
		listener = zili;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {

		double radio;
		BufferedImage img = getMapControl().getImage();
		g.drawImage(img, 0, 0, null);
		g.setColor(Color.black);
		g.setXORMode(Color.white);
		if ((m_FirstPoint != null) && (m_LastPoint != null)) {
			radio = m_LastPoint.distance(m_FirstPoint);
			Arc2D.Double arc = new Arc2D.Double(m_FirstPoint.getX()-radio,
												m_FirstPoint.getY()-radio,
												2*radio,
												2*radio, 0, 360, Arc2D.OPEN);
			((Graphics2D) g).draw(arc);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			m_FirstPoint = e.getPoint();
			isClicked = true;
			getMapControl().repaint();
		}

		if (listener.cancelDrawing()) {
			getMapControl().cancelDrawing();
			isClicked = false;
			getMapControl().repaint();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException {
		m_FirstPoint = null;
		m_LastPoint = null;
		isClicked = false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) throws BehaviorException {
		mouseMoved(e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e)  throws BehaviorException {
	    if (m_FirstPoint == null) return;

	    m_LastPoint = e.getPoint();

		ViewPort vp = getMapControl().getMapContext().getViewPort();

		Point2D p1 = vp.toMapPoint(m_FirstPoint);
		Point2D p2 = vp.toMapPoint(m_LastPoint);

		//	Fijamos el nuevo extent
		Rectangle2D.Double r = new Rectangle2D.Double();
		r.setFrameFromDiagonal(p1, p2);

		Rectangle2D rectPixel = new Rectangle();
		rectPixel.setFrameFromDiagonal(m_FirstPoint, m_LastPoint);

		Double[] x = new Double[2];
		Double[] y = new Double[2];
		x[0] = new Double(p1.getX());
		x[1] = new Double(p2.getX());
		y[0] = new Double(p1.getY());
		y[1] = new Double(p2.getY());
		MeasureEvent event = new MeasureEvent(x, y, e);
		listener.circle(event);
		getMapControl().repaint();
	}

	/**
	 * <p>Sets a tool listener to work with the <code>MapControl</code> using this behavior.</p>
	 *
	 * @param listener a <code>CircleListener</code> object for this behavior
	 */
	public void setListener(ToolListener listener) {
		this.listener = (CircleListener)listener;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getListener()
	 */
	public ToolListener getListener() {
		return listener;
	}
}


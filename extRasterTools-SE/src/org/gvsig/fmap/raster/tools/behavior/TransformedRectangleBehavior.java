/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2008 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.fmap.raster.tools.behavior;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.RectangleListener;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Comportamiento de una selección por rectangulo con una transformación
 * aplicada. El resultado visual es la selección como un rectangulo común con
 * el rectangulo transformado superpuesto.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TransformedRectangleBehavior extends Behavior {
	private static final GeometryManager	geomManager		 = GeometryLocator.getGeometryManager();
	private static final Logger 			logger 			 = LoggerFactory.getLogger(GeometryManager.class);
	/**
	 * First point of the rectangle area, that represents a corner.
	 */
	private Point2D             			m_FirstPoint     = null;

	/**
	 * Second point of the rectangle area, that represents the opposite corner of the first,
	 *  along the diagonal.
	 */
	private Point2D              			m_LastPoint     = null;

	/**
	 * Tool listener used to work with the <code>MapControl</code> object.
	 *
	 * @see #getListener()
	 * @see #setListener(ToolListener)
	 */
	private RectangleListener   			listener        = null;
	private AffineTransform      			at              = null;
	private BasicStroke          			stroke          = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f);

	/**
	 * <p>Creates a new behavior for selecting rectangle areas.</p>
	 *
	 * @param zili listener used to permit this object to work with the associated <code>MapControl</code>
	 */
	public TransformedRectangleBehavior(RectangleListener zili) {
		listener = zili;
	}

	/**
	 * Asigna la matriz de transformación para el cuadro externo.
	 * @param at AffineTransform
	 */
	public void setAffineTransform(AffineTransform at) {
		this.at = at;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		BufferedImage img = getMapControl().getImage();
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		((Graphics2D)g).setRenderingHints(hints);
		g.drawImage(img, 0, 0, null);
		g.setColor(Color.black);
		g.setXORMode(Color.white);

		// Borramos el anterior
		Rectangle r = new Rectangle();

		// Dibujamos el actual
		if ((m_FirstPoint != null) && (m_LastPoint != null)) {
			r.setFrameFromDiagonal(m_FirstPoint, m_LastPoint);
			g.drawRect(r.x, r.y, r.width, r.height);
		}

		//Dibujamos el cuadro exterior
		if(at != null) {
			Point2D.Double ul = new Point2D.Double(r.x, r.y);
			Point2D.Double ur = new Point2D.Double(r.x + r.width, r.y);
			Point2D.Double ll = new Point2D.Double(r.x, r.y + r.height);
			Point2D.Double lr = new Point2D.Double(r.x + r.width, r.y + r.height);

			Point2D center = new Point2D.Double((r.width) / 2.0, (r.height) / 2.0);
			AffineTransform T1 = new AffineTransform(1D, 0, 0, 1, -center.getX(), -center.getY());
			AffineTransform R1 = new AffineTransform(1D, at.getShearY() / at.getScaleY(), at.getShearX() / at.getScaleX(), 1, 0, 0);
			AffineTransform T2 = new AffineTransform(1D, 0, 0, 1, center.getX(), center.getY());
			T2.concatenate(R1);
			T2.concatenate(T1);

			try {
				T2.inverseTransform(ul, ul);
				T2.inverseTransform(ll, ll);
				T2.inverseTransform(ur, ur);
				T2.inverseTransform(lr, lr);

				Point2D.Double ptMin = new Point2D.Double(	Math.min(Math.min(ul.getX(), ur.getX()), Math.min(ll.getX(), lr.getX())),
															Math.min(Math.min(ul.getY(), ur.getY()), Math.min(ll.getY(), lr.getY())));
				Point2D.Double ptMax = new Point2D.Double(	Math.max(Math.max(ul.getX(), ur.getX()), Math.max(ll.getX(), lr.getX())),
															Math.max(Math.max(ul.getY(), ur.getY()), Math.max(ll.getY(), lr.getY())));
				double w = ptMax.getX() - ptMin.getX();
				double h = ptMax.getY() - ptMin.getY();

				((Graphics2D)g).setStroke(stroke);
				((Graphics2D)g).transform(T2);
				((Graphics2D)g).drawRect((int)ptMin.getX(), (int)ptMin.getY(), (int)w, (int)h);
				((Graphics2D)g).transform(at.createInverse());
			} catch (NoninvertibleTransformException e) {
				return;
			}
		}
		g.setPaintMode();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			m_FirstPoint = e.getPoint();
			getMapControl().repaint();
		}
		if (listener.cancelDrawing()) {
			getMapControl().cancelDrawing();
			getMapControl().repaint();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException {
	    if (m_FirstPoint == null) return;
		Point2D p1;
		Point2D p2;
		Point pScreen = e.getPoint();

		ViewPort vp = getMapControl().getMapContext().getViewPort();

		p1 = vp.toMapPoint(m_FirstPoint);
		p2 = vp.toMapPoint(pScreen);

		if (e.getButton() == MouseEvent.BUTTON1) {
			//	Fijamos el nuevo extent
			Rectangle2D.Double r = new Rectangle2D.Double();
			r.setFrameFromDiagonal(p1, p2);
			Envelope env = null;
			try {
				env = geomManager.createEnvelope(p1.getX(), p1.getY(),
						p2.getX(), p2.getY(), SUBTYPES.GEOM2D);
			} catch (CreateEnvelopeException e1) {
				logger.error("Error creating the envelope", e);
			}

			Rectangle2D rectPixel = new Rectangle();
			rectPixel.setFrameFromDiagonal(m_FirstPoint, pScreen);

			EnvelopeEvent event = new EnvelopeEvent(env, e, rectPixel);
			listener.rectangle(event);
		}

		m_FirstPoint = null;
		m_LastPoint = null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		m_LastPoint = e.getPoint();
		getMapControl().repaint();
	}

	/**
	 * <p>Sets a tool listener to work with the <code>MapControl</code> using this behavior.</p>
	 *
	 * @param listener a <code>RectangleListener</code> object for this behavior
	 */
	public void setListener(ToolListener listener) {
		this.listener = (RectangleListener) listener;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getListener()
	 */
	public ToolListener getListener() {
		return listener;
	}
}
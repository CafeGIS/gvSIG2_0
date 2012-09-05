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
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.MeasureEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PolylineListener;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;



/**
 * <p>Behavior that allows user to draw a polyline by its vertexes on the image of the associated
 *  <code>MapControl</code> using a {@link PolylineListener PolylineListener}.</p>
 *
 * @author Vicente Caballero Navarro
 * @author Pablo Piqueras Bartolomé
 */
public class PolylineBehavior extends Behavior {
	/**
	 * The abscissa coordinate of all vertexes of the polyline.
	 */
	protected ArrayList arrayX = new ArrayList(); //<Double>

	/**
	 * The ordinate coordinate of all vertexes of the polyline.
	 */
	protected ArrayList arrayY = new ArrayList(); //<Double>

	/**
	 * Determines if user is setting the vertexes (with one click of the button 1 of the mouse), or not.
	 */
	protected boolean isClicked = false;

	/**
	 * Tool listener used to work with the <code>MapControl</code> object.
	 *
	 * @see #getListener()
	 * @see #setListener(ToolListener)
	 */
	protected PolylineListener listener;

	/**
	 * <p>Creates a new behavior for drawing a polyline by its vertexes.</p>
	 *
	 * @param mli listener used to permit this object to work with the associated <code>MapControl</code>
	 */
	public PolylineBehavior(PolylineListener mli) {
		listener = mli;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(getMapControl().getImage(), 0, 0, null);
		g.setColor(Color.black);

		if (!arrayX.isEmpty()) {
			drawPolyLine((Graphics2D) g);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) throws BehaviorException {
		if (e.getClickCount() == 2) {
			listener.polylineFinished(new MeasureEvent((Double[]) arrayX.toArray(new Double[arrayX.size()]), (Double[]) arrayY.toArray(new Double[arrayY.size()]), e));

			arrayX.clear();
			arrayY.clear();

			isClicked = false;
		} else {
			//System.err.println("simpleclick");
			isClicked = true;
			Point2D point = getMapControl().getViewPort().toMapPoint(e.getPoint());
			addPoint(point);

			if (arrayX.size() < 2) {
				addPoint(point);
			}

			listener.pointFixed(new MeasureEvent((Double[]) arrayX.toArray(new Double[arrayX.size()]), (Double[]) arrayY.toArray(new Double[arrayY.size()]), e));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) throws BehaviorException {
		mouseMoved(e);
	}

	/**
	 * <p>Changes the last point added of the polyline.</p>
	 *
	 * @param p a point which will replace the last added to the polyline
	 */
	protected void changeLastPoint(Point2D p) {
		if (arrayX.size() > 0) {
			arrayX.set(arrayX.size() - 1, new Double(p.getX()));
			arrayY.set(arrayY.size() - 1, new Double(p.getY()));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) throws BehaviorException {
		//System.err.println("moved antes de click");
		if (isClicked) {
			//System.err.println("moved despues de click");
			changeLastPoint(getMapControl().getViewPort().toMapPoint(e.getPoint()));

			MeasureEvent event = new MeasureEvent((Double[]) arrayX.toArray(new Double[arrayX.size()]), (Double[]) arrayY.toArray(new Double[arrayY.size()]), e);

			listener.points(event);
			getMapControl().repaint();
		}
	}

	/**
	 * <p>Draws the polyline in the <code>Graphics2D</code> of the associated <code>MapControl</code>.</p>
	 *
	 * @param g2 the 2D context that allows draw the polyline
	 */
	protected void drawPolyLine(Graphics2D g2) {
		GeneralPathX polyline = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD, arrayX.size());

		Point2D pScreen0=getMapControl().getViewPort().fromMapPoint(new Point2D.Double(((Double) arrayX.get(0)).doubleValue(),
				((Double) arrayY.get(0)).doubleValue()));

		polyline.moveTo(pScreen0.getX(), pScreen0.getY());

		for (int index = 0; index < arrayX.size(); index++) {
			Point2D pScreen=getMapControl().getViewPort().fromMapPoint(new Point2D.Double(((Double) arrayX.get(index)).doubleValue(),
					((Double) arrayY.get(index)).doubleValue()));

			polyline.lineTo(pScreen.getX(),pScreen.getY());
		}

		g2.draw(polyline);
	}

	/**
	 * <p>Adds a new point to the polyline.</p>
	 *
	 * @param p a new point to the polyline
	 */
	protected void addPoint(Point2D p) {
		arrayX.add(new Double(p.getX()));
		arrayY.add(new Double(p.getY()));
	}

	/**
	 * <p>Sets a tool listener to work with the <code>MapControl</code> using this behavior.</p>
	 *
	 * @param listener a <code>PolylineListener</code> object for this behavior
	 */
	public void setListener(ToolListener listener) {
		this.listener = (PolylineListener) listener;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getListener()
	 */
	public ToolListener getListener() {
		return listener;
	}
}

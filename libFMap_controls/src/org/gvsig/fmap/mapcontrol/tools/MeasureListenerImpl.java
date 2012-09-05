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
package org.gvsig.fmap.mapcontrol.tools;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Events.MeasureEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PolylineListener;



/**
 * <p>Listener for calculating the length of the segments of a polyline, defined in the associated {@link MapControl MapControl}
 *  object.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class MeasureListenerImpl implements PolylineListener {
	/**
	 * The image to display when the cursor is active.
	 */
	private final Image iruler = new ImageIcon(MapControl.class.getResource(
				"images/RulerCursor.gif")).getImage();

	/**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
//	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(iruler,
//			new Point(16, 16), "");

	/**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
	protected MapControl mapCtrl;

	/**
	 * <p>Creates a new listener for calculating the length of a polyline.</p>
	 *
	 * @param mc the <code>MapControl</code> where is calculated the length
	 */
	public MeasureListenerImpl(MapControl mc) {
		this.mapCtrl = mc;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PolylineListener#points(com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent)
	 */
	public void points(MeasureEvent event) {
		double dist = 0;
		double distAll = 0;

		ViewPort vp = mapCtrl.getMapContext().getViewPort();

		for (int i = 0; i < (event.getXs().length - 1); i++) {
			dist = 0;

			Point p = new Point(event.getXs()[i].intValue(),
					event.getXs()[i].intValue());
			Point p2 = new Point(event.getXs()[i + 1].intValue(),
					event.getXs()[i + 1].intValue());

			///dist = vp.toMapDistance((int) p.distance(p2));
			dist = vp.distanceWorld(p, p2);
			distAll += dist;
		}

		System.out.println("Distancia = " + dist + " Distancia Total = " +
			(distAll));
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return iruler;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PolylineListener#pointFixed(com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent)
	 */
	public void pointFixed(MeasureEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PolylineListener#polylineFinished(com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent)
	 */
	public void polylineFinished(MeasureEvent event) {
	}
}

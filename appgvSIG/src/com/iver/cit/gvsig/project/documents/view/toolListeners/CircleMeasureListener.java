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
package com.iver.cit.gvsig.project.documents.view.toolListeners;


import java.awt.Image;
import java.awt.geom.Point2D;
import java.text.NumberFormat;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.MeasureEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.CircleListener;

import com.iver.andami.PluginServices;


/**
 * <p>Listener for tools that measure using a circular area, displaying
 *   its radius at the status bar of the application's main frame.</p>
 *
 * @see CircleListener
 *
 * @author Laura
 */
public class CircleMeasureListener implements CircleListener {
	/**
	 * The image to display when the cursor is active.
	 */
	private final Image img = PluginServices.getIconTheme().get("cursor-query-distance").getImage();

	/**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
//	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(img,
//			new Point(16, 16), "");

	/**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
	private MapControl mapCtrl;

	/**
	 * <p>Creates a new listener for measure circular areas.</p>
	 *
	 * @param mc the <code>MapControl</code> object where the measures are made
	 */
	public CircleMeasureListener(MapControl mc) {
		this.mapCtrl = mc;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.CircleListener#circle(com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent)
	 */
	public void circle(MeasureEvent event) throws BehaviorException {
		double dist = 0;
		double distAll = 0;

		ViewPort vp = mapCtrl.getMapContext().getViewPort();

		for (int i = 0; i < (event.getXs().length - 1); i++) {
			dist = 0;

			Point2D p = new Point2D.Double(event.getXs()[i].doubleValue(),
					event.getYs()[i].doubleValue());
			Point2D p2 = new Point2D.Double(event.getXs()[i + 1].doubleValue(),
					event.getYs()[i + 1].doubleValue());

			///dist = vp.toMapDistance((int) p.distance(p2));
			dist = vp.distanceWorld(p, p2);
			distAll += dist;
		}
		//System.out.println("Distancia = " + dist + " Distancia Total = " +
		//	(distAll));
		NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        if (PluginServices.getMainFrame() != null)
        {
        	double[] trans2Meter=MapContext.getDistanceTrans2Meter();
            PluginServices.getMainFrame().getStatusBar().setMessage("4",
    			"Dist:" + nf.format(dist/trans2Meter[mapCtrl.getViewPort().getDistanceUnits()]) + "");
    		PluginServices.getMainFrame().getStatusBar().setMessage("5",
    			"Total:" + nf.format(distAll/trans2Meter[mapCtrl.getViewPort().getDistanceUnits()]) + "");
        }
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return img;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return false;
	}
}

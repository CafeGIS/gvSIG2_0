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
package com.iver.cit.gvsig.project.documents.view.toolListeners;

import java.awt.geom.Point2D;
import java.text.NumberFormat;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.MeasureListenerImpl;
import org.gvsig.fmap.mapcontrol.tools.Events.MeasureEvent;

import com.iver.andami.PluginServices;


/**
 * <p>Listener for calculating distances using vertexes of a polyline, defined in the associated {@link MapControl MapControl}
 *  object, and displaying that information at the status bar of the application's main frame.</p>
 *
 * <p>Calculates and displays:
 *  <ul>
 *   <li>Distance of the last segment of the polyline.</li>
 *   <li>Accumulated distance of all segments of the polyline.</li>
 *  </ul>
 * </p>
 *
 * @see MeasureListenerImpl
 *
 * @author Vicente Caballero Navarro
 */
public class MeasureListener extends MeasureListenerImpl {
	/**
	 * <p>Creates a new listener for calculating distances using points of a polyline.</p>
	 *
	 * @param mc the <code>MapControl</code> where is calculated the length
	 */
	public MeasureListener(MapControl mc) {
		super(mc);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.MeasureListenerImpl#points(com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent)
	 */
	public void points(MeasureEvent event) {
		double dist = 0;
		double distAll = 0;

		ViewPort vp = mapCtrl.getMapContext().getViewPort();

		Point2D p = new Point2D.Double(event.getXs()[0].doubleValue(),event.getYs()[0].doubleValue());//vp.toMapPoint(new Point(event.getXs()[0].intValue(),event.getYs()[0].intValue()));
		for (int i = 1; i < (event.getXs().length); i++) {
			Point2D p2 = new Point2D.Double(event.getXs()[i].doubleValue(),	event.getYs()[i].doubleValue());// vp.toMapPoint(new Point(event.getXs()[i].intValue(),event.getYs()[i].intValue()));
			dist = vp.distanceWorld(p, p2);
			distAll += dist;
			p = p2;
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
}

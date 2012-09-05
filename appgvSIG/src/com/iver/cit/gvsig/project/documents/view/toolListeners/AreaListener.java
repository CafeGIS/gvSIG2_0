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
import org.gvsig.fmap.mapcontrol.tools.AreaListenerImpl;
import org.gvsig.fmap.mapcontrol.tools.Events.MeasureEvent;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.MainFrame;


/**
 * <p>Listener for calculating the area of a polygon, defined in the associated {@link MapControl MapControl}
 *  object.</p>
 * <p>Moves the extent of the associated {@link MapControl MapControl} object
 *  according the movement between the initial and final points of the line determined by the movement
 *  dragged with the third button of the mouse.</p>
 *
 * <p>Updates the status bar of the main frame of the application with the current area.</p>
 *
 * @see AreaListenerImpl
 *
 * @author Vicente Caballero Navarro
 */
public class AreaListener extends AreaListenerImpl {
	/**
 	 * <p>Creates a new listener for calculating the area of a polygon and displaying it at the status bar
 	 *  of the main frame of the application.</p>
	 *
	 * @param mc the <code>MapControl</code> where is calculated the area
	 */
    public AreaListener(MapControl mc) {
    	super(mc);
    }

    /*
     * (non-Javadoc)
     * @see com.iver.cit.gvsig.fmap.tools.AreaListenerImpl#points(com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent)
     */
    public void points(MeasureEvent event) {
        this.event = event;

        double dist = 0;
        double distAll = 0;

        ViewPort vp = mapCtrl.getMapContext().getViewPort();
        for (int i = 0; i < (event.getXs().length - 1); i++) {
            dist = 0;

            Point2D p = new Point2D.Double(event.getXs()[i].doubleValue(), event.getYs()[i].doubleValue());//vp.toMapPoint(new Point(event.getXs()[i].intValue(), event.getYs()[i].intValue()));
            Point2D p2 = new Point2D.Double(event.getXs()[i + 1].doubleValue(), event.getYs()[i + 1].doubleValue());//vp.toMapPoint(new Point(event.getXs()[i + 1].intValue(), event.getYs()[i + 1].intValue()));
            ///dist = vp.toMapDistance((int) p.distance(p2));
            dist = vp.distanceWorld(p,p2);
//            System.out.println("distancia parcial = "+dist);
            distAll += dist;
        }

       // System.out.println("Perímetro = " + distAll + " Área = " +
       //     (returnArea(vp.toMapPoint(
       //             new Point2D.Double(
       //                 event.getXs()[event.getXs().length - 2].doubleValue(),
       //                 event.getYs()[event.getYs().length - 2].doubleValue())))));
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        MainFrame mF = PluginServices.getMainFrame();
        if (mF != null)
        {
        	int distanceUnits=mapCtrl.getViewPort().getDistanceUnits();
        	int distanceArea=mapCtrl.getViewPort().getDistanceArea();
            mF.getStatusBar().setMessage("4",
        			"P:" + nf.format(distAll/MapContext.getDistanceTrans2Meter()[distanceUnits]) + "");
        		mF.getStatusBar().setMessage("5",
        			///"A:" + nf.format(returnArea(vp.toMapPoint(
                    ///        new Point2D.Double(
                    ///                event.getXs()[event.getXs().length - 2].doubleValue(),
                    ///                event.getYs()[event.getYs().length - 2].doubleValue())))/FMap.CHANGEM[mapCtrl.getViewPort().getDistanceUnits()]) + "");
        				"A:" + nf.format(returnArea(
                                new Point2D.Double(
                                        event.getXs()[event.getXs().length - 2].doubleValue(),
                                        event.getYs()[event.getYs().length - 2].doubleValue()))/Math.pow(MapContext.getAreaTrans2Meter()[distanceArea],2)) + "");
        		mF.getStatusBar().setMessage("distancearea",MapContext.getAreaAbbr()[distanceArea]);
        }
    }
}

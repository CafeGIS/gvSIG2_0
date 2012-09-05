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
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Events.MeasureEvent;
import org.gvsig.fmap.mapcontrol.tools.Listeners.PolylineListener;
import org.gvsig.fmap.mapcontrol.tools.geo.Geo;




/**
 * <p>Listener for calculating the area of a polygon, defined in the associated {@link MapControl MapControl}
 *  object.</p>
 *
 * <p>If the view port of the associated <code>MapControl</code> isn't projected gets the area according the
 *  geographical coordinates.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class AreaListenerImpl implements PolylineListener {
	/**
	 * The image to display when the cursor is active.
	 */
	private final Image iarea = new ImageIcon(MapControl.class.getResource(
				"images/AreaCursor.gif")).getImage();

	/**
	 * The cursor used to work with this tool listener.
	 *
	 * @see #getCursor()
	 */
//	private Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(iarea,
//			new Point(16, 16), "");

	/**
	 * Reference to the <code>MapControl</code> object that uses.
	 */
	protected MapControl mapCtrl;

	/**
	 * Information about all vertexes and {@link GeneralPathX GeneralPathX}s of the polyline.
	 */
	protected MeasureEvent event;

	/**
 	 * <p>Creates a new listener for calculating the area of a polygon.</p>
	 *
	 * @param mc the <code>MapControl</code> where is calculated the area
	 */
	public AreaListenerImpl(MapControl mc) {
		this.mapCtrl = mc;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PolylineListener#points(com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent)
	 */
	public void points(MeasureEvent event) {
		this.event = event;

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

		System.out.println("Perímetro = " + distAll + " Área = " +
			(returnArea(vp.toMapPoint(
					new Point2D.Double(
						event.getXs()[event.getXs().length - 2].doubleValue(),
						event.getYs()[event.getYs().length - 2].doubleValue())))));
	}

	/**
	 * <p>Returns the area of the polygon, using {@link #returnCoordsArea(Double[], Double[], Point2D) returnCoordsArea}
	 *  if the <code>ViewPort</code> of the associated <code>MapControl</code> is projected, or using
	 *  {@link #returnGeoCArea(Double[], Double[], Point2D) returnGeoCArea} if isn't.</p>
	 *
	 * @param point unused parameter
	 *
	 * @return area from the vertexes stored at the measure event in real coordinates, or, if the <code>MapControl</code>
	 *  isn't projected, in geographical coordinates
	 *
	 * @see #returnCoordsArea(Double[], Double[], Point2D)
	 * @see #returnGeoCArea(Double[], Double[], Point2D)
	 */
	protected double returnArea(Point2D point) {
		Double[] xs=event.getXs();
		Double[] ys=event.getYs();
		if (mapCtrl.getProjection().isProjected()) {
			return returnCoordsArea(xs,ys,point);
		}
		return returnGeoCArea(xs,ys,point);
	}

	/**
	 * <p>Returns the area of the polygon using <i>point</i> as initial, in
	 *  real values with the current measure unit, according the projection in the <code>ViewPort</code>
	 *  of the <code>MapControl</code>.</p>
	 *
	 * @param xs abscissa coordinate of all vertexes of the polygon
	 * @param ys ordinate coordinate of all vertexes of the polygon
	 * @param point point 2D used as first vertex in the calculation of the area
	 *
	 * @return the area of the polygon
	 */
	public double returnCoordsArea(Double[] xs,Double[] ys, Point2D point) {
		Point2D aux=point;
		double elArea = 0.0;
		Point2D pPixel;
		Point2D p = new Point2D.Double();
		Point2D.Double pAnt = new Point2D.Double();
		ViewPort vp = mapCtrl.getMapContext().getViewPort();
		for (int pos = 0; pos < xs.length-1; pos++) {
			pPixel = new Point2D.Double(xs[pos].doubleValue(),
					ys[pos].doubleValue());
			p = pPixel;//vp.toMapPoint(pPixel);
			if (pos == 0) {
				pAnt.x = aux.getX();
				pAnt.y = aux.getY();
			}
			elArea = elArea + ((pAnt.x - p.getX()) * (pAnt.y + p.getY()));
			pAnt.setLocation(p);
		}

		elArea = elArea + ((pAnt.x - aux.getX()) * (pAnt.y + aux.getY()));
		elArea = Math.abs(elArea / 2.0);
		return (elArea*(Math.pow(MapContext.getDistanceTrans2Meter()[vp.getMapUnits()],2)));
	}
	public static void main(String[] args) {
		IProjection projectionUTM = CRSFactory.getCRS("EPSG:23030");
		ViewPort vpUTM = new ViewPort(projectionUTM);
		MapControl mcUTM=new MapControl();
		mcUTM.setMapContext(new MapContext(vpUTM));
		AreaListenerImpl areaListenerUTM=new AreaListenerImpl(mcUTM);
		IProjection projectionGeo = CRSFactory.getCRS("EPSG:4230");
		ViewPort vpGeo = new ViewPort(projectionGeo);
		MapControl mcGeo=new MapControl();
		mcGeo.setMapContext(new MapContext(vpGeo));
		AreaListenerImpl areaListenerGeo=new AreaListenerImpl(mcGeo);

		Double[] xsUTMCaseta=new Double[] {new Double(547508.77),new Double(547517.73),new Double(547512.65)};
		Double[] ysUTMCaseta=new Double[] {new Double(4704333.97),new Double(4704331.3),new Double(4704315.2)};
		double areaUTMCaseta=areaListenerUTM.returnCoordsArea(xsUTMCaseta,ysUTMCaseta,new Point2D.Double(547512.65,4704315.2));
		Double[] xsGeoCaseta=new Double[] {new Double(-2.42192383),new Double(-2.42181545),new Double(-2.42187771)};
		Double[] ysGeoCaseta=new Double[] {new Double(42.48914909),new Double(42.48912295),new Double(42.48897922)};
		double areaGeoCCaseta=areaListenerGeo.returnGeoCArea(xsGeoCaseta,ysGeoCaseta,new Point2D.Double(-2.42187771,42.48897922));

		System.out.println("AreaUTMCaseta = "+ areaUTMCaseta);
		System.out.println("AreaGeoCCaseta = "+ areaGeoCCaseta);


		Double[] xsUTM=new Double[] {new Double(731292),new Double(731901),new Double(730138)};
		Double[] ysUTM=new Double[] {new Double(4351223),new Double(4350768),new Double(4349232)};
		double areaUTM=areaListenerUTM.returnCoordsArea(xsUTM,ysUTM,new Point2D.Double(730138,4349232));
		Double[] xsGeo=new Double[] {new Double(-0.31888183),new Double(-0.31173131),new Double(-0.33268401)};
		Double[] ysGeo=new Double[] {new Double(39.27871741),new Double(39.27464327),new Double(39.26117368)};
		double areaGeoC=areaListenerGeo.returnGeoCArea(xsGeo,ysGeo,new Point2D.Double(-0.33268401,39.26117368));

		System.out.println("AreaUTM = "+ areaUTM);
		System.out.println("AreaGeoC = "+ areaGeoC);

		Double[] xsUTMspain=new Double[] {new Double(-12806),new Double(1025790),new Double(-31353.14)};
		Double[] ysUTMspain=new Double[] {new Double(4793276.43),new Double(4719090.94),new Double(4125607.02)};
		double areaUTMspain=areaListenerUTM.returnCoordsArea(xsUTMspain,ysUTMspain,new Point2D.Double(730138,4349232));
		Double[] xsGeospain=new Double[] {new Double(-9.22743872),new Double(3.33087936),new Double(-9.01458587),new Double(-9.22743872)};
		Double[] ysGeospain=new Double[] {new Double(43.02384666),new Double(42.38528811),new Double(37.06396689),new Double(43.02384666)};
		double areaGeospainC=areaListenerGeo.returnGeoCArea(xsGeospain,ysGeospain,new Point2D.Double(-9.01458587,37.06396689));

		System.out.println("AreaUTMSpain = "+ areaUTMspain);
		System.out.println("AreaGeoSpainC = "+ areaGeospainC);
	}

	/**
	 * <p>Returns the area in geographical coordinates of the polygon, according the
	 *  <a href="http://en.wikipedia.org/wiki/Haversine_formula">Haversine function</a>.</p>
	 *
	 * @see Geo#sphericalPolyArea(double[], double[], int)
	 */
	public double returnGeoCArea(Double[] xs, Double[] ys, Point2D point) {
		double[] lat = new double[xs.length];
		double[] lon = new double[xs.length];
		for (int K = 0; K < xs.length; K++) {
			lon[K] = xs[K].doubleValue() / Geo.Degree;
			lat[K] = ys[K].doubleValue() / Geo.Degree;
		}
		return (Geo.sphericalPolyArea(lat, lon, xs.length - 1) * Geo.SqM);// /1.29132441;//Esto
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getImageCursor()
	 */
	public Image getImageCursor() {
		return iarea;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PolylineListener#pointFixed(com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent)
	 */
	public void pointFixed(MeasureEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.PolylineListener#polylineFinished(com.iver.cit.gvsig.fmap.tools.Events.MeasureEvent)
	 */
	public void polylineFinished(MeasureEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return false;
	}
}

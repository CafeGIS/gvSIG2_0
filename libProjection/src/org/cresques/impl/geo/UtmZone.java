/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
 * cresques@gmail.com
 */
package org.cresques.impl.geo;

import geojava.GeoUtm;
import geojava.Ra2De;
import geojava.UtmGeo;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IDatum;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;

import org.cresques.px.Extent;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class UtmZone extends Projection {
    static String name = "UTM";
    static String abrev = "UTM";
    private final static UtmZone ed5028N = new UtmZone(Ellipsoid.ed50, 28, 0,
                                                       0.0);
    private final static UtmZone ed5029N = new UtmZone(Ellipsoid.ed50, 29, 0,
                                                       0.0);
    private final static UtmZone ed5030N = new UtmZone(Ellipsoid.ed50, 30, 0,
                                                       0.0);
    private final static UtmZone ed5031N = new UtmZone(Ellipsoid.ed50, 31, 0,
                                                       0.0);
    private final static UtmZone hayford28N = new UtmZone(Ellipsoid.hayford,
                                                          28, 0, 0.0);
    private final static UtmZone hayford29N = new UtmZone(Ellipsoid.hayford,
                                                          29, 0, 0.0);
    private final static UtmZone hayford30N = new UtmZone(Ellipsoid.hayford,
                                                          30, 0, 0.0);
    private final static UtmZone hayford31N = new UtmZone(Ellipsoid.hayford,
                                                          31, 0, 0.0);
    static GeoUtm geoutm = new GeoUtm();
    static UtmGeo utmgeo = new UtmGeo();
    public int Zone = 30;
    public int Hemisphere = 0;
    public double H = 0.0;

    public UtmZone(Ellipsoid eli, int zone, int hemisphere, double h) {
        super(eli);
        Zone = zone;
        Hemisphere = hemisphere;
        H = h;
        grid = new Graticule(this);
    }

    public String getAbrev() {
        return abrev + Zone;
    }

    public static UtmZone getProjection(Ellipsoid eli, int zone, int hemisphere) {
        if ((eli == Ellipsoid.hayford) && (hemisphere == 0)) {
            switch (zone) {
            case 28:
                return hayford28N;

            case 29:
                return hayford29N;

            case 30:
                return hayford30N;

            case 31:
                return hayford31N;
            }
        } else if ((eli == Ellipsoid.ed50) && (hemisphere == 0)) {
            switch (zone) {
            case 28:
                return ed5028N;

            case 29:
                return ed5029N;

            case 30:
                return ed5030N;

            case 31:
                return ed5031N;
            }
        }

        System.err.println("UtmZone.getProjection(): new " + zone);

        return new UtmZone(eli, zone, hemisphere, 0.0);
    }

    /**
     *
     */
    public static IProjection getProjectionByName(IDatum eli, String name) {
        int zone;
        int hemisphere = NORTH;

        if (name.indexOf("UTM") < 0) {
            return null;
        }

        if (name.substring(0, 1).compareTo("S") == 0) {
            hemisphere = SOUTH;
        }

        zone = Integer.parseInt(name.substring(name.length() - 2));

        return getProjection((Ellipsoid) eli, zone, hemisphere);
    }

    /**
     *
     */
    public Point2D createPoint(double x, double y) {
        return new UtmPoint(this, x, y);
    }

    /**
     *
     * @param uPt
     * @return
     */
    public Point2D toGeo(Point2D uPt) {
        GeoPoint gPt = new GeoPoint();

        return toGeo((UtmPoint) uPt, gPt);
    }

    /**
     *
     * @param uPt
     * @param gPt
     * @return
     */
    public GeoPoint toGeo(UtmPoint uPt, GeoPoint gPt) {
        int[] ai = new int[3];
        double[] ad = new double[3];
        ai[1] = ((UtmZone) uPt.proj).Zone;
        ai[2] = ((UtmZone) uPt.proj).Hemisphere;
        ad[1] = uPt.X;
        ad[2] = uPt.Y;

        utmgeo.set(((Projection) uPt.proj).getElliPar(), ai, ad);
        utmgeo.go();

        gPt.Longitude = Ra2De.go(utmgeo.Ge[1]);
        gPt.Latitude = Ra2De.go(utmgeo.Ge[2]);
        gPt.proj = Geodetic.getProjection(((Projection) uPt.proj).eli);

        return gPt;
    }

    /**
     *
     * @param gPt
     * @param uPt
     * @return
     */
    public Point2D fromGeo(Point2D gPt, Point2D uPt) {
        int[] ai = { 0, 0, 2 };

        return fromGeo((GeoPoint) gPt, (UtmPoint) uPt, ai);
    }

    /**
     *
     * @param gPt
     * @param uPt
     * @param proj
     * @return
     */
    public UtmPoint fromGeo(GeoPoint gPt, UtmPoint uPt, UtmZone proj) {
        int[] ai = { 0, proj.Zone, proj.Hemisphere };

        return fromGeo(gPt, uPt, ai);
    }

    /**
     *
     * @param gPt
     * @param uPt
     * @param ai
     * @return
     */
    public UtmPoint fromGeo(GeoPoint gPt, UtmPoint uPt, int[] ai) {
        double[] ad = new double[4];
        ad[1] = gPt.Longitude.ToRadians();
        ad[2] = gPt.Latitude.ToRadians();
        geoutm.set(((Projection) gPt.proj).getElliPar(), ai, ad);
        geoutm.go();

        if (geoutm.Ier != 0) {
            return null;
        }

        uPt.setLocation(geoutm.Pr[1], geoutm.Pr[2]);

        if ((((UtmZone) uPt.proj).Zone != geoutm.Iopar[1]) ||
                (((UtmZone) uPt.proj).Hemisphere != geoutm.Iopar[2])) {
            uPt.proj = UtmZone.getProjection(((Projection) uPt.proj).eli,
                                             geoutm.Iopar[1], geoutm.Iopar[2]);
        }

        return uPt;
    }

    // Calcula el step en función del zoom
    private void generateGrid(Graphics2D g, Extent extent, AffineTransform mat) {
        // calculo del step en función del zoom
        Point2D pt1 = extent.getMin();

        double step = 1.0;
        double x = (int) pt1.getX();
        double dist = 0.0;

        /*        GeoPoint gp1, gp2;
                gp1 = (GeoPoint) createPoint( x, (int) pt1.getY());
                mat.transform(gp1, gp1);
                gp2 = (GeoPoint) createPoint(gp1.getX()+100, gp1.getY()-100);
                try {
                        mat.inverseTransform(gp2, gp2);
                } catch (NoninvertibleTransformException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                dist = (gp2.getX()-x);
                System.err.println("distX = " + dist);

                if (dist > 30.0) {                         step = 30.0;
                } else if (dist > 18.0) {         step = 18.0;
                } else if (dist > 12.0) {        step = 12.0;
                } else if (dist > 6.0) {        step = 6.0;
                } else if (dist > 3.0) {        step = 3.0;
                } else if (dist > 2.0) {        step = 2.0;
                } else if (dist > 1.0) {        step = 1.0;
                } else if (dist > .5) {                step =.5;
                } else if (dist > .25) {        step =.25;
                } else if (dist > 1.0/60*5.0) { step = 1.0/60*5.0;
                } else {                                        step = 1.0/60*2.0;
                }
                        //step = 1.0;
                */
        generateGrid(g, extent, mat, step);
    }

    private void generateGrid(Graphics2D g, Extent extent, AffineTransform mat,
                              double step) {
        grid = new Graticule(this);

        Point2D pt1 = extent.getMin();
        Point2D pt2 = extent.getMax();
        GeoPoint gp1;
        GeoPoint gp2;
        UtmPoint up1 = (UtmPoint) createPoint(0, 0);
        UtmPoint up2 = (UtmPoint) createPoint(0, 0);
        Point2D.Double mp1 = new Point2D.Double(0.0, 0.0);
        Point2D.Double mp2 = new Point2D.Double(0.0, 0.0);
        Geodetic geoProj = Geodetic.getProjection((Ellipsoid) getDatum());
        boolean esUso = false;

        System.err.println(name + ": ViewPort Extent = (" + pt1 + "," + pt2 +
                           ")");
        gp1 = (GeoPoint) toGeo(new UtmPoint(pt1.getX(), pt2.getY()));
        gp2 = (GeoPoint) toGeo(new UtmPoint(pt2));

        double xMin = (int) gp1.getX() - 1.0;
        double xMax = (int) gp2.getX() + 1.0;
        gp1 = (GeoPoint) toGeo(new UtmPoint(pt1.getX() +
                                            ((pt2.getX() - pt1.getX()) / 2.0),
                                            pt2.getY()));

        double yMax = (int) gp1.getY() + 2.0;
        gp1 = (GeoPoint) toGeo(new UtmPoint(pt1));

        double yMin = (int) gp1.getY() - 1.0;
        xMin = -30.0;
        xMax = 30.0;
        yMin = 20.0;
        yMax = 60.0;

        for (double x = xMin; x <= xMax; x += step) {
            up1 = null;

            if (Math.abs((x) % 6) < .001) {
                esUso = true;
            }

            for (double y = yMin; y <= yMax; y += step) {
                gp2 = (GeoPoint) geoProj.createPoint(x, y);
                fromGeo(gp2, up2, this);

                if ((up1 != null) && (extent.isAt(up1) || extent.isAt(up2))) {
                    mat.transform(up2, mp2);

                    if (up1 != null) {
                        if (esUso) {
                            grid.addLine(mp1, mp2, 1);
                        } else {
                            grid.addLine(mp1, mp2);
                        }
                    }
                } else {
                    mat.transform(up2, mp2);
                }

                up1 = (UtmPoint) up2.clone();
                mp1 = (Point2D.Double) mp2.clone();
            }

            esUso = false;
        }

        for (double y = yMin; y <= yMax; y += step) {
            up1 = null;

            for (double x = xMin; x <= xMax; x += step) {
                gp2 = (GeoPoint) geoProj.createPoint(x, y);
                fromGeo(gp2, up2, this);

                if ((up1 != null) && (extent.isAt(up1) || extent.isAt(up2))) {
                    mat.transform(up2, mp2);

                    if (up1 != null) {
                        grid.addLine(mp1, mp2);
                    }
                } else {
                    mat.transform(up2, mp2);
                }

                up1 = (UtmPoint) up2.clone();
                mp1 = (Point2D.Double) mp2.clone();
            }
        }
    }

    public void drawGrid(Graphics2D g, ViewPortData vp) {
        generateGrid(g, vp.getExtent(), vp.getMat());
        grid.setColor(gridColor);
        grid.draw(g, vp);
    }

    /* (non-Javadoc)
     * @see org.cresques.cts.IProjection#getScale(double, double, double, double)
     */
    public double getScale(double minX, double maxX, double width, double dpi) {
        double scale = ((maxX - minX) * // metros
                       (dpi / 2.54 * 100.0)) / // px / metro
                       width; // pixels

        return scale;
    }

	public ICoordTrans getCT(IProjection dest) {
		// TODO Auto-generated method stub
		return null;
	}

	public Rectangle2D getExtent(Rectangle2D extent, double scale, double wImage, double hImage, double mapUnits,double distanceUnits, double dpi) {
		double w =0;
		double h =0;
		double wExtent =0;
		double hExtent =0;
   		w = ((wImage / dpi) * 2.54);
		h = ((hImage / dpi) * 2.54);
		wExtent =w * scale * distanceUnits / mapUnits;
		hExtent =h * scale * distanceUnits / mapUnits;
		double xExtent = extent.getCenterX() - wExtent/2;
		double yExtent = extent.getCenterY() - hExtent/2;
		Rectangle2D rec=new Rectangle2D.Double(xExtent,yExtent,wExtent,hExtent);
    	return  rec;
	}

	/* (non-Javadoc)
	 * @see org.cresques.cts.IProjection#getFullCode()
	 */
	public String getFullCode() {
		return getAbrev();
	}
}

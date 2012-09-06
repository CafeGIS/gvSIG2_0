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

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IDatum;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;

import org.cresques.px.Extent;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.TreeMap;


/**
 * Proyeccion Mercator
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>* @author administrador
 */
public class Mercator extends Projection {
    static String name = "Mercator";
    static String abrev = "Mer";
    private static TreeMap projPool = new TreeMap();
    public static final Mercator hayford = new Mercator(Ellipsoid.hayford);
    public static final Mercator wgs84 = new Mercator(Ellipsoid.wgs84);
    private double a;
    private double f;
    private double b;
    private double Eps2;
    private double EE2;
    private double EE3;
    private double Epps2;

    public Mercator(Ellipsoid eli) {
        super(eli);
        grid = new Graticule(this);

        double[] p = eli.getParam();
        a = p[1];
        f = 1 / p[2];
        b = p[3];

        Eps2 = p[5];
        EE2 = Eps2 * Eps2;
        EE3 = EE2 * Eps2;
        Epps2 = p[7];
    }

    public String getAbrev() {
        return abrev;
    }

    public static Mercator getProjection(Ellipsoid eli) {
        Mercator ret = null;

        if (projPool.containsKey(eli.getName())) {
            ret = (Mercator) Mercator.projPool.get(eli.getName());
        } else {
            if (eli == Ellipsoid.hayford) {
                ret = hayford;
            } else if (eli == Ellipsoid.wgs84) {
                ret = wgs84;
            } else {
                ret = new Mercator(eli);
            }

            projPool.put(eli.getName(), ret);
        }

        return ret;
    }

    /**
     *
     */
    public static IProjection getProjectionByName(IDatum eli, String name) {
        if (name.indexOf("ME") < 0) {
            return null;
        }

        return getProjection((Ellipsoid) eli);
    }

    /**
     *
     */
    public Point2D createPoint(double x, double y) {
        return new ProjPoint(this, x, y);
    }

    /**
     *
     * @param uPt
     * @return
     */
    public Point2D toGeo(Point2D mPt) {
        GeoPoint gPt = new GeoPoint();

        return toGeo((ProjPoint) mPt, gPt);
    }

    /**
     *
     * @param mPt
     * @param gPt
     * @return
     */
    public GeoPoint toGeo(ProjPoint mPt, GeoPoint gPt) {
        double t = Math.pow(Math.E, (-mPt.getY() / a));

        double x1;
        double x = ((Math.PI / 2) - (2 * Math.atan(t)));

        do {
            x1 = x;
            x = (Math.PI / 2) -
                (2 * Math.atan(t * (Math.pow((1 -
                                             (Math.sqrt(Eps2) * Math.sin(x))) / (1 +
                                             (Math.sqrt(Eps2) * Math.sin(x))),
                                             (Math.sqrt(Eps2) / 2)))));
        } while ((x - x1) > 0.0000000001);

        double lat = (Math.PI / 2) -
                     (2 * Math.atan(t * (Math.pow((1 -
                                                  (Math.sqrt(Eps2) * Math.sin(x))) / (1 +
                                                  (Math.sqrt(Eps2) * Math.sin(x))),
                                                  (Math.sqrt(Eps2) / 2)))));

        double lng = mPt.getX() / a;
        gPt.setLocation((lng * 180.0) / Math.PI, (lat * 180.0) / Math.PI);
        gPt.proj = Geodetic.getProjection(((Projection) mPt.proj).eli);

        return gPt;
    }

    /**
     *
     * @param gPt
     * @param uPt
     * @return
     */
    public Point2D fromGeo(Point2D gPt, Point2D mPt) {
        double sl = Math.sin(((GeoPoint) gPt).Latitude.ToRadians());
        double cl = Math.cos(((GeoPoint) gPt).Latitude.ToRadians());
        double tl = (1 + sl) / (1 - sl);

        // Calcula Easting
        double x = a * ((GeoPoint) gPt).Longitude.ToRadians();

        // Calcula Northing
        double y = Math.pow(((1 - (Math.sqrt(Eps2) * sl)) / (1 +
                            (Math.sqrt(Eps2) * sl))), (Math.sqrt(Eps2)));
        y = a / 2 * (Math.log(tl * y));
        ((ProjPoint) mPt).setLocation(x, y);
        ((ProjPoint) mPt).proj = this;

        return mPt;
    }

    // Calcula el step en función del zoom
    private void generateGrid(Graphics2D g, Extent extent, AffineTransform mat) {
        // calculo del step en función del zoom
        Point2D pt1 = extent.getMin();

        double step = 3.0;
        double x = pt1.getX();
        double dist = 0.0;
        ProjPoint ppt1;
        ProjPoint ppt2;
        GeoPoint gp1;
        GeoPoint gp2;
        ppt1 = (ProjPoint) createPoint(x, pt1.getY());
        ppt2 = (ProjPoint) createPoint(x + 100, pt1.getY() - 100);
        gp1 = (GeoPoint) ppt1.toGeo();
        gp2 = (GeoPoint) ppt2.toGeo();

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
        Point2D.Double ptx = new Point2D.Double(0.0, 0.0);
        GeoPoint gp1;
        GeoPoint gp2;
        ProjPoint up1 = (ProjPoint) createPoint(0, 0);
        ProjPoint up2 = (ProjPoint) createPoint(0, 0);
        Geodetic geoProj = Geodetic.getProjection((Ellipsoid) getDatum());
        double xAxis;
        double yAxis;

        // Calculos para el texto
        FontMetrics fm = g.getFontMetrics();
        int fmWidth = 0;
        int fmHeight = fm.getAscent();
        String tit = "";
        String fmt = "%Gº%N";

        if (step < 1.0) {
            fmt = "%Gº%M'%N";
        }

        // Lineas Horzontales
        gp1 = (GeoPoint) toGeo(new ProjPoint(pt1));
        gp2 = (GeoPoint) toGeo(new ProjPoint(pt2));
        xAxis = gp1.getX();
        yAxis = gp2.getY();
        System.err.println(name + ": ViewPort Extent = (" + gp1 + "," + gp2 +
                           ")");

        double xMin = (int) gp1.getX() - 1.0;
        xMin -= (xMin % step);

        double xMax = (int) gp2.getX() + 1.0;
        double yMin = (int) gp1.getY() - 1.0;
        yMin -= (yMin % step);

        double yMax = (int) gp2.getY() + 1.0;

        if (xMin < -180.0) {
            xMin = -180.0;
        }

        if (xMax > 180.0) {
            xMax = 180.0;
        }

        if (yMin < -80.0) {
            yMin = -80.0;
        }

        if (yMax > 80.0) {
            yMax = 80.0;
        }

        if (xAxis < -180.0) {
            xAxis = -180.0;
        }

        if (yAxis > 80.0) {
            yAxis = 80.0;
        }

        for (double y = yMin; y <= yMax; y += step) {
            gp1 = (GeoPoint) geoProj.createPoint(xAxis, y);
            gp2 = (GeoPoint) geoProj.createPoint(xMax, y);
            fromGeo(gp1, up1);
            fromGeo(gp2, up2);
            mat.transform(up1, up1);
            mat.transform(up2, up2);
            grid.addLine(up1, up2);

            tit = coordToString(y, fmt, true);

            //fmWidth = fm.stringWidth(tit);
            ptx.setLocation(up1.getX() + 3, up1.getY() - 2);
            grid.addText(tit, ptx);
        }

        // Lineas Verticales
        for (double x = xMin; x <= xMax; x += step) {
            gp1 = (GeoPoint) geoProj.createPoint(x, yMin);
            gp2 = (GeoPoint) geoProj.createPoint(x, yAxis);
            fromGeo(gp1, up1);
            fromGeo(gp2, up2);
            mat.transform(up1, up1);
            mat.transform(up2, up2);
            grid.addLine(up1, up2);

            tit = coordToString(x, fmt, false);

            //fmWidth = fm.stringWidth(tit);
            ptx.setLocation(up2.getX() + 3, up2.getY() + fmHeight);
            grid.addText(tit, ptx);
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
        Projection prj = Geodetic.getProjection((Ellipsoid) getDatum());
        GeoPoint pt1 = (GeoPoint) prj.createPoint(1.0, 0.0);
        GeoPoint pt2 = (GeoPoint) prj.createPoint(2.0, 0.0);
        ProjPoint ppt1 = (ProjPoint) createPoint(0.0, 0.0);
        ProjPoint ppt2 = (ProjPoint) createPoint(0.0, 0.0);
        fromGeo(pt1, ppt1);
        fromGeo(pt2, ppt2);

        //scale = ppt2.getX()-ppt1.getX();
        double scale = (((maxX - minX) / (ppt2.getX() - ppt1.getX())) *
        //scale = ((extent.maxX()-extent.minX())/ getWidth());// *
        (dpi / 2.54 * 100.0 * 1852.0 * 60.0)) / width;

        return scale;
    }

	public ICoordTrans getCT(IProjection dest) {
		// TODO Auto-generated method stub
		return null;
	}

	public Rectangle2D getExtent(Rectangle2D extent, double scale, double wImage, double hImage, double mapUnits,double distanceUnits, double dpi) {
		Projection prj = Geodetic.getProjection((Ellipsoid) getDatum());
        GeoPoint pt1 = (GeoPoint) prj.createPoint(1.0, 0.0);
        GeoPoint pt2 = (GeoPoint) prj.createPoint(2.0, 0.0);
        ProjPoint ppt1 = (ProjPoint) createPoint(0.0, 0.0);
        ProjPoint ppt2 = (ProjPoint) createPoint(0.0, 0.0);
        fromGeo(pt1, ppt1);
        fromGeo(pt2, ppt2);
		double w =0;
		double h =0;
		double wExtent =0;
		double hExtent =0;
    	w = ((wImage / dpi) * 2.54);
		h = ((hImage / dpi) * 2.54);
		wExtent =((w*scale*distanceUnits)/ (ppt2.getX() - ppt1.getX()))/ (mapUnits*1852.0*60.0);
		hExtent =((h*scale*distanceUnits)/ (ppt2.getX() - ppt1.getX()))/ (mapUnits*1852.0*60.0);
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

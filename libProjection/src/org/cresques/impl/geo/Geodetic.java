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
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.TreeMap;


public class Geodetic extends Projection {
    private static TreeMap projList = new TreeMap();
    static String name = "Geodesica";
    static String abrev = "Geo";
    public static Geodetic hayford = new Geodetic(Ellipsoid.hayford);

    public Geodetic() {
        super();
        grid = new Graticule(this);
    }

    public Geodetic(Ellipsoid eli) {
        super(eli);
        grid = new Graticule(this);
    }

    public String getAbrev() {
        return abrev;
    }

    /**
     *
     */
    public static IProjection getProjectionByName(IDatum eli, String name) {
        if (name.indexOf("GEO") < 0) {
            return null;
        }

        if (name.indexOf("WGS84") >= 0) {
            return getProjection(Ellipsoid.wgs84);
        }

        if (name.indexOf("ED50") >= 0) {
            return getProjection(Ellipsoid.wgs84);
        }

        return null;
    }

    public static Geodetic getProjection(Ellipsoid eli) {
        Geodetic ret = null;
        String key = eli.toString();

        if (Geodetic.projList.containsKey(key)) {
            ret = (Geodetic) Geodetic.projList.get(key);
        } else {
            if (eli == Ellipsoid.hayford) {
                ret = hayford;
            } else {
                ret = new Geodetic(eli);
            }

            Geodetic.projList.put(key, ret);
        }

        return ret;
    }

    public Point2D createPoint(double x, double y) {
        return new GeoPoint(this, x, y);
    }

    /**
     *
     * @param gPt Punto para pasar a GeoPoint
     * @return
     */
    public Point2D toGeo(Point2D gPt) {
        return (GeoPoint) gPt;
    }

    public Point2D fromGeo(Point2D gPt, Point2D pPt) {
        return gPt;
    }

    // Calcula el step en función del zoom
    private void generateGrid(Graphics2D g, Extent extent, AffineTransform mat) {
        // calculo del step en función del zoom
        Point2D pt1 = extent.getMin();

        double step = 1.0;
        double x = (int) pt1.getX();
        double dist = 0.0;
        GeoPoint gp1;
        GeoPoint gp2;
        gp1 = (GeoPoint) createPoint(x, (int) pt1.getY());
        mat.transform(gp1, gp1);
        gp2 = (GeoPoint) createPoint(gp1.getX() + 100, gp1.getY() - 100);

        try {
            mat.inverseTransform(gp2, gp2);
        } catch (NoninvertibleTransformException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        dist = (gp2.getX() - x);
        System.err.println("distX = " + dist);

        if (dist > 30.0) {
            step = 30.0;
        } else if (dist > 15.0) {
            step = 15.0;
        } else if (dist > 10.0) {
            step = 10.0;
        } else if (dist > 5.0) {
            step = 5.0;
        } else if (dist > 3.0) {
            step = 3.0;
        } else if (dist > 2.0) {
            step = 2.0;
        } else if (dist > 1.0) {
            step = 1.0;
        } else if (dist > .5) {
            step = .5;
        } else if (dist > .25) {
            step = .25;
        } else if (dist > (1.0 / 60 * 5.0)) {
            step = 1.0 / 60 * 5.0;
        } else {
            step = 1.0 / 60 * 2.0;
        }

        //step = 1.0;
        generateGrid(g, extent, mat, step);
    }

    private void generateGrid(Graphics2D g, Extent extent, AffineTransform mat,
                              double step) {
        grid = new Graticule(this);

        GeoPoint gp1;
        GeoPoint gp2;
        Point2D.Double ptx = new Point2D.Double(0.0, 0.0);

        Point2D pt1 = extent.getMin();
        Point2D pt2 = extent.getMax();
        System.err.println(name + ": ViewPort Extent = (" + pt1 + "," + pt2 +
                           ")");

        // Calculos para el texto
        FontMetrics fm = g.getFontMetrics();
        int fmWidth = 0;
        int fmHeight = fm.getAscent();
        String tit = "";
        String fmt = "%Gº%N";

        if (step < 1.0) {
            fmt = "%Gº%M'%N";
        }

        // Lineas Verticales
        double y1 = pt1.getY();

        // Lineas Verticales
        double y2 = pt2.getY();
        double xIni = (int) pt1.getX() - 1;
        xIni -= (xIni % step);

        double xFin = ((int) pt2.getX()) + 1;

        if (y1 < -90.0) {
            y1 = -90.0;
        }

        if (y2 > 90.0) {
            y2 = 90.0;
        }

        if (xIni < -180.0) {
            xIni = -180.0;
        }

        if (xFin > 180.0) {
            xFin = 180.0;
        }

        for (double x = xIni; x <= xFin; x += step) {
            gp1 = (GeoPoint) createPoint(x, y1);
            gp2 = (GeoPoint) createPoint(x, y2);
            mat.transform(gp1, gp1);
            mat.transform(gp2, gp2);
            grid.addLine(gp1, gp2);
            tit = coordToString(x, fmt, false);

            //fmWidth = fm.stringWidth(tit);
            ptx.setLocation(gp2.getX() + 3, gp2.getY() + fmHeight);
            grid.addText(tit, ptx);
        }

        // Lineas Horizontales
        double x1 = pt1.getX();

        // Lineas Horizontales
        double x2 = pt2.getX();
        double yIni = (int) pt1.getY() - 1;
        yIni -= (yIni % step);

        double yFin = ((int) pt2.getY()) + 1;

        if (x1 < -180.0) {
            x1 = -180.0;
        }

        if (x2 > 180.0) {
            x2 = 180.0;
        }

        if (yIni < -90.0) {
            yIni = -90.0;
        }

        if (yFin > 90.0) {
            yFin = 90.0;
        }

        for (double y = yIni; y <= yFin; y += step) {
            gp1 = (GeoPoint) createPoint(x1, y);
            gp2 = (GeoPoint) createPoint(x2, y);
            mat.transform(gp1, gp1);
            mat.transform(gp2, gp2);
            grid.addLine(gp1, gp2);
            tit = coordToString(y, fmt, true);

            //fmWidth = fm.stringWidth(tit);
            ptx.setLocation(gp1.getX() + 3, gp1.getY() - 2);
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
        double scale = ((maxX - minX) * // grados

        // 1852.0 metros x minuto de meridiano
        (dpi / 2.54 * 100.0 * 1852.0 * 60.0)) / // px / metro
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
		wExtent =(w*scale*distanceUnits)/ (mapUnits*1852.0*60.0);
		hExtent =(h*scale*distanceUnits)/ (mapUnits*1852.0*60.0);
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

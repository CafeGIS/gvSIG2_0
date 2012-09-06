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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 * Proyeccion de Conica Comforme Lambert
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>* @author administrador
 */
public class CCLambert extends Projection {
    static String name = "Conica Comforme Lambert";
    static String abrev = "CCLam";

    public CCLambert(Ellipsoid eli) {
        super(eli);
        grid = new Graticule(this);
    }

    public ICoordTrans getCT(IProjection dest) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAbrev() {
        return abrev;
    }

    public static CCLambert getProjection(Ellipsoid eli) {
        return new CCLambert(eli);
    }

    /**
     *
     */
    public static IProjection getProjectionByName(IDatum eli, String name) {
        if (name.indexOf("CCL") < 0) {
            return null;
        }

        return getProjection((Ellipsoid) eli);
    }

    /**
     *
     */
    public Point2D createPoint(double x, double y) {
        return new Point2D.Double(x, y);
    }

    /**
     *
     * @param uPt
     * @return
     */
    public Point2D toGeo(Point2D lPt) {
        GeoPoint gPt = new GeoPoint();

        return toGeo(lPt, gPt);
    }

    /**
     *
     * @param uPt
     * @param gPt
     * @return
     */
    public GeoPoint toGeo(Point2D lPt, GeoPoint gPt) {
        return gPt;
    }

    /**
     *
     * @param gPt
     * @param uPt
     * @return
     */
    public Point2D fromGeo(Point2D gPt, Point2D lPt) {
        return lPt;
    }

    private void generateGrid(Graphics2D g, Extent extent, AffineTransform mat) {
        grid = new Graticule(this);
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
        // TODO Auto-generated method stub
        return -1D;
    }

	/* (non-Javadoc)
	 * @see org.cresques.cts.IProjection#getExtent(java.awt.geom.Rectangle2D, double, double, double, double, double, double)
	 */
	public Rectangle2D getExtent(Rectangle2D extent, double scale, double wImage, double hImage, double mapUnits, double distanceUnits, double dpi) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.cresques.cts.IProjection#getFullCode()
	 */
	public String getFullCode() {
		return getAbrev();
	}
}

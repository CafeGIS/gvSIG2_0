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
package org.cresques.px.gml;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Polygon2D;
import org.cresques.geo.ViewPortData;
import org.cresques.px.Extent;


/**
 * Geometria de tipo Polygon
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class Polygon extends Geometry {
    final static Color colorBase = new Color(192, 64, 64);
    final static Color fColorBase = new Color(192, 64, 64, 0x10); // new Color(255,192,192,64);
    public static int pointNr = 0;
    Polygon2D outPol = null;
    Polygon2D inPol = null;
    boolean outer = true;
    private Color fillColor = fColorBase; //new Color(255,222,165,64);
    private Color color = colorBase; //Color(255,214,132,255);

    public Polygon() {
        super();
        outPol = new Polygon2D();
        inPol = new Polygon2D();
    }

    public void add(Point2D pt) {
        pointNr++;

        if (outer) {
            outPol.addPoint(pt);
        } else {
            inPol.addPoint(pt);
        }

        extent.add(pt);
    }

    public Point2D get(int i) {
        if (outer) {
            return (Point2D) outPol.get(i);
        }

        return (Point2D) inPol.get(i);
    }

    public void remove(int i) {
        if (outer) {
            outPol.remove(i);
        } else {
            inPol.remove(i);
        }
    }

    public int pointNr() {
        if (outer) {
            return outPol.size();
        }

        return inPol.size();
    }

    public void setOuterBoundary() {
        outer = true;
    }

    public void setInnerBoundary() {
        outer = false;
    }

    public Color c() {
        return color;
    }

    public Color c(Color color) {
        this.color = color;

        return color;
    }

    public Color fillColor() {
        return fillColor;
    }

    public Color fillColor(Color c) {
        fillColor = c;

        return fillColor;
    }

    public IProjection getProjection() {
        return proj;
    }

    public void setProjection(IProjection p) {
        proj = p;
    }

    public void reProject(ICoordTrans rp) {
        Polygon2D savePol = outPol;

        outPol = new Polygon2D();
        extent = new Extent();

        Point2D ptDest = null;

        for (int i = 0; i < savePol.size(); i++) {
            ptDest = rp.getPDest().createPoint(0.0, 0.0);
            ptDest = rp.convert((Point2D) savePol.get(i), ptDest);
            outPol.addPoint(ptDest);
            extent.add(ptDest);
        }

        setProjection(rp.getPDest());
    }

    public void draw(Graphics2D g, ViewPortData vp) {
        // relleno el poligono si es preciso
        if (fillColor() != null) {
            g.setColor(fillColor());
            outPol.fill(g, vp);
        }

        // pinto el poligono si es preciso
        g.setColor(c());
        outPol.draw(g, vp);
    }
}

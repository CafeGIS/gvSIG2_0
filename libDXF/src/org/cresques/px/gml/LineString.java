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
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;
import org.cresques.px.Extent;


/**
 * Geometria de tipo Polygon
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class LineString extends Geometry {
    public static int pointNr = 0;
    private Color fColor = null; //new Color(255,222,165,64);
    private Color color = new Color(255, 0, 0); //Color(255,214,132,255);

    public LineString() {
        super();
    }

    public void add(Point2D pt) {
        pointNr++;
        super.add(pt);
    }

    public void remove(int i) {
        getData().remove(i);
    }

    public Color c() {
        return color;
    }

    public Color c(Color color) {
        this.color = color;

        return color;
    }

    public Color fillColor() {
        return fColor;
    }

    public Color fillColor(Color c) {
        fColor = c;

        return fColor;
    }

    public IProjection getProjection() {
        return proj;
    }

    public void setProjection(IProjection p) {
        proj = p;
    }

    public void reProject(ICoordTrans rp) {
        Vector saveLine = data;

        data = new Vector();
        extent = new Extent();

        Point2D ptDest = null;

        for (int i = 0; i < saveLine.size(); i++) {
            ptDest = rp.getPDest().createPoint(0.0, 0.0);
            ptDest = rp.convert((Point2D) saveLine.get(i), ptDest);
            data.add(ptDest);
            extent.add(ptDest);
        }

        setProjection(rp.getPDest());
    }

    public void draw(Graphics2D g, ViewPortData vp) {
        AffineTransform msave = g.getTransform();
        g.setTransform(vp.mat);

        // relleno el marco si es preciso
        if (fillColor() != null) {
            g.setColor(fillColor());

            //			outPol.fill(g);
        }

        // pinto el marco si es preciso
        g.setColor(c());
        drawLine2D(g, data);
        g.setTransform(msave);
    }

    void drawLine2D(Graphics2D g, Vector v) {
        Point2D pt = null;
        Point2D pt1 = null;
        Iterator iter = v.iterator();

        while (iter.hasNext()) {
            pt1 = (Point2D) iter.next();

            if (pt != null) {
                g.draw(new Line2D.Double(pt, pt1));
            }

            pt = pt1;
        }
    }
}

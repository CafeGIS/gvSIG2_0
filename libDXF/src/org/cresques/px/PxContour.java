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
package org.cresques.px;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Polygon2D;
import org.cresques.geo.Projected;
import org.cresques.geo.ViewPortData;
import org.cresques.geo.cover.Hoja;


public class PxContour extends PxObj implements Projected {
    final static Color colorBase = new Color(0, 64, 128, 255); //Color(255,214,132,255);
    final static Color fColorBase = new Color(64, 128, 192, 255); //Color(255,222,165,64);
    IProjection proj = null;
    protected String name;
    protected String fName;
    private Color fColor = fColorBase;
    private Color pc = colorBase;
    private Polygon2D polygon = null;

    public PxContour(Extent e, String fName, String name, IProjection proj) {
        this.fName = fName;
        this.name = name;
        this.proj = proj;

        Point2D[] v = new Point2D[4];
        v[0] = proj.createPoint(e.minX(), e.minY());
        v[1] = proj.createPoint(e.maxX(), e.minY());
        v[2] = proj.createPoint(e.maxX(), e.maxY());
        v[3] = proj.createPoint(e.minX(), e.maxY());
        setContour(v);
    }

    public PxContour(Hoja h) {
        name = h.getCode();
        setContour(h.getVertex());
    }

    public PxContour(Point2D[] v, String name) {
        this.name = name;
        setContour(v);
    }

    public void _PxContour(Point2D pt1, Point2D pt2, String fName, String name) {
        this.fName = fName;
        this.name = name;
        extent = new Extent(pt1, pt2);
    }

    public void _PxContour(Point2D[] v, String fName, String name) {
        this.fName = fName;
        this.name = name;
        setContour(v);
    }

    public IProjection getProjection() {
        return proj;
    }

    public void setProjection(IProjection p) {
        proj = p;
    }

    private void setContour(Point2D[] v) {
        extent = new Extent();
        polygon = new Polygon2D();

        for (int i = 0; i < v.length; i++) {
            polygon.addPoint(v[i]);
            extent.add(v[i]);
        }
    }

    /**
     * Vertices de un contorno.
     * @return
     */
    public Vector getVertex() {
        return polygon;
    }

    public Point2D[] getPtList() {
        Point2D[] v = new Point2D[polygon.size()];

        for (int i = 0; i < polygon.size(); i++)
            v[i] = (Point2D) polygon.get(i);

        return v;
    }

    public String getName() {
        return name;
    }

    public Color c() {
        return pc;
    }

    public Color c(Color color) {
        this.pc = color;

        return pc;
    }

    public Color fillColor() {
        return fColor;
    }

    public Color fillColor(Color c) {
        fColor = c;

        return fColor;
    }

    public void setColor(Color color) {
        pc = color;
    }

    public Color getColor() {
        return pc;
    }

    public void setFillColor(Color color) {
        fColor = color;
    }

    public Color getFillColor() {
        return fColor;
    }

    public void reProject(ICoordTrans rp) {
        Polygon2D savePol = polygon;

        polygon = new Polygon2D();
        extent = new Extent();

        Point2D ptDest = null;

        for (int i = 0; i < savePol.size(); i++) {
            ptDest = rp.getPDest().createPoint(0.0, 0.0);
            ptDest = rp.convert((Point2D) savePol.get(i), ptDest);
            polygon.addPoint(ptDest);
            extent.add(ptDest);
        }

        setProjection(rp.getPDest());
    }

    public void draw(Graphics2D g, ViewPortData vp, ICoordTrans rp) {
        IProjection saveProj = proj;
        Polygon2D savePol = polygon;
        Extent saveExt = extent;

        reProject(rp);
        draw(g, vp);

        polygon = savePol;
        extent = saveExt;
        proj = saveProj;
    }

    public void draw(Graphics2D g, ViewPortData vp) {
        //AffineTransform msave=g.getTransform();
        //g.setTransform(vp.mat);
        // relleno el marco si es preciso
        if (fColor != null) {
            g.setColor(fColor);

            if (polygon == null) {
                g.fillRect((int) extent.minX(), (int) extent.minY(),
                           (int) extent.width(), (int) extent.height());
            } else {
                polygon.fill(g, vp);
            }
        }

        // pinto el marco si es preciso
        if (pc != null) {
            g.setColor(pc);
        }

        if (polygon != null) {
            polygon.draw(g, vp);
        } else {
            g.drawRect((int) extent.minX(), (int) extent.minY(),
                       (int) extent.width(), (int) extent.height());
        }

        //g.setTransform(msave);
        // Pinto el name
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(name);
        int h = fm.getAscent();
        Point2D.Double pt = new Point2D.Double((extent.minX() +
                                               (extent.width() / 2.0)),
                                               (extent.minY() +
                                               (extent.height() / 2.0)));

        try {
            Point2D.Double min = new Point2D.Double(extent.minX(), extent.minY());
            Point2D.Double max = new Point2D.Double(extent.maxX(), extent.minY());
            vp.mat.transform(min, min);
            vp.mat.transform(max, max);

            if ((max.getX() - min.getX()) < w) {
                return;
            }

            vp.mat.transform(pt, pt);

            //if ((int)(pt2.getY()-pt1.getY()) >= g.getFontMetrics().getAscent())
            g.drawString(name, (int) pt.getX() - (w / 2),
                         (int) pt.getY() + (h / 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

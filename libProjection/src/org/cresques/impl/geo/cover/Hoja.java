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
package org.cresques.impl.geo.cover;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;

import org.cresques.geo.Projected;

import org.cresques.px.Extent;

import java.awt.geom.Point2D;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.Vector;


/**
 * @author Luis W. Sevilla <sevilla_lui@gva.es>
 */
public class Hoja implements Projected {
    IProjection proj;
    String code = null;
    String name = null;
    Extent extent = null;
    Point2D tl;
    Point2D tr;
    Point2D bl;
    Point2D br;

    public Hoja(IProjection proj, String code, String name) {
        this.proj = proj;
        this.code = code;
        this.name = name;
        tl = tr = bl = br = null;
    }

    public Hoja(String cod, Point2D p1, Point2D p2, Point2D p3, Point2D p4,
                String name) {
        code = cod;
        tl = p1;
        tr = p2;
        bl = p3;
        br = p4;

        if (name != null) {
            this.name = name;
        }

        setExtent();
    }

    public Hoja(String cod, Point2D[] pt, String name) {
        code = cod;
        tl = pt[0];
        tr = pt[1];
        br = pt[2];
        bl = pt[3];

        if (name != null) {
            this.name = name;
        }

        setExtent();
    }

    public Hoja(String cod, Vector pt, String name) {
        code = cod;
        tl = (Point2D) pt.get(0);
        tr = (Point2D) pt.get(1);
        br = (Point2D) pt.get(2);
        bl = (Point2D) pt.get(3);

        if (name != null) {
            this.name = name;
        }

        setExtent();
    }

    public Hoja(String cod, Hoja h, String name) {
        code = cod;
        tl = h.tl;
        tr = h.tr;
        br = h.br;
        bl = h.bl;

        if (name != null) {
            this.name = name;
        }

        setExtent();
    }

    public IProjection getProjection() {
        return proj;
    }

    public void setProjection(IProjection p) {
        proj = p;
    }

    public void reProject(ICoordTrans rp) {
        // TODO metodo reProject pendiente de implementar
    }

    public Point2D getTL() {
        return tl;
    }

    public void setTL(Point2D pt) {
        tl = pt;
        extent.add(pt);
    }

    public Point2D getTR() {
        return tr;
    }

    public void setTR(Point2D pt) {
        tr = pt;
        extent.add(pt);
    }

    public Point2D getBL() {
        return bl;
    }

    public void setBL(Point2D pt) {
        bl = pt;
        extent.add(pt);
    }

    public Point2D getBR() {
        return br;
    }

    public void setBR(Point2D pt) {
        br = pt;
        extent.add(pt);
    }

    public Extent getExtent() {
        return extent;
    }

    private void setExtent() {
        extent = new Extent(tl, br);
        extent.add(tr);
        extent.add(bl);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Point2D[] getVertex() {
        Point2D[] v = { tl, tr, br, bl };

        return v;
    }

    public void toXml(OutputStream os) {
    }

    public void fromXml(InputStream is) {
    }
}

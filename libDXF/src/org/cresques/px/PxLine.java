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
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import org.cresques.geo.ViewPortData;


public class PxLine extends PxObj implements IPoint, Colored {
    private int px = 0;
    private int py = 0;
    private int px2 = 0;
    private int py2 = 0;
    private Color pc;

    public PxLine(IPoint p, IPoint p2, Color color) {
        x(p.x());
        y(p.y());
        x2(p2.x());
        y2(p2.y());
        c(color);
        extent = new Extent(Math.min(x(), x2()), Math.min(y(), y2()),
                            Math.max(x(), x2()), Math.max(y(), y2()));
    }

    public PxLine(int x, int y, int x2, int y2, Color color) {
        x(x);
        y(y);
        x2(x2);
        y2(y2);
        c(color);
        extent = new Extent(Math.min(x(), x2()), Math.min(y(), y2()),
                            Math.max(x(), x2()), Math.max(y(), y2()));
    }

    public int x() {
        return px;
    }

    public int x(int x) {
        px = x;

        return px;
    }

    public int y() {
        return py;
    }

    public int y(int y) {
        py = y;

        return py;
    }

    public int x2() {
        return px2;
    }

    public int x2(int x) {
        px2 = x;

        return px2;
    }

    public int y2() {
        return py2;
    }

    public int y2(int y) {
        py2 = y;

        return py2;
    }

    public Color c() {
        return pc;
    }

    public Color c(Color color) {
        pc = color;

        return pc;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    /*        public void draw(Graphics2D g) {
                    draw(g, g.getTransform(), null);
            }

            public void draw(Graphics2D g, AffineTransform mat, Extent sz) {
    */
    public void draw(Graphics2D g, ViewPortData vp) {
        Stroke strkSave = null;

        if (stroke != null) {
            strkSave = g.getStroke();
            g.setStroke(stroke);
        }

        AffineTransform msave = g.getTransform();
        g.setTransform(vp.mat);
        g.setColor(c());
        g.drawLine(x(), y(), x2(), y2());

        if (strkSave != null) {
            g.setStroke(strkSave);
        }

        g.setTransform(msave);
    }
}

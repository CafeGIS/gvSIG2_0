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
import java.awt.geom.AffineTransform;

import org.cresques.geo.ViewPortData;


public class PxRect extends PxObj implements IPoint, ISize, Colored {
    private int px = 0;
    private int py = 0;
    private int pw = 0;
    private int ph = 0;
    private Color pc;
    private Color filColor = null;

    public PxRect(IPoint p, ISize s, Color color, Color fc) {
        x(p.x());
        y(p.y());
        w(s.w());
        h(s.h());
        c(color);
        fillColor(fc);
        extent = new Extent(x(), y(), x() + w(), y() + h());
    }

    public PxRect(int x, int y, int w, int h, Color color, Color fc) {
        x(x);
        y(y);
        w(w);
        h(h);
        c(color);
        fillColor(fc);
        extent = new Extent(x(), y(), x() + w(), y() + h());
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

    public int w() {
        return pw;
    }

    public int w(int w) {
        pw = w;

        return pw;
    }

    public int h() {
        return ph;
    }

    public int h(int h) {
        ph = h;

        return ph;
    }

    public Color c() {
        return pc;
    }

    public Color c(Color color) {
        pc = color;

        return pc;
    }

    public Color fillColor() {
        return filColor;
    }

    public Color fillColor(Color c) {
        filColor = c;

        return filColor;
    }

    /*        public void draw(Graphics2D g) {
                    draw(g, g.getTransform(), null);
            }

            public void draw(Graphics2D g, AffineTransform mat, Extent sz) {
    */
    public void draw(Graphics2D g, ViewPortData vp) {
        AffineTransform msave = g.getTransform();
        g.setTransform(vp.mat);

        if (fillColor() != null) {
            g.setColor(fillColor());
            g.fillRect(x(), y(), w(), h());
        }

        g.setColor(c());
        g.drawRect(x(), y(), w(), h());
        g.setTransform(msave);
    }
}

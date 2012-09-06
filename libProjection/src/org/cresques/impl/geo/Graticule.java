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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import java.util.Iterator;
import java.util.Vector;

import org.cresques.geo.ViewPortData;


class Text2D extends Point2D {
    private double x;
    private double y;
    private String txt;

    public Text2D(String txt, Point2D pt) {
        this.txt = txt;
        setLocation(pt.getX(), pt.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g, ViewPortData vp) {
        g.drawString(txt, (int) x, (int) y);
    }
}


/**
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 *
 * TODO Añadir soporte para hasta 3 niveles de grid (3 colores y 3 GeneralPath
 */
public class Graticule {
    static final Color color = new Color(128, 128, 128, 192);
    Color pc = null;
    Color tColor = null;
    GeneralPath gp = null;
    GeneralPath gp1 = null;
    Vector vText = null;
    Projection proj;

    public Graticule(Projection proj) {
        this.proj = proj;
        gp = new GeneralPath();
        gp1 = new GeneralPath();
        vText = new Vector();
        setColor(color);
    }

    public Color getColor() {
        return pc;
    }

    public void setColor(Color color) {
        pc = color;
        tColor = new Color(pc.getRed(), pc.getGreen(), pc.getBlue(), 255);
    }

    public void addLine(Point2D pt1, Point2D pt2) {
        gp.moveTo((float) pt1.getX(), (float) pt1.getY());
        gp.lineTo((float) pt2.getX(), (float) pt2.getY());
    }

    public void addLine(Point2D pt1, Point2D pt2, int num) {
        if (num == 0) {
            gp.moveTo((float) pt1.getX(), (float) pt1.getY());
            gp.lineTo((float) pt2.getX(), (float) pt2.getY());
        } else if (num == 1) {
            gp1.moveTo((float) pt1.getX(), (float) pt1.getY());
            gp1.lineTo((float) pt2.getX(), (float) pt2.getY());
        }
    }

    public void addText(String txt, Point2D pt) {
        vText.add(new Text2D(txt, pt));
    }

    public void draw(Graphics2D g, ViewPortData vp) {
        g.setColor(pc);
        g.draw(gp);

        g.setColor(tColor);
        g.draw(gp1);

        Iterator iter = vText.iterator();

        while (iter.hasNext())
            ((Text2D) iter.next()).draw(g, vp);
    }
}

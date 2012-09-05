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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;


/**
 * Geometria de tipo Point
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class Point extends Geometry {
    public static int pointNr = 0;
    public String text = null;
    private boolean textPoint;
    private Color fColor = null; //new Color(255,222,165,64);
    private Color color = new Color(255, 0, 0); //Color(255,214,132,255);

    public Point() {
        super();
    }

    public void add(Point2D pt) {
        pointNr++;
        super.add(pt);
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
        // TODO metodo reProject pendiente de implementar
    }

    public void draw(Graphics2D g, ViewPortData vp) {
        g.setColor(c());

        Point2D pt = new Point2D.Double(0D, 0D);
        vp.mat.transform((Point2D) data.get(0), pt);
        g.draw(new Line2D.Double(pt, pt));

        if (text != null) {
            g.drawString(text, (int) pt.getX(), (int) pt.getY());
        }
    }

    /**
     * @return Returns the textPoint.
     */
    public boolean isTextPoint() {
        return textPoint;
    }

    /**
     * @param textPoint The textPoint to set.
     */
    public void setTextPoint(boolean textPoint) {
        this.textPoint = textPoint;
    }
}

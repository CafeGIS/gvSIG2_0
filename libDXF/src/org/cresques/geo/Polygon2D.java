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
package org.cresques.geo;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import java.util.Iterator;
import java.util.Vector;

import org.cresques.geo.ViewPortData;


/**
 * Clase que representa un polígono 2D
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class Polygon2D extends Vector {
    GeneralPath gp = null;

    public Polygon2D() {
        super();
        gp = null;
    }

    /**
     * Añade un vertice al po?ígono
     * @param pt        punto 2D que representa el vertice añadido
     */
    public void addPoint(Point2D pt) {
        super.add(pt);
    }

    /**
     * Dibuja el polígono
     * @param g        Graphics sobre el que dibuja
     * @param vp        ViewPort con la vista
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        newGP(vp);
        g.draw(gp);

        //g.draw(new Line2D.Double(pt,pt0));
    }

    /**
     *
     * @param g
     * @param vp
     */
    public void fill(Graphics2D g, ViewPortData vp) {
        newGP(vp);
        g.fill(gp);
    }

    /**
     *
     * @param vp
     */
    private void newGP(ViewPortData vp) {
        //if (gp != null) return;
        gp = new GeneralPath();

        Point2D pt0 = null;
        Point2D pt = null;
        Point2D pt1 = null;
        Point2D.Double ptTmp = new Point2D.Double(0.0, 0.0);
        Iterator iter = iterator();

        while (iter.hasNext()) {
            pt1 = (Point2D) iter.next();
            vp.mat.transform(pt1, ptTmp);

            if (pt0 == null) {
                pt0 = ptTmp;
                gp.moveTo((float) ptTmp.getX(), (float) ptTmp.getY());
            } else {
                gp.lineTo((float) ptTmp.getX(), (float) ptTmp.getY());
            }
        }

        gp.closePath();
    }
}

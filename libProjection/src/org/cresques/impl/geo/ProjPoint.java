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
import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;

import java.awt.geom.Point2D;


/**
 *
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>*
 */
public class ProjPoint extends Point2D implements Projected {
    IProjection proj = Mercator.getProjection(Ellipsoid.ed50);
    public double X;
    public double Y;

    public ProjPoint() {
        setLocation(0.0, 0.0);
    }

    public ProjPoint(Projection proj) {
        setLocation(0.0, 0.0);
        this.proj = proj;
    }

    public ProjPoint(double x, double y) {
        setLocation(x, y);
    }

    public ProjPoint(Projection proj, double x, double y) {
        setLocation(x, y);
        this.proj = proj;
    }

    public ProjPoint(Point2D pt) {
        setLocation(pt.getX(), pt.getY());
    }

    public ProjPoint(Projection proj, Point2D pt) {
        setLocation(pt.getX(), pt.getY());
        this.proj = proj;
    }

    public IProjection getProjection() {
        return proj;
    }

    public void reProject(ICoordTrans rp) {
        // TODO metodo reProject pendiente de implementar
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public void setLocation(double x, double y) {
        X = x;
        Y = y;
    }

    public String toString() {
        return "(" + proj + ": " + getX() + "," + getY() + ")";
    }

    public Point2D toGeo() {
        return ((Projection) proj).toGeo(this);
    }
}

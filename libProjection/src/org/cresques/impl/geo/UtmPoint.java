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

import java.awt.geom.Point2D;


public class UtmPoint extends ProjPoint {
    public UtmPoint() {
        proj = UtmZone.getProjection(Ellipsoid.hayford, 30, UtmZone.NORTH);
        setLocation(0.0, 0.0);
    }

    public UtmPoint(double x, double y) {
        proj = UtmZone.getProjection(Ellipsoid.hayford, 30, UtmZone.NORTH);
        setLocation(x, y);
    }

    public UtmPoint(Point2D pt) {
        proj = UtmZone.getProjection(Ellipsoid.hayford, 30, UtmZone.NORTH);
        setLocation(pt.getX(), pt.getY());
    }

    public UtmPoint(UtmZone zone) {
        setLocation(0.0, 0.0);
        proj = zone;
    }

    public UtmPoint(UtmZone zone, double x, double y) {
        setLocation(x, y);
        proj = zone;
    }

    public UtmPoint(UtmZone zone, Point2D pt) {
        setLocation(pt.getX(), pt.getY());
        proj = zone;
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
        return "(" + ((UtmZone) proj).Zone + ": " + getX() + "," + getY() +
               ")";
    }

    public Point2D toGeo() {
        return ((Projection) proj).toGeo(this);
    }
}

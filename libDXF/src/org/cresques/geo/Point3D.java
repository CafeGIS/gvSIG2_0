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

import java.awt.geom.Point2D;


/**
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class Point3D extends Point2D {
    public double X;
    public double Y;
    public double Z;

    public Point3D() {
        setLocation(0.0, 0.0);
    }

    public Point3D(double x, double y) {
        setLocation(x, y);
    }

    public Point3D(double x, double y, double z) {
        setLocation(x, y, z);
    }

    public Point3D(Point2D pt) {
        setLocation(pt.getX(), pt.getY());
    }

    public Point3D(Point3D pt) {
        setLocation(pt.getX(), pt.getY(), pt.getZ());
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public double getZ() {
        return Z;
    }

    public void setLocation(double x, double y) {
        X = x;
        Y = y;
        Z = 0D;
    }

    public void setLocation(double x, double y, double z) {
        X = x;
        Y = y;
        Z = z;
    }

    public String toString() {
        return "(" + getX() + "," + getY() + ")";
    }
}

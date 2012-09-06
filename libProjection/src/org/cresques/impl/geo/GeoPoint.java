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

import geojava.Mutil;
import geojava.SexaAngle;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;

import java.awt.geom.Point2D;


public class GeoPoint extends Point2D implements Projected {
    public static int decimales = 4;
    IProjection proj = null;
    public SexaAngle Longitude;
    public SexaAngle Latitude;
    public double h;

    public GeoPoint(SexaAngle sexaangle, SexaAngle sexaangle1, double d) {
        Longitude = sexaangle;
        Latitude = sexaangle1;
        h = d;
    }

    public GeoPoint() {
        Longitude = new SexaAngle();
        Latitude = new SexaAngle();
        h = 0.0D;
    }

    public GeoPoint(double x, double y) {
        setLocation(x, y);
        h = 0.0D;
    }

    public GeoPoint(Projection proj, double x, double y) {
        setLocation(x, y);
        h = 0.0D;
        ;
        this.proj = proj;
    }

    public GeoPoint(Point2D pt) {
        setLocation(pt.getX(), pt.getY());
        h = 0.0D;
    }

    public GeoPoint(Projection proj, Point2D pt) {
        setLocation(pt.getX(), pt.getY());
        h = 0.0D;
        ;
        this.proj = proj;
    }

    public IProjection getProjection() {
        return proj;
    }

    public void reProject(ICoordTrans rp) {
        // TODO metodo reProject pendiente de implementar
    }

    public double getX() {
        return Longitude.ToDecimal();
    }

    public double getY() {
        return Latitude.ToDecimal();
    }

    public void setLocation(double x, double y) {
        Longitude = SexaAngle.fromDecimal(x);
        Latitude = SexaAngle.fromDecimal(y);
    }

    public String toString() {
        String str = "";
        String s = String.valueOf(Longitude.Deg);
        String s1 = String.valueOf(Longitude.Min);
        String s2 = String.valueOf(Mutil.double2String(Longitude.Sec, decimales));
        SexaAngle sexaangle = new SexaAngle(s, s1, s2);
        sexaangle.Normalize();
        str += (String.valueOf(sexaangle.Deg) + "º");
        str += (String.valueOf(sexaangle.Min) + "'");
        str += (Mutil.double2String(sexaangle.Sec, decimales) + "\"");
        str += " ";
        s = String.valueOf(Latitude.Deg);
        s1 = String.valueOf(Latitude.Min);
        s2 = String.valueOf(Mutil.double2String(Latitude.Sec, decimales));
        sexaangle = new SexaAngle(s, s1, s2);
        sexaangle.Normalize();
        str += (String.valueOf(sexaangle.Deg) + "º");
        str += (String.valueOf(sexaangle.Min) + "'");
        str += (Mutil.double2String(sexaangle.Sec, decimales) + "\"");

        return str;
    }
}

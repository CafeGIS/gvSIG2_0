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

import geojava.EGeoUtmCom;

import java.awt.Frame;
import java.awt.geom.Point2D;


public class Calc {
    static int CATALA = 1;
    static int CASTELLANO = 2;
    static int ENGLISH = 3;

    public static void ICCCalculator() {
        Frame frame = new Frame("EGeoUtmCom");
        int idioma = CASTELLANO;
        EGeoUtmCom calcula = new EGeoUtmCom(false, idioma);
        frame.add("Center", calcula);
        frame.pack();
        frame.show();
    }

    public static void prbUtmToGeo() {
        UtmZone utmZone = UtmZone.getProjection(Ellipsoid.hayford, 30,
                                                UtmZone.NORTH);

        //		UtmPoint uPt = new UtmPoint(utmZone, 739027.0, 4468771.0); // 593 tl
        UtmPoint uPt = new UtmPoint(utmZone, 773177.0, 4303195.0); // 823 tl
        GeoPoint gPt = (GeoPoint) ((Projection) uPt.proj).toGeo(uPt);
        GeoPoint.decimales = 1;
        System.out.println("UTM to Geo : " + uPt.toString() + " == [" +
                           gPt.toString() + "] (" + gPt.getX() + "," +
                           gPt.getY() + ")");

        UtmPoint uPt2 = utmZone.fromGeo(gPt, new UtmPoint(utmZone),
                                        UtmZone.getProjection(Ellipsoid.hayford,
                                                              29, UtmZone.NORTH));
        System.out.println("Geo to Utm : " + uPt2.toString() + " == [" +
                           gPt.toString() + "] (" + gPt.getX() + "," +
                           gPt.getY() + ")");
    }

    public Point2D pointFactory(Projection proj, double x, double y) {
        Point2D.Double pt = new Point2D.Double(x, y);

        return pt;
    }
}

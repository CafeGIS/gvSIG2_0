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
package org.cresques.px.dxf;

import org.cresques.geo.Point3D;

/**
 * La clase DxfCalXtru se emplea para aplicar los parámetros de extrusión de las
 * entidades de un DXF. Esta transformación provoca alteraciones en las coordenadas de
 * estos elementos.
 * @author jmorell
 */
public class DxfCalXtru {
    
    /**
     * Aplica a un punto en 3D sus correspondientes parámetros de extrusión.
     * @param coord_in, punto antes de aplicar la extrusión.
     * @param xtru, parámetros de extrusión en X, en Y y en Z.
     * @return Point3D, punto 3D afectado de extrusión.
     */
    public static Point3D CalculateXtru(Point3D coord_in, Point3D xtru) {
        Point3D coord_out = new Point3D();

        double dxt0 = 0D;
        double dyt0 = 0D;
        double dzt0 = 0D;
        double dvx1;
        double dvx2;
        double dvx3;
        double dvy1;
        double dvy2;
        double dvy3;
        double dmod;
        double dxt;
        double dyt;
        double dzt;

        double aux = 1D / 64D; //LWS
        double aux1 = Math.abs(xtru.getX());
        double aux2 = Math.abs(xtru.getY());

        dxt0 = coord_in.getX();
        dyt0 = coord_in.getY();
        dzt0 = coord_in.getZ();

        double xtruX;
        double xtruY;
        double xtruZ;
        xtruX = xtru.getX();
        xtruY = xtru.getY();
        xtruZ = xtru.getZ();

        if ((aux1 < aux) && (aux2 < aux)) {
            dmod = Math.sqrt((xtruZ * xtruZ) + (xtruX * xtruX));
            dvx1 = xtruZ / dmod;
            dvx2 = 0;
            dvx3 = -xtruX / dmod;
        } else {
            dmod = Math.sqrt((xtruY * xtruY) + (xtruX * xtruX));
            dvx1 = -xtruY / dmod;
            dvx2 = xtruX / dmod;
            dvx3 = 0;
        }

        dvy1 = (xtruY * dvx3) - (xtruZ * dvx2);
        dvy2 = (xtruZ * dvx1) - (xtruX * dvx3);
        dvy3 = (xtruX * dvx2) - (xtruY * dvx1);

        dmod = Math.sqrt((dvy1 * dvy1) + (dvy2 * dvy2) + (dvy3 * dvy3));

        dvy1 = dvy1 / dmod;
        dvy2 = dvy2 / dmod;
        dvy3 = dvy3 / dmod;

        dxt = (dvx1 * dxt0) + (dvy1 * dyt0) + (xtruX * dzt0);
        dyt = (dvx2 * dxt0) + (dvy2 * dyt0) + (xtruY * dzt0);
        dzt = (dvx3 * dxt0) + (dvy3 * dyt0) + (xtruZ * dzt0);

        coord_out.setLocation(dxt, dyt, dzt);

        //coord_out.z = dzt;
        dxt0 = 0;
        dyt0 = 0;
        dzt0 = 0;

        return coord_out;
    }
    
    /**
     * Devuelve el coseno de un ángulo introducido en grados sexagesimales.
     * @param alfa, ángulo.
     * @return double, valor del coseno.
     */
    public static double cosDeg(double alfa) {
        final double toRad = Math.PI / (double) 180.0;

        return Math.cos(alfa * toRad);
    }
    
    /**
     * Devuelve el seno de un ángulo introducido en grados sexagesimales.
     * @param alfa, ángulo.
     * @return double, valor del seno.
     */
    public static double sinDeg(double alfa) {
        final double toRad = Math.PI / (double) 180.0;

        return Math.sin(alfa * toRad);
    }
}

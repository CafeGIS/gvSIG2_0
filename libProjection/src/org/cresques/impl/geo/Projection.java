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

import org.cresques.cts.IDatum;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;


abstract public class Projection implements IProjection {
    public static int NORTH = 0;
    public static int SOUTH = 1;
    static String name = "Sin Proyeccion";
    static String abrev = "None";
    private static final Color basicGridColor = new Color(64, 64, 64, 128);
    Color gridColor = basicGridColor;
    Ellipsoid eli = Ellipsoid.hayford;
    Graticule grid;

    public Projection() {
        eli = Ellipsoid.hayford;
    }

    public Projection(Ellipsoid e) {
        eli = e;
    }

    public String getName() {
        return name;
    }

    abstract public String getAbrev();

    public IDatum getDatum() {
        return eli;
    }

    public double[] getElliPar() {
        return eli.getParam();
    }

    abstract public Point2D createPoint(double x, double y);

    public Point2D createPoint(Point2D pt) {
        return createPoint(pt.getX(), pt.getY());
    }

    public static IProjection getProjectionByName(IDatum eli, String name) {
        if (name.indexOf("UTM") >= 0) {
            return UtmZone.getProjectionByName(eli, name);
        }

        if (name.indexOf("GEO") >= 0) {
            return Geodetic.getProjectionByName(eli, name);
        }

        if (name.indexOf("MERC") >= 0) {
            return Mercator.getProjectionByName(eli, name);
        }

        return null;
    }

    public ReProjection getReproyectionTo(Projection proj) {
        ReProjection rp = new ReProjection(this, proj);

        return rp;
    }

    abstract public Point2D toGeo(Point2D pt);

    abstract public Point2D fromGeo(Point2D gPt, Point2D mPt);

    public void setGridColor(Color c) {
        gridColor = c;
    }

    public Color getGridColor() {
        return gridColor;
    }

    public static String coordToString(double coord, String fmt, boolean isLat) {
        String txt = fmt;
        int donde;
        donde = txt.indexOf("%G");

        if (donde >= 0) {
            int deg = (int) coord;

            if ((fmt.indexOf("%N") >= 0) && (deg < 0)) {
                deg = -deg;
            }

            txt = txt.substring(0, donde) + Integer.toString(deg) +
                  txt.substring(donde + 2);
        }

        donde = txt.indexOf("%M");

        if (donde >= 0) {
            int min = (int) ((coord - (int) coord) * 60.0) % 60;

            if ((fmt.indexOf("%N") >= 0) && (min < 0)) {
                min = -min;
            }

            txt = txt.substring(0, donde) + Integer.toString(min) +
                  txt.substring(donde + 2);
            ;
        }

        donde = txt.indexOf("%N");

        if (donde >= 0) {
            String t = "";

            if (isLat) {
                if (coord > 0) {
                    t = "N";
                } else if (coord < 0) {
                    t = "S";
                }
            } else {
                if (coord > 0) {
                    t = "E";
                } else if (coord < 0) {
                    t = "W";
                }
            }

            txt = txt.substring(0, donde) + t + txt.substring(donde + 2);
            ;
        }

        return txt;
    }

    abstract public void drawGrid(Graphics2D g, ViewPortData vp);
    
    public boolean isProjected() {
    	return false;
    }
}

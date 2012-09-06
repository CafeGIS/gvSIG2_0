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
package org.cresques.cts;

import java.awt.geom.Point2D;


/**
 * Operaciones relacionadas con las proyecciones y sistemas
 * de coordenadas.
 *
 * cmartinez: Esta clase no debería formar parte de una API, pero
 * se deja hasta que se aborde el refactoring de libProjection.
 *
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 */
public class GeoCalc {
    IProjection proj;

    /**
     *
     * @param proj
     */
    public GeoCalc(IProjection proj) {
        this.proj = proj;
    }

    /* (non-Javadoc)
	 * @see org.cresques.impl.cts.GeoCalc#distanceGeo(java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
    public double distanceGeo(Point2D pt1, Point2D pt2) {
        double R2 = Math.pow(proj.getDatum().getESemiMajorAxis(), 2);
        double dLat = Math.toRadians(pt2.getY() - pt1.getY());
        double dLong = Math.toRadians(pt2.getX() - pt1.getX());

        double alfa = Math.toRadians(pt1.getY());
        double alfa2 = Math.toRadians(pt2.getY());

        if (Math.abs(alfa2) < Math.abs(alfa)) {
            alfa = alfa2;
        }

        double ds2 = (R2 * dLat * dLat) +
                     (R2 * Math.cos(alfa) * Math.cos(alfa) * dLong * dLong);

        return Math.sqrt(ds2);
    }

    /* (non-Javadoc)
	 * @see org.cresques.impl.cts.GeoCalc#distanceEli(java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
    public double distanceEli(Point2D pt1, Point2D pt2) {
        double lat1 = Math.toRadians(pt1.getY());
        double lon1 = -Math.toRadians(pt1.getX());
        double lat2 = Math.toRadians(pt2.getY());
        double lon2 = -Math.toRadians(pt2.getX());

        double F = (lat1 + lat2) / 2D;
        double G = (lat1 - lat2) / 2D;
        double L = (lon1 - lon2) / 2D;

        double sing = Math.sin(G);
        double cosl = Math.cos(L);
        double cosf = Math.cos(F);
        double sinl = Math.sin(L);
        double sinf = Math.sin(F);
        double cosg = Math.cos(G);

        double flat = 1D / proj.getDatum().getEIFlattening();

        double S = (sing * sing * cosl * cosl) + (cosf * cosf * sinl * sinl);
        double C = (cosg * cosg * cosl * cosl) + (sinf * sinf * sinl * sinl);
        double W = Math.atan2(Math.sqrt(S), Math.sqrt(C));
        double R = Math.sqrt((S * C)) / W;
        double H1 = ((3D * R) - 1D) / (2D * C);
        double H2 = ((3D * R) + 1D) / (2D * S);
        double D = 2D * W * proj.getDatum().getESemiMajorAxis();

        return (D * ((1D + (flat * H1 * sinf * sinf * cosg * cosg)) -
               (flat * H2 * cosf * cosf * sing * sing)));
    }

    /**
     * Algrothims from Geocentric Datum of Australia Technical Manual
     *
     * http://www.anzlic.org.au/icsm/gdatum/chapter4.html
     *
     * This page last updated 11 May 1999
     *
     * Computations on the Ellipsoid
     *
     * There are a number of formulae that are available
     * to calculate accurate geodetic positions,
     * azimuths and distances on the ellipsoid.
     *
     * Vincenty's formulae (Vincenty, 1975) may be used
     * for lines ranging from a few cm to nearly 20,000 km,
     * with millimetre accuracy.
     * The formulae have been extensively tested
     * for the Australian region, by comparison with results
     * from other formulae (Rainsford, 1955 & Sodano, 1965).
     *
     * * Inverse problem: azimuth and distance from known
     *                 latitudes and longitudes
     * * Direct problem: Latitude and longitude from known
     *                 position, azimuth and distance.
     * * Sample data
     * * Excel spreadsheet
     *
     * Vincenty's Inverse formulae
     * Given: latitude and longitude of two points
     *                 (phi1, lembda1 and phi2, lembda2),
     * Calculate: the ellipsoidal distance (s) and
     * forward and reverse azimuths between the points (alpha12, alpha21).
     */
    /* (non-Javadoc)
	 * @see org.cresques.impl.cts.GeoCalc#distanceVincenty(java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
    public double distanceVincenty(Point2D pt1, Point2D pt2) {
        return distanceAzimutVincenty(pt1, pt2).dist;
    }

    /**
     * Returns the distance between two geographic points on the ellipsoid
     *        and the forward and reverse azimuths between these points.
     *       lats, longs and azimuths are in decimal degrees, distance in metres
     *        Returns ( s, alpha12,  alpha21 ) as a tuple
     * @param pt1
     * @param pt2
     * @return
     */
    protected GeoData distanceAzimutVincenty(Point2D pt1, Point2D pt2) {
        GeoData gd = new GeoData(0, 0);
        double f = 1D / proj.getDatum().getEIFlattening();
        double a = proj.getDatum().getESemiMajorAxis();
        double phi1 = pt1.getY();
        double lembda1 = pt1.getX();
        double phi2 = pt2.getY();
        double lembda2 = pt2.getX();

        if ((Math.abs(phi2 - phi1) < 1e-8) &&
                (Math.abs(lembda2 - lembda1) < 1e-8)) {
            return gd;
        }

        double piD4 = Math.atan(1.0);
        double two_pi = piD4 * 8.0;

        phi1 = (phi1 * piD4) / 45.0;
        lembda1 = (lembda1 * piD4) / 45.0; // unfortunately lambda is a key word!
        phi2 = (phi2 * piD4) / 45.0;
        lembda2 = (lembda2 * piD4) / 45.0;

        double b = a * (1.0 - f);

        double TanU1 = (1 - f) * Math.tan(phi1);
        double TanU2 = (1 - f) * Math.tan(phi2);

        double U1 = Math.atan(TanU1);
        double U2 = Math.atan(TanU2);

        double lembda = lembda2 - lembda1;
        double last_lembda = -4000000.0; // an impossibe value
        double omega = lembda;

        // Iterate the following equations,
        //  until there is no significant change in lembda
        double Sin_sigma = 0;

        // Iterate the following equations,
        //  until there is no significant change in lembda
        double Cos_sigma = 0;
        double Cos2sigma_m = 0;
        double alpha = 0;
        double sigma = 0;
        double sqr_sin_sigma = 0;

        while ((last_lembda < -3000000.0) ||
                   ((lembda != 0) &&
                   (Math.abs((last_lembda - lembda) / lembda) > 1.0e-9))) {
            sqr_sin_sigma = Math.pow(Math.cos(U2) * Math.sin(lembda), 2) +
                            Math.pow(((Math.cos(U1) * Math.sin(U2)) -
                                     (Math.sin(U1) * Math.cos(U2) * Math.cos(lembda))),
                                     2);

            Sin_sigma = Math.sqrt(sqr_sin_sigma);

            Cos_sigma = (Math.sin(U1) * Math.sin(U2)) +
                        (Math.cos(U1) * Math.cos(U2) * Math.cos(lembda));

            sigma = Math.atan2(Sin_sigma, Cos_sigma);

            double Sin_alpha = (Math.cos(U1) * Math.cos(U2) * Math.sin(lembda)) / Math.sin(sigma);
            alpha = Math.asin(Sin_alpha);

            Cos2sigma_m = Math.cos(sigma) -
                          ((2 * Math.sin(U1) * Math.sin(U2)) / Math.pow(Math.cos(alpha),
                                                                        2));

            double C = (f / 16) * Math.pow(Math.cos(alpha), 2) * (4 +
                       (f * (4 - (3 * Math.pow(Math.cos(alpha), 2)))));

            last_lembda = lembda;

            lembda = omega +
                     ((1 - C) * f * Math.sin(alpha) * (sigma +
                     (C * Math.sin(sigma) * (Cos2sigma_m +
                     (C * Math.cos(sigma) * (-1 +
                     (2 * Math.pow(Cos2sigma_m, 2))))))));
        }

        double u2 = (Math.pow(Math.cos(alpha), 2) * ((a * a) - (b * b))) / (b * b);

        double A = 1 +
                   ((u2 / 16384) * (4096 +
                   (u2 * (-768 + (u2 * (320 - (175 * u2)))))));

        double B = (u2 / 1024) * (256 +
                   (u2 * (-128 + (u2 * (74 - (47 * u2))))));

        double delta_sigma = B * Sin_sigma * (Cos2sigma_m +
                             ((B / 4) * ((Cos_sigma * (-1 +
                             (2 * Math.pow(Cos2sigma_m, 2)))) -
                             ((B / 6) * Cos2sigma_m * (-3 +
                             (4 * sqr_sin_sigma)) * (-3 +
                             (4 * Math.pow(Cos2sigma_m, 2)))))));

        double s = b * A * (sigma - delta_sigma);

        double alpha12 = Math.atan2((Math.cos(U2) * Math.sin(lembda)),
                                    ((Math.cos(U1) * Math.sin(U2)) -
                                    (Math.sin(U1) * Math.cos(U2) * Math.cos(lembda))));

        double alpha21 = Math.atan2((Math.cos(U1) * Math.sin(lembda)),
                                    ((-Math.sin(U1) * Math.cos(U2)) +
                                    (Math.cos(U1) * Math.sin(U2) * Math.cos(lembda))));

        if (alpha12 < 0.0) {
            alpha12 = alpha12 + two_pi;
        }

        if (alpha12 > two_pi) {
            alpha12 = alpha12 - two_pi;
        }

        alpha21 = alpha21 + (two_pi / 2.0);

        if (alpha21 < 0.0) {
            alpha21 = alpha21 + two_pi;
        }

        if (alpha21 > two_pi) {
            alpha21 = alpha21 - two_pi;
        }

        alpha12 = (alpha12 * 45.0) / piD4;
        alpha21 = (alpha21 * 45.0) / piD4;

        return new GeoData(0, 0, s, alpha12, alpha21);
    }

    /**
     * Vincenty's Direct formulae
     * Given: latitude and longitude of a point (phi1, lembda1) and
     * the geodetic azimuth (alpha12)
     * and ellipsoidal distance in metres (s) to a second point,
     *
     * Calculate: the latitude and longitude of the second point (phi2, lembda2)
     * and the reverse azimuth (alpha21).
     */
	/**
	 * Returns the lat and long of projected point and reverse azimuth
	 *        given a reference point and a distance and azimuth to project.
	 *  lats, longs and azimuths are passed in decimal degrees.
	 * Returns ( phi2,  lambda2,  alpha21 ) as a tuple
	 * @param pt
	 * @param azimut
	 * @param dist
	 * @return
	 */
    protected GeoData getPointVincenty(Point2D pt, double azimut, double dist) {
        GeoData ret = new GeoData(0, 0);
        double f = 1D / proj.getDatum().getEIFlattening();
        double a = proj.getDatum().getESemiMajorAxis();
        double phi1 = pt.getY();
        double lembda1 = pt.getX();
        double alpha12 = azimut;
        double s = dist;

        double piD4 = Math.atan(1.0);
        double two_pi = piD4 * 8.0;

        phi1 = (phi1 * piD4) / 45.0;
        lembda1 = (lembda1 * piD4) / 45.0;
        alpha12 = (alpha12 * piD4) / 45.0;

        if (alpha12 < 0.0) {
            alpha12 = alpha12 + two_pi;
        }

        if (alpha12 > two_pi) {
            alpha12 = alpha12 - two_pi;
        }

        double b = a * (1.0 - f);

        double TanU1 = (1 - f) * Math.tan(phi1);
        double U1 = Math.atan(TanU1);
        double sigma1 = Math.atan2(TanU1, Math.cos(alpha12));
        double Sinalpha = Math.cos(U1) * Math.sin(alpha12);
        double cosalpha_sq = 1.0 - (Sinalpha * Sinalpha);

        double u2 = (cosalpha_sq * ((a * a) - (b * b))) / (b * b);
        double A = 1.0 +
                   ((u2 / 16384) * (4096 +
                   (u2 * (-768 + (u2 * (320 - (175 * u2)))))));
        double B = (u2 / 1024) * (256 +
                   (u2 * (-128 + (u2 * (74 - (47 * u2))))));

        // Starting with the approximation
        double sigma = (s / (b * A));

        double last_sigma = (2.0 * sigma) + 2.0; // something impossible

        // Iterate the following three equations
        //  until there is no significant change in sigma
        // two_sigma_m , delta_sigma
        double two_sigma_m = 0;

        while (Math.abs((last_sigma - sigma) / sigma) > 1.0e-9) {
            two_sigma_m = (2 * sigma1) + sigma;

            double delta_sigma = B * Math.sin(sigma) * (Math.cos(two_sigma_m) +
                                 ((B / 4) * (Math.cos(sigma) * ((-1 +
                                 (2 * Math.pow(Math.cos(two_sigma_m), 2))) -
                                 ((B / 6) * Math.cos(two_sigma_m) * (-3 +
                                 (4 * Math.pow(Math.sin(sigma), 2))) * (-3 +
                                 (4 * Math.pow(Math.cos(two_sigma_m), 2))))))));

            last_sigma = sigma;
            sigma = (s / (b * A)) + delta_sigma;
        }

        double phi2 = Math.atan2(((Math.sin(U1) * Math.cos(sigma)) +
                                 (Math.cos(U1) * Math.sin(sigma) * Math.cos(alpha12))),
                                 ((1 - f) * Math.sqrt(Math.pow(Sinalpha, 2) +
                                                      Math.pow((Math.sin(U1) * Math.sin(sigma)) -
                                                               (Math.cos(U1) * Math.cos(sigma) * Math.cos(alpha12)),
                                                               2))));

        double lembda = Math.atan2((Math.sin(sigma) * Math.sin(alpha12)),
                                   ((Math.cos(U1) * Math.cos(sigma)) -
                                   (Math.sin(U1) * Math.sin(sigma) * Math.cos(alpha12))));

        double C = (f / 16) * cosalpha_sq * (4 + (f * (4 - (3 * cosalpha_sq))));

        double omega = lembda -
                       ((1 - C) * f * Sinalpha * (sigma +
                       (C * Math.sin(sigma) * (Math.cos(two_sigma_m) +
                       (C * Math.cos(sigma) * (-1 +
                       (2 * Math.pow(Math.cos(two_sigma_m), 2))))))));

        double lembda2 = lembda1 + omega;

        double alpha21 = Math.atan2(Sinalpha,
                                    ((-Math.sin(U1) * Math.sin(sigma)) +
                                    (Math.cos(U1) * Math.cos(sigma) * Math.cos(alpha12))));

        alpha21 = alpha21 + (two_pi / 2.0);

        if (alpha21 < 0.0) {
            alpha21 = alpha21 + two_pi;
        }

        if (alpha21 > two_pi) {
            alpha21 = alpha21 - two_pi;
        }

        phi2 = (phi2 * 45.0) / piD4;
        lembda2 = (lembda2 * 45.0) / piD4;
        alpha21 = (alpha21 * 45.0) / piD4;

        ret.pt = new Point2D.Double(lembda2, phi2);
        ret.azimut = alpha21;

        return ret;
    }

    /* (non-Javadoc)
	 * @see org.cresques.impl.cts.GeoCalc#surfaceSphere(java.awt.geom.Point2D, java.awt.geom.Point2D, java.awt.geom.Point2D)
	 */
    public double surfaceSphere(Point2D pt1, Point2D pt2, Point2D pt3) {
        double sup = -1;
        double A = distanceGeo(pt1, pt2);
        double B = distanceGeo(pt2, pt3);
        double C = distanceGeo(pt3, pt1);
        sup = (((A + B + C) - Math.toRadians(180D)) * Math.PI * proj.getDatum()
                                                                    .getESemiMajorAxis()) / Math.toRadians(180D);

        return sup;
    }

    /*
     * Fórmulas de Vincenty's.
     * (pasadas de http://wegener.mechanik.tu-darmstadt.de/GMT-Help/Archiv/att-8710/Geodetic_py
     * http://www.icsm.gov.au/icsm/gda/gdatm/index.html
     */
    protected class GeoData {
        Point2D pt;
        double azimut;
        double revAzimut;
        double dist;

        public GeoData(double x, double y) {
            pt = new Point2D.Double(x, y);
            azimut = revAzimut = dist = 0;
        }

        public GeoData(double x, double y, double dist, double azi, double rAzi) {
            pt = new Point2D.Double(x, y);
            azimut = azi;
            revAzimut = rAzi;
            this.dist = dist;
        }
    }
}

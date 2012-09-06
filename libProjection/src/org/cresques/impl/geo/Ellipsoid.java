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


public class Ellipsoid implements IDatum {
    //'WGS72':	(6378135.0, 298.26, 1972),
    public static final Ellipsoid wgs72 = new Ellipsoid("WGS72", 6378135.0,
                                                        298.26);

    //'WGS84':	(6378137.0, 298.257223563, 1984),
    public static final Ellipsoid wgs84 = new Ellipsoid("WGS84", 6378137.0,
                                                        298.257223563);

    //'GRS80':	(6378137.0, 298.257222101, 1980), // , 6356752.314
    public static final Ellipsoid grs80 = new Ellipsoid("GRS80", 6378137.0,
                                                        298.257222101);

    //'Hayford':	(6378388.0, 297.0, 1909),
    public static final Ellipsoid hayford = new Ellipsoid("Hayford", 6378388.0,
                                                          297.0);

    //'Struve':	(6378298.0, 299.73, 1860),
    public static final Ellipsoid struve = new Ellipsoid("Struve", 6378298.0,
                                                         299.73);

    // ed50 ... ¿hayford?
    public static final Ellipsoid ed50 = new Ellipsoid("ED50", 6378388.0, 297.0);

    // Clarcke 1866	a = 6378206.4;	f = 294.9786982;
    public static final Ellipsoid clarke66 = new Ellipsoid("CLARKE 1866",
                                                           6378206.4,
                                                           294.9786982);
    private String pName = null;
    private double pMajor = 0.0;
    private double pFlat = 0.0;
    public double a;
    public double b;
    public double f;
    public double e;
    public double e2;
    public double ep;
    public double ep2;

    public Ellipsoid(String name, double major, double flat) {
        pName = name;
        pMajor = major;
        pFlat = flat;
        a = pMajor;
        f = pFlat;
        ainvto();
    }

    public String getName() {
        return pName;
    }

    private void ainvto() {
        b = (a * (pFlat - 1.0D)) / pFlat;
        e2 = ((2D * pFlat) - 1.0D) / (pFlat * pFlat);
        e = Math.sqrt(e2);
        ep2 = e2 / (1.0D - e2);
        ep = Math.sqrt(ep2);
    }

    private void abto() {
        if (Math.abs(a - b) < 9.9999999999999995E-021D) {
            pFlat = 0.0D;
            e2 = 0.0D;
        } else {
            pFlat = a / (a - b);
            e2 = ((2D * pFlat) - 1.0D) / (pFlat * pFlat);
        }

        e = Math.sqrt(e2);
        ep2 = e2 / (1.0D - e2);
        ep = Math.sqrt(ep2);
    }

    public double[] getParam() {
        double[] elipar = new double[8];
        elipar[1] = pMajor;
        elipar[2] = pFlat;
        elipar[3] = b;
        elipar[4] = e;
        elipar[5] = e2;
        elipar[6] = ep;
        elipar[7] = ep2;

        return elipar;
    }

    /* (non-Javadoc)
     * @see org.cresques.cts.IDatum#getSemiMajorAxis()
     */
    public double getESemiMajorAxis() {
        return pMajor;
    }

    /* (non-Javadoc)
     * @see org.cresques.cts.IDatum#getEIFlattening()
     */
    public double getEIFlattening() {
        return pFlat;
    }
}

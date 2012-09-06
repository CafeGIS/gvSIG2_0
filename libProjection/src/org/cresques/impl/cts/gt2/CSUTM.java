/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-7.
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
package org.cresques.impl.cts.gt2;

import org.cresques.cts.UTM;
import org.geotools.cs.AxisInfo;
import org.geotools.cs.Projection;
import org.geotools.units.Unit;
import org.opengis.referencing.FactoryException;


/**
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class CSUTM extends CoordSys implements UTM {
    public CSUTM(CSDatum datum, int zone) {
        super(datum);
        init(datum, zone, "N");
    }
    
    public CSUTM(CSDatum datum, int zone, String ns) {
        super(datum);
        init(datum, zone, ns);
    }
 
    public void init(CSDatum datum, int zone, String ns) {
        Unit linearUnit = Unit.METRE;

        javax.media.jai.ParameterList params = csFactory.createProjectionParameterList("Transverse_Mercator");
        params.setParameter("semi_major",
                            datum.getDatum().getEllipsoid().getSemiMajorAxis());
        params.setParameter("semi_minor",
                            datum.getDatum().getEllipsoid().getSemiMinorAxis());
        params.setParameter("central_meridian", (double) ((zone * 6) - 183));
        params.setParameter("latitude_of_origin", 0.0);
        params.setParameter("scale_factor", 0.9996);
        params.setParameter("false_easting", 500000.0);
        if (ns.toUpperCase().compareTo("S") == 0)
        	params.setParameter("false_northing", 10000000.0);
        else
        	params.setParameter("false_northing", 0.0);

        try {
            Projection projection = csFactory.createProjection("UTM" + zone,
                                                               "Transverse_Mercator",
                                                               params);
            projCS = csFactory.createProjectedCoordinateSystem(projection.getName()
                                                                         .toString(),
                                                               geogCS,
                                                               projection,
                                                               linearUnit,
                                                               AxisInfo.X,
                                                               AxisInfo.Y);
        } catch (FactoryException e) {
            // TODO Bloque catch generado automáticamente
            e.printStackTrace();
        }
    }

    public double getScale(double minX, double maxX, double w, double dpi) {
        double scale = super.getScale(minX, maxX, w, dpi);

        if (projCS != null) { // Es geográfico; calcula la escala.
            scale = ((maxX - minX) * // metros
                    (dpi / 2.54 * 100.0)) / // px / metro
                    w; // pixels
        }

        return scale;
    }

    public String toString() {
        return projCS.toString();
    }
}

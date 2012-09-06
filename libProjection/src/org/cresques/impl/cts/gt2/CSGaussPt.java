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
package org.cresques.impl.cts.gt2;

import org.geotools.cs.AxisInfo;
import org.geotools.cs.GeographicCoordinateSystem;
import org.geotools.cs.Projection;

import org.geotools.measure.Angle;

import org.geotools.units.Unit;

import org.opengis.referencing.FactoryException;


/**
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 *
 */
public class CSGaussPt extends CoordSys {
    public static CSGaussPt hgd73 = new CSGaussPt(CSDatum.d73);

    public CSGaussPt(CSDatum datum) {
        super(datum);
        geogCS = new GeographicCoordinateSystem(datum.getName(null),
                                                datum.getDatum());

        Unit linearUnit = Unit.METRE;

        javax.media.jai.ParameterList params = csFactory.createProjectionParameterList("Oblique_Stereographic");
        params.setParameter("semi_major",
                            datum.getDatum().getEllipsoid().getSemiMajorAxis());
        params.setParameter("semi_minor",
                            datum.getDatum().getEllipsoid().getSemiMinorAxis());
        params.setParameter("central_meridian",
                            (new Angle("8°07'54.862\"W")).degrees());

        //params.setParameter("longitude_of_origin", (new Angle("8º07'54.862\"")).degrees());
        params.setParameter("latitude_of_origin",
                            (new Angle("39°40'N")).degrees());
        params.setParameter("scale_factor", 1.0);
        params.setParameter("false_easting", 0D); //180598D);
        params.setParameter("false_northing", 0D); //-86990D);

        try {
            Projection projection = csFactory.createProjection("GAUSS",
                                                               "Oblique_Stereographic",
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

    public String toString() {
        return projCS.toString();
    }
}

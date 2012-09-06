/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002-2005, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.referencing.operation.projection;

// J2SE dependencies and extensions
import java.awt.geom.Point2D;
import java.util.Collection;

import javax.units.SI;
import javax.units.NonSI;
import javax.units.Unit;

import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.XMath;
import org.geotools.resources.cts.ResourceKeys;
import org.geotools.resources.cts.Resources;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;


public class IdrLoximuthal extends MapProjection {
    protected final double latitudeOfCenter;
    protected final double longitudeOfCenter;
    protected IdrLoximuthal(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
        final Collection expected = getParameterDescriptors().descriptors();
        if (expected.contains(Provider.LATITUDE_OF_CENTER)) {
        	latitudeOfCenter = Math.abs(doubleValue(expected,
                                        Provider.LATITUDE_OF_CENTER, parameters));
            ensureLatitudeInRange(Provider.LATITUDE_OF_CENTER, latitudeOfCenter, false);
        } else {
            // standard parallel is the equator (Plate Carree or Equirectangular)
        	latitudeOfCenter = Double.NaN;
        }
        if (expected.contains(Provider.LONGITUDE_OF_CENTER)) {
        	longitudeOfCenter = Math.abs(doubleValue(expected,
                                        Provider.LONGITUDE_OF_CENTER, parameters));
            ensureLongitudeInRange(Provider.LONGITUDE_OF_CENTER, longitudeOfCenter, false);
        } else {
            // standard parallel is the equator (Plate Carree or Equirectangular)
        	longitudeOfCenter = Double.NaN;
        }
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * {@inheritDoc}
     */
    public ParameterValueGroup getParameterValues() {
        final Collection expected = getParameterDescriptors().descriptors();
        final ParameterValueGroup values = super.getParameterValues();
        set(expected, Provider.LATITUDE_OF_CENTER,       values, latitudeOfCenter      );
        set(expected, Provider.LONGITUDE_OF_CENTER,      values, longitudeOfCenter       );
        return values;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    protected Point2D transformNormalized(final double lambda, final double phi, Point2D ptDst)
            throws ProjectionException
    {
        return null;
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinate
     * and stores the result in {@code ptDst}.
     */
    protected Point2D inverseTransformNormalized(final double x, final double y, Point2D ptDst)
            throws ProjectionException
    {
        return null;
    }




    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDER                                 ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@link org.geotools.referencing.operation.MathTransformProvider}
     * for an {@link IdrMillerCylindrical krovak} projection.
     *
     * @author jezekjan
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * The operation parameter descriptor for the {@linkPlain #latitudeOfOrigin
         * latitude of origin} parameter value. Valid values range is from -90 to 90.
         * Default value is 49.5.
         */
        public static final ParameterDescriptor LATITUDE_OF_CENTER = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,     "latitude_of_center"),
                    new NamedIdentifier(CitationImpl.OGC,     "central_parallel"),
                    new NamedIdentifier(CitationImpl.OGC,     "Central parallel"),
                    new NamedIdentifier(CitationImpl.OGC,     "central-parallel"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Latitude of projection center"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Latitude of projection centre"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Spherical latitude of origin"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Latitude of natural origin"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "CenterLat")
                }, 0.0, -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@linkPlain #centralMeridian central
         * meridian} parameter value. Valid values range is from -180 to 180. Default value
         * is 2450' (= 4250' from Ferro prime meridian).
         */
       public static final ParameterDescriptor LONGITUDE_OF_CENTER = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,     "longitude_of_center"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Longitude of projection center"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Longitude of projection centre"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Spherical longitude of origin"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Longitude of natural origin"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "CenterLong")
                }, 0.0, -180, 180, NonSI.DEGREE_ANGLE);


        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
            new NamedIdentifier(CitationImpl.OGC,     "Miller_Cylindrical"),
            new NamedIdentifier(CitationImpl.GEOTIFF, "MillerCylindrical"),
            new NamedIdentifier(CitationImpl.EPSG,    "Miller Cylindrical"),
            new NamedIdentifier(new CitationImpl("IDR"), "IDR")//,
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR, SEMI_MINOR,
                    LATITUDE_OF_CENTER, LONGITUDE_OF_CENTER,
                    FALSE_EASTING, FALSE_NORTHING
                });

        /**
         * Constructs a new provider. 
         */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Returns the operation type for this map projection.
         */
        protected Class getOperationType() {
            return CylindricalProjection.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            return new IdrLoximuthal(parameters);
        }
    }

}

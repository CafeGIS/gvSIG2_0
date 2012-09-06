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
import org.opengis.referencing.operation.ConicProjection;
import org.opengis.referencing.operation.MathTransform;


/**
 * Krovak Oblique Conformal Conic projection (EPSG code 9819). This projection is
 * used in the Czech Republic and Slovakia under the name 'Krovak' projection. The
 * geographic coordinates on the ellipsoid are first reduced to conformal
 * coordinates on the conformal (Gaussian) sphere. These spherical coordinates
 * are then projected onto the oblique cone and converted to grid coordinates.
 * The pseudo standard parallel is defined on the conformal sphere after its
 * rotation, to obtain the oblique aspect of the projection. It is then the
 * parallel on this sphere at which the map projection is true to scale; on
 * the ellipsoid it maps as a complex curve.
 *
 * <p>The compulsory parameters are just the ellipsoid characteristics.
 * All other parameters are optional and have defaults to match the common
 * usage with Krovak projection.</p>
 *
 * <p>In general the axis of Krovak projection are defined as westing and
 * southing (not easting and northing) and they are also reverted, so if the
 * value of projected coordinates should (and in <var>y</var>, <var>x</var>
 * order in Krovak) be positive the 'Axis' parameter for projection should be
 * defined explicitly like this (in wkt):</p>
 * 
 * <pre>PROJCS["S-JTSK (Ferro) / Krovak",  
 *         .                                                              
 *         .                                                              
 *         .
 *                                                                       
 *     PROJECTION["Krovak"]                                         
 *     PARAMETER["semi_major", 6377397.155],  
 *     PARAMETER["semi_minor", 6356078.963],                   
 *     UNIT["meter",1.0],                                  
 *     AXIS["x", WEST],                     
 *     AXIS["y", SOUTH]]                                              
 *     </pre>Axis in Krovak:
 * <pre>                                                              
 *   y<------------------+                                                                                                  
 *                       |                                             
 *    Czech. Rep.        | 
 *                       |                                                                   
 *                       x                              
 * </pre>
 * <p>By default, the axis are 'easting, northing' so the values of projected coordinates
 * are negative and in (and in <var>y</var>, <var>x</var> order in Krovak - it is cold
 * Krovak GIS version).</p>
 *
 * <p><strong>References:</strong>
 *  <ul>
 *      <li>Proj-4.4.7 available at <A HREF="http://www.remotesensing.org/proj">www.remotesensing.org/proj</A><br>
 *      Relevant files is: {@code PJ_krovak.c}</li>
 *      <li>"Coordinate Conversions and Transformations including Formulas" available at, <A
 *      HREF="http://www.remotesensing.org/geotiff/proj_list/guid7.html">http://www.remotesensing.org/geotiff/proj_list/guid7.html</A></li>
 *  </ul>
 * </p>
 *
 * @since 2.4
 * @version $Id: IdrKrovak.java 12357 2007-06-27 09:05:41Z dguerrero $
 * @source $URL: http://svn.geotools.org/geotools/tags/2.3.0/module/referencing/src/org/geotools/referencing/operation/projection/Krovak.java $
 * @author Jan Jezek
 * @author Martin Desruisseaux
 *
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/krovak.html">Krovak on
 *      RemoteSensing.org </A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/guid7.html">Krovak on "Coordinate
 *      Conversions and Transformations including Formulas"</A>
 */
public class IdrKrovak extends MapProjection {
    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAXIMUM_ITERATIONS = 15;
    
    /**
     * When to stop the iteration.
     */
    private static final double ITERATION_TOLERANCE = 1E-11;

    /**
     * Azimuth of the centre line passing through the centre of the projection.
     * This is equals to the co-latitude of the cone axis at point of intersection
     * with the ellipsoid.
     */
    protected final double azimuth;

    /**
     * Latitude of pseudo standard parallel.
     */
    protected final double pseudoStandardParallel;

    /**
     * Useful variables calculated from parameters defined by user.
     */
    private final double sinAzim, cosAzim, n, tanS2, alfa, hae, k1, ka, ro0, rop;

    /**
     * Useful constant - 45 in radians.
     */
    private static final double s45 = 0.785398163397448;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected IdrKrovak(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
        final Collection expected = getParameterDescriptors().descriptors();
        // Fetch parameters from user input.
        latitudeOfOrigin       = doubleValue(expected, Provider.LATITUDE_OF_CENTER,       parameters);
        centralMeridian        = doubleValue(expected, Provider.LONGITUDE_OF_CENTER,      parameters);
        azimuth                = doubleValue(expected, Provider.AZIMUTH,                  parameters);
        pseudoStandardParallel = doubleValue(expected, Provider.PSEUDO_STANDARD_PARALLEL, parameters);
        scaleFactor            = doubleValue(expected, Provider.SCALE_FACTOR,             parameters);
        ensureLatitudeInRange (Provider.LATITUDE_OF_CENTER,  latitudeOfOrigin, false);
        ensureLongitudeInRange(Provider.LONGITUDE_OF_CENTER, centralMeridian,  false);

        // Calculates useful constants.
        sinAzim = Math.sin(azimuth);
        cosAzim = Math.cos(azimuth);
        n       = Math.sin(pseudoStandardParallel);
        tanS2   = Math.tan(pseudoStandardParallel / 2 + s45);

        final double sinLat, cosLat, cosL2, u0;
        sinLat = Math.sin(latitudeOfOrigin);
        cosLat = Math.cos(latitudeOfOrigin);
        cosL2  = cosLat * cosLat;
        alfa   = Math.sqrt(1 + ((excentricitySquared * (cosL2*cosL2)) / (1 - excentricitySquared)));
        hae    = alfa * excentricity / 2;
        u0     = Math.asin(sinLat / alfa);

        final double g, esl;
        esl = excentricity * sinLat;
        g   = Math.pow((1 - esl) / (1 + esl), (alfa * excentricity) / 2);
        k1  = Math.pow(Math.tan(latitudeOfOrigin/2 + s45), alfa) * g / Math.tan(u0/2 + s45);
        ka  = Math.pow(1 / k1, -1 / alfa);

        final double radius;
        radius = Math.sqrt(1 - excentricitySquared) / (1 - (excentricitySquared * (sinLat * sinLat)));

        ro0 = scaleFactor * radius / Math.tan(pseudoStandardParallel);
        rop = ro0 * Math.pow(tanS2, n);
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
        set(expected, Provider.LATITUDE_OF_CENTER,       values, latitudeOfOrigin      );
        set(expected, Provider.LONGITUDE_OF_CENTER,      values, centralMeridian       );
        set(expected, Provider.AZIMUTH,                  values, azimuth               );
        set(expected, Provider.PSEUDO_STANDARD_PARALLEL, values, pseudoStandardParallel);
        set(expected, Provider.SCALE_FACTOR,             values, scaleFactor           );
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
        final double esp = excentricity * Math.sin(phi);
        final double gfi = Math.pow(((1. - esp) / (1. + esp)), hae);
        final double u   = 2 * (Math.atan(Math.pow(Math.tan(phi/2 + s45), alfa) / k1 * gfi) - s45);
        final double deltav = -lambda * alfa;
        final double cosU = Math.cos(u);
        final double s = Math.asin((cosAzim * Math.sin(u)) + (sinAzim * cosU * Math.cos(deltav)));
        final double d = Math.asin(cosU * Math.sin(deltav) / Math.cos(s));
        final double eps = n * d;
        final double ro = rop / Math.pow(Math.tan(s/2 + s45), n);

        /* x and y are reverted  */
        final double y = -(ro * Math.cos(eps));
        final double x = -(ro * Math.sin(eps));

        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinate
     * and stores the result in {@code ptDst}.
     */
    protected Point2D inverseTransformNormalized(final double x, final double y, Point2D ptDst)
            throws ProjectionException
    {
        // x -> southing, y -> westing
        final double ro  = XMath.hypot(x, y);
        final double eps = Math.atan2(-x, -y);
        final double d   = eps / n;
        final double s   = 2 * (Math.atan(Math.pow(ro0/ro, 1/n) * tanS2) - s45);
        final double cs  = Math.cos(s);
        final double u   = Math.asin((cosAzim * Math.sin(s)) - (sinAzim * cs * Math.cos(d)));
        final double kau = ka * Math.pow(Math.tan((u / 2.) + s45), 1 / alfa);
        final double deltav = Math.asin((cs * Math.sin(d)) / Math.cos(u));
        final double lambda = -deltav / alfa;
        double phi = 0;
        double fi1 = u;

        // iteration calculation
        for (int i=MAXIMUM_ITERATIONS;;) {
            fi1 = phi;
            final double esf = excentricity * Math.sin(fi1);
            phi = 2. * (Math.atan(kau * Math.pow((1. + esf) / (1. - esf), excentricity/2.)) - s45);
            if (Math.abs(fi1 - phi) <= ITERATION_TOLERANCE) {
                break;
            }
            if (--i < 0) {
            	throw new ProjectionException(Resources.format(ResourceKeys.ERROR_NO_CONVERGENCE));
            }
        }

        if (ptDst != null) {
            ptDst.setLocation(lambda, phi);
            return ptDst;
        }
        return new Point2D.Double(lambda, phi);
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
     * for an {@link IdrKrovak krovak} projection.
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
                    new NamedIdentifier(CitationImpl.EPSG,    "Latitude of projection center"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Latitude of projection centre"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "CenterLat")
                }, 49.5, -90, 90, NonSI.DEGREE_ANGLE);

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
                    new NamedIdentifier(CitationImpl.GEOTIFF, "CenterLong")
                }, 42.5-17.66666666666667, -180, 180, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@linkPlain #azimuth azimuth} parameter
         * value. Valid values range is from -90 to 90. Default value is 30.28813972222.
         */
        public static final ParameterDescriptor AZIMUTH = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,     "azimuth"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Azimuth of the center line"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Azimuth of initial line"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "AzimuthAngle")
                }, 30.28813972222222, 0, 360, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the pseudo {@linkplain #pseudoStandardParallel
         * pseudi standard parallel} parameter value. Valid values range is from -90 to 90.
         * Default value is 78.5.
         */
        public static final ParameterDescriptor PSEUDO_STANDARD_PARALLEL =
                createDescriptor(new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,  "pseudo_standard_parallel_1"),
                    new NamedIdentifier(CitationImpl.EPSG, "Latitude of Pseudo Standard Parallel")
                }, 78.5, -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@link #scaleFactor scaleFactor}
         * parameter value. Valid values range is from 0 to infinity. Default value is 1.
         */
        public static final ParameterDescriptor SCALE_FACTOR = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,  "scale_factor"),
                    new NamedIdentifier(CitationImpl.EPSG, "Scale factor on the pseudo standard line"),
                    new NamedIdentifier(CitationImpl.EPSG, "Scale factor on pseudo standard parallel"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "ScaleAtCenter")
                }, 0.9999, 0, Double.POSITIVE_INFINITY, Unit.ONE);

        /**
         * The operation parameter descriptor for the {@link #scaleFactor scaleFactor}
         * parameter value. Valid values range is from 0 to infinity. Default value is 1.
         */
        public static final ParameterDescriptor FALSEEASTING = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.EPSG, "Easting at projection centre")
                }, 0.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, SI.METER);

        /**
         * The operation parameter descriptor for the {@link #scaleFactor scaleFactor}
         * parameter value. Valid values range is from 0 to infinity. Default value is 1.
         */
        public static final ParameterDescriptor FALSENORTHING = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.EPSG, "Northing at projection centre")
                }, 0.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, SI.METER);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
            new NamedIdentifier(CitationImpl.OGC,     "Krovak"),
            new NamedIdentifier(CitationImpl.GEOTIFF, "Krovak"),
            new NamedIdentifier(CitationImpl.EPSG,    "Krovak Oblique Conformal Conic"),
            new NamedIdentifier(CitationImpl.EPSG,    "Krovak Oblique Conic Conformal"),
            new NamedIdentifier(CitationImpl.EPSG,    "9819"),
            new NamedIdentifier(new CitationImpl("IDR"), "IDR")
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR, SEMI_MINOR, LATITUDE_OF_CENTER, LONGITUDE_OF_CENTER,
                    AZIMUTH, PSEUDO_STANDARD_PARALLEL, SCALE_FACTOR,
                    FALSEEASTING, FALSENORTHING
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
            return ConicProjection.class;
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
            return new IdrKrovak(parameters);
        }
    }

    /**
     * Returns a hash value for this projection.
     */
    public int hashCode() {
        final long code = Double.doubleToLongBits(azimuth) ^
                          Double.doubleToLongBits(pseudoStandardParallel);
        return ((int)code ^ (int)(code >>> 32)) + 37*super.hashCode();
    }
    
    /**
     * Compares the specified object with this map projection for equality.
     */
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final IdrKrovak that = (IdrKrovak) object;
            return equals(azimuth, that.azimuth) &&
                   equals(pseudoStandardParallel, that.pseudoStandardParallel);
        }
        return false;
    }
}

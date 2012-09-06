/*
 * Geotools 2 - OpenSource mapping toolkit
 * (C) 2003, Geotools Project Managment Committee (PMC)
 * (C) 2001, Institut de Recherche pour le Dveloppement
 * (C) 1999, Fisheries and Oceans Canada
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 *    This package contains formulas from the PROJ package of USGS.
 *    USGS's work is fully acknowledged here.
 */
package org.geotools.referencing.operation.projection;

// J2SE dependencies and extensions
import java.awt.geom.Point2D;
import java.util.Collection;
import javax.units.NonSI;

// OpenGIS dependencies
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;

// Geotools dependencies
import org.geotools.measure.Latitude;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.resources.cts.ResourceKeys;
import org.geotools.resources.cts.Resources;


/**
 * Mercator Cylindrical Projection. The parallels and the meridians are straight lines and
 * cross at right angles; this projection thus produces rectangular charts. The scale is true
 * along the equator (by default) or along two parallels equidistant of the equator (if a scale
 * factor other than 1 is used). This projection is used to represent areas close to the equator.
 * It is also often used for maritime navigation because all the straight lines on the chart are
 * <em>loxodrome</em> lines, i.e. a ship following this line would keep a constant azimuth on its
 * compass.
 * <br><br>
 *
 * This implementation handles both the 1 and 2 stardard parallel cases.
 * For <code>Mercator_1SP</code> (EPSG code 9804), the line of contact is the equator. 
 * For <code>Mercator_2SP</code> (EPSG code 9805) lines of contact are symmetrical 
 * about the equator.
 * <br><br>
 *
 * <strong>References:</strong><ul>
 *   <li>John P. Snyder (Map Projections - A Working Manual,<br>
 *       U.S. Geological Survey Professional Paper 1395, 1987)</li>
 *   <li>"Coordinate Conversions and Transformations including Formulas",<br>
 *       EPSG Guidence Note Number 7, Version 19.</li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/MercatorProjection.html">Mercator projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/mercator_1sp.html">"mercator_1sp" on Remote Sensing</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/mercator_2sp.html">"mercator_2sp" on Remote Sensing</A>
 * 
 * @version $Id: IdrMercator.java 12357 2007-06-27 09:05:41Z dguerrero $
 * @author Andr Gosselin
 * @author Martin Desruisseaux
 * @author Rueben Schulz
 */
public class IdrMercator extends MapProjection {
    /**
     * Standard Parallel used for the <code>Mercator_2SP</code> case.
     * Set to {@link Double#NaN} for the <code>Mercator_1SP</code> case.
     */
    protected final double standardParallel;
    //protected final double latitudeOfOrigin;

    /**
     * The {@link MathTransformProvider} for a {@link IdrMercator} 1SP projection.
     *
     * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/mercator_1sp.html">"mercator_1sp" on Remote Sensing</A>
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     *
     * @version $Id: IdrMercator.java 12357 2007-06-27 09:05:41Z dguerrero $
     * @author Martin Desruisseaux
     * @author Rueben Schulz
     */
    public static final class Provider1SP extends AbstractProvider {
        /**
         * The parameters group.
         */
    	public static final ParameterDescriptor LATITUDE_OF_ORIGIN = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,     "latitude_of_origin"),
                    new NamedIdentifier(CitationImpl.EPSG,    "CenterLat"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Latitude of projection centre"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "NatOriginLat"),
                    new NamedIdentifier(CitationImpl.EPSG,    "FalseOriginLat"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Latitude of false origin"),		
                    new NamedIdentifier(CitationImpl.EPSG,    "Latitude of natural origin"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Latitude of projection centre"),
                    new NamedIdentifier(CitationImpl.EPSG,    "ProjCenterLat")
                }, 0.0, -90.0, 90.0, NonSI.DEGREE_ANGLE);

    	static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(CitationImpl.OGC,      "Mercator_1SP"),
                new NamedIdentifier(CitationImpl.EPSG,     "Mercator (1SP)"),
                new NamedIdentifier(CitationImpl.EPSG,     "9804"),
                new NamedIdentifier(CitationImpl.GEOTIFF,  "CT_Mercator"),
                new NamedIdentifier(CitationImpl.GEOTOOLS, Resources.formatInternational(
                                                           ResourceKeys.CYLINDRICAL_MERCATOR_PROJECTION)),
                new NamedIdentifier(new CitationImpl("IDR"), "IDR")
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,       SEMI_MINOR,
                CENTRAL_MERIDIAN, SCALE_FACTOR,
                LATITUDE_OF_ORIGIN,
                FALSE_EASTING,    FALSE_NORTHING
            });

        /**
         * Constructs a new provider. 
         */
        public Provider1SP() {
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
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final Collection descriptors = PARAMETERS.descriptors();
            if (isSpherical(parameters)) {
                return new Spherical(parameters, descriptors);
            } else {
                return new IdrMercator (parameters, descriptors);
            }
        }
    }

    /**
     * The {@link MathTransformProvider} for a {@link IdrMercator} 2SP projection.
     *
     * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/mercator_2sp.html">"mercator_2sp" on Remote Sensing</A>
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     *
     * @version $Id: IdrMercator.java 12357 2007-06-27 09:05:41Z dguerrero $
     * @author Martin Desruisseaux
     * @author Rueben Schulz
     */
    public static final class Provider2SP extends AbstractProvider {
        /**
         * The operation parameter descriptor for the {@link #standardParallel standard parallel}
         * parameter value. Valid values range is from -90 to 90. Default value is 0.
         */
        public static final ParameterDescriptor STANDARD_PARALLEL = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,      "standard_parallel_1"),
                    new NamedIdentifier(CitationImpl.EPSG,     "Latitude of 1st standard parallel"),
                    new NamedIdentifier(CitationImpl.GEOTIFF,  "StdParallel1")
                },
                0, -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(CitationImpl.OGC,      "Mercator_2SP"),
                new NamedIdentifier(CitationImpl.EPSG,     "Mercator (2SP)"),
                new NamedIdentifier(CitationImpl.EPSG,     "9805"),
                new NamedIdentifier(CitationImpl.GEOTIFF,  "CT_Mercator"),
                new NamedIdentifier(CitationImpl.ESRI,     "Mercator"),
                new NamedIdentifier(CitationImpl.GEOTOOLS, Resources.formatInternational(
                                                           ResourceKeys.CYLINDRICAL_MERCATOR_PROJECTION)),
                new NamedIdentifier(new CitationImpl("IDR"), "IDR")
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,       SEMI_MINOR,
                CENTRAL_MERIDIAN, STANDARD_PARALLEL,
                FALSE_EASTING,    FALSE_NORTHING
            });

        /**
         * Constructs a new provider. 
         */
        public Provider2SP() {
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
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final Collection descriptors = PARAMETERS.descriptors();
            if (isSpherical(parameters)) {
                return new Spherical(parameters, descriptors);
            } else {
                return new IdrMercator (parameters, descriptors);
            }
        }
    }


    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected IdrMercator(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        this(parameters, getDescriptor(parameters).descriptors());
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     */
    private static ParameterDescriptorGroup getDescriptor(final ParameterValueGroup parameters) {
        try {
            parameters.parameter(Provider2SP.STANDARD_PARALLEL.getName().getCode());
            return Provider2SP.PARAMETERS;
        } catch (ParameterNotFoundException ignore) {
            return Provider1SP.PARAMETERS;
        }
    }

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @param  expected The expected parameter descriptors.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    IdrMercator(final ParameterValueGroup parameters, final Collection expected)
            throws ParameterNotFoundException
    {
        //Fetch parameters 
        super(parameters, expected);
        if (expected.contains(Provider2SP.STANDARD_PARALLEL)) {
            // scaleFactor is not a parameter in the Mercator_2SP case and is computed from
            // the standard parallel.   The super-class constructor should have initialized
            // 'scaleFactor' to 1. We still use the '*=' operator rather than '=' in case a
            // user implementation still provides a scale factor for its custom projections.
            standardParallel = Math.abs(doubleValue(expected,
                                        Provider2SP.STANDARD_PARALLEL, parameters));
            ensureLatitudeInRange(Provider2SP.STANDARD_PARALLEL, standardParallel, false);
            if (isSpherical) {
                scaleFactor *= Math.cos(standardParallel);
            }  else {
                scaleFactor *= msfn(Math.sin(standardParallel),
                                    Math.cos(standardParallel));
            }
            globalScale = scaleFactor*semiMajor;
        } else {
            // No standard parallel. Instead, uses the scale factor explicitely provided.
            standardParallel = Double.NaN;
        }
        //assert latitudeOfOrigin == 0 : latitudeOfOrigin;
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Double.isNaN(standardParallel) ? Provider1SP.PARAMETERS
                                              : Provider2SP.PARAMETERS;
    }

    /**
     * {@inheritDoc}
     */
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        if (!Double.isNaN(standardParallel)) {
            final Collection expected = getParameterDescriptors().descriptors();
            set(expected, Provider2SP.STANDARD_PARALLEL, values, standardParallel);
        }
        return values;
    }
    
    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinate (units in radians)
     * and stores the result in <code>ptDst</code> (linear distance on a unit sphere).
     */
    protected Point2D transformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException
    {
        if (Math.abs(y) > (Math.PI/2 - EPS)) {
            throw new ProjectionException(Resources.format(
                    ResourceKeys.ERROR_POLE_PROJECTION_$1, new Latitude(Math.toDegrees(y))));
        }

        y = - Math.log(tsfn(y, Math.sin(y)));

        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }
    
    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinate
     * and stores the result in <code>ptDst</code>.
     */
    protected Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException
    {
        y = Math.exp(-y);
        y = cphi2(y);

        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }


    /**
     * Provides the transform equations for the spherical case of the Mercator projection.
     *
     * @version $Id: IdrMercator.java 12357 2007-06-27 09:05:41Z dguerrero $
     * @author Martin Desruisseaux
     * @author Rueben Schulz
     */
    private static final class Spherical extends IdrMercator {
        /**
         * Constructs a new map projection from the suplied parameters.
         *
         * @param  parameters The parameter values in standard units.
         * @param  expected The expected parameter descriptors.
         * @throws ParameterNotFoundException if a mandatory parameter is missing.
         */
        protected Spherical(final ParameterValueGroup parameters, final Collection expected)
                throws ParameterNotFoundException
        {
            super(parameters, expected);
            //assert isSpherical;
	}

	/**
	 * Transforms the specified (<var>x</var>,<var>y</var>) coordinate
         * and stores the result in <code>ptDst</code> using equations for a Sphere.
	 */
        protected Point2D transformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            if (Math.abs(y) > (Math.PI/2 - EPS)) {
                throw new ProjectionException(Resources.format(
                        ResourceKeys.ERROR_POLE_PROJECTION_$1, new Latitude(Math.toDegrees(y))));
            }
            // Compute using ellipsoidal formulas, for comparaison later.
            //assert (ptDst = super.transformNormalized(x, y, ptDst)) != null;
          
            y = Math.log(Math.tan((Math.PI/4) + 0.5*y));

            //assert Math.abs(ptDst.getX()-x) <= EPS*globalScale : x;
            //assert Math.abs(ptDst.getY()-y) <= EPS*globalScale : y;
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }

        /**
         * Transforms the specified (<var>x</var>,<var>y</var>) coordinate
         * and stores the result in <code>ptDst</code> using equations for a sphere.
         */
        protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            // Compute using ellipsoidal formulas, for comparaison later.
            //assert (ptDst = super.inverseTransformNormalized(x, y, ptDst)) != null;

            y = (Math.PI/2) - 2.0*Math.atan(Math.exp(-y));

            //assert Math.abs(ptDst.getX()-x) <= EPS : x;
            //assert Math.abs(ptDst.getY()-y) <= EPS : y;
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
	}      
    }


    /**
     * Returns a hash value for this projection.
     */
    public int hashCode() {
        final long code = Double.doubleToLongBits(standardParallel);
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
            final IdrMercator that = (IdrMercator) object;
            return equals(this.standardParallel,  that.standardParallel);
        }
        return false;
    } 
}

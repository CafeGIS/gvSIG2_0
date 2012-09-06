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


public class IdrWinkelII extends MapProjection {
    protected final double standardParallel;
    protected IdrWinkelII(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
        final Collection expected = getParameterDescriptors().descriptors();
        if (expected.contains(Provider.STANDARD_PARALLEL)) {
        	standardParallel = Math.abs(doubleValue(expected,
                                        Provider.STANDARD_PARALLEL, parameters));
            ensureLatitudeInRange(Provider.STANDARD_PARALLEL, standardParallel, false);
        } else {
            // standard parallel is the equator (Plate Carree or Equirectangular)
        	standardParallel = Double.NaN;
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
        set(expected, Provider.STANDARD_PARALLEL,       values, standardParallel      );
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

        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
            new NamedIdentifier(CitationImpl.OGC,     "Winkel_II"),
            new NamedIdentifier(CitationImpl.GEOTIFF, "Winkel II"),
            new NamedIdentifier(CitationImpl.EPSG,    "Winkel II"),
            new NamedIdentifier(new CitationImpl("IDR"), "IDR")//,
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR, SEMI_MINOR,
                    CENTRAL_MERIDIAN, STANDARD_PARALLEL,
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
            return new IdrWinkelII(parameters);
        }
    }

}

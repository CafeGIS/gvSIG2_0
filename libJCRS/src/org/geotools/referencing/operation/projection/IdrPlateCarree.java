/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *
 *   (C) 2005-2006, Geotools Project Managment Committee (PMC)
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
 */
package org.geotools.referencing.operation.projection;

// OpenGIS dependencies
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.cts.ResourceKeys;
import org.geotools.resources.cts.Resources;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;
//import org.geotools.resources.i18n.ErrorKeys;
//import org.geotools.resources.i18n.Errors;


/**
 * Plate Carree (or Equirectangular) projection. This is a particular case of
 * {@linkplain IdrEquidistantCylindrical Equidistant Cylindrical} projection where the
 * {@code standard_parallel_1} is 0.
 *
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/equirectangular.html">"Equirectangular" on Remote Sensing</A>
 *
 * @since 2.2
 * @source $URL: http://svn.geotools.org/geotools/tags/2.3.0/module/referencing/src/org/geotools/referencing/operation/projection/PlateCarree.java $
 * @version $Id: IdrPlateCarree.java 12357 2007-06-27 09:05:41Z dguerrero $
 * @author John Grange
 * @author Martin Desruisseaux
 */
public class IdrPlateCarree extends IdrEquidistantCylindrical {
    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected IdrPlateCarree(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * The {@link org.geotools.referencing.operation.MathTransformProvider} for an
     * {@linkplain org.geotools.referencing.operation.projection.IdrPlateCarree Plate Carree}
     * projection.
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     *
     * @since 2.2
     * @version $Id: IdrPlateCarree.java 12357 2007-06-27 09:05:41Z dguerrero $
     * @author John Grange
     */
    public static class Provider extends AbstractProvider {
        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(CitationImpl.ESRI,     "Plate_Carree"),
                new NamedIdentifier(CitationImpl.OGC,      "Equirectangular"),
                new NamedIdentifier(CitationImpl.GEOTIFF,  "CT_Equirectangular"),
                new NamedIdentifier(new CitationImpl("IDR"), "IDR")
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,       SEMI_MINOR,
                                  CENTRAL_MERIDIAN,
                FALSE_EASTING,    FALSE_NORTHING
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
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup parameters) 
                throws ParameterNotFoundException, FactoryException
        {
            if (isSpherical(parameters)) {
                return new IdrPlateCarree(parameters);
            } else {
                throw new FactoryException(Resources.format(ResourceKeys.ERROR_ELLIPTICAL_NOT_SUPPORTED));
            }
        }
    }
}

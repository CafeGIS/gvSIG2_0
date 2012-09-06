package org.geotools.referencing.operation.projection;

import java.awt.geom.Point2D;
import java.util.Collection;

import javax.units.NonSI;

import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.projection.MapProjection.AbstractProvider;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;


public class IdrOrthographic extends MapProjection {

    private final double latitudeOfOrigin;

    protected IdrOrthographic(ParameterValueGroup parameters) throws ParameterNotFoundException {
		super(parameters);
        final Collection expected = getParameterDescriptors().descriptors();
        if (expected.contains(Provider.LATITUDE_OF_ORIGIN)) {
        	latitudeOfOrigin = Math.abs(doubleValue(expected,
                                        Provider.LATITUDE_OF_ORIGIN, parameters));
            ensureLatitudeInRange(Provider.LATITUDE_OF_ORIGIN, latitudeOfOrigin, false);
        } else {
            // standard parallel is the equator (Plate Carree or Equirectangular)
        	latitudeOfOrigin = Double.NaN;
        }
		// TODO Auto-generated constructor stub
	}

	
	public ParameterDescriptorGroup getParameterDescriptors() {
		// TODO Auto-generated method stub
        return Provider.PARAMETERS;
	}

    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        if (!Double.isNaN(latitudeOfOrigin)) {
            final Collection expected = getParameterDescriptors().descriptors();
            set(expected,Provider.LATITUDE_OF_ORIGIN, values, latitudeOfOrigin);
        }
        return values;
    }

	protected Point2D inverseTransformNormalized(double x, double y,
			Point2D ptDst) throws ProjectionException {
		// TODO Auto-generated method stub
		return null;
	}

	protected Point2D transformNormalized(double x, double y, Point2D ptDst)
			throws ProjectionException {
		// TODO Auto-generated method stub
		return null;
	}
	public static class Provider extends AbstractProvider {

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

        /**
         * The parameters group. Note the EPSG includes a "Latitude of natural origin" parameter instead
         * of "standard_parallel_1". I have sided with ESRI and Snyder in this case.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(CitationImpl.OGC,      "Orthographic"),
    			new NamedIdentifier(CitationImpl.EPSG,     "Orthographic"),
                new NamedIdentifier(new CitationImpl("IDR"), "IDR")//,
//                new NamedIdentifier(CitationImpl.GEOTOOLS, Vocabulary.formatInternational(
//                                    VocabularyKeys.EQUIDISTANT_CYLINDRICAL_PROJECTION))
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,       SEMI_MINOR,
                CENTRAL_MERIDIAN, LATITUDE_OF_ORIGIN,
                FALSE_EASTING,    FALSE_NORTHING
            });

		/*String[] parameterName={"central_meridian"};
		projectionParameterList.add(count,parameterName);
		addProjectionParameter(count,"standard_parallel_1");
		addProjectionParameter(count,"false_easting");
		addProjectionParameter(count,"false_northing");*/

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
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            return new IdrOrthographic(parameters);
        }
    }

}

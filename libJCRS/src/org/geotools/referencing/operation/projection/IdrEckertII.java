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


public class IdrEckertII extends MapProjection {

    private final double longitudeOfCenter;

    protected IdrEckertII(ParameterValueGroup parameters) throws ParameterNotFoundException {
		super(parameters);
        final Collection expected = getParameterDescriptors().descriptors();
        if (expected.contains(Provider.LONGITUDE_OF_CENTER)) {
        	longitudeOfCenter = Math.abs(doubleValue(expected,
                                        Provider.LONGITUDE_OF_CENTER, parameters));
            ensureLatitudeInRange(Provider.LONGITUDE_OF_CENTER, longitudeOfCenter, false);
        } else {
            // standard parallel is the equator (Plate Carree or Equirectangular)
        	longitudeOfCenter = Double.NaN;
        }
		// TODO Auto-generated constructor stub
	}

	
	public ParameterDescriptorGroup getParameterDescriptors() {
		// TODO Auto-generated method stub
        return Provider.PARAMETERS;
	}

    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        if (!Double.isNaN(longitudeOfCenter)) {
            final Collection expected = getParameterDescriptors().descriptors();
            set(expected,Provider.LONGITUDE_OF_CENTER, values, longitudeOfCenter);
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

        public static final ParameterDescriptor LONGITUDE_OF_CENTER = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,     "longitude_of_center"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Longitude of origin"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Longitude of false origin"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "ProjCenterLong"),
                    new NamedIdentifier(CitationImpl.EPSG,    "NatOriginLong"),
                    new NamedIdentifier(CitationImpl.EPSG,    "central_meridian"),		
                    new NamedIdentifier(CitationImpl.EPSG,    "CenterLong")
                }, 0.0, -360.0, 360.0, NonSI.DEGREE_ANGLE);

        /**
         * The parameters group. Note the EPSG includes a "Latitude of natural origin" parameter instead
         * of "standard_parallel_1". I have sided with ESRI and Snyder in this case.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(CitationImpl.OGC,      "Eckert II"),
    			new NamedIdentifier(CitationImpl.EPSG,     "Eckert-II"),
    			new NamedIdentifier(CitationImpl.EPSG,     "Eckert_II"),
                new NamedIdentifier(new CitationImpl("IDR"), "IDR")//,
//                new NamedIdentifier(CitationImpl.GEOTOOLS, Vocabulary.formatInternational(
//                                    VocabularyKeys.EQUIDISTANT_CYLINDRICAL_PROJECTION))
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,       SEMI_MINOR,
                LONGITUDE_OF_CENTER,
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
            return new IdrEckertII(parameters);
        }
    }

}

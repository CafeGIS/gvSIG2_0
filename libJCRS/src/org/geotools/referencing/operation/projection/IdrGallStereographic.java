package org.geotools.referencing.operation.projection;

import java.awt.geom.Point2D;
import java.util.Collection;

import javax.units.NonSI;

import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;

import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.XMath;
import org.geotools.resources.cts.ResourceKeys;
import org.geotools.resources.cts.Resources;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.referencing.operation.Projection;
import org.opengis.referencing.operation.MathTransform;



public class IdrGallStereographic extends MapProjection {


    protected IdrGallStereographic(ParameterValueGroup parameters) throws ParameterNotFoundException {
		super(parameters);
        final Collection expected = getParameterDescriptors().descriptors();
	}

	
	public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
	}

    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
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

	        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
	                new NamedIdentifier(CitationImpl.OGC,      "Gall_Stereographic"),
        			new NamedIdentifier(CitationImpl.EPSG,     "Gall Stereograpic"),
        			new NamedIdentifier(CitationImpl.EPSG,     "Gall"),
                    new NamedIdentifier(new CitationImpl("IDR"), "IDR")//,
	                //new NamedIdentifier(CitationImpl.EPSG,     "9823")//,
//	                new NamedIdentifier(CitationImpl.GEOTOOLS, Vocabulary.formatInternational(
//	                                    VocabularyKeys.EQUIDISTANT_CYLINDRICAL_PROJECTION))
	            }, new ParameterDescriptor[] {
	                SEMI_MAJOR,       SEMI_MINOR,
	                CENTRAL_MERIDIAN,
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
	        	return Projection.class;
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
	        	return new IdrGallStereographic(parameters);
	        }
	    }

}

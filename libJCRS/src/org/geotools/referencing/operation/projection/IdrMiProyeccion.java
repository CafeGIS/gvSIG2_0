package org.geotools.referencing.operation.projection;

import java.awt.geom.Point2D;

import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;


public class IdrMiProyeccion extends MapProjection {

	protected IdrMiProyeccion(ParameterValueGroup values) throws ParameterNotFoundException {
		super(values);
		// TODO Auto-generated constructor stub
	}

	
	public ParameterDescriptorGroup getParameterDescriptors() {
		// TODO Auto-generated method stub
		return null;
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

}

/**
 *
 */
package org.gvsig.fmap.geom.primitive.impl;

import java.awt.geom.Point2D;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Spline;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author paco
 *
 */
public class Spline2DZ extends Spline2D implements Spline {
	private static final long serialVersionUID = 8745988712085629939L;
	private double z;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Spline2DZ(GeometryType geometryType) {
		super(geometryType);		
	}
	
	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method
	 * @param geometryType
	 * @param id
	 * @param projection
	 * @param ps
	 * @param z
	 */
	Spline2DZ(GeometryType geometryType, String id, IProjection projection, Point2D[] ps, double z) {
		super(geometryType, id, projection, ps);
		this.z = z;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Curve2D#setCoordinateAt(int, int, double)
	 */
	public void setCoordinateAt(int index, int dimension, double value) {
		if (dimension == 2){
			z = value;
		}else{
			super.setCoordinateAt(index, dimension, value);
		}
	}
	
	

}

/**
 *
 */
package org.gvsig.fmap.geom.primitive.impl;

import java.awt.geom.Point2D;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Circle;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author paco
 *
 */
public class Circle2DZ extends Circle2D implements Circle {
	private static final long serialVersionUID = 5806439460681368421L;
	private double z;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Circle2DZ(GeometryType geometryType) {
		super(geometryType);		
	}
	
	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method
	 * @param id
	 * @param projection
	 * @param gpx
	 * @param c
	 * @param r
	 * @param z
	 */
	Circle2DZ(GeometryType geometryType, String id, IProjection projection, GeneralPathX gpx, Point2D c, double r, double z) {
		super(geometryType, id, projection, gpx, c, r);
		this.z = z;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Circle2D#setCoordinateAt(int, int, double)
	 */
	public void setCoordinateAt(int index, int dimension, double value) {
		if (dimension == 2){
			z = value;
		}else{
			super.setCoordinateAt(index, dimension, value);
		}
	}

	

}

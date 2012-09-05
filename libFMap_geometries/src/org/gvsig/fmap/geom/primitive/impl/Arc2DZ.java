/**
 *
 */
package org.gvsig.fmap.geom.primitive.impl;

import java.awt.geom.Point2D;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Arc;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author paco
 *
 */
public class Arc2DZ extends Arc2D implements Arc {
	private static final long serialVersionUID = -4622030261473894548L;
	private double z;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Arc2DZ(GeometryType geometryType) {
		super(geometryType);		
	}
	
	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method
	 */
	private Arc2DZ(GeometryType geometryType,String id, IProjection projection, GeneralPathX gpx, Point2D i, Point2D c, Point2D e, double z) {
		super(geometryType, id, projection, gpx, i, c, e);
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

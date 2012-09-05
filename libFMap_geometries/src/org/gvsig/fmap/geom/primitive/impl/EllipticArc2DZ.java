/**
 *
 */
package org.gvsig.fmap.geom.primitive.impl;

import java.awt.geom.Point2D;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author paco
 *
 */
public class EllipticArc2DZ extends EllipticArc2D implements Curve {
	private static final long serialVersionUID = -7625487589450160319L;
	private double z;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public EllipticArc2DZ(GeometryType geometryType) {
		super(geometryType);		
	}
	
	/**
	 * Constructor used in the {@link Geometry#cloneGeometry()} method
	 * @param geometryType
	 * @param id
	 * @param projection
	 * @param gpx
	 * @param axis1Start
	 * @param axis1End
	 * @param axis2Dist
	 * @param angSt
	 * @param andExt
	 * @param z
	 */
	EllipticArc2DZ(GeometryType geometryType, String id, IProjection projection, GeneralPathX gpx, Point2D axis1Start,Point2D axis1End, double axis2Dist, double angSt, double andExt, double z) {
		super(geometryType, id, projection, gpx, axis1Start, axis1End, axis2Dist, angSt, andExt);
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

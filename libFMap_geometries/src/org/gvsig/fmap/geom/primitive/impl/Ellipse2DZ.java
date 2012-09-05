/**
 *
 */
package org.gvsig.fmap.geom.primitive.impl;

import java.awt.geom.Point2D;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.type.GeometryType;

/**
 * @author paco
 *
 */
public class Ellipse2DZ extends Ellipse2D implements Surface {
	private static final long serialVersionUID = -6678908069111004754L;
	private double z;

	/**
	 * The constructor with the GeometryType like and argument 
	 * is used by the {@link GeometryType}{@link #create()}
	 * to create the geometry
	 * @param type
	 * The geometry type
	 */
	public Ellipse2DZ(GeometryType geometryType) {
		super(geometryType);		
	}
	
	/**
	 * @param id
	 * @param projection
	 * @param gpx
	 * @param i
	 * @param e
	 * @param d
	 */
	Ellipse2DZ(GeometryType geometryType, String id, IProjection projection, GeneralPathX gpx,
			Point2D i, Point2D e, double d, double z) {
		super(geometryType, id, projection, gpx, i, e, d);
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

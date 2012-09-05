package org.gvsig.geometries3D;


import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.DirectPosition;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.impl.AbstractPrimitive;
import org.gvsig.fmap.geom.type.GeometryType;


public class Point3D extends AbstractPrimitive	implements Point,DirectPosition {

	private static final long serialVersionUID = 1L;
	protected double[] coordinates;
	
	public Point3D(GeometryType geomType) {
		this(geomType, null, null, 0.0, 0.0,0.0);
	}
	
	public Point3D(GeometryType geomType,String id,  IProjection projection, double x, double y, double z){
		super(geomType, id, projection);
		coordinates = new double[getDimension()];
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
	}
	public Point3D(GeometryType geomType,String id, IProjection projection, java.awt.geom.Point2D p) {
		super(geomType, id, projection);
		coordinates = new double[getDimension()];
		coordinates[0] = p.getX();
		coordinates[1] = p.getY();
		coordinates[2] = 0;
	}
	public Point3D(GeometryType geomType,double x, double y,double z) {
		this(geomType, null, null, x, y, z);
	}
	public Point3D(GeometryType geomType,double x, double y) {
		this(geomType, null, null, x, y, 0);
	}
	public double getX() {
		return coordinates[0];
	}

	public void setX(double x) {
		coordinates[0]=x;
	}

	public double getY() {
		return coordinates[1];
	}

	public void setY(double y) {
		coordinates[1]=y;
	}

	public double getZ() {
		return coordinates[2];
	}

	public void setZ(double z) {
		coordinates[2]=z;
	}

	
	public GeometryType getGeometryType() {
		return geometryType;
	}

	

//	public int getShapeType() {
//		return FShape.POINT;
//	}

	public int getType() {
		return geometryType.getType();
	}

	

	public boolean contains(Point2D p) {
		if ((p.getX() == coordinates[0]) || (p.getY() == coordinates[1])) {
			return true;
		} else {
			return false;
		}
	}


	public FShape cloneFShape() {
		return new Point3D(geometryType,id, projection, coordinates[0], coordinates[1], coordinates[2]);
	}


	public double getCoordinateAt(int dimension) {
		return coordinates[dimension];
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public DirectPosition getDirectPosition() {
		return this;
	}

	public void setCoordinateAt(int dimension, double value) {
		coordinates[dimension]= value;
		
	}

	public void setCoordinates(double[] values) {
		coordinates = values;
		
	}

	public int getDimension() {
		return 3;
	}

	public double getOrdinate(int dimension) {
		if (dimension == 0) {
			return getX();
		} else if (dimension == 1) {
			return getY();
		} else if (dimension==2) {
			return getZ();
		}else return 0;
	}

	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}

	public GeneralPathX getGeneralPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public PathIterator getPathIterator(AffineTransform at) {
		// TODO Auto-generated method stub
		return null;
	}

	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean intersects(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	public void reProject(ICoordTrans ct) {
		// TODO Auto-generated method stub
		
	}

	public void transform(AffineTransform at) {
		// TODO Auto-generated method stub
		
	}

	public boolean contains(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(double arg0, double arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public Handler[] getSelectHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Handler[] getStretchingHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

}

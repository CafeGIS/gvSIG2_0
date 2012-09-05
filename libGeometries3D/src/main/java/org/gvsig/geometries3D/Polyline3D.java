package org.gvsig.geometries3D;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.impl.AbstractPrimitive;
import org.gvsig.fmap.geom.type.GeometryType;

public class Polyline3D extends AbstractPrimitive implements Curve {
	
	private static final long serialVersionUID = 1L;
	
	protected Vector<Point> _varray;
	
	public Polyline3D(GeometryType geometryType) {
		super(geometryType);
		_varray = new Vector<Point>();
	}

	
	
	
	
	public Polyline3D(GeometryType geometryType,String id,  IProjection projection){
		super(geometryType,id, projection);
		_varray = new Vector<Point>();
	}
	
	public GeometryType getGeometryType() {
		return geometryType;
	}
	public void addVertex(Point p){
		_varray.add(p);
	}
	public void removeVertex(int index){
		_varray.remove(index);
	}
	public Point getVertex(int index)
	{
		return _varray.get(index);
	}
	public int getNumVertices(){
		return _varray.size();
	}
	public void insertVertex(int index, Point p){
		_varray.insertElementAt(p, index);
	}

	public int getDimension() {
		return 3;
	}

//	public int getShapeType() {
//		return FShape.LINE;
//	}
	
	
	public int getType() {
		return geometryType.getType();
	}

	
	
	///CURVE INTERFACE METHODS
	
	public double getCoordinateAt(int index, int dimension) {
		return _varray.get(index).getCoordinateAt(dimension);
	}


	public int getNumVertex() {
		return _varray.size();
	}


	public void setCoordinateAt(int index, int dimension, double value) {
		_varray.get(index).setCoordinateAt(dimension, value);
	}


	public void setGeneralPath(GeneralPathX generalPathX) {
		throw new UnsupportedOperationException("Method not implemented");		
		
	}


	public void setPoints(Point startPoint, Point endPoint) {
		throw new UnsupportedOperationException("Method not implemented");		
		
	}

///// ABSTRACT GEOMETRY METHODS

	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}



	public GeneralPathX getGeneralPath() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public PathIterator getPathIterator(AffineTransform at) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}




	public boolean intersects(Rectangle2D r) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public void reProject(ICoordTrans ct) {
		throw new UnsupportedOperationException("Method not implemented");		
		
	}


	public void transform(AffineTransform at) {
		throw new UnsupportedOperationException("Method not implemented");		
		
	}


	public boolean contains(Point2D p) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public boolean contains(Rectangle2D r) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public boolean contains(double x, double y) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public boolean contains(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public Rectangle getBounds() {
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public boolean intersects(double x, double y, double w, double h) {
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public FShape cloneFShape() {
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public Handler[] getSelectHandlers() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}


	public Handler[] getStretchingHandlers() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented");		
	}
	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}


}

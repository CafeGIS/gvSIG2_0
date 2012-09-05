package org.gvsig.geometries3D;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.primitive.SurfaceAppearance;
import org.gvsig.fmap.geom.primitive.impl.AbstractPrimitive;
import org.gvsig.fmap.geom.type.GeometryType;

public class Polygon3D extends AbstractPrimitive implements Surface{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	
	
	protected Vector<Point> _varray;
	
	public Polygon3D(GeometryType geometryType) {
		super(geometryType,null, null);
		_varray = new Vector<Point>();
	}
	public Polygon3D(GeometryType geometryType,String id,  IProjection projection){
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

	public double getCoordinateAt(int index, int dimension) {
		return _varray.get(index).getCoordinateAt(dimension);
		
	}

	public void setCoordinateAt(int index, int dimension, double value) {
		_varray.get(index).setCoordinateAt(dimension, value);
		
	}

	public void setGeneralPath(GeneralPathX generalPathX) {
		// TODO Auto-generated method stub
		
	}

	public Geometry cloneGeometry() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean fastIntersects(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}

	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}

	public GeneralPathX getGeneralPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public Handler[] getHandlers(int type) {
		// TODO Auto-generated method stub
		return null;
	}

	public Shape getInternalShape() {
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

	public int getShapeType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean intersects(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object invokeOperation(int index, GeometryOperationContext ctx)
			throws GeometryOperationNotSupportedException,
			GeometryOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object invokeOperation(String opName, GeometryOperationContext ctx)
			throws GeometryOperationNotSupportedException,
			GeometryOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSimple() {
		// TODO Auto-generated method stub
		return false;
	}

	public void reProject(ICoordTrans ct) {
		// TODO Auto-generated method stub
		
	}

	public void transform(AffineTransform at) {
		// TODO Auto-generated method stub
		
	}

	public boolean contains(Point2D p) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean contains(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}

	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean intersects(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public FShape cloneFShape() {
		// TODO Auto-generated method stub
		return null;
	}

	public Handler[] getSelectHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	public Handler[] getStretchingHandlers() {
		// TODO Auto-generated method stub
		return null;
	}
	public SurfaceAppearance getSurfaceAppearance() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setSurfaceAppearance(SurfaceAppearance app) {
		// TODO Auto-generated method stub
		
	}
	
}

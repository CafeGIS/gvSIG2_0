package org.gvsig.geometries3D;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Envelope;

import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.primitive.SurfaceAppearance;
import org.gvsig.fmap.geom.type.GeometryType;

public class Surface3D implements Surface {

	public void addVertex(Point point) {
		// TODO Auto-generated method stub
		
	}

	public double getCoordinateAt(int index, int dimension) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumVertices() {
		// TODO Auto-generated method stub
		return 0;
	}

	public SurfaceAppearance getSurfaceAppearance() {
		// TODO Auto-generated method stub
		return null;
	}

	public Point getVertex(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertVertex(int index, Point p) {
		// TODO Auto-generated method stub
		
	}

	public void removeVertex(int index) {
		// TODO Auto-generated method stub
		
	}

	public void setCoordinateAt(int index, int dimension, double value) {
		// TODO Auto-generated method stub
		
	}

	public void setGeneralPath(GeneralPathX generalPathX) {
		// TODO Auto-generated method stub
		
	}

	public void setSurfaceAppearance(SurfaceAppearance app) {
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

	public GeometryType getGeometryType() {
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

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

/*	private Vector<PrimitiveSet> _primitiveSets;

	private Vector<Point> _vertices;

	private Vector<Point> _normals;

	private Vector<Integer> _indices;

	private Vector<Point> _colors;

	private Vector<Point> _texcoord;

	private Vector<Image> _textures;

	private AttributeBinding _colorBinding, _normalBinding;

	private Mode _primitiveMode;

	private Type _primitiveType;

	private Material _material;

	public void addColor(Point p) {
		_colors.add(p);

	}

	public void addIndex(int index) {
		_indices.add(index);
	}

	public void addNormal(Point p) {
		_normals.add(p);
	}

	public void addTextureCoord(Point p) {
		_texcoord.add(p);

	}

	public Point getColorAt(int position) {
		return _colors.get(position);

	}

	public AttributeBinding getColorBinding() {

		return _colorBinding;
	}

	public int getIndexAt(int position) {

		return _indices.get(position);
	}

	public Mode getPrimitiveMode() {
		return _primitiveMode;
	}

	public Point getNormalAt(int position) {
		return _normals.get(position);
	}

	public AttributeBinding getNormalBinding() {
		return _normalBinding;
	}

	public int getNumColors() {

		return _colors.size();
	}

	public int getNumIndices() {

		return _indices.size();
	}

	public int getNumNormals() {

		return _normals.size();
	}

	public int getNumTextureCoords() {

		return _texcoord.size();
	}

	public Point getTextureCoordAt(int position) {
		return _texcoord.get(position);
	}

	public Type getPrimitiveType() {

		return _primitiveType;
	}

	public void removeColor(int position) {
		_colors.remove(position);
	}

	public void removeIndex(int position) {
		_indices.remove(position);

	}

	public void removeNormal(int position) {
		_normals.remove(position);
	}

	public void removeTextureCoord(int position) {
		_texcoord.remove(position);
	}

	public void setColorAt(int position, Point p) {
		_colors.set(position, p);

	}

	public void setColorBinding(AttributeBinding binding) {
		_colorBinding = binding;

	}

	public void setIndexAt(int position, int index) {
		_indices.set(position, index);

	}

	public void setPrimitiveMode(Mode mode) {
		_primitiveMode = mode;

	}

	public void setNormalAt(int position, Point p) {
		_normals.set(position, p);
	}

	public void setNormalBinding(AttributeBinding binding) {
		_normalBinding = binding;

	}

	public void setTextureCoordAt(int position, Point p) {
		_texcoord.set(position, p);

	}

	public void setPrimitiveType(Type type) {
		_primitiveType=type;

	}

	public void addVertex(Point point) {
		_vertices.add(point);

	}

	public double getCoordinateAt(int index, int dimension) {
		// TODO Auto-generated method stub
		return _vertices.get(index).getCoordinateAt(dimension);
	}

	public int getNumVertices() {
		return _vertices.size();
	}

	public Point getVertex(int index) {
		return _vertices.get(index);
	}

	public void insertVertex(int index, Point p) {
		_vertices.add(index, p);
	}

	public void removeVertex(int index) {
		_vertices.remove(index);

	}

	public void setCoordinateAt(int index, int dimension, double value) {
		_vertices.get(index).setCoordinateAt(dimension, value);

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

	public GeometryType getGeometryType() {
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

	public boolean contains(Point2D arg0) {
		// TODO Auto-generated method stub
		return false;
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

	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

*/



}

package org.gvsig.geometries3D;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.primitive.Appearance;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Solid;
import org.gvsig.fmap.geom.primitive.Surface;

import org.gvsig.fmap.geom.primitive.impl.AbstractPrimitive;
import org.gvsig.fmap.geom.type.GeometryType;


public class DefaultSolid extends AbstractPrimitive implements Solid {

	/** Instancia de GeometryType obtenida al registrar esta clase */
	
	public static class AttributeBinding {
		// default
		public static int BIND_OFF = 0;
		public static int BIND_OVERALL = 1;
		public static int BIND_PER_PRIMITIVE_SET = 2;
		public static int BIND_PER_PRIMITIVE = 3;
		public static int BIND_PER_VERTEX = 4;
	}

	private Vector<PrimitiveSet> primitiveSets;

	private Vector<Point> vertices;

	private Vector<Point> normals;

	private Vector<Color> colors;

	private Vector<Vector<Point2D>> texcoord;

	private Vector<Image> _textures;

	private int colorBinding, normalBinding;

	private Material material;

	private boolean blending = false;

	private String id;

	private Vector<Surface> _surfaces;

	public DefaultSolid(GeometryType geometryType) {
		super(geometryType, null, null);
		// this.setId(id);
		this.setPrimitiveSets(new Vector<PrimitiveSet>());
		this.setVertices(new Vector<Point>());
		this.setNormals(new Vector<Point>());
		this.setColors(new Vector<Color>());
		this.setTexcoord(new Vector<Vector<Point2D>>());
		this.setTextures(new Vector<Image>());
		this.setColorBinding(AttributeBinding.BIND_OFF);
		this.setNormalBinding(AttributeBinding.BIND_OFF);
	}

	public Vector<PrimitiveSet> getPrimitiveSets() {
		return primitiveSets;
	}

	public void setPrimitiveSets(Vector<PrimitiveSet> primitiveSets) {
		this.primitiveSets = primitiveSets;
	}

	public Vector<Point> getVertices() {
		return vertices;
	}

	public void setVertices(Vector<Point> vertices) {
		this.vertices = vertices;
	}

	public Vector<Point> getNormals() {
		return normals;
	}

	public void setNormals(Vector<Point> normals) {
		this.normals = normals;
	}

	public Vector<Color> getColors() {
		return colors;
	}

	public void setColors(Vector<Color> colors) {
		this.colors = colors;
	}

	public Vector<Vector<Point2D>> getTexcoord() {
		return texcoord;
	}

	public void setTexcoord(Vector<Vector<Point2D>> texcoord) {
		this.texcoord = texcoord;
	}

	public Vector<Image> getTextures() {
		return _textures;
	}

	public void setTextures(Vector<Image> textures) {
		this._textures = textures;
	}

	public int getColorBinding() {
		return colorBinding;
	}

	public void setColorBinding(int colorBinding) {
		this.colorBinding = colorBinding;
	}

	public int getNormalBinding() {
		return normalBinding;
	}

	public void setNormalBinding(int normalBinding) {
		this.normalBinding = normalBinding;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public boolean isBlending() {
		return blending;
	}

	public void setBlending(boolean blending) {
		this.blending = blending;
	}

	/*
	 * 
	 * All this methods are from abstract geometry
	 */

	public GeometryType getGeometryType() {
		return geometryType;
	}

	public int getShapeType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCoordinateDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	public PathIterator getPathIterator(AffineTransform arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getType() {
		return geometryType.getType();
	}

	public boolean intersects(Rectangle2D arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public void reProject(ICoordTrans arg0) {
		// TODO Auto-generated method stub

	}

	public void transform(AffineTransform arg0) {
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

	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}

	public GeneralPathX getGeneralPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getDimension() {
		// TODO Auto-generated method stub
		return 3;
	}

	public void addSurface(Surface surface) {
		// TODO Auto-generated method stub
		
	}

	public Appearance getAppearance() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNumSurfaces() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Surface getSurfaceAt(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeSurface(int position) {
		// TODO Auto-generated method stub
		
	}

	public void setAppearance(Appearance app) {
		// TODO Auto-generated method stub
		
	}

	

}

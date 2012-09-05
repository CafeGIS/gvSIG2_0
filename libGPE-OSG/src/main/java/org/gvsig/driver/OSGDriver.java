/* gvSIG. Geographic Information System of the Valencian Government
 *  osgVP. OSG Virtual Planets.
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */
/*
 * AUTHORS (In addition to CIT):
 * 2008 Instituto de Automática e Informática Industrial, UPV.
 */

/**
 * 
 */
package org.gvsig.driver;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.logging.Level;

import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.primitive.Solid;
import org.gvsig.fmap.geom.primitive.impl.AbstractPrimitive;
import org.gvsig.geometries3D.Material;
import org.gvsig.geometries3D.MultiGeometry;
import org.gvsig.geometries3D.MultiSolid;
import org.gvsig.geometries3D.Point3D;
import org.gvsig.geometries3D.PrimitiveSet;
import org.gvsig.gpe.IGPEContentHandler3D;
import org.gvsig.gpe.parser.GPEContentHandler;
import org.gvsig.osgvp.util.Util;

/**
 * @author rgaitan
 * 
 */
public class OSGDriver extends GPEContentHandler implements
		IGPEContentHandler3D {

	private MultiGeometry _root;

	@Override
	public Object startMultiGeometry(String id, String srs) {
//		Util.logger.log(Level.FINEST, "Starting MultiGeometry");
//		MultiGeometry feature = (MultiGeometry) ((GeometryFactory3D) GeometryManager
//				.getInstance().getGeometryFactory()).createMultiGeometry(id);
//		if (_root == null)
//			_root = feature;
//		return feature;
		return null;
	}

	public Object startMultiSolid(String id, String srs) {
//		Util.logger.log(Level.FINEST, "Starting MultiSolid");
//		return (MultiSolid) ((GeometryFactory3D) GeometryManager.getInstance()
//				.getGeometryFactory()).createMultiSolid(id);
		return null;
	}

	public void endMultiSolid(Object solid) {
	}

	public Object startSolid(String id, String srs) {
		// Util.logger.log(Level.FINEST,"Starting Geometry");
//		Solid geometry = (Solid) ((GeometryFactory3D) GeometryManager
//				.getInstance().getGeometryFactory()).createSolid(id);
//		return geometry;
	return null;
	}

	public void endSolid(Object geometry) {
		// Util.logger.log(Level.FINEST,"Ending Geometry");

	}

	@Override
	public void addGeometryToMultiGeometry(Object geometry, Object multiGeometry) {
		// Util.logger.log(Level.FINEST,"Adding Feature to Feature");
		((MultiGeometry) multiGeometry)
				.addGeometry((AbstractPrimitive) geometry);
	}

	public void addSolidToMultiSolid(Object solid, Object multiSolid) {
		// Util.logger.log(Level.FINEST,"Adding Geometry to Feature");
		((MultiSolid) multiSolid).addSolid((Solid) solid);
	}

	public void startSolidVertexArray(Object geometry) {
	}

	public void endSolidVertexArray() {
	}

	public void startSolidNormalArray(Object geometry) {
	}

	public void endSolidNormalArray() {
	}

	public void startSolidColorArray(Object geometry) {

	}

	public void endSolidColorArray() {
	}

	public void startSolidTexCoordArray(Object geometry, int nTexCoords,
			int stage) {
	}

	public void endSolidTexCoordArray() {
	}

	public void addVertexToSolid(Object geometry, double x, double y, double z) {

//		Point3D point = (Point3D) ((GeometryFactory3D) GeometryManager
//				.getInstance().getGeometryFactory()).createPoint3D("");
//		point.setX(x);
//		point.setY(y);
//		point.setZ(z);
//		((Solid) geometry).getVertices().add(point);
		// Util.logger.log(Level.FINEST,"Adding Vertex");
	}

	public void addNormalToSolid(Object geometry, double x, double y, double z) {
//		Point3D point = (Point3D) ((GeometryFactory3D) GeometryManager
//				.getInstance().getGeometryFactory()).createPoint3D("");
//		point.setX(x);
//		point.setY(y);
//		point.setZ(z);
//		((Solid) geometry).getNormals().add(point);
//		Util.logger.log(Level.FINEST, "Adding Normal" + point.getX());
	}

	public void addTextureToSolid(Object geometry, int stage,
			BufferedImage image) {

//		((Solid) geometry).getTextures().add(stage, image);

	}

	public void setNormalBindingToSolid(Object geometry, int mode) {

//		((Solid) geometry).setNormalBinding(mode);

	}

	public void setColorBindingToSolid(Object geometry, int mode) {

//		((Solid) geometry).setColorBinding(mode);

	}

	public void addColorToSolid(Object geometry, float r, float g, float b,
			float a) {

//		((Solid) geometry).getColors().add(convertColor(r, g, b, a));

	}

	public void addTextureCoordinateToSolid(Object geometry, double x,
			double y, int stage) {
//		try {
//			((Solid) geometry).getTexcoord().get(stage);
//		} catch (ArrayIndexOutOfBoundsException ex) {
//			((Solid) geometry).getTexcoord().add(stage, new Vector<Point2D>());
//		}
//		Point2D point = new Point2D.Double();
//		point.setLocation(x, y);
//		((Solid) geometry).getTexcoord().get(stage).add(point);

	}

	public MultiGeometry getRootFeature() {
		return _root;
	}

	public void addPrimitiveSetToSolid(Object geometry, Object primitiveSet) {
	//	((Solid) geometry).getPrimitiveSets().add((PrimitiveSet) primitiveSet);
	}

	public void addIndexToPrimitiveSet(Object primitiveSet, int i) {
		((PrimitiveSet) primitiveSet).getIndices().add(i);
	}

	public void endPrimitiveSet(Object primitiveSet) {
	}

	public void endPrimitiveSetIndexArray() {
	}

	public Object startPrimitiveSet(int mode, int type) {
		return new PrimitiveSet(mode, type);
	}

	public void startPrimitiveSetIndexArray(Object primitiveSet, int indices) {
		((PrimitiveSet) primitiveSet).setIndices(new Vector<Integer>());
	}

	public void addMaterialToSolid(Object solid, Object material) {
//		((Solid) solid).setMaterial((Material) material);

	}

	public void endMaterial(Object material) {

	}

	public Object startMaterial() {
		Material material = new Material();
		return material;
	}

	public void addAmbientToMaterial(Object material, float r, float g,
			float b, float a) {

		((Material) material).setAmbient(convertColor(r, g, b, a));

	}

	public void addDiffuseToMaterial(Object material, float r, float g,
			float b, float a) {

		((Material) material).setDiffuse(convertColor(r, g, b, a));

	}

	public void addEmissionToMaterial(Object material, float r, float g,
			float b, float a) {

		((Material) material).setEmission(convertColor(r, g, b, a));

	}

	public void addShininessToMaterial(Object material, float s) {

		((Material) material).setShininess(s);

	}

	public void addSpecularToMaterial(Object material, float r, float g,
			float b, float a) {

		((Material) material).setSpecular(convertColor(r, g, b, a));

	}

	protected Color convertColor(float r, float g, float b, float a) {

		int _r, _g, _b, _a;

		_r = Math.min((int) (r * 255), 255);
		_g = Math.min((int) (g * 255), 255);
		_b = Math.min((int) (b * 255), 255);
		_a = Math.min((int) (a * 255), 255);

		Color color = new Color(_r, _g, _b, _a);

		return color;

	}

	public void addBlendingToSolid(Object solid, boolean blending) {

	//	((Solid) solid).setBlending(blending);

	}

}

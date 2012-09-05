package org.gvsig.operations3D;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.logging.Level;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.geometries3D.Solid;
import org.gvsig.operations3D.context.Draw3DContext;
import org.gvsig.osgvp.core.osg.DrawElementsUInt;
import org.gvsig.osgvp.core.osg.Geode;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Material;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Texture2D;
import org.gvsig.osgvp.core.osg.Vec2;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.osgvp.exceptions.InvalidValueException;
import org.gvsig.osgvp.exceptions.image.ImageException;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.util.Util;

public class Draw3DSolid extends GeometryOperation {

	// Check GeometryManager for alternative methods to register an operation
	public static final int CODE = GeometryManager.getInstance()
			.registerGeometryOperation("Draw3DSolid", new Draw3DSolid(),Solid.class);

	@Override
	public Object invoke(Geometry geom, GeometryOperationContext ctx) {
		Solid solid = (Solid) geom;
		System.out.println("Codigo de la geometria: " + solid.getType());
		System.out.println("Nombre: " + solid.getId());
		if (ctx!=null){
			System.out.println("contexto no nulo");
		}
		
		/******************************************************************/
		
		Group group = ((Draw3DContext)ctx).getGroup();
		
		int i;
		Geode geode = new Geode();
		org.gvsig.osgvp.core.osg.Geometry geometry = new org.gvsig.osgvp.core.osg.Geometry();

		try {
			group.addChild(geode);
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			geode.addDrawable(geometry);
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Vector<Vec3> vertices = new Vector<Vec3>();
		for (i = 0; i < solid.getVertices().size(); i++) {

			Vec3 vertex = new Vec3();

			vertex.set(solid.getVertices().get(i).getX(), solid
					.getVertices().get(i).getY(), solid.getVertices().get(
					i).getZ());
			vertices.add(vertex);

		}

		geometry.setVertexArray(vertices);

		Vector<Vec3> normals = new Vector<Vec3>();
		for (i = 0; i < solid.getNormals().size(); i++) {

			Vec3 normal = new Vec3();
			normal.set(solid.getNormals().get(i).getX(), solid
					.getNormals().get(i).getY(), solid.getNormals().get(i)
					.getZ());
			normals.add(normal);

		}

		geometry.setNormalArray(normals);

		geometry.setNormalBinding(solid.getNormalBinding());

		Vector<Vec4> colors = new Vector<Vec4>();
		for (i = 0; i < solid.getColors().size(); i++) {

			Vec4 color = new Vec4();
			color
					.setX((double) (solid.getColors().get(i).getRed()) / 255.0);
			color
					.setY((double) (solid.getColors().get(i).getGreen()) / 255.0);
			color
					.setZ((double) (solid.getColors().get(i).getBlue()) / 255.0);
			color
					.setW((double) (solid.getColors().get(i).getAlpha()) / 255.0);
			colors.add(color);

		}

		geometry.setColorArray(colors);

		for (int j = 0; j < solid.getPrimitiveSets().size(); j++) {
			Vector<Integer> indices = new Vector<Integer>();
			for (i = 0; i < solid.getPrimitiveSets().get(j)
					.getIndexArray().size(); i++) {

				indices.add(solid.getPrimitiveSets().get(j)
						.getIndexArray().get(i));

			}

			DrawElementsUInt elements;
			try {
				elements = new DrawElementsUInt(solid.getPrimitiveSets()
						.get(j).getMode());
				elements.setIndexArray(indices);
				geometry.addPrimitiveSet(elements);
			} catch (InvalidValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Util.logger.log(Level.FINEST, "Drawable has: "
				+ solid.getTextures().size() + " textures");
		for (int j = 0; j < solid.getTextures().size(); j++) {

			try {

				BufferedImage image = (BufferedImage) solid.getTextures()
						.get(j);
				org.gvsig.osgvp.core.osg.Image osgImage = new org.gvsig.osgvp.core.osg.Image();

				osgImage.setBufferedImage(image);

				Texture2D tex = new Texture2D();

				tex.setImage(osgImage);

				geometry.getOrCreateStateSet().setTexture2D(tex, j,
						Node.Mode.ON | Node.Mode.OVERRIDE);

			} catch (ArrayIndexOutOfBoundsException ex) {

				// TODO Auto-generated catch block
				ex.printStackTrace();
			} catch (ImageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		for (int j = 0; j < solid.getTexcoord().size(); j++) {

			try {

				Vector<Vec2> osgCoords = new Vector<Vec2>();
				Vector<Point2D> coord = solid.getTexcoord().get(j);

				for (int h = 0; h < coord.size(); h++) {

					Vec2 vec = new Vec2();
					Point2D point = coord.get(h);

					vec.setX(point.getX());
					vec.setY(point.getY());

					osgCoords.add(vec);

				}

				geometry.setTexCoordArray(j, osgCoords);

			} catch (ArrayIndexOutOfBoundsException ex) {
			}

		}

		try {

			Material material = new Material();

			Vec4 ambient = new Vec4();
			ambient.setX((float) (solid.getMaterial().getAmbient()
					.getRed() / 255.0));
			ambient.setY((float) (solid.getMaterial().getAmbient()
					.getGreen() / 255.0));
			ambient.setZ((float) (solid.getMaterial().getAmbient()
					.getBlue() / 255.0));
			ambient.setW((float) (solid.getMaterial().getAmbient()
					.getAlpha() / 255.0));

			Vec4 diffuse = new Vec4();
			diffuse.setX((float) (solid.getMaterial().getDiffuse()
					.getRed() / 255.0));
			diffuse.setY((float) (solid.getMaterial().getDiffuse()
					.getGreen() / 255.0));
			diffuse.setZ((float) (solid.getMaterial().getDiffuse()
					.getBlue() / 255.0));
			diffuse.setW((float) (solid.getMaterial().getDiffuse()
					.getAlpha() / 255.0));

			Vec4 specular = new Vec4();
			specular.setX((float) (solid.getMaterial().getSpecular()
					.getRed() / 255.0));
			specular.setY((float) (solid.getMaterial().getSpecular()
					.getGreen() / 255.0));
			specular.setZ((float) (solid.getMaterial().getSpecular()
					.getBlue() / 255.0));
			specular.setW((float) (solid.getMaterial().getSpecular()
					.getAlpha() / 255.0));

			Vec4 emission = new Vec4();
			emission.setX((float) (solid.getMaterial().getEmission()
					.getRed() / 255.0));
			emission.setY((float) (solid.getMaterial().getEmission()
					.getGreen() / 255.0));
			emission.setZ((float) (solid.getMaterial().getEmission()
					.getBlue() / 255.0));
			emission.setW((float) (solid.getMaterial().getEmission()
					.getAlpha() / 255.0));

			float shininess = solid.getMaterial().getShininess();

			material.setAmbient(Material.Face.FRONT, ambient);
			material.setDiffuse(Material.Face.FRONT, diffuse);
			material.setSpecular(Material.Face.FRONT, specular);
			material.setEmission(Material.Face.FRONT, emission);
			material.setShininess(Material.Face.FRONT, shininess);

			geometry.getOrCreateStateSet().setMaterial(material, 1);

		}

		catch (NullPointerException e) {

		} catch (InvalidValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		geometry.getOrCreateStateSet().setEnabledBlending(
				solid.isBlending());

		
		
		
		/*******************************************************************/
		
		
		return group;
	}

	@Override
	public int getOperationIndex() {
		// TODO Auto-generated method stub
		return CODE;
	}

}

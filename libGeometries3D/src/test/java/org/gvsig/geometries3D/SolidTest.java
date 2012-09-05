package org.gvsig.geometries3D;


import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.Vector;

import junit.framework.TestCase;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.impl.DefaultGeometryManager;

import org.gvsig.impl.Geometry3DLibrary;
import org.gvsig.tools.exception.BaseException;

public class SolidTest extends TestCase {
	private GeometryManager gf3D;
	
	public void setUp() throws Exception {
		super.setUp();
		GeometryLocator.registerGeometryManager(DefaultGeometryManager.class);
		
		Geometry3DLibrary lib = new Geometry3DLibrary();		
		lib.initialize();
		lib.postInitialize();
		gf3D = GeometryLocator.getGeometryManager();
	}

	public void testCreateSolidID() throws BaseException {
		System.out.println("testCreateSolidID()");
	
//		DefaultSolid solid = (DefaultSolid) gf3D.create(TYPES.SOLID, SUBTYPES.GEOM3D);
//		//System.out.println("Solid created "+solid.getType());
//		// Testing ID default
//		assertEquals(solid.getId(), "solid");
	}
	public void testSolidBindings() throws BaseException {
//		System.out.println("testSolidBindings()");
//		GeometryFactory3D gf3D = new GeometryFactory3D();
//		Solid solid = (Solid) gf3D.createSolid("solid");
//		System.out.println("Solid created ");
//		// Testing binding attributes 
//		assertEquals(solid.getColorBinding(), AttributeBinding.BIND_OFF);
//		assertEquals(solid.getNormalBinding(), AttributeBinding.BIND_OFF);
//		// Setting new binding attributes
//		solid.setColorBinding(AttributeBinding.BIND_OVERALL);
//		solid.setNormalBinding(AttributeBinding.BIND_PER_VERTEX);
//		// Testing new binding attributes 
//		assertEquals(solid.getColorBinding(), AttributeBinding.BIND_OVERALL);
//		assertEquals(solid.getNormalBinding(), AttributeBinding.BIND_PER_VERTEX);
		
	}
	
	public void testSolidVectors() throws BaseException {
//		System.out.println("testSolidVectors()");
//		GeometryFactory3D gf3D = new GeometryFactory3D();
//		Solid solid = (Solid) gf3D.createSolid("solid");
//		System.out.println("Solid created ");
//		// Testing not null values of the vectors
//		this.assertNotNull(solid.getPrimitiveSets());
//		this.assertNotNull(solid.getVertices());
//		this.assertNotNull(solid.getNormals());
//		this.assertNotNull(solid.getColors());
//		this.assertNotNull(solid.getTexcoord());
//		this.assertNotNull(solid.getTextures());
//		
//		// Setting creating and setting new vectors
//		solid.setPrimitiveSets(new Vector<PrimitiveSet>());
//		solid.setVertices(new Vector<Point3D>());
//		solid.setNormals(new Vector<Point3D>());
//		solid.setColors(new Vector<Color>());
//		solid.setTexcoord(new Vector<Vector<Point2D>>());
//		solid.setTextures(new Vector<Image>());
//		
//		// Testing not null values of the new vectors
//		this.assertNotNull(solid.getPrimitiveSets());
//		this.assertNotNull(solid.getVertices());
//		this.assertNotNull(solid.getNormals());
//		this.assertNotNull(solid.getColors());
//		this.assertNotNull(solid.getTexcoord());
//		this.assertNotNull(solid.getTextures());
		
	}
	


}

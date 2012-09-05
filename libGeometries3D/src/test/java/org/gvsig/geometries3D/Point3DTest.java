package org.gvsig.geometries3D;

import junit.framework.TestCase;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.impl.DefaultGeometryManager;
import org.gvsig.impl.Geometry3DLibrary;
import org.gvsig.tools.exception.BaseException;


public class Point3DTest extends TestCase {
	private GeometryManager gf3D;
//	public void testRegisterGeometryFactory() throws BaseException {
//		GeometryFactory3D geomfact3D = new GeometryFactory3D();
//		GeometryManager.getInstance().registerGeometryFactory(geomfact3D);
//	}
//
//	public void testGetGeometryFactory() throws BaseException {
//		GeometryFactory3D gf3D = (GeometryFactory3D) GeometryManager
//				.getInstance().getGeometryFactory();
//		assertNotNull(gf3D);
//	}
	public void setUp() throws Exception {
		super.setUp();
		GeometryLocator.registerGeometryManager(DefaultGeometryManager.class);
		Geometry3DLibrary lib = new Geometry3DLibrary();		
		lib.initialize();
		lib.postInitialize();
		
		gf3D = GeometryLocator.getGeometryManager();
	}
	public void testPoint1() throws BaseException {

	
		Point3D point = (Point3D) gf3D.create(TYPES.POINT, SUBTYPES.GEOM3D);
		//Point3D point = new Point3D();
		System.out.println("creado punto "+point.getId());
		point.setCoordinateAt(0, 1.0);
		point.setCoordinateAt(1, 2.0);
		point.setCoordinateAt(2, 3.0);
		
	//	assertEquals(point.getId(), "Point3D");
		assertEquals(point.getX(), 1.0);
		assertEquals(point.getY(), 2.0);
		assertEquals(point.getZ(), 3.0);
		assertNotNull(point.getGeometryType());

		

	}

	public void testPoint2() throws BaseException {
		
		Point3D point = (Point3D) gf3D.create(TYPES.POINT, SUBTYPES.GEOM3D);
		//Point3D point = new Point3D();
		assertEquals(point.getX(), 0.0);
		assertEquals(point.getY(),0.0);
		assertEquals(point.getCoordinateAt(2), 0.0);
		assertNotNull(point.getGeometryType());
	}

	public void testPoint3() throws BaseException {
		Point3D point = (Point3D) gf3D.create(TYPES.POINT, SUBTYPES.GEOM3D);
		//Point3D point = new Point3D();
		point.setX(2.0);
		point.setY(3.0);
		point.setZ(4.0);
		assertEquals(point.getX(), 2.0);
		assertEquals(point.getY(), 3.0);
		assertEquals(point.getZ(), 4.0);
		assertNotNull(point.getGeometryType());
	}
	
	

}

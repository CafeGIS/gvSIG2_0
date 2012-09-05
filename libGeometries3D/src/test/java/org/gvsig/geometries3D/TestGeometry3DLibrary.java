package org.gvsig.geometries3D;

import junit.framework.TestCase;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.impl.DefaultGeometryManager;
import org.gvsig.impl.Geometry3DLibrary;
import org.gvsig.tools.exception.BaseException;

public class TestGeometry3DLibrary extends TestCase {
	private GeometryManager gf3D;
	public void testRegisterGeometryFactory() throws BaseException {
		System.out.println("Creating new 3D Geometry Library.");
		GeometryLocator.registerGeometryManager(DefaultGeometryManager.class);
		Geometry3DLibrary lib = new Geometry3DLibrary();		
		lib.initialize();
		lib.postInitialize();
		gf3D = GeometryLocator.getGeometryManager();
		assertNotNull(gf3D);
		System.out.println("Registering it into the geometry manager.");

	}

	

}

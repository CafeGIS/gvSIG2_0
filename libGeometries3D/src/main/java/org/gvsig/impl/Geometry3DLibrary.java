package org.gvsig.impl;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.geometries3D.DefaultSolid;
import org.gvsig.geometries3D.MultiGeometry;
import org.gvsig.geometries3D.MultiSolid;
import org.gvsig.geometries3D.Point3D;
import org.gvsig.geometries3D.Polygon3D;
import org.gvsig.geometries3D.Polyline3D;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

public class Geometry3DLibrary extends BaseLibrary {

	
	public void initialize() throws ReferenceNotRegisteredException {
        super.initialize();
		
       // Register the default GeometryManager
       //GeometryLocator.registerGeometryManager(DefaultGeometryManager.class);
    }

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#postInitialize()
	 */
	public void postInitialize() {
		super.postInitialize();

		// Validate there is any implementation registered.
		GeometryManager geometryManager = GeometryLocator.getGeometryManager();
		if (geometryManager == null) {
			throw new ReferenceNotRegisteredException(
					GeometryLocator.GEOMETRY_MANAGER_NAME, GeometryLocator.getInstance());
		}
		//Register the geometries in 3D
        geometryManager.registerGeometryType(Point3D.class, "Point3D", TYPES.POINT, SUBTYPES.GEOM3D);
        geometryManager.registerGeometryType(Polyline3D.class, "Polyline3D", TYPES.CURVE, SUBTYPES.GEOM3D);
        geometryManager.registerGeometryType(Polygon3D.class, "Polygon3D", TYPES.SURFACE, SUBTYPES.GEOM3D);
        geometryManager.registerGeometryType(DefaultSolid.class, "DefaultSolid", TYPES.SOLID, SUBTYPES.GEOM3D);
        geometryManager.registerGeometryType(MultiGeometry.class, "MultiGeometry", TYPES.GEOMETRY, SUBTYPES.GEOM3DM);
        geometryManager.registerGeometryType(MultiSolid.class, "MultiSolid", TYPES.MULTISOLID, SUBTYPES.GEOM3DM);
       }
}
	
	
	


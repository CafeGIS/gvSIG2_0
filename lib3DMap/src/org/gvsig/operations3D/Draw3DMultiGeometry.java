package org.gvsig.operations3D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.geometries3D.MultiGeometry;
import org.gvsig.osgvp.core.osg.Group;

public class Draw3DMultiGeometry extends GeometryOperation {

	// Check GeometryManager for alternative methods to register an operation
	public static final int CODE = GeometryManager.getInstance()
			.registerGeometryOperation("Draw3DMultiGeometry",
					new Draw3DMultiGeometry(), MultiGeometry.class);

	@Override
	public int getOperationIndex() {
		return CODE;
	}

	@Override
	public Object invoke(Geometry geom, GeometryOperationContext ctx)
			throws GeometryOperationException {
		MultiGeometry multiGeometry = (MultiGeometry) geom;
		Group group = null;
		if (ctx == null) {
			System.out.println("Operation in test mode");
			System.out.println("Id :" + multiGeometry.getId());
			System.out.println("Code registration : "
					+ multiGeometry.getGeometryType());
			System.out.println("Code registration : " + multiGeometry.getType());
			System.out.println("Code registration : "
					+ multiGeometry.getCoordinateDimension());
		} else {
			
		}
		return group;
	}
}

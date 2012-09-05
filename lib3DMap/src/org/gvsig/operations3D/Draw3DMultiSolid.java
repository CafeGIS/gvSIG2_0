package org.gvsig.operations3D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.geometries3D.MultiSolid;
import org.gvsig.geometries3D.Solid;
import org.gvsig.operations3D.context.Draw3DContext;
import org.gvsig.osgvp.core.osg.Group;


public class Draw3DMultiSolid extends GeometryOperation {

	// Check GeometryManager for alternative methods to register an operation
	public static final int CODE = GeometryManager.getInstance()
			.registerGeometryOperation("Draw3DMultiSolid",
					new Draw3DMultiSolid(), MultiSolid.class);

	@Override
	public int getOperationIndex() {
		return CODE;
	}

	@Override
	public Object invoke(Geometry geom, GeometryOperationContext ctx)
			throws GeometryOperationException {
		MultiSolid multiSolid = (MultiSolid) geom;
		Group group = null;
		if (ctx == null) {
			System.out.println("Operation in test mode");
			System.out.println("Id :" + multiSolid.getId());
			System.out.println("Code registration : "
					+ multiSolid.getGeometryType());
			System.out.println("Code registration : " + multiSolid.getType());
			System.out.println("Code registration : "
					+ multiSolid.getCoordinateDimension());
		} else {
			group = ((Draw3DContext) ctx).getGroup();
			for (int i = 0; i < multiSolid.getSolids().size(); i++) {
				Solid solid = multiSolid.getSolids().get(i);
				try {
					Draw3DSolid d3DSolid = new Draw3DSolid();
					group = (Group) solid.invokeOperation(d3DSolid
							.getOperationIndex(), ctx);
				} catch (GeometryOperationNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}

		return group;
	}
	
	
}

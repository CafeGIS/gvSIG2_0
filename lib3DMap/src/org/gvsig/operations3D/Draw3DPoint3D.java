package org.gvsig.operations3D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.geometries3D.Point3D;


public class Draw3DPoint3D extends GeometryOperation {

	// Check GeometryManager for alternative methods to register an operation
	public static final int CODE = GeometryManager.getInstance()
			.registerGeometryOperation("Draw3DPoint3D", new Draw3DPoint3D(),
					Point3D.class);

	public Object invoke(Geometry geom, GeometryOperationContext ctx) {
		Point3D p3D = (Point3D) geom;
		System.out.println("Codigo de la geometria: " + p3D.getType());
		System.out.println("Nombre: " + p3D.getId());
		System.out.println("X: " + p3D.getX());
		System.out.println("Y: " + p3D.getY());
		System.out.println("Z: " + p3D.getZ());
		if (ctx!=null){
			System.out.println("contexto no nulo");
		}
		return null;
	}

	public int getOperationIndex() {
		return CODE;
	}

}

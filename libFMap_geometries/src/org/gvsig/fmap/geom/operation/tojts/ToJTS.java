package org.gvsig.fmap.geom.operation.tojts;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.util.Converter;

public class ToJTS extends GeometryOperation {

	public static final int CODE = GeometryLocator.getGeometryManager()
			.registerGeometryOperation("toJTS", new ToJTS());

	public Object invoke(Geometry geom, GeometryOperationContext ctx) {
		return Converter.geometryToJts(geom);
	}

	public int getOperationIndex() {
		return CODE;
	}

}

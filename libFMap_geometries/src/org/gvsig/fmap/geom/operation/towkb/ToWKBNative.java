package org.gvsig.fmap.geom.operation.towkb;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;

public class ToWKBNative extends GeometryOperation {


	public static final int CODE = GeometryLocator.getGeometryManager()
			.registerGeometryOperation("toWKBNative", new ToWKBNative());

	private static WKBEncoder encoder = new WKBEncoder();

	public Object invoke(Geometry geom, GeometryOperationContext ctx)
			throws GeometryOperationException {
		try {
			return encoder.encode(geom);
		} catch (Exception e) {
			throw new GeometryOperationException(e);
		}
	}

	public int getOperationIndex() {
		return CODE;
	}

}

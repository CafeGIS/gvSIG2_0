package org.gvsig.fmap.geom.operation.towkt;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.util.Converter;

import com.vividsolutions.jts.io.WKTWriter;

public class ToWKT extends GeometryOperation {

	private static WKTWriter writer = new WKTWriter();

	public static final int CODE = GeometryLocator.getGeometryManager()
			.registerGeometryOperation("toWKT", new ToWKT());

	public Object invoke(Geometry geom, GeometryOperationContext ctx) {
		return writer.write(Converter.geometryToJts(geom));
	}

	public int getOperationIndex() {
		return CODE;
	}

}

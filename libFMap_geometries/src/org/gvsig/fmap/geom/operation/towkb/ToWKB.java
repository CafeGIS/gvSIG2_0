package org.gvsig.fmap.geom.operation.towkb;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.util.Converter;

import com.vividsolutions.jts.io.WKBWriter;

public class ToWKB extends GeometryOperation {

	private static WKBWriter writer = new WKBWriter();

	public static final int CODE = GeometryLocator.getGeometryManager()
			.registerGeometryOperation("toWKB", new ToWKB());

	public Object invoke(Geometry geom, GeometryOperationContext ctx) {
		if (ctx == null){
			return writer.write(Converter.geometryToJts(geom));
		}
		return writer.write(Converter.geometryToJtsWithSRID(geom,
				((ToWKBOperationContext) ctx).getSrID()));
	}

	public int getOperationIndex() {
		return CODE;
	}

}

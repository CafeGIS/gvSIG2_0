package org.gvsig.fmap.geom.operation;

import java.awt.geom.AffineTransform;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;

public class Draw extends GeometryOperation{
	public static final int CODE = GeometryLocator.getGeometryManager()
	.registerGeometryOperation("draw", new Draw());

	public Object invoke(Geometry geom, GeometryOperationContext ctx) throws GeometryOperationException {
		DrawOperationContext doc=(DrawOperationContext)ctx;
		AffineTransform at=doc.getViewPort().getAffineTransform();
		geom.transform(at);
		doc.getSymbol().draw(doc.getGraphics(), at, geom, doc.getCancellable());
		return null;
	}

	public int getOperationIndex() {
		return CODE;
	}

	public static void register() {
		// Nothing to do

	}

}

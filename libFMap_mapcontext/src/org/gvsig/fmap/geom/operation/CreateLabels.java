package org.gvsig.fmap.geom.operation;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.utils.FLabel;

public class CreateLabels extends GeometryOperation{
	public static final int CODE = GeometryLocator.getGeometryManager()
			.registerGeometryOperation("createLabels", new CreateLabels());

	public Object invoke(Geometry geom, GeometryOperationContext ctx)
			throws GeometryOperationException {
		CreateLabelsOperationContext cloc = (CreateLabelsOperationContext) ctx;
		try {
			return createLabels(geom, cloc.getPosition(), cloc.isDublicates());
		} catch (CreateGeometryException e) {
			throw new GeometryOperationException(e);
		}
	}

	public int getOperationIndex() {
		return CODE;
	}

	private FLabel[] createLabels(Geometry geom, int position,
			boolean duplicates) throws CreateGeometryException {
		FLabel[] aux = new FLabel[1];
		aux[0] = FLabel.createFLabel(geom);

		return aux;
	}
}

package org.gvsig.fmap.mapcontext.layers.vectorial;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.relationship.DefaultRelationshipGeometryOperationContext;
import org.gvsig.fmap.geom.operation.relationship.Within;
import org.gvsig.tools.evaluator.AbstractEvaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;
/**
 *
 * @author Vicente Caballero Navarro
 *
 */
public class WithinGeometryEvaluator extends AbstractEvaluator {

	private String geomName;
	private Geometry geometry;
	private Geometry geometryTrans;
	private String srs;
	private boolean isDefault;

	public WithinGeometryEvaluator(Geometry geometry,
			IProjection envelopeProjection, FeatureType featureType,
			String geomName) {
		FeatureAttributeDescriptor fad = (FeatureAttributeDescriptor) featureType
				.get(geomName);
		this.isDefault = featureType.getDefaultGeometryAttributeName().equals(
				geomName);
		this.geometry = geometry;
		this.geometryTrans = geometry.cloneGeometry();
		this.srs = envelopeProjection.getAbrev();
		ICoordTrans ct = envelopeProjection.getCT(fad.getSRS());
		if (ct != null) {
			geometryTrans.reProject(ct);
		}
		this.geomName = geomName;

		this.getFieldsInfo().addMatchFieldValue(geomName, geometryTrans);

	}

	public Object evaluate(EvaluatorData data) throws EvaluatorException {
		try {
			DefaultRelationshipGeometryOperationContext context;
			if (isDefault) {
				Feature feature = (Feature) data.getContextValue("feature");
				context =new DefaultRelationshipGeometryOperationContext(feature
						.getDefaultGeometry());

			} else {
				Geometry geom = (Geometry) data.getDataValue(geomName);

				context = new DefaultRelationshipGeometryOperationContext(geom);
			}
			return geometryTrans.invokeOperation(Within.CODE, context);

		} catch (Exception e) {
			throw new EvaluatorException(e);
		}
	}

	public String getName() {
		return "within with geometry";
	}

	public String getCQL() {
		return " within(GeomFromText('" + geometry.toString() + "', " + "'"
				+ srs + "'" + "), " + geomName + ") ";
	}

}

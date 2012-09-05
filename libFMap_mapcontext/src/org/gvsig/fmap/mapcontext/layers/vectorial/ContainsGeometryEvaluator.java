package org.gvsig.fmap.mapcontext.layers.vectorial;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.exception.DataEvaluatorRuntimeException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.relationship.Contains;
import org.gvsig.fmap.geom.operation.relationship.DefaultRelationshipGeometryOperationContext;
import org.gvsig.fmap.geom.operation.towkt.ToWKT;
import org.gvsig.tools.evaluator.AbstractEvaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;
/**
 *
 * @author Vicente Caballero Navarro
 *
 */
public class ContainsGeometryEvaluator extends AbstractEvaluator {

	private String geomName;
	private Geometry geometry;
	private Geometry geometryTrans;
	private String srs;
	private boolean isDefault;
	private String geometryWKT;

	public ContainsGeometryEvaluator(Geometry geometry,
			IProjection projection, FeatureType featureType,
			String geomName) {
		FeatureAttributeDescriptor fad = (FeatureAttributeDescriptor) featureType
				.get(geomName);
		this.isDefault = featureType.getDefaultGeometryAttributeName().equals(
				geomName);
		this.geometry = geometry;
		this.geometryTrans = geometry.cloneGeometry();
		this.srs = projection.getAbrev();
		ICoordTrans ct = projection.getCT(fad.getSRS());
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
			return geometryTrans.invokeOperation(Contains.CODE, context);

		} catch (Exception e) {
			throw new EvaluatorException(e);
		}
	}

	public String getName() {
		return "contains with geometry";
	}

	public String getCQL() {
		if (geometryWKT == null) {
			try {
				geometryWKT = (String) geometry.invokeOperation(ToWKT.CODE,
						null);
			} catch (GeometryOperationNotSupportedException e) {
				throw new DataEvaluatorRuntimeException(e);
			} catch (GeometryOperationException e) {
				throw new DataEvaluatorRuntimeException(e);
			}
		}

		return " contains(GeomFromText('" + geometryWKT + "', " + "'"
				+ srs + "'" + "), " + geomName + ") ";
	}

}

package org.gvsig.fmap.mapcontext.layers.vectorial;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.exception.DataEvaluatorRuntimeException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.towkt.ToWKT;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.evaluator.AbstractEvaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;

public class IntersectsEnvelopeEvaluator extends AbstractEvaluator {

	private Envelope envelope;
	private String geomName;
	private String envelopeWKT = null;
	private Envelope envelopeTrans;
	private boolean isDefault;
	private String srs;

	public IntersectsEnvelopeEvaluator(Envelope envelope,
			IProjection envelopeProjection, FeatureType featureType,
			String geomName) {
		FeatureAttributeDescriptor fad = (FeatureAttributeDescriptor) featureType
				.get(geomName);

		this.isDefault = featureType.getDefaultGeometryAttributeName().equals(
				geomName);
		this.envelope = envelope;
		// this.srs = envelopeProjection.getAbrev();
		// this.projection=CRSFactory.getCRS(fad.getSRS());
		// this.envelopeProjection=envelopeProjection;
//		ICoordTrans ct = null;
//		if (!this.srs.equals(fad.getSRS())) { // FIXME comparación
//			ct = envelopeProjection.getCT(fad.getSRS());
//		}
//		if (ct != null) {
//			this.envelopeTrans = envelope.convert(ct);
//		} else {
			this.envelopeTrans = envelope;
//		}
		this.geomName = geomName;

		this.srs = envelopeProjection.getAbrev();

		this.getFieldsInfo().addMatchFieldValue(geomName, envelopeTrans);

	}

	public Object evaluate(EvaluatorData data) throws EvaluatorException {
		Envelope featureEnvelope;
		if (isDefault) {
			featureEnvelope = ((Feature) data.getContextValue("feature"))
					.getDefaultEnvelope();
		} else {
			featureEnvelope = ((Geometry) data.getDataValue(geomName)).getEnvelope();
		}
		return new Boolean(envelopeTrans.intersects(featureEnvelope));

	}

	public String getName() {
		return "intersets in envelope";
	}

	public String getCQL() {


		if (envelopeWKT == null) {
			try {
				envelopeWKT = (String) envelope.getGeometry().invokeOperation(
						ToWKT.CODE, null);
			} catch (GeometryOperationNotSupportedException e) {
				throw new DataEvaluatorRuntimeException(e);
			} catch (GeometryOperationException e) {
				throw new DataEvaluatorRuntimeException(e);
			}
		}

		return " intersects(GeomFromText('" + envelopeWKT + "', " + "'"
				+ srs + "'" + "), boundary(" + geomName + ")) ";
	}

}

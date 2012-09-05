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

public class CrossEnvelopeEvaluator extends AbstractEvaluator {

	private Envelope envelope;
	private String geomName;
	private Envelope envelopeTrans;
	private boolean isDefault;
	private String srs;
	private String envelopeWKT;

	public CrossEnvelopeEvaluator(Envelope envelope,
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
		if (isDefault) {
			Feature feature = (Feature) data.getContextValue("feature");
			//			return new Boolean(envelopeTrans.contains(feature
			//					.getDefaultEnvelope())
			//					|| envelopeTrans.intersects(feature.getDefaultEnvelope()));
//			System.out.println("== Filter[" + envelopeTrans.getLowerCorner()[0]
//					+ "," + envelopeTrans.getLowerCorner()[1] + "] ["
//					+ envelopeTrans.getUpperCorner()[0] + ","
//					+ envelopeTrans.getUpperCorner()[1] + "]");

			Envelope featureEnvelope = feature.getDefaultEnvelope();
//			System.out.println("== Geom ["
//					+ featureEnvelope.getLowerCorner()[0]
//					+ "," + featureEnvelope.getLowerCorner()[1] + "] ["
//					+ featureEnvelope.getUpperCorner()[0] + ","
//					+ featureEnvelope.getUpperCorner()[1] + "]");
			Boolean r = new Boolean(envelopeTrans.intersects(featureEnvelope));
//			System.out.println("==3 "+r);
			return r;

		} else {
			Geometry geom = (Geometry) data.getDataValue(geomName);
			//			return new Boolean(envelopeTrans.contains(geom.getEnvelope())
			//					|| envelopeTrans.intersects(geom.getEnvelope()));

			return new Boolean(envelopeTrans.intersects(geom.getEnvelope()));

		}
	}

	public String getName() {
		return "contains in envelope";
	}

	public String getCQL() {
		if (envelopeWKT == null) {
			try {
				envelopeWKT = (String) envelope.getGeometry().invokeOperation(
						ToWKT.CODE,
						null);
			} catch (GeometryOperationNotSupportedException e) {
				throw new DataEvaluatorRuntimeException(e);
			} catch (GeometryOperationException e) {
				throw new DataEvaluatorRuntimeException(e);
			}
		}

		return " intersects(GeomFromText('" + envelopeWKT + "', "
				+ "'"
				+ srs + "'" + "), boundary(" + geomName + ")) ";
	}

}

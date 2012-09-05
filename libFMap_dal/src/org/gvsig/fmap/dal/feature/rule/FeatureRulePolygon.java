package org.gvsig.fmap.dal.feature.rule;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.AbstractFeatureRule;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.geom.util.Converter;

public class FeatureRulePolygon extends AbstractFeatureRule {

	public FeatureRulePolygon() {
		super("RulePolygon", "Ensure orientation of geometry");
	}

	public void validate(Feature feature, FeatureStore store)
			throws DataException {

        try {
			Geometry geom = feature.getDefaultGeometry();
			GeneralPathX gp = new GeneralPathX();
			gp.append(geom.getPathIterator(null, Converter.FLATNESS), true);

			if (gp.isClosed()) {
				if (gp.isCCW()) {
					gp.flip();
					GeometryManager geomManager = GeometryLocator.getGeometryManager();
					Surface surface = (Surface)geomManager.create(TYPES.SURFACE, SUBTYPES.GEOM2D);
					surface.setGeneralPath(gp);
					geom = surface;
	        		EditableFeature editable = feature.getEditable();
					editable.setDefaultGeometry(geom);
					store.update(editable);
				}
	        }
		} catch (Exception e) {
			throw new FeatureRulePolygonException(e, store.getName());
		}
	}

	public class FeatureRulePolygonException extends DataException {

		/**
		 *
		 */
		private static final long serialVersionUID = -3014970171661713021L;
		private final static String MESSAGE_FORMAT = "Can't apply rule  in store %(store)s.";
		private final static String MESSAGE_KEY = "_FeatureRulePolygonException";

		public FeatureRulePolygonException(Throwable cause, String store) {
			super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
			this.setValue("store", store);
		}
	}

}

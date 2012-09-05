package org.gvsig.fmap.dal.feature.impl;

import java.util.Arrays;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.*;
import org.gvsig.fmap.dal.feature.exception.CreateGeometryException;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

/**
 *
 * This transform adds a new attribute of type Geometry to the original store's
 * default FeatureType. When applying the transform to a single feature this new
 * attribute is assigned the value of a point whose coordinates proceed from two
 * numeric attributes from the store, called xname, yname.
 *
 */
class MyTransform extends AbstractFeatureStoreTransform {

	private FeatureType originalType;
	private String geomName;
	private String xname;
	private String yname;

	/**
	 * Empty default constructor
	 */
	public MyTransform() {
	}

	/**
	 * Initializes the transform by assigning the source store and the names of
	 * the necessary attributes.
	 *
	 * @param store
	 *            source store.
	 *
	 * @param geomName
	 *            name of the geometry attribute in the default feature type
	 *            from the source store.
	 *
	 * @param xname
	 *            name of the attribute containing the X coordinates
	 *
	 * @param yname
	 *            name of the attribute containing the Y coordinates
	 *
	 * @throws DataException
	 */
	public void initialize(FeatureStore store, String geomName, String xname, String yname) throws DataException {

		// Initialize some data
		this.setFeatureStore(store);
		this.geomName = geomName;
		this.xname = xname;
		this.yname = yname;

		this.originalType = store.getDefaultFeatureType();
		// obtain the feature type, add the new attribute and keep a reference to the resulting feature type
		EditableFeatureType type = store.getDefaultFeatureType().getEditable();
		type.add(geomName, DataTypes.GEOMETRY);
		FeatureType[] types = new FeatureType[] { type.getNotEditableCopy() };
		setFeatureTypes(Arrays.asList(types), types[0]);
	}

	/**
	 * Applies this transform to a target editable feature, using data from the
	 * source feature.
	 */
	public void applyTransform(Feature source, EditableFeature target)
			throws DataException {

		// copy source feature data over target feature
		target.copyFrom(source);

		// calculate and assign new attribute's value
		GeometryManager geomManager = GeometryLocator.getGeometryManager();
		Point point;
		try {
			point = (Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
			point.setX(source.getDouble(xname));
			point.setY(source.getDouble(yname));
			target.setGeometry(this.geomName, point);
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
        	throw new CreateGeometryException(TYPES.POINT, SUBTYPES.GEOM2D, e);
		}
	}

	public void saveToState(PersistentState state) throws PersistenceException {
		state.set("geomName", this.geomName);
		state.set("xname", this.xname);
		state.set("yname", this.yname);
	}

	public void loadFromState(PersistentState state) throws PersistenceException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.feature.FeatureStoreTransform#getSourceFeatureTypeFrom
	 * (org.gvsig.fmap.dal.feature.FeatureType)
	 */
	public FeatureType getSourceFeatureTypeFrom(FeatureType targetFeatureType) {
		return originalType;
	}

	public boolean isTransformsOriginalValues() {
		return false;
	}

}

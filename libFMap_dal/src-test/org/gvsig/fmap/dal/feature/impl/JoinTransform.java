package org.gvsig.fmap.dal.feature.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.AbstractFeatureStoreTransform;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;

public class JoinTransform extends AbstractFeatureStoreTransform {

	/**
	 * Store from which the join transform will get the additional attributes
	 */
	private FeatureStore store2;

	/**
	 * name of the key attr in store1 that will be used to match features in
	 * store2
	 */
	private String keyAttr1;

	/**
	 * name of the key attr in store2 that will be used to match features in
	 * store1
	 */
	private String keyAttr2;

	/**
	 * names of the attributes to join from store2 to store1
	 */
	private String[] attrs;

	/**
	 * Attribute names may change after transformation if they are repeated in
	 * both stores. This map keeps correspondence between store2 original names
	 * and their transformed counterparts.
	 */
	private Map targetNamesMap;

	private FeatureType originalFeatureType;

	/**
	 * A default constructor
	 */
	public JoinTransform() {
		targetNamesMap = new HashMap();
	}

	/**
	 * Initializes all the necessary data for this transform
	 *
	 * @param store1
	 *            store whose default feature type is the target of this
	 *            transform
	 *
	 * @param store2
	 *            store whose default feature type will provide the new
	 *            attributes to join
	 *
	 * @param keyAttr1
	 *            key attribute in store1 that matches keyAttr2 in store2
	 *            (foreign key), used for joining both stores.
	 *
	 * @param keyAttr2
	 *            key attribute in store2 that matches keyAttr1 in store2
	 *            (foreign key), used for joining both stores.
	 *
	 * @param attrs
	 *            names of the attributes in store2 that will be joined to
	 *            store1.
	 */
	public void initialize(FeatureStore store1, FeatureStore store2,
			String keyAttr1, String keyAttr2, String[] attrs)
			throws DataException {

		// Initialize needed data
		this.setFeatureStore(store1);

		this.store2 = store2;
		this.keyAttr1 = keyAttr1;
		this.keyAttr2 = keyAttr2;
		this.attrs = attrs;
		this.originalFeatureType = this.getFeatureStore()
				.getDefaultFeatureType();

		// calculate this transform resulting feature type
		// by adding all specified attrs from store2 to store1's default
		// feature type
		EditableFeatureType type = this.getFeatureStore().getDefaultFeatureType().getEditable();

		for (int i = 0; i < attrs.length; i++) {
			String name = attrs[i];

			// If an attribute already exists with the same name in store1's
			// default feature type,
			// calculate an alternate name and add it to our type
			int j = 0;
			while (type.getIndex(name) >= 0) {
				name = attrs[i] + "_" + ++j;
			}
			type.add(name, store2.getDefaultFeatureType()
					.getAttributeDescriptor(attrs[i]).getDataType());

			// keep correspondence between original name and transformed name
			this.targetNamesMap.put(attrs[i], name);
		}

		// assign calculated feature type as this transform's feature type
		FeatureType[] types = new FeatureType[] { type.getNotEditableCopy() };
		setFeatureTypes(Arrays.asList(types), types[0]);
	}

	/**
	 *
	 *
	 * @param source
	 *
	 * @param target
	 *
	 * @throws DataException
	 */
	public void applyTransform(Feature source, EditableFeature target)
			throws DataException {

		// copy the data from store1 into the resulting feature
		target.copyFrom(source);

		// ask store2 for the specified attributes, filtering by the key
		// attribute value
		// from the source feature
		Evaluator eval = DALLocator.getDataManager().createExpresion(
				keyAttr2 + "=" + source.get(keyAttr1));
		FeatureQuery query = this.getFeatureStore().createFeatureQuery();
		query.setAttributeNames(attrs);
		query.setFilter(eval);
		FeatureSet set = store2.getFeatureSet(query);

		// In this join implementation, we will take only the first matching
		// feature found in store2
		Iterator it = set.iterator();
		if (it.hasNext()) {
			Feature feat = (Feature) it.next();

			// copy all attributes from joined feature to target
			Iterator it2 = feat.getType().iterator();
			while (it2.hasNext()) {
				FeatureAttributeDescriptor attr = (FeatureAttributeDescriptor) it2
						.next();
				// find original attribute name
				String targetName = (String) this.targetNamesMap.get(attr
						.getName());
				// copy its value to target feature attribute
				target.set(targetName, feat.get(attr.getName()));
			}
		}
		set.dispose();
	}

	public void saveToState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	public void loadFromState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.feature.FeatureStoreTransform#getSourceFeatureTypeFrom
	 * (org.gvsig.fmap.dal.feature.FeatureType)
	 */
	public FeatureType getSourceFeatureTypeFrom(FeatureType targetFeatureType) {
		return this.originalFeatureType;
	}

	public boolean isTransformsOriginalValues() {
		return false;
	}

}

package org.gvsig.app.join.dal.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.AbstractFeatureStoreTransform;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;
import org.gvsig.tools.evaluator.EvaluatorFieldsInfo;
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

	private JoinTransformEvaluator evaluator = null;

	private FeatureType originalFeatureType;

	private String[] attrsForQuery;

	private String prefix1;

	private String prefix2;

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
			String keyAttr1, String keyAttr2, String prefix1, String prefix2,
			String[] attrs)
			throws DataException {

		if (store1 == store2) {
			throw new IllegalArgumentException("store1 == store2");
		}

		// Initialize needed data
		this.setFeatureStore(store1);
		this.store2 = store2;
		this.keyAttr1 = keyAttr1;
		this.keyAttr2 = keyAttr2;
		this.prefix1 = prefix1; // TODO
		this.prefix2 = prefix2; // TODO
		this.attrs = attrs;

		// calculate this transform resulting feature type
		// by adding all specified attrs from store2 to store1's default
		// feature type
		// FIXME for more than one FTypes ??
		this.originalFeatureType = this.getFeatureStore()
				.getDefaultFeatureType();

		// TODO tener en cuenta prefix1
		EditableFeatureType type = this.getFeatureStore().getDefaultFeatureType().getEditable();

		FeatureType type2 = store2.getDefaultFeatureType();

		// TODO tener en cuenta prefix2
		for (int i = 0; i < attrs.length; i++) {
			String name = attrs[i];

			// If an attribute already exists with the same name in store1's
			// default feature type,
			// calculate an alternate name and add it to our type
			int j = 0;
			while (type.getIndex(name) >= 0) {
				name = attrs[i] + "_" + ++j;
			}
			type.add(name,
					type2.getAttributeDescriptor(attrs[i]).getDataType());

			// keep correspondence between original name and transformed name
			this.targetNamesMap.put(attrs[i], name);
		}
		if (this.targetNamesMap.containsKey(keyAttr2)) {
			this.attrsForQuery = this.attrs;
		} else {
			ArrayList list = new ArrayList(this.attrs.length + 1);
			list.addAll(Arrays.asList(this.attrs));
			list.add(keyAttr2);
			this.attrsForQuery = (String[]) list.toArray(new String[] {});
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
		this.copySourceToTarget(source, target);

		// ask store2 for the specified attributes, filtering by the key
		// attribute value
		// from the source feature
		JoinTransformEvaluator eval = this.getEvaluator();
		eval.updateValue(source.get(this.keyAttr1));

		FeatureQuery query = store2.createFeatureQuery();
		query.setAttributeNames(attrsForQuery);
		query.setFilter(eval);
		FeatureSet set = null;
		DisposableIterator itFeat = null;

		try {

		set = store2.getFeatureSet(query);
		// In this join implementation, we will take only the first matching
		// feature found in store2

		Feature feat;


		itFeat = set.iterator();
		if (itFeat.hasNext()) {
			feat = (Feature) itFeat.next();

			// copy all attributes from joined feature to target
			this.copyJoinToTarget(feat, target);
		}
		} finally {
			if (itFeat != null) {
				itFeat.dispose();
			}
			if (set != null) {
				set.dispose();
			}
		}
	}

	/**
	 * @param feat
	 * @param target
	 */
	private void copyJoinToTarget(Feature join, EditableFeature target) {
		Iterator iter = targetNamesMap.entrySet()
				.iterator();
		Entry entry;
		FeatureType trgType = target.getType();
		FeatureAttributeDescriptor attr;
		while (iter.hasNext()) {
			entry = (Entry) iter.next();
			attr = trgType.getAttributeDescriptor((String) entry.getValue());
			if (attr != null) {
				target.set(attr.getIndex(), join.get((String) entry.getKey()));
			}
		}


	}

	/**
	 * @param source
	 * @param target
	 */
	private void copySourceToTarget(Feature source, EditableFeature target) {
		FeatureAttributeDescriptor attr, attrTrg;
		FeatureType ftSrc = source.getType();
		FeatureType ftTrg = target.getType();


		for (int i = 0; i < source.getType().size(); i++) {
			attr = ftSrc.getAttributeDescriptor(i);
			attrTrg = ftTrg.getAttributeDescriptor(attr.getName());
			if (attrTrg != null) {
				try {
					target.set(attrTrg.getIndex(), source.get(i));
				} catch (IllegalArgumentException e) {
					attrTrg = ftTrg.getAttributeDescriptor(attr.getName());
					target.set(attrTrg.getIndex(), attrTrg.getDefaultValue());
				}

			}
		}

	}

	private JoinTransformEvaluator getEvaluator() {
		if (this.evaluator == null){
			this.evaluator = new JoinTransformEvaluator(keyAttr2);
		}
		return evaluator;

	}

	private class JoinTransformEvaluator implements Evaluator {

		private String attribute;
		private Object value;
		private String cql;
		private EvaluatorFieldsInfo info = null;

		//		private int attributeIndex;

		public JoinTransformEvaluator(String attribute) {
			this.attribute = attribute;
			this.value = null;
			this.info = new EvaluatorFieldsInfo();

			//			this.attributeIndex = attrIndex;
		}

		public void updateValue(Object value) {
			this.value = value;
			this.cql = this.attribute + "= '" + this.value + "'";
			this.info = new EvaluatorFieldsInfo();
			this.info.addMatchFieldValue(this.attribute, value);
		}

		public Object evaluate(EvaluatorData arg0) throws EvaluatorException {
			Object curValue = arg0.getDataValue(attribute);
			if (curValue == null) {
				return value == null;
			}
			return curValue.equals(value);
		}

		public String getCQL() {
			return this.cql;
		}

		public String getDescription() {
			return "Evaluates join transform match";
		}

		public String getName() {
			return "JoinTransformEvaluator";
		}

		public EvaluatorFieldsInfo getFieldsInfo() {
			return this.info;
		}

	}

	public void saveToState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	public void loadFromState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	public FeatureType getSourceFeatureTypeFrom(FeatureType arg0) {
		EditableFeatureType orgType = originalFeatureType.getEditable();
		Iterator iter = arg0.iterator();
		FeatureAttributeDescriptor attr;
		ArrayList toRetain = new ArrayList();
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			if (this.targetNamesMap.containsValue(attr.getName())) {
				continue;
			}
			toRetain.add(attr.getName());
		}

		if (!toRetain.contains(keyAttr1)) {
			toRetain.add(keyAttr1);
		}

		iter = originalFeatureType.iterator();
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			if (!toRetain.contains(attr.getName())) {
				orgType.remove(attr.getName());
			}

		}

		return orgType.getNotEditableCopy();
	}

	public boolean isTransformsOriginalValues() {
		return false;
	}

}

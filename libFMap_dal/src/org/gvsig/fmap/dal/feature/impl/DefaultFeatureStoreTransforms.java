package org.gvsig.fmap.dal.feature.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.dal.feature.FeatureStoreTransform;
import org.gvsig.fmap.dal.feature.FeatureStoreTransforms;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.Persistent;
import org.gvsig.tools.persistence.PersistentState;

public class DefaultFeatureStoreTransforms implements FeatureStoreTransforms,
		Persistent {

	private DefaultFeatureStore store;
	private List transforms;
	private Boolean isTramsformValues;
	private TransformTemporalList lastTransformStack;

	public DefaultFeatureStoreTransforms() {
		this.store = null;
		this.transforms = new ArrayList();
		this.isTramsformValues = Boolean.FALSE;
	}

	public DefaultFeatureStoreTransforms(DefaultFeatureStore store) {
		this.store = store;
		this.transforms = new ArrayList();
	}

	protected void checkEditingMode() {
		if (store == null || store.getMode() != FeatureStore.MODE_QUERY) {
			throw new IllegalStateException();
		}
	}

	protected void notifyChangeToStore() {
		this.store.notifyChange(FeatureStoreNotification.TRANSFORM_CHANGE);
	}

	public FeatureStoreTransform add(FeatureStoreTransform transform)
			throws DataException {
		checkEditingMode();
		if( ! transform.getFeatureTypes().contains(transform.getDefaultFeatureType())) {
			throw new IllegalArgumentException(); // FIXME: Añadir tipo especifico.
		}
		this.transforms.add(transform);
		this.notifyChangeToStore();
		if (this.isTramsformValues == null
				|| (!this.isTramsformValues.booleanValue())) {
			if (transform.isTransformsOriginalValues()) {
				this.isTramsformValues = Boolean.TRUE;
			}

		}

		return transform;
	}


	public void clear() {
		checkEditingMode();
		this.transforms.clear();
		this.notifyChangeToStore();
		this.isTramsformValues = Boolean.FALSE;
	}

	public FeatureStoreTransform getTransform(int index) {
		return (FeatureStoreTransform) this.transforms.get(index);
	}

	public Iterator iterator() {
		return Collections.unmodifiableList(transforms).iterator();
	}

	public Object remove(int index) {
		checkEditingMode();
		Object trans = this.transforms.remove(index);
		this.notifyChangeToStore();
		this.isTramsformValues = null;
		return trans;
	}

	public boolean remove(FeatureStoreTransform transform) {
		checkEditingMode();
		boolean removed = this.transforms.remove(transform);
		if (removed) {
			this.notifyChangeToStore();

		}
		this.isTramsformValues = null;
		return removed;

	}

	public int size() {
		return this.transforms.size();
	}

	public boolean isEmpty() {
		return this.transforms.isEmpty();
	}

	private class TransformTemporalListElement {
		public FeatureStoreTransform transform = null;
		public FeatureType targetFeatureType = null;
	}

	private class TransformTemporalList extends ArrayList {
		private FeatureType targetFType;

		public boolean add(Object arg0) {
			if (this.isEmpty()) {
				targetFType = ((TransformTemporalListElement) arg0).targetFeatureType;
			}
			return super.add(arg0);
		}

	}

	protected TransformTemporalList getTransformTemporalList(
			FeatureType targetFeatureType) {
		if (this.lastTransformStack == null
				|| this.lastTransformStack.size() != this.transforms.size()
				|| !(this.lastTransformStack.targetFType
						.equals(targetFeatureType))) {
			TransformTemporalList result = new TransformTemporalList();
			TransformTemporalListElement item;
			FeatureType nextFType = targetFeatureType;

			for (int i = transforms.size() - 1; i > -1; i--) {
				item = new TransformTemporalListElement();
				item.transform = (FeatureStoreTransform) transforms.get(i);
				item.targetFeatureType = nextFType;
				nextFType = item.transform
						.getSourceFeatureTypeFrom(item.targetFeatureType);
				result.add(item);
			}
			this.lastTransformStack = result;
		}
		return this.lastTransformStack;
	}

	public Feature applyTransform(DefaultFeature source,
			FeatureType targetFeatureType)
			throws DataException {
		if (this.transforms.isEmpty()) {
			return source;
		}

		TransformTemporalList stack = this
				.getTransformTemporalList(targetFeatureType);
		TransformTemporalListElement item;
		FeatureProvider targetData;
		EditableFeature target;
		ListIterator iterator = stack.listIterator(stack.size());


		while (iterator.hasPrevious()) {
			item = (TransformTemporalListElement) iterator.previous();
			targetData = this.store
					.createDefaultFeatureProvider(item.targetFeatureType);
			targetData.setOID(source.getData().getOID());
			targetData.setNew(false);
			target = (new DefaultEditableFeature(this.store, targetData))
					.getEditable();
			item.transform.applyTransform(source, target);
 			source = (DefaultFeature) target.getNotEditableCopy();
		}

		return source;

	}

	public FeatureType getSourceFeatureTypeFrom(FeatureType targetFeatureType) {
		FeatureType tmpFType = targetFeatureType;

		for (int i = transforms.size() - 1; i > -1; i--) {
			FeatureStoreTransform transform = (FeatureStoreTransform) transforms
					.get(i);
			tmpFType = transform.getSourceFeatureTypeFrom(tmpFType);
		}
		return tmpFType;
	}

	public FeatureType getDefaultFeatureType() throws DataException {
		if (this.transforms.isEmpty()) {
			return null;
		}
		FeatureStoreTransform transform = (FeatureStoreTransform) this.transforms
				.get(this.transforms.size() - 1);
		return transform.getDefaultFeatureType();
	}

	public List getFeatureTypes() throws DataException {
		if (this.transforms.isEmpty()) {
			return null;
		}
		FeatureStoreTransform transform = (FeatureStoreTransform) this.transforms
				.get(this.transforms.size() - 1);
		return transform.getFeatureTypes();
	}

	public void saveToState(PersistentState state) throws PersistenceException {
		this.isTramsformValues = null;
		state.set("transforms", this.iterator());
		Iterator iter = this.iterator();
		while (iter.hasNext()) {
			((FeatureStoreTransform) iter.next()).setFeatureStore(store);
		}
	}

	public void loadFromState(PersistentState state) throws PersistenceException {
		// TODO
		this.isTramsformValues = null;
	}

	public FeatureStore getFeatureStore() {
		return this.store;
	}

	public void setFeatureStore(FeatureStore featureStore) {
		if (this.store != null) {
			throw new IllegalStateException();// FIXME: Añadir tipo especifico.
		}
		this.store = (DefaultFeatureStore) featureStore;
	}

	public boolean isTransformsOriginalValues() {
		if (this.isTramsformValues == null) {
			Iterator iter = this.transforms.iterator();
			FeatureStoreTransform transform;
			this.isTramsformValues = Boolean.FALSE;
			while (iter.hasNext()) {
				transform = (FeatureStoreTransform) iter.next();
				if (transform.isTransformsOriginalValues()){
					this.isTramsformValues = Boolean.TRUE;
					break;
				}
			}
		}
		return this.isTramsformValues.booleanValue();
	}

	public FeatureType getFeatureType(String featureTypeId)
			throws DataException {
		if (this.transforms.isEmpty()) {
			return null;
		}
		if (featureTypeId == null) {
			return this.getDefaultFeatureType();
		}
		Iterator iter = this.getFeatureTypes().iterator();
		FeatureType fType;
		while (iter.hasNext()) {
			fType = (FeatureType) iter.next();
			if (fType.getId().equals(featureTypeId)) {
				return fType;
			}
		}
		return null;
	}

}

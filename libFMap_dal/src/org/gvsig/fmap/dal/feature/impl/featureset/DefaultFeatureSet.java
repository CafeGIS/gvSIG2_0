package org.gvsig.fmap.dal.feature.impl.featureset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.FeatureQueryOrder.FeatureQueryOrderMember;
import org.gvsig.fmap.dal.feature.exception.ConcurrentDataModificationException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureStore;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureStoreTransforms;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.visitor.Visitor;

public class DefaultFeatureSet implements FeatureSet, Observer {


	private static final int NO_CHECKED = -1;
	private static final int DEFAULT = 0;
	private static final int FILTERED = 1;
	private static final int ORDERED = 2;
	private static final int ORDERED_FILTERED = 3;
	private static final int EDITED = 4;
	private static final int EDITED_FILTERED = 5;
	private static final int ORDERD_EDITED = 6;
	private static final int ORDERED_EDITED_FILTER = 7;

	private boolean modified;
	DefaultFeatureStore store;
	private List featureTypes;
	FeatureQuery query;
	FeatureSetProvider provider;
	private long size;
	private int iteratorMode;
	List orderedData;
	private Feature featureToIgnoreNotification;
	private List providerFeatureTypes;
	DefaultFeatureStoreTransforms transform;
	private FeatureQuery queryForProvider;
	private FeatureType defatulFeatureType;
	private FeatureType defatulFeatureTypeForProvider;


	public DefaultFeatureSet(DefaultFeatureStore store, FeatureQuery query)
	throws DataException {
		this.featureToIgnoreNotification = null;
		this.iteratorMode = NO_CHECKED;
		this.modified = false;
		this.size = -1;
		this.orderedData = null;
		this.store = store;
		if (this.store.isEditing()) {
			this.transform = this.store.getFeatureTypeManager().getTransforms();
		} else {
			this.transform = (DefaultFeatureStoreTransforms) store
				.getTransforms();
		}
		this.query = query;
		this.queryForProvider = query.getCopy();

		this.featureTypes = new ArrayList();
		if (this.query.getFeatureTypeId() == null
				&& this.query.getAttributeNames() == null) {
			this.defatulFeatureType = this.store.getDefaultFeatureType();
			this.featureTypes.addAll(this.store.getFeatureTypes());
		} else {
			this.defatulFeatureType = this.store.getFeatureType(this.query);
			this.featureTypes.add(this.defatulFeatureType);
		}
		if (!this.transform.isEmpty()) {
			this.fixQueryForProvider(this.queryForProvider, this.transform);
		} else {
			this.defatulFeatureTypeForProvider = this.defatulFeatureType;
		}

		if (this.queryForProvider.hasFilter() && store.getIndexes() != null) {
			this.provider = (FeatureSetProvider) store.getIndexes()
					.getFeatureSet(this.queryForProvider.getFilter());
		}
		if (this.provider == null) {
			this.provider = this.store.getProvider().createSet(
					this.queryForProvider, this.defatulFeatureTypeForProvider);
		}
		this.store.addObserver(this);
	}

	private void fixQueryForProvider(FeatureQuery theQueryForProvider,
			DefaultFeatureStoreTransforms transformsToUse) throws DataException {
		theQueryForProvider.setAttributeNames(null);
		FeatureType ftype = transformsToUse
				.getSourceFeatureTypeFrom(this.defatulFeatureType);
		theQueryForProvider.setFeatureTypeId(ftype.getId());
		this.defatulFeatureTypeForProvider = ftype;

		if (transformsToUse.isTransformsOriginalValues()) {
			theQueryForProvider.setFilter(null);
			theQueryForProvider.getOrder().clear();
			return;

		}

		// Filter
		Evaluator filter = theQueryForProvider.getFilter();
		if (filter != null) {
			boolean canUseFilter = true;
			if (filter.getFieldsInfo() == null) {
				canUseFilter = false;
			} else {
				canUseFilter = areEvaluatorFieldsInAttributes(filter, ftype);
			}

			if (!canUseFilter) {
				theQueryForProvider.setFilter(null);
			}

		}


		// Order
		boolean canUseOrder = true;
		Iterator iter = theQueryForProvider.getOrder().iterator();
		FeatureQueryOrderMember item;
		while (iter.hasNext()) {
			item = (FeatureQueryOrderMember) iter.next();
			if (item.hasEvaluator()) {
				if (!areEvaluatorFieldsInAttributes(item.getEvaluator(), ftype)) {
					canUseOrder = false;
					break;
				}
			} else {
				if (ftype.get(item.getAttributeName()) == null) {
					canUseOrder = false;
					break;
				}
			}
		}

		if (!canUseOrder) {
			theQueryForProvider.getOrder().clear();
		}


	}

	private boolean areEvaluatorFieldsInAttributes(Evaluator evaluator,
			FeatureType fType) {
		if (evaluator.getFieldsInfo() == null) {
			return false;
		}
		String[] fieldNames = evaluator.getFieldsInfo().getFieldNames();
		if (fieldNames.length == 0) {
			return false;
		} else {
			for (int i = 0; i < fieldNames.length; i++) {
				if (fType.get(fieldNames[i]) == null) {
					return false;
				}

			}
		}
		return true;
	}

	public FeatureType getDefaultFeatureType() {
		return this.defatulFeatureType;
	}

	public List getFeatureTypes() {
		return Collections.unmodifiableList(this.featureTypes);
	}

	public long getSize() throws DataException {
		this.checkModified();
		if (size < 0) {
			size = calculateSize();
		}
		return size;
	}

	private long calculateSize() throws DataException {
		int mode = this.getIteratorMode();
		if ((mode & FILTERED) == FILTERED) {
			long mySize =0;
			Iterator iter = this.fastIterator();
			try{
				while (true) {
					iter.next();
					mySize++;
				}
			} catch (NoSuchElementException e){
				return mySize;
			}
		} else if ((mode & EDITED) == EDITED) {
			return provider.getSize()
			+ store.getFeatureManager().getDeltaSize();
		}
		return provider.getSize();
	}

	public void dispose() {
		this.store.deleteObserver(this);
		this.provider.dispose();
		this.provider = null;

		this.featureToIgnoreNotification = null;
		if (orderedData != null) {
			orderedData.clear();
		}
		this.orderedData = null;
		this.store = null;
		this.transform = null;
		this.query = null;
		this.queryForProvider = null;
		this.featureTypes = null;
		this.providerFeatureTypes = null;
		this.defatulFeatureType = null;
		this.defatulFeatureTypeForProvider = null;


	}

	public boolean isFromStore(DataStore store) {
		return this.store.equals(store);
	}

	public void update(Observable obsevable, Object notification) {
		if (modified) {
			return;
		}

		String type = ((FeatureStoreNotification) notification).getType();

		if (
				type.equalsIgnoreCase(FeatureStoreNotification.AFTER_INSERT)
				|| type.equalsIgnoreCase(FeatureStoreNotification.AFTER_DELETE)
				|| type.equalsIgnoreCase(FeatureStoreNotification.AFTER_UPDATE)
		) {
			if( this.featureToIgnoreNotification == ((FeatureStoreNotification) notification).getFeature() ) {
				return;
			}
			modified = true;
			return;
		}
		if (
				type.equalsIgnoreCase(FeatureStoreNotification.AFTER_UPDATE_TYPE)
				|| type.equalsIgnoreCase(FeatureStoreNotification.AFTER_REDO)
				|| type.equalsIgnoreCase(FeatureStoreNotification.AFTER_UNDO)
				|| type.equalsIgnoreCase(FeatureStoreNotification.AFTER_CANCELEDITING)
				|| type.equalsIgnoreCase(FeatureStoreNotification.AFTER_REFRESH)
				|| type.equalsIgnoreCase(FeatureStoreNotification.COMPLEX_NOTIFICATION)
				|| type.equalsIgnoreCase(FeatureStoreNotification.AFTER_CLOSE)
				|| type.equalsIgnoreCase(FeatureStoreNotification.AFTER_DISPOSE)
				|| type.equalsIgnoreCase(FeatureStoreNotification.RESOURCE_CHANGED)
				|| type.equalsIgnoreCase(FeatureStoreNotification.TRANSFORM_CHANGE)
		) {
			modified = true;
			return;
		}
	}

	public void accept(Visitor visitor) throws BaseException {
		Iterator iterator = iterator();

		while (iterator.hasNext()) {
			Feature feature = (Feature) iterator.next();
			visitor.visit(feature);
		}
	}

	protected void checkModified() {
		if (modified) {
			throw new ConcurrentDataModificationException(store.getName());
		}
	}

	public boolean isEmpty() throws DataException {
		checkModified();
		if (this.store.isEditing()) {
			if (this.store.getFeatureManager().hasNews()) {
				return false;
			}
			if (this.provider.isEmpty()) {
				return true;
			}
			return this.provider.getSize()
			+ this.store.getFeatureManager().getDeltaSize() == 0;
		}
		return this.provider.isEmpty();
	}

	public DisposableIterator fastIterator() throws DataException {
		return this.fastIterator(0);
	}

	public DisposableIterator fastIterator(long index) throws DataException {
		if (index < 0) {
			throw new IndexOutOfBoundsException("The index (" + index
					+ ") is less than 0");
		}
		int mode = this.getIteratorMode();

		switch (mode) {
		case DEFAULT:
			return new FastDefaultIterator(this, index);

		case FILTERED:
			return new FastFilteredIterator(this, index);


		case ORDERED:
			if (this.orderedData != null) {
				return new FastOrderedIterator(this, index);
			} else {
				return new FastOrderedIterator(this, new FastDefaultIterator(
						this, 0), index);
			}

		case ORDERED_FILTERED:
			if (this.orderedData != null) {
				return new FastOrderedIterator(this, index);
			} else {
				return new FastOrderedIterator(this, new FastFilteredIterator(
						this, 0), index);
			}

		case EDITED:
			return new FastEditedIterator(this, index);

		case EDITED_FILTERED:
			return new FastEditedFilteredIterator(this, index);

		case ORDERD_EDITED:
			if (this.orderedData != null) {
				return new FastOrderedIterator(this, index);
			} else {
				return new FastOrderedIterator(this, new FastEditedIterator(
						this, 0), index);
			}

		case ORDERED_EDITED_FILTER:
			if (this.orderedData != null) {
				return new FastOrderedIterator(this, index);
			} else {
				return new FastOrderedIterator(this,
						new FastEditedFilteredIterator(this, 0), index);
			}
		default:
			throw new IllegalArgumentException();
		}
	}

	public DisposableIterator iterator() throws DataException {
		return this.iterator(0);
	}

	public DisposableIterator iterator(long index) throws DataException {
		if (index < 0) {
			throw new IndexOutOfBoundsException("The index (" + index
					+ ") is less than 0");
		}
		int mode = this.getIteratorMode();

		switch (mode) {
		case DEFAULT:
			return new DefaultIterator(this, index);

		case FILTERED:
			return new FilteredIterator(this, index);

		case ORDERED:
			if (orderedData != null) {
				return new OrderedIterator(this, index);

			} else {
				return new OrderedIterator(this, new DefaultIterator(this, 0),
						index);
			}

		case ORDERED_FILTERED:
			return new OrderedIterator(this, new FilteredIterator(this, 0),
					index);

		case EDITED:
			return new EditedIterator(this, index);

		case EDITED_FILTERED:
			return new EditedFilteredIterator(this, index);

		case ORDERD_EDITED:
			return new OrderedIterator(this,
					new EditedIterator(this, 0), index);

		case ORDERED_EDITED_FILTER:
			return new OrderedIterator(this,
					new EditedFilteredIterator(this, 0), index);

		default:
			throw new IllegalArgumentException();
		}

	}

	private boolean providerCanOrder() {
		return this.provider.canOrder();
	}

	private boolean providerCanFilter() {
		return this.provider.canFilter();
	}

	private int getIteratorMode() {

		if (this.iteratorMode != NO_CHECKED) {
			return this.iteratorMode;
		}

		// TODO Tener en cuenta las transformaciones ???

		if (store.isEditing() && store.getFeatureManager().hasChanges()) {
			if (this.query.hasOrder()) { // En edicion siempre ordeno yo.
				if (this.query.hasFilter()) {
					return ORDERED_EDITED_FILTER;
				} else {
					return ORDERD_EDITED;
				}
			} else {
				if (this.query.hasFilter()) {
					return EDITED_FILTERED;
				} else {
					return EDITED;
				}
			}
		} else {
			boolean useMyFilter = this.query.hasFilter();
			boolean useMyOrder = this.query.hasOrder();
			if (this.providerCanOrder() && this.transform.isEmpty()) {
				useMyOrder = false;
			}
			if (this.providerCanFilter() && this.transform.isEmpty()) {
				useMyFilter = false;
			}

			if (useMyOrder) {
				if (useMyFilter) {
					return ORDERED_FILTERED;// ORDERED_FILTERED;
				} else {
					return ORDERED;// ORDERED;
				}
			} else {
				if (useMyFilter) {
					return FILTERED;// FILTERED;
				} else {
					return DEFAULT;// DEFAULT;
				}
			}
		}

	}

	public void delete(Feature feature) throws DataException {
		this.featureToIgnoreNotification = feature;
		this.store.delete(feature);
		if (this.size > 0) {
			this.size--;
		}
		this.featureToIgnoreNotification = null;
	}

	public void insert(EditableFeature feature) throws DataException {
		this.featureToIgnoreNotification = feature;
		this.store.insert(feature);
		if (this.size >= 0) {
			this.size++;
		}
		this.featureToIgnoreNotification = null;
	}

	public void update(EditableFeature feature) throws DataException {
		this.featureToIgnoreNotification = feature;
		this.store.update(feature);
		this.featureToIgnoreNotification = null;
	}

}

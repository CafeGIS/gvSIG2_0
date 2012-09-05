/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I. S.A.   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.feature.testmulithread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.exception.ConcurrentDataModificationException;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;

/**
 * @author jmvivo
 *
 */
public class UpdateFeature extends StoreTask {

	private List updateList = new ArrayList();

	public static final int UPDATE_ALL_FEATURES = -2;
	public static final int UPDATE_LAST_FEATURE = -1;

	/**
	 * @param store
	 * @param timeToWait
	 */
	public UpdateFeature(String id, FeatureStore store) {
		super(id, store);
	}

	/**
	 * @param store
	 * @param timeToWait
	 */
	public UpdateFeature(String id, FeatureStore store, int timeToWait) {
		super(id, store, timeToWait);
	}

	public void addUpdate(int featureToUpdate, String attName, Object newValue) {
		UpdateItem item = new UpdateItem();
		item.featureToUpdate = featureToUpdate;
		item.attributeName = attName;
		item.newValue = newValue;
		item.evaluator = null;
		this.updateList.add(item);
	}

	public void addUpdate(int featureToUpdate, String attName,
			Evaluator evaluator) {
		UpdateItem item = new UpdateItem();
		item.featureToUpdate = featureToUpdate;
		item.attributeName = attName;
		item.newValue = null;
		item.evaluator = evaluator;
		this.updateList.add(item);
	}

	public Iterator getUpdateList() {
		return this.updateList.iterator();
	}

	public UpdateItem getUpdate(int index) {
		return (UpdateItem) this.updateList.get(index);
	}

	public int getUpdateListSize() {
		return this.updateList.size();
	}

	public EditableFeature applyUpdateList(Feature feature, long index,
			long lastIndex)
			throws DataException, EvaluatorException {
		Iterator iter = this.getUpdateList();
		EditableFeature editable = feature.getEditable();
		boolean hasChanges = false;
		while (iter.hasNext()) {
			UpdateItem update = (UpdateItem) iter.next();
			if (index == update.featureToUpdate
					|| update.featureToUpdate == UPDATE_ALL_FEATURES
					|| (update.featureToUpdate == UPDATE_LAST_FEATURE && (index == lastIndex))) {
				if (update.evaluator != null) {
					editable.set(update.attributeName, update.evaluator
							.evaluate((EvaluatorData) feature));
				} else {
					editable.set(update.attributeName, update.newValue);
				}
				hasChanges = true;
			}

		}
		if (hasChanges) {
			return editable;
		}
		return null;
	}

	public void run() {
		if (!this.startProcess()) {
			return;
		}
		if (this.updateList.size() == 0) {
			finishedNoOk();
			return;
		}
		FeatureSet set;
		DisposableIterator iter = null;
		try {
			set = this.store.getFeatureSet();
		} catch (DataException e) {
			finishedError(e);
			return;
		}
		try {
			long size = set.getSize();
			EditableFeature newFeature = this.store.createNewFeature(true);

			long i = 0;
			try {

				iter = set.iterator();
				while (iter.hasNext()) {
					Feature feature = (Feature) iter.next();
					EditableFeature result = this.applyUpdateList(feature,
							i, size - 1);
					if (result != null) {
						set.update(result);
					}
					i++;
				}
			} catch (ConcurrentDataModificationException e) {
				finishedConcurrentError(e);
				return;
			} catch (DataException e) {
				finishedError(e);
				return;
			}

			if ((size == set.getSize()) && (set.getSize() == i)) {
				finishedOk();
			} else {
				finishedNoOk();
			}
		} catch (Throwable e) {
			finishedError(e);
			return;
		} finally {
			iter.dispose();
			set.dispose();
		}
	}


	public class UpdateItem{
		public long featureToUpdate = UPDATE_ALL_FEATURES;
		public String attributeName = null;
		public Evaluator evaluator = null;
		public Object newValue = null;

	}

}

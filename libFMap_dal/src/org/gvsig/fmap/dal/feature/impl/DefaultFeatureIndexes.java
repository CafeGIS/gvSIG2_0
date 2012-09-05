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
 * 2008 {{Company}}   {{Task}}
 */

package org.gvsig.fmap.dal.feature.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureIndex;
import org.gvsig.fmap.dal.feature.FeatureIndexes;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProviderServices;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorFieldValue;
import org.gvsig.tools.evaluator.EvaluatorFieldValueMatch;
import org.gvsig.tools.evaluator.EvaluatorFieldValueNearest;
import org.gvsig.tools.evaluator.EvaluatorFieldValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides access to a FeatureStore local indexes and also decides
 * which index to use given an evaluator containing the filter expression.
 *
 * @author jyarza
 */
public class DefaultFeatureIndexes implements FeatureIndexes {
	private static final Logger logger = LoggerFactory
			.getLogger(DefaultFeatureIndexes.class);
	// Access by index name
	private Map names;
	// Store to which this belongs
	private DefaultFeatureStore store;

	/**
	 * Creates an empty DataIndexes for the given FeatureStore
	 *
	 * @param store
	 *            FeatureStore to whom this belongs
	 * @throws DataException
	 */
	public DefaultFeatureIndexes(DefaultFeatureStore store)
			throws DataException {
		names = new HashMap();
		this.store = store;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.index.DataIndexes#getDataIndex(java.lang.String)
	 */
	public FeatureIndex getFeatureIndex(String name) {
		return (FeatureIndex) names.get(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.index.DataIndexes#addIndex(org.gvsig.fmap.dal.feature.FeatureType,
	 *      java.lang.String, org.gvsig.fmap.dal.feature.DataIndex)
	 */
	public void addIndex(FeatureIndexProviderServices index) {
		// By name
		names.put(index.getName(), index);
	}

	public Iterator iterator() {
		return names.values().iterator();
	}

	/**
	 * Using the given evaluator attributes, choose and use an appropriate index
	 * to obtain a FeatureSet. If no index can be applied, then this method
	 * returns null
	 *
	 * @param evaluator
	 * @return FeatureSet or null if could not find any appropriate index.
	 * @throws FeatureIndexException
	 *
	 */
	public FeatureSet getFeatureSet(Evaluator evaluator)
			throws FeatureIndexException {

		class ApplyIndex {
			DefaultFeatureIndex index;
			EvaluatorFieldValue[] data;

			ApplyIndex(DefaultFeatureIndex index, EvaluatorFieldValue[] data) {
				this.index = index;
				this.data = data;
			}

			/**
			 * Checks whether the index supports the evaluator request
			 *
			 * @return
			 */
			boolean isSupported() {
				switch (data[0].getType()) {
				case EvaluatorFieldValue.MATCH:
					return index.getFeatureIndexProvider().isMatchSupported();
				case EvaluatorFieldValue.NEAREST:
					return index.getFeatureIndexProvider().isNearestSupported();
				case EvaluatorFieldValue.RANGE:
					return index.getFeatureIndexProvider().isRangeSupported();
				default:
					return false;
				}
			}

			/**
			 * Applies the index using the evaluator fields
			 *
			 * @return FeatureSet with the result
			 * @throws FeatureIndexException
			 */
			IndexFeatureSet apply() throws FeatureIndexException {

				EvaluatorFieldValueRange rangeField;
				EvaluatorFieldValueNearest nearestField;
				// Trick: we know DefaultIndexProvider returns an IndexFeatureSet,
				// which implements both FeatureSetProvider and FeatureSet.
				switch (data[0].getType()) {
				case EvaluatorFieldValue.MATCH:
					return (IndexFeatureSet) index.getMatchFeatureSet(
							((EvaluatorFieldValueMatch) data[0])
									.getValue());
				case EvaluatorFieldValue.RANGE:
					rangeField = (EvaluatorFieldValueRange) data[0];
					return (IndexFeatureSet) index.getRangeFeatureSet(
							rangeField.getValue1(), rangeField.getValue2());
				case EvaluatorFieldValue.NEAREST:
					nearestField = (EvaluatorFieldValueNearest) data[0];
					if ((nearestField.getTolerance() == null)
							|| (!isSupported())) {
						return (IndexFeatureSet) index.getNearestFeatureSet(
								nearestField.getCount(), nearestField
										.getValue());
					} else {
						return (IndexFeatureSet) index.getNearestFeatureSet(
								nearestField.getCount(), nearestField
										.getValue(), nearestField
										.getTolerance());
					}
				}
				return null;
			}
		}

		// Select applicable indexes
		List applyIndexes = new ArrayList();
		Iterator indexes = this.iterator();
		while (indexes.hasNext()) {
			DefaultFeatureIndex index = (DefaultFeatureIndex) indexes.next();
			String[] attrs = (String[]) index.getAttributeNames().toArray(
					new String[0]);
			for (int i = 0; i < attrs.length; i++) {
				String attrname = attrs[i];
				EvaluatorFieldValue[] values = null;
				if (evaluator.getFieldsInfo() != null) {
					values = evaluator.getFieldsInfo().getFieldValues(attrname);
				}
				if (values != null) {
					applyIndexes.add(new ApplyIndex(index, values));
					break;
				}
			}
		}

		// If there's not any applicable index, return null
		if (applyIndexes.size() == 0) {
			return null;
		}

		// Lookup an index with support for the requested function
		Iterator it = applyIndexes.iterator();
		ApplyIndex index = (ApplyIndex) it.next();
		while (it.hasNext() && (!index.isSupported())) {
			index = (ApplyIndex) it.next();
		}

		// If there is not any any index supporting the function, use the
		// first one
		if (!index.isSupported()) {
			logger
					.info("No index support for the evaluator values. Using default index.");
			index = (ApplyIndex) applyIndexes.get(0);
		}

		// Apply index
		return index.apply();

	}
}

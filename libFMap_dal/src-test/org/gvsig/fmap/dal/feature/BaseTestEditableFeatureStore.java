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
package org.gvsig.fmap.dal.feature;

import java.util.Iterator;

import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.exception.ConcurrentDataModificationException;
import org.gvsig.fmap.dal.feature.testmulithread.DeleteFirstAndLastFeature;
import org.gvsig.fmap.dal.feature.testmulithread.DeleteLastFeature;
import org.gvsig.fmap.dal.feature.testmulithread.InsertFeature;
import org.gvsig.fmap.dal.feature.testmulithread.StoreTask;
import org.gvsig.fmap.dal.feature.testmulithread.UpdateFeature;
import org.gvsig.tools.evaluator.AbstractEvaluator;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;
import org.gvsig.tools.evaluator.EvaluatorFieldValue;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;

/**
 * @author jmvivo
 *
 */
public abstract class BaseTestEditableFeatureStore extends BaseTestFeatureStore {

	public abstract NewFeatureStoreParameters getDefaultNewDataStoreParameters()
			throws Exception;

	public abstract boolean resourcesNotifyChanges();

	//=================================================
	//=================================================


	public void fillPrimaryKeyInserFeature(EditableFeature feature) {
		return;
	}

	protected Evaluator getEvaluatorToLower(String attibuteName) {
		return new ToLower(attibuteName);

	}

	protected Evaluator getEvaluatorToUpper(String attibuteName) {
		return new ToUpper(attibuteName);
	}

	public class ToLower extends AbstractEvaluator {
		private String attributeName;

		public ToLower(String attrName) {
			this.attributeName = attrName;
			this.getFieldsInfo().addFieldValue(this.attributeName);
		}

		public Object evaluate(EvaluatorData data) throws EvaluatorException {
			String value = (String) data.getDataValue(this.attributeName);
			if (value == null) {
				return null;
			}
			return value.toLowerCase();
		}

		public String getCQL() {
			return null;
		}

		public String getDescription() {
			return null;
		}

		public String getName() {
			return null;
		}

	}

	public class ToUpper extends AbstractEvaluator {
		private String attributeName;

		public ToUpper(String attrName) {
			this.attributeName = attrName;
			this.getFieldsInfo().addFieldValue(this.attributeName);
		}

		public Object evaluate(EvaluatorData data) throws EvaluatorException {
			String value = (String) data.getDataValue(this.attributeName);
			if (value == null) {
				return null;
			}
			return value.toUpperCase();
		}

		public String getCQL() {
			return null;
		}

		public String getDescription() {
			return null;
		}

		public EvaluatorFieldValue[] getFieldValues(String name) {
			return null;
		}

		public String getName() {
			return null;
		}

	}

	protected FeatureStore getStoreCopy(FeatureStore source,
			NewFeatureStoreParameters targetParams) throws DataException,
			ValidateDataParametersException {
		DataServerExplorer explorer = source.getExplorer();
		source.export(explorer, targetParams);
		FeatureStore result = (FeatureStore) dataManager
				.createStore(targetParams);

		return result;
	}

	protected void clearCopy(FeatureStore source,
			NewFeatureStoreParameters targetParams) throws Exception {
		DataServerExplorer explorer = source.getExplorer();
		explorer.remove(targetParams);

	}

	protected boolean compareFeatures(Feature f1, Feature f2,
			String[] attrsNames, UpdateFeature updated, long index,
			long lastIndex) throws DataException, EvaluatorException {
		FeatureAttributeDescriptor attr1;
		FeatureAttributeDescriptor attr2;
		EditableFeature f2e = f2.getEditable();
		updated.applyUpdateList(f2e, index, lastIndex);

		Object v1, v2;
		for (int i = 0; i < attrsNames.length; i++) {
			attr1 = f1.getType().getAttributeDescriptor(attrsNames[i]);
			attr2 = f2e.getType().getAttributeDescriptor(attrsNames[i]);
			if (attr1 != attr2) {
				if (!compareAttributes(attr1, attr1)) {
					return false;
				}
			}
			v1 = f1.get(attr1.getName());
			v2 = f2.get(attr2.getName());
			if (!compareFeatureValue(v1, v2, attr1)) {
				return false;
			}
		}

		return true;
	}

	protected boolean compareFeatures(Feature f1, Feature f2,
			UpdateFeature updated, long index, long lastIndex)
			throws DataException, EvaluatorException {
		if (!compareTypes(f1.getType(), f2.getType())) {
			System.out.println("compareFeatures() type !=");
			return false;
		}
		Iterator iter = f1.getType().iterator();
		FeatureAttributeDescriptor attr;
		EditableFeature f1e = f1.getEditable();
		updated.applyUpdateList(f1e, index, lastIndex);
		Object v1, v2;
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			v1 = f1e.get(attr.getName());
			v2 = f2.get(attr.getName());
			if (!compareFeatureValue(v1, v2, attr)) {
				return false;
			}
		}


		return true;

	}


	//---------------




	//=================================================
	//=================================================




	public void testExport() throws Exception {
		DataStoreParameters parameters = null;

		parameters = getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager.createStore(parameters);

		NewFeatureStoreParameters newParams = this
				.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			// Dp nothing
		}

		FeatureStore result = this.getStoreCopy(store, newParams);

		fullStoreIteratorTest(result);

		FeatureSet set;
		FeatureSet originalSet;
		if (result.getDefaultFeatureType().getPrimaryKey() != null
				&& result.getDefaultFeatureType().getPrimaryKey().length > 0) {
			FeatureQuery queryR = result.createFeatureQuery();
			FeatureQuery queryO = store.createFeatureQuery();
			FeatureAttributeDescriptor[] pk = result.getDefaultFeatureType()
					.getPrimaryKey();
			for (int i = 0; i < pk.length; i++) {
				queryO.getOrder().add(pk[i].getName(), true);
				queryR.getOrder().add(pk[i].getName(), true);
			}


			set = result.getFeatureSet(queryR);
			originalSet = store.getFeatureSet(queryO);


		} else {
			set = result.getFeatureSet();
			originalSet = store.getFeatureSet();
		}
		assertEquals(set.getSize(), originalSet.getSize());

		DisposableIterator originalIter = originalSet.iterator();
		DisposableIterator iter = set.iterator();
		assertTrue(compareFeatureIterators(originalIter, iter));
		originalIter.dispose();
		iter.dispose();

		if (store.getEnvelope() != result.getEnvelope()) {
			if (store.getEnvelope() != null) {
				assertTrue(store.getEnvelope().equals(result.getEnvelope()));
			} else {
				fail("Envelope: src=" + store.getEnvelope() + " traget="
						+ store.getEnvelope());
			}
		}

		set.dispose();
		originalSet.dispose();

		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();

	}

	public void testRemove() throws Exception {
		DataStoreParameters parameters = null;

		parameters = getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager.createStore(parameters);

		NewFeatureStoreParameters newParams = this
				.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			// Dp nothing
		}

		FeatureStore result = this.getStoreCopy(store, newParams);

		result.edit(FeatureStore.MODE_FULLEDIT);

		FeatureSet set = result.getFeatureSet(getDefaultQuery(result));
		FeatureSet originalSet = store.getFeatureSet(getDefaultQuery(store));
		assertEquals(set.getSize(), originalSet.getSize());

		DisposableIterator originalIter = originalSet.iterator();
		DisposableIterator iter = set.iterator();
		int i = 0;
		while (iter.hasNext()) {
			assertTrue("" + i, compareFeatures((Feature) originalIter.next(),
					(Feature) iter.next()));
			i++;
		}

		iter.remove();


		assertEquals(originalSet.getSize() - 1, set.getSize());

		iter.dispose();
		originalIter.dispose();


		originalIter = originalSet.iterator();
		iter = set.iterator();
		i = 0;
		while (iter.hasNext()) {
			assertTrue("" + i, compareFeatures((Feature) originalIter.next(),
					(Feature) iter.next()));
			i++;
		}

		iter.remove();

		assertEquals(originalSet.getSize() - 2, set.getSize());

		iter.dispose();
		originalIter.dispose();

		set.dispose();

		result.finishEditing();

		set = result.getFeatureSet();
		assertEquals(originalSet.getSize() - 2, set.getSize());

		originalIter = originalSet.iterator();
		iter = set.iterator();
		i = 0;
		while (iter.hasNext()) {
			assertTrue("" + i, compareFeatures((Feature) originalIter.next(),
					(Feature) iter.next()));
			i++;
		}

		iter.dispose();
		originalIter.dispose();


		set.dispose();
		originalSet.dispose();

		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();


	}

	public void testInsert() throws Exception {
		DataStoreParameters parameters = null;

		parameters = getDefaultDataStoreParameters();


		FeatureStore store = (FeatureStore) dataManager
		.createStore(parameters);

		NewFeatureStoreParameters newParams = this
		.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			//Do nothing
		}

		FeatureStore result = this.getStoreCopy(store, newParams);

		result.edit(FeatureStore.MODE_FULLEDIT);

		FeatureSet set = result.getFeatureSet(getDefaultQuery(result));
		FeatureSet originalSet = store.getFeatureSet(getDefaultQuery(store));
		assertEquals(set.getSize(), originalSet.getSize());

		EditableFeature newFeature = result.createNewFeature(true);
		fillPrimaryKeyInserFeature(newFeature);
		set.insert(newFeature);
		DisposableIterator originalIter = originalSet.iterator();
		DisposableIterator iter = set.iterator();
		int i = 0;
		while (originalIter.hasNext()) {
			assertTrue("" + i, compareFeatures((Feature) originalIter
					.next(), (Feature) iter.next()));
			i++;
		}
		assertTrue(iter.hasNext());
		assertNotNull(iter.next());
		assertFalse(iter.hasNext());

		assertEquals(originalSet.getSize() + 1, set.getSize());

		iter.dispose();
		originalIter.dispose();

		originalIter = originalSet.iterator();
		iter = set.iterator();
		i = 0;
		while (originalIter.hasNext()) {
			assertTrue("" + i, compareFeatures((Feature) originalIter
					.next(), (Feature) iter.next()));
			i++;
		}
		assertTrue(iter.hasNext());
		assertNotNull(iter.next());

		newFeature = result.createNewFeature(true);
		fillPrimaryKeyInserFeature(newFeature);
		set.insert(newFeature);

		assertEquals(originalSet.getSize() + 2, set.getSize());

		iter.dispose();
		originalIter.dispose();

		set.dispose();

		result.finishEditing();

		set = result.getFeatureSet();
		assertEquals(originalSet.getSize() + 2, set.getSize());

		originalIter = originalSet.iterator();
		iter = set.iterator();
		i = 0;
		while (originalIter.hasNext()) {
			assertTrue("" + i, compareFeatures((Feature) originalIter
					.next(), (Feature) iter.next()));
			i++;
		}

		assertNotNull(iter.next());
		assertNotNull(iter.next());
		assertFalse(iter.hasNext());

		iter.dispose();
		originalIter.dispose();

		set.dispose();
		originalSet.dispose();

		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();


	}



	public void testConcurrentRemove() throws Exception {
		DataStoreParameters parameters = null;

		parameters = getDefaultDataStoreParameters();


		FeatureStore store = (FeatureStore) dataManager.createStore(parameters);

		NewFeatureStoreParameters newParams = this
		.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			// Dp nothing
		}

		FeatureStore result = this.getStoreCopy(store, newParams);

		result.edit(FeatureStore.MODE_FULLEDIT);

		DeleteFirstAndLastFeature deleteFeature = new DeleteFirstAndLastFeature(
				"1", result, StoreTask.TIME_TO_WAIT_NO_WAIT);

		FeatureSet set = result.getFeatureSet();

		DisposableIterator iter = set.iterator();
		assertTrue(iter.hasNext());
		assertNotNull(iter.next());

		deleteFeature.start();

		while (deleteFeature.getCurrentStatus() <= StoreTask.STATUS_RUNING) {
			Thread.yield();
			Thread.sleep(100);
			if (deleteFeature.isOutOfDate()) {
				break;
			}
		}

		assertEquals(deleteFeature.getCurrentStatus(),
				StoreTask.STATUS_FINISHED_OK);

		Exception ex = null;
		try {
			iter.next();
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
		assertEquals(ConcurrentDataModificationException.class, ex.getClass());

		ex = null;
		try {
			set.getSize();
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
		assertEquals(ConcurrentDataModificationException.class, ex.getClass());

		iter.dispose();
		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();

	}

	public void testConcurrentInsert() throws Exception{
		DataStoreParameters parameters = null;

		parameters = getDefaultDataStoreParameters();


		FeatureStore store = (FeatureStore) dataManager.createStore(parameters);

		NewFeatureStoreParameters newParams = this
		.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			// Dp nothing
		}

		FeatureStore result = this.getStoreCopy(store, newParams);

		result.edit(FeatureStore.MODE_FULLEDIT);

		InsertFeature insertFeature = new InsertFeature("1", result,
				StoreTask.TIME_TO_WAIT_NO_WAIT, this);

		FeatureSet set = result.getFeatureSet();

		DisposableIterator iter = set.iterator();
		assertTrue(iter.hasNext());
		assertNotNull(iter.next());

		insertFeature.start();

		while (insertFeature.getCurrentStatus() <= StoreTask.STATUS_RUNING) {
			Thread.yield();
			Thread.sleep(100);
			if (insertFeature.isOutOfDate()) {
				break;
			}
		}

		assertEquals(StoreTask.STATUS_FINISHED_OK, insertFeature
				.getCurrentStatus());

		Exception ex = null;
		try {
			iter.next();
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
		assertEquals(ConcurrentDataModificationException.class, ex.getClass());

		ex = null;
		try {
			set.getSize();
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
		assertEquals(ConcurrentDataModificationException.class, ex.getClass());

		iter.dispose();
		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();
	}

	public void testConcurrentUpdate() throws Exception {
		DataStoreParameters parameters = null;

		parameters = getDefaultDataStoreParameters();


		FeatureStore store = (FeatureStore) dataManager.createStore(parameters);

		NewFeatureStoreParameters newParams = this
		.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			// Dp nothing
		}

		FeatureStore result = this.getStoreCopy(store, newParams);


		result.edit(FeatureStore.MODE_FULLEDIT);

		UpdateFeature updateFeature = new UpdateFeature("1", result,
				StoreTask.TIME_TO_WAIT_NO_WAIT);

		FeatureType fType = result.getDefaultFeatureType();
		FeatureAttributeDescriptor attr;
		Iterator fTypeIter = fType.iterator();
		String attrName = null;
		while (fTypeIter.hasNext()){
			attr = (FeatureAttributeDescriptor) fTypeIter.next();
			if (attr.getDataType() == DataTypes.STRING && !attr.isReadOnly() && !attr.isPrimaryKey() && !attr.isAutomatic()){
				attrName= attr.getName();
			}
		}
		if (attrName == null) {
			fail("This test needs an normal attribute String (no Pk, no ReadOnly & no Auto)");
			return;
		}


		updateFeature.addUpdate(UpdateFeature.UPDATE_ALL_FEATURES, attrName,
				"XXX");

		FeatureSet set = result.getFeatureSet();

		DisposableIterator iter = set.iterator();
		assertTrue(iter.hasNext());
		assertNotNull(iter.next());

		updateFeature.start();

		while (updateFeature.getCurrentStatus() <= StoreTask.STATUS_RUNING) {
			Thread.yield();
			Thread.sleep(100);
			if (updateFeature.isOutOfDate()) {
				break;
			}
		}

		assertEquals(StoreTask.STATUS_FINISHED_OK, updateFeature
				.getCurrentStatus());

		Exception ex = null;
		try {
			iter.next();
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
		assertEquals(ConcurrentDataModificationException.class, ex.getClass());

		ex = null;
		try {
			set.getSize();
		} catch (Exception e) {
			ex = e;
		}
		assertNotNull(ex);
		assertEquals(ConcurrentDataModificationException.class, ex.getClass());

		iter.dispose();
		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();



	}

	public void testSequencedRemove() throws Exception {
		DataStoreParameters parameters = null;

		int ntimes = 3;

		parameters = getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager.createStore(parameters);

		NewFeatureStoreParameters newParams = this
		.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			// Dp nothing
		}

		FeatureStore result = this.getStoreCopy(store, newParams);

		result.edit(FeatureStore.MODE_FULLEDIT);

		DeleteLastFeature[] deletesFeature = new DeleteLastFeature[ntimes];
		int i;
		for (i = 0; i < deletesFeature.length; i++) {
			deletesFeature[i] = new DeleteLastFeature("DeleteLastFeature:" + i,
					result, 300 * i);
		}

		for (i = 0; i < deletesFeature.length; i++) {
			deletesFeature[i].start();
		}

		try {
			for (int x = 0; x < (deletesFeature.length + 2); x++) {
				Thread.yield();
				Thread.sleep(500);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			result.dispose();
			fail();
			return;
		}

		boolean testAll = false;
		boolean restart;
		DeleteLastFeature item;
		while (!testAll) {
			restart = false;
			for (i = 0; i < deletesFeature.length; i++) {
				item = deletesFeature[i];
				if (item.getCurrentStatus() <= StoreTask.STATUS_RUNING) {
					if (item.isOutOfDate()) {
						result.dispose();
						fail("OutOfDate: " + i);
						return;
					}
					try {
						Thread.yield();
						Thread.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
						result.dispose();
						fail();
						return;
					}
					restart = true;
					break;
				} else if (item.getCurrentStatus() == StoreTask.STATUS_ERROR) {
					item.getException().printStackTrace();
					result.dispose();
					fail("ERROR: " + i);
					return;
				} else if (item.getCurrentStatus() != StoreTask.STATUS_FINISHED_OK) {
					item.getException().printStackTrace();
					result.dispose();
					fail("Data ERROR: " + i);
					return;
				}
			}
			if (restart) {
				continue;
			}
			testAll = true;
		}

		FeatureSet set = result.getFeatureSet(getDefaultQuery(result));
		FeatureSet originalSet = store.getFeatureSet(getDefaultQuery(store));
		assertEquals(originalSet.getSize() - deletesFeature.length, set
				.getSize());

		DisposableIterator originalIter = originalSet.iterator();
		DisposableIterator iter = set.iterator();

		i = 0;
		while (iter.hasNext()) {
			assertTrue("" + i, compareFeatures((Feature) originalIter.next(),
					(Feature) iter.next()));
			i++;
		}

		originalIter.dispose();
		iter.dispose();

		set.dispose();

		result.finishEditing();

		set = result.getFeatureSet(getDefaultQuery(result));
		assertEquals(originalSet.getSize() - deletesFeature.length, set
				.getSize());

		originalIter = originalSet.iterator();
		iter = set.iterator();
		i = 0;
		while (iter.hasNext()) {
			assertTrue("" + i, compareFeatures((Feature) originalIter.next(),
					(Feature) iter.next()));
			i++;
		}


		originalIter.dispose();
		iter.dispose();

		set.dispose();
		originalSet.dispose();

		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();
	}

	public void testSequencedInsert() throws Exception {
		DataStoreParameters parameters = null;

		int ntimes = 3;

		parameters = getDefaultDataStoreParameters();


		FeatureStore store = (FeatureStore) dataManager.createStore(parameters);

		NewFeatureStoreParameters newParams = this
		.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			// Dp nothing
		}

		FeatureStore result = this.getStoreCopy(store, newParams);

		result.edit(FeatureStore.MODE_FULLEDIT);

		InsertFeature[] insertsFeature = new InsertFeature[ntimes];
		int i;
		for (i = 0; i < insertsFeature.length; i++) {
			insertsFeature[i] = new InsertFeature("InsertFeature:" + i, result,
					300 * i, this);
		}

		for (i = 0; i < insertsFeature.length; i++) {
			insertsFeature[i].start();
		}

		try {

			for (int x = 0; x < (insertsFeature.length + 2); x++) {
				Thread.yield();
				Thread.sleep(500);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			result.dispose();
			fail();
			return;
		}

		boolean testAll = false;
		boolean restart;
		InsertFeature item;
		while (!testAll) {
			restart = false;
			for (i = 0; i < insertsFeature.length; i++) {
				item = insertsFeature[i];
				if (item.getCurrentStatus() <= StoreTask.STATUS_RUNING) {
					if (item.isOutOfDate()) {
						result.dispose();
						fail("OutOfDate: " + i);
						return;
					}
					try {
						Thread.yield();
						Thread.sleep(400);
					} catch (InterruptedException e) {
						e.printStackTrace();
						result.dispose();
						fail();
						return;
					}
					restart = true;
					break;
				} else if (item.getCurrentStatus() == StoreTask.STATUS_ERROR) {
					item.getException().printStackTrace();
					result.dispose();
					fail("ERROR: " + i);
					return;
				} else if (item.getCurrentStatus() == StoreTask.STATUS_FINISHED_NO_OK) {
					result.dispose();
					fail("No OK: " + i);
					return;
				} else if (item.getCurrentStatus() != StoreTask.STATUS_FINISHED_OK) {
					item.getException().printStackTrace();
					result.dispose();
					fail("Data ERROR: " + i);
					return;

				}
			}
			if (restart) {
				continue;
			}
			testAll = true;
		}

		FeatureSet set = result.getFeatureSet(getDefaultQuery(result));
		FeatureSet originalSet = store.getFeatureSet(getDefaultQuery(store));
		assertEquals(originalSet.getSize() + insertsFeature.length, set
				.getSize());

		DisposableIterator originalIter = originalSet.iterator();
		DisposableIterator iter = set.iterator();

		i = 0;
		while (originalIter.hasNext()) {
			assertTrue("" + i, compareFeatures((Feature) originalIter.next(),
					(Feature) iter.next()));
			i++;
		}
		for (; i < insertsFeature.length; i++) {
			iter.next();
		}


		originalIter.dispose();
		iter.dispose();

		set.dispose();

		result.finishEditing();

		set = result.getFeatureSet();
		assertEquals(originalSet.getSize() + insertsFeature.length, set
				.getSize());

		originalIter = originalSet.iterator();
		iter = set.iterator();
		i = 0;
		while (originalIter.hasNext()) {
			assertTrue("" + i, compareFeatures((Feature) originalIter.next(),
					(Feature) iter.next()));
			i++;
		}
		for (; i < insertsFeature.length; i++) {
			iter.next();
		}

		set.dispose();
		originalSet.dispose();

		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();

	}


	public void testUpdate() throws Exception {
		DataStoreParameters parameters = null;

		parameters = getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager.createStore(parameters);

		NewFeatureStoreParameters newParams = this
		.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			// Dp nothing
		}

		FeatureStore result = this.getStoreCopy(store, newParams);

		result.edit(FeatureStore.MODE_FULLEDIT);

		FeatureSet set = result.getFeatureSet();
		long size = set.getSize();

		set.dispose();

		UpdateFeature updater = new UpdateFeature("1", result,
				UpdateFeature.TIME_TO_WAIT_NO_WAIT);
		FeatureAttributeDescriptor attr = getFirstAttributeOfType(result
				.getDefaultFeatureType(), DataTypes.STRING);
		assertNotNull("No String attributes found", attr);

		updater.addUpdate(UpdateFeature.UPDATE_ALL_FEATURES, attr.getName(),
				getEvaluatorToLower(attr.getName()));

		updater.run();
		while (updater.getCurrentStatus() < StoreTask.STATUS_FINISHED_OK
				&& !updater.isOutOfDate()) {
			Thread.yield();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail();
			}
		}
		set = result.getFeatureSet(getDefaultQuery(result));
		DisposableIterator iter = set.iterator();

		printFeatureTypeColNames(set.getDefaultFeatureType(), 15);
		Feature f;
		while (iter.hasNext()) {
			f = (Feature) iter.next();
			this.printFeature(f, false, 15);
		}

		iter.dispose();

		FeatureSet originalSet = store.getFeatureSet(getDefaultQuery(store));

		DisposableIterator originalIter = originalSet.iterator();

		iter = set.iterator();

		long i = 0;
		while (originalIter.hasNext()) {
			try {
				assertTrue("" + i, compareFeatures((Feature) originalIter
						.next(), (Feature) iter.next(), updater, i, size));
			} catch (EvaluatorException e) {
				e.printStackTrace();
				fail();
			}
			i++;
		}
		iter.dispose();
		originalIter.dispose();


		set.dispose();

		result.finishEditing();

		set = result.getFeatureSet();
		assertEquals(originalSet.getSize(), set.getSize());

		originalIter = originalSet.iterator();
		iter = set.iterator();
		i = 0;
		while (originalIter.hasNext()) {
			try {
				assertTrue("" + i, compareFeatures((Feature) originalIter
						.next(), (Feature) iter.next(), updater, i, size));
			} catch (EvaluatorException e) {
				e.printStackTrace();
				fail();
			}
			i++;
		}


		iter.dispose();
		originalIter.dispose();
		set.dispose();


		updater = new UpdateFeature("1", result,
				UpdateFeature.TIME_TO_WAIT_NO_WAIT);

		updater.addUpdate(UpdateFeature.UPDATE_ALL_FEATURES, attr.getName(),
				getEvaluatorToLower(attr.getName()));

		updater.run();
		while (updater.getCurrentStatus() < StoreTask.STATUS_FINISHED_OK
				&& !updater.isOutOfDate()) {
			Thread.yield();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail();
			}
		}
		set = result.getFeatureSet();

		iter = set.iterator();
		originalIter = originalSet.iterator();
		i = 0;
		while (originalIter.hasNext()) {
			try {
				assertTrue("" + i, compareFeatures((Feature) originalIter
						.next(), (Feature) iter.next(), updater, i, size));
			} catch (EvaluatorException e) {
				e.printStackTrace();
				fail();
			}
			i++;
		}

		iter.dispose();
		originalIter.dispose();
		set.dispose();





		originalSet.dispose();

		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();

	}


	public void testResourceChangeNotification() throws Exception{
		if (!resourcesNotifyChanges()) {
			return;
		}

		DataStoreParameters parameters = null;

		parameters = getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager.createStore(parameters);

		NewFeatureStoreParameters newParams = this
		.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			// Dp nothing
		}

		FeatureStore result = this.getStoreCopy(store, newParams);

		FeatureStore result2 = (FeatureStore) dataManager
		.createStore(newParams);

		FeatureAttributeDescriptor attr = getFirstAttributeOfType(result
				.getDefaultFeatureType(), DataTypes.STRING);
		assertNotNull("No String attributes found", attr);


		UpdateFeature updater = new UpdateFeature("1", result,
				UpdateFeature.TIME_TO_WAIT_NO_WAIT);

		updater.addUpdate(UpdateFeature.UPDATE_LAST_FEATURE, attr.getName(),
				getEvaluatorToLower(attr.getName()));

		StoreObserverForNotify observer = new StoreObserverForNotify(result2,
				FeatureStoreNotification.RESOURCE_CHANGED);
		result2.addObserver(observer);

		result.edit();
		updater.run();
		while (updater.getCurrentStatus() < StoreTask.STATUS_FINISHED_OK
				&& !updater.isOutOfDate()) {
			Thread.yield();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail();
			}
		}

		result.finishEditing();

		assertTrue(observer.notified());

		result2.refresh();

		FeatureSet set = result.getFeatureSet();
		FeatureSet set2 = result2.getFeatureSet();

		DisposableIterator iter1 = set.iterator();
		DisposableIterator iter2 = set2.iterator();
		assertTrue(this.compareFeatureIterators(iter1, iter2));
		iter1.dispose();
		iter2.dispose();

		set2.dispose();
		set.dispose();

		// TODO checks FeatureType change

		result2.dispose();
		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();
	}

	public abstract class StoreObserver implements Observer{

		public DataStore store = null;

	}

	public class StoreObserverForNotify implements Observer{

		public DataStore store = null;

		public String notifyType = null;

		private boolean notifyRecived = false;

		public StoreObserverForNotify(DataStore store,String notifyType){
			this.store = store;
			this.notifyType = notifyType;
			this.notifyRecived = false;
		}

		public boolean notified(){
			return notifyRecived;
		}

		public void update(Observable observable, Object notification) {
			if (!(observable == this.store)){
				return;
			}
			if (!(notification instanceof FeatureStoreNotification)){
				return;
			}
			FeatureStoreNotification fsNotification = (FeatureStoreNotification) notification;

			if (fsNotification.getType().equals(this.notifyType)){
				notifyRecived = true;
			}
		}

	}

	public void testUpdateFeatureType_AddField() throws Exception {
		DataStoreParameters parameters = null;

		parameters = getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager
		.createStore(parameters);

		NewFeatureStoreParameters newParams = this
		.getDefaultNewDataStoreParameters();
		try {
			this.clearCopy(store, newParams);
		} catch (RemoveException e) {
			// Dp nothing
		}

		int pkAttributesCount = 0;

		FeatureStore result = this.getStoreCopy(store, newParams);

		result.edit();


		String[] orgAttNames = new String[result.getDefaultFeatureType()
		                                  .size()];

		Iterator iter = result.getDefaultFeatureType().iterator();
		int i = 0;

		while (iter.hasNext()) {
			FeatureAttributeDescriptor attr = ((FeatureAttributeDescriptor) iter
					.next());
			orgAttNames[i] = attr.getName();
			i++;
			if (attr.isPrimaryKey()) {
				pkAttributesCount++;
			}
		}

		EditableFeatureType edFtype = result
		.getDefaultFeatureType().getEditable();

		edFtype.add("__ATTR1__", DataTypes.STRING).setSize(10)
		.setDefaultValue("HOLA");

		result.update(edFtype);

		assertEquals(store.getDefaultFeatureType().size() + 1, result
				.getDefaultFeatureType().size());

		testIterationFastAndStandart(result);

		FeatureSet orgSet = store.getFeatureSet(getDefaultQuery(store));
		FeatureSet curSet = result.getFeatureSet(getDefaultQuery(result));

		assertEquals(orgSet.getDefaultFeatureType().size() + 1, curSet
				.getDefaultFeatureType().size());

		DisposableIterator orgIter = orgSet.iterator();
		DisposableIterator curIter = curSet.iterator();

		assertTrue(compareFeatureIterators(orgIter, curIter, orgAttNames));

		orgIter.dispose();
		curIter.dispose();

		orgIter = orgSet.fastIterator();
		curIter = curSet.fastIterator();

		assertTrue(compareFeatureIterators(orgIter, curIter, orgAttNames));
		orgIter.dispose();
		curIter.dispose();

		orgIter = orgSet.iterator();
		curIter = curSet.fastIterator();

		assertTrue(compareFeatureIterators(orgIter, curIter, orgAttNames));
		orgIter.dispose();
		curIter.dispose();

		orgIter = orgSet.fastIterator();
		curIter = curSet.iterator();

		assertTrue(compareFeatureIterators(orgIter, curIter, orgAttNames));
		orgIter.dispose();
		curIter.dispose();

		curIter = curSet.iterator();
		Feature feature;
		while (curIter.hasNext()) {
			feature = (Feature) curIter.next();
			assertEquals("HOLA", feature.get("__ATTR1__"));
		}
		curIter.dispose();
		curSet.dispose();

		FeatureQuery query = this.getDefaultQuery(result);
		query.setAttributeNames(new String[] { "__ATTR1__" });
		curSet = result.getFeatureSet(query);

		assertEquals(1 + pkAttributesCount, curSet.getDefaultFeatureType()
				.size());

		assertEquals(orgSet.getSize(), curSet.getSize());
		curIter = curSet.iterator();
		while (curIter.hasNext()) {
			feature = (Feature) curIter.next();
			assertEquals("HOLA", feature.get("__ATTR1__"));
		}
		curIter.dispose();
		curSet.dispose();

		result.finishEditing();

		testIterationFastAndStandart(result);

		assertEquals(store.getDefaultFeatureType().size() + 1, result
				.getDefaultFeatureType().size());


		orgSet = store.getFeatureSet(getDefaultQuery(store));
		curSet = result.getFeatureSet(getDefaultQuery(result));

		assertEquals(orgSet.getDefaultFeatureType().size() + 1, curSet
				.getDefaultFeatureType().size());

		orgIter = orgSet.iterator();
		curIter = curSet.iterator();

		assertTrue(compareFeatureIterators(orgIter, curIter, orgAttNames));
		orgIter.dispose();
		curIter.dispose();

		orgIter = orgSet.fastIterator();
		curIter = curSet.fastIterator();

		assertTrue(compareFeatureIterators(orgIter, curIter, orgAttNames));
		orgIter.dispose();
		curIter.dispose();

		orgIter = orgSet.iterator();
		curIter = curSet.fastIterator();

		assertTrue(compareFeatureIterators(orgIter, curIter, orgAttNames));
		orgIter.dispose();
		curIter.dispose();

		orgIter = orgSet.fastIterator();
		curIter = curSet.iterator();

		assertTrue(compareFeatureIterators(orgIter, curIter, orgAttNames));
		orgIter.dispose();
		curIter.dispose();

		curIter = curSet.iterator();
		while (curIter.hasNext()) {
			feature = (Feature) curIter.next();
			assertEquals("HOLA", feature.get("__ATTR1__"));
		}
		curIter.dispose();
		curSet.dispose();

		query = this.getDefaultQuery(result);
		query.setAttributeNames(new String[] { "__ATTR1__" });
		curSet = result.getFeatureSet(query);

		assertEquals(1 + pkAttributesCount, curSet.getDefaultFeatureType()
				.size());

		assertEquals(orgSet.getSize(), curSet.getSize());
		curIter = curSet.iterator();
		while (curIter.hasNext()) {
			feature = (Feature) curIter.next();
			assertEquals("HOLA", feature.get("__ATTR1__"));
		}
		curIter.dispose();
		curSet.dispose();

		orgSet.dispose();
		result.dispose();
		this.clearCopy(store, newParams);
		store.dispose();


	}

	public void testResourcesLocks() throws Exception {


	}

}

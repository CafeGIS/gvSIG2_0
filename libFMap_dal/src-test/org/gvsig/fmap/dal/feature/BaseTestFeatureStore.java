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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataEvaluatorRuntimeException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureQueryOrder.FeatureQueryOrderMember;
import org.gvsig.fmap.dal.feature.testmulithread.StoreTask;
import org.gvsig.fmap.dal.resource.ResourceManager;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;
import org.gvsig.tools.evaluator.EvaluatorFieldsInfo;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmvivo
 *
 */
public abstract class BaseTestFeatureStore extends TestCase {

	private static Logger logger = null;

	protected static DataManager dataManager = null;
	protected boolean baseTestInitialized = false;
	private static Random rnd;

	public Logger getLogger() {
		if (logger == null) {
			logger = LoggerFactory.getLogger(this.getClass());
		}
		return logger;
	}

	public abstract boolean usesResources();

	public abstract boolean hasExplorer();

	public FeatureQuery getDefaultQuery(FeatureStore store)
			throws DataException {
		FeatureQuery query = store.createFeatureQuery();
		FeatureAttributeDescriptor[] key = store.getDefaultFeatureType()
				.getPrimaryKey();
		for (int i = 0; i < key.length; i++) {
			query.getOrder().add(key[i].getName(), true);
		}

		return query;
	}


	public abstract DataStoreParameters getDefaultDataStoreParameters()
			throws DataException;

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		if (baseTestInitialized) {
			return;
		}
		ToolsLibrary tools = new ToolsLibrary();
		tools.initialize();
		tools.postInitialize();

		DALLibrary lib = new DALLibrary();
		lib.initialize();
		lib.postInitialize();

		dataManager = DALLocator.getDataManager();
		baseTestInitialized = true;

	}



	//=================================================


	public void printFeature(Feature feature, int maxColSize) {
		printFeature(feature, true, maxColSize);
	}


	public void printFeature(Feature feature, boolean showColName,int maxColSize) {
		FeatureType fType = feature.getType();
		if (showColName){
			this.printFeatureTypeColNames(feature.getType(), maxColSize);
		}
		StringBuffer row = new StringBuffer();
		Iterator iter = fType.iterator();
		FeatureAttributeDescriptor attr;

		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			row.append(truncateOrFillString(feature.get(attr.getName()),
					maxColSize + 1, ' '));
		}
		System.out.println(row.toString());
	}

	public String truncateOrFillString(Object str, int max, char fillWith) {
		if (str == null) {
			return truncateOrFillString("{null}", max, fillWith);
		}
		return truncateOrFillString(str.toString(), max, fillWith);
	}

	public String truncateOrFillString(String str, int max, char fillWith) {
		if (str.length() > max) {
			return str.substring(0, max - 1) + " ";
		} else {
			StringBuffer strB = new StringBuffer(str);
			while (strB.length() < max) {
				strB.append(fillWith);
			}
			return strB.toString();
		}

	}

	public void printFeatureTypeColNames(FeatureType fType, int maxColSize) {
		Iterator iter = fType.iterator();
		FeatureAttributeDescriptor attr;
		StringBuffer colNames = new StringBuffer();
		StringBuffer typeNames = new StringBuffer();
		StringBuffer sep = new StringBuffer();
		if (maxColSize < 1){
			maxColSize = 15;
		}
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			colNames.append(truncateOrFillString(attr.getName(), maxColSize + 1, ' '));
			typeNames.append(truncateOrFillString("(" + attr.getDataTypeName() + ")",
					maxColSize + 1, ' '));
			sep.append(truncateOrFillString("", maxColSize, '='));
			sep.append(' ');
		}

		System.out.println("");
		System.out.println("");
		System.out.println(colNames.toString());
		System.out.println(typeNames.toString());
		System.out.println(sep.toString());
	}

	protected FeatureAttributeDescriptor getFirstAttributeOfType(
			FeatureType ftype, int dataType) {
		return getFirstAttributeOfType(ftype, new int[] { dataType });
	}

	protected FeatureAttributeDescriptor getFirstAttributeOfType(
			FeatureType ftype, int[] dataTypes) {
		FeatureAttributeDescriptor attr;
		Iterator iter = ftype.iterator();
		int i;
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			for (i = 0; i < dataTypes.length; i++) {
				if (attr.getDataType() == dataTypes[i]) {
					return attr;
				}
			}
		}
		return null;
	}

	protected boolean compareDynObject(DynObject obj1, DynObject obj2) {
		DynClass dynClass = obj1.getDynClass();
		if (!dynClass.getName().equals(obj2.getDynClass().getName())) {
			return false;
		}

		DynField[] fields = dynClass.getDeclaredDynFields();
		String fieldName;
		Object v1, v2;
		for (int i = 0; i < fields.length; i++) {
			fieldName = fields[i].getName();
			v1 = obj1.getDynValue(fieldName);
			v2 = obj2.getDynValue(fieldName);
			if (v1 == v2) {
				continue;
			} else if (v1 != null) {
				if (!v1.equals(v2)) {
					return false;
				}
			}
		}

		return true;
	}

	protected boolean compareStores(FeatureStore store1, FeatureStore store2)
			throws DataException {
		if (store1.getParameters().getClass() != store2.getParameters()
				.getClass()) {
			return false;
		}
		if (!compareDynObject(store1.getParameters(), store2.getParameters())) {
			return false;
		}

		if (store1.getEnvelope() != store2.getEnvelope()) {
			if (store1.getEnvelope() != null) {
				return store1.getEnvelope().equals(store2.getEnvelope());
			} else {
				return false;
			}
		}

		if (!store1.getName().equals(store2.getName())) {
			return false;
		}
		if (((FeatureSelection) store1.getSelection()).getSize() != ((FeatureSelection) store2
				.getSelection()).getSize()) {
			return false;
		}
		DisposableIterator iter1 = ((FeatureSelection) store1.getSelection())
				.fastIterator();
		DisposableIterator iter2 = ((FeatureSelection) store2.getSelection())
				.fastIterator();
		if (!compareFeatureIterators(iter1, iter2)) {
			return false;
		}
		iter1.dispose();
		iter2.dispose();

		if (store1.getFeatureTypes().size() != store2.getFeatureTypes().size()) {
			return false;
		}
		Iterator iterTypes1 = store1.getFeatureTypes().iterator();
		Iterator iterTypes2 = store2.getFeatureTypes().iterator();
		while (iterTypes1.hasNext()) {
			if (!compareTypes((FeatureType) iterTypes1.next(),
					(FeatureType) iterTypes2
					.next())) {
				return false;
			}
		}
		if (!compareTypes(store1.getDefaultFeatureType(), store2
				.getDefaultFeatureType())) {
			return false;
		}

		if (store1.getLocks() != null) {
			if (store1.getLocks().getLocksCount() != store2.getLocks()
					.getLocksCount()) {
				return false;
			}
			if (!compareFeatureIterators(store1.getLocks().getLocks(), store2
					.getLocks().getLocks())) {
				return false;
			}

		} else if (store2.getLocks() != null) {
			return false;
		}

		return true;
	}

	protected boolean compareFeatureIterators(Iterator iter1, Iterator iter2) {
		Feature feature;
		Feature ffeature;
		while (iter1.hasNext()) {
			feature = (Feature) iter1.next();
			ffeature = (Feature) iter2.next();
			if (!this.compareFeatures(feature, ffeature)) {
				return false;
			}
		}

		if (!iter2.hasNext()) {
			return true;
		} else {
			getLogger().warn("size !=");
			return false;
		}

	}

	protected boolean compareFeatureIterators(Iterator iter1, Iterator iter2,
			String[] attrNames) {
		Feature feature;
		Feature ffeature;
		while (iter1.hasNext()) {
			feature = (Feature) iter1.next();
			ffeature = (Feature) iter2.next();
			if (!this.compareFeatures(feature, ffeature, attrNames)) {
				return false;
			}
		}

		return !iter2.hasNext();

	}



	protected boolean compareTypes(FeatureType ft1, FeatureType ft2) {
		if (ft1.size() != ft2.size()) {
			getLogger().warn("size !=");
			return false;
		}
		if (ft1.getDefaultGeometryAttributeIndex() != ft2
				.getDefaultGeometryAttributeIndex()) {
			getLogger().warn(
					"getDefaultGeometryAttributeIndex "
							+ ft1.getDefaultGeometryAttributeIndex() +
					" !="+ ft2.getDefaultGeometryAttributeIndex());
			return false;
		}
		if (ft1.getDefaultGeometryAttributeIndex() > -1) {
			if (ft1.getDefaultSRS() != null) {
				if (!ft1.getDefaultSRS().equals(ft2.getDefaultSRS())) {
					getLogger().warn("getDefaultSRS !=");
					return false;
				}

			} else {
				if (ft2.getDefaultSRS() != null) {
					getLogger().warn("getDefaultSRS !=");
					return false;
				}
			}
		}

		if (ft1.getDefaultGeometryAttributeName() != null) {
			if (!ft1.getDefaultGeometryAttributeName().equals(
					ft2.getDefaultGeometryAttributeName())) {
				getLogger().warn("getDefaultGeometryAttributeName !=");

				return false;
			}
		} else {
			if (ft2.getDefaultGeometryAttributeName() != null) {
				getLogger().warn("getDefaultGeometryAttributeName !=");
				return false;
			}
		}



		FeatureAttributeDescriptor attr1, attr2;
		for (int i = 0; i < ft1.size(); i++) {
			attr1 = ft1.getAttributeDescriptor(i);
			attr2 = ft2.getAttributeDescriptor(i);

			if (!compareAttributes(attr1, attr2)) {
				return false;
			}

		}
		return true;

	}

	protected boolean compareAttributes(FeatureAttributeDescriptor attr1,
			FeatureAttributeDescriptor attr2) {
		if (attr1 == null || attr2 == null) {
			getLogger().warn("attr1 == null || attr2 == null");
			return false;
		}
		if (!attr1.getName().equals(attr2.getName())) {
			getLogger().warn(
					"name '" + attr1.getName() + "' != '" + attr2.getName()
							+ "'");
			return false;
		}

		if (attr1.getDataType() != attr2.getDataType()) {
			getLogger().warn(
					attr1.getName() + ":" +
					"dataType '" + attr1.getDataTypeName() + "'["
							+ attr1.getDataType() + "] != '"
							+ attr2.getDataTypeName() + "'["
							+ attr2.getDataType() + "]");
			return false;
		}

		if (attr1.getSize() != attr2.getSize()) {
			getLogger().warn(
					attr1.getName() + ":" +
					"size " + attr1.getSize() + " != " + attr2.getSize());
			return false;
		}

		if (attr1.getPrecision() != attr2.getPrecision()) {
			getLogger().warn(
					attr1.getName() + ":" +
					"precision " + attr1.getPrecision() + " != "
							+ attr1.getPrecision());
			return false;
		}

		if (attr1.getGeometryType() != attr2.getGeometryType()) {
			getLogger().warn(
					attr1.getName() + ":" +
					"GeometryType " + attr1.getGeometryType() + " != "
							+ attr2.getGeometryType());
			return false;
		}

		if (attr1.getGeometrySubType() != attr2.getGeometrySubType()) {
			getLogger().warn(
					attr1.getName() + ":" +
					"GeometrySubType " + attr1.getGeometrySubType() + " != "
							+ attr2.getGeometrySubType());

			return false;
		}

		if (attr1.getSRS() != null) {
			if (!attr1.getSRS().equals(attr2.getSRS())) {
				getLogger().warn(
						attr1.getName() + ":" +
						"srs " + attr1.getSRS() + " != " + attr2.getSRS());
				return false;
			}
		} else {
			if (attr2.getSRS() != null) {
				getLogger().warn(
						attr1.getName() + ":" +
						"srs " + attr1.getSRS() + " != " + attr2.getSRS());
				return false;
			}
		}

		return true;
	}

	protected boolean compareFeatures(Feature f1, Feature f2,
			String[] attrsNames) {
		FeatureAttributeDescriptor attr1;
		FeatureAttributeDescriptor attr2;
		Object v1, v2;
		for (int i = 0; i < attrsNames.length; i++) {
			attr1 = f1.getType().getAttributeDescriptor(attrsNames[i]);
			attr2 = f2.getType().getAttributeDescriptor(attrsNames[i]);
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

	protected boolean compareFeatures(Feature f1, Feature f2) {
		if (!compareTypes(f1.getType(), f2.getType())) {
			return false;
		}
		Iterator iter = f1.getType().iterator();
		FeatureAttributeDescriptor attr;
		Object v1, v2;
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			v1 = f1.get(attr.getName());
			v2 = f2.get(attr.getName());
			if (!compareFeatureValue(v1, v2, attr)) {
				return false;
			}
		}

		return true;

	}

	protected boolean compareFeatureValue(Object v1, Object v2,
			FeatureAttributeDescriptor attr) {

		if ((v1 == null || v2 == null) &&  !attr.allowNull() ){
			getLogger().warn("null and !allowNull:"
							+ attr.getName());
			return false;
		}

		if (v1 == v2) {
			return true;
		} else if (v1 == null) {
			getLogger().warn(" v1 == null and v2 != null:"
							+ attr.getName());
			return false;
		} else if (v2 == null) {
			getLogger().warn("v2 == null and v1 != null:"
							+ attr.getName());
			return false;

		}
		switch (attr.getDataType()) {
		case DataTypes.FEATURE:
			return compareFeatures((Feature) v1, (Feature) v2);

		case DataTypes.GEOMETRY:
			Geometry geom1 = (Geometry) v1;
			Geometry geom2 = (Geometry) v2;
			if (!geom1.equals(geom2)) {
				getLogger().warn(" v1 != v2 (Geom):" + attr.getName());
				return false;

			}
			return true;
		case DataTypes.DOUBLE:
			double diff = ((Double) v1).doubleValue()
					- ((Double) v1).doubleValue();
			if (!(Math.abs(diff) < 0.000001)) {
				getLogger().warn(" v1 != v2 (Dobule):" + attr.getName());
				return false;

			}
			return true;

//		case DataTypes.BYTEARRAY:
//			byte[] bytes1 = (byte[]) v1;
//			byte[] bytes2 = (byte[]) v2;
//			if (bytes1.length != bytes2.length) {
//				getLogger().warn(
//						"v1.length != v2.length (byte []):" + attr.getName());
//				return false;
//			}
//			for (int i = 0; i < bytes1.length; i++) {
//				if (bytes1[i] != bytes2[i]) {
//					getLogger().warn(
//							" v1[" + i + "] != v2[" + i + "] (byte []):"
//									+ attr.getName());
//					return false;
//				}
//			}
//			return true;

		case DataTypes.OBJECT:
			if (!v1.equals(v2)) {
				getLogger().warn(
						" v1 != v2 (object):" + attr.getName() + " [ignored]");

			}
			return true;

		default:
			if (!v1.equals(v2)) {
				getLogger()
						.warn(
								" v1 != v2:" + attr.getName() + ": " + v1
										+ " != " + v2);
				return false;
			}
		}
		return true;

	}


	//------------------------------------------------

	public void testSimpleIteration(FeatureStore store) {
		this.testSimpleIteration(store, null);
	}

	protected String[] getRandomAttibuteList(FeatureType fType) {
		String[] attrNames = new String[fType.size()];
		Iterator iter = fType.iterator();
		int i = 0;
		while (iter.hasNext()) {
			attrNames[i] = ((FeatureAttributeDescriptor) iter.next()).getName();
			i++;
		}
		return this.getRandomAttibuteList(attrNames);
	}

	protected Random getRandom(){
		if (rnd == null){
			rnd = new Random();
			rnd.setSeed(System.currentTimeMillis());
		}
		return rnd;
	}

	protected String[] getRandomAttibuteList(String[] attrNames) {
		int nAttributes = getRandom().nextInt(
				attrNames.length + (attrNames.length / 2)) + 1;
		TreeSet set = new TreeSet();
		for (int i = 0; i < nAttributes; i++) {
			set.add(attrNames[getRandom().nextInt(attrNames.length)]);
		}
		return (String[]) set.toArray(new String[0]);
	}

	public void testIterationFastAndStandart(FeatureStore store)
			throws Exception {
		this.testIterationFastAndStandart(store, null);

		FeatureQuery query = this.getDefaultQuery(store);
		// Random Attribute list
		query.setAttributeNames(getRandomAttibuteList(store
				.getDefaultFeatureType()));
		this.testIterationFastAndStandart(store, query);

		// Sorted
		FeatureAttributeDescriptor attr = getFirstAttributeOfType(store
				.getDefaultFeatureType(), new int[] { DataTypes.INT,
				DataTypes.LONG, DataTypes.STRING });
		{
			// asure that attr is in query attributes
			boolean attrFound = false;
			String[] curAttrs = query.getAttributeNames();
			for (int i = 0; i < curAttrs.length; i++) {
				if (curAttrs[i].equals(attr.getName())) {
					attrFound = true;
					break;

				}
			}
			if (!attrFound) {
				String[] newAttrs = new String[curAttrs.length + 1];
				for (int i = 0; i < curAttrs.length; i++) {
					newAttrs[i] = curAttrs[i];
				}
				newAttrs[curAttrs.length] = attr.getName();
				query.setAttributeNames(newAttrs);
			}
		}


		query.getOrder().add(attr.getName(), true);
		this.testIterationFastAndStandart(store, query);

		// Filter
		query = this.getDefaultQuery(store);

		query.setFilter(new Evaluator(){

			public Object evaluate(EvaluatorData data)
					throws EvaluatorException {
				// TODO Auto-generated method stub
				return Boolean.TRUE;
			}

			public String getCQL() {
				return "true = true";
			}

			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}

			public String getName() {
				return "AlwaysTrue";
			}

			public EvaluatorFieldsInfo getFieldsInfo() {
				// TODO Auto-generated method stub
				return null;
			}

		});
		this.testIterationFastAndStandart(store, query);

		// Filter + Sorted
		query.getOrder().add(attr.getName(), true);
		this.testIterationFastAndStandart(store, query);
	}

	public void testSimpleIteration(FeatureStore store, FeatureQuery query) {
		FeatureSet set;
		try {

			if (query == null) {
				query = this.getDefaultQuery(store);
			}
			set = store.getFeatureSet(query);
			FeatureType type = set.getDefaultFeatureType();

			DisposableIterator it = set.iterator();
			Feature feature;
			printFeatureTypeColNames(type, 15);
			while (it.hasNext()) {

				feature = (Feature) it.next();
				printFeature(feature, false, 15);
			}

			it.dispose();
			set.dispose();

		} catch (DataException e3) {
			e3.printStackTrace();
			fail();
			return;
		}

	}

	public void testIterationFastAndStandart(FeatureStore store,
			FeatureQuery query) {
		FeatureSet set;
		try {

			if (query == null) {
				query = this.getDefaultQuery(store);
			}
			set = store.getFeatureSet(query);

			DisposableIterator it = set.iterator();
			DisposableIterator fit = set.fastIterator();

			assertTrue(this.compareFeatureIterators(it, fit));

			it.dispose();
			fit.dispose();
			set.dispose();

		} catch (DataException e3) {
			e3.printStackTrace();
			fail();
			return;
		}

	}

	public void testSimpleIteration(DataStoreParameters parameters)
			throws Exception {
		FeatureStore store = null;
		store = (FeatureStore) dataManager.createStore(parameters);

		this.testSimpleIteration(store);

		store.dispose();

	}

	public void testIterationFastAndStandart(DataStoreParameters parameters)
			throws Exception {
		FeatureStore store = null;
		store = (FeatureStore) dataManager.createStore(parameters);

		this.testIterationFastAndStandart(store);

		store.dispose();

	}

	/**
	 *
	 * @param count
	 *            if (< 0) list.size() >= 1 else list.size() == count
	 * @throws Exception
	 */
	public void testExplorerList(int count) throws Exception {
		FeatureStore store = null;
		store = (FeatureStore) dataManager.createStore(this
				.getDefaultDataStoreParameters());

		DataServerExplorer explorer;
		explorer = store.getExplorer();

		if (count < 0) {
			assertTrue(explorer.list().size() >= 1);
		} else {
			assertTrue(explorer.list().size() == count);
		}

		store.dispose();

		explorer.dispose();
	}

	//=================================================
	//=================================================




	public void testIterationFastAndStandart() throws Exception {
		this.testIterationFastAndStandart(this.getDefaultDataStoreParameters());
	}

	public void testSimpleIteration() throws Exception {
		this.testSimpleIteration(this.getDefaultDataStoreParameters());
	}

	public void testInitializeStore() throws Exception {
		FeatureStore store = (FeatureStore) dataManager.createStore(this
				.getDefaultDataStoreParameters());

		assertNotNull(store.getMetadataID());
		assertNotNull(store.getName());
		assertEquals(store.getEnvelope(), store.getDynValue("Envelope"));
		assertTrue(store.getFeatureCount() > 0);
		if (store.isLocksSupported()) {
			assertNotNull(store.getLocks());
		} else {
			assertNull(store.getLocks());
		}
		store.dispose();
	}


	public void testExplorer() throws Exception {
		if (!this.hasExplorer()) {
			return;
		}
		this.testExplorerList(-1);

	}

	public void testSelection() throws Exception {
		DataStoreParameters parameters = this.getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager.createStore(parameters);
		FeatureSet set = store.getFeatureSet();

		assertTrue(store.getFeatureSelection().isEmpty());
		store.setSelection(set);
		assertFalse(store.getFeatureSelection().isEmpty());

		assertEquals(set.getSize(), store.getFeatureSelection().getSize());

		DisposableIterator iter = set.iterator();
		while (iter.hasNext()) {
			assertTrue(store.getFeatureSelection().isSelected(
					(Feature) iter.next()));
		}
		iter.dispose();

		store.getFeatureSelection().reverse();
		assertTrue(store.getFeatureSelection().isEmpty());
		assertEquals(0, store.getFeatureSelection().getSize());
		iter = set.iterator();
		while (iter.hasNext()) {
			assertFalse(store.getFeatureSelection().isSelected(
					(Feature) iter.next()));
		}
		iter.dispose();

		store.getFeatureSelection().reverse();
		assertEquals(set.getSize(), store.getFeatureSelection().getSize());
		assertFalse(store.getFeatureSelection().isEmpty());

		set.dispose();

	}

	public void testCustomFTypeSet() throws Exception {
		DataStoreParameters dbfParameters = this
				.getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager
				.createStore(dbfParameters);

		testCustomFTypeSet(store);

		store.dispose();
	}

	public void testCustomFTypeSet(FeatureStore store) throws Exception{

		FeatureSet set, set1;
		FeatureQuery query;
		DisposableIterator iter, iter1;
		Iterator attrIter;
		FeatureAttributeDescriptor attr;

		set = store.getFeatureSet(this.getDefaultQuery(store));
		attrIter = store.getDefaultFeatureType().iterator();

		String[] names;
		while (attrIter.hasNext()) {
			attr = (FeatureAttributeDescriptor) attrIter.next();
			int fieldIndex = attr.getIndex();

			query = this.getDefaultQuery(store);
			String fieldName = store.getDefaultFeatureType()
			.getAttributeDescriptor(fieldIndex).getName();

			names = new String[] { fieldName };
			query.setAttributeNames(names);
			set1 = store.getFeatureSet(query);

			if (getRandom().nextBoolean()) {
				iter = set.fastIterator();
			} else {
				iter = set.iterator();
			}
			if (getRandom().nextBoolean()) {
				iter1 = set1.fastIterator();
			} else {
				iter1 = set1.iterator();
			}

			assertTrue(compareFeatureIterators(iter, iter1, names));

			iter.dispose();
			iter1.dispose();
			set1.dispose();
		}

		int ntimes = getRandom().nextInt(10) + 5;
		FeatureType type = store.getDefaultFeatureType();
		query = this.getDefaultQuery(store);
		for (int i = 0; i < ntimes; i++) {
			names = getRandomAttibuteList(type);

			query.setAttributeNames(names);
			set1 = store.getFeatureSet(query);

			if (getRandom().nextBoolean()) {
				iter = set.fastIterator();
			} else {
				iter = set.iterator();
			}
			if (getRandom().nextBoolean()) {
				iter1 = set1.fastIterator();
			} else {
				iter1 = set1.iterator();
			}

			assertTrue(compareFeatureIterators(iter, iter1, names));

			iter.dispose();
			iter1.dispose();

			iter1 = set1.fastIterator();
			assertTrue(checksAttributesPositions(iter1, names));
			iter1.dispose();

			iter1 = set1.iterator();
			assertTrue(checksAttributesPositions(iter1, names));
			iter1.dispose();

			set1.dispose();


		}



		set.dispose();

	}

	protected boolean checksAttributesPositions(DisposableIterator iter,
			String[] names) {
		Feature feature;
		FeatureType type;
		FeatureAttributeDescriptor attr;
		while (iter.hasNext()) {
			feature = (Feature) iter.next();
			type = feature.getType();
			for (int i = 0; i < names.length; i++) {
				attr = type.getAttributeDescriptor(i);
				if (!names[i].equals(attr.getName())) {
					getLogger().error(
							"Error in attribute {} (expected: '{}' have: '{}'",
							new Object[] { new Integer(i), names[i],
									attr.getName() });
					return false;
				}
			}
		}
		return true;
	}

	// TODO: Corregir este test
    // public void testPersistence() throws Exception {
    // if (ToolsLocator.getPersistenceManager() == null) {
    // fail("Default Persistence Manager not register");
    // }
    // DataStoreParameters params = this.getDefaultDataStoreParameters();
    //
    //
    // FeatureStore store = (FeatureStore) dataManager.createStore(params);
    //
    // testSimpleIteration(store);
    //
    // PersistentState state = store.getState();
    //
    // FeatureStore store2 = (FeatureStore) ToolsLocator
    // .getPersistenceManager().create(state);
    //
    // testSimpleIteration(store2);
    //
    // assertTrue(compareStores(store, store2));
    //
    // // TODO Compare states
    // // XMLEntityState state2 = (XMLEntityState) store.getState();
    // //
    // // assertEquals(state.getXMLEntity().toString(), state2.getXMLEntity()
    // // .toString());
    // //
    // store.dispose();
    // store2.dispose();
    //
    //
    // }


	public void testSort() throws Exception {
		DataStoreParameters dbfParameters = this
				.getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager
				.createStore(dbfParameters);

		testSort(store);

		store.dispose();

	}

	public void testSort(FeatureStore store) throws Exception{
		FeatureSet set1;
		FeatureQuery query;
		DisposableIterator iter1;
		Iterator attrIter;
		FeatureAttributeDescriptor attr;

		attrIter = store.getDefaultFeatureType().iterator();

		String[] names;
		while (attrIter.hasNext()) {
			attr = (FeatureAttributeDescriptor) attrIter.next();

			if (attr.getDataType() == DataTypes.GEOMETRY) {
				continue;
			}
			query = this.getDefaultQuery(store);
			String fieldName = attr.getName();



			names = new String[] { fieldName };
			query.setAttributeNames(names);
			query.getOrder().add(fieldName, getRandom().nextBoolean());

			set1 = store.getFeatureSet(query);
			if (getRandom().nextBoolean()) {
				iter1 = set1.fastIterator();
			} else {
				iter1 = set1.iterator();
			}

			assertTrue(checkSort(iter1, query));

			iter1.dispose();
			set1.dispose();
		}

		int ntimes = getRandom().nextInt(10) + 5;
		FeatureType type = store.getDefaultFeatureType();
		query = this.getDefaultQuery(store);
		for (int i = 0; i < ntimes; i++) {
			names = getRandomAttibuteList(type);

			int nShortFields = getRandom().nextInt(names.length) + 1;
			query.getOrder().clear();
			for (int j = 0; j < nShortFields; j++) {
				attr = store.getDefaultFeatureType().getAttributeDescriptor(names[getRandom().nextInt(names.length)]);
				if (attr.getDataType() == DataTypes.INT
						|| attr.getDataType() == DataTypes.LONG
						|| attr.getDataType() == DataTypes.DOUBLE
						|| attr.getDataType() == DataTypes.STRING
						|| attr.getDataType() == DataTypes.DATE
						|| attr.getDataType() == DataTypes.BOOLEAN
						|| attr.getDataType() == DataTypes.BYTE
						|| attr.getDataType() == DataTypes.FLOAT) {

					query.getOrder().add(attr.getName(),
							getRandom().nextBoolean());
				}
			}

			query.setAttributeNames(names);
			set1 = store.getFeatureSet(query);

			// if (getRandom().nextBoolean()) {
				iter1 = set1.fastIterator();
				// } else {
				// iter1 = set1.iterator();
				// }

				assertTrue(checkSort(iter1, query));

				iter1.dispose();
				set1.dispose();

		}

	}


	public boolean checkSort(Iterator iter, FeatureQuery query) {

		FeatureQueryOrderMember order;
		Feature prevFeature = null;
		Feature currFeature = null;
		boolean isFirst = true;
		Comparable v1, v2;
		Object o1, o2;
		int v;
		FeatureQueryOrder queryOrder = query.getOrder();

		Iterator orderIter;

		//for debug only
		/*
		System.out.println("\nCheck order:");
		Iterator orderIter = queryOrder.iterator();
		while (orderIter.hasNext()) {
			order = (FeatureQueryOrderMember) orderIter.next();
			System.out.print(order.getAttributeName() + " ");
			if (order.getAscending()) {
				System.out.print("Asc, ");
			} else {
				System.out.print("Desc, ");
			}
		}
		System.out.println(";");
		*/

		while (iter.hasNext()) {
			currFeature = (Feature) iter.next();
			if (isFirst) {
				prevFeature = currFeature.getCopy();
				// printFeature(prevFeature, true, 15);
				isFirst = false;
				continue;
			}
			// printFeature(currFeature, false, 15);
			orderIter = queryOrder.iterator();
			while (orderIter.hasNext()) {
				order = (FeatureQueryOrderMember) orderIter.next();
				if (order.hasEvaluator()) {
					try {
						o1 = order.getEvaluator().evaluate(
								(EvaluatorData) prevFeature);
						o2 = order.getEvaluator().evaluate(
								(EvaluatorData) currFeature);
					} catch (EvaluatorException e) {
						throw new DataEvaluatorRuntimeException(e);
					}
				} else {

					o1 = prevFeature.get(order.getAttributeName());
					o2 = currFeature.get(order.getAttributeName());
				}
				if (o1 instanceof Comparable && o2 instanceof Comparable) {
					v1 = (Comparable) o1;
					v2 = (Comparable) o2;
				} else {
					// uncomparable objets
					break;
				}
				if (v1 == null) {
					if (v2 == null) {
						break;
					} else {
						v = 1;
					}
				} else {
					v = v1.compareTo(v2);
				}
				if (v != 0) {
					if (!order.getAscending()) {
						v = -v;
					}
				}
				if (v < 0) {
					break;
				} else if (v > 0) {
					// Checks for ignore case short
					if (v1 instanceof String && v2 instanceof String) {
						v1 = ((String)v1).toLowerCase();
						v2 = ((String)v2).toLowerCase();
						v = v1.compareTo(v2);
						if (v != 0) {
							if (!order.getAscending()) {
								v = -v;
							}
						}
						if (v < 0) {
							getLogger()
									.warn("Short compartor String ok with ignore case");
							break;
						} else if (v > 0) {
							return false;
						}
					} else {
						return false;
					}
				}
			}
			prevFeature = currFeature.getCopy();
		}

		return true;
	}

	protected void fullStoreIteratorTest(FeatureStore store) throws Exception{
		testIterationFastAndStandart(store);
		testCustomFTypeSet(store);
		testSort(store);
	}


	public void testTransformsData() throws Exception {
		DataStoreParameters dbfParameters = this
				.getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager
				.createStore(dbfParameters);

		FeatureStore store1 = (FeatureStore) dataManager
				.createStore(dbfParameters);

		FeatureStoreTransform transform = new StringsToLowerTransform();
		transform.setFeatureStore(store);

		store.getTransforms().add(transform);

		FeatureSet set, set1;
		DisposableIterator iter, iter1;
		Iterator iterAttr;
		FeatureAttributeDescriptor attr;
		Feature feature, feature1;
		int i, ntimes;
		Object v1, v2;


		fullStoreIteratorTest(store);

		set = store.getFeatureSet();
		set1 = store1.getFeatureSet();
		ntimes = getRandom().nextInt(3) + 1;
		for (i = 0; i < ntimes; i++) {
			if (getRandom().nextBoolean()) {
				iter = set.fastIterator();
			} else {
				iter = set.iterator();
			}
			if (getRandom().nextBoolean()) {
				iter1 = set1.fastIterator();
			} else {
				iter1 = set1.iterator();
			}
			while (iter.hasNext()) {
				feature = (Feature) iter.next();
				feature1 = (Feature) iter1.next();

				iterAttr = set.getDefaultFeatureType().iterator();
				while (iterAttr.hasNext()) {
					attr = (FeatureAttributeDescriptor) iterAttr.next();
					v1 = feature.get(attr.getIndex());
					v2 = feature1.get(attr.getIndex());
					if (attr.getDataType() == DataTypes.STRING) {
						if (v2 != null) {
							v2 = ((String) v2).toLowerCase();
						}

					}
					assertTrue(compareFeatureValue(v1, v2, attr));
				}
			}
			assertFalse(iter1.hasNext());
			iter.dispose();
			iter1.dispose();
		}


		set.dispose();
		set1.dispose();


		transform = new StringsToUpperTransform();
		transform.setFeatureStore(store);

		store.getTransforms().add(transform);

		fullStoreIteratorTest(store);

		set = store.getFeatureSet();
		set1 = store1.getFeatureSet();
		ntimes = getRandom().nextInt(3) + 1;
		for (i = 0; i < ntimes; i++) {
			if (getRandom().nextBoolean()) {
				iter = set.fastIterator();
			} else {
				iter = set.iterator();
			}
			if (getRandom().nextBoolean()) {
				iter1 = set1.fastIterator();
			} else {
				iter1 = set1.iterator();
			}
			while (iter.hasNext()) {
				feature = (Feature) iter.next();
				feature1 = (Feature) iter1.next();

				iterAttr = set.getDefaultFeatureType().iterator();
				while (iterAttr.hasNext()) {
					attr = (FeatureAttributeDescriptor) iterAttr.next();
					v1 = feature.get(attr.getIndex());
					v2 = feature1.get(attr.getIndex());
					if (attr.getDataType() == DataTypes.STRING) {
						if (v2 != null) {
							v2 = ((String) v2).toUpperCase();
						}

					}
					compareFeatureValue(v1, v2, attr);
				}
			}
			assertFalse(iter1.hasNext());
			iter.dispose();
			iter1.dispose();
		}
		set.dispose();
		set1.dispose();


		transform = new AddPrefixAttributeName("_");
		transform.setFeatureStore(store);

		store.getTransforms().add(transform);

		fullStoreIteratorTest(store);

		set = store.getFeatureSet();
		set1 = store1.getFeatureSet();
		ntimes = getRandom().nextInt(3) + 1;
		for (i = 0; i < ntimes; i++) {
			if (getRandom().nextBoolean()) {
				iter = set.fastIterator();
			} else {
				iter = set.iterator();
			}
			if (getRandom().nextBoolean()) {
				iter1 = set1.fastIterator();
			} else {
				iter1 = set1.iterator();
			}
			while (iter.hasNext()) {
				feature = (Feature) iter.next();
				feature1 = (Feature) iter1.next();

				iterAttr = set1.getDefaultFeatureType().iterator();
				while (iterAttr.hasNext()) {
					attr = (FeatureAttributeDescriptor) iterAttr.next();
					v1 = feature.get("_" + attr.getName());
					v2 = feature1.get(attr.getIndex());
					if (attr.getDataType() == DataTypes.STRING) {
						if (v2 != null) {
							v2 = ((String) v2).toUpperCase();
						}

					}
					compareFeatureValue(v1, v2, attr);
				}
			}
			assertFalse(iter1.hasNext());
			iter.dispose();
			iter1.dispose();
		}
		set.dispose();
		set1.dispose();

		transform = new AddAttribute("__new__", DataTypes.STRING, "$$OK$$", 10);
		transform.setFeatureStore(store);

		store.getTransforms().add(transform);

		fullStoreIteratorTest(store);

		set = store.getFeatureSet();
		set1 = store1.getFeatureSet();
		ntimes = getRandom().nextInt(3) + 1;
		for (i = 0; i < ntimes; i++) {
			if (getRandom().nextBoolean()) {
				iter = set.fastIterator();
			} else {
				iter = set.iterator();
			}
			if (getRandom().nextBoolean()) {
				iter1 = set1.fastIterator();
			} else {
				iter1 = set1.iterator();
			}
			while (iter.hasNext()) {
				feature = (Feature) iter.next();
				feature1 = (Feature) iter1.next();

				iterAttr = set1.getDefaultFeatureType().iterator();
				while (iterAttr.hasNext()) {
					attr = (FeatureAttributeDescriptor) iterAttr.next();
					v1 = feature.get("_" + attr.getName());
					v2 = feature1.get(attr.getIndex());
					if (attr.getDataType() == DataTypes.STRING) {
						if (v2 != null) {
							v2 = ((String) v2).toUpperCase();
						}

					}
					compareFeatureValue(v1, v2, attr);
				}
				compareFeatureValue(feature.get("__new__"), "$$OK$$", set1
						.getDefaultFeatureType().getAttributeDescriptor(
								"__new__"));
			}
			assertFalse(iter1.hasNext());
			iter.dispose();
			iter1.dispose();
		}
		set.dispose();
		set1.dispose();


		transform = new RemoveAttribute("__new__");
		transform.setFeatureStore(store);

		store.getTransforms().add(transform);

		fullStoreIteratorTest(store);

		set = store.getFeatureSet();
		set1 = store1.getFeatureSet();
		ntimes = getRandom().nextInt(3) + 1;
		for (i = 0; i < ntimes; i++) {
			if (getRandom().nextBoolean()) {
				iter = set.fastIterator();
			} else {
				iter = set.iterator();
			}
			if (getRandom().nextBoolean()) {
				iter1 = set1.fastIterator();
			} else {
				iter1 = set1.iterator();
			}
			while (iter.hasNext()) {
				feature = (Feature) iter.next();
				feature1 = (Feature) iter1.next();

				iterAttr = set1.getDefaultFeatureType().iterator();
				while (iterAttr.hasNext()) {
					attr = (FeatureAttributeDescriptor) iterAttr.next();
					v1 = feature.get("_" + attr.getName());
					v2 = feature1.get(attr.getIndex());
					if (attr.getDataType() == DataTypes.STRING) {
						if (v2 != null) {
							v2 = ((String) v2).toUpperCase();
						}

					}
					compareFeatureValue(v1, v2, attr);
				}
				assertNull(feature.getType().getAttributeDescriptor("__new__"));
			}
			assertFalse(iter1.hasNext());
			iter.dispose();
			iter1.dispose();
		}
		set.dispose();
		set1.dispose();

		store.getTransforms().clear();

		compareStores(store, store1);


		store.dispose();
		store1.dispose();
	}

	abstract class myTransform implements FeatureStoreTransform {
		protected FeatureStore store;
		protected FeatureType orgDefaultFType;
		protected List orgFTypes;

		public void applyTransform(Feature source, EditableFeature target)
				throws DataException {

			Iterator iter = target.getType().iterator();
			FeatureAttributeDescriptor attr;
			while (iter.hasNext()) {
				attr = (FeatureAttributeDescriptor) iter.next();
				this.setValue(source, target, attr);
			}

		}

		protected void setValue(Feature source, EditableFeature target,
				FeatureAttributeDescriptor attrTarget) throws DataException {
			target.set(attrTarget.getIndex(), source.get(attrTarget.getName()));
		}

		public void saveToState(PersistentState state)
		throws PersistenceException {
			// TODO Auto-generated method stub

		}

		public void loadFromState(PersistentState state) throws PersistenceException {
			// TODO Auto-generated method stub

		}

		public FeatureType getDefaultFeatureType() throws DataException {
			return orgDefaultFType;
		}

		public FeatureStore getFeatureStore() {
			return store;
		}

		public List getFeatureTypes() throws DataException {
			return orgFTypes;
		}

		public void setFeatureStore(FeatureStore featureStore) {
			this.store = featureStore;
			try {
				this.orgDefaultFType = this.store.getDefaultFeatureType();
				this.orgFTypes = this.store.getFeatureTypes();

			} catch (DataException e) {
				throw new RuntimeException(e);
			}

		}

		public PersistentState getState() throws PersistenceException {
			// TODO Auto-generated method stub
			return null;
		}

		public FeatureType getSourceFeatureTypeFrom(
				FeatureType targetFeatureType) {

			return targetFeatureType;
		}

	}

	abstract class TransformTypeTransform extends myTransform {

		private FeatureType myDefaultFeatureType = null;
		private List myFeatureTypes = null;

		public FeatureType getDefaultFeatureType() throws DataException {
			if (this.myDefaultFeatureType == null) {
				this.myDefaultFeatureType = this.transformType(orgDefaultFType);
			}

			return this.myDefaultFeatureType;
		}

		protected abstract FeatureType transformType(FeatureType type);

		protected abstract FeatureType restoreType(FeatureType type);


		public List getFeatureTypes() throws DataException {
			if (this.myFeatureTypes == null) {
				ArrayList list = new ArrayList();
				Iterator iter = orgFTypes.iterator();
				while (iter.hasNext()) {
					FeatureType type = (FeatureType) iter.next();
					if (type.getId().equals(
							this.getDefaultFeatureType().getId())) {
						list.add(this.getDefaultFeatureType());
					} else {
						list.add(this.transformType(type));
					}
				}
				this.myFeatureTypes = Collections.unmodifiableList(list);
			}
			return this.myFeatureTypes;
		}

		public boolean isTransformsOriginalValues() {
			return false;
		}

		public FeatureType getSourceFeatureTypeFrom(
				FeatureType targetFeatureType) {
			FeatureType org = null;
			FeatureType cur = null;
			Iterator iter = null;
			try {
				iter = this.getFeatureTypes().iterator();
			} catch (DataException e) {
				new RuntimeException(e);
			}
			while (iter.hasNext()) {
				cur = (FeatureType) iter.next();
				if (cur.getId().equals(targetFeatureType.getId())) {
					org = cur;
					break;
				}
			}
			if (org == null) {
				throw new RuntimeException();
			}

			return this.restoreType(org);
		}

	}

	abstract class TransformAttributeNameTransform extends
			TransformTypeTransform {

		protected void setValue(Feature source, EditableFeature target,
				FeatureAttributeDescriptor attrTarget) throws DataException {
			target.set(attrTarget.getIndex(), source.get(this
					.restoreAttributeName(attrTarget.getName())));
		}


		protected FeatureType transformType(FeatureType type) {
			EditableFeatureType result = type.getEditable();
			Iterator iter = result.iterator();
			EditableFeatureAttributeDescriptor attr;
			while (iter.hasNext()) {
				attr = (EditableFeatureAttributeDescriptor) iter.next();
				attr.setName(transformAttributeName(attr.getName()));
			}
			return result.getNotEditableCopy();
		}

		protected abstract String transformAttributeName(String source);

		protected abstract String restoreAttributeName(String target);

		protected FeatureType restoreType(FeatureType type) {
			EditableFeatureType result;
			if (type instanceof EditableFeatureType) {
				result = (EditableFeatureType) type.getCopy();
			} else {
				result = type.getEditable();
			}
			Iterator iter = result.iterator();
			EditableFeatureAttributeDescriptor attr;
			while (iter.hasNext()) {
				attr = (EditableFeatureAttributeDescriptor) iter.next();

				attr.setName(restoreAttributeName(attr.getName()));
			}
			return result.getNotEditableCopy();
		}

		public FeatureType getSourceFeatureTypeFrom(
				FeatureType targetFeatureType) {
			FeatureType org = null;
			FeatureType cur = null;
			Iterator iter = null;
			iter = this.orgFTypes.iterator();
			while (iter.hasNext()) {
				cur = (FeatureType) iter.next();
				if (cur.getId().equals(targetFeatureType.getId())) {
					org = cur;
					break;
				}
			}
			if (cur == null) {
				throw new RuntimeException();
			}
			EditableFeatureType r = org.getEditable();
			iter = r.iterator();
			FeatureAttributeDescriptor attr;
			while (iter.hasNext()) {
				attr = (FeatureAttributeDescriptor) iter.next();
				if (targetFeatureType.getIndex(transformAttributeName(attr
						.getName())) == -1) {
					iter.remove();
				}
			}
			return r.getNotEditableCopy();
		}

	}


	class AddPrefixAttributeName extends TransformAttributeNameTransform {

		private String prefix;

		AddPrefixAttributeName(String prefix) {
			this.prefix = prefix;
		}

		protected String restoreAttributeName(String target) {
			return target.substring(getPrefix().length(), target.length());
		}

		private String getPrefix() {
			return prefix;
		}

		protected String transformAttributeName(String source) {
			return getPrefix() + source;
		}

	}

	class AddAttribute extends TransformTypeTransform {

		private String name;
		private int type;
		private Object defValue;
		private int size;
		private Evaluator eval;

		AddAttribute(String name) {
			this(name, DataTypes.STRING, null, 15);
		}

		AddAttribute(String name, int type) {
			this(name, type, null, 15);
		}

		AddAttribute(String name, int type, Object defValue) {
			this(name, type, defValue, 15);
		}

		AddAttribute(String name, int type, Evaluator evaluator, int size) {
			this.name = name;
			this.type = type;
			this.defValue = null;
			this.size = size;
			this.eval = evaluator;
		}

		AddAttribute(String name, int type, Object defValue, int size) {
			this.name = name;
			this.type = type;
			this.defValue = defValue;
			this.size = size;
			this.eval = null;
		}



		protected FeatureType restoreType(FeatureType type) {
			EditableFeatureType result;
			if (type instanceof EditableFeatureType) {
				result = (EditableFeatureType) type.getCopy();
			} else {
				result = type.getEditable();
			}
			result.remove(this.name);
			return result.getNotEditableCopy();
		}

		protected FeatureType transformType(FeatureType type) {
			EditableFeatureType result = type.getEditable();

			EditableFeatureAttributeDescriptor att;
			if (this.eval == null) {
				att = result.add(name, this.type)
						.setDefaultValue(this.defValue);
			} else {
				att = result.add(name, this.type, this.eval);
			}
			att.setSize(size);


			return result.getNotEditableCopy();
		}

		protected void setValue(Feature source, EditableFeature target,
				FeatureAttributeDescriptor attrTarget) throws DataException {
			if (attrTarget.getName().equals(this.name)) {
				target.set(attrTarget.getIndex(), attrTarget.getDefaultValue());
			} else {
				target.set(attrTarget.getIndex(), source.get(attrTarget
						.getName()));
			}
		}
	}


	class RemoveAttribute extends TransformTypeTransform {

		private String attributeName;
		private String fTypeIdToRemoveAttribute = null;
		private FeatureAttributeDescriptor attr;

		RemoveAttribute(String attributeName) {
			this.attributeName = attributeName;
		}

		RemoveAttribute(String attributeName, FeatureType fType) {
			this.attributeName = attributeName;
		}

		protected FeatureType restoreType(FeatureType type) {
			if (!type.getId().equals(this.fTypeIdToRemoveAttribute)) {
				return type;
			}
			EditableFeatureType result = type.getEditable();

			EditableFeatureAttributeDescriptor att;
			if (this.attr.getEvaluator() == null) {
				att = result.add(attributeName, this.attr.getDataType())
						.setDefaultValue(this.attr.getDefaultValue());
			} else {
				att = result.add(attributeName, this.attr.getDataType(),
						this.attr.getEvaluator());
			}
			att.setSize(this.attr.getSize());
			att.setAllowNull(this.attr.allowNull());
			att.setGeometryType(this.attr.getGeometryType());
			att.setSRS(this.attr.getSRS());
			att.setPrecision(this.attr.getPrecision());
			// TODO


			return result.getNotEditableCopy();
		}

		protected FeatureType transformType(FeatureType type) {
			if (!type.getId().equals(this.fTypeIdToRemoveAttribute)) {
				return type;
			}

			EditableFeatureType result;
			if (type instanceof EditableFeatureType) {
				result = (EditableFeatureType) type.getCopy();
			} else {
				result = type.getEditable();
			}
			result.remove(this.attributeName);
			return result.getNotEditableCopy();

		}

		public void setFeatureStore(FeatureStore featureStore) {
			Iterator iter;
			try {
				iter = featureStore.getFeatureTypes().iterator();
			} catch (DataException e) {
				throw new RuntimeException(e);
			}
			FeatureType type;
			FeatureAttributeDescriptor attrTmp;
			while (iter.hasNext()) {
				type = (FeatureType) iter.next();
				attrTmp = type.getAttributeDescriptor(this.attributeName);
				if (attrTmp != null) {
					this.fTypeIdToRemoveAttribute = type.getId();
					this.attr = attrTmp;
					break;
				}
			}
			super.setFeatureStore(featureStore);
		}

		public void applyTransform(Feature source, EditableFeature target)
				throws DataException {
			// TODO Auto-generated method stub
			super.applyTransform(source, target);
		}



	}


	public class StringsToLowerTransform extends myTransform {

		public boolean isTransformsOriginalValues() {
			return true;
		}

		protected void setValue(Feature source, EditableFeature target,
				FeatureAttributeDescriptor attr) throws DataException {
			if (attr.getDataType() == DataTypes.STRING) {
				String v = ((String) source.get(attr.getName()));
				if (v != null){
					v = v.toLowerCase();
				} else if (!attr.allowNull()) {
					v = (String) attr.getDefaultValue();
					v = v.toLowerCase();
				}
				target.set(attr.getName(), v);
			} else {
				target.set(attr.getName(), source.get(attr.getName()));
			}
		}

	}

	class StringsToUpperTransform extends StringsToLowerTransform {
		protected void setValue(Feature source, EditableFeature target,
				FeatureAttributeDescriptor attr) throws DataException {
			if (attr.getDataType() == DataTypes.STRING) {
				String v = ((String) source.get(attr.getName()));
				if (v != null) {
					v = v.toUpperCase();
				} else if (!attr.allowNull()) {
					v = (String) attr.getDefaultValue();
					v.toUpperCase();
				}
				target.set(attr.getName(), v);
			} else {
				target.set(attr.getName(), source.get(attr.getName()));
			}
		}

	}


	public void testFeatureReference() throws Exception {
		DataStoreParameters dbfParameters = this
				.getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager
				.createStore(dbfParameters);

		Feature feature, refered;
		FeatureSet set;
		int nTimes2 = getRandom().nextInt(2) + 1;

		for (int j = 0; j < nTimes2; j++) {
			set = store.getFeatureSet();
			DisposableIterator iter;

			int nTimes = getRandom().nextInt(2) + 3;
			for (int i = 0; i < nTimes; i++) {
				if (getRandom().nextBoolean()) {
					iter = set.fastIterator();
				} else {
					iter = set.fastIterator();
				}
				while (iter.hasNext()) {
					feature = (Feature) iter.next();
					refered = feature.getReference().getFeature();
					compareFeatures(feature, refered);
				}
				iter.dispose();
			}

			set.dispose();
		}

		nTimes2 = getRandom().nextInt(5) + 2;
		FeatureQuery query = store.createFeatureQuery();

		for (int j = 0; j < nTimes2; j++) {
			DisposableIterator iter;

			query.setAttributeNames(getRandomAttibuteList(store
					.getDefaultFeatureType()));
			set = store.getFeatureSet(query);

			int nTimes = getRandom().nextInt(3) + 3;
			for (int i = 0; i < nTimes; i++) {
				if (getRandom().nextBoolean()) {
					iter = set.fastIterator();
				} else {
					iter = set.fastIterator();
				}
				while (iter.hasNext()) {
					feature = (Feature) iter.next();
					refered = feature.getReference().getFeature(
							set.getDefaultFeatureType());
					compareFeatures(feature, refered);
				}
				iter.dispose();
			}

			set.dispose();
		}



		store.dispose();

	}

	public void testResourcesLocks() throws Exception {

		if (!this.usesResources()) {
			return;
		}

		DataStoreParameters dbfParameters = this
				.getDefaultDataStoreParameters();

		FeatureStore store = (FeatureStore) dataManager
				.createStore(dbfParameters);

		int nThreads = getRandom().nextInt(4) + 2;
		List threads = new ArrayList();
		TaskTestIterators task;
		for (int i = 0; i < nThreads; i++) {
			task = new TaskTestIterators(this, "" + i, store);
			threads.add(task);
			task.start();
		}

		Iterator iter;
		List stillAlives = new ArrayList();
		stillAlives.addAll(threads);

		while (!stillAlives.isEmpty()) {
			iter = stillAlives.iterator();
			while (iter.hasNext()) {
				task = (TaskTestIterators) iter.next();
				if (!task.isAlive()) {
					iter.remove();
				} else if ((!task.isFinished()) && task.isOutOfDate()) {
					iter.remove();
					getLogger().error("task {} outOfDate", task.getName());
				} else {
					Thread.yield();
					Thread.sleep(100);
				}
			}
		}

		store.dispose();

		iter = threads.iterator();
		while (iter.hasNext()) {
			task = (TaskTestIterators) iter.next();
			assertTrue(task.isFinishedOk());
		}
	}

	class TaskTestIterators extends StoreTask {
		private BaseTestFeatureStore testInstance;

		public TaskTestIterators(BaseTestFeatureStore testInstance,
				String name, FeatureStore store) {
			super(name, store);
			this.testInstance = testInstance;
		}

		public TaskTestIterators(BaseTestFeatureStore testInstance,
				String name, FeatureStore store, int timeToWait) {
			super(name, store, timeToWait);
			this.testInstance = testInstance;
		}

		public void run() {
			if (!this.startProcess()) {
				return;
			}
			try {
				this.testInstance.fullStoreIteratorTest(store);

				finishedOk();
			} catch (Throwable e) {
				finishedError(e);
				return;
			}
		}


	}


	protected void tearDown() throws Exception {
		super.tearDown();
		if (!usesResources()) {
			return;
		}

		ResourceManager resMan = DALLocator.getResourceManager();
		resMan.closeResources();
	}



}

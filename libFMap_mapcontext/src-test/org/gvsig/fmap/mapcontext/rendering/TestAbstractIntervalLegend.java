 /* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.fmap.mapcontext.rendering;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DataQuery;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataSet;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureIndex;
import org.gvsig.fmap.dal.feature.FeatureIndexes;
import org.gvsig.fmap.dal.feature.FeatureLocks;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreTransforms;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.feature.exception.NeedEditingModeException;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.mapcontext.rendering.legend.AbstractIntervalLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.FInterval;
import org.gvsig.fmap.mapcontext.rendering.legend.IClassifiedVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.symbol.TestISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.metadata.Metadata;
import org.gvsig.metadata.exceptions.MetadataNotFoundException;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.operations.OperationContext;
import org.gvsig.tools.operations.OperationException;
import org.gvsig.tools.operations.OperationNotSupportedException;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.gvsig.tools.undo.RedoException;
import org.gvsig.tools.undo.UndoException;

/**
 * Integration test to ensure that the legends which implements the
 * IVectorialIntervalLegend interface follow the rules that follow the managing
 * of them by the application.
 *
 * @author pepe vidal salvador - jose.vidal.salvador@iver.es
 */
public class TestAbstractIntervalLegend extends TestCase {

	private static final Integer v0 = new Integer(0);
	private static final Integer v1 = new Integer(1);
	private static final Integer v2 = new Integer(2);
	private static final Integer v3 = new Integer(3);

	private static final String FIELD3 = "field3";
	private static final String FIELD2 = "field2";
	private static final String FIELD1 = "field1";
	private static final String FIELD0 = "field0";
	private static final int FIELDID = 0;

	private static final Integer[] feature0Values = new Integer[] { v0, v1, v2, v3, };
	private static final Integer[] feature1Values = new Integer[] { v3, v0, v1, v2, };
	private static final Integer[] feature2Values = new Integer[] { v2, v3, v0, v1, };
	private static final Integer[] feature3Values = new Integer[] { v1, v2, v3, v0, };

	private static final Integer[][] featureValues = new Integer[][] {
		feature0Values,
		feature1Values,
		feature2Values,
		feature3Values,};

	private static String[] fieldNames = new String[] {FIELD0,FIELD1,FIELD2,FIELD3,};


	// private static final Value v4 = (Value)ValueFactory.createValue(4);
	// private static final Value v5 = (Value)ValueFactory.createValue(5);
	// private static final Value v6 = (Value)ValueFactory.createValue(6);
	// private static final Value v7 = (Value)ValueFactory.createValue(7);
	// private static final Value v8 = (Value)ValueFactory.createValue(8);
	// private static final Value v9 = (Value)ValueFactory.createValue(9);

	private AbstractIntervalLegend[] intervalLegends;
	private MockDataSource mockDataSource = new MockDataSource();

	/**
	 * To avoid duplicated validation logic in the test a mock object is created
	 * to use a DataSource for this test.
	 *
	 */
	private class MockDataSource implements FeatureStore {

		public boolean allowWrite() {
			// TODO Auto-generated method stub
			return false;
		}

		public void beginEditingGroup(String description)
				throws NeedEditingModeException {
			// TODO Auto-generated method stub

		}

		public boolean canWriteGeometry(int gvSIGgeometryType)
				throws DataException {
			// TODO Auto-generated method stub
			return false;
		}

		public void cancelEditing() throws DataException {
			// TODO Auto-generated method stub

		}

		public FeatureSelection createFeatureSelection() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public FeatureIndex createIndex(FeatureType featureType,
				String attributeName, String indexName) throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public EditableFeature createNewFeature() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public EditableFeature createNewFeature(FeatureType type,
				Feature defaultValues) throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public EditableFeature createNewFeature(FeatureType type,
				boolean defaultValues) throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public EditableFeature createNewFeature(boolean defaultValues)
				throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public void delete(Feature feature) throws DataException {
			// TODO Auto-generated method stub

		}

		public void edit() throws DataException {
			// TODO Auto-generated method stub

		}

		public void edit(int mode) throws DataException {
			// TODO Auto-generated method stub

		}

		public void endEditingGroup() throws NeedEditingModeException {
			// TODO Auto-generated method stub

		}

		public void export(DataServerExplorer explorer,
				NewFeatureStoreParameters params) throws DataException {
			// TODO Auto-generated method stub

		}

		public void finishEditing() throws DataException {
			// TODO Auto-generated method stub

		}

		public FeatureType getDefaultFeatureType() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public Envelope getEnvelope() {
			// TODO Auto-generated method stub
			return null;
		}

		public Feature getFeatureByReference(FeatureReference reference)
				throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public Feature getFeatureByReference(FeatureReference reference,
				FeatureType featureType) throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public FeatureSelection getFeatureSelection() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public FeatureSet getFeatureSet() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public FeatureSet getFeatureSet(FeatureQuery featureQuery)
				throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public void getFeatureSet(FeatureQuery featureQuery, Observer observer)
				throws DataException {
			// TODO Auto-generated method stub

		}

		public void getFeatureSet(Observer observer) throws DataException {
			// TODO Auto-generated method stub

		}

		public List getFeatureTypes() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public FeatureIndexes getIndexes() {
			// TODO Auto-generated method stub
			return null;
		}

		public FeatureLocks getLocks() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public DataStoreParameters getParameters() {
			// TODO Auto-generated method stub
			return null;
		}

		public IProjection getSRSDefaultGeometry() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public FeatureStoreTransforms getTransforms() {
			// TODO Auto-generated method stub
			return null;
		}

		public void insert(EditableFeature feature) throws DataException {
			// TODO Auto-generated method stub

		}

		public boolean isAppendModeSupported() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isAppending() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isEditing() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isLocksSupported() {
			// TODO Auto-generated method stub
			return false;
		}


		public void setSelection(FeatureSet selection) throws DataException {
			// TODO Auto-generated method stub

		}
		public void update(EditableFeatureType featureType)
				throws DataException {
			// TODO Auto-generated method stub

		}

		public void update(EditableFeature feature) throws DataException {
			// TODO Auto-generated method stub

		}

		public void validateFeatures(int mode) throws DataException {
			// TODO Auto-generated method stub

		}

		public DataSet createSelection() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public void dispose() throws DataException {
			// TODO Auto-generated method stub

		}

		public Iterator getChildren() {
			// TODO Auto-generated method stub
			return null;
		}

		public DataSet getDataSet() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public DataSet getDataSet(DataQuery dataQuery) throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public void getDataSet(Observer observer) throws DataException {
			// TODO Auto-generated method stub

		}

		public void getDataSet(DataQuery dataQuery, Observer observer)
				throws DataException {
			// TODO Auto-generated method stub

		}

		public DataServerExplorer getExplorer() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public DataSet getSelection() throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public void refresh() throws DataException {
			// TODO Auto-generated method stub

		}

		public void setSelection(DataSet selection) throws DataException {
			// TODO Auto-generated method stub

		}

		public void beginComplexNotification() {
			// TODO Auto-generated method stub

		}

		public void disableNotifications() {
			// TODO Auto-generated method stub

		}

		public void enableNotifications() {
			// TODO Auto-generated method stub

		}

		public void endComplexNotification() {
			// TODO Auto-generated method stub

		}

		public void addObserver(Observer o) {
			// TODO Auto-generated method stub

		}

		public void deleteObserver(Observer o) {
			// TODO Auto-generated method stub

		}

		public void deleteObservers() {
			// TODO Auto-generated method stub

		}

		public PersistentState getState() throws PersistenceException {
			// TODO Auto-generated method stub
			return null;
		}

		public void saveToState(PersistentState state)
				throws PersistenceException {
			// TODO Auto-generated method stub

		}

		public void loadFromState(PersistentState state) throws PersistenceException {
			// TODO Auto-generated method stub

		}

		public Object getOperation(int code) throws OperationException,
				OperationNotSupportedException {
			// TODO Auto-generated method stub
			return null;
		}

		public Object getOperation(String name) throws OperationException,
				OperationNotSupportedException {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean hasOperation(int code) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean hasOperation(String name) {
			// TODO Auto-generated method stub
			return false;
		}

		public Object invokeOperation(int code, OperationContext context)
				throws OperationException, OperationNotSupportedException {
			// TODO Auto-generated method stub
			return null;
		}

		public Object invokeOperation(String name, OperationContext context)
				throws OperationException, OperationNotSupportedException {
			// TODO Auto-generated method stub
			return null;
		}

		public FeatureQuery createFeatureQuery() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean canRedo() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean canUndo() {
			// TODO Auto-generated method stub
			return false;
		}

		public List getRedoInfos() {
			// TODO Auto-generated method stub
			return null;
		}

		public List getUndoInfos() {
			// TODO Auto-generated method stub
			return null;
		}

		public void redo() throws RedoException {
			// TODO Auto-generated method stub

		}

		public void redo(int num) throws RedoException {
			// TODO Auto-generated method stub

		}

		public void undo() throws UndoException {
			// TODO Auto-generated method stub

		}

		public void undo(int num) throws UndoException {
			// TODO Auto-generated method stub

		}

		public FeatureIndex createIndex(FeatureType featureType,
				String attributeName, String indexName, Observer observer)
				throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public Metadata getMetadata() throws MetadataNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}

		public Object getMetadataID() {
			// TODO Auto-generated method stub
			return null;
		}

		public void delegate(DynObject dynObject) {
			// TODO Auto-generated method stub

		}

		public DynClass getDynClass() {
			// TODO Auto-generated method stub
			return null;
		}

		public Object getDynValue(String name) throws DynFieldNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean hasDynValue(String name) {
			// TODO Auto-generated method stub
			return false;
		}

		public void implement(DynClass dynClass) {
			// TODO Auto-generated method stub

		}

		public Object invokeDynMethod(String name, DynObject context)
				throws DynMethodException {
			// TODO Auto-generated method stub
			return null;
		}

		public Object invokeDynMethod(int code, DynObject context)
				throws DynMethodException {
			// TODO Auto-generated method stub
			return null;
		}

		public void setDynValue(String name, Object value)
				throws DynFieldNotFoundException {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.gvsig.metadata.Metadata#getMetadataChildren()
		 */
		public Set getMetadataChildren() {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.gvsig.metadata.Metadata#getMetadataName()
		 */
		public String getMetadataName() {
			// TODO Auto-generated method stub
			return null;
		}

		public FeatureType getFeatureType(String featureTypeId)
				throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		public DataQuery createQuery() {
			// TODO Auto-generated method stub
			return null;
		}

		public long getFeatureCount() throws DataException {
			// TODO Auto-generated method stub
			return 0;
		}


	}

	 private static final FInterval interval0=new FInterval(0,2);
	 private static final FInterval interval1=new FInterval(3,5);
	 private static final FInterval interval2=new FInterval(6,8);
	 private static final FInterval interval3=new FInterval(9,11);
	// private static final Value interval4;
	// private static final Value interval5;
	// private static final Value interval6;
	// private static final Value interval7;
	// private static final Value interval8;
	// private static final Value interval9;

	Hashtable symTable;

	private ISymbol[] symbols;
	private FInterval[] sampleIntervals = new FInterval[] { interval0, interval1, interval2, interval3, };
	private Feature[] features;

	// private FInterval[] intervals = new FInterval[] {
	// interval0,
	// interval1,
	// interval2,
	// interval3,
	// interval4,
	// interval5,
	// interval6,
	// interval7,
	// interval8,
	// interval9,
	// };

	protected void setUp() throws Exception {
		super.setUp();

		//Initializes the geometries library
		DefaultGeometryLibrary lib = new DefaultGeometryLibrary();
		lib.initialize();
		lib.postInitialize();

		features = new Feature[4];

		// initialize test values
		for (int i = 0; i < features.length; i++) {

			// create the geometry associated to the feature
			int size = 200;
			Dimension d = new Dimension(size, size);
			Rectangle aShape = new Rectangle(i * size, i * size, d.width,
					d.height);
			GeometryManager geomManager = GeometryLocator.getGeometryManager();
			Curve curve = (Curve)geomManager.create(TYPES.CURVE, SUBTYPES.GEOM2D);
			curve.setGeneralPath(new GeneralPathX(aShape));

			/*
			 * create a full-featured Feature with randomed values at its fields
			 * to avoid testing over the same values each time
			 */
			//TODO
//			features[i] = new MemoryFeature(geom, featureValues[i], "[" + i
//					+ "]");
		}

		// initialize the symbol subset for this test
		symbols = TestISymbol.getNewSymbolInstances();

		// initialize the legends for this test
		ILegend[] allLegends = TestILegend.getNewLegendInstances();
		ArrayList intervalLegends = new ArrayList();
		for (int i = 0; i < allLegends.length; i++) {

			if (allLegends[i] instanceof AbstractIntervalLegend) {
				intervalLegends.add(allLegends[i]);
			}

			if (allLegends[i] instanceof IClassifiedVectorLegend) {
				IClassifiedVectorLegend cvl = (IClassifiedVectorLegend) allLegends[i];
				cvl.setClassifyingFieldNames(new String[] { fieldNames[FIELDID] });
				cvl.setFeatureStore(mockDataSource);

			}
		}

		this.intervalLegends = (AbstractIntervalLegend[]) intervalLegends
				.toArray(new AbstractIntervalLegend[intervalLegends.size()]);
	}

	/**
	 * This method is used to add symbols to a legend.That is, it takes an array
	 * of AbstractIntervalLegend which is empty and, using a second array
	 * of FIntervals(values), the first one is filled.Also, a hash table is filled
	 * using the array of FIntervals (it will be useful in some tests to check
	 * that a symbol can be taken using a feature) .
	 *
	 * @param legend
	 * @return
	 */
	private void fillClassifiedLegend(AbstractIntervalLegend legend,
			FInterval[] values) {
		// initialize the hash table
		symTable = new Hashtable();

		// to add symbols to the legend and the hash table
		for (int j = 0; j < values.length; j++) {

			ISymbol sym = symbols[j % symbols.length];
			legend.addSymbol(values[j], sym);
			symTable.put(values[j], sym);
		}
	}

	/**
	 * This test ensures that when a legend is filled, the number of symbols
	 * added is correct. To do it, is checked that the number of symbols of a
	 * legend is the same as the length of the array of example values that we
	 * have.
	 *
	 * @throws ReadDriverException
	 */
	public void testICLAdittion() throws ReadException {

		// Fills the legend
		for (int i = 0; i < intervalLegends.length; i++) {
			fillClassifiedLegend(intervalLegends[i], sampleIntervals);
		}

		for (int i = 0; i < intervalLegends.length; i++) {
			assertEquals(intervalLegends[i].getClassName()
					+ " fails with the comparation of the number of symbols",
					intervalLegends[i].getSymbols().length,
					sampleIntervals.length);
		}

	}

	/**
	 * This test ensures that the symbols that we have previously added to a
	 * legend are accessible using its features.To do it, this test compares the
	 * symbol taken from the legend with the symbol taken from the hashTable
	 * (using the same feature).
	 *
	 * @throws ReadDriverException
	 */

	public void testICLCheckValueSymbols() throws ReadException {

		ISymbol tableSym =null;

		// fills the legends
		for (int i = 0; i < intervalLegends.length; i++) {
			fillClassifiedLegend(intervalLegends[i], sampleIntervals);
		}

		for (int i = 0; i < intervalLegends.length; i++) {
			// For each feature
			for (int j = 0; j < features.length; j++) {
				Feature myFeature = features[i];
				// takes the value of the field that identifies the feature
				Object val = myFeature.get(FIELDID);
				// the last value is used to access to the hash table to obtain
				// a symbol

				if(interval0.isInInterval(val)) {
					tableSym = (ISymbol) symTable.get(interval0);
				} else if(interval1.isInInterval(val)) {
					tableSym = (ISymbol) symTable.get(interval1);
				} else if(interval2.isInInterval(val)) {
					tableSym = (ISymbol) symTable.get(interval2);
				} else if(interval3.isInInterval(val)) {
					tableSym = (ISymbol) symTable.get(interval3);
				}

				AbstractIntervalLegend leg = intervalLegends[i];
				// takes the symbol from a legend using the feature
				ISymbol legendSym = leg.getSymbolByFeature(myFeature);
				// compares that both symbols are the same
				assertEquals(legendSym.getClassName()
						+ " fails with the comparation of the class symbols",
						legendSym, tableSym);
			}
		}
	}

}



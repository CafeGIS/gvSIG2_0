package org.gvsig.geocoding.data;

import java.io.File;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPStoreProvider;
import org.gvsig.fmap.geom.GeometryLibrary;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.sqljep.SQLJEPEvaluator;
import org.gvsig.tools.evaluator.sqljep.SQLJEPLibrary;
import org.gvsig.tools.locator.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class FilterTest extends TestCase {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(FilterTest.class);
	private DataManager manager = null;
	private SHPStoreParameters params = null;
	private FeatureStore store = null;
	private FeatureSet features = null;

	/**
	 * setUp
	 */
	protected void setUp() throws Exception {
		super.setUp();

		Library tools = new ToolsLibrary();
		tools.initialize();
		tools.postInitialize();

		Library dlib = new DALLibrary();
		dlib.initialize();
		dlib.postInitialize();

		Library libFile = new DALFileLibrary();
		libFile.initialize();
		libFile.postInitialize();

		Library lib = new GeometryLibrary();
		lib.initialize();
		lib.postInitialize();

		Library shpLib = new SHPLibrary();
		shpLib.initialize();
		shpLib.postInitialize();

		 Library jep = new SQLJEPLibrary();
		 jep.initialize();
		 jep.postInitialize();

		manager = DALLocator.getDataManager();
		params = (SHPStoreParameters) manager
				.createStoreParameters(SHPStoreProvider.NAME);
		params.setFile(new File("./test-data/geocoder/streets.shp"));
		store = (FeatureStore) manager.createStore(params);

		manager.registerDefaultEvaluator(SQLJEPEvaluator.class);

	}

	/**
	 * tearDown
	 */
	protected void tearDown() throws Exception {

	}

	
	/**
	 * Simple search
	 * @throws DataException
	 */
	public void testSimpleFilter() throws DataException {
		System.out.println("Number of features: "
				+ store.getFeatureSet().getSize());

		FeatureQuery query = store.createFeatureQuery();
		// Evaluator exp = manager.createExpresion("RT_ADD < 20");
		Evaluator exp = manager.createExpresion("STREET_TYP like 'C'");
		query.setFilter(exp);
		features = store.getFeatureSet(query);
		long cant = features.getSize();
		System.out.println("Number of features finded: " + cant);

	}
}

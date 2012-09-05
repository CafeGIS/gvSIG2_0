/**
 * 
 */
package org.gvsig.data.vectorial.filter;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.gvsig.data.CloseException;
import org.gvsig.data.DataManager;
import org.gvsig.data.DataStoreParameters;
import org.gvsig.data.InitializeException;
import org.gvsig.data.OpenException;
import org.gvsig.data.ReadException;
import org.gvsig.data.datastores.vectorial.file.shp.Register;
import org.gvsig.data.datastores.vectorial.file.shp.SHPStore;
import org.gvsig.data.datastores.vectorial.file.shp.SHPStoreParameters;
import org.gvsig.data.vectorial.Feature;
import org.gvsig.data.vectorial.FeatureCollection;
import org.gvsig.data.vectorial.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.sort.SortBy;

/**
 * @author jsanz
 * 
 */
public class testStreetsFilters extends TestCase {

    private static final float THRESHOLD = 0.78f;
    static Logger log = null;
    static String TEST_SHP = "test-data/geocoder/streets.shp";
    static String SEARCH_TERM = "PECHINA";

    SHPStore store;

    public testStreetsFilters() {
	super();
    }

    public testStreetsFilters(String name) {
	super(name);
	// TODO Auto-generated constructor stub
    }

    /**
     * @throws java.lang.Exception
     */
    protected void setUp() throws Exception {
	if (log == null) {
	    // Configurar el log4j
	    PropertyConfigurator.configure("log4j.properties");
	    log = Logger.getLogger(testStreetsFilters.class);
	    log.info("Test initialized");
	}

	Register.selfRegister();
	DataManager manager = DataManager.getManager();
	SHPStoreParameters parameters = null;
	parameters = (SHPStoreParameters) manager
		.createDataStoreParameters(SHPStore.DATASTORE_NAME);
	parameters.setFile(new File(TEST_SHP));

	try {
	    store = (SHPStore) manager
		    .createDataStore((DataStoreParameters) parameters);
	} catch (InitializeException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * @throws java.lang.Exception
     */
    protected void tearDown() throws Exception {
    }



    public void testTextDistanceFilter() {
	log.info("-----------testTextDistanceFilter-----------");
	// long t1 = System.currentTimeMillis();
	DistanceFilterFactory2Impl fact = new DistanceFilterFactory2Impl();
	Expression exp1 = fact.property("STREET_NAM");
	Filter filter = fact.distance(exp1, SEARCH_TERM, THRESHOLD);
	// Filter filter = fact.equals(exp1, exp2);
	int results = testshp(filter, null);
	assertEquals(5, results);

	log.info("-----------testTextDistanceFilter-----------");
    }

    /**
     * Convenience method to test the filter
     * 
     * @param filter
     * @param order
     * @return
     */
    private int testshp(Filter filter, SortBy[] order) {
	try {
	    store.open();
	} catch (OpenException e2) {
	    // TODO Auto-generated catch block
	    e2.printStackTrace();
	}

	FeatureType ft = store.getDefaultFeatureType();
	FeatureCollection featureCollection = null;

	try {
	    long t1 = System.currentTimeMillis();
	    featureCollection = (FeatureCollection) store.getDataCollection(ft,
		    filter, order);
	    log.info("Search retrieved in " + (System.currentTimeMillis() - t1)
		    + " msecs");
	    log.info(featureCollection.size() + " results");
	} catch (ReadException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	for (Object obj : featureCollection) {
	    if (obj instanceof Feature) {
		Feature feat = (Feature) obj;
		log.info("STREET NAME : " + feat.get("STREET_NAM"));
	    }
	}

	int numFeat = featureCollection.size();
	featureCollection.dispose();

	try {
	    store.close();
	    store.dispose();
	} catch (CloseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return numFeat;
    }
}

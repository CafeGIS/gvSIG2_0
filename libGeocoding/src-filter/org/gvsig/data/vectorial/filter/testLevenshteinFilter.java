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
 * 2008 Prodevelop S.L  main development
 */

package org.gvsig.data.vectorial.filter;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.gvsig.data.CloseException;
import org.gvsig.data.DataCollection;
import org.gvsig.data.DataManager;
import org.gvsig.data.DataStoreParameters;
import org.gvsig.data.InitializeException;
import org.gvsig.data.OpenException;
import org.gvsig.data.ReadException;
import org.gvsig.data.datastores.vectorial.file.dbf.DBFFeature;
import org.gvsig.data.datastores.vectorial.file.dbf.DBFStore;
import org.gvsig.data.datastores.vectorial.file.dbf.DBFStoreParameters;
import org.gvsig.data.datastores.vectorial.file.dbf.Register;
import org.gvsig.data.vectorial.AttributeDescriptor;
import org.gvsig.data.vectorial.DefaultFeatureType;
import org.gvsig.data.vectorial.Feature;
import org.gvsig.data.vectorial.FeatureCollection;
import org.gvsig.data.vectorial.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.sort.SortBy;

/**
 * @author jsanz
 * 
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 * 
 */
public class testLevenshteinFilter extends TestCase {

	private static final float THRESHOLD = 0.78f;
	static Logger log = null;
	static String TEST_DBF = "test-data/AL.dbf";
	static String SEARCH_FIELD = "ASCIINAME";
	static String SEARCH_TERM = "Okshtunit";

	DBFStore store;

	public testLevenshteinFilter() {
		super();
	}

	public testLevenshteinFilter(String name) {
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
			log = Logger.getLogger(testLevenshteinFilter.class);
			log.info("Test initialized");
		}

		Register.selfRegister();
		DataManager manager = DataManager.getManager();
		DBFStoreParameters parameters = null;
		parameters = (DBFStoreParameters) manager
				.createDataStoreParameters(DBFStore.DATASTORE_NAME);
		parameters.setFile(new File(TEST_DBF));

		try {
			store = (DBFStore) manager
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

	@SuppressWarnings("unchecked")
	public void testIterate() throws ReadException {
		log.info("-----------testIterate-----------");

		long t1 = System.currentTimeMillis();
		DataCollection data = store.getDataCollection();
		log.info("Get data in " + (System.currentTimeMillis() - t1) + " msecs");

		Iterator<DBFFeature> it = data.iterator();
		DBFFeature dbfFeature;
		t1 = System.currentTimeMillis();
		while (it.hasNext()) {
			dbfFeature = it.next();
			dbfFeature.get(SEARCH_FIELD);
		}
		log.info("Iterate data in " + (System.currentTimeMillis() - t1)
				+ " msecs");

		log.info("-----------testIterate-----------");
	}

	public void testGetFeatureTypes() {
		log.info("-----------testGetFeatureTypes-----------");

		List<FeatureType> types = store.getFeatureTypes();
		assertEquals(1, types.size());
		DefaultFeatureType ftype = (DefaultFeatureType) types.get(0);
		log.info("Type with " + ftype.size() + " attributes");
		Object att = ftype.get(SEARCH_FIELD);

		// for (int i=0;i<ftype.size();i++){
		// log.info(((AttributeDescriptor) ftype.get(i)).getName());
		// }

		assertNotNull(att);
		assertTrue(att instanceof AttributeDescriptor);
		AttributeDescriptor ad = (AttributeDescriptor) att;
		assertEquals(AttributeDescriptor.TYPE_STRING, ad.getDataType());
		log.info("-----------testGetFeatureTypes-----------");
	}

	public void testEqualsFilter() {
		log.info("-----------testEqualsFilter-----------");
		// long t1 = System.currentTimeMillis();
		FilterFactory2 fact = new DistanceFilterFactory2Impl();
		Expression exp1 = fact.property(SEARCH_FIELD);
		Expression exp2 = fact.literal(SEARCH_TERM);
		Filter filter = fact.equals(exp1, exp2);

		int results = testshp(filter, null);
		assertEquals(0, results);

		log.info("-----------testEqualsFilter-----------");
	}

	public void testLikeFilter() {
		log.info("-----------testLikeFilter-----------");
		// long t1 = System.currentTimeMillis();
		FilterFactory2 fact = new DistanceFilterFactory2Impl();
		Expression exp1 = fact.property(SEARCH_FIELD);
		Filter filter = fact.like(exp1, "%" + SEARCH_TERM + "%");

		int results = testshp(filter, null);
		assertEquals(2, results);

		log.info("-----------testLikeFilter-----------");
	}

	public void testTextDistanceFilter() {
		log.info("-----------testTextDistanceFilter-----------");
		// long t1 = System.currentTimeMillis();
		DistanceFilterFactory2Impl fact = new DistanceFilterFactory2Impl();
		Expression exp1 = fact.property(SEARCH_FIELD);
		Filter filter = fact.distance(exp1, SEARCH_TERM, THRESHOLD);
		// Filter filter = fact.equals(exp1, exp2);
		int results = testshp(filter, null);
		assertEquals(4, results);

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
				log.info(SEARCH_FIELD + " : " + feat.get(SEARCH_FIELD) + "("
						+ feat.get("GENAMEID") + ")");
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

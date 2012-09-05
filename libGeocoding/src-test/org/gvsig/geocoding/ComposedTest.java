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
 * 2008 PRODEVELOP		Main development
 */

package org.gvsig.geocoding;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPStoreProvider;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.ComposedAddress;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.impl.DefaultAddressComponent;
import org.gvsig.geocoding.address.impl.DefaultComposedAddress;
import org.gvsig.geocoding.address.impl.DefaultLiteral;
import org.gvsig.geocoding.address.impl.DefaultRelationsComponent;
import org.gvsig.geocoding.impl.DataGeocoderImpl;
import org.gvsig.geocoding.impl.DefaultGeocodingLibrary;
import org.gvsig.geocoding.pattern.GeocodingSource;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.pattern.impl.DefaultGeocodingSource;
import org.gvsig.geocoding.pattern.impl.DefaultPatterngeocoding;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.geocoding.styles.impl.Composed;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.evaluator.sqljep.SQLJEPEvaluator;
import org.gvsig.tools.locator.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class ComposedTest extends TestCase {

	protected DataManager dataManager = null;
	private File file = new File("./test-data/geocoder/streets.shp");
	private Logger log = LoggerFactory.getLogger(ComposedTest.class);

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

		Library shpLib = new SHPLibrary();
		shpLib.initialize();
		shpLib.postInitialize();

		Library geocoLib = new DefaultGeocodingLibrary();
		geocoLib.initialize();
		geocoLib.postInitialize();

		dataManager = DALLocator.getDataManager();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 * @throws ReadException
	 */
	public void testCrossGeocode() throws ReadException {

		/* Set the pattern */
		Patterngeocoding pat = getPattern();

		Geocoder geocoder = GeocodingLocator.getInstance().getGeocoder();
		((DataGeocoderImpl) geocoder).setPattern(pat);

		/* Define a address search */
		Address address = getCrossAddress();

		Set<GeocodingResult> results = null;
		try {
			results = geocoder.geocode(address);
		} catch (Exception e) {
			log.error("Geocoding", e);
		}
		int si = results.size();
		assertNotNull(results);
		assertEquals(4, si);
		Iterator<GeocodingResult> it = results.iterator();
		log.debug("CROSS GEOCODING");
		while (it.hasNext()) {
			GeocodingResult res = it.next();
			Point pto = (Point) res.getGeometry();
			// log.debug("PTO: "+pto.getX()+","+pto.getY());
		}
	}

	/**
	 * 
	 * @throws ReadException
	 */
	public void testThirdGeocode() throws ReadException {

		/* Set the pattern */
		Patterngeocoding pat = getPattern();
		
		Geocoder geocoder = GeocodingLocator.getInstance().getGeocoder();
		((DataGeocoderImpl) geocoder).setPattern(pat);

		/* Define a address search */
		Address address = getBetweenAddress();

		Set<GeocodingResult> results = null;
		try {
			results = geocoder.geocode(address);
		} catch (Exception e) {
			log.error("Geocoding", e);
		}
		assertNotNull(results);
		assertEquals(2, results.size());
		Iterator<GeocodingResult> it = results.iterator();
		log.debug("THIRD GEOCODING");
		while (it.hasNext()) {
			GeocodingResult res = it.next();
			Point pto = (Point) res.getGeometry();
			log.debug("PTO: " + pto.getX() + "," + pto.getY());
		}
	}

	/**
	 * get pattern
	 * 
	 * @return
	 */
	private Patterngeocoding getPattern() {

		assertNotNull(dataManager);
		dataManager.registerDefaultEvaluator(SQLJEPEvaluator.class);

		Patterngeocoding pat = new DefaultPatterngeocoding();

		try {
			pat.setPatternName("ComposedCentroid");

			GeocodingSource source = new DefaultGeocodingSource();

			SHPStoreParameters params = (SHPStoreParameters) dataManager
					.createStoreParameters(SHPStoreProvider.NAME);
			assertNotNull(file);
			params.setFile(file);
			FeatureStore store = (FeatureStore) dataManager.createStore(params);
			source.setLayerSource(store);

			AbstractGeocodingStyle style = new Composed();
			FeatureType ftype = store.getDefaultFeatureType();
			Literal relations = new DefaultLiteral();
			relations.add(new DefaultRelationsComponent("Calle", "STREET_NAM"));
			relations.add(new DefaultRelationsComponent("TipoVia","STREET_TYP"));
			relations.add(new DefaultRelationsComponent("Municipio","MUNI"));
			relations.add(new DefaultRelationsComponent("Provincia","PROV"));
			relations.add(new DefaultRelationsComponent("Pais","PAIS"));

			style.setRelationsLiteral(relations);
			source.setStyle(style);

			pat.setSource(source);

		} catch (Exception e) {
			log.error("Building a pattern", e);
		}

		return pat;
	}

	/**
	 * get Address
	 * 
	 * @return
	 */
	private Address getCrossAddress() {

		Literal literal = new DefaultLiteral();
		literal.add(new DefaultAddressComponent("Calle", "DOCTOR ROMAGOSA"));
		literal.add(new DefaultAddressComponent("TipoVia", "C"));
		literal.add(new DefaultAddressComponent("Municipio", "Valencia"));
		literal.add(new DefaultAddressComponent("Provincia", "Valencia"));
		literal.add(new DefaultAddressComponent("Pais", "Espanya"));

		Literal literal2 = new DefaultLiteral();
		literal2
				.add(new DefaultAddressComponent("Calle", "DON JUAN DE AUSTRIA"));
		literal2.add(new DefaultAddressComponent("TipoVia", "C"));
		literal2.add(new DefaultAddressComponent("Municipio", "Valencia"));
		literal2.add(new DefaultAddressComponent("Provincia", "Valencia"));
		literal2.add(new DefaultAddressComponent("Pais", "Espanya"));

		Address address = new DefaultComposedAddress(literal);

		List<Literal> intersect = new ArrayList<Literal>();
		intersect.add(literal2);
		((ComposedAddress) address).setIntersectionLiterals(intersect);

		return address;
	}

	/**
	 * get Address
	 * 
	 * @return
	 */
	private Address getBetweenAddress() {

		DefaultLiteral literal = new DefaultLiteral();
		literal.add(new DefaultAddressComponent("Calle", "ROGER DE LAURIA"));
		literal.add(new DefaultAddressComponent("TipoVia", "C"));
		literal.add(new DefaultAddressComponent("Municipio", "Valencia"));
		literal.add(new DefaultAddressComponent("Provincia", "Valencia"));
		literal.add(new DefaultAddressComponent("Pais", "Espanya"));

		Literal literal2 = new DefaultLiteral();
		literal2.add(new DefaultAddressComponent("Calle", "PASCUAL Y GENIS"));
		literal2.add(new DefaultAddressComponent("TipoVia", "C"));
		literal2.add(new DefaultAddressComponent("Municipio", "Valencia"));
		literal2.add(new DefaultAddressComponent("Provincia", "Valencia"));
		literal2.add(new DefaultAddressComponent("Pais", "Espanya"));

		Literal literal3 = new DefaultLiteral();
		literal3.add(new DefaultAddressComponent("Calle", "PEREZ PUJOL"));
		literal3.add(new DefaultAddressComponent("TipoVia", "C"));
		literal3.add(new DefaultAddressComponent("Municipio", "Valencia"));
		literal3.add(new DefaultAddressComponent("Provincia", "Valencia"));
		literal3.add(new DefaultAddressComponent("Pais", "Espanya"));

		Address address = new DefaultComposedAddress(literal);

		List<Literal> intersect = new ArrayList<Literal>();
		intersect.add(literal2);
		intersect.add(literal3);
		((ComposedAddress) address).setIntersectionLiterals(intersect);

		return address;
	}

}

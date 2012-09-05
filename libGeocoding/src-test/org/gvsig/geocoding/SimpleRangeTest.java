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
import java.util.Iterator;
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
import org.gvsig.fmap.geom.GeometryLibrary;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.NumberAddress;
import org.gvsig.geocoding.address.RelationsComponent;
import org.gvsig.geocoding.address.impl.DefaultAddressComponent;
import org.gvsig.geocoding.address.impl.DefaultLiteral;
import org.gvsig.geocoding.address.impl.DefaultNumberAddress;
import org.gvsig.geocoding.address.impl.DefaultRelationsComponent;
import org.gvsig.geocoding.impl.DataGeocoderImpl;
import org.gvsig.geocoding.impl.DefaultGeocodingLibrary;
import org.gvsig.geocoding.pattern.GeocodingSource;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.pattern.impl.DefaultGeocodingSource;
import org.gvsig.geocoding.pattern.impl.DefaultPatterngeocoding;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.geocoding.styles.impl.SimpleRange;
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
public class SimpleRangeTest extends TestCase {

	protected DataManager dataManager = null;
	private File shape = new File("./test-data/geocoder/streets.shp");
	private Logger log = LoggerFactory.getLogger(SimpleRangeTest.class);

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
		
		Library geocoLib = new DefaultGeocodingLibrary();
		geocoLib.initialize();
		geocoLib.postInitialize();

		dataManager = DALLocator.getDataManager();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test
	 * @throws ReadException
	 */
	public void testGeocode() throws ReadException {

		/* Set the pattern */
		Patterngeocoding pat = getPattern();

		Geocoder geocoder = GeocodingLocator.getInstance().getGeocoder();
		((DataGeocoderImpl) geocoder).setPattern(pat);

		Address address = getAddress();

		Set<GeocodingResult> results = null;
		try {
			results = geocoder.geocode(address);
		} catch (Exception e) {
			log.error("Geocoding", e);
		}
		assertNotNull(results);
		assertEquals(2, results.size());
		Iterator<GeocodingResult> it = results.iterator();
		log.debug("SIMPLE RANGE GEOCODING");
		while (it.hasNext()) {
			GeocodingResult res = it.next();
			Point pto = (Point)res.getGeometry();
			log.debug("PTO: "+pto.getX()+","+pto.getY());			
		}
	}

	/**
	 * get the pattern
	 * @return
	 */
	private Patterngeocoding getPattern() {

		assertNotNull(dataManager);
		dataManager.registerDefaultEvaluator(SQLJEPEvaluator.class);

		Patterngeocoding pat = new DefaultPatterngeocoding();

		try {
			pat.setPatternName("SimpleRange");

			GeocodingSource source = new DefaultGeocodingSource();

			SHPStoreParameters params = (SHPStoreParameters) dataManager
					.createStoreParameters(SHPStoreProvider.NAME);
			assertNotNull(shape);
			params.setFile(shape);
			FeatureStore store = (FeatureStore) dataManager.createStore(params);
			source.setLayerSource(store);

			AbstractGeocodingStyle style = new SimpleRange();
			DefaultLiteral relations = new DefaultLiteral();
			relations.add(new DefaultRelationsComponent("Calle","STREET_NAM"));
			relations.add(new DefaultRelationsComponent("TipoVia","STREET_TYP"));
			relations.add(new DefaultRelationsComponent("Municipio","MUNI"));
			relations.add(new DefaultRelationsComponent("Provincia","PROV"));
			relations.add(new DefaultRelationsComponent("Pais","PAIS"));

			style.setRelationsLiteral(relations);
			source.setStyle(style);

			RelationsComponent from = new DefaultRelationsComponent("NParFrom","RF_ADD");
			;
			((SimpleRange) style).setFirstNumber(from);

			RelationsComponent to = new DefaultRelationsComponent("NParTo","RT_ADD");
			((SimpleRange) style).setLastNumber(to);

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
	private Address getAddress() {
		Literal literal = new DefaultLiteral();
		literal.add(new DefaultAddressComponent("Calle", "COLON"));
		literal.add(new DefaultAddressComponent("TipoVia", "C"));
		literal.add(new DefaultAddressComponent("Municipio", "Valencia"));
		literal.add(new DefaultAddressComponent("Provincia", "Valencia"));
		literal.add(new DefaultAddressComponent("Pais", "Espanya"));

		Address address = new DefaultNumberAddress(literal);
		((NumberAddress)address).setNumber(56);
		return address;
	}

}

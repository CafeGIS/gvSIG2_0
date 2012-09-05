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

package org.gvsig.geocoding.geomatches;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPStoreProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLibrary;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.geocoding.geommatches.MatcherUtils;
import org.gvsig.geocoding.impl.DefaultGeocodingLibrary;
import org.gvsig.geocoding.result.DissolveResult;
import org.gvsig.geocoding.result.ScoredFeature;
import org.gvsig.geocoding.result.impl.DefaultScoredFeature;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.locator.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class TestMatcherUtils extends TestCase {

	private static final Logger log = LoggerFactory
			.getLogger(TestMatcherUtils.class);

	private DataManager dataManager = null;
	private FeatureStore provStore = null;
	private FeatureStore streetsStore = null;
	private FeatureStore sLinesStore = null;

	/**
	 * setUP
	 */
	public void setUp() {

		try {
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

		} catch (Exception e) {
			e.printStackTrace();
		}

		dataManager = DALLocator.getDataManager();

		try {
			// prov file
			File prov = new File("./test-data/geocoder/prov.shp");
			assertTrue(prov.exists());
			SHPStoreParameters provParam = (SHPStoreParameters) dataManager
					.createStoreParameters(SHPStoreProvider.NAME);
			provParam.setFile(prov);
			provStore = (FeatureStore) dataManager.createStore(provParam);
			assertNotNull(provStore);
		} catch (Exception e) {
			log.error("Getting prov.shp store", e);
		}
		try {
			// streets file
			File streets = new File("./test-data/geocoder/streets.shp");
			assertTrue(streets.exists());
			SHPStoreParameters strParam = (SHPStoreParameters) dataManager
					.createStoreParameters(SHPStoreProvider.NAME);
			strParam.setFile(streets);
			streetsStore = (FeatureStore) dataManager.createStore(strParam);
			assertNotNull(streetsStore);

		} catch (Exception e) {
			log.error("Getting streets.shp store", e);
		}
		try {
			// streets file
			File sLines = new File("./test-data/geocoder/simpleLines.shp");
			assertTrue(sLines.exists());
			SHPStoreParameters sLinesParam = (SHPStoreParameters) dataManager
					.createStoreParameters(SHPStoreProvider.NAME);
			sLinesParam.setFile(sLines);
			sLinesStore = (FeatureStore) dataManager.createStore(sLinesParam);
			assertNotNull(sLinesStore);

		} catch (Exception e) {
			log.error("Getting streets.shp store", e);
		}
	}

	/**
	 * tearDown
	 */
	public void tearDown() {

	}

	/**
	 * Test internal point of geometries
	 * 
	 * @throws DataException
	 */
	public void testInternalPointGeometry() throws DataException {

		FeatureSet feats = provStore.getFeatureSet();
		assertNotNull(feats);
		for (Iterator<Feature> iterator = feats.iterator(); iterator.hasNext();) {
			Feature fea = iterator.next();
			Geometry geom = fea.getDefaultGeometry();
			Point pto = MatcherUtils.internalPointGeometry(geom);
			assertTrue(geom.contains(pto.getX(), pto.getY()));
		}
	}

	/**
	 * Parse geometries to gvSIG model to JTS model and return to gvSIG Model
	 * 
	 * @throws DataException
	 * @throws CreateGeometryException 
	 */
	public void testParseGeomsGVToJTSToGV() throws DataException, CreateGeometryException {
		FeatureSet feats = provStore.getFeatureSet();
		assertNotNull(feats);
		List<Geometry> geomsGV = new ArrayList<Geometry>();
		for (Iterator<Feature> iterator = feats.iterator(); iterator.hasNext();) {
			Feature fea = iterator.next();
			geomsGV.add(fea.getDefaultGeometry());
		}
		List<com.vividsolutions.jts.geom.Geometry> geomsJTS = MatcherUtils
				.parseGeomsGVToJTS(geomsGV);
		List<Geometry> geomsGV2 = MatcherUtils.parseGeomsJTSToGV(geomsJTS);

		assertTrue(geomsGV.get(0).getDimension() == geomsGV2.get(0)
				.getDimension());
		assertTrue(geomsGV.get(1).getType() == geomsGV2.get(1).getType());
		assertTrue(geomsGV.get(2).getDimension() == geomsGV2.get(2)
				.getDimension());
		assertTrue(geomsGV.get(3).getType() == geomsGV2.get(3).getType());
		assertTrue(geomsGV.get(4).getType() == geomsGV2.get(4).getType());
		assertTrue(geomsGV.get(5).getBounds().getCenterX() == geomsGV2.get(5)
				.getBounds().getCenterX());
	}

	/**
	 * Intersection test
	 * 
	 * @throws DataException
	 */
	public void testDissolveLines() throws DataException {

		List<DissolveResult> results = new ArrayList<DissolveResult>();

		FeatureSet feats = sLinesStore.getFeatureSet();
		assertNotNull(feats);

		List<ScoredFeature> listFeats = new ArrayList<ScoredFeature>();
		for (Iterator<Feature> iterator = feats.iterator(); iterator.hasNext();) {
			Feature fea = iterator.next();
			ScoredFeature sFeat = new DefaultScoredFeature();
			sFeat.setFeature(fea);
			listFeats.add(sFeat);
		}
		// group by attribute
		HashMap<String, List<ScoredFeature>> dissolAttributes = MatcherUtils
				.groupScoredFeaturesByAttribute("Campo1", listFeats);

		// For each group of features, to do geometries dissolve process getting
		// at the end one o more geometries
		for (Iterator<Map.Entry<String, List<ScoredFeature>>> iterator = dissolAttributes
				.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, List<ScoredFeature>> e = iterator.next();
			List<ScoredFeature> inlist = e.getValue();

			// dissolve Lines (if first geometry of array is Line)
			List<DissolveResult> geomsDissol = MatcherUtils.dissolveGeomsJTS(
					inlist, MatcherUtils.LINES);

			// insert each dissolve result in the general array
			for (DissolveResult dissolveResult : geomsDissol) {
				results.add(dissolveResult);
			}
		}
		assertNotNull(results);
		assertEquals(results.size(), 4);

	}

	/**
	 * Intersection test
	 * 
	 * @throws DataException
	 */
	public void testDissolvePolys() throws DataException {
		List<DissolveResult> results = new ArrayList<DissolveResult>();

		FeatureSet feats = provStore.getFeatureSet();
		assertNotNull(feats);

		List<ScoredFeature> listFeats = new ArrayList<ScoredFeature>();
		for (Iterator<Feature> iterator = feats.iterator(); iterator.hasNext();) {
			Feature fea = iterator.next();
			ScoredFeature sFeat = new DefaultScoredFeature();
			sFeat.setFeature(fea);
			sFeat.setScore(Math.random());
			listFeats.add(sFeat);
		}
		// group by attribute		
		HashMap<String, List<ScoredFeature>> dissolAttributes = MatcherUtils
				.groupScoredFeaturesByAttribute("COM", listFeats);

		// For each group of features, to do geometries dissolve process getting
		// at the end one o more geometries
		for (Iterator<Map.Entry<String, List<ScoredFeature>>> iterator = dissolAttributes
				.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, List<ScoredFeature>> e = iterator.next();
			List<ScoredFeature> inlist = e.getValue();

			// dissolve Polys (if first geometry of array is Line)
			List<DissolveResult> geomsDissol = MatcherUtils.dissolveGeomsJTS(
					inlist, MatcherUtils.POLYS);

			// insert each dissolve result in the general array
			for (DissolveResult dissolveResult : geomsDissol) {
				results.add(dissolveResult);
			}
		}
		assertNotNull(results);
		assertEquals(results.size(), 57);
	}

	/**
	 * Intersection test
	 * 
	 * @throws DataException
	 */
	public void testIntersectLines() throws DataException {

		// FeatureSet feats = sLinesStore.getFeatureSet();
		// assertNotNull(feats);
		//		
		// List<ScoredFeature> listFeats = new ArrayList<ScoredFeature>();
		// for (Iterator<Feature> iterator = feats.iterator();
		// iterator.hasNext();) {
		// Feature fea = iterator.next();
		// ScoredFeature sFeat = new ScoredFeature();
		// sFeat.setFeature(fea);
		// listFeats.add(sFeat);
		// }
		// // group by attribute
		// FeatureAttributeDescriptor field =
		// sLinesStore.getDefaultFeatureType().getAttributeDescriptor("Campo1");
		// HashMap<String, List<ScoredFeature>> dissolAttributes = MatcherUtils
		// .groupScoredFeaturesByAttribute(field, listFeats);

	}

	public void testPositionOverLines() throws DataException {
		FeatureSet feats = sLinesStore.getFeatureSet();
		assertNotNull(feats);

		List<ScoredFeature> listFeats = new ArrayList<ScoredFeature>();
		for (Iterator<Feature> iterator = feats.iterator(); iterator.hasNext();) {
			Feature fea = iterator.next();
			ScoredFeature sFeat = new DefaultScoredFeature();
			sFeat.setFeature(fea);
			listFeats.add(sFeat);
		}

		for (ScoredFeature sFeat : listFeats) {
			Geometry geo = sFeat.getFeature().getDefaultGeometry();
			com.vividsolutions.jts.geom.Geometry geomJTS = Converter
					.geometryToJts(geo);
			Point pto = MatcherUtils.getLinePositionFromDistance(geomJTS, 1000);
			com.vividsolutions.jts.geom.Geometry ptoJTS = Converter
					.geometryToJts(pto);
			assertTrue(ptoJTS.intersects(geomJTS) || ptoJTS.touches(geomJTS));
		}
	}

	/**
	 * Calculate the relative position ( 50% of the geometry)
	 * 
	 * @throws DataException
	 */
	public void testRelativePositionOverLines() throws DataException {
		FeatureSet feats = sLinesStore.getFeatureSet();
		assertNotNull(feats);

		List<ScoredFeature> listFeats = new ArrayList<ScoredFeature>();
		for (Iterator<Feature> iterator = feats.iterator(); iterator.hasNext();) {
			Feature fea = iterator.next();
			ScoredFeature sFeat = new DefaultScoredFeature();
			sFeat.setFeature(fea);
			listFeats.add(sFeat);
		}

		for (ScoredFeature sFeat : listFeats) {
			Geometry geo = sFeat.getFeature().getDefaultGeometry();
			com.vividsolutions.jts.geom.Geometry geomJTS = Converter
					.geometryToJts(geo);
			Point pto = MatcherUtils.getLinePositionFromRelativeDistance(
					geomJTS, 50);
			com.vividsolutions.jts.geom.Geometry ptoJTS = Converter
					.geometryToJts(pto);
			assertTrue(ptoJTS.intersects(geomJTS) || ptoJTS.touches(geomJTS));
		}

	}

}

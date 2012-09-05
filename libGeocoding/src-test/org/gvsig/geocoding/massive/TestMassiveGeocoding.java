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

package org.gvsig.geocoding.massive;

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
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFNewStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreProvider;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.fmap.dal.store.shp.SHPNewStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPStoreProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLibrary;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.geocoding.Geocoder;
import org.gvsig.geocoding.GeocodingLibrary;
import org.gvsig.geocoding.GeocodingLocator;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.AddressComponent;
import org.gvsig.geocoding.address.ComposedAddress;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.impl.DefaultAddress;
import org.gvsig.geocoding.address.impl.DefaultAddressComponent;
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
import org.gvsig.geocoding.styles.impl.SimpleCentroid;
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

public class TestMassiveGeocoding extends TestCase {

	private static final Logger log = LoggerFactory
			.getLogger(TestMassiveGeocoding.class);

	private DataManager dataManager = null;
	private FeatureStore masStore = null;
	private FeatureStore streetsStore = null;

	private Patterngeocoding pat = null;
	private List<Set<GeocodingResult>> listResults;

	public TestMassiveGeocoding() {
		listResults = new ArrayList<Set<GeocodingResult>>();

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
			File prov = new File("./test-data/massive/streetsmassive.dbf");
			assertTrue(prov.exists());
			DBFStoreParameters provParam = (DBFStoreParameters) dataManager
					.createStoreParameters(DBFStoreProvider.NAME);
			provParam.setFile(prov);
			masStore = (FeatureStore) dataManager.createStore(provParam);
			assertNotNull(masStore);
		} catch (Exception e) {
			log.error("Getting massivestreets.dbf store", e);
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

		// Get ppatern
		pat = getPattern();
	}

	/**
	 * setUP
	 */
	public void setUp() {

	}

	/**
	 * tearDown
	 */
	public void tearDown() {

	}

	/**
	 * geocoding Massive
	 * 
	 * @throws DataException
	 * @throws CreateGeometryException
	 */
	public void testMassive() throws DataException, CreateGeometryException {

		Geocoder geocoder = GeocodingLocator.getInstance().getGeocoder();
		((DataGeocoderImpl) geocoder).setPattern(pat);

		// Get addresses
		List<Address> addresses = getAddresses();

		for (Address addr : addresses) {
			Set<GeocodingResult> results = null;
			try {
				results = geocoder.geocode(addr);
			} catch (Exception e) {
				log.error("Geocoding", e);
			}
			listResults.add(results);
		}
		assertEquals(6, listResults.size());

		// Create DBF file
		FeatureStore allResults = createDBFAllResults();

		// Create Layer select results
		FeatureStore selResults = createGeocodingResultsLayer();
		fillSelectedResultsStore(selResults, allResults);
	}

	/**
	 * geocoding Massive
	 * 
	 * @throws DataException
	 */
	private FeatureStore createDBFAllResults() throws DataException {

		// File dbfFile = File.createTempFile("allresults", "dbf", new
		// File("c:/geoco"));

		File dbfFile = new File("c:/geoco/allresults.dbf");

		FeatureStore store = null;
		DataManager manager = DALLocator.getDataManager();

		if (dbfFile != null) {
			// build DBF store
			File parentfile = dbfFile.getParentFile();

			FilesystemServerExplorerParameters exParam = null;
			try {
				exParam = (FilesystemServerExplorerParameters) manager
						.createServerExplorerParameters(FilesystemServerExplorer.NAME);

				exParam.setRoot(parentfile.getAbsolutePath());

				DataServerExplorer serverExplorer = manager
						.createServerExplorer(exParam);

				NewDataStoreParameters newParams = serverExplorer
						.getAddParameters(DBFStoreProvider.NAME);
				((FilesystemStoreParameters) newParams).setFile(dbfFile);
				EditableFeatureType featureType = generateFeatureTypeAllResults(store, listResults
						.get(0));
				((DBFNewStoreParameters) newParams)
						.setDefaultFeatureType(featureType);
				serverExplorer.add(newParams, true);

				// fill DBF store
				int max = 10;

				DBFStoreParameters dbfParameters = (DBFStoreParameters) manager
						.createStoreParameters(DBFStoreProvider.NAME);
				dbfParameters.setFile(dbfFile);

				store = (FeatureStore) manager.createStore(dbfParameters);
				store.edit();

				int id = 0;
				int idres = 0;

				Integer[] sels = { 0, 0, 0, 0, 0, 0 };
				for (Set<GeocodingResult> set : listResults) {
					int sel = sels[idres];
					int i = 0;
					int selec = 0;
					for (GeocodingResult res : set) {
						if (i >= max) {
							break;
						}
						EditableFeature feature = store.createNewFeature();
						// FIRST fields
						feature.setInt("gID", id);
						feature.setInt("gID_RESUL", idres);
						if (selec == sel) {
							feature.setInt("gSELECT", 1);
						} else {
							feature.setInt("gSELECT", 0);
						}
						feature.setDouble("gSCORE", res.getScore());
						Point pto = (Point) res.getGeometry();
						feature.setDouble("gX", pto.getX());
						feature.setDouble("gY", pto.getY());
						// Dynamic address fields
						List<AddressComponent> comps = getListAddressElements(res
								.getAddress());
						for (AddressComponent comp : comps) {
							feature.setString(comp.getKeyElement(), comp
									.getValue());
						}
						store.insert(feature);
						id++;
						i++;
						selec++;
					}
					idres++;
				}
				store.finishEditing();
				store.dispose();

			} catch (Exception e) {
				log.error("Creating dbf store", e);
			}
		}
		assertTrue(dbfFile.exists());
		return store;
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
			pat.setPatternName("SimpleCentroidLines");

			GeocodingSource source = new DefaultGeocodingSource();
			source.setLayerSource(streetsStore);

			AbstractGeocodingStyle style = new SimpleCentroid();
			FeatureType ftype = streetsStore.getDefaultFeatureType();
			Literal relations = new DefaultLiteral();
			relations.add(new DefaultRelationsComponent("Calle","STREET_NAM"));

			style.setRelationsLiteral(relations);
			source.setStyle(style);

			pat.setSource(source);

		} catch (Exception e) {
			log.error("Building a pattern", e);
		}

		return pat;
	}

	private List<Address> getAddresses() {
		List<Address> list = new ArrayList<Address>();
		try {
			FeatureSet features = masStore.getFeatureSet();
			for (int i = 0; i < features.getSize(); i++) {
				list.add(getTableAddress(i));
			}
		} catch (Exception e) {
			log.error("", e);
		}

		return list;

	}

	public Address getTableAddress(int row) {

		Address address = null;

		try {
			FeatureType type = masStore.getDefaultFeatureType();
			FeatureAttributeDescriptor nameDesc = type
					.getAttributeDescriptor("tcalle");

			// Get store from Gmodel and the feature
			FeatureSet features = null;
			Feature feature = null;

			features = masStore.getFeatureSet();
			Iterator<Feature> it = features.iterator(row);
			feature = it.next();

			// Create the address
			address = new DefaultAddress();
			DefaultLiteral literal = new DefaultLiteral();

			for (int i = 0; i < features.getSize(); i++) {
				if (row == i) {
					String key = "calle";
					Object obj = feature.get(nameDesc.getName());
					String value = obj.toString();
					literal.add(new DefaultAddressComponent(key, value));
				}

			}
			address.setMainLiteral(literal);

		} catch (DataException e) {
			log.error("Get the feature of FeatureStore", e);
		}

		return address;
	}

	/**
	 * Build feature type dbf all results
	 * 
	 * @return
	 * @throws DataException 
	 */
	private EditableFeatureType generateFeatureTypeAllResults(FeatureStore store,
			Set<GeocodingResult> result) throws DataException {

		boolean compose = false;
		boolean cross = true;

		// address fields
		Address address = null;
		for (GeocodingResult res : result) {
			address = res.getAddress();
			break;
		}
		Literal mainLiteral = address.getMainLiteral();
		int cant = mainLiteral.size();
		String[] fields = new String[cant];
		int i = 0;
		for (Object obj : mainLiteral) {
			AddressComponent aComp = (AddressComponent) obj;
			fields[i] = aComp.getKeyElement();
			i++;
		}
		if (address instanceof ComposedAddress) {
			compose = true;
			ComposedAddress caddress = (ComposedAddress) address;
			List<Literal> inter = caddress.getIntersectionLiterals();
			if (inter.size() == 2) {
				cross = false;
			}
		}

		EditableFeatureType eFType = store.getDefaultFeatureType().getEditable();
		eFType.add("gID", DataTypes.INT).setSize(10);
		eFType.add("gID_RESUL", DataTypes.INT).setSize(10);
		eFType.add("gSELECT", DataTypes.INT).setSize(10);
		eFType.add("gSCORE", DataTypes.DOUBLE).setSize(10);
		eFType.add("gX", DataTypes.DOUBLE).setSize(14);
		eFType.add("gY", DataTypes.DOUBLE).setSize(14);

		// no compose address
		if (!compose) {
			for (String field : fields) {
				eFType.add(field, DataTypes.STRING).setSize(20);
			}
		}
		// compose address
		else {
			for (String field : fields) {
				eFType.add(field + "_1", DataTypes.STRING).setSize(20);
			}
			for (String field : fields) {
				eFType.add(field + "_2", DataTypes.STRING).setSize(20);
			}
			// compose between address
			if (!cross) {
				for (String field : fields) {
					eFType.add(field + "_3", DataTypes.STRING).setSize(20);
				}
			}
		}
		return eFType;
	}

	/**
	 * get list address elements
	 * 
	 * @param address
	 * @return
	 */
	private List<AddressComponent> getListAddressElements(Address address) {

		List<AddressComponent> comps = new ArrayList<AddressComponent>();
		Literal mainLiteral = address.getMainLiteral();

		for (Object obj : mainLiteral) {
			AddressComponent aComp = (AddressComponent) obj;
			comps.add(aComp);
		}
		if (address instanceof ComposedAddress) {
			ComposedAddress caddress = (ComposedAddress) address;
			List<Literal> inter = caddress.getIntersectionLiterals();
			if (inter.size() == 1) {
				Literal seclit = inter.get(0);
				for (Object obj : seclit) {
					AddressComponent aComp = (AddressComponent) obj;
					comps.add(aComp);
				}
			}
			if (inter.size() == 2) {
				Literal thilit = inter.get(1);
				for (Object obj : thilit) {
					AddressComponent aComp = (AddressComponent) obj;
					comps.add(aComp);
				}
			}
		}
		return comps;
	}

	/**
	 * 
	 * @param shpFile
	 * @return
	 */
	private FeatureStore createGeocodingResultsLayer() {

		FeatureStore lyrstore = null;

		// File shpFile = File.createTempFile("shpresults", "shp", new
		// File("c:/geoco"));

		File shpFile = new File("c:/geoco/shpresults.shp");

		FLyrVect lyr = null;
		FilesystemServerExplorerParameters explorerParam = null;

		DataManager manager = DALLocator.getDataManager();

		File directory = shpFile.getParentFile();

		try {
			explorerParam = (FilesystemServerExplorerParameters) manager
					.createServerExplorerParameters(FilesystemServerExplorer.NAME);
			explorerParam.setRoot(directory.getAbsolutePath());

			DataServerExplorer serverExplorer = manager
					.createServerExplorer(explorerParam);

			SHPNewStoreParameters newShpParams = (SHPNewStoreParameters) serverExplorer
					.getAddParameters(SHPStoreProvider.NAME);
			newShpParams.setSHPFileName(shpFile.getName());
			newShpParams.setFile(shpFile);

			EditableFeatureType featureType = generateFeatureTypeSelectedResult(lyrstore);
			newShpParams.setDefaultFeatureType(featureType);
			serverExplorer.add(newShpParams, true);

			lyrstore = (FeatureStore) dataManager.createStore(newShpParams);

			// LayerFactory factory = LayerFactory.getInstance();
			// String name = shpFile.getName();
			// lyr = (FLyrVect) factory.createLayer(name, newShpParams);
			// lyrstore = lyr.getFeatureStore();

		} catch (Exception e) {
			log.error("ERROR building FLayer");
		}

		return lyrstore;
	}

	/**
	 * Build feature type of the selected results store
	 * 
	 * @return
	 * @throws DataException 
	 */
	private EditableFeatureType generateFeatureTypeSelectedResult(FeatureStore store) throws DataException {

		EditableFeatureType eFType = store.getDefaultFeatureType().getEditable();

		eFType.add("gID_RESUL", DataTypes.INT).setSize(10);
		eFType.add("id", DataTypes.STRING).setSize(10);
		eFType.add("gSCORE", DataTypes.DOUBLE).setSize(10);
		eFType.add("gX", DataTypes.DOUBLE).setSize(14);
		eFType.add("gY", DataTypes.DOUBLE).setSize(14);
		// GEOM field
		eFType.add("gGEOMETRY", DataTypes.GEOMETRY).setSize(14);
		eFType.setDefaultGeometryAttributeName("gGEOMETRY");

		return eFType;
	}

	/**
	 * Fill selected results store
	 * 
	 * @param newstore
	 * @throws DataException
	 * @throws CreateGeometryException
	 */
	private void fillSelectedResultsStore(FeatureStore newstore,
			FeatureStore allResults) throws DataException,
			CreateGeometryException {

		Integer[] sels = { 0, 0, 0, 0, 0, 0 };

		newstore.edit();
		int i = 0;
		for (Set<GeocodingResult> set : listResults) {
			int sel = sels[i];

			GeocodingResult resu = null;
			int j = 0;
			for (GeocodingResult res : set) {
				if (j == sel) {
					resu = res;
					break;
				}
				j++;
			}

			EditableFeature feature = newstore.createNewFeature();

			String fieldname = "id";
			// feature.setInt(GeocodingTags.gID, i);

			if (resu != null) {

				FeatureSet feats = allResults.getFeatureSet();
				Iterator<Feature> it = feats.iterator(i);
				Feature tableFeat = it.next();
				Object obj = tableFeat.get(fieldname);

				feature.setInt("gID_RESUL", i);

				feature.set(fieldname, obj);

				feature.setDouble("gSCORE", resu.getScore());
				Point pto = (Point) resu.getGeometry();
				feature.setDouble("gX", pto.getX());
				feature.setDouble("gY", pto.getY());
				GeometryManager geomManager = GeometryLocator
						.getGeometryManager();
				Point geom = (Point) geomManager.createPoint(pto.getX(), pto
						.getY(), Geometry.SUBTYPES.GEOM2D);

				feature.setGeometry("gGEOMETRY", geom);
			}
			newstore.insert(feature);
			i++;
		}
		newstore.finishEditing();
		newstore.dispose();
	}

}

package org.gvsig.geocoding.massive;

import java.io.File;

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
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.store.dbf.DBFNewStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreProvider;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
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
public class NewDBFTest extends TestCase {

	private Logger log = LoggerFactory.getLogger(NewDBFTest.class);
	private DataManager manager = null;
	private FeatureStore store = null;

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

		Library shpLib = new SHPLibrary();
		shpLib.initialize();
		shpLib.postInitialize();

		manager = DALLocator.getDataManager();

	}

	/**
	 * tearDown
	 */
	protected void tearDown() throws Exception {

	}

	/**
	 * Simple search
	 * 
	 * @throws DataException
	 */
	public void testNewDBFFile() throws DataException {

		File dbfFile = null;
		dbfFile = new File("c:/midbf1.dbf");

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

				NewDataStoreParameters newDBFParams = serverExplorer
						.getAddParameters(DBFStoreProvider.NAME);
				((DBFNewStoreParameters) newDBFParams).setDBFFile(dbfFile);
				EditableFeatureType featureType = generateFeatureTypeAllResults(newDBFParams);
				((DBFNewStoreParameters) newDBFParams)
						.setDefaultFeatureType(featureType);

				serverExplorer.add(newDBFParams, true);

				DBFStoreParameters dbfParameters = (DBFStoreParameters) manager
						.createStoreParameters(DBFStoreProvider.NAME);
				dbfParameters.setFile(dbfFile);

				store = (FeatureStore) manager.createStore(dbfParameters);
				store.edit();

				EditableFeature feat1 = store.createNewFeature();
				feat1.setInt("CAMPO1", 5);
				feat1.setDouble("CAMPO2", 5.658);
				store.insert(feat1);
				
				EditableFeature feat2 = store.createNewFeature();
				feat2.setInt("CAMPO1", 4);
				feat2.setDouble("CAMPO2", 2.876);
				store.insert(feat2);

				store.finishEditing();
				store.dispose();

			} catch (Exception e) {
				log.error("Creating dbf store", e);
			}
		}
		// ASSERTS

	}

	/**
	 * Build feature type dbf all results
	 * 
	 * @return
	 */
	private EditableFeatureType generateFeatureTypeAllResults(
			NewDataStoreParameters params) {

		EditableFeatureType eFType = (EditableFeatureType) ((NewFeatureStoreParameters) params)
				.getDefaultFeatureType();
		eFType.add("CAMPO1", DataTypes.INT).setSize(10);
		eFType.add("CAMPO2", DataTypes.DOUBLE).setSize(10);

		return eFType;
	}
}

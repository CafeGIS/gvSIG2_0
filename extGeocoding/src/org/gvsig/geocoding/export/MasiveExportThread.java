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
 * 2009 Prodevelop S.L  main developer
 */

package org.gvsig.geocoding.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.feature.impl.DefaultEditableFeatureType;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreProvider;
import org.gvsig.fmap.dal.store.shp.SHPNewStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPStoreProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.AddressComponent;
import org.gvsig.geocoding.address.ComposedAddress;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.extension.GeocodingController;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.utils.GeocodingTags;
import org.gvsig.geocoding.utils.GeocodingUtils;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.ProjectFactory;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.GenericFileFilter;

/**
 * This class generates a Layer with geocoding results and dbf file with all
 * results
 * 
 * @author Jorge Gaspar Sanz Salinas (jsanz@prodevelop.es)
 * @author Vicente Sanjaime Calvet (vsanjaime@prodevelop.es)
 * 
 */

public class MasiveExportThread extends Thread {

	private Logger log = LoggerFactory.getLogger(MasiveExportThread.class);
	private GeocodingController control = null;

	/**
	 * Contructor
	 * 
	 * @param control
	 */
	public MasiveExportThread(GeocodingController control) {
		super();
		this.control = control;

	}

	/**
	 * Run export process
	 */
	public void run() {
		// get file name
		File exportFile = getFilesExportResults();
		if (exportFile != null) {
			// Build dbf name
			File suffixfile = GeocodingUtils.addSuffixFile(exportFile,
					GeocodingTags.GEOCODINGALLRESULTS);
			File dbfFile = GeocodingUtils.addDBFSuffix(suffixfile);
			// Create DBF Store Results
			FeatureStore dbfStore = createDBFStoreAllResults(dbfFile);
			// Load layer in gvSIG proyect
			FeatureTableDocument table = loadTableAllResults(dbfStore, dbfFile);
			// Build shp name
			File shpFile = GeocodingUtils.addSHPSuffix(exportFile);
			// create layer
			FLyrVect flyr = createGeocodingResultsLayer(shpFile);
			if (flyr != null) {
				try {
					fillStoreWithSelectedResults(flyr.getFeatureStore());
				} catch (Exception e) {
					log.error("filling store gocoding layer", e);
				}
				// relate table all results to layer
				flyr.setProperty(GeocodingTags.GEOCODINGPROPERTY, table);
				// add layer to view
				View view = control.getCurrentView();
				FLayers lyrs = view.getMapControl().getMapContext().getLayers();
				lyrs.addLayer(flyr);
			}
		}
	}

	/**
	 * Create dbf store all results
	 * 
	 * @param dbfFile
	 * @return store
	 */
	private FeatureStore createDBFStoreAllResults(File dbfFile) {

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
				EditableFeatureType featureType = generateFeatureTypeAllResults();
				((NewFeatureStoreParameters) newParams)
						.setDefaultFeatureType(featureType);
				serverExplorer.add(newParams, true);

				// fill DBF store
				int max = control.getGmodel().getPattern().getSettings()
						.getResultsNumber();

				DBFStoreParameters dbfParameters = (DBFStoreParameters) manager
						.createStoreParameters(DBFStoreProvider.NAME);
				dbfParameters.setFile(dbfFile);

				store = (FeatureStore) manager.createStore(dbfParameters);
				store.edit();

				List<Set<GeocodingResult>> results = control.getGmodel()
						.getAllResults();

				int id = 0;
				int idres = 0;

				Integer[] sels = control.getGmodel().getExportElements();
				for (Set<GeocodingResult> set : results) {
					int sel = sels[idres];
					int i = 0;
					int selec = 0;
					for (GeocodingResult res : set) {
						if (i >= max) {
							break;
						}
						EditableFeature feature = store.createNewFeature();
						// FIRST fields
						feature.setInt(GeocodingTags.gID, id);
						feature.setInt(GeocodingTags.gID_RESUL, idres);
						if (selec == sel) {
							feature.setInt(GeocodingTags.gSELECT, 1);
						} else {
							feature.setInt(GeocodingTags.gSELECT, 0);
						}
						feature.setDouble(GeocodingTags.gSCORE, res.getScore());
						Point pto = (Point) res.getGeometry();
						feature.setDouble(GeocodingTags.gX, pto.getX());
						feature.setDouble(GeocodingTags.gY, pto.getY());
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

			} catch (Exception e) {
				log.error("Creating dbf store", e);
			}
		}
		return store;
	}

	/**
	 * Build feature type dbf all results
	 * 
	 * @return
	 */
	private EditableFeatureType generateFeatureTypeAllResults() {

		boolean compose = false;
		boolean cross = true;

		// address fields
		Set<GeocodingResult> result = control.getGmodel().getAllResults()
				.get(0);
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

		EditableFeatureType eFType = new DefaultEditableFeatureType();
		eFType.add(GeocodingTags.gID, DataTypes.INT).setSize(10);
		eFType.add(GeocodingTags.gID_RESUL, DataTypes.INT).setSize(10);
		eFType.add(GeocodingTags.gSELECT, DataTypes.INT).setSize(10);
		eFType.add(GeocodingTags.gSCORE, DataTypes.DOUBLE).setSize(10);
		eFType.add(GeocodingTags.gX, DataTypes.DOUBLE).setSize(14);
		eFType.add(GeocodingTags.gY, DataTypes.DOUBLE).setSize(14);

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
	 * Load in gvSIG the dbf with all results
	 * 
	 * @param store
	 * @param file
	 * @return
	 */
	private FeatureTableDocument loadTableAllResults(FeatureStore store,
			File file) {
		Project project = ((ProjectExtension) PluginServices
				.getExtension(ProjectExtension.class)).getProject();

		FeatureTableDocument table = ProjectFactory.createTable(file.getName(),
				store);

		project.addDocument(table);

		// IWindow window = table.createWindow();
		// if (window == null) {
		// JOptionPane.showMessageDialog((Component) PluginServices
		// .getMainFrame(), PluginServices.getText(this,
		// "error_opening_the_document"));
		// return null;
		// }
		// PluginServices.getMDIManager().addWindow(window);
		return table;
	}

	/**
	 * Get the file to export the geocoding results process
	 * 
	 * @return
	 */
	private File getFilesExportResults() {
		File thefile = null;
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(PluginServices.getText(this, "exportresults"));
		String[] extensions = { "shp" };
		jfc.setCurrentDirectory(new File(""));
		jfc.addChoosableFileFilter(new GenericFileFilter(extensions,
				PluginServices.getText(this, "shp_file")));
		int returnval = jfc.showSaveDialog((java.awt.Component) PluginServices
				.getMainFrame());
		if (returnval == JFileChooser.APPROVE_OPTION) {
			thefile = jfc.getSelectedFile();
			log.debug("file opened: " + thefile);
		} else {
			return null;
		}
		if (thefile != null) {
			thefile = GeocodingUtils.delSHPSuffix(thefile);
		}
		return thefile;
	}

	/**
	 * Fill selected results store
	 * 
	 * @param newstore
	 * @throws DataException
	 * @throws CreateGeometryException
	 */
	private void fillStoreWithSelectedResults(FeatureStore newstore)
			throws DataException, CreateGeometryException {

		Integer[] sels = control.getGmodel().getExportElements();
		List<Set<GeocodingResult>> results = control.getGmodel()
				.getAllResults();

		FeatureStore table = control.getGmodel().getSelectedTableStore();

		newstore.edit();
		int i = 0;
		for (Set<GeocodingResult> set : results) {
			int sel = sels[i];
			GeocodingResult result = null;
			int j = 0;
			for (GeocodingResult res : set) {
				if (j == sel) {
					result = res;
					break;
				}
				j++;
			}

			EditableFeature feature = newstore.createNewFeature();
			String fieldname = control.getGmodel().getIdMasiveTable();

			if (result != null) {

				FeatureSet feats = table.getFeatureSet();
				Iterator<Feature> it = feats.iterator(i);
				Feature tableFeat = it.next();
				Object obj = tableFeat.get(fieldname);      
				
				Point pto = (Point) result.getGeometry();
				//fill fields
				feature.setInt(GeocodingTags.gID_RESUL, i);
				feature.set(fieldname, obj);
				feature.setDouble(GeocodingTags.gSCORE, result.getScore());				
				feature.setDouble(GeocodingTags.gX, pto.getX());
				feature.setDouble(GeocodingTags.gY, pto.getY());
				feature.setGeometry(GeocodingTags.gGEOMETRY, pto);
			}
			newstore.insert(feature);
			i++;
		}
		newstore.dispose();
		newstore.finishEditing();		
	}

	/**
	 * Build layer with selected geocoding results
	 * 
	 * @param shpFile
	 * @return
	 */
	private FLyrVect createGeocodingResultsLayer(File shpFile) {

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
			newShpParams.setFile(shpFile);
			String proj = control.getCurrentView().getProjection().getAbrev();
			newShpParams.setSRSID(proj);
			newShpParams.setDynValue("SRSOriginalParameters", proj);

			EditableFeatureType featureType = generateFeatureTypeSelectedResult();
			newShpParams.setDefaultFeatureType(featureType);
			serverExplorer.add(newShpParams, true);

			LayerFactory factory = LayerFactory.getInstance();
			String name = shpFile.getName();
			lyr = (FLyrVect) factory.createLayer(name, newShpParams);
					
			
			
		} catch (Exception e) {
			log.error("ERROR building FLayer");
		}

		return lyr;
	}

	/**
	 * Build feature type of the selected results store
	 * 
	 * @return
	 * @throws DataException
	 */
	private EditableFeatureType generateFeatureTypeSelectedResult()
			throws DataException {

		String fname = control.getGmodel().getIdMasiveTable();

		FeatureStore table = control.getGmodel().getSelectedTableStore();

		FeatureAttributeDescriptor desc = table.getDefaultFeatureType()
				.getAttributeDescriptor(fname);

		EditableFeatureType eFType = new DefaultEditableFeatureType();

		eFType.add(GeocodingTags.gID_RESUL, DataTypes.INT).setSize(10);
		eFType.add(fname, desc.getDataType()).setSize(desc.getSize());
		eFType.add(GeocodingTags.gSCORE, DataTypes.DOUBLE).setSize(10);
		eFType.add(GeocodingTags.gX, DataTypes.DOUBLE).setSize(14);
		eFType.add(GeocodingTags.gY, DataTypes.DOUBLE).setSize(14);
		// GEOM field
		eFType.add(GeocodingTags.gGEOMETRY, DataTypes.GEOMETRY);
		eFType.setDefaultGeometryAttributeName(GeocodingTags.gGEOMETRY);		

		return eFType;
	}

}

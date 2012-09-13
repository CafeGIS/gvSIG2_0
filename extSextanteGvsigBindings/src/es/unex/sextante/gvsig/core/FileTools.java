package es.unex.sextante.gvsig.core;

import java.io.File;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.store.dbf.DBFStoreParameters;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.raster.dataset.RasterDataset;

import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.ProjectFactory;


public class FileTools {

	public final static String [] RASTER_EXT_IN = {"tif","asc", "dat", "tiff", "bmp", "gif", "img", "jpg", "png", "vrt",
													"lan", "gis", "pix", "aux", "adf", "mpr", "mpl", "map", "hdr"};
	public final static String [] RASTER_DRIVERS_IN = {"gvSIG Image Driver", "gvSIG Image Driver",
														"gvSIG Image Driver", "gvSIG Image Driver",
														"gvSIG Image Driver", "gvSIG Image Driver",
														"gvSIG Image Driver", "gvSIG Image Driver",
														"gvSIG Image Driver", "gvSIG Image Driver",
														"gvSIG Image Driver", "gvSIG Image Driver",
														"gvSIG Image Driver", "gvSIG Image Driver",
														"gvSIG Image Driver", "gvSIG Image Driver",
														"gvSIG Image Driver", "gvSIG Image Driver",
														"gvSIG Image Driver"
														};

	public final static String [] VECTOR_EXT_IN = {"shp","gml","dxf","dgn","dwg"};
	public final static String [] VECTOR_DRIVERS_IN = {"gvSIG shp driver",
													"gvSIG GML Memory Driver", "gvSIG DXF Memory Driver",
													"gvSIG DGN Memory Driver", "gvSIG DWG Memory Driver"};
	public final static String [] TABLE_EXT = {"dbf"};
	public static final String[] LAYERS_EXT_IN = {"tif","asc", "dat", "tiff", "bmp", "gif", "img", "jpg", "png", "vrt",
												"lan", "gis", "pix", "aux", "adf", "mpr", "mpl", "map", "shp","gml",
												"dxf","dgn","dwg"};
	public static final String[] LAYER_DRIVERS_IN = {"RasterStore", "RasterStore",
													"RasterStore", "RasterStore",
													"RasterStore", "RasterStore",
													"RasterStore", "RasterStore",
													"RasterStore", "RasterStore",
													"RasterStore", "RasterStore",
													"RasterStore", "RasterStore",
													"RasterStore", "RasterStore",
													"RasterStore", "RasterStore",
													"SHPStore",
													"GMLStore", "DXFStore",
													"DGNStore", "DWGStore"};

	public static FLayer openLayer(String sFilename, String sName, IProjection projection) {

		String sExtension = sFilename.substring(sFilename.lastIndexOf('.') + 1, sFilename.length());

		String[] extensionSupported = RasterDataset.getExtensionsSupported();
		FLyrRasterSE rlayer = null;
		for (int i = 0; i < extensionSupported.length; i++) {
			if(sExtension.equals(extensionSupported[i]))
				try {
					rlayer = FLyrRasterSE.createLayer(sName, new File(sFilename), projection);
				} catch (LoadLayerException e) {
					e.printStackTrace();
					return null;
				}
			if (rlayer != null && rlayer.isAvailable()){
				return rlayer;
			}
		}

		for (int i = 0; i < LAYERS_EXT_IN.length; i++) {
			if (sExtension.equals(LAYERS_EXT_IN[i])){
				try {
					FLayer layer;
//					Driver driver = LayerFactory.getDM().getDriver(FileTools.LAYER_DRIVERS_IN[i]);
					DataManager dm=DALLocator.getDataManager();
					DataStoreParameters params=dm.createStoreParameters(FileTools.LAYER_DRIVERS_IN[i]);
					((FilesystemStoreParameters)params).setFile(new File(sFilename));

					//FIXME ver si esto es necesario para todos los casos.
					if (params.hasDynValue("DefualtSRS")){
						params.setDynValue("DefaultSRS", projection);
					}
					layer = LayerFactory.getInstance().createLayer(sName, params);


					if (layer != null && layer.isAvailable()){
						return layer;
					}
					else{
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}

	public static FeatureTableDocument openTable(String sFilename, String sName) {
		DBFStoreParameters dbfParams=new DBFStoreParameters();
		dbfParams.setDBFFileName(sFilename);
		DataManager dm=DALLocator.getDataManager();
		try {
			FeatureStore store=(FeatureStore)dm.createStore(dbfParams);
			FeatureTableDocument pt = ProjectFactory.createTable(sName, store);
			return pt;

		} catch (Exception e) {
			return null;
		}

	}

	public static Object open(String sFilename){

		FLayer layer = openLayer(sFilename, sFilename, Project.getDefaultProjection());
		if (layer != null){
			return layer;
		}

		FeatureTableDocument table = openTable(sFilename, sFilename);

		return table;


	}

}

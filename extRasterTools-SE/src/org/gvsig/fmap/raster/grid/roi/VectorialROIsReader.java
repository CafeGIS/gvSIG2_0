package org.gvsig.fmap.raster.grid.roi;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.raster.dataset.FileNotExistsException;
import org.gvsig.raster.grid.Grid;


public class VectorialROIsReader {

	private String 			filename 			= null;
	private IProjection 	projection 			= null;
	private FLyrVect		fLyrVect			= null;
	private HashMap			rois				= null;
	private Grid			grid				= null;


	public VectorialROIsReader(String filename, Grid grid, IProjection projection) throws LoadLayerException, FileNotExistsException {
		this.filename = filename;
		this.projection = projection;
		this.grid = grid;
		File file = new File(filename);
		if(file.exists()){
			DataManager datamanager=DALLocator.getDataManager();
			SHPStoreParameters params=null;
			try {
				params = (SHPStoreParameters)datamanager.createStoreParameters(SHPStoreParameters.DYNCLASS_NAME);
				params.setSRS(projection);
			} catch (InitializeException e) {
				throw new LoadLayerException(file.getName(),e);
			} catch (ProviderNotRegisteredException e) {
				throw new LoadLayerException(file.getName(),e);
			}
			params.setFile(file);
			fLyrVect = (FLyrVect)LayerFactory.getInstance().createLayer("layer1", params);
		}else
			throw new FileNotExistsException("file not found");
	}


	public ArrayList read(ArrayList existingROIs) throws InvalidROIsShpException, DataException{
		FeatureStore featureStore = fLyrVect.getFeatureStore();

		// Validación del .shp:
		FeatureType featureType=featureStore.getDefaultFeatureType();

		int nameFieldIndex = featureType.getIndex("name");
		int rFiledIndex = featureType.getIndex("R");
		int gFiledIndex = featureType.getIndex("G");
		int bFiledIndex = featureType.getIndex("B");

		int typeFieldIndex = featureType.getAttributeDescriptor(nameFieldIndex).getDataType();
		int typerFiledIndex = featureType.getAttributeDescriptor(rFiledIndex).getDataType();
		int typegFiledIndex = featureType.getAttributeDescriptor(gFiledIndex).getDataType();
		int typebFiledIndex = featureType.getAttributeDescriptor(bFiledIndex).getDataType();

		if (nameFieldIndex < 0 || rFiledIndex < 0 || gFiledIndex < 0 || bFiledIndex < 0)
			throw new InvalidROIsShpException("");

		if (typeFieldIndex != DataTypes.STRING ||
				typerFiledIndex != DataTypes.DOUBLE || typerFiledIndex != DataTypes.INT  ||
				typegFiledIndex != DataTypes.DOUBLE || typegFiledIndex != DataTypes.INT  ||
				typebFiledIndex != DataTypes.DOUBLE || typebFiledIndex != DataTypes.INT )
			throw new InvalidROIsShpException("");


		if (existingROIs != null)
		rois = new HashMap();
		if (existingROIs != null)
			for (int i = 0; i < existingROIs.size(); i++) {
				VectorialROI roi = (VectorialROI)existingROIs.get(i);
				rois.put(roi.getName(), roi);
			}
		String roiName;
		int r, g, b;
		FeatureSet set = null;
		DisposableIterator features = null;
		try {
			set = featureStore.getFeatureSet();
			features = set.iterator();
			while (features.hasNext()) {
				Feature feature = (Feature) features.next();

				// }
				// for (int i = 0; i<dataSource.getRowCount(); i++) {
				// IFeature feature = fLyrVect.getSource().getFeature(i);
				roiName = feature.getString(nameFieldIndex).toString();
				VectorialROI roi = null;
				if (!rois.containsKey(roiName)) {
					roi = new VectorialROI(grid);
					roi.setName(roiName);
					r = ((Number) feature.get(rFiledIndex)).intValue();
					g = ((Number) feature.get(gFiledIndex)).intValue();
					b = ((Number) feature.get(bFiledIndex)).intValue();
					roi.setColor(new Color(r, g, b));
					rois.put(roi.getName(), roi);
				} else
					roi = (VectorialROI) rois.get(roiName);
				roi.addGeometry(feature.getDefaultGeometry());
			}
		} finally {
			if (features != null)
				features.dispose();
			if (set != null)
				set.dispose();
		}
		return new ArrayList(rois.values());
	}
}

package org.gvsig.fmap.raster.grid.roi;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.feature.impl.DefaultEditableFeatureType;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.geom.Geometry;

/**
 * Clase dedicada a la escritura de ROIs en formato en disco (formato shape).
 *
 * Si las ROIs a escribir incluyen distintos tipos de geometrías la escritura se
 * realizará en distintos shape files, uno para poligonos, otro para polilíneas y
 * otro para puntos.
 *
 * @author Diego Guerrero (diego.guerrero@uclm.es)
 *
 */
public class VectorialROIsWriter {
	private String 			baseFilename 		= null;
	private IProjection 	projection 			= null;
	private int 			iPolygon;
	private int 			iPoint;
	private int 			iPolyline;
	private FeatureStore 	surfaceFS			= null;
	private FeatureStore 	pointFS				= null;
	private FeatureStore 	curveFS				= null;


	/**
	 * Constructor.
	 *
	 * @param baseFilename ruta base para formar los nombres de fichero (ruta/prefijo)
	 * @param projection CRS de las geometrías.
	 */
	public VectorialROIsWriter(String baseFilename, IProjection projection) {
		this.baseFilename = baseFilename;
		this.projection = projection;
	}

	/**
	 * Escribe el Array de ROIs pasado como parámetro.
	 *
	 * @param rois Array de VectorialROI
	 */
	public void write (VectorialROI rois[]){
		boolean monoType = true;
		int geometryType = -1;
		ArrayList geometries = null;

		Object values[] = new Object[4];

		if (baseFilename.endsWith(".shp"))
			baseFilename = baseFilename.replaceAll(".shp", "");

		for (int i = 0; i < rois.length; i++) {
			geometries = rois[i].getGeometries();
			for (Iterator iterator = geometries.iterator(); iterator.hasNext();) {
				if (geometryType < 0)
					geometryType = ((Geometry) iterator.next()).getType();
				else
					if (geometryType != ((Geometry) iterator.next()).getType()){
						monoType = false;
						break;
					}
			}
		}

		if (monoType){
			create(baseFilename+".shp", projection, geometryType);
		} else{
			create(baseFilename+"_polygons"+".shp", projection, Geometry.TYPES.SURFACE);
			create(baseFilename+"_points"+".shp", projection, Geometry.TYPES.POINT);
			create(baseFilename+"_polylines"+".shp", projection, Geometry.TYPES.CURVE);
		}

		for (int i = 0; i < rois.length; i++) {
			geometries = rois[i].getGeometries();
			values[0] = rois[i].getName();
			values[1] = new Integer(rois[i].getColor().getRed());
			values[2] = new Integer(rois[i].getColor().getGreen());
			values[3] = new Integer(rois[i].getColor().getBlue());


			for (Iterator iterator = geometries.iterator(); iterator.hasNext();) {
				Geometry geometry = (Geometry) iterator.next();
				addFeature(geometry, values);
			}
		}
		try {
			if (surfaceFS != null)
				surfaceFS.finishEditing();
			if (pointFS != null)
				pointFS.finishEditing();
			if (curveFS != null)
				curveFS.finishEditing();
		} catch (DataException e) {
			e.printStackTrace();
		}

	}

	private void addFeature(Geometry geom, Object[] values) {
		try {
			switch (geom.getType()) {
			case Geometry.TYPES.SURFACE:
				if (surfaceFS != null){
					EditableFeature ef=surfaceFS.createNewFeature();
					ef.set("name", values[0]);
					ef.set("R", values[1]);
					ef.set("G", values[2]);
					ef.set("B", values[3]);
					ef.set("GEOMETRY", geom);
					surfaceFS.insert(ef);
					iPolygon++;
				}
				break;

			case Geometry.TYPES.POINT:
				if (pointFS != null){
					EditableFeature ef=pointFS.createNewFeature();
					ef.set("name", values[0]);
					ef.set("R", values[1]);
					ef.set("G", values[2]);
					ef.set("B", values[3]);
					ef.set("GEOMETRY", geom);
					pointFS.insert(ef);
					iPoint++;
				}
				break;

			case Geometry.TYPES.CURVE:
				if (curveFS != null){
					EditableFeature ef=curveFS.createNewFeature();
					ef.set("name", values[0]);
					ef.set("R", values[1]);
					ef.set("G", values[2]);
					ef.set("B", values[3]);
					ef.set("GEOMETRY", geom);
					curveFS.insert(ef);
					iPolyline++;
				}
				break;

			default:
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void create(String filename, IProjection crs, int geometryType) {
		File newFile=new File(filename);
		try {
			EditableFeatureType featureType = getFeatureType();

			EditableFeatureAttributeDescriptor efad=featureType.add("GEOMETRY", DataTypes.GEOMETRY);
			efad.setGeometryType(geometryType);
			featureType.setDefaultGeometryAttributeName("GEOMETRY");


			DataManager datamanager=DALLocator.getDataManager();
			FilesystemServerExplorerParameters explorerParams;

			explorerParams = (FilesystemServerExplorerParameters) datamanager.createServerExplorerParameters(FilesystemServerExplorer.NAME);

			explorerParams.setRoot(newFile.getParent());
			FilesystemServerExplorer explorer=(FilesystemServerExplorer) datamanager.createServerExplorer(explorerParams);
			NewFeatureStoreParameters newParams = (NewFeatureStoreParameters) explorer.getAddParameters(newFile);


			newParams.setDefaultFeatureType(featureType);
			explorer.add(newParams, true);
			DataManager manager = DALLocator.getDataManager();

			switch (geometryType) {
			case Geometry.TYPES.SURFACE:
				surfaceFS=(FeatureStore) manager.createStore(newParams);
				surfaceFS.edit(FeatureStore.MODE_APPEND);
				break;

			case Geometry.TYPES.POINT:
				pointFS=(FeatureStore) manager.createStore(newParams);
				pointFS.edit(FeatureStore.MODE_APPEND);
				break;

			case Geometry.TYPES.CURVE:
				curveFS=(FeatureStore) manager.createStore(newParams);
				curveFS.edit(FeatureStore.MODE_APPEND);
				break;

			default:
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private EditableFeatureType getFeatureType(){
		EditableFeatureType eft=new DefaultEditableFeatureType();

		eft.add("name", DataTypes.STRING, 20);

		eft.add("R", DataTypes.INT);

		eft.add("G", DataTypes.INT);

		eft.add("B", DataTypes.INT);

		return eft;
	}
}

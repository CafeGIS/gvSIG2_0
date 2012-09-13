/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.rastertools.vectorizacion.process;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.sql.Types;

import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.feature.impl.DefaultEditableFeatureType;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.vectorization.VectorizationBinding;
import org.gvsig.rastertools.vectorizacion.fmap.BezierPathX;
/**
 * Este proceso vectoriza la capa de entrada que debe estar ya preprocesada.
 * Usa la libreria de potrace por debajo.
 *
 * @version 15/09/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PotraceProcess extends RasterProcess {
	private static final GeometryManager	geomManager 		  = GeometryLocator.getGeometryManager();
	private double       					percent               = 0;
	private FLyrRasterSE 					lyr                   = null;
	private String       					fileName              = null;
	private FeatureStore 					fsWriter              = null;
	private Object[]     					values                = null;
	private int          					m_iGeometry           = 0;

	// Default Values
	private int          					bezierPoints          = 7;
	private int          					policy                = VectorizationBinding.POLICY_MINORITY;
	private int          					despeckle             = 0;
	private double   					    cornerThreshold       = 1.0;
	private double      			        optimizationTolerance = 0.2;
	private int         					outputQuantization    = 10;
	private boolean      					curveOptimization     = true;


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.RasterProcess#init()
	 */
	public void init() {
		lyr = getLayerParam("layer");
		fileName = getStringParam("filename");
		policy = getIntParam("policy");
		bezierPoints = getIntParam("points");
		despeckle = getIntParam("despeckle");
		cornerThreshold = getDoubleParam("cornerThreshold");
		optimizationTolerance = getDoubleParam("optimizationTolerance");
		outputQuantization = getIntParam("outputQuantization");
		curveOptimization = getBooleanParam("curveoptimization");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.RasterProcess#process()
	 */
	public void process() throws InterruptedException {
		try {
			insertLineLog("Potrace");
			generatePotrace();
		} catch (DataException e) {
			RasterToolsUtil.messageBoxError("error_loading_store", this, e);
		} catch (CreateGeometryException e) {
			RasterToolsUtil.messageBoxError("error_closing_grid", this, e);
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError("error_closing_grid", this, e);
		} catch (ValidateDataParametersException e) {
			RasterToolsUtil.messageBoxError("error_loading_store", this, e);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			if (incrementableTask != null) {
				incrementableTask.processFinalize();
				incrementableTask = null;
			}
		}
		if (externalActions != null)
			externalActions.end(fileName);
	}

	/**
	 * Genera la capa vectorial a partir del raster de entrada
	 * 
	 * @param lyr
	 * @param fileOut
	 * @param bezierPoints
	 * @throws CreateGeometryException
	 * @throws ValidateDataParametersException
	 * @throws Exception
	 */
	public void generatePotrace() throws InterruptedException,
			RasterDriverException, DataException, CreateGeometryException,
			ValidateDataParametersException {
		VectorizationBinding binding = new VectorizationBinding(lyr.getBufferFactory());
		binding.setPolicy(policy);
		binding.setDespeckle(despeckle);
		binding.setCornerThreshold(cornerThreshold);
		binding.setOptimizationTolerance(optimizationTolerance);
		binding.setOutputQuantization(outputQuantization);
		binding.setEnabledCurveOptimization(curveOptimization);

		// binding.setCornerThreshold(-1);
		double geometrias[] = binding.VectorizeBuffer();

		String sFields[] = new String[2];
		sFields[0] = "ID";
		sFields[1] = fileName + "";
		
		EditableFeatureType featureType = getFeatureType(fileName);

		EditableFeatureAttributeDescriptor efad = featureType.add("GEOMETRY", DataTypes.GEOMETRY);
		efad.setGeometryType(Geometry.TYPES.CURVE);
		efad.setSRS(CRSFactory.getCRS("EPSG:23030"));
		featureType.setDefaultGeometryAttributeName("GEOMETRY");

		DataManager datamanager = DALLocator.getDataManager();
		FilesystemServerExplorerParameters explorerParams;

		explorerParams = (FilesystemServerExplorerParameters) datamanager.createServerExplorerParameters(FilesystemServerExplorer.NAME);

		explorerParams.setRoot(new File(fileName).getParent());
		FilesystemServerExplorer explorer = (FilesystemServerExplorer) datamanager.createServerExplorer(explorerParams);
		NewFeatureStoreParameters newParams = (NewFeatureStoreParameters) explorer.getAddParameters(new File(fileName));

		newParams.setDefaultFeatureType(featureType);
		newParams.setDynValue("srs", CRSFactory.getCRS("EPSG:23030"));
		explorer.add(newParams, true);
		DataManager manager = DALLocator.getDataManager();
		////////////////////////
		fsWriter = (FeatureStore)manager.createStore(newParams);
		fsWriter.edit(FeatureStore.MODE_APPEND);

		values = new Object[2];
		values[0] = new Double(0);
		values[1] = new Double(0);

		showPotrace(geometrias, bezierPoints);

		fsWriter.finishEditing();
	}

	public void addGeometry(Geometry geom, Object[] value) throws DataException {
		m_iGeometry++;
		EditableFeature ef = fsWriter.createNewFeature();
		ef.set("ID", value[0]);
		ef.set("NAME", value[1]);
		ef.setGeometry("GEOMETRY", geom);
		fsWriter.insert(ef);
	}

	private Point2D getTransformPixel(double x, double y) {
		AffineTransform at = lyr.getAffineTransform();
		Point2D ptSrc = new Point2D.Double(x, lyr.getPxHeight() - y);
		at.transform(ptSrc, ptSrc);
		return ptSrc;
	}

	private void showPotrace(double[] potraceX, int trozos) throws DataException, CreateGeometryException, InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		BezierPathX pathX = new BezierPathX(trozos);
		int ID = 0;
		double dZ;
		
		double increment = (100 / (double)potraceX.length);
		int cont = 1;
		while (true) {
			if ((cont >= potraceX.length) || (cont >= potraceX[0]))
				return;
			switch ((int) potraceX[cont]) {
				case 0: // MoveTo
					pathX.moveTo(getTransformPixel(potraceX[cont + 1], potraceX[cont + 2]));
					cont += 3;
					percent += (increment * 3);
					break;
				case 1: // LineTo
					pathX.lineTo(getTransformPixel(potraceX[cont + 1], potraceX[cont + 2]));
					cont += 3;
					percent += (increment * 3);
					break;
				case 2: // CurveTo
					pathX.curveTo(getTransformPixel(potraceX[cont + 1], potraceX[cont + 2]), getTransformPixel(potraceX[cont + 3], potraceX[cont + 4]), getTransformPixel(potraceX[cont + 5], potraceX[cont + 6]));
					cont += 7;
					percent += (increment * 7);
					break;
				case 3: // closePath
					Geometry line =  geomManager.createCurve(pathX, SUBTYPES.GEOM2D);
					ID ++;
					values[0] = new Double(ID);
					addGeometry(line, values);
					pathX = new BezierPathX(trozos);
					cont ++;
					percent += increment;
					break;
			}
			if(task.getEvent() != null)
				task.manageEvent(task.getEvent());
		}
	}
	private EditableFeatureType getFeatureType(String fileName){
		EditableFeatureType eft=new DefaultEditableFeatureType();

		EditableFeatureAttributeDescriptor efad1 = eft.add("ID", DataTypes.DOUBLE);
		efad1.setPrecision(5);
		EditableFeatureAttributeDescriptor efad2 = eft.add("NAME", DataTypes.DOUBLE);
		efad2.setPrecision(5);

		return eft;
	}
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		return (int) percent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return RasterToolsUtil.getText(this, "vectorization");
	}

	/**
	 * Returns the length of field
	 * @param dataType
	 * @return length of field
	 */
	public int getDataTypeLength(int dataType) {
		switch (dataType) {
		case Types.NUMERIC:
		case Types.DOUBLE:
		case Types.REAL:
		case Types.FLOAT:
		case Types.BIGINT:
		case Types.INTEGER:
		case Types.DECIMAL:
			return 20;
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			return 254;
		case Types.DATE:
			return 8;
		case Types.BOOLEAN:
		case Types.BIT:
			return 1;
		}
		return 0;
	}
}
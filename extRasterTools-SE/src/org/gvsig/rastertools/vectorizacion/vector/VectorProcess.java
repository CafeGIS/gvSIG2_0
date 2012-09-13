/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.vectorizacion.vector;

import java.io.File;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPStoreProvider;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.rastertools.vectorizacion.process.ContourLinesProcess;
import org.gvsig.rastertools.vectorizacion.process.PotraceProcess;

/**
 * Procesos necesarios para la funcionalidad de vectorización.
 *
 * 11/07/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class VectorProcess implements IProcessActions {
	private FLyrRasterSE                  sourceLayer       = null;
	private IProcessActions               endActions        = null;
	private String                        shapeName         = null;
	private IProjection                   proj              = null;

	/**
	 * Constructor que asigna el nombre de capa
	 * @param lyrPath
	 */
	public VectorProcess(FLyrRasterSE lyr, IProcessActions endActions, IProjection proj) {
		this.sourceLayer = lyr;
		this.endActions = endActions;
		this.proj = proj;
	}

	/**
	 * Aplica el proceso de recorte sobre el raster inicial
	 * @throws FilterTypeException
	 * @throws LoadLayerException
	 */
	public void contourLines(double distance) throws FilterTypeException, LoadLayerException {
		if(sourceLayer == null)
			return;

		RasterProcess contourLinesProcess = new ContourLinesProcess();
		contourLinesProcess.setActions(this);
		shapeName = RasterLibrary.getTemporalPath() + File.separator + RasterLibrary.usesOnlyLayerName() + ".shp";
		contourLinesProcess.addParam("filename", shapeName);
		contourLinesProcess.addParam("layer", sourceLayer);
		contourLinesProcess.addParam("min", new Double(0));
		contourLinesProcess.addParam("max", new Double(255));
		contourLinesProcess.addParam("distance", new Double(distance));
		contourLinesProcess.start();
	}

	/**
	 * Aplica el proceso para vectorizar con potrace
	 */
	public void potraceLines(int policy, int bezierPoints, int despeckle, double cornerThreshold, double optimizationTolerance, int outputQuantization, boolean curveOptimization) {
		PotraceProcess potraceProcess = new PotraceProcess();
		potraceProcess.setActions(this);
		shapeName = RasterLibrary.getTemporalPath() + File.separator + RasterLibrary.usesOnlyLayerName() + ".shp";
		potraceProcess.addParam("filename", shapeName);
		potraceProcess.addParam("layer", sourceLayer);
		potraceProcess.addParam("policy", new Integer(policy));
		potraceProcess.addParam("points", new Integer(bezierPoints));
		potraceProcess.addParam("despeckle", new Integer(despeckle));
		potraceProcess.addParam("cornerThreshold", new Double(cornerThreshold));
		potraceProcess.addParam("optimizationTolerance", new Double(optimizationTolerance));
		potraceProcess.addParam("outputQuantization", new Integer(outputQuantization));
		potraceProcess.addParam("curveoptimization", new Boolean(curveOptimization));
		potraceProcess.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object param) {
		FLayer shpLyr = null;
		try {
			String layerName = RasterUtilities.getFileNameFromCanonical(shapeName);
			DataManager datamanager=DALLocator.getDataManager();
			SHPStoreParameters params=null;
			try {
				params = (SHPStoreParameters)datamanager.createStoreParameters(SHPStoreProvider.NAME);
				params.setSRS(proj);
			} catch (InitializeException e) {
				throw new LoadLayerException(shapeName,e);
			} catch (ProviderNotRegisteredException e) {
				throw new LoadLayerException(shapeName,e);
			}
			params.setFile(shapeName);

			shpLyr = LayerFactory.getInstance().createLayer(layerName, params);
		} catch (LoadLayerException e) {
			RasterToolsUtil.messageBoxError("error_generating_layer", null, e);
		}
		if(endActions != null)
			endActions.end(new Object[]{this, shpLyr});
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#interrupted(java.lang.Object)
	 */
	public void interrupted() {
	}

	/**
	 * Asigna la capa raster a vectorizar
	 * @param lyr
	 */
	public void setSourceLayer(FLyrRasterSE lyr) {
		this.sourceLayer = lyr;
	}

	/**
	 * Asigna el interfaz para que el proceso ejectute las acciones de finalización
	 * al acabar.
	 * @param endActions
	 */
	public void setProcessActions(IProcessActions endActions) {
		this.endActions = endActions;
	}
}

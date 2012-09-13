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
package org.gvsig.rastertools.vectorizacion.clip;

import java.awt.geom.AffineTransform;
import java.io.File;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferInterpolation;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.process.ClippingProcess;

/**
 * Procesos necesarios para la funcionalidad de vectorización.
 * 
 * 11/07/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ClipProcess implements IProcessActions {
	private FLyrRasterSE                  sourceLayer       = null;
	private IProcessActions               endActions        = null;
	
	/**
	 * Asigna la clase IProcessActions para las acciones de
	 * finalización.
	 * @param pa
	 */
	public ClipProcess(IProcessActions endActions) {
		this.endActions = endActions;
	}
	
	/**
	 * Aplica el proceso de recorte sobre el raster inicial
	 * @throws FilterTypeException
	 */
	public void clip(ClipData data) throws FilterTypeException {
		RasterProcess clippingProcess = new ClippingProcess();
		clippingProcess.setActions(this);
		clippingProcess.addParam("realcoordinates", data.getWCCoordinates());
		String tempRaster = RasterLibrary.getTemporalPath() + File.separator + RasterLibrary.usesOnlyLayerName();
		clippingProcess.addParam("filename", tempRaster);
		clippingProcess.addParam("datawriter", new WriterBufferServer());
		clippingProcess.addParam("layer", sourceLayer);
		if(sourceLayer.getBandCount() > 1)
			clippingProcess.addParam("drawablebands", sourceLayer.getRenderBands());
		else
			clippingProcess.addParam("drawablebands", new int[]{0});
		clippingProcess.addParam("onelayerperband", new Boolean(false));
		clippingProcess.addParam("interpolationmethod", new Integer(BufferInterpolation.INTERPOLATION_Undefined));
		double newCellSize = sourceLayer.getCellSize() / data.getScaleSelectedValue();
		AffineTransform at = new AffineTransform(newCellSize, 0, 0, -newCellSize, data.getWCCoordinates()[0], data.getWCCoordinates()[1]);
		clippingProcess.addParam("affinetransform", at);
		if(sourceLayer.getBandCount() < 3)
			clippingProcess.addParam("colorInterpretation", DatasetColorInterpretation.createGrayInterpretation());
		else
			clippingProcess.addParam("colorInterpretation", DatasetColorInterpretation.createRGBInterpretation());
		double w = Math.abs((data.getWCCoordinates()[0] - data.getWCCoordinates()[2]) / sourceLayer.getCellSize());
		double h = Math.abs((data.getWCCoordinates()[1] - data.getWCCoordinates()[3]) / sourceLayer.getCellSize());
		clippingProcess.addParam("resolution", new int[]{(int) (w * data.getScaleSelectedValue()), (int) (h * data.getScaleSelectedValue())});
		clippingProcess.start();
	}
			
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object param) {
		FLyrRasterSE clip = null;
		try {
			if(param instanceof Object[] && ((Object[])param).length == 2 && ((Object[])param)[0] instanceof String) {
				clip = FLyrRasterSE.createLayer(RasterLibrary.getOnlyLayerName(), (String)((Object[])param)[0], sourceLayer.getProjection());
			}
		} catch (LoadLayerException e) {
			RasterToolsUtil.messageBoxError("error_generating_layer", null, e);
		}
		if(endActions != null)
			endActions.end(new Object[]{this, clip});
	}

	/**
	 * Asigna la capa fuente
	 * @param sourceLayer
	 */
	public void setSourceLayer(FLyrRasterSE sourceLayer) {
		this.sourceLayer = sourceLayer;
	}
	
	/**
	 * Asigna el interfaz para que el proceso ejectute las acciones de finalización
	 * al acabar.
	 * @param endActions
	 */
	public void setProcessActions(IProcessActions endActions) {
		this.endActions = endActions;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#interrupted(java.lang.Object)
	 */
	public void interrupted() {
	}
}

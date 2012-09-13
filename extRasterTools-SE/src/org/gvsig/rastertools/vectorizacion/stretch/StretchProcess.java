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
package org.gvsig.rastertools.vectorizacion.stretch;

import java.io.File;
import java.util.ArrayList;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.beans.previewbase.ParamStruct;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchEnhancementFilter;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.process.FilterProcess;

/**
 * Proceso para la generación de la imagen intermedia dividida en tramos.
 * 
 * 19/08/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class StretchProcess implements IProcessActions {
	private FLyrRasterSE                  sourceLayer       = null;
	private IProcessActions               endActions        = null;
	
	/**
	 * Asigna el objeto para informar que el proceso ha terminado
	 * @param endActions
	 */
	public StretchProcess(IProcessActions endActions) {
		this.endActions = endActions;
	}
	
	/**
	 * Aplica el proceso de filtrado sobre una capa dando como resultado otra capa
	 * @throws FilterTypeException
	 */
	public void stretchProcess(StretchPreviewRender prevRender, StretchData data ) throws FilterTypeException {
		if(sourceLayer == null)
			return;
		sourceLayer.setRenderBands(sourceLayer.getRenderBands());

		RasterProcess filterProcess = new FilterProcess();
		filterProcess.setActions(this);
		filterProcess.addParam("rendering", sourceLayer);
		String tempRaster = RasterLibrary.getTemporalPath() + File.separator + RasterLibrary.usesOnlyLayerName();
		filterProcess.addParam("filename", tempRaster + ".tif");
		filterProcess.addParam("rasterdatasource", sourceLayer.getDataSource());
		filterProcess.addParam("listfilterused", getParamStruct(sourceLayer, prevRender, data));
		filterProcess.addParam("onlyrenderbands", Boolean.TRUE);
		filterProcess.start();
	}
	
	/**
	 * Obtiene la lista de parámetros de los filtros añadidos
	 * @param lyr Capa raster
	 * @return ArrayList
	 */
	public ArrayList getParamStruct(FLyrRasterSE lyr, StretchPreviewRender prevRender, StretchData data) {
		RasterFilterList filterList = new RasterFilterList();
		filterList.setInitDataType(lyr.getDataType()[0]);
		RasterFilterListManager filterManager = new RasterFilterListManager(filterList);
		
		try {
			prevRender.addPosterization(filterManager, lyr);
		} catch (FilterTypeException e1) {
			RasterToolsUtil.messageBoxError(RasterToolsUtil.getText(null, "noposterization"), null, e1);
		}
		
		return getParams(filterList);
	}
	
	/**
	 * A partir de una lista de filtros devuelve un array con sus parámetros
	 * @param filterList
	 * @return ArrayList
	 */
	public ArrayList getParams(RasterFilterList filterList) {
		ArrayList listFilterUsed = new ArrayList();
		for (int i = 0; i < filterList.lenght(); i++) {
			try {
				RasterFilter filter = (RasterFilter)filterList.get(i);
				Params params = (Params) filter.getUIParams(filter.getName()).clone();
			
				ParamStruct newParam = new ParamStruct();
				Class c = null;
				if(filter instanceof LinearStretchEnhancementFilter)
					c = LinearStretchEnhancementFilter.class;
				newParam.setFilterClass(c);
				newParam.setFilterName(filter.getName());
				newParam.setFilterParam(params);
				listFilterUsed.add(newParam);
			} catch (CloneNotSupportedException e) {
			}
		}		
		return listFilterUsed;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object param) {
		FLyrRasterSE grayConv = null;
		try {
			if(param instanceof String)
				grayConv = FLyrRasterSE.createLayer(RasterLibrary.getOnlyLayerName(), (String)param, sourceLayer.getProjection());
		} catch (LoadLayerException e) {
			RasterToolsUtil.messageBoxError("error_generating_layer", null, e);
		}
		if(endActions != null)
			endActions.end(new Object[]{this, grayConv});
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#interrupted()
	 */
	public void interrupted() {
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
}

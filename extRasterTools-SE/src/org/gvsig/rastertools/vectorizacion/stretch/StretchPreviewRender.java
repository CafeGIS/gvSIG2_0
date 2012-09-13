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

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.imagenavigator.ImageUnavailableException;
import org.gvsig.raster.beans.previewbase.IPreviewRenderProcess;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.enhancement.EnhancementStretchListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Clase para el renderizado de la vista previa en la generación de
 * tramos
 * 10/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class StretchPreviewRender implements IPreviewRenderProcess  {

	private boolean             showPreview            = false;	
	private FLyrRasterSE        lyr                    = null;
	private StretchData         data                   = null;
	
	/**
	 * Constructor. 
	 * @param lyr
	 */
	public StretchPreviewRender(FLyrRasterSE lyr, StretchData data) {
		this.lyr = lyr;
		this.data = data;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IPreviewRenderProcess#process(org.gvsig.raster.hierarchy.IRasterRendering)
	 */
	public void process(IRasterRendering rendering) throws FilterTypeException, ImageUnavailableException {
		if(!showPreview)
			throw new ImageUnavailableException(RasterToolsUtil.getText(this, "panel_preview_not_available"));
		
		if(lyr == null)
			throw new ImageUnavailableException(RasterToolsUtil.getText(this, "preview_not_available"));
		
		RasterFilterList filterList = rendering.getRenderFilterList();
		RasterFilterListManager filterManager = new RasterFilterListManager(filterList);
		
		addPosterization(filterManager, rendering);
			
	}
	
		
	/**
	 * Añade la posterización si la opción está activa
	 * @throws FilterTypeException 
	 */
	public void addPosterization(RasterFilterListManager filterManager, IRasterRendering rendering) throws FilterTypeException {

		EnhancementStretchListManager elm = new EnhancementStretchListManager(filterManager);
		LinearStretchParams leParams = new LinearStretchParams();

		//Obtenemos el máximo y el mínimo para la banda
		double min = data.getMin();
		double max = data.getMax();
		
		double[] stretchs = data.getStretchs();
		double distance = max - min;
		for (int i = 0; i < stretchs.length; i++) 
			stretchs[i] = min + stretchs[i] * distance;
		
		//Creamos arrays de entrada y salida
		double[] in = new double[(stretchs.length - 1) * 2 + 4];
		int[] out = new int[(stretchs.length - 1) * 2 + 4];

		//Inicializamos los valores de los extremos
		in[0] = in[1] = min;
		out[0] = out[1] = 0;
		in[in.length - 1] = in[in.length - 2] = max;
		out[out.length - 1] = out[out.length - 2] = 255;

		//Construimos el array de salida
		boolean even = true;
		out[2] = 0;
		for (int i = 3; i < in.length - 2; i = i + 2) {
			if(even) 
				out[i] = out[i + 1] = 255;
			else
				out[i] = out[i + 1] = 0;
			even = !even;
		}
		out[out.length - 2] = 255;

		//Construimos el array de entrada
		for (int i = 2; i < in.length - 2; i = i + 2) 
			in[i] = in[i + 1] = stretchs[(int)(i / 2)];
			

		//Creamos y añadimos el filtro
		leParams.rgb = true;
		leParams.red.stretchIn = in;
		leParams.red.stretchOut = out;
		leParams.green.stretchIn = in;
		leParams.green.stretchOut = out;
		leParams.blue.stretchIn = in;
		leParams.blue.stretchOut = out;
		elm.addEnhancedStretchFilter(leParams, 
				lyr.getDataSource().getStatistics(), 
				rendering.getRenderBands(), 
				false);
	}

	/**
	 * Obtiene el flag que informa de si se está mostrando la previsualización o no.
	 * En caso de no mostrarse se lanza una excepción ImageUnavailableExcepcion con el 
	 * mensaje "La previsualización no está disponible para este panel"
	 * @return
	 */
	public boolean isShowPreview() {
		return showPreview;
	}

	/**
	 * Asigna el flag para mostrar u ocultar la preview. En caso de no mostrarse se lanza una 
	 * excepción ImageUnavailableExcepcion con el mensaje "La previsualización no está disponible para
	 * este panel"
	 * @param showPreview
	 */
	public void setShowPreview(boolean showPreview) {
		this.showPreview = showPreview;
	}

}

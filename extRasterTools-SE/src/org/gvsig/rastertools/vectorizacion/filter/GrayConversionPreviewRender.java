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
package org.gvsig.rastertools.vectorizacion.filter;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.imagenavigator.ImageUnavailableException;
import org.gvsig.raster.beans.previewbase.IPreviewRenderProcess;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.filter.grayscale.GrayScaleManager;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.correction.MedianListManager;
import org.gvsig.raster.grid.filter.correction.ModeManager;
import org.gvsig.raster.grid.filter.enhancement.EnhancementStretchListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.hierarchy.IStatistics;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Clase para el renderizado de la vista previa en la conversión 
 * a escala de grises.
 * 10/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GrayConversionPreviewRender implements IPreviewRenderProcess  {

	private boolean             showPreview            = false;	
	private FLyrRasterSE        lyr                    = null;
	private GrayConversionData  data                   = null;
	
	/**
	 * Constructor. 
	 * @param lyr
	 */
	public GrayConversionPreviewRender(FLyrRasterSE lyr, GrayConversionData data) {
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

		GrayScaleManager manager = new GrayScaleManager(filterManager);
		manager.addGrayScaleFilter(data.getBandType());
		
		addPosterization(filterManager, rendering);
		addMode(filterManager, rendering);
		addHighPass(filterManager, rendering);
		addNoiseReduction(filterManager, rendering);		
	}
	
	/**
	 * Añade el paso alto si la opción está activa
	 * @throws FilterTypeException 
	 */
	private void addHighPass(RasterFilterListManager filterManager, IRasterRendering rendering) throws FilterTypeException {
		if(data.isHighPassActive()) {

		}
	}

	/**
	 * Añade el filtro de moda si la opción está activa
	 * @throws FilterTypeException 
	 */
	private void addMode(RasterFilterListManager filterManager, IRasterRendering rendering) throws FilterTypeException {
		if(data.isModeActive()) {
			ModeManager mlm = new ModeManager(filterManager);
			mlm.addModeFilter(data.getModeThreshold());
		}
	}
	
	/**
	 * Añade el filtro de ruido si la opción está activa
	 * @throws FilterTypeException 
	 */
	private void addNoiseReduction(RasterFilterListManager filterManager, IRasterRendering rendering) throws FilterTypeException {
		if(data.isNoiseActive()) {
			MedianListManager mlm = new MedianListManager(filterManager);
			mlm.addMedianFilter(data.getNoiseThreshold());
		}
	}
		
	/**
	 * Añade la posterización si la opción está activa
	 * @throws FilterTypeException 
	 */
	public void addPosterization(RasterFilterListManager filterManager, IRasterRendering rendering) throws FilterTypeException {
		if(data.isPosterizationActive()) {
			EnhancementStretchListManager elm = new EnhancementStretchListManager(filterManager);
			LinearStretchParams leParams = new LinearStretchParams();
			
			//Obtenemos el máximo y el mínimo para la banda
			IStatistics stats = lyr.getDataSource().getStatistics();
			double min = 0;
			double max = 0;
			try {
				stats.calcFullStatistics();
				if(lyr.getDataType()[0] == IBuffer.TYPE_BYTE) {
					min = 0;
					max = 255;
				} else if(data.getBandFromBandType() < 3) {
					min = stats.getMin()[data.getBandFromBandType()];
					max = stats.getMax()[data.getBandFromBandType()];
				} else if(data.getBandFromBandType() == 3)  {
					min = Math.min(Math.min(stats.getMin()[0], stats.getMin()[1]), stats.getMin()[2]);
					max = Math.max(Math.max(stats.getMax()[0], stats.getMax()[1]), stats.getMax()[2]);
				}
			} catch (InterruptedException e) {
				return;
			} catch (RasterDriverException e) {
				return;
			} catch (FileNotOpenException e) {
				return;
			}
			
			//Creamos arrays de entrada y salida
			double[] in = new double[(data.getPosterizationLevels() - 1) * 2 + 4];
			int[] out = new int[(data.getPosterizationLevels() - 1) * 2 + 4];
			double lastIn = 0, lastOut = 0;
			
			//Inicializamos los valores de los extremos
			lastIn = in[0] = in[1] = min;
			lastOut = out[0] = out[1] = 0;
			in[in.length - 1] = in[in.length - 2] = max;
			out[out.length - 1] = out[out.length - 2] = 255;

			//Construimos el array de salida
			int nPieces = data.getPosterizationLevels() -1;
			int n = 0;
			int increment = 255 / nPieces;
			for (int i = 3; i < in.length - 2; i++) {
				if((i % 2) != 0) {
					out[i] = (int)Math.round(lastOut + increment);
					lastOut = (int)out[i]; 
				} else
					out[i] = (int)lastOut;
				n++;
			}

			//Construimos el array de entrada
			//En caso de ser conversión a B/W se asigna el valor de umbral para la posterización
			if(data.getPosterizationLevels() == 2) {
				in[2] = in[3] = lastIn + ((data.getPosterizationThreshold() * (max - min)) / 255);
			} else {
				for (int i = 2; i < in.length - 2; i = i + 2) {
					in[i] = in[i + 1] = lastIn + ((max - min) / data.getPosterizationLevels());
					lastIn = (int)in[i];
				}	
			}
			
			//Creamos y añadimos el filtro
			leParams.rgb = (lyr.getDataType()[0] == IBuffer.TYPE_BYTE);
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

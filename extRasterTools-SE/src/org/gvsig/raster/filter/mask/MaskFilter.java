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
package org.gvsig.raster.filter.mask;

import java.util.ArrayList;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.MultiRasterDataset;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.filter.RasterFilter;

/**
 * Filtro que aplica una máscara con la lista de ROIS. Los píxeles dentro
 * de la ROI se ponen al valor de la imagen de origen. Los píxeles fuera
 * de la ROI se ponen a NoData.
 * 
 * 14/03/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class MaskFilter extends RasterFilter {
	public static String[]         names           = new String[] { "mask" };
	protected MaskUI               maskUI          = null;
	protected ArrayList            rois            = null;
	protected double               nodata          = -99999;
	protected boolean              inverse         = false;
	
	//Extent de la ventana de datos y de la imagen completa.
	protected GridExtent           gridExtent      = null;
	protected GridExtent           windowExtent    = null;
	protected MultiRasterDataset   dataset         = null;
	protected IBuffer              rasterAlpha     = null;
		
	/**
	 * Constructor
	 */
	public MaskFilter() {
		super();
		setName(names[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getGroup()
	 */
	public String getGroup() {
		return "mascaras";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getNames()
	 */
	public String[] getNames() {
		return names;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getResult(java.lang.String)
	 */
	public Object getResult(String name) {
		if (name.equals("alphaBand"))
			return rasterAlpha;
		
		if (!name.equals("raster"))
			return null;

		if (!exec)
			return (Object) this.raster;

		return (Object) this.rasterResult;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getUIParams(java.lang.String)
	 */
	public Params getUIParams(String nameFilter) {
		Params params = new Params();
		params.setParam("Panel", getMaskUI(), -1, null);
		params.setParam("FilterName", nameFilter, -1, null);
		return params;
	}
	
	/**
	 * Obtiene el interfaz gráfico para el filtro de máscara.
	 * @return MaskUI
	 */
	private MaskUI getMaskUI() {
		if (maskUI == null) {
			maskUI = new MaskUI();
			FLyrRasterSE raster = (FLyrRasterSE) getEnv().get("initRaster");
			maskUI.setRois(rois);
			maskUI.setLayer(raster);
		}
		return maskUI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#pre()
	 */
	public void pre() {
		exec = true;
		raster = rasterResult;
		raster = (RasterBuffer) params.get("raster");
		Boolean inverseBoolean = (Boolean)params.get("inverse");
		if(inverseBoolean != null)
			inverse = inverseBoolean.booleanValue();
		Double nodataDouble = (Double)params.get("nodata");
		if(nodataDouble != null)
			nodata = nodataDouble.doubleValue();
		Boolean transpBoolean = (Boolean)params.get("transparency");
								
		rois = (ArrayList) params.get("rois");
		if (rois == null)
			rois = new ArrayList();
		height = raster.getHeight();
		width = raster.getWidth();
		
		gridExtent = (GridExtent) environment.get("GridExtent");
		windowExtent = (GridExtent) environment.get("WindowExtent");
		dataset = (MultiRasterDataset) environment.get("MultiRasterDataset");
		
		rasterResult = RasterBuffer.getBuffer(raster.getDataType(), raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
		
		if(transpBoolean.booleanValue() && raster.getDataType() == IBuffer.TYPE_BYTE) 
			rasterAlpha = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), 1, true);
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#process(int, int)
	 */
	public void process(int x, int y) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#post()
	 */
	public void post() {
	}
  
	public int getInRasterDataType() {return 0;}
	public int getOutRasterDataType() {return 0;}
}
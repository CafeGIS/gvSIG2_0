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
package org.gvsig.raster.filter.regionalpha;

import java.util.ArrayList;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.MultiRasterDataset;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.roi.ROI;
/**
 * 
 * @version 15/01/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class RegionAlphaFilter extends RasterFilter {
	public static String[]     names           = new String[] { "regionalpha" };
	private RegionAlphaUI      regionAlphaUI   = null;
	private ArrayList          rois            = null;
	private IBuffer            rasterAlpha     = null;
	private int                alpha           = 255;
	protected boolean          inverse         = false;

	/* Variables que hacen falta en el process */
	private GridExtent         gridExtent;
	private GridExtent         windowExtent;
	private MultiRasterDataset dataset;
	
	/**
	 * Constructor
	 */
	public RegionAlphaFilter() {
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
			if(rois == null || rois.size() == 0)
				return null;
			else
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
		params.setParam("Panel", getRegionAlphaUI(), -1, null);
		params.setParam("FilterName", nameFilter, -1, null);
		params.setParam("Alpha",
				new Integer(alpha),
				Params.SLIDER,
				new String[]{ "0", "255", "0", "1", "25" }); //min, max, valor defecto, intervalo pequeño, intervalo grande;

		return params;
	}
	
	private RegionAlphaUI getRegionAlphaUI() {
		if (regionAlphaUI == null) {
			regionAlphaUI = new RegionAlphaUI();
			FLayer raster = (FLayer) getEnv().get("initRaster");
			regionAlphaUI.setRois(rois);
			regionAlphaUI.setLayer(raster);
		}
		return regionAlphaUI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#pre()
	 */
	public void pre() {
		exec = true;
		raster = rasterResult;
		raster = (RasterBuffer) params.get("raster");
		
		Boolean inverseBoolean = (Boolean) params.get("inverse");
		if (inverseBoolean != null)
			inverse = inverseBoolean.booleanValue();
		
		rois = (ArrayList) params.get("rois");
		if (rois == null)
			rois = new ArrayList();
		height = raster.getHeight();
		width = raster.getWidth();
		
		alpha = ((Integer) params.get("alpha")).intValue();
		
		gridExtent = (GridExtent) environment.get("GridExtent");
		windowExtent = (GridExtent) environment.get("WindowExtent");
		dataset = (MultiRasterDataset) environment.get("MultiRasterDataset");
		rasterAlpha = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), 1, true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#post()
	 */
	public void post() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#process(int, int)
	 */
	public void process(int x, int y) {
		if ((windowExtent == null) || (gridExtent == null))
			return;
		double wcX = windowExtent.minX() + ((((double) x) * windowExtent.width()) / ((double) raster.getWidth()));
		double wcY = windowExtent.minY() + ((((double) (raster.getHeight() - (y))) * windowExtent.height()) / ((double) raster.getHeight()));
		
		for (int i = 0; i < rois.size(); i++) {
			if (((ROI) rois.get(i)).isInside(wcX, wcY, dataset.getCellSize(), dataset.getCellSize())) {
				if (inverse)
					rasterAlpha.setElem(y, x, 0, (byte) 255);
				else
					rasterAlpha.setElem(y, x, 0, (byte) (255 - alpha));
				return;
			}
		}
		if (inverse)
			rasterAlpha.setElem(y, x, 0, (byte) (255 - alpha));
		else
			rasterAlpha.setElem(y, x, 0, (byte) 255);
	}

	public int getInRasterDataType() {return 0;}
	public int getOutRasterDataType() {return 0;}
}
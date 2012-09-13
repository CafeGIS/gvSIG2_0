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

import java.awt.image.DataBuffer;

import org.gvsig.raster.grid.roi.ROI;

/**
 * Filtro que aplica una máscara con la lista de ROIS. Los píxeles dentro
 * de la ROI se ponen al valor de la imagen de origen. Los píxeles fuera
 * de la ROI se ponen a NoData. Esta clase es la gestiona los datos para los
 * raster de tipo short.
 * 
 * 14/03/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class MaskShortFilter extends MaskFilter {
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.filter.mask.MaskFilter#process(int, int)
	 */
	public void process(int x, int y) {
		if ((windowExtent == null) || (gridExtent == null))
			return;
		double wcX = windowExtent.minX() + ((((double) x) * windowExtent.width()) / ((double) raster.getWidth()));
		double wcY = windowExtent.minY() + ((((double) (raster.getHeight() - (y))) * windowExtent.height()) / ((double) raster.getHeight()));

		
		if(inverse) {
			for (int i = 0; i < rois.size(); i++) {
				if (((ROI) rois.get(i)).isInside(wcX, wcY, dataset.getCellSize(), dataset.getCellSize())) {
					for (int j = 0; j < raster.getBandCount(); j++) 
						rasterResult.setElem(y, x, j, (short)nodata);
					return;
				}
			}
			
			for (int j = 0; j < raster.getBandCount(); j++) 
				rasterResult.setElem(y, x, j, raster.getElemShort(y, x, j));
			return;
		}
		
		for (int i = 0; i < rois.size(); i++) {
			if (((ROI) rois.get(i)).isInside(wcX, wcY, dataset.getCellSize(), dataset.getCellSize())) {
				for (int j = 0; j < raster.getBandCount(); j++) 
					rasterResult.setElem(y, x, j, raster.getElemShort(y, x, j));
				return;
			}
		}
		
		for (int j = 0; j < raster.getBandCount(); j++) 
			rasterResult.setElem(y, x, j, (short)nodata);
		return;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.filter.mask.MaskFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return DataBuffer.TYPE_SHORT;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.filter.mask.MaskFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() { 
		return DataBuffer.TYPE_SHORT;
	}
}
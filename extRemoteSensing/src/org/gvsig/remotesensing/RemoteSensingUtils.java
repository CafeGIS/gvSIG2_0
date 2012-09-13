/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
	 *
	 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
	 *
	 * For more information, contact:
	 *
	 *  Generalitat Valenciana
	 *   Conselleria d'Infraestructures i Transport
	 *   Av. Blasco Ibañez, 50
	 *   46010 VALENCIA
	 *   SPAIN
	 *
	 *      +34 963862235
	 *   gvsig@gva.es
	 *      www.gvsig.gva.es
	 *
	 *    or
	 *
	 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
	 *   Campus Universitario s/n
	 *   02071 Alabacete
	 *   Spain
	 *
	 *   +34 967 599 200
	 */

package org.gvsig.remotesensing;

import java.util.ArrayList;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.grid.GridPalette;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.bands.ColorTableFilter;
import org.gvsig.raster.grid.filter.bands.ColorTableListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearEnhancementFilter;
import org.gvsig.raster.grid.filter.statistics.TailTrimFilter;
import org.gvsig.raster.hierarchy.IRasterRendering;

/**
 * Herramientas de usa general para la extensión extRemoteSensing.
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class RemoteSensingUtils {

	/**
	* Asigna a la capa <code>lyr</code> una leyenda construida a partir
	* de un <code>ArrayList</code> de <code>ColorItem</code>.
	*
	* @param lyr capa a la que se asigna la leyenda
	* @param colorItems ColorItems con los que se contruye la leyenda
	*
	* @throws FilterTypeException
	*/
	public  static void setLeyend(FLayer lyr, ArrayList colorItems) throws FilterTypeException {
		if(lyr instanceof IRasterRendering) {
			IRasterRendering rendering = (IRasterRendering) lyr;

			ColorTable colorTable = new ColorTable();
			colorTable.createPaletteFromColorItems(colorItems, false);

			RasterFilterList filterList = rendering.getRenderFilterList();
			RasterFilterListManager manager = new RasterFilterListManager(filterList);
			ColorTableListManager cManager = (ColorTableListManager) manager.getManagerByClass(ColorTableListManager.class);
			try {
				filterList.remove(ColorTableFilter.class);

				((FLyrRasterSE)lyr).setLastLegend(null);

				filterList.remove(LinearEnhancementFilter.class);
				filterList.remove(TailTrimFilter.class);
				GridPalette gridPalette = new GridPalette(colorTable);
				cManager.addColorTableFilter(gridPalette);
				((FLyrRasterSE)lyr).setLastLegend(gridPalette);
			} catch (FilterTypeException e) {
				throw e;
			}

			rendering.setRenderFilterList(filterList);
		}
	}

	public static double getCellValueInLayerCoords(IBuffer buffer, int x, int y, int band) {
		int iType = buffer.getDataType();

		if (iType == RasterBuffer.TYPE_DOUBLE) {
			return  buffer.getElemDouble(y, x, band);
		} else if (iType == RasterBuffer.TYPE_INT) {
			return (double)  buffer.getElemInt(y, x, band);
		} else if (iType == RasterBuffer.TYPE_FLOAT) {
			return (double)  buffer.getElemFloat(y, x, band);
		} else if (iType == RasterBuffer.TYPE_BYTE) {
			return (double) (buffer.getElemByte(y, x, band) & 0xff);
		} else if ((iType == RasterBuffer.TYPE_SHORT) | (iType == RasterBuffer.TYPE_USHORT)) {
			return (double)  buffer.getElemShort(y, x, band);
		}

		return buffer.getNoDataValue();
	}
}

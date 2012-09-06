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
package org.gvsig.raster.grid.filter.bands;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.GridPalette;
import org.gvsig.raster.grid.filter.RasterFilter;
/**
 * <P>
 * Clase base para los filtros de tabla de color. Siempre gastará la banda cero
 * del raster para aplicar el filtro ya que la aplicación de tablas suele tener
 * sentido solo en raster de una sola banda.
 * </P>
 * <P>
 * La salida siempre es un ARGB.
 * </P>
 *
 * @version 06/06/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ColorTableFilter extends RasterFilter {
	protected IBuffer      rasterAlpha  = null;
	public static String[] names        = new String[] { "colortable" };
	protected GridPalette  colorTable   = new GridPalette();
	protected boolean      hasAlpha     = false; 

	/**
	 * Constructor
	 */
	public ColorTableFilter() {
		super();
		setName(names[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#pre()
	 */
	public void pre() {
		exec = true;
		raster = (RasterBuffer) params.get("raster");
		colorTable = ((GridPalette) params.get("colorTable"));
		hasAlpha = colorTable.hasAlpha();
		if (raster != null) {
			height = raster.getHeight();
			width = raster.getWidth();
			rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), 3, true);
			if (hasAlpha)
				rasterAlpha = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), 1, true);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getGroup()
	 */
	public String getGroup() {
		return "radiometrico";
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
		
		if (name.equals("raster"))
			return (Object) this.rasterResult;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getUIParams(java.lang.String)
	 */
	public Params getUIParams(String nameFilter) {
		Params params = new Params();
		params.setParam("colorTable",
				new GridPalette(colorTable),
				-1,
				null);
		return params;
	}

	public void post() {
	}

	public void process(int x, int y) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return IBuffer.TYPE_BYTE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#isVisible()
	 */
	public boolean isVisible() {
		return false;
	}

	/**
	 * Obtiene la tabla de color que se usará para la aplicación del filtro
	 * @return GridPalette
	 */
	public GridPalette getColorTable() {
		if ((colorTable == null) || (colorTable.getColorItems() == null))
			colorTable = ((GridPalette) params.get("colorTable"));
		return colorTable;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return 0;
	}

	/**
	 * Define la tabla de color
	 * @param colorTable the colorTable to set
	 */
	public void setColorTable(GridPalette colorTable) {
		this.colorTable = colorTable;
	}
}
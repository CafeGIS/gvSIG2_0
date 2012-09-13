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
package org.gvsig.raster.filter.grayscale;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.RasterFilter;
/**
 * <P>
 * Clase base para los filtros de conversión a escala de gris
 * </P>
 *
 * @version 30/11/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GrayScaleFilter extends RasterFilter {
	public static final int        R                = 0;
	public static final int        G                = 1;
	public static final int        B                = 2;
	public static final int        RGB              = 3;
	public static final int        GRAY             = 4;
	public static final int        UNDEFINED        = -1;
	public static String[]         names            = new String[] { "grayscale" };
	protected int                  type             = UNDEFINED;

	/**
	 * Constructor
	 */
	public GrayScaleFilter() {
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
		type = ((Integer)params.get("typeBand")).intValue();
		
		if(type == RGB && raster.getBandCount() < 3)
			type = R;
		
		height = raster.getHeight();
		width = raster.getWidth();
		
		rasterResult = RasterBuffer.getBuffer(raster.getDataType(), raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getGroup()
	 */
	public String getGroup() {
		return "colores";
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
		if (name.equals("raster"))
			return (Object) this.rasterResult;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getUIParams(java.lang.String)
	 */
	public Params getUIParams(String nameFilter) {
		if(type == UNDEFINED) {
			if(this.params != null) {
				Object obj = this.params.get("typeBand");
				if(obj != null)
					type = ((Integer)obj).intValue();
			}
		}
		
		Params params = new Params();
		params.setParam("typeBand", 
				new Integer(type), 
				Params.CHOICE, 
				new String[]{ "R", "G", "B", "RGB", "GRAY"});

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

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return IBuffer.TYPE_BYTE;
	}
}
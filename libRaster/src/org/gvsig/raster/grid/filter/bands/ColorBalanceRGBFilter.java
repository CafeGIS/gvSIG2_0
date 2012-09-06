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
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.util.ColorConversion;
/**
 * <P>
 * Clase base para los filtros de balance de color RGB
 * </P>
 *
 * @version 30/11/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ColorBalanceRGBFilter extends RasterFilter {
	protected IBuffer          rasterAlpha      = null;
	public static String[]     names            = new String[] { "colorbalancergb" };
	protected ColorConversion  colorConversion  = null;
	protected int              out              = IBuffer.TYPE_BYTE;
	protected int              red              = 0;
	protected int              green            = 0;
	protected int              blue             = 0;
	protected boolean          luminosity       = false;
	protected int[]            renderBands      = null;

	/**
	 * Constructor
	 */
	public ColorBalanceRGBFilter() {
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
		int[] rb = (int[]) params.get("renderBands");
		
		switch (raster.getBandCount()) {
		case 1:renderBands = new int[]{0, 0, 0}; break;
		case 2:renderBands = rb; break;
		case 3:renderBands = new int[]{0, 1, 2}; break;
		}
		//Mantenemos los valores de -1 ya que esas bandas no se procesan
		for(int i = 0; i < renderBands.length; i++)
			if(rb[i] == -1)
				renderBands[i] = -1;
										
		if(raster.getDataType() != IBuffer.TYPE_BYTE) {
			exec = false;
			raster = rasterResult;
			return;
		}
		if(params.get("red") != null)
			red = ((Integer) params.get("red")).intValue();
		if(params.get("green") != null)
			green = ((Integer) params.get("green")).intValue();
		if(params.get("blue") != null)
			blue = ((Integer) params.get("blue")).intValue();
		if(params.get("luminosity") != null)
			luminosity = ((Boolean) params.get("luminosity")).booleanValue();
		if(raster != null) {
			height = raster.getHeight();
			width = raster.getWidth();
			if(raster.getBandCount() >= 3)
				rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
			else
				rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), 3, true);
		}
		if(colorConversion == null)
			colorConversion = new ColorConversion();
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
		Params params = new Params();
		params.setParam("red",
				new Integer(red),
				Params.SLIDER,
				new String[]{ "0", "100", "0", "5", "25"}); //min, max, valor defecto, intervalo pequeño, intervalo grande;
		params.setParam("green",
				new Integer(green),
				Params.SLIDER,
				new String[]{ "0", "100", "0", "5", "25"}); //min, max, valor defecto, intervalo pequeño, intervalo grande;
		params.setParam("blue",
				new Integer(blue),
				Params.SLIDER,
				new String[]{ "0", "100", "0", "5", "25"}); //min, max, valor defecto, intervalo pequeño, intervalo grande;
		params.setParam("luminosity",
				new Boolean(luminosity),
				Params.CHECK,
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
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return IBuffer.TYPE_BYTE;
	}
}
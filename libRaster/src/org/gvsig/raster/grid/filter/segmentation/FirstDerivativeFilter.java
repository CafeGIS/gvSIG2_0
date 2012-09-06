/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.grid.filter.segmentation;

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.RasterFilter;
/**
 * Clase base para los filtros de primera derivada.
 *
 * @version 04/06/2007
 * @author Diego Guerrero Sevilla  <diego.guerrero@uclm.es>
 */
public class FirstDerivativeFilter extends RasterFilter {
	public static String[] names = new String[] {"sobel", "roberts", "prewitt", "freichen"};
	public static final int TYPE_SOBEL  = 0;
	public static final int TYPE_ROBERTS  = 1;
	public static final int TYPE_PREWITT  = 2;
	public static final int TYPE_FREICHEN  = 3;

	protected int 			umbral = 0;
	protected boolean 	compare = false;
	protected Kernel 		operatorH;
	protected Kernel 		operatorV;
	private int					operator = 0;

	// Kernels:------------------------------------------
	static final double sobelH[][]= {{-1,0,1},{-2,0,2},{-1,0,1}};
	static final double sobelV[][]= {{-1,-2,-1},{0,0,0},{1,2,1}};
	static final Kernel sobelHKernel = new Kernel(sobelH);
	static final Kernel sobelVKernel = new Kernel(sobelV);

	static final double robertsH[][]= {{0,0,-1},{0,1,0},{0,0,0}};
	static final double robertsV[][]= {{-1,0,0},{0,1,0},{0,0,0}};
	static final Kernel robertsHKernel = new Kernel(robertsH);
	static final Kernel robertsVKernel = new Kernel(robertsV);

	static final double prewittH[][]= {{1,0,-1},{1,0,-1},{1,0,-1}};
	static final double prewittV[][] = {{-1,-1,-1},{0,0,0},{1,1,1}};
	static final Kernel prewittHKernel = new Kernel(prewittH);
	static final Kernel prewittVKernel = new Kernel(prewittV);

	static final double freiChenH[][]= {{-1,-1.4D,-1},{0,0,0},{1,1.4D,1}};
	static final double freiChenV[][]= {{-1,0,1},{-1.4D,0,1.4D},{-1,0,1}};
	//static final double freiChenH[][]= {{-1,-1.4142D,-1},{0,0,0},{1,1.4142D,1}};
	//static final double freiChenV[][]= {{-1,0,1},{-1.4142D,0,1.4142D},{-1,0,1}};
	static final Kernel freiChenHKernel = new Kernel(freiChenH);
	static final Kernel freiChenVKernel = new Kernel(freiChenV);
	// --------------------------------------------------

	/**
	 * Constructor
	 */
	public FirstDerivativeFilter() {
		setName(names[0]);
	}

	public void pre() {
		exec = true;
		raster = (RasterBuffer) params.get("raster");
		height = raster.getHeight();
		width = raster.getWidth();

		if (params.get("umbral") != null)
			umbral = ((Integer) params.get("umbral")).intValue();
		else
			umbral = 0;

		if (params.get("compare") != null)
			compare = ((Boolean) params.get("compare")).booleanValue();
		else
			compare = false;

		operator = 0;
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals(getName())) {
				operator = i;
				break;
			}
		}

		switch (operator) {
			case TYPE_SOBEL:
				operatorH = sobelHKernel;
				operatorV = sobelVKernel;
				break;
			case TYPE_ROBERTS:
				operatorH = robertsHKernel;
				operatorV = robertsVKernel;
				break;
			case TYPE_PREWITT:
				operatorH = prewittHKernel;
				operatorV = prewittVKernel;
				break;
			case TYPE_FREICHEN:
				operatorH = freiChenHKernel;
				operatorV = freiChenVKernel;
				break;
		}

		rasterResult = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, raster.getWidth(), raster.getHeight(), raster.getBandCount(), true);
	}

	/**
	 * Obtiene el umbral
	 * @return entero que representa el umbral
	 */
	public int getUmbral() {
		return umbral;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getGroup()
	 */
	public String getGroup() {
		return "deteccion_bordes";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#post()
	 */
	public void post() {
		// En caso de que nadie apunte a raster, se liberará su memoria.
		raster = null;
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
	 * @see org.gvsig.raster.grid.filter.IRasterFilter#getParams()
	 */
	public Params getUIParams(String nameFilter) {
		Params params = new Params();
		params.setParam("Umbral",
				new Integer(umbral),
				Params.SLIDER,
				new String[] {"0", "255", "0", "1", "25" }); //min, max, valor defecto, intervalo pequeño, intervalo grande;
		params.setParam("Compare",
				new Boolean(compare),
				Params.CHECK,
				null);
		params.setParam("FilterName",
				getName(),
				-1,
				null);
		return params;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return RasterBuffer.TYPE_BYTE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getResult(java.lang.String)
	 */
	public Object getResult(String name) {
		if (name.equals("raster")) {
			if (!exec)
				return this.raster;
			return this.rasterResult;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#process(int, int)
	 */
	public void process(int x, int y) {
	}
}
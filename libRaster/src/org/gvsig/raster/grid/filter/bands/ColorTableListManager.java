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

import java.awt.Color;
import java.util.ArrayList;

import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.grid.GridPalette;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
/**
 * Gestor del filtro de aplicación de tablas de color sobre un raster.
 *
 * @version 06/06/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class ColorTableListManager  implements IRasterFilterListManager {

	protected RasterFilterList	filterList = null;

	/**
	 * Registra ColorTableListManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("ColorTable", "", ColorTableListManager.class);
	}

	/**
	 * Constructor.
	 * Asigna la lista de filtros y el managener global.
	 *
	 * @param filterListManager
	 */
	public ColorTableListManager(RasterFilterListManager filterListManager) {
		this.filterList = filterListManager.getFilterList();
	}

	/**
	 * Añade un filtro de tabla de color a la pila de filtros.
	 * @param ladoVentana
 * @throws FilterTypeException
	 */
	public void addColorTableFilter(GridPalette palette) throws FilterTypeException {
		RasterFilter filter = new ColorTableByteFilter();

		//Cuando el filtro esta creado, tomamos los valores y lo añadimos a la pila

		if (filter != null) {
			filter.addParam("colorTable", palette);
			filterList.add(filter);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(ColorTableFilter.class);
		return filters;
	}

	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(ColorTableFilter.class)) {
			GridPalette colorTable = null;
			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("colorTable"))
					colorTable = (GridPalette) params.getParam(i).defaultValue;
			}
			addColorTableFilter(colorTable);
		}
	}

	/**
	 * Devuelve el color si lo encuentra en el arraylist y lo elimina, en caso
	 * contrario devuelve null
	 * @param list
	 * @param value
	 * @return
	 */
	private static ColorItem getColorItem(ArrayList list, double value) {
		for (int i = 0; i < list.size(); i++) {
			if (((ColorItem) list.get(i)).getValue() == value) {
				return (ColorItem) list.remove(i);
			}
		}
		return null;
	}

	public static ColorTable createColorTableFromArray(ArrayList lines) {
		String pkgBase = "filter.colortable.";
		ArrayList linesCloned = (ArrayList) lines.clone();

		String paletteName = "";
		int color = 0;
		int alpha = 0;
		ArrayList rows = new ArrayList();

		ColorItem colorItem = new ColorItem();
		boolean interpolated = false;

		while (linesCloned.size() > 0) {
			String elem = (String) linesCloned.get(0);

			if (!elem.startsWith(pkgBase)) {
				linesCloned.remove(0);
				continue;
			}

			if (elem.startsWith(pkgBase + "name"))
				paletteName = RasterFilterListManager.getValue(elem);

			if (elem.startsWith(pkgBase + "interpolated"))
				interpolated = Boolean.parseBoolean(RasterFilterListManager.getValue(elem));

			if (elem.startsWith(pkgBase + "color" + color)) {
				if (elem.startsWith(pkgBase + "color" + color + ".value"))
					colorItem.setValue(Double.parseDouble(RasterFilterListManager.getValue(elem)));
				if (elem.startsWith(pkgBase + "color" + color + ".name"))
					colorItem.setNameClass(RasterFilterListManager.getValue(elem));
				if (elem.startsWith(pkgBase + "color" + color + ".rgb")) {

					String rgb = RasterFilterListManager.getValue(elem);
					int r = Integer.valueOf(rgb.substring(0, rgb.indexOf(","))).intValue();
					int g = Integer.valueOf(rgb.substring(rgb.indexOf(",") + 1, rgb.lastIndexOf(","))).intValue();
					int b = Integer.valueOf(rgb.substring(rgb.lastIndexOf(",") + 1, rgb.length())).intValue();

					colorItem.setColor(new Color(r, g, b));
				}
				if (elem.startsWith(pkgBase + "color" + color + ".interpolated"))
					colorItem.setInterpolated(Double.parseDouble(RasterFilterListManager.getValue(elem)));

				if ((linesCloned.size() <= 1) || (!((String) linesCloned.get(1)).startsWith(pkgBase + "color" + color))) {
					rows.add(colorItem);
					color++;
					colorItem = new ColorItem();
				}
			}

			if (elem.startsWith(pkgBase + "alpha" + alpha)) {
				if (elem.startsWith(pkgBase + "alpha" + alpha + ".value")) {
					ColorItem aux = getColorItem(rows, Double.parseDouble(RasterFilterListManager.getValue(elem)));
					if (aux != null) {
						colorItem = aux;
						colorItem.setNameClass(aux.getNameClass());
						colorItem.setInterpolated(aux.getInterpolated());
						colorItem.setColor(new Color(aux.getColor().getRed(), aux.getColor().getGreen(), aux.getColor().getBlue(), colorItem.getColor().getAlpha()));
					}

					colorItem.setValue(Double.parseDouble(RasterFilterListManager.getValue(elem)));
				}
				if (elem.startsWith(pkgBase + "alpha" + alpha + ".a")) {
					Color c = colorItem.getColor();
					colorItem.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(),Integer.parseInt(RasterFilterListManager.getValue(elem))));
				}
				if (elem.startsWith(pkgBase + "alpha" + alpha + ".interpolated"))
					colorItem.setInterpolated(Double.parseDouble(RasterFilterListManager.getValue(elem)));

				if ((linesCloned.size() <= 1) || (!((String) linesCloned.get(1)).startsWith(pkgBase + "alpha" + alpha))) {
					rows.add(colorItem);
					alpha++;
					colorItem = new ColorItem();
				}
			}

			linesCloned.remove(0);
		}

		ColorTable colorTable = new ColorTable();

		colorTable = new ColorTable();
		colorTable.setName(paletteName);
		colorTable.createPaletteFromColorItems(rows, false);
		colorTable.setInterpolated(interpolated);

		return colorTable;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#createFilterListFromStrings(java.util.ArrayList, java.lang.String, int)
	 */
	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) throws FilterTypeException {
		String pkgBase = "filter.colortable.";
		if (fil.startsWith(pkgBase + "active")) {
			boolean exec = true;
			if ((RasterFilterListManager.getValue(fil).equals("false")))
				exec = false;
			filters.remove(0);

			ColorTable colorTable = createColorTableFromArray(filters);

			filterList.remove(ColorTableFilter.class);
			addColorTableFilter(new GridPalette(colorTable));

			ColorTableFilter ct = (ColorTableFilter) filterList.getFilterByBaseClass(ColorTableFilter.class);
			ct.setExec(exec);
		}
		return filteri;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getStringsFromFilterList(java.util.ArrayList, org.gvsig.raster.grid.filter.RasterFilter)
	 */
	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
		if (rf instanceof ColorTableFilter) {
			String pkgBase = "filter.colortable.";
			ColorTableFilter colorTableFilter = (ColorTableFilter) rf;
			ColorTable colorTable = (ColorTable) colorTableFilter.getParam("colorTable");
			if (colorTable != null) {
				if (colorTableFilter.isExec())
					filterList.add(pkgBase + "active=true");
				else
					filterList.add(pkgBase + "active=false");

				filterList.add(pkgBase + "name=" + colorTable.getName());
				filterList.add(pkgBase + "interpolated=" + colorTable.isInterpolated());

				for (int i = 0; i < colorTable.getColorItems().size(); i++) {
					ColorItem colorItem = (ColorItem) colorTable.getColorItems().get(i);
					filterList.add(pkgBase + "color" + i + ".value=" + colorItem.getValue());
					filterList.add(pkgBase + "color" + i + ".name=" + colorItem.getNameClass());
					Color c = colorItem.getColor();
					filterList.add(pkgBase + "color" + i + ".rgb=" + c.getRed() + "," + c.getGreen() + "," + c.getBlue());
					filterList.add(pkgBase + "color" + i + ".interpolated=" + colorItem.getInterpolated());
				}

				for (int i = 0; i < colorTable.getColorItems().size(); i++) {
					ColorItem colorItem = (ColorItem) colorTable.getColorItems().get(i);
					filterList.add(pkgBase + "alpha" + i + ".value=" + colorItem.getValue());
					Color c = colorItem.getColor();
					filterList.add(pkgBase + "alpha" + i + ".a=" + c.getAlpha());
					filterList.add(pkgBase + "alpha" + i + ".interpolated=" + colorItem.getInterpolated());
				}
			}
		}
		return filterList;
	}
}
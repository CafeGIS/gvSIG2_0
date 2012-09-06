/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.grid.filter.enhancement;

import java.util.ArrayList;

import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.IRasterFilterListManager;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams.Stretch;
import org.gvsig.raster.grid.filter.statistics.StatisticsListManager;
import org.gvsig.raster.hierarchy.IStatistics;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
/**
 * Gestor de la pila de filtros para el filtro de realce por intervalos.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EnhancementStretchListManager implements IRasterFilterListManager {
	protected RasterFilterList      filterList        = null;
	private RasterFilterListManager filterListManager = null;
	private IStatistics             stats             = null;

	/**
	 * Constructor
	 * @param filterListManager
	 */
	public EnhancementStretchListManager(RasterFilterListManager filterListManager) {
		this.filterListManager = filterListManager;
		this.filterList = filterListManager.getFilterList();
		stats = (IStatistics)filterList.getEnvParam("IStatistics");
	}

	/**
	 * Asigna el objeto con las estadisticas.
	 * @param stats
	 */
	public void setStatistics(IStatistics stats) {
		this.stats = stats;
	}

	/**
	 * Registra EnhancementStretchListManager en los puntos de extension de RasterFilter
	 */
	public static void register() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		point.append("EnhancementStretch", "", EnhancementStretchListManager.class);
	}

	/**
	 * Añade un filtro de realce.
	 * La forma de inserción del filtro es fija ya que la inserción de un realce lleva implicita
	 * la inserción de un filtro de recorte de colas (tailtrim), aunque no en todos los casos.
	 * Si ya existe un filtro de realce en la lista se obtiene la posición de este.
	 * Si es necesario un recorte de colas entonces se comprueba si existe un uno reemplazandose
	 * por el nuevo y sino se insertará uno. Al final reemplazamos el realce que existia.
	 *
	 * Si por el contrario no existen realce ni trim se añaden ambos al final de la lista.
	 * @param stats Objeto de estadisticas asociado
	 * @param tailTrim porcentaje de recorte de colas. Será un valor entre 0 y 1.
	 * @param insertionMode Modo de inserción
	 * @param renderBands bandas RGB mostradas en la visualización.
	 * @throws FilterTypeException
	 */
	public void addEnhancedStretchFilter(LinearStretchParams leParams, IStatistics stats, int[] renderBands, boolean removeEnds) throws FilterTypeException {
		try {
			if (!leParams.hasTailTrim()) { // En este caso siempre es necesario el máximo y
				// mínimo
				if (!stats.isCalculated())
					try {
						stats.calcFullStatistics();
					} catch (FileNotOpenException e) {
						// No podemos aplicar el filtro
						return;
					} catch (RasterDriverException e) {
						// No podemos aplicar el filtro
						return;
					}
			} else {
				StatisticsListManager slm = new StatisticsListManager(filterListManager, stats);
				slm.addTailFilter(leParams.getTailTrimList(), 0D, removeEnds, stats);
			}

			RasterFilter filter = createEnhancedFilter(leParams, stats, renderBands, removeEnds);
			if (filter != null)
				filterList.add(filter);
		} catch (InterruptedException e) {
			//Si se ha interrumpido no añadimos el filtro
		}
	}

	/**
	 * Crea un filtro de realce por tramos de forma estática
	 * @param leParams Parámetros del filtro
	 * @param stats
	 * @param renderBands
	 * @return
	 */
	public static RasterFilter createEnhancedFilter(LinearStretchParams leParams, IStatistics stats, int[] renderBands, boolean removeEnds) {
		RasterFilter filter = new LinearStretchEnhancementFloatFilter();
		if (filter != null) {
			filter.addParam("stats", stats);
			filter.addParam("remove", new Boolean(false));
			filter.addParam("renderBands", renderBands);
			filter.addParam("stretchs", leParams);
			filter.addParam("remove", new Boolean(removeEnds));
		}

		return filter;
	}

	/**
	 * Convierte un array de dobles a una cadena
	 * @param values
	 * @return
	 */
	private String convertArrayToString(double[] values) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			buffer.append(values[i]);
			if (i < (values.length - 1))
				buffer.append(",");
		}
		return buffer.toString();
	}

	/**
	 * Convierte una array de enteros a una cadena
	 * @param values
	 * @return
	 */
	private String convertArrayToString(int[] values) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			buffer.append(values[i]);
			if (i < (values.length - 1))
				buffer.append(",");
		}
		return buffer.toString();
	}

	/**
	 * Convierte una cadena a un array de enteros
	 * @param from
	 * @return
	 */
	private int[] StringToIntegerArray(String from) {
		String[] valueList = from.split(",");
		int[] values = new int[valueList.length];
		for (int i = 0; i < values.length; i++)
			try {
				values[i] = Integer.parseInt(valueList[i]);
			} catch (NumberFormatException e) {
			}
		return values;
	}

	/**
	 * Convierte una cadena a un array de dobles
	 * @param from
	 * @return
	 */
	private double[] StringToDoubleArray(String from) {
		String[] valueList = from.split(",");
		double[] values = new double[valueList.length];
		for (int i = 0; i < values.length; i++)
			try {
				values[i] = Double.parseDouble(valueList[i]);
			} catch (NumberFormatException e) {
			}
		return values;
	}

	/**
	 * Guarda en el array de valores, todos los valores del objeto Strech para ser
	 * almacenado en el
	 * @param filterList
	 * @param band
	 * @param stretch
	 */
	private void putStretchBand(ArrayList filterList, String band, Stretch stretch) {
		filterList.add("filter.linearstretchenhancement." + band + ".maxValue=" + stretch.maxValue);
		filterList.add("filter.linearstretchenhancement." + band + ".minValue=" + stretch.minValue);
		if (stretch.offset != null)
			filterList.add("filter.linearstretchenhancement." + band + ".offset=" + convertArrayToString(stretch.offset));
		if (stretch.scale != null)
			filterList.add("filter.linearstretchenhancement." + band + ".scale=" + convertArrayToString(stretch.scale));
		if (stretch.stretchIn != null)
			filterList.add("filter.linearstretchenhancement." + band + ".stretchIn=" + convertArrayToString(stretch.stretchIn));
		if (stretch.stretchOut != null)
			filterList.add("filter.linearstretchenhancement." + band + ".stretchOut=" + convertArrayToString(stretch.stretchOut));
		filterList.add("filter.linearstretchenhancement." + band + ".tailTrimMax=" + stretch.tailTrimMax);
		filterList.add("filter.linearstretchenhancement." + band + ".tailTrimMin=" + stretch.tailTrimMin);
		filterList.add("filter.linearstretchenhancement." + band + ".tailTrimValueMax=" + stretch.tailTrimValueMax);
		filterList.add("filter.linearstretchenhancement." + band + ".tailTrimValueMin=" + stretch.tailTrimValueMin);
		filterList.add("filter.linearstretchenhancement." + band + ".functionType=" + stretch.functionType);
		filterList.add("filter.linearstretchenhancement." + band + ".valueFunction=" + stretch.valueFunction);
	}

	/**
	 * Obtiene un Array de Strings a partir de una pila de filtros. Cada elemento
	 * del array tendrá la forma de elemento=valor.
	 */
	public ArrayList getStringsFromFilterList(ArrayList filterList, RasterFilter rf) {
		if (rf instanceof LinearStretchEnhancementFilter) {
			LinearStretchEnhancementFilter filter = (LinearStretchEnhancementFilter) rf;

			filterList.add("filter.linearstretchenhancement.active=true");
			filterList.add("filter.linearstretchenhancement.removeends=" + filter.getRemoveEnds());
			putStretchBand(filterList, "red", filter.stretchs.red);
			putStretchBand(filterList, "green", filter.stretchs.green);
			putStretchBand(filterList, "blue", filter.stretchs.blue);
			filterList.add("filter.linearstretchenhancement.renderbands=" + convertArrayToString(filter.renderBands));
			filterList.add("filter.linearstretchenhancement.RGB=" + Boolean.valueOf(filter.stretchs.rgb).toString());
		}

		return filterList;
	}

	/**
	 * Configura algun parametro del objeto Stretch, respecto a una banda, su
	 * propiedad y el valor, en caso de no encontrar la propiedad o no ser dicha
	 * banda, devuelve false. Es util para usarlo para extraer los valores de
	 * createFilterListFromStrings
	 * @param band
	 * @param property
	 * @param value
	 * @param stretch
	 * @return
	 */
	public boolean getStretchBand(String band, String property, String value, Stretch stretch) {
		if (property.startsWith("filter.linearstretchenhancement." + band)) {
			if (property.startsWith("filter.linearstretchenhancement." + band + ".maxValue")) {
				stretch.maxValue = Double.parseDouble(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".minValue")) {
				stretch.minValue = Double.parseDouble(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".offset")) {
				stretch.offset = StringToDoubleArray(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".scale")) {
				stretch.scale = StringToDoubleArray(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".stretchIn")) {
				stretch.stretchIn = StringToDoubleArray(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".stretchOut")) {
				stretch.stretchOut = StringToIntegerArray(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".tailTrimMax")) {
				stretch.tailTrimMax = Double.parseDouble(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".tailTrimMin")) {
				stretch.tailTrimMin = Double.parseDouble(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".tailTrimValueMax")) {
				stretch.tailTrimValueMax = Double.parseDouble(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".tailTrimValueMin")) {
				stretch.tailTrimValueMin = Double.parseDouble(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".functionType")) {
				stretch.functionType = Integer.parseInt(value);
				return true;
			}
			if (property.startsWith("filter.linearstretchenhancement." + band + ".valueFunction")) {
				stretch.valueFunction = Double.parseDouble(value);
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#createFilterListFromStrings(java.util.ArrayList, java.lang.String, int)
	 */
	public int createFilterListFromStrings(ArrayList filters, String fil, int filteri) throws FilterTypeException {
		String pkgBase = "filter.linearstretchenhancement.";
		if (fil.startsWith(pkgBase + "active")) {
			boolean exec = true;
			boolean removeEnds = false;
			if ((RasterFilterListManager.getValue(fil).equals("false")))
				exec = false;
			filters.remove(0);
			int[] renderBands = new int[] { 0, 0, 0 };
			LinearStretchParams stretchParams = new LinearStretchParams();

			for (int propFilter = 0; propFilter < filters.size(); propFilter++) {
				String elem = (String) filters.get(propFilter);
				String value = RasterFilterListManager.getValue(elem);

				if (elem.startsWith("filter.linearstretchenhancement.renderbands")) {
					renderBands = StringToIntegerArray(value);
					continue;
				}

				if (elem.startsWith("filter.linearstretchenhancement.RGB")) {
					stretchParams.rgb = Boolean.parseBoolean(value);
					continue;
				}

				if (elem
						.startsWith("filter.linearstretchenhancement.removeends")) {
					removeEnds = Boolean.parseBoolean(value);
					continue;
				}

				if (getStretchBand("red", elem, value, stretchParams.red))
					continue;
				if (getStretchBand("green", elem, value, stretchParams.green))
					continue;
				if (getStretchBand("blue", elem, value, stretchParams.blue))
					continue;
			}

			filterList.remove(LinearStretchEnhancementFilter.class);
			addEnhancedStretchFilter(stretchParams, stats, renderBands,
					removeEnds);

			LinearStretchEnhancementFilter lsef = (LinearStretchEnhancementFilter) filterList.getFilterByBaseClass(LinearStretchEnhancementFilter.class);
			lsef.setExec(exec);
		}
		return filteri;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		filters.add(LinearStretchEnhancementFilter.class);
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#addFilter(java.lang.Class, org.gvsig.raster.dataset.Params)
	 */
	public void addFilter(Class classFilter, Params params) throws FilterTypeException {
		if (classFilter.equals(LinearStretchEnhancementFilter.class)) {
			int[] renderBands = { 0, 1, 2 };
			boolean removeEnds = false;

			LinearStretchParams leParams = new LinearStretchParams();

			for (int i = 0; i < params.getNumParams(); i++) {
				if (params.getParam(i).id.equals("RenderBands") &&
					params.getParam(i).defaultValue instanceof String) {
					String[] bands = new String((String) params.getParam(i).defaultValue).split(" ");
					renderBands[0] = new Integer(bands[0]).intValue();
					renderBands[1] = new Integer(bands[1]).intValue();
					renderBands[2] = new Integer(bands[2]).intValue();
					continue;
				}

				if (params.getParam(i).id.equals("Remove"))
					removeEnds = ((Boolean) params.getParam(i).defaultValue).booleanValue();

				if (params.getParam(i).id.equals("RGB"))
					leParams.rgb = ((Boolean) params.getParam(i).defaultValue).booleanValue();

				if (params.getParam(i).id.equals("StretchInRed") &&
					params.getParam(i).defaultValue instanceof double[])
					leParams.red.stretchIn = ((double[]) params.getParam(i).defaultValue);

				if (params.getParam(i).id.equals("StretchInGreen") &&
					params.getParam(i).defaultValue instanceof double[])
					leParams.green.stretchIn = ((double[]) params.getParam(i).defaultValue);

				if (params.getParam(i).id.equals("StretchInBlue") &&
						params.getParam(i).defaultValue instanceof double[])
					leParams.blue.stretchIn = ((double[]) params.getParam(i).defaultValue);

				if (params.getParam(i).id.equals("StretchOutRed") &&
					params.getParam(i).defaultValue instanceof int[])
					leParams.red.stretchOut = ((int[]) params.getParam(i).defaultValue);

				if (params.getParam(i).id.equals("StretchOutGreen") &&
					params.getParam(i).defaultValue instanceof int[])
					leParams.green.stretchOut = ((int[]) params.getParam(i).defaultValue);

				if (params.getParam(i).id.equals("StretchOutBlue") &&
					params.getParam(i).defaultValue instanceof int[])
					leParams.blue.stretchOut = ((int[]) params.getParam(i).defaultValue);

				if (params.getParam(i).id.equals("TailTrimRedMin") &&
					params.getParam(i).defaultValue instanceof Double)
					leParams.red.tailTrimMin = ((Double) params.getParam(i).defaultValue).doubleValue();

				if (params.getParam(i).id.equals("TailTrimRedMax") &&
					params.getParam(i).defaultValue instanceof Double)
					leParams.red.tailTrimMax = ((Double) params.getParam(i).defaultValue).doubleValue();

				if (params.getParam(i).id.equals("TailTrimGreenMin") &&
					params.getParam(i).defaultValue instanceof Double)
					leParams.green.tailTrimMin = ((Double) params.getParam(i).defaultValue).doubleValue();

				if (params.getParam(i).id.equals("TailTrimGreenMax") &&
					params.getParam(i).defaultValue instanceof Double)
					leParams.green.tailTrimMax = ((Double) params.getParam(i).defaultValue).doubleValue();

				if (params.getParam(i).id.equals("TailTrimBlueMin") &&
					params.getParam(i).defaultValue instanceof Double)
					leParams.blue.tailTrimMin = ((Double) params.getParam(i).defaultValue).doubleValue();

				if (params.getParam(i).id.equals("TailTrimBlueMax") &&
					params.getParam(i).defaultValue instanceof Double)
					leParams.blue.tailTrimMax = ((Double) params.getParam(i).defaultValue).doubleValue();

				if (params.getParam(i).id.equals("StretchRedFunctionType") &&
						params.getParam(i).defaultValue instanceof Integer)
						leParams.red.functionType = ((Integer) params.getParam(i).defaultValue).intValue();

				if (params.getParam(i).id.equals("StretchRedValueFunction") &&
						params.getParam(i).defaultValue instanceof Double)
						leParams.red.valueFunction = ((Double) params.getParam(i).defaultValue).doubleValue();

				if (params.getParam(i).id.equals("StretchGreenFunctionType") &&
						params.getParam(i).defaultValue instanceof Integer)
						leParams.green.functionType = ((Integer) params.getParam(i).defaultValue).intValue();

				if (params.getParam(i).id.equals("StretchGreenValueFunction") &&
						params.getParam(i).defaultValue instanceof Double)
						leParams.green.valueFunction = ((Double) params.getParam(i).defaultValue).doubleValue();

				if (params.getParam(i).id.equals("StretchBlueFunctionType") &&
						params.getParam(i).defaultValue instanceof Integer)
						leParams.blue.functionType = ((Integer) params.getParam(i).defaultValue).intValue();

				if (params.getParam(i).id.equals("StretchBlueValueFunction") &&
						params.getParam(i).defaultValue instanceof Double)
						leParams.blue.valueFunction = ((Double) params.getParam(i).defaultValue).doubleValue();
			}

			addEnhancedStretchFilter(leParams, (IStatistics) filterList.getEnvParam("IStatistics"), renderBands, removeEnds);
		}
	}
}
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
package org.gvsig.raster.grid.filter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

/*
 * TODO: FUNCIONALIDAD: Anulada las estadísticas. Hay que incluirlas de nuevo.
 */
/**
 * Esta clase es de la parte cliente y es la encargada de la gestión de la pila
 * de filtros. Cada tipo de filtro o conjunto de tipos de filtro deben tener un
 * gestor que implementa IRasterFilterManager. Esos gestores deben registrarse
 * en esta clase con la función addClassListManager. Este registro puede hacerse
 * desde esta misma clase o desde una extensión. Un cliente que desee aplicar un
 * filtro deberá introducirlo en la lista usando para ello las funciones que su
 * manager de filtros le ofrece. Además se encarga de otras funciones como la
 * conversión de un filtro a cadena de Strings y la recuperación desde cadena de
 * Strings y el control de tipos de la salida de un raster de la lista con la
 * entrada del siguiente filtro.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RasterFilterListManager {
	protected RasterFilterList	rasterFilterList = null;
	private boolean				debug = false;
	protected ArrayList			filterList = null;
	private ArrayList			managers = new ArrayList();

	/**
	 * Constructor
	 * @param filterStack
	 */
	public RasterFilterListManager(RasterFilterList filterStack) {
		this.rasterFilterList = filterStack;

		// Cargamos el manager con los gestores de drivers registrados
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.get("RasterFilter");
		Iterator iterator = point.iterator();
		while (iterator.hasNext()) {
			ExtensionPoint.Extension entry = (ExtensionPoint.Extension) iterator
					.next();
			if (entry != null) {
				Class RasterClass = entry.getExtension();
				Object obj = RasterFilterListManager.loadClass(RasterClass, this);
				if (obj != null)
					managers.add(obj);
			}
		}
	}

	/**
	 * Controla que los tipos de los filtros de la pila sean correctos, es decir,
	 * que el tipo de salida de un filtro de salida coincida con el tipo de la
	 * entrada del siguiente. En caso de no ser así crea el filtro de tipo
	 * adecuado y lo sustituye en el no coincidente. Esto es necesario ya que en
	 * la eliminación de filtros puede quedarse en inconsistencia de tipos.
	 */
	public void controlTypes() throws FilterTypeException {
		ArrayList exceptions = new ArrayList();
		for (int i = 0; i < rasterFilterList.lenght(); i++) {
			String classFilter = null, packageFilter = null, oldClass = null;
			try {
				RasterFilter rf = rasterFilterList.get(i);
				if (rf == null)
					return;
				classFilter = rf.getClass().toString();
				packageFilter = classFilter.substring(classFilter.indexOf(" ") + 1, classFilter.lastIndexOf("."));
				oldClass = classFilter.substring(classFilter.lastIndexOf(".") + 1, classFilter.length());
			} catch (ArrayIndexOutOfBoundsException ex) {
				return;
			} catch (NullPointerException ex) {
				return;
			}

			// Para el primer filtro comprobamos con el tipo de dato de entrada a la pila
			if (i == 0) {
				if (rasterFilterList.getInitDataType() != rasterFilterList.get(i).getInRasterDataType()) {
					Pattern p = Pattern.compile(RasterUtilities.typesToString(rasterFilterList.get(i).getInRasterDataType()));
					Matcher m = p.matcher(oldClass);
					String newClass = m.replaceAll(RasterUtilities.typesToString(rasterFilterList.getInitDataType()));
					String strPackage = packageFilter + "." + newClass;

					renewFilterFromControlTypes(strPackage, i, exceptions);
				}

				// Desde el filtro 2 en adelante se compara la salida de uno con la entrada del siguiente
			} else if (rasterFilterList.get(i - 1).getOutRasterDataType() != rasterFilterList.get(i).getInRasterDataType()) {
				Pattern p = Pattern.compile(RasterUtilities.typesToString(rasterFilterList.get(i).getInRasterDataType()));
				Matcher m = p.matcher(oldClass);
				String newClass = m.replaceAll(RasterUtilities.typesToString(rasterFilterList.get(i - 1).getOutRasterDataType()));
				String strPackage = packageFilter + "." + newClass;

				renewFilterFromControlTypes(strPackage.trim(), i, exceptions);
			}
		}

		if (exceptions.size() > 0)
			throw new FilterTypeException("");

		if (debug)
			rasterFilterList.show();
	}

	/**
	 * Reemplaza un filtro segun su nombre a la lista de filtros, util para cambiar
	 * el tipo de datos de un filtro en la pila de filtros.
	 * @param nameFilter
	 * @param pos
	 * @param exceptions
	 */
	private void renewFilterFromControlTypes(String nameFilter, int pos, ArrayList exceptions) {
		try {
			RasterFilter newFilter = RasterFilter.createFilter(nameFilter);
			newFilter.params = rasterFilterList.get(pos).params;
			if (newFilter.params.get("filterName") != null)
				newFilter.setName((String) newFilter.params.get("filterName"));
			else
				newFilter.setName(rasterFilterList.get(pos).getName());
			rasterFilterList.replace(newFilter, pos);
		} catch (FilterTypeException e) {
			exceptions.add(e);
		}
	}

	/**
	 * Método que devuelve true si el tipo de filtro pasado por parámetro está en
	 * la pila y false si no lo está.
	 * @param filter Tipo de filtro a comprobar
	 * @return true si está en la pila y false si no lo está
	 */
	public boolean isActive(String name) {
		return rasterFilterList.isActive(name);
	}

	/**
	 * Elimina los filtros de la pila de un determinado tipo
	 *
	 * @param type Tipo de filtro a eliminar
	 * @throws FilterTypeException
	 */
	public void removeFilter(String name) throws FilterTypeException {
		rasterFilterList.remove(name);
		this.controlTypes();
	}

	public ArrayList getStringsFromFilterList() {
		filterList = new ArrayList();
		for (int i = 0; i < rasterFilterList.lenght(); i++) {
			RasterFilter rf = rasterFilterList.get(i);

			// Se recorren todos los managers registrados comprobando si corresponde a
			// la clase del filtro
			for (int j = 0; j < managers.size(); j++)
				filterList = ((IRasterFilterListManager) managers.get(j)).getStringsFromFilterList(filterList, rf);
		}

		return filterList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#createStackFromStrings(java.util.ArrayList, java.lang.String, int)
	 */
	public int createStackFromStrings(ArrayList filters, String fil, int filteri) {
		return filteri;
	}

	/**
	 * Crea una pila de filtros a partir de un Array de Strings. Cada elemento del array debe
	 * tener la forma elemento=valor.
	 * @param filters
	 * @throws FilterTypeException
	 */
	public void createFilterListFromStrings(ArrayList f) throws FilterTypeException {
		createFilterListFromStrings(f, new Integer(0));
	}

	/**
	 * Crea una pila de filtros a partir de un Array de Strings. Cada elemento del array debe
	 * tener la forma elemento=valor.
	 * @param pos Posición desde la cual se empieza a analizar.
	 * @param filters
	 * @throws FilterTypeException
	 */
	private void createFilterListFromStrings(ArrayList f, Integer pos) throws FilterTypeException {
		ArrayList filters = (ArrayList) f.clone();
		rasterFilterList.clear();

		int filteri = pos.intValue();

		// Busca un filtro activo y después todas las propiedades que necesita ese
		// filtro para ser creado. Una vez las tiene añade en la pila el tipo de
		// filtro.
		while ((filters.size() > 0) && (filteri < filters.size())) {
			String fil = (String) filters.get(filteri);

			for (int j = 0; j < managers.size(); j++)
				try {
					filteri = ((IRasterFilterListManager) managers.get(j)).createFilterListFromStrings(filters, fil, filteri);
				} catch (NullPointerException e) {
				}

			filteri++;
		}
	}

	/**
	 * Obtiene el elemento de una cadena de la forma elemento=valor
	 * @param cadena
	 * @return
	 */
	public static String getElem(String cadena) {
		if (cadena != null)
			return cadena.substring(0, cadena.indexOf("="));
		else
			return null;
	}

	/**
	 * Obtiene el valor de una cadena de la forma elemento=valor
	 * @param cadena
	 * @return
	 */
	public static String getValue(String cadena) {
		if (cadena != null)
			return cadena.substring(cadena.indexOf("=") + 1, cadena.length());
		else
			return null;
	}

	/**
	 * Convierte un rango contenido en una array doble en una cadena de strings
	 * para poder salvar a xml
	 * @param rang
	 * @return
	 */
	/*private String rangeToString(int[][] rang) {
		StringBuffer rangoStr = new StringBuffer();

		if (rang != null) {
			for (int i = 0; i < rang.length; i++) {
				rangoStr.append(String.valueOf(rang[i][0]) + ":");
				rangoStr.append(String.valueOf(rang[i][1]) + ":");
			}

			String r = rangoStr.toString();

			if (r.endsWith(":")) {
				r = r.substring(0, r.length() - 1);
			}

			return r;
		} else {
			return null;
		}
	}*/

	/**
	 * Convierte una cadena en una lista de rangos numericos para poder asignar
	 * transparencias a la imagen
	 * @param rang
	 * @return
	 */
	/*private int[][] stringToRange(String rang) {
		if ((rang != null) && !rang.equals("null")) {
			ArrayList lista = new ArrayList();
			StringTokenizer tokenizer = new StringTokenizer(rang, ":");

			while (tokenizer.hasMoreTokens())
				lista.add(tokenizer.nextToken());

			int[][] intervalos = new int[(int) (lista.size() / 2)][2];

			for (int i = 0; i < lista.size(); i = i + 2) {
				intervalos[i / 2][0] = Integer.valueOf((String) lista.get(i)).intValue();
				intervalos[i / 2][1] = Integer.valueOf((String) lista.get(i + 1)).intValue();
			}

			return intervalos;
		} else {
			return null;
		}
	}*/

	/**
	 * Obtiene la lista de filtros
	 * @return RasterFilterList
	 */
	public RasterFilterList getFilterList() {
		return rasterFilterList;
	}

	/**
	 * Carga una clase pasada por parámetro. Como argumento del constructor de la
	 * clase se pasará un RasterFilterStackManager. Esto es usado para instanciar
	 * los gestores de filtros registrados
	 * @param clase Clase a instanciar
	 * @param stackManager Parámetro del constructor de la clase a instanciar
	 * @return Objeto que corresponde a la instancia de la clase pasada.
	 */
	public static Object loadClass(Class clase, RasterFilterListManager stackManager) {
		Object obj = null;
		Class[] args = { RasterFilterListManager.class };
		try {
			Constructor hazNuevo = clase.getConstructor(args);
			Object[] args2 = { stackManager };
			obj = hazNuevo.newInstance(args2);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * Obtiene el manager registrado a partir de la clase
	 * @return
	 */
	public IRasterFilterListManager getManagerByClass(Class c) {
		for (int j = 0; j < managers.size(); j++)
			if (managers.get(j).getClass().equals(c))
				return (IRasterFilterListManager) managers.get(j);
		return null;
	}

	/**
	 * Obtiene el manager registrado a partir de la clase de un filtro
	 * @return
	 */
	public IRasterFilterListManager getManagerByFilterClass(Class c) {
		for (int i = 0; i < managers.size(); i++)
			for (int j = 0; j < ((IRasterFilterListManager) managers.get(i)).getRasterFilterList().size(); j++)
				if (((IRasterFilterListManager) managers.get(i)).getRasterFilterList().get(j).equals(c))
					return (IRasterFilterListManager) managers.get(i);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilterListManager#getRasterFilterList()
	 */
	public ArrayList getRasterFilterList() {
		ArrayList filters = new ArrayList();
		for (int i = 0; i < managers.size(); i++) {
			ArrayList auxFilters = ((IRasterFilterListManager) managers.get(i)).getRasterFilterList();
			for (int j = 0; j < auxFilters.size(); j++)
				filters.add(auxFilters.get(j));
		}
		return filters;
	}
}
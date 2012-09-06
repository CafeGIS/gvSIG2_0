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

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;

import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.datastruct.Transparency;
/**
 * Esta clase representa la lista de filtros que debe ser manejada desde el
 * RasterFilterListManager.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RasterFilterList {
	private IBuffer          rasterBuf            = null;
	private IBuffer          alphaBand            = null;
	private int              typeFilter           = -1;
	private TreeMap          environment          = new TreeMap();

	// Pila de objetos Filter (Contiene un RasterFilter)
	private ArrayList        list                 = new ArrayList();
	private Stack            status               = new Stack();
	/**
	 * Array de listeners que serán informados cuando cambia la lista de filtros
	 */
	private ArrayList        filterListListener   = new ArrayList();

	/**
	 * Asigna un listener a la lista que será informado cuando cambie un
	 * filtro o posición en la lista. 
	 * @param listener FilterListListener
	 */
	public void addFilterListListener(FilterListChangeListener listener) {
		filterListListener.add(listener);
	}
	
	/**
	 * Método llamado cuando hay un cambio en una propiedad de visualización
	 */
	private void callFilterListChanged(Object obj) {
		for (int i = 0; i < filterListListener.size(); i++) {
			FilterListChangeEvent ev = new FilterListChangeEvent(obj);
			((FilterListChangeListener)filterListListener.get(i)).filterListChanged(ev);
		}
	}
	
	/**
	 * Añade un parámetro a la lista de filtros. Estos parámetros luego pueden ser
	 * utilizados por los managers que se registren
	 * @param key Nombre del parámetro que coincide con el nombre de la clase.
	 * @param value Objeto
	 */
	public void addEnvParam(String key, Object value) {
		environment.put(key, value);
	}

	/**
	 * Obtiene un parámetro de la lista de filtros.
	 * @param key Identificador del parámetro. Coincide con el nombre de la clase del parámetro.
	 */
	public Object getEnvParam(String key) {
		return environment.get(key);
	}

	/**
	 * Controla que los tipos de entrada y salida de los filtros sean los
	 * correctos
	 * @throws FilterTypeException
	 */
	public void controlTypes() throws FilterTypeException {
		RasterFilterListManager stackManager = new RasterFilterListManager(this);
		stackManager.controlTypes();
	}

	/**
	 * Añade un filtro al final de la lista
	 * @param filter        filtro añadido
	 * @throws FilterTypeException
	 */
	public void add(RasterFilter filter) throws FilterTypeException {
		if (isActive(filter.getName())) {
			replace(filter, filter.getName());
		} else {
			list.add(filter);
			controlTypes();
		}
		filter.setEnv(environment);
		callFilterListChanged(this);
	}

	/**
	 * Sustituye un filtro de una posición de la pila por otro
	 * @param filter
	 * @param i
	 * @throws FilterTypeException
	 */
	public void replace(RasterFilter filter, String name) throws FilterTypeException {
		boolean changed = false;
		filter.setEnv(environment);
		for (int i = list.size() - 1; i >= 0; i--)
			if (((RasterFilter) list.get(i)).getName().equals(name)) {
				list.remove(i);
				list.add(i, filter);
				changed = true;
			}

		if (changed)
			controlTypes();
		callFilterListChanged(this);
	}

	/**
	 * Añade un filtro en la lista en la posición indicada.
	 * @param filter        filtro añadido
	 * @param pos	posición
	 * @throws FilterTypeException
	 */
	public void add(RasterFilter filter, int pos) throws FilterTypeException {
		try {
			list.add(pos, filter);
			controlTypes();
		} catch (IndexOutOfBoundsException e) {
			add(filter);
		}
		filter.setEnv(environment);
		callFilterListChanged(this);
	}

	/**
	 * Elimina un filtro a partir de su nombre
	 * @param name Nombre del filtro a eliminar
	 * @throws FilterTypeException
	 */
	public void remove(String name) throws FilterTypeException {
		boolean changed = false;
		for (int i = list.size() - 1; i >= 0; i--)
			if (((RasterFilter) list.get(i)).getName().equals(name)) {
				list.remove(i);
				changed = true;
			}
		if (changed)
			controlTypes();
		callFilterListChanged(this);
	}

	/**
	 * Elimina un filtro por clase.
	 *
	 * @param baseFilterClass
	 * @throws FilterTypeException
	 */
	public void remove(Class baseFilterClass) throws FilterTypeException {
		boolean changed = false;
		for (int i = 0; i < lenght(); i++)
			if (baseFilterClass.isInstance((RasterFilter) list.get(i))) {
				list.remove(i);
				i--;
				changed = true;
			}
		if (changed)
			controlTypes();
		callFilterListChanged(this);
	}

	/**
	 * Devuelve el tipo de dato de retorno al aplicar la pila de filtros
	 * @return
	 */
	public int getOutDataType() {
		if (list.size() > 0)
			return ((RasterFilter) list.get(list.size() - 1)).getOutRasterDataType();
		else
			return rasterBuf.getDataType();
	}

	/**
	 * Devuelve el raster resultado de la aplicacion de la pila de filtros
	 * @return
	 */
	public IBuffer getResult() {
		return rasterBuf;
	}
	
	/**
	 * Devuelve la banda alpha para los filtros que generan una
	 * @return
	 */
	public IBuffer getAlphaBand() {
		return alphaBand;
	}

	/**
	 * Obtiene la cantidad de filtros en la lista
	 * @return Número de filtros apilados
	 */
	public int lenght() {
		return list.size();
	}

	/**
	 * Obtiene el filtro apilado de la posición i o null si el indice es incorrecto
	 * @param i        Posición a acceder en la pila
	 * @return        Filtro
	 */
	public RasterFilter get(int i) {
		if (i >= list.size() || i < 0)
			return null;
		return (RasterFilter) list.get(i);
	}

	/**
	 * Obtiene el filtro apilado de nombre name o null si no existe
	 * @param i       Nombre del filtro buscado
	 * @return        Filtro
	 */
	public RasterFilter get(String name) {
		for (int i = list.size() - 1; i >= 0; i--) {
			if (((RasterFilter) list.get(i)).getName().equals(name))
				return (RasterFilter) list.get(i);
		}
		return null;
	}

	/**
	 * Obtiene el filtro apilado que corresponde con el nombre
	 * @param name	Nombre de filtro
	 * @return      Filtro en caso de que exista un filtro apilado de ese tipo
	 * o null si no hay ninguno.
	 */
	public RasterFilter getByName(String name) {
		for (int i = 0; i < lenght(); i++) {
			if (((RasterFilter) list.get(i)).getName().equals(name))
				return (RasterFilter) list.get(i);
		}
		return null;
	}

	/**
	 * Obtiene el primer filtro de la lista que es instancia de la clase pasada por
	 * parámetro
	 * @param baseFilterClass Filtro base
	 * @return RasterFilter
	 */
	public RasterFilter getFilterByBaseClass(Class baseFilterClass) {
		for (int i = 0; i < lenght(); i++) {
			if (baseFilterClass.isInstance((RasterFilter) list.get(i)))
				return (RasterFilter) list.get(i);
		}
		return null;
	}

	/**
	 * Obtiene el tipo del filtro de la pila de la posición i
	 * @param i Posición a acceder en la pila
	 * @return tipo de filtro
	 */
	public String getName(int i) {
		return ((RasterFilter) list.get(i)).getName();
	}

	/**
	 * Elimina todos los filtros de la pila
	 */
	public void clear() {
		list.clear();
		callFilterListChanged(this);
	}

	/**
	 * Sustituye un filtro de una posición de la pila por otro
	 * @param filter
	 * @param i
	 */
	public void replace(RasterFilter filter, int i) {
		filter.setEnv(environment);
		list.remove(i);
		list.add(i, filter);
		callFilterListChanged(this);
	}

	/**
	 * Move un filtro determinado a la posición especificada.
	 * @param filter Filtro a mover
	 * @param position Posición a asignar
	 * @return Devuelve true si el filtro existia y lo ha movido y false si no lo ha hecho.
	 */
	public boolean move(Class filter, int position) {
		RasterFilter f = null;
		for (int i = 0; i < list.size(); i++) {
			if(filter.isInstance(list.get(i))) {
				f = (RasterFilter) list.get(i);
				list.remove(i);
				break;
			}
		}
		if(f != null) {
			list.add(position, f);
			return true;
		}
		return false;
	}

	/**
	 * Asigna el raster de entrada inicial
	 * @param raster
	 */
	public void setInitRasterBuf(IBuffer raster) {
		rasterBuf = (IBuffer) raster;
		if(rasterBuf != null)
			typeFilter = rasterBuf.getDataType();
	}

	/**
	 * Devuelve el tipo de datos inicial de la lista
	 * @return Tipo de dato del raster inicial
	 */
	public int getInitDataType() {
		return typeFilter;
	}

	/**
	 * Asigna el tipo de dato inicial
	 * @param dt
	 */
	public void setInitDataType(int dt) {
		this.typeFilter = dt;
	}

	/**
	 * Método que devuelve true si el tipo de filtro pasado por parámetro está en la
	 * pila y false si no lo está.
	 * @param type        Tipo de parámetro a comprobar
	 * @return true si está en la pila y false si no lo está
	 */
	public boolean isActive(String name) {
		for (int i = list.size() - 1; i >= 0; i--) {
			if (((RasterFilter) list.get(i)).getName().equals(name))
				return true;
		}
		return false;
	}

	/**
	 * Método que devuelve true si el tipo de filtro pasado por parámetro está en
	 * la pila y false si no lo está.
	 *
	 * @param filter Tipo de filtro a comprobar
	 * @return true si está en la pila y false si no lo está
	 */
	public boolean isActive(RasterFilter filter) {
		for (int i = list.size() - 1; i >= 0; i--) {
			if (((RasterFilter) list.get(i)).equals(filter)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve la posición en la lista de una clase de filtro concreta
	 *
	 * @param c Clase a buscar en la lista
	 * @return posición en la lista
	 */
	public int getPosition(Class c) {
		for (int i = 0; i < list.size(); i++)
			if (c.isInstance(list.get(i)))
				return i;
		return -1;
	}

	/**
	 * Aplica los filtros de la pila sobre el buffer correspondiente
	 * @param dataType
	 * @throws InterruptedException
	 */
	private void executeFilterByDataType(int dataType) throws InterruptedException {
		environment.put("FirstUseTransparency", Boolean.TRUE);
		environment.put("HasNoDataFilter", Boolean.valueOf(isActive("nodata")));
		
		environment.put("FirstRaster", rasterBuf);
		alphaBand = null;
		for (int i = 0; i < list.size(); i++) {
			RasterFilter filter = (RasterFilter) list.get(i);

			// TODO: Arquitectura. Quitar el ControlTypes y en este momento
			// cerciorarse de si el tipo del filtro es totalmente el correcto o hay
			// que recrearlo. Ejemplo:
			// Si el filtro que tenemos antes de preprocesar es de tipo Byte y la
			// entrada de datos es de tipo float, reconstruir solo este filtro para
			// que sea de tipo float

			filter.addParam("raster", rasterBuf);
			filter.execute();

			if (filter.getResult("raster") != null)
				this.rasterBuf = (IBuffer) filter.getResult("raster");
			
			//Si el filtro genera una banda alpha se mezcla con la que han generado otros
			if (filter.getResult("alphaBand") != null) {
				if(alphaBand != null)
					alphaBand = Transparency.merge(alphaBand, (IBuffer)filter.getResult("alphaBand"));
				else 
					alphaBand = (IBuffer)filter.getResult("alphaBand");
			}
		}
		environment.remove("FirstRaster");
	}

	/**
	 * Aplica los filtros sobre un RasterBuf
	 * @return IBuffer de salida
	 * @throws InterruptedException
	 */
	public IBuffer execute() throws InterruptedException {
		if (rasterBuf == null)
			return null;
		executeFilterByDataType(rasterBuf.getDataType());
		return rasterBuf;
	}

	/**
	 * Muestra el contenido de la pila de filtros para depuración
	 */
	public void show() {
		System.out.println("--------------------------------------------");

		for (int i = 0; i < list.size() ; i++) {
			System.out.println("FILTRO:" + i + " NAME:" + ((RasterFilter) list.get(i)).getName() + " FIL:" + ((RasterFilter) list.get(i)).toString());
		}
	}

	public void resetPercent() {
		for (int i = 0; i < list.size(); i++)
			((RasterFilter) list.get(i)).resetPercent();
	}

	public int getPercent() {
		int percent = 0;
		if (list.size() == 0)
			return 0;
		for (int i = 0; i < list.size(); i++)
			percent += ((RasterFilter) list.get(i)).getPercent();

		percent = percent / list.size();
		return percent;
	}

	/**
	 * Guarda el estado de la lista de filtros en una pila, que se podrá
	 * ir recuperando con popStatus()
	 */
	public void pushStatus() {
		status.push(getStatusCloned());
	}

	/**
	 * Obtiene el estado actual de los filtros, el ArrayList devuelto es una
	 * clonación del original, asi no compartiran datos.
	 * @return
	 */
	public ArrayList getStatusCloned() {
		ArrayList newArray = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			try {
				newArray.add(((RasterFilter) list.get(i)).clone());
			} catch (CloneNotSupportedException e) {
				System.out.println("No se ha podido clonar");
			}
		}
		return newArray;
	}

	/**
	 * Define el estado actual de los filtros
	 * @param newArray
	 */
	public void setStatus(ArrayList newArray) {
		list.clear();
		if(newArray == null)
			return;
		for (int i = 0; i < newArray.size(); i++) {
			list.add(newArray.get(i));
		}
		callFilterListChanged(this);
	}

	/**
	 * Recupera un estado guardado con antelación mediante el método pushStatus()
	 */
	public void popStatus() {
		if (status.size() <= 0)
			return;

		setStatus((ArrayList) status.pop());
	}

	/**
	 * Obtiene el TreeMap con los parámetros
	 * @return TreeMap
	 */
	public TreeMap getEnv() {
		return environment;
	}

	/**
	 * Asigna el TreeMap con los parámetros del entorno
	 * @param env
	 */
	public void setEnv(TreeMap env) {
		this.environment = env;
	}
}
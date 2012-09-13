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
package org.gvsig.raster.util;

import java.util.ArrayList;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.grid.render.Rendering;

/**
 * Clase para almacenar estados visuales de una capa raster. Estos estados deben ser locales
 * a cada funcionalidad para que no interfieran entre ellos. Luego es posible tener una lista de
 * estados globales para restaurar el que nos convenga.
 * 07/10/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class LayerVisualStatusList {
	private ArrayList list = new ArrayList();
	
	/**
	 * Clase que contiene el estado visual de una capa raster.
	 * 07/10/2008
	 * @author Nacho Brodin nachobrodin@gmail.com
	 */
	public class LayerVisualStatus {
		private ArrayList         filterStatus = null;
		private GridTransparency  transparency = null;
		
		/**
		 * Obtiene el estado de la lista de filtros
		 * @return Array con el estado de la lista de filtros
		 */
		public ArrayList getFilterStatus() {
			return filterStatus;
		}
		
		/**
		 * Asigna el estado de la lista de filtros
		 * @return Array con el estado de la lista de filtros
		 */
		public void setFilterStatus(ArrayList filterStatus) {
			this.filterStatus = filterStatus;
		}
		
		/**
		 * Obtiene el estado de la transparencia
		 * @return GridTransparency
		 */
		public GridTransparency getTransparency() {
			return transparency;
		}
		
		/**
		 * Asigna el estado de la transparencia
		 * @param GridTransparency
		 */
		public void setTransparency(GridTransparency transparency) {
			this.transparency = transparency;
		}
	}
	
	/**
	 * Limpia la pila de elementos
	 */
	public void clear() {
		list.clear();
	}
	
	/**
	 * Salva un estado al final de la lista
	 * @param status 
	 */
	public void add(LayerVisualStatus status) {
		list.add(status);
	}
	
	/**
	 * Recupera el último estado introducido en la lista
	 * @return StatusLayer
	 */
	public LayerVisualStatus getLast() {
		return (LayerVisualStatus)list.get(list.size() - 1);
	}
	
	/**
	 * Recupera el estado de la posición i
	 * @return StatusLayer
	 */
	public LayerVisualStatus get(int i) {
		return (LayerVisualStatus)list.get(i);
	}
		
	
	/**
	 * Saca de la lista el último estado y lo asigna a la capa indicada.
	 * @param lyr Capa raster
	 */
	public void restoreVisualStatus(FLyrRasterSE lyr) {
		Rendering rendering = lyr.getRender();
		LayerVisualStatus status = getLast();
		if(status != null) {
			if(rendering.getFilterList() != null)
				rendering.getFilterList().setStatus(status.filterStatus);
			lyr.getRender().setLastTransparency(status.transparency);
		}
	}
	
	/**
	 * Obtiene de la capa su estado de visualización y lo salva en la lista
	 * @param lyr Capa raster
	 */
	public void getVisualStatus(FLyrRasterSE lyr) {
		LayerVisualStatus status = new LayerVisualStatus();
		status.transparency = lyr.getRenderTransparency();
		status.filterStatus = lyr.getRenderFilterList().getStatusCloned();
		add(status);
	}
}

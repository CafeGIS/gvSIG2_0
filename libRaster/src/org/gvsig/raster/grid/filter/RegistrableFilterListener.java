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
package org.gvsig.raster.grid.filter;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.JPanel;

import org.gvsig.raster.dataset.Params;

/**
 * Clase de la que deben heredar todos los paneles para filtros 
 * definidos por el usuario. Tiene los métodos para el registro de listeners
 * 
 * 28/09/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class RegistrableFilterListener extends JPanel {
	private static final long  serialVersionUID = 5528469123516861351L;
	private ArrayList          actionCommandListeners = new ArrayList();
	protected Params           params = null;
	
	/**
	 * Borrar el disparador de eventos de los botones.
	 * @param listener
	 */
	public void removeStateChangedListener(FilterUIListener listener) {
		actionCommandListeners.remove(listener);
	}

	
	/**
	 * 
	 * Ejecución del método actionValuesCompleted en todos los filtros registrados
	 */
	protected void callStateChanged() {
		Iterator acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			FilterUIListener listener = (FilterUIListener) acIterator.next();
			listener.actionValuesCompleted(new EventObject(this));
		}
	}
	
	/**
	 * Añadir el listener para los paneles definidos por el usuario
	 * @param listener
	 */
	public void addFilterUIListener(FilterUIListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}
	
	/**
	 * Obtiene los parámetros cargados por el panel
	 * @return
	 */
	public Params getParams() {
		return params;
	}
}

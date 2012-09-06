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
package org.gvsig.gui.beans.treelist.listeners;

import org.gvsig.gui.beans.treelist.event.TreeListEvent;

/**
 * Interfaz que contiene los eventos del componente TreeList
 * Nacho Brodin (brodin_ign@gva.es)
 */

public interface TreeListComponentListener {
	/**
	 * Este método es ejecutado cuando se inserta un nuevo elemento en la lista
	 * @param e TreeListEvent
	 */
	public void elementAdded(TreeListEvent e);
	
	/**
	 * Este método es ejecutado cuando se elimina un elemento de la lista
	 * @param e TreeListEvent
	 */
	public void elementRemoved(TreeListEvent e);
	
	/**
	 * Este método es ejecutado cuando se mueve un elemento de la lista
	 * @param e TreeListEvent
	 */
	public void elementMoved(TreeListEvent e);
}
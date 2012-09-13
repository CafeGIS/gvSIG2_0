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
package org.gvsig.georeferencing.ui.zoom;

import java.util.EventListener;

/**
 * Interfaz que informa de eventos de acciones que se están produciendo
 * o que se han producido sobre la vista
 * 31/01/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public interface ViewListener extends EventListener {
	
	/**
	 * Evento que se produce cuando se añade una herramienta sobre la vista
	 * @param ev ViewEvent
	 */
	public void addingTool(ViewEvent ev);
		
	/**
	 * Evento producido cuando se empieza a dibujar
	 * @param ev ViewEvent
	 */
	public void startDraw(ViewEvent ev);
	
	/**
	 * Evento producido cuando se empieza a dibujar
	 * @param ev ViewEvent
	 */
	public void endDraw(ViewEvent ev);
	
	/**
	 * Informa de los cambios sobre el zoom de la vista
	 * @param ev
	 */
	public void zoomViewChanged(ViewEvent ev);
}

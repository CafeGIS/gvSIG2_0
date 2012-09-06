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
package org.gvsig.raster.process;

import java.util.EventObject;
/**
 * Clase que deben contener los procesos de raster. Un proceso debe registrarse
 * al arrancar y eliminarse al terminar o ser cancelado. El proceso debe ser
 * el encargado de realizar estas acciones.
 * 
 * @version 30/08/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RasterTask {
	public EventObject activeEvent = null;
	public Object      process     = null;
	private String     idThread    = "";

	/**
	 * Asigna la clase que contiene el proceso.
	 * @param process
	 */
	public RasterTask(Object process) {
		idThread = Thread.currentThread().toString();
		this.process = process;
	}
	/**
	 * Asigna el evento
	 * @param ev EventObject
	 */
	public void setEvent(EventObject ev) {
		activeEvent = ev;
	}
	
	/**
	 * Obtiene el evento
	 * @return EventObject
	 */
	public EventObject getEvent() {
		return activeEvent;
	}
	
	/**
	 * Obtiene el identificador único del proceso. 
	 * @return Identificador del proceso
	 */
	public String getID() {
		return idThread;
	}
	
	/**
	 * Gestión de las señales. Este método define las acciones por defecto para
	 * cada señal. No es necesario utilizar estas. Se puede en cada caso, hacer un método
	 * manageEvent en el que se gestione las acciones concretas de una señal.
	 * 
	 * @param ev Evento a gestionar
	 */
	public  void manageEvent(EventObject ev) throws InterruptedException {
		if (ev instanceof CancelEvent)
			throw new InterruptedException("Proceso cancelado por señal del usuario.");
	}
}
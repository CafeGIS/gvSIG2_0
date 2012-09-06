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

import java.util.ArrayList;
import java.util.EventObject;
import java.util.TreeMap;

/**
 * Clase donde se registran todos los procesos raster para poder ser accedidos por distintos
 * objetos y puedan mandarles señales.
 * 
 * @version 30/08/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class RasterTaskQueue {
	
	private static TreeMap         processPool = new TreeMap();
	private static ArrayList       processID = new ArrayList();
	
	/**
	 * Registra procesos para que puedan ser accedidos
	 * @param id Identificador del proceso
	 * @param process Proceso
	 */
	public static void register(RasterTask process) {
		processPool.put(process.getID(), process);
		processID.add(process.getID());
	}	
	
	/**
	 * Saca un proceso del repositorio
	 * @param id Identificador del proceso.
	 */
	public static void remove(RasterTask process) {
		processPool.remove(process.getID());
		for (int i = 0; i < processID.size(); i++) {
			if(((String)processID.get(i)).compareTo(process.getID()) == 0) {
				processID.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Obtiene un proceso a partir de su identificador
	 * @param id Identificador del proceso
	 * @return Proceso
	 */
	public static RasterTask get(String id) {
		return ((RasterTask)processPool.get(id) == null) ? new RasterTask(null) : (RasterTask)processPool.get(id);
	}
	
	/**
	 * Envia un evento al proceso cuyo id coincide con el parámetro
	 * @param id Identificador del proceso
	 * @param ev Evento
	 */
	public static void sendSignal(String id, EventObject ev) {
		Object obj = processPool.get(id);
		if(obj != null && obj instanceof RasterTask) 
			((RasterTask)obj).setEvent(ev);
	}
	
	/**
	 * Envia un evento a todos los procesos
	 * @param id Identificador del proceso
	 * @param ev Evento
	 */
	public static void sendSignal(EventObject ev) {
		for (int i = 0; i < processID.size(); i++) {
			String id = (String)processID.get(i);
			sendSignal(id, ev);
		}
	}
	
}

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
package org.gvsig.raster;

import org.gvsig.raster.util.Queue;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Cola de procesos de ejecución exclusiva. Los procesos de esta lista se irán ejecutando
 * por orden de llegada impidiendo que se ejecuten dos al mismo tiempo.
 * 
 * 16/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class UniqueProcessQueue implements IProcessActions {
	private static final int          LAPSE_TIME       = 1000;

	private Queue                     queue            = new Queue();
	private RasterProcess             executionProcess = null;
	static private UniqueProcessQueue singleton        = new UniqueProcessQueue();

	/**
	 * Dejamos el constructor privado para que nadie pueda referenciarlo
	 */
	private UniqueProcessQueue() {}
	
	/**
	 * Devuelve una instancia al unico objeto de UniqueProcessQueue que puede existir.
	 * @return
	 */
	static public UniqueProcessQueue getSingleton() {
		return singleton;
	}
	
	/**
	 * Añade un proceso a la cola.
	 * @param id Identificador del proceso
	 * @param process Proceso
	 */
	public synchronized void add(RasterProcess process) {
		queue.put(process);
		process.setUniqueProcessActions(this);
		
		//Decisión de arranque
		if(queue.size() >= 1 && executionProcess == null)
			start();
	}
	
	/**
	 * Pone en marcha el primer proceso de la lista
	 */
	public void start() {
		nextProcess();
	}
	
	private void nextProcess() {
		executionProcess = ((RasterProcess) queue.get());
		if (executionProcess != null)
			executionProcess.start();
	}

	/**
	 * Evento de finalización de un proceso. Cuando un proceso acaba se 
	 * pone en marcha el siguiente de la lista si existe. Se asigna un tiempo de latencia para
	 * la ejecución del siguiente marcado por LAPSE_TIME.  
	 */
	public void end(Object param) {
		try {
			Thread.sleep(LAPSE_TIME);
		} catch (InterruptedException e) {
			RasterToolsUtil.debug("sleep Exception", null, e);
		}
		nextProcess();
	}
	
	/**
	 * How many elements are there in this queue?
	 */
	public int size() {
		return queue.size();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#interrupted()
	 */
	public void interrupted() {}	
}
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

/**
 * Acciones que puede ejecutar un proceso sobre un objeto externo. Un proceso
 * debe ser independiente de objetos que sean dependientes de su funcionalidad. Es
 * decir, cualquier extensión debe poder usar un proceso y este ejecutar metodos
 * de la funcionalidad. Esto se hace ha través de este interfaz.
 * 
 * @version 26/09/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface IProcessActions {
	
	/**
	 * El proceso comunica que ha sido interrumpido
	 *
	 */
	public void interrupted();

	/**
	 * Acciones de finalización del proceso
	 */
	public void end(Object param);
}

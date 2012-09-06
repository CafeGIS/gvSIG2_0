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
package org.gvsig.raster.dataset;

/**
 * Excepción cuando un fichero no se encuentra en una lista en la
 * que se está buscando y dicha falta constituye una situación de
 * error.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class FileNotFoundInListException extends Exception{
	private static final long serialVersionUID = 1557651260026647067L;

	public FileNotFoundInListException(String msg){
		super(msg);
	}
}
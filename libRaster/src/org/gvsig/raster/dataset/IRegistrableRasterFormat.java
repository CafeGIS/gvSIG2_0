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
 * Interfaz que deben implementar las clases que contiene los parámetros de un driver. 
 * Cuando se registra un driver se hace por la extesión si es un fichero o por el nombre
 * del formato si es de otro tipo. Este interfaz permite a los objetos que se usan como parámetro
 * para un driver obtener el nombre de formato con el que se ha registrado.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface IRegistrableRasterFormat {
	/**
	 * Obtiene el identificador del formato con el que se ha registrado.
	 * @return ID del formato.
	 */
	public String getFormatID();
}

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
 * Excepción que se lanza cuando se está intentando generar un mosaico con
 * datasets no validos. Esto puede ocurrir por distintos motivos como, que no
 * sean contiguos los extends, que no sea posible formar una matriz de NXM, que no 
 * tengan igual tamaño de pixel, etc...
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class MosaicNotValidException extends Exception{
	private static final long serialVersionUID = 7401956202923548194L;

	public MosaicNotValidException(String msg){
		super(msg);
	}
}
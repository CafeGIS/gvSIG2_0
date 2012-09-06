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
package org.gvsig.raster.buffer;



/**
 * Interfaz que deben implementar las clases que sirven datos y tienen posibilidad
 * de consulta. 
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface IQueryableRaster {	  	
	/**
	 * Consulta si una capa está siendo tileada o no
	 * @return true si está siendo tileada y false si no lo está
	 */
	public boolean isTiled();
	
	/**
	 * Obtiene el tamaño del tile si la capa está siendo tileada. Esta función e fundamentalmente
	 * usada por salvar a raster donde se consulta el tamaño del tile para hacer bandas de ese ancho.
	 * @return array con dos enteros que representan el ancho y el alto del tile
	 */
	public int[] getTileSize();
	
}

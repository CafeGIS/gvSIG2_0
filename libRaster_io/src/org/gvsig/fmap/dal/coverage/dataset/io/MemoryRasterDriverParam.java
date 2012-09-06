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
package org.gvsig.fmap.dal.coverage.dataset.io;

import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRegistrableRasterFormat;
import org.gvsig.raster.datastruct.Extent;

/**
 * Parámetro de inicialización del driver MemoryRasterDriver.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class MemoryRasterDriverParam implements IRegistrableRasterFormat{
	private final String id = "memory";
	private IBuffer buffer = null;
	private Extent extent = null;
	
	/**
	 * Constructor vacio
	 */
	public MemoryRasterDriverParam() {
	}
	
	/**
	 * Contructor
	 * @param buf buffer del driver
	 * @param ext extensión del buffer
	 */
	public MemoryRasterDriverParam(IBuffer buf, Extent ext) {
		this.buffer = buf;
		this.extent = ext;
	}
	
	/**
	 * Obtiene el buffer de datos
	 * @return IBuffer
	 */
	public IBuffer getBuffer() {
		return buffer;
	}
	
	/**
	 * Asigna el buffer de datos
	 * @param buffer IBuffer
	 */
	public void setBuffer(IBuffer buffer) {
		this.buffer = buffer;
	}
	
	/**
	 * Obtiene la extensión del buffer de datos
	 * @return Extent
	 */
	public Extent getExtent() {
		return extent;
	}
	
	/**
	 * Asigna la extensión del buffer de datos
	 * @param extent
	 */
	public void setExtent(Extent extent) {
		this.extent = extent;
	}

	/**
	 * Obtiene el identificador del driver
	 * @return String 
	 */
	public String getFormatID() {
		return id;
	}
	
}

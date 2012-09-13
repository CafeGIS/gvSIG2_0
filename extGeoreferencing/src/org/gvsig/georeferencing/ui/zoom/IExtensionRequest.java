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

import java.awt.geom.Rectangle2D;

/**
 * Interfaz necesario en un ZoomControl para la petición de extensióna un objeto
 * externo cuando se pulsan los botones zoom más y zoom menos.
 *
 * 30/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public interface IExtensionRequest {

	/**
	 * Petición de coordenadas al pulsar zoom más o zoom menos en el control.
	 * Estas coordenadas son calculadas a partir del extent del control actual aplicando
	 * un escalado sobre este. La coordenada central del control se mantiene invariante, es
	 * decir, el zoom se calcula sobre el centro actual. El escalado del siguiente buffer
	 * pasado en setParams será de 1.0.
	 * @param Rectangle2D extent
	 * @return Extent aplicado
	 */
	public Rectangle2D request(Rectangle2D extent)throws InvalidRequestException;

	/**
	 * Extensión completa de la capa.
	 */
	public void fullExtent() throws InvalidRequestException ;
}

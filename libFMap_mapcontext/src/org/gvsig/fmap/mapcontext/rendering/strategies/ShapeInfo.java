/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.fmap.mapcontext.rendering.strategies;

import java.awt.geom.Rectangle2D;


/**
 * Estructura de datos con la información relativa a las geometrías de una
 * fuente de datos necesaria para acelerar el procesado de la capa
 */
public interface ShapeInfo {
	/**
	 * Añade al final de la estructura de datos la información de una geometría
	 *
	 * @param boundingBox Extent del shape.
	 * @param type Tipo de shape.
	 */
	void addShapeInfo(Rectangle2D boundingBox, int type);

	/**
	 * Establece en la posición index-ésima de la estructura de datos la
	 * información de una geometría
	 *
	 * @param index índice.
	 * @param boundingBox Extent del shape.
	 * @param type Tipo de shape.
	 *
	 * @throws ArrayIndexOutOfBoundsException Si se intenta establecer la
	 * 		   información para una geometría que no existe
	 */
	void setShapeInfo(int index, Rectangle2D boundingBox, int type)
		throws ArrayIndexOutOfBoundsException;

	/**
	 * Obtiene el bounding box de la geometría index-ésima
	 *
	 * @param index índice de la geometría.
	 *
	 * @return Extent de la geometría.
	 */
	Rectangle2D getBoundingBox(int index);

	/**
	 * Obtiene el tipo de la geometría index-ésima
	 *
	 * @param index índice.
	 *
	 * @return Tipo de geometría.
	 */
	int getType(int index);
}

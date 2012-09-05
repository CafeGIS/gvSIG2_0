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
package org.gvsig.fmap.mapcontext.layers.operations;

import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.layers.FBitSet;

/**
 * Interfaz que implementan las capas vectoriales de acceso aleatorio.
 *
 */
public interface RandomVectorialData {
	/**
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.Strategy#createIndex()
	 */
	void createSpatialIndex();

    void deleteSpatialIndex();

	/**
	 * Devuelve un BitSet con los índices de los shapes que estan dentro del
	 * rectángulo que se pasa como parámetro.
	 *
	 * @param rect Rectángulo.
	 *
	 * @return BitSet con los índices.
	 * @throws ReadDriverException TODO
	 * @throws VisitorException TODO
	 * @see org.gvsig.fmap.mapcontext.rendering.strategies.Strategy#queryByRect(java.awt.geom.Rectangle2D)
	 */
//	FBitSet queryByRect(Rectangle2D rect) throws ReadException;
}

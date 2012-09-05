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

import java.awt.Point;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.tools.task.Cancellable;


/**
 * <p>Interface that must be implemented by layers which support the operation "<i>information by point</i>".</p>
 *
 * <p>That operation allows get meta-information from a point and an area around, of a layer.</p>
 */
public interface InfoByPoint {
	/**
	 * <p>Executes a consultation about information of a point on the layer.</p>
	 *
	 * <p>There is an area around the point where will got the information.</p>
	 *
	 * @param p point where is the consultation
	 * @param tolerance permissible margin around the coordinates of the point where the method will got the information. Each
	 *  singular implementation of this method would use it in a different way. The coordinates also depend on the implementation.
	 * @param cancel shared object that determines if this layer can continue being drawn
	 *
	 * @return an array with XML items that have the information of that point
	 * @throws DataException TODO
	 * @throws LoadLayerException any exception produced using the driver.
	 */
	public XMLItem[] getInfo(Point p, double tolerance, Cancellable cancel) throws LoadLayerException, DataException;
}

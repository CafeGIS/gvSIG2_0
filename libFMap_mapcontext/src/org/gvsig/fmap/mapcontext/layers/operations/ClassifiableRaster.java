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

import org.gvsig.fmap.mapcontext.rendering.legend.IRasterLegend;


/**
 * <p>Interface that all raster layers that can be classifiable and can have associated a legend,
 *  must implement.</p>
 *
 * @see Classifiable
 */
public interface ClassifiableRaster extends Classifiable {
	/**
	 * <p>Sets the layer's legend as a raster legend.</p>
	 *
	 * @param r the legend with raster data
	 *
	 * @throws DriverException if fails the driver used in this method.
	 */
	void setLegend(IRasterLegend r);

	/**
	 * <p>Returns the type of this shape.</p>
	 * <p>All geometry types are defined in <code>Geometry.TYPES</code>, and their shape equivalent in <code>FConstant</code>.</p>
	 *
	 * @return the type of this shape
	 *
	 * @see Geometry
	 * @see Constant
	 */
	int getShapeType();
}

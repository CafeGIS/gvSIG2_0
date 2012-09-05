/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.fmap.mapcontext.rendering.symbols;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;



/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class CartographicSupportToolkit {

	/**
	 * Calculates the distance in pixels corresponding to the provided length,
	 * according to the current rendering context (output dpi, map scale, map units) and the
	 * symbol cartographic settings (unit, size, and unit reference system).
	 *
	 * @param cartographicElement CartographicSupport object which contains the reference system
	 * and the measuring units for the provided length
	 * @param length The length to be computed in pixels. The length is supposed to be
	 * provided in the units defined in the cartographicElement
	 * @param viewPort The viewport, which defines the relationship between pixels and maps units
	 * @param dpi The resolution (dots per inch) of the target display device
	 * (printer, screen, etc)
	 * @return The distance in pixels corresponding to the provided length
	 */
	public static double getCartographicLength(CartographicSupport cartographicElement, double length, ViewPort viewPort, double dpi) {
		int unit = cartographicElement.getUnit();
		double lengthInPixel = length;

		if (unit != -1) {
			double[] trans2Meter=MapContext.getDistanceTrans2Meter();
			if (cartographicElement.getReferenceSystem() == CartographicSupport.WORLD) {
				double dist1PixelInMeters = viewPort.getDist1pixel()*trans2Meter[viewPort.getMapUnits()];
				double realWidthMeter = length*trans2Meter[unit];
				lengthInPixel = realWidthMeter/dist1PixelInMeters;
			} else if (cartographicElement.getReferenceSystem() == CartographicSupport.PAPER) {
				double lengthInInches = 1/trans2Meter[7]*trans2Meter[unit]*length;
				lengthInPixel = lengthInInches*dpi;
			}
		} else{
			double scale=dpi/72;
			lengthInPixel=lengthInPixel*scale;
		}
		return  (lengthInPixel);

	}

	/**
	 * The unit that will be used when creating new symbols.<br>
	 * <b>Factory defaults to pixel.</b>
	 */
	public static int DefaultMeasureUnit = -1;
	/**
	 * The reference system that will be used when creating new symbols.<br>
	 * <b>Factory defaults to pixel.</b>
	 */
	public static int DefaultReferenceSystem = CartographicSupport.WORLD;

}



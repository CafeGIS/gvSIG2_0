/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.ViewPort;



/**
 * <p>This interface enables Cartographic support for those graphical elements that require
 * additional runtime information rather than the feature itself geometric definitions.<br></p>
 *
 * <p>It allows to realworld's measure units dimensioning.<br></p>
 *
 * <p>It also supplies a toolkit to perform operations with centralized static methods.
 * @see CartographicSupportToolkit inner class' methods<br></p>
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface CartographicSupport {
	public static final int WORLD = 0;
	public static final int PAPER = 1;

	/**
	 * Defines the unit used to express measures. It is the position of the unit in the <b>MapContext.NAMES</b> array.
	 * @param unitIndex, the index of the unit in the MapContext.NAMES array
	 */
	public abstract void setUnit(int unitIndex);
	/**
	 * Returns the unit used to express measures. It is the position of the unit in the <b>MapContext.NAMES</b> array.
	 * @returns an <b>int</b> with the index of the unit in the MapContext.NAMES array, or -1 if the size is specified in pixel
	 */
	public abstract int getUnit();

	/**
	 * Returns the <b>Reference System</b> used to draw the elements of the image.<br>
	 * <p>
	 * The elements have to be scaled to <b>pixel</b> when the image is bein drawn in
	 * order to compose the map. The elements of the map may define its size in
	 * other units than pixel.<br>
	 * </p>
	 * <p><b>CartographicSupport</b> enables the elements to define the size in
	 * measure units but these units may refer to different reference system. Two
	 * kinds of Reference Systems are allowed in this context: <b>CartographicSupport.WORLD</b>,
	 * and <b>CartographicSupport.PAPER</b>.
	 * <br></p>
	 * <p>
	 * Depending on which <b>Reference System</b> is used the unit used by this element
	 * refers to distances in the real world (then they are map's CRS-dependant)
	 * or screen or printer output (then they are output DPI-dependant)<br>
	 * </p>
	 * <p>
	 * In case the unit used is <b>pixel</b> then the reference system does not
	 * have any effect since the source unit is the same than the target unit.
	 * <br>
	 * </p>
	 * @return
	 */
	public abstract int getReferenceSystem();

	/**
	 * Sets the <b>Reference System</b> that defines how this units have to be
	 * handled. Possible values are:
	 * <ol>
	 *   <li><b>CartographySupport.WORLD</b>: Defines that the unit values refer
	 *   to distances in the world. So, the size of the element displayed <b>depends
	 *   directly on the scale and CRS</b> used by the map. The size of the elements
	 *   defined to use CartographicSupport.WORLD will match exactly to their
	 *   actual size in the real world.</li>
	 *   <li><b>CartographySupport.PAPER</b>: Defines that the unit values refer
	 *   to the length that the element will have regardless where it appears. If
	 *   ReferenceSystem is <b>CartographySupport.PAPER</b>, and the length is (e.g.)
	 *   millimeters the element will be displayed with the <b>same</b> amount of
	 *   millimeters of length whether in the <b>screen</b> or the <b>printer</b>
	 *   (screen DPI and printer DPI must be correctly configured).</li>
	 * </ol>
	 */
	public abstract void setReferenceSystem(int referenceSystem);

	/**
	 * Computes and sets the size (in pixels) of the cartographic element according
	 * to the current rendering context (output dpi, map scale, map units) and the
	 * symbol cartgraphic settings (unit, size, and unit reference system).
	 *
	 * @param viewPort, the ViewPort containing the symbol.
	 * @param dpi, current output dpi (screen or printer)
	 * @param shp, used only for MultiShapeSymbols in order to discriminate the internal symbol to be applied
	 * @return a double containing the previous defined size
	 */
	public abstract double toCartographicSize(ViewPort viewPort, double dpi, Geometry geom);

	/**
	 * Sets the size of the cartographic element in pixels.
	 *
	 * @param cartographicSize, the size in pixels of the element
	 * @param shp, used only for MultiShapeSymbols in order to discriminate the internal symbol to be applied
	 */
	public abstract void setCartographicSize(double cartographicSize, Geometry geom);

	/**
	 * Gets the size (in pixels) of the cartographic element according
	 * to the current rendering context (output dpi, map scale, map units) and the
	 * symbol cartgraphic settings (unit, size, and unit reference system).
	 * @param viewPort
	 * @param dpi
	 * @param shp
	 * @return double containing the size in [screen/printer] pixels for the current symbol
	 */
	public abstract double getCartographicSize(ViewPort viewPort, double dpi, Geometry geom);


}

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
package org.gvsig.fmap.mapcontrol.tools.Events;

import java.awt.event.MouseEvent;

import org.gvsig.fmap.geom.primitive.GeneralPathX;

/**
 * <p><code>MeasureEvent</code> is used to notify the selection of a polyline.</p>
 *
 * <p>Stores the information about the 2D vertexes and the {@link GeneralPathX GeneralPathX}
 *  between them.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class MeasureEvent {
	/**
	 * The polyline, broken down in straight lines, and quadratic and cubic (Bézier) curves.
	 */
	private GeneralPathX gp;

	/**
	 * Vector with the X coordinates.
	 */
	private Double[] x;

	/**
	 * Vector with the Y coordinates.
	 */
	private Double[] y;

	/**
	 * Mouse event that has been the cause of creating this event.
	 */
	private MouseEvent event;

	/**
	 * <p>Creates a new <code>MeasureEvent</code> with all necessary data.</p>
	 * <p>The general path is calculated according the enclosed regions of the path alternate between interior
	 *  and exterior areas are traversed from the outside of the path towards a point inside the region.</p>
	 *
	 * @param x vector with the X coordinates
	 * @param y vector with the Y coordinates
	 * @param e event that has been the cause of creating this one
	 */
	public MeasureEvent(Double[] x, Double[] y, MouseEvent e) {
		this.x = x;
		this.y = y;
		this.event = e;
		gp = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD, x.length);
		gp.moveTo(x[0].doubleValue(), y[0].doubleValue());

		for (int index = 1; index < x.length; index++) {
			gp.lineTo(x[index].doubleValue(), y[index].doubleValue());
		}
	}

	/**
	 * <p>Gets the {@link GeneralPathX GeneralPathX} of the measurement.</p>
	 *
	 * @see GeneralPathX
	 *
	 * @return geometric path constructed from straight lines, and quadratic and cubic (Bézier) curves
	 */
	public GeneralPathX getGP() {
		return gp;
	}

	/**
	 * <p>Sets the {@link GeneralPathX GeneralPathX} of the measurement.</p>
	 *
	 * @see GeneralPathX
	 *
	 * @param gP geometric path constructed from straight lines, and quadratic and cubic (Bézier) curves
	 */
	public void setGP(GeneralPathX gp) {
		this.gp = gp;
	}

	/**
	 * <p>Gets a vector with the X coordinates.</p>
	 *
	 * @return vector with the X coordinates
	 */
	public Double[] getXs() {
		return x;
	}

	/**
	 * <p>Gets a vector with the Y coordinates.</p>
	 *
	 * @return vector with the Y coordinates
	 */
	public Double[] getYs() {
		return y;
	}

	/**
	 * <p>Gets the event that has been the cause of creating this one.</p>
	 *
	 * @return mouse event that has been the cause of creating this one
	 */
	public MouseEvent getEvent() {
		return event;
	}
}

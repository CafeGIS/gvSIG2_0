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
package org.gvsig.fmap.mapcontext.events;


import org.gvsig.fmap.geom.primitive.Envelope;



/**
 * <p>Event produced when the adjusted extent of the view port has changed.</p>
 *
 * @author Vicente Caballero Navarro
 */
public class ExtentEvent extends FMapEvent {
	/**
	 * <p>Reference to the new adjusted extent.</p>
	 */
	private Envelope newExtent;

	/**
	 * <p>Identifier of this kind of event.</p>
	 */
	private static final int EXTENT_EVENT = 0;

	/**
	 * <p>Returns a new extent event.</p>
	 *
	 * @param c the new adjusted extent
	 *
	 * @return a new extent event
	 */
	public static ExtentEvent createExtentEvent(Envelope r){
		return new ExtentEvent(r, EXTENT_EVENT);
	}

	/**
	 * <p>Creates a new extent event.</p>
	 *
	 * @param c the new adjusted extent
	 * @param eventType identifier of this kind of event
	 */
	private ExtentEvent(Envelope r, int eventType) {
		setEventType(eventType);
		newExtent = r;
	}

	/**
	 * <p>Gets the new adjusted event.</p>
	 *
	 * @return the new adjusted extent
	 */
	public Envelope getNewExtent() {
		return newExtent;
	}
}



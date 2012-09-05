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
package org.gvsig.fmap.mapcontext.rendering.legend;

import org.gvsig.fmap.mapcontext.events.FMapEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendEvent;


/**
 * <p>Event produced when changes any value of a legend.</p>
 *
 * @see FMapEvent
 * @author Vicente Caballero Navarro
 */
public class ValueLegendEvent extends ClassificationLegendEvent {
	/**
	 * <p>Previous legend value.</p>
	 */
	private Object oldValue;

	/**
	 * <p>New legend value.</p>
	 */
	private Object newValue;

	/**
	 * <p>Creates a new value legend event.</p>
	 *
	 * @param oldValue previous legend value
	 * @param newValue new legend value
	 */
	public ValueLegendEvent(Object oldValue, Object newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * <p>Gets the previous value.</p>
	 *
	 * @return the previous value
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * <p>Gets the new value.</p>
	 *
	 * @return the new value
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * <p>Returns the type of this legend event.</p>
	 * 
	 * @return the type of this legend event
	 * 
	 * @see LegendEvent#LEGEND_VALUE_CHANGED
	 */
	public int getEventType() {
		return LegendEvent.LEGEND_VALUE_CHANGED;
	}
}
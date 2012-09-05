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
 * <p>Event produced when changes the interval value of a classification legend.</p>
 * 
 * @see FMapEvent
 * 
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class IntervalLegendEvent extends ClassificationLegendEvent {
	/**
	 * <p>Previous legend interval.</p> 
	 */
	private IInterval oldInterval;
	/**
	 * <p>Previous legend interval.</p> 
	 */
	private IInterval newInterval;

	/**
	 * <p>Creates a new legend interval event.</p>
	 *
	 * @param oldInterval previous legend interval
	 * @param newInterval new legend interval
	 */
	public IntervalLegendEvent(IInterval oldInterval, IInterval newInterval) {
		this.oldInterval = oldInterval;
		this.newInterval = newInterval;
	}

	/**
	 * <p>Gets the previous legend interval.</p>
	 *
	 * @return the previous legend interval
	 */
	public IInterval getOldInterval() {
		return oldInterval;
	}

	/**
	 * <p>Gets the new legend interval.</p>
	 *
	 * @return the new legend interval
	 */
	public IInterval getNewInterval() {
		return newInterval;
	}

	/**
	 * <p>Returns the type of this legend event.</p>
	 * 
	 * @return the type of this legend event
	 * 
	 * @see LegendEvent#LEGEND_INTERVAL_CHANGED
	 */
	public int getEventType() {
		return LegendEvent.LEGEND_INTERVAL_CHANGED;
	}
}
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
package org.gvsig.fmap.mapcontext.rendering.legend.events;

import org.gvsig.fmap.mapcontext.events.FMapEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;



/**
 * <p>Event or set of events produced when changes a legend.</p>
 * 
 * @see FMapEvent
 * @author Vicente Caballero Navarro
 */
public class LegendChangedEvent extends LegendEvent {
	/**
	 * <p>Identifies this event as a changed of a legend.</p>
 	 */
	private static final int LEGEND_CHANGED = 0;

	/**
	 * <p>Previous vector legend.</p>
	 */
	private ILegend oldLegend;

	/**
	 * <p>New vector legend.</p>
	 */
	private ILegend newLegend;

	/**
	 * <p>Events that constitute this one.</p>
	 */
	private LegendChangedEvent[] events;

	/**
	 * <p>Creates a new legend change event.</p>
	 * 
	 * @param oldLegend previous vector legend
	 * @param newLegend new vector legend
	 * 
	 * @return a new legend change event
	 */
	public static LegendChangedEvent createLegendChangedEvent(ILegend oldLegend,
			ILegend newLegend){
		return new LegendChangedEvent(oldLegend, newLegend, LEGEND_CHANGED);
	}
	
	/**
	 * <p>Creates a new legend change event.</p>
	 *
	 * @param oldLegend previous vector legend
	 * @param newLegend new vector legend
	 */
	private LegendChangedEvent(ILegend oldLegend,
			ILegend newLegend, int eventType) {
		this.oldLegend = oldLegend;
		this.newLegend = newLegend;
		setEventType(eventType);
	}

	/**
	 * <p>Gets the previous vector legend.</p>
	 *
	 * @return the previous vector legend
	 */
	public ILegend getOldLegend() {
		return oldLegend;
	}

	/**
	 * <p>Gets the new vector legend.</p>
	 *
	 * @return the new vector legend
	 */
	public ILegend getNewLegend() {
		return newLegend;
	}

	/**
	 * <p>Gets the events that constitute this one.</p>
	 *
	 * @return an array with the events that constitute this one
	 */
	public LegendChangedEvent[] getEvents() {
		return events;
	}

	/**
	 * <p>Sets the events that constitute this one.</p>
	 *
	 * @param events an array with the events that constitute this one
	 */
	public void setEvents(LegendChangedEvent[] events) {
		this.events = events;
	}
}
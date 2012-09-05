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



/**
 * <p>Event produced when changes the value in the field of labeling of a legend.</p>
 *
 * @see FMapEvent
 * @author Vicente Caballero Navarro
 */
public class LabelLegendEvent extends LegendEvent {
	/**
	 * <p>Previous field value.</p>
	 */
	private int oldFieldId;
	
	/**
	 * <p>New field value.</p>
	 */
	private int newFieldId;

	/**
	 * <p>Creates a new legend label event.</p>
	 *
	 * @param oldFieldId previous field value
	 * @param newFieldId new field value
	 */
	public LabelLegendEvent(int oldFieldId, int newFieldId) {
		this.oldFieldId = oldFieldId;
		this.newFieldId = newFieldId;
	}

	/**
	 * <p>Gets the new field value.</p>
	 *
	 * @return the new field value
	 */
	public int getNewFieldId() {
		return newFieldId;
	}

	/**
	 * <p>Gets the previous field value.</p>
	 *
	 * @return the previous field value
	 */
	public int getOldFieldId() {
		return oldFieldId;
	}
}
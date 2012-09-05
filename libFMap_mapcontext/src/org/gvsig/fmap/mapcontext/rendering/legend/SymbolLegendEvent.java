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
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;


/**
 * <p>Event produced when changes one of the symbols of a classified legend.</p>
 *
 * @see FMapEvent
 * @author Vicente Caballero Navarro
 */
public class SymbolLegendEvent extends ClassificationLegendEvent {
/**
	 * <p>Previous symbol style.</p>
	 */
	private ISymbol oldSymbol;
	
	/**
	 * <p>New symbol style.</p>
	 */
	private ISymbol newSymbol;

	/**
	 * <p>Creates a new symbol legend event.</p>
	 *
	 * @param oldValue previous symbol style
	 * @param newValue new symbol style
	 */
	public SymbolLegendEvent(ISymbol oldSymbol, ISymbol newSymbol) {
		this.oldSymbol = oldSymbol;
		this.newSymbol = newSymbol;
	}

	/**
	 * <p>Gets the previous symbol style.</p>
	 *
	 * @return the previous symbol style
	 */
	public ISymbol getOldSymbol() {
		return oldSymbol;
	}

	/**
	 * <p>Gets the new symbol style.</p>
	 *
	 * @return the new symbol style
	 */
	public ISymbol getNewSymbol() {
		return newSymbol;
	}

	/**
	 * <p>Returns the type of this legend event.</p>
	 * 
	 * @return the type of this legend event
	 * 
	 * @see LegendEvent#LEGEND_SYMBOL_CHANGED
	 */
	public int getEventType() {
		return LegendEvent.LEGEND_SYMBOL_CHANGED;
	}
}
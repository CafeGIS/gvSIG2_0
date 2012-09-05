package org.gvsig.fmap.mapcontext.rendering.legend.events;

import org.gvsig.fmap.mapcontext.events.FMapEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.ClassificationLegendEvent;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;

/**
 * <p>The class <code>LegendClearEvent</code> stores all necessary information of an event 
 * produced when a legend is cleared.</p>
 * 
 * @see FMapEvent
 * @author Vicente Caballero Navarro
 */
public class LegendClearEvent extends ClassificationLegendEvent {
	private ISymbol[] oldSymbols;
	/**
	 * Constructor method
	 * @param oldSymbols
	 */
	public LegendClearEvent(ISymbol[] oldSymbols) {
		this.oldSymbols = oldSymbols;
	}
	/**
	 * Obtains the old symbols of the legend
	 * @return
	 */
	public ISymbol[] getOldSymbols() {
		return oldSymbols;
	}
	/**
	 * Returns the type of the event
	 */
	public int getEventType() {
		return LegendEvent.LEGEND_CLEARED;
	}
}
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

import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendClearEvent;

/**
 * Abstract class that implements the interface for legends composed by
 * classified symbols.It will have two methods that will be executed depending
 * on the action that had been done with the legend (addition of a new symbol or
 * clear the legend).
 *
 * @author pepe vidal salvador - jose.vidal.salvador@iver.es
 */
public abstract class AbstractClassifiedVectorLegend extends AbstractLegend implements IClassifiedVectorLegend {
	private String[] fieldNames;
	private int[] fieldTypes;
	private ZSort zSort;

	/**
	 * Looks for a change in a classified symbol of a legend. To perform
	 * it, the Array of LegendListeners is scaned and when the corresponding
	 * listener is true, the method is invoked and the change will be done.
	 * @param event
	 */
	public void fireClassifiedSymbolChangeEvent(SymbolLegendEvent event) {
		for (int i = 0; i < getListeners().length; i++) {
			getListeners()[i].symbolChanged(event);
		}
	}
	/**
	 * Looks for a change in a legend of classified symbols. In this case
	 * if the specific legend is cleared.
	 * @param event
	 */
	public void fireLegendClearEvent(LegendClearEvent event) {
		for (int i = 0; i < getListeners().length; i++) {
			getListeners()[i].legendCleared(event);
		}
	}

	public String[] getClassifyingFieldNames() {
		return fieldNames;
	}


	public void setClassifyingFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public int[] getClassifyingFieldTypes() {
		return fieldTypes;
	}

	public void setClassifyingFieldTypes(int[] fieldTypes) {
		this.fieldTypes =  fieldTypes;
	}

	public ZSort getZSort() {
		return zSort;
	}

	public void setZSort(ZSort zSort) {
		if (zSort == null) {
			removeLegendListener(this.zSort);
		}
		this.zSort = zSort;
		addLegendListener(zSort);
	}


	public boolean isSuitableForShapeType(int shapeType) {
		return getShapeType() == shapeType;
	}
}

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.gui.beans.treelist.event;


/**
 * Event para el TreeListComponentListener
 * Nacho Brodin (brodin_ign@gva.es)
 */

public class TreeListEvent {
	
	private String elementAdded = null;
	private String elementRemoved = null;
	private String sourceElement = null;
	private String DestElement = null;
	private int positionSourceElement = -1;
	private int positionDestElement = -1;
	
	/**
	 * Obtiene el elemento añadido
	 * @return Elemento añadido
	 */
	public String getElementAdded() {
		return elementAdded;
	}
	
	/**
	 * Asigna el elemento añadido
	 * @param elementAdded
	 */
	public void setElementAdded(String elementAdded) {
		this.elementAdded = elementAdded;
	}
	
	/**
	 * Obtiene el elemento eliminado
	 * @return Elemento eliminado
	 */
	public String getElementRemoved() {
		return elementRemoved;
	}
	
	/**
	 * Asigna el elemento eliminado
	 * @param elementRemoved
	 */
	public void setElementRemoved(String elementRemoved) {
		this.elementRemoved = elementRemoved;
	}

	/**
	 * Obtiene el elemento de destino
	 * @return Elemento de destino
	 */
	public String getDestElement() {
		return DestElement;
	}

	/**
	 * Asigna el elemento de destino
	 * @param destElement
	 */
	public void setDestElement(String destElement) {
		DestElement = destElement;
	}

	/**
	 * Obtiene la posición del elemento de destino
	 * @return Elemento de destino
	 */
	public int getPositionDestElement() {
		return positionDestElement;
	}

	/**
	 * Asigna la posición del elemento de destino
	 * @param positionDestElement
	 */
	public void setPositionDestElement(int positionDestElement) {
		this.positionDestElement = positionDestElement;
	}

	/**
	 * Obtiene la posición del elemento de origen
	 * @return Elemento de origen
	 */
	public int getPositionSourceElement() {
		return positionSourceElement;
	}

	/**
	 * Asigna la posición del elemento de origen
	 * @param positionSourceElement
	 */
	public void setPositionSourceElement(int positionSourceElement) {
		this.positionSourceElement = positionSourceElement;
	}

	/**
	 * Obtiene el elemento de origen
	 * @return Elemento de origen
	 */
	public String getSourceElement() {
		return sourceElement;
	}

	/**
	 * Asigna el elemento de origen
	 * @param sourceElement
	 */
	public void setSourceElement(String sourceElement) {
		this.sourceElement = sourceElement;
	}
	
	/**
	 * Resetea los valores
	 */
	public void resetValues(){
		elementAdded = null;
		elementRemoved = null;
		sourceElement = null;
		DestElement = null;
		positionSourceElement = -1;
		positionDestElement = -1;
	}
}

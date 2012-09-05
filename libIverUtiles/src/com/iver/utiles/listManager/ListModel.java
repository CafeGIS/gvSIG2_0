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
package com.iver.utiles.listManager;

import java.util.Vector;

/**
 * Modelo del control ListManager
 *
 * @author Fernando González Cortés
 */
public interface ListModel extends javax.swing.ListModel {
	/**
	 * Removes the element at the specified position in this Vector. shifts any
	 * subsequent elements to the left (subtracts one from their indices).
	 * Returns the element that was removed from the Vector.
	 *
	 * @param i Índice del elemento que se quiere eliminar
	 *
	 * @return Objeto eliminado
	 *
	 * @throws ArrayIndexOutOfBoundsException Si el índice está fuera del array
	 */
	public Object remove(int i) throws ArrayIndexOutOfBoundsException;

	/**
	 * Inserts the specified element at the specified position in this Vector.
	 * Shifts the element currently at that position (if any) and any
	 * subsequent elements to the right (adds one to their indices).
	 *
	 * @param i index at which the specified element is to be inserted.
	 * @param o element to be inserted.
	 */
	public void insertAt(int i, Object o);

	/**
	 * Appends the specified element to the end of this Vector
	 *
	 * @param o element to be appended to this Vector
	 */
	public void add(Object o);

	/**
	 * Obtains the objects that are in the model
	 * @return Vector with the objetos
	 */
	public Vector getObjects();
}

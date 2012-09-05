package com.iver.utiles.vectorUtilities;

import java.util.Comparator;
import java.util.Vector;

import com.iver.utiles.MathExtension;

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

/**
 * New functionality to work with vectors (of elements).
 *
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class VectorUtilities {
	/**
	 * Adds an item in alphabetical order.
	 *
	 * @param v java.util.Vector in alphabetical order.
	 * @param obj java.lang.Object
	 */
	public static synchronized void addAlphabeticallyOrdered(Vector v, Object obj) {
		int size = v.size();
		int currentIteration = 0;
		int index, aux_index;
		int lowIndex = 0;
		int highIndex = size -1;
		int maxNumberOfIterations = (int) MathExtension.log2(size);

		// If there are no items
		if (size == 0) {
			v.add(obj);
			return;
		}

		while ((lowIndex <= highIndex) && (currentIteration <= maxNumberOfIterations)) {
			index = ( lowIndex + highIndex ) / 2;

			// If the item in the index position has the same value as obj
			if (v.get(index).toString().compareTo(obj.toString()) == 0) {
				v.add(index, obj);
				return;
			}

			// If the item in the index position has a lower value than the obj
			if (v.get(index).toString().compareTo(obj.toString()) < 0) {
				aux_index = index + 1;

				if ((aux_index) >= size) {
					v.add(v.size() , obj);
					return;
				}

				if (v.get(aux_index).toString().compareTo(obj.toString()) > 0) {
					v.add(aux_index, obj);
					return;
				}

				lowIndex = aux_index;
			}
			else {
				// If the item in the index position has a higher value than the obj
				if (v.get(index).toString().compareTo(obj.toString()) > 0) {
					aux_index = index - 1;

					if ((aux_index) < 0) {
						v.add(0 , obj);
						return;
					}

					if (v.get(aux_index).toString().compareTo(obj.toString()) < 0) {
						v.add(index, obj);
						return;
					}

					highIndex = aux_index;
				}
			}

			currentIteration ++;
		}
	}

	/**
	 * Adds an item in alphabetical order using a comparator for compare the objects. The vector must be alhabetically ordered.
	 *
	 * @param v java.util.Vector in alphabetical order.
	 * @param obj java.lang.Object
	 * @param comp java.util.Comparator
	 */
	public static synchronized void addAlphabeticallyOrdered(Vector v, Object obj, Comparator comp) {
		int size = v.size();
		int currentIteration = 0;
		int index, aux_index;
		int lowIndex = 0;
		int highIndex = size -1;
		int maxNumberOfIterations = (int) MathExtension.log2(size);

		// If there are no items
		if (size == 0) {
			v.add(obj);
			return;
		}

		while ((lowIndex <= highIndex) && (currentIteration <= maxNumberOfIterations)) {
			index = ( lowIndex + highIndex ) / 2;

			// If the item in the index position has the same value as obj
			if (comp.compare(v.get(index), obj) == 0) {
				v.add(index, obj);
				return;
			}

			// If the item in the index position has a lower value than the obj
			if (comp.compare(v.get(index), obj) < 0) {
				aux_index = index + 1;

				if ((aux_index) >= size) {
					v.add(v.size() , obj);
					return;
				}

				if (comp.compare(v.get(aux_index), obj) > 0) {
					v.add(aux_index, obj);
					return;
				}

				lowIndex = aux_index;
			}
			else {
				// If the item in the index position has a higher value than the obj
				if (comp.compare(v.get(index), obj) > 0) {
					aux_index = index - 1;

					if ((aux_index) < 0) {
						v.add(0 , obj);
						return;
					}

					if (comp.compare(v.get(aux_index), obj) < 0) {
						v.add(index, obj);
						return;
					}

					highIndex = aux_index;
				}
			}

			currentIteration ++;
		}
	}
}

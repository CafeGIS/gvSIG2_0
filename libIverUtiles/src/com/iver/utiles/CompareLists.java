package com.iver.utiles;

import java.util.Comparator;
import java.util.List;

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
 * This class has methods to compare lists of objects
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class CompareLists {
	/**
	 * Compares two lists of elements, using the value returned of the <i><b>toString()</b></i> method
	 *   of each element. <br>
	 * Each element must also implement the {@link Comparable} interface. <br>
	 * This method considers case sensitive .
	 * 
	 * @param l1 First list of items. @see java.util.List
	 * @param l2 Second list of items. @see java.util.List
	 * @return True if the two lists have the same number of elements, and elements are in the same
	 *   position of the two lists and have the same <i>String</i> value .
	 */
	public synchronized static boolean compare(List l1, List l2) {
		// If both are null -> true; if one yes but the other no -> return false
		if ((l1 == null) || (l2 == null)) {
			if (l1 == l2)
				return true;
			else
				return false;
		}
		
		// If the length isn't equal
		if (l1.size() != l2.size())
			return false;

		// Compares each item: must have the same value in the same position in the list
		for (int i = 0; i < l1.size(); i++) {
			if ( l1.get(i).toString().compareTo(l2.get(i).toString()) != 0 )
				return false;
		}
		
		return true;
	}
	
	/**
	 * Compares two lists of elements, using the value returned of the <i><b>toString()</b></i> method
	 *   of each element. <br>
	 * Each element must also implement the {@link Comparable} interface. <br>
	 * This method ignores case sensitive during the comparation.
	 * 
	 * @param l1 First list of items. @see java.util.List
	 * @param l2 Second list of items. @see java.util.List
	 * @return True if the two lists have the same number of elements, and elements are in the same
	 *   position of the two lists and have the same <i>String</i> value .
	 */
	public synchronized static boolean compareIgnoringCaseSensitive(List l1, List l2) {
		// If both are null -> true; if one yes but the other no -> return false
		if ((l1 == null) || (l2 == null)) {
			if (l1 == l2)
				return true;
			else
				return false;
		}

		// If the length isn't equal
		if (l1.size() != l2.size())
			return false;

		// Compares each item: must have the same value in the same position in the list
		for (int i = 0; i < l1.size(); i++) {
			if ( l1.get(i).toString().compareToIgnoreCase(l2.get(i).toString()) != 0 )
				return false;
		}
		
		return true;
	}
	
	/**
	 * Compares two lists of elements, using the value returned of the <i><b>toString()</b></i> method
	 *   of each element. <br>
	 * Each element must also implement the {@link Comparable} interface. <br>
	 * 
	 * @param l1 First list of items. @see java.util.List
	 * @param l2 Second list of items. @see java.util.List
	 * @param comp A class which implements the <i><b>compare</b></i> method of the <i>Comparator</i> interface . 
	 * @return True if the two lists have the same number of elements, and elements are in the same
	 *   position of the two lists and have the same <i>String</i> value .
	 */
	public synchronized static boolean compare(List l1, List l2, Comparator comp) {
		// If both are null -> true; if one yes but the other no -> return false
		if ((l1 == null) || (l2 == null)) {
			if (l1 == l2)
				return true;
			else
				return false;
		}

		// If the length isn't equal
		if (l1.size() != l2.size())
			return false;

		// Compares each item: must have the same value in the same position in the list
		for (int i = 0; i < l1.size(); i++) {
			if ( comp.compare(l1.get(i).toString(), (l2.get(i).toString())) != 0 )
				return false;
		}

		return true;
	}
	
	/**
	 * Compares two lists of elements, using the value returned of the <i><b>toString()</b></i> method
	 *   of each element. <br>
	 * Each element must also implement the {@link Comparable} interface. <br>
	 * This method ignores case sensitive during the comparation.
	 * 
	 * @param l1 First list of items. @see java.util.List
	 * @param l2 Second list of items. @see java.util.List
	 * @param comp A class which implements the <i><b>compare</b></i> method of the <i>Comparator</i> interface . 
	 * @return True if the two lists have the same number of elements, and elements are in the same
	 *   position of the two lists and have the same <i>String</i> value .
	 */
	public synchronized static boolean compareIgnoringCaseSensitive(List l1, List l2, Comparator comp) {
		// If both are null -> true; if one yes but the other no -> return false
		if ((l1 == null) || (l2 == null)) {
			if (l1 == l2)
				return true;
			else
				return false;
		}

		// If the length isn't equal
		if (l1.size() != l2.size())
			return false;

		// Compares each item: must have the same value in the same position in the list
		for (int i = 0; i < l1.size(); i++) {
			if ( comp.compare(l1.get(i).toString().toLowerCase(), (l2.get(i).toString().toLowerCase())) != 0 )
				return false;
		}

		return true;
	}
}

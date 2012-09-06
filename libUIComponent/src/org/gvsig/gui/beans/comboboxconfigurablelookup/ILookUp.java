/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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

package org.gvsig.gui.beans.comboboxconfigurablelookup;

import java.util.List;
import java.util.Vector;

/**
 * <p>Interface for implementing algorithms that get a sublist of items that match with a text written according
 *  an orthographical rules and particular search requirements.</p>
 * 
 * <p>There are two possibilities, considering or ignoring case sensitive.</p>
 * 
 * <p>The particular implementation will be useful to integrate customized algorithms as the <i>look up</i>
 *  logic in the {@link JComboBoxConfigurableLookUp JComboBoxConfigurableLookUp} model.</p> 
 * 
 * @version 07/02/2008
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public interface ILookUp {
	/**
	 * <p>This method should be used when is wanted to distinguish small letters from capital letters during the search.</p>
	 *   
	 * <p>It's necessary that all items of the array implement the {@link Comparable} interface.</p>
	 * 
	 * <p>It's also necessary that the value returned by the <i>toString()</i> method of each item (supposing 
	 *   they inherit from Object) would be the expected value user saw (that would be used to compare the items).</p>
	 * 
	 * <p>And elements of the <code>Vector</code> should be sort ordered by a <code>StringComparator</code> with the same configuration as <code>comp</code>.</p>
	 * 
	 * @param text java.lang.String
	 * @param sortOrderedItems java.util.Vector
	 * @param comp An <code>StringComparator</code> object which implements the <i><b>compareTo()</b><i> method. Must have the same configuration that was
	 *  used to sort order the items of <code>sortOrderedItems</code>.
	 *  
	 * @return A sublist with all items that carry out with the particular search algorithm requirements
	 */
	public List<Object> doLookUpConsideringCaseSensitive(String text, Vector<Object> sortOrderedItems, StringComparator comp);

	/**
	 * <p>This method should be used when is wanted not to distinguish small letters from capital letters during the search, and the comparison of items
	 *   done according an algorithm we define.</p>
	 *   
	 * <p>It's necessary that all items of the array implement the {@link Comparable} interface.</p>
	 * 
	 * <p>It's also necessary that the value returned by the <i>toString()</i> method of each item (supposing 
	 *   they inherit from Object) would be the expected value user saw (that would be used to compare the items).</p>
	 * 
	 * <p>And elements of the <code>Vector</code> should be sort ordered by a <code>StringComparator</code> with the same configuration as <code>comp</code>.</p>
	 * 
	 * @param text java.lang.String
	 * @param sortOrderedItems java.util.Vector
	 * @param comp An <code>StringComparator</code> object which implements the <i><b>compareTo()</b><i> method. Must have the same configuration that was
	 *  used to sort order the items of <code>sortOrderedItems</code>.
	 *  
	 * @return A sublist with all items that carry out with the particular search algorithm requirements
	 */
	public List<Object> doLookUpIgnoringCaseSensitive(String text, Vector<Object> sortOrderedItems, StringComparator comp);
}

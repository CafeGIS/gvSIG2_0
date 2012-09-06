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

package org.gvsig.gui.beans.comboboxconfigurablelookup.agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.gvsig.gui.beans.comboboxconfigurablelookup.ILookUp;
import org.gvsig.gui.beans.comboboxconfigurablelookup.StringComparator;

/**
 * <p>Agent that looks up items of an locale-rules alphabetically sort ordered <code>Vector</code> that
 *  start with a text. Those items will be returned as a {@link List List}.</p>
 * 
 * <p>Supports two versions: with or without considering case sensitive.</p>
 * 
 * @version 07/02/2008
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class StartsWithLookUpAgent implements ILookUp {
	/**
	 * <p>Creates a new instance of the class <code>StartsWithLookUpAgent</code>.</p>
	 */
	public StartsWithLookUpAgent() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.comboboxconfigurablelookup.ILookUp#doLookUpConsideringCaseSensitive(java.lang.String, java.util.Vector, org.gvsig.gui.beans.comboboxconfigurablelookup.StringComparator)
	 */
	public synchronized List<Object> doLookUpConsideringCaseSensitive(String text, Vector<Object> sortOrderedItems, StringComparator comp) {
		if (text == null)
			return null;
		
		List<Object> results_list = doLookUpIgnoringCaseSensitive(text, sortOrderedItems, comp);

		if (results_list == null)
			return null;

		List<Object> results = new ArrayList<Object>();

		for (int i = 0; i < (results_list.size()); i++) {
			if (results_list.get(i).toString().startsWith(text)) {
				results.add(results_list.get(i));
			}
		}

		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.comboboxconfigurablelookup.ILookUp#doLookUpIgnoringCaseSensitive(java.lang.String, java.util.Vector, org.gvsig.gui.beans.comboboxconfigurablelookup.StringComparator)
	 */
	public synchronized List<Object> doLookUpIgnoringCaseSensitive(String text, Vector<Object> sortOrderedItems, StringComparator comp) {
		if (text == null)
			return null;
		
		int currentIteration = 0;
		int size = sortOrderedItems.size();
		int maxNumberOfIterations = (int) (Math.log(size) / Math.log(2));
		int lowIndex = 0;
		int highIndex = sortOrderedItems.size() - 1;
		int mIndx;
		
		while ((lowIndex <= highIndex) && (currentIteration <= maxNumberOfIterations)) {
			mIndx = ( lowIndex + highIndex ) / 2;

			if ( sortOrderedItems.get( mIndx ).toString().toLowerCase().startsWith( text.toLowerCase() ) ) {
				lowIndex = highIndex = mIndx;
				highIndex ++;
				
				// Expand the rank to up
				while ( ( highIndex < size ) && ( sortOrderedItems.get( highIndex ).toString().toLowerCase().startsWith( text.toLowerCase() ) ) ) {
					highIndex ++;
				}

				// Expand the rank to down
				while ( ( (lowIndex - 1) > -1 ) && ( sortOrderedItems.get( (lowIndex - 1) ).toString().toLowerCase().startsWith( text.toLowerCase() ) ) ) {
					lowIndex --;
				}
				
				// Returns all items in the rank			
				return Arrays.asList((sortOrderedItems.subList(lowIndex, highIndex)).toArray()); // Breaks the loop
			}
			else {
				if ( comp.compare(sortOrderedItems.get( mIndx ).toString().toLowerCase(), text.toLowerCase() ) > 0 ) {
					highIndex = mIndx - 1;
				}
				else {
					lowIndex = mIndx + 1;
				}
			}
			
			currentIteration ++;
		}
		
		return null;
	}
}

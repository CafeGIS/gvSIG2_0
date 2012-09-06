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

package org.gvsig.gui.beans.comboboxconfigurablelookup.programmertests;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.gvsig.gui.beans.comboboxconfigurablelookup.ILookUp;
import org.gvsig.gui.beans.comboboxconfigurablelookup.StringComparator;

/**
 * <p>Sample of personalized look up algorithm for the model of a <code>JComboBoxConfigurableLookUp</code> object.</p>
 * 
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 * @version 08/02/2008
 */
public class SampleAgent implements ILookUp {
	/**
	 * <p>Creates a new instance of the class <code>StartsWithLookUpAgent</code>.</p>
	 */
	public SampleAgent() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.comboboxconfigurablelookup.ILookUp#doLookUpConsideringCaseSensitive(java.lang.String, java.util.Vector, org.gvsig.gui.beans.comboboxconfigurablelookup.StringComparator)
	 */
	public List<Object> doLookUpConsideringCaseSensitive(String text, Vector<Object> sortOrderedItems, StringComparator comp) {
		if (sortOrderedItems == null)
			return null;

		List<Object> list = new ArrayList<Object>();
		
		for (int i = 0; i < (sortOrderedItems.size()); i++) {
			if (i % 2 == 0)
				list.add(sortOrderedItems.get(i));
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.comboboxconfigurablelookup.ILookUp#doLookUpIgnoringCaseSensitive(java.lang.String, java.util.Vector, org.gvsig.gui.beans.comboboxconfigurablelookup.StringComparator)
	 */
	public List<Object> doLookUpIgnoringCaseSensitive(String text, Vector<Object> sortOrderedItems, StringComparator comp) {
		if (sortOrderedItems == null)
			return null;

		List<Object> list = new ArrayList<Object>();
		
		for (int i = 0; i < (sortOrderedItems.size()); i++) {
			if (i % 2 == 0)
				list.add(sortOrderedItems.get(i));
		}

		return list;
	}
}

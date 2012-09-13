/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.generictoolbar;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.gvsig.gui.beans.comboboxconfigurablelookup.ILookUp;
import org.gvsig.gui.beans.comboboxconfigurablelookup.JComboBoxConfigurableLookUp;
import org.gvsig.gui.beans.comboboxconfigurablelookup.StringComparator;
/**
 * Clase para reimplementar una nueva busqueda para el componente
 * {@link JComboBoxConfigurableLookUp}
 * 
 * @version 13/02/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class BinarySearch implements ILookUp {
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.comboboxconfigurablelookup.ILookUp#doLookUpConsideringCaseSensitive(java.lang.String, java.util.Vector, org.gvsig.gui.beans.comboboxconfigurablelookup.StringComparator)
	 */
	public List doLookUpConsideringCaseSensitive(String text, Vector sortOrderedItems, StringComparator comp) {
		Vector list = new Vector();
		for (int i = 0; i < sortOrderedItems.size(); i++)
			if (sortOrderedItems.get(i).toString().indexOf(text) != -1)
				list.add(sortOrderedItems.get(i));
		return Arrays.asList(list.toArray());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.comboboxconfigurablelookup.ILookUp#doLookUpIgnoringCaseSensitive(java.lang.String, java.util.Vector, org.gvsig.gui.beans.comboboxconfigurablelookup.StringComparator)
	 */
	public List doLookUpIgnoringCaseSensitive(String text, Vector sortOrderedItems, StringComparator comp) {
		Vector list = new Vector();
		for (int i = 0; i < sortOrderedItems.size(); i++)
			if (sortOrderedItems.get(i).toString().toLowerCase().indexOf(text.toLowerCase()) != -1)
				list.add(sortOrderedItems.get(i));
		return Arrays.asList(list.toArray());
	}
}
/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 {DiSiD Technologies}  {Implement data selection}
 */
package org.gvsig.fmap.dal.feature;

import java.util.Iterator;

import org.gvsig.fmap.dal.DataSelection;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.observer.WeakReferencingObservable;
import org.gvsig.tools.persistence.Persistent;

/**
 * Interface to manage selection of objects from an undetermined set. Allows to
 * add and remove values from the selection, as well as reversing the selection.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public interface FeatureReferenceSelection extends DataSelection, Observer,
        WeakReferencingObservable, Persistent {

    /**
     * Adds a feature to the selection.
     *
     * @param reference
     *            the selected feature reference
     * @return true if the feature was not selected before selecting it
     */
    boolean select(FeatureReference reference);

    /**
     * Removes a feature from the selection.
     *
     * @param reference
     *            the deselected feature reference
     * @return true if the feature was selected before deselecting it
     */
    boolean deselect(FeatureReference reference);

    /**
     * Selects all values.
     * 
     * @throws DataException
     *             if there is an error selecting all values
     */
    void selectAll() throws DataException;

    /**
     * Deselects all values.
     * 
     * @throws DataException
     *             if there is an error deselecting all values
     */
    void deselectAll() throws DataException;

    /**
     * Returns if a feature is selected.
     *
     * @param reference
     *            to check
     * @return if it is selected
     */
    boolean isSelected(FeatureReference reference);

    /**
     * Reverses the selection. Currently selected objects will become the not
     * selected ones, so any other object will be a selected one.
     */
    void reverse();

    /**
     * Returns the number of selected values.
     *
     * @return the number of selected values
     */
    long getSelectedCount();

    /**
     * Returns an unmodifiable Iterator of selected feature references.
     *
     * @return the iterator of selected feature references
     */
    Iterator referenceIterator();
}
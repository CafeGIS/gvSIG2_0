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

import org.gvsig.fmap.dal.exception.DataException;

/**
 * Manages a selection of Features.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public interface FeatureSelection extends FeatureReferenceSelection, FeatureSet {

    /**
     * Adds a feature to the selection.
     * 
     * @param feature
     *            the selected feature
     * @return true if the feature was not selected before selecting it
     */
    boolean select(Feature feature);

    /**
     * Removes a feature from the selection.
     * 
     * @param feature
     *            the deselected feature
     * @return true if the feature was selected before deselecting it
     */
    boolean deselect(Feature feature);

    /**
     * Adds a DataSet of features to the selection.
     * 
     * @param features
     *            the selected features
     * @return true if any of the feature was not selected before selecting it
     * @throws DataException
     *             if there is an error reading the FeatureSet values
     */
    boolean select(FeatureSet features) throws DataException;

    /**
     * Removes a DataSet of features from the selection.
     * 
     * @param features
     *            the deselected features
     * @return true if any of the features was selected before deselecting it
     * @throws DataException
     *             if there is an error reading the FeatureSet values
     */
    boolean deselect(FeatureSet features) throws DataException;

    /**
     * Returns if a feature is selected.
     * 
     * @param feature
     *            to check
     * @return if it is selected
     */
    boolean isSelected(Feature feature);
}
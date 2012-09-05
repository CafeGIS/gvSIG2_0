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
* 2009 {DiSiD Technologies}  {{Task}}
*/
package org.gvsig.fmap.dal.feature.impl;

import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.impl.undo.FeatureCommandsStack;

/**
 * Helper methods for the Default Selection implementation classes.
 * 
 * @see DefaultFeatureReferenceSelection
 * @see DefaultFeatureSelection
 * @author <a href="mailto:cordinyana@gvsig.org">Cèsar Ordiñana</a>
 */
public interface FeatureSelectionHelper {
    
    /**
     * If the store is in editing mode, it will have the count of added or
     * removed features. This is used to correct the total count of Features.
     * 
     * @return the total number of added and deleted features
     */
    long getFeatureStoreDeltaSize();

    /**
     * Returns the {@link FeatureCommandsStack} of the {@link FeatureStore}. If
     * the store is not in editing mode, this method may return null
     * 
     * @return the commands stack of the {@link FeatureStore}
     */
    FeatureCommandsStack getFeatureStoreCommandsStack();

}
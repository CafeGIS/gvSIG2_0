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

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.impl.undo.FeatureCommandsStack;

/**
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class DefaultFeatureSelectionHelper implements FeatureSelectionHelper {

    private final DefaultFeatureStore store;

    public DefaultFeatureSelectionHelper(DefaultFeatureStore store) {
        this.store = store;
    }

    public FeatureCommandsStack getFeatureStoreCommandsStack() {
        try {
            return store.getCommandsStack();
        } catch (DataException e) {
            // Ignore
            return null;
        }
    }

    public long getFeatureStoreDeltaSize() {
        // If the FeatureManager is present, and the store is in editing
        // mode, the number of current Features may have changed.
        if (store.isEditing() && store.getFeatureManager() != null) {
            return store.getFeatureManager().getDeltaSize();
        } else {
            return 0;
        }
    }
}
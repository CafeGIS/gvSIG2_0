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
 * 2008 {DiSiD Technologies}  {{Task}}
 */
package org.gvsig.fmap.data.feature.swing.table.notification;

import org.gvsig.fmap.data.feature.swing.FeatureTable;

/**
 * Notification fired when the column header selection has been changed.
 * 
 * @author <a href="mailto:cordin@disid.com">C�sar Ordi�ana</a>
 */
public class ColumnHeaderSelectionChangeNotification implements
        FeatureTableNotification {
    
    public static final String COLUMNHEADER_SELECTION_CHANGE = "Column_Header_Selection_Change";

    private final FeatureTable featureTable;

    /**
     * Creates a new ColumnHeaderSelectionChangeNotification with the
     * originating {@link FeatureTable}.
     * 
     * @param featureTable
     *            origin of the notification
     */
    public ColumnHeaderSelectionChangeNotification(FeatureTable featureTable) {
        this.featureTable = featureTable;
    }

    public FeatureTable getFeatureTable() {
        return featureTable;
    }
    
    public String getType() {
        return COLUMNHEADER_SELECTION_CHANGE;
    }
}
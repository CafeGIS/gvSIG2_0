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
package org.gvsig.fmap.dal.feature.impl.undo.command;

import org.gvsig.fmap.dal.feature.exception.SelectionStateException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureReferenceSelection;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureReferenceSelection.SelectionData;
import org.gvsig.tools.undo.RedoException;
import org.gvsig.tools.undo.UndoException;
import org.gvsig.tools.undo.command.impl.AbstractCommand;

/**
 * Command to allow undo and redo over a select or deselect ALL operation
 * performed on a selection.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class SelectionCommandSelectAll extends AbstractCommand {

    private final DefaultFeatureReferenceSelection selection;
    private final boolean allSelected;
    private SelectionData selectionData;

    public SelectionCommandSelectAll(
            DefaultFeatureReferenceSelection selection,
            boolean allSelected, String description)
            throws SelectionStateException {
        super(description);
        this.selection = selection;
        this.allSelected = allSelected;
        try {
            this.selectionData = (SelectionData) selection.getData().clone();
        } catch (CloneNotSupportedException ex) {
            throw new SelectionStateException(ex);
        }
    }

    public void execute() {
        // Nothing to do
    }

    public int getType() {
        return allSelected ? INSERT : DELETE;
    }

    public void undo() throws UndoException {
        selection.setData(selectionData);
    }

    public void redo() throws RedoException {
        try {
            // Re-Obtain the state
            selectionData = (SelectionData) selection.getData().clone();
            if (allSelected) {
                selection.selectAll(false);
            } else {
                selection.deselectAll(false);
            }
        } catch (Exception ex) {
            throw new RedoException(this, ex);
        }
    }
}
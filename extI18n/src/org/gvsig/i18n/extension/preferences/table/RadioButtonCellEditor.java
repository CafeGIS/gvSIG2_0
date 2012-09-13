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
 * 2008 {DiSiD Technologies}  {New extension for installation and update of text translations}
 */
package org.gvsig.i18n.extension.preferences.table;

import java.awt.Component;
import java.awt.event.*;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Allows to edit a cell content with a Radio button.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class RadioButtonCellEditor extends AbstractCellEditor implements
	TableCellEditor, ItemListener {

    private static final long serialVersionUID = 1000179477526963659L;

    private RadioButtonCell delegate = new RadioButtonCell();

    /**
     * Constructor.
     */
    public RadioButtonCellEditor() {
	delegate.getRadioButton().addItemListener(this);
    }

    public Object getCellEditorValue() {
	return Boolean.valueOf(delegate.getRadioButton().isSelected());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
	    boolean isSelected, int row, int column) {
	return delegate.getTableCellComponent(table, value, isSelected,
		isSelected, row, column);
    }

    public void actionPerformed(ActionEvent e) {
	// Stop editing when the user clicks into the radio button,
	// so other cells get its rendering updated
	fireEditingStopped();
    }

    public void itemStateChanged(ItemEvent e) {
	fireEditingStopped();
    }

}
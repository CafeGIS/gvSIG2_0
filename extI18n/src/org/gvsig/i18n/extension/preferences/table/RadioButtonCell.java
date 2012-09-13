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

import javax.swing.*;

/**
 * Renders a radio button on a table cell.
 * 
 * @author <a href="mailto:dcervera@disid.com">David Cervera</a>
 */
public class RadioButtonCell {

    private JRadioButton radio = new JRadioButton();

    /**
     * Constructor.
     */
    public RadioButtonCell() {
	radio.setHorizontalAlignment(SwingConstants.CENTER);
    }

    protected Component getTableCellComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {

	if (isSelected) {
	    radio.setForeground(table.getSelectionForeground());
	    radio.setBackground(table.getSelectionBackground());
	}
	else {
	    radio.setForeground(table.getForeground());
	    radio.setBackground(table.getBackground());
	}

	radio.setSelected(Boolean.TRUE.equals(value));

	return radio;
    }

    protected JRadioButton getRadioButton() {
	return radio;
    }
}
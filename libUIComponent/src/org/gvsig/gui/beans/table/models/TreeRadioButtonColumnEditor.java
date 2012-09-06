/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.gui.beans.table.models;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

/**
 * Componente tabla
 * 
 * @author Nacho Brodin (brodin_ign@gva.es)
 *
 */
public class TreeRadioButtonColumnEditor extends AbstractCellEditor
implements TableCellEditor {
    final private static long   serialVersionUID = -3370601314380922368L;
    public JRadioButton         theRadioButton;
	private ActionListener      listener = null;
    
    public TreeRadioButtonColumnEditor() {
        super();
        theRadioButton = new JRadioButton();
        theRadioButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    fireEditingStopped() ;
                    if(listener != null)
                    	listener.actionPerformed(event);
                }
            });
    }
    
    /**
     * Asigna un listener externo
     * @param listener
     */
    public void setActionListener(ActionListener listener) {
    	this.     listener = listener;
    }

    public Component getTableCellEditorComponent(JTable table, Object obj,
                                                 boolean isSelected,
                                                 int row, int col) {
        theRadioButton.setHorizontalAlignment(SwingUtilities.CENTER);

        Boolean lValueAsBoolean = (Boolean) obj;
        theRadioButton.setSelected(lValueAsBoolean.booleanValue());

        return theRadioButton;
    }

    public Object getCellEditorValue() {
        return new Boolean(theRadioButton.isSelected());
    }
}
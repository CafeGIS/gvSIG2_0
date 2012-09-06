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

import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * Componente tabla
 * 
 * @author Nacho Brodin (brodin_ign@gva.es)
 *
 */
public class TreeRadioButtonColumnRenderer extends JRadioButton implements TableCellRenderer {
    final private static long serialVersionUID = -3370601314380922368L;

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column) {
        if (value == null) {
            this.setSelected(false);
        }
        
        if(value instanceof Boolean){
	        Boolean ValueAsBoolean = (Boolean) value;
	        this.setSelected(ValueAsBoolean.booleanValue());
	        this.setHorizontalAlignment(SwingConstants.CENTER);
        }
        	
        return this;
    }
}
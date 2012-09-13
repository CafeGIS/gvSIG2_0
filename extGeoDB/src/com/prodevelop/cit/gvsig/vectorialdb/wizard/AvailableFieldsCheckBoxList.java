/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *   Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integración de Tecnologías SL
 *   Conde Salvatierra de Álava , 34-10
 *   46004 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   gis@prodevelop.es
 *   http://www.prodevelop.es
 */
package com.prodevelop.cit.gvsig.vectorialdb.wizard;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


/**
 * Utility class to keep the list of available fields.
 *
 * @author jldominguez
 *
 */
public class AvailableFieldsCheckBoxList extends JList {
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public AvailableFieldsCheckBoxList() {
        setCellRenderer(new CellRenderer());

        addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int index = locationToIndex(e.getPoint());

                    if (index == -1) {
                        return;
                    }

                    FieldsListItem sel = (FieldsListItem) getModel()
                                                              .getElementAt(index);

                    if ((e.getClickCount() == 2) || (e.getX() < 15)) {
                        sel.setSelected(!sel.isSelected());
                    }
                }
            });

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public Object[] getCheckedItems() {
        int size = getModel().getSize();
        ArrayList resp = new ArrayList();

        for (int i = 0; i < size; i++) {
            FieldsListItem item = (FieldsListItem) getModel().getElementAt(i);

            if (item.isSelected()) {
                resp.add(item);
            }
        }

        return resp.toArray();
    }

    public void checkAll(boolean b) {
        int size = getModel().getSize();

        for (int i = 0; i < size; i++) {
            FieldsListItem item = (FieldsListItem) getModel().getElementAt(i);
            item.setSelected(b);
        }

        updateUI();
    }

    protected class CellRenderer implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
            FieldsListItem checkbox = (FieldsListItem) value;
            checkbox.setBackground(isSelected ? getSelectionBackground()
                                              : getBackground());
            checkbox.setForeground(isSelected ? getSelectionForeground()
                                              : getForeground());
            checkbox.setEnabled(isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);
            checkbox.setBorder(isSelected
                ? UIManager.getBorder("List.focusCellHighlightBorder")
                : noFocusBorder);

            return checkbox;
        }
    }
}

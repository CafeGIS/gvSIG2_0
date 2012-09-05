/*
 * Created on 15-mar-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.gui.utils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;


/**
 * DOCUMENT ME!
 *
 * @author vcn
 */
public class ColorRenderer extends JLabel implements TableCellRenderer {
    Border unselectedBorder = null;
    Border selectedBorder = null;
    boolean isBordered = true;

    /**
     * Creates a new ColorRenderer object.
     *
     * @param isBordered DOCUMENT ME!
     */
    public ColorRenderer(boolean isBordered) {
        this.isBordered = isBordered;
        setOpaque(true); //MUST do this for background to show up.
    }

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param isSelected DOCUMENT ME!
     * @param hasFocus DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTableCellRendererComponent(JTable table, Object color,
        boolean isSelected, boolean hasFocus, int row, int column) {
        Color newColor = (Color) color;
        setBackground(newColor);

        //JLabel boton=new JLabel("si");
        //boton.setBackground(new Color(10,10,100));
        //add(boton);
        //System.out.println("dentro de Color Renderer");
        if (isBordered) {
            if (isSelected) {
                if (selectedBorder == null) {
                    selectedBorder = BorderFactory.createMatteBorder(2, 5, 2,
                            5, table.getSelectionBackground());
                }

                setBorder(selectedBorder);
            } else {
                if (unselectedBorder == null) {
                    unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2,
                            5, table.getBackground());
                }

                setBorder(unselectedBorder);
            }
        }

      /*  setToolTipText("RGB value: " + newColor.getRed() + ", " +
            newColor.getGreen() + ", " + newColor.getBlue());
*/
        return this;
    }
}

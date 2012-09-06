/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ib��ez, 50
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
package org.gvsig.gui.beans.swing.cellrenderers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.jfree.layout.CenterLayout;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class BooleanTableCellRenderer extends DefaultTableCellRenderer {
  private static final long serialVersionUID = 2121615214282741840L;

	private JCheckBox chk;

	private boolean isBordered;
	private MatteBorder selectedBorder;
	private MatteBorder unselectedBorder;


	public BooleanTableCellRenderer(boolean bordered) {
		this.isBordered = bordered;
		setOpaque(true);
	}

	public JCheckBox getCheck() {
		return chk;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value == null)
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

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
		try {
			Boolean v = (Boolean) value;
			JPanel content = new JPanel(new CenterLayout());
			content.setBackground(table.getBackground());
			chk = new JCheckBox("", v.booleanValue());
			chk.setBackground(table.getBackground());
			content.add(chk, BorderLayout.CENTER);
			return content;
		} catch (ClassCastException ccEx) {
			throw new RuntimeException("Trying to use a Boolean cell renderer with a non-Boolean datatype");
		}

	}

}

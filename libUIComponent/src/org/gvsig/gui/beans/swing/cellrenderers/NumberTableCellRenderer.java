/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package org.gvsig.gui.beans.swing.cellrenderers;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.gvsig.gui.beans.swing.JIncrementalNumberField;
import org.gvsig.gui.beans.swing.ValidatingTextField;
import org.gvsig.gui.beans.swing.ValidatingTextField.Validator;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class NumberTableCellRenderer extends DefaultTableCellRenderer {
  private static final long serialVersionUID = -4551232953341602636L;

	private JIncrementalNumberField txt;

	private boolean isBordered;
	private MatteBorder selectedBorder;
	private MatteBorder unselectedBorder;

	private boolean acceptsDoubles;



	public NumberTableCellRenderer(boolean bordered, boolean acceptsDoubles) {
		this.isBordered = bordered;
		this.acceptsDoubles = acceptsDoubles;
		setOpaque(true);
	}

	public JIncrementalNumberField getIncrementalNumberField() {
		return txt;
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

			JPanel content = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			content.setBackground(table.getBackground());
			Validator valid = acceptsDoubles ? ValidatingTextField.DOUBLE_VALIDATOR : ValidatingTextField.INTEGER_VALIDATOR;
			txt = new JIncrementalNumberField("", 6,valid, ValidatingTextField.NUMBER_CLEANER, 0, 80, 1);
			if (acceptsDoubles) {
				txt.setDouble(((Double) value).doubleValue());
			} else {
				txt.setInteger(((Integer) value).intValue());
			}

			txt.setBackground(table.getBackground());
			// TODO figure out a way to know the cell's width to fit in the editor
//			txt.setPreferredSize(new Dimension(
//					table.getColumnModel().getColumn(column).getPreferredWidth(),
//					table.getRowHeight()));
			content.add(txt);
			return content;
		} catch (ClassCastException ccEx) {
			throw new RuntimeException("Trying to use a numeric cell renderer with a non-numeric datatype");
		}

	}
}

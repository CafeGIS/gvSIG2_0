/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * 2008 Prodevelop S.L. main development
 */
package org.gvsig.geocoding.gui.address;

import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.geocoding.address.Address;


/**
 * Abstract panel of geocoding style to insert the address relates with the style
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class AbstractAddressPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * get address from user interface
	 * 
	 * @param model
	 * @return
	 */
	public Address getSimpleAddress() {
		return null;
	};

	/**
	 * Get the addres from table
	 * 
	 * @param model
	 * @param row
	 * @return
	 */
	public Address getTableAddress(int row) {

		return null;
	}
	
	/**
	 * Create fields combo model to number
	 * 
	 * @param descs
	 * @return
	 */
	protected DefaultComboBoxModel createFieldsComboModel(
			List<String> descs) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (String desc : descs) {
			model.addElement(desc);
		}
		return model;
	}
	
	/**
	 * Put combos in cells of the one table
	 * 
	 * @param table
	 * @param descs
	 */
	public void putCombosInTable(JTable table,
			List<String> descs, int col) {
		TableColumn column = table.getColumnModel().getColumn(col);
		JComboBox combo = new JComboBox();
		DefaultComboBoxModel modelcombo = new DefaultComboBoxModel();
		for (String desc : descs) {
			modelcombo.addElement(desc);
		}
		combo.setModel(modelcombo);
		DefaultCellEditor dce = new DefaultCellEditor(combo);
		dce.setClickCountToStart(2);
		column.setCellEditor(dce);
	}
	
	/**
	 * Clear all rows of the TableModel
	 * 
	 * @param model
	 */
	public void clearTableModel(DefaultTableModel model) {
		int n = model.getRowCount();
		for (int i = n - 1; i > -1; i--) {
			model.removeRow(i);
		}
	}

}

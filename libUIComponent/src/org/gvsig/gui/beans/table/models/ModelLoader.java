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

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
/**
 * Cargador de Modelos
 *
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class ModelLoader {
	private static DefaultTableModel tableModel = null;

	public JTable load(String model, String[] columnNames, ArrayList listeners) {
		if (model == null || model.equals(""))
			return null;

		ActionListener actionListener = null;
		if(listeners != null) {
			for (int i = 0; i < listeners.size(); i++) {
				if(listeners.get(i) instanceof ActionListener) 
					actionListener = (ActionListener)listeners.get(i);
			}
		}
		
		JTable table = null;

		if (model.equals("ListModel")) {
			tableModel = new ListModel(columnNames);
			table = new JTable(tableModel);
		}

		if (model.equals("TreeRadioButtonModel")) {
			tableModel = new TreeRadioButtonModel(columnNames);
			table = new JTable(tableModel);
			TableColumn column = null;
			
			for (int i = 0; i < columnNames.length - 1; i++) {
				column = table.getColumnModel().getColumn(i);
				column.setCellRenderer(new TreeRadioButtonColumnRenderer());
				column.setCellEditor(new TreeRadioButtonColumnEditor());
			}
		}
		
		if (model.equals("ARGBBandSelectorModel")) {
			tableModel = new ARGBBandSelectorModel(columnNames);
			table = new JTable(tableModel);
			TableColumn column = null;

			for (int i = 0; i < columnNames.length - 1; i++) {
				column = table.getColumnModel().getColumn(i);
				column.setMaxWidth(52);
				column.setMinWidth(26);
				column.setCellRenderer(new TreeRadioButtonColumnRenderer());
				TreeRadioButtonColumnEditor trbce = new TreeRadioButtonColumnEditor();
				trbce.setActionListener(actionListener);
				column.setCellEditor(trbce);
			}
		}

		if (model.equals("RadioButtonModel")) {
			tableModel = new RadioButtonModel(columnNames);
			table = new JTable(tableModel);
			TableColumn column = null;

			for (int i = 0; i < columnNames.length - 1; i++) {
				column = table.getColumnModel().getColumn(i);
				column.setCellRenderer(new TreeRadioButtonColumnRenderer());
				column.setCellEditor(new TreeRadioButtonColumnEditor());
			}
		}

		if (model.equals("CheckBoxModel")) {
			tableModel = new CheckBoxModel(columnNames);
			table = new JTable(tableModel);
			TableColumn column = null;

			column = table.getColumnModel().getColumn(0);
			column.setCellRenderer(new CheckBoxColumnRenderer());
			column.setCellEditor(new CheckBoxColumnEditor());
		}

		if (model.equals("TableButtonModel")) {
			tableModel = new TableButtonModel(columnNames);
			table = new JTable(tableModel);
			TableColumn column = null;

			column = table.getColumnModel().getColumn(0);
			column.setCellRenderer(new TableColorButtonColumnRenderer(true));
			column.setCellEditor(new TableColorButtonColumnEditor((DefaultTableModel) tableModel, table));

			column = table.getColumnModel().getColumn(4);
			column.setCellRenderer(new TableSelectorButtonColumnRenderer());
			column.setCellEditor(new TableSelectorButtonColumnEditor());
		}

		if (model.equals("TableColorModel")) {
			tableModel = new TableColorModel(columnNames);
			table = new JTable(tableModel);
			TableColumn column = null;

			column = table.getColumnModel().getColumn(0);
			column.setCellRenderer(new TableColorButtonColumnRenderer(true));
			column.setCellEditor(new TableColorButtonColumnEditor((DefaultTableModel) tableModel, table));

			column = table.getColumnModel().getColumn(5);
			column.setCellRenderer(new TableSelectorButtonColumnRenderer());
			column.setCellEditor(new TableSelectorButtonColumnEditor());
		}
		
		if (model.equals("ROIsTableModel")) {
			tableModel = new ROIsTableModel(columnNames);
			table = new JTable(tableModel);
			TableColumn column = null;
	
			column = table.getColumnModel().getColumn(4);
			column.setCellRenderer(new TableColorButtonColumnRenderer(true));
			column.setCellEditor(new TableColorButtonColumnEditor((DefaultTableModel) tableModel, table));	
		}
		if (model.equals("ProfilesTableModel")) {
			tableModel = new ProfilesTableModel(columnNames);
			table = new JTable(tableModel);
			TableColumn column = null;
	
			column = table.getColumnModel().getColumn(1);
			column.setCellRenderer(new TableColorButtonColumnRenderer(true));
			column.setCellEditor(new TableColorButtonColumnEditor((DefaultTableModel) tableModel, table));	
		}
		
		if (model.equals("GCPModel")) {
			tableModel = new GCPModel(columnNames);
			table = new JTable(tableModel);
			TableColumn column = null;

			column = table.getColumnModel().getColumn(0);
			column.setCellRenderer(new CheckBoxColumnRenderer(actionListener));
			column.setCellEditor(new CheckBoxColumnEditor());
		}
		
		return table;
	}

	/**
	 * Obtiene el modelo de la tabla
	 * @return DefaultTableModel
	 */
	public DefaultTableModel getTableModel() {
		return tableModel;
	}
}
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
*/
package org.gvsig.gui.beans.table.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gvsig.gui.beans.table.MoveRowsPanel;
import org.gvsig.gui.beans.table.Table;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.TableControlerPanel;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.gui.beans.table.models.IModel;
/**
 * Listener para la tabla
 * @author Nacho Brodin <brodin_ign@gva.es>
 */
public class TableListener implements ActionListener, ListSelectionListener {
	public static boolean       comboEventEnable      = true;
	private TableContainer      tableContainer        = null;
	private TableControlerPanel controlPanel          = null;
	private MoveRowsPanel       moveRowsPanel         = null;
	private Table               table                 = null;
	public boolean              enableNewLineListener = true;

	/**
	 * Constructor
	 * @param tableContainer
	 */
	public TableListener(TableContainer tableContainer){
		this.tableContainer = tableContainer;
	}

	private void initialize(){
		controlPanel = tableContainer.getPTableControl();
		moveRowsPanel = tableContainer.getMoveRowsPanel();
		table = tableContainer.getTable();
	}

	/**
	 * Captura y gestiona los eventos del control de la tabla para añadir filas,
	 * eliminar filas y moverse por la tabla.
	 */
	public void actionPerformed(ActionEvent e) {
		if (controlPanel == null || table == null)
			initialize();

		try {
			/**
			 * Nueva entrada
			 */
			if (enableNewLineListener && e.getSource() == controlPanel.getBNew()) {
				if (table.getTableModel() instanceof IModel)
					tableContainer.addRow(((IModel) table.getTableModel()).getNewLine());
			}

			/**
			 * Eliminar todas las filas
			 */
			if (enableNewLineListener && e.getSource() == controlPanel.getBClear()) {
				tableContainer.removeAllRows();
			}

			/**
			 * Elimina una entrada concreta de la tabla
			 */
			if (enableNewLineListener && e.getSource() == controlPanel.getBDelPoint()) {
				int[] lista = tableContainer.getSelectedRows();
				for (int i = lista.length - 1; i>=0; i--)
					tableContainer.delRow(lista[i]);
			}

			/**
			 * Subir un elemento
			 */
			if (e.getSource() == moveRowsPanel.getBUp()) {
				int[] lista = tableContainer.getSelectedRows();
				if (lista.length > 0) {
  				for (int i = 0; i < lista.length; i++)
  					tableContainer.swapRow(lista[i] - 1, lista[i]);
  				if (lista[0] > 0)
  					tableContainer.setSelectedIndex(lista[0] - 1);
				}
			}

			/**
			 * Bajar un elemento
			 */
			if (e.getSource() == moveRowsPanel.getBDown()) {
				int[] lista = tableContainer.getSelectedRows();
				if (lista.length > 0) {
  				for (int i = lista.length - 1; i >= 0; i--)
  					tableContainer.swapRow(lista[i], lista[i] + 1);
  				tableContainer.setSelectedIndex(lista[0] + 1);
				}
			}

			if (e.getSource() == controlPanel.getBFirst()) {
				tableContainer.setSelectedIndex(0);
			}

			if (e.getSource() == controlPanel.getBLast()) {
				tableContainer.setSelectedIndex(tableContainer.getRowCount() - 1);
			}

			if (e.getSource() == controlPanel.getBNext()) {
				if (tableContainer.getSelectedRow() < tableContainer.getRowCount())
					tableContainer.setSelectedIndex(tableContainer.getSelectedRow() + 1);
			}

			if (e.getSource() == controlPanel.getBPrev()) {
				if (tableContainer.getSelectedRow() > 0)
					tableContainer.setSelectedIndex(tableContainer.getSelectedRow() - 1);
			}

			if (e.getSource() == controlPanel.getCPoint()) {
				if (comboEventEnable)
					tableContainer.setSelectedIndex(controlPanel.getCPoint().getSelectedIndex());
			}

		} catch (NotInitializeException ex) {

		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		try {
	    tableContainer.setSelectedIndex(tableContainer.getSelectedRow());
    } catch (NotInitializeException e1) {
	    e1.printStackTrace();
    }
  }
}
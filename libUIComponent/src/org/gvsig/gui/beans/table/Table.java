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
package org.gvsig.gui.beans.table;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.gvsig.gui.beans.table.models.ModelLoader;
/**
 * Componente tabla
 *
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class Table extends JPanel {
	private static final long serialVersionUID = -4375500244443538451L;
	private JTable            jTable           = null;
	private JScrollPane       scrollPanel      = null;
	private DefaultTableModel tableModel       = null;
	private String[]          columnNames      = null;
	private int[]             columnWidths     = null;
	private TableContainer    tableContainer   = null;
	public String             tableModelClass  = null;
	private ArrayList         listeners        = null;

	/**
	 *
	 * @param width Ancho de la tabla en pixeles
	 * @param height Alto de la tabla en pixeles
	 * @param columnNames Vector de nombres de columna
	 * @param columnsWidth	Vector de anchos para cada columna. Ha de tener el mismo número de elementos que columnNames.
	 * Si vale null las columnas se pondrán equidistantes.
	 */
	public Table(String[] columnNames, int[] columnWidths, String tableModelClass){
		this.columnNames = columnNames;
		this.columnWidths = columnWidths;
		this.tableModelClass = tableModelClass;
		initialize();
	}
	
	/**
	 *
	 * @param width Ancho de la tabla en pixeles
	 * @param height Alto de la tabla en pixeles
	 * @param columnNames Vector de nombres de columna
	 * @param columnsWidth	Vector de anchos para cada columna. Ha de tener el mismo número de elementos que columnNames.
	 * @para listener Liste de eventos para recoger en las tablas. Cada modelo tiene la posibilida de recoger unos listener
	 * u otros. El usuario le pasará el listener y el modelo se encargará de gestionarlos si puede hacerlo. Si no puede no
	 * hará nada con ellos.
	 * Si vale null las columnas se pondrán equidistantes.
	 */
	public Table(String[] columnNames, int[] columnWidths, String tableModelClass, ArrayList listeners){
		this.columnNames = columnNames;
		this.columnWidths = columnWidths;
		this.tableModelClass = tableModelClass;
		this.listeners = listeners;
		initialize();
	}


		/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout(5, 5));
		this.add(getJScrollPane(), BorderLayout.CENTER);
	}

	/**
	 * Obtiene la Tabla
	 * @return Tabla de bandas de la imagen
	 */
	public JTable getJTable() {
			if (jTable == null) {
			// tableModel = new ListModel(columnNames);
			ModelLoader loader = new ModelLoader();
			jTable = loader.load(tableModelClass, columnNames, listeners);
			tableModel = loader.getTableModel();

			TableColumn column = null;

			int widthPerColumn = (int) (this.getWidth() / columnNames.length);
			for (int i = 0; i < columnNames.length; i++) {
				column = jTable.getColumnModel().getColumn(i);
				column.setResizable(true);
				if (columnWidths == null)
					column.setPreferredWidth(widthPerColumn);
				else
					column.setPreferredWidth(columnWidths[i]);
			}
		}

		return jTable;
	}

	/**
	 * This method initializes jScrollPane
	 * @return javax.swing.JPanel
	 */
	private JScrollPane getJScrollPane() {
		if (scrollPanel == null) {
			scrollPanel = new JScrollPane(getJTable());
		}
		return scrollPanel;
	}

	/**
	 * Obtiene el contenedor padre de la tabla
	 * @return
	 */
	public TableContainer getTableContainer() {
		return tableContainer;
	}

	/**
	 * Asigna el contenedor padre de la tabla
	 * @param tableContainer
	 */
	public void setTableContainer(TableContainer tableContainer) {
		this.tableContainer = tableContainer;
	}

	/**
	 * Obtiene el modelo de la tabla
	 * @return
	 */
	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	//********************Métodos de Tabla*****************************//

	/**
	 * Añade una fila a la tabla.
	 * @param list Lista de cadenas
	 */
	public void addRow(Object[] list){
		((DefaultTableModel)this.getJTable().getModel()).addRow(list);
	}

	/**
	 * Elimina una fila de la tabla.
	 * @param i Fila a eliminar
	 */
	public void delRow(int i){
		((DefaultTableModel)this.getJTable().getModel()).removeRow(i);
	}
	
	/**
	 * Intercambia una fila de la tabla por otra.
	 * @param i
	 * @param j
	 */
	public void swapRow(int i, int j) {
		DefaultTableModel model = (DefaultTableModel) this.getJTable().getModel();
		if ((i < 0) || (j < 0))
			return;
		if ((i >= model.getRowCount()) || (j >= model.getRowCount()))
			return;
		model.moveRow(i, i, j);
	}

	/**
	 * Elimina todas las filas de la tabla.
	 */
	public void removeAllRows(){
		((DefaultTableModel)this.getJTable().getModel()).setNumRows(0);
	}
}
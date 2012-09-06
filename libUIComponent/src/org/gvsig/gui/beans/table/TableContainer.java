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
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.gui.beans.table.listeners.TableListener;

/**
 * Contenedor para los componentes de la tabla. Incluye la tabla y el panel de
 * control.
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class TableContainer extends JPanel {
	private static final long   serialVersionUID = 384372026944926838L;

	private Table               table           = null;
	private TableControlerPanel pTableControl   = null;
	private MoveRowsPanel       moveRowsPanel   = null;
	private String[]            columnNames     = null;
	private int[]               columnWidths    = null;
	private TableListener       tableListener   = null;
	// Variable que indica si la tabla ha sido inicializada
	private boolean             initTable       = false;
	private String              tableModelClass = "ListModel";
	private ArrayList           listeners       = null;

	/**
	 * @param width Ancho de la tabla en pixeles
	 * @param height Alto de la tabla en pixeles
	 * @param columnNames Vector de nombres de columna
	 * @param columnsWidth Vector de anchos para cada columna. Ha de tener el
	 *          mismo número de elementos que columnNames. Si vale null las
	 *          columnas se pondrán equidistantes.
	 */
	public TableContainer(String[] columnNames, int[] columnWidths) {
		this.columnNames = columnNames;
		this.columnWidths = columnWidths;
	}
	
	/**
	 * @param width Ancho de la tabla en pixeles
	 * @param height Alto de la tabla en pixeles
	 * @param columnNames Vector de nombres de columna
	 * @param columnsWidth Vector de anchos para cada columna. Ha de tener el
	 *          mismo número de elementos que columnNames. Si vale null las
	 *          columnas se pondrán equidistantes.
	 * @para listener Liste de eventos para recoger en las tablas. Cada modelo tiene la posibilida de recoger unos listener
	 * u otros. El usuario le pasará el listener y el modelo se encargará de gestionarlos si puede hacerlo. Si no puede no
	 * hará nada con ellos.
	 */
	public TableContainer(String[] columnNames, int[] columnWidths, ArrayList listeners) {
		this.columnNames = columnNames;
		this.columnWidths = columnWidths;
		this.listeners = listeners;
	}

	/**
	 * This method initializes this
	 */
	public void initialize() {
		initTable = true;
		tableListener = new TableListener(this);
		getTable().getJTable().getSelectionModel().addListSelectionListener(tableListener);

		this.setLayout(new BorderLayout(5, 5));
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
		this.add(getTable(), BorderLayout.CENTER);

		JPanel panel = new JPanel();

		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.add(getPTableControl());
		panel.add(getMoveRowsPanel());
		this.add(panel, BorderLayout.SOUTH);
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	public Table getTable() {
		if (table == null) {
			table = new Table(columnNames, columnWidths, tableModelClass, listeners);
			table.setTableContainer(this);
		}
		return table;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	public TableControlerPanel getPTableControl() {
		if (pTableControl == null) {
			pTableControl = new TableControlerPanel(tableListener);
		}
		return pTableControl;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	public MoveRowsPanel getMoveRowsPanel() {
		if (moveRowsPanel == null) {
			moveRowsPanel = new MoveRowsPanel(tableListener);
			moveRowsPanel.setVisible(false);
		}
		return moveRowsPanel;
	}

	// ********************Métodos de Tabla*****************************//

	/**
	 * Añade una fila a la tabla.
	 * @param list Lista de cadenas
	 */
	public void addRow(Object[] list) throws NotInitializeException {
		if (!initTable)
			throw new NotInitializeException();

		TableListener.comboEventEnable = false;
		getTable().addRow(list);
		getPTableControl().addPointToTable((getTable()).getJTable().getRowCount());
		setSelectedIndex(getRowCount() - 1);
		TableListener.comboEventEnable = true;
	}

	/**
	 * Elimina una fila de la tabla.
	 * @param i Fila a eliminar
	 */
	public void delRow(int i) throws NotInitializeException {
		if (!initTable)
			throw new NotInitializeException();

		TableListener.comboEventEnable = false;
		try {
			setSelectedIndex(getSelectedRow() - 1);
		} catch (ArrayIndexOutOfBoundsException ex) {
			try {
				setSelectedIndex(getSelectedRow() + 1);
			} catch (ArrayIndexOutOfBoundsException exc) {
			}
		}
		getTable().delRow(i);
		getPTableControl().setNItems(getRowCount());
		TableListener.comboEventEnable = true;
	}

	/**
	 * Intercambia una fila de la tabla por otra.
	 * @param i Fila a eliminar
	 */
	public void swapRow(int i, int j) throws NotInitializeException {
		if (!initTable)
			throw new NotInitializeException();

		TableListener.comboEventEnable = false;
		getTable().swapRow(i, j);
		TableListener.comboEventEnable = true;
	}

	/**
	 * Elimina todas las filas de la tabla.
	 */
	public void removeAllRows() throws NotInitializeException {
		if (!initTable)
			throw new NotInitializeException();

		TableListener.comboEventEnable = false;
		getTable().removeAllRows();
		getPTableControl().setNItems(0);
		TableListener.comboEventEnable = true;
	}

	/**
	 * Obtiene el número de filas en la tabla
	 * @return Número de filas de la tabla
	 */
	public int getRowCount() throws NotInitializeException {
		if (!initTable)
			throw new NotInitializeException();

		return getTable().getJTable().getRowCount();
	}

	/**
	 * Selecciona un punto de la lista
	 * @param i punto a seleccionar
	 */
	public void setSelectedIndex(int i) throws NotInitializeException {
		if (!initTable)
			throw new NotInitializeException();

		TableListener.comboEventEnable = false;
		try {
			getPTableControl().setSelectedIndex(i);
			getMoveRowsPanel().setSelectedIndex(i, getRowCount());
			getTable().getJTable().setRowSelectionInterval(i, i);
		} catch (IllegalArgumentException ex) {

		}
		TableListener.comboEventEnable = true;
	}

	/**
	 * Obtiene el punto seleccionado de la lista
	 * @return Posición del punto seleccionado de la tabla
	 */
	public int getSelectedRow() throws NotInitializeException {
		if (!initTable)
			throw new NotInitializeException();

		return getTable().getJTable().getSelectedRow();
	}

	/**
	 * Obtiene los puntos seleccionados de la lista
	 * @return Posición del punto seleccionado de la tabla
	 */
	public int[] getSelectedRows() throws NotInitializeException {
		if (!initTable)
			throw new NotInitializeException();

		return getTable().getJTable().getSelectedRows();
	}

	/**
	 * Asigna un valor a una posición de la tabla
	 * @param value Valor
	 * @param row Fila
	 * @param col Columna
	 */
	public void setValueAt(Object value, int row, int col) throws NotInitializeException {
		if (!initTable)
			throw new NotInitializeException();

		getTable().getJTable().setValueAt(value, row, col);
	}

	/**
	 * Dice si una tabla es editable o no. Este flag hay que asignarlo antes de la
	 * inicialización de tabla.
	 * @param editable
	 */
	public void setEditable(boolean editable) throws NotInitializeException {
		if (!initTable)
			throw new NotInitializeException();

		if (!editable)
			getTable().getJTable().setBackground(this.getBackground());
		else
			getTable().getJTable().setBackground(Color.white);

		getTable().getJTable().setEnabled(editable);
	}

	/**
	 * Asigna el modelo de la tabla
	 * @param model cadena con el nombre del modelo
	 */
	public void setModel(String model) {
		tableModelClass = model;
	}

	/**
	 * Obtiene el model de la tabla
	 * @return
	 */
	public DefaultTableModel getModel() {
		return (DefaultTableModel) getTable().getJTable().getModel();
	}

	/**
	 * Asigna al panel de control de tabla la propiedad de visible/invisible a
	 * true o false
	 * @param visible
	 */
	public void setControlVisible(boolean visible) {
		getPTableControl().setVisible(visible);
	}
	
	/**
	 * Asigna al panel de control de tabla la propiedad de visible/invisible a
	 * true o false
	 * @param visible
	 */
	public void setMoveRowsButtonsVisible(boolean visible) {
		getMoveRowsPanel().setVisible(visible);
	}

	/**
	 * Obtiene el control de tabla
	 * @return TableControlerPanel
	 */
	public TableControlerPanel getControl() {
		return getPTableControl();
	}

	/**
	 * Desactiva o activa el evento de nueva linea. Si se desactiva tendrá que ser
	 * gestionado por el cliente
	 * @param enabled true para activar y false para desactivar
	 */
	public void setEnableControlsListener(boolean enabled) {
		tableListener.enableNewLineListener = enabled;
	}

	/**
	 * Activar o desactivar los componentes del panel
	 */
	public void setEnabled(boolean enabled) {
		getTable().getJTable().setEnabled(enabled);

		if (!enabled)
			getPTableControl().disableAllControls();
		else
			getPTableControl().restoreControlsValue();
	}
}

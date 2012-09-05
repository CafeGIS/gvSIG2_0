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
package com.iver.ai2.gvsig3d.legend.extrusion;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.gvsig.fmap.mapcontext.rendering.legend.NullIntervalValue;
import org.gvsig.fmap.mapcontext.rendering.legend.NullUniqueValue;
import org.gvsig.fmap.mapcontext.rendering.legend.NullValue;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.JMenuItem;
import com.iver.andami.ui.mdiFrame.JPopUpMenu;
import com.iver.cit.gvsig.project.documents.gui.SymbolCellEditor;
import com.iver.cit.gvsig.project.documents.gui.TableSymbolCellRenderer;
import com.iver.cit.gvsig.project.documents.view.legend.edition.gui.IntervalCellEditor;
import com.iver.cit.gvsig.project.documents.view.legend.edition.gui.ValueCellEditor;
import com.iver.utiles.swing.jtable.JTable;
import com.iver.utiles.swing.jtable.TextFieldCellEditor;

public class SymbolTableExtrusion  extends JPanel {
	private static final long serialVersionUID = -8694846716328735113L;
	private static Hashtable<String,TableCellEditor> cellEditors = new Hashtable<String,TableCellEditor>();

	public static final String VALUES_TYPE = "values";
	public static final String INTERVALS_TYPE = "intervals";
	private JTable table;
	private String type;
	private int shapeType;
	private OnTableMouseAdapter rightClickActions;

	/**
	 * Crea un nuevo FSymbolTable.
	 *
	 * @param type
	 *            tipo de valor si es intervalo: "intervals" y si es por
	 *            valores: "values".
	 */
	public SymbolTableExtrusion(Component ownerComponent, String type, int shapeType) {
		super(new GridLayout(1, 0));
		this.type = type;
		this.shapeType = shapeType;

		rightClickActions = new OnTableMouseAdapter();
		table = new JTable();
		table.setModel(new MyTableModel());
		table.setPreferredScrollableViewportSize(new Dimension(480, 110));

		initializeCellEditors();

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Set up column sizes.
		// initColumnSizes(table);
		setUpSymbolColumn(table, table.getColumnModel().getColumn(0));

		if(cellEditors.get(type) == null)
			throw new Error("Symbol table type not set!");

		setUpValueColumn(table, table.getColumnModel().getColumn(0),cellEditors.get(this.type));
		setUpLabelColumn(table, table.getColumnModel().getColumn(1));

		// Add the scroll pane to this panel.
		add(scrollPane);
		table.setRowSelectionAllowed(true);
		table.addMouseListener(rightClickActions);
	}
	/**
	 * Inicializa los valores de los CellEditors que la SymbolTable poseerá por defecto
	 */
	private void initializeCellEditors() {
		this.cellEditors.put(this.INTERVALS_TYPE,new IntervalCellEditor());
		this.cellEditors.put(this.VALUES_TYPE, new ValueCellEditor());
	}
	/**
	 * Añade un nuevo CellEditor a la lista de disponibles
	 *
	 * @param key String con el nombre identificativo del CellEditor
	 * @param cellEditor CellEditor que va a ser añadido
	 */
	public static void addCellEditor(String key,TableCellEditor cellEditor ) {
		cellEditors.put(key, cellEditor);
	}
	/**
	 * Obtiene el valor de los elementos de una fila seleccionada
	 *
	 * @return Object[] Array con los objetos de cada una de las columnas de la fila seleccionada
	 */
	public Object[] getSelectedRowElements() {
		Object[] values = new Object[2];

		MyTableModel m = (MyTableModel) table.getModel();
		int[] selectedRows = table.getSelectedRows();

		if(selectedRows.length != 1)
			return null;

		for (int i = 0; i < 2; i++) {
			values[i] = m.getValueAt(selectedRows[0], i);
		}

		return values;
	}
	/**
	 * Añade una fila al modelo.
	 *
	 * @param vector
	 *            Fila en forma de vector de Object para añadir al modelo.
	 */
	public void addRow(Object[] vector) {
		MyTableModel m = (MyTableModel) table.getModel();
		m.addRow(vector);

	}

	/**
	 * Elimina la fila que tiene como clave el objeto que se pasa como
	 * parámetro.
	 *
	 * @param obj
	 *            clave del objeto a eliminar.
	 */
	public void removeRow(Object obj) {
		MyTableModel m = (MyTableModel) table.getModel();

		for (int i = 0; i < m.getRowCount(); i++) {
			if (m.getValueAt(i, 0) instanceof NullUniqueValue
					|| m.getValueAt(i, 0) instanceof NullIntervalValue) {
				m.removeRow(i);
			}
		}
	}

	/**
	 * Elimina las filas que están seleccionadas.
	 */
	public void removeSelectedRows() {
		if (table.getCellEditor() != null) {
			table.getCellEditor().cancelCellEditing();
		}

		MyTableModel m = (MyTableModel) table.getModel();
		int[] selectedRows = table.getSelectedRows();

		for (int i = selectedRows.length - 1; i >= 0; i--) {
			m.removeRow(selectedRows[i]);
		}
	}

	/**
	 * Rellena la tabla con los símbolos valores y descripciones que se pasan
	 * como parámetro.
	 *
	 * @param symbols
	 *            Array de símbolos
	 * @param values
	 *            Array de valores.
	 * @param descriptions
	 *            Array de descripciones.
	 */
	public void fillTableFromSymbolList(ISymbol[] symbols, Object[] values,
			Object[] descriptions) {
		ISymbol theSymbol;

		for (int i = 0; i < symbols.length; i++) {
			theSymbol = symbols[i];
			if(!(values[i] instanceof NullIntervalValue) && !(values[i] instanceof NullUniqueValue))
				addTableRecord(theSymbol, values[i], descriptions[i]);
		}
	}

	/**
	 * Añade una fila con los objetos que se pasan como parámetros.
	 *
	 * @param symbol
	 *            símbolo de la fila.
	 * @param value
	 *            Valor de la fila.
	 * @param description
	 *            Descripción.
	 */
	public void addTableRecord(ISymbol symbol, Object value, Object description) {
		Object[] theRow = new Object[2];
		theRow[0] = value;
		theRow[1] = description;
		addRow(theRow);
	}

	/**
	 * Devuelve el valor a partie del número de fila y columna.
	 *
	 * @param row
	 *            número de fila.
	 * @param col
	 *            número de columna.
	 *
	 * @return Objeto.
	 */
	public Object getFieldValue(int row, int col) {
		MyTableModel m = (MyTableModel) table.getModel();

		return m.getValueAt(row, col);
	}

	/**
	 * Devuelve el número total de filas que contiene el modelo.
	 *
	 * @return Número de filas.
	 */
	public int getRowCount() {
		MyTableModel m = (MyTableModel) table.getModel();

		return m.getRowCount();
	}

	/**
	 * Elimina todas las filas del modelo.
	 */
	public void removeAllItems() {
		table.setModel(new MyTableModel());
		setUpValueColumn(table, table.getColumnModel().getColumn(0),cellEditors.get(this.type));
		setUpLabelColumn(table, table.getColumnModel().getColumn(1));
	}

	/**
	 * Inicializa el cell editor de tipo descripción de la columna que se pasa
	 * como parámetro.
	 *
	 * @param table2
	 *            Tabla.
	 * @param column
	 *            Columna.
	 */
	public void setUpLabelColumn(JTable table2, TableColumn column) {
		TextFieldCellEditor labeleditor = new TextFieldCellEditor();
		column.setCellEditor(labeleditor);
	}

	/**
	 * Inicializa el cell editor de tipo valor de la columna que se pasa como
	 * parámetro.
	 *
	 * @param table2
	 *            Tabla.
	 * @param column
	 *            Columna.
	 * @param tableCellEditor
	 */
	public void setUpValueColumn(JTable table2,TableColumn column, TableCellEditor tableCellEditor) {
		column.setCellEditor(tableCellEditor);
	}
	/**
	 * Inicializa el cell editor de tipo símbolo de la columna que se pasa como
	 * parámetro.
	 *
	 * @param table2
	 *            Tabla.
	 * @param column
	 *            Columna.
	 */
	public void setUpSymbolColumn(JTable table2, TableColumn column) {
		// Set up the editor
		column.setMaxWidth(100);
		column.setWidth(60);
		column.setPreferredWidth(60);
		column.setMinWidth(50);

		// FSymbolCellEditor symboleditor = new FSymbolCellEditor();
//		SymbolCellEditor symboleditor = new SymbolCellEditor(shapeType);
	
		//HARDCODED EL CONSTRUCTOR DE AQUI ABAJO SOLO PA QUE COMPILE
		SymbolCellEditor symboleditor = new SymbolCellEditor(0);
		column.setCellEditor(symboleditor);

		TableSymbolCellRenderer renderer = new TableSymbolCellRenderer(true);
		column.setCellRenderer(renderer);
	}

	/**
	 * Modelo que propio que se aplica a la tabla.
	 *
	 * @author Vicente Caballero Navarro
	 */
	class MyTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		// AbstractTableModel {
		private String[] columnNames = {
				PluginServices.getText(this, "Valor_inicial"),
				PluginServices.getText(this, "Valor_extrusionado") };

		/**
		 * Devuelve el número de columnas.
		 *
		 * @return Número de columnas.
		 */
		public int getColumnCount() {
			return columnNames.length;
		}

		/**
		 * Devuelve el String del valor de la columna.
		 *
		 * @param col
		 *            Número de columna.
		 *
		 * @return Nombre de la columna.
		 */
		public String getColumnName(int col) {
			return columnNames[col];
		}

		/**
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. If we didn't implement this method, then the last column
		 * would contain text ("true"/"false"), rather than a check box.
		 */
		public Class getColumnClass(int c) {
			if (getValueAt(0, c) == null) {
				return NullValue.class;
			}
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
			// Note that the data/cell address is constant,
			// no matter where the cell appears onscreen.
			// if (col > 0) {
			return true;
		}

		@Override
		public Object getValueAt(int row, int column) {
//			if(column == 1)
//				return ((ISymbol)getValueAt(row,0)).getDescription();

			return super.getValueAt(row, column);
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {

//			if(column == 1){
//				ISymbol symbol = (ISymbol) getValueAt(row,0);
//				symbol.setDescription((String) aValue);
//				setValueAt(symbol,row,0);
//			}

			super.setValueAt(aValue, row, column);
		}

	}

	private class OnTableMouseAdapter extends MouseAdapter {

		private JPopUpMenu menu = new JPopUpMenu();

		// group option
		private JMenuItem groupItem = new JMenuItem(
				PluginServices.getText(this, "group"));
		private ActionListener groupAction = new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				hidePopUp();
				int[] selectedRows = table.getSelectedRows();
				if (selectedRows.length > 1) {
					DefaultTableModel model = (DefaultTableModel) table
							.getModel();
					int theRow = selectedRows[0];
//					ISymbol symboToBeApplied = (ISymbol) model.getValueAt(
//							theRow, 0);
					String labelToBeApplied = (String) model.getValueAt(
							theRow, 1);
					ArrayList<Object> valuesToBeApplied = new ArrayList<Object>(
							selectedRows.length);
					for (int i = 0; i < selectedRows.length; i++) {
						valuesToBeApplied.add(model.getValueAt(selectedRows[i],
								0));
					}

					for (int i = selectedRows.length - 1; i > 0; i--) {
						model.removeRow(selectedRows[i]);
					}

//					model.setValueAt(symboToBeApplied, theRow, 0);
					model.setValueAt(labelToBeApplied, theRow, 1);
					table.clearSelection();
					table.doLayout();
				}
			}
		};

		// combine option
		private JMenuItem combineItem = new JMenuItem(
				PluginServices.getText(this, "combine"));
		private ActionListener combineAction = new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				hidePopUp();
				int[] selectedRows = table.getSelectedRows();
				if (selectedRows.length > 1) {
					DefaultTableModel model = (DefaultTableModel) table
							.getModel();
					int theRow = selectedRows[0];
//					ISymbol symboToBeApplied = (ISymbol) model.getValueAt(
//							theRow, 0);
					String labelToBeApplied = (String) model.getValueAt(
							theRow, 1);
					ArrayList<Object> valuesToBeApplied = new ArrayList<Object>(
							selectedRows.length);
					for (int i = 0; i < selectedRows.length; i++) {
						valuesToBeApplied.add(model.getValueAt(selectedRows[i],
								0));
					}

					for (int i = selectedRows.length - 1; i > 0; i--) {
						model.removeRow(selectedRows[i]);
					}

//					model.setValueAt(symboToBeApplied, theRow, 0);
					model.setValueAt(labelToBeApplied, theRow, 1);
					table.clearSelection();
					table.doLayout();
				}
			}
		};

		private boolean menuEmpty = false;


		{
			groupItem.addActionListener(groupAction);
			if (VALUES_TYPE.equals(type)) {
				menu.add(groupItem);
			} else if (INTERVALS_TYPE.equals(type)) {
				menu.add(combineItem);
			} else {
				menuEmpty = true;
			}

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// if we click outside the popup menu
			if (menu.isVisible()) {
				Rectangle tableBounds = table.getBounds();
				tableBounds.setLocation(table.getLocationOnScreen());
				if (!tableBounds.contains(getClickLocation(e))) {
					hidePopUp();
				}
			}

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);

			// if we click outside the popup menu
			if (menu.isVisible()
					&& !menu.getBounds().contains(getClickLocation(e))) {
				hidePopUp();
			}

			if (e.getButton() == MouseEvent.BUTTON3) {
				e.consume();
				int[] selectedRows = table.getSelectedRows();
				if (selectedRows.length > 0) {
					Point realClickLocation = getClickLocation(e);
					menu.setLocation(realClickLocation);
					showPopUp();
				}
			}

		}

		private void showPopUp() {
			if (!menuEmpty) {
				table.setEnabled(false);
				table.editingCanceled(new ChangeEvent(table));
				menu.setVisible(true);
			}
		}

		private void hidePopUp() {
			if (!menuEmpty ) {
				menu.setVisible(false);
				table.setEnabled(true);
			}
		}

		private Point getClickLocation(MouseEvent e) {
			Point tableLocation = table.getLocationOnScreen();
			Point relativeClickPoint = e.getPoint();
			Point realClickLocation = new Point(tableLocation.x
					+ relativeClickPoint.x, tableLocation.y
					+ relativeClickPoint.y);
			return realClickLocation;
		}

	}

}

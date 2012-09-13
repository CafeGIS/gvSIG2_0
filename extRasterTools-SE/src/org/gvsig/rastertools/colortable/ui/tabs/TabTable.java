/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.colortable.ui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.gui.beans.table.models.TableColorModel;
import org.gvsig.raster.beans.previewbase.IUserPanelInterface;
import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;
/**
 * Pestaña para definir una tabla de color en formato de tabla
 *
 * @version 27/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TabTable extends BasePanel implements IColorTableUI, TableModelListener, IUserPanelInterface {
	private static final long  serialVersionUID       = -6971866166164473789L;
	private ArrayList          actionCommandListeners = new ArrayList();
	private boolean            listenerEnabled        = false;

	/**
	 * Tabla de color interna que se esta utilizando actualmente
	 */
	private ColorTable colorTable = new ColorTable();
	private TableContainer  tableContainer  = null;

	/**
	 * Construye un TabTable
	 * @param colorTablePanel
	 */
	public TabTable() {
		init();
		translate();
		getTableContainer().getTable().getJTable().getModel().addTableModelListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		setLayout(new BorderLayout());
		add(getTableContainer(), BorderLayout.CENTER);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
	}

	private TableContainer getTableContainer() {
		if (tableContainer == null) {
			String[] columnNames = {getText(this, "color"), getText(this, "clase"), "RGB", getText(this, "valor"), getText(this, "hasta"), getText(this, "alpha")};
			int[] columnWidths = {55, 68, 110, 64, 71, 43};
			tableContainer = new TableContainer(columnNames, columnWidths);
			tableContainer.setModel("TableColorModel");
			tableContainer.initialize();
		}
		return tableContainer;
	}

	/**
	 * Borra todas las filas de la tabla.
	 */
	private void clearTable() {
		try {
			getTableContainer().removeAllRows();
		} catch (NotInitializeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Devuelve el String de un color en formato ##0, ##0, ##0
	 * @param c
	 * @return
	 */
	private String getColorString(Color c) {
		return c.getRed() + ", " + c.getGreen() + ", " + c.getBlue();
	}

	/**
	 * Añade una fila a la tabla asignando el color por parámetro. Este
	 * color asignado será el que aparezca en el botón y en el texto RGB
	 * @param color
	 */
	private void addRowToTable(Color color, String name, Double fromRange, Double toRange, String alpha){
		try {
			getTableContainer().addRow(new Object[] { color, name, getColorString(color), fromRange, toRange, alpha });
		} catch (NotInitializeException e1) {
		}
	}
	
	/**
	 * Convierte la tabla en un array de objetos para poder crear con el el objeto Palette
	 * @return
	 * @throws NotInitializeException
	 */
	private ArrayList getPalette() {
		ArrayList arrayList = new ArrayList();
		JTable jTable = getTableContainer().getTable().getJTable();
		TableColorModel model = (TableColorModel) jTable.getModel();
		for (int iRow = 0; iRow < jTable.getRowCount(); iRow++) {
			Color rgb = (Color) model.getValueAt(iRow, 0);
			ColorItem colorItem = new ColorItem();
			colorItem.setColor(new Color(
					rgb.getRed(),
					rgb.getGreen(),
					rgb.getBlue(),
					Integer.valueOf((String) model.getValueAt(iRow, 5)).intValue()));

			if (model.getValueAt(iRow, 3) != null)
				colorItem.setValue(((Double) model.getValueAt(iRow, 3)).doubleValue());

			colorItem.setNameClass((String) model.getValueAt(iRow, 1));
			arrayList.add(colorItem);
		}

		return arrayList;
	}
	
	/**
	 * Carga inicial de los colores del panel
	 * @param colorItems
	 */
	private void reloadItems() {
		listenerEnabled = false;
		ArrayList colorItems = colorTable.getColorItems();
		clearTable();
		for (int i = 0; i < colorItems.size(); i++) {
			ColorItem c1 = (ColorItem) colorItems.get(i);
			Double toRange = null;
			if ((i + 1) < colorItems.size())
				toRange = new Double(((ColorItem) colorItems.get(i + 1)).getValue());
			addRowToTable(c1.getColor(), c1.getNameClass(), new Double(c1.getValue()), toRange, c1.getColor().getAlpha() + "");
		}
		listenerEnabled = true;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
		if (!listenerEnabled)
			return;
		colorTable.createPaletteFromColorItems(getPalette(), false);
		callColorTableUIChangedListener();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.panels.IColorTableUI#getColorTable()
	 */
	public ColorTable getColorTable() {
		return colorTable;
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.panels.IColorTableUI#setColorTable(org.gvsig.raster.datastruct.ColorTable)
	 */
	public void setColorTable(ColorTable colorTable) {
		this.colorTable = colorTable;
		reloadItems();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.panels.IColorTableUI#addColorTableUIChangedListener(org.gvsig.rastertools.colortable.panels.ColorTableUIListener)
	 */
	public void addColorTableUIChangedListener(ColorTableUIListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.panels.IColorTableUI#removeColorTableUIChangedListener(org.gvsig.rastertools.colortable.panels.ColorTableUIListener)
	 */
	public void removeColorTableUIChangedListener(ColorTableUIListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Invoca el evento de cambio de un ColorTable
	 * @param colorTable
	 */
	private void callColorTableUIChangedListener() {
		Iterator acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ColorTableUIListener listener = (ColorTableUIListener) acIterator.next();
			listener.actionColorTableUIChanged(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getTitle()
	 */
	public String getTitle() {
		return RasterToolsUtil.getText(this, "tabla");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.colortable.ui.tabs.IColorTableUI#getPanel()
	 */
	public JPanel getPanel() {
		return this;
	}
}
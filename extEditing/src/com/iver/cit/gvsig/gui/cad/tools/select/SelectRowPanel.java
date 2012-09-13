package com.iver.cit.gvsig.gui.cad.tools.select;

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
* 2009 IVER T.I. S.A.   {{Task}}
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.iver.utiles.swing.jtable.JTable;
/**
*
* @author Vicente Caballero Navarro
*
*/
public class SelectRowPanel extends JPanel implements IWindow {

	private static final long serialVersionUID = 1L;

	private JPanel jTablePanel;
	private JPanel buttonsPanel;
	private JScrollPane scrollPane;
	private JButton accept, cancel;
	private JTable jTable;
	private MyTableModel tableModel;
	private MyActionListener action;

	private ArrayList<Feature> features=new ArrayList<Feature>();

	private VectorialLayerEdited vle;
	private FeatureType featureType;

	public SelectRowPanel(FeatureSet featureSet, VectorialLayerEdited vle) throws ReadException, DataException {
		super();
		this.vle = vle;
		featureType = vle.getFeatureStore().getDefaultFeatureType();
		Iterator<Feature> iterator=featureSet.iterator();
		while (iterator.hasNext()) {
			Feature feature = (Feature) iterator.next();
			features.add(feature);
		}
		action = new MyActionListener();
		initialize();
	}

	private void initialize() {
		Dimension preferred = new Dimension(400, 150);
		this.setSize(preferred);
		this.setPreferredSize(preferred);
		this.setLayout(new BorderLayout());
		this.add(getScrollPane(), BorderLayout.CENTER);
		this.add(getButtonsPanel(), BorderLayout.SOUTH);
	}

	private JPanel getTablePanel() {
		if (jTablePanel == null) {
			jTablePanel = new JPanel(new BorderLayout());
			jTablePanel.setSize(new Dimension(400, 150));
			jTablePanel.add(getTable().getTableHeader(), BorderLayout.NORTH);
			jTablePanel.add(getTable(), BorderLayout.CENTER);
		}
		return jTablePanel;
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTablePanel());
		}
		return scrollPane;
	}

	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel(new FlowLayout());
			buttonsPanel.add(getButtonAceptar());
			buttonsPanel.add(getButtonCancelar());
		}
		return buttonsPanel;
	}

	private JButton getButtonAceptar() {
		if (accept == null) {
			accept = new JButton();
			accept.setText(PluginServices.getText(this, "accept"));
			accept.addActionListener(action);
		}
		return accept;
	}

	private JButton getButtonCancelar() {
		if (cancel == null) {
			cancel = new JButton();
			cancel.setText(PluginServices.getText(this, "cancel"));
			cancel.addActionListener(action);
		}
		return cancel;
	}

	@SuppressWarnings("unchecked")
	private JTable getTable() {
		if (jTable == null) {

			jTable = new JTable();
			tableModel = new MyTableModel(features);
			jTable.setModel(tableModel);
			jTable.setSelectionBackground(Color.yellow);
			jTable.setSelectionForeground(Color.blue);
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			jTable.setCellSelectionEnabled(false);
			jTable.setRowSelectionAllowed(true);
			jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		return jTable;
	}

	/** ******************************************************* */
	/** ******************** IWindow ************************** */
	/** ******************************************************* */

	public WindowInfo getWindowInfo() {
		WindowInfo wi = new WindowInfo(WindowInfo.MODALDIALOG);
		wi.setTitle(PluginServices.getText(this, "select_rows"));
		wi.setWidth(this.getWidth());
		wi.setHeight(this.getHeight());
		return wi;
	}

	/** ******************************************************* */
	/** ***************** ActionListener ********************** */
	/** ******************************************************* */
	private class MyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			FeatureSelection rowSelecteds;
			try {
				rowSelecteds = vle.getFeatureStore().getFeatureSelection();

			if (e.getSource().equals(accept)) {
				int[] index = jTable.getSelectedRows();
				if (index.length > 0) {
					for (int i = 0; i < index.length; i++) {
						Feature sl = features.get(index[i]);
						rowSelecteds.select(sl);
					}
					vle.getFeatureStore().setSelection(rowSelecteds);
					PluginServices.getMDIManager().closeWindow(
							SelectRowPanel.this);
				} else {
					JOptionPane.showMessageDialog(null, PluginServices.getText(
							this, "select_one_row"), PluginServices.getText(
							this, "attention"), JOptionPane.WARNING_MESSAGE);
				}
			}
			} catch (ReadException e1) {
				e1.printStackTrace();
			} catch (DataException e1) {
				e1.printStackTrace();
			}
			if (e.getSource().equals(cancel)) {
				PluginServices.getMDIManager().closeWindow(SelectRowPanel.this);
			}
		}
	}

	/** ******************************************************* */
	/** ******************* TableModel ************************ */
	/** ******************************************************* */
	private class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private ArrayList<Feature> features;

		public void setSelectedSymbols(ArrayList<Feature> rs) {
			features = rs;
			fireTableDataChanged();
		}

		public MyTableModel(ArrayList<Feature> rs) {
			features = rs;
		}

		public ArrayList<Feature> getSymbolsList() {
			return features;
		}

		public int getRowCount() {
			return features.size();
		}

		public int getColumnCount() {
			return featureType.size();
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Feature r = (Feature) features.get(rowIndex);
			String s = r.get(columnIndex).toString();
			return s;
		}

		@SuppressWarnings("unchecked")
		public Class getColumnClass(int columnIndex) {
			return String.class;
		}

		public String getColumnName(int columnIndex) {
			return featureType.getAttributeDescriptor(columnIndex).getName();
		}
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}

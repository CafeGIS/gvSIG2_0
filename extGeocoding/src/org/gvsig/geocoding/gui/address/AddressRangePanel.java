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
 * 2008 Prodevelop S.L. main developer
 */

package org.gvsig.geocoding.gui.address;

import java.awt.Color;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.NumberAddress;
import org.gvsig.geocoding.address.RelationsComponent;
import org.gvsig.geocoding.address.impl.DefaultAddressComponent;
import org.gvsig.geocoding.address.impl.DefaultLiteral;
import org.gvsig.geocoding.address.impl.DefaultNumberAddress;
import org.gvsig.geocoding.extension.GeocodingController;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.geocoding.styles.impl.SimpleRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;

/**
 * Address simple and double range panel
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class AddressRangePanel extends AbstractAddressPanel {

	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(AddressRangePanel.class);

	private GeocodingController control = null;

	private JLabel jLabNumber;
	private JLabel jLabType;
	private JPanel jPanNumber;
	private JPanel jPanTable;
	private JScrollPane jScrollTable;
	private JTable jTableAddress;
	private JTextField jTextNumber;
	private JComboBox jComboNumber;
	private JPanel jPanId;
	private JLabel jLabId;
	private JComboBox jComboId;

	/**
	 * Constructor
	 */
	public AddressRangePanel(GeocodingController control, Literal literal) {
		this.control = control;
		initComponents();

		setImages();
		setMesages();

		// create table model
		if (control.getGmodel().isSimple()) {
			createSimpleTableModel(literal);
		} else {
			List<String> descs = control.getGmodel()
					.getListDescriptorSelectedTable();
			createTableTableModel(literal, descs);
			jComboId.setModel(createFieldsComboModel(descs));
			jComboNumber.setModel(createFieldsComboModel(descs));
		}

	}

	/**
	 * Set images in the panel
	 */
	private void setImages() {
		String baseDir = PluginServices.getPluginServices(this)
				.getClassLoader().getBaseDir();

		AbstractGeocodingStyle style = control.getPattern().getSource()
				.getStyle();
		if (style instanceof SimpleRange) {
			jLabType.setIcon(new ImageIcon(baseDir + File.separator + "images"
					+ File.separator + "new" + File.separator
					+ "simplerange.png"));
		} else {
			jLabType.setIcon(new ImageIcon(baseDir + File.separator + "images"
					+ File.separator + "new" + File.separator
					+ "doublerange.png"));
		}
	}

	/**
	 * Set strings in the panel
	 */
	private void setMesages() {
		PluginServices ps = PluginServices.getPluginServices(this);

		jLabNumber.setText(ps.getText("xnumAddress"));
		jLabId.setText(ps.getText("xidfield"));

		DefaultTableModel model = (DefaultTableModel) jTableAddress.getModel();
		String element = ps.getText("xelement");
		String address1 = ps.getText("xvalue");
		Object[] headers = { element, address1 };
		model.setColumnIdentifiers(headers);
	}

	/**
	 * Initialize components
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jLabType = new JLabel();
		jPanTable = new JPanel();
		jScrollTable = new JScrollPane();
		jTableAddress = new JTable();
		jPanNumber = new JPanel();
		jLabNumber = new JLabel();
		jTextNumber = new JTextField();
		jComboNumber = new JComboBox();
		jLabId = new JLabel();
		jComboId = new JComboBox();
		jPanId = new JPanel();

		setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(3, 15, 5, 10);
		add(jLabType, gridBagConstraints);

		jPanTable.setLayout(new java.awt.GridBagLayout());

		jPanId.setLayout(new java.awt.GridBagLayout());

		if (!control.getGmodel().isSimple()) {
			jLabId.setText("Field ID");
			jLabId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 5);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			jPanId.add(jLabId, gridBagConstraints);

			jComboId.setModel(new javax.swing.DefaultComboBoxModel(
					new String[] { "" }));
			jComboId.setMinimumSize(new java.awt.Dimension(200, 20));
			jComboId.setName("jComboId");

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
			jPanId.add(jComboId, gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
			jPanTable.add(jPanId, gridBagConstraints);
		}

		jTableAddress
				.setModel(new javax.swing.table.DefaultTableModel(
						new Object[][] { { null, null }, { null, null },
								{ null, null } }, new String[] { "Element",
								"Value" }) {
					Class[] types = new Class[] { java.lang.String.class,
							java.lang.String.class };
					boolean[] canEdit = new boolean[] { false, true };

					public Class getColumnClass(int columnIndex) {
						return types[columnIndex];
					}

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return canEdit[columnIndex];
					}
				});

		jScrollTable.setViewportView(jTableAddress);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanTable.add(jScrollTable, gridBagConstraints);

		jPanNumber.setLayout(new java.awt.GridBagLayout());

		jLabNumber.setText("Number");
		jLabNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 5);
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		jPanNumber.add(jLabNumber, gridBagConstraints);

		if (control.getGmodel().isSimple()) {
			jTextNumber.setText("1");
			jTextNumber.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
			jTextNumber.setMinimumSize(new java.awt.Dimension(30, 19));
			jTextNumber.setPreferredSize(new java.awt.Dimension(70, 19));
			jTextNumber.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent evt) {
					evNumberLostFocus(evt);
				}
			});
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
			jPanNumber.add(jTextNumber, gridBagConstraints);
		} else {

			jComboNumber.setModel(new javax.swing.DefaultComboBoxModel(
					new String[] { "" }));
			jComboNumber.setMinimumSize(new java.awt.Dimension(200, 20));
			jComboNumber.setName("jComboNumber");

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
			jPanNumber.add(jComboNumber, gridBagConstraints);
		}

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
		jPanTable.add(jPanNumber, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 10);
		add(jPanTable, gridBagConstraints);
	}

	/**
	 * Number text field lost focus
	 * 
	 * @param evt
	 */
	private void evNumberLostFocus(java.awt.event.FocusEvent evt) {
		String txt = jTextNumber.getText();
		try {
			Integer.parseInt(txt);
		} catch (Exception e) {
			jTextNumber.setText("1");
			jTextNumber.requestFocus();
		}
	}

	/**
	 * Create a simple table model from literal components
	 * 
	 * @param lit
	 */
	private void createSimpleTableModel(Literal lit) {
		DefaultTableModel model = (DefaultTableModel) jTableAddress.getModel();

		// first column
		TableColumn column0 = jTableAddress.getColumnModel().getColumn(0);
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setBackground(new Color(255, 253, 224));
		column0.setCellRenderer(render);

		// fill elements
		super.clearTableModel(model);

		for (Object obj : lit) {
			RelationsComponent comp = (RelationsComponent) obj;
			String key = comp.getKeyElement();
			Object[] row = { key, "" };
			model.addRow(row);
		}
		jTableAddress.setModel(model);
	}

	/**
	 * Create a simple table model from literal components
	 * 
	 * @param lit
	 */
	private void createTableTableModel(Literal lit,
			List<String> descs) {
		DefaultTableModel model = (DefaultTableModel) jTableAddress.getModel();

		// first column
		TableColumn column0 = jTableAddress.getColumnModel().getColumn(0);
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setBackground(new Color(255, 253, 224));
		column0.setCellRenderer(render);

		// fill elements
		super.clearTableModel(model);

		// Put combos in the column 1
		super.putCombosInTable(jTableAddress, descs, 1);

		// put the elements in the column 0
		for (Object obj : lit) {
			RelationsComponent comp = (RelationsComponent) obj;
			// key
			String key = comp.getKeyElement();
			// insert
			Object[] row = { key, "" };
			model.addRow(row);
		}
		jTableAddress.setModel(model);
	}

	/**
	 * get address from user interface
	 * 
	 * @param model
	 * @return
	 */
	public Address getSimpleAddress() {

		jTableAddress.validate();
		jTableAddress.setRowSelectionInterval(0, 0);
		DefaultTableModel model = (DefaultTableModel) jTableAddress.getModel();
		NumberAddress address = new DefaultNumberAddress();
		Literal mainLiteral = new DefaultLiteral();
		Vector vec = model.getDataVector();

		for (int i = 0; i < vec.size(); i++) {
			Vector vecc = (Vector) vec.elementAt(i);
			String key = (String) vecc.elementAt(0);
			String value = (String) vecc.elementAt(1);
			mainLiteral.add(new DefaultAddressComponent(key, value));
		}
		address.setMainLiteral(mainLiteral);
		String strNumber = jTextNumber.getText();
		address.setNumber(Integer.parseInt(strNumber));
		return address;
	}

	/**
	 * Get the addres from table
	 * 
	 * @param model
	 * @param row
	 * @return
	 */
	public Address getTableAddress(int row) {

		jTableAddress.validate();
		jTableAddress.setRowSelectionInterval(0, 0);
		DefaultTableModel model = (DefaultTableModel) jTableAddress.getModel();

		// save in the gmodel the descriptor of identifier field
		String descid = (String) jComboId.getModel().getSelectedItem();
		control.getGmodel().setIdMasiveTable(descid);

		// Get store from Gmodel and the feature
		FeatureStore store = control.getGmodel().getSelectedTableStore();
		FeatureSet features = null;
		Feature feature = null;
		try {
			features = store.getFeatureSet();
			Iterator<Feature> it = features.iterator(row);
			feature = it.next();
		} catch (DataException e) {
			log.error("Get the feature of FeatureStore", e);
		}

		// Create the address
		NumberAddress address = new DefaultNumberAddress();
		Literal literal = new DefaultLiteral();
		Vector vec = model.getDataVector();

		for (int i = 0; i < vec.size(); i++) {
			Vector vecc = (Vector) vec.elementAt(i);
			String key = (String) vecc.elementAt(0);
			String fieldName = (String) vecc.elementAt(1);
			Object obj = feature.get(fieldName);
			String value = obj.toString();
			literal.add(new DefaultAddressComponent(key, value));
		}

		address.setMainLiteral(literal);
		String nfieldName = (String) jComboNumber.getModel()
				.getSelectedItem();
		Object nobj = feature.get(nfieldName);
		String strNumber = nobj.toString();
		int ii = 1;
		try {
			ii = Integer.parseInt(strNumber);
			address.setNumber(ii);
		} catch (Exception e) {
			address.setNumber(ii);
		}

		return address;
	}

}

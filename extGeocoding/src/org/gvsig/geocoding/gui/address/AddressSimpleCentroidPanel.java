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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.RelationsComponent;
import org.gvsig.geocoding.address.impl.DefaultAddress;
import org.gvsig.geocoding.address.impl.DefaultAddressComponent;
import org.gvsig.geocoding.address.impl.DefaultLiteral;
import org.gvsig.geocoding.extension.GeocodingController;
import org.gvsig.geocoding.utils.GeocodingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;

/**
 * Address simple centroid panel
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class AddressSimpleCentroidPanel extends AbstractAddressPanel {

	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory
			.getLogger(AddressSimpleCentroidPanel.class);

	private GeocodingController control = null;

	private JLabel jLabType;
	private JPanel jPanAddress;
	private JScrollPane jScrollAddress;
	private JTable jTableAddress;
	private JLabel jLabId;
	private JPanel jPanId;
	private JComboBox jComboId;

	/**
	 * Constructor
	 */
	public AddressSimpleCentroidPanel(GeocodingController control, Literal literal) {

		this.control = control;
		initComponents();

		setImages();
		setMesages();

		// create table model
		if (this.control.getGmodel().isSimple()) {
			createSimpleTableModel(literal);
		} else {
			List<String> descs = this.control.getGmodel()
					.getListDescriptorSelectedTable();
			createTableTableModel(literal, descs);
			jComboId.setModel(createFieldsComboModel(descs));
		}

	}

	/**
	 * Set images in the panel
	 */
	private void setImages() {
		String baseDir = PluginServices.getPluginServices(this)
				.getClassLoader().getBaseDir();

		jLabType.setIcon(new ImageIcon(baseDir + File.separator + "images"
				+ File.separator + "new" + File.separator + "centroid.png"));
	}

	/**
	 * Set strings in the panel
	 */
	private void setMesages() {
		PluginServices ps = PluginServices.getPluginServices(this);

		DefaultTableModel model = (DefaultTableModel) jTableAddress.getModel();
		String element = ps.getText("xelement");
		String value = ps.getText("xvalue");
		Object[] headers = { element, value };
		model.setColumnIdentifiers(headers);
		jLabId.setText(ps.getText("xidfield"));
	}

	/**
	 * Initialize components
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jLabType = new JLabel();
		jPanAddress = new JPanel();
		jScrollAddress = new JScrollPane();
		jTableAddress = new JTable();
		jLabId = new JLabel();
		jComboId = new JComboBox();
		jPanId = new JPanel();

		setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(3, 15, 5, 5);
		add(jLabType, gridBagConstraints);

		jPanAddress.setLayout(new java.awt.GridBagLayout());
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
			jPanAddress.add(jPanId, gridBagConstraints);
		}

		jTableAddress
				.setModel(new javax.swing.table.DefaultTableModel(
						new Object[][] { { null, null }, { null, null },
								{ null, null } }, new String[] { "Element",
								"value" }) {
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
		jScrollAddress.setViewportView(jTableAddress);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanAddress.add(jScrollAddress, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(3, 15, 5, 10);
		add(jPanAddress, gridBagConstraints);
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
			// Element
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

		Address address = new DefaultAddress();
		Literal literal = new DefaultLiteral();
		Vector vec = model.getDataVector();

		for (int i = 0; i < vec.size(); i++) {
			Vector vecc = (Vector) vec.elementAt(i);
			String key = (String) vecc.elementAt(0);
			String value = (String) vecc.elementAt(1);
			literal.add(new DefaultAddressComponent(key, value));
		}

		address.setMainLiteral(literal);
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
		Address address = new DefaultAddress();
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

		return address;
	}

}

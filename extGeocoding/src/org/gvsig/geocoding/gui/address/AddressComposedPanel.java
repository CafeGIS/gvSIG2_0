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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
import org.gvsig.geocoding.address.ComposedAddress;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.RelationsComponent;
import org.gvsig.geocoding.address.impl.DefaultAddressComponent;
import org.gvsig.geocoding.address.impl.DefaultComposedAddress;
import org.gvsig.geocoding.address.impl.DefaultLiteral;
import org.gvsig.geocoding.extension.GeocodingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;

/**
 * Address composed panel
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class AddressComposedPanel extends AbstractAddressPanel {

	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(AddressComposedPanel.class);

	private GeocodingController control = null;

	private JLabel jLabTypeBetween;
	private JLabel jLabTypeCross;
	private JPanel jPanAddressBetween;
	private JPanel jPanAddressCross;
	private JPanel jPanBetween;
	private JPanel jPanCross;
	private JScrollPane jScrollAddressBetween;
	private JScrollPane jScrollAddressCross;
	private JTabbedPane jTabbedComposed;
	private JTable jTableAddressBetween;
	private JTable jTableAddressCross;
	private JLabel jLabIdCross;
	private JPanel jPanIdCross;
	private JComboBox jComboIdCross;
	private JLabel jLabIdBetween;
	private JPanel jPanIdBetween;
	private JComboBox jComboIdBetween;

	/**
	 * Constructor
	 */
	public AddressComposedPanel(GeocodingController control, Literal literal) {

		this.control = control;
		initComponents();

		setImages();
		setMesages();

		// create table model

		if (control.getGmodel().isSimple()) {
			createSimpleTablesModels(literal);
		} else {
			List<String> descs = control.getGmodel()
					.getListDescriptorSelectedTable();
			createTableTablesModels(literal, descs);
			jComboIdCross.setModel(createFieldsComboModel(descs));
			jComboIdBetween.setModel(createFieldsComboModel(descs));
		}
	}

	/**
	 * Set images in the panel
	 */
	private void setImages() {
		String baseDir = PluginServices.getPluginServices(this)
				.getClassLoader().getBaseDir();

		jLabTypeCross
				.setIcon(new ImageIcon(baseDir + File.separator + "images"
						+ File.separator + "new" + File.separator
						+ "composedcross.png"));
		jLabTypeBetween.setIcon(new ImageIcon(baseDir + File.separator
				+ "images" + File.separator + "new" + File.separator
				+ "composedbetween.png"));
	}

	/**
	 * Set strings in the panel
	 */
	private void setMesages() {
		PluginServices ps = PluginServices.getPluginServices(this);

		String element = ps.getText("xelement");

		DefaultTableModel cmodel = (DefaultTableModel) jTableAddressCross
				.getModel();
		String value1 = ps.getText("xvalue1");
		String value2 = ps.getText("xvalue2");
		Object[] headers = { element, value1, value2 };
		cmodel.setColumnIdentifiers(headers);
		jTableAddressCross.setModel(cmodel);

		DefaultTableModel bmodel = (DefaultTableModel) jTableAddressBetween
				.getModel();
		String value11 = ps.getText("xmainvalue");
		String value22 = ps.getText("xvalue2");
		String value33 = ps.getText("xvalue3");
		Object[] bheaders = { element, value11, value22, value33 };
		bmodel.setColumnIdentifiers(bheaders);
		jTableAddressBetween.setModel(bmodel);

		// tabs
		String titleCross = ps.getText("xcross");
		jTabbedComposed.setTitleAt(0, titleCross);
		String titleBetween = ps.getText("xbetween");
		jTabbedComposed.setTitleAt(1, titleBetween);
	}

	/**
	 * Initialize components
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jTabbedComposed = new javax.swing.JTabbedPane();
		jPanCross = new javax.swing.JPanel();
		jLabTypeCross = new javax.swing.JLabel();
		jPanAddressCross = new javax.swing.JPanel();
		jScrollAddressCross = new javax.swing.JScrollPane();
		jTableAddressCross = new javax.swing.JTable();
		jPanBetween = new javax.swing.JPanel();
		jLabTypeBetween = new javax.swing.JLabel();
		jPanAddressBetween = new javax.swing.JPanel();
		jScrollAddressBetween = new javax.swing.JScrollPane();
		jTableAddressBetween = new javax.swing.JTable();
		jLabIdCross = new JLabel();
		jComboIdCross = new JComboBox();
		jPanIdCross = new JPanel();
		jLabIdBetween = new JLabel();
		jComboIdBetween = new JComboBox();
		jPanIdBetween = new JPanel();

		setLayout(new java.awt.GridBagLayout());

		jPanCross.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(3, 15, 5, 10);
		jPanCross.add(jLabTypeCross, gridBagConstraints);

		jPanAddressCross.setLayout(new java.awt.GridBagLayout());

		jPanIdCross.setLayout(new java.awt.GridBagLayout());

		if (!control.getGmodel().isSimple()) {
			jLabIdCross.setText("Field ID");
			jLabIdCross
					.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 5);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			jPanIdCross.add(jLabIdCross, gridBagConstraints);

			jComboIdCross.setModel(new javax.swing.DefaultComboBoxModel(
					new String[] { "" }));
			jComboIdCross.setMinimumSize(new java.awt.Dimension(200, 20));
			jComboIdCross.setName("jComboId");

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
			jPanIdCross.add(jComboIdCross, gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
			jPanAddressCross.add(jPanIdCross, gridBagConstraints);
		}

		jTableAddressCross.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null }, { null, null, null },
						{ null, null, null } }, new String[] { "Elements",
						"Values address 1 ", "Values address 2" }) {
			Class[] types = new Class[] { java.lang.String.class,
					java.lang.String.class, java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, true, true };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});

		jScrollAddressCross.setViewportView(jTableAddressCross);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanAddressCross.add(jScrollAddressCross, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 15, 10, 10);
		jPanCross.add(jPanAddressCross, gridBagConstraints);

		jTabbedComposed.addTab("tab1", jPanCross);

		jPanBetween.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(5, 15, 5, 10);
		jPanBetween.add(jLabTypeBetween, gridBagConstraints);

		jPanAddressBetween.setLayout(new java.awt.GridBagLayout());

		jPanIdBetween.setLayout(new java.awt.GridBagLayout());

		if (!control.getGmodel().isSimple()) {
			jLabIdBetween.setText("Field ID");
			jLabIdBetween
					.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 5);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			jPanIdBetween.add(jLabIdBetween, gridBagConstraints);

			jComboIdBetween.setModel(new javax.swing.DefaultComboBoxModel(
					new String[] { "" }));
			jComboIdBetween.setMinimumSize(new java.awt.Dimension(200, 20));
			jComboIdBetween.setName("jComboId");

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
			jPanIdBetween.add(jComboIdBetween, gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
			jPanAddressBetween.add(jPanIdBetween, gridBagConstraints);
		}

		jTableAddressBetween
				.setModel(new javax.swing.table.DefaultTableModel(
						new Object[][] { { null, null, null, null },
								{ null, null, null, null },
								{ null, null, null, null } }, new String[] {
								"Elements", "Values main address",
								"Values address 2", "values address 3" }) {
					Class[] types = new Class[] { java.lang.String.class,
							java.lang.String.class, java.lang.String.class,
							java.lang.String.class };
					boolean[] canEdit = new boolean[] { false, true, true, true };

					public Class getColumnClass(int columnIndex) {
						return types[columnIndex];
					}

					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return canEdit[columnIndex];
					}
				});

		jScrollAddressBetween.setViewportView(jTableAddressBetween);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanAddressBetween.add(jScrollAddressBetween, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 15, 10, 10);
		jPanBetween.add(jPanAddressBetween, gridBagConstraints);

		jTabbedComposed.addTab("tab2", jPanBetween);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		add(jTabbedComposed, gridBagConstraints);
	}

	/**
	 * Create a simple tables models from literal components
	 * 
	 * @param lit
	 */
	private void createSimpleTablesModels(Literal lit) {

		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setBackground(new Color(255, 253, 224));

		// Cross tab
		DefaultTableModel modelcross = (DefaultTableModel) jTableAddressCross
				.getModel();
		TableColumn colcross0 = jTableAddressCross.getColumnModel()
				.getColumn(0);
		colcross0.setCellRenderer(render);

		// fill elements
		super.clearTableModel(modelcross);

		for (Object obj : lit) {
			RelationsComponent comp = (RelationsComponent) obj;
			String key = comp.getKeyElement();
			Object[] row = { key, "", "" };
			modelcross.addRow(row);
		}
		jTableAddressCross.setModel(modelcross);

		// Between tab
		DefaultTableModel modelbetween = (DefaultTableModel) jTableAddressBetween
				.getModel();
		// first column
		TableColumn colbetween0 = jTableAddressBetween.getColumnModel()
				.getColumn(0);
		colbetween0.setCellRenderer(render);

		// fill elements
		super.clearTableModel(modelbetween);

		for (Object obj : lit) {
			RelationsComponent comp = (RelationsComponent) obj;
			String key = comp.getKeyElement();
			Object[] row = { key, "", "", "" };
			modelbetween.addRow(row);
		}
		jTableAddressBetween.setModel(modelbetween);

	}

	/**
	 * Create a table tables models from literal components
	 * 
	 * @param lit
	 */
	private void createTableTablesModels(Literal lit,
			List<String> descs) {

		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setBackground(new Color(255, 253, 224));

		// Croos tab
		DefaultTableModel modelcross = (DefaultTableModel) jTableAddressCross
				.getModel();

		// first column
		TableColumn columncross0 = jTableAddressCross.getColumnModel()
				.getColumn(0);
		columncross0.setCellRenderer(render);

		// fill elements
		super.clearTableModel(modelcross);

		// Put combos in the column 1
		super.putCombosInTable(jTableAddressCross, descs, 1);

		// Put combos in the column 2
		super.putCombosInTable(jTableAddressCross, descs, 2);

		// put the elements in the column 0
		for (Object obj : lit) {
			RelationsComponent comp = (RelationsComponent) obj;
			// key
			String key = comp.getKeyElement();

			// insert
			Object[] row = { key, "", "" };
			modelcross.addRow(row);
		}
		jTableAddressCross.setModel(modelcross);

		// Between tab
		DefaultTableModel modelbetween = (DefaultTableModel) jTableAddressBetween
				.getModel();

		// first column
		TableColumn columnbetween0 = jTableAddressBetween.getColumnModel()
				.getColumn(0);
		columnbetween0.setCellRenderer(render);

		// fill elements
		super.clearTableModel(modelbetween);

		// Put combos in the column 1
		super.putCombosInTable(jTableAddressBetween, descs, 1);

		// Put combos in the column 2
		super.putCombosInTable(jTableAddressBetween, descs, 2);

		// Put combos in the column 3
		super.putCombosInTable(jTableAddressBetween, descs, 3);

		// put the elements in the column 0
		for (Object obj : lit) {
			RelationsComponent comp = (RelationsComponent) obj;
			// key
			String key = comp.getKeyElement();

			// insert
			Object[] row = { key, "", "", "" };
			modelbetween.addRow(row);
		}
		jTableAddressBetween.setModel(modelbetween);
	}

	/**
	 * get address from user interface
	 * 
	 * @param model
	 * @return
	 */
	public Address getSimpleAddress() {

		jTableAddressCross.validate();
		jTableAddressBetween.validate();
		jTableAddressCross.setRowSelectionInterval(0, 0);
		jTableAddressBetween.setRowSelectionInterval(0, 0);
		int n = jTabbedComposed.getSelectedIndex();
		DefaultTableModel model = null;
		if (n == 0) {
			jTableAddressCross.setRowSelectionInterval(0, 0);
			model = (DefaultTableModel) jTableAddressCross.getModel();
		}
		if (n == 1) {
			jTableAddressBetween.setRowSelectionInterval(0, 0);
			model = (DefaultTableModel) jTableAddressBetween.getModel();
		}

		ComposedAddress address = new DefaultComposedAddress();
		Literal mainLiteral = new DefaultLiteral();
		Literal literal2 = new DefaultLiteral();
		Literal literal3 = new DefaultLiteral();
		List<Literal> intersectionLiterals = new ArrayList<Literal>();
		Vector vec = model.getDataVector();
		// get main and secon address
		for (int i = 0; i < vec.size(); i++) {
			Vector vecc = (Vector) vec.elementAt(i);
			String key = (String) vecc.elementAt(0);
			String value = (String) vecc.elementAt(1);
			String value2 = (String) vecc.elementAt(2);
			mainLiteral.add(new DefaultAddressComponent(key, value));
			literal2.add(new DefaultAddressComponent(key, value2));
		}
		address.setMainLiteral(mainLiteral);
		intersectionLiterals.add(literal2);
		// get third address if exist
		int num = ((Vector) vec.elementAt(0)).size();
		if (num == 4) {
			for (int i = 0; i < vec.size(); i++) {
				Vector vecc = (Vector) vec.elementAt(i);
				String key = (String) vecc.elementAt(0);
				String value3 = (String) vecc.elementAt(3);
				literal3.add(new DefaultAddressComponent(key, value3));
			}
			intersectionLiterals.add(literal3);
		}

		address.setIntersectionLiterals(intersectionLiterals);

		return address;
	}

	/**
	 * Get the addres from table
	 * 
	 * @param model
	 * @param row
	 * @return
	 */
	public Address getTableAddress(DefaultTableModel model, int row) {

		jTableAddressCross.validate();
		jTableAddressCross.setRowSelectionInterval(0, 0);
		jTableAddressBetween.validate();
		jTableAddressBetween.setRowSelectionInterval(0, 0);

		// save in the gmodel the descriptor of identifier field
		int n = jTabbedComposed.getSelectedIndex();
		if (n == 0) {
			String desc = (String) jComboIdCross.getModel()
					.getSelectedItem();
			control.getGmodel().setIdMasiveTable(desc);
		} else {
			String desc = (String) jComboIdBetween.getModel()
					.getSelectedItem();
			control.getGmodel().setIdMasiveTable(desc);
		}

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
		ComposedAddress address = new DefaultComposedAddress();
		Literal mainLiteral = new DefaultLiteral();
		Literal literal2 = new DefaultLiteral();
		Literal literal3 = new DefaultLiteral();
		List<Literal> intersectionLiterals = new ArrayList<Literal>();
		Vector vec = model.getDataVector();

		for (int i = 0; i < vec.size(); i++) {
			Vector vecc = (Vector) vec.elementAt(i);
			String key = (String) vecc.elementAt(0);
			String fieldName1 = (String) vecc.elementAt(1);
			String fieldName2 = (String) vecc.elementAt(2);
			Object obj1 = feature.get(fieldName1);
			Object obj2 = feature.get(fieldName2);
			String value1 = obj1.toString();
			String value2 = obj2.toString();
			mainLiteral.add(new DefaultAddressComponent(key, value1));
			literal2.add(new DefaultAddressComponent(key, value2));
		}

		address.setMainLiteral(mainLiteral);
		intersectionLiterals.add(literal2);

		// get third address if exist
		int num = ((Vector) vec.elementAt(0)).size();
		if (num == 4) {
			for (int i = 0; i < vec.size(); i++) {
				Vector vecc = (Vector) vec.elementAt(i);
				String key = (String) vecc.elementAt(0);
				String fieldName3 = (String) vecc.elementAt(3);
				Object obj3 = feature.get(fieldName3);
				String value3 = obj3.toString();
				literal3.add(new DefaultAddressComponent(key, value3));
			}
			intersectionLiterals.add(literal3);
		}
		address.setIntersectionLiterals(intersectionLiterals);

		return address;
	}

}

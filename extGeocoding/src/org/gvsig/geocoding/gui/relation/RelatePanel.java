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

package org.gvsig.geocoding.gui.relation;

import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.geocoding.extension.GeocodingController;
import org.gvsig.geocoding.utils.GeocodingTags;
import org.gvsig.geocoding.utils.GeocodingUtils;
import org.gvsig.project.document.table.FeatureTableDocument;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class RelatePanel extends JPanel implements IWindow {

	private static final long serialVersionUID = 1L;

	private GeocodingController control = null;
	private FLyrVect lyr = null;

	private JButton jButRelate;
	private JButton jButCancel;
	private JComboBox jComboResultTable;
	private JPanel jPanButtons;
	private JPanel jPanResultTable;

	/**
	 * Contructor with control
	 */
	public RelatePanel(GeocodingController control) {
		this.control = control;
		initComponents();

		setImages();
		setMesages();
	}

	/**
	 * initialize panel components
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jPanResultTable = new javax.swing.JPanel();
		jComboResultTable = new javax.swing.JComboBox();
		jPanButtons = new javax.swing.JPanel();
		jButRelate = new javax.swing.JButton();
		jButCancel = new javax.swing.JButton();

		setLayout(new java.awt.GridBagLayout());

		jPanResultTable.setBorder(javax.swing.BorderFactory
				.createTitledBorder("DBF Table All Results"));
		jPanResultTable.setLayout(new java.awt.GridBagLayout());

		jComboResultTable.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
		jPanResultTable.add(jComboResultTable, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		add(jPanResultTable, gridBagConstraints);

		jPanButtons.setLayout(new java.awt.GridBagLayout());

		jButRelate.setText("Relate");
		jButRelate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evRelate(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		jPanButtons.add(jButRelate, gridBagConstraints);

		jButCancel.setText("Cancel");
		jButCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evCancel(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		jPanButtons.add(jButCancel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		add(jPanButtons, gridBagConstraints);
	}

	/**
	 * relate event
	 * 
	 * @param evt
	 */
	private void evRelate(java.awt.event.ActionEvent evt) {
		PluginServices ps = PluginServices.getPluginServices(this);
		FeatureTableDocument table = (FeatureTableDocument) jComboResultTable
				.getModel().getSelectedItem();
		if (this.lyr != null) {
			this.lyr.setProperty(GeocodingTags.GEOCODINGPROPERTY, table);
			String message1 = ps.getText("message1") + " " + table.getName();
			String message2 = message1 + " " + ps.getText("message2");
			String message3 = message2 + " " + this.lyr.getName();
			String title = ps.getText("relatelayer");

			JOptionPane.showMessageDialog(null, message3, title,
					JOptionPane.PLAIN_MESSAGE);
		}
		this.closePanel();
	}

	/**
	 * cancel event
	 * 
	 * @param evt
	 */
	private void evCancel(java.awt.event.ActionEvent evt) {
		this.closePanel();
	}

	/**
	 * Close panel
	 */
	private void closePanel() {
		IWindow[] iws = PluginServices.getMDIManager().getAllWindows();
		for (int i = 0; i < iws.length; i++) {
			if (iws[i] instanceof RelatePanel) {
				PluginServices.getMDIManager().closeWindow(iws[i]);
			}
		}
	}

	/**
	 * set panel icons
	 */
	private void setImages() {
		PluginServices ps = PluginServices.getPluginServices(this);
		if (ps != null) {
			String baseDir = ps.getClassLoader().getBaseDir();
			jButRelate.setIcon(new ImageIcon(baseDir + File.separator
					+ "images" + File.separator + "icons16" + File.separator
					+ "open.png"));
			jButCancel.setIcon(new ImageIcon(baseDir + File.separator
					+ "images" + File.separator + "icons16" + File.separator
					+ "out.png"));
		}
	}

	/**
	 * set panel strings
	 */
	private void setMesages() {
		PluginServices ps = PluginServices.getPluginServices(this);
		if (ps != null) {
			jPanResultTable.setBorder(GeocodingUtils.getTitledBorder(ps
					.getText("pantable")));
			this.jButRelate.setText(ps.getText("butrelate"));
			this.jButCancel.setText(ps.getText("butcancel"));
		}
	}

	/**
	 * get window info
	 */
	public WindowInfo getWindowInfo() {
		PluginServices ps = PluginServices.getPluginServices(this);
		WindowInfo info = new WindowInfo(WindowInfo.MODALDIALOG
				+ WindowInfo.RESIZABLE);
		info.setMinimumSize(this.getMinimumSize());
		info.setWidth(350);
		info.setHeight(80);
		String title = ps.getText("relatelayer");
		info.setTitle(title);
		return info;
	}

	public Object getWindowProfile() {

		return null;
	}

	/**
	 * Put availables project tables in combo
	 */
	public void putTablesInCombo() {
		// add tables in combo
		DefaultComboBoxModel comboModel = control.getModelgvSIGTables();
		if (comboModel == null) {
			comboModel = new DefaultComboBoxModel();
		}
		jComboResultTable.setModel(comboModel);
		jComboResultTable.validate();
	}

	/**
	 * set selected layer with property "GeocodingResult"
	 * 
	 * @param lyr
	 */
	public void setLyr(FLyrVect lyr) {
		this.lyr = lyr;
	}

}

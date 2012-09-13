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
 * 2008 Prodevelop S.L.main developer
 */
package org.gvsig.geocoding.preferences;

import com.iver.andami.PluginServices;

/**
 * Geocoding preferences panel
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class PreferencesGeocoPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;

	/* COMPONENTS */
	private javax.swing.JLabel jLabEdit;
	private javax.swing.JLabel jLabLocation;
	private javax.swing.JPanel jPanElememts;
	private javax.swing.JScrollPane jScrollPaneArea;
	private javax.swing.JTextArea jTextArea;
	private javax.swing.JTextField jTextFile;

	private String text = "";

	/**
	 * Constructor
	 */
	public PreferencesGeocoPanel() {
		initComponents();
	}

	/**
	 * Panel's components
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jPanElememts = new javax.swing.JPanel();
		jScrollPaneArea = new javax.swing.JScrollPane();
		jTextArea = new javax.swing.JTextArea();
		jTextFile = new javax.swing.JTextField();
		jLabEdit = new javax.swing.JLabel();
		jLabLocation = new javax.swing.JLabel();

		setPreferredSize(new java.awt.Dimension(450, 350));
		setLayout(new java.awt.GridBagLayout());

		jPanElememts.setBorder(javax.swing.BorderFactory
				.createTitledBorder(PluginServices.getText(this,
						"preference_file")));
		jPanElememts.setLayout(new java.awt.GridBagLayout());

		jTextArea.setColumns(20);
		jTextArea.setFont(new java.awt.Font("Arial", 1, 11));
		jTextArea.setRows(5);
		jTextArea.setMargin(new java.awt.Insets(5, 10, 5, 5));
		jScrollPaneArea.setViewportView(jTextArea);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
		jPanElememts.add(jScrollPaneArea, gridBagConstraints);

		jTextFile.setBackground(new java.awt.Color(204, 204, 204));
		jTextFile.setEditable(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
		jPanElememts.add(jTextFile, gridBagConstraints);

		jLabEdit.setText(PluginServices.getText(this, "preference_edit"));
		jLabEdit.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
		jPanElememts.add(jLabEdit, gridBagConstraints);

		jLabLocation.setText(PluginServices
				.getText(this, "preference_location"));
		jLabLocation.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
		jPanElememts.add(jLabLocation, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
		add(jPanElememts, gridBagConstraints);
	}

	public void setFileName(String name) {
		jTextFile.setText(name);
	}

	public void setFileText(String text) {
		jTextArea.setText(text);
	}

	public String getFileText() {
		return jTextArea.getText();
	}

	/* EVENTS */

	private void EvJButSave(java.awt.event.ActionEvent evt) {
		text = jTextArea.getText();
	}

}

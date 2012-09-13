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
package org.gvsig.normalization.preferences;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;

/**
 * Panel of preferences
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class PreferencesNormPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory
	.getLogger(NormPreferences.class);

	private JButton jButAdd;
	private JLabel jLabFile;
	private JTextField jTextPath;
	private String folder = "";

	/**
	 * Cnstructor
	 * 
	 * @param fol
	 */
	public PreferencesNormPanel(String fol) {
		initComponents();
		updateLabels();
		folder = fol;
		setFolderPattern(folder);
	}

	/**
	 * Initialize panel components
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jLabFile = new javax.swing.JLabel();
		jTextPath = new javax.swing.JTextField();
		jButAdd = new javax.swing.JButton();

		setLayout(new java.awt.GridBagLayout());
		setBorder(javax.swing.BorderFactory
				.createTitledBorder("folder path of normalization patterns"));
		setMinimumSize(new Dimension(500, 100));

		jLabFile.setText("path pattern folder");
		jLabFile.setMinimumSize(new java.awt.Dimension(200, 20));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		add(jLabFile, gridBagConstraints);
		jTextPath.setMinimumSize(new java.awt.Dimension(200, 20));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
		add(jTextPath, gridBagConstraints);

		jButAdd.setText("Change");
		jButAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evtChangeFile(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
		add(jButAdd, gridBagConstraints);
	}

	/**
	 * Event of change file
	 * 
	 * @param evt
	 */
	private void evtChangeFile(java.awt.event.ActionEvent evt) {
		File fol = loadFolder();
		if (folder != null) {
			folder = fol.getAbsolutePath();
			setFolderPattern(folder);
		} else {
			log.debug("Folder null");
		}
	}

	/**
	 * Change the panel string (Internationalization)
	 */
	private void updateLabels() {

		jLabFile.setText(PluginServices.getText(this, "pref_path_folder"));
		jButAdd.setText(PluginServices.getText(this, "pref_change_folder"));

		this.setBorder(BorderFactory.createTitledBorder(PluginServices.getText(
				this, "pref_title_folder")));
	}

	/**
	 * Set the file
	 * 
	 * @param file
	 */
	public void setFolderPattern(String file) {
		jTextPath.setText(file);
	}

	/**
	 * Get the file
	 * 
	 * @return
	 */
	public String getFolderPattern() {

		return jTextPath.getText().trim();
	}

	/**
	 * This method loads a folder where you save the Normalization pattern
	 * 
	 * @return File folder
	 */
	private File loadFolder() {

		File thefolder = null;

		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setDialogTitle(PluginServices.getText(this, "pref_dialog_forder"));

		int returnval = jfc.showOpenDialog((Component) PluginServices
				.getMainFrame());

		if (returnval == JFileChooser.APPROVE_OPTION) {
			thefolder = jfc.getSelectedFile();
			log.debug("Open folder: " + thefolder.toString());
		} else {
			return null;
		}

		return thefolder;
	}

}

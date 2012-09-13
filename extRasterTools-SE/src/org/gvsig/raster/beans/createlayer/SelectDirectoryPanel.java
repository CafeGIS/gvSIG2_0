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
package org.gvsig.raster.beans.createlayer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.swing.JFileChooser;

import com.iver.andami.PluginServices;

/**
 * Panel para la selección de directorios.
 * 
 * 13/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class SelectDirectoryPanel extends JPanel implements ActionListener {
	private static final long  serialVersionUID     = 1L;
	private JTextField         directoryTextField   = null;
	private JLabel             jLabelDirectory      = null;
	private JButton            jBChooseDirectory    = null;
	
	/**
	 * Constructor. 
	 * Inicializa los componentes gráficos
	 */
	public SelectDirectoryPanel() {
		init();
	}
	
	/**
	 * This method initializes jPNameFile
	 * @return javax.swing.JPanel
	 */
	private void init() {
		GridBagConstraints gridBagConstraints = null;

		setLayout(new GridBagLayout());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(2, 5, 5, 2);
		add(getJLabelDirectory(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 5, 2);
		add(getDirectoryTextField(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 5, 5);
		add(getJBChooseDirectory(), gridBagConstraints);

		getJBChooseDirectory().addActionListener(this);
	}
	
	/**
	 * Obtiene el botón de asignación de directorio
	 * @return JButton
	 */
	private JButton getJBChooseDirectory() {
		if (jBChooseDirectory == null) {
			jBChooseDirectory = new JButton(PluginServices.getText(this, "cambiar_ruta"));
			jBChooseDirectory.setEnabled(false);
		}
		return jBChooseDirectory;
	}
	
	/**
	 * Obtiene la etiqueta con la ruta
	 * @return JLabel
	 */
	private JLabel getJLabelDirectory() {
		if (jLabelDirectory == null) {
			jLabelDirectory = new JLabel(PluginServices.getText(this, "ruta") + ":");
			jLabelDirectory.setEnabled(false);
		}
		return jLabelDirectory;
	}
	
	/**
	 * Obtiene el campo de texto con la ruta de directorio
	 * @return JTextField
	 */
	public JTextField getDirectoryTextField() {
		if (directoryTextField  == null) {
			directoryTextField = new JTextField();
			directoryTextField.setText(JFileChooser.getLastPath(this.getClass().getName(), (File) null).toString());
			directoryTextField.setEditable(false);
			directoryTextField.setEnabled(false);
			directoryTextField.setPreferredSize(new Dimension(200, directoryTextField.getPreferredSize().height));
		}
		return directoryTextField;
	}
	
	/**
	 * Accion que sucede cuando se pulsa el boton de cambiar directorio
	 */
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser(this.getClass().getName(), new File(getDirectoryTextField().getText()));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_directorio"));

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			getDirectoryTextField().setText(chooser.getSelectedFile().toString());
		else
			chooser.setLastPath(new File(getDirectoryTextField().getText()));
	}
	
	/**
	 * Obtiene la ruta al directorio.
	 * @return String
	 */
	public String getPath() {
		return getDirectoryTextField().getText();
	}
}

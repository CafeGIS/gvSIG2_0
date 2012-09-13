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
package org.gvsig.raster.gui.preferences.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.Configuration;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.util.BasePanel;

import com.iver.andami.PluginServices;
/**
 * PreferenceTemporal es una clase para la configuracion de los directorios
 * temporales de raster en gvSIG.
 * 
 * @version 12/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PreferenceTemporal extends BasePanel implements ActionListener {
	private static final long serialVersionUID   = 1L;
	private JLabel            labelTemporales    = null;
	private JButton           buttonOpen         = null;
	private JTextField        textFieldDirectory = null;

	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public PreferenceTemporal() {
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
		setBorder(BorderFactory.createTitledBorder(getText(this, "rutas")));
		getLabelTemporales().setText(getText(this, "temporales") + ":");
		getButtonOpen().setText(getText(this, "seleccionar_directorio"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		GridBagConstraints gridBagConstraints;

		setLayout(new GridBagLayout());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 5, 5, 2);
		add(getLabelTemporales(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 2, 5, 2);
		add(getTextFieldDirectory(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 2, 5, 5);
		add(getButtonOpen(), gridBagConstraints);
	}

	private JLabel getLabelTemporales() {
		if (labelTemporales == null) {
			labelTemporales = new JLabel();
		}
		return labelTemporales;
	}

	private JButton getButtonOpen() {
		if (buttonOpen == null) {
			buttonOpen = new JButton();
			buttonOpen.addActionListener(this);
		}
		return buttonOpen;
	}

	private JTextField getTextFieldDirectory() {
		if (textFieldDirectory == null) {
			textFieldDirectory = new JTextField();
			textFieldDirectory.setEditable(false);
			textFieldDirectory.setPreferredSize(new Dimension(0, textFieldDirectory.getPreferredSize().height));
		}
		return textFieldDirectory;
	}

	/**
	 * Carga los valores por defecto del componente
	 */
	public void initializeDefaults() {
		File file = new File((String) Configuration.getDefaultValue("path_temp_cache_directory"));
		if (!file.exists())
			file.mkdir();
		getTextFieldDirectory().setText(file.getAbsolutePath());
	}

	/**
	 * Carga los valores establecidos por el usuario en el componente
	 */
	public void initializeValues() {
		File file = new File(Configuration.getValue("path_temp_cache_directory", RasterLibrary.getTemporalPath()));
		if (!file.exists())
			file.mkdir();
		getTextFieldDirectory().setText(file.getAbsolutePath());
	}

	/**
	 * Guarda los valores establecidos por el usuario
	 */
	public void storeValues() {
		Configuration.setValue("path_temp_cache_directory", getTextFieldDirectory().getText());
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser(this.getClass().getName(), new File(getTextFieldDirectory().getText()));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_directorio"));

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			getTextFieldDirectory().setText(chooser.getSelectedFile().getAbsolutePath());

		JFileChooser.setLastPath(this.getClass().getName(), new File(getTextFieldDirectory().getText()));
	}
}
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
package org.gvsig.rastertools.clipping.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;

import com.iver.andami.PluginServices;
/**
 *
 * @version 25/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ClippingOptionsPanel extends JPanel implements PropertyListener, ItemListener, ActionListener {
	private static final long serialVersionUID = 7299254074235648334L;
	private JPanel     jPNameFile           = null;
	private JPanel     jPNameDirectory      = null;
	private JCheckBox  jCheckBox            = null;
	private JCheckBox  jCheckLoadLayerInToc = null;
	private JButton    jBChooseDirectory    = null;
	private JLabel     jLabelDirectory      = null;
	private JCheckBox  jCheckSaveFile       = null;
	private JTextField filenameTextField    = null;
	private JTextField directoryTextField   = null;

	public ClippingOptionsPanel() {
		initialize();
	}

	private void initialize() {
		GridBagConstraints gridBagConstraints = null;

		setLayout(new GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 5);
		add(getJPNameFile(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
		add(getCbOneLyrPerBand(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
		//Preguntamos al acabar para que no nos peten la aplicación
		//add(getCbLoadLayerInToc(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
		add(getCbSaveFile(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
		add(getJPNameDirectory(), gridBagConstraints);

		updateNewLayerText();
	}

	/**
	 * This method initializes jPNameFile
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPNameFile() {
		if (jPNameFile == null) {
			jPNameFile = new JPanel();
			jPNameFile.add(new JLabel(PluginServices.getText(this, "nombre_capas") + ":"));
			jPNameFile.add(getFilenameTextField());
		}
		return jPNameFile;
	}

	/**
	 * This method initializes jPNameFile
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPNameDirectory() {
		if (jPNameDirectory == null) {
			GridBagConstraints gridBagConstraints = null;
			jPNameDirectory = new JPanel();

			jPNameDirectory.setLayout(new GridBagLayout());

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(2, 5, 5, 2);
			jPNameDirectory.add(getJLabelDirectory(), gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 5, 2);
			jPNameDirectory.add(getDirectoryTextField(), gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 5, 5);
			jPNameDirectory.add(getJBChooseDirectory(), gridBagConstraints);

			getJBChooseDirectory().addActionListener(this);
		}
		return jPNameDirectory;
	}

	/**
	 * This method initializes jCheckLoadLayerInToc
	 * @return javax.swing.JCheckBox
	 */
	public JCheckBox getCbLoadLayerInToc() {
		if (jCheckLoadLayerInToc == null) {
			jCheckLoadLayerInToc = new JCheckBox();
			jCheckLoadLayerInToc.setText(PluginServices.getText(this, "cargar_en_toc"));
			jCheckLoadLayerInToc.setSelected(true);
			jCheckLoadLayerInToc.setEnabled(false);
			jCheckLoadLayerInToc.addItemListener(this);
		}
		return jCheckLoadLayerInToc;
	}

	/**
	 * This method initializes jCheckBox
	 * @return javax.swing.JCheckBox
	 */
	public JCheckBox getCbOneLyrPerBand() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
			jCheckBox.setText(PluginServices.getText(this, "crear_1_capa_por_banda"));
		}
		return jCheckBox;
	}

	/**
	 * Cada vez que cambia un checkbox de cargar en toc o guardar en fichero, nos
	 * aseguramos de que no pueden estar las dos opciones desmarcadas
	 */
	public void itemStateChanged(ItemEvent e) {
		getCbLoadLayerInToc().setEnabled(true);
		getCbSaveFile().setEnabled(true);
		if (getCbLoadLayerInToc().isSelected() && !getCbSaveFile().isSelected())
			getCbLoadLayerInToc().setEnabled(false);
		if (!getCbLoadLayerInToc().isSelected() && getCbSaveFile().isSelected())
			getCbSaveFile().setEnabled(false);

		getJBChooseDirectory().setEnabled(getCbSaveFile().isSelected());
		getDirectoryTextField().setEnabled(getCbSaveFile().isSelected());
		getJLabelDirectory().setEnabled(getCbSaveFile().isSelected());
	}

	private JButton getJBChooseDirectory() {
		if (jBChooseDirectory == null) {
			jBChooseDirectory = new JButton(PluginServices.getText(this, "cambiar_ruta"));
			jBChooseDirectory.setEnabled(false);
		}
		return jBChooseDirectory;
	}

	private JLabel getJLabelDirectory() {
		if (jLabelDirectory == null) {
			jLabelDirectory = new JLabel(PluginServices.getText(this, "ruta") + ":");
			jLabelDirectory.setEnabled(false);
		}
		return jLabelDirectory;
	}

	/**
	 * This method initializes jCheckSaveFile
	 *
	 * @return javax.swing.JCheckBox
	 */
	public JCheckBox getCbSaveFile() {
		if (jCheckSaveFile == null) {
			jCheckSaveFile = new JCheckBox();
			jCheckSaveFile.setText(PluginServices.getText(this, "guardar_en_disco"));
			jCheckSaveFile.addItemListener(this);
		}
		return jCheckSaveFile;
	}

	/**
	 * This method initializes filenameTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getFilenameTextField() {
		if (filenameTextField == null) {
			filenameTextField = new JTextField();
			filenameTextField.setPreferredSize(new Dimension(150, filenameTextField.getPreferredSize().height));
			RasterLibrary.addOnlyLayerNameListener(this);
		}
		return filenameTextField;
	}

	/**
	 * This method initializes filenameTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getDirectoryTextField() {
		if (directoryTextField == null) {
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
	 * Especificar el nombre de la nueva capa para el recuadro de texto asignándo
	 * en cada llamada un nombre consecutivo.
	 */
	public void updateNewLayerText() {
		filenameTextField.setText(RasterLibrary.getOnlyLayerName());
	}

	/**
	 * Cuando alguien ha cambiado la propiedad del nombre de la 
	 * capa se actualiza automáticamente
	 */
	public void actionValueChanged(PropertyEvent e) {
		updateNewLayerText();
	}
}

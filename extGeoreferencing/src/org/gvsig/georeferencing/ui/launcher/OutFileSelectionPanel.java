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
package org.gvsig.georeferencing.ui.launcher;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.util.RasterToolsUtil;


/**
 * Panel de selección de fichero de salida.
 *
 * 10/01/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class OutFileSelectionPanel extends JPanel implements ActionListener {
	private static final long    serialVersionUID    = 1L;
	private JTextField           fileName            = null;
	private JButton              bSelection          = null;

	/**
	 * Constructor. Asigna la lista de nombres de vistas para el selector.
	 * @param viewList
	 */
	public OutFileSelectionPanel() {
		init();
	}

	/**
	 * Acciones de inicialización del panel
	 */
	public void init() {
		BorderLayout fl = new BorderLayout();
		fl.setHgap(3);
		fl.setVgap(0);
		setLayout(fl);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "output_file"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		add(getFileName(), BorderLayout.CENTER);
		add(getSelectFileButton(), BorderLayout.EAST);
	}

	/**
	 * Obtiene el campo con la ruta al ficheo a georreferenciar
	 * @return JFormattedTextField
	 */
	private JTextField getFileName() {
		if (fileName == null) {
			String path = JFileChooser.getLastPath("OUT_FILE_SELECTION_PANEL_GEOREFERENCING", null).getAbsolutePath();
			if(path == null)
				path = System.getProperty("user.home");
			fileName = new JTextField(path + File.separator + RasterLibrary.usesOnlyLayerName() + ".tif");
			fileName.setEditable(true);
		}
		return fileName;
	}

	/**
	 * Obtiene el botón de selección de fichero
	 * @return JButton
		*/
	private JButton getSelectFileButton() {
		if(bSelection == null) {
			bSelection = new JButton(RasterToolsUtil.getText(this, "select"));
			bSelection.addActionListener(this);
		}
		return bSelection;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getSelectFileButton()) {
			selectDirectory();
		}
	}

	/**
	 * Muestra el dialogo de selección de fichero para la carga de la capa
	 * raster en los formatos definidos para georreferenciar.
	 * @return Capa raster cargada o null si no se consigue ninguna
	 */
	private void selectDirectory() {
		JFileChooser chooser = new JFileChooser("OUT_FILE_SELECTION_PANEL_GEOREFERENCING",JFileChooser.getLastPath("OUT_FILE_SELECTION_PANEL_GEOREFERENCING", null));
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.isDirectorySelectionEnabled();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String outPath = chooser.getSelectedFile().getAbsolutePath();
			String fileName = RasterLibrary.getOnlyLayerName() + ".tif";
			int index = getFileName().getText().lastIndexOf(File.separator);
			if(index != -1)
				fileName = getFileName().getText().substring(index + 1);
			if(outPath.compareTo(File.separator) == 0)
				getFileName().setText(outPath + fileName);
			else
				getFileName().setText(outPath + File.separator + fileName);
			JFileChooser.setLastPath("OUT_FILE_SELECTION_PANEL_GEOREFERENCING", chooser.getSelectedFile());
		}
		return;
	}

	//-------Consulta de propiedades seleccionadas---------

	/**
	 * Obtiene el fichero seleccionado
	 * @return String con el nombre del fichero seleccionado
	 */
	public String getOutFile() {
		return getFileName().getText();
	}

	/**
	 * Asigna el fichero de salida
	 * @param file
	 */
	public void setOutFile(String file) {
		getFileName().setText(file);
	}

}

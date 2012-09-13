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

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;


/**
 * Panel de selección de fichero a georreferenciar.
 *
 * 10/01/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class FileSelectionPanel extends JPanel implements ActionListener {
	private static final long     serialVersionUID    = 1L;

	private JTextField            fileName            = null;
	private JButton               bSelection          = null;
	private FLyrRasterSE          layer               = null;
	private DataInputContainer    yCellSize           = null;
	private DataInputContainer    xCellSize           = null;

	/**
	 * Constructor. Asigna la lista de nombres de vistas para el selector.
	 * @param viewList
	 */
	public FileSelectionPanel(DataInputContainer xCellSize, DataInputContainer yCellSize) {
		this.xCellSize = xCellSize;
		this.yCellSize = yCellSize;
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
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "georef_file"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		add(getFileName(), BorderLayout.CENTER);
		add(getSelectFileButton(), BorderLayout.EAST);
	}

	/**
	 * Obtiene el campo con la ruta al ficheo a georreferenciar
	 * @return JFormattedTextField
	 */
	private JTextField getFileName() {
		if (fileName == null) {
			fileName = new JTextField();
			fileName.setEditable(false);
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

	/**
	 * Gestiona el evento del botón de apertura de capa
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getSelectFileButton()) {
			loadRasterLayer();
			if(getLayer() != null && xCellSize != null && yCellSize != null) {
				xCellSize.setValue(getLayer().getDataSource().getCellSize() + "");
				yCellSize.setValue(getLayer().getDataSource().getCellSize() + "");
			}
		}
	}

	/**
	 * Muestra el dialogo de selección de fichero para la carga de la capa
	 * raster en los formatos definidos para georreferenciar.
	 * @return Capa raster cargada o null si no se consigue ninguna
	 */
	private FLyrRasterSE loadRasterLayer() {
		String path = null;
		JFileChooser chooser = new JFileChooser("FILE_SELECTION_PANEL_GEOREFERENCING",JFileChooser.getLastPath("FILE_SELECTION_PANEL_GEOREFERENCING", null));
		chooser.setAcceptAllFileFilterUsed(false);
		String[] extensionsSupported = RasterDataset.getExtensionsSupported();
		ExtendedFileFilter allFiles = new ExtendedFileFilter();
		for (int i = 0; i < extensionsSupported.length; i++) {
			ExtendedFileFilter fileFilter = new ExtendedFileFilter();
			fileFilter.addExtension((String) extensionsSupported[i]);
			allFiles.addExtension((String) extensionsSupported[i]);
			chooser.addChoosableFileFilter(fileFilter);
		}
		allFiles.setDescription(RasterToolsUtil.getText(this, "todos_soportados"));
		chooser.addChoosableFileFilter(allFiles);
		if(chooser.getSelectedFile() != null)
			JFileChooser.setLastPath("FILE_SELECTION_PANEL_GEOREFERENCING", chooser.getSelectedFile());
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			path = chooser.getSelectedFile().getAbsolutePath();
			try {
				if (layer != null)
					layer.getDataSource().close();
				layer = FLyrRasterSE.createLayer(RasterUtilities.getLastPart(path, File.separator), path, null);
				if (layer != null)
					getFileName().setText(path);
				return layer;
			} catch (LoadLayerException e) {
			}
		}
		RasterToolsUtil.messageBoxError(RasterToolsUtil.getText(this, "error_load_layer"), null);
		return null;
	}

	//-------Consulta de propiedades seleccionadas---------

	/**
	 * Obtiene la capa que ha sido abierta por el usuario
	 * @return Obtiene la capa que ha sido abierta por el usuario o null si no
	 * hay abierta ninguna.
	 */
	public FLyrRasterSE getLayer() {
		return layer;
	}

}

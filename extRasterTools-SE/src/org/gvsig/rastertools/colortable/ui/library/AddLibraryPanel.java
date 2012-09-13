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
package org.gvsig.rastertools.colortable.ui.library;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.raster.beans.stretchselector.StretchSelectorPanel;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Ventana que contiene el panel de añadir librería de tablas de color.
 * 
 * 06/08/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class AddLibraryPanel extends BasePanel {
	private static final long serialVersionUID  = 1L;
	private StretchSelectorPanel  stretchListPanel  = null;
	private JPanel            libraryName       = null;
	private JTextField        jTextName         = null;
	private JLabel            jLabelName        = null;
	
	/**
	 * Constructor. Llama al inicializador de componentes gráficos
	 */
	public AddLibraryPanel() {
		init();
		translate();
	}
	
	/**
	 * Inicializa los componentes gráficos
	 */
	protected void init() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
	    setLayout(new GridBagLayout());
	    gridBagConstraints.weightx = 1;
	    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
	    add(getLibraryNamePanel(), gridBagConstraints);
	    gridBagConstraints.gridy = 1;
	    add(getStretchListPanel(), gridBagConstraints);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
	}
	
	/**
	 * Obtiene el componente para introducir tramos
	 * @return StretchListPanel
	 */
	public StretchSelectorPanel getStretchListPanel() {
		if(stretchListPanel == null)
			stretchListPanel = new StretchSelectorPanel();
		return stretchListPanel;
	}
	
	/**
	 * Obtiene el panel con el nombre de la librería.
	 * @return JPanel
	 */
	private JPanel getLibraryNamePanel() {
		if(libraryName == null) {
			libraryName = new JPanel();
			libraryName.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(5, 5, 2, 2);
			libraryName.add(getLabelName(), gridBagConstraints);

			gridBagConstraints.weightx = 1;
			gridBagConstraints.insets = new Insets(5, 2, 2, 5);
			libraryName.add(getLibraryName(), gridBagConstraints);
		}
		return libraryName;
	}
	
	/**
	 * Obtiene la etiqueta con el nombre de librería
	 * @return JLabel
	 */
	private JLabel getLabelName() {
		if(jLabelName == null) 
			jLabelName = new JLabel(RasterToolsUtil.getText(this, "nombre") + ":");
		return jLabelName;
	}
	
	/**
	 * Obtiene el nombre de la librería
	 * @return JTextField
	 */
	public JTextField getLibraryName() {
		if(jTextName == null)
			jTextName = new JTextField(RasterToolsUtil.getText(this, "nueva_libreria"));
		return jTextName;
	}
}

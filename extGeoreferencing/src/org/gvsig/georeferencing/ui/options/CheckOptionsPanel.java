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
package org.gvsig.georeferencing.ui.options;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Panel de selección de tipo de georreferenciación.
 * 
 * 10/01/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class CheckOptionsPanel extends JPanel {
	private static final long     serialVersionUID    = 1L;
	
	private DataInputContainer    threshold           = null;
	private JCheckBox             showNumber          = null;
	private JCheckBox             addErrorInCSV       = null;
	private JCheckBox             centerPoint         = null;
	private ColorSelector         backgroundColorSel  = null;
	private ColorSelector         textColorSel        = null;
		
	/**
	 * Constructor. Asigna la lista de nombres de vistas para el selector. 
	 * @param viewList
	 */
	public CheckOptionsPanel() {
		init();
	}
	
	/**
	 * Acciones de inicialización del panel
	 */
	public void init() {	    
		GridBagLayout gl = new GridBagLayout();
		setLayout(gl);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "options"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 5, 0);
		add(getBackGroundColorSelector(), gbc);

		gbc.gridy = 1;
		add(getTextSelector(), gbc);
		
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridy = 2;
		add(getShowNumberCheck(), gbc);
		
		gbc.gridy = 3;
		add(getAddErrorsCSVCheck(), gbc);
		
		gbc.gridy = 4;
		add(getCenterViewCheck(), gbc);
		
		gbc.gridy = 5;
		add(getThresholdError(), gbc);
	}
		
	/**
	 * Obtiene el control para selección de umbral de error
	 * @return JButton
	 */
	public DataInputContainer getThresholdError() {
		if(threshold == null) {
			threshold = new DataInputContainer();
			threshold.setLabelText(RasterToolsUtil.getText(this, "umbral_error"));
		}
		return threshold;
	}
	
	/**
	 * Obtiene el selector de color para el fondo
	 * @return JButton
	 */
	public ColorSelector getBackGroundColorSelector() {
		if(backgroundColorSel == null) {
			backgroundColorSel = new ColorSelector(Color.BLACK, RasterToolsUtil.getText(this, "background_color"));
		}
		return backgroundColorSel;
	}
	
	/**
	 * Obtiene el selector de color para el texto
	 * @return JButton
	 */
	public ColorSelector getTextSelector() {
		if(textColorSel == null) {
			textColorSel = new ColorSelector(Color.RED, RasterToolsUtil.getText(this, "text_color"));
		}
		return textColorSel;
	}
	
	/**
	 * Obtiene el check de mostrar numeración
	 * @return JCheckBox
	 */
	public JCheckBox getShowNumberCheck() {
		if(showNumber == null) {
			showNumber = new JCheckBox(RasterToolsUtil.getText(this, "show_number"));
		}
		return showNumber;
	}
	
	/**
	 * Obtiene el check de añadir errores al fichero CSV o no
	 * @return JCheckBox
	 */
	public JCheckBox getAddErrorsCSVCheck() {
		if(addErrorInCSV == null) {
			addErrorInCSV = new JCheckBox(RasterToolsUtil.getText(this, "add_errors_csv"));
		}
		return addErrorInCSV;
	}
	
	/**
	 * Obtiene el check de centrar la vista sobre el punto seleccionado
	 * @return JCheckBox
	 */
	public JCheckBox getCenterViewCheck() {
		if(centerPoint == null) {
			centerPoint = new JCheckBox(RasterToolsUtil.getText(this, "center_view"));
		}
		return centerPoint;
	}
	
}


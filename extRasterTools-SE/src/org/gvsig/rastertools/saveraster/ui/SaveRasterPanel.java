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
package org.gvsig.rastertools.saveraster.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.cresques.cts.IProjection;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.rastertools.saveraster.ui.panels.SelectFilePanel;
import org.gvsig.rastertools.saveraster.ui.panels.SelectionParamsPanel;
import org.gvsig.rastertools.saveraster.ui.panels.WCCoordsInputPanel;

/**
 * Panel General de salvar a raster.
 * 
 * 05/10/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class SaveRasterPanel extends JPanel {
	private static final long       serialVersionUID = 1L;
	private WCCoordsInputPanel      wcCoordsPanel = null;
	private SelectionParamsPanel    paramsPanel = null;
	private SelectFilePanel         filePanel = null;
	private IProjection				proj = null;
	  
	/**
	 * This is the default constructor
	 */
	public SaveRasterPanel() {
		super();
		initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
				
		setLayout(new GridBagLayout());
		
		gridBagConstraints.gridy = 0;
		add(getWCCoordsPanel(), gridBagConstraints);
		
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 1;
		add(getSelectionParamsPanel(), gridBagConstraints);
		
		gridBagConstraints.gridy = 2;
		add(getSelectFilePanel(), gridBagConstraints);
	}
	
	/**
	 * Obtiene el panel de selección de coordenadas
	 * @return WCCoordsInputPanel
	 */
	public WCCoordsInputPanel getWCCoordsPanel() {
		if(wcCoordsPanel == null)
			wcCoordsPanel = new WCCoordsInputPanel();
		return wcCoordsPanel;
	}
	
	/**
	 * Obtiene el panel de selección de parámetros
	 * @return SelectionParamsPanel
	 */
	public SelectionParamsPanel getSelectionParamsPanel() {
		if(paramsPanel == null)
			paramsPanel = new SelectionParamsPanel();
		return paramsPanel;
	}
	
	/**
	 * Obtiene el panel de selección de ficheros
	 * @return SelectFilePanel
	 */
	public SelectFilePanel getSelectFilePanel() {
		if(filePanel == null)
			filePanel = new SelectFilePanel();
		return filePanel;
	}
	
//-------------------------------------------
	/**
	 * Obtiene la etiqueta del nombre de fichero seleccionado
	 * return JLabel
	 */
	public JLabel getLFileName() {
		return getSelectFilePanel().getLabelText();
	}
	
	/**
	 * Obtiene el botón de propiedades 
	 * @return JButton
	 */
	public JButton getBProperties() {
		return getSelectFilePanel().getBProperties();
	}
	
	/**
	 * Obtiene la entrada de texto correspondiente a la X de la esquina superior izquierda
	 * @return DataInputContainer
	 */
	public DataInputContainer getTSupIzqX() {
		return getWCCoordsPanel().getCoordinatesPanel().getDataInputContainer11();
	}
	
	/**
	 * Obtiene la entrada de texto correspondiente a la Y de la esquina superior izquierda
	 * @return DataInputContainer
	 */
	public DataInputContainer getTSupIzqY() {
		return getWCCoordsPanel().getCoordinatesPanel().getDataInputContainer12();
	}
	
	/**
	 * Obtiene la entrada de texto correspondiente a la X de la esquina inferior derecha
	 * @return DataInputContainer
	 */
	public DataInputContainer getTInfDerX() {
		return getWCCoordsPanel().getCoordinatesPanel().getDataInputContainer21();
	}
	
	/**
	 * Obtiene la entrada de texto correspondiente a la Y de la esquina inferior derecha
	 * @return DataInputContainer
	 */
	public DataInputContainer getTInfDerY() {
		return getWCCoordsPanel().getCoordinatesPanel().getDataInputContainer22();
	}
	
	/**
	 * Obtiene el botón de selección de fichero 
	 * @return JButton
	 */
	public JButton getBSelect() {
		return getSelectFilePanel().getBSelect();
	}
		
	/**
	 * Obtiene la entrada de texto correspondiente al ancho
	 * @return DataInputContainer
	 */
	public DataInputContainer getTWidth() {
		return getSelectionParamsPanel().getInputSizePanel().getTWidth();
	}
	
	/**
	 * Obtiene la entrada de texto correspondiente al alto
	 * @return DataInputContainer
	 */
	public DataInputContainer getTHeight() {
		return getSelectionParamsPanel().getInputSizePanel().getTHeight();
	}
	
	public JComboBox getCbMeasureType() {
		return getSelectionParamsPanel().getInputSizePanel().getCbMeasureType();
	}
	
	/**
	 * Obtiene la entrada de texto correspondiente a la escala
	 * @return DataInputContainer
	 */
	public DataInputContainer getTScale() {
		return getSelectionParamsPanel().getInputScalePanel().getTScale();
	}
	
	/**
	 * Obtiene el combo que informa de la resolución seleccionada
	 * @return JComboBox
	 */
	public JComboBox getCbResolution() {
		return getSelectionParamsPanel().getInputScalePanel().getCbResolution();
	}
	
	/**
	 * Obtiene la entrada de texto correspondiente a los metros por pixel
	 * @return DataInputContainer
	 */
	public DataInputContainer getTMtsPixel() {
		return getSelectionParamsPanel().getInputScalePanel().getTMtsPixel();
	}
	
	/**
	 * Obtiene el RadioButton correspondiente a la escala
	 * @return JRadioButton
	 */
	public JRadioButton getRbScale() {
		return getSelectionParamsPanel().getSelectorPanel().getRbScale();
	}
	
	/**
	 * Obtiene el RadioButton correspondiente a los metros por pixel
	 * @return JRadioButton
	 */
	public JRadioButton getRbMtsPixel() {
		return getSelectionParamsPanel().getSelectorPanel().getRbMtsPixel();
	}
	
	/**
	 * Obtiene el RadioButton correspondiente al tamaño
	 * @return JRadioButton
	 */
	public JRadioButton getRbSize() {
		return getSelectionParamsPanel().getSelectorPanel().getRbSize();
	}
	
	/**
	 * Asigna la proyección 
	 * @param proj Interfaz IProjection
	 */
	public void setProjection(IProjection proj) {
		this.proj = proj;
	}
	
	/**
	 * Obtiene la proyección
	 * @return Interfaz IProjection
	 */
	public IProjection getProjection() {
		return proj;
	}
}

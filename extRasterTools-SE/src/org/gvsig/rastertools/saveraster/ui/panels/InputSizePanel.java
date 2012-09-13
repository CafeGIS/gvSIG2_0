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
package org.gvsig.rastertools.saveraster.ui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.gvsig.gui.beans.datainput.DataInputContainer;

import com.iver.andami.PluginServices;

/**
 * Panel de entrada de datos de ancho y alto.
 * @version 18/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class InputSizePanel extends JPanel {
	private static final long  serialVersionUID = 1L;
	private DataInputContainer inputWidth = null;
	private DataInputContainer inputHeight = null;
	private JComboBox          cbMeasureType = null;

	/**
	 * This is the default constructor
	 */
	public InputSizePanel() {
		super();
		initialize();
		this.setActive(false);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		
		add(getTWidth(), gridBagConstraints);
		
		gridBagConstraints.gridx = 1;
		add(getTHeight(), gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		add(getCbMeasureType(), gridBagConstraints);
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public DataInputContainer getTWidth() {
		if (inputWidth == null) {
			inputWidth = new DataInputContainer();
			inputWidth.setLabelText(PluginServices.getText(this, "ancho"));
		}
		return inputWidth;
	}

	/**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public DataInputContainer getTHeight() {
		if (inputHeight == null) {
			inputHeight = new DataInputContainer();
			inputHeight.setLabelText(PluginServices.getText(this, "alto"));
		}
		return inputHeight;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getCbMeasureType() {
		if (cbMeasureType == null) {
			cbMeasureType = new JComboBox();
			cbMeasureType.addItem("Pixels");
			cbMeasureType.addItem("Cms");
			cbMeasureType.addItem("Mms");
			cbMeasureType.addItem("Mts");
			cbMeasureType.addItem("Inches");
		}
		return cbMeasureType;
	}

	/**
	 * Activa o desactiva las cajas de texto con los datos
	 * ancho y alto.
	 * @param active true activa los JTextBox y false los desactiva
	 */
	public void setActive(boolean active){		
		this.getTWidth().setControlEnabled(active);
		this.getTHeight().setControlEnabled(active);
		this.getCbMeasureType().setEnabled(active);
	}
}

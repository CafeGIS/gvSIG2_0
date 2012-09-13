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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.gui.beans.datainput.DataInputContainer;

import com.iver.andami.PluginServices;

/**
 * Entrada de datos de Escala y tamaño por pixel.
 * 
 * @version 18/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class InputScaleDataPanel extends JPanel {
	private static final long    serialVersionUID = 1L;
	private JPanel               pResolution = null;
	private JLabel               lResolution = null;
	private DataInputContainer   inputMtsPixel = null;
	private DataInputContainer   inputScale = null;
	private JComboBox            cResolution = null;
	
	/**
	 * This is the default constructor
	 */
	public InputScaleDataPanel() {
		super();
		initialize();
		setActiveScale(0);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(5, 0, 0, 0);
		gridBagConstraints.weightx = 1.0;
		//gridBagConstraints.weighty = 1.0;		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		this.add(getTScale(), gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		this.add(getPResolution(), gridBagConstraints);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		this.add(getTMtsPixel(), gridBagConstraints);
	}

	/**
	 * Obtiene la entrada de datos de metros por pixel
	 * @return DataInputContainer
	 */
	public DataInputContainer getTMtsPixel() {
		if (inputMtsPixel == null) {
			inputMtsPixel = new DataInputContainer();
			inputMtsPixel.setLabelText(PluginServices.getText(this, "mtspixel"));
		}
		return inputMtsPixel;
	}
	
	/**
	 * Obtiene la entrada de datos de escala
	 * @return DataInputContainer
	 */
	public DataInputContainer getTScale() {
		if (inputScale == null) {
			inputScale = new DataInputContainer();
			inputScale.setLabelText(PluginServices.getText(this, "escale"));
		}
		return inputScale;
	}
	
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPResolution() {
		if (pResolution == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.RIGHT);
			flowLayout1.setVgap(0);
			flowLayout1.setHgap(0);
			lResolution = new JLabel();
			lResolution.setText(PluginServices.getText(this, "resolucion_espacial") + ":");
			pResolution = new JPanel();
			pResolution.setLayout(flowLayout1);
			pResolution.add(lResolution, null);
			pResolution.add(getCbResolution(), null);
		}
		return pResolution;
	}

	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	public JComboBox getCbResolution() {
		if (cResolution == null) {
			cResolution = new JComboBox();
			cResolution.addItem(75 + "");
			cResolution.addItem(150 + "");
			cResolution.addItem(300 + "");
			cResolution.addItem(600 + "");
		}
		return cResolution;
	}

	/**
	 * Pone como activo el input scale o mts por pixel dependiendo del
	 * valor del parámetro. 
	 * @param active Si vale 0 activa la Escala y desactiva mts/pixel.
	 * 	Si vale 1 activa mts/pixel y desactiva la Escala.
	 * 	Si vale 2 desactiva ambos.
	 */
	public void setActiveScale(int active){
		this.getCbResolution().setEnabled(true);
		
		if(active == 0){
			getTScale().setControlEnabled(true);
			getTMtsPixel().setControlEnabled(false);
		}else if(active == 1){
			getTScale().setControlEnabled(false);
			getTMtsPixel().setControlEnabled(true);
		}else if(active == 2){
			getTScale().setControlEnabled(false);
			getTMtsPixel().setControlEnabled(false);
		}
	}

	/**
	 * Obtiene la etiqueta de la Resolución
	 * @return
	 */
	public JLabel getLResolution() {
		return lResolution;
	}	
}

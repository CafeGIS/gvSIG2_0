/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.gui.beans.labelslidertext;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.gui.beans.slidertext.SliderTextContainer;
/**
 * Añade una etiqueta al componente Slider ajustando el tamaño del componente
 * a la longitud de la etiqueta. Al redimensionar el componente varia el tamaño
 * del slider
 * 
 * @author Nacho Brodin(nachobrodin@gmail.com) 
 */
public class LabelSliderTextContainer extends SliderTextContainer {
	private static final long serialVersionUID = -4617272063786945078L;
	public JLabel lName = null;
	private JPanel pLabel = null;

	/**
	 * Constructor	
	 * @param min Valor mínimo del slider
	 * @param max Valor máximo del slider
	 * @param defaultPos Posición inicial
	 * @param up Si es true el texto se coloca sobre el componente y si es false a la izquierda 
	 * @param txt Texto de la etiqueta
	 */
	public LabelSliderTextContainer(int min, int max, int defaultPos, boolean up, String txt){
		super(min, max, defaultPos);
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.insets = new Insets(0,0,0,0);
		if(!up)
			super.add(getPLabel(up), BorderLayout.WEST);
		else
			super.add(getPLabel(up), BorderLayout.NORTH);
		getLName().setText(txt);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPLabel(boolean up) {
		if (pLabel == null) {
			pLabel = new JPanel();
			if(!up){
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.insets = new java.awt.Insets(0, 10, 8, 0);
				pLabel.setLayout(new GridBagLayout());
				pLabel.add(getLName(), gridBagConstraints1);
			}else{
				FlowLayout fl = new FlowLayout();
				fl.setAlignment(FlowLayout.LEFT);
				pLabel.setLayout(fl);
				pLabel.add(getLName(), null);
			}

		}
		return pLabel;
	}

	/**
	 * This method initializes JLabel
	 * @return
	 */
	public JLabel getLName(){
		if(lName == null)
			lName = new JLabel();
		return lName;
	}

	/**
	 * Asigna el texto de la etiqueta
	 * @param Texto de la etiqueta
	 */
	public void setName(String name){
		lName.setText(name);
	}

	/**
	 * Activa o desactiva el control
	 */
	public void setControlEnabled(boolean active){
		this.lName.setEnabled(active);
		super.setControlEnabled(active);
	}
}
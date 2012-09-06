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
package org.gvsig.gui.beans.checkslidertext;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.gui.beans.slidertext.SliderTextContainer;
/**
 * Añade un check al componente Slider ajustando el tamaño del componente
 * a la longitud del check. Al redimensionar el componente varia el tamaño
 * del slider
 * 
 * @author Nacho Brodin(nachobrodin@gmail.com) 
 */
public class CheckSliderTextContainer extends SliderTextContainer implements ActionListener{
	private static final long serialVersionUID  = -542842897847364459L;
	public JCheckBox          check             = null;
	private JPanel            pLabel            = null;
	private String            text              = null;
	private boolean           showCheck         = true;
	private JLabel            labelWithoutCheck = null;

	/**
	 * Constructor	
	 * @param min Valor mínimo del slider
	 * @param max Valor máximo del slider
	 * @param defaultPos Posición inicial
	 * @param up Si es true el texto se coloca sobre el componente y si es false a la izquierda 
	 * @param txt Texto de la etiqueta del check
	 * @param active Valor por defecto del control.True activo y false desactivo
	 * @deprecated Es recomendable el uso del constructor con el parámetro border. Tiene la misma funcionalidad 
	 * si se pone ese parámetro a true. Se mantiene este constructor por compatibilidad con versiones 
	 * anteriores.
	 */
	public CheckSliderTextContainer(int min, int max, int defaultPos, boolean up, String txt, boolean active){
		this(min, max, defaultPos, up, txt, active, false, true);
	}
	
	/**
	 * Constructor	
	 * @param min Valor mínimo del slider
	 * @param max Valor máximo del slider
	 * @param defaultPos Posición inicial
	 * @param up Si es true el texto se coloca sobre el componente y si es false a la izquierda 
	 * @param txt Texto de la etiqueta del check
	 * @param active Valor por defecto del control.True activo y false desactivo
	 * @param border True para mostrar el borde al control y false para ocultarlo
	 * @param showCheck True para mostrar el checkbox de activación y desactivación. False para ocultarlo
	 */
	public CheckSliderTextContainer(int min, int max, int defaultPos, boolean up, String txt, boolean active, boolean border, boolean showCheck) {
		super(min, max, defaultPos, border);
		this.showCheck = showCheck;
		text = txt;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.insets = new Insets(0,0,0,0);
		if(!up)
			super.add(getPCheck(up), BorderLayout.WEST);
		else
			super.add(getPCheck(up), BorderLayout.NORTH);
		if(showCheck) {
			check.addActionListener(this);
			check.setSelected(active);
		}
		setControlEnabled(active);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPCheck(boolean up) {
		if (pLabel == null) {
			pLabel = new JPanel();
			if (!up) {
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				pLabel.setLayout(new GridBagLayout());
				if(showCheck) {
					gridBagConstraints1.insets = new java.awt.Insets(0, 10, 13, 0);
					pLabel.add(getCheck(), gridBagConstraints1);
				} else {
					gridBagConstraints1.insets = new java.awt.Insets(0, 0, 13, 0);
					labelWithoutCheck = new JLabel(text); 
					pLabel.add(labelWithoutCheck, gridBagConstraints1);
				}
			} else {
				FlowLayout fl = new FlowLayout();
				fl.setAlignment(FlowLayout.LEFT);
				pLabel.setLayout(fl);
				if(showCheck)
					pLabel.add(getCheck(), null);
				else {
					labelWithoutCheck = new JLabel(text); 
					pLabel.add(labelWithoutCheck, null);
				}
			}
		}
		return pLabel;
	}

	/**
	 * This method initializes JLabel
	 * @return
	 */
	public JCheckBox getCheck() {
		if(check == null)
			check = new JCheckBox(text);
		return check;
	}

	/**
	 * Activa o desactiva el control del panel
	 * @param active
	 */
	public void setControlEnabled(boolean active) {
		if(showCheck)
			getCheck().setSelected(active);
		if(labelWithoutCheck != null)
			labelWithoutCheck.setEnabled(active);
		super.setControlEnabled(active);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == check) {
			setControlEnabled(check.isSelected());
			callChangeValue(true);
		}
	}
}
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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.gui.beans.slidertext.ColorSliderTextContainer;
/**
 * Añade un check al componente Slider ajustando el tamaño del componente a la
 * longitud del check. Al redimensionar el componente varia el tamaño del slider
 *
 * @version 08/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class CheckColorSliderTextContainer extends ColorSliderTextContainer implements ActionListener {
	private static final long	serialVersionUID	= 7047604405777061995L;
	public JCheckBox check = null;
	private JPanel pLabel = null;
	private String	text = null;

	/**
	 * Constructor
	 * @param min Valor mínimo del slider
	 * @param max Valor máximo del slider
	 * @param defaultPos Posición inicial
	 * @param txt Texto de la etiqueta del check
	 * @param active Valor por defecto del control.True activo y false desactivo
	 */
	public CheckColorSliderTextContainer(int min, int max, int defaultPos, String txt, boolean active) {
		super(min, max, defaultPos);
		text = txt;
		add(getPCheck(), BorderLayout.WEST);
		check.addActionListener(this);
		check.setSelected(active);
		setControlEnabled(active);

		this.setMaximumSize(new Dimension(32767, 31));
		this.setMinimumSize(new Dimension(250, 31));
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPCheck() {
		if (pLabel == null) {
			pLabel = new JPanel();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(0, 0, 13, 0);
			pLabel.setLayout(new GridBagLayout());
			pLabel.add(getCheck(), gridBagConstraints1);
			pLabel.add(new JLabel(text), gridBagConstraints1);
		}
		return pLabel;
	}

	/**
	 * This method initializes JLabel
	 * @return
	 */
	private JCheckBox getCheck() {
		if (check == null)
			check = new JCheckBox();
		return check;
	}

	/**
	 * Activa o desactiva el control del panel
	 * @param active
	 */
	public void setChecked(boolean active) {
		getCheck().setSelected(active);
		super.setControlEnabled(active);
	}

	/**
	 * Hace visible el checkbox o no del componente
	 * @param value
	 */
	public void setCheckboxVisible(boolean value) {
		if (!value)
			setChecked(true);
		getCheck().setVisible(value);
	}

	/**
	 * Devuelve si el control esta activo
	 * @param active
	 */
	public boolean isChecked() {
		return getCheck().isSelected();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == check) {
			setControlEnabled(check.isSelected());
			callChangeValue();
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		getCheck().setEnabled(enabled);

		if (!getCheck().isSelected())
			enabled = false;

		getSlider().setEnabled(enabled);
		getJSpinner().setEnabled(enabled);
	}
}
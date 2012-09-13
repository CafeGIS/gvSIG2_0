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
package org.gvsig.rastertools.properties.panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.iver.andami.PluginServices;
/**
 * Panel con el slider para regular la opacidad de la capa.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TranspOpacitySliderPanel extends JPanel implements ActionListener, FocusListener, ChangeListener{
	final private static long serialVersionUID = 0;
	private JCheckBox         cbTransparencia  = null;
	private JCheckBox         cbOpacidad       = null;
	private JSlider           slOpacidad       = null;
	private JTextField        tOpacidad        = null;

	/**
	 * Constructor.
	 */
	public TranspOpacitySliderPanel() {
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "opacidad"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		setLayout(new FlowLayout());
		add(getOpacityCheck(), null);
		add(getOpacitySlider(), null);
		add(getOpacityText(), null);
	}

	/**
	 * Inicializa controles a sus valores por defecto
	 */
	public void initControls() {
		this.getOpacityText().setText("100");
		this.setActiveOpacityControl(false);
	}

	/**
	 * This method initializes jCheckBox
	 * @return javax.swing.JCheckBox
	 */
	public JCheckBox getTransparencyCheck() {
		if (cbTransparencia == null) {
			cbTransparencia = new JCheckBox();
			cbTransparencia.setText(PluginServices.getText(this, "activar"));
			cbTransparencia.addActionListener(this);
		}

		return cbTransparencia;
	}

	/**
	 * This method initializes jCheckBox1
	 * @return javax.swing.JCheckBox
	 */
	public JCheckBox getOpacityCheck() {
		if (cbOpacidad == null) {
			cbOpacidad = new JCheckBox();
			cbOpacidad.setText(PluginServices.getText(this, "activar"));
			cbOpacidad.addActionListener(this);
			cbOpacidad.addFocusListener(this);
		}

		return cbOpacidad;
	}

	/**
	 * This method initializes jSlider
	 * @return javax.swing.JSlider
	 */
	public JSlider getOpacitySlider() {
		if (slOpacidad == null) {
			slOpacidad = new JSlider();
//			 slOpacidad.setPreferredSize(new java.awt.Dimension(wSlider, 16));
			slOpacidad.addChangeListener(this);
		}

		return slOpacidad;
	}

	/**
	 * This method initializes jTextField3
	 * @return javax.swing.JTextField
	 */
	public JTextField getOpacityText() {
		if (tOpacidad == null) {
			tOpacidad = new JTextField();
			tOpacidad.setPreferredSize(new java.awt.Dimension(30, 19));
			tOpacidad.addActionListener(this);
			tOpacidad.addFocusListener(this);
		}

		return tOpacidad;
	}

	/**
	 * Activa/Desactiva los controles de opacidad
	 * @param active
	 */
	public void setActiveOpacityControl(boolean active) {
		this.getOpacityCheck().setSelected(active);
		this.getOpacitySlider().setEnabled(active);
		this.getOpacityText().setEnabled(active);
	}

	/**
	 * Asigna el valor de opacidad a los controles de la ventana para que cuando
	 * esta se abra tenga los valores seleccionados en la imagen.
	 * @param alpha
	 */
	public void setOpacity(int alpha) {
		int opacityPercent = (int) ((alpha * 100) / 255);
		this.getOpacityText().setText(String.valueOf(opacityPercent));
		this.getOpacitySlider().setValue(opacityPercent);
		this.setActiveOpacityControl(true);

		if (opacityPercent == 100)
			this.setActiveOpacityControl(false);
	}

	/**
	 * Eventos sobre TextField y CheckBox. Controla eventos de checkbox de
	 * opacidad, transparencia, recorte de colas y los textfield de opacidad,
	 * valores de transparencia por banda y porcentaje de recorte.
	 */
	public void actionPerformed(ActionEvent e) {
		// Evento sobre el checkbox de opacidad
		if (e.getSource().equals(getOpacityCheck())) {
			// Check de opacidad activado -> Activar controles de opacidad
			if (getOpacityCheck().isSelected()) {
				getOpacitySlider().setEnabled(true);
				getOpacityText().setEnabled(true);
			} else {
				getOpacitySlider().setEnabled(false);
				getOpacityText().setEnabled(false);
			}
		}

		//Evento sobre el textfield de opacidad
		if (e.getSource().equals(getOpacityText()))
			checkOpacityText();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e) {
//	pTrans.updateTextBox();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e) {
		checkOpacityText();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		// Ponemos el valor del texto de la opacidad de pendiendo de la posición del
		// Slider
		if (e.getSource().equals(getOpacitySlider())) {
			getOpacityText().setText(String.valueOf(getOpacitySlider().getValue()));
		}
	}

	/**
	 * Controla que si el formato introducido en el textfield de opacidad es
	 * numerico se actualiza la posición del slider. Si es numerico pero excede
	 * del rango 0-100 pone el valor a 0 o a 100 dependiendo de si excede por
	 * arriba o por abajo
	 */
	private void checkOpacityText() {
		String op = getOpacityText().getText();
		int value = 0;

		try {
			if (op == null)
				throw new NumberFormatException();

			value = Integer.parseInt(op);

			if (value > 100)
				getOpacityText().setText("100");
			else
				if (value < 0)
					getOpacityText().setText("0");

			getOpacitySlider().setValue(value);
		} catch (NumberFormatException exc) {
			getOpacityText().setText("100");
			getOpacitySlider().setValue(100);
		}
	}
}
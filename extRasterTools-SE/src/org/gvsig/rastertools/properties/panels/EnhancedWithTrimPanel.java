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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.gvsig.gui.beans.slidertext.SliderTextContainer;

import com.iver.andami.PluginServices;
/**
 * Panel para los controles de brillo y contraste.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EnhancedWithTrimPanel extends JPanel implements ActionListener {
	private static final long   serialVersionUID = 9023137365069951866L;

	private JCheckBox           active           = null;
	private SliderTextContainer trimSlider       = null;
	private JCheckBox           removeCheck      = null;
	private JCheckBox           trimCheck        = null;
	
	/**
	 * Contructor
	 */
	public EnhancedWithTrimPanel() {
		super();
		init();
	}

	private void init() {
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "realce"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 10, 0);
		add(getActive(), gbc);
		gbc.gridy = 1;
		add(getRemoveCheck(), gbc);		
		gbc.gridy = 2;
		add(getTrimCheck(), gbc);		
		gbc.gridy = 3;
		add(getTrimSlider(), gbc);		
		getActive().addActionListener(this);
		getTrimCheck().addActionListener(this);
	}
	
	/**
	 * Obtiene el check de activar
	 * @return
	 */
	public JCheckBox getActive() {
		if (active == null) {
			active = new JCheckBox(PluginServices.getText(this, "activar"));
			active.setSelected(false);
			this.setControlEnabled(false);
		}
		return active;
	}
	
	/**
	 * Obtiene el check de eliminar extremos
	 * @return
	 */
	public JCheckBox getRemoveCheck() {
		if (removeCheck == null) {
			removeCheck = new JCheckBox(PluginServices.getText(this, "eliminar_extremos"));
		}
		return removeCheck;
	}
	
	/**
	 * Obtiene el check de recorte de colas
	 * @return
	 */
	public JCheckBox getTrimCheck() {
		if (trimCheck == null) {
			trimCheck = new JCheckBox(PluginServices.getText(this, "recorte_de_colas"));
		}
		return trimCheck;
	}
	
	/**
	 * Obtiene el control con el slider de recorte de colas
	 * @return SliderTextContainer
	 */
	public SliderTextContainer getTrimSlider() {
		if (trimSlider == null) {
			trimSlider = new SliderTextContainer(0, 50, 0, false);
			trimSlider.setDecimal(true);
			trimSlider.setBorder("");
		}
		return trimSlider;
	}
	
	/**
	 * Activa o desactiva el control
	 * @param enable true activa y false desactiva los controles del panel
	 */
	public void setControlEnabled(boolean enabled) {
		if (enabled && getTrimCheck().isSelected())
			getTrimSlider().setControlEnabled(true);
		else
			getTrimSlider().setControlEnabled(false);
		getRemoveCheck().setEnabled(enabled);
		getTrimCheck().setEnabled(enabled);
		
		getActive().setSelected(enabled);
	}
	
	/**
	 * Maneja eventos de activar y desactivar el panel
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getActive()) {
			if (getActive().isSelected())
				setControlEnabled(true);
			else
				setControlEnabled(false);
		}
		if (e.getSource() == getTrimCheck())
			getTrimSlider().setControlEnabled(getTrimCheck().isSelected());
	}

	/**
	 * Obtiene el valor de selección del check de eliminar extremos
	 * @return boolean, true si está seleccionado y false si no lo está. 
	 */
	public boolean isRemoveEndsSelected() {
		return getRemoveCheck().isSelected();
	}

	/**
	 * Obtiene el valor de selección del check de recorte de colas.
	 * @return boolean, true si está seleccionado y false si no lo está.
	 */
	public boolean isTailTrimCheckSelected() {
		return getTrimCheck().isSelected();
	}

	/**
	 * Obtiene el valor de recorte de colas seleccionado en el slider
	 * @return double
	 */
	public double getTrimValue() {
		return getTrimSlider().getValue();
	}

	/**
	 * Activa o desactiva el checkbox de recorte de colas
	 * @param active true si deseamos activarlo y false para desactivarlo
	 */
	public void setTailTrimCheckActive(boolean active) {
		getTrimCheck().setSelected(active);
		getTrimSlider().setControlEnabled(active);
	}

	/**
	 * Activa o desactiva el checkbox de eliminar extremos
	 * @param active true si deseamos activarlo y false para desactivarlo
	 */
	public void setRemoveEndsActive(boolean active) {
		getRemoveCheck().setSelected(active);
	}

	/**
	 * Asigna un valor al slider de porcentaje de recorte
	 * @param active true si deseamos activarlo y false para desactivarlo
	 */
	public void setTailTrimValue(double value) {
		getTrimSlider().setValue(value);
	}
}
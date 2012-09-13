/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.gvsig.gui.beans.slidertext.SliderTextContainer;
import org.gvsig.gui.beans.slidertext.listeners.SliderListener;

import com.iver.andami.PluginServices;
/**
 * Panel para los controles de brillo y contrase .
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EnhancedBrightnessContrastPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = -475762560608930500L;
	private InternalPanel     internalPanel    = null;
	private JCheckBox         active           = null;

	/**
	 * Panel con los controles deslizantes de brillo y contraste
	 * 
	 * @author Nacho Brodin (nachobrodin@gmail.com)
	 */
	class InternalPanel extends JPanel {
		final private static long	serialVersionUID = 0;
		protected SliderTextContainer brightness = null;
		protected SliderTextContainer contrast = null;

		/**
		 * Contructor
		 */
		public InternalPanel() {
			brightness = new SliderTextContainer(-255, 255, 0, false);
			contrast = new SliderTextContainer(-255, 255, 0, false);
			brightness.setBorder(PluginServices.getText(this, "brillo"));
			contrast.setBorder(PluginServices.getText(this, "contraste"));
			init();
		}

		private void init() {
			this.setLayout(new GridLayout(2, 1));
			this.add(brightness);
			this.add(contrast);
		}

		/**
		 * Activa o desactiva el control
		 * @param enable true activa y false desactiva los controles del panel
		 */
		public void setControlEnabled(boolean enabled) {
			brightness.setControlEnabled(enabled);
			contrast.setControlEnabled(enabled);
		}
	}

	/**
	 * Contructor
	 */
	public EnhancedBrightnessContrastPanel() {
		super();
		internalPanel = new InternalPanel();
		initialize();
	}

	private void initialize() {
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "brillo_y_contraste"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		setLayout(new BorderLayout());
		add(getActive(), BorderLayout.NORTH);
		add(internalPanel, BorderLayout.CENTER);
		getActive().addActionListener(this);
		this.setPreferredSize(new Dimension(100, 80));
	}

	/**
	 * Obtiene el check de activar
	 * @return
	 */
	public JCheckBox getActive() {
		if (active == null) {
			active = new JCheckBox(PluginServices.getText(this, "activar"));
			active.setSelected(false);
			setControlEnabled(false);
		}
		return active;
	}

	/**
	 * Activa o desactiva el control
	 * @param enable true activa y false desactiva los controles del panel
	 */
	public void setControlEnabled(boolean enabled){
		this.getActive().setSelected(enabled);
		internalPanel.setControlEnabled(enabled);
	}

	/**
	 * Obtiene el valor del brillo que hay seleccionado en el control
	 * @return double que representa el contraste seleccionado. Puede hacerse un casting a entero ya que 
	 * no se consideran decimales
	 */
	public double getBrightnessValue(){
		return internalPanel.brightness.getValue();
	}

	/**
	 * Obtiene el valor del contraste que hay seleccionado en el control
	 * @return double que representa el contraste seleccionado. Puede hacerse un casting a entero ya que 
	 * no se consideran decimales
	 */
	public double getContrastValue(){
		return internalPanel.contrast.getValue();
	}

	/**
	 * Asigna el valor del brillo al control
	 * @param value
	 */
	public void setBrightnessValue(double value){
		internalPanel.brightness.setValue(value);
	}

	/**
	 * Asigna el valor del contraste al control
	 * @param value
	 */
	public void setContrastValue(double value){
		internalPanel.contrast.setValue(value);
	}

	/**
	 * Añade un listener para el slider de brillo
	 * @param l ChangeListener
	 */
	public void addBrightnessValueChangedListener(SliderListener l){
		internalPanel.brightness.addValueChangedListener(l);
	}

	/**
	 * Añade un listener para el slider de contraste
	 * @param l ChangeListener
	 */
	public void addContrastValueChangedListener(SliderListener l){
		internalPanel.contrast.addValueChangedListener(l);
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
	}
}
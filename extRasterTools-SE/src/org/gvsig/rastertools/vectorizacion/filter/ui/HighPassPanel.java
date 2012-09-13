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
package org.gvsig.rastertools.vectorizacion.filter.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

import org.gvsig.gui.beans.checkslidertext.CheckSliderTextContainer;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Panel con los controles del filtro de paso alto.
 * 
 * 09/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class HighPassPanel extends BasePanel implements ActionListener {
	private static final long         serialVersionUID  = 1L;
	private JCheckBox                 active            = null;
	private CheckSliderTextContainer  radio             = null;
	private boolean                   enabled           = true;
	
	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public HighPassPanel() {
		init();
		translate();
	}
	
	/**
	 * Inicialización de los componentes gráficos
	 */
	protected void init() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "highpassfilter"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		
		add(getActive(), gbc);
		
		gbc.gridy = 1;
		add(getRadio(), gbc);
		
		getActive().addActionListener(this);
		setComponentEnabled(false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
		
	}
	
	/**
	 * Obtiene el check de activo 
	 * @return JCheckBox
	 */
	public JCheckBox getActive() {
		if(active == null) {
			active = new JCheckBox();
			active.setSelected(false);
		}
		return active;
	}
		
	/**
	 * Obtiene la barra deslizadora con el radio del filtro de paso alto
	 * @return CheckSliderTextContainer
	 */
	public CheckSliderTextContainer getRadio() {
		if(radio == null)
			radio = new CheckSliderTextContainer(0, 255, 127, false, RasterToolsUtil.getText(this, "radio"), true, false, false);
		return radio;
	}

	/**
	 * Gestión del evento del check de activación y desactivación
	 */
	public void actionPerformed(ActionEvent e) {
		setComponentEnabled(enabled);
	}

	/**
	 * Activa o desactiva el componente. El estado de activación y desactivación de un
	 * componente depende de los controles que contiene. En este caso activa o desactiva
	 * la barra de incremento.
	 * @param enabled
	 */
	public void setComponentEnabled(boolean enabled) {
		if(getActive().isSelected() != enabled)
			getActive().setSelected(enabled);
		getRadio().setControlEnabled(enabled);
		this.enabled = !enabled;
	}
}

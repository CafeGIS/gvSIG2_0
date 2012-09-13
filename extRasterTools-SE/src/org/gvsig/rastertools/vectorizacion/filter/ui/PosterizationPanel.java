/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Panel con los controles para la posterizaci�n.
 * 
 * 09/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class PosterizationPanel extends BasePanel implements ActionListener {
	private static final long         serialVersionUID  = 1L;
	private JCheckBox                 active            = null;
	private DataInputContainer        levels            = null;
	private CheckSliderTextContainer  threshold         = null;
	private boolean                   enabled           = true;
	
	/**
	 *Inicializa componentes gr�ficos y traduce
	 */
	public PosterizationPanel() {
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "posterization"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		
		add(getActive(), gbc);
		
		gbc.gridy = 1;
		add(getLevels(), gbc);
		
		gbc.gridy = 2;
		add(getThreshold(), gbc);
		
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
		}
		return active;
	}
	
	/**
	 * Obtiene el control con el n�mero de niveles
	 * @return DataInputContainer
	 */
	public DataInputContainer getLevels() {
		if(levels == null) {
			levels = new DataInputContainer();
			levels.setLabelText(RasterToolsUtil.getText(this, "levels"));
		}
		return levels;
	}
	
	/**
	 * Obtiene la barra deslizadora con el umbral de posterizaci�n
	 * @return CheckSliderTextContainer
	 */
	public CheckSliderTextContainer getThreshold() {
		if(threshold == null)
			threshold = new CheckSliderTextContainer(0, 255, 127, false, RasterToolsUtil.getText(this, "threshold"), true, false, false);
		return threshold;
	}

	/**
	 * Gesti�n del evento del check de activaci�n y desactivaci�n
	 */
	public void actionPerformed(ActionEvent e) {
		setComponentEnabled(enabled);
	}

	/**
	 * Activa o desactiva el componente. El estado de activaci�n y desactivaci�n de un
	 * componente depende de los controles que contiene. En este caso activa o desactiva
	 * la barra de incremento y el campo de texto con el n�mero de niveles.
	 * @param enabled
	 */
	public void setComponentEnabled(boolean enabled) {
		if(getActive().isSelected() != enabled)
			getActive().setSelected(enabled);
		getThreshold().setControlEnabled(enabled);
		getLevels().setControlEnabled(enabled);
		this.enabled = !enabled;
	}
}

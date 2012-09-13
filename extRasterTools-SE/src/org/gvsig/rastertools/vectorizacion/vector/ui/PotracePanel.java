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
package org.gvsig.rastertools.vectorizacion.vector.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

import org.gvsig.gui.beans.checkslidertext.CheckSliderTextContainer;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;
/**
 * Panel con los controles de generación de lineas de contorno usando
 * la libreria de Potrace
 * 
 * @version 18/09/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PotracePanel extends BasePanel {
	private static final long serialVersionUID = 9192151992995371080L;
	private CheckSliderTextContainer bezierPoints          = null;
	private JComboBox                policy                = null;
	private JCheckBox                curveOptimization     = null;
	private JFormattedTextField      despeckle             = null;
	private JFormattedTextField      cornerThreshold       = null;
	private JFormattedTextField      optimizationTolerance = null;
	private JFormattedTextField      outputQuantization    = null;

	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public PotracePanel() {
		init();
		translate();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		GridBagConstraints gbc;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "potracelines"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));

		int line = 0;

		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = line;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(5, 5, 2, 2);
		add(getBezierPoints(), gbc);
		
		line++;
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 5, 2, 2);
		add(new JLabel(RasterToolsUtil.getText(this, "policy") + ":"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 2, 2, 5);
		add(getPolicy(), gbc);

		line++;
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 5, 2, 2);
		add(new JLabel(RasterToolsUtil.getText(this, "despeckle") + ":"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 2, 2, 5);
		add(getDespeckle(), gbc);

		line++;
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 5, 2, 2);
		add(new JLabel(RasterToolsUtil.getText(this, "corner_threshold") + ":"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 2, 2, 5);
		add(getCornerThreshold(), gbc);

		line++;
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 5, 2, 2);
		add(new JLabel(RasterToolsUtil.getText(this, "optimization_tolerance") + ":"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 2, 2, 5);
		add(getOptimizationTolerance(), gbc);

		line++;
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 5, 2, 2);
		add(new JLabel(RasterToolsUtil.getText(this, "output_quantization") + ":"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 2, 2, 5);
		add(getOutputQuantization(), gbc);
		
		line++;
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 0;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 5, 5, 2);
		add(new JLabel(RasterToolsUtil.getText(this, "curve_optimization") + ":"), gbc);
		
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 1;
		gbc.gridy = line;
		gbc.insets = new Insets(2, 2, 5, 5);
		add(getCurveOptimization(), gbc);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
		
	}

	/**
	 * Activa o desactiva el componente. El estado de activación y desactivación de un
	 * componente depende de los controles que contiene. En este caso activa o desactiva
	 * todos los componentes que tiene dentro.
	 * @param enabled
	 */
	public void setComponentEnabled(boolean enabled) {
		this.getPolicy().setEnabled(enabled);
		this.getBezierPoints().setControlEnabled(enabled);
	}
	
	/**
	 * Obtiene el check de activo 
	 * @return JCheckBox
	 */
	public JComboBox getPolicy() {
		if (policy == null) {
			policy = new JComboBox();
			policy.addItem("black");
			policy.addItem("white");
			policy.addItem("right");
			policy.addItem("left");
			policy.addItem("minority");
			policy.addItem("majority");
			policy.addItem("random");
			policy.setSelectedItem("minority");
		}
		return policy;
	}

	/**
	 * Obtiene el Slider que controla el numero de puntos en las curvas de bezier
	 * @return
	 */
	public CheckSliderTextContainer getBezierPoints() {
		if (bezierPoints == null) {
			bezierPoints = new CheckSliderTextContainer(2, 100, 7, false, RasterToolsUtil.getText(this, "points"), true, false, false);
		}
		return bezierPoints;
	}
	
	/**
	 * Obtiene el check de la curva de optimizacion
	 * @return
	 */
	public JCheckBox getCurveOptimization() {
		if (curveOptimization == null) {
			curveOptimization = new JCheckBox();
			curveOptimization.setSelected(true);
		}
		return curveOptimization;
	}
	
	/**
	 * @return the despeckle
	 */
	public JFormattedTextField getDespeckle() {
		if (despeckle == null) {
			despeckle = new JFormattedTextField();
			despeckle.setValue(new Integer(0));
		}
		return despeckle;
	}

	/**
	 * @return the cornerThreshold
	 */
	public JFormattedTextField getCornerThreshold() {
		if (cornerThreshold == null) {
			cornerThreshold = new JFormattedTextField();
			cornerThreshold.setValue(new Double(1.0));
		}
		return cornerThreshold;
	}

	/**
	 * @return the optimizationTolerance
	 */
	public JFormattedTextField getOptimizationTolerance() {
		if (optimizationTolerance == null) {
			optimizationTolerance = new JFormattedTextField();
			optimizationTolerance.setValue(new Double(0.2));
		}
		return optimizationTolerance;
	}

	/**
	 * @return the outputQuantization
	 */
	public JFormattedTextField getOutputQuantization() {
		if (outputQuantization == null) {
			outputQuantization = new JFormattedTextField();
			outputQuantization.setValue(new Integer(10));
		}
		return outputQuantization;
	}
}
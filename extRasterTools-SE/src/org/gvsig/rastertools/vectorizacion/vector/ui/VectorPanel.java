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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.gvsig.raster.beans.previewbase.IUserPanelInterface;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.vectorization.VectorizationBinding;
import org.gvsig.rastertools.vectorizacion.vector.VectorData;
/**
 * Panel con los controles de opciones de vectorización
 * 
 * 09/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class VectorPanel extends BasePanel implements ActionListener, IUserPanelInterface, Observer {
	private static final long serialVersionUID = -3059193834485803008L;
	private ContourLinesPanel contourPanel      = null;
	private PotracePanel      potracePanel      = null;
	private JComboBox         algorithm         = null;
	
	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public VectorPanel() {
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#init()
	 */
	protected void init() {
		setBorder(BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "vector_generation"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		setLayout(new GridBagLayout());

		GridBagConstraints gbc;


		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 2, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(getAlgorithm(), gbc);

		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 2, 5);
		gbc.gridy = 1;
		gbc.gridx = 0;
		add(getContourLinesPanel(), gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(2, 5, 5, 5);
		gbc.gridy = 2;
		gbc.gridx = 0;
		add(getPotracePanel(), gbc);

		gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridy = 3;
		gbc.gridx = 0;
		add(new JPanel(), gbc);
		
		/*gbc.gridy = 1;
		add(getOutputScalePanel(), gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridy = 2;
		add(getPosterizationPanel(), gbc);
		
		gbc.gridy = 3;
		add(getHighPassPanel(), gbc);
		
		gbc.gridy = 4;
		add(getNoisePanel(), gbc);*/
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
	}
	
	public void actionPerformed(ActionEvent e) {
	}

	/**
	 * Obtiene el panel de vectorización por lineas de contorno
	 * @return
	 */
	public ContourLinesPanel getContourLinesPanel() {
		if (contourPanel == null) {
			contourPanel = new ContourLinesPanel();
		}
		return contourPanel;
	}

	/**
	 * Obtiene el panel de vectorización por lineas de contorno para el algoritmo
	 * de Potrace
	 * @return
	 */
	public PotracePanel getPotracePanel() {
		if (potracePanel == null) {
			potracePanel = new PotracePanel();
		}
		return potracePanel;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getTitle()
	 */
	public String getTitle() {
		return RasterToolsUtil.getText(this, "vectorization");
	}

	/**
	 * Actualiza los valores de los paneles cuando los datos de ClippingData varian
	 * @param o 
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		if (!(o instanceof VectorData))
			return;

		VectorData data = (VectorData) o;

		setEnableValueChangedEvent(false);

		getContourLinesPanel().setVisible(data.getAlgorithm() == VectorData.CONTOUR_LINES);
		getPotracePanel().setVisible(data.getAlgorithm() == VectorData.POTRACE_LINES);

		switch (data.getAlgorithm()) {
			case VectorData.CONTOUR_LINES:
				getAlgorithm().setSelectedItem("contour");
				break;
			case VectorData.POTRACE_LINES:
				getAlgorithm().setSelectedItem("potrace");
				break;
		}

		getContourLinesPanel().getDistance().setValue(String.valueOf(data.getDistance()));
		
		getPotracePanel().getCurveOptimization().setSelected(data.isCurveOptimization());
		getPotracePanel().getDespeckle().setValue(new Integer(data.getDespeckle()));
		getPotracePanel().getCornerThreshold().setValue(new Double(data.getCornerThreshold()));
		getPotracePanel().getOptimizationTolerance().setValue(new Double(data.getOptimizationTolerance()));
		getPotracePanel().getOutputQuantization().setValue(new Integer(data.getOutputQuantizqtion()));
		
		switch (data.getPolicy()) {
			case VectorizationBinding.POLICY_BLACK:
				getPotracePanel().getPolicy().setSelectedItem("black");
				break;
			case VectorizationBinding.POLICY_WHITE:
				getPotracePanel().getPolicy().setSelectedItem("white");
				break;
			case VectorizationBinding.POLICY_RIGHT:
				getPotracePanel().getPolicy().setSelectedItem("right");
				break;
			case VectorizationBinding.POLICY_LEFT:
				getPotracePanel().getPolicy().setSelectedItem("left");
				break;
			case VectorizationBinding.POLICY_MAJORITY:
				getPotracePanel().getPolicy().setSelectedItem("majority");
				break;
			case VectorizationBinding.POLICY_RANDOM:
				getPotracePanel().getPolicy().setSelectedItem("random");
				break;
			default:
				getPotracePanel().getPolicy().setSelectedItem("minority");
				break;
		}
		setEnableValueChangedEvent(true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getPanel()
	 */
	public JPanel getPanel() {
		return this;
	}
	
	/**
	 * Obtiene el algoritmo a usar
	 * @return JCheckBox
	 */
	public JComboBox getAlgorithm() {
		if (algorithm == null) {
			algorithm = new JComboBox();
			algorithm.addItem("contour");
			algorithm.addItem("potrace");
			algorithm.setSelectedItem("contour");
		}
		return algorithm;
	}
}

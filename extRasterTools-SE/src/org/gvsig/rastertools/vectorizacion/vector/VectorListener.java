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
package org.gvsig.rastertools.vectorizacion.vector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainerListener;
import org.gvsig.gui.beans.slidertext.listeners.SliderEvent;
import org.gvsig.gui.beans.slidertext.listeners.SliderListener;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.vectorization.VectorizationBinding;
import org.gvsig.rastertools.vectorizacion.vector.ui.VectorPanel;
/**
 * Clase para la gestión de eventos de los componentes gráficos del panel de preproceso
 * de vectorización.
 * 
 * 12/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class VectorListener implements ActionListener, DataInputContainerListener, ItemListener, SliderListener, PropertyChangeListener {
	private VectorPanel   panel   = null;
	private FLyrRasterSE  lyr     = null;
	private VectorData    data    = null;
	private VectorProcess process = null;

	/**
	 * Asigna el nombre de la capa
	 * @param lyrName
	 */
	public VectorListener(FLyrRasterSE lyr, VectorPanel panel, VectorData data) {
		this.lyr = lyr;
		setData(data);
		setDataView(panel);
		if (process != null)
			process.setSourceLayer(lyr);
	}
	
	/**
	 * Asigna la vista de datos. En este caso es el panel de preprocesado de la vectorización
	 * @param prepPanel
	 */
	private void setDataView(VectorPanel prepPanel) {
		this.panel = prepPanel;
		process = new VectorProcess(lyr, null, data.getProjLayer());
		panel.getContourLinesPanel().getDistance().addValueChangedListener(this);
		panel.getPotracePanel().getPolicy().addItemListener(this);
		panel.getAlgorithm().addItemListener(this);
		panel.getPotracePanel().getCurveOptimization().addActionListener(this);
		panel.getPotracePanel().getBezierPoints().addValueChangedListener(this);
		panel.getPotracePanel().getDespeckle().addPropertyChangeListener("value", this);
		panel.getPotracePanel().getCornerThreshold().addPropertyChangeListener("value", this);
		panel.getPotracePanel().getOptimizationTolerance().addPropertyChangeListener("value", this);
		panel.getPotracePanel().getOutputQuantization().addPropertyChangeListener("value", this);
	}
	
	/**
	 * Asigna el modelo de datos de los interfaces
	 * @param coorData
	 * @param grayConvData
	 */
	private void setData(VectorData data) {
		this.data = data;
	}
		
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (!panel.isEnableValueChangedEvent())
			return;
		
		if (e.getSource() == panel.getPotracePanel().getCurveOptimization()) {
			data.setCurveOptimization(panel.getPotracePanel().getCurveOptimization().isSelected());
		}
	}
	
	/**
	 * Asigna la capa fuente para el proceso
	 * @param lyr
	 */
	public void setProcessSource(FLyrRasterSE lyr) {
		if (process != null)
			process.setSourceLayer(lyr);
	}
	
	/**
	 * Acciones realizadas cuando se activa el proceso
	 */
	public void apply() {
		try {
			switch (data.getAlgorithm()) {
				case VectorData.CONTOUR_LINES:
					process.contourLines(data.getDistance());
					break;
				case VectorData.POTRACE_LINES:
					process.potraceLines(
							data.getPolicy(),
							data.getBezierPoints(),
							data.getDespeckle(),
							data.getCornerThreshold(),
							data.getOptimizationTolerance(),
							data.getOutputQuantizqtion(),
							data.isCurveOptimization());
					break;
			}
		} catch (LoadLayerException e1) {
			RasterToolsUtil.messageBoxError("error_carga_capa", null, e1);
		} catch (FilterTypeException e1) {
			RasterToolsUtil.messageBoxError("error_filtering", null, e1);
		}
	}

	/**
	 * Captura los eventos de las cajas de texto
	 */
	public void actionValueChanged(EventObject e) {
		if (!panel.isEnableValueChangedEvent())
			return;

		//Distancia del método de lineas de contorno
		if (e.getSource() == panel.getContourLinesPanel().getDistance().getDataInputField()) {
			String valueStr = panel.getContourLinesPanel().getDistance().getValue();
			double value = 0;
			try {
				value = Double.parseDouble(valueStr);
				data.setDistance(value);
			} catch (NumberFormatException ex) {
				RasterToolsUtil.debug("Imposible convertir a entero", panel, ex);
			}
		}
	}
	
	/**
	 * Asigna el interfaz para que el proceso ejectute las acciones de finalización
	 * al acabar.
	 * @param endActions
	 */
	public void setProcessActions(IProcessActions endActions) {
		if (process != null)
			process.setProcessActions(endActions);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		if (!panel.isEnableValueChangedEvent())
			return;

		// Comprobamos si ha cambiado la seleccion del algoritmo a usar en la vectorización
		if (e.getSource() == panel.getAlgorithm()) {
			do {
				if (panel.getAlgorithm().getSelectedItem().equals("contour")) {
					data.setAlgorithm(VectorData.CONTOUR_LINES);
					break;
				}
				if (panel.getAlgorithm().getSelectedItem().equals("potrace")) {
					data.setAlgorithm(VectorData.POTRACE_LINES);
					break;
				}
			} while (false);
		}

		// Comprobamos si ha cambiado la seleccion en el combo de policy de potrace
		if (e.getSource() == panel.getPotracePanel().getPolicy()) {
			do {
				if (panel.getPotracePanel().getPolicy().getSelectedItem().equals("black")) {
					data.setPolicy(VectorizationBinding.POLICY_BLACK);
					break;
				}
				if (panel.getPotracePanel().getPolicy().getSelectedItem().equals("white")) {
					data.setPolicy(VectorizationBinding.POLICY_WHITE);
					break;
				}
				if (panel.getPotracePanel().getPolicy().getSelectedItem().equals("right")) {
					data.setPolicy(VectorizationBinding.POLICY_RIGHT);
					break;
				}
				if (panel.getPotracePanel().getPolicy().getSelectedItem().equals("left")) {
					data.setPolicy(VectorizationBinding.POLICY_LEFT);
					break;
				}
				if (panel.getPotracePanel().getPolicy().getSelectedItem().equals("minority")) {
					data.setPolicy(VectorizationBinding.POLICY_MINORITY);
					break;
				}
				if (panel.getPotracePanel().getPolicy().getSelectedItem().equals("majority")) {
					data.setPolicy(VectorizationBinding.POLICY_MAJORITY);
					break;
				}
				if (panel.getPotracePanel().getPolicy().getSelectedItem().equals("random")) {
					data.setPolicy(VectorizationBinding.POLICY_RANDOM);
					break;
				}
			} while (false);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.slidertext.listeners.SliderListener#actionValueChanged(org.gvsig.gui.beans.slidertext.listeners.SliderEvent)
	 */
	public void actionValueChanged(SliderEvent e) {
		if (!panel.isEnableValueChangedEvent())
			return;
		
		// Comprobamos si ha cambiado el valor de numero de puntos de bezier para la vectorizacion
		if (e.getSource() == panel.getPotracePanel().getBezierPoints()) {
			data.setBezierPoints((int) panel.getPotracePanel().getBezierPoints().getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (!panel.isEnableValueChangedEvent())
			return;

		if (evt.getSource() == panel.getPotracePanel().getDespeckle()) {
			data.setDespeckle(((Integer) panel.getPotracePanel().getDespeckle().getValue()).intValue());
		}
		
		if (evt.getSource() == panel.getPotracePanel().getCornerThreshold()) {
			data.setCornerThreshold(((Double) panel.getPotracePanel().getCornerThreshold().getValue()).doubleValue());
		}
		
		if (evt.getSource() == panel.getPotracePanel().getOptimizationTolerance()) {
			data.setOptimizationTolerance(((Double) panel.getPotracePanel().getOptimizationTolerance().getValue()).doubleValue());
		}
		
		if (evt.getSource() == panel.getPotracePanel().getOutputQuantization()) {
			data.setOutputQuantization(((Integer) panel.getPotracePanel().getOutputQuantization().getValue()).intValue());
		}
	}
	
	public void actionValueDragged(SliderEvent e) {}
}
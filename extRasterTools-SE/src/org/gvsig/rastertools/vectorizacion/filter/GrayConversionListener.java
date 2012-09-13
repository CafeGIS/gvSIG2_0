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
package org.gvsig.rastertools.vectorizacion.filter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainerListener;
import org.gvsig.gui.beans.slidertext.listeners.SliderEvent;
import org.gvsig.gui.beans.slidertext.listeners.SliderListener;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.beans.previewbase.PreviewBasePanel;
import org.gvsig.raster.filter.grayscale.GrayScaleFilter;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.vectorizacion.filter.ui.GrayConversionPanel;

/**
 * Clase para la gestión de eventos de los componentes gráficos del panel de preproceso
 * de vectorización.
 * 
 * 12/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GrayConversionListener implements ActionListener, DataInputContainerListener, SliderListener {
	private GrayConversionPanel              panel                    = null;
	private FLyrRasterSE                     lyr                      = null;
	private GrayConversionData               data                     = null;
	private GrayConversionProcess            process                  = null;
	private GrayConversionPreviewRender      previewRender            = null;
	private PreviewBasePanel                 previewPanel             = null;
	
	/**
	 * Constructor. Asigna los listeners a los componentes
	 * @param prepPanel
	 */
	public GrayConversionListener(FLyrRasterSE lyr, GrayConversionPanel panel, GrayConversionData data) {
		this.lyr = lyr;
		setDataView(panel);
		setData(data);
	}
	
	/**
	 * Acciones de inicialización del componente
	 */
	private void initActions() {
		getPreviewRender();
		if(lyr.getBandCount() == 1)
			data.setBands(new String[]{"GRAY"});
		
		data.setPosterizationActive(true);
		panel.getPosterizationPanel().setComponentEnabled(true);
		data.updateObservers();
	}
	
	/**
	 * Obtiene el render de la preview asociada
	 * @return IPreviewRenderProcess
	 */
	public GrayConversionPreviewRender getPreviewRender() {
		if(previewRender == null)
			previewRender = new GrayConversionPreviewRender(lyr, data);
		return previewRender;
	}
	
	/**
	 * Asigna la vista de datos. En este caso es el panel de preprocesado de la vectorización
	 * @param prepPanel
	 */
	public void setDataView(GrayConversionPanel prepPanel) {
		this.panel = prepPanel;
		process = new GrayConversionProcess(null);
		process.setSourceLayer(lyr);
		
		panel.getComboBands().addActionListener(this);
		panel.getPosterizationPanel().getActive().addActionListener(this);
		panel.getPosterizationPanel().getLevels().addValueChangedListener(this);
		panel.getPosterizationPanel().getThreshold().addValueChangedListener(this);
		panel.getPosterizationPanel().getActive().setSelected(true);
		
		panel.getNoisePanel().getActive().addActionListener(this);
		panel.getNoisePanel().getThreshold().addValueChangedListener(this);
		
		panel.getModePanel().getActive().addActionListener(this);
		panel.getModePanel().getThreshold().addValueChangedListener(this);
		
		//panel.getHighPassPanel().getActive().addActionListener(this);
		//panel.getHighPassPanel().getRadio().addValueChangedListener(this);
	}
	
	/**
	 * Asigna el modelo de datos de los interfaces
	 * @param coorData
	 * @param grayConvData
	 */
	public void setData(GrayConversionData grayConvData) {
		this.data = grayConvData;
		initActions();
	}
	
	/**
	 * Asigna el panel con la previsualización
	 * @param prev
	 */
	public void setPreviewPanel(PreviewBasePanel prev) {
		this.previewPanel = prev;
	}
	
	/**
	 * Método para refresco de preview. Este puede no existir en caso de 
	 * usarse la funcionalidad de forma independiente por lo que habrá que
	 * comprobar si existe antes del refresco.
	 */
	public void refreshPreview() {
		if(previewPanel != null) {
			/*while(RasterTaskQueue.getProcessCount() > 0)
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}*/
			previewPanel.refreshPreview();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(!panel.isEnableValueChangedEvent())
			return;
		
		//Cambio combo de bandas seleccionadas para la conversión a B/W
		if(e.getSource() == panel.getComboBands()) {
			String value = (String)panel.getComboBands().getSelectedItem();
			if(value.compareTo("R") == 0) 
				data.setBandType(GrayScaleFilter.R);
			if(value.compareTo("G") == 0) 
				data.setBandType(GrayScaleFilter.G);
			if(value.compareTo("B") == 0) 
				data.setBandType(GrayScaleFilter.B);
			if(value.compareTo("RGB") == 0) 
				data.setBandType(GrayScaleFilter.RGB);
			if(value.compareTo("GRAY") == 0) 
				data.setBandType(GrayScaleFilter.GRAY);
		}
		
		//Check de activar/desactivar posterización
		if(e.getSource() == panel.getPosterizationPanel().getActive()) {
			data.setPosterizationActive(panel.getPosterizationPanel().getActive().isSelected());
		}
		
		//Slider para el umbral de posterización
		if(e.getSource() == panel.getPosterizationPanel().getThreshold().getSlider()) {
			data.setPosterizationThreshold((int)panel.getPosterizationPanel().getThreshold().getValue());
		}
		
		//Check de activar/desactivar ruido
		if(e.getSource() == panel.getNoisePanel().getActive()) {
			data.setNoiseActive(panel.getNoisePanel().getActive().isSelected());
		}
		
		//Slider para el umbral de ruido
		if(e.getSource() == panel.getNoisePanel().getThreshold().getSlider()) {
			data.setNoiseThreshold((int)panel.getNoisePanel().getThreshold().getValue());
		}
		
		//Check de activar/desactivar moda
		if(e.getSource() == panel.getModePanel().getActive()) {
			data.setModeActive(panel.getModePanel().getActive().isSelected());
		}
		
		//Slider para el umbral de moda
		if(e.getSource() == panel.getModePanel().getThreshold().getSlider()) {
			data.setModeThreshold((int)panel.getModePanel().getThreshold().getValue());
		}
		
		//Check de activar/desactivar el paso alto
		/*if(e.getSource() == panel.getHighPassPanel().getActive()) {
			data.setHighPassActive(panel.getHighPassPanel().getActive().isSelected());
		}*/
		
		refreshPreview();
	}
	
	/**
	 * Asigna la capa fuente para el proceso
	 * @param lyr
	 */
	public void setProcessSource(FLyrRasterSE lyr) {
		if(process != null)
			process.setSourceLayer(lyr);
	}
	
	/**
	 * Aplica las acciones
	 */
	public void apply() {
		try {
			process.grayScaleProcess(previewRender, data);
		} catch (FilterTypeException e) {
			RasterToolsUtil.messageBoxError("error_filtering", null, e);
		}
		//refreshPreview();
	}
	
	/**
	 * Captura los eventos de las cajas de texto
	 */
	public void actionValueChanged(EventObject e) {
		if(!panel.isEnableValueChangedEvent())
			return;
		
		//Niveles de la posterización
		if(e.getSource() == panel.getPosterizationPanel().getLevels().getDataInputField()) {
			try {
				String value = panel.getPosterizationPanel().getLevels().getValue();
				data.setPosterizationLevels((int)Double.parseDouble(value));
				if((int)Double.parseDouble(value) == 2)
					panel.getPosterizationPanel().getThreshold().setControlEnabled(true);
				else
					panel.getPosterizationPanel().getThreshold().setControlEnabled(false);
			} catch (NumberFormatException ex) {
				RasterToolsUtil.debug("Imposible convertir a entero", panel, ex);
			}
			refreshPreview();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.slidertext.listeners.SliderListener#actionValueChanged(org.gvsig.gui.beans.slidertext.listeners.SliderEvent)
	 */
	public void actionValueChanged(SliderEvent e) {
		if(!panel.isEnableValueChangedEvent())
			return;
		
		//Evento de movimiento del slider de cambio de umbral de posterización
		if(e.getSource() == panel.getPosterizationPanel().getThreshold()) 
			data.setPosterizationThreshold((int)panel.getPosterizationPanel().getThreshold().getValue());
		
		//Evento de movimiento del slider de cambio de umbral de reducción de ruido
		if(e.getSource() == panel.getNoisePanel().getThreshold()) 
			data.setNoiseThreshold((int)panel.getNoisePanel().getThreshold().getValue());
		
		//Evento de movimiento del slider de cambio de umbral de moda
		if(e.getSource() == panel.getModePanel().getThreshold()) 
			data.setModeThreshold((int)panel.getModePanel().getThreshold().getValue());
		
		refreshPreview();
	}
	
	/**
	 * Asigna el interfaz para que el proceso ejectute las acciones de finalización
	 * al acabar.
	 * @param endActions
	 */
	public void setProcessActions(IProcessActions endActions) {
		if(process != null)
			process.setProcessActions(endActions);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#interrupted()
	 */
	public void interrupted() {
	}

	public void actionValueDragged(SliderEvent e) {
	}

}
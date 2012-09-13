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
package org.gvsig.rastertools.vectorizacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.slidertext.listeners.SliderEvent;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.beans.previewbase.IPreviewRenderProcess;
import org.gvsig.raster.util.RasterNotLoadException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.vectorizacion.clip.ClipListener;
import org.gvsig.rastertools.vectorizacion.clip.ClipProcess;
import org.gvsig.rastertools.vectorizacion.filter.GrayConversionListener;
import org.gvsig.rastertools.vectorizacion.filter.GrayConversionProcess;
import org.gvsig.rastertools.vectorizacion.filter.ui.GrayConversionPanel;
import org.gvsig.rastertools.vectorizacion.stretch.StretchListener;
import org.gvsig.rastertools.vectorizacion.stretch.StretchProcess;
import org.gvsig.rastertools.vectorizacion.stretch.ui.StretchPanel;
import org.gvsig.rastertools.vectorizacion.vector.VectorListener;
import org.gvsig.rastertools.vectorizacion.vector.VectorProcess;

/**
 * Clase para la gestión de eventos de los componentes gráficos del panel de preproceso
 * de vectorización.
 * 
 * 12/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class MainListener implements ActionListener, IProcessActions  {
	private MainPanel                        panel                    = null;
	private FLyrRasterSE                     lyr                      = null;
	
	private GrayConversionListener           grayConvList             = null;
	private ClipListener                     clipList                 = null;
	private VectorListener                   vectList                 = null;
	private StretchListener                  stretchList              = null;
	
	private int                              tabIndexSelected         = 0;
	private IPreviewRenderProcess            prevRender               = null;
	
	private FLyrRasterSE                     clip                     = null;
	private FLyrRasterSE                     gray                     = null;
	private MainDialog                       dialog                   = null;
	
	/**
	 * Constructor. Asigna los listeners a los componentes
	 * @param prepPanel
	 */
	public MainListener(FLyrRasterSE lyr, MainPanel prepPanel, GrayConversionListener grayConvList, 
			ClipListener clipList, VectorListener vectList, StretchListener stretchList) {
		this.lyr = lyr;
		setDataView(prepPanel);
		this.grayConvList = grayConvList;
		this.clipList = clipList;
		this.vectList = vectList;
		this.stretchList = stretchList;
		this.grayConvList.setProcessActions(this);
		this.clipList.setProcessActions(this);
		this.vectList.setProcessActions(this);
		this.stretchList.setProcessActions(this);
		tabIndexSelected = 0;
	}
	
	/**
	 * Asigna la vista de datos. En este caso es el panel de preprocesado de la vectorización
	 * @param prepPanel
	 */
	private void setDataView(MainPanel prepPanel) {
		this.panel = prepPanel;
		panel.getPreviewBasePanel().getButtonsPanel().getButton(13).addActionListener(this);
		panel.getPreviewBasePanel().getButtonsPanel().getButton(14).addActionListener(this);
		panel.getPreviewBasePanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_CANCEL).addActionListener(this);
	}
		
	/**
	 * Asigna el render de la preview
	 * @param prevRender
	 */
	public void setPreviewRender(IPreviewRenderProcess prevRender) {
		this.prevRender = prevRender;
	}
	
	/**
	 * Asigna el dialogo
	 * @param dialog
	 */
	public void setDialog(MainDialog dialog) {
		this.dialog = dialog;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(!panel.isEnableValueChangedEvent())
			return;
		
		//Botón de pantalla siguiente
		if(e.getSource() == panel.getPreviewBasePanel().getButtonsPanel().getButton(13)) {
			if(tabIndexSelected < (panel.getPanelCount() - 1)) {
				panel.getPreviewBasePanel().setUniquePanel(panel.getPanel(++tabIndexSelected));
				panel.getPreviewBasePanel().getButtonsPanel().getButton(14).setEnabled(true);
				if(tabIndexSelected == (panel.getPanelCount() - 1)) 
					panel.getPreviewBasePanel().getButtonsPanel().getButton(13).setText("finalizar");
			} else { //Finalizar
				tabIndexSelected++;
				vectList.setProcessSource(gray);
				vectList.apply();
				if(dialog != null) {
					dialog.close();
					return;
				}
			}
		}
		
		//Botón de pantalla anterior		
		if(e.getSource() == panel.getPreviewBasePanel().getButtonsPanel().getButton(14)) {
			if(tabIndexSelected > 0) {
				panel.getPreviewBasePanel().setUniquePanel(panel.getPanel(--tabIndexSelected));
				panel.getPreviewBasePanel().getButtonsPanel().getButton(13).setText("siguiente");
				if(tabIndexSelected == 0) 
					panel.getPreviewBasePanel().getButtonsPanel().getButton(14).setEnabled(false);
			}
		}
				
		//Cancelar
		if(e.getSource() == panel.getPreviewBasePanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_CANCEL)) {
			if(dialog != null)
				dialog.close();
		}

		clipProcess();
		grayScaleProcess();
		stretchProcess();
		
		//Mostrar u ocultar preview
		if(tabIndexSelected == 0) {
			prevRender.setShowPreview(false);
			panel.getPreviewBasePanel().refreshPreview();
		} else
			prevRender.setShowPreview(true);
		
		//panel.getPreviewBasePanel().refreshPreview();
	}
	
	/**
	 * Lanza el proceso de recorte
	 */
	private void clipProcess() {
		//Generación de recorte
		if(tabIndexSelected == 1) {
			if(clip != null) 
				if(!RasterToolsUtil.messageBoxYesOrNot("clip_raster_again", this)) {
					prevRender.setShowPreview(true);
					panel.getPreviewBasePanel().refreshPreview();
					return;
				}

			clipList.setProcessSource(lyr);
			clipList.apply();
		}
	}
	
	/**
	 * Lanza el proceso de conversión a escala de grises
	 */
	private void grayScaleProcess() {
		//Generación de raster preprocesado con escala de grises y posterización
		if(tabIndexSelected == 2 && panel.getPanel(1) instanceof GrayConversionPanel) {
			if(gray != null) 
				if(!RasterToolsUtil.messageBoxYesOrNot("filter_raster_again", this)) {
					prevRender.setShowPreview(true);
					panel.getPreviewBasePanel().refreshPreview();
					return;
				}
			grayConvList.setProcessSource(clip);
			grayConvList.apply();
		}
	}
	
	/**
	 * Lanza el proceso de generación de tramos
	 */
	private void stretchProcess() {
		//Generación de tramos
		if(tabIndexSelected == 2 && panel.getPanel(1) instanceof StretchPanel) {
			if(gray != null) 
				if(!RasterToolsUtil.messageBoxYesOrNot("filter_raster_again", this)) {
					prevRender.setShowPreview(true);
					panel.getPreviewBasePanel().refreshPreview();
					return;
				}
			stretchList.setProcessSource(clip);
			stretchList.apply();
		}
	}
			
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.slidertext.listeners.SliderListener#actionValueDragged(org.gvsig.gui.beans.slidertext.listeners.SliderEvent)
	 */
	public void actionValueDragged(SliderEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#interrupted()
	 */
	public void interrupted() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object param) {
		if(param instanceof Object[] && ((Object[])param).length == 2) {
			if(((Object[])param)[0] instanceof ClipProcess) {
				clip = (FLyrRasterSE)((Object[])param)[1]; 
				if(grayConvList != null)
					grayConvList.refreshPreview();
				if(stretchList != null)
					stretchList.refreshPreview();
			}
			if(((Object[])param)[0] instanceof GrayConversionProcess ||
			   ((Object[])param)[0] instanceof StretchProcess) {
				gray = (FLyrRasterSE)((Object[])param)[1]; 
			}
			if(((Object[])param)[0] instanceof VectorProcess) {
				if (RasterToolsUtil.messageBoxYesOrNot("cargar_toc", this)) {
					try {
						RasterToolsUtil.loadLayer(null, (FLayer)((Object[])param)[1]);
					} catch (RasterNotLoadException e) {
						RasterToolsUtil.messageBoxError("error_loading_layer", null, e);
					}
				}
			}
		}
	}

}
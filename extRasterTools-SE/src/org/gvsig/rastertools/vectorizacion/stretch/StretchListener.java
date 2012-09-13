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
package org.gvsig.rastertools.vectorizacion.stretch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.beans.canvas.GCanvasEvent;
import org.gvsig.raster.beans.canvas.IGCanvasListener;
import org.gvsig.raster.beans.previewbase.PreviewBasePanel;
import org.gvsig.raster.datastruct.HistogramException;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.vectorizacion.stretch.ui.StretchPanel;

/**
 * Listener para el panel de selección de tramos
 * 
 * 08/08/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class StretchListener implements ActionListener, IGCanvasListener, TableModelListener, KeyListener {
	private FLyrRasterSE         lyr           = null;
	private StretchPanel         panel         = null;
	private StretchProcess       process       = null;
	private StretchData          data          = null;
	private StretchPreviewRender previewRender = null;
	private PreviewBasePanel     previewPanel  = null;
	
	/**
	 * Constructor. Asigna el panel asociado al listener
	 * @param panel
	 */
	public StretchListener(FLyrRasterSE lyr, StretchPanel panel, StretchData data) {
		this.panel = panel;
		this.lyr = lyr;
		this.data = data;
		panel.getCanvas().addValueChangedListener(this);
		panel.getModel().addTableModelListener(this);
		panel.getLoadButton().addActionListener(this);
		panel.getLoadButton().addKeyListener(this);
		panel.registerListener(BasePanel.KEYLISTENER, this);
		
		process = new StretchProcess(null);
		process.setSourceLayer(lyr);
		
		initActions();
	}

	/**
	 * Acciones de inicialización del componente
	 */
	private void initActions() {
		if(lyr == null)
			return;
		double min = lyr.getDataSource().getStatistics().getMinimun();
		double max = lyr.getDataSource().getStatistics().getMaximun();
		data.setMin(min);
		data.setMax(max);
		data.setSizeInterval(max - min);
		data.setNInterval(1);
		try {
			data.setHistogram(lyr.getDataSource().getHistogram().getTable());
		} catch (HistogramException e) {
			RasterToolsUtil.debug("No se puede crear un histograma", this, e);
		} catch (InterruptedException e) {
		}
		panel.getModel().setShiftAndDistance(min, max - min);
		data.updateObservers();
	}
	
	/**
	 * Obtiene el render de la preview asociada
	 * @return IPreviewRenderProcess
	 */
	public StretchPreviewRender getPreviewRender() {
		if(previewRender == null)
			previewRender = new StretchPreviewRender(lyr, data);
		return previewRender;
	}
	
	/**
	 * Asigna el panel con la previsualización
	 * @param prev
	 */
	public void setPreviewPanel(PreviewBasePanel prev) {
		this.previewPanel = prev;
		refreshPreview();
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
		if(e.getSource() == panel.getLoadButton()) {
			double min = panel.getStretchSelectorPanel().getData().getMinimum();
			double max = panel.getStretchSelectorPanel().getData().getMaximum();
			if(panel.getStretchSelectorPanel().getIntervalSize().isSelected()) 
				data.setSizeInterval(panel.getStretchSelectorPanel().getData().getStretchSize());
			
			if(panel.getStretchSelectorPanel().getIntervalNumber().isSelected()) 
				data.setNInterval(panel.getStretchSelectorPanel().getData().getStretchNumber());
			
			panel.getModel().setShiftAndDistance(min, max - min);
			
			panel.getCanvas().repaint();
			panel.getTable().updateUI();
			refreshPreview();
		}
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
			process.stretchProcess(previewRender, data);
		} catch (FilterTypeException e) {
			RasterToolsUtil.messageBoxError("error_filtering", null, e);
		}
		refreshPreview();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.IGCanvasListener#actionDataChanged(org.gvsig.raster.beans.canvas.GCanvasEvent)
	 */
	public void actionDataChanged(GCanvasEvent e) {
		panel.getTable().updateUI();
		refreshPreview();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.IGCanvasListener#actionDataDragged(org.gvsig.raster.beans.canvas.GCanvasEvent)
	 */
	public void actionDataDragged(GCanvasEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
		panel.getCanvas().repaint();
		refreshPreview();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			actionPerformed(new ActionEvent(panel.getLoadButton(), 0, null));
			refreshPreview();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}

}

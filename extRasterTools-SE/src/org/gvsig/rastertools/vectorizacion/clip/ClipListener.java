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
package org.gvsig.rastertools.vectorizacion.clip;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.RectangleBehavior;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.roi.ui.ROIManagerDialog;
import org.gvsig.rastertools.vectorizacion.clip.ui.ClipPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

/**
 * Clase para la gestión de eventos de los componentes gráficos del panel de preproceso
 * de vectorización.
 * 
 * 12/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ClipListener implements ActionListener, TableModelListener, ButtonsPanelListener, IProcessActions {
	private FLyrRasterSE                     lyr                      = null;
	private ClipData                         data                     = null;
	private ClipPanel                        panel                    = null;	
	private ClipProcess                      process                  = null;

	
	/**
	 * Constructor. Asigna los listeners a los componentes
	 * @param prepPanel
	 */
	public ClipListener(FLyrRasterSE lyr, ClipPanel prepPanel, ClipData data) {
		this.lyr = lyr;
		setDataView(prepPanel);
		setData(data);
	}
	
	/**
	 * Acciones de inicialización del componente
	 */
	private void initActions() {
		assignFullExtent();
		loadROIs();
	}
	
	/**
	 * Asigna la vista de datos. En este caso es el panel de preprocesado de la vectorización
	 * @param prepPanel
	 */
	private void setDataView(ClipPanel prepPanel) {
		this.panel = prepPanel;
		process = new ClipProcess(this);
		process.setSourceLayer(lyr);
		
		panel.getSelectionAreaPanel().getROI().addActionListener(this);
		panel.getSelectionAreaPanel().getButtonBarContainer().getButton(0).addActionListener(this);
		panel.getSelectionAreaPanel().getButtonBarContainer().getButton(1).addActionListener(this);
		panel.getSelectionAreaPanel().getTableContainer().getModel().addTableModelListener(this);
		panel.getComboOutputScale().addActionListener(this);		
	}
	
	/**
	 * Asigna el modelo de datos de los interfaces
	 * @param coorData
	 * @param grayConvData
	 */
	public void setData(ClipData coorData) {
		this.data = coorData;
		initActions();
	}
		
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(!panel.isEnableValueChangedEvent())
			return;
		//Seleccionar extent completo
		if(e.getSource() == panel.getSelectionAreaPanel().getButtonBarContainer().getButton(0)) {
			assignFullExtent();
		}
		
		//Seleccionar área desde la vista
		if(e.getSource() == panel.getSelectionAreaPanel().getButtonBarContainer().getButton(1)) {
			selectToolButton();
		}
		
		//Cuadro de seleccionar ROIS
		if(e.getSource() == panel.getSelectionAreaPanel().getROI()) {
			ROIManagerDialog roiManagerDialog = new ROIManagerDialog(500,250);

			try {
				roiManagerDialog.setLayer(lyr);
				roiManagerDialog.getROIsManagerPanel().addButtonPressedListener(this);
			} catch (GridException ex) {
			}
			RasterToolsUtil.addWindow(roiManagerDialog);
		}
		
		//Cambio de escala
		if(e.getSource() == panel.getComboOutputScale()) {
			data.setScaleSelected(panel.getComboOutputScale().getSelectedIndex());
		}
		
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
	 * Asigna el interfaz para que el proceso ejectute las acciones de finalización
	 * al acabar.
	 * @param endActions
	 */
	public void setProcessActions(IProcessActions endActions) {
		if(process != null)
			process.setProcessActions(endActions);
	}
	
	/**
	 * Acciones realizadas cuando se acepta la operación
	 */
	public void apply() {
		try {
			process.clip(data);
		} catch (FilterTypeException e) {
			RasterToolsUtil.messageBoxError("error_cutting", null, e);
		}
	}
			
	/**
	 * Gestión de los eventos de los botones de la ventana de selección de ROIs
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		//Al pulsar Aceptar o Aplicar se ejecuta el aceptar del panel
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY || e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			loadROIs();
		}
	}

	/**
	 * Asigna el extent completo a los cuadros de texto donde se introducen las coordenadas
	 * reales y píxel. 
	 */
	private void assignFullExtent() {
		Point2D ulPx = new Point2D.Double(0, 0);
		Point2D lrPx = new Point2D.Double(lyr.getPxWidth(), lyr.getPxHeight());

		//Convertimos a coordenadas reales
		Point2D ulWc = new Point2D.Double();
		Point2D lrWc = new Point2D.Double();
		lyr.getAffineTransform().transform(ulPx, ulWc);
		lyr.getAffineTransform().transform(lrPx, lrWc);

		data.setCoorRealFromDouble(ulWc.getX(), ulWc.getY(), lrWc.getX(), lrWc.getY());
		data.setCoorPixelFromDouble(ulPx.getX(), ulPx.getY(), lrPx.getX() - 1, lrPx.getY() - 1);
	}
	
	/**
	 * Acciones que se realizan para seleccionar la tool CutRaster
	 */
	public void selectToolButton() {
		// seleccionamos la vista de gvSIG
		IWindow[] list = PluginServices.getMDIManager().getAllWindows();
		BaseView view = null;
		for (int i = 0; i < list.length; i++) {
			if(list[i] instanceof View)
				view = (BaseView)list[i];
		}
		if (view == null)
			return;
	
		MapControl m_MapControl = view.getMapControl();

		// Listener de eventos de movimiento que pone las coordenadas del ratón en
		// la barra de estado
		StatusBarListener sbl = new StatusBarListener(m_MapControl);

		// Cortar Raster
		ClipMouseViewListener clippingMouseViewListener = new ClipMouseViewListener(m_MapControl, data, lyr);
		m_MapControl.addMapTool("cutRaster", new Behavior[] {
				new RectangleBehavior(clippingMouseViewListener), new MouseMovementBehavior(sbl)
				}
		);

		m_MapControl.setTool("cutRaster");
	}
	
	/**
	 * Carga las ROIS de la capa sobre la tabla
	 * @param layer
	 */
	public void loadROIs() {
		if (lyr == null)
			return;
		TableContainer table = panel.getSelectionAreaPanel().getTableContainer();
		try {
			table.removeAllRows();
		} catch (NotInitializeException e1) {
			return;
		}
		ArrayList roisArray = lyr.getRois();
		if (roisArray != null) {
			for (int i = 0; i < roisArray.size(); i++) {
				ROI roi = (ROI) roisArray.get(i);
	
				Object row[] = {"", "", ""};
				
				boolean active = false;
								
				row[0] = new Boolean(active);
				row[1] = roi.getName(); 
				row[2] = new Integer(i);
				try {
					table.addRow(row);
				} catch (NotInitializeException e) {
				}
			}
		}
	}
	
	/**
	 * Obtiene la lista de ROIs seleccionadas
	 * @return ArrayList con la lista de ROIs
	 */
	private ArrayList getSelectedROIs() {
		if (lyr == null)
			return null;

		ArrayList roisArray = lyr.getRois();
		ArrayList selected = new ArrayList();
		TableContainer table = panel.getSelectionAreaPanel().getTableContainer();
		if (roisArray != null) {
			for (int i = 0; i < roisArray.size(); i++) {
				try {
					if (((Boolean) table.getModel().getValueAt(i, 0)).booleanValue()) {
						selected.add(roisArray.get(i));
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//Entra aquí si se han añadido ROIs con el cuadro abierto. Pasamos de hacer nada
				}
			}
		}
		return selected;
	} 
	
	/**
	 * Asigna el extent completo a los cuadros de texto donde se introducen las coordenadas
	 * reales y píxel. 
	 */
	private void assignROISExtent() {
		ArrayList roiList = getSelectedROIs();
		Extent ext = null;
		if(roiList != null && roiList.size() > 0) {
			ext = ROI.getROIsMaximunExtent(roiList);
		} else
			assignFullExtent();
		
		if(ext == null)
			return;
		
		AffineTransform at = lyr.getAffineTransform();
		Point2D ulWc = new Point2D.Double(ext.minX(), ext.maxY());
		Point2D lrWc = new Point2D.Double(ext.maxX(), ext.minY());
		
		ulWc = lyr.adjustWorldRequest(ulWc);
		lrWc = lyr.adjustWorldRequest(lrWc);
		
		Point2D ulPx = new Point2D.Double();
		Point2D lrPx = new Point2D.Double();
		
		try {
			at.inverseTransform(ulWc, ulPx);
			at.inverseTransform(lrWc, lrPx);
		} catch (NoninvertibleTransformException e) {
			JOptionPane.showMessageDialog((Component) PluginServices.getMainFrame(), PluginServices.getText(this, "coordenadas_erroneas"));
			return;
		}

		data.setCoorRealFromDouble(ulWc.getX(), ulWc.getY(), lrWc.getX(), lrWc.getY());
		data.setCoorPixelFromDouble(ulPx.getX(), ulPx.getY(), lrPx.getX() - 1, lrPx.getY() - 1);
	}
	
	/**
	 * Gestión de eventos producidos en la tabla con la lista de ROIs
	 * @param e
	 */
	public void tableChanged(TableModelEvent e) {
		assignROISExtent();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#interrupted()
	 */
	public void interrupted() {
	}

	public void end(Object param) {
	}

}
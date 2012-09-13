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
package org.gvsig.rastertools.clipping.ui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.rastertools.clipping.ClippingData;
import org.gvsig.rastertools.clipping.panels.ClippingCoordinatesPanel;
import org.gvsig.rastertools.clipping.panels.ClippingOptionsPanel;
import org.gvsig.rastertools.clipping.panels.ClippingResolutionPanel;
import org.gvsig.rastertools.clipping.panels.ClippingSelectionPanel;
import org.gvsig.rastertools.clipping.ui.listener.ClippingPanelListener;

import com.iver.andami.PluginServices;
/**
 * <code>ClippingPanel</code>. Interfaz de usuario para el recorte de rasters.
 *
 * @version 17/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ClippingPanel extends DefaultButtonsPanel implements Observer {
	private static final long         serialVersionUID = 3078196473228467834L;

	private ClippingDialog            clippingDialog       = null;

	private JTabbedPane               jTabbedPane1         = null;

	private ClippingCoordinatesPanel  coordinatesPanel     = null;
	private ClippingResolutionPanel   resolutionPanel      = null;
	private ClippingSelectionPanel    selectionPanel       = null;
	private ClippingOptionsPanel      optionsPanel         = null;

	private ClippingPanelListener     clippingPanelListener = null;

	/**
	 * Crea un nuevo <code>ClippingPanel</code>
	 * @param clippingDialog
	 */
	public ClippingPanel(ClippingDialog clippingDialog) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCELAPPLY);
		getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setEnabled(false);
		getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(false);

		this.clippingDialog = clippingDialog;

		initialize();
	}
	
	/**
	 * Asigna el gestor de eventos y añade los listener a los controles
	 * que lo necesiten 
	 * @param list
	 */
	public void setClippingListener(ClippingPanelListener list) {
		this.clippingPanelListener = list;
		addListeners(clippingPanelListener);
		getCoordinatesPanel().setClippingListener(clippingPanelListener);
	}
	
	/**
	 * Añade listeners a los controles que lo necesitan
	 * @param list
	 */
	private void addListeners(ClippingPanelListener list) {
		getResolutionPanel().getCCellSize().addValueChangedListener(list);
		getResolutionPanel().getCWidth().addValueChangedListener(list);
		getResolutionPanel().getCHeight().addValueChangedListener(list);
		getResolutionPanel().getButtonRestore().addActionListener(list);
		addButtonPressedListener(list);
	}
		
	/**
	 * Obtener el <code>ClippingDialog</code> asociado a este objeto.
	 * @return ClippingDialog
	 */
	public ClippingDialog getClippingDialog() {
		return clippingDialog;
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		JPanel jPanel = null;
		jTabbedPane1 = new JTabbedPane();
		
		jTabbedPane1.addTab(PluginServices.getText(this, "coordenadas_recorte"), getCoordinatesPanel());

		jPanel = new JPanel();
		jPanel.setLayout(new BorderLayout());
		jPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		jPanel.add(getResolutionPanel());
		jTabbedPane1.addTab(PluginServices.getText(this, "resolucion_espacial"), jPanel);

		jTabbedPane1.addTab(PluginServices.getText(this, "seleccion_bandas"), getSelectionPanel());

		jPanel = new JPanel();
		jPanel.setLayout(new BorderLayout());
		jPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		jPanel.add(getOptionsPanel());
		jTabbedPane1.addTab(PluginServices.getText(this, "otras_opciones"), jPanel);

		setLayout(new BorderLayout());
		add(jTabbedPane1, BorderLayout.CENTER);
	}

	public ClippingCoordinatesPanel getCoordinatesPanel() {
		if (coordinatesPanel == null) {
			coordinatesPanel = new ClippingCoordinatesPanel();
		}
		return coordinatesPanel;
	}
	
	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	public ClippingResolutionPanel getResolutionPanel() {
		if (resolutionPanel == null) {
			resolutionPanel = new ClippingResolutionPanel();
			resolutionPanel.validate();
		}
		return resolutionPanel;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	public ClippingSelectionPanel getSelectionPanel() {
		if (selectionPanel == null) {
			selectionPanel = new ClippingSelectionPanel();
		}
		return selectionPanel;
	}

	/**
	 * This method initializes jOptions
	 *
	 * @return javax.swing.JPanel
	 */
	public ClippingOptionsPanel getOptionsPanel() {
		if (optionsPanel == null) {
			optionsPanel = new ClippingOptionsPanel();
		}
		return optionsPanel;
	}

	double textWidth, textHeight;
	
	/**
	 * Almacena el estado actual del ancho y alto para poder restaurarlo
	 * si lo necesitamos.
	 */
	public void saveStatus(ClippingData data) {
		textWidth = data.getPxWidth();
		textHeight = data.getPxHeight();
	}

	/**
	 * Restaura el estado almacenado por saveStatus
	 */
	public void restoreStatus(ClippingData data) {
		data.setPxWidth(textWidth);
		data.setPxHeight(textHeight);
		data.updateObservers();
	}

	/**
	 * Actualiza los valores de los paneles cuando los datos de ClippingData varian
	 * @param o 
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		if(!(o instanceof ClippingData))
			return;
		ClippingData data = (ClippingData)o;
		
		clippingPanelListener.setEnableValueChangedEvent(false);
		getResolutionPanel().setWidthText(Math.round(data.getPxWidth()), 0);
		getResolutionPanel().setHeightText(Math.round(data.getPxHeight()), 0);
		getCoordinatesPanel().setCoordReal(RasterUtilities.getCoord(data.getUlxWc(), data.getUlyWc(), data.getLrxWc(), data.getLryWc(), ClippingData.DEC));
		getCoordinatesPanel().setCoordPixel(RasterUtilities.getCoord(data.getPxMinX(), data.getPxMinY(), data.getPxMaxX(), data.getPxMaxY(), ClippingData.DEC));
		getResolutionPanel().setCellSizeText(data.getCellSize(), ClippingData.DEC);
		clippingPanelListener.setEnableValueChangedEvent(true);
	}
}

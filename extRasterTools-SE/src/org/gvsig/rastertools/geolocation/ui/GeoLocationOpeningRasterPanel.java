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
package org.gvsig.rastertools.geolocation.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.geom.AffineTransform;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.rastertools.geolocation.listener.GeoLocationPanelListener;

import com.iver.andami.PluginServices;

/**
 * Panel de geolocalización. Este muestra los parámetros de la matriz de transformación
 * que está aplicandose en esos momentos al raster. Tenemos también la posibilidad de introducir
 * los parámetros con las esquinas superior derecha e inferior izquierda.
 * 
 * @version 12/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class GeoLocationOpeningRasterPanel extends GeolocationBaseClassPanel {
	private static final long                       serialVersionUID = -7797379892312214949L;
	
	private JButton                                 apply = null;
	private JButton                                 cancel = null;
		 
	private JTabbedPane			                    coordsPanel = null;
	private JPanel			                        paramsPanel = null;
	private JPanel			                        buttonsPanel = null;

	private GeoLocationOpeningRasterCornersPanel    cornersPanel = null;
	private GeoLocationOpeningRasterTransfPanel     transfPanel = null;
	private GeoLocationOpeningRasterDialog          parent = null;
   
	/**
	 * Constructor
	 */
	public GeoLocationOpeningRasterPanel(GeoLocationOpeningRasterDialog parent) {
		this.parent = parent;
		listener = new GeoLocationPanelListener(this, null);
		transfPanel = new GeoLocationOpeningRasterTransfPanel(listener);
		cornersPanel = new GeoLocationOpeningRasterCornersPanel(listener);
		
		GridBagLayout gl = new GridBagLayout();
		this.setLayout(gl);
				
		apply = new JButton(PluginServices.getText(this,"apply"));
		apply.setPreferredSize(new Dimension(64,24));
		apply.addActionListener(listener);
				
		cancel = new JButton(PluginServices.getText(this,"cancel"));
		cancel.setPreferredSize(new Dimension(64,24));
		cancel.addActionListener(listener);
						
		getJTabbedPane();
		
		paramsPanel = new JPanel();
		GridLayout l1 = new GridLayout(2, 2);
		l1.setVgap(2);
		paramsPanel.setLayout(l1);

		getButtonsPanel();
		
		init();
	}

	/**
	 * Obtiene el dialogo que contiene al panel.
	 * @return GeoLocationOpeningRasterDialog
	 */
	public GeoLocationOpeningRasterDialog getDialog() {
		return parent;
	}
	
	/**
	 * Obtiene el panel con los Tabs
	 * @return
	 */
	private JTabbedPane getJTabbedPane() {
		if(coordsPanel == null) {
			coordsPanel = new JTabbedPane();
			coordsPanel.addTab(PluginServices.getText(this,"transf"), transfPanel);
			coordsPanel.addTab(PluginServices.getText(this, "corners"), cornersPanel);
		}
		return coordsPanel;
	}

	private void init() {
		JPanel applyButton = new JPanel();
		applyButton.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new java.awt.Insets(1, 0, 0, 2);
				
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		applyButton.add(apply, gbc);
		gbc.gridx = 1;
		applyButton.add(cancel, gbc);
		
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		buttonsPanel.add(applyButton, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new java.awt.Insets(1, 1, 1, 1);
		this.add(coordsPanel, gbc);
		
		gbc.gridy = 1;
		this.add(paramsPanel, gbc);
		gbc.weightx = 1.0;
		gbc.gridy = 2;
		this.add(buttonsPanel, gbc);
	}
	
	/**
	 * Obtiene el panel con los botones
	 * @return JPanel
	 */
	public JPanel getButtonsPanel() {
		if(buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new GridBagLayout());
			//buttonsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		}
		return buttonsPanel;
	}
	
	/**
	 * Asigna la capa raster del raster seleccionado en el TOC en base 
	 * al cual se asigna la georreferenciación al dialogo.
	 * @param lyr
	 */
	public void setParams(FLyrRasterSE lyr) {
		setLayer(lyr);
		if(transfPanel != null)
			transfPanel.setLayer(lyr);
		if(cornersPanel != null)
			cornersPanel.setLayer(lyr);
		loadTransform(lyr.getAffineTransform());
	}
	
	/**
	 * Carga los parámetros en el dialogo a partir de la capa
	 * @param lyr Capa raster
	 */
	public void loadTransform(AffineTransform at) {
		transfPanel.loadTransform(at);
		cornersPanel.loadTransform(at);
	}
	
	/**
	 * Obtiene el botón de aplicar
	 * @return JButton
	 */
	public JButton getApplyButton() {
		return apply;
	}
	
	/**
	 * Obtiene el botón de cancelar
	 * @return JButton
	 */
	public JButton getCancelButton() {
		return cancel;
	}
	
	/**
	 * Obtiene el tamaño de pixel en X
	 * @return
	 */
	public DataInputContainer getPsx() {
		if(coordsPanel.getSelectedComponent() == transfPanel)
			return transfPanel.getPsx();
		if(coordsPanel.getSelectedComponent() == cornersPanel)
			return cornersPanel.getPsx();
		return null;
	}

	/**
	 * Obtiene el tamaño de pixel en Y
	 * @return
	 */
	public DataInputContainer getPsy() {
		if(coordsPanel.getSelectedComponent() == transfPanel)
			return transfPanel.getPsy();
		if(coordsPanel.getSelectedComponent() == cornersPanel)
			return cornersPanel.getPsy();
		return null;
	}

	/**
	 * Obtiene la rotación en X
	 * @return
	 */
	public DataInputContainer getRotx() {
		if(coordsPanel.getSelectedComponent() == transfPanel)
			return transfPanel.getRotx();
		if(coordsPanel.getSelectedComponent() == cornersPanel)
			return cornersPanel.getRotx();
		return null;
	}

	/**
	 * Obtiene la rotación en Y
	 * @return
	 */
	public DataInputContainer getRoty() {
		if(coordsPanel.getSelectedComponent() == transfPanel)
			return transfPanel.getRoty();
		if(coordsPanel.getSelectedComponent() == cornersPanel)
			return cornersPanel.getRoty();
		return null;
	}

	/**
	 * Obtiene la X de la coordenada superior izquierda
	 * @return
	 */
	public DataInputContainer getUlx() {
		if(coordsPanel.getSelectedComponent() == transfPanel)
			return transfPanel.getUlx();
		if(coordsPanel.getSelectedComponent() == cornersPanel)
			return cornersPanel.getUlx();
		return null;
	}

	/**
	 * Obtiene la Y de la coordenada superior izquierda
	 * @return
	 */
	public DataInputContainer getUly() {
		if(coordsPanel.getSelectedComponent() == transfPanel)
			return transfPanel.getUly();
		if(coordsPanel.getSelectedComponent() == cornersPanel)
			return cornersPanel.getUly();
		return null;
	}

}

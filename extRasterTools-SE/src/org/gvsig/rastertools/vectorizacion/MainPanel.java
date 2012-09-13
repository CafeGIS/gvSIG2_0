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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.raster.beans.previewbase.IPreviewRenderProcess;
import org.gvsig.raster.beans.previewbase.PreviewBasePanel;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.rastertools.vectorizacion.clip.ui.ClipPanel;
import org.gvsig.rastertools.vectorizacion.filter.ui.GrayConversionPanel;
import org.gvsig.rastertools.vectorizacion.vector.ui.VectorPanel;

/**
 * Panel para la conversión a escala de grises. Lleva incluida una previsualización
 * de capa.
 * 
 * 09/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class MainPanel extends BasePanel {
	private static final long           serialVersionUID   = -1;
	private PreviewBasePanel            previewBasePanel   = null;
	private IPreviewRenderProcess       renderProcess      = null;
	private FLyrRasterSE                lyr                = null;
	//Lista de paneles en el Tab de PreviewBasePanel
	private ArrayList                   panels             = new ArrayList();
		
	/**
	 * Constructor.
	 * @param lyr Capa para la preview
	 * @param previewRender Gestor de la previsualización
	 */
	public MainPanel(FLyrRasterSE lyr, IPreviewRenderProcess previewRender) {
		this.lyr = lyr;
		this.renderProcess = previewRender;
	}
	
	/**
	 * Asigna un panel a los tabs.
	 * @param p JPanel para añadir a los tabs
	 */
	public void setPanel(JPanel p) {
		panels.add(p);
	}
	
	/**
	 * Inicialización de componentes gráficos. Se llama al
	 * terminar de añadir todos los paneles.
	 */
	public void initialize() {
		init();
		translate();
	}
	
	/**
	 * Inicializa los componentes gráficos
	 */
	protected void init() {
		setLayout(new BorderLayout());
		add(getPreviewBasePanel(), BorderLayout.CENTER);
		getPreviewBasePanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setVisible(false);
		getPreviewBasePanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setVisible(false);
		getPreviewBasePanel().getButtonsPanel().addButton("anterior", 14);
		getPreviewBasePanel().getButtonsPanel().addButton("siguiente", 13);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
	}
	
	/**
	 * Obtiene el panel con la previsualización
	 * @return HistogramPanel
	 */
	public PreviewBasePanel getPreviewBasePanel() {
		if (previewBasePanel == null) {
			ArrayList list = new ArrayList();
			list.add(panels.get(0));
			previewBasePanel = new PreviewBasePanel(list, null, null, renderProcess, lyr);
			previewBasePanel.setPreviewSize(new Dimension(230, 215));
		}
		return previewBasePanel;
	}
	
	/**
	 * Obtiene el panel de la posición indicada en el parámetro
	 * @param position
	 * @return JPanel
	 */
	public JPanel getPanel(int position) {
		return (JPanel)panels.get(position);
	}
	
	/**
	 * Obtiene el número de paneles de la serie
	 * @return 
	 */
	public int getPanelCount() {
		return panels.size();
	}
		
	/**
	 * Obtiene el panel con los paneles de selección de coordenadas para
	 * el recorte.
	 * @return ClipPanel
	 */
	public ClipPanel getCoordinatesSelectionPanel() {
		for (int i = 0; i < panels.size(); i++) {
			if(panels.get(i) instanceof ClipPanel)
				return (ClipPanel)panels.get(i);
		}
		return null;
	}
	
	/**
	 * Obtiene el panel con los controles para vectorizar
	 * @return VectorPanel
	 */
	public VectorPanel getVectorizationPanel() {
		for (int i = 0; i < panels.size(); i++) {
			if(panels.get(i) instanceof VectorPanel)
				return (VectorPanel)panels.get(i);
		}
		return null;
	}
	
	/**
	 * Obtiene el panel con los paneles de conversión a B/W
	 * @return GrayConversionPanel
	 */
	public GrayConversionPanel getGrayConversionPanel() {
		for (int i = 0; i < panels.size(); i++) {
			if(panels.get(i) instanceof GrayConversionPanel)
				return (GrayConversionPanel)panels.get(i);
		}
		return null;
	}

}

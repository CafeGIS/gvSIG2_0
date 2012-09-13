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
package org.gvsig.georeferencing.ui.options;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import org.gvsig.georeferencing.ui.launcher.AlgorithmSelectionPanel;
import org.gvsig.georeferencing.ui.launcher.OutFileSelectionPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;

/**
 * Panel general de opciones de georreferenciación. Consta de dos paneles principales, 
 * el de selección de algoritmo y el de selección de opciones.
 * 
 * 10/01/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GeorefOptionsPanel extends DefaultButtonsPanel {
	private static final long            serialVersionUID   = 1L;
	private int                          polynomialDegree;
	
	private AlgorithmSelectionPanel      algorithmPanel     = null;
	private CheckOptionsPanel            checkOptionsPanel  = null;
	private OutFileSelectionPanel        outFilePanel       = null;
	private CellSizeOptionsPanel         cellSizePanel      = null;
	
	/**
	 * Constructor
	 * @param viewList Lista de nombres de las vistas disponibles
	 * @param degreeList grado máximo para la georreferenciación polinomial
	 */
	public GeorefOptionsPanel(int polynomialDegree, ButtonsPanelListener listener) {
		this.polynomialDegree = polynomialDegree;
		
		//Ocultamos el botón de aplicar
		getButtonsPanel().addButtonPressedListener(listener);
		init();
	}
	
	/**
	 * Acciones de inicialización del panel
	 */
	public void init() {
		GridBagLayout gl = new GridBagLayout();
		setLayout(gl);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		
		add(getAlgorithmSelectionPanel(), gbc);
		
		gbc.gridy = 1;
		add(getOutFileSelectionPanel(), gbc);	
		
		gbc.gridy = 2;
		add(getCheckOptionsPanel(), gbc);
		
		gbc.gridy = 3;
		add(getCellSizePanel(), gbc);
	}
	
	/**
	 * Obtiene el panel de selección de algoritmo
	 * @return AlgorithmSelectionPanel
	 */
	public AlgorithmSelectionPanel getAlgorithmSelectionPanel() {
		if(algorithmPanel == null)
			algorithmPanel = new AlgorithmSelectionPanel(polynomialDegree);
		return algorithmPanel;
	}
	
	/**
	 * Obtiene el panel de selección de fichero
	 * @return FileSelectionPanel
	 */
	public CheckOptionsPanel getCheckOptionsPanel() {
		if(checkOptionsPanel == null)
			checkOptionsPanel = new CheckOptionsPanel();
		return checkOptionsPanel;
	}
	
	/**
	 * Obtiene el panel de selección de fichero de salida
	 * @return FileSelectionPanel
	 */
	public OutFileSelectionPanel getOutFileSelectionPanel() {
		if(outFilePanel == null)
			outFilePanel = new OutFileSelectionPanel();
		return outFilePanel;
	}
	
	/**
	 * Obtiene el panel del tamaño de celda
	 * @return CellSizeOptionsPanel
	 */
	public CellSizeOptionsPanel getCellSizePanel() {
		if(cellSizePanel == null)
			cellSizePanel = new CellSizeOptionsPanel();
		return cellSizePanel;
	}
}

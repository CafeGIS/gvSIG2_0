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
package org.gvsig.rastertools.saveraster.ui.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.gvsig.gui.beans.coordinatespanel.CoordinatesPanel;

import com.iver.andami.PluginServices;

/**
 * Panel para la selección de coordenadas reales.
 * 
 * 04/10/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class WCCoordsInputPanel extends JPanel {
	private static final long serialVersionUID = 2294445086139039312L;
	private CoordinatesPanel   coordinatesPanel   = null;
	
	/**
	 * Constructor. Crea los paneles y añadel el objeto CoordinatesPanel.
	 */
	public WCCoordsInputPanel() {
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "coordenadas_reales"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10), null));
		
		coordinatesPanel = new CoordinatesPanel();
		this.add(coordinatesPanel);
	}
	
	/**
	 * Obtiene el panel con las coordenadas de la selección en la vista
	 * @return
	 */
	public CoordinatesPanel getCoordinatesPanel() {
		return coordinatesPanel;
	}
}

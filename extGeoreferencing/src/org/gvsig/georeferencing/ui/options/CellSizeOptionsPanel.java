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
import java.awt.Insets;

import javax.swing.JPanel;

import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Panel de selección del tamaño de pixel en X e Y
 * 
 * 10/01/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class CellSizeOptionsPanel extends JPanel {
	private static final long     serialVersionUID    = 1L;
	
	private DataInputContainer    xCellSize           = null;
	private DataInputContainer    yCellSize           = null;
		
	/**
	 * Constructor. Asigna la lista de nombres de vistas para el selector. 
	 * @param viewList
	 */
	public CellSizeOptionsPanel() {
		init();
	}
	
	/**
	 * Acciones de inicialización del panel
	 */
	public void init() {	    
		GridBagLayout gl = new GridBagLayout();
		setLayout(gl);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "cellsize"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 5, 5, 0);
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		add(getXCellSizeTextField(), gbc);

		gbc.gridy = 1;
		add(getYCellSizeTextField(), gbc);
	}
	
	/**
	 * Obtiene el control para selección de tamaño de pixel en X
	 * @return DataInputContainer
	 */
	public DataInputContainer getXCellSizeTextField() {
		if(xCellSize == null) {
			xCellSize = new DataInputContainer();
			xCellSize.setLabelText("X ");
		}
		return xCellSize;
	}
	
	/**
	 * Obtiene el control para selección de tamaño de pixel en Y
	 * @return DataInputContainer
	 */
	public DataInputContainer getYCellSizeTextField() {
		if(yCellSize == null) {
			yCellSize = new DataInputContainer();
			yCellSize.setLabelText("Y ");
		}
		return yCellSize;
	}
	
	/**
	 * Obtiene el control para selección de tamaño de pixel en X
	 * @return DataInputContainer
	 */
	public double getXCellSizeValue() {
		try {
			return Double.valueOf(xCellSize.getValue()).doubleValue();
		} catch(NumberFormatException ex) {
			return -1;
		}
	}
	
	/**
	 * Obtiene el control para selección de tamaño de pixel en Y
	 * @return DataInputContainer
	 */
	public double getYCellSizeValue() {
		try {
			return Double.valueOf(yCellSize.getValue()).doubleValue();
		} catch(NumberFormatException ex) {
			return -1;
		}
	}
	
	/**
	 * Asigna el tamaño de pixel en X
	 * @param dataCellSize
	 */
	public void setXCellSize(double dataCellSize) {
		if(xCellSize != null) 
			xCellSize.setValue(dataCellSize + "");
	}
	
	/**
	 * Asigna el tamaño de pixel en Y
	 * @param dataCellSize
	 */
	public void setYCellSize(double dataCellSize) {
		if(yCellSize != null) 
			yCellSize.setValue(dataCellSize + "");
	}
		
}


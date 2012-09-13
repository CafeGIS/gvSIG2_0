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
package org.gvsig.rastertools.vectorizacion.clip.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.gui.beans.coordinatespanel.CoordinatesPanel;
import org.gvsig.raster.beans.previewbase.IUserPanelInterface;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.rastertools.clipping.ClippingData;
import org.gvsig.rastertools.vectorizacion.clip.ClipData;

/**
 * Panel con los controles de selección de área y coordenadas.
 * 
 * 09/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ClipPanel extends BasePanel implements ActionListener, IUserPanelInterface, Observer {
	private static final long         serialVersionUID       = 1L;
	
	private CoordinatesPanel          coordinatesPixel   = null;
	private CoordinatesPanel          coordinatesReales  = null;
	private SelectionAreaPanel        selectArea         = null;
	
	private JComboBox                 outputScale        = null;
	private JPanel                    outputScalePanel   = null;
	
	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public ClipPanel() {
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
		getRealCoordinates().setTitlePanel(getText(this, "coordenadas_reales"));
		getPixelCoordinates().setTitlePanel(getText(this, "coordenadas_pixel"));
	}
	
	/**
	 * Inicialización de los componentes gráficos
	 */
	protected void init() {
		setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		getPanel().add(getPixelCoordinates(), gridBagConstraints);

		gridBagConstraints.gridy = 1;
		add(getRealCoordinates(), gridBagConstraints);

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridy = 2;
		add(getSelectionAreaPanel(), gridBagConstraints);
		
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.insets = new Insets(5, 1, 5, 1);
		add(getOutputScalePanel(), gridBagConstraints);
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	public CoordinatesPanel getPixelCoordinates() {
		if (coordinatesPixel == null) {
			coordinatesPixel = new CoordinatesPanel();
		}
		return coordinatesPixel;
	}

	/**
	 * This method initializes jPanel1
	 * @return javax.swing.JPanel
	 */
	public CoordinatesPanel getRealCoordinates() {
		if (coordinatesReales == null) {
			coordinatesReales = new CoordinatesPanel();
		}
		return coordinatesReales;
	}
	
	/**
	 * Obtiene el panel con la selección de área
	 * @return
	 */
	public SelectionAreaPanel getSelectionAreaPanel() {
		if(selectArea == null)
			selectArea = new SelectionAreaPanel();
		return selectArea;
	}
	
	public void actionPerformed(ActionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getTitle()
	 */
	public String getTitle() {
		return RasterToolsUtil.getText(this, "selectarea");
	}
	
	/**
	 * Obtiene el panel con la escala de salida
	 * @return
	 */
	public JPanel getOutputScalePanel() {
		if(outputScalePanel == null) {
			outputScalePanel = new JPanel();
			outputScalePanel.setLayout(new BorderLayout());
			outputScalePanel.add(new JLabel(RasterToolsUtil.getText(this, "outputscale")), BorderLayout.WEST);
			outputScalePanel.add(getComboOutputScale(), BorderLayout.CENTER);
		}
		return outputScalePanel;
	} 
	
	/**
	 * Obtiene el combo con la escala de salida
	 * @return
	 */
	public JComboBox getComboOutputScale() {
		if(outputScale == null) {
			outputScale = new JComboBox();
		}
		return outputScale;
	}

	/**
	 * Actualiza los valores de los paneles cuando los datos de ClippingData varian
	 * @param o 
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		if(!(o instanceof ClipData))
			return;
		ClipData data = (ClipData)o;
		
		setEnableValueChangedEvent(false);
		String[] scales = data.getScales();
		getComboOutputScale().removeAllItems();
		for (int i = 0; i < scales.length; i++) 
			getComboOutputScale().addItem(scales[i]);
		
		getComboOutputScale().setSelectedIndex(data.getScaleSelected());
		getRealCoordinates().setValues(RasterUtilities.getCoord(data.getUlxWc(), data.getUlyWc(), data.getLrxWc(), data.getLryWc(), ClippingData.DEC));
		getPixelCoordinates().setValues(RasterUtilities.getCoord(data.getPxMinX(), data.getPxMinY(), data.getPxMaxX(), data.getPxMaxY(), ClippingData.DEC));
		setEnableValueChangedEvent(true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getPanel()
	 */
	public JPanel getPanel() {
		return this;
	}
}

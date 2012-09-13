/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.clipping.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.gvsig.gui.beans.buttonbar.ButtonBarContainer;
import org.gvsig.gui.beans.coordinatespanel.CoordinatesPanel;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.clipping.ui.listener.ClippingPanelListener;

import com.iver.andami.PluginServices;
/**
 * Pestaña donde se definirán las coordenadas del panel de recorte
 * 
 * @version 25/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ClippingCoordinatesPanel extends JPanel {
  private static final long serialVersionUID = -410791369185749893L;
	private CoordinatesPanel   coordinatesPixel   = null;
	private CoordinatesPanel   coordinatesReales  = null;
	private ButtonBarContainer buttonBarContainer = null;

  public ClippingCoordinatesPanel() {
  	initialize();
  }
  
  /**
   * Añade los listener a los controles
   * que lo necesiten 
   * @param list
   */
  public void setClippingListener(ClippingPanelListener clippingPanelListener) {
	  getPixelCoordinates().addValueChangedListener(clippingPanelListener);
	  getRealCoordinates().addValueChangedListener(clippingPanelListener);
	  getButtonBarContainer().getButton(0).addActionListener(clippingPanelListener);
	  getButtonBarContainer().getButton(1).addActionListener(clippingPanelListener);
	  getButtonBarContainer().getButton(2).addActionListener(clippingPanelListener);
	  getButtonBarContainer().getButton(3).addActionListener(clippingPanelListener);
	  getButtonBarContainer().getButton(4).addActionListener(clippingPanelListener);
	  
  }
  
  /**
   * Inicializa los componentes gráficos
   */
  private void initialize() {
		setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
		add(getPixelCoordinates(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 5, 2, 5);
		add(getRealCoordinates(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.insets = new Insets(2, 5, 5, 5);
		add(getButtonBarContainer(), gridBagConstraints);
  }
  

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	public CoordinatesPanel getPixelCoordinates() {
		if (coordinatesPixel == null) {
			coordinatesPixel = new CoordinatesPanel();
			coordinatesPixel.setTitlePanel(PluginServices.getText(this, "coordenadas_pixel")
);
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
			coordinatesReales.setTitlePanel(PluginServices.getText(this, "coordenadas_reales")
);
		}
		return coordinatesReales;
	}
	
	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	public ButtonBarContainer getButtonBarContainer() {
		if (buttonBarContainer == null) {
			buttonBarContainer = new ButtonBarContainer();
			buttonBarContainer.addButton("export.png", RasterToolsUtil.getText(this, "load_parameters"), 0);
			buttonBarContainer.addButton("import.png", RasterToolsUtil.getText(this, "save_parameters"), 1);
			buttonBarContainer.addButton("rois.png", RasterToolsUtil.getText(this, "adjust_to_rois"), 2);
			buttonBarContainer.addButton("fullExtent.png", RasterToolsUtil.getText(this, "fullExtent"), 3);
			buttonBarContainer.addButton("selectTool.png", RasterToolsUtil.getText(this, "select_tool"), 4);
			buttonBarContainer.setButtonAlignment("right");
			buttonBarContainer.setComponentBorder(false);
			buttonBarContainer.setBorder(BorderFactory.createLineBorder(new Color(189, 190, 176)));
		}
		return buttonBarContainer;
	}
	
	/**
	 * Asigna los valores de las coordenadas reales.
	 * @param values
	 */
	public void setCoordReal(String[] values) {
		getRealCoordinates().setValues(values);
	}
	
	/**
	 * Asigna los valores de las coordenadas píxel.
	 * @return
	 */
	public void setCoordPixel(String[] values) {
		getPixelCoordinates().setValues(values);
	}
}

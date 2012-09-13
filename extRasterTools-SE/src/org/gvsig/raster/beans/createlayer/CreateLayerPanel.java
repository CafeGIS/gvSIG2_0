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
package org.gvsig.raster.beans.createlayer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;

import com.iver.andami.PluginServices;
/**
 * Panel para unificar las propiedades de generacion de capas nuevas en ficheros
 * o solo en visualizacion.
 * 
 * @version 10/03/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class CreateLayerPanel {
	private static final long serialVersionUID = 3921564127360827156L;
	private JPanel        panel         = null;
	private JRadioButton  jRBOnlyView   = null;
	private JRadioButton  jRBNewLayer   = null;
	private NewLayerPanel panelNewLayer = null;
	private FLyrRasterSE  lyr           = null;

	/**
	 * Constructor de un CreateLayerPanel
	 */
	public CreateLayerPanel(FLyrRasterSE lyr) {
		this.lyr = lyr;
		initialize();
		translate();
	}
	
	/**
	 * Devuelve el panel principal
	 * @return
	 */
	public JPanel getJPanel() {
		if (panel == null) {
			panel = new JPanel();
		}
		return panel;
	}

	/**
	 * Seccion donde irán todas las traducciones invariables del componente
	 */
	private void translate() {
		getRadioOnlyView().setText(PluginServices.getText(this, "solo_visualizacion"));
		getRadioNewLayer().setText(PluginServices.getText(this, "capa_nueva"));
	}
	
	/**
	 * Inicializar el panel de CreateLayer
	 */
	private void initialize() {
		ButtonGroup buttonGroup1;
		GridBagConstraints gridBagConstraints;

		getJPanel().setLayout(new GridBagLayout());
		
		buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(getRadioOnlyView());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		getJPanel().add(getRadioOnlyView(), gridBagConstraints);

		buttonGroup1.add(getRadioNewLayer());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		getJPanel().add(getRadioNewLayer(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		getJPanel().add(getPanelNewLayer().getJPanel(), gridBagConstraints);
	}
	
	public NewLayerPanel getPanelNewLayer() {
		if (panelNewLayer == null)
			panelNewLayer = new NewLayerPanel(lyr);
		return panelNewLayer;
	}
	
	/**
	 * Poner los estados de los RadioButton en caso de que cambien de valor
	 * @param evt
	 */
	private void jRBNewLayerStateChanged(ItemEvent evt) {
		if (getRadioNewLayer().getSelectedObjects() != null) {
			getPanelNewLayer().getRadioFileGenerate().setEnabled(true);
			getPanelNewLayer().getRadioOpenMemory().setEnabled(true);
			if (getPanelNewLayer().getRadioOpenMemory().getSelectedObjects() != null)
				getPanelNewLayer().setFilenameEnabled(true);
			else
				getPanelNewLayer().setFilenameEnabled(false);
		} else {
			getPanelNewLayer().getRadioFileGenerate().setEnabled(false);
			getPanelNewLayer().getRadioOpenMemory().setEnabled(false);
			getPanelNewLayer().setFilenameEnabled(false);
		}
	}

	/**
	 * Especifica si se generara solo en la vista o se guardara en un fichero
	 * @param enabled
	 */
	public void setOnlyView(boolean enabled) {
		getRadioOnlyView().setSelected(enabled);
	}

	/**
	 * Devuelve el JRadioButton de Solo vista
	 * @return
	 */
	private JRadioButton getRadioOnlyView() {
		if (jRBOnlyView == null) {
			jRBOnlyView = new JRadioButton();
			jRBOnlyView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jRBOnlyView.setMargin(new Insets(0, 0, 0, 0));
		}
		return jRBOnlyView;
	}
	
	/**
	 * Devuelve el JRadioButton de nueva capa
	 * @return
	 */
	private JRadioButton getRadioNewLayer() {
		if (jRBNewLayer == null) {
			jRBNewLayer = new JRadioButton();
			jRBNewLayer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jRBNewLayer.setMargin(new Insets(0, 0, 0, 0));
			jRBNewLayer.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					jRBNewLayerStateChanged(evt);
				}
			});
			jRBNewLayer.setSelected(true);
		}
		return jRBNewLayer;
	}
	
	/**
	 * This method initializes jPNameFile
	 *
	 * @return javax.swing.JPanel
	 */
	public JPanel getFileNamePanel() {
		return getPanelNewLayer().getFileNamePanel();
	}
	
	/**
	 * Establece el texto de la etiqueta del nombre de fichero
	 * @param text
	 */
	public void setLabelFilename(String text) {
		getPanelNewLayer().setLabelFilename(text);
	}
	
	public boolean isNewLayerSelected() {
		return getRadioNewLayer().isSelected();
	}

	public boolean isOnlyViewSelected() {
		return getRadioOnlyView().isSelected();
	}
	
	/**
	 * Asigna un valor para el parámetro que informa de si el raster de salida hay
	 * que comprimirlo o no. Este valor es necesario cuando el raster de salida 
	 * es mayor de 4G ya que no se puede crear un tiff tan grande.
	 * @param compress true para comprimir el raster de salida y false para no hacerlo.
	 */
	public void setCompress(boolean compress) {
		getPanelNewLayer().setCompress(compress);
	}
	
	/**
	 * Devuelve la ruta del fichero donde se va a guardar, en caso de guardarse
	 * en memoria, calcula el nombre sin preguntar y devuelve la ruta.
	 * @return
	 */
	public String getFileSelected() {
		return getPanelNewLayer().getFileSelected();
	}
	
	public void updateNewLayerText() {
		getPanelNewLayer().updateNewLayerText();
	}
}
/*
* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
*
* For more information, contact:
*
*  Generalitat Valenciana
*   Conselleria d'Infraestructures i Transport
*   Av. Blasco Ibañez, 50
*   46010 VALENCIA
*   SPAIN
*
*      +34 963862235
*   gvsig@gva.es
*      www.gvsig.gva.es
*
*    or
*
*   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
*   Campus Universitario s/n
*   02071 Alabacete
*   Spain
*
*   +34 967 599 200
*/

package org.gvsig.remotesensing.imagefusion.gui.components;

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
import org.gvsig.raster.beans.createlayer.NewLayerPanel;

import com.iver.andami.PluginServices;
/**
* Panel para unificar las propiedades de generacion de capas nuevas en ficheros
* o solo en visualizacion.
* 
* @version 10/03/2008
* @author aMuÑoz (alejandro.munoz@uclm.es)
*/
public class CreateOptionsFusionPanel {
	private static final long serialVersionUID = 3921564127360827156L;
	private JPanel        panel          = null;
	private JRadioButton  jAsignedBand   = null;
	private JRadioButton  jAllBand       = null;
	private NewLayerPanel panelNewLayer  = null;
	private FLyrRasterSE  layer          = null;

	/**
	 * Constructor de un CreateLayerPanel
	 */
	public CreateOptionsFusionPanel(FLyrRasterSE lyr) {
		this.layer = lyr;
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
		getRadioAsignedBand().setText(PluginServices.getText(this, "bandas_visualizacion"));
		getRadioAllBand().setText(PluginServices.getText(this, "all_bands"));
	}
	
	private void initialize() {
		ButtonGroup buttonGroup1;
		GridBagConstraints gridBagConstraints;

		getJPanel().setLayout(new GridBagLayout());
		
		buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(getRadioAsignedBand());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		getJPanel().add(getRadioAsignedBand(), gridBagConstraints);

		buttonGroup1.add(getRadioAllBand());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		getJPanel().add(getRadioAllBand(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);
		getJPanel().add(getPanelNewLayer().getJPanel(), gridBagConstraints);
	}
	
	public NewLayerPanel getPanelNewLayer() {
		if (panelNewLayer == null)
			panelNewLayer = new NewLayerPanel(layer);
		return panelNewLayer;
	}
	
	/**
	 * Poner los estados de los RadioButton en caso de que cambien de valor
	 * @param evt
	 */
	private void jRBNewLayerStateChanged(ItemEvent evt) {
		
	}

	/**
	 * Especifica si se generara solo en la vista o se guardara en un fichero
	 * @param enabled
	 */
	public void setOnlyView(boolean enabled) {
		getRadioAsignedBand().setSelected(enabled);
	}

	/**
	 * Devuelve el JRadioButton de Solo vista
	 * @return
	 */
	public JRadioButton getRadioAsignedBand() {
		if (jAsignedBand == null) {
			jAsignedBand = new JRadioButton();
			jAsignedBand.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jAsignedBand.setMargin(new Insets(0, 0, 0, 0));
			jAsignedBand.setSelected(true);
		}
		return jAsignedBand;
	}
	
	/**
	 * Devuelve el JRadioButton de nueva capa
	 * @return
	 */
	public JRadioButton getRadioAllBand() {
		if (jAllBand == null) {
			jAllBand = new JRadioButton();
			jAllBand.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jAllBand.setMargin(new Insets(0, 0, 0, 0));
			jAllBand.setEnabled(false);
			jAllBand.setSelected(false);
			jAllBand.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					jRBNewLayerStateChanged(evt);
				}
			});
			jAllBand.setSelected(true);
		}
		return jAllBand;
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
		return getRadioAllBand().isSelected();
	}

	public boolean isOnlyViewSelected() {
		return getRadioAsignedBand().isSelected();
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
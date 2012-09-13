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

package org.gvsig.remotesensing.imagefusion.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.beans.previewbase.IPreviewRenderProcess;
import org.gvsig.raster.beans.previewbase.PreviewBasePanel;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.remotesensing.imagefusion.gui.components.CreateOptionsFusionPanel;

import com.iver.andami.PluginServices;

/**
 * Panel que compone el dialogo para la fusion de imagenes.
 *
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 */
public class FusionPanel extends JPanel implements IPreviewRenderProcess {
	private static final long serialVersionUID = 7152780112689637266L;

	private PreviewBasePanel    previewBasePanel    = null;
	private FLayer              layer               = null;
	private FusionListener      fusionListener      = null;
	private String              viewName            = null;
	private JPanel              jPanelOptions       = null;
	private FusionMainPanel     fusionMain          = null;
	private FusionDialog        fusionDialog        = null;
	private CreateOptionsFusionPanel    newLayerPanel       = null;

	/**
	 * Constructor
	 * @param width Ancho del panel
	 * @param height Alto del panel
	 */
	public FusionPanel(FLayer layer, FusionDialog filterDialog) {
		this.fusionDialog = filterDialog;
		setLayer(layer);
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		add(getPreviewBasePanel(), BorderLayout.CENTER);
	}

	/**
	 * Obtiene el panel con el histograma
	 * @return HistogramPanel
	 */
	private PreviewBasePanel getPreviewBasePanel() {
		if (previewBasePanel == null) {
			ArrayList list = new ArrayList();
			list.add(getMainPanel());
			previewBasePanel = new PreviewBasePanel(list, null, panelOptions(), this, (FLyrRasterSE) layer);
			previewBasePanel.setPreviewSize(new Dimension(230, 215));
			previewBasePanel.addButtonPressedListener(fusionDialog);
		}
		return previewBasePanel;
	}

	/**
	 * Devuelve el componente <code>fusionListener</code>, que contendrá el
	 * proceso en si del panel
	 */
	private FusionListener getFusionListener() {
		if (fusionListener == null) {
			fusionListener = new FusionListener(this);
		}
		return fusionListener;
	}

	/**
	 * Devuelve el panel de Opciones, en caso de no existir, lo crea.
	 * @return
	 */
	private JPanel panelOptions() {
		if (jPanelOptions == null) {
			jPanelOptions = new JPanel();
			GridBagConstraints gridBagConstraints;
			jPanelOptions.setLayout(new GridBagLayout());
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 4;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			jPanelOptions.add(getNewLayerPanel().getJPanel(), gridBagConstraints);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 5;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			jPanelOptions.add(new JPanel(), gridBagConstraints);
		}
		return jPanelOptions;
	}

	/**Panel con las opciones de seleccion de bandas para la fusion*/
	public CreateOptionsFusionPanel getNewLayerPanel() {
		if (newLayerPanel == null) {
			newLayerPanel = new CreateOptionsFusionPanel((FLyrRasterSE)layer);
		}
		return newLayerPanel;
	}


	/**
	 * Definir el FLayer del panel, haciendo todas las cargas necesarias despues
	 * de especificarlo.
	 * @param layer
	 */
	private void setLayer(FLayer layer) {
		if (layer == null)
			return;
		this.layer = layer;
		getPreviewBasePanel().setLayer((FLyrRasterSE) layer);
	}

	public FusionMainPanel getMainPanel() {
		if (fusionMain == null) {
			fusionMain = new FusionMainPanel(getFusionListener());
			JPanel panel = getNewLayerPanel().getFileNamePanel();
			panel.setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "nombre_capa"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			fusionMain.getCentralPanel().add(panel, BorderLayout.SOUTH);
		}
		return fusionMain;
	}

	/**
	 * Volvemos todo a la normalidad cuando se cancela
	 */
	public void cancel() {
		if (layer != null)
			layer.getMapContext().invalidate();
	}

	/**
	 * Cuando se aceptan los cambios en el panel ejecutaremos el aceptar del
	 * listener
	 */
	public void accept() {
		fusionListener.accept();
	}


	/**
	 * Actualizamos la vista previa
	 */
	public void refreshPreview() {
		getPreviewBasePanel().refreshPreview();
	}

	/**
	 * Devuelve el FLayer asignado al panel
	 * @return
	 */
	public FLayer getLayer() {
		return layer;
	}

	/**
	 * Obtiene el nombre de la vista
	 * @return
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * Especificar el nombre de la nueva capa para el recuadro de texto asignándo
	 * en cada llamada un nombre consecutivo.
	 */
	public void updateNewLayerText() {
		getNewLayerPanel().updateNewLayerText();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IPreviewRenderProcess#process(org.gvsig.raster.hierarchy.IRasterRendering)
	 */
	public void process(IRasterRendering rendering) throws FilterTypeException {
		//getFusionListener().drawImage(rendering);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IPreviewRenderProcess#isShowPreview()
	 */
	public boolean isShowPreview() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IPreviewRenderProcess#setShowPreview(boolean)
	 */
	public void setShowPreview(boolean showPreview) {
	}
}
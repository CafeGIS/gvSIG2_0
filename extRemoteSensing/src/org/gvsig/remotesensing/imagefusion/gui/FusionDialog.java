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

import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.raster.RasterLibrary;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Dialogo para la fusión de imagenes de distinta resolucion espacial
 *
 * @version 25/02/2008
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 */

public class FusionDialog extends JPanel implements IWindow, IWindowListener, ButtonsPanelListener {

	private static final long serialVersionUID = 818691082984915388L;
	private FLayer      layer       = null;
	private FusionPanel fusionPanel = null;

	/**
	 * Controla si se ha presionado el boton aceptar para el cerrado de la ventana
	 */
	private boolean     accepted    = false;

	/**
	 * Constructor
	 * @param width Ancho
	 * @param height Alto
	 */
	public FusionDialog(FLayer layer, int width, int height) {
		this.layer = layer;
		setSize(width, height);
		setLayout(new BorderLayout(5, 5));
		add(getFusionPanel(), java.awt.BorderLayout.CENTER);
	}


	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG);
		if (layer != null)
			m_viewinfo.setAdditionalInfo(layer.getName());
		m_viewinfo.setTitle(PluginServices.getText(this, "fusion"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}


	/**
	 * Obtiene el panel para la fusion
	 * @return
	 */
	private FusionPanel getFusionPanel() {
		if (fusionPanel == null) {
			fusionPanel = new FusionPanel(layer, this);
		}
		return fusionPanel;
	}

	/**
	 * Acciones a ejecutar cuando se cancela
	 */
	private void close() {
		try {
			RasterLibrary.removeOnlyLayerNameListener(getFusionPanel().getNewLayerPanel().getPanelNewLayer());
			PluginServices.getMDIManager().closeWindow(this);
		} catch (ArrayIndexOutOfBoundsException e) {
			//Si la ventana no se puede eliminar no hacemos nada
		}
	}


	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowClosed()
	 */
	public void windowClosed() {
		if (!accepted) {
			getFusionPanel().cancel();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {

		// Al pulsar Aceptar o Aplicar se ejecuta el aceptar del panel
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY || e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			getFusionPanel().accept();
		}

		// Al pulsar Cancelar la ventana se cierra y se refresca la vista
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			getFusionPanel().cancel();
			close();
		}

		// Al pulsar Aceptar simplemente la ventana se cierra
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			accepted = true;
			close();
		}
	}


	public void windowActivated() {}


	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}
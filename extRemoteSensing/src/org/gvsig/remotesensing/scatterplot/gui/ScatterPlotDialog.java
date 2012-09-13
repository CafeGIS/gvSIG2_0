/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
*   Av. Blasco Ibáñez, 50
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

package org.gvsig.remotesensing.scatterplot.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.raster.grid.GridException;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
* Dialogo para el grafico de dispersion.
*
* @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
* @version 11/12/2007
*
**/

public class ScatterPlotDialog extends JPanel implements IWindow, IWindowListener, ButtonsPanelListener {

	private static final long serialVersionUID = 2847035927527203595L;
	private ScatterPlotPanel scatterPlotPanel = null;
	private MapControl mapControl = null;
	private String previousTool = null;


	/**
	 * Constructor
	 * @param width Ancho del panel
	 * @param height Alto del panel
	 */
	public ScatterPlotDialog(int width, int height) {
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		this.add(getScatterPlotPanel(), BorderLayout.CENTER);
		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		mapControl = view.getMapControl();
		previousTool = mapControl.getCurrentTool();

	}


	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.PALETTE | WindowInfo.RESIZABLE);
		if(scatterPlotPanel.getLayer() != null)
			m_viewinfo.setAdditionalInfo(scatterPlotPanel.getLayer().getName());
		m_viewinfo.setTitle(PluginServices.getText(this, "diagrama_dispersion")+" - "+m_viewinfo.getAdditionalInfo());
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
		// Al pulsar Cancelar la ventana se cierra y se refresca la vista
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			close();
		}

		// Al pulsar Aceptar simplemente la ventana se cierra
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			getScatterPlotPanel().aplicar();
		}
	}

	/**
	 * Acciones a ejecutar cuando se cancela
	 */
	public void close() {
		try {
			PluginServices.getMDIManager().closeWindow(this);
		} catch (ArrayIndexOutOfBoundsException e) {
			//Si la ventana no se puede eliminar no hacemos nada
		}
	}

	/**
	 * Se asigna el layer sobre el que esta asociado el diagrama
	 * @param layer
	 * @throws GridException
	 */
	public void setLayer(FLayer layer) throws GridException {
		getScatterPlotPanel().setFLayer(layer);
	}


	/** Recupera la herramienta previa seleccionada */
	public void setPreviousTool(){
		if (previousTool!=null)
			getScatterPlotPanel().getMapControl().setTool(previousTool);
	}

	public void windowActivated() {

	}

	public void windowClosed() {
		GraphicLayer graphicLayer = mapControl.getMapContext().getGraphicsLayer();
		graphicLayer.clearAllGraphics();
		graphicLayer.clearSymbolsGraphics();
		getScatterPlotPanel().getMapControl().rePaintDirtyLayers();
		setPreviousTool();
	}


	/**
	 *  @return scatterplotpanel
	 * */
	public ScatterPlotPanel getScatterPlotPanel() {
		if (scatterPlotPanel == null){
			scatterPlotPanel = new ScatterPlotPanel(this);
			scatterPlotPanel.addButtonPressedListener(this);
		}
		return scatterPlotPanel;
	}


	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}
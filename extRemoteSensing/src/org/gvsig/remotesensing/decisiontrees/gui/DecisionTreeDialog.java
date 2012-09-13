/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package org.gvsig.remotesensing.decisiontrees.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Diálogo para la herramienta de árboles de decisión.
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class DecisionTreeDialog extends JPanel implements ButtonsPanelListener,
		IWindow, IWindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8876811061688760990L;
	
	private DecisionTreePanel 		decisionTreePanel 	= null;
	private View 					view 				= null;


	/**
	 * Constructor
	 * @param width Ancho del panel
	 * @param height Alto del panel
	 */
	public DecisionTreeDialog(int width, int height, View view) {
		this.setSize(width, height);
		this.view = view;
		this.setLayout(new BorderLayout());		
		this.add(getDecisionTreePanel(), BorderLayout.CENTER);
	}
	
	public void actionButtonPressed(ButtonsPanelEvent e) {
		if (e.getButton() == ButtonsPanel.BUTTON_CLOSE) {
			close();
		}
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		m_viewinfo.setTitle(PluginServices.getText(this, "arboles_decision"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}

	public void windowActivated() {
		// TODO Auto-generated method stub

	}

	public void windowClosed() {
		// TODO Auto-generated method stub

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

	public DecisionTreePanel getDecisionTreePanel() {
		if (decisionTreePanel ==null)
			decisionTreePanel = new DecisionTreePanel(this, view);
		return decisionTreePanel;
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}

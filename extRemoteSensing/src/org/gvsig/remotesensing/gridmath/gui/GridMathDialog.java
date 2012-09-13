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

package org.gvsig.remotesensing.gridmath.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Diálogo para la calculadora de bandas.
 * 
 * @author Alejandro Muñoz Sanchez	(alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 19/10/2007 
 */
 
public class GridMathDialog extends JPanel implements IWindow, ButtonsPanelListener{

	private static final long serialVersionUID = -8676017674682458513L;
	
	private View view = null;
	private GridMathPanel gridMathPanel = null;
	private int width=630;
	private int height=300;

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG);
		m_viewinfo.setWidth(width);
		m_viewinfo.setHeight(height);
		m_viewinfo.setTitle(PluginServices.getText(this,"band_math"));
		m_viewinfo.setX(300);
		return m_viewinfo;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		if (e.getButton() == ButtonsPanel.BUTTON_CLOSE) {
			try {
				PluginServices.getMDIManager().closeWindow(this);
			} catch (ArrayIndexOutOfBoundsException ex) {
				// Si la ventana no se puede eliminar no hacemos nada
			}
		}
	}

	/**
	 * Constructor
	 * @param view vista actual
	 */
	public GridMathDialog(View view) {
		this.view = view;
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(new BorderLayout(3, 3));
		this.add(getGridMathPanel(), java.awt.BorderLayout.CENTER);
	}
	
	
	/**
	 * @return CalculatorPanel del dialogo
	 */
	public GridMathPanel getGridMathPanel() {
		if (gridMathPanel==null){
			gridMathPanel = new GridMathPanel(this,view);
			gridMathPanel.addButtonPressedListener(this);
		}
		return gridMathPanel;
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.vectorizacion;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Dialogo para el preprocesado del raster antes de la aplicación de la vectorización.
 * 
 * 12/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class MainDialog extends JPanel implements IWindow, ButtonsPanelListener {
	private static final long            serialVersionUID = -5374834293534046986L;
	private String                       name             = null;
	private MainPanel                    panel            = null;

	/**
	 * Constructor
	 * @param width Ancho
	 * @param height Alto
	 */
	public MainDialog(int width, int height, String name, MainPanel mainPanel) {
		this.name = name;
		this.panel = mainPanel;
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(new BorderLayout(5, 5));
		this.add(mainPanel, java.awt.BorderLayout.CENTER);
		mainPanel.getPreviewBasePanel().addButtonPressedListener(this);
	}

	/**
	 * Obtiene el panel principal
	 * @return HistogramPanel
	 */
	public MainPanel getMainPanel() {
		return panel;
	}


	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		if (name != null)
			m_viewinfo.setAdditionalInfo(name);
		m_viewinfo.setTitle(PluginServices.getText(this, "vectorization"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}

	/**
	 * Acciones a ejecutar cuando se cancela
	 */
	public void close() {
		try {
			PluginServices.getMDIManager().closeWindow(this);
		} catch (ArrayIndexOutOfBoundsException e) {
			// Si la ventana no se puede eliminar no hacemos nada
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			close();
		}
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}
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
package org.gvsig.raster.gui.wizards.projection;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
/**
 * Dialogo de opciones de sobre la proyección de la capa raster y la vista.
 *
 * @version 07/04/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class RasterProjectionActionsDialog extends JPanel implements IWindow, IWindowListener, ActionListener {
	private static final long            serialVersionUID = 6954391896451933337L;
	private RasterProjectionActionsPanel panel            = null;
	private Point                        posWindow        = null;
	private int                          widthWindow      = 390;
	private int                          heightWindow     = 250;
	private FLyrRasterSE                 lyr              = null;

	/**
	 * Constructor.
	 */
	public RasterProjectionActionsDialog(FLyrRasterSE lyr) {
		this.lyr = lyr;
		BorderLayout bl = new BorderLayout();
		bl.setHgap(2);
		bl.setVgap(2);
		setLayout(bl);
		add(getRasterProjectionActionsPanel(), BorderLayout.CENTER);
		getRasterProjectionActionsPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).addActionListener(this);
		getRasterProjectionActionsPanel().getButtonsPanel().getButton(ButtonsPanel.BUTTON_CANCEL).addActionListener(this);
		getRasterProjectionActionsPanel().getCheckOption().addActionListener(this);
		posWindow = RasterToolsUtil.iwindowPosition(widthWindow, heightWindow);
		PluginServices.getMDIManager().addWindow(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		m_viewinfo.setTitle(PluginServices.getText(this, "options"));
		m_viewinfo.setHeight(heightWindow);
		m_viewinfo.setWidth(widthWindow);
		if (posWindow != null) {
			m_viewinfo.setX((int) posWindow.getX());
			m_viewinfo.setY((int) posWindow.getY());
		}
		return m_viewinfo;
	}

	/**
	 * Obtiene el panel con las opciones de proyección
	 * @return RasterProjectionActionsPanel
	 */
	public RasterProjectionActionsPanel getRasterProjectionActionsPanel() {
		if (panel == null) {
			panel = new RasterProjectionActionsPanel(lyr);
		}
		return panel;
	}

	/**
	 * Obtiene la selección del panel
	 * @return entero con la selección. Esta representada por las constantes de la
	 *         clase RasterReprojectionPanel.
	 */
	public int getSelection() {
		return getRasterProjectionActionsPanel().getSelection();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getRasterProjectionActionsPanel().getCheckOption()) {
			RasterProjectionActionsPanel.selectAllFiles = getRasterProjectionActionsPanel().getCheckOption().isSelected();
			return;
		}
		PluginServices.getMDIManager().closeWindow(this);
	}

	public void windowClosed() {}

	public void windowActivated() {}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}
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
package org.gvsig.rastertools.clipping.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.raster.RasterLibrary;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
/**
 * <code>ClippingDialog</code>. Creación de la ventana de recorte para gvSIG.
 *
 * @version 17/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ClippingDialog extends JPanel implements IWindow, ButtonsPanelListener {
	private static final long  serialVersionUID = -5374834293534046986L;
	private String             name             = null;

	/**
	 * Panel de recortado de imagen que está en la libreria raster
	 */
	private ClippingPanel clippingPanel = null;

	/**
	 * Constructor
	 * @param width Ancho
	 * @param height Alto
	 */
	public ClippingDialog(int width, int height, String name) {
		this.name = name;
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(new BorderLayout(5, 5));
		this.add(getClippingPanel(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * Obtiene el panel con el histograma
	 * @return HistogramPanel
	 */
	public ClippingPanel getClippingPanel() {
		if (clippingPanel == null) {
			clippingPanel = new ClippingPanel(this);
			clippingPanel.addButtonPressedListener(this);
		}
		return clippingPanel;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG);
		if (name != null)
			m_viewinfo.setAdditionalInfo(name);
		m_viewinfo.setTitle(PluginServices.getText(this, "recorte"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}

	/**
	 * Acciones a ejecutar cuando se cancela
	 */
	public void close() {
		try {
			RasterLibrary.removeOnlyLayerNameListener(getClippingPanel().getOptionsPanel());
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
		if (e.getButton() == ButtonsPanel.BUTTON_CLOSE) {
			close();
		}
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}
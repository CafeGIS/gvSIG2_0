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
package org.gvsig.rastertools.colortable.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
/**
* <code>ColorTableDialog</code>. Creación de la ventana de ColorTable para gvSIG.
*
* @version 17/04/2007
* @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
*/
public class ColorTableDialog extends JPanel implements IWindow, IWindowListener {
	private static final long serialVersionUID = -5374834293534046986L;
	private FLayer layer = null;

	/**
	 * Panel de recortado de imagen que está en la libreria raster
	 */
	private ColorTablePanel colorTablePanel = null;
	
	/**
	 * Constructor
	 * @param width Ancho
	 * @param height Alto
	 */
	public ColorTableDialog(FLayer layer, int width, int height) {
		this.layer = layer;
		setPreferredSize(new Dimension(width, height));
		setSize(width, height);
		setLayout(new BorderLayout(5, 5));
		add(getColorTablePanel(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * Obtiene el panel con el histograma
	 * @return HistogramPanel
	 */
	private ColorTablePanel getColorTablePanel() {
		if (colorTablePanel == null) {
			colorTablePanel = new ColorTablePanel(layer, this);
		}
		return colorTablePanel;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		if(getColorTablePanel().getLayer() != null)
			m_viewinfo.setAdditionalInfo(getColorTablePanel().getLayer().getName());
		m_viewinfo.setTitle(PluginServices.getText(this, "tablas_color"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowClosed()
	 */
	public void windowClosed() {
		getColorTablePanel().windowClosed();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowActivated()
	 */
	public void windowActivated() {}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}
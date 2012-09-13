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
package org.gvsig.rastertools.saveraster.ui.properties;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.gvsig.gui.beans.propertiespanel.PropertiesPanel;
import org.gvsig.raster.dataset.Params;
import org.gvsig.rastertools.saveraster.ui.listener.WriterPropertiesListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Ventana para el dialogo de propiedades del driver de escritura.
 * 
 * @version 24/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class WriterPropertiesDialog extends JPanel implements IWindow {
	private static final long serialVersionUID = 1L;
	private PropertiesPanel pd = null;
	private WriterPropertiesListener listener = null;
	
	/**
	 * Constructor. Asigna el panel de propiedades.
	 * @param pd PropertiesDialog
	 */
	public WriterPropertiesDialog(PropertiesPanel pd, Params params) {
		this.pd = pd;
		listener = new WriterPropertiesListener(this, params);
		pd.addButtonPressedListener(listener);
		setSize(330, 225);
		this.setLayout(new BorderLayout());
		add(pd, BorderLayout.CENTER);
	}
	
	/**
	 * Obtiene el componente panel de propiedades
	 * @return PropertiesPanel
	 */
	public PropertiesPanel getPropertiesPanel() {
		return pd;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE);
		m_viewinfo.setTitle(PluginServices.getText(this, "properties"));
		return m_viewinfo;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}

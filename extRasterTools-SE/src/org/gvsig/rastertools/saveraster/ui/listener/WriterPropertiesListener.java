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
package org.gvsig.rastertools.saveraster.ui.listener;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.propertiespanel.PropertiesComponent;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.saveraster.ui.properties.WriterPropertiesDialog;

import com.iver.andami.PluginServices;

/**
 * Listener para el dialogo de propiedades de los escritores.
 * 
 * @version 24/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class WriterPropertiesListener implements ButtonsPanelListener {

	private WriterPropertiesDialog dialog = null;
	private Params params = null;
	private PropertiesComponent pComp = null;
	
	/**
	 * Constructor
	 * @param dialog
	 */
	public WriterPropertiesListener(WriterPropertiesDialog dialog, Params params) {
		this.dialog = dialog;
		this.params = params;
		this.pComp = dialog.getPropertiesPanel().getPropertiesComponent();
		RasterToolsUtil.loadPropertiesFromWriterParams(pComp, params, null);
	}
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		switch (e.getButton()) {
		case ButtonsPanel.BUTTON_APPLY:
			RasterToolsUtil.loadWriterParamsFromPropertiesPanel(pComp, params);
			break;
		case ButtonsPanel.BUTTON_ACCEPT:
			RasterToolsUtil.loadWriterParamsFromPropertiesPanel(pComp, params);
		case ButtonsPanel.BUTTON_CANCEL:
			PluginServices.getMDIManager().closeWindow(dialog);
			break;
		}
		
	}

}

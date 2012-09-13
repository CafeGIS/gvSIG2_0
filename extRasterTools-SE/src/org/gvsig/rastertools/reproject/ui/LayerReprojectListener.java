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
package org.gvsig.rastertools.reproject.ui;

import java.io.File;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.UniqueProcessQueue;
import org.gvsig.raster.util.RasterNotLoadException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.reproject.ReprojectProcess;
import org.gvsig.rastertools.saveraster.ui.info.EndInfoDialog;
/**
 * Listener del panel de selección de proyección.
 * 
 * 29/04/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class LayerReprojectListener implements ButtonsPanelListener, IProcessActions {
	private LayerReprojectPanel dialog       = null;
	private FLyrRasterSE        lyr          = null;
	private Boolean             isInTOC     = Boolean.FALSE;
	
	/**
	 * Constructor. Asigna el dialogo y la capa y registra los listener.
	 * @param lyr Capa
	 * @param dialog Dialogo
	 */
	public LayerReprojectListener(LayerReprojectPanel dialog) {
		this.dialog = dialog;
	}

	/**
	 * Se asigna la capa al listener
	 * @param lyr
	 */
	public void setLayer(FLyrRasterSE lyr) {
		this.lyr = lyr;
	}

	/**
	 * Se especifica si hay que cerrar la capa de origen cuando acabe de reproyectar
	 * @param lyr
	 */
	public void setIsInTOC(Boolean isInTOC) {
		this.isInTOC = isInTOC;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			RasterToolsUtil.closeWindow(dialog);
			return;
		}
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			String path = null;
			path = dialog.getNewLayerPanel().getFileSelected();
			if (path == null)
				return;
			
			RasterProcess process = new ReprojectProcess();
			process.addParam("layer", lyr);
			process.addParam("path", path);
			process.addParam("projection", dialog.getProjectionDst());
			process.addParam("srcprojection", dialog.getProjectionSrc());
			process.addParam("isintoc", isInTOC);
			process.setActions(this);
			
			UniqueProcessQueue queue = UniqueProcessQueue.getSingleton();
			queue.add(process);
			
			RasterToolsUtil.closeWindow(dialog);
		}
	}

	/**
	 * Acciones que se realizan al finalizar de crear los recortes de imagen.
	 * Este método es llamado por el thread TailRasterProcess al finalizar.
	 */
	public void loadLayerInToc(String fileName, Long milis) {
		try {
			RasterToolsUtil.loadLayer(dialog.getViewName(), fileName, null);
		} catch (RasterNotLoadException e) {
			RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object params) {
		if(	params instanceof Object[] &&
				((Object[])params).length == 3 &&
				((Object[])params)[0] instanceof String &&
				((Object[])params)[1] instanceof Long) {
			Object[] objects = (Object[]) params;
			
			String fileName = (String) objects[0];
			Long milis = (Long) objects[1];
			Boolean isInToc = (Boolean) objects[2];
			if (new File(fileName).exists()) {
				if (!isInToc.booleanValue())
					loadLayerInToc((String) objects[0], (Long) objects[1]);
				EndInfoDialog.show(fileName, milis.longValue());
			}
		}
	}

	public void interrupted() {}
}
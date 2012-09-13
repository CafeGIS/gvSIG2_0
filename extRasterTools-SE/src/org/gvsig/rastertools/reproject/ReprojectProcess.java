/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.reproject;

import javax.swing.SwingUtilities;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
/**
 * Proceso para la generación de capas reproyectadas.
 *
 * 10/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ReprojectProcess extends RasterProcess {
	private FLyrRasterSE lyr       = null;
	private String       filename  = null;
	private IProjection  proj      = null;
	private IProjection  projsrc   = null;
	private Reproject    reproject = null;
	private long         milis     = 0;
	private Boolean      isInTOC   = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.RasterProcess#init()
	 */
	public void init() {
		lyr = getLayerParam("layer");
		filename = getStringParam("path");
		proj = (IProjection) getParam("projection");
		projsrc = (IProjection) getParam("srcprojection");
		isInTOC = (Boolean) getParam("isintoc");
	}

	/**
	 * Método donde se ejecutará el Thread, aquí se reproyecta el raster.
	 */
	public void process() throws InterruptedException {
		long t1 = new java.util.Date().getTime();
		insertLineLog(PluginServices.getText(this, "reprojecting"));
		reproject = new Reproject(lyr, filename);
		try {
			reproject.warp(proj, projsrc);
			// Si hay que cerrar la capa de origen
			if ((isInTOC != null) && (isInTOC.booleanValue() == false)) {
				lyr.setRemoveRasterFlag(true);
				lyr.removeLayerListener(null);
			}

			long t2 = new java.util.Date().getTime();
			milis = t2 - t1;
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (externalActions != null) {
						externalActions.end(new Object[]{filename, new Long(milis), isInTOC});
					}
				}
			});
		} catch (ReprojectException e) {
			if (incrementableTask != null)
				incrementableTask.processFinalize();
			RasterToolsUtil.messageBoxError("error_reprojecting", this, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		if(reproject != null)
			return reproject.getPercent();
		else 
			return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this, "reprojecting");
	}
}
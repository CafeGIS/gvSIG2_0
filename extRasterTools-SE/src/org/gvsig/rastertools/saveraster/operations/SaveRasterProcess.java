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
package org.gvsig.rastertools.saveraster.operations;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Date;

import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IDataWriter;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.ViewPortData;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
/**
 * Thread que se encarga de llamar a los writer para realizar la tarea de
 * salvado y/p compresión
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class SaveRasterProcess extends RasterProcess {
	private ViewPort          viewPort          = null;
	private Dimension         dimension         = null;
	private RasterizerLayer   rasterizerLayer   = null;
	private String            fileName          = "";
	private Params            writerParams      = null;

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.RasterProcess#init()
	 */
	public void init() {
		viewPort = (ViewPort) getParam("viewport");
		dimension = (Dimension) getParam("dimension");
		rasterizerLayer = (RasterizerLayer) getParam("rasterizerlayer");
		fileName = getStringParam("filename");
		writerParams = (Params) getParam("writerparams");
	}

	/**
	 * Procesos de escritura de una porción de la vista.
	 */
	public void process() throws InterruptedException {
		GeoRasterWriter geoRasterWriter = null;
		long t2;
		long t1 = new java.util.Date().getTime();

		//Creamos el driver
		Envelope env = viewPort.getAdjustedExtent();
		Extent ex = new Extent(env.getMinimum(0), env.getMaximum(1), env.getMaximum(0), env.getMinimum(1));
		Dimension imgSz = viewPort.getImageSize();
		ViewPortData vpData = new ViewPortData(viewPort.getProjection(), ex, imgSz );
		AffineTransform at = new AffineTransform(vpData.getExtent().width() / imgSz.width,
												 0, 0,
												 -(vpData.getExtent().height() / imgSz.height),
												 vpData.getExtent().getULX(),
												 vpData.getExtent().getULY());

		try {
			//TODO: FUNCIONALIDAD: Poner los gerWriter con la proyección de la vista
			geoRasterWriter = GeoRasterWriter.getWriter((IDataWriter)rasterizerLayer, fileName,
												3, at, dimension.width,
												dimension.height, IBuffer.TYPE_IMAGE, writerParams, null);
		} catch (NotSupportedExtensionException e) {
			RasterToolsUtil.messageBoxError("extension_no_soportada", (Component)PluginServices.getMainFrame(), e);
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError("no_driver_escritura", (Component)PluginServices.getMainFrame(), e);
		}

		//Ejecutamos el driver con los datos pasados
		try {
			geoRasterWriter.dataWrite();
			geoRasterWriter.writeClose();
			t2 = new Date().getTime();
			try {
				saveRasterFinalize(fileName, (t2 - t1));
			} catch(ArrayIndexOutOfBoundsException exc) {
				//Si la ventana se ha cerrado ya es porque ha sido cancelada por lo que
				//producirá esta excepción. En este caso no se lanza la ventana de información
				//de finalización.
			}
		} catch(IOException ev) {
			if (incrementableTask != null) {
				incrementableTask.processFinalize();
				incrementableTask = null;
			}
			RasterToolsUtil.messageBoxError("error_processing", this, ev);
			return;
		} catch(OutOfMemoryError ev) {
			if (incrementableTask != null) {
				incrementableTask.processFinalize();
				incrementableTask = null;
			}
			RasterToolsUtil.messageBoxError("memoria_excedida", this, new IOException("Out of memory en salvar a raster"));
		}
	}

	/**
	 * Acciones que se realizan al finalizar de salvar a raster.
	 * @param fileName Nombre del fichero
	 * @param milis Tiempo que ha tardado en ejecutarse
	 */
	private void saveRasterFinalize(String fileName, long milis) {
		if (incrementableTask != null)
			incrementableTask.hideWindow();
		externalActions.end(new Object[]{fileName, new Long(milis)});
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		if(rasterizerLayer != null)
			return rasterizerLayer.getPercent();
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		if(rasterizerLayer != null)
			return rasterizerLayer.getTitle();
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.RasterProcess#getLog()
	 */
	public String getLog() {
		if(rasterizerLayer != null)
			return rasterizerLayer.getLog();
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLabel()
	 */
	public String getLabel() {
		if(rasterizerLayer != null)
			return rasterizerLayer.getLabel();
		return null;
	}
}
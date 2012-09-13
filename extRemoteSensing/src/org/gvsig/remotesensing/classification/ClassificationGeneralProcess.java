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
	 *   Av. Blasco Ibañez, 50
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

package org.gvsig.remotesensing.classification;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.RemoteSensingUtils;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * ClassificationGeneraProccess es la clase abstracta base que extienden todos
 * los metodos de clasificación. Recoge procedimientos comunes a todos los metodos:
 * construccion del grid que se va a clasificar, asignacion de la leyenda a la capa
 * resultante o escritura en fichero.
 * Cada procedimiento de clasificación implementará los métodos que determinan la
 * clase a la que pertenece un pixel, además del metodo run().

 *@author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 *@author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *@version 19/10/2007
 * */
public abstract class ClassificationGeneralProcess extends RasterProcess {

	protected Grid 					inputGrid			= null;
	protected FLyrRasterSE			rasterSE			= null;
	protected RasterBuffer 			rasterResult		= null;
	protected MapContext 			mapContext 			= null;
	protected int 					percent 		  	= 0;
	protected ArrayList				rois				= null;
	protected WriterBufferServer 	writerBufferServer	= null;
	protected String 				filename			= null;
	protected boolean				selectedBands[]		= null;
	protected	int					numClases			= 0;
	protected View 					view				= null;
	protected   int[] 				bandList			= null;
	protected boolean				withdefaultClass		= false;

	/**
	* Método que determina la clase de pertenencia del pixel cuyos valores
	* por banda se pasan en un array de bytes.
	* @param  valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel
	*/
	public abstract int getPixelClassForTypeByte(byte pixelBandsValues[]);

	/**
	* Método que determina la clase de pertenencia del pixel cuyos valores
	* por banda se pasan en un array de shorts.
	* @param  valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel
	*/
	public abstract int getPixelClassForTypeShort(short pixelBandsValues[]);

	/**
	* Método que determina la clase de pertenencia del pixel cuyos valores
	* por banda se pasan en un array de int.
	* @param  valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel
	*/
	public abstract int getPixelClassForTypeInt(int pixelBandsValues[]);

	/**
	* Método que determina la clase de pertenencia del pixel cuyos valores
	* por banda se pasan en un array de float.
	* @param  valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel
	*/
	public abstract int getPixelClassForTypeFloat(float pixelBandsValues[]);


	/**
	* Método que determina la clase de pertenencia del pixel cuyos valores
	* por banda se pasan en un array de double.
	* @param  valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel
	*/
	public abstract int getPixelClassForTypeDouble(double pixelBandsValues[]);


	/**
	* Establece el grid con las bandas recogidas en bandList.
	*/
	public  void setGrid(){

		IRasterDataSource dsetCopy = null;
		dsetCopy = rasterSE.getDataSource().newDataset();
		BufferFactory bufferFactory = new BufferFactory(dsetCopy);
		if (!RasterBuffer.loadInMemory(dsetCopy))
			bufferFactory.setReadOnly(true);
		try {
			bufferFactory.setAllDrawableBands();
			inputGrid = new Grid(bufferFactory,bandList);
		} catch (RasterBufferInvalidException e) {
			RasterToolsUtil.messageBoxError("buffer_incorrecto", this, e);
		}
	}

	/**
	* Escritura del resultado a disco y carga de la capa en la vista.
	*/
	public void writeToFile(){

		try{
			if(filename==null)
				return;
			GeoRasterWriter grw = null;
			writerBufferServer = new WriterBufferServer(rasterResult);
			grw = GeoRasterWriter.getWriter(writerBufferServer, filename, rasterResult.getBandCount(),rasterSE.getAffineTransform(), rasterResult.getWidth(), rasterResult.getHeight(), rasterResult.getDataType(), GeoRasterWriter.getWriter(filename).getParams(), null);
			grw.dataWrite();
			grw.setWkt(rasterSE.getWktProjection());
			grw.writeClose();
			rasterResult.free();
			mapContext= view.getModel().getMapContext();
			mapContext.beginAtomicEvent();
			FLayer lyr = null;
			int endIndex = filename.lastIndexOf(".");
			if (endIndex < 0)
				endIndex = filename.length();

			lyr = FLyrRasterSE.createLayer(
					filename.substring(filename.lastIndexOf(File.separator) + 1, endIndex),
					filename,
					view.getMapControl().getProjection()
					);

			/*
			 * Añadir la leyenda a la nueva capa.
			 */
			ArrayList colorItems = new ArrayList();
			ColorItem colorItem = null;
			int classValue = 0;
			for (Iterator iter = rois.iterator(); iter.hasNext();) {
				ROI roi = (ROI) iter.next();
				colorItem = new ColorItem();
				colorItem.setColor(roi.getColor());
				colorItem.setNameClass(roi.getName());
				colorItem.setValue(classValue);
				colorItems.add(colorItem);
				classValue++;
			}
			if(withdefaultClass){
				ColorItem defaultColor = new ColorItem();
				defaultColor.setColor(Color.BLACK);
				defaultColor.setNameClass("no_class_assigned");
				defaultColor.setValue(classValue);
				colorItems.add(defaultColor);
			}
			RemoteSensingUtils.setLeyend(lyr,colorItems);

			mapContext.getLayers().addLayer(lyr);
			mapContext.endAtomicEvent();
			mapContext.invalidate();

		} catch (NotSupportedExtensionException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer_notsupportedextension"), this, e);
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		}catch (LoadLayerException e) {
			RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		}catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (FilterTypeException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_adding_leyend"), this, e);
		}

	}


	/**
	* @return buffer monobanda con el resultado de la clasificación
	*/
	public  Object getResult(){
		return rasterResult;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLabel()
	 */
	public String getLabel() {
		return  PluginServices.getText(this,"procesando");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLog()
	 */
	public String getLog() {
		if (writerBufferServer==null)
			return PluginServices.getText(this,"clasificando_imagen")+"...";
		else
			return PluginServices.getText(this,"escribiendo_resultado")+"...";
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		if (writerBufferServer==null)
			return percent;
		else
			return writerBufferServer.getPercent();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this,"clasificacion");
	}


}

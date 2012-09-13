/*
* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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

package org.gvsig.remotesensing.imagefusion.process;

import java.io.IOException;

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
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.util.ColorConversion;
import org.gvsig.raster.util.RasterNotLoadException;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;

/**
 * ImageFusionProccess es la clase que representa el proceso general de fusion de una imagen 
 *
 *@author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 *@version 28/2/2007 
 * */

public abstract class ImageFusionProcess extends RasterProcess {

	protected FLyrRasterSE 				rasterSE= null;
	protected Grid 						inputGrid = null;
	protected FLyrRasterSE  			rasterBand= null;
	protected Grid 						highBandGrid = null;
	protected int 						method=  0;
	protected double 					coef = 0.0;
	protected RasterBuffer 				rasterResult = null;
	protected double					cellFactorX =  0.0;
	protected double					cellFactorY =  0.0;
	protected int						percent= 0;
	protected String 					filename= null	;
	protected String					viewName				=null;
	protected WriterBufferServer 		writerBufferServer	= null;
	protected ColorConversion           colorConversion         = new ColorConversion();
	protected int 						band = 0;
	
	/** Metodo que recoge los parametros del proceso de fusion de imagenes. 
	* <LI> rasterSE: Capa de entrada para la fusion</LI>
	* <LI> bandaHigth:	Banda alta resolucion</LI> 
	* <LI> metodo: metodo seleccionado </LI> 
	* <LI> view: vista sobre la que se carga la capa al acabar el proceso</LI>
	* <LI> filename: path con el fichero de salida</LI>
	*/
	public void init() {
		rasterSE  = (FLyrRasterSE) getParam("layer");
		rasterBand = (FLyrRasterSE) getParam("high_band");
		band= getIntParam("band");
		
		/**	if((rasterSE.getDataSource().getExtent().getLLX() != rasterBand.getDataSource().getExtent().getLLX())
		   || (rasterSE.getDataSource().getExtent().getLLX() != rasterBand.getDataSource().getExtent().getLLX())
		   || (rasterSE.getDataSource().getExtent().getLLX() != rasterBand.getDataSource().getExtent().getLLX())
		   || (rasterSE.getDataSource().getExtent().getLLX() != rasterBand.getDataSource().getExtent().getLLX() ))
				{
			
				incrementableTask.processFinalize();
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "extends_distintos"), this);
				return;
			
				}
		**/
		coef 	  =  getDoubleParam("coef");
		filename  = getStringParam("filename");
		viewName= (String)getParam("view");
		if(rasterSE != null)
			initparams();	
	}

	/**
	* Establece el grid con las bandas recogidas en bandList.
	*/
	public  void initparams(){
		
		IRasterDataSource dsetCopy = null; 
		dsetCopy = rasterSE.getDataSource().newDataset();
		BufferFactory bufferFactory = new BufferFactory(dsetCopy);
		IRasterDataSource dsetCopy2 = null; 
		
		// Ninguna banda seleccionada de alta resolucion seleccionada
		if(rasterBand==null){
			try {
				incrementableTask.processFinalize();
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "no_band_selected"), this);
				return;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		dsetCopy2 = rasterBand.getDataSource().newDataset();
		BufferFactory bufferFactory2 = new BufferFactory(dsetCopy2);
		
		if (!RasterBuffer.loadInMemory(dsetCopy))
			bufferFactory.setReadOnly(true);
		if (!RasterBuffer.loadInMemory(dsetCopy2))
			bufferFactory2.setReadOnly(true);
		try {	
			// ELECCION DE BANDAS CASO DEL PROCESO QUE LO PERMITA
			bufferFactory.setDrawableBands(rasterSE.getRenderBands());
			inputGrid = new Grid(bufferFactory);		
			
			highBandGrid = new Grid(bufferFactory2,new int[]{band});
			cellFactorX = (double)highBandGrid.getRasterBuf().getWidth()/inputGrid.getRasterBuf().getWidth();
			cellFactorY = (double)highBandGrid.getRasterBuf().getHeight()/inputGrid.getRasterBuf().getHeight();

			// Comprobar si las dos imagenes tienen el mismo extend
			
		} catch (RasterBufferInvalidException e) {
			RasterToolsUtil.messageBoxError("buffer_incorrecto", this, e);
		}
	}
	

	public String getTitle() {
		return PluginServices.getText(this,"image_fusion_process");
	}

	public int getPercent() {
		if(writerBufferServer==null)
			return percent;
		else 
			return writerBufferServer.getPercent();
	}

	/**
	 * Escritura del resultado en disco.
	 */
	public void writeToFile(){
		try{
			// Escritura de los datos a fichero temporal
			int endIndex = filename.lastIndexOf(".");
			if (endIndex < 0)
				 endIndex = filename.length();
			GeoRasterWriter grw = null;
			writerBufferServer = new WriterBufferServer(rasterResult);
			grw = GeoRasterWriter.getWriter(writerBufferServer, filename, rasterResult.getBandCount(),rasterBand.getAffineTransform(), rasterResult.getWidth(), rasterResult.getHeight(), rasterResult.getDataType(), GeoRasterWriter.getWriter(filename).getParams(), null);
			grw.dataWrite();
			grw.setWkt(rasterSE.getWktProjection());
			grw.writeClose();
			rasterResult.free();
			RasterToolsUtil.loadLayer(viewName, filename, null);
	
		} catch (NotSupportedExtensionException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer_notsupportedextension"), this, e);
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
		} catch (RasterNotLoadException e) {
				RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
	}
	
}

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

package org.gvsig.remotesensing.mosaic.process;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridCell;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.OutOfGridException;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.enhancement.EnhancementStretchListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.RemoteSensingUtils;

import com.iver.andami.PluginServices;

/**
* Clase que implementa el proceso de construccion de un mosaico mediante los métodos básicos.
*
* @params
* <LI>FLyrRasterSE[] "inputRasterLayers": Capas raster de entrada</LI>
* <LI>int "methodCode": Método de construcción (0:Valor máximo, 1:Valor mínimo, 2: Valor Medio,
* 3: Valor del pixel de la capa superior, 4:Valor del pixel de la capa inferior)</LI>
* <LI>String "outputPath": Ruta completa al fichero de salida del proceso</LI>
*
* @result
* <LI>outputRassterLayers[]: Capas raster resultantes</LI>
*
*
* @author aMuÑoz (alejandro.muñoz@uclm.es)
* @version 30/4/2008
* */

public class MosaicProcess extends RasterProcess {

	public static final int MAX 		= 0;
	public static final int MIN 		= 1;
	public static final int AVERAGE 	= 2;
	public static final int FRONT 		= 3;
	public static final int BACK 		= 4;

	// Layers que intervienen en el proceso
	private FLyrRasterSE inputRasterLayers[] = null;

	//Layer de salida
	private FLyrRasterSE outputRasterLayer = null;

	// Extend completo del mosaico
	private GridExtent fullExtend= null;

	// Grid resultante
	Grid mosaicGrid = null;

	// Buffers con las imagenes
	IBuffer buffers[]= null;

	// Codigo operacion mayor, menor,media valor de la situada encima.
	int codOp= 0;

	// indicador de proceso
	int percent=0, proceso=0;

	// writer para escritura en fichero
	private WriterBufferServer writerBufferServer =null;

	//  Numero de bandas 3 o 1 dependiendo de si es RGB o Nivel de gris
	int resultbandCount=0;

	// Fichero de salida
	private String fileName=null;


	private Grid layersGrid[]= null;

	/** Inicialización de los parámetros
	 *  layers -  FLayers con los layers seleccionados para el mosaico.
	 *
	 * 	En la inicializacion se calcula el grid resultante.
	 * */
	public void init() {

		inputRasterLayers= (FLyrRasterSE[])getParam("inputRasterLayers");
		codOp= getIntParam("methodCode");
		resultbandCount= getIntParam("numbands");
		fileName = getStringParam("outputPath");
		layersGrid = new Grid [inputRasterLayers.length];

// 		Calculo del extend resultante
		fullExtend= calculateExtend(inputRasterLayers);

		try {
				mosaicGrid= new Grid(fullExtend,fullExtend,IBuffer.TYPE_BYTE,new int[] { 0, 1, 2 });
				//mosaicGrid.setNoDataValue(-127);
				resultbandCount = mosaicGrid.getBandCount();
		} catch (RasterBufferInvalidException e) {
			RasterToolsUtil.messageBoxError("buffer_incorrecto", this, e);
		}
	}


	/**
	 *  Proceso
	 * */
	public void process() throws InterruptedException {

		// Parte de carga de los datos
		buffers= new RasterBuffer[inputRasterLayers.length];
		IRasterDataSource dsetCopy = null;
		try {

			double minX= fullExtend.getMin().getX();
			double minY= fullExtend.getMin().getY();
			double maxX=  fullExtend.getMax().getX();
			double maxY=  fullExtend.getMax().getY();
			// Se cargan todos los raster en los grid correspondientes
			percent=1;
			for(int i=0; i< inputRasterLayers.length;i++)
			{
				dsetCopy = ((FLyrRasterSE)inputRasterLayers[i]).getDataSource().newDataset();
				BufferFactory bufferFactory = new BufferFactory(dsetCopy);
				bufferFactory.setAdjustToExtent(false);
				if (!RasterBuffer.loadInMemory(dsetCopy))
					bufferFactory.setReadOnly(true);
				// Si pongo solo las renderizadas en algunos casos da problemas
				bufferFactory.setDrawableBands(inputRasterLayers[i].getRenderBands());

				GridExtent gridExtent = new GridExtent(fullExtend.getMin().getX(),
						fullExtend.getMin().getY(),
						fullExtend.getMax().getX(),
						fullExtend.getMax().getY(),
						inputRasterLayers[i].getAffineTransform().getScaleX(),
						inputRasterLayers[i].getAffineTransform().getScaleY());

				bufferFactory.setAreaOfInterest(minX,minY,maxX,maxY,gridExtent.getNX(),gridExtent.getNY());
				buffers[i]= (RasterBuffer) bufferFactory.getRasterBuf();
				//buffers[i] = ((RasterBuffer)buffers[i]).getAdjustedWindow(fullExtend.getNX(), fullExtend.getNY(), BufferInterpolation.INTERPOLATION_Bilinear);
				try {
					layersGrid[i]= new Grid (bufferFactory,inputRasterLayers[i].getRenderBands());
				} catch (RasterBufferInvalidException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				};
				percent=(int)((i+1)*100/inputRasterLayers.length);

				//Aplicar filtro de realce si es necesario:
				if(buffers[i].getDataType()!=DataBuffer.TYPE_BYTE){
					LinearStretchParams leParams = null;
					leParams = LinearStretchParams.createStandardParam(inputRasterLayers[i].getRenderBands(), 0.0, bufferFactory.getDataSource().getStatistics(), false);

					RasterFilter linearStretchEnhancementFilter = EnhancementStretchListManager.createEnhancedFilter(leParams, bufferFactory.getDataSource().getStatistics(),
							inputRasterLayers[i].getRenderBands(), false);
					linearStretchEnhancementFilter.addParam("raster", buffers[i]);
					linearStretchEnhancementFilter.execute();
					buffers[i] = (IBuffer)linearStretchEnhancementFilter.getResult("raster");
				}

			}
		}catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		} catch (InvalidSetViewException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_extension"), this, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (FileNotOpenException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		}

		proceso=1;

// 		Construccion del mosaico: Operación Máximo
		if(codOp==MAX){
			int progress = 0;
			for(int band=0; band<resultbandCount; band++){
				mosaicGrid.setBandToOperate(band);
				for(int row=0; row<mosaicGrid.getLayerNY(); row++){
					progress++;
					for(int col=0; col<mosaicGrid.getLayerNX();col++){
						setValueMax(col,row,band);
					}
					percent=(int)( progress*100/(mosaicGrid.getLayerNY()*resultbandCount));
				}
			}
		}

//		Construccion del mosaico: Operación Mínimo
		if(codOp==MIN){
			int progress = 0;
			for(int band=0; band<resultbandCount; band++){
				mosaicGrid.setBandToOperate(band);
				for(int row=0; row<mosaicGrid.getLayerNY(); row++){
					progress++;
					for(int col=0; col<mosaicGrid.getLayerNX();col++){
						setValueMin(col,row,band);
					}
					percent=(int)( progress*100/(mosaicGrid.getLayerNY()*resultbandCount));
				}
			}
		}

// 		Construccion del mosaico: Operación Media
		if(codOp==AVERAGE){
			int progress = 0;
			for(int band=0; band<resultbandCount; band++){
				mosaicGrid.setBandToOperate(band);
				for(int col=0; col<mosaicGrid.getLayerNY(); col++){
					progress++;
					for(int row=0; row<mosaicGrid.getLayerNX();row++){
						setValueMean(row,col,band);
					}
					percent=(int)( progress*100/(mosaicGrid.getLayerNY()*resultbandCount));
				}
			}
		}

//		Construccion del mosaico: Operacion valor de capa de delantera
		if(codOp==FRONT){
			int progress = 0;
			for(int band=0; band<resultbandCount; band++){
				mosaicGrid.setBandToOperate(band);
				for(int row=0; row<mosaicGrid.getLayerNY(); row++){
					progress++;
					for(int col=0; col<mosaicGrid.getLayerNX();col++){
						setValueFront(col,row,band);
					}
					percent=(int)( progress*100/(mosaicGrid.getLayerNY()*resultbandCount));
				}
			}
		}


//		Construccion del mosaico: Operación Valor de capa de trasera
		if(codOp==BACK){
			int progress = 0;
			for(int band=0; band<resultbandCount; band++){
				mosaicGrid.setBandToOperate(band);
				for(int row=0; row<mosaicGrid.getLayerNY(); row++){
					progress++;
					for(int col=0; col<mosaicGrid.getLayerNX();col++){
						setValueBack(col,row,band);
					}
					percent=(int)( progress*100/(mosaicGrid.getLayerNY()*resultbandCount));
				}
			}
		}

		// Se liberan los buffers
		for(int i=0; i<inputRasterLayers.length;i++)
			buffers[i].free();

		// Escritura en fichero
		proceso=2;
		createLayer();
		if (externalActions != null)
			externalActions.end(outputRasterLayer);
	}


	/**
	 *  Método que establece para la coordenada x,y el valor máximo
	 *  de todos los valores para ese píxel en cualquiera de las imagenes.
	 *  @param cordenada x
	 *  @param coordenada y
	 * */
	public void setValueMax(int x, int y, int band){
		byte result=Byte.MIN_VALUE; byte data=Byte.MIN_VALUE;
		int numLayers= buffers.length;
		Point2D worldPoint= (Point2D) mosaicGrid.getGridExtent().getWorldCoordsFromGridCoords(new GridCell(x,y,(double)0));
		for(int i=0; i<numLayers;i++){
			if(layersGrid[i].getGridExtent().isAt(worldPoint))
			{
				data = (byte)RemoteSensingUtils.getCellValueInLayerCoords(buffers[i], x, y, band);
				result=(byte) Math.max((byte)result,(byte)data);
			}
		}
		try {
				mosaicGrid.setCellValue(x,y,(byte)result);
		} catch (OutOfGridException e) {
				e.printStackTrace();
		}
	}

	/**
	 *  Método que establece para la coordenada x,y el valor máximo
	 *  de todos los valores para ese píxel en cualquiera de las imagenes.
	 *  Si el valor en cualquiera de las imagenes es noData no es tenido en cuenta
	 *  @param cordenada x
	 *  @param coordenada y
	 * */
	public void setValueMin(int x, int y,int band){
		byte result=Byte.MAX_VALUE; byte data=Byte.MAX_VALUE;
		int numLayers= buffers.length;
		Point2D worldPoint= (Point2D) mosaicGrid.getGridExtent().getWorldCoordsFromGridCoords(new GridCell(x,y,(double)0));
		for(int i=0; i<numLayers;i++){
				if(layersGrid[i].getGridExtent().isAt(worldPoint))
				{
					data = (byte)RemoteSensingUtils.getCellValueInLayerCoords(buffers[i], x, y, band);
					result=(byte) Math.min((byte)result,(byte)data);
				}
		}
		try {
				mosaicGrid.setCellValue(x,y,(byte)result);
		} catch (OutOfGridException e) {
				e.printStackTrace();
		}
	}


	/**
	 *  Método que establece para la coordenada x,y el valor medio
	 *  de todos los valores para ese píxel en cualquiera de las imagenes.
	 *  Si el valor en cualquiera de las imagenes es noData no es tenido en cuenta
	 *  @param cordenada x
	 *  @param coordenada y
	 * */
	public void setValueMean(int x, int y, int band){
		int buffTotales=0; byte result=0;
		int numLayers= buffers.length;
		Point2D worldPoint= (Point2D) mosaicGrid.getGridExtent().getWorldCoordsFromGridCoords(new GridCell(x,y,(double)0));
		for(int i=0; i<numLayers;i++){
			if(layersGrid[i].getGridExtent().isAt(worldPoint))
			{
				result+= (byte)RemoteSensingUtils.getCellValueInLayerCoords(buffers[i], x, y, band);
				buffTotales++;
			}
		}
		if(buffTotales==0)
			buffTotales=1;
		result=(byte) ((byte)(new Double(result/(byte)buffTotales).byteValue())&0xff);

		try {
				mosaicGrid.setCellValue(x,y,(byte)(result));
		} catch (OutOfGridException e) {
				e.printStackTrace();
		}
	}


	/**
	 *  Método que establece para la coordenada x,y el valor de la capa superior
	 *  en caso de solape. Se parte de un array de buffer ordenados, de tal manera que
	 *  el primer elemento corresponde a la capa situada mas al frente. El último por
	 *  contra es el situado al fondo.

	 *  @param cordenada x
	 *  @param coordenada y
	 * */
	public void setValueFront(int x, int y, int band){
		byte result=0;
		int numLayers= buffers.length;
		Point2D worldPoint= (Point2D) mosaicGrid.getGridExtent().getWorldCoordsFromGridCoords(new GridCell(x,y,(double)0));
		for(int i=0; i<numLayers;i++){
			if(layersGrid[i].getGridExtent().isAt(worldPoint))
				{
					result = (byte)RemoteSensingUtils.getCellValueInLayerCoords(buffers[i], x, y, band);
					break;
				}
		}
		try {
				mosaicGrid.setCellValue(x,y,(byte)result);
		} catch (OutOfGridException e) {
				e.printStackTrace();
		}
	}


	/**
	 *  Método que establece para la coordenada x,y el valor de la capa inferior
	 *  en caso de solape. Se parte de un array de buffer ordenados, de tal manera que
	 *  el primer elemento corresponde a la capa situada mas al frente. El último por
	 *  contra es el situado al fondo.

	 *  @param cordenada x
	 *  @param coordenada y
	 * */
	public void setValueBack(int x, int y, int band){
		byte result=0;
		int numLayers= buffers.length;
		Point2D worldPoint= (Point2D) mosaicGrid.getGridExtent().getWorldCoordsFromGridCoords(new GridCell(x,y,(double)0));
		for(int i=0; i<numLayers;i++){
			if(layersGrid[i].getGridExtent().isAt(worldPoint))
				{
					result = (byte)RemoteSensingUtils.getCellValueInLayerCoords(buffers[i], x, y, band);

				}
		}
		try {
				mosaicGrid.setCellValue(x,y,(byte)result);
		} catch (OutOfGridException e) {
				e.printStackTrace();
		}
	}


	/**
	 * Método que calcula el extend resultante para la operación de mosaico
	 *
	 * @param layers que intervienen en la operacion.
	 * @return GridExtend del mosaico
	 * */

	private GridExtent calculateExtend (FLyrRasterSE layers[]){

		GridExtent result= null;
		IRasterDataSource dsetCopy = null;
		double cellSize=Double.MAX_VALUE;
		double minX=0,maxX=0,minY=0,maxY=0;
		// Se obtiene el menor tamaño de celda mayor
		for(int i=0; i< layers.length;i++)
		{
			dsetCopy = ((FLyrRasterSE)layers[i]).getDataSource().newDataset();
			BufferFactory bufferFactory = new BufferFactory(dsetCopy);
			bufferFactory.setAdjustToExtent(false);
			cellSize= Math.min(cellSize,bufferFactory.getDataSource().getCellSize());
		}

		minX = layers[0].getFullEnvelope().getMinimum(0);
		minY = layers[0].getFullEnvelope().getMinimum(1);
		maxX = layers[0].getFullEnvelope().getMaximum(0);
		maxY = layers[0].getFullEnvelope().getMaximum(1);

		for(int i=1; i<layers.length;i++){

			minX= Math.min(minX,layers[i].getFullEnvelope().getMinimum(0));
			minY= Math.min(minY,layers[i].getFullEnvelope().getMinimum(1));
			maxX= Math.max(maxX,layers[i].getFullEnvelope().getMaximum(0));
			maxY= Math.max(maxY,layers[i].getFullEnvelope().getMaximum(1));
		}

		result = new GridExtent(minX,minY,maxX,maxY,cellSize);
		return result;
	}

	/**
	 * Escritura del resultado en disco y carga en la vista
	 */
	public void createLayer(){
		try{
			// Escritura de los datos a fichero temporal
			int endIndex = fileName.lastIndexOf(".");
			if (endIndex < 0)
				 endIndex = fileName.length();
			GeoRasterWriter grw = null;
			writerBufferServer = new WriterBufferServer(mosaicGrid.getRasterBuf());
			AffineTransform aTransform = new AffineTransform(fullExtend.getCellSize(),0.0,0.0,-fullExtend.getCellSize(),fullExtend.getMin().getX(),fullExtend.getMax().getY());
			grw = GeoRasterWriter.getWriter(writerBufferServer, fileName, mosaicGrid.getBandCount(),aTransform, mosaicGrid.getRasterBuf().getWidth(), mosaicGrid.getRasterBuf().getHeight(), mosaicGrid.getRasterBuf().getDataType(), GeoRasterWriter.getWriter(fileName).getParams(), inputRasterLayers[0].getProjection());
			grw.dataWrite();
			grw.setWkt((String)((FLyrRasterSE)inputRasterLayers[0]).getWktProjection());
			grw.writeClose();
			mosaicGrid.getRasterBuf().free();
			outputRasterLayer = FLyrRasterSE.createLayer(fileName.substring(fileName.lastIndexOf(File.separator) + 1, endIndex),
					fileName, null);

		} catch (NotSupportedExtensionException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer_notsupportedextension"), this, e);
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
		} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "raster_buffer_invalid_extension"), this, e);
		} catch (LoadLayerException e) {
			RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		}

	}

	public Object getResult() {
		return outputRasterLayer;
	}


	/**
	 * @return descripcion
	 * */
	public String getTitle() {
		return PluginServices.getText(this,"mosaic_process");
	}


	/**
	 *  @return String con el log en cada parte del proceso
	 * */
	public String getLog()
	{
		if(proceso==0)
			return PluginServices.getText(this,"load_buffer_data");
		else if (proceso==1)
			return PluginServices.getText(this,"generate_mosaic");
		else
			return PluginServices.getText(this,"write_to_file");
	}

	/**
	 * @return  indicador de progreso
	 * */
	public int getPercent() {
		if(writerBufferServer==null)
			return percent;
		else
			return writerBufferServer.getPercent();
	}


	/*
	// Identificación de zonas de solapamiento
	public boolean getSolapes(FLyrRasterSE raster1, FLyrRasterSE raster2){

		Grid grid1=null, grid2=null, aux=null;;
		IRasterDataSource dsetCopy = null;
		dsetCopy =raster1.getDataSource().newDataset();
		BufferFactory bufferFactory = new BufferFactory(dsetCopy);

		IRasterDataSource dsetCopy2 = null;
		dsetCopy2 =raster2.getDataSource().newDataset();
		BufferFactory bufferFactory2 = new BufferFactory(dsetCopy2);


		if (!RasterBuffer.loadInMemory(dsetCopy))
			bufferFactory.setReadOnly(true);

		try {
			grid1 = new Grid(bufferFactory,raster1.getRenderBands());
			grid2= new Grid(bufferFactory2,raster2.getRenderBands());
		} catch (RasterBufferInvalidException e) {
			e.printStackTrace();
		}

		// En grid1 la imagen con la cordenada x menor.
		if(grid2.getGridExtent().getMin().getX()< grid1.getGridExtent().getMin().getX())
			{
				try {
					grid1 = new Grid(bufferFactory2,raster2.getRenderBands());
					grid2= new Grid(bufferFactory,raster1.getRenderBands());
				} catch (RasterBufferInvalidException e) {
					e.printStackTrace();
				}

			}

		double xmin= grid1.getGridExtent().getMin().getX();
		double xmax= grid1.getGridExtent().getMax().getX();
		double ymin= grid1.getGridExtent().getMin().getY();
		double ymax= grid1.getGridExtent().getMax().getY();

		double xmin2= grid2.getGridExtent().getMin().getX();
		double ymin2= grid2.getGridExtent().getMin().getY();

		if(!(xmin2>xmin && xmin2<xmax)){
			System.out.print("Las imagenes no se solapan en las X");
			return false;
		}

		if(!(ymin2>ymin && ymin2<ymax)){
			System.out.print("Las imagenes no se solapan en las Y");
			return false;
		}

		// Detectado el solapamiento
		System.out.print("Rango x["+ xmin2 + ","+ Math.min(xmax,grid2.getGridExtent().getMax().getX())+"].");
		System.out.print("Rango y["+ ymin2 + ","+ Math.min(ymax,grid2.getGridExtent().getMax().getY())+"].");

		return true;
	}*/


}

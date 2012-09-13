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

package org.gvsig.georeferencing.process;

import java.awt.geom.AffineTransform;
import java.io.IOException;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.georeferencing.process.geotransform.GeoTransformProcess;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.GeoPointList;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.GridInterpolated;
import org.gvsig.raster.grid.OutOfGridException;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;

/**
 *  Clase que representa una proceso de georreferenciacion de un raster.
 *  
 *  @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * 	@version 10/2/2008
 **/
public class GeoreferencingProcess extends RasterProcess implements IProcessActions{
	
	//Capa a georreferenciar
	private FLyrRasterSE             rasterSE			= null;
	
	//Grid resultante de georreferenciacion
	private Grid                     imageGrid          = null;
	
	//Fichero de salida
	private String                   filename           = null;
	
	
	// Lista puntos de control
	private GeoPointList             gpcs               = null;
	
	//Extend de imagen corregida
	GridExtent                       newExtend          = null;
	
	// Metodo de resampleado utilizado
	private int                      rMethod            = 0;
	
	//Indicador de progreso
	private int                      percent            = 0;
	
	// Grid resultado
	private Grid                     gridResult         = null;
	
	WriterBufferServer               writerBufferServer = null;

	private int                      orden              = 0;
	
	private int[]                    bands              = null;
	
	//Tamaño de celda en X si es pasada por el usuario
	private double                   xCellSize           = 0;
	//Tamaño de celda en Y si es pasada por el usuario
	private double                   yCellSize           = 0;
	
	/** Metodo que recoge los parametros del proceso georreferenciacion de un raster
	* <LI>rasterSE: capa a georeferenciar</LI>
	* <LI>filename: path con el fichero de salida</LI>
	* <LI>method: metodo de resampleo </LI>
	*/
	public void init() {
		
		rasterSE = (FLyrRasterSE)getParam("fLayer");
		filename = (String)getParam("filename");
		rMethod = (int)getIntParam("method");
		gpcs = (GeoPointList) getParam("gpcs");
		orden= (int)getIntParam("orden");
		bands = new int[rasterSE.getBandCount()];
		xCellSize = (double)getDoubleParam("xCellSize");
		yCellSize = (double)getDoubleParam("yCellSize");
		
		for(int i=0; i<rasterSE.getBandCount(); i++)
			bands[i]= i;
		// Inicializacion del grid correspondiente a la imagen a corregir
		IRasterDataSource dsetCopy = null; 
		dsetCopy = rasterSE.getDataSource().newDataset();
		BufferFactory bufferFactory = new BufferFactory(dsetCopy);
		bufferFactory.setReadOnly(true);	
		try {
				imageGrid = new Grid(bufferFactory);	
		}catch (RasterBufferInvalidException e) {
			e.printStackTrace();			
		}
	}
	

	public void process() throws InterruptedException {
		
		GeoTransformProcess transform = new GeoTransformProcess();
		transform.setActions(this);
		transform.addParam("gpcs", gpcs);
		transform.addParam("orden",new Integer(orden));
		transform.run();		
		
		// Obtenida la transformacion la aplicamos a los puntos extremos de la imagen
		double p1[]=transform.getCoordMap(0,0);		
		double p2[]=transform.getCoordMap(rasterSE.getPxWidth(),0);		
		double p3[]=transform.getCoordMap(0,rasterSE.getPxHeight());
		double p4[]=transform.getCoordMap(rasterSE.getPxWidth(),rasterSE.getPxHeight());	
				
		double xmin=Math.min(p1[0],p3[0]);
		double ymin=Math.min(p3[1],p4[1]);
		double xmax=Math.max(p2[0],p4[0]);	
		double ymax=Math.max(p1[1],p2[1]);
		
		if(xCellSize <= 1)
			xCellSize = (xmax - xmin) / (double)rasterSE.getPxWidth();
		if(yCellSize <= 1)
			yCellSize = (ymax - ymin) / (double)rasterSE.getPxHeight();
			
		newExtend= new GridExtent(xmin, ymin, xmax, ymax, xCellSize);
		int datatype= rasterSE.getBufferFactory().getRasterBuf().getDataType();
		
		try {
			gridResult = new Grid(newExtend, newExtend, datatype, bands);
		} catch (RasterBufferInvalidException e) {
			RasterToolsUtil.messageBoxError("error_grid", this, e);
		}
		
		double minPointX=gridResult.getGridExtent().getMin().getX();
		double maxPointY=gridResult.getGridExtent().getMax().getY();
		double cellsize=gridResult.getCellSize();
	
		
		GridInterpolated gridInterpolated=null;
		gridInterpolated = new GridInterpolated((RasterBuffer)imageGrid.getRasterBuf(),imageGrid.getGridExtent(),imageGrid.getGridExtent(),bands);
		
		// SE ESTABLECE EL METODO DE INTERPOLACION (por defecto vecino mas proximo)
		if(rMethod==GridInterpolated.INTERPOLATION_BicubicSpline)
			gridInterpolated.setInterpolationMethod(GridInterpolated.INTERPOLATION_BicubicSpline);
		else if(rMethod==GridInterpolated.INTERPOLATION_Bilinear)
			gridInterpolated.setInterpolationMethod(GridInterpolated.INTERPOLATION_Bilinear);
		else
			gridInterpolated.setInterpolationMethod(GridInterpolated.INTERPOLATION_NearestNeighbour);
		
		double coord[]=null;
		try {
		
			if(datatype==IBuffer.TYPE_BYTE)
			{
				byte values[]=new byte[bands.length];
				int progress=0;
				// OPTIMIZACION. Se esta recorriendo secuencialmente cada banda.
				for(int band=0; band<bands.length;band++){
					gridResult.setBandToOperate(band);
					gridInterpolated.setBandToOperate(band);
					for(int row=0; row<gridResult.getLayerNY(); row++){
						progress++;
						for(int col=0; col<gridResult.getLayerNX();col++)
							{
								coord=transform.getCoordPixel(col*cellsize+minPointX, maxPointY-row*cellsize);	
								values[band] = (byte)gridInterpolated._getValueAt(coord[0],coord[1]);
								gridResult.setCellValue(col,row,(byte)values[band]);
							}
						percent=(int)( progress*100/(gridResult.getLayerNY()*bands.length));	
					}	
				}	
			}
			
			if(datatype==IBuffer.TYPE_SHORT)
			{
				short values[]=new short[bands.length];
				int progress=0;
				// OPTIMIZACION. Se esta recorriendo secuencialmente cada banda.
				for(int band=0; band<bands.length;band++){
					gridResult.setBandToOperate(band);
					gridInterpolated.setBandToOperate(band);
					for(int row=0; row<gridResult.getLayerNY(); row++){
						progress++;
						for(int col=0; col<gridResult.getLayerNX();col++)
							{
								coord=transform.getCoordPixel(col*cellsize+minPointX, maxPointY-row*cellsize);	
								values[band] = (short)gridInterpolated._getValueAt(coord[0],coord[1]);
								gridResult.setCellValue(col,row,(short)values[band]);
							}
						percent=(int)( progress*100/(gridResult.getLayerNY()*bands.length));	
					}	
				}	
			}
			
			if(datatype==IBuffer.TYPE_INT)
			{
				int values[]=new int[bands.length];
				int progress=0;
				// OPTIMIZACION. Se esta recorriendo secuencialmente cada banda.
				for(int band=0; band<bands.length;band++){
					gridResult.setBandToOperate(band);
					gridInterpolated.setBandToOperate(band);
					for(int row=0; row<gridResult.getLayerNY(); row++){
						progress++;
						for(int col=0; col<gridResult.getLayerNX();col++)
							{
								coord=transform.getCoordPixel(col*cellsize+minPointX, maxPointY-row*cellsize);	
								values[band] = (int)gridInterpolated._getValueAt(coord[0],coord[1]);
								gridResult.setCellValue(col,row,(int)values[band]);
							}
						percent=(int)( progress*100/(gridResult.getLayerNY()*bands.length));	
					}	
				}	
			}
			
			if(datatype==IBuffer.TYPE_FLOAT)
			{	
				float values[]=new float[bands.length];
				int progress=0;
				// OPTIMIZACION. Se esta recorriendo secuencialmente cada banda.
				for(int band=0; band<bands.length;band++){
					gridResult.setBandToOperate(band);
					gridInterpolated.setBandToOperate(band);
					for(int row=0; row<gridResult.getLayerNY(); row++){
						progress++;
						for(int col=0; col<gridResult.getLayerNX();col++)
							{
								coord=transform.getCoordPixel(col*cellsize+minPointX, maxPointY-row*cellsize);	
								values[band] = (float)gridInterpolated._getValueAt(coord[0],coord[1]);
								gridResult.setCellValue(col,row,(float)values[band]);
							}
						percent=(int)( progress*100/(gridResult.getLayerNY()*bands.length));	
					}	
				}	
			}
			
			if(datatype==IBuffer.TYPE_DOUBLE)
			{
				double values[]=new double[bands.length];
				int progress=0;
				// OPTIMIZACION. Se esta recorriendo secuencialmente cada banda.
				for(int band=0; band<bands.length;band++){
					gridResult.setBandToOperate(band);
					gridInterpolated.setBandToOperate(band);
					for(int row=0; row<gridResult.getLayerNY(); row++){
						progress++;
						for(int col=0; col<gridResult.getLayerNX();col++)
							{
								coord=transform.getCoordPixel(col*cellsize+minPointX, maxPointY-row*cellsize);	
								values[band] = (double)gridInterpolated._getValueAt(coord[0],coord[1]);
								gridResult.setCellValue(col,row,(double)values[band]);
							}
						percent=(int)( progress*100/(gridResult.getLayerNY()*bands.length));	
					}	
				}	
			}
			
		} catch (OutOfGridException e) {
			e.printStackTrace();
		} catch (RasterBufferInvalidAccessException e) {
			e.printStackTrace();
		} catch (RasterBufferInvalidException e) {
			e.printStackTrace();
		}
		
		generateLayer();
		if(externalActions!=null)
			externalActions.end(filename);
	}


	private void generateLayer(){	
	
		GeoRasterWriter grw = null;
		IBuffer buffer= gridResult.getRasterBuf();
			writerBufferServer = new WriterBufferServer(buffer);
		AffineTransform aTransform = new AffineTransform(newExtend.getCellSize(),0.0,0.0,-newExtend.getCellSize(),newExtend.getMin().getX(),newExtend.getMax().getY());
		int endIndex =filename.lastIndexOf(".");
		if (endIndex < 0)
			endIndex = filename.length();
		try {
			grw = GeoRasterWriter.getWriter(writerBufferServer, filename,gridResult.getRasterBuf().getBandCount(),aTransform, gridResult.getRasterBuf().getWidth(),gridResult.getRasterBuf().getHeight(), gridResult.getRasterBuf().getDataType(), GeoRasterWriter.getWriter(filename).getParams(),null);
			grw.dataWrite();
			grw.setWkt(rasterSE.getWktProjection());
			grw.writeClose();
		} catch (NotSupportedExtensionException e1) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer_notsupportedextension"), this, e1);
		} catch (RasterDriverException e1) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e1);	
		} catch (IOException e) {
			RasterToolsUtil.messageBoxError("error_salvando_rmf", this, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
		

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return PluginServices.getText(this,"georreferenciacion_process");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLabel()
	 */
	public int getPercent() {
		if(writerBufferServer==null)
			return percent;
		else
			return writerBufferServer.getPercent();
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLabel()
	 */
	public String getLog() {
		return PluginServices.getText(this,"georreferencing_log_message");
	}

	
	public void interrupted() {
		// TODO Auto-generated method stub	
	}

	public void end(Object param) {	
	}

}

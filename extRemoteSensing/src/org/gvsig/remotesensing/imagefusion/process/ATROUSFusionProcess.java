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

import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.HistogramException;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.grid.filter.RasterFilter.Kernel;
import org.gvsig.raster.grid.filter.convolution.ConvolutionByteFilter;
import org.gvsig.raster.grid.filter.convolution.ConvolutionFilter;
import org.gvsig.raster.grid.filter.histogramMatching.HistogramMatchingByteFilter;
import org.gvsig.raster.util.RasterNotLoadException;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;

/**
 * Proceso de fusion por wavelets atrous.
 * 
 * @version 27/02/2008
 * @author Alejandro Muñoz Sánchez (alejandro.munoz@uclm.es)
 * 
 */
public class ATROUSFusionProcess extends ImageFusionProcess{
	
	RasterFilter filter = new ConvolutionFilter();
	RasterFilter filtro= null;
	int bands[]= null;
	int percent=0;
	double [][]kernel= null;
	RasterBuffer levels[][]= null;
	RasterBuffer wavelets_buffers[][]= null;
	int nivel= 0;
	
	// Variables para la información en el log del proceso
	String bandLog= null;
	String level= null;
	
	double filter_level1[][]= { {1,	4,	6,	4,	1},	{4,	16,	24,	16,	4},	{6,	24,	36,	24,	6},	{4,	16,	24,	16,	4},{1,	4,	6,	4,	1} };
	
	double filter_level2[][]= 
	{
		{1,	0,	4,	0,	6,	0,	4,	0,	1},
		{0,	0,	0,	0,	0,	0,	0,	0,	0},
		{4,	0,	16,	0,	24,	0,	16,	0,	4},
		{0,	0,	0,	0,	0,	0,	0,	0,	0},
		{6,	0,	24,	0,	36,	0,	24,	0,	6},
		{0,	0,	0,	0,	0,	0,	0,	0,	0},
		{4,	0,	16,	0,	24,	0,	16,	0,	4},
		{0,	0,	0,	0,	0,	0,	0,	0,	0},
		{1,	0,	4,	0,	6,	0,	4,	0,  1}
	};
	
	double filter_level3[][]= 
	{
				{1,	0,	0,	0,	4,	0,	0,	0,	6,	0,	0,	0,	4,	0,	0,	0,	1},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{4,	0,	0,	0,	16,	0,	0,	0,	24,	0,	0,	0,	16,	0,	0,	0,	4},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{6,	0,	0,	0,	24,	0,	0,	0,	36,	0,	0,	0,	24,	0,	0,	0,	6},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{4,	0,	0,	0,	16,	0,	0,	0,	24,	0,	0,	0,	16,	0,	0,	0,	4},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0},
				{1,	0,	0,	0,	4,	0,	0,	0,	6,	0,	0,	0,	4,	0,	0,	0,	1},
	};
	
	
	/** 
	 *  Asinación de parámetros al proceso
	 **/
	public void init() {
		super.init();
		
		// Bandas a las que se aplicara el proceso.
		bands = (int [])getParam("bands");
		int numbands= bands.length;
		
		// Niveles de wavelets
		nivel= getIntParam("nivel");
		
		levels = new RasterBuffer[nivel+1][numbands];
		wavelets_buffers = new RasterBuffer [nivel][numbands];
	}

	
	public void process() throws InterruptedException {
		
		/**
		 *  La primera tarea del proceso es aplicar a la pancromatica un proceso de correspondencia de 
		 *  histograma con cada una de las bandas de la multiespectral.
		 *  El resultado del ajuste de estas pancromáticas se almacenacomo primer nivel en el array levels.
		 *  
		 * */
		insertLineLog(RasterToolsUtil.getText(this, "histogram_matching"));
		for(int i=0; i<bands.length;i++){
			bandLog = PluginServices.getText(this,"band")+i;
			filtro= new HistogramMatchingByteFilter();
			IRasterDataSource dsetCopy = null; 
			dsetCopy = rasterSE.getDataSource().newDataset();
			BufferFactory bufferFactory = new BufferFactory(dsetCopy);
			if (!RasterBuffer.loadInMemory(dsetCopy))
				bufferFactory.setReadOnly(true);
			
			Grid bandGrid=null;
			try {
				bandGrid = new Grid(bufferFactory,new int[]{bands[i]});
			
			} catch (RasterBufferInvalidException e1) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e1);	
			}
			filtro.addParam("raster",highBandGrid.getRasterBuf());
			try {
				filtro.addParam("histogramReference",bandGrid.getRasterBuf().getHistogram());
			} catch (HistogramException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			filtro.addParam("numbands",new Integer(1));
			filtro.addParam("filterName",new String("histogram"));
			filtro.execute();
			levels[0][i]=(RasterBuffer) filtro.getResult("raster");
		}
		filtro=null;
		
		
		/** Para cada banda se obtiene el resultado de aplicar el filtro de convolucion correspondiente 
		 *  al nivel k. El resultado para cada banda i se almacena en el array levels[k][i].
		 *  */
		insertLineLog(RasterToolsUtil.getText(this, "convoultion_filter"));
		for(int k=1; k<=nivel;k++)
		for(int i=0; i<bands.length;i++){	
			bandLog = PluginServices.getText(this,"band")+i;
			level = PluginServices.getText(this,"level")+k;
			levels[k][i]=calculus_level_transform(levels[k-1][i], k);
			wavelets_buffers[k-1][i]=getWavelet(levels[k-1][i],levels[k][i]);
			levels[k-1][i].free();
		}
		filter=null;
		
		insertLineLog(RasterToolsUtil.getText(this, "composicion_wavelets"));
		rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_INT, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), bands.length, true);
		// Resampleado de la multiespectral y asignacion de las componentes wavelets
		for(int row=0; row<inputGrid.getNY(); row++){	 
			for(int col=0; col<inputGrid.getNX();col++){	
				byte data[]= new byte[inputGrid.getBandCount()];
				int result[]= new int[bands.length];
				inputGrid.getRasterBuf().getElemByte(row,col,data);	 
				for(int facx= 0; facx<cellFactorX; facx++){	 
					int line= (int)(cellFactorY*row)+facx;
					for(int facy= 0; facy<cellFactorY; facy++)	{	
						int colum= (int)(cellFactorY*col)+facy;
						int k=0;		
						for(int i=0; i<bands.length;i++){
								for(int j=0; j<nivel;j++)
								result[k]= (int) ((data[bands[i]]& 0xff)+(wavelets_buffers[j][i].getElemByte(line,colum,0)));
							//	if(result[k]<-128) result[k]=-128;
							//	if(result[k]>127) result[k]= 127;
								k++;	
						}	
						rasterResult.setElemInt(line,colum,result);
					}	 
				}	 
			}	
			percent = row*100/inputGrid.getRasterBuf().getHeight();	 
		}
		// Se liberan los buffers en memoria
		insertLineLog(RasterToolsUtil.getText(this, "liberando_recursos"));
		for(int i=0; i<wavelets_buffers.length;i++)
			for(int j=0; j<wavelets_buffers[0].length;j++)
				wavelets_buffers[i][j].free();
		
		insertLineLog(RasterToolsUtil.getText(this, "escribiendo_resultado"));
		writeToFile(rasterResult, filename);
	}

	
	/**
	 *  Método que aplica el filtro correspondiente al buffer pasado como argumento
	 *  según en nivel correspondiente.
	 *  @return resultado del filtro
	 **/
	public RasterBuffer calculus_level_transform(RasterBuffer rb, int level){
		try {		
			filter= new ConvolutionByteFilter();
			setKernel(level);
			filter.addParam("raster",rb);
			Kernel k1= new Kernel(kernel,256);
			filter.addParam("kernel",k1);
			filter.addParam("ladoVentana",new Integer(5));
			filter.addParam("filterName",new String("personalizado"));
			filter.execute();
		
		} catch (InterruptedException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "apply_filter_error"), this);
		}
		return (RasterBuffer)filter.getResult("raster");
	}
	
	
	/**
	 * Metodo que obtiene la diferencia de los pixeles de los dos buffers que se pasan
	 * como parametro.
	 * @param buffer 
	 * @param buffer
	 * */
	
	public RasterBuffer getWavelet(RasterBuffer rb, RasterBuffer rb2){
		
		byte data=0;
		byte dataOrg=0;
		byte result=0;
		rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_BYTE,highBandGrid.getRasterBuf().getWidth(),highBandGrid.getRasterBuf().getHeight(),highBandGrid.getRasterBuf().getBandCount(),true);
		percent=0;
		
		// MODIFICAR PARA CUALQUIER TIPO DE DATO
		for(int row=0; row<rasterResult.getHeight(); row++){	 
			for(int col=0; col<rasterResult.getWidth();col++){	
				data=(byte) (rb2.getElemByte(row,col,0)& 0xff);
				dataOrg=(byte) (rb.getElemByte(row,col,0)&0xff);
				result=(byte) ((byte) (dataOrg- data));
				rasterResult.setElem(row,col,0,result);
			}
			percent = row*100/rasterResult.getHeight();	 
		}
		//writeToFile(rasterResult,"Wavelete"+bandLog+"_"+level+".tif");
		return rasterResult;	
	}
	


	public int getPercent() {
		if (filtro!=null)
			return filtro.getPercent();
		if(filter!=null)
			return filter.getPercent();
		else if(writerBufferServer!=null)
			return writerBufferServer.getPercent();
		else
			return percent;
	}
	
	
	public String getLog() {
		if (filtro!=null)
			 return PluginServices.getText(this,"fusionando_imagen")+"..." +"\n\n"+PluginServices.getText(this,"metodo")+PluginServices.getText(this,"wavelets_atrous") + "\n\nAjustando Histograma Pancromática a Histograma " + bandLog;
		else if(filter!=null)
			return PluginServices.getText(this,"fusionando_imagen")+"..." +"\n\n"+PluginServices.getText(this,"metodo")+PluginServices.getText(this,"wavelets_atrous") +"\n\nAplicando filtros"+ bandLog +"  "+level ;
		else if(writerBufferServer!=null)
			return PluginServices.getText(this,"fusionando_imagen")+"..." +"\n\n"+PluginServices.getText(this,"metodo")+PluginServices.getText(this,"wavelets_atrous")+"\n\nEscribiendo resultados";
		else
			return PluginServices.getText(this,"fusionando_imagen")+"..." +"\n\n"+PluginServices.getText(this,"metodo") +PluginServices.getText(this,"wavelets_atrous")+"\n\nConstruyendo imagen";
	}
	

	
	/** 
	 * @ return valor de la posicion line col de la banda pancromática
	 * 
	 * */
	protected double getValuePanc(int line, int col){

		double value= 0.0;
		switch (highBandGrid.getRasterBuf().getDataType()){
			case RasterBuffer.TYPE_BYTE:
				value= highBandGrid.getRasterBuf().getElemByte(line,col,0)&0xff;
				break;
			case RasterBuffer.TYPE_SHORT:
				value= highBandGrid.getRasterBuf().getElemShort(line,col,0);
				break;
			case RasterBuffer.TYPE_INT:
				value= highBandGrid.getRasterBuf().getElemInt(line,col,0);
				break;
			case RasterBuffer.TYPE_FLOAT:
				value= highBandGrid.getRasterBuf().getElemFloat(line,col,0);
				break;
			case RasterBuffer.TYPE_DOUBLE:
				value= highBandGrid.getRasterBuf().getElemDouble(line,col,0);
		}
		return  value;
	}

	
	
	
	public void writeToFile(RasterBuffer rb, String path){
		try{
			// Escritura de los datos a fichero temporal
			
			int endIndex = path.lastIndexOf(".");
			if (endIndex < 0)
				 endIndex = path.length();
			GeoRasterWriter grw = null;
			writerBufferServer = new WriterBufferServer(rb);
			grw = GeoRasterWriter.getWriter(writerBufferServer,path, rb.getBandCount(),rasterBand.getAffineTransform(), rb.getWidth(), rb.getHeight(), rb.getDataType(), GeoRasterWriter.getWriter(filename).getParams(), null);
			grw.dataWrite();
			grw.setWkt(rasterBand.getWktProjection());
			grw.writeClose();
			rb.free();
			RasterToolsUtil.loadLayer(viewName,path, null);
	
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
	
	
	/**
	 *  Método que asigna el kernel en función del nivel que se pasa como parámetro.
	 *  @param level mivel para el que se asigna el kernel
	 * 
	 * */
	void setKernel(int level){
		switch(level){
		case 1:	
			kernel= filter_level1;
			break;
		case 2:
			kernel=filter_level2;
			break;
		case 3:
			kernel=filter_level3;
			break;
		}
	}
	
	public String getTitle() {
		return PluginServices.getText(this, "fusion_atrous");
	}
	
}

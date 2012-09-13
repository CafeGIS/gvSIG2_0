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

import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;


/**
 * Proceso Brovey.
 * 
 * @version 27/02/2008
 * @author Alejandro Muñoz Sánchez (alejandro.munoz@uclm.es)
 * 
 */
public class BroveyFusionProcess  extends ImageFusionProcess{

	public void init(){
		super.init();
	}
	
	/** Proceso de construccion de la imagen resultado de la fusion*/
	public void process() throws InterruptedException {
		
			byte   [][] pRGBMultiespectralByte = null;
			short  [][] pRGBMultiespectralShort = null;
			int    [][] pRGBMultiespectralInt = null;
			float  [][] pRGBMultiespectralFloat = null;
			double [][] pRGBMultiespectralDouble = null;
			
			int heightMultiespec = inputGrid.getRasterBuf().getHeight();
			insertLineLog(RasterToolsUtil.getText(this, "aplicando_brovey"));
			
			
			// Caso Bufer tipo byte
			if(inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_BYTE){
				rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_BYTE, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), 3, true);
				for(int iLine=0;iLine<heightMultiespec ;iLine++){
					pRGBMultiespectralByte = inputGrid.getRasterBuf().getLineByte(iLine);
					processBrovey(pRGBMultiespectralByte, iLine);	    		
					percent= (int)iLine*100/heightMultiespec;	
				}
			}	
			
			// Caso Bufer tipo short
			if(inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_SHORT){
				
				rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_SHORT, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), 3, true);
				for(int iLine=0;iLine<heightMultiespec ;iLine++){
					pRGBMultiespectralShort = inputGrid.getRasterBuf().getLineShort(iLine);
					processBrovey(pRGBMultiespectralShort, iLine);	    		
					percent= (int)iLine*100/heightMultiespec;	
				
				}
				
			}
			
			// Caso Bufer tipo int
			if(inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_INT){
				
				rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_INT, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), 3, true);
				for(int iLine=0;iLine<heightMultiespec ;iLine++){
					pRGBMultiespectralInt = inputGrid.getRasterBuf().getLineInt(iLine);
					processBrovey(pRGBMultiespectralInt, iLine);	    		
					percent= (int)iLine*100/heightMultiespec;	
				}
			}	
			
//			 Caso Bufer tipo float
			if(inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_FLOAT){
				
				rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_FLOAT, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), 3, true);
				for(int iLine=0;iLine<heightMultiespec ;iLine++){
					pRGBMultiespectralFloat = inputGrid.getRasterBuf().getLineFloat(iLine);
					processBrovey(pRGBMultiespectralFloat, iLine);	    		
					percent= (int)iLine*100/heightMultiespec;	
				}
			}	
			
//			 Caso Bufer tipo double
			if(inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_DOUBLE){
				
				rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_DOUBLE, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), 3, true);
				for(int iLine=0;iLine<heightMultiespec ;iLine++){
					pRGBMultiespectralFloat = inputGrid.getRasterBuf().getLineFloat(iLine);
					processBrovey(pRGBMultiespectralDouble, iLine);	    		
					percent= (int)iLine*100/heightMultiespec;	
				}
			}	
			
		writeToFile();
	}
	
	
	/**
	 * Aplica la operación de refinamiento sobre el buffer Multiespectral que contiene 
	 * el RGB que es pasado por parámetro utilizando la pancromática.
	 * @param bufferInput	Buffer rgb 
	 * @param length		longitud del buffer de la pancromatica utilizado
	 * @param iLine			línea leida de la imagen multiespectral
	 * @return				
	 */
	private void processBrovey(byte[][] bufferInput, int iLine){    	
		int r=0,g=0,b=0;
		double r1=0, g1=0, b1=0;
		double scale=0;
		double valorPanc=0;
		int line=0, col=0;
		// Para cada elemento Punto de la multiespectral
		for(int iElem=0; iElem<bufferInput[0].length; iElem++){
			r = (int)(bufferInput[0][iElem]& 0xff);
			g = (int) (bufferInput[1][iElem]&0xff);
			b = (int)(bufferInput[2][iElem]& 0xff);
			for(int j=0;j<cellFactorX;j++){
				for(int k=0;k<cellFactorY;k++){	
					line=(int) (iLine*cellFactorY+j);
					col= (int) (iElem*cellFactorX+k);
					valorPanc =  getValuePanc(line, col);
					scale = (3*(valorPanc+coef))/(r+g+b+1.0);		
					r1 = r*scale;
					g1 = g*scale; 
					b1 = b*scale;
					
					// Evitar desbordamiento ???
					if(r1>255) r1=255;
					if(g1>255) g1=255;
					if(b1>255) b1=255;
					rasterResult.setElem(line, col, 0, (byte)r1);
					rasterResult.setElem(line, col, 1, (byte)g1);
					rasterResult.setElem(line, col, 2, (byte)b1);
				}
			}
		}
	}
	
	
	
	/**
	 * Aplica la operación de refinamiento sobre el buffer Multiespectral que contiene 
	 * el RGB que es pasado por parámetro utilizando la pancromática.
	 * @param bufferInput	buffer de entrada (tipo short)
	 * @param length		longitud del buffer de la pancromatica utilizado
	 * @param iLine			línea leida de la imagen multiespectral
	 * @return				
	 */
	private void processBrovey(short[][] bufferInput, int iLine){    	
		int r=0,g=0,b=0;
		double r1=0, g1=0, b1=0;
		double scale=0;
		double valorPanc=0;
		int line=0, col=0;
		// Para cada elemento Punto de la multiespectral
		for(int iElem=0; iElem<bufferInput[0].length; iElem++){
			r = (int)(bufferInput[0][iElem]& 0xff);
			g = (int) (bufferInput[1][iElem]&0xff);
			b = (int)(bufferInput[2][iElem]& 0xff);
			for(int j=0;j<cellFactorX;j++){
				for(int k=0;k<cellFactorY;k++){	
					line=(int) (iLine*cellFactorY+j);
					col= (int) (iElem*cellFactorX+k);
					valorPanc =  getValuePanc(line, col);//(highBandGrid.getRasterBuf().getElemByte(line,col,0)&0xff);
					scale = (3*(valorPanc+coef))/(r+g+b+1.0);		
					r1 = r*scale;
					g1 = g*scale; 
					b1 = b*scale;
					
					// Evitar desbordamiento ???
					if(r1>255) r1=255;
					if(g1>255) g1=255;
					if(b1>255) b1=255;
					rasterResult.setElem(line, col, 0, (short)r1);
					rasterResult.setElem(line, col, 1, (short)g1);
					rasterResult.setElem(line, col, 2, (short)b1);
				}
			}
		}
	
	}

	
	/**
	 * Aplica la operación de refinamiento sobre el buffer Multiespectral que contiene 
	 * el RGB que es pasado por parámetro utilizando la pancromática.
	 * @param bufferInput	buffer de entrada (tipo int)
	 * @param length		longitud del buffer de la pancromatica utilizado
	 * @param iLine			línea leida de la imagen multiespectral
	 * @return				
	 */
	private void processBrovey(int[][] bufferInput, int iLine){    	
		int r=0,g=0,b=0;
		double r1=0, g1=0, b1=0;
		double scale=0;
		double valorPanc=0;
		int line=0, col=0;
		// Para cada elemento Punto de la multiespectral
		for(int iElem=0; iElem<bufferInput[0].length; iElem++){
			r = (int)bufferInput[0][iElem];
			g = (int)bufferInput[1][iElem];
			b = (int)bufferInput[2][iElem];
			for(int j=0;j<cellFactorX;j++){
				for(int k=0;k<cellFactorY;k++){	
					line=(int) (iLine*cellFactorY+j);
					col= (int) (iElem*cellFactorX+k);
					valorPanc =  getValuePanc(line, col);//(highBandGrid.getRasterBuf().getElemByte(line,col,0)&0xff);
					scale = (3*(valorPanc+coef))/(r+g+b+1.0);		
					r1 = r*scale;
					g1 = g*scale; 
					b1 = b*scale;
					rasterResult.setElem(line, col, 0, (int)r1);
					rasterResult.setElem(line, col, 1, (int)g1);
					rasterResult.setElem(line, col, 2, (int)b1);
				}
			}
		}
	
	}
	
	/**
	 * Aplica la operación de refinamiento sobre el buffer Multiespectral que contiene 
	 * el RGB que es pasado por parámetro utilizando la pancromática.
	 * @param bufferInput	buffer de entrada (tipo float)
	 * @param length		longitud del buffer de la pancromatica utilizado
	 * @param iLine			línea leida de la imagen multiespectral
	 * @return				
	 */
	private void processBrovey(float[][] bufferInput, int iLine){    	
		float r=0,g=0,b=0;
		double r1=0, g1=0, b1=0;
		double scale=0;
		double valorPanc=0;
		int line=0, col=0;
		// Para cada elemento Punto de la multiespectral
		for(int iElem=0; iElem<bufferInput[0].length; iElem++){
			r = (float)bufferInput[0][iElem];
			g = (float)bufferInput[1][iElem];
			b = (float)bufferInput[2][iElem];
			for(int j=0;j<cellFactorX;j++){
				for(int k=0;k<cellFactorY;k++){	
					line=(int) (iLine*cellFactorY+j);
					col= (int) (iElem*cellFactorX+k);
					valorPanc =  getValuePanc(line, col);//(highBandGrid.getRasterBuf().getElemByte(line,col,0)&0xff);
					scale = (3*(valorPanc+coef))/(r+g+b+1.0);		
					r1 = r*scale;
					g1 = g*scale; 
					b1 = b*scale;
					rasterResult.setElem(line, col, 0, (float)r1);
					rasterResult.setElem(line, col, 1, (float)g1);
					rasterResult.setElem(line, col, 2, (float)b1);
				}
			}
		}
	}
	
	/**
	 * Aplica la operación de refinamiento sobre el buffer Multiespectral que contiene 
	 * el RGB que es pasado por parámetro utilizando la pancromática.
	 * @param bufferInput	buffer de entrada (tipo double)
	 * @param length		longitud del buffer de la pancromatica utilizado
	 * @param iLine			línea leida de la imagen multiespectral
	 * @return				
	 */
	private void processBrovey(double[][] bufferInput, int iLine){    	
		double r=0,g=0,b=0;
		double r1=0, g1=0, b1=0;
		double scale=0;
		double valorPanc=0;
		int line=0, col=0;
		// Para cada elemento Punto de la multiespectral
		for(int iElem=0; iElem<bufferInput[0].length; iElem++){
			r = (double)bufferInput[0][iElem];
			g = (double)bufferInput[1][iElem];
			b = (double)bufferInput[2][iElem];
			for(int j=0;j<cellFactorX;j++){
				for(int k=0;k<cellFactorY;k++){	
					line=(int) (iLine*cellFactorY+j);
					col= (int) (iElem*cellFactorX+k);
					valorPanc =  getValuePanc(line, col);//(highBandGrid.getRasterBuf().getElemByte(line,col,0)&0xff);
					scale = (3*(valorPanc+coef))/(r+g+b+1.0);		
					r1 = r*scale;
					g1 = g*scale; 
					b1 = b*scale;
					rasterResult.setElem(line, col, 0, (double)r1);
					rasterResult.setElem(line, col, 1, (double)g1);
					rasterResult.setElem(line, col, 2, (double)b1);
				}
			}
		}
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
				value= highBandGrid.getRasterBuf().getElemShort(line,col,0)&0xff;
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
		
	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getLog()
	 */
	public String getLog() {
		if (writerBufferServer==null)
			return PluginServices.getText(this,"fusionando_imagen")+"..." +"\n\nMétodo seleccionado:" +PluginServices.getText(this,"brovey");
		else
			return PluginServices.getText(this,"escribiendo_resultado")+"...";
	}
}

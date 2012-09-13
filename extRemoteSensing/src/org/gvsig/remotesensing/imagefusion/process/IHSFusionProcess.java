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
 * Proceso IHS.
 * 
 * @version 27/02/2008
 * @author Alejandro Muñoz Sánchez (alejandro.munoz@uclm.es)
 * 
 */
public class IHSFusionProcess extends ImageFusionProcess {

	public void init(){
		super.init();
	}
	
	/** Proceso de construccion de la imagen resultado de la fusion*/
	public void process() throws InterruptedException {
		
		byte   [][] pRGBMultiespectralByte   = null;
		short  [][] pRGBMultiespectralShort  = null;
		int    [][] pRGBMultiespectralInt    = null;
		float  [][] pRGBMultiespectralFloat  = null;
		double [][] pRGBMultiespectralDouble = null;
	
		
		int heightMultiespec = inputGrid.getRasterBuf().getHeight();
		insertLineLog(RasterToolsUtil.getText(this, "aplicando_ihs"));
		
		// Caso Bufer tipo byte
		if(inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_BYTE){
			
			rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_BYTE, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), 3, true);
			for(int iLine=0;iLine<heightMultiespec ;iLine++){
				pRGBMultiespectralByte = inputGrid.getRasterBuf().getLineByte(iLine);
				processIHS(pRGBMultiespectralByte, iLine);	    		
				percent= (int)iLine*100/heightMultiespec;	
			}
		}	
		
		// Caso Bufer tipo short
		if(inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_SHORT){
			
			rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_SHORT, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), 3, true);
			for(int iLine=0;iLine<heightMultiespec ;iLine++){
				pRGBMultiespectralShort = inputGrid.getRasterBuf().getLineShort(iLine);
				processIHS(pRGBMultiespectralShort, iLine);	    		
				percent= (int)iLine*100/heightMultiespec;	
			}	
		}
		
		// Caso Bufer tipo int
		if(inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_INT){
			
			rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_INT, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), 3, true);
			for(int iLine=0;iLine<heightMultiespec ;iLine++){
				pRGBMultiespectralInt = inputGrid.getRasterBuf().getLineInt(iLine);
				processIHS(pRGBMultiespectralInt, iLine);	    		
				percent= (int)iLine*100/heightMultiespec;	
			}
		}
		
//		 Caso Bufer tipo int
		if(inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_FLOAT){
			
			rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_FLOAT, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), 3, true);
			for(int iLine=0;iLine<heightMultiespec ;iLine++){
				pRGBMultiespectralInt = inputGrid.getRasterBuf().getLineInt(iLine);
				processIHS(pRGBMultiespectralFloat, iLine);	    		
				percent= (int)iLine*100/heightMultiespec;	
			}
		}
		
//		 Caso Bufer tipo int
		if(inputGrid.getRasterBuf().getDataType()== RasterBuffer.TYPE_DOUBLE){
			
			rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_DOUBLE, highBandGrid.getRasterBuf().getWidth(), highBandGrid.getRasterBuf().getHeight(), 3, true);
			for(int iLine=0;iLine<heightMultiespec ;iLine++){
				pRGBMultiespectralInt = inputGrid.getRasterBuf().getLineInt(iLine);
				processIHS(pRGBMultiespectralDouble, iLine);	    		
				percent= (int)iLine*100/heightMultiespec;	
			}
		}	
		
	writeToFile();
	}
	

	private void processIHS(byte[][] bufferInput, int iLine){    	
		
		int[] uvw , tmp = new int[3];
		int line=0,col=0;
	    double[] xyz;
		
	    for(int iElem=0; iElem<bufferInput[0].length; iElem++){
	    	xyz = colorConversion.RGBtoHSL((int)(bufferInput[0][iElem]& 0xff),(int)(bufferInput[1][iElem]& 0xff),(int)(bufferInput[2][iElem]& 0xff));
			for(int j=0;j<cellFactorX;j++){
				for(int k=0;k<cellFactorY;k++){
					line= (int) (iLine*cellFactorX+j);
					col= (int) (iElem*cellFactorY+k);
					xyz[2] = ((getValuePanc(line,col))/255.0)+coef;
					tmp[0] = (int)(255.0 * xyz[0] / 360.0 + 0.5);
					tmp[2] = (int) (xyz[2]*255. + 0.5);
					tmp[1] = (int) (xyz[1]*255. + 0.5);
					uvw = colorConversion.HSLtoRGB(tmp[0],
								   tmp[1],
								   tmp[2]);
					
					// Evitar desbordamiento ???
					if(uvw[0]>255) uvw[0]=255;
					if(uvw[1]>255) uvw[1]=255;
					if(uvw[2]>255) uvw[2]=255;
					rasterResult.setElem(line, col, 0, (byte)uvw[0]);
					rasterResult.setElem(line, col, 1, (byte)uvw[1]);
					rasterResult.setElem(line, col, 2, (byte)uvw[2]);
				
				}
			}
		} // Fin del prodeso para la linea iLine
	}
	
	
	private void processIHS(short[][] bufferInput, int iLine){    	
		
		int[] uvw , tmp = new int[3];
		int line=0,col=0;
	    double[] xyz;
		
	    for(int iElem=0; iElem<bufferInput[0].length; iElem++){
	    	xyz = colorConversion.RGBtoHSL((int)(bufferInput[0][iElem]),(int)(bufferInput[1][iElem]),(short)(bufferInput[2][iElem]));
			for(int j=0;j<cellFactorX;j++){
				for(int k=0;k<cellFactorY;k++){
					line= (int) (iLine*cellFactorX+j);
					col= (int) (iElem*cellFactorY+k);
					xyz[2] = ((getValuePanc(line,col))/255.0)+ coef;
					tmp[0] = (int)(255.0 * xyz[0] / 360.0 + 0.5);
					tmp[2] = (int) (xyz[2]*255. + 0.5);
					tmp[1] = (int) (xyz[1]*255. + 0.5);
					uvw = colorConversion.HSLtoRGB(tmp[0],
								   tmp[1],
								   tmp[2]);
					
					rasterResult.setElem(line, col, 0, (short)uvw[0]);
					rasterResult.setElem(line, col, 1, (short)uvw[1]);
					rasterResult.setElem(line, col, 2, (short)uvw[2]);
				
				}
			}
		} // Fin del prodeso para la linea iLine
	}
	
	
	
	private void processIHS(int [][] bufferInput, int iLine){    	
		
		int[] uvw , tmp = new int[3];
		int line=0,col=0;
	    double[] xyz;
		
	    for(int iElem=0; iElem<bufferInput[0].length; iElem++){
	    	xyz = colorConversion.RGBtoHSL((int)(bufferInput[0][iElem]),(int)(bufferInput[1][iElem]),(short)(bufferInput[2][iElem]));
			for(int j=0;j<cellFactorX;j++){
				for(int k=0;k<cellFactorY;k++){
					line= (int) (iLine*cellFactorX+j);
					col= (int) (iElem*cellFactorY+k);
					xyz[2] = ((highBandGrid.getRasterBuf().getElemInt(line,col,0))/255.0)+ coef;
					tmp[0] = (int)(255.0 * xyz[0] / 360.0 + 0.5);
					tmp[2] = (int) (xyz[2]*255. + 0.5);
					tmp[1] = (int) (xyz[1]*255. + 0.5);
					uvw = colorConversion.HSLtoRGB(tmp[0],
								   tmp[1],
								   tmp[2]);
					
					rasterResult.setElem(line, col, 0, (int)uvw[0]);
					rasterResult.setElem(line, col, 1, (int)uvw[1]);
					rasterResult.setElem(line, col, 2, (int)uvw[2]);
				
				}
			}
		} // Fin del prodeso para la linea iLine
	}
	
	
	
	
	private void processIHS(float [][] bufferInput, int iLine){    	
		
		int[] uvw , tmp = new int[3];
		int line=0,col=0;
	    double[] xyz;
		
	    for(int iElem=0; iElem<bufferInput[0].length; iElem++){
	    	xyz = colorConversion.RGBtoHSL((int)(bufferInput[0][iElem]),(int)(bufferInput[1][iElem]),(short)(bufferInput[2][iElem]));
			for(int j=0;j<cellFactorX;j++){
				for(int k=0;k<cellFactorY;k++){
					line= (int) (iLine*cellFactorX+j);
					col= (int) (iElem*cellFactorY+k);
					xyz[2] = ((highBandGrid.getRasterBuf().getElemInt(line,col,0))/255.0)+ coef;
					tmp[0] = (int)(255.0 * xyz[0] / 360.0 + 0.5);
					tmp[2] = (int) (xyz[2]*255. + 0.5);
					tmp[1] = (int) (xyz[1]*255. + 0.5);
					uvw = colorConversion.HSLtoRGB(tmp[0],
								   tmp[1],
								   tmp[2]);
					
					rasterResult.setElem(line, col, 0, (float)uvw[0]);
					rasterResult.setElem(line, col, 1, (float)uvw[1]);
					rasterResult.setElem(line, col, 2, (float)uvw[2]);
				
				}
			}
		} // Fin del prodeso para la linea iLine
	}
	
	
	private void processIHS(double [][] bufferInput, int iLine){    	
		
		int[] uvw , tmp = new int[3];
		int line=0,col=0;
	    double[] xyz;
		
	    for(int iElem=0; iElem<bufferInput[0].length; iElem++){
	    	xyz = colorConversion.RGBtoHSL((int)(bufferInput[0][iElem]),(int)(bufferInput[1][iElem]),(short)(bufferInput[2][iElem]));
			for(int j=0;j<cellFactorX;j++){
				for(int k=0;k<cellFactorY;k++){
					line= (int) (iLine*cellFactorX+j);
					col= (int) (iElem*cellFactorY+k);
					xyz[2] = ((highBandGrid.getRasterBuf().getElemInt(line,col,0))/255.0)+ coef;
					tmp[0] = (int)(255.0 * xyz[0] / 360.0 + 0.5);
					tmp[2] = (int) (xyz[2]*255. + 0.5);
					tmp[1] = (int) (xyz[1]*255. + 0.5);
					uvw = colorConversion.HSLtoRGB(tmp[0],
								   tmp[1],
								   tmp[2]);
					
					rasterResult.setElem(line, col, 0, (double)uvw[0]);
					rasterResult.setElem(line, col, 1, (double)uvw[1]);
					rasterResult.setElem(line, col, 2, (double)uvw[2]);
				
				}
			}
		} // Fin del prodeso para la linea iLine
	}
	
	
	/** 
	 * @ return valor de la posicion line col de la banda pancromática
	 * 
	 * */
	private double getValuePanc(int line, int col){

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
			return PluginServices.getText(this,"fusionando_imagen")+"..." +"\n\nMétodo seleccionado:" +PluginServices.getText(this,"ihs");
		else
			return PluginServices.getText(this,"escribiendo_resultado")+"...";
	}

}

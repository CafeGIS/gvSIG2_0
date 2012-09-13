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
import java.util.Arrays;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.RemoteSensingUtils;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/** NoSupervisedClassificationProcess implementa el método de clasificación de
 * no supervisada
 *
 * @see ClassificationGeneralProcess
 *
 * @author Victor Olaya volaya@unex.es
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @version 15/8/2008
*/

public class NoSupervisedClassificationProcess extends ClassificationGeneralProcess{

	double mean[][]=null;
	double dmax[]= null;
	double dmin[]= null;
	int m_iCells[]=null;
	int m_iThreshold=0;
	int iChangedCells=0;

	private static Color[]   	colors  = new Color[] {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN,
													   Color.ORANGE, Color.PINK, Color.WHITE, Color.BLACK};


	/** Metodo que recoge los parametros del proceso de clasificacion no supervisada
	* <LI>rasterSE: Capa de entrada para la clasificación</LI>
	* <LI> bandList:bandas habilitadas </LI>
	* <LI>view: vista sobre la que se carga la capa al acabar el proceso</LI>
	* <LI>filename: path con el fichero de salida</LI>
	*/
	public void init() {

		rasterSE= (FLyrRasterSE)getParam("layer");
		view=(View)getParam("view");
		filename= getStringParam("filename");
		bandList = (int[])getParam("bandList");
		numClases = getIntParam("numClases");
		if (bandList.length == 0 || numClases == 0)
			// no se puede clasificar
			return;
		setGrid();
		rasterResult= RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, inputGrid.getRasterBuf().getWidth(), inputGrid.getRasterBuf().getHeight(), 1, true);
		mean= new double[numClases][inputGrid.getBandCount()];

		dmax=new double [inputGrid.getBandCount()];
		dmin= new double [inputGrid.getBandCount()];

		// Se completan los datos  de
		for(int i=0; i< inputGrid.getBandCount();i++){
			inputGrid.setBandToOperate(i);
			try {
				dmax[i]= inputGrid.getMaxValue();
				dmin[i]= inputGrid.getMinValue();
			} catch (GridException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		double dStep=0.0;
		for (int i = 0; i < inputGrid.getBandCount(); i++){
			dStep = (dmax[i] - dmin[i]) / ((double) (numClases + 1));
			for (int j = 0; j < numClases; j++) {
				mean[j][i] = dmin[i] + dStep * (j + 1);
			}
		}
		m_iThreshold = (int) (inputGrid.getLayerNX()*inputGrid.getNY() * 0.02);
	}

	/** Proceso de clasificacion no supervisada*/
	public void process() throws InterruptedException {

			int i;
			int x,y;

			int iPrevClass=0;
			int iClass=0;
			double dNewMean[][] = null;
			double swap[][];
			m_iCells = new int [numClases];

			do{
				Arrays.fill(m_iCells, (byte)0);
				iChangedCells = 0;
				dNewMean = new double [numClases][inputGrid.getBandCount()];
				for (i = 0; i < numClases; i++){
					Arrays.fill(dNewMean[i], 0.0);
				}
				if(inputGrid.getRasterBuf().getDataType()==RasterBuffer.TYPE_BYTE){
					byte dValues[] = new byte[inputGrid.getBandCount()];
					for(y=0; y<inputGrid.getNY(); y++){
						for(x=0; x<inputGrid.getNX(); x++){
							iPrevClass = rasterResult.getElemByte(y,x,0);
							inputGrid.getRasterBuf().getElemByte(y,x,dValues);
							iClass = getPixelClassForTypeByte(dValues);
							rasterResult.setElem(y, x, 0,(byte)iClass);

							for (i = 0; i < inputGrid.getBandCount(); i++){
								dNewMean[iClass][i] += dValues[i]&0xff;
							}
							m_iCells[iClass]++;
							if (iClass != iPrevClass){
								iChangedCells++;
							}
						}
					}
				}

				if(inputGrid.getRasterBuf().getDataType()==RasterBuffer.TYPE_SHORT){
					short dValues[] = new short[inputGrid.getBandCount()];
					for(y=0; y<inputGrid.getNY(); y++){
						for(x=0; x<inputGrid.getNX(); x++){
							iPrevClass = rasterResult.getElemByte(y,x,0);
							inputGrid.getRasterBuf().getElemShort(y,x,dValues);
							iClass = getPixelClassForTypeShort(dValues);
							rasterResult.setElem(y, x, 0,(byte)iClass);

							for (i = 0; i < inputGrid.getBandCount(); i++){
								dNewMean[iClass][i] += dValues[i];
							}
							m_iCells[iClass]++;
							if (iClass != iPrevClass){
								iChangedCells++;
							}
						}
					}
				}

				if(inputGrid.getRasterBuf().getDataType()==RasterBuffer.TYPE_INT){
					int dValues[] = new int[inputGrid.getBandCount()];
					for(y=0; y<inputGrid.getNY(); y++){
						for(x=0; x<inputGrid.getNX(); x++){
							iPrevClass = rasterResult.getElemByte(y,x,0);
							inputGrid.getRasterBuf().getElemInt(y,x,dValues);
							iClass = getPixelClassForTypeInt(dValues);
							rasterResult.setElem(y, x, 0,(byte)iClass);

							for (i = 0; i < inputGrid.getBandCount(); i++){
								dNewMean[iClass][i] += dValues[i];
							}
							m_iCells[iClass]++;
							if (iClass != iPrevClass){
								iChangedCells++;
							}
						}
					}
				}

				if(inputGrid.getRasterBuf().getDataType()==RasterBuffer.TYPE_FLOAT){
					float dValues[] = new float[inputGrid.getBandCount()];
					for(y=0; y<inputGrid.getNY(); y++){
						for(x=0; x<inputGrid.getNX(); x++){
							iPrevClass = rasterResult.getElemByte(y,x,0);
							inputGrid.getRasterBuf().getElemFloat(y,x,dValues);
							iClass = getPixelClassForTypeFloat(dValues);
							rasterResult.setElem(y, x, 0,(byte)iClass);

							for (i = 0; i < inputGrid.getBandCount(); i++){
								dNewMean[iClass][i] += dValues[i];
							}
							m_iCells[iClass]++;
							if (iClass != iPrevClass){
								iChangedCells++;
							}
						}
					}
				}

				if(inputGrid.getRasterBuf().getDataType()==RasterBuffer.TYPE_DOUBLE){
					double dValues[] = new double[inputGrid.getBandCount()];
					for(y=0; y<inputGrid.getNY(); y++){
						for(x=0; x<inputGrid.getNX(); x++){
							iPrevClass = rasterResult.getElemByte(y,x,0);
							inputGrid.getRasterBuf().getElemDouble(y,x,dValues);
							iClass = getPixelClassForTypeDouble(dValues);
							rasterResult.setElem(y, x, 0,(byte)iClass);

							for (i = 0; i < inputGrid.getBandCount(); i++){
								dNewMean[iClass][i] += dValues[i];
							}
							m_iCells[iClass]++;
							if (iClass != iPrevClass){
								iChangedCells++;
							}
						}
					}
				}

				for (i = 0; i <inputGrid.getBandCount(); i++){
					for (int j = 0; j < numClases; j++) {
						dNewMean[j][i] /= (double)m_iCells[j];
					}
				}

				swap = mean;
				mean= dNewMean;
				dNewMean = swap;

			}while (iChangedCells > m_iThreshold);

		writeToFile();
	}



	public int getPercent() {
		// TODO Auto-generated method stub
		return 0;
	}


	public String getLog(){
		return super.getLog()+"\n\n"+ PluginServices.getText(this,"reclasified_cells")+ iChangedCells +"\n";
	}

	/**
	* Método que implementa el clasificador no supervisado
	* @param  array de tipo byte con los valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel
    */
	public int getPixelClassForTypeByte(byte[] pixelBandsValues) {
		int iClass = 0;
		double dMinDist = Double.MAX_VALUE;
		double dDist=0;
		double dDif=0;

		for (int i = 0; i < numClases; i++) {
			dDist = 0;
			for (int j = 0; j < pixelBandsValues.length; j++) {
				dDif = mean[i][j] - (pixelBandsValues[j]&0xff);
				dDist += (dDif* dDif);
			}
			if (dDist < dMinDist){
				dMinDist = dDist;
				iClass = i;
			}
		}
		return iClass;
	}

	/**
	* Método que implementa el clasificador no supervisado
	* @param  array de tipo double con los valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel
    */
	public int getPixelClassForTypeShort(short[] pixelBandsValues) {
		int iClass = 0;
		double dMinDist = Double.MAX_VALUE;
		double dDist;
		double dDif;

		for (int i = 0; i < numClases; i++) {
			dDist = 0;
			for (int j = 0; j < pixelBandsValues.length; j++) {
				dDif = mean[i][j] - pixelBandsValues[j];
				dDist += (dDif* dDif);
			}
			if (dDist < dMinDist){
				dMinDist = dDist;
				iClass = i;
			}
		}
		return iClass;
	}

	/**
	* Método que implementa el clasificador no supervisado
	* @param  array de tipo int con los valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel
    */
	public int getPixelClassForTypeInt(int[] pixelBandsValues) {
		int iClass = 0;
		double dMinDist = Double.MAX_VALUE;
		double dDist;
		double dDif;

		for (int i = 0; i < numClases; i++) {
			dDist = 0;
			for (int j = 0; j < pixelBandsValues.length; j++) {
				dDif = mean[i][j] - pixelBandsValues[j];
				dDist += (dDif* dDif);
			}
			if (dDist < dMinDist){
				dMinDist = dDist;
				iClass = i;
			}
		}
		return iClass;
	}


	/**
	* Método que implementa el clasificador no supervisado
	* @param  array de tipo float con los valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel
    */
	public int getPixelClassForTypeFloat(float[] pixelBandsValues) {
		int iClass = 0;
		double dMinDist = Double.MAX_VALUE;
		double dDist;
		double dDif;

		for (int i = 0; i < numClases; i++) {
			dDist = 0;
			for (int j = 0; j < pixelBandsValues.length; j++) {
				dDif = mean[i][j] - pixelBandsValues[j];
				dDist += (dDif* dDif);
			}
			if (dDist < dMinDist){
				dMinDist = dDist;
				iClass = i;
			}
		}
		return iClass;
	}


	/**
	* Método que implementa el clasificador no supervisado
	* @param  array de tipo double con los valores del pixel en cada una de las bandas
	* @return clase a la que pertenece el pixel
    */
	public int getPixelClassForTypeDouble(double[] pixelBandsValues) {
		int iClass = 0;
		double dMinDist = Double.MAX_VALUE;
		double dDist;
		double dDif;

		for (int i = 0; i < numClases; i++) {
			dDist = 0;
			for (int j = 0; j < pixelBandsValues.length; j++) {
				dDif = mean[i][j] - pixelBandsValues[j];
				dDist += (dDif* dDif);
			}
			if (dDist < dMinDist){
				dMinDist = dDist;
				iClass = i;
			}
		}
		return iClass;
	}




	public void writeToFile() {
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

			ArrayList colorItems = new ArrayList();
			ColorItem colorItem = null;
			int classValue = 0;
			for (int i=0; i< numClases; i++) {
				colorItem = new ColorItem();
				if(i<10)
					colorItem.setColor(colors[i]);
				else
					colorItem.setColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random()));
				colorItem.setNameClass("class"+i);
				colorItem.setValue(classValue);
				colorItems.add(colorItem);
				classValue++;
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
		e.printStackTrace();
	}
	}
}

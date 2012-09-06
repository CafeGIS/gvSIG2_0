/*
 * Created on 9-ago-2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.buffer;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IDataWriter;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;

/**
 * Test para probar las interpolaciones de un buffer de datos completo. El test creará un 
 * buffer a partir de una imagen de 870x870 y aplicará una reducción usando los métodos
 * de interpolación implementados. Después comparará el resultado con imagenes ya generadas
 * y correctas que se encuentran en el directorio de test-images. 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestBufferInterpolation extends TestCase {
	private String baseDir = "./test-images/";
	private String path = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
	private RasterDataset f = null;	
	private BufferFactory ds = null;
	private int size = 50;
	
	private String fileNeighbour1 = baseDir + "neighbour50x50.tif";
	private String fileBilinear1 = baseDir + "bilinear50x50.tif";
	private String fileInverseDistance1 = baseDir + "inverseDistance50x50.tif";
	private String fileBSpline1 = baseDir + "bspline50x50.tif";
	//private String fileBicubicSpline1 = baseDir + "bicubicspline50x50.tif";
	
	private String fileNeighbour = "/tmp/neighbour.tif";
	private String fileBilinear = "/tmp/bilinear.tif";
	private String fileInverseDistance = "/tmp/invdistance.tif";
	private String fileBSpline = "/tmp/bspline.tif";
	private String fileBicubicSpline = "/tmp/bicubicspline.tif";
	
	static {
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestBufferInterpolation running...");
		int[] drawableBands = {0, 1, 2};
		try {
			f = RasterDataset.open(null, path);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
			return;
		} catch (RasterDriverException e) {
			e.printStackTrace();
			return;
		}
		ds = new BufferFactory(f);
		ds.setDrawableBands(drawableBands);
		try {
			ds.setAreaOfInterest(0, 0, f.getWidth(), f.getHeight());
		} catch (InvalidSetViewException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		RasterBuffer buf = (RasterBuffer)ds.getRasterBuf();
		
		try {
			IBuffer b1 = buf.getAdjustedWindow(size, size, BufferInterpolation.INTERPOLATION_NearestNeighbour);
			convertBufferToTif(fileNeighbour, f.getAffineTransform(), b1);
			b1 = buf.getAdjustedWindow(size, size, BufferInterpolation.INTERPOLATION_Bilinear);
			convertBufferToTif(fileBilinear, f.getAffineTransform(), b1);
			b1 = buf.getAdjustedWindow(size, size, BufferInterpolation.INTERPOLATION_InverseDistance);
			convertBufferToTif(fileInverseDistance, f.getAffineTransform(), b1);
			b1 = buf.getAdjustedWindow(size, size, BufferInterpolation.INTERPOLATION_BSpline);
			convertBufferToTif(fileBSpline, f.getAffineTransform(), b1);
			b1 = buf.getAdjustedWindow(size, size, BufferInterpolation.INTERPOLATION_BicubicSpline);
			convertBufferToTif(fileBicubicSpline, f.getAffineTransform(), b1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}  
		
	}
	
	public void testStack(){
		compareResult(fileNeighbour, fileNeighbour1);
		compareResult(fileBilinear, fileBilinear1);
		compareResult(fileInverseDistance, fileInverseDistance1);
		compareResult(fileBSpline, fileBSpline1);
		//compareResult(fileBicubicSpline, fileBicubicSpline1);
		File file1 = new File(fileNeighbour);
		File file2 = new File(fileBilinear);
		File file3 = new File(fileInverseDistance);
		File file4 = new File(fileBSpline);
		//File file5 = new File(fileBicubicSpline);
		if(file1.exists())
			file1.delete();
		if(file2.exists())
			file2.delete();
		if(file3.exists())
			file3.delete();
		if(file4.exists())
			file4.delete();
		//if(file5.exists())
		//	file5.delete();
	}
	
	/**
	 * Compara dos ficheros raster
	 * @param f1
	 * @param f2
	 */
	private void compareResult(String f1, String f2) {
		int[] drawableBands = {0, 1, 2};
		RasterDataset d1 = null;
		RasterDataset d2 = null;
		try {
			d1 = RasterDataset.open(null, f1);
			d2 = RasterDataset.open(null, f2);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
			return;
		} catch (RasterDriverException e) {
			e.printStackTrace();
			return;
		}
		BufferFactory ds = new BufferFactory(d1);
		ds.setDrawableBands(drawableBands);
		try {
			ds.setAreaOfInterest(0, 0, d1.getWidth(), d1.getHeight());
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		IBuffer b1 = ds.getRasterBuf();
		
		ds = new BufferFactory(d2);
		ds.setDrawableBands(drawableBands);
		try {
			ds.setAreaOfInterest(0, 0, d1.getWidth(), d1.getHeight());
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		IBuffer b2 = ds.getRasterBuf(); 
		
		for (int k = 0; k < b1.getBandCount(); k++) {
			for (int i = 0; i < b1.getHeight(); i++) {
				for (int j = 0; j < b1.getWidth(); j++) {
					switch(b1.getDataType()) {
					case IBuffer.TYPE_BYTE:
						assertEquals(b1.getElemByte(i, j, k), b2.getElemByte(i, j, k));
						break;
					case IBuffer.TYPE_SHORT:
						assertEquals(b1.getElemShort(i, j, k), b2.getElemShort(i, j, k));
						break;
						
					case IBuffer.TYPE_INT:
						assertEquals(b1.getElemInt(i, j, k), b2.getElemInt(i, j, k));
						break;
						
					case IBuffer.TYPE_FLOAT:
						assertEquals((int)b1.getElemFloat(i, j, k), (int)b2.getElemFloat(i, j, k));
						break;
						
					case IBuffer.TYPE_DOUBLE:
						assertEquals((int)b1.getElemDouble(i, j, k), (int)b2.getElemDouble(i, j, k));
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Función para pruebas.
	 * Convierte los ficheros generados por la función cachear en ficheros tif para comprobar que están
	 * bien generados.
	 * @param grf
	 * @param pageBuffer
	 * @param pageLines
	 * @throws IOException
	 */
	private void convertBufferToTif(String fileName, AffineTransform at, IBuffer buffer)throws IOException, InterruptedException, RasterDriverException {
		IDataWriter dataWriter1 = new WriterBufferServer(buffer);
		GeoRasterWriter grw = null;
		try {
			Params params = GeoRasterWriter.getWriter(fileName).getParams();
			params.changeParamValue("blocksize", "7"); //posición 7 del array -> 512
			params.changeParamValue("tfw", "false");
			params.changeParamValue("interleave", new Integer(1));//posición 1 del array -> PIXEL
			grw = GeoRasterWriter.getWriter(dataWriter1, 
											fileName,
											buffer.getBandCount(),
											at,
											buffer.getWidth(), 
											buffer.getHeight(), 
											buffer.getDataType(),
											params,
											null);
			
		} catch (NotSupportedExtensionException e) {
			throw new RasterDriverException("");
		}
		grw.dataWrite();
		grw.writeClose();
	}

}

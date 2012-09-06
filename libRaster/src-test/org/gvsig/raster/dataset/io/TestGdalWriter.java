/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
package org.gvsig.raster.dataset.io;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IDataWriter;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;

/**
 * Test para salvar un raster a tif variando sus parámetros.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestGdalWriter extends TestCase {
	
	private String baseDir = "./test-images/";
	private String path1 = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
	private String out = baseDir + "testGdalWriter";
	private IBuffer buf = null;
	private RasterDataset d = null;
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	static {
		RasterLibrary.wakeUp();
	}
	
	public void setUp() {
		System.err.println("TestGdalWriter running...");
		try {
			d = RasterDataset.open(null, path1);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		BufferFactory bf = new BufferFactory(d);
		bf.setDrawableBands(new int[]{0, 1, 2}); 
		try {
			bf.setAreaOfInterest(0, 0, d.getWidth(), d.getHeight());
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		buf = bf.getRasterBuf();
	}
	
	public void testStack(){
		try {
			File f = new File(out + ".tif");
			f.delete();
			f = new File(out + ".tfw");
			f.delete();
			convertBufferToTif(out + ".tif", d.getAffineTransform(), buf);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
	private void convertBufferToTif(String fileName, AffineTransform at, IBuffer buffer)throws IOException, InterruptedException {
		IDataWriter dataWriter1 = new WriterBufferServer(buffer);
		GeoRasterWriter grw = null;
		try {
			Params params = GeoRasterWriter.getWriter(fileName).getParams();
			params.changeParamValue("blocksize", "7");//posición 7 del array -> 512
			params.changeParamValue("tfw", "true");
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
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		grw.dataWrite();
		grw.writeClose();
	}
}

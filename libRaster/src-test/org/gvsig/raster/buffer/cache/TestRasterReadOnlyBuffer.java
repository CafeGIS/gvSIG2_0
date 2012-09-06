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
package org.gvsig.raster.buffer.cache;

import java.io.IOException;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;

/**
 * Test para la prueba de buffer de solo lectura.
 * Genera una imagen con las bandas alteradas usando un buffer de solo lectura.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestRasterReadOnlyBuffer extends TestCase {
	private IBuffer buf = null;
	private String baseDir = "./test-images/";
	private String path = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
		
	static {
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestRasterReadOnlyHugeBuffer running...");
		
		//Cargamos un buffer sin cachear
		
		RasterDataset f = null;
		try {
			f = RasterDataset.open(null, path);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		//Reducimos el tamaño de la caché para poder trabajar con un volumen de datos reducido
		long cacheSize = RasterLibrary.cacheSize;
		double pageSize = RasterLibrary.pageSize;
		RasterLibrary.cacheSize = 1;
		RasterLibrary.pageSize = 0.2;
		
		BufferFactory ds = new BufferFactory(f);
		ds.setReadOnly(true);
		int[] drawableBands = {2, 1, 0};
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
		buf = ds.getRasterBuf();
		
		try {
			WriterBufferServer writerBufferServer = new WriterBufferServer(buf);
			GeoRasterWriter grw = GeoRasterWriter.getWriter(writerBufferServer, "/tmp/out.tif",
					buf.getBandCount(), f.getAffineTransform(), buf.getWidth(),
					buf.getHeight(), buf.getDataType(), GeoRasterWriter.getWriter("/tmp/out.tif").getParams(), null);
			grw.dataWrite();
			grw.writeClose();
		} catch(NotSupportedExtensionException e) {

		} catch(RasterDriverException e) {

		} catch(InterruptedException e) {

		} catch(IOException e) {

		}
		
		RasterLibrary.cacheSize = cacheSize;
		RasterLibrary.pageSize = pageSize;
	}
	
	public void testStack(){
		/*for (int iBand = 0; iBand < buf.getBandCount(); iBand++)
			for (int iRow = 0; iRow < buf.getHeight(); iRow++) 
				for (int iCol = 0; iCol < buf.getWidth(); iCol++) {
					byte a = buf.getElemByte(iRow, iCol, iBand);
					byte b = bufCache.getElemByte(iRow, iCol, iBand);
					//if(a != b)
						//System.out.println(iRow + " " + iCol + " " + a + " " + b);
					assertEquals(a, b);
				}*/
	}

}

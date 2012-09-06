/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package org.gvsig.raster.buffer.cache;

import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
/**
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestSaveAndLoadPages extends TestCase {
	private boolean       show    = false;
	private String        baseDir = "./test-images/";
	private String        path    = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
	private RasterDataset f       = null;
	private BufferFactory ds      = null;
	private int           pages   = 10;
		
	static{
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestSaveAndLoadPages running...");
		try {
			f = RasterDataset.open(null, path);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
	}
	
	public void testStack() {
		int[] drawableBands = {0, 1, 2};
		
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
		
		int h =  f.getHeight() / pages;
		PageBandBuffer[] pbWriterList = new PageBandBuffer[h];
		CacheDataServer[] cds = new CacheDataServer[h];
				
		long t2, t3, t4;
				long t1 = new Date().getTime();
				
		//Salvamos los PageBuffer
		IBuffer buf = ds.getRasterBuf();
		byte[] data = new byte[3];
		for (int i = 0; i < pages; i++) {
			pbWriterList[i] = new PageBandBuffer(ds.getDataSource().getDataType()[0], f.getWidth(), h, 3, true, i);
			cds[i] = new CacheDataServer(null, 0, i);
			for (int k = 0; k < f.getWidth(); k++) {
				for (int j = 0; j < h; j++) {
					buf.getElemByte((i * h) + j, k, data);
					pbWriterList[i].setElemByte(j, k, data);
				}	
			}
			try {
				cds[i].savePage(pbWriterList[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		t2 = new Date().getTime();
		if (show)
			System.out.println("Time: Cargar PageBuffers y salvar a disco: " + ((t2 - t1) / 1000D) + ", secs.");
		
		//Recuperamos las páginas
		PageBandBuffer[] pbReaderList = new PageBandBuffer[h];
		for (int i = 0; i < pages; i++) {
			pbReaderList[i] = new PageBandBuffer(ds.getDataSource().getDataType()[0], f.getWidth(), h, 3, true, i);
			cds[i].loadPage(pbReaderList[i]);
		}
		
		t3 = new Date().getTime();
		if (show)
			System.out.println("Time: Recuperar páginas de disco: " + ((t3 - t2) / 1000D) + ", secs.");
		
		//Comparamos
		byte[] a = new byte[3];
		byte[] b = new byte[3];
		for (int i = 0; i < pages; i++) {
			for (int k = 0; k < f.getWidth(); k++) {
				for (int j = 0; j < h; j++) {
					pbReaderList[i].getElemByte(j, k, a);
					pbWriterList[i].getElemByte(j, k, b);
					for (int t = 0; t < 3; t++) 
						assertEquals(a[t], b[t]);
				}
			}
		}

		t4 = new Date().getTime();
		if (show)
			System.out.println("Time: Comparaciones: " + ((t4 - t3) / 1000D) + ", secs.");
	}
}
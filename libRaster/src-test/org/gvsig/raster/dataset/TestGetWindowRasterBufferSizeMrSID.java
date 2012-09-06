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
package org.gvsig.raster.dataset;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;

/**
 * Test que compara los resultados de la llamada getWindowRaster en coordenadas pixel 
 * y coordenadas reales para comprobar que producen el mismo resultado para la misma extensión
 * y el mismo tamaño de buffer.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestGetWindowRasterBufferSizeMrSID extends TestCase {
	
	private String baseDir = "./test-images/";
	private String path1 = baseDir + "q101866.sid";
	private RasterDataset f1 = null;
	private RasterDataset f2 = null;
	
	static {
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestGetWindowRasterBufferSizeMrSID running...");
		try {
			f1 = RasterDataset.open(null, path1);
			f2 = RasterDataset.open(null, path1);
		} catch (NotSupportedExtensionException e1) {
			e1.printStackTrace();
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		}
	}
	
	public void testStack(){
		BufferFactory bf1 = new BufferFactory(f1);
		bf1.setAllDrawableBands();
		try {
			bf1.setAreaOfInterest(f1.getExtent().getMin().getX(), f1.getExtent().getMin().getY(), f1.getExtent().getMax().getX(), f1.getExtent().getMax().getY(), 100, 100);
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		} catch (InvalidSetViewException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		IBuffer buf1 = bf1.getRasterBuf();
		
		BufferFactory bf2 = new BufferFactory(f2);
		bf2.setAllDrawableBands();
		try {
			bf2.setAreaOfInterest(0, 0, f2.getWidth(), f2.getHeight(), 100, 100);
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		}
		IBuffer buf2 = bf2.getRasterBuf();
		
		for (int band = 0; band < buf1.getBandCount(); band++) {
			for (int row = 0; row < buf1.getHeight(); row++) {
				for (int col = 0; col < buf1.getWidth(); col++) {
					//try {
					assertEquals(buf1.getElemByte(row, col, band), buf2.getElemByte(row, col, band));
					//System.out.println(buf1.getElemByte(row, col, band) + " " + buf2.getElemByte(row, col, band));
					/*} catch (AssertionFailedError e) {
						System.out.println(band + " " + line + " " + column);
						
					}*/
				}
			}
		}
	}

}

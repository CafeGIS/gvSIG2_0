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
 * Test del uso de la función readBlock de MrSID. Cargará un dataset del formato mrsid y recorrerá todo
 * el raster con la llamada readBlock. El resultado de los datos leidos con esta llamada serán 
 * comparados a los leidos con setAreaOfInterest
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestReadBlockMrSID extends TestCase {
	
	private String baseDir = "./test-images/";
	private String path1 = baseDir + "q101866.sid";
	private RasterDataset f1 = null;
	
	static {
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestReadBlockMrSID running...");
		try {
			f1 = RasterDataset.open(null, path1);
		} catch (NotSupportedExtensionException e1) {
			e1.printStackTrace();
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		}
	}
	
	public void testStack() {
		BufferFactory bf = new BufferFactory(f1);
		bf.setAllDrawableBands();
		try {
			bf.setAreaOfInterest(f1.getExtent().getMin().getX(), f1.getExtent().getMax().getY(), f1.getExtent().width(), f1.getExtent().height());
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		} catch (InvalidSetViewException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		IBuffer buf1 = bf.getRasterBuf();
		
		int block = 100;
		int nblocks = (int)Math.ceil(f1.getHeight() / block);
		try {
			int line = 0;
			int initBand = 0;
			int column = 0;
			for (int i = 0; i < nblocks; i++) {
				byte[][][] buf = (byte[][][])f1.readBlock(i * block, block);
				initBand = i * block;
				for (int band = 0; band < buf.length; band++) {
					line = initBand;
					for (int row = 0; row < buf[band].length; row++) {
						column = 0;
						for (int col = 0; col < buf[band][row].length; col++) {
							//try {
							assertEquals(buf[band][row][col], buf1.getElemByte(line, column, band));
							/*} catch (AssertionFailedError e) {
								System.out.println(band + " " + line + " " + column);
								
							}*/
							column ++;
						}
						line ++;
					}
					
				}
			}
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (FileNotOpenException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}

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
package org.gvsig.raster.dataset;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.grid.GridException;

/**
 * Test para validar la lectura por líneas de un ecw.
 * Muestrea valores sobre toda la extensión leida y comprueba que el resultado sea correcto
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestReadLineEcw extends TestCase {

	private String baseDir = "./test-images/";
	private String path1 = baseDir + "miniraster30x30.jp2";
	
	private RasterDataset f1 = null;
	
	static {
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestReadLineEcw running...");
		try {
			f1 = RasterDataset.open(null, path1);
		} catch (NotSupportedExtensionException e1) {
			e1.printStackTrace();
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public void testStack() {
		try {
			Object line = null;
			for(int iBand = 0; iBand < f1.getBandCount(); iBand ++) {
				for(int iLine = 0; iLine < f1.getHeight(); iLine ++) {
					line = f1.readCompleteLine(iLine, iBand);
					if(f1.getDataType()[iBand] == IBuffer.TYPE_BYTE){
						byte[] b = (byte[])line;
						//printByteLine(b, iLine);
						if(iBand == 0 && iLine == 0)
							testLine0_Banda0(b);
						if(iBand == 0 && iLine == 9)
							testLine9_Banda0(b);
						if(iBand == 0 && iLine == 19)
							testLine19_Banda0(b);
						if(iBand == 0 && iLine == 29)
							testLine29_Banda0(b);
						
						if(iBand == 2 && iLine == 0)
							testLine0_Banda2(b);
						if(iBand == 2 && iLine == 9)
							testLine9_Banda2(b);
						if(iBand == 2 && iLine == 19)
							testLine19_Banda2(b);
						if(iBand == 2 && iLine == 29)
							testLine29_Banda2(b);
					}
				}
			}
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (FileNotOpenException e) {
			e.printStackTrace();
		} catch (GridException e) {
			e.printStackTrace();
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		}
	}
	
	/*private void printByteLine(byte[] line, int nLine){
		System.out.print("Línea: " + nLine + "| ");
		for (int i = 0; i < line.length; i++)
			System.out.print((int)(line[i] & 0x000000ff) + " ");
		System.out.println();
	}*/
	
	public void testLine0_Banda0(byte[] line)throws InvalidSetViewException, FileNotOpenException, GridException{
		assertEquals((int)(line[0] & 0x000000ff), 43);
		assertEquals((int)(line[9] & 0x000000ff), 83);
		assertEquals((int)(line[19] & 0x000000ff), 75);
		assertEquals((int)(line[29] & 0x000000ff), 255);
	}
	
	public void testLine9_Banda0(byte[] line)throws InvalidSetViewException, FileNotOpenException, GridException{
		assertEquals((int)(line[0] & 0x000000ff), 118);
		assertEquals((int)(line[9] & 0x000000ff), 81);
		assertEquals((int)(line[19] & 0x000000ff), 105);
		assertEquals((int)(line[29] & 0x000000ff), 114);
	}
	
	public void testLine19_Banda0(byte[] line)throws InvalidSetViewException, FileNotOpenException, GridException{
		assertEquals((int)(line[0] & 0x000000ff), 70);
		assertEquals((int)(line[9] & 0x000000ff), 49);
		assertEquals((int)(line[19] & 0x000000ff), 63);
		assertEquals((int)(line[29] & 0x000000ff), 147);
	}
	
	public void testLine29_Banda0(byte[] line)throws InvalidSetViewException, FileNotOpenException, GridException{
		assertEquals((int)(line[0] & 0x000000ff), 175);
		assertEquals((int)(line[9] & 0x000000ff), 129);
		assertEquals((int)(line[19] & 0x000000ff), 15);
		assertEquals((int)(line[29] & 0x000000ff), 126);
	}
	
	public void testLine0_Banda2(byte[] line)throws InvalidSetViewException, FileNotOpenException, GridException{
		assertEquals((int)(line[0] & 0x000000ff), 26);
		assertEquals((int)(line[9] & 0x000000ff), 68);
		assertEquals((int)(line[19] & 0x000000ff), 70);
		assertEquals((int)(line[29] & 0x000000ff), 246);
	}
	
	public void testLine9_Banda2(byte[] line)throws InvalidSetViewException, FileNotOpenException, GridException{
		assertEquals((int)(line[0] & 0x000000ff), 96);
		assertEquals((int)(line[9] & 0x000000ff), 93);
		assertEquals((int)(line[19] & 0x000000ff), 93);
		assertEquals((int)(line[29] & 0x000000ff), 102);
	}
	
	public void testLine19_Banda2(byte[] line)throws InvalidSetViewException, FileNotOpenException, GridException{
		assertEquals((int)(line[0] & 0x000000ff), 81);
		assertEquals((int)(line[9] & 0x000000ff), 46);
		assertEquals((int)(line[19] & 0x000000ff), 63);
		assertEquals((int)(line[29] & 0x000000ff), 123);
	}
	
	public void testLine29_Banda2(byte[] line)throws InvalidSetViewException, FileNotOpenException, GridException{
		assertEquals((int)(line[0] & 0x000000ff), 147);
		assertEquals((int)(line[9] & 0x000000ff), 110);
		assertEquals((int)(line[19] & 0x000000ff), 14);
		assertEquals((int)(line[29] & 0x000000ff), 111);
	}

}

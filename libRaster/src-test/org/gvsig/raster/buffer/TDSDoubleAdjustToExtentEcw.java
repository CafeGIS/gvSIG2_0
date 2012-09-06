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
package org.gvsig.raster.buffer;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;

/**
 * Este test prueba el acceso a datos a traves de un DataSource sin resampleo
 * para un ECW con coordenadas reales. 
 * 
 * Lee el raster completo y comprueba que los datos leidos sean correctos 
 * comparando los valores de las cuatro esquinas y algunos valores dentro de la imagen.
 * Se lee un área más grande que el extent completo del raster para comprobar que el ajuste
 * al extent lo hace correctamente.
 * 
 * Después hace selecciona un área dentro de la imagen de 2x2 y compara que los valores
 * leidos sean correctos.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TDSDoubleAdjustToExtentEcw extends TestCase{

	private String baseDir = "./test-images/";
	private String path = baseDir + "miniraster30x30.jp2";	
	private RasterDataset f = null;	
	private BufferFactory ds = null;
	
	public void start(){
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TDSDoubleAdjustToExtentEcw running...");
		//System.out.println("TestDataSource (Real Coord) Adjust To Extent [Ecw] running...");
	}
	
	static{
		RasterLibrary.wakeUp();
	}
	
	public void testStack(){
		int[] drawableBands = {0, 1, 2};
		try {
			f = RasterDataset.open(null, path);
		} catch (NotSupportedExtensionException e) {
			return;
		} catch (RasterDriverException e) {
			return;
		}
		ds = new BufferFactory(f);
		ds.setDrawableBands(drawableBands);
		//Se selecciona un área mayor que el extent para probar que lo ajusta a este 
		try {
			ds.setAreaOfInterest(645860.0, 4923870.0, 130, 130);
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dataTest1();
		//print();
		
		try {
			ds.setAreaOfInterest(645889.0, 4923858.0, 5, 5);
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dataTest2();
		//print();
	}
	
	private void dataTest2(){
		IBuffer raster = ds.getRasterBuf();
		//Upper Left
		assertEquals((int)(raster.getElemByte(0, 0, 0) & 0xff), 121);
		assertEquals((int)(raster.getElemByte(0, 0, 1) & 0xff), 135);
		assertEquals((int)(raster.getElemByte(0, 0, 2) & 0xff), 110);
		//Upper Right
		assertEquals((int)(raster.getElemByte(0, 1, 0) & 0xff), 235);
		assertEquals((int)(raster.getElemByte(0, 1, 1) & 0xff), 224);
		assertEquals((int)(raster.getElemByte(0, 1, 2) & 0xff), 192);
		//Lower Left
		assertEquals((int)(raster.getElemByte(1, 0, 0) & 0xff), 102);
		assertEquals((int)(raster.getElemByte(1, 0, 1) & 0xff), 104);
		assertEquals((int)(raster.getElemByte(1, 0, 2) & 0xff), 83);
		//Lower Right
		assertEquals((int)(raster.getElemByte(1, 1, 0) & 0xff), 95);
		assertEquals((int)(raster.getElemByte(1, 1, 1) & 0xff), 77);
		assertEquals((int)(raster.getElemByte(1, 1, 2) & 0xff), 99);
	}
	
	private void dataTest1(){
		IBuffer raster = ds.getRasterBuf();
		//Upper Left
		assertEquals((int)(raster.getElemByte(0, 0, 0) & 0xff), 43);
		assertEquals((int)(raster.getElemByte(0, 0, 1) & 0xff), 53);
		assertEquals((int)(raster.getElemByte(0, 0, 2) & 0xff), 26);
		//Upper Right
		assertEquals((int)(raster.getElemByte(0, 29, 0) & 0xff), 255);
		assertEquals((int)(raster.getElemByte(0, 29, 1) & 0xff), 252);
		assertEquals((int)(raster.getElemByte(0, 29, 2) & 0xff), 246);
		//Lower Left
		assertEquals((int)(raster.getElemByte(29, 0, 0) & 0xff), 175);
		assertEquals((int)(raster.getElemByte(29, 0, 1) & 0xff), 175);
		assertEquals((int)(raster.getElemByte(29, 0, 2) & 0xff), 147);
		//Lower Right
		assertEquals((int)(raster.getElemByte(29, 29, 0) & 0xff), 126);
		assertEquals((int)(raster.getElemByte(29, 29, 1) & 0xff), 134);
		assertEquals((int)(raster.getElemByte(29, 29, 2) & 0xff), 111);
		
		assertEquals((int)(raster.getElemByte(6, 6, 0) & 0xff), 61);
		assertEquals((int)(raster.getElemByte(6, 6, 1) & 0xff), 84);
		assertEquals((int)(raster.getElemByte(6, 6, 2) & 0xff), 64);
		
		assertEquals((int)(raster.getElemByte(6, 23, 0) & 0xff), 168);
		assertEquals((int)(raster.getElemByte(6, 23, 1) & 0xff), 171);
		assertEquals((int)(raster.getElemByte(6, 23, 2) & 0xff), 164);
		
		assertEquals((int)(raster.getElemByte(23, 6, 0) & 0xff), 46);
		assertEquals((int)(raster.getElemByte(23, 6, 1) & 0xff), 64);
		assertEquals((int)(raster.getElemByte(23, 6, 2) & 0xff), 26);
		
		assertEquals((int)(raster.getElemByte(23, 23, 0) & 0xff), 54);
		assertEquals((int)(raster.getElemByte(23, 23, 1) & 0xff), 57);
		assertEquals((int)(raster.getElemByte(23, 23, 2) & 0xff), 36);
		
		assertEquals((int)(raster.getElemByte(11, 13, 0) & 0xff), 203);
		assertEquals((int)(raster.getElemByte(11, 13, 1) & 0xff), 182);
		assertEquals((int)(raster.getElemByte(11, 13, 2) & 0xff), 181);
		
		assertEquals((int)(raster.getElemByte(22, 21, 0) & 0xff), 71);
		assertEquals((int)(raster.getElemByte(22, 21, 1) & 0xff), 97);
		assertEquals((int)(raster.getElemByte(22, 21, 2) & 0xff), 62);
	}
	
	/**
	 * Imprime todos los pixels de la fuente de datos en RGB
	 */
	/*private void print(){
		IBuffer raster = ds.getRasterBuf();
		for(int line = 0; line < raster.getHeight(); line++){
			for(int col = 0; col < raster.getWidth(); col++)
				System.out.print("(" + (int)(raster.getElemByte(line, col, 0) & 0xff) + " " + (int)(raster.getElemByte(line, col, 1) & 0xff) + " " + (int)(raster.getElemByte(line, col, 2) & 0xff) + ")");
			System.out.println();
		}
	}*/

}
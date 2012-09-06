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
 * para un raster leido con Gdal con coordenadas pixel. 
 * 
 * Lee el raster completo y comprueba que los datos leidos sean correctos 
 * comparando los valores de las cuatro esquinas y algunos valores dentro de la imagen.
 * Después hace selecciona un área dentro de la imagen de 2x2 y compara que los valores
 * leidos sean correctos.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TDSDoubleAdjustToExtentBufferGdal extends TestCase{

	private String baseDir = "./test-images/";
	private String path = baseDir + "miniRaster25x24.tif";	
	private RasterDataset f = null;	
	private BufferFactory ds = null;
	
	public void start(){
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TDSDoubleAdjustToExtentBufferGdal running...");
		//System.out.println("TestDataSource (Real Coord) Adjust To Extent With Buffer [Gdal] running...");
	}
	
	static{
		RasterLibrary.wakeUp();
	}
	
	public void testStack(){
		int[] drawableBands = {0, 1, 2};
		try {
			f = RasterDataset.open(null, path);
		} catch (RasterDriverException e) {
			e.printStackTrace();
			return;
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
			return;
		}
		ds = new BufferFactory(f);
		ds.setDrawableBands(drawableBands);
		try {
			ds.setAreaOfInterest(645817.0, 4923851.0, 645853.0, 4923815.0, 10, 10);
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//print();
		dataTest1();
		
		try {
			ds.setAreaOfInterest(645826.9, 4923844.0, 645838.3, 4923833.3, 2, 2);
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
		assertEquals((int)(raster.getElemByte(0, 0, 0) & 0xff), 24);
		assertEquals((int)(raster.getElemByte(0, 0, 1) & 0xff), 37);
		assertEquals((int)(raster.getElemByte(0, 0, 2) & 0xff), 7);
		//Upper Right
		assertEquals((int)(raster.getElemByte(0, 1, 0) & 0xff), 70);
		assertEquals((int)(raster.getElemByte(0, 1, 1) & 0xff), 78);
		assertEquals((int)(raster.getElemByte(0, 1, 2) & 0xff), 55);
		//Lower Left
		assertEquals((int)(raster.getElemByte(1, 0, 0) & 0xff), 97);
		assertEquals((int)(raster.getElemByte(1, 0, 1) & 0xff), 115);
		assertEquals((int)(raster.getElemByte(1, 0, 2) & 0xff), 73);
		//Lower Right
		assertEquals((int)(raster.getElemByte(1, 1, 0) & 0xff), 65);
		assertEquals((int)(raster.getElemByte(1, 1, 1) & 0xff), 75);
		assertEquals((int)(raster.getElemByte(1, 1, 2) & 0xff), 51);
	}
	
	private void dataTest1(){
		IBuffer raster = ds.getRasterBuf();
		//Upper Left
		assertEquals((int)(raster.getElemByte(0, 0, 0) & 0xff), 97);
		assertEquals((int)(raster.getElemByte(0, 0, 1) & 0xff), 101);
		assertEquals((int)(raster.getElemByte(0, 0, 2) & 0xff), 68);
		//Upper Right
		assertEquals((int)(raster.getElemByte(0, 9, 0) & 0xff), 58);
		assertEquals((int)(raster.getElemByte(0, 9, 1) & 0xff), 85);
		assertEquals((int)(raster.getElemByte(0, 9, 2) & 0xff), 42);
		//Lower Left
		assertEquals((int)(raster.getElemByte(9, 0, 0) & 0xff), 164);
		assertEquals((int)(raster.getElemByte(9, 0, 1) & 0xff), 158);
		assertEquals((int)(raster.getElemByte(9, 0, 2) & 0xff), 144);
		//Lower Right
		assertEquals((int)(raster.getElemByte(9, 9, 0) & 0xff), 135);
		assertEquals((int)(raster.getElemByte(9, 9, 1) & 0xff), 126);
		assertEquals((int)(raster.getElemByte(9, 9, 2) & 0xff), 127);
		
		assertEquals((int)(raster.getElemByte(6, 6, 0) & 0xff), 120);
		assertEquals((int)(raster.getElemByte(6, 6, 1) & 0xff), 127);
		assertEquals((int)(raster.getElemByte(6, 6, 2) & 0xff), 96);
		
		assertEquals((int)(raster.getElemByte(2, 3, 0) & 0xff), 24);
		assertEquals((int)(raster.getElemByte(2, 3, 1) & 0xff), 37);
		assertEquals((int)(raster.getElemByte(2, 3, 2) & 0xff), 7);
		
		assertEquals((int)(raster.getElemByte(5, 4, 0) & 0xff), 88);
		assertEquals((int)(raster.getElemByte(5, 4, 1) & 0xff), 103);
		assertEquals((int)(raster.getElemByte(5, 4, 2) & 0xff), 82);
		
		assertEquals((int)(raster.getElemByte(9, 2, 0) & 0xff), 157);
		assertEquals((int)(raster.getElemByte(9, 2, 1) & 0xff), 143);
		assertEquals((int)(raster.getElemByte(9, 2, 2) & 0xff), 130);
		
		assertEquals((int)(raster.getElemByte(7, 8, 0) & 0xff), 222);
		assertEquals((int)(raster.getElemByte(7, 8, 1) & 0xff), 222);
		assertEquals((int)(raster.getElemByte(7, 8, 2) & 0xff), 220);
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
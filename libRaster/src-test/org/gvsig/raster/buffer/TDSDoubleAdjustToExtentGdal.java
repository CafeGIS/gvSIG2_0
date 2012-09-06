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
public class TDSDoubleAdjustToExtentGdal extends TestCase{

	private String baseDir = "./test-images/";
	private String path = baseDir + "miniRaster25x24.tif";	
	private RasterDataset f = null;	
	private BufferFactory ds = null;
	
	public void start(){
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TDSDoubleAdjustToExtentGdal running...");
		//System.out.println("TestDataSource (Real Coord) Adjust To Extent [Gdal] running...");
	}
	
	static{
		RasterLibrary.wakeUp();
	}
	
	public void testStack(){
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
			ds.setAreaOfInterest(645817.0, 4923851.0, 40, 40);
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
			ds.setAreaOfInterest(645829.8, 4923840.4, 2, 2);
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
		assertEquals((int)(raster.getElemByte(0, 0, 0) & 0xff), 64);
		assertEquals((int)(raster.getElemByte(0, 0, 1) & 0xff), 81);
		assertEquals((int)(raster.getElemByte(0, 0, 2) & 0xff), 39);
		//Upper Right
		assertEquals((int)(raster.getElemByte(0, 1, 0) & 0xff), 64);
		assertEquals((int)(raster.getElemByte(0, 1, 1) & 0xff), 81);
		assertEquals((int)(raster.getElemByte(0, 1, 2) & 0xff), 39);
		//Lower Left
		assertEquals((int)(raster.getElemByte(1, 0, 0) & 0xff), 64);
		assertEquals((int)(raster.getElemByte(1, 0, 1) & 0xff), 81);
		assertEquals((int)(raster.getElemByte(1, 0, 2) & 0xff), 39);
		//Lower Right
		assertEquals((int)(raster.getElemByte(1, 1, 0) & 0xff), 64);
		assertEquals((int)(raster.getElemByte(1, 1, 1) & 0xff), 81);
		assertEquals((int)(raster.getElemByte(1, 1, 2) & 0xff), 39);
	}
	
	private void dataTest1(){
		IBuffer raster = ds.getRasterBuf();
		//Upper Left
		assertEquals((int)(raster.getElemByte(0, 0, 0) & 0xff), 14);
		assertEquals((int)(raster.getElemByte(0, 0, 1) & 0xff), 14);
		assertEquals((int)(raster.getElemByte(0, 0, 2) & 0xff), 0);
		//Upper Right
		assertEquals((int)(raster.getElemByte(0, 24, 0) & 0xff), 68);
		assertEquals((int)(raster.getElemByte(0, 24, 1) & 0xff), 90);
		assertEquals((int)(raster.getElemByte(0, 24, 2) & 0xff), 52);
		//Lower Left
		assertEquals((int)(raster.getElemByte(23, 0, 0) & 0xff), 129);
		assertEquals((int)(raster.getElemByte(23, 0, 1) & 0xff), 122);
		assertEquals((int)(raster.getElemByte(23, 0, 2) & 0xff), 106);
		//Lower Right
		assertEquals((int)(raster.getElemByte(23, 24, 0) & 0xff), 145);
		assertEquals((int)(raster.getElemByte(23, 24, 1) & 0xff), 140);
		assertEquals((int)(raster.getElemByte(23, 24, 2) & 0xff), 134);
		
		assertEquals((int)(raster.getElemByte(6, 6, 0) & 0xff), 21);
		assertEquals((int)(raster.getElemByte(6, 6, 1) & 0xff), 37);
		assertEquals((int)(raster.getElemByte(6, 6, 2) & 0xff), 10);
		
		assertEquals((int)(raster.getElemByte(6, 23, 0) & 0xff), 91);
		assertEquals((int)(raster.getElemByte(6, 23, 1) & 0xff), 105);
		assertEquals((int)(raster.getElemByte(6, 23, 2) & 0xff), 92);
		
		assertEquals((int)(raster.getElemByte(23, 6, 0) & 0xff), 153);
		assertEquals((int)(raster.getElemByte(23, 6, 1) & 0xff), 133);
		assertEquals((int)(raster.getElemByte(23, 6, 2) & 0xff), 122);
		
		assertEquals((int)(raster.getElemByte(9, 14, 0) & 0xff), 63);
		assertEquals((int)(raster.getElemByte(9, 14, 1) & 0xff), 69);
		assertEquals((int)(raster.getElemByte(9, 14, 2) & 0xff), 55);
		
		assertEquals((int)(raster.getElemByte(6, 13, 0) & 0xff), 70);
		assertEquals((int)(raster.getElemByte(6, 13, 1) & 0xff), 78);
		assertEquals((int)(raster.getElemByte(6, 13, 2) & 0xff), 55);
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
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
 * Este test prueba el acceso a datos a traves de un DataSource con resampleo
 * para un raster leido con Gdal con coordenadas pixel. 
 * 
 * Lee el raster completo y comprueba que los datos leidos sean correctos 
 * comparando los valores de las cuatro esquinas y algunos valores dentro de la imagen.
 * Después hace selecciona un área dentro de la imagen de 3x3 y compara que los valores
 * leidos sean correctos.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TDSIntBufferGdal extends TestCase{

	private String baseDir = "./test-images/";
	private String path = baseDir + "miniRaster25x24.tif";
	private RasterDataset f = null;	
	private BufferFactory ds = null;
	
	public void start(){
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TDSIntBufferGdal running...");
		//System.out.println("TestDataSource (Pixel Coord) Buffer [Gdal] running...");
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
			ds.setAreaOfInterest(0, 0, 25, 24, 5, 5);
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		dataTest1();
		//print();
		
		try {
			ds.setAreaOfInterest(5, 5, 15, 15, 3, 3);
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		dataTest2();
		//print();
	}
	
	private void dataTest2(){
		IBuffer raster = ds.getRasterBuf();
		//Upper Left
		assertEquals((int)(raster.getElemByte(0, 0, 0) & 0xff), 61);
		assertEquals((int)(raster.getElemByte(0, 0, 1) & 0xff), 66);
		assertEquals((int)(raster.getElemByte(0, 0, 2) & 0xff), 59);
		//Upper Right
		assertEquals((int)(raster.getElemByte(0, 2, 0) & 0xff), 91);
		assertEquals((int)(raster.getElemByte(0, 2, 1) & 0xff), 101);
		assertEquals((int)(raster.getElemByte(0, 2, 2) & 0xff), 77);
		//Lower Left
		assertEquals((int)(raster.getElemByte(2, 0, 0) & 0xff), 112);
		assertEquals((int)(raster.getElemByte(2, 0, 1) & 0xff), 126);
		assertEquals((int)(raster.getElemByte(2, 0, 2) & 0xff), 103);
		//Lower Right
		assertEquals((int)(raster.getElemByte(2, 2, 0) & 0xff), 178);
		assertEquals((int)(raster.getElemByte(2, 2, 1) & 0xff), 187);
		assertEquals((int)(raster.getElemByte(2, 2, 2) & 0xff), 184);
	}
	
	private void dataTest1(){
		IBuffer raster = ds.getRasterBuf();
		//Upper Left
		assertEquals((int)(raster.getElemByte(0, 0, 0) & 0xff), 58);
		assertEquals((int)(raster.getElemByte(0, 0, 1) & 0xff), 86);
		assertEquals((int)(raster.getElemByte(0, 0, 2) & 0xff), 37);
		//Upper Right
		assertEquals((int)(raster.getElemByte(0, 4, 0) & 0xff), 30);
		assertEquals((int)(raster.getElemByte(0, 4, 1) & 0xff), 47);
		assertEquals((int)(raster.getElemByte(0, 4, 2) & 0xff), 11);
		//Lower Left
		assertEquals((int)(raster.getElemByte(4, 0, 0) & 0xff), 164);
		assertEquals((int)(raster.getElemByte(4, 0, 1) & 0xff), 158);
		assertEquals((int)(raster.getElemByte(4, 0, 2) & 0xff), 144);
		//Lower Right
		assertEquals((int)(raster.getElemByte(4, 4, 0) & 0xff), 135);
		assertEquals((int)(raster.getElemByte(4, 4, 1) & 0xff), 126);
		assertEquals((int)(raster.getElemByte(4, 4, 2) & 0xff), 127);
		
		assertEquals((int)(raster.getElemByte(1, 1, 0) & 0xff), 61);
		assertEquals((int)(raster.getElemByte(1, 1, 1) & 0xff), 66);
		assertEquals((int)(raster.getElemByte(1, 1, 2) & 0xff), 59);
		
		assertEquals((int)(raster.getElemByte(2, 2, 0) & 0xff), 81);
		assertEquals((int)(raster.getElemByte(2, 2, 1) & 0xff), 91);
		assertEquals((int)(raster.getElemByte(2, 2, 2) & 0xff), 67);
		
		assertEquals((int)(raster.getElemByte(3, 3, 0) & 0xff), 178);
		assertEquals((int)(raster.getElemByte(3, 3, 1) & 0xff), 187);
		assertEquals((int)(raster.getElemByte(3, 3, 2) & 0xff), 184);
		
		assertEquals((int)(raster.getElemByte(1, 3, 0) & 0xff), 91);
		assertEquals((int)(raster.getElemByte(1, 3, 1) & 0xff), 101);
		assertEquals((int)(raster.getElemByte(1, 3, 2) & 0xff), 77);
		
		assertEquals((int)(raster.getElemByte(3, 1, 0) & 0xff), 112);
		assertEquals((int)(raster.getElemByte(3, 1, 1) & 0xff), 126);
		assertEquals((int)(raster.getElemByte(3, 1, 2) & 0xff), 103);
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
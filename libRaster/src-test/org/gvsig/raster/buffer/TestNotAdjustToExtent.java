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
import org.gvsig.raster.dataset.MultiRasterDataset;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
/**
 * Prueba del acceso a datos usando el flag setAdjustToExtent a false. Esto consigue que 
 * no se ajuste la petición a los margenes del raster sino que se dibuje dentro y el resto 
 * se rellene con valores NoData.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestNotAdjustToExtent extends TestCase{

	private String baseDir = "./test-images/";
	private String path = baseDir + "miniRaster25x24.tif";
	private MultiRasterDataset f = null;	
	private BufferFactory ds = null;
	
	public void start(){
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestAdjustToExtent running...");
	}
	
	static{
		RasterLibrary.wakeUp();
	}
	
	public void testStack(){
		int[] drawableBands = {0, 1, 2};
		try {
			f = MultiRasterDataset.open(null, path);
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
			ds.setAdjustToExtent(false);
			ds.setAreaOfInterest(645800.0, 4923860.0, 645830.0, 4923830, 10, 10); //Superior izquierda
			//ds.setAreaOfInterest(645841.0, 4923860.0, 645871.0, 4923830, 10, 10); //Superior derecha
			//ds.setAreaOfInterest(645841.0, 4923837.0, 645871.0, 4923807.0, 10, 10); //Inferior derecha
			//ds.setAreaOfInterest(645800.0, 4923837.0, 645830.0, 4923807.0, 10, 10); //Inferior izquierda
			//ds.setAreaOfInterest(645810.0, 4923860.0, 645862.0, 4923807.0, 10, 10); //Centro
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
			return;
		}
		//print();
		dataTest1();
		
	}
		
	private void dataTest1(){
		IBuffer raster = ds.getRasterBuf();
		//Upper Left
		assertEquals((int)(raster.getElemByte(4, 7, 0) & 0xff), 97);
		assertEquals((int)(raster.getElemByte(4, 7, 1) & 0xff), 101);
		assertEquals((int)(raster.getElemByte(4, 7, 2) & 0xff), 68);
		
		//Prueba de escritura en disco
//		WriterBufferServer writerBufferServer = new WriterBufferServer();
//		writerBufferServer.setBuffer(raster, -1);
//		
//		try {
//			GeoRasterWriter geoRasterWriter = GeoRasterWriter.getWriter(writerBufferServer, "/tmp/test.tif", 3, new AffineTransform(1, 0, 0, -1, 0, 0), raster.getWidth(), raster.getHeight(), raster.getDataType(), GeoRasterWriter.getWriter("test.tif").getParams(), null);
//			geoRasterWriter.dataWrite();
//			geoRasterWriter.writeClose();
//		} catch (NotSupportedExtensionException e) {
//			e.printStackTrace();
//		} catch (RasterDriverException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
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
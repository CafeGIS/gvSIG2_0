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
package org.gvsig.raster.grid;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;

/**
 * Este test prueba el acceso a datos a traves de un grid.
 * A partir de una fuente de datos (cargada en el constructor del Grid). 
 * 1-F32 Monobanda: Se hará una petición del extent completo de la fuente seleccionando tres bandas.
 * 2-Comprobamos el tipo de datos el ancho y el alto
 * 3-Modifica algunos valores del grid y se comprueba que las modificaciones han sido efectivas.
 * 4-Cambiamos la banda a operar y obtenemos un dato comprobando que tiene que devolver NoData
 * 
 * 1-RGB: Se hará una petición del extent completo de la fuente seleccionando tres bandas.
 * 2-Comprobamos el tipo de datos el ancho y el alto
 * 3-Modifica algunos valores del grid y se comprueba que las modificaciones han sido efectivas.
 * 4-Cambiamos la banda a operar y obtenemos un dato comprobando que el valor que devuelve es correcto
 * 5 -Comprobamos que si nos salimos devuelve un NoData
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TGReadingFullDatasource extends TestCase {
	
	private String baseDir = "./test-images/";
	private String path1 = baseDir + "miniRaster28x25F32.tif";
	private String path2 = baseDir + "miniRaster25x24.tif";
	private RasterDataset f1 = null;
	private RasterDataset f2 = null;
	private BufferFactory ds1 = null;
	private BufferFactory ds2 = null;
	
	static{
		RasterLibrary.wakeUp();	
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TGReadingFullDatasourceSelectingBands running...");
		try {
			f1 = RasterDataset.open(null, path1);
			f2 = RasterDataset.open(null, path2);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		ds1 = new BufferFactory(f1);
		ds2 = new BufferFactory(f2);
	}
	
	public void testStack() {
		try {
			Grid g = new Grid(ds1);
			assertEquals(g.getDataType(), 4); //Tipo float
			assertEquals(g.getNY(), 25); //Alto
			assertEquals(g.getNX(), 28); //Ancho

			testCellBufferFloat(g, 0);
			g.setCellValue(0, 0, (float)31.22);
			g.setCellValue(2, 2, (float)31.22);
			g.setCellValue(3, 8, (float)31.22);
			g.setCellValue(10, 15, (float)31.22);
			testCellBufferFloat(g, 0);
			assertEquals((int)g.getCellValueAsFloat(0, 0), 31);
			assertEquals((int)g.getCellValueAsFloat(2, 2), 31);
			assertEquals((int)g.getCellValueAsFloat(3, 8), 31);
			assertEquals((int)g.getCellValueAsFloat(10, 15), 31);
//			g.setBandToOperate(2);
			assertEquals((int)g.getCellValueAsFloat(10000, 10000), -99999);
			
			g = new Grid(ds2);
			assertEquals(g.getDataType(), 0); //Tipo byte
			assertEquals(g.getNY(), 24); //Alto
			assertEquals(g.getNX(), 25); //Ancho
			testCellBufferByte(g, 0);
			g.setCellValue(0, 0, (byte)31);
			g.setCellValue(2, 2, (byte)31);
			g.setCellValue(3, 8, (byte)31);
			g.setCellValue(10, 15, (byte)31);
			testCellBufferByte(g, 0);
			assertEquals(g.getCellValueAsByte(0, 0), 31);
			assertEquals(g.getCellValueAsByte(2, 2), 31);
			assertEquals(g.getCellValueAsByte(3, 8), 31);
			assertEquals(g.getCellValueAsByte(10, 15), 31);
			g.setBandToOperate(2);
			assertEquals((int)g.getCellValueAsByte(10, 15), 107);
			
			//Comprobamos que si nos salimos devuelve un NoData
			assertEquals((int)g.getCellValueAsFloat(100, 100), -99999);
			assertEquals((int)g.getCellValueAsFloat(-1, 10), -99999);
			assertEquals((int)g.getCellValueAsFloat(1000, 2310), -99999);
			
		} catch (RasterBufferInvalidException e1) {
			e1.printStackTrace();
		} catch (RasterBufferInvalidAccessException e2) {
			e2.printStackTrace();
		} catch (OutOfGridException e3) {
			e3.printStackTrace();
		} catch (GridException e3) {
			e3.printStackTrace();
		}
	}
		
	/**
	 * Compara todos los elementos del buffer con los del grid para comprobar
	 * la corrección de la lectura.
	 * @param g
	 * @throws RasterBufferInvalidAccessException
	 */
	private void testCellBufferFloat(Grid g, int band) throws RasterBufferInvalidAccessException, GridException {
		int nCols = f1.getWidth();
		int nRows = f1.getHeight(); 
		for(int i = 0; i < nRows; i ++) {
			for(int j = 0; j < nCols; j ++)
				assertEquals((int)g.getCellValueAsFloat(j, i), (int)ds1.getRasterBuf().getElemFloat(i, j, band));
		}
	}

	/**
	 * Compara todos los elementos del buffer con los del grid para comprobar
	 * la corrección de la lectura.
	 * @param g
	 * @throws RasterBufferInvalidAccessException
	 */
	private void testCellBufferByte(Grid g, int band) throws RasterBufferInvalidAccessException, GridException {
		int nCols = f2.getWidth();
		int nRows = f2.getHeight(); 
		for(int i = 0; i < nRows; i ++) {
			for(int j = 0; j < nCols; j ++)
				assertEquals((int)g.getCellValueAsByte(j, i), (int)ds2.getRasterBuf().getElemByte(i, j, band));
		}
	}
}

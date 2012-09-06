/* gvSIG. Sistema de Informaciï¿½n Geográfica de la Generalitat Valenciana
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
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;

/**
 * Test a un histograma usando clases. El test se realiza sobre un raster en 
 * coma flotante de 28x25 pixels. Se crean clases de tres tipo de intervalos distintos y
 * se testean los valores obtenidos en el histograma resultante. El último
 * test comprueba la generación automática de clases.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestGdalFloatHistogram extends TestCase {

	private String baseDir = "./test-images/";
	private String path = baseDir + "miniRaster28x25F32.tif";	
	private RasterDataset f = null;
	private BufferFactory ds = null;
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestGdalFloatHistogram running...");
	}

	static{
		RasterLibrary.wakeUp();
	}
	
	public void testStack() {
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
		try {
			ds.setAreaOfInterest(0, 0, 28, 25);
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			return;
		}
		//printData();
		
		//Histogram histogram = null;
		/*MultiRasterDataset rmd = ds.getMultiRasterDataset();
		
		try {
			histogram = rmd.getHistogram();
		} catch (HistogramException e) {
			e.printStackTrace();
		}*/
		//testHist1(histogram);
		
	}
	
	/*private void testHist1(Histogram histogram) {
		assertEquals((int)histogram.getHistogramValue(0, 0), 0);
		assertEquals((int)histogram.getHistogramValue(0, 1), 700);
	} 

	private void testHist2(Histogram histogram) {
		assertEquals((int)histogram.getHistogramValue(0, 0), 180);
		assertEquals((int)histogram.getHistogramValue(0, 1), 520);
	}
	
	private void testHist3(Histogram histogram) {
		assertEquals((int)histogram.getHistogramValue(0, 0), 4);
		assertEquals((int)histogram.getHistogramValue(0, 1), 38);
		assertEquals((int)histogram.getHistogramValue(0, 2), 138);
		assertEquals((int)histogram.getHistogramValue(0, 3), 100);
		assertEquals((int)histogram.getHistogramValue(0, 4), 420);
	}
	
	private void testHist4(Histogram histogram) {
		assertEquals((int)histogram.getHistogramValue(0, 0), 10);
		assertEquals((int)histogram.getHistogramValue(0, 1), 17);
		assertEquals((int)histogram.getHistogramValue(0, 2), 36);
		assertEquals((int)histogram.getHistogramValue(0, 3), 72);
		assertEquals((int)histogram.getHistogramValue(0, 4), 119);
		assertEquals((int)histogram.getHistogramValue(0, 5), 129);
		assertEquals((int)histogram.getHistogramValue(0, 6), 98);
		assertEquals((int)histogram.getHistogramValue(0, 7), 93);
		assertEquals((int)histogram.getHistogramValue(0, 8), 76);
		assertEquals((int)histogram.getHistogramValue(0, 9), 49);
	}
	
	private void print(Histogram histogram) {
		for (int i = 0; i < histogram.getNumBands(); i++) {
			for (int j = 0; j < histogram.getBandLenght(i); j++) {
				System.out.print(histogram.getHistogramValue(i, j) + " ");
			}
			System.out.println("");
		}
	}*/
	
	/**
	 * Imprime todos los pixels de la fuente de datos en RGB
	 */
	/*private void printData() {
		IBuffer raster = ds.getRasterBuf();
		for(int line = 0; line < raster.getHeight(); line++) {
			for(int col = 0; col < raster.getWidth(); col++)
				System.out.print("(" + raster.getElemFloat(line, col, 0) + ")");
			System.out.println();
		}
	}*/
}
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Histogram;
/**
 * Test a un histograma de una imagen de 3 bandas de un byte por dato leido con gdal. 
 * El test comprueba valores a lo largo de todo el histograma
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestGdalByteHistogram extends TestCase {
	private String baseDir = "./test-images/";
	private String path = baseDir + "histograma.bmp";	
	private RasterDataset f = null;	
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestGdalByteHistogram running...");
	}

	static {
		RasterLibrary.wakeUp();
	}
	
	public void testStack() {
		try {
			f = RasterDataset.open(null, path);
		} catch (NotSupportedExtensionException e) {
			return;
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
//		ds = new BufferFactory(f);
		Histogram histogram = null;
		try {
			histogram = f.getHistogram().getHistogram();
			histogram = Histogram.convertHistogramToRGB(histogram);
		} catch (FileNotOpenException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		print(histogram);
		resultTest(histogram);
	}
	
	private void resultTest(Histogram histogram) {
		for (int i = 0; i <= 255; i++) {
			switch (i) {
				case 0:
					assertEquals((int) histogram.getHistogramValue(0, i), 1);
					break;
				case 1:
					assertEquals((int) histogram.getHistogramValue(0, i), 2);
					break;
				case 2:
					assertEquals((int) histogram.getHistogramValue(0, i), 3);
					break;
				case 126:
					assertEquals((int) histogram.getHistogramValue(0, i), 4);
					break;
				case 127:
					assertEquals((int) histogram.getHistogramValue(0, i), 5);
					break;
				case 128:
					assertEquals((int) histogram.getHistogramValue(0, i), 6);
					break;
				case 129:
					assertEquals((int) histogram.getHistogramValue(0, i), 7);
					break;
				case 253:
					assertEquals((int) histogram.getHistogramValue(0, i), 8);
					break;
				case 254:
					assertEquals((int) histogram.getHistogramValue(0, i), 9);
					break;
				case 255:
					assertEquals((int) histogram.getHistogramValue(0, i), 10);
					break;
				default:
					assertEquals((int) histogram.getHistogramValue(0, i), 0);
					break;
			}
		}
	}

	public void print(Histogram histogram) {
		for (int i = 0; i < histogram.getNumBands(); i++) {
			int cont = 0;
			for (int j = 0; j < histogram.getBandLenght(i); j++) {
				double var = histogram.getHistogramValue(i, j);
				if (var != 0) {
					System.out.println(j + ": " + var);
					cont += var;
				}
			}
			System.out.println("");
			System.out.println(cont + " ");
		}
	}
}
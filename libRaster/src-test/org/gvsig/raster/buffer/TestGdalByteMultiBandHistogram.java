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

import org.gvsig.raster.BaseTestCase;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.HistogramException;
/**
 * Test a un histograma de una imagen de 3 bandas de un byte por dato leido con gdal. 
 * El test comprueba valores a lo largo de todo el histograma
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestGdalByteMultiBandHistogram extends BaseTestCase {
	private boolean printCode = false;
	private String  baseDir   = "./test-images/";
	private String  path1     = baseDir + "band1-30x28byte.tif";
	private String  path2     = baseDir + "band2-30x28byte.tif";
	private String  path3     = baseDir + "band3-30x28byte.tif";

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TestGdalByteMultiBandHistogram running...");
	}

	static {
		RasterLibrary.wakeUp();
	}

	public void testStack() {
		try {
			deleteRMF(path1);
			deleteRMF(path2);
			deleteRMF(path3);
			RasterDataset f1 = RasterDataset.open(null, path1);
			RasterDataset f2 = RasterDataset.open(null, path2);
			RasterDataset f3 = RasterDataset.open(null, path3);

			BufferFactory ds = new BufferFactory(f1);
			ds.addFile(f2);
			ds.addFile(f3);

			IRasterDataSource rmd = ds.getDataSource();
			Histogram histogram = rmd.getHistogram();
			histogram = Histogram.convertHistogramToRGB(histogram);

			if (printCode)
				print(histogram);

			int band0[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 9, 12, 23, 33, 32, 29, 17, 29, 11, 9, 9, 8, 3, 3, 3, 1, 1, 0, 1, 6, 4, 5, 3, 2, 7, 6, 10, 8, 12, 12, 4, 5, 16, 13, 11, 13, 13, 13, 21, 20, 16, 27, 14, 14, 21, 23, 20, 20, 15, 17, 12, 9, 20, 15, 7, 11, 5, 10, 6, 5, 8, 4, 10, 6, 5, 5, 4, 1, 5, 2, 3, 3, 6, 3, 3, 3, 4, 1, 1, 2, 1, 3, 0, 2, 4, 2, 1, 2, 2, 3, 3, 0, 0, 0, 0, 3, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 };
			for (int i = 0; i < band0.length; i++)
				assertEquals((int) histogram.getHistogramValue(0, i), band0[i]);

			int band1[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 4, 12, 17, 30, 34, 22, 17, 11, 4, 15, 11, 16, 5, 4, 5, 3, 5, 1, 2, 1, 1, 2, 1, 1, 2, 0, 1, 3, 0, 3, 1, 0, 1, 0, 2, 0, 3, 3, 4, 2, 5, 9, 5, 9, 6, 8, 6, 5, 7, 7, 5, 6, 8, 5, 12, 9, 21, 12, 18, 7, 13, 21, 13, 22, 16, 13, 13, 15, 14, 16, 14, 13, 21, 10, 4, 12, 13, 11, 15, 9, 8, 5, 5, 7, 5, 5, 6, 7, 3, 9, 5, 1, 2, 8, 2, 1, 2, 1, 1, 4, 3, 3, 3, 2, 1, 4, 3, 2, 0, 2, 2, 3, 0, 1, 3, 2, 2, 0, 1, 0, 0, 3, 2, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			for (int i = 0; i < band1.length; i++)
				assertEquals((int) histogram.getHistogramValue(1, i), band1[i]);

			int band2[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 4, 5, 6, 14, 31, 28, 32, 27, 23, 12, 10, 5, 3, 2, 5, 3, 1, 2, 1, 4, 0, 1, 1, 1, 2, 1, 1, 0, 1, 0, 1, 1, 0, 2, 0, 0, 0, 1, 1, 0, 1, 1, 1, 2, 1, 3, 1, 1, 1, 0, 0, 0, 3, 1, 0, 0, 1, 4, 2, 1, 2, 3, 3, 5, 3, 5, 1, 5, 4, 3, 3, 2, 3, 4, 5, 7, 9, 2, 3, 5, 4, 3, 6, 4, 4, 4, 6, 4, 8, 10, 9, 5, 13, 6, 7, 4, 4, 7, 8, 6, 10, 7, 6, 6, 11, 9, 8, 5, 6, 8, 14, 10, 12, 11, 7, 7, 5, 8, 7, 8, 11, 10, 7, 10, 8, 8, 3, 2, 4, 1, 5, 4, 5, 6, 8, 6, 7, 8, 4, 4, 4, 5, 3, 3, 5, 5, 1, 3, 5, 4, 3, 3, 1, 2, 1, 3, 0, 2, 2, 1, 2, 5, 3, 1, 0, 2, 0, 3, 0, 1, 1, 1, 2, 0, 2, 1, 0, 0, 0, 2, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 2 };
			for (int i = 0; i < band2.length; i++)
				assertEquals((int) histogram.getHistogramValue(2, i), band2[i]);

		} catch (HistogramException e) {
			e.printStackTrace();
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void print(Histogram histogram) {
		for (int i = 0; i < histogram.getNumBands(); i++) {
			System.out.println("");
			System.out.print("int band" + i + "[] = { ");
			for (int j = 0; j < histogram.getBandLenght(i); j++) {
				if (j != 0)
					System.out.print(", ");
				System.out.print(((int) histogram.getHistogramValue(i, j)));
			}
			System.out.println("};");
			System.out.println("for (int i = 0; i < band" + i + ".length; i++)");
			System.out.println("	assertEquals((int) histogram.getHistogramValue(" + i + ", i), band" + i + "[i]);");
		}
	}
}
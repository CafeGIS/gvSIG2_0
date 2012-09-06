/*
 * Created on 9-ago-2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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

import org.gvsig.raster.BaseTestCase;
import org.gvsig.raster.dataset.properties.DatasetStatistics;
/**
 * Este test prueba el calculo de estadisticas de un dataset.
 * Comprueba que los valores obtenidos en las estadisticas son correctos.
 *
 * Algunos costes del calculo de estadistica en el raster completo
 *
 * Ordenador: AMD Athlon(tm) XP 1800+ 1540MHz, 256KB Caché, 500M RAM
 * 	Coste 1:
 * 		Raster: 17910x16062, 275M (GTiff), 1 Banda byte
 * 		Tiempo: 74.295 segs (sgtes igual)
 * 	Coste 2:
 * 		Raster: 8955x8031, 69M (GTiff), 1 Banda byte
 *  	Tiempo: 18.905 segs (sgtes 3.362s)
 *  Coste 3:
 * 		Raster: 812x586, 2.8M (GTiff), 3 Banda short
 *  	Tiempo: 0.925 segs (sgtes 0.152s)
 *  Coste 4:
 * 		Raster: 870x870, 210K (Jpeg), 3 Banda byte
 *  	Tiempo: 0.318 segs (sgtes 0.255s)
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestStatistics extends BaseTestCase {

	private String baseDir = "./test-images/";
	private String path1 = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
	private String path2 = baseDir + "wcs16bits.tif";

	private RasterDataset f1 = null;
	private RasterDataset f2 = null;
	private DatasetStatistics stats1 = null;
	private DatasetStatistics stats2 = null;

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TestStatistics running...");
		try {
			deleteRMF(path1);
			deleteRMF(path2);
			f1 = RasterDataset.open(null, path1);
			f2 = RasterDataset.open(null, path2);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		stats1 = f1.getStatistics();
		stats2 = f2.getStatistics();
	}

	public void testStack(){
		try {
			stats1.calcFullStatistics();
		} catch (FileNotOpenException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		print(stats1);
		dataTestB1(stats1);
		dataTestB2(stats1);
		dataTestB3(stats1);
		try {
			stats2.calcFullStatistics();
		} catch (FileNotOpenException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		print(stats2);
		dataTestB1_2(stats2);
		dataTestB2_2(stats2);
		dataTestB3_2(stats2);
	}

	private void dataTestB1(DatasetStatistics stats){
		assertEquals(new Double(stats.getMaxRGB()[0]), new Double(255.0));
		assertEquals(new Double(stats.getMinRGB()[0]), new Double(0.0));
		assertEquals(new Double(stats.getSecondMaxRGB()[0]), new Double(254.0));
		assertEquals(new Double(stats.getSecondMinRGB()[0]), new Double(1.0));
		assertEquals((int)stats.getMean()[0], -8);
		assertEquals((int)stats.getVariance()[0], 5353);
	}

	private void dataTestB2(DatasetStatistics stats){
		assertEquals(new Double(stats.getMaxRGB()[1]), new Double(255.0));
		assertEquals(new Double(stats.getMinRGB()[1]), new Double(0.0));
		assertEquals(new Double(stats.getSecondMaxRGB()[1]), new Double(254.0));
		assertEquals(new Double(stats.getSecondMinRGB()[1]), new Double(1.0));
		assertEquals((int)stats.getMean()[1], -16);
		assertEquals((int)stats.getVariance()[1], 6012);
	}

	private void dataTestB3(DatasetStatistics stats){
		assertEquals(new Double(stats.getMaxRGB()[2]), new Double(255.0));
		assertEquals(new Double(stats.getMinRGB()[2]), new Double(0.0));
		assertEquals(new Double(stats.getSecondMaxRGB()[2]), new Double(254.0));
		assertEquals(new Double(stats.getSecondMinRGB()[2]), new Double(1.0));
		assertEquals((int)stats.getMean()[2], -1);
		assertEquals((int)stats.getVariance()[2], 5861);
	}

	private void dataTestB1_2(DatasetStatistics stats){
		assertEquals(new Double(stats.getMax()[0]), new Double(1269.0));
		assertEquals(new Double(stats.getMin()[0]), new Double(0.0));
		assertEquals(new Double(stats.getSecondMax()[0]), new Double(1235.0));
		assertEquals(new Double(stats.getSecondMin()[0]), new Double(6.0));
		assertEquals((int)stats.getMean()[0], 71);
		assertEquals((int)stats.getVariance()[0], 22678);
	}

	private void dataTestB2_2(DatasetStatistics stats){
		assertEquals(new Double(stats.getMax()[1]), new Double(1525.0));
		assertEquals(new Double(stats.getMin()[1]), new Double(0.0));
		assertEquals(new Double(stats.getSecondMax()[1]), new Double(1522.0));
		assertEquals(new Double(stats.getSecondMin()[1]), new Double(14.0));
		assertEquals((int)stats.getMean()[1], 104);
		assertEquals((int)stats.getVariance()[1], 37907);
	}

	private void dataTestB3_2(DatasetStatistics stats){
		assertEquals(new Double(stats.getMax()[2]), new Double(888.0));
		assertEquals(new Double(stats.getMin()[2]), new Double(0.0));
		assertEquals(new Double(stats.getSecondMax()[2]), new Double(884.0));
		assertEquals(new Double(stats.getSecondMin()[2]), new Double(13.0));
		assertEquals((int)stats.getMean()[2], 75);
		assertEquals((int)stats.getVariance()[2], 17437);
	}

	public void print(DatasetStatistics stats) {
		for (int iBand = 0; iBand < f1.getBandCount(); iBand++) {
			System.out.println("Band " + iBand);
			System.out.println("...Max: " + stats.getMax()[iBand]);
			System.out.println("...Min: " + stats.getMin()[iBand]);
			System.out.println("...SecondMax: " + stats.getSecondMax()[iBand]);
			System.out.println("...SecondMin: " + stats.getSecondMin()[iBand]);
			System.out.println("...Mean: " + stats.getMean()[iBand]);
			System.out.println("...Variance: " + stats.getVariance()[iBand]);
		}
	}
}
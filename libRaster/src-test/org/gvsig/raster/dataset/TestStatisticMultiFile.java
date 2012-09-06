/*
 * Created on 19-jul-2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.properties.DatasetStatistics;
/**
 * Prueba el calculo de estadisticas para un dataset con multiples ficheros.
 * Comprueba que los valores obtenidos en las estadisticas son correctos.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestStatisticMultiFile extends BaseTestCase {

	private String baseDir = "./test-images/";
	private String path1 = baseDir + "band1-30x28byte.tif";
	private String path2 = baseDir + "band2-30x28byte.tif";
	private String path3 = baseDir + "band3-30x28byte.tif";
	
	private RasterDataset f1 = null;
	private RasterDataset f2 = null;
	private RasterDataset f3 = null;
	
	static {
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestStatisticMultiFile running...");
		try {
			deleteRMF(path1);
			deleteRMF(path2);
			deleteRMF(path3);
			f1 = RasterDataset.open(null, path1);
			f2 = RasterDataset.open(null, path2);
			f3 = RasterDataset.open(null, path3);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
	}
	
	public void testStack() {
		MultiRasterDataset grmf = new MultiRasterDataset();
		try {
			grmf.addDataset(new RasterDataset[]{f1});
			grmf.addDataset(new RasterDataset[]{f2});
			grmf.addDataset(new RasterDataset[]{f3});
			try {
				grmf.getStatistics().calcFullStatistics();
			} catch (FileNotOpenException e) {
				e.printStackTrace();
			} catch (RasterDriverException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			print(grmf);
			dataTestB1(grmf.getStatistics());
			dataTestB2(grmf.getStatistics());
			dataTestB3(grmf.getStatistics());
		} catch (FileNotFoundInListException e) {
			e.printStackTrace();
		}
	}

	private void dataTestB1(DatasetStatistics stats){
		assertEquals(new Double(stats.getMaxRGB()[0]), new Double(249.0));
		assertEquals(new Double(stats.getMinRGB()[0]), new Double(82.0));
		assertEquals(new Double(stats.getSecondMaxRGB()[0]), new Double(211.0));
		assertEquals(new Double(stats.getSecondMinRGB()[0]), new Double(83.0));
		assertEquals((int)stats.getMean()[0], 19);
		assertEquals((int)stats.getVariance()[0], 11599);
	}
	
	private void dataTestB2(DatasetStatistics stats){
		assertEquals(new Double(stats.getMaxRGB()[1]), new Double(234.0));
		assertEquals(new Double(stats.getMinRGB()[1]), new Double(49.0));
		assertEquals(new Double(stats.getSecondMaxRGB()[1]), new Double(216.0));
		assertEquals(new Double(stats.getSecondMinRGB()[1]), new Double(50.0));
		assertEquals((int)stats.getMean()[1], 51);
		assertEquals((int)stats.getVariance()[1], 7109);
	}
	
	private void dataTestB3(DatasetStatistics stats){
		assertEquals(new Double(stats.getMaxRGB()[2]), new Double(255.0));
		assertEquals(new Double(stats.getMinRGB()[2]), new Double(28.0));
		assertEquals(new Double(stats.getSecondMaxRGB()[2]), new Double(250.0));
		assertEquals(new Double(stats.getSecondMinRGB()[2]), new Double(34.0));
		assertEquals((int)stats.getMean()[2], -12);
		assertEquals((int)stats.getVariance()[2], 7991);
	}
	
	public void print(MultiRasterDataset grmf) {
		for (int iBand = 0; iBand < grmf.getStatistics().getBandCount(); iBand++) {
			System.out.println("Band " + iBand);
			System.out.println("...Max: " + grmf.getStatistics().getMax()[iBand]);
			System.out.println("...Min: " + grmf.getStatistics().getMin()[iBand]);
			System.out.println("...SecondMax: " + grmf.getStatistics().getSecondMax()[iBand]);
			System.out.println("...SecondMin: " + grmf.getStatistics().getSecondMin()[iBand]);
			System.out.println("...Mean: " + grmf.getStatistics().getMean()[iBand]);
			System.out.println("...Variance: " + grmf.getStatistics().getVariance()[iBand]);
		}
	}
}
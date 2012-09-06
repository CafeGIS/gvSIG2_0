/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.properties.DatasetHistogram;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.serializer.HistogramRmfSerializer;

/**
 * Test para comprobar la construcción de un histograma desde un XML. Este test calcula el histograma
 * de un raster y lo convierte a XML. Después creará un objeto Histogram a partir del XML. Finalmente
 * se comparará el Histograma original con el final.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestHistogramSerializer extends TestCase {
	
	private String baseDir = "./test-images/";
	private String path = baseDir + "miniraster30x30.jp2";
	private RasterDataset f = null;	
	
	static {
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestHistogramSerializer running...");
		//int[] drawableBands = {0, 1, 2};
		try {
			f = RasterDataset.open(null, path);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
			return;
		} catch (RasterDriverException e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	public void testStack() {
		DatasetHistogram dsh = f.getHistogram();
		Histogram hist1 = null;
		try {
			hist1 = dsh.getHistogram();
		} catch (FileNotOpenException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		HistogramRmfSerializer serial1 = new HistogramRmfSerializer(hist1);
		String s = serial1.write();
		//System.out.println(s);
		
		HistogramRmfSerializer serial2 = new HistogramRmfSerializer();
		try {
			serial2.read(s);
		} catch (ParsingException e) {
			e.printStackTrace();
		}
		Histogram hist2 = (Histogram)serial2.getResult();
		
		assertEquals(hist1.getNumBands(), hist2.getNumBands());
		for (int iBand = 0; iBand < hist1.getNumBands(); iBand++) {
			for(int i = 0; i < hist1.getNumValues(); i++) 
				assertEquals((long)hist1.getHistogramValue(iBand, i), (long)hist2.getHistogramValue(iBand, i));	
		}
		
	}
	
}

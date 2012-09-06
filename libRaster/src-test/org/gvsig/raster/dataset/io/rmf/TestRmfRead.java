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
package org.gvsig.raster.dataset.io.rmf;

import java.awt.geom.AffineTransform;

import org.gvsig.raster.BaseTestCase;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.properties.DatasetStatistics;
import org.gvsig.raster.dataset.rmf.IRmfBlock;
import org.gvsig.raster.dataset.rmf.ParsingException;
import org.gvsig.raster.dataset.rmf.RmfBlocksManager;
import org.gvsig.raster.dataset.serializer.ColorInterpretationRmfSerializer;
import org.gvsig.raster.dataset.serializer.GeoInfoRmfSerializer;
import org.gvsig.raster.dataset.serializer.GeoPointListRmfSerializer;
import org.gvsig.raster.dataset.serializer.StatisticsRmfSerializer;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.GeoPoint;
import org.gvsig.raster.datastruct.GeoPointList;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.HistogramClass;
import org.gvsig.raster.datastruct.NoData;
import org.gvsig.raster.datastruct.serializer.ColorTableRmfSerializer;
import org.gvsig.raster.datastruct.serializer.HistogramRmfSerializer;
import org.gvsig.raster.datastruct.serializer.NoDataRmfSerializer;
/**
 * Test de lectura para ficheros rmf. Obtiene distintos tipos de bloques y
 * comprueba que el objeto que han de generar es correcto.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestRmfRead extends BaseTestCase {
	private String           baseDir = "./test-images/";
	private RmfBlocksManager manager = null;
	public String            file    = "readtest.rmf";
	private RasterDataset    f1      = null;
	private String           path1   = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";

	static {
		RasterLibrary.wakeUp();
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TestRmfRead running...");
		try {
			deleteRMF(path1);
			f1 = RasterDataset.open(null, path1);
		} catch (NotSupportedExtensionException e1) {
			e1.printStackTrace();
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		}
	}

	public void testStack(){
		manager = new RmfBlocksManager(baseDir + file);
		if (!manager.checkRmf())
			assertEquals(0, 1);
		HistogramRmfSerializer ser = new HistogramRmfSerializer();
		ColorTableRmfSerializer ser1 = new ColorTableRmfSerializer();
		StatisticsRmfSerializer ser2 = new StatisticsRmfSerializer();
		GeoInfoRmfSerializer ser3 = new GeoInfoRmfSerializer(f1);
		GeoPointListRmfSerializer ser4 = new GeoPointListRmfSerializer();
		NoDataRmfSerializer ser5 = new NoDataRmfSerializer();
		ColorInterpretationRmfSerializer ser6 = new ColorInterpretationRmfSerializer(f1.getColorInterpretation());

		manager.addClient(ser);
		manager.addClient(ser1);
		manager.addClient(ser2);
		manager.addClient(ser3);
		manager.addClient(ser4);
		manager.addClient(ser5);
		manager.addClient(ser6);

		try {
			manager.read(null);
			for (int i = 0; i < manager.getClientsCount(); i++) {
				IRmfBlock client = manager.getClient(i);
				if (client instanceof HistogramRmfSerializer)
					testHistogram((Histogram) client.getResult());
				if (client instanceof ColorTableRmfSerializer) {
					ColorTable colorTable = (ColorTable) client.getResult();
					colorTable.setInterpolated(false);
					testColorTable(colorTable);
				}
				if (client instanceof StatisticsRmfSerializer)
					testStatistics((DatasetStatistics) client.getResult());
				if (client instanceof NoDataRmfSerializer)
					testNoData((NoData) client.getResult());
//				if (client instanceof GeoInfoRmfSerializer)
//					testGeoInfo((RasterDataset) client.getResult());
				if (client instanceof GeoPointListRmfSerializer)
					testGeoPoints((GeoPointList) client.getResult());
				if (client instanceof ColorInterpretationRmfSerializer) {
					DatasetColorInterpretation ci = (DatasetColorInterpretation) client.getResult();
					testColorInterpretation(ci);
				}
			}

		} catch (ParsingException e) {
			e.printStackTrace();
		}

	}

	public void testColorInterpretation(DatasetColorInterpretation ci) {
		for (int j = 0; j < ci.length(); j++) {
			String value = (String) ci.get(j);
//			System.out.println(value);
			if (j == 0)
				assertEquals(value, "Blue");
			if (j == 1)
				assertEquals(value, "Green");
			if (j == 2)
				assertEquals(value, "Red");
		}
	}
	
	public void testHistogram(Histogram h) {
		Histogram h2 = Histogram.convertHistogramToRGB(h);
		
		HistogramClass[][] classes = h2.getHistogram();
		for (int iBand = 0; iBand < classes.length; iBand++) {
			for (int iValue = 0; iValue < classes[iBand].length; iValue++) {
				assertEquals((int) classes[iBand][iValue].getMin(), iValue);
				assertEquals((int) classes[iBand][iValue].getMax(), iValue + 1);
			}
		}

		assertEquals((int) h.getHistogramValue(0, 0), 16);
		assertEquals((int) h.getHistogramValue(0, 1), 4);
		assertEquals((int) h.getHistogramValue(0, 23), 6);
		assertEquals((int) h.getHistogramValue(0, 48), 4);

		assertEquals((int) h.getHistogramValue(1, 0), 2);
		assertEquals((int) h.getHistogramValue(1, 4), 1);
		assertEquals((int) h.getHistogramValue(1, 7), 8);
		assertEquals((int) h.getHistogramValue(1, 20), 4);

		assertEquals((int) h.getHistogramValue(2, 0), 25);
		assertEquals((int) h.getHistogramValue(2, 2), 1);
		assertEquals((int) h.getHistogramValue(2, 13), 4);
		assertEquals((int) h.getHistogramValue(2, 21), 2);
	}

	public void testColorTable(ColorTable ct) {

//		byte[][] color = ct.getColorTableByBand();

//		assertEquals(ct.getName(), "Prueba Tabla de Color");

		assertEquals(ct.getRGBAByBand(15)[0] & 0xff, 95);
		assertEquals(ct.getRGBAByBand(15)[1] & 0xff, 95);
		assertEquals(ct.getRGBAByBand(15)[2] & 0xff, 95);

		assertEquals(ct.getRGBAByBand(22)[0] & 0xff, 0);
		assertEquals(ct.getRGBAByBand(22)[1] & 0xff, 0);
		assertEquals(ct.getRGBAByBand(22)[2] & 0xff, 0);

		assertEquals(ct.getRGBAByBand(24)[0] & 0xff, 0);
		assertEquals(ct.getRGBAByBand(24)[1] & 0xff, 0);
		assertEquals(ct.getRGBAByBand(24)[2] & 0xff, 0);

		assertEquals(ct.getRGBAByBand(28)[0] & 0xff, 0);
		assertEquals(ct.getRGBAByBand(28)[1] & 0xff, 0);
		assertEquals(ct.getRGBAByBand(28)[2] & 0xff, 0);

		assertEquals(ct.getRGBAByBand(0)[0] & 0xff, 255);
		assertEquals(ct.getRGBAByBand(0)[1] & 0xff, 255);
		assertEquals(ct.getRGBAByBand(0)[2] & 0xff, 255);

		assertEquals(ct.getRGBAByBand(1)[0] & 0xff, 0);
		assertEquals(ct.getRGBAByBand(1)[1] & 0xff, 0);
		assertEquals(ct.getRGBAByBand(1)[2] & 0xff, 0);

		assertEquals(ct.getRGBAByBand(2)[0] & 0xff, 0);
		assertEquals(ct.getRGBAByBand(2)[1] & 0xff, 102);
		assertEquals(ct.getRGBAByBand(2)[2] & 0xff, 255);

//		System.out.println(ct.getName());
//		for (int i = 0; i < color.length; i++) {
//			System.out.println((color[i][0] & 0xff) + " " + (color[i][1] & 0xff) + " " + (color[i][2] & 0xff));
//		}
	}

	public void testNoData(NoData noData) {
		assertEquals((int) noData.getValue(), 5450);
		assertEquals((int) noData.getType(), 2);
	}

	public void testStatistics(DatasetStatistics ds) {
		int bandCount = ds.getBandCount();
		double[] max = ds.getMaxRGB();
		double[] min = ds.getMinRGB();
		double[] secondMax = ds.getSecondMaxRGB();
		double[] secondMin = ds.getSecondMinRGB();
		double[] mean = ds.getMean();
		double[] variance = ds.getVariance();

		assertEquals(bandCount, 3);
		for (int i = 0; i < bandCount; i++) {
			switch (i) {
				case 0:
					assertEquals((int) max[i], 250);
					assertEquals((int) min[i], 0);
					assertEquals((int) secondMax[i], 248);
					assertEquals((int) secondMin[i], 1);
					assertEquals((int) mean[i], 36);
					assertEquals((int) variance[i], 4984);
					break;
				case 1:
					assertEquals((int) max[i], 255);
					assertEquals((int) min[i], 0);
					assertEquals((int) secondMax[i], 254);
					assertEquals((int) secondMin[i], 3);
					assertEquals((int) mean[i], 37);
					assertEquals((int) variance[i], 6292);
					break;
				case 2:
					assertEquals((int) max[i], 254);
					assertEquals((int) min[i], 0);
					assertEquals((int) secondMax[i], 250);
					assertEquals((int) secondMin[i], 1);
					assertEquals((int) mean[i], 35);
					assertEquals((int) variance[i], 4154);
					break;
			}
		}

//		System.out.println(ct.getName());
//		System.out.println(ct.getType());
//		for (int i = 0; i < color.length; i++) {
//			System.out.print(range[i] + ": ");
//			System.out.println(color[i][0] + " " + color[i][1] + " " + color[i][2]);
//		}
	}

	public void testGeoInfo(RasterDataset dataset) {
	//TODO: TEST: Terminar test de acceso a la georreferenciación.
//		String proj = "";
//		if(dataset.getProjection() != null)
//			proj = dataset.getProjection().getAbrev();
		AffineTransform at = dataset.getAffineTransform();

		assertEquals((int) at.getTranslateX(), 5000);
		assertEquals((int) at.getTranslateY(), 5000);
		assertEquals((int) at.getScaleX(), 2);
		assertEquals((int) at.getScaleY(), -2);
	}

	public void testGeoPoints(GeoPointList gpList) {
		GeoPoint p = gpList.get(0);

		assertEquals((int) p.pixelPoint.getX(), 10);
		assertEquals((int) p.pixelPoint.getY(), 10);
		assertEquals((int) p.mapPoint.getX(), 34223);
		assertEquals((int) p.mapPoint.getY(), 2344);

		assertEquals(p.active, true);

//		assertEquals((int) p.leftCenterPoint.getX(), 24223);
//		assertEquals((int) p.leftCenterPoint.getY(), 3244);
//		assertEquals((int) p.rightCenterPoint.getX(), 2433);
//		assertEquals((int) p.rightCenterPoint.getY(), 6244);
//
//		assertEquals((int) p.leftViewPort.getExtent().minX(), 30032);
//		assertEquals((int) p.leftViewPort.getExtent().maxY(), 2103);
//
//		assertEquals((int) p.rightViewPort.getExtent().minX(), 30032);
//		assertEquals((int) p.rightViewPort.getExtent().maxY(), 2103);

		assertEquals((int) p.zoomLeft, 1);
		assertEquals((int) p.zoomRight, 1);
	}
}
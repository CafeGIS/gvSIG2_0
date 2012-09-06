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

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.gvsig.raster.BaseTestCase;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.properties.DatasetStatistics;
import org.gvsig.raster.dataset.rmf.RmfBlocksManager;
import org.gvsig.raster.dataset.serializer.ColorInterpretationRmfSerializer;
import org.gvsig.raster.dataset.serializer.GeoInfoRmfSerializer;
import org.gvsig.raster.dataset.serializer.GeoPointListRmfSerializer;
import org.gvsig.raster.dataset.serializer.StatisticsRmfSerializer;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.GeoPoint;
import org.gvsig.raster.datastruct.GeoPointList;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.datastruct.NoData;
import org.gvsig.raster.datastruct.ViewPortData;
import org.gvsig.raster.datastruct.serializer.ColorTableRmfSerializer;
import org.gvsig.raster.datastruct.serializer.HistogramRmfSerializer;
import org.gvsig.raster.datastruct.serializer.NoDataRmfSerializer;
/**
 * Test de escritura de ficheros rmf.
 * Escribe un fichero rmf con distintos bloques y a continuación le pasa un
 * test de lectura para comprobar que se ha generado bien.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestRmfWrite extends BaseTestCase {
	private RmfBlocksManager manager    = null;
	private String           baseDir    = "./test-images/";
	private String           path       = baseDir + "miniRaster25x24.tif";
	private String           pathGif    = baseDir + "gifTransparente.gif";
	private String           pathJpg    = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
	private Histogram        histogram  = null;
	private ColorTable       colorTable = null;
	private RasterDataset    f          = null;
	private RasterDataset    f2         = null;

	static {
		RasterLibrary.wakeUp();
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TestRmfWrite running...");

		RasterDataset f1 = null;
		try {
			deleteRMF(path);
			deleteRMF(pathGif);
			deleteRMF(pathJpg);
			f = RasterDataset.open(null, path);
			f1 = RasterDataset.open(null, pathGif);
			f2 = RasterDataset.open(null, pathJpg);
		} catch (NotSupportedExtensionException e) {
			return;
		} catch (RasterDriverException e) {
			return;
		}
		BufferFactory ds = new BufferFactory(f);
		try {
			ds.setAreaOfInterest();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}

		try {
			histogram = f.getHistogram().getHistogram();
			colorTable = f1.getColorTable();
			f.getStatistics().calcFullStatistics();
		} catch (FileNotOpenException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void testStack() {
		deleteRMF(baseDir + "writetest.rmf");
		manager = new RmfBlocksManager(baseDir + "writetest.rmf");

		// Histograma
		HistogramRmfSerializer ser = new HistogramRmfSerializer(histogram);
		manager.addClient(ser);

		// Tabla de color
		// colorTable.setName("Prueba Tabla de Color");
		ColorTableRmfSerializer ser1 = new ColorTableRmfSerializer(colorTable);
		manager.addClient(ser1);

		// Estadisticas
		DatasetStatistics stat = f.getStatistics();
		stat.setTailTrimValue(3.0, new Double(10.0));
		stat.setTailTrimValue(4.0, new Double(16.0));
		StatisticsRmfSerializer ser2 = new StatisticsRmfSerializer(stat);
		manager.addClient(ser2);

		// Georreferenciación
		GeoInfoRmfSerializer ser3 = new GeoInfoRmfSerializer(f2);
		manager.addClient(ser3);

		// Puntos de control
		GeoPoint p1 = new GeoPoint();
		p1.pixelPoint = new Point2D.Double(10, 10);
		p1.mapPoint = new Point2D.Double(34223.3, 2344.2);
		p1.leftViewPort = new ViewPortData();
		p1.leftViewPort.setExtent(new Extent(30032.3, 2103.3, 50023.3, 1234.3));
		p1.rightViewPort = new ViewPortData();
		p1.rightViewPort.setExtent(new Extent(30032.3, 2103.3, 50023.3, 1234.3));
		p1.leftCenterPoint = new Point2D.Double(24223.3, 3244.2);
		p1.rightCenterPoint = new Point2D.Double(2433.3, 6244.2);
		GeoPoint p2 = new GeoPoint();
		p2.pixelPoint = new Point2D.Double(10, 10);
		p2.mapPoint = new Point2D.Double(34223.3, 2344.2);
		p2.leftViewPort = new ViewPortData();
		p2.leftViewPort.setExtent(new Extent(30032.3, 2103.3, 50023.3, 1234.3));
		p2.rightViewPort = new ViewPortData();
		p2.rightViewPort.setExtent(new Extent(30032.3, 2103.3, 50023.3, 1234.3));
		p2.leftCenterPoint = new Point2D.Double(24223.3, 3244.2);
		p2.rightCenterPoint = new Point2D.Double(2433.3, 6244.2);

		p1.leftViewPort.pxSize = new Point2D.Double(32, 34);
		
		GeoPointList list = new GeoPointList();
		list.add(p1);
		list.add(p2);
		GeoPointListRmfSerializer ser4 = new GeoPointListRmfSerializer(list, p1.leftViewPort);
		manager.addClient(ser4);

		// Valor NoData
		NoDataRmfSerializer ser5 = new NoDataRmfSerializer(new NoData(5450.0, 2));
		manager.addClient(ser5);

		// Interpretación de color
		DatasetColorInterpretation ci = f.getColorInterpretation();
		ci.setColorInterpValue(0, DatasetColorInterpretation.BLUE_BAND);
		ci.setColorInterpValue(2, DatasetColorInterpretation.RED_BAND);
		ColorInterpretationRmfSerializer ser6 = new ColorInterpretationRmfSerializer(f.getColorInterpretation());
		manager.addClient(ser6);

		try {
			manager.write();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Pasamos el test de lectura para comprobar que se ha generado bien
		TestRmfRead t = new TestRmfRead();
		t.file = "writetest.rmf";
		t.start();
	}
}
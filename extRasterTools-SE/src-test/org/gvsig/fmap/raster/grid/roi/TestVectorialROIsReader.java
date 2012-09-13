package org.gvsig.fmap.raster.grid.roi;

import java.io.File;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.FileNotExistsException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.OutOfGridException;

public class TestVectorialROIsReader extends TestCase {

	Grid grid = null;
	GridExtent extent = null;
	int cellSize = 1;
	double xMin = 0;
	double xMax = 10;
	double yMin = 0;
	double yMax = 10;
	ArrayList rois = null;
	GeneralPathX path = null;
	Geometry geometry = null;

//	public static final String fwAndamiDriverPath = "../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers";
	private static File baseDriversPath;

	public void setUp() {
		int bands[] = {0};
		extent = new GridExtent();
		extent.setCellSize(cellSize);
		extent.setXRange(xMin, xMax);
		extent.setYRange(yMin, yMax);
		try {
			grid = new Grid(extent, extent, IBuffer.TYPE_DOUBLE, bands);
			for (int x = 0; x<5; x++)
				for (int y = 0; y<5; y++){
					grid.setCellValue(x, y, 0.0);
				}
			for (int x = 5; x<xMax; x++)
				for (int y = 0; y<5; y++){
					grid.setCellValue(x, y, 1.0);
				}
			for (int x = 0; x<5; x++)
				for (int y = 5; y<10; y++){
					grid.setCellValue(x, y, 2.0);
				}
			for (int x = 5; x<10; x++)
				for (int y = 5; y<10; y++){
					grid.setCellValue(x, y, 3.0);
				}
		} catch (RasterBufferInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfGridException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		setUpDrivers();
	}

//	public static void setUpDrivers() {
//		try {
//			baseDriversPath = new File(fwAndamiDriverPath);
//			if (!baseDriversPath.exists())
//				throw new Exception("Can't find drivers path: " + fwAndamiDriverPath);
//
//			LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
//			if (LayerFactory.getDM().getDriverNames().length < 1)
//				throw new Exception("Can't find drivers in path: " + fwAndamiDriverPath);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void testStack() {
		String path = "./test-images/rois_polygons.shp";
		try {
			VectorialROIsReader reader = new VectorialROIsReader(path, grid, CRSFactory.getCRS("EPSG:23030"));
			rois = reader.read(null);
		} catch (LoadLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidROIsShpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

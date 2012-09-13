package org.gvsig.fmap.raster.grid.roi;

import java.awt.Color;

import junit.framework.TestCase;

import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.OutOfGridException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestVectorialROIsWriter extends TestCase {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(TestVectorialROIsWriter.class);
	Grid grid = null;
	GridExtent extent = null;
	int cellSize = 1;
	double xMin = 0;
	double xMax = 10;
	double yMin = 0;
	double yMax = 10;
	VectorialROI rois[] = null;
	GeneralPathX path = null;
	Geometry geometry = null;

	public void setUp() {
		int bands[] = {0};
		extent = new GridExtent();
		extent.setCellSize(cellSize);
		extent.setXRange(xMin, xMax);
		extent.setYRange(yMin, yMax);
		rois = new VectorialROI[3];
		try {
			grid = new Grid(extent, extent, IBuffer.TYPE_DOUBLE, bands);
			for (int x = 0; x<5; x++)
				for (int y = 0; y<5; y++)
					grid.setCellValue(x, y, 0.0);
			for (int x = 5; x<xMax; x++)
				for (int y = 0; y<5; y++)
					grid.setCellValue(x, y, 1.0);
			for (int x = 0; x<5; x++)
				for (int y = 5; y<10; y++)
					grid.setCellValue(x, y, 2.0);
			for (int x = 5; x<10; x++)
				for (int y = 5; y<10; y++)
					grid.setCellValue(x, y, 3.0);
		} catch (RasterBufferInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfGridException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() throws CreateGeometryException {
		this.setUp();
		this.testStack();
	}

	public void testStack() throws CreateGeometryException {


		path = new GeneralPathX();

		path.moveTo(4,4);
		path.lineTo(4,0);
		path.lineTo(0,0);
		path.lineTo(0,4);
		path.closePath();

		geometry = geomManager.createSurface(path, SUBTYPES.GEOM2D);

		rois[0] = new VectorialROI(grid);
		rois[0].addGeometry(geometry);
		rois[0].setColor(Color.RED);
		rois[0].setName("PoligonsROI");

		path = new GeneralPathX();

		path.moveTo(9,4);
		path.lineTo(9,2);
		path.lineTo(6,2);
		path.lineTo(6,4);
		path.closePath();

		geometry = geomManager.createSurface(path, SUBTYPES.GEOM2D);
		rois[0].addGeometry(geometry);

		//ROI tipo linea:
		path = new GeneralPathX();

		path.moveTo(14,14);
		path.lineTo(14,10);
		path.lineTo(10,10);
		path.lineTo(10,14);

		geometry = geomManager.createSurface(path, SUBTYPES.GEOM2D);

		rois[1] = new VectorialROI(grid);
		rois[1].addGeometry(geometry);
		rois[1].setColor(Color.GREEN);
		rois[1].setName("LinesROI");

		path = new GeneralPathX();

		path.moveTo(24,24);
		path.lineTo(24,20);
		path.lineTo(20,20);
		path.lineTo(20,24);

		geometry = geomManager.createSurface(path, SUBTYPES.GEOM2D);

		rois[1].addGeometry(geometry);

		//ROI tipo puntos:
		geometry = geomManager.createPoint(30, 30, SUBTYPES.GEOM2D);

		rois[2] = new VectorialROI(grid);
		rois[2].addGeometry(geometry);
		rois[2].setColor(Color.BLUE);
		rois[2].setName("PointsROI");

		geometry = geomManager.createPoint(35, 35, SUBTYPES.GEOM2D);

		rois[2].addGeometry(geometry);

		VectorialROIsWriter roisWriter = new VectorialROIsWriter("./test-images/rois",CRSFactory.getCRS("EPSG:23030"));
		roisWriter.write(rois);
	}

}

package org.gvsig.raster.grid.roi;

import junit.framework.TestCase;

import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.OutOfGridException;

public class TestRasterROI extends TestCase {
	
	Grid grid = null;
	GridExtent extent = null;
	int cellSize = 1;
	double xMin = 0;
	double xMax = 10;
	double yMin = 0;
	double yMax = 10;
	RasterROI rasterROI = null;
	
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
		
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void testStack() {
		
		rasterROI = new RasterROI(grid);
		rasterROI.addPoint(0,0);
		rasterROI.addPoint(0,1);
		rasterROI.addPoint(1,0);
		rasterROI.addPoint(1,1);
		

		try {
			assertEquals(0.0,rasterROI.getMaxValue(),0.0);
			assertEquals(0.0,rasterROI.getMeanValue(),0.0);
			assertEquals(0.0,rasterROI.getMinValue(),0.0);
		} catch (GridException e) {
			e.printStackTrace();
		}
		
		rasterROI.clear();
		rasterROI.addPoint(8,8);
		rasterROI.addPoint(9,8);
		rasterROI.addPoint(8,9);
		rasterROI.addPoint(9,9);
		
		try {
			assertEquals(3.0,rasterROI.getMaxValue(),0.0);
			assertEquals(3.0,rasterROI.getMeanValue(),0.0);
			assertEquals(3.0,rasterROI.getMinValue(),0.0);
		} catch (GridException e) {
			e.printStackTrace();
		}
		
		rasterROI.clear();
		rasterROI.addPoint(4,4);
		rasterROI.addPoint(5,4);
		rasterROI.addPoint(4,5);
		rasterROI.addPoint(5,5);
		
		try {
			assertEquals(3.0,rasterROI.getMaxValue(),0.0);
			assertEquals(1.5,rasterROI.getMeanValue(),0.0);
			assertEquals(0.0,rasterROI.getMinValue(),0.0);	
		} catch (GridException e) {
			e.printStackTrace();
		}
	}

}

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibañez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.fmap.raster.grid.roi;

import junit.framework.TestCase;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.rastertools.rasterresolution.ZoomPixelCursorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test calculo de estadisticas avanzadas (Matriz de varianza covarianza) para una ROI
 * El test comprueba el calculo de la matriz de varianza covarianza para una imagen de tipo
 * double de tamaño 3X4 con 2 bandas.
 *
 * @autor: aMuÑoz (alejandro.munoz@uclm.es)
 *
 * */
public class TestRoiStatisticAdvanced extends TestCase {
	private static final GeometryManager 	geomManager = GeometryLocator.getGeometryManager();
	private static final Logger 			logger		= LoggerFactory.getLogger(TestRoiStatisticAdvanced.class);
	Grid 									inputGrid 	= null;
	GridExtent 								extent 		= null;
	int 									cellSize 	= 1;
	double 									xMin 		= 0;
	double 									xMax 		= 4;
	double 									yMin 		= 0;
	double 									yMax 		= 4;
	int 									bands[] 	={0,1};
	VectorialROI 							roi 		= null;
	GeneralPathX 							path 		= null;
	Geometry 								geometry 	= null;

	static{
		RasterLibrary.wakeUp();
	}

	public void start() throws CreateGeometryException {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		int bands[] = {0,1};
		extent = new GridExtent();
		extent.setCellSize(cellSize);
		extent.setXRange(xMin, xMax);
		extent.setYRange(yMin, yMax);

		try {

			inputGrid = new Grid(extent, extent, IBuffer.TYPE_DOUBLE, bands);

			inputGrid.getRasterBuf().setLineInBandDouble(new double[]{963,754,753}, 0, 0);
			inputGrid.getRasterBuf().setLineInBandDouble(new double[]{1135,858,823}, 1, 0);
			inputGrid.getRasterBuf().setLineInBandDouble(new double[]{1169,893,823}, 2, 0);
			inputGrid.getRasterBuf().setLineInBandDouble(new double[]{1103,961,823}, 3, 0);

			inputGrid.getRasterBuf().setLineInBandDouble(new double[]{1601,1476,1536}, 0, 1);
			inputGrid.getRasterBuf().setLineInBandDouble(new double[]{1787,1537,1536}, 1, 1);
			inputGrid.getRasterBuf().setLineInBandDouble(new double[]{1786,1537,1474}, 2, 1);
			inputGrid.getRasterBuf().setLineInBandDouble(new double[]{1539,1661,1474}, 3, 1);
		} catch (RasterBufferInvalidException e) {
			System.out.print("fallo en la generacion del grid.");
		}
	}

	public void testStack() throws CreateGeometryException{
		VectorialROI r= new VectorialROI(inputGrid);
		path = new GeneralPathX();

		path.moveTo(4,4);
		path.lineTo(0,4);
		path.lineTo(0,0);
		path.lineTo(4,0);
		path.closePath();
		geometry = geomManager.createSurface(path, SUBTYPES.GEOM2D);
		r.addGeometry(geometry);

		double[][] resultado ={{12151.25,19455.25},{11233.722222222,12151.25}};
		double matrix[][];
		try {
			matrix = r.getVarCovMatrix();
			for (int i=0; i< matrix.length;i++)
				for(int j=0; j<matrix[0].length;j++)
					assertEquals(matrix[i][j],resultado[i][j],0.01);
		} catch (GridException e) {
		}
	}
}

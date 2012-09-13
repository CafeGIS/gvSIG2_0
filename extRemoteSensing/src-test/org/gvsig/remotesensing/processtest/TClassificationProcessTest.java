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

package org.gvsig.remotesensing.processtest;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.grid.roi.VectorialROI;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.Grid;
import org.gvsig.rastertools.rasterresolution.ZoomPixelCursorListener;
import org.gvsig.remotesensing.classification.ClassificationMaximumLikelihoodProcess;
import org.gvsig.remotesensing.classification.ClassificationMinimumDistanceProcess;
import org.gvsig.remotesensing.classification.ClassificationParallelepipedProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* Este test prueba el proceso de clasificación de una imagen de 4x4 con
* tres bandas.El proceso de clasificación se realiza por minima distancia, maxima probabilidad
* y paralelepipedos. Los resultados se comparan con el resultado teórico válido
*
* ** @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
* */
public class TClassificationProcessTest extends TestCase {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(ZoomPixelCursorListener.class);
	private String baseDir = "./test-images/";
	private String path1 = baseDir + "classification_image_test.tif";
	private FLyrRasterSE lyr=null;
	private Grid dataGrid= null;
	GeneralPathX path, path2 = null;
	Geometry geometry, geometry2 = null;

	static{
		RasterLibrary.wakeUp();
	}

	public void start() throws CreateGeometryException {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TClassificationProcessTest running...");
		try {
			lyr = FLyrRasterSE.createLayer(
					path1,
					path1,
					null
					);
			BufferFactory ds1 = new BufferFactory(lyr.getDataSource());
			dataGrid= new Grid(ds1);
		} catch (LoadLayerException e) {
			System.out.print("Error en la construcción de la capa");
		} catch (RasterBufferInvalidException e) {
			System.out.print("Error en la carga del grid de datos");
		}
	}

	public void testStack() throws CreateGeometryException {
		// Definición de las clases (rois) para el proceso
		VectorialROI class1= new VectorialROI(dataGrid);
		VectorialROI class2= new VectorialROI(dataGrid);
		path = new GeneralPathX();
		path2= new GeneralPathX();

		// class1-- Roi con los pixeles 1 y 2 de las dos primeras filas
		path.moveTo(dataGrid.getGridExtent().getULX(),dataGrid.getGridExtent().getULY());
		path.lineTo(dataGrid.getGridExtent().getULX()+(2*dataGrid.getCellSize())-0.3,dataGrid.getGridExtent().getULY());
		path.lineTo(dataGrid.getGridExtent().getULX()+(2*dataGrid.getCellSize())-0.3,dataGrid.getGridExtent().getULY()-2*(dataGrid.getCellSize())+0.3);
		path.lineTo(dataGrid.getGridExtent().getULX(),dataGrid.getGridExtent().getULY()-2*(dataGrid.getCellSize())+0.3);
		path.closePath();
		geometry = geomManager.createSurface(path, SUBTYPES.GEOM2D);
		class1.addGeometry(geometry);


		//class2-- Roi con los pixeles 1 y 2 de las dos ultimas filas
		path2.moveTo(dataGrid.getGridExtent().getLLX(),dataGrid.getGridExtent().getLLY());
		path2.lineTo(dataGrid.getGridExtent().getLLX()+(2*dataGrid.getCellSize())-0.3,dataGrid.getGridExtent().getLLY());
		path2.lineTo(dataGrid.getGridExtent().getLLX()+(2*dataGrid.getCellSize())-0.3,dataGrid.getGridExtent().getLLY()+2*(dataGrid.getCellSize())-0.3);
		path2.lineTo(dataGrid.getGridExtent().getLLX(),dataGrid.getGridExtent().getLLY()+2*(dataGrid.getCellSize())-0.3);

		geometry2 = geomManager.createSurface(path2, SUBTYPES.GEOM2D);
		class2.addGeometry(geometry2);

		ArrayList listRois=new ArrayList();
		listRois.add(class1);
		listRois.add(class2);

		// Clasificacion por el método de mínima distancia
		ClassificationMinimumDistanceProcess proceso = new ClassificationMinimumDistanceProcess();
		proceso.addParam("layer",lyr);
		proceso.addParam("bandList",new int[]{0,1,2});
		proceso.addParam("rois",listRois);
		proceso.run();

		IBuffer result= (IBuffer) proceso.getResult();

		assertEquals(result.getBandCount(),1);

		//Comparación de valores
		assertEquals(result.getElemByte(0, 0,0),0);
		assertEquals(result.getElemByte(0, 1,0),0);
		assertEquals(result.getElemByte(0, 2,0),0);
		assertEquals(result.getElemByte(0, 3,0),0);

		assertEquals(result.getElemByte(1, 0,0),1);
		assertEquals(result.getElemByte(1, 1,0),0);
		assertEquals(result.getElemByte(1, 2,0),0);
		assertEquals(result.getElemByte(1, 3,0),0);

		assertEquals(result.getElemByte(2, 0,0),1);
		assertEquals(result.getElemByte(2, 1,0),1);
		assertEquals(result.getElemByte(2, 2,0),1);
		assertEquals(result.getElemByte(2, 3,0),0);

		assertEquals(result.getElemByte(3, 0,0),0);
		assertEquals(result.getElemByte(3, 1,0),0);
		assertEquals(result.getElemByte(3, 2,0),1);
		assertEquals(result.getElemByte(3, 3,0),0);


//		 Clasificacion por el método de máxima probabilidad distancia
		ClassificationMaximumLikelihoodProcess proceso2 = new ClassificationMaximumLikelihoodProcess();
		proceso2.addParam("layer",lyr);
		proceso2.addParam("bandList",new int[]{0,1,2});
		proceso2.addParam("rois",listRois);
		proceso2.run();


		IBuffer result2= (IBuffer) proceso2.getResult();

		assertEquals(result2.getBandCount(),1);

		//Comparación de valores
		assertEquals(result2.getElemByte(0, 0,0),0);
		assertEquals(result2.getElemByte(0, 1,0),0);
		assertEquals(result2.getElemByte(0, 2,0),1);
		assertEquals(result2.getElemByte(0, 3,0),1);

		assertEquals(result2.getElemByte(1, 0,0),0);
		assertEquals(result2.getElemByte(1, 1,0),0);
		assertEquals(result2.getElemByte(1, 2,0),1);
		assertEquals(result2.getElemByte(1, 3,0),1);

		assertEquals(result2.getElemByte(2, 0,0),1);
		assertEquals(result2.getElemByte(2, 1,0),1);
		assertEquals(result2.getElemByte(2, 2,0),1);
		assertEquals(result2.getElemByte(2, 3,0),1);

		assertEquals(result2.getElemByte(3, 0,0),1);
		assertEquals(result2.getElemByte(3, 1,0),1);
		assertEquals(result2.getElemByte(3, 2,0),1);
		assertEquals(result2.getElemByte(3, 3,0),1);


//		 Clasificacion por el método de máxima probabilidad distancia
		ClassificationParallelepipedProcess proceso3 = new ClassificationParallelepipedProcess();
		proceso3.addParam("layer",lyr);
		proceso3.addParam("bandList",new int[]{0,1,2});
		proceso3.addParam("rois",listRois);
		proceso3.addParam("dev", new Double(3));
		proceso3.run();


		IBuffer result3= (IBuffer) proceso3.getResult();

		assertEquals(result3.getBandCount(),1);

		//Comparación de valores
		assertEquals(result3.getElemByte(0, 0,0),0);
		assertEquals(result3.getElemByte(0, 1,0),0);
		assertEquals(result3.getElemByte(0, 2,0),0);
		assertEquals(result3.getElemByte(0, 3,0),2);

		assertEquals(result3.getElemByte(1, 0,0),0);
		assertEquals(result3.getElemByte(1, 1,0),0);
		assertEquals(result3.getElemByte(1, 2,0),0);
		assertEquals(result3.getElemByte(1, 3,0),2);

		assertEquals(result3.getElemByte(2, 0,0),1);
		assertEquals(result3.getElemByte(2, 1,0),1);
		assertEquals(result3.getElemByte(2, 2,0),0);
		assertEquals(result3.getElemByte(2, 3,0),0);

		assertEquals(result3.getElemByte(3, 0,0),0);
		assertEquals(result3.getElemByte(3, 1,0),0);
		assertEquals(result3.getElemByte(3, 2,0),0);
		assertEquals(result3.getElemByte(3, 3,0),0);

	}

}

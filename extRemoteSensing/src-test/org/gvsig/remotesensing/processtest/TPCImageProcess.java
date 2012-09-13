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

import junit.framework.TestCase;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.Grid;
import org.gvsig.remotesensing.principalcomponents.PCImageProcess;
import org.gvsig.remotesensing.principalcomponents.PCStatistics;

import Jama.Matrix;


/**
* Este test prueba el proceso de construcción de la imagen resultante del proceso
* de analisis de componentes principales de la imagen pc_CreateImageTest.tif de
* dimensiones 4x4 y tres bandas.
*
* @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
* */
public class TPCImageProcess extends TestCase {

	private String baseDir = "./test-images/";
	private String path1 = baseDir + "pc_CreateImageTest.tif";
	FLyrRasterSE lyr = null;
	Grid dataGrid=null;
	IBuffer   result= null;

	static{
		RasterLibrary.wakeUp();
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TPCImageProcess running...");
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
			e.printStackTrace();
		}
	}

	public void testStack() {

		PCStatistics pcStatistics = new PCStatistics(new Matrix(new double[][]{{-0.922766631036,0.2990630039783, 0.2430289371596},
				   {-0.030678162029,-0.685664096085, 0.7272713370632},
                   {0.3841361672896,0.6636460404393, 0.6418826512605}}),
                   new double[]{425.104,1753.227,18714.369},
                   null);

		PCImageProcess iProcess = new PCImageProcess ();
		iProcess.addParam("inputRasterLayer",lyr);
		iProcess.addParam("statistics",pcStatistics);
		iProcess.addParam("selectedBands", new boolean[]{true,true,true});
		iProcess.addParam("selectedComponents",new boolean[]{true,true,true});
		iProcess.run();
		result=iProcess.getBufferResult();
		compare();
	}

	private void compare() {

		// Mismo numero de bandas
		assertEquals(result.getBandCount(),3);

		//Comparación de valores
		assertEquals(result.getElemFloat(0, 0,0),1977.6109,0.1);
		assertEquals(result.getElemFloat(0, 1,0),1864.9603,0.1);
		assertEquals(result.getElemFloat(0, 2,0),1896.0991,0.1);
		assertEquals(result.getElemFloat(0, 3,0),1768.2855,0.1);

		assertEquals(result.getElemFloat(1, 0,0),2081.7544,0.1);
		assertEquals(result.getElemFloat(1, 1,0),2062.1047,0.1);
		assertEquals(result.getElemFloat(1, 2,0),1989.8268,0.1);
		assertEquals(result.getElemFloat(1, 3,0),1777.2758,0.1);

		assertEquals(result.getElemFloat(2, 0,0),2212.70,0.1);
		assertEquals(result.getElemFloat(2, 1,0),2251.86,0.1);
		assertEquals(result.getElemFloat(2, 2,0),2094.61,0.1);
		assertEquals(result.getElemFloat(2, 3,0),1907.58,0.1);

		assertEquals(result.getElemFloat(3, 0,0),1986.52,0.1);
		assertEquals(result.getElemFloat(3, 1,0),2056.09,0.1);
		assertEquals(result.getElemFloat(3, 2,0),2155.27,0.1);
		assertEquals(result.getElemFloat(3, 3,0),2060.12,0.1);

		// Comparación de los resultados de la banda 2

		assertEquals(result.getElemFloat(0, 0,1),621.25,0.1);
		assertEquals(result.getElemFloat(0, 1,1),592.24,0.1);
		assertEquals(result.getElemFloat(0, 2,1),623.04,0.1);
		assertEquals(result.getElemFloat(0, 3,1),669.02,0.1);

		assertEquals(result.getElemFloat(1, 0,1),639.79,0.1);
		assertEquals(result.getElemFloat(1, 1,1),621.29,0.1);
		assertEquals(result.getElemFloat(1, 2,1),632.87,0.1);
		assertEquals(result.getElemFloat(1, 3,1),678.50,0.1);

		assertEquals(result.getElemFloat(2, 0,1),597.30,0.1);
		assertEquals(result.getElemFloat(2, 1,1),637.98,0.1);
		assertEquals(result.getElemFloat(2, 2,1),652.07,0.1);
		assertEquals(result.getElemFloat(2, 3,1),633.76,0.1);

		assertEquals(result.getElemFloat(3, 0,1),538.32,0.1);
		assertEquals(result.getElemFloat(3, 1,1),610.48,0.1);
		assertEquals(result.getElemFloat(3, 2,1),713.41,0.1);
		assertEquals(result.getElemFloat(3, 3,1),704.34,0.1);

		// Comparación de los resultados de la banda 3

		assertEquals(result.getElemFloat(0, 0,2),302.79,0.1);
		assertEquals(result.getElemFloat(0, 1,2),301.65,0.1);
		assertEquals(result.getElemFloat(0, 2,2),318.75,0.1);
		assertEquals(result.getElemFloat(0, 3,2),335.75,0.1);

		assertEquals(result.getElemFloat(1, 0,2),336.23,0.1);
		assertEquals(result.getElemFloat(1, 1,2),283.88,0.1);
		assertEquals(result.getElemFloat(1, 2,2),267.51,0.1);
		assertEquals(result.getElemFloat(1, 3,2),304.35,0.1);

		assertEquals(result.getElemFloat(2, 0,2),315.77,0.1);
		assertEquals(result.getElemFloat(2, 1,2),302.42,0.1);
		assertEquals(result.getElemFloat(2, 2,2),301.33,0.1);
		assertEquals(result.getElemFloat(2, 3,2),286.24,0.1);

		assertEquals(result.getElemFloat(3, 0,2),314.02,0.1);
		assertEquals(result.getElemFloat(3, 1,2),317.80,0.1);
		assertEquals(result.getElemFloat(3, 2,2),336.10,0.1);
		assertEquals(result.getElemFloat(3, 3,2),271.26,0.1);

	}
}

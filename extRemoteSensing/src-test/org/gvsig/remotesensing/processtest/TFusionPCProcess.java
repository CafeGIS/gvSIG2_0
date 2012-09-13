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
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.grid.Grid;
import org.gvsig.remotesensing.principalcomponents.PCImageProcess;
import org.gvsig.remotesensing.principalcomponents.PCStatisticsProcess;

import Jama.Matrix;


/**
* Este test prueba la obtencion de la imagen original a partir de las
* componetes principales calculadas. En el proceso de fusión de imágenes
* por el método de PCA está básado en transformación inversa PCA.
*
* ** @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
* */

public class TFusionPCProcess extends TestCase {

	private String baseDir = "./test-images/";
	private String path1 = baseDir+"pc_CreateImageTest.tif";
	FLyrRasterSE lyr = null;

	static{
		RasterLibrary.wakeUp();
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TFusionPC running...");
		try{
			lyr = FLyrRasterSE.createLayer(
					path1,
					path1,
					null
					);

		} catch (LoadLayerException e) {
			System.out.print("Error en la construcción de la capa");
		}
	}

	public void testStack() {

		PCStatisticsProcess sProcess= new PCStatisticsProcess();
		sProcess.addParam("selectedBands",new boolean[]{true,true,true});
		sProcess.addParam("inputRasterLayer",lyr);
		sProcess.run();

		Matrix autoV=sProcess.getAutoVectorMatrix();
		// Reordenamos en orden descencente del valor de los autovectores
		int resultOrden[]= new int[autoV.getRowDimension()];
		int cont = autoV.getRowDimension()-1;
		for(int i=0;i<autoV.getRowDimension();i++){
					resultOrden[i]=cont;
					cont--;
		}
		double order[][]= new double[autoV.getRowDimension()][autoV.getColumnDimension()];
		for(int i=0; i<resultOrden.length;i++)
			for(int j=0; j<autoV.getColumnDimension();j++)
				order[i][j]=autoV.get(j,resultOrden[i]);


		PCImageProcess imgProcess= new PCImageProcess();
		imgProcess.addParam("inputRasterLayer",lyr);
		imgProcess.addParam("statistics",sProcess.getResult());
		imgProcess.addParam("selectedBands",new boolean[]{true,true,true});
		imgProcess.addParam("selectedComponents",new boolean[]{true,true,true});
		imgProcess.addParam("outputPath",new String ("outfile.tif"));
		imgProcess.run();

		FLyrRasterSE resultado= (FLyrRasterSE) imgProcess.getResult();
		Matrix inverse=autoV.inverse();

		BufferFactory ds1 = new BufferFactory(resultado.getDataSource());
		BufferFactory ds2 = new BufferFactory(lyr.getDataSource());
		Grid dataGrid=null; Grid dataOrigen=null;
		try {
			dataGrid= new Grid(ds1);
			dataOrigen= new Grid(ds2);
		} catch (RasterBufferInvalidException e) {
			e.printStackTrace();
		}
		float newData[]= new float[3];
		float data[] = new float[3];


		RasterBuffer rasterResult= RasterBuffer.getBuffer(RasterBuffer.TYPE_FLOAT, dataGrid.getRasterBuf().getWidth(), dataGrid.getRasterBuf().getHeight(), dataGrid.getBandCount(), true);
		for(int row=0; row<dataGrid.getRasterBuf().getHeight(); row++){
			for(int col=0; col<dataGrid.getRasterBuf().getWidth();col++){
				dataGrid.getRasterBuf().getElemFloat(row,col,data);{

						newData=solveSystem(new Matrix(order),data);
						//newData[i]+= data[i]*inverse.get(resultOrden[i],j);
				}
				rasterResult.setElemFloat(row,col,newData);
			}
		}


//		 Comprobar que el resultado de la imagen original es el mismo que tras aplicar la inversa a las componentes

	for(int i=0; i<dataGrid.getRasterBuf().getHeight();i++)
		for(int j=0; j<dataGrid.getRasterBuf().getHeight();j++){
			assertEquals(rasterResult.getElemFloat(i,j,0),(float)dataOrigen.getRasterBuf().getElemShort(i,j,0),0.1);
			assertEquals(rasterResult.getElemFloat(i,j,1),(float)dataOrigen.getRasterBuf().getElemShort(i,j,1),0.1);
			assertEquals(rasterResult.getElemFloat(i,j,2),(float)dataOrigen.getRasterBuf().getElemShort(i,j,2),0.1);
		}
	}


	public float[] solveSystem(Matrix matrix, float columResult[]){
		float xCoef[] = new float[3];
		double[][] a = new double[columResult.length][1];
		for (int i = 0; i < columResult.length; i++)
			a[i][0] = columResult[i];
		Matrix c = null;
		//if (matrix.det() == 0.0) {
			// Resolucion del sistema usando la libreria flanagan
		//	flanagan.math.Matrix matrixFL = new flanagan.math.Matrix(matrix.getArray());
		//	xCoef = matrixFL.solveLinearSet(columResult);
		//} else {
		c = matrix.solve(new Matrix(a));
		for (int i = 0; i < columResult.length; i++)
				xCoef[i] = (float)c.get(i, 0);
	//	}
		return xCoef;
	}


}

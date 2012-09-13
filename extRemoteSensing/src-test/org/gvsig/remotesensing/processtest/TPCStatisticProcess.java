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

import java.io.File;

import junit.framework.TestCase;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.remotesensing.principalcomponents.PCStatisticsProcess;

import Jama.Matrix;


/**
* Este test prueba el proceso de calculo de las matriz de autovectores de la
* imagen "PCA_bouldr_tm_int16.img", de seis bandas.
* Los elementos de la matriz obtenida en el proceso se comparan en valor absoluto
* comparan con los elementos de la matriz de autovalores proporcionada por ERDAS.
*
* ** @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
* */

public class TPCStatisticProcess extends TestCase {

	private String baseDir = "./test-images/";
	private String path1 = baseDir + "bouldr_tm_Int16.dat";

	static{
		RasterLibrary.wakeUp();
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TPCStatisticProcess running...");
	}

	public void testStack() {

		// Matriz de autovectores obtenida con ERDAS
		double autoVectorMatrixERDAS[][] = {
				{0.4467930765462516 ,-0.2132448427522436, 0.61999384198935, 0.5583203571234574 ,0.1212877203683758 ,0.2099473443357927 },
				{0.2302721175129133 ,-0.06284659594556273, 0.2921484644686843 ,-0.1714351492532642, -0.09649740387440189 ,-0.9049710341330732},
				{0.287184731205834, 0.07705684070022263, 0.3977736576013743, -0.7037344497503186, -0.351412521922381 ,0.3669200927798035},
				{0.3884643668946926, -0.7858370140117444 ,-0.408108215862561 ,-0.2060071443748747 ,0.1431802274591002, 0.04542891295109729},
				{0.6376828181240171, 0.4126887053417631, -0.451856773747645, 0.2418214698550539, -0.4001988234001318, -0.01540747915579309},
				{0.3276151521697355 ,0.3959638943275853 ,-0.0361409474886444, -0.2504588305907446 ,0.8196505984901843 ,0.004243695755382132}
		};


		FLyrRasterSE lyr = null;
		int endIndex = path1.lastIndexOf(".");
		if (endIndex < 0)
			endIndex = path1.length();
		try {
			lyr = FLyrRasterSE.createLayer(
					path1.substring(path1.lastIndexOf(File.separator) + 1, endIndex),
					path1,
				null
					);
		} catch (LoadLayerException e) {
			System.out.print("Error en la construcción de la capa");
		}


		PCStatisticsProcess sProcess= new PCStatisticsProcess();
		sProcess.addParam("selectedBands",new boolean[]{true,true,true,true,true,true});
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

		/** La comparacion se realiza en valor absoluto, ya que el criterio de signos
		 *  puede variar */

		for(int i=0; i<autoVectorMatrixERDAS.length;i++)
			for(int j=0;j<autoVectorMatrixERDAS[0].length;j++)
			{
				assertEquals(java.lang.Math.abs(autoVectorMatrixERDAS[i][j]), java.lang.Math.abs(autoV.get(i, resultOrden[j])), 0.005);
			}
	}
}

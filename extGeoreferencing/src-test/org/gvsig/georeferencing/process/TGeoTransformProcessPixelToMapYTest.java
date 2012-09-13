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

package org.gvsig.georeferencing.process;

import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.gvsig.georeferencing.process.geotransform.GeoTransformDataResult;
import org.gvsig.georeferencing.process.geotransform.GeoTransformProcess;
import org.gvsig.raster.datastruct.GeoPoint;

import Jama.Matrix;

/**Test que prueba la el proceso de geotransfornmcion dados una serie de puntos de
 * control.  Compara los coeficientes obtenidos para el polinomio de transformacion  
 * (de coordenadas pixel a coordenadas mapa para las Y), con los obtenidos 
 * manualmente, resolviendo por el método de Cramer.
 * 
 * 
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * */

public class TGeoTransformProcessPixelToMapYTest extends TestCase {

	private GeoPoint gpcs[] = new GeoPoint[3];
	private double geoPoints[][] ={{ 1369.000000  	,	2985.750000	},
								   { 1673.500000  	,   2803.250000	},
								   { 2092.500000 	,   2933.250000	},
							       }; 
								   
	private double imagePoints[][]={{ 577.500000     ,	2427.500000 },
									{ 803.000000     ,  2235.500000 },
									{ 1165.500000    ,  2285.250000	},
									};
	public void start() {
		this.testStack();
	}
	
	
	public void testStack() {
		System.err.println("TGeoTransformProcessPixelToMapYTest running...");
		
		for(int i=0;i<geoPoints.length;i++)
		{
			Point2D  pW = new Point2D.Double(geoPoints[i][0],geoPoints[i][1]);
			Point2D  pP = new Point2D.Double(imagePoints[i][0],imagePoints[i][1]);
			gpcs[i]= new GeoPoint(pP,pW);
		}
		
		GeoTransformProcess proceso = new GeoTransformProcess();
		proceso.addParam("gpcs",gpcs);
		proceso.addParam("orden", new Integer(1));
		proceso.run();
		
		GeoTransformDataResult resultado= (GeoTransformDataResult) proceso.getResult();
		
		// Estimacion de los coeficientes de forma manual, siquiento la teoria del manual de Jose Gonzalez
		// Matriz para el ejmplo correspondiente a la figura 2.20 del documento.
		double M[][]={
					{3,			2546,			6948.25},
					{2546,		2336705.5,		5860446.625},
					{6948.25,	5860446.625,	16112584.0625}
					};
		Matrix m_M = new Matrix(M);
		double det= m_M.det();
		
		
		double A0[][]={
				{8722.25,			2546,			6948.25},
				{7393983.25,		2336705.5,		5860446.625},
				{20217783.062,		5860446.625,	16112584.0625}
		};
		Matrix m_A0= new Matrix (A0);
		double coef0= m_A0.det()/det;
		
		
		
		double A1[][]={
				{3,			8722.25,		  6948.25},
				{2546,		7393983.25,	  	  5860446.625},
				{6948.25,	20217783.062,	  16112584.0625}
		};
		Matrix m_A1= new Matrix (A1);
		double coef1= m_A1.det()/det;
		
		double A2[][]={
				{3,			2546,			8722.25,		  },
				{2546,		2336705.5,		7393983.25,	  	  },
				{6948.25,	5860446.625,	20217783.062,	  }
				};
			
		Matrix m_A2= new Matrix (A2);
		double coef2= m_A2.det()/det;
		
		// Comprobacion de los coeficientes obtenidos tras el proceso con los determinados de forma manual
		
		assertEquals(coef0, resultado.getPixelToMapCoefY()[0],0.1);
		assertEquals(coef1, resultado.getPixelToMapCoefY()[1],0.1);
		assertEquals(coef2, resultado.getPixelToMapCoefY()[2],0.1);
		
	}
}

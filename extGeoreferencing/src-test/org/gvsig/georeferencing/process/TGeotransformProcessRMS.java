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
import org.gvsig.raster.datastruct.GeoPointList;

/**
 * Test que prueba la el proceso de geotransfornmcion dados una serie de puntos de
 * control.  Compara las coordenadas aproximadas tras el calculo de los polinomios de transformación.
 * El punto en coordenadas mapa que se evalua es (1500,2500). Se comprueban los errores obtenidos tras el proceso 
 * con los calculados manualmente.
 * 
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * */

public class TGeotransformProcessRMS extends TestCase {

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
		System.err.println("TGeotransformProcessRMS running...");
		
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
		double coordPixel[]= resultado.getCoordPixel(1500,2500);
		
		// Aproximacion manual conocidos los coeficientes
		double x_value= -957.700+0.822655*1500+0.1369789*2500;
		double y_value= 155.357-0.13683*1500+0.82373*2500;
		assertEquals(coordPixel[0],x_value, 0.1);
		assertEquals(coordPixel[1],y_value, 0.1);
		
		
		// Se evaluan los RMS de los tres puntos de partida y se comparan con los obtenidos 
		// tras la transformacióm.
		double x_1= -957.700+0.822655*1369+0.1369789*2985.75;
		double x_2= -957.700+0.822655*1673.5+0.1369789*2803.25;
		double x_3= -957.700+0.822655*2092.5+0.1369789*2933.25;
		
		double y_1= 155.357-0.13683*1369+0.82373*2985.75;
		double y_2= 155.357-0.13683*1673.5+0.82373*2803.25;
		double y_3= 155.357-0.13683*2092.5+0.82373*2933.25;
		
		assertEquals(x_1,resultado.getCoordPixel(1369.000000 ,2985.750000)[0],0.1);
		assertEquals(y_1,resultado.getCoordPixel(1369.000000 ,2985.750000)[1],0.1);
		
		assertEquals(x_2,resultado.getCoordPixel(1673.500000, 2803.250000)[0],0.1);
		assertEquals(y_2,resultado.getCoordPixel(1673.500000, 2803.250000)[1],0.1);
		
		assertEquals(x_3,resultado.getCoordPixel(2092.500000 , 2933.250000)[0],0.1);
		assertEquals(y_3,resultado.getCoordPixel(2092.500000 , 2933.250000)[1],0.1);
		
	
		GeoPointList list = resultado.getGpcs();

		// Comparativa del error en x
		assertEquals(Math.pow(x_1 - gpcs[0].pixelPoint.getX(), 2), list.get(0).getErrorX(), 0.001);
		assertEquals(Math.pow(x_2 - gpcs[1].pixelPoint.getX(), 2), list.get(1).getErrorX(), 0.001);
		assertEquals(Math.pow(x_3 - gpcs[2].pixelPoint.getX(), 2), list.get(2).getErrorX(), 0.001);

		// Comparativa del error en y
		assertEquals(Math.pow(y_1 - gpcs[0].pixelPoint.getY(), 2), list.get(0).getErrorY(), 0.001);
		assertEquals(Math.pow(y_2 - gpcs[1].pixelPoint.getY(), 2), list.get(1).getErrorY(), 0.001);
		assertEquals(Math.pow(y_3 - gpcs[2].pixelPoint.getY(), 2), list.get(2).getErrorY(), 0.001);	
				
		double rms1=Math.sqrt( Math.pow(x_1 - gpcs[0].pixelPoint.getX(), 2)+ Math.pow(y_1 - gpcs[0].pixelPoint.getY(), 2));
		double rms2=Math.sqrt( Math.pow(x_2 - gpcs[1].pixelPoint.getX(), 2)+ Math.pow(y_2 - gpcs[1].pixelPoint.getY(), 2));
		double rms3=Math.sqrt( Math.pow(x_3 - gpcs[2].pixelPoint.getX(), 2)+ Math.pow(y_3 - gpcs[2].pixelPoint.getY(), 2));
		
		assertEquals((rms1+rms2+rms3)/3, resultado.getRmsTotal(),0.01);
		
		
	}
}

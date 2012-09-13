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

/**
 *  Test para el proceso de transformacion.
 *  
 *  El test prueba transformacion de orden dos calculados a partir de una lista de diez puntos.
 *  Los resultados de la transformacion son comparados con los calculos realizados de forma manual.
 *  Se compara la evaluacion de los polinomios para los cuatro primeros puntos de la lista tanto para 
 *  la funcion directa como la inversa. Se compara el rms obtenido en cada punto.
 *   
 *  @author Alejandro Muñoz Sanchez
 * 
 * */

public class TestGeoTransformProcess  extends TestCase{

	private GeoPoint gpcs[] = new GeoPoint[10];
	private GeoTransformDataResult transformData;
	
	private double geoPoints[][] ={	{ 1369.000000  	,	2985.750000	},
									{ 1673.500000  	,   2803.250000	},
									{ 2092.500000 	,   2933.250000	},
									{ 2354.000000  	,   3211.000000	},
									{ 2731.500000    ,  3193.500000	},
									{ 3054.500000    ,  3079.500000	},
									{ 3009.250000    , 	3380.500000	},
									{ 3057.250000    ,  3552.750000	},
									{ 2944.750000    ,  3584.750000	},
									{ 2892.500000    ,  3444.000000	},}; 

								   
	private double imagePoints[][]={{ 577.500000     ,	2427.500000 },
									{ 803.000000     ,  2235.500000 },
									{ 1165.500000    ,  2285.250000	},
									{ 1419.250000    ,  2478.750000 },
									{ 1727.000000    ,  2412.250000 },
									{ 1977.500000    ,  2274.250000	},
									{ 1981.500000    ,  2528.250000	},
									{ 2044.000000    ,  2663.750000 },
									{ 1955.750000    ,  2705.500000 }, 
									{ 1893.500000    , 	2596.250000},};
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	
	public void setUp(){
	}
	
	public void testStack(){
		for(int i=0;i<geoPoints.length;i++)
			{
				Point2D  pW = new Point2D.Double(geoPoints[i][0],geoPoints[i][1]);
				Point2D  pP = new Point2D.Double(imagePoints[i][0],imagePoints[i][1]);
				gpcs[i]= new GeoPoint(pP,pW);
			}
	
		GeoTransformProcess order2Transform = new GeoTransformProcess();
		order2Transform.addParam("gpcs",gpcs );
		order2Transform.addParam("orden",new Integer(2));
		order2Transform.run();	
		transformData= (GeoTransformDataResult)order2Transform.getResult();

		
// 		Resolucion del sistema  calculado manualmente
		double data[][]={
				{10,			15544.5,			24607.25,		 26758589.88,			38703411.06	,			60795805.19	  	},		
				{15544.5,		26758589.88,		38703411.06,	 48625448159.44,		67148886519.52,			96748134874.2 	},
				{24607.25,		38703411.06,		6.07958051875E7, 67148886519.52,		96748134874.2,			150805605253.3	},
				{26758589.88,	48625448159.44,		67148886519.52,	 90973349316414.1,		1.22623111101991E014,	1.69145826718794E014},
				{38703411.06,	67148886519.52,		96748134874.2,	 1.22623111101991E014,	1.69145826718794E014,	2.42781993510560E014},
				{60795805.19,	96748134874.2, 		150805605253.3,	 1.69145826718794E014,	2.42781993510560E014,	3.7555377732236494E14}	
		};
		double[][] result={{25178.75},{42119706.94}, {62445537.63}, {75433589688.61}, {105416642600.75}, {155494570974.03} };
		double[][] result2={{32168.25},{5.10484513125E07}, {7.9534535625E07}, {8.899835767423438E10}, {1.276577668393125E11}, {1.9743243258296875E11} };
		
		
//		 Resolucion del sistema  calculado manualmente
		double data2[][]={
				{10,				25178.75,				32168.25,		 		 6.68261699375E7,			8.2157005375E7	,			1.041305340625E8 },		
				{25178.75,			6.68261699375E7,		8.2157005375E7,	 		 1.8389906361945312E11,		2.2036071049725E11,			2.69580937964375E11 },
				{32168.25,			8.2157005375E7,			1.041305340625E8, 		 2.2036071049725E11,		2.69580937964375E11,		3.391527268821094E11},
				{6.68261699375E7,	1.8389906361945312E11,	2.2036071049725E11,	 	 5.185110007030175E14,		6.109291412182252E14,		7.302233191555645E14},
				{8.2157005375E7,	2.2036071049725E11,		2.69580937964375E11,	 6.109291412182252E14,		7.302233191555645E14,		8.893696706241302E14},
				{1.041305340625E8,	2.69580937964375E11, 	3.391527268821094E11,	 7.302233191555645E14,		8.893696706241302E14,		1.1112299175456508E15}	
		};
		
		double[][] result3={{24607.25},{6.2445537625E7}, {7.9534535625E7}, {1.6674342953621875E11}, {2.0468178349823438E11}, {2.5867104246403125E11} };
		double[][] result4={{15544.5},{4.21197069375E7}, {5.10484513125E7}, {1.1748417342507812E11}, {1.3954105605085938E11}, {1.6852035242098438E11} };
		
		
		Matrix matrix= new Matrix(data);
		Matrix matrix2= new Matrix(data2);
		
		Matrix coef=matrix.solve(new Matrix(result)); 
		Matrix coef2= matrix.solve(new Matrix(result2));
		Matrix coef3= matrix2.solve(new Matrix(result3));
		Matrix coef4= matrix2.solve(new Matrix(result4));
		
		// Comparacion de los coeficientes con los calculados manualmente
		for(int i=0; i<transformData.getPixelToMapCoefX().length; i++){
			assertEquals(coef.get(i,0),transformData.getPixelToMapCoefX()[i],0.2);
			assertEquals(coef2.get(i,0),transformData.getPixelToMapCoefY()[i],0.2);
			assertEquals(coef3.get(i,0),transformData.getMapToPixelCoefY()[i],0.2);
			assertEquals(coef4.get(i,0),transformData.getMapToPixelCoefX()[i],0.2);
		}
		
		// Evaluacion del polinomio en los cuatro primeros puntos de la lista
		double evaluate[][]= {{577.49,2427.49},{803 , 2235.5},{1165.49,2285.25},{1419.25,2478.75}};
		for(int i=0; i<4; i++)
		{
			evaluate[i]=transformData.getCoordPixel(gpcs[i].mapPoint.getX(),gpcs[i].mapPoint.getY());
			assertEquals(evaluate[i][0],gpcs[i].getEvaluateX(),0.3);
			assertEquals(evaluate[i][1],gpcs[i].getEvaluateY(),0.3);
		
		}
		
		double  rms []= {2.5846147599746093E-4,	1.4315791732756158E-4, 1.4937615015849659E-4,4.836399323913263E-4};
		for(int i=0; i<4; i++)
			assertEquals(gpcs[i].getRms(),rms[i],0.1);
		
	}	
}

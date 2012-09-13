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

package org.gvsig.georeferencing.process.geotransform;

import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.datastruct.GeoPoint;
import org.gvsig.raster.datastruct.GeoPointList;
import org.gvsig.raster.util.RasterToolsUtil;

import Jama.Matrix;

/**
 * Clase que representa una transformacion polinomial para la convertir las
 * coordenadas de una imagen en la imagen rectificada.
 * 
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @version 20/1/2008
 */
public class GeoTransformProcess extends RasterProcess {

	// Lista de puntos de control.
	private GeoPointList   gpcs              = null;

	// porcentage del proceso global de la tarea
	private int            percent           = 0;

	// orden del polinomio aproximador
	protected int          orden             = 0;

	// numero minimo de puntos
	protected int          minGPC            = 0;

	// Lista con los valores de las potencias de x e y
	private int            exp[][]           = null;

	// rms total en las x
	private double         rmsXTotal         = 0;

	// rms total en las y
	private double         rmsYTotal         = 0;

	// rms total
	private double         rmsTotal          = 0;

	GeoTransformDataResult resultData        = null;

	// coeficientes polinomios conversion coordenadas reales a coordenadas pixel
	private double         mapToPixelCoefX[] = null;
	private double         mapToPixelCoefY[] = null;

	// coeficientes polinomio conversion coordenadas pixel a coordenadas reales
	private double         pixelToMapCoefX[] = null;
	private double         pixelToMapCoefY[] = null;

	/**
	 * Metodo que recoge los parametros del proceso de transformación
	 * <LI>gpcs: lista de puntos de control</LI>
	 * <LI>orden: orden del polinomio de transformacion</LI>
	 */
	public void init() {
		gpcs = (GeoPointList) getParam("gpcs");
		orden = (int) getIntParam("orden");
		minGPC = (orden + 1) * (orden + 2) / 2;
		exp = new int[minGPC][2];
		resetErrors();
		resultData = new GeoTransformDataResult();
		// Chequear si el numero de puntos es suficiente para determinar la transformacion de orden n.
		if (!enoughPoints()) {
			minGPC = 0;
		}
	}
	
	/**
	 * Inicializa los valores de los errores a 0
	 */
	private void resetErrors() {
		for (int i = 0; i < gpcs.size(); i++)
			gpcs.get(i).resetErrors();
	}
	
	/**
	 * @return true si se proporciona el numero minimo de puntos de entrada 
	 * para la transformación de orden seleccionado. false en caso contrario.
	 */		
	public boolean enoughPoints() {
		return (gpcs.size() >= minGPC);
	}

	/**
	 * Obtiene el resultado del proceso.
	 * @return
	 */
	public Object getResult() {
		return resultData;
	}
	
	/**
	 *  Proceso
	 */
	public void process() {
		if (minGPC > 0) {
			// Obtencion los polinomios de transformación
			calculatePolinomialCoefs();
			// calculo de los resultados
			calculateRMSerror();
			// Se almacenan los resultados en dataResult
			resultData.setGpcs(gpcs);
			resultData.setPixelToMapCoefX(pixelToMapCoefX);
			resultData.setPixelToMapCoefY(pixelToMapCoefY);
			resultData.setMapToPixelCoefX(mapToPixelCoefX);
			resultData.setMapToPixelCoefY(mapToPixelCoefY);
			resultData.setRmsXTotal(rmsXTotal);
			resultData.setRmsYTotal(rmsYTotal);
			resultData.setRmsTotal(rmsTotal);
			resultData.setPolynomialOrden(orden);

			if (externalActions != null)
				externalActions.end(resultData);
			return;
		}
		resultData = null;
	}

	/**
	 * Calculo de los coeficientes de los polinomios de aproximacion.
	 */
	public void calculatePolinomialCoefs() {
		double matrixD[][] = new double[minGPC][minGPC];
		double matrixD2[][] = new double[minGPC][minGPC];
		double result[] = new double[minGPC];
		double result2[] = new double[minGPC];
		double result3[] = new double[minGPC];
		double result4[] = new double[minGPC];
		int k = -1;
		// Obtencion de la primera fila de la matriz
		for (int filas = 0; filas < minGPC; filas++) {
			k = -1;
			for (int i = 0; i <= orden; i++) {
				for (int j = 0; j <= i; j++) {
					k++;
					for (int v = 0; v < gpcs.size(); v++) {
						GeoPoint point = gpcs.get(v);
						if (point.active) {
							matrixD[filas][k] += (Math.pow(point.pixelPoint.getX(), i - j) * Math.pow(point.pixelPoint.getX(), exp[filas][0])) * (Math.pow(point.pixelPoint.getY(), j) * Math.pow(point.pixelPoint.getY(), exp[filas][1]));
							matrixD2[filas][k] += (Math.pow(point.mapPoint.getX(), i - j) * Math.pow(point.mapPoint.getX(), exp[filas][0])) * (Math.pow(point.mapPoint.getY(), j) * Math.pow(point.mapPoint.getY(), exp[filas][1]));
						}
					}
					// Para la fila 0 se guardan los exponentes
					if (filas == 0) {
						exp[k][0] = i - j;
						exp[k][1] = j;

						// Se calcula el resultado de !!!!!
						for (int v = 0; v < gpcs.size(); v++) {
							GeoPoint point = gpcs.get(v);
							if (point.active) {
								result[k] += (Math.pow(point.pixelPoint.getX(), i - j) * Math.pow(point.pixelPoint.getX(), exp[filas][0])) * (Math.pow(point.pixelPoint.getY(), j) * Math.pow(point.pixelPoint.getY(), exp[filas][1])) * point.mapPoint.getY();
								result2[k] += (Math.pow(point.mapPoint.getX(), i - j) * Math.pow(point.mapPoint.getX(), exp[filas][0])) * (Math.pow(point.mapPoint.getY(), j) * Math.pow(point.mapPoint.getY(), exp[filas][1])) * point.pixelPoint.getY();
								result3[k] += (Math.pow(point.pixelPoint.getX(), i - j) * Math.pow(point.pixelPoint.getX(), exp[filas][0])) * (Math.pow(point.pixelPoint.getY(), j) * Math.pow(point.pixelPoint.getY(), exp[filas][1])) * point.mapPoint.getX();
								result4[k] += (Math.pow(point.mapPoint.getX(), i - j) * Math.pow(point.mapPoint.getX(), exp[filas][0])) * (Math.pow(point.mapPoint.getY(), j) * Math.pow(point.mapPoint.getY(), exp[filas][1])) * point.pixelPoint.getX();
							}
						}
					}
				}
			}
		}
		
		Matrix matrixResult= new Matrix(matrixD);
		Matrix matrixResult2= new Matrix(matrixD2);
		pixelToMapCoefY=solveSystem(matrixResult,result);	
		mapToPixelCoefY=  solveSystem(matrixResult2,result2);
		pixelToMapCoefX=solveSystem(matrixResult,result3);	
		mapToPixelCoefX=  solveSystem(matrixResult2,result4);
	}

	/**
	 * @return array con la solucion al sistema de ecuadiones.
	 */
	public double[] solveSystem(Matrix matrix, double columResult[]){
		double xCoef[] = new double[minGPC];
		double[][] a = new double[columResult.length][1];
		for (int i = 0; i < columResult.length; i++)
			a[i][0] = columResult[i];
		Matrix c = null;
		if (matrix.det() == 0.0) {
			// Resolucion del sistema usando la libreria flanagan
			flanagan.math.Matrix matrixFL = new flanagan.math.Matrix(matrix.getArray());
			xCoef = matrixFL.solveLinearSet(columResult);
		} else {
			c = matrix.solve(new Matrix(a));
			for (int i = 0; i < columResult.length; i++)
				xCoef[i] = c.get(i, 0);
		}
		return xCoef;
	}

	/**
	 * @return vector con los RMS
	 */
	public void calculateRMSerror() {
		int numgpcs = gpcs.size();
		double rmsxTotal = 0;
		double rmsyTotal = 0;
		rmsTotal = 0;
		int num = 0;

		for (int im_point = 0; im_point < numgpcs; im_point++) {
			GeoPoint point = gpcs.get(im_point);
			if (point.active) {
				double tmp[] = getCoordMap(point.pixelPoint.getX(), point.pixelPoint.getY());
				double tmp2[] = getCoordPixel(tmp[0], tmp[1]);
				point.setEvaluateX(tmp2[0]);
				point.setEvaluateY(tmp2[1]);
				point.setErrorX(Math.pow(point.getEvaluateX() - point.pixelPoint.getX(), 2));
				rmsxTotal += point.getErrorX();
				point.setErrorY(Math.pow(point.getEvaluateY() - point.pixelPoint.getY(), 2));
				rmsyTotal += point.getErrorY();
				point.setRms(Math.sqrt(point.getErrorX() + point.getErrorY()));
				rmsTotal += point.getRms();
				num++;
			}
		}

		this.rmsTotal = Math.sqrt(rmsTotal / num);
		this.rmsXTotal = Math.sqrt(rmsxTotal / num);
		this.rmsYTotal = Math.sqrt(rmsyTotal / num);
	}

	/**
	 * @return error total para la coordenada X
	 */
	public double getRMSx() {
		return rmsXTotal;
	}

	/**
	 * @return error total para la coordenada y
	 */
	public double getRMSy() {
		return rmsYTotal;
	}

	/**
	 * @return error total
	 */
	public double getRMSTotal() {
		return rmsTotal;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getTitle()
	 */
	public String getTitle() {
		return RasterToolsUtil.getText(this, "transformacion");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.RasterProcess#getLog()
	 */
	public String getLog() {
		return RasterToolsUtil.getText(this, "calculando_transformacion");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#getPercent()
	 */
	public int getPercent() {
		return percent;
	}

	/**
	 *  Función que evalua el polinomio de transformación para obtener las coordenadas
	 *  reales de unas coordenadas pixeles que se pasan como parametros.
	 * @param x coordenada x del punto
	 * @param y coordenada y del punto
	 * @return resultado de la evaluación para x e y
	 */
	public double[] getCoordMap(double x, double y) {
		// setExpPolinomial();
		double eval[] = new double[2];
		for (int i = 0; i < pixelToMapCoefX.length; i++) {
			eval[0] += pixelToMapCoefX[i] * Math.pow(x, exp[i][0]) * Math.pow(y, exp[i][1]);
			eval[1] += pixelToMapCoefY[i] * Math.pow(x, exp[i][0]) * Math.pow(y, exp[i][1]);
		}
		return eval;
	}

	
	/**
	 *  Función que evalua el polinomio de transformación para obtener las coordenadas
	 *  pixeles de unas coordenadas mapa en un proceso de transformacion.
	 * @param x coordenada x del punto
	 * @param y coordenada y del punto
	 * @return resultado de la evaluación para x e y
	 */
	public double[] getCoordPixel(double x, double y){
		// setExpPolinomial();
		double eval[] = new double[2];
		for (int i = 0; i < pixelToMapCoefX.length; i++) {
			eval[0] += mapToPixelCoefX[i] * Math.pow(x, exp[i][0]) * Math.pow(y, exp[i][1]);
			eval[1] += mapToPixelCoefY[i] * Math.pow(x, exp[i][0]) * Math.pow(y, exp[i][1]);
		}
		return eval;
	}
	
//	private void setExpPolinomial() {
//		if (exp == null) {
//			int minGPC = (orden + 1) * (orden + 2) / 2;
//			exp = new int[minGPC][2];
//			int k = -1;
//			for (int i = 0; i <= orden; i++) {
//				for (int j = 0; j <= i; j++) {
//					k++;
//					exp[k][0] = i - j;
//					exp[k][1] = j;
//				}
//			}
//		}
//	}
}
	
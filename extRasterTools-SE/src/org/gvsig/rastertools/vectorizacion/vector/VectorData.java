/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.rastertools.vectorizacion.vector;

import java.util.Observable;

import org.cresques.cts.IProjection;
import org.gvsig.raster.vectorization.VectorizationBinding;
/**
 * Modelo de datos correspondiente a la interfaz de vectorización
 * 
 * 15/07/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class VectorData extends Observable {
	public static int       NONE                  = -1;
	public final static int CONTOUR_LINES         = 1;
	public final static int POTRACE_LINES         = 2;
	private int             algorithm             = CONTOUR_LINES;
	private double          distance              = 255;
	private IProjection     projLayer             = null;
	private int             policy                = VectorizationBinding.POLICY_MINORITY;
	private int             despeckle             = 0;
	private double          cornerThreshold       = 1.0;
	private double          optimizationTolerance = 0.2;
	private int             outputQuantization    = 10;
	private boolean         curveOptimization     = true;
	private int             bezierPoints          = 7;

	/**
	 * @return the policy
	 */
	public int getPolicy() {
		return policy;
	}

	/**
	 * @param policy the policy to set
	 */
	public void setPolicy(int policy) {
		this.policy = policy;
		updateObservers();
	}

	/**
	 * @return the despeckle
	 */
	public int getDespeckle() {
		return despeckle;
	}

	/**
	 * @param despeckle the despeckle to set
	 */
	public void setDespeckle(int despeckle) {
		this.despeckle = despeckle;

		if (this.despeckle < 0)
			this.despeckle = 0;

		updateObservers();
	}
	
	/**
	 * @return the bezierPoints
	 */
	public int getBezierPoints() {
		return bezierPoints;
	}

	/**
	 * @param bezierPoints the bezierPoints to set
	 */
	public void setBezierPoints(int bezierPoints) {
		this.bezierPoints = bezierPoints;
		updateObservers();
	}

	/**
	 * @return the cornerThreshold
	 */
	public double getCornerThreshold() {
		return cornerThreshold;
	}

	/**
	 * @param cornerThreshold the cornerThreshold to set
	 */
	public void setCornerThreshold(double cornerThreshold) {
		this.cornerThreshold = cornerThreshold;
		
		if (this.cornerThreshold < -1.00)
			this.cornerThreshold = -1.00;

		if (this.cornerThreshold > 1.34)
			this.cornerThreshold = 1.34;

		updateObservers();
	}

	/**
	 * @return the optimizationTolerance
	 */
	public double getOptimizationTolerance() {
		return optimizationTolerance;
	}

	/**
	 * @param optimizationTolerance the optimizationTolerance to set
	 */
	public void setOptimizationTolerance(double optimizationTolerance) {
		this.optimizationTolerance = optimizationTolerance;
		
		if (this.optimizationTolerance < 0)
			this.optimizationTolerance = 0;

		if (this.optimizationTolerance > 9999)
			this.optimizationTolerance = 9999;
		
		updateObservers();
	}

	/**
	 * @return the outputQuantizqtion
	 */
	public int getOutputQuantizqtion() {
		return outputQuantization;
	}

	/**
	 * @param outputQuantization the outputQuantizqtion to set
	 */
	public void setOutputQuantization(int outputQuantization) {
		this.outputQuantization = outputQuantization;
		
		if (this.outputQuantization < 0)
			this.outputQuantization = 0;

		updateObservers();
	}

	/**
	 * @return the curveOptimization
	 */
	public boolean isCurveOptimization() {
		return curveOptimization;
	}

	/**
	 * @param curveOptimization the curveOptimization to set
	 */
	public void setCurveOptimization(boolean curveOptimization) {
		this.curveOptimization = curveOptimization;
		updateObservers();
	}

	/**
	 * Obtiene la proyeccion que se le asignará a la capa 
	 * vectorial.
	 * @return IProjection
	 */
	public IProjection getProjLayer() {
		return projLayer;
	}

	/**
	 * Asigna la proyeccion que se le asignará a la capa 
	 * vectorial.
	 * @param IProjection
	 */
	public void setProjLayer(IProjection projLayer) {
		this.projLayer = projLayer;
		updateObservers();
	}

	/**
	 * Obtiene el algoritmo de vectorización
	 * @return
	 */
	public int getAlgorithm() {
		return algorithm;
	}
	
	/**
	 * Asigna el algoritmo de vectorización
	 * @param algorithm
	 */
	public void setAlgorithm(int algorithm) {
		this.algorithm = algorithm;
		updateObservers();
	}

	/**
	 * Obtiene el valor de distancia para el algoritmo de vectorización de 
	 * líneas de contorno.
	 * @return
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Asigna el valor de distancia para el algoritmo de vectorización de 
	 * líneas de contorno.
	 * @return
	 */
	public void setDistance(double distance) {
		this.distance = distance;
		updateObservers();
	}
	
	/**
	 * Actualiza datos y llama al update de los observadores
	 */
	public void updateObservers() {
		setChanged();
		notifyObservers();
	}
}
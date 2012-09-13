/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2008 Instituto de Desarrollo Regional and Generalitat Valenciana.
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

package org.gvsig.remotesensing.principalcomponents;

import Jama.Matrix;

/**
 * Representa las stadísticas necesarias para generar un raster transformado por 
 * el método de Análisis de Componentes principales.
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class PCStatistics {
	
	private Matrix 			autoVectorsMatrix 	= null;
	private Matrix 			coVarMatrix 		= null;
	private double			autovalues[]		= null;
	
	/**
	 * Constructor.
	 * 
	 * @param autoVectorsMatrix Matriz de autovectores.
	 * @param autovalues Array de autovalores.
	 * @param coVarMatrix Matriz de Covarianza.
	 */
	public PCStatistics(Matrix autoVectorsMatrix, double[] autovalues, Matrix coVarMatrix) {
		this.autoVectorsMatrix = autoVectorsMatrix;
		this.autovalues = autovalues;
		this.coVarMatrix = coVarMatrix;
	}


	public Matrix getAutoVectorsMatrix() {
		return autoVectorsMatrix;
	}


	public void setAutoVectorsMatrix(Matrix autoVectorsMatrix) {
		this.autoVectorsMatrix = autoVectorsMatrix;
	}


	public Matrix getCoVarMatrix() {
		return coVarMatrix;
	}


	public void setCoVarMatrix(Matrix coVarMatrix) {
		this.coVarMatrix = coVarMatrix;
	}


	public double[] getAutovalues() {
		return autovalues;
	}


	public void setAutovalors(double[] autovalors) {
		this.autovalues = autovalors;
	}
	
}

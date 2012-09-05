package com.iver.utiles;
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/**
 * Some more mathematical functions that 'java.lang.Math' doesn't have:
 * - Logarithm operations: log2, log2Integer, logX, logXInteger
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public final class MathExtension {
	
	/**
	 * 2-base logarithm of a
	 * @param a The value to do the calculations
	 * @return Logarithm in 2 base of 'a'
	 */
	public static double log2(double a) {
		return (Math.log(a) / Math.log(2));
	}
	
	/**
	 * 2-base logarithm of a, but the result doesn't have decimals (approximated to the integer number most near by below)
	 * @param a The value to do the calculations
	 * @return Logarithm in 2 base of 'a', as an integer
	 */
	public static int log2Integer(double a) {
		return (int) Math.floor(log2(a));
	}
	
	/**
	 * X-base logarithm of a
	 * @param a The value to do the calculations
	 * @return Logarithm in X base of 'a'
	 */
	public static double logX(double x_base, double a) {
		return (Math.log(a) / Math.log(x_base));
	}
	
	/**
	 * X-base logarithm of a, but the result doesn't have decimals (approximated to the integer number most near by below)
	 * @param a The value to do the calculations
	 * @return Logarithm in X base of 'a', as an integer
	 */
	public static int logXInteger(double x_base, double a) {
		return (int) Math.floor(logX(x_base, a));
	}
}

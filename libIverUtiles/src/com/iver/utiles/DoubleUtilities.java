package com.iver.utiles;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
/* CVS MESSAGES:
 *
 * $Id: DoubleUtilities.java 8421 2006-10-30 11:59:47Z nacho $
 * $Log$
 * Revision 1.3  2006-10-30 11:59:47  nacho
 * formateado de double
 *
 * Revision 1.2  2006/09/13 08:10:07  jorpiell
 * Se limita el número de decimales por arriba y por abajo
 *
 * Revision 1.1  2006/05/16 13:02:41  jorpiell
 * Se ha añadido la clase DoubleUtiles, donde ha un metodo para limitar el tamaño de los decimales de un double y para quitar los "puntos" de la parte entera
 *
 *
 */
/**
 * This class has some methods to manage "Doubles"
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class DoubleUtilities {
	/**
	 * Formats a double with an specified number of decimals. It 
	 * removes the separator character of the integer part. 
	 * @param value
	 * Separator char of the integer part
	 * @param decimalSeparator
	 * Separator char of the decimal part
	 * @param decimalsNumber
	 * Number of decimals
	 * @return
	 * The formatted double
	 */
	public static double format(double value,
			char decimalSeparator,
			int decimalsNumber){
			
		DecimalFormat dFormat = new DecimalFormat("#");
		DecimalFormatSymbols dFormatSymbols = new DecimalFormatSymbols();
		
		dFormatSymbols.setDecimalSeparator(decimalSeparator);
		dFormat.setMaximumFractionDigits(decimalsNumber);
		dFormat.setMaximumFractionDigits(decimalsNumber);
		dFormat.setDecimalFormatSymbols(dFormatSymbols);
		double d = Double.parseDouble(dFormat.format(value));
		return Double.parseDouble(dFormat.format(value));		
	}

	/**
	 * Formats a double with an specified number of decimals. 
	 * @param num
	 * Value to tail
	 * @param n
	 * Number of decimals
	 * @return
	 * The formatted double
	 */
    public static double format(double num, int n){
    	long m = (long)Math.pow(10, n);
        num *= m;
        long aux = ((long)num);
        num = (double)((double)aux / (double)m);
        return num;
    }

}

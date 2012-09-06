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
package org.gvsig.raster.util;

/**
 * Utilidades para calculos matemáticos
 * 
 * @version 18/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class MathUtils {
	public static double INCHESMTR = 39.73007874D;
	public static double INCHESCM = 0.3973007874D;
	public static double INCHESMM = 0.03973007874D;
	public static double MTRSINCH = 0.0254D;
	public static double MMSINCH = 25.4D;
	public static double CMSINCH = 2.54D;
	
	/**
	 * Recorta el número de decimales a n del número pasado por parámetro
	 * @return Número recortado
	 */
	public static double clipDecimals(double num, int n) {
		long m = (long) Math.pow(10, n);
		long aux = Math.round(num * m);
		return (double) aux / (double) m;
	}
		
	/**
	 * Convierte pixels en milimetros dependiendo del los puntos por pulgada
	 * @param pixels Número de pixels
	 * @param ppp Puntos por pulgada
	 * @return Número de milimetros
	 */
	public static double convertPixelsToMms(double pixels, int ppp){
		return convertPixelsToInches(pixels, ppp) * MMSINCH;
	}
	
	/**
	 * Convierte pixels en centimetros dependiendo del los metros por pixel
	 * @param pixels Número de pixels
	 * @param mtsPixel Metros por pixel
	 * @return Número de centimetros
	 */
	public static double convertPixelsToCms(double pixels, int ppp){
		return convertPixelsToInches(pixels, ppp) * CMSINCH;
	}
	
	/**
	 * Convierte pixels en metros dependiendo del los metros por pixel
	 * @param pixels Número de pixels
	 * @param mtsPixel Metros por pixel
	 * @return Número de metros
	 */
	public static double convertPixelsToMts(double pixels, int ppp){
		return convertPixelsToInches(pixels, ppp) * MTRSINCH;
	}
	
	/**
	 * Convierte pixels en pulgadas dependiendo del los metros por pixel
	 * @param pixels Número de pixels
	 * @param mtsPixel Metros por pixel
	 * @return Número de pulgadas
	 */
	public static double convertPixelsToInches(double pixels, int ppp){
		return (double)((double)pixels / (double)ppp);
	}
	
	/**
	 * Convierte metros en pixels dependiendo de los puntos por pulgada
	 * @param mts Número de metros
	 * @param ppp Puntos por pulgada
	 * @return Número de pixeles
	 */
	public static int convertMtsToPixels(double mts, int ppp){
		return (int)(mts * MathUtils.INCHESMTR * ppp);
	}
	
	/**
	 * Convierte pulgadas en pixels dependiendo de los puntos por pulgada
	 * @param mts Número de pulgadas
	 * @param ppp Puntos por pulgada
	 * @return Número de pixeles
	 */
	public static int convertInchesToPixels(double inches, int ppp){
		return (int)(inches * ppp);
	}
	
	/**
	 * Convierte centimetros en pixels dependiendo de los puntos por pulgada
	 * @param mts Número de centmetros
	 * @param ppp Puntos por pulgada
	 * @return Número de pixeles
	 */
	public static int convertCmsToPixels(double cms, int ppp){
		return (int)(cms * MathUtils.INCHESCM * ppp);
	}
	
	/**
	 * Convierte milimetros en pixels dependiendo de los puntos por pulgada
	 * @param mts Número de milimetros
	 * @param ppp Puntos por pulgada
	 * @return Número de pixeles
	 */
	public static int convertMmsToPixels(double mms, int ppp) {
		return (int) (mms * MathUtils.INCHESMM * ppp);
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
	public static double format(double num, int n) {
		long m = (long) Math.pow(10, n);
		num *= m;
		long aux = ((long) num);
		num = (double) ((double) aux / (double) m);
		return num;
	}

	/**
	 * Convierte un array de Double en un array de tipos básicos double
	 * @param list Lista de objetos Double
	 * @return double[] lista de tipos básicos double
	 */
	public static double[] convertDoubleList(Double[] list) {
		if (list == null)
			return null;
		double[] result = new double[list.length];
		for (int i = 0; i < list.length; i++)
			result[i] = list[i].doubleValue();
		return result;
	}

	/**
	 * Convierte un array de Integer en un array de tipos básicos int
	 * @param list Lista de objetos Integer
	 * @return int[] lista de tipos básicos int
	 */
	public static int[] convertIntList(Integer[] list) {
		if (list == null)
			return null;
		int[] result = new int[list.length];
		for (int i = 0; i < list.length; i++)
			result[i] = list[i].intValue();
		return result;
	}
}
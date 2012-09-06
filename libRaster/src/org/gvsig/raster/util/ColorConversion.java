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
 * Coversión de de colores y valores de pixel aplicando distintos métodos.
 * 
 * 31/10/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ColorConversion {
	
	/**
	 * Conversión de RGB a HSL (Hue, Saturation, Lightness)
	 * @param rojo Banda del rojo del pixel de entrada
	 * @param verde Banda del verde del pixel de entrada
	 * @param azul Banda del azul del pixel de entrada
	 * @return array de 3 valores con los datos HSL correspondiente al pixel de entrada.
	 */
	public double[] RGBtoHSL(int rojo, int verde, int azul) {
		double   scaler, scaleg, scaleb;
		double   sat;
		double   red, green, blue;
		double   low, high, intens;
		double   hue=0.0;
		double[] res1 = new double[3];
	
		scaler = (double) (rojo / 255.0);
		scaleg = (double) (verde / 255.0);
		scaleb = (double) (azul / 255.0);
  	 
		high = scaler;
    
		if (scaleg > high)
			high = scaleg;
		if (scaleb > high)
			high = scaleb;
		low = scaler;
		if (scaleg < low)
			low = scaleg;
		if (scaleb < low)
			low = scaleb;
    
		intens = ((high + low) / 2.0);
		if (high == low){
			sat = 0.0;
			hue = 0.0;
			res1[0] = (double) hue;
			res1[2] = (double) intens;
			res1[1] = (double) sat;
		}else if (high != low){
			if (intens <= 0.5)
				sat = (high-low)/(high+low);
			else
				sat = (high-low)/(2 - high - low);
			red = (high-scaler)/(high-low);
			green = (high-scaleg)/(high-low);
			blue =(high-scaleb)/(high-low);			
			if (scaler == high)
				hue = blue - green;
			else
				if (scaleg == high)
					hue = 2 + red - blue;
				else
					if (scaleb == high)
						hue = 4 + green - red;
			hue *= 60.0;
			if (hue < 0.0)
				hue += 360.0;
		res1[0] = (double) (hue);
		res1[2] = (double) (intens);
		res1[1] = (double) (sat);
		}
		return res1;
	}
	
	/**
	 * Conversión de RGB a HSV (Hue, Saturation, Value). El valor de H es el mismo que en
	 * HSL pero difiere en el calculo de la S y la V.
	 * @param rojo Banda del rojo del pixel de entrada
	 * @param verde Banda del verde del pixel de entrada
	 * @param azul Banda del azul del pixel de entrada
	 * @return array de 3 valores con los datos HSL correspondiente al pixel de entrada.
	 */
	public double[] RGBtoHSV(int rojo, int verde, int azul) {
		double   scaler, scaleg, scaleb;
		double   sat;
		double   red, green, blue;
		double   low, high, value;
		double   hue=0.0;
		double[] res1 = new double[3];
	
		scaler = (double) (rojo / 255.0);
		scaleg = (double) (verde / 255.0);
		scaleb = (double) (azul / 255.0);
  	 
		high = scaler;
    
		if (scaleg > high)
			high = scaleg;
		if (scaleb > high)
			high = scaleb;
		low = scaler;
		if (scaleg < low)
			low = scaleg;
		if (scaleb < low)
			low = scaleb;
    
		value = high;
		if (high == 0) {
			sat = 0.0;
			hue = 0.0;
			res1[0] = (double) hue;
			res1[1] = (double) sat;
			res1[2] = (double) value;
		} else {
			sat = (high-low) / high;
			red = (high-scaler)/(high-low);
			green = (high-scaleg)/(high-low);
			blue =(high-scaleb)/(high-low);			
			if (scaler == high)
				hue = blue - green;
			else
				if (scaleg == high)
					hue = 2 + red - blue;
				else
					if (scaleb == high)
						hue = 4 + green - red;
			hue *= 60.0;
			if (hue < 0.0)
				hue += 360.0;
		res1[0] = (double)(hue);
		res1[1] = (double) (sat);
		res1[2] = (double) (value);
		}
		return res1;
	}
	
	/**
	 * Conversión de HSL a RGB
	 * @param h Banda de matiz del pixel de entrada
	 * @param s Banda de saturación del pixel de entrada
	 * @param azul Banda de brillo del pixel de entrada
	 * @return array de 3 valores con los datos RGB correspondiente al pixel de entrada.
	 */
	public int[] HSLtoRGB(int h, int s, int i) {
		/*return HSLtoRGB((360.0 * h / 255.0), 
						(s / 255.0), 
						(i / 255.0));*/
		double   red;                   /* the red band output                       */
		double   red255;                /* the red band output                       */
		double   green;                 /* the green band output                     */
		double   green255;              /* the green band output                     */
		double   blue;                  /* the blue band output                      */
		double   blue255;               /* the blue band output                      */
		double   m1;                    /* value used for determining RGB            */
		double   m2;                    /* value used for determining RGB            */
		double   scalei;                /* intensity value                           */
		double   scales;                /* saturation value                          */
		double   hue;                   /* hue                                       */
		double   savehue;               /* save the hue for future processing        */
		int[]   res = new int[3];
	 
		if(h == s && h == 0){
			res[0] = res[1] = res[2] = i;
			return res;
		}
			
		red = green = blue = 0.0;
		scalei = (double) (i / 255.0);
		scales = (double) (s / 255.0);
		m2 = 0.0;
	
		if (scalei <= 0.50)
			m2 = scalei * (1.0 + scales);
		else if (scalei > 0.50)
			m2 = scalei + scales - (scalei * scales);
		m1 = 2.0 * scalei - m2;
	 
		hue = (double) 360.0 * h / 255.0;
	
		if (scales == 0.0){
			if (hue == -1.0){
				red = scalei;
				green = scalei;
				blue = scalei;
			}
		}else{
			savehue = hue + 120.0;
			if (savehue > 360.0)
				savehue -= 360.0;
			if (savehue < 0.0)
				savehue += 360.0;
			if (savehue < 60.0)
				red = m1 + (m2-m1) * savehue/60.0;
			else if (savehue < 180.0)
				red = m2;
	        else if (savehue < 240.0)
	          red = m1 + (m2-m1) * (240.0-savehue)/60.0;
	        else
	          red = m1;
	 
			savehue = hue;
			if (savehue > 360.0)
				savehue -= 360.0;
			if (savehue < 0.0)
				savehue += 360.0;
			if (savehue < 60.0)
				green = m1 + (m2-m1) * savehue/60.0;
			else if (savehue < 180.0)
				green = m2;
			else if (savehue < 240.0)
				green = m1 + (m2-m1) * (240.0-savehue)/60.0;
			else
				green = m1; 
			savehue = hue - 120.0;
	   
			if (savehue > 360.0)
				savehue -= 360.0;
			if (savehue < 0.0)
				savehue += 360.0;
			if (savehue < 60.0)
				blue = m1 + (m2-m1) * savehue/60.0;
			else if (savehue < 180.0)
				blue = m2;
			else if (savehue < 240.0)
				blue = m1 + (m2-m1) * (240.0-savehue)/60.0;
			else
				blue = m1;
		}
	 
		red255 = red * 255.0;
		green255 = green * 255.0;
		blue255 = blue * 255.0;
		if (red255 > 255.0)
			red = 255.0;
		else
			red = red255;
		if (green255 > 255.0)
			green = 255.0;
		else
			green = green255;
		if (blue255 > 255.0)
			blue = 255.0;
		else
			blue = blue255;
	 
		if (red   > 254.5) red   = 254.5;
		if (red   <   0.0) red   =   0.0;
		if (green > 254.5) green = 254.5;
		if (green <   0.0) green =   0.0;
		if (blue  > 254.5) blue  = 254.5;
		if (blue  <   0.0) blue  =   0.0;
	 
		res[0] = (int) (red + 0.5);
		res[1] = (int) (green +0.5);
		res[2] = (int) (blue + 0.5);
		return res;
	}
	

	/**
	 * Conversión de HSL a RGB
	 * @param h Banda de matiz del pixel de entrada. Valor entre 0 y 1.
	 * @param s Banda de saturación del pixel de entrada. Valor entre 0 y 1.
	 * @param i Banda de brillo del pixel de entrada. Valor entre 0 y 1.
	 * @return array de 3 valores con los datos RGB correspondiente al pixel de entrada.
	 */
	public int[] HSLtoRGB(double h, double s, double i) {
		double   red;                   /* the red band output                       */
		double   red255;                /* the red band output                       */
		double   green;                 /* the green band output                     */
		double   green255;              /* the green band output                     */
		double   blue;                  /* the blue band output                      */
		double   blue255;               /* the blue band output                      */
		double   m1;                    /* value used for determining RGB            */
		double   m2;                    /* value used for determining RGB            */
		double   scalei;                /* intensity value                           */
		double   scales;                /* saturation value                          */
		double   hue;                   /* hue                                       */
		double   savehue;               /* save the hue for future processing        */
		int[]   res = new int[3];
	 
		if(h == s && h == 0){
			res[0] = res[1] = res[2] = (int)(i * 255);
			return res;
		}
			
		red = green = blue = 0.0;
		scalei = i;
		scales = s;
		m2 = 0.0;
	
		if (scalei <= 0.50)
			m2 = scalei * (1.0 + scales);
		else if (scalei > 0.50)
			m2 = scalei + scales - (scalei * scales);
		m1 = 2.0 * scalei - m2;
	 
		hue = h;
	
		if (scales == 0.0){
			if (hue == -1.0){
				red = scalei;
				green = scalei;
				blue = scalei;
			}
		}else{
			savehue = hue + 120.0;
			if (savehue > 360.0)
				savehue -= 360.0;
			if (savehue < 0.0)
				savehue += 360.0;
			if (savehue < 60.0)
				red = m1 + (m2-m1) * savehue/60.0;
			else if (savehue < 180.0)
				red = m2;
	        else if (savehue < 240.0)
	          red = m1 + (m2-m1) * (240.0-savehue)/60.0;
	        else
	          red = m1;
	 
			savehue = hue;
			if (savehue > 360.0)
				savehue -= 360.0;
			if (savehue < 0.0)
				savehue += 360.0;
			if (savehue < 60.0)
				green = m1 + (m2-m1) * savehue/60.0;
			else if (savehue < 180.0)
				green = m2;
			else if (savehue < 240.0)
				green = m1 + (m2-m1) * (240.0-savehue)/60.0;
			else
				green = m1; 
			savehue = hue - 120.0;
	   
			if (savehue > 360.0)
				savehue -= 360.0;
			if (savehue < 0.0)
				savehue += 360.0;
			if (savehue < 60.0)
				blue = m1 + (m2-m1) * savehue/60.0;
			else if (savehue < 180.0)
				blue = m2;
			else if (savehue < 240.0)
				blue = m1 + (m2-m1) * (240.0-savehue)/60.0;
			else
				blue = m1;
		}
	 
		red255 = red * 255.0;
		green255 = green * 255.0;
		blue255 = blue * 255.0;
		if (red255 > 255.0)
			red = 255.0;
		else
			red = red255;
		if (green255 > 255.0)
			green = 255.0;
		else
			green = green255;
		if (blue255 > 255.0)
			blue = 255.0;
		else
			blue = blue255;
	 
		if (red   > 254.5) red   = 254.5;
		if (red   <   0.0) red   =   0.0;
		if (green > 254.5) green = 254.5;
		if (green <   0.0) green =   0.0;
		if (blue  > 254.5) blue  = 254.5;
		if (blue  <   0.0) blue  =   0.0;
	 
		res[0] = (int) (red + 0.5);
		res[1] = (int) (green +0.5);
		res[2] = (int) (blue + 0.5);
		return res;
	}
	
	/**
	 * Conversión de RGB a CMYK (Cyan, Magenta, Yellow, Key). 
	 * @param red Banda del rojo del pixel de entrada
	 * @param green Banda del verde del pixel de entrada
	 * @param blue Banda del azul del pixel de entrada
	 * @param kScale valor entre 0 y 1
	 * @return array de 4 valores con los datos CMYK correspondiente al pixel de entrada.
	 */
	public double[] RGBtoCMYK(int red, int green, int blue, double kScale) {
		double[] cmyk = new double[4];
		double c = 1D - (red / 255D);
		double m = 1D - (green / 255D);
		double y = 1D - (blue / 255D);
		cmyk[3] = 1D;
		
		if(c < cmyk[3])
			cmyk[3] = c;
		if(m < cmyk[3])
			cmyk[3] = m;
		if(y < cmyk[3])
			cmyk[3] = y;
		
		cmyk[3] *= kScale;
		
		if(cmyk[3] < 1.0) {
			cmyk[0] = ((double)(c - cmyk[3]) / (double)(1D - cmyk[3]));
			cmyk[1] = ((double)(m - cmyk[3]) / (double)(1D - cmyk[3]));
			cmyk[2] = ((double)(y - cmyk[3]) / (double)(1D - cmyk[3]));
		} 
		return cmyk;
	}
	
	/**
	 * Conversión de CMYK (Cyan, Magenta, Yellow, Key) a RGB (red, green, blue). 
	 * @param red Banda del rojo del pixel de entrada
	 * @param green Banda del verde del pixel de entrada
	 * @param blue Banda del azul del pixel de entrada
	 * @param kScale valor entre 0 y 1
	 * @return array de 4 valores con los datos CMYK correspondiente al pixel de entrada.
	 */
	public double[] CMYKtoRGB(double cyan, double magenta, double yellow, double key) {
		double[] rgb = new double[3];
		double c = 1, m = 1, y = 1;
		
		if(key < 1D) {
			c = cyan * (1D - key) + key;
			m = magenta * (1D - key) + key;
			y = yellow * (1D - key) + key;
		}
		
		rgb[0] = 1D - c;
		rgb[1] = 1D - m;
		rgb[2] = 1D - y;
		return rgb;
	}
	
	/**
	 * Obtiene la luminosidad del RGB pasado por parámetro
	 * @param red Banda del rojo del pixel de entrada
	 * @param green Banda del verde del pixel de entrada
	 * @param blue Banda del azul del pixel de entrada
	 * @return double que representa la luminosidad. Para tenerlo en un rango de 255 hay que aplicar (lum * 255. + 0.5);
	 */
	public double getLuminosity(int red, int green, int blue) {
		double   scaler, scaleg, scaleb;
		double   low, high;
	
		scaler = (double) (red / 255.0);
		scaleg = (double) (green / 255.0);
		scaleb = (double) (blue / 255.0);
  	 
		high = scaler;
    
		if (scaleg > high)
			high = scaleg;
		if (scaleb > high)
			high = scaleb;
		low = scaler;
		if (scaleg < low)
			low = scaleg;
		if (scaleb < low)
			low = scaleb;
    
		return ((high + low) / 2.0);
	}
}

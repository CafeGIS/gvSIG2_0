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
package org.gvsig.raster.grid.filter.enhancement;

import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetListStatistics;
import org.gvsig.raster.hierarchy.IStatistics;
/**
 * Parámetros necesarios para el filtro de realce por tramos. Contiene
 * información de cada tramo. Valores máximos y mínimos reales y correspondencia
 * a RGB de cada tramo.
 * 
 * También contiene los valores calculados en el preproceso de escala y
 * desplazamiento para cada banda.
 * 
 * @version 27/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class LinearStretchParams {
	
	public class Stretch {
		/**
		 * Valores de los datos de entrada correspondientes al mínimo y al máximo de 
		 * cada tramo. Estos tendrán un rango entre el mínimo y el máximo en cada banda
		 * de la imagen. 
		 */
		public double[]        stretchIn           = null;
		/**
		 * Valores de los datos de salida correspondientes al mínimo y al máximo de 
		 * cada tramo. Estos tendrán un rango entre 0 y 255.
		 */
		public int[]           stretchOut          = null;
		/**
		 * Porcentajes de recorte de colas (Valores de entrada)
		 */
		public double          tailTrimMin         = 0;
		public double          tailTrimMax         = 0;
		/**
		 * Valores de recorte de colas (Valores de salida calculados por el filtro TailTrim)
		 */
		public double          tailTrimValueMin    = 0;
		public double          tailTrimValueMax    = 0;
		/**
		 * Valores de escala a calcular por el filtro lineal por tramos.
		 * Cada elemento del array es un tramo.
		 */
		public double[]        scale               = null;
		/**
		 * Valores de desplazamiento a calcular por el filtro lineal por tramos.
		 * Cada elemento del array es un tramo.
		 */
		public double[]        offset              = null;
		/**
		 * Valores máximos y mínimos
		 */
		public double          maxValue            = 0;
		public double          minValue            = 0;
		
		/**
		 * Funcion grafica que se ha usado para generar el LinearStretchParams
		 * 0 - Lineal
		 * 1 - Exponencial / Logaritmica
		 * 2 - Raiz cuadrada / Cuadrada
		 * 3 - Level slice
		 */
		public int functionType = 0;

		/**
		 * Valor de la gráfica para la función. No se usa en una gráfica lineal.
		 */
		public double valueFunction = 0.0;
		
		/**
		 * Aplica el recorte de colas sobre los extremos de los máximos y mínimos de entrada.
		 * La aplicación de esta función considera que ya se han calculado los valores con 
		 * loadTailTrimValues.
		 */
		public void applyTrimToStretchs() {
			if(stretchIn != null && stretchIn.length >= 2) {
				minValue = stretchIn[0] = tailTrimValueMin;
				maxValue = stretchIn[stretchIn.length - 1] = tailTrimValueMax;
			}
		}
		
		/**
		 * Aplica el eliminado de extremos. Para ello utiliza el segundo máximo y mínimo de entrada.
		 */
		/*public void applyRemoveEndsToStretchs(double secondMin, double secondMax) {
			if(stretchIn != null && stretchIn.length >= 2) {
				minValue = stretchIn[0] = secondMin;
				maxValue = stretchIn[stretchIn.length - 1] = secondMax;
			}
		}*/
		
		/**
		 * Consulta si tiene algún valor el recorte de colas
		 * @return true si tiene valor el recorte de colas y false si no lo tiene
		 */
		public boolean hasTailTrim() {
			return (tailTrimMin != 0 || tailTrimMax != 0);
		}
		
		/**
		 * Calcula la escala y el desplazamiento teniendo en cuenta que
		 * ya tenga todos los valores de entrada asignados.
		 *
		 */
		public void calcLinearScaleAndOffset() {
			if(stretchIn != null && stretchOut != null) {
				//simplifyStretch();
				scale = new double[stretchIn.length - 1];
				offset = new double[stretchIn.length - 1];
				for (int i = 0; i < scale.length; i++) {
					double rgbRange = (stretchOut[i + 1] - stretchOut[i]);
					if((stretchIn[i + 1] - stretchIn[i]) == 0)
						scale[i] = 0;
					else
						scale[i] = rgbRange / (stretchIn[i + 1] - stretchIn[i]);
					offset[i] = stretchOut[i];
				}
			}
		}
		
		/**
		 * Elimina puntos redundantes en stretchIn y stretchOut
		 */
//		private void simplifyStretch() {
//			boolean simplified = false;
//			for (int i = 0; i < (stretchIn.length - 1); i++) {
//				if(stretchIn[i] == stretchIn[i + 1]) {
//					double[] auxIn = new double[stretchIn.length -1];
//					int[] auxOut = new int[stretchIn.length -1];
//					for (int j = 0; j < auxIn.length; j++) {
//						if(j < i) {
//							auxIn[j] = stretchIn[j];
//							auxOut[j] = stretchOut[j];
//						} else {
//							auxIn[j] = stretchIn[j + 1];
//							auxOut[j] = stretchOut[j + 1];
//						}
//							
//					}
//					stretchIn = auxIn;
//					stretchOut = auxOut;
//					simplified = true;
//				}
//			}
//			if(simplified)
//				simplifyStretch();
//		}
		
		/**
		 * Asigna el máximo y el mínimo
		 * @param stats
		 * @param type
		 * @param band
		 */
		public void setMaxMin(DatasetListStatistics stats, int band, boolean rgb) {
			try {
				if (rgb) {
					if (stats.getMinRGB() != null)
						minValue = stats.getMinRGB()[band];
					if (stats.getMaxRGB() != null)
						maxValue = stats.getMaxRGB()[band];
				} else {
					if (stats.getMin() != null) {
						minValue = stats.getMin()[band];
					}
					if (stats.getMax() != null) {
						maxValue = stats.getMax()[band];
					}
				}
				if (stretchIn == null) {
					stretchIn = new double[] { minValue, maxValue };
				} else {
					stretchIn[0] = minValue;
					stretchIn[stretchIn.length - 1] = maxValue;
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				// No se asigna el máximo o mínimo
			}
		}
		
		/**
		 * Aplica el eliminado de extremos. Para ello utiliza el segundo máximo y mínimo de entrada.
		 */
		public void applyRemoveEndsToStretchs(DatasetListStatistics stats, boolean rgb, int band) {
			if(stretchIn == null)
				return;
			try {
				if(rgb) {
					if(stats.getSecondMinRGB() != null)
						stretchIn[0] = minValue = stats.getSecondMinRGB()[band];
					if(stats.getSecondMaxRGB() != null)
						stretchIn[stretchIn.length - 1] = maxValue = stats.getSecondMaxRGB()[band];
				} else {
					if(stats.getSecondMin() != null)
						stretchIn[0] = minValue = stats.getSecondMin()[band];
					if(stats.getMax() != null)
						stretchIn[stretchIn.length - 1] = maxValue = stats.getSecondMax()[band];
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				//No se asigna el máximo o mínimo 
			}
		}
	}

	public Stretch             red               = new Stretch();
	public Stretch             green             = new Stretch();
	public Stretch             blue              = new Stretch();
	public boolean             rgb               = false;
			
	/**
	 * Consulta si tiene algún valor el recorte de colas
	 * @return true si tiene valor el recorte de colas y false si no lo tiene
	 */
	public boolean hasTailTrim() {
		return (red.hasTailTrim() ||
				green.hasTailTrim() ||
				blue.hasTailTrim());
	}
	
	/**
	 * Obtiene la lista de valores de recorte. Este formato es que el que necesita el
	 * filtro TailTrim para calcular el valor del porcentaje recorte.
	 * @return double
	 */
	public double[] getTailTrimList() {
		return new double[]{
				red.tailTrimMin, green.tailTrimMin, blue.tailTrimMin, 
				red.tailTrimMax, green.tailTrimMax,	blue.tailTrimMax};
	}
	
	/**
	 * Carga los valores de recorte calculados por el filtro TailTrim que ya debería haber
	 * sido aplicado. Estos valores estarán salvados en DatasetListStatistics.
	 * @param stats
	 */
	public void loadTailTrimValues(DatasetListStatistics stats) {
		double[][] result;
		result = (double[][]) stats.getTailTrimValue(red.tailTrimMin);
		if (result != null)
			red.tailTrimValueMin = result[0][0];
		result = (double[][]) stats.getTailTrimValue(red.tailTrimMax);
		if (result != null)
			red.tailTrimValueMax = result[0][1];
		result = (double[][]) stats.getTailTrimValue(green.tailTrimMin);
		if (result != null && result.length >= 2)
			green.tailTrimValueMin = result[1][0];
		result = (double[][]) stats.getTailTrimValue(green.tailTrimMax);
		if (result != null && result.length >= 2)
			green.tailTrimValueMax = result[1][1];
		result = (double[][]) stats.getTailTrimValue(blue.tailTrimMin);
		if (result != null && result.length >= 3)
			blue.tailTrimValueMin = result[2][0];
		result = (double[][]) stats.getTailTrimValue(blue.tailTrimMax);
		if (result != null && result.length >= 3)
			blue.tailTrimValueMax = result[2][1];
	}
	
	/**
	 * Aplica el recorte de colas sobre los extremos de los máximos y mínimos de entrada.
	 * La aplicación de esta función considera que ya se han calculado los valores con 
	 * loadTailTrimValues.
	 */
	public void applyTrimToStretchs() {
		red.applyTrimToStretchs();
		green.applyTrimToStretchs();
		blue.applyTrimToStretchs();
	}
	
	/**
	 * Calcula la escala y el desplazamiento teniendo en cuenta que
	 * ya tenga todos los valores de entrada asignados.
	 */
	public void calcLinearScaleAndOffset() {
		red.calcLinearScaleAndOffset();
		green.calcLinearScaleAndOffset();
		blue.calcLinearScaleAndOffset();
	}
	
	/**
	 * Asigna el máximo y el mínimo
	 * @param stats
	 * @param type
	 * @param band
	 */
	public void setMaxMin(DatasetListStatistics stats, int[] renderBands) {
		if (renderBands[0] > -1)
			red.setMaxMin(stats, renderBands[0], rgb);
		if (renderBands[1] > -1)
			green.setMaxMin(stats, renderBands[1], rgb);
		if (renderBands[2] > -1)
			blue.setMaxMin(stats, renderBands[2], rgb);
	}
	
	/**
	 * Aplica el eliminado de extremos. Para ello utiliza el segundo máximo y mínimo de entrada.
	 */
	public void applyRemoveEndsToStretchs(DatasetListStatistics stats, int[] renderBands) {
		if (renderBands[0] > -1)
			red.applyRemoveEndsToStretchs(stats, rgb, renderBands[0]);
		if (renderBands[1] > -1)
			green.applyRemoveEndsToStretchs(stats, rgb, renderBands[1]);
		if (renderBands[2] > -1)
			blue.applyRemoveEndsToStretchs(stats, rgb, renderBands[2]);
	}
	
	/**
	 * Obtiene un objeto LinearStretchParams para una aplicación de realce lineal estandar, es decir, el rango
	 * de datos de salida es 0-255 y con solo un tramo para los datos. 
	 * @param nBands Número de bandas
	 * @param tailTrim Recorte de colas
	 * @param stats Estadísticas
	 * @return LinearStretchParams
	 * @throws FileNotOpenException
	 * @throws RasterDriverException
	 */
	public static LinearStretchParams createStandardParam(int[] renderBands, double tailTrim, IStatistics stats, boolean rgb) throws FileNotOpenException, RasterDriverException {
		LinearStretchParams leParams = new LinearStretchParams();
		leParams.rgb = rgb;
		try {
			stats.calcFullStatistics();
		} catch (InterruptedException e) {
			return null;
		}
		if (renderBands[0] >= 0) {
			if (rgb)
				leParams.red.stretchIn = new double[] { stats.getMinRGB()[0], stats.getMaxRGB()[0] };
			else
				leParams.red.stretchIn = new double[] { stats.getMin()[renderBands[0]], stats.getMax()[renderBands[0]] };
		}
		leParams.red.stretchOut = new int[] { 0, 255 };
		leParams.red.tailTrimMin = tailTrim;
		leParams.red.tailTrimMax = tailTrim;
		if (renderBands[1] >= 0) {
			if (rgb)
				leParams.green.stretchIn = new double[] { stats.getMinRGB()[0], stats.getMaxRGB()[0] };
			else
				leParams.green.stretchIn = new double[] { stats.getMin()[renderBands[1]], stats.getMax()[renderBands[1]] };
		}
		leParams.green.stretchOut = new int[] { 0, 255 };
		leParams.green.tailTrimMin = tailTrim;
		leParams.green.tailTrimMax = tailTrim;
		if (renderBands[2] >= 0) {
			if (rgb)
				leParams.blue.stretchIn = new double[] { stats.getMinRGB()[0], stats.getMaxRGB()[0] };
			else
				leParams.blue.stretchIn = new double[] { stats.getMin()[renderBands[2]], stats.getMax()[renderBands[2]] };
		}
		leParams.blue.stretchOut = new int[] { 0, 255 };
		leParams.blue.tailTrimMin = tailTrim;
		leParams.blue.tailTrimMax = tailTrim;
		return leParams;
	}
}
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
package org.gvsig.raster.datastruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.IBuffer;
/**
 * Representa un histograma.
 * 
 * @version 27/03/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class Histogram {
	/**
	 * Variable que representa el histograma. La primera dimensión es el número de 
	 * bandas y la segunda un array de clases. En el caso básico de un RGB una clase tendrá el
	 * valor mínimo igual al máximo.
	 */
	private HistogramClass[][] histogram   = null;

	private long[][]           table       = null;
	private double[]           min         = null;
	private double[]           max         = null;
	private double             noDataValue = RasterLibrary.defaultNoDataValue;
	private int                dataType    = IBuffer.TYPE_UNDEFINED;
	
	private int                nClasses    = 0;

	/**
	 * Constructor
	 * @param nBands Número de bandas
	 * @param nClasses Número de clases en que hacemos la división
	 * @param min Valor mínimo del histograma
	 * @param max Valor máximo del histograma
	 */
	public Histogram(int nBands, int nClasses, double[] min, double[] max) {
		if ((nBands != min.length) || (nBands != max.length)) {
			System.out.println("No tiene las mismas bandas");
		}
		constructorHistogramImpl(min, max, nClasses);
	}
	
	private void constructorHistogramImpl(double[] min, double[] max, int nClasses) {
		table = new long[min.length][nClasses];
		for (int i = 0; i < table.length; i++)
			for (int j = 0; j < table[0].length; j++)
				table[i][j] = 0;
		this.min = (double[]) min.clone();
		this.max = (double[]) max.clone();

		this.nClasses = getNumValues();
	}

	/**
	 * Constructor
	 * @param nBands Número de bandas
	 * @param nClasses Número de clases en que hacemos la división
	 * @param min Valor mínimo del histograma
	 * @param max Valor máximo del histograma
	 */
	public Histogram(int nBands, double[] min, double[] max, int dataType) {
		int numberClasses;
		this.dataType = dataType;
		this.min = (double[]) min.clone();
		this.max = (double[]) max.clone();
		if (dataType == IBuffer.TYPE_BYTE) {
			numberClasses = RasterLibrary.defaultNumberOfColors;
			boolean unsigned = false;
			for (int i = 0; i < this.min.length; i++)
				if (this.min[i] < 0)
					unsigned = true;
			for (int i = 0; i < this.min.length; i++) {
				if (unsigned) {
					this.min[i] = -128;
					this.max[i] = 127;
				} else {
					this.min[i] = 0;
					this.max[i] = 255;
				}
			}
		} else {
			numberClasses = RasterLibrary.defaultNumberOfClasses;
		}
		
		constructorHistogramImpl(this.min, this.max, numberClasses);
	}
	
	/**
	 * Realiza la unión entre el histograma actual y el pasado 
	 * por parámetro.
	 * @param hist
	 * @deprecated Con los nuevos maximos y minimos por banda, habria que estudiar
	 * la viabilidad de este metodo y si se comporta correctamente
	 */
	public boolean union(Histogram hist) {
		long[][] auxTable = hist.getTable();

		if (auxTable.length != table.length)
			return false;

		if (auxTable.length == 0)
			return true;

		if (auxTable[0].length != table[0].length)
			return false;

		for (int i = 0; i < table.length; i++)
			for (int j = 0; j < table[i].length; j++)
				table[i][j] += auxTable[i][j];

		return true;
	}

	/**
	 * Asigna la tabla
	 * @param t
	 */
	public void setTable(long[][] t) {
		table = new long[t.length][t[0].length];
		for (int i = 0; i < table.length; i++)
			for (int j = 0; j < table[0].length; j++)
				table[i][j] = t[i][j];
		
		this.nClasses = getNumValues();
	}
	
	/**
	 * Devuelve el minimo valor del histograma
	 * @return
	 */
	public double getMin(int band) {
		return min[band];
	}

	/**
	 * Devuelve el maximo valor del histograma
	 * @return
	 */
	public double getMax(int band) {
		return max[band];
	}

	/**
	 * Devuelve los minimos valores del histograma
	 * @return
	 */
	public double[] getMinims() {
		return min;
	}

	/**
	 * Devuelve los maximos valores del histograma
	 * @return
	 */
	public double[] getMaxims() {
		return max;
	}

	/**
	 * Devuelve si un histograma esta en un rango RGB aceptable
	 * @return
	 */
	public boolean isInRangeRGB() {
		if (dataType == IBuffer.TYPE_BYTE)
			return true;
		return true;
	}

	/**
	 * Obtiene el histograma sin modificar
	 * @return array bidimensional donde el primer elemento es el valor del pixel
	 * o rango y el segundo el número de elementos que aparecen.
	 */
	public HistogramClass[][] getHistogram() {
		if (nClasses == 0)
			return null;
		
		histogram = new HistogramClass[table.length][nClasses];
				
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				HistogramClass hc = new HistogramClass(	min[i] + ((j * (max[i] - min[i])) / (nClasses - 1)), 
														min[i] + (((j + 1) * (max[i] - min[i])) / (nClasses - 1)));
				hc.setValue(table[i][j]);
				histogram[i][j] = hc;
			}
		}
		return histogram;
	}
	
	/**
	 * Obtiene el número de bandas del histograma 
	 * @return entero que representa el número de bandas 
	 */
	public int getNumBands() {
		if (table != null)
			return table.length;
		return 0;
	}
	
	/**
	 * Obtiene el tipo de datos del histograma original
	 * @return
	 */
	public int getDataType() {
		return dataType;
	}
	
	/**
	 * Obtiene la longitud (número de valores) de una banda determinada
	 * @param band Banda o obtener la longitud
	 * @return entero con la longitud de la banda
	 * rangos de valores y DataclassList.
	 */
	public int getBandLenght(int band) {
		if (table != null)
			return table[band].length;
		return 0;
	}
	
	/**
	 * Obtiene el número de valores o clases del histograma
	 * @return entero que representa el número de valores o clases del histograma
	 */
	public int getNumValues() {
		if (table == null)
			return 0;

		if (table.length == 0)
			return 0;
		
		return table[0].length;
	}
	
	/**
	 * Asigna un histograma
	 * @param hist histograma asignado
	 */
	public void setHistogram(HistogramClass[][] hist){
		histogram = hist;
	}
		
	/**
	 * Asigna un valor para una posición del histograma
	 * @param band Valor del pixel o clase a asignar
	 * @param px Valor del pixel
	 * @param value Valor a asignar
	 */
	public void setHistogramValue(int band, double px, long value) {
		int pos = (int) (((nClasses - 1) * (px - min[band])) / (max[band] - min[band]));
		if (pos < 0)
			pos = 0;
		if (pos >= nClasses)
			pos = nClasses - 1;
		table[band][pos] = value;
	}
	
	/**
	 * Asigna un valor para una posición del histograma segun la posicion en las
	 * clases
	 * @param band Valor del pixel o clase a asignar
	 * @param pos Posicion dentro de la clase. Ejemplo 0..63
	 * @param value Valor a asignar
	 */
	public void setHistogramValueByPos(int band, int pos, long value) {
		if (pos < 0)
			pos = 0;
		if (pos >= nClasses)
			pos = nClasses - 1;
		table[band][pos] = value;
	}

	/**
	 * Obtiene un valor del histograma
	 * @param band Número de banda del valor a recuperar
	 * @param px Pixel o valor de la clase del valor a recuperar
	 * @return valor
	 */
	public double getHistogramValue(int band, double px) {
		if (histogram == null)
			getHistogram();

		for (int i = 0; i < histogram[band].length; i++)
			if (((HistogramClass) histogram[band][i]).isIn(px))
				return ((HistogramClass) histogram[band][i]).getValue();

		return 0;
	}
	
	/**
	 * Obtiene un valor del histograma segun la posicion dentro de las clases
	 * @param band Número de banda del valor a recuperar
	 * @param px Pixel o valor de la clase del valor a recuperar
	 * @return valor
	 */
	public double getHistogramValueByPos(int band, int pos) {
		if (pos < 0)
			pos = 0;

		if (pos >= nClasses)
			pos = nClasses - 1;

		return table[band][pos];
	}
	
	/**
	 * Incrementa un valor de una posición del histograma
	 * @param band Número de banda
	 * @param px Pixel o valor de la clase
	 */
	public void incrementPxValue(int band, double px) {
		if (Double.isNaN(px))
			return;

		if (px == noDataValue)
			return;

		int pos = (int) (((nClasses - 1) * (px - min[band])) / (max[band] - min[band]));

		if (pos < 0)
			pos = 0;

		if (pos >= nClasses)
			pos = nClasses - 1;

		table[band][pos]++;
	}

	/**
	 * Calculo de estadísticas a partir de un histograma. El resultado de la función es un array
	 * bidimensional donde el primer índice inndica la estadistica y el segundo el número de banda.
	 * 
	 * <UL>
	 * <LI>mínimo</LI>
	 * <LI>máximo</LI>
	 * <LI>media</LI>
	 * <LI>mediana</LI>
	 * <LI>Número de pixels</LI>
	 * </UL>
	 * @param histogram
	 * @param bandas solicitadas. Cada elemento del vector representa una banda. Si está a true se calcula la 
	 * estadistica para esa banda y si está a false no se calculará.
	 * @return
	 */
	public double[][] getBasicStats(boolean[] bands) {
		if (histogram == null)
			return null;
		return getBasicStats(0, histogram[0].length - 1, bands);
	}

	/**
	 * Calculo de estadísticas a partir de un histograma. El resultado de la función es un array
	 * bidimensional donde el primer índice inndica la estadistica y el segundo el número de banda.
	 * 
	 * <UL>
	 * <LI>mínimo</LI>
	 * <LI>máximo</LI>
	 * <LI>media</LI>
	 * <LI>mediana</LI>
	 * <LI>Número de pixels</LI>
	 * </UL>
	 * @param histogram
	 * @param beginPos Posición de inicio del histograma para contabilizar estadisticas
	 * @param endPos Posición de fin del histograma para contabilizar estadisticas
	 * @param bandas solicitadas. Cada elemento del vector representa una banda. Si está a true se calcula la 
	 * estadistica para esa banda y si está a false no se calculará.
	 * @return
	 */
	public double[][] getBasicStats(double beginPos2, double endPos2, boolean[] bands) {
		if (histogram == null)
			getHistogram();
		
		int beginPos = (int) ((beginPos2 * (nClasses - 1)) / 100.0);
		int endPos = (int) ((endPos2 * (nClasses - 1)) / 100.0);

		// Contamos el número de bandas para las cuales se calcula la estadística
		int bandCount = 0;
		for (int iBand = 0; iBand < bands.length; iBand++)
			if (bands[iBand])
				bandCount++;

		double[][] res = new double[5][];

		double[] min = new double[bandCount];// Mínimo
		double[] max = new double[bandCount];// Máximo
		for (int iBand = 0; iBand < bandCount; iBand++) {
			max[iBand] = Double.NEGATIVE_INFINITY;
			min[iBand] = Double.POSITIVE_INFINITY;
		}
		double[] average = new double[bandCount]; // Valor de pixel medio (Media)
		double[] middle = new double[bandCount]; // Mediana
		double[] nPixelsBand = new double[bandCount];// Número de pixels por banda

		int showBandCounter = 0; // Contador de bandas de las que hay calcular la
															// estadistica
		for (int iBand = 0; iBand < histogram.length; iBand++) {
			if (bands[iBand]) {
				for (int i = beginPos; i <= endPos; i++) {
					// Calculo del mínimo
					if (histogram[iBand][i].getValue() != 0 && histogram[iBand][i].getMin() < min[showBandCounter])
						min[showBandCounter] = histogram[iBand][i].getMin();

					// Calculo del máximo
					if (histogram[iBand][i].getValue() != 0 && histogram[iBand][i].getMax() > max[showBandCounter])
						max[showBandCounter] = histogram[iBand][i].getMin();

					// Calculo del número de pixeles
					nPixelsBand[showBandCounter] += histogram[iBand][i].getValue();

					average[showBandCounter] += histogram[iBand][i].getValue() * ((histogram[iBand][i].getMax() + histogram[iBand][i].getMin())/2);
				}
				// Calculo de la media
				try {
					average[showBandCounter] /= nPixelsBand[showBandCounter];
				} catch (ArithmeticException exc) {
					average[showBandCounter] = 0;
				}

				// Calculo de mediana
				double stopPos = nPixelsBand[showBandCounter] / 2;
				int aux = 0;
				int i = beginPos;
				for (i = beginPos; aux <= stopPos; i++) {
					if (i >= histogram[iBand].length)
						break;
					try {
						aux += histogram[iBand][i].getValue();
					} catch (ArrayIndexOutOfBoundsException e) {
						LoggerFactory.getLogger(getClass().getName()).debug("Acceso a un valor de histogram (Banda: " + iBand + " Posición: " + i + ") fuera de límites. (NBandas: " + histogram.length + " Valores:" + histogram[iBand].length + ")", e);
					}
				}
				middle[showBandCounter] = (histogram[iBand][i - 1].getMax() + histogram[iBand][i - 1].getMin()) / 2;

				showBandCounter++;
			}
		}
	 
		res[0] = min;
		res[1] = max;
		res[2] = average;
		res[3] = middle;
		res[4] = nPixelsBand;
		return res;
	}
	
	/**
	 * Obtiene la tabla de valores
	 * @return
	 */
	public long[][] getTable() {
		return table;
	}
	
	/**
	 * Obtiene la tabla de valores normalizada. Divide todos los elementos por el número de
	 * pixeles total.
	 * @return tabla de valores normalizada
	 */
	public static double[][] convertToNormalizeHistogram(long[][] tableToConvert) {
		long[] nValues = new long[tableToConvert.length];
		for (int i = 0; i < tableToConvert.length; i++)
			for (int j = 0; j < tableToConvert[i].length; j++)
				nValues[i] += tableToConvert[i][j];
		
		double[][] res = new double[tableToConvert.length][tableToConvert[0].length];
		for (int i = 0; i < tableToConvert.length; i++)
			for (int j = 0; j < tableToConvert[i].length; j++) 
				res[i][j] = (double)((double)tableToConvert[i][j] / (double)nValues[i]);

		return res;
	}
	
	/**
	 * Obtiene la tabla de valores normalizada y acumulada. Divide todos los elementos por el número de
	 * pixeles total.
	 * @return tabla de valores normalizada
	 */
	public static double[][] convertTableToNormalizeAccumulate(long[][] tableToConvert) {
		double[][] res = convertToNormalizeHistogram(tableToConvert);
		for (int i = 0; i < tableToConvert.length; i++)
			for (int j = 0; j < tableToConvert[i].length; j++)
				if(j > 0)
					res[i][j] += res[i][j - 1];

		return res;
	}
	
	/**
	 * Obtiene el histograma de la imagen negativa.
	 * @return long[][]
	 */
	public long[][] getNegativeTable() {
		long[][] tableNeg = new long[table.length][table[0].length];
		for (int i = 0; i < tableNeg.length; i++) 
			for (int j = 0; j < tableNeg[i].length; j++) 
				tableNeg[i][table[i].length - j - 1] = table[i][j];
		return tableNeg;
	}

	/**
	 * Convierte un histograma al rango de valores de RGB, en pocas palabras
	 * aplica una operacion 0xff a cada pixel para quitar los numeros negativos
	 * y desplazarlos a su rango visual.
	 * @param histogram
	 */
	public static Histogram convertHistogramToRGB(Histogram histogram) {
		if (histogram == null)
			return null;

		if (histogram.dataType != IBuffer.TYPE_BYTE)
			return histogram;

		double min = histogram.getMin(0);
		double max = histogram.getMax(0);

		long table2[][] = histogram.getTable();

		long newTable[][] = new long[table2.length][table2[0].length];

		// Ponemos a cero todos los valores
		for (int i = 0; i < table2.length; i++)
			for (int j = 0; j < table2[i].length; j++)
				newTable[i][j] = 0;

		// Sumamos en la posicion calculada nueva el valor correspondiente
		for (int i = 0; i < table2.length; i++) {
			for (int j = 0; j < table2[i].length; j++) {
				double val = ((j * (max - min)) / 255) + min;
				int pos = ((int) val) & 0xff;
				if (pos < 0)
					pos = 0;
				if (pos >= newTable[i].length)
					pos = newTable[i].length - 1;

				newTable[i][pos] += table2[i][j];
			}
		}

		double mins[] = new double[histogram.getNumBands()];
		double maxs[] = new double[histogram.getNumBands()];
		
		for (int i = 0; i < mins.length; i++) {
			mins[i] = 0;
			maxs[i] = 255;
		}

		Histogram histogramNew = new Histogram(histogram.getNumBands(), mins, maxs, IBuffer.TYPE_BYTE);

		histogramNew.setTable(newTable);

		return histogramNew;
	}

	/**
	 * @param noDataValue the noDataValue to set
	 */
	public void setNoDataValue(double noDataValue) {
		this.noDataValue = noDataValue;
	}
}
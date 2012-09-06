/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.grid.filter.statistics;

import java.util.Arrays;

import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.RasterFilter;
import org.gvsig.raster.hierarchy.IStatistics;
/**
 * Filtro de recorte de colas. Este filtro toma pixels de la imagen (todos o
 * algunas muestras dependiendo de la variable percentSample) y los ordena.
 * Recorta un porcentaje controlado por tailPercenten ambos extremos del vector
 * ordenado. El nuevo máximo y mínimo coinciden con el valor de la posición del
 * vector recortado. Por arriba para el máximo y por abajo para el mínimo.
 * El execute de este filtro no recorre toda la imagen sino que lo hace en
 * función del porcentaje de muestras que quieren tomarse y calculando a partir
 * de este porcentaje un incremento.
 *
 * @version 31/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TailTrimFilter extends RasterFilter {
	public static String[] names             = new String[] {"tailTrim"};

	protected int 			count            = 0;
	protected int 			tailSize         = 0;
	protected int[]			tailSizeList     = null;
	protected int 			nSamples         = 0;
	protected boolean 		removeMaxValue   = false;
	protected int 			incX , incY;

	//Parámetros del filtro
	protected double 		tailPercent      = 0D;
	protected double[] 		tailPercentList  = null;
	public double 			percentSamples   = 0D;

	protected int[][] 		sample           = null;
	protected double[][] 	sampleDec        = null;
	protected IStatistics	stats            = null;
	/**
	 * Array con el resultado. La primera dimensión es el número de bandas y la segunda son dos elementos
	 * el máximo y el mínimo para esa banda.
	 */
	protected double[][]	result = null;


	public TailTrimFilter() {
		setName(names[0]);
	}

	/**
	 * Calcula el incremento de X y de Y para la toma de muestras en el calculo de
	 * valores para el recorte
	 */
	public void pre() {
		raster = (IBuffer) params.get("raster");
		height = raster.getHeight();
		width = raster.getWidth();
		if(params.get("tail") != null)
			tailPercent = ((Double) params.get("tail")).doubleValue();
		tailPercentList = ((double[]) params.get("tailList"));
		if(params.get("samples") != null)
			percentSamples = ((Double) params.get("samples")).doubleValue();
		if(params.get("remove") != null)
			removeMaxValue = ((Boolean) params.get("remove")).booleanValue();
		stats = ((IStatistics) params.get("stats"));

		if(tailPercentList != null)
			tailSizeList = new int[tailPercentList.length];
		
		if (exec) {
			count = 0;

			if (this.percentSamples == 0) { // Se toman todos los pixeles de la imagen
				nSamples = height * width;
				tailSize = (int) Math.round(this.tailPercent * nSamples);
				if(tailPercentList != null) {
					for (int i = 0; i < tailPercentList.length; i++)
						tailSizeList[i] = (int) Math.round(this.tailPercentList[i] * nSamples);
				}		
			} else { // Se toma un porcentaje de pixeles de la imagen para el calculo
				incX = (int) Math.round(width / (int) Math.round(this.percentSamples * width));
				incY = (int) Math.round(height / (int) Math.round(this.percentSamples * height));
				nSamples = (int) ((Math.round(width / incX) + 1) * (Math.round(height / incY) + 1));
				tailSize = (int) (nSamples * this.tailPercent);
				if(tailPercentList != null) {
					for (int i = 0; i < tailPercentList.length; i++)
						tailSizeList[i] = (int) (nSamples * this.tailPercentList[i]);
				}
			}
		}
	}

	protected int posInit = 0;
	protected int posFin = 0;

	/**
	 * Ordena las muestras , recorta y asigna máximo y mínimo dependiendo del
	 * porcentaje de recorte
	 */
	public void post() {
		if (exec) {
			// Ordenamos los vectores
			if (sample != null) {
				posInit = 0;
				posFin = sample[0].length - 1;
				for (int i = 0; i < raster.getBandCount(); i++)
					Arrays.sort(sample[i]);
			} else {
				posInit = 0;
				posFin = sampleDec[0].length - 1;
				for (int i = 0; i < raster.getBandCount(); i++)
					Arrays.sort(sampleDec[i]);
			}

			// Si está marcada la opción removeMaxValue se calcula la posición en la que el máximo valor
			// y el mínimo ya no estan, teniendo así un subconjunto del vector que elimina estos valores
			if (removeMaxValue) {
				if (sample != null)
					this.calcPosInitEnd();

				if (sampleDec != null)
					this.calcPosInitEndDec();
			}

			// Calculamos de nuevo el número de muestras ya que hemos quitado los valores máximo y mínimo
			nSamples = posFin - posInit;

			// Como ha podido cambiar nSamples recalculamos tailsize
			tailSize = (int) (nSamples * this.tailPercent);
			if(tailPercentList != null) {
				for (int i = 0; i < tailPercentList.length; i++)
					tailSizeList[i] = (int) (nSamples * this.tailPercentList[i]);
			}
		}
	}

	/**
	 * Calcula la posición de inicio y final donde el máximo y el mínimo ya no
	 * están para valores enteros.
	 */
	private void calcPosInitEnd() {
		for (int i = 0; i < sample[0].length; i++) {
			for (int iBand = 0; iBand < raster.getBandCount(); iBand++) {
				if (sample[iBand][i] != sample[iBand][0]) {
					posInit = i;
					break;
				}
			}
			if (posInit != 0)
				break;
		}

		for (int i = sample[0].length - 1; i > 0; i--) {
			for (int iBand = 0; iBand < raster.getBandCount(); iBand++) {
				if (sample[0][i] != sample[0][sample[0].length - 1]) {
					posFin = i;
					break;
				}
			}
			if (posFin != sample[0].length - 1)
				break;
		}
	}

	/**
	 * Calcula la posición de inicio y final donde el máximo y el mínimo ya no
	 * están para valores decimal.
	 */
	private void calcPosInitEndDec() {
		for (int i = 0; i < sampleDec[0].length; i++) {
			for (int iBand = 0; iBand < raster.getBandCount(); iBand++) {
				if (sampleDec[iBand][i] != sampleDec[iBand][0]) {
					posInit = i;
					break;
				}
			}
			if (posInit != 0)
				break;
		}

		for (int i = sampleDec[0].length - 1; i > 0; i--) {
			for (int iBand = 0; iBand < raster.getBandCount(); iBand++) {
				if (sampleDec[0][i] != sampleDec[0][sampleDec[0].length - 1]) {
					posFin = i;
					break;
				}
			}
			if (posFin != sampleDec[0].length - 1)
				break;
		}
	}

	/**
	 * Obtiene el porcentaje de recorte
	 * @return porcentaje de recorte
	 */
	public double getTailPercent() {
		return tailPercent;
	}

	/**
	 * Obtiene la lista de porcentajes de recorte 
	 * @return porcentajes de recorte
	 */
	public double[] getTailPercentList() {
		return tailPercentList;
	}
	
	/**
	 * Devuelve true si se eliminan los extremos de la serie antes del calculo del recorte de colas
	 * o false si no se eliminan.
	 * @return
	 */
	public boolean removeMaxValue() {
		return this.removeMaxValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getGroup()
	 */
	public String getGroup() {
		return "basics";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getUIParams()
	 */
	public Params getUIParams(String nameFilter) {
		Params params = new Params();
		return params;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getInRasterDataType()
	 */
	public int getInRasterDataType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getOutRasterDataType()
	 */
	public int getOutRasterDataType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getResult(java.lang.String)
	 */
	public Object getResult(String name) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#process(int, int)
	 */
	public void process(int x, int y) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#getNames()
	 */
	public String[] getNames() {
		return names;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.RasterFilter#isVisible()
	 */
	public boolean isVisible() {
		return false;
	}
}
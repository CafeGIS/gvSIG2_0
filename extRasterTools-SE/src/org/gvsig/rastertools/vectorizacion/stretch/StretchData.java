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
package org.gvsig.rastertools.vectorizacion.stretch;

import java.util.Observable;

import org.gvsig.raster.beans.canvas.layers.StretchLayerDataModel;
import org.gvsig.raster.beans.stretchselector.StretchSelectorData;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Modelo de datos para el panel de gestión de tramos.
 * 
 * 19/08/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class StretchData extends Observable {
	
	private StretchSelectorData    selectorData   = null;
	private StretchLayerDataModel  layerDataModel = null;
	private double[]               histogram      = new double[]{0, 0, 3, 4, 5, 8, 7, 18, 45, 36, 21, 36, 12, 23, 23, 40, 17, 10, 5, 1, 0, 0, 0};
	
	/**
	 * Constructor
	 * @param selectorData
	 */
	public StretchData() {
		this.selectorData = new StretchSelectorData();
	}
	
	/**
	 * Actualiza datos y llama al update de los observadores
	 */
	public void updateObservers() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Asigna el modelo de datos de la capa gráfica. Este es el que contiene la lista
	 * de valores de los tramos.
	 */
	public void setStretchLayerDataModel(StretchLayerDataModel layerDataModel) {
		this.layerDataModel = layerDataModel;
	}
	
	/**
	 * Obtiene el modelo de datos de la capa gráfica
	 * @return
	 */
	public StretchLayerDataModel getStretchDataModel() {
		return this.layerDataModel;
	}
	
	/**
	 * Obtiene los tramos a partir del modelo de datos de la gráfica.
	 * @return double[]
	 */
	public double[] getStretchs() {
		if(layerDataModel == null)
			return new double[]{getMin(), getMax()};
		return layerDataModel.getValues();		
	}
	
	/**
	 * Obtiene el el modelo de datos del selector de intervalos
	 * @return StretchSelectorData
	 */
	public StretchSelectorData getStretchSelectorData() {
		return selectorData;
	}

	/**
	 * Obtiene el histograma para asignarlo a la capa del canvas
	 * gráfico.
	 * @return double[]
	 */
	public double[] getHistogram() {
		return histogram;
	}

	/**
	 * Asigna el histograma en el formato tabla obtenido desde
	 * el objeto Histogram. Es almacenado en formato double[] que es
	 * el necesario para la capa del canvas.
	 * @param hist
	 */
	public void setHistogram(long[][] hist) {
		if(hist != null && hist.length > 0) {	
			this.histogram = new double[hist[0].length];
			for (int i = 0; i < hist[0].length; i++) 
				this.histogram[i] = hist[0][i];
		}
	}

	/**
	 * Obtiene el valor máximo
	 * @return
	 */
	public double getMax() {
		try {
			return new Double(selectorData.getMax()).doubleValue();
		} catch(NumberFormatException e) {
			RasterToolsUtil.debug("El valor de max de StretchSelectorData no es convertible a double", this, e);
		}
		return 0;
	}

	/**
	 * Asigna el valor máximo
	 * @param max
	 */
	public void setMax(double max) {
		selectorData.setMax(new Double(max).toString());
	}

	/**
	 * Obtiene el valor mínimo
	 * @return
	 */
	public double getMin() {
		try {
			return new Double(selectorData.getMin()).doubleValue();
		} catch(NumberFormatException e) {
			RasterToolsUtil.debug("El valor de max de StretchSelectorData no es convertible a double", this, e);
		}
		return 0;
	}

	/**
	 * Asigna el valor mínimo
	 * @param min
	 */
	public void setMin(double min) {
		selectorData.setMin(new Double(min).toString());
	}

	/**
	 * Obtiene el número de intervalos
	 * @return
	 */
	public double getNInterval() {
		try {
			return new Double(selectorData.getNumber()).doubleValue();
		} catch(NumberFormatException e) {
			RasterToolsUtil.debug("El valor de número de intervalos de StretchSelectorData no es convertible a double", this, e);
		}
		return 0;
	}

	/**
	 * Asigna el número de intervalos
	 * @param interval
	 */
	public void setNInterval(double nInterval) {
		selectorData.setNumber(new Double(nInterval).toString());
		if(layerDataModel != null)
			layerDataModel.generate((int)nInterval);
		updateObservers();
	}

	/**
	 * Obtiene el tamaño de intervalo
	 * @return
	 */
	public double getSizeInterval() {
		try {
			return new Double(selectorData.getIntervalSize()).doubleValue();
		} catch(NumberFormatException e) {
			RasterToolsUtil.debug("El valor de tamaño de intervalo de StretchSelectorData no es convertible a double", this, e);
		}
		return 0;
	}

	/**
	 * Asigna el tamaño de intervalo
	 * @param sizeInterval
	 */
	public void setSizeInterval(double sizeInterval) {
		selectorData.setIntervalSize(new Double(sizeInterval).toString());
		if(layerDataModel != null)
			layerDataModel.generate(sizeInterval, getMin(), getMax());
	}
}

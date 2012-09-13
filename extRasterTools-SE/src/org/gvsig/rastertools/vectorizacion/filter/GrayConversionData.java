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
package org.gvsig.rastertools.vectorizacion.filter;

import java.util.Observable;

import org.gvsig.raster.filter.grayscale.GrayScaleFilter;

/**
 * Datos asociados al panel GrayConversionPanel
 * 
 * 12/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GrayConversionData extends Observable {
	private String[]   bands                  = {"R", "G" , "B", "RGB"};
	private int        bandType               = GrayScaleFilter.GRAY;
	
	private int        posterizationLevels    = 2;
	private boolean    posterizationActive    = true;
	private int        posterizationThreshold = 127;
	
	private boolean    highPassActive         = false;
	private int        radiusHighPass         = 0;
	
	private boolean    noiseActive            = false;	
	private int        noiseThreshold         = 1;
	
	private boolean    modeActive            = false;	
	private int        modeThreshold         = 2;
	
	/**
	 * Consulta si está activo el filtro de ruido
	 * @param true si está activo y false si no lo está
	 */
	public boolean isNoiseActive() {
		return noiseActive;
	}

	/**
	 * Asigna si está activo el filtro de ruido
	 * @return true si está activo y false si no lo está
	 */
	public void setNoiseActive(boolean noiseActive) {
		this.noiseActive = noiseActive;
		updateObservers();
	}
	
	/**
	 * Obtiene el umbral para el filtro de reducción de ruido
	 * @return Entero con el valor del umbral. El umbral es el lado de la ventana 
	 * de píxeles a analizar.
	 */
	public int getNoiseThreshold() {
		return noiseThreshold;
	}

	/**
	 * Asigna el umbral para el filtro de reducción de ruido
	 * @param Entero con el valor del umbral. El umbral es el lado de la ventana 
	 * de píxeles a analizar.
	 */
	public void setNoiseThreshold(int noiseThreshold) {
		this.noiseThreshold = noiseThreshold;
		updateObservers();
	}
	
	/**
	 * Consulta si está activo el filtro de moda
	 * @param true si está activo y false si no lo está
	 */
	public boolean isModeActive() {
		return modeActive;
	}

	/**
	 * Asigna si está activo el filtro de moda
	 * @return true si está activo y false si no lo está
	 */
	public void setModeActive(boolean modeActive) {
		this.modeActive = modeActive;
		updateObservers();
	}

	/**
	 * Obtiene el umbral para el filtro de moda
	 * @return Entero con el valor del umbral. El umbral es el lado de la ventana 
	 * de píxeles a analizar.
	 */
	public int getModeThreshold() {
		return modeThreshold;
	}

	/**
	 * Asigna el umbral para el filtro de moda
	 * @param Entero con el valor del umbral. El umbral es el lado de la ventana 
	 * de píxeles a analizar.
	 */
	public void setModeThreshold(int modeThreshold) {
		this.modeThreshold = modeThreshold;
		updateObservers();
	}
	
	/**
	 * Consulta los niveles de posterización
	 * @return
	 */
	public int getPosterizationLevels() {
		return posterizationLevels;
	}

	/**
	 * Asigna los niveles de posterización
	 * @param posterizationLevels
	 */
	public void setPosterizationLevels(int posterizationLevels) {
		this.posterizationLevels = posterizationLevels;
		updateObservers();
	}
	
	/**
	 * Obtiene el texto del desplegable de bandas. Este depende del número de bandas
	 * del raster de origen.
	 * @return Lista de textos con la interpretación de las bandas con las que se hará la 
	 * posterización
	 */
	public String[] getBands() {
		return bands;
	}
	
	/**
	 * Asigna el texto del desplegable de bandas. Este depende del número de bandas
	 * del raster de origen.
	 * @param Lista de textos con la interpretación de las bandas con las que se hará la 
	 * posterización
	 */
	public void setBands(String[] bands) {
		this.bands = bands;
		updateObservers();
	}
	
	/**
	 * Consulta si está activo el filtro de paso alto
	 * @return true si está activo y false si no lo está
	 */
	public boolean isHighPassActive() {
		return highPassActive;
	}

	/**
	 * Asigna si está activo el filtro de paso alto
	 * @param true si está activo y false si no lo está
	 */
	public void setHighPassActive(boolean highPassActive) {
		this.highPassActive = highPassActive;
		updateObservers();
	}

	/**
	 * Consulta si está activa la posterización
	 * @return true si está activa y false si no lo está
	 */
	public boolean isPosterizationActive() {
		return posterizationActive;
	}

	/**
	 * Asigna si está activa la posterización
	 * @return true si está activo y false si no lo está
	 */
	public void setPosterizationActive(boolean posterizationActive) {
		this.posterizationActive = posterizationActive;
		updateObservers();
	}
	
	/**
	 * Consulta el tipo de banda asignado
	 * @return Entero con el tipo de banda. Debe corresponder a una constante
	 * definida en la clase GrayScaleFilter
	 */
	public int getBandType() {
		return bandType;
	}

	/**
	 * Asigna el tipo de banda asignado
	 * @param Entero con el tipo de banda. Debe corresponder a una constante
	 * definida en la clase GrayScaleFilter
	 */
	public void setBandType(int bandType) {
		this.bandType = bandType;
		updateObservers();
	}
	
	/**
	 * Obtiene el número de banda a partir del tipo de interpretación
	 * @return
	 */
	public int getBandFromBandType() {
		switch (bandType) {
		case GrayScaleFilter.R:
		case GrayScaleFilter.GRAY:	return 0;
		case GrayScaleFilter.G:	return 1;
		case GrayScaleFilter.B:	return 2;
		case GrayScaleFilter.RGB: return 3;
		}
		return -1;
	}
	
	/**
	 * Asigna el valor del umbral de la posterización
	 * @param posterizationThreshold
	 */
	public void setPosterizationThreshold(int posterizationThreshold) {
		this.posterizationThreshold = posterizationThreshold;
		updateObservers();
	}
	
	/**
	 * Obtener el valor del umbral de la posterización
	 * @param posterizationThreshold
	 */
	public int getPosterizationThreshold() {
		return posterizationThreshold;
	}

	/**
	 * Asigna el valor del radio del filtro de paso alto
	 * @param radiusHighPass
	 */
	public void setRadiusHighPass(int radiusHighPass) {
		this.radiusHighPass = radiusHighPass;
		updateObservers();
	}
	
	/**
	 * Obtiene el valor del radio del filtro de paso alto
	 * @return radiusHighPass
	 */
	public int getRadiusHighPass() {
		return this.radiusHighPass;
	}
	
	/**
	 * Actualiza datos y llama al update de los observadores
	 */
	public void updateObservers() {
		setChanged();
		notifyObservers();
	}

}

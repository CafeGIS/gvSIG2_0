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
package org.gvsig.rastertools.vectorizacion.clip;

import java.awt.geom.Point2D;
import java.util.Observable;

/**
 * Datos asociados a la interfaz de usuario CoordinatesSelectionPanel.
 * 
 * 12/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ClipData extends Observable {
	/**
	 * Número de decimales a mostrar en visualización
	 */
	public static final int    DEC               = 4;
	private String[]           scales            = {"1/4", "1/3", "1/2", "1", "2", "3", "4", "5"};
	private int                scaleSelected     = 3;
	/**
	 * Coordenadas reales y pixel
	 */
	private Point2D            ulWc              = new Point2D.Double();
	private Point2D            lrWc              = new Point2D.Double();
	private Point2D            ulPx              = new Point2D.Double();
	private Point2D            lrPx              = new Point2D.Double();
	
	/**
	 * Actualiza datos y llama al update de los observadores
	 */
	public void updateObservers() {
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Obtiene las coordenadas del mundo real
	 * @return double[]
	 */
	public double[] getWCCoordinates() {
		return new double[]{ulWc.getX(), ulWc.getY(), lrWc.getX(), lrWc.getY()};
	}
	
	/**
	 * Obtiene la coordenada de máxima X en pixel.
	 * @return Coordenada de máxima X en pixel
	 */
	public double getPxMaxX() {
		return Math.max(ulPx.getX(), lrPx.getX());
	}

	/**
	 * Obtiene la coordenada de máxima Y en pixel.
	 * @return Coordenada de máxima Y en pixel
	 */
	public double getPxMaxY() {
		return Math.max(ulPx.getY(), lrPx.getY());
	}

	/**
	 * Obtiene la coordenada de mínima X en pixel.
	 * @return Coordenada de mínima X en pixel
	 */
	public double getPxMinX() {
		return Math.min(ulPx.getX(), lrPx.getX());
	}

	/**
	 * Obtiene la coordenada de mínima Y en pixel.
	 * @return Coordenada de mínima Y en pixel
	 */
	public double getPxMinY() {
		return Math.min(ulPx.getY(), lrPx.getY());
	}

	/**
	 * Obtiene la coordenada de máxima X real.
	 * @return Coordenada de máxima X real.
	 */
	public double getUlxWc() {
		return ulWc.getX();
	}

	/**
	 * Obtiene la coordenada de máxima Y real.
	 * @return Coordenada de máxima Y real
	 */
	public double getUlyWc() {
		return ulWc.getY();
	}

	/**
	 * Obtiene la coordenada de mínima X real.
	 * @return Coordenada de mínima X real
	 */
	public double getLrxWc() {
		return lrWc.getX();
	}

	/**
	 * Obtiene la coordenada de mínima Y real.
	 * @return Coordenada de mínima Y real
	 */
	public double getLryWc() {
		return lrWc.getY();
	}
	
	/**
	 * Obtiene la coordenada de máxima X pixel
	 * @return Coordenada de máxima X pixel
	 */
	public double getUlxPx() {
		return ulPx.getX();
	}

	/**
	 * Obtiene la coordenada de máxima Y pixel.
	 * @return Coordenada de máxima Y pixel
	 */
	public double getUlyPx() {
		return ulPx.getY();
	}

	/**
	 * Obtiene la coordenada de mínima X pixel
	 * @return Coordenada de mínima X pixel
	 */
	public double getLrxPx() {
		return lrPx.getX();
	}

	/**
	 * Obtiene la coordenada de mínima Y pixel.
	 * @return Coordenada de mínima Y pixel
	 */
	public double getLryPx() {
		return lrPx.getY();
	}
	
	/**
	 * Asigna las coordenadas del mundo real a partir de números en coma flotante.
	 * @param minx coordenada mínima de X
	 * @param miny coordenada mínima de Y
	 * @param maxx coordenada máxima de X
	 * @param maxy coordenada máxima de Y
	 * @param dec Número de decimales a mostrar en la caja de texto
	 */
	public void setCoorRealFromDouble(double ulx, double uly, double lrx, double lry) {
		ulWc = new Point2D.Double(ulx, uly);
		lrWc = new Point2D.Double(lrx, lry);
		updateObservers();
	}
	
	/**
	 * Asigna las coordenadas pixel a partir de números en coma flotante.
	 * @param minx coordenada mínima de X
	 * @param miny coordenada mínima de Y
	 * @param maxx coordenada máxima de X
	 * @param maxy coordenada máxima de Y
	 * @param dec Número de decimales a mostrar en la caja de texto
	 */
	public void setCoorPixelFromDouble(double minx, double miny, double maxx, double maxy) {
		ulPx = new Point2D.Double(minx, miny);
		lrPx = new Point2D.Double(maxx, maxy);
		updateObservers();
	}
	
	/**
	 * Asigna la coordenada X de la esquina superior izquierda en coordenadas reales
	 * @param ulx
	 */
	public void setUlxWc(double ulx) {
		ulWc = new Point2D.Double(ulx, ulWc.getY());
	}
	
	/**
	 * Asigna la coordenada Y de la esquina superior izquierda en coordenadas reales
	 * @param uly
	 */
	public void setUlyWc(double uly) {
		ulWc = new Point2D.Double(ulWc.getX(), uly);
	}
	
	/**
	 * Asigna la coordenada X de la esquina superior izquierda en píxeles
	 * @param ulx
	 */
	public void setUlxPx(double ulx) {
		ulPx = new Point2D.Double(ulx, ulPx.getY());
	}
	
	/**
	 * Asigna la coordenada Y de la esquina superior izquierda en píxeles
	 * @param uly
	 */
	public void setUlyPx(double uly) {
		ulPx = new Point2D.Double(ulPx.getX(), uly);
	}

	/**
	 * Asigna la coordenada X de la esquina inferior derecha en coordenadas reales
	 * @param lrx
	 */
	public void setLrxWc(double lrx) {
		lrWc = new Point2D.Double(lrx, lrWc.getY());
	}
	
	/**
	 * Asigna la coordenada Y de la esquina inferior derecha en coordenadas reales
	 * @param lry
	 */
	public void setLryWc(double lry) {
		lrWc = new Point2D.Double(lrWc.getX(), lry);
	}
	
	/**
	 * Asigna la coordenada X de la esquina inferior derecha en píxeles
	 * @param ulx
	 */
	public void setLrxPx(double lrx) {
		lrPx = new Point2D.Double(lrx, lrPx.getY());
	}
	
	/**
	 * Asigna la coordenada Y de la esquina inferior derecha en píxeles
	 * @param uly
	 */
	public void setLryPx(double lry) {
		lrPx = new Point2D.Double(lrPx.getX(), lry);
	}
	
	/**
	 * Inicializa los valores a cero. Esto se hace cuando la selección es fuera del área.
	 */
	public void setOutOfArea() {
		ulWc = new Point2D.Double();
		lrWc = new Point2D.Double();
		ulPx = new Point2D.Double();
		lrPx = new Point2D.Double();
	}
	
	/**
	 * Obtiene el texto del desplegable de escalas. 
	 * @return Lista de textos con las escalas
	 */
	public String[] getScales() {
		return scales;
	}
	
	/**
	 * Asigna el texto del desplegable de escalas. 
	 * @param Lista de textos con las escalas
	 */
	public void setScales(String[] scales) {
		this.scales = scales;
		updateObservers();
	}
	
	/**
	 * Obtiene la escala seleccionada en el selector
	 * @return posición en la lista de escalas
	 */
	public int getScaleSelected() {
		return scaleSelected;
	}

	/**
	 * Asigna la escala seleccionada
	 * @param posición en la lista de escalas
	 */
	public void setScaleSelected(int scaleSelected) {
		this.scaleSelected = scaleSelected;
	}
	
	/**
	 * Obtiene el valor de escala a partir de la posición 
	 * seleccionada en la lista.
	 * @return
	 */
	public double getScaleSelectedValue() {
		switch (getScaleSelected()) {
		case 0: return 0.25;
		case 1: return 0.33;
		case 2: return 0.5;
		case 3: return 1;
		case 4: return 2;
		case 5: return 3;
		case 6: return 4;
		case 7: return 5;
		}
		return 1;
	}
}

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
package org.gvsig.rastertools.clipping;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Observable;

/**
 * Datos necesarios para realizar un recorte.
 *
 * 27/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ClippingData extends Observable {
	/**
	 * Número de decimales a mostrar en visualización
	 */
	public static final int  DEC              = 4;
	/**
	 * Coordenadas reales y pixel
	 */
	private Point2D          ulWc             = new Point2D.Double();
	private Point2D          lrWc             = new Point2D.Double();
	private Point2D          llWc             = new Point2D.Double();
	private Point2D          urWc             = new Point2D.Double();

	private Point2D          ulPx             = new Point2D.Double();
	private Point2D          lrPx             = new Point2D.Double();
	private Point2D          llPx             = new Point2D.Double();
	private Point2D          urPx             = new Point2D.Double();
	/**
	 * Valores reales para el ancho, alto y tamaño de celda. Esto es necesario
	 * porque en la caja de texto se guardan con decimales recortados y cuando se
	 * pide el valor se devuelven completos.
	 */
	private double           pxWidth          = 0;
	private double           pxHeight         = 0;
	/**
	 * Relación entre el ancho y alto en pixeles de la imagen
	 */
	private double           ratioWidthHeight = 0;

	private AffineTransform  at               = null;

	/**
	 * Inicializa los valores de ancho y alto de la imagen de salida a partir del ancho y alto en pixeles
	 * de la de entrada. Esto solo es necesario al inicio ya que no tienen por que coincidir. También inicializa
	 * la relación entre ancho y alto. La llamada a este método notificará a los observadores cambios en los
	 * datos.
	 */
	public void initSize() {
		pxHeight = Math.ceil(getPxMaxY()) - Math.floor(getPxMinY());
		pxWidth = Math.ceil(getPxMaxX()) - Math.floor(getPxMinX());
		ratioWidthHeight = (double) (pxWidth / pxHeight);
		updateObservers();
	}

	/**
	 * Actualiza datos y llama al update de los observadores
	 */
	public void updateObservers() {
		setChanged();
		notifyObservers();
	}

	/**
	 * Inicializa los valores a cero. Esto se hace cuando la selección es fuera del área.
	 */
	public void setOutOfArea() {
		ulWc = new Point2D.Double();
		lrWc = new Point2D.Double();
		urWc = new Point2D.Double();
		llWc = new Point2D.Double();

		ulPx = new Point2D.Double();
		lrPx = new Point2D.Double();
		llPx = new Point2D.Double();
		urPx = new Point2D.Double();
		pxHeight = pxWidth = 0;
	}

	/**
	 * Obtiene la relación entre el ancho y alto en píxeles;
	 * @return
	 */
	public double getRatio() {
		return (double) (ratioWidthHeight);
	}

	/**
	 * Obtiene el valor del ancho del raster de salida
	 * @return
	 */
	public double getPxWidth() {
		return pxWidth;
	}

	/**
	 * Obtiene el valor del alto del raster de salida
	 * @return
	 */
	public double getPxHeight() {
		return pxHeight;
	}

	/**
	 * Asigna el valor del ancho del raster de salida
	 * @param pxw Ancho en píxeles del raster de salida
	 */
	public void setPxWidth(double pxw) {
		pxWidth = pxw;
	}

	/**
	 * Asigna el valor del alto del raster de salida
	 * @param pxh Alto en píxeles del raster de salida
	 */
	public void setPxHeight(double pxh) {
		pxHeight = pxh;
	}

	/**
	 * Obtiene el tamaño de celda
	 * @return
	 */
	public double getCellSize() {
		return Math.abs(ulWc.getX() - lrWc.getX()) / pxWidth;
	}

	/**
	 * Obtiene la coordenada de máxima X en pixel.
	 * @return Coordenada de máxima X en pixel
	 */
	public double getPxMaxX() {
		return Math.max(Math.max(ulPx.getX(), lrPx.getX()), Math.max(urPx.getX(), llPx.getX()));
	}

	/**
	 * Obtiene la coordenada de máxima Y en pixel.
	 * @return Coordenada de máxima Y en pixel
	 */
	public double getPxMaxY() {
		return Math.max(Math.max(ulPx.getY(), lrPx.getY()), Math.max(urPx.getY(), llPx.getY()));
	}

	/**
	 * Obtiene la coordenada de mínima X en pixel.
	 * @return Coordenada de mínima X en pixel
	 */
	public double getPxMinX() {
		return Math.min(Math.min(ulPx.getX(), lrPx.getX()), Math.min(llPx.getX(), urPx.getX()));
	}

	/**
	 * Obtiene la coordenada de mínima Y en pixel.
	 * @return Coordenada de mínima Y en pixel
	 */
	public double getPxMinY() {
		return Math.min(Math.min(ulPx.getY(), lrPx.getY()), Math.min(urPx.getY(), llPx.getY()));
	}

	/**
	 * Obtiene la coordenada de máxima X real.
	 * @return Coordenada de máxima X real
	 */
	public double getWcMaxX() {
		return Math.max(Math.max(ulWc.getX(), lrWc.getX()), Math.max(urWc.getX(), llWc.getX()));
	}

	/**
	 * Obtiene la coordenada de máxima Y real.
	 * @return Coordenada de máxima Y real
	 */
	public double getWcMaxY() {
		return Math.max(Math.max(ulWc.getY(), lrWc.getY()), Math.max(urWc.getY(), llWc.getY()));
	}

	/**
	 * Obtiene la coordenada de mínima X real.
	 * @return Coordenada de mínima X real
	 */
	public double getWcMinX() {
		return Math.min(Math.min(ulWc.getX(), lrWc.getX()), Math.min(llWc.getX(), urWc.getX()));
	}

	/**
	 * Obtiene la coordenada de mínima Y real.
	 * @return Coordenada de mínima Y real.
	 */
	public double getWcMinY() {
		return Math.min(Math.min(ulWc.getY(), lrWc.getY()), Math.min(urWc.getY(), llWc.getY()));
	}

	/**
	 * Obtiene la coordenada superior izquierda real en X.
	 * @return Coordenada de máxima X real.
	 */
	public double getUlxWc() {
		return ulWc.getX();
	}

	/**
	 * Obtiene la coordenada superior izquierda real en Y.
	 * @return Coordenada de máxima Y real
	 */
	public double getUlyWc() {
		return ulWc.getY();
	}

	/**
	 * Obtiene la coordenada inferior izquierda real en X.
	 * @return Coordenada de máxima X real.
	 */
	public double getLlxWc() {
		return llWc.getX();
	}

	/**
	 * Obtiene la coordenada inferior izquierda real en Y.
	 * @return Coordenada de máxima Y real
	 */
	public double getLlyWc() {
		return llWc.getY();
	}

	/**
	 * Obtiene la coordenada inferior derecha real en X.
	 * @return Coordenada de mínima X real
	 */
	public double getLrxWc() {
		return lrWc.getX();
	}

	/**
	 * Obtiene la coordenada inferior derecha real en Y.
	 * @return Coordenada de mínima Y real
	 */
	public double getLryWc() {
		return lrWc.getY();
	}

	/**
	 * Obtiene la coordenada superior derecha real en X.
	 * @return Coordenada de mínima X real
	 */
	public double getUrxWc() {
		return urWc.getX();
	}

	/**
	 * Obtiene la coordenada superior derecha real en Y.
	 * @return Coordenada de mínima Y real
	 */
	public double getUryWc() {
		return urWc.getY();
	}

	/**
	 * Obtiene la coordenada superior izquierda pixel en X.
	 * @return Coordenada de máxima X real.
	 */
	public double getUlxPx() {
		return ulPx.getX();
	}

	/**
	 * Obtiene la coordenada superior izquierda pixel en Y.
	 * @return Coordenada de máxima Y real
	 */
	public double getUlyPx() {
		return ulPx.getY();
	}

	/**
	 * Obtiene la coordenada inferior izquierda pixel en X.
	 * @return Coordenada de máxima X real.
	 */
	public double getLlxPx() {
		return llPx.getX();
	}

	/**
	 * Obtiene la coordenada inferior izquierda pixel en Y.
	 * @return Coordenada de máxima Y real
	 */
	public double getLlyPx() {
		return llPx.getY();
	}

	/**
	 * Obtiene la coordenada inferior derecha pixel en X.
	 * @return Coordenada de mínima X real
	 */
	public double getLrxPx() {
		return lrPx.getX();
	}

	/**
	 * Obtiene la coordenada inferior derecha pixel en Y.
	 * @return Coordenada de mínima Y real
	 */
	public double getLryPx() {
		return lrPx.getY();
	}

	/**
	 * Obtiene la coordenada superior derecha pixel en X.
	 * @return Coordenada de mínima X real
	 */
	public double getUrxPx() {
		return urPx.getX();
	}

	/**
	 * Obtiene la coordenada superior derecha pixel en Y.
	 * @return Coordenada de mínima Y real
	 */
	public double getUryPx() {
		return urPx.getY();
	}

	/**
	 * Ancho en coordenadas reales
	 * @return
	 */
	public double getWcWidth() {
		return Math.abs(getWcMaxX() - getWcMinX());
	}

	/**
	 * Alto en coordenadas reales
	 * @return
	 */
	public double getWcHeight() {
		return Math.abs(getWcMaxY() - getWcMinY());
	}

	/**
	 * Asigna las coordenadas del mundo real a partir de números en coma flotante.
	 * @param ul coordenada superior izquierda
	 * @param lr coordenada inferior derecha
	 * @param ll coordenada inferior izquierda
	 * @param ur coordenada superior derecha
	 */
	public void setCoorReal(Point2D ul, Point2D lr, Point2D ll, Point2D ur) {
		ulWc = ul;
		lrWc = lr;
		llWc = ll;
		urWc = ur;
	}

	/**
	 * Asigna las coordenadas pixel a partir de números en coma flotante.
	 * @param ul coordenada superior izquierda
	 * @param lr coordenada inferior derecha
	 * @param ll coordenada inferior izquierda
	 * @param ur coordenada superior derecha
	 */
	public void setCoorPixel(Point2D ul, Point2D lr, Point2D ll, Point2D ur) {
		ulPx = ul;
		lrPx = lr;
		llPx = ll;
		urPx = ur;
	}

	/**
	 * Asigna las coordenadas del mundo real a partir de números en coma flotante.
	 * @param minx coordenada mínima de X
	 * @param miny coordenada mínima de Y
	 * @param maxx coordenada máxima de X
	 * @param maxy coordenada máxima de Y
	 * @param dec Número de decimales a mostrar en la caja de texto
	 */
	/*public void setCoorRealFromDouble(double ulx, double uly, double lrx, double lry) {
		ulWc = new Point2D.Double(ulx, uly);
		lrWc = new Point2D.Double(lrx, lry);
	}*/

	/**
	 * Asigna las coordenadas pixel a partir de números en coma flotante.
	 * @param minx coordenada mínima de X
	 * @param miny coordenada mínima de Y
	 * @param maxx coordenada máxima de X
	 * @param maxy coordenada máxima de Y
	 * @param dec Número de decimales a mostrar en la caja de texto
	 */
	/*public void setCoorPixelFromDouble(double minx, double miny, double maxx, double maxy) {
		ulPx = new Point2D.Double(minx, miny);
		lrPx = new Point2D.Double(maxx, maxy);
	}*/

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
	 * Asigna la matriz de transformación
	 * @param at AffineTransform
	 */
	public void setAffineTransform(AffineTransform at) {
		this.at = at;
	}

	/**
	 * Obtiene las coordenadas del mundo real para la petición de recorte
	 * @return double[]
	 */
	public double[] getWcCoordinatesToClip() {
		Point2D ul = new Point2D.Double(getPxMinX(), getPxMinY());
		Point2D lr = new Point2D.Double(getPxMaxX(), getPxMaxY());
		at.transform(ul, ul);
		at.transform(lr, lr);
		return new double[]{ul.getX(), ul.getY(), lr.getX(), lr.getY()};
	}

	/**
	 * Obtiene el tamaño en pixeles de la imagen de la petición de recorte
	 * @return double[]
	 */
	public double[] getPxSizeToClip() {
		return new double[]{(getPxMaxX() - getPxMinX()), (getPxMaxY() - getPxMinY())};
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		ClippingData data = new ClippingData();
		data.ulWc = (Point2D)this.ulWc.clone();
		data.llWc = (Point2D)this.llWc.clone();
		data.lrWc = (Point2D)this.lrWc.clone();
		data.urWc = (Point2D)this.urWc.clone();
		data.ulPx = (Point2D)this.ulPx.clone();
		data.llPx = (Point2D)this.llPx.clone();
		data.lrPx = (Point2D)this.lrPx.clone();
		data.urPx = (Point2D)this.urPx.clone();
		data.pxWidth = this.pxWidth;
		data.pxHeight = this.pxHeight;
		data.ratioWidthHeight = this.ratioWidthHeight;
		if(this.at!=null)
			data.at = (AffineTransform)this.at.clone();
		return data;
	}
}

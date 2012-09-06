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

import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * Esta clase representa a un punto de control que puede estar asociado a un 
 * raster. Mantiene información del punto en coordenadas reales y coordenadas pixel
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GeoPoint {
	public Point2D        pixelPoint       = null;
	public Point2D        mapPoint         = null;
	public boolean        active           = true;
	public String         label            = "";
	public int            number           = 0;
	private double        errorX           = 0;
	private double        errorY           = 0;
	private double        rms              = 0;
	private double        xEvaluate        = 0;
	private double        yEvaluate        = 0;
	
	//Compatibilidad viejo modelo
	public static int     WIDTH_WINDOW     = 140;
	public static int     HEIGHT_WINDOW    = 140;
	public ViewPortData   leftViewPort     = null, rightViewPort = null;
	public Point2D        leftCenterPoint  = null, rightCenterPoint = null;
	public double         zoomLeft         = 1, zoomRight = 1;

	/**
	 * Constructor
	 */
	public GeoPoint(){}

	/**
	 * Constructor
	 * @param p pixelPoint
	 * @param m mapPoint
	 */
	public GeoPoint(Point2D p, Point2D m){
		this.pixelPoint = p;
		this.mapPoint = m;
	}
	
	/**
	 * Inicializa a 0 los valores de los errores
	 */
	public void resetErrors() {
		xEvaluate = 0;
		yEvaluate = 0;
		errorX = 0;
		errorY = 0;
		rms = 0;
	}
	
	/**
	 * Obtiene la predicción de la coordenada X
	 * @return double
	 */
	public double getEvaluateX() {
		return xEvaluate;
	}

	/**
	 * Asigna la predicción de la coordenada X
	 * @param errorX
	 */
	public void setEvaluateX(double xEvaluate) {
		this.xEvaluate = xEvaluate;
	}

	/**
	 * Obtiene la predicción de la coordenada Y
	 * @return double
	 */
	public double getEvaluateY() {
		return yEvaluate;
	}

	/**
	 * Asigna la predicción de la coordenada Y
	 * @param errorY
	 */
	public void setEvaluateY(double yEvaluate) {
		this.yEvaluate = yEvaluate;
	}
	
	/**
	 * Obtiene el error en X
	 * @return double
	 */
	public double getErrorX() {
		return errorX;
	}

	/**
	 * Asigna el error en X
	 * @param errorX
	 */
	public void setErrorX(double errorX) {
		this.errorX = errorX;
	}

	/**
	 * Obtiene el error en Y
	 * @return double
	 */
	public double getErrorY() {
		return errorY;
	}

	/**
	 * Asigna el error en Y
	 * @param errorY
	 */
	public void setErrorY(double errorY) {
		this.errorY = errorY;
	}

	/**
	 * Obtiene el error RMS
	 * @return
	 */
	public double getRms() {
		return rms;
	}

	/**
	 * Asigna el error RMS
	 * @param rms
	 */
	public void setRms(double rms) {
		this.rms = rms;
	}

	/**
	 * Hace una copia de la instancia del punto.
	 * @return GeoPoint
	 */
	public GeoPoint cloneGeoPoint(){
		Point2D pixel = null;
		Point2D map = null;
		if(pixelPoint != null)
			pixel = (Point2D)pixelPoint.clone();

		if(mapPoint != null)
			map = (Point2D)mapPoint.clone();

		GeoPoint gp = new GeoPoint(pixel, map);
		gp.setErrorX(this.getErrorX());
		gp.setErrorY(this.getErrorY());
		gp.setRms(this.getRms());

		if(leftViewPort != null){
			gp.leftViewPort = new ViewPortData();
			gp.leftViewPort.setProjection(leftViewPort.getProjection());
			gp.leftViewPort.setExtent(leftViewPort.getExtent());
			gp.leftViewPort.setSize((Dimension)leftViewPort.getSize().clone());
			gp.leftViewPort.reExtent();
		}

		if(rightViewPort != null){
			gp.rightViewPort = new ViewPortData();
			gp.rightViewPort.setProjection(rightViewPort.getProjection());
			gp.rightViewPort.setExtent(rightViewPort.getExtent());
			gp.rightViewPort.setSize((Dimension)rightViewPort.getSize().clone());
			gp.rightViewPort.reExtent();
		}

		if(leftCenterPoint != null)
			gp.leftCenterPoint = (Point2D)this.leftCenterPoint.clone();
		if(rightCenterPoint != null)
			gp.rightCenterPoint = (Point2D)this.rightCenterPoint.clone();
		gp.active = this.active;
		gp.zoomLeft = this.zoomLeft;
		gp.zoomRight = this.zoomRight;
		return gp;
	}

	/**
	 * Muestra por consola algunos valores de un geopunto
	 */
	public void show(){
		System.out.println("********GeoPoint**********");
		System.out.println("Pixel: "+pixelPoint);
		System.out.println("Map: "+mapPoint);
		System.out.println("Left Zoom Center: "+leftCenterPoint);
		System.out.println("Right Zoom Center: "+rightCenterPoint);
		System.out.println("Active: "+active);
		System.out.println("ZoomLeft: "+zoomLeft);
		System.out.println("ZoomRight: "+zoomRight);
		System.out.println("******End GeoPoint********");
	}
}
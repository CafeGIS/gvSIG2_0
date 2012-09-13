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
package org.gvsig.raster.beans.canvas.layers.functions;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import org.gvsig.raster.beans.canvas.layers.InfoLayer;
import org.gvsig.raster.util.MathUtils;
import org.gvsig.raster.util.RasterToolsUtil;
/**
 * Representa una funcion con posibilidad de arrastre para las funciones
 * logaritmicas y exponenciales. Con el raton se puede pasar de una a otra
 * directamente.
 * 
 * Las formas mas logicas de uso seria pasandole:
 * 1.0: Para una funcion logaritmica
 * -1.0: Para una funcion exponencial
 * 0.0: Para una funcion lineal
 * 
 * @version 02/04/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class LogaritmicExponentialLine extends StraightLine {
	/**
	 * Numero de puntos que contiene esta función
	 */
	private double num = 40.0;
	
	/**
	 * Base de la exponencial
	 */
	private double baseExp = 6.0;
	
	private double valueFunction = 1.0;
	
	/**
	 * Constructor. Asigna el color y establece la posicion de la funcion.
	 * Los valores normales son 1.0 para logaritmica y -1.0 para exponencial.
	 * El rango va desde -2.0 hasta 2.0. Siendo 0.0 una funcion lineal.
	 * @param c
	 */
	public LogaritmicExponentialLine(Color c, double point) {
		super(c);
		setShowSquares(false);

		valueFunction = point;
		recalcList();
	}

	/**
	 * Actualiza la barra informativa para saber en que estado se encuentra el
	 * componente.
	 * Cuando el porcentaje es mayor a 0 siempre estamos en la Logaritmica
	 * Cuando el porcentaje es menor a 0 siempre estamos en la exponencial
	 * @param perc
	 */
	private void setInfoPoint(Double perc) {
		if (canvas != null) {
			ArrayList list = canvas.getDrawableElements(InfoLayer.class);
			if (list.size() > 0) {
				InfoLayer infoLayer = (InfoLayer) list.get(0);
				
				if (perc == null) {
					infoLayer.setStatusLeft(null);
					infoLayer.setStatusRight(null);
					canvas.repaint();
					return;
				}

				if (perc.doubleValue() > 0.0)
					infoLayer.setStatusLeft(RasterToolsUtil.getText(this, "logarithmical"));
				else
					infoLayer.setStatusLeft(RasterToolsUtil.getText(this, "exponential"));

				infoLayer.setStatusRight(MathUtils.clipDecimals(Math.abs(perc.doubleValue()*100.0), 2) + "%");
			}
		}
	}
	
	/**
	 * Recalcula todos los puntos de la funcion
	 * ( 0.0 a  1.0) - Funcion logaritmica con aproximacion al centro
	 * ( 1.0 a  2.0) - Funcion logaritmica con aproximacion al borde
	 * ( 0.0 a -1.0) - Funcion exponencial con aproximacion al centro
	 * (-1.0 a -2.0) - Funcion exponencial con aproximacion al borde
	 */
	private void recalcList() {
		double x, y = 0.0;

		setInfoPoint(new Double(valueFunction));

		this.listSquare.clear();

		for (int i = 0; i <= num; i++) {
			x = ((double) i) / num;

			// Aproximacion al centro de una funcion logaritmica
			if (valueFunction >= 0.0 && valueFunction <= 1.0) {
				y = logFunction(x);
				y = x + ((y - x) * valueFunction);
			}

			// Aproximacion al borde de una funcion logaritmica
			if (valueFunction > 1.0 && valueFunction <= 2.0) {
				y = logFunction(x);
				y = y + ((1.0 - y) * (valueFunction - 1.0));
			}

			// Aproximacion al centro de una funcion exponencial
			if (valueFunction >= -1.0 && valueFunction < 0.0) {
				y = expFunction(x);
				y = x - ((x - y) * Math.abs(valueFunction));
			}

			// Aproximacion al borde de una funcion exponencial
			if (valueFunction >= -2.0 && valueFunction < -1.0) {
				y = expFunction(x);
				y = y * (1.0 - (Math.abs(valueFunction) - 1.0));
			}

			if (y < 0.0)
				y = 0.0;
			if (y > 1.0)
				y = 1.0;

			this.listSquare.add(new Square(x, y));
		}
	}
	
	/**
	 * Formula para calcular el valor en y de una funcion logaritmica en x
	 * @param x
	 * @return
	 */
	private double logFunction(double x) {
		return Math.log10(1.0 + (x * 99.0)) / 2.0;
	}
	
	/**
	 * Formula para calcular el valor en y de una funcion exponencial en x
	 * @param x
	 * @return
	 */
	private double expFunction(double x) {
		return (Math.exp(x * baseExp) - 1.0) / (Math.exp(baseExp) - 1.0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#mouseDragged(java.awt.event.MouseEvent)
	 */
	public boolean mouseDragged(MouseEvent e) {
		if (canvas.getCursor().getType() != Cursor.DEFAULT_CURSOR)
			return true;

		double x = pixelToValueX(e.getX());
		double y = pixelToValueY(e.getY());
		
		double y2 = 0.0;
		
		// Localizamos el punto del raton para pasar un porcentaje correcto y que 
		// asi coincida la funcion con el raton
		if (y >= x) {
			y2 = logFunction(x);
			if (y < y2)
				valueFunction = (y - x) / (y2 - x);
			else
				valueFunction = ((y - y2) / (1.0 - y2)) + 1.0;
		} else {
			y2 = expFunction(x);
			
			if (y > y2)
				valueFunction = -Math.abs((y - x) / (y2 - x));
			else
				valueFunction = -Math.abs((y2 - y) / y2)  - 1.0;
		}
		
		if (valueFunction > 2.0)
			valueFunction = 2.0;
		if (valueFunction < -2.0)
			valueFunction = -2.0;
		
		recalcList();
		canvas.repaint();
		canvas.callDataDragged("line", this);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#mouseMoved(java.awt.event.MouseEvent)
	 */
	public boolean mouseMoved(MouseEvent e) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#mousePressed(java.awt.event.MouseEvent)
	 */
	public boolean mousePressed(MouseEvent e) {
		return mouseDragged(e);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#mouseReleased(java.awt.event.MouseEvent)
	 */
	public boolean mouseReleased(MouseEvent e) {
		setInfoPoint(null);
		return true;
	}
	
	/**
	 * Devuelve si esta usando la funcion logaritmica
	 * @return
	 */
	public boolean isLogaritmical() {
		return (valueFunction > 0.0);
	}

	/**
	 * Devuelve si esta usando la funcion exponencial
	 * @return
	 */
	public boolean isExponencial() {
		return (valueFunction < 0.0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#getFunctionType()
	 */
	public int getFunctionType() {
		return 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#getValueFunction()
	 */
	public double getValueFunction() {
		return valueFunction;
	}
}
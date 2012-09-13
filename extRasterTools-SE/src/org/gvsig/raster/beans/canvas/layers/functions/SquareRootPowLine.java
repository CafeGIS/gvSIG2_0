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
 * raices cuadradas y cuadradas. Con el raton se puede pasar de una a otra
 * directamente.
 * 
 * Las formas mas logicas de uso seria pasandole:
 * 1.0: Para una funcion raiz cuadrada
 * -1.0: Para una funcion cuadrada
 * 0.0: Para una funcion lineal
 * 
 * @version 02/04/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class SquareRootPowLine extends StraightLine {
	/**
	 * Numero de puntos que contiene esta función
	 */
	private double num = 40.0;
	
	private double valueFunction = 1.0;
	
	/**
	 * Constructor. Asigna el color y establece la posicion de la funcion.
	 * Los valores normales son 1.0 para raiz cuadrada y -1.0 para cuadrada.
	 * El rango va desde -2.0 hasta 2.0. Siendo 0.0 una funcion lineal.
	 * @param c
	 */
	public SquareRootPowLine(Color c, double point) {
		super(c);
		setShowSquares(false);
		valueFunction = point;
		recalcList();
	}

	/**
	 * Actualiza la barra informativa para saber en que estado se encuentra el
	 * componente.
	 * Cuando el porcentaje es mayor a 0 siempre estamos en la raiz cuadrada
	 * Cuando el porcentaje es menor a 0 siempre estamos en la cuadrada
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
					infoLayer.setStatusLeft(RasterToolsUtil.getText(this, "square_root"));
				else
					infoLayer.setStatusLeft(RasterToolsUtil.getText(this, "pow"));

				infoLayer.setStatusRight(MathUtils.clipDecimals(Math.abs(perc.doubleValue() * 100.0), 2) + "%");
			}
		}
	}
	
	/**
	 * Recalcula todos los puntos de la funcion
	 * Posibles rangos para perc:
	 * ( 0.0 a  1.0) - Funcion raiz cuadrada con aproximacion al centro
	 * ( 1.0 a  2.0) - Funcion raiz cuadrada con aproximacion al borde
	 * ( 0.0 a -1.0) - Funcion cuadrada con aproximacion al centro
	 * (-1.0 a -2.0) - Funcion cuadrada con aproximacion al borde
	 * 
	 * @param perc
	 */
	private void recalcList() {
		double x, y = 0.0;

		setInfoPoint(new Double(valueFunction));

		this.listSquare.clear();

		for (int i = 0; i <= num; i++) {
			x = ((double) i) / num;

			// Aproximacion al centro de una funcion raiz cuadrada
			if (valueFunction >= 0.0 && valueFunction <= 1.0) {
				y = squareFunction(x);
				y = x + ((y - x) * valueFunction);
			}

			// Aproximacion al borde de una funcion raiz cuadrada
			if (valueFunction > 1.0 && valueFunction <= 2.0) {
				y = squareFunction(x);
				y = y + ((1.0 - y) * (valueFunction - 1.0));
			}

			// Aproximacion al centro de una funcion cuadrada
			if (valueFunction >= -1.0 && valueFunction < 0.0) {
				y = powFunction(x);
				y = x - ((x - y) * Math.abs(valueFunction));
			}

			// Aproximacion al borde de una funcion cuadrada
			if (valueFunction >= -2.0 && valueFunction < -1.0) {
				y = powFunction(x);
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
	 * Formula para calcular el valor en y de una funcion raiz cuadrada en x
	 * @param x
	 * @return
	 */
	private double squareFunction(double x) {
		return Math.sqrt(x);
	}
	
	/**
	 * Formula para calcular el valor en y de una funcion cuadrada en x
	 * @param x
	 * @return
	 */
	private double powFunction(double x) {
		return Math.pow(x, 2);
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
			y2 = squareFunction(x);
			if (y < y2)
				valueFunction = (y - x) / (y2 - x);
			else
				valueFunction = ((y - y2) / (1.0 - y2)) + 1.0;
		} else {
			y2 = powFunction(x);
			
			if (y > y2)
				valueFunction = -Math.abs((y - x) / (y2 - x));
			else
				valueFunction = -Math.abs((y2 - y) / y2)  - 1.0;
		}
		
		if (valueFunction < -2.0)
			valueFunction = -2.0;

		if (valueFunction > 2.0)
			valueFunction = 2.0;

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
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#getFunctionType()
	 */
	public int getFunctionType() {
		return 2;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.layers.functions.StraightLine#getValueFunction()
	 */
	public double getValueFunction() {
		return valueFunction;
	}
}
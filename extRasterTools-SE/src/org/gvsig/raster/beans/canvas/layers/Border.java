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
package org.gvsig.raster.beans.canvas.layers;

import java.awt.Color;
import java.awt.Graphics;

import org.gvsig.raster.beans.canvas.DrawableElement;
/**
 * Gráfico que representa un borde para el canvas. Dibuja un cuadrado sin relleno en el perimetro máximo de
 * dibujo del canvas. 
 *
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class Border extends DrawableElement {
	public static final int BORDER = 5;
		
	/**
	 * Constructor. Asigna el color
	 * @param c
	 */
	public Border(Color c) {
		setColor(c);
	}
	
	/**
	 * Dibujado de la línea de incremento exponencial sobre el canvas.
	 */
	public void paint(Graphics g) {
		g.setColor(color);
		g.drawRect(canvas.getCanvasMinX(), canvas.getCanvasMinY(), canvas.getCanvasWidth(), canvas.getCanvasHeight());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.DrawableElement#firstActions()
	 */
	public void firstActions() {
		canvas.addBorder(BORDER, BORDER, BORDER, BORDER);
	}

	public void firstDrawActions() {}
}
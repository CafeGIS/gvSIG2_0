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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.gvsig.raster.beans.canvas.DrawableElement;
import org.gvsig.raster.beans.canvas.GCanvas;
import org.gvsig.raster.beans.canvas.layers.functions.BaseFunction;
/**
 * Representa dos líneas rectas que señalizan el máximo y el mínimo.
 *
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class MinMaxLines extends DrawableElement {
	/**
	 * Representa el borde de separacion para los limites
	 */
	private int     border      = 2;

	private double  minPos      = 0.0D;
	private double  maxPos      = 1.0D;

	private boolean minSelected = false;
	private boolean maxSelected = false;
	
	/**
	 * Constructor. Asigna el color
	 * @param c
	 */
	public MinMaxLines(Color c) {
		setColor(c);
	}
	
	private int valueToPixel(double value) {
		return (int) Math.round(canvas.getCanvasMinX() + border + ((canvas.getCanvasMaxX() - canvas.getCanvasMinX() - (border * 2)) * value));
	}

	private double pixelToValue(int pixel) {
		return ((double) (pixel - canvas.getCanvasMinX() - border) / (double) (canvas.getCanvasMaxX() - canvas.getCanvasMinX() - (border * 2)));
	}
	
	/**
	 * Dibujado de las líneas y cuadros sobre el canvas
	 */
	public void paint(Graphics g) {
		g.setColor(color);
		int y = (int) canvas.getCanvasMinY();
		int x1 = valueToPixel(minPos);
		int x2 = valueToPixel(maxPos);
		Graphics2D g2 = (Graphics2D) g;
		float dash1[] = {10.0f};
		BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		Stroke stroke2 = g2.getStroke();
		g2.setStroke(stroke);

		g2.drawLine(x1, y, x1, canvas.getCanvasMaxY());
		g2.drawLine(x2, y, x2, canvas.getCanvasMaxY());

		g2.setStroke(stroke2);
	}

	/**
	 * Asigna el objeto JComponent donde se pintan los elementos. Inicializa los puntos
	 * de inicio y fin de línea y asigna los listener
	 * @param canvas
	 */
	public void setCanvas(GCanvas canvas) {
		super.setCanvas(canvas);
	}
	
	/**
	 * Se captura el punto a arrastrar
	 */
	public boolean mousePressed(MouseEvent e) {
		if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == 0)
			return true;

		if (e.getY() > canvas.getCanvasMinY() && e.getY() < canvas.getCanvasMaxY()) {
			if (e.getX() >= (valueToPixel(minPos) - 3) && e.getX() <= (valueToPixel(minPos) + 3)) {
				minSelected = true;
				return false;
			}
			if (e.getX() >= (valueToPixel(maxPos) - 3) && e.getX() <= (valueToPixel(maxPos) + 3)) {
				maxSelected = true;
				return false;
			}
		}
		return true;
	}

	/**
	 * Inicializamos el punto arrastrado a un valor fuera del array
	 */
	public boolean mouseReleased(MouseEvent e) {
		if (canvas != null && (minSelected != false || maxSelected != false )) {
			canvas.repaint();
			canvas.callDataChanged("minmax", this);
		}
		minSelected = false;
		maxSelected = false;
		return true;
	}

	/**
	 * Cuando se ha pinchado un punto y se arrastra se define aquí su comportamiento.
	 */
	public boolean mouseDragged(MouseEvent e) {
		if (canvas == null)
			return true;

		if (minSelected) {
			minPos = pixelToValue(e.getX());
			if (minPos < 0)
				minPos = 0;
			if (minPos > 1.0)
				minPos = 1.0;
			if (minPos > maxPos)
				maxPos = minPos;
			
			ArrayList elements = canvas.getDrawableElements(BaseFunction.class);
			if (elements.size() > 0) {
				BaseFunction baseFunction = (BaseFunction) elements.get(0);
				baseFunction.setMinX(minPos);
				baseFunction.setMaxX(maxPos);
			}
			
			canvas.repaint();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					canvas.callDataDragged("minmax", this);
				}
			});
			return false;
		}
		if (maxSelected) {
			maxPos = pixelToValue(e.getX());
			if (maxPos < 0)
				maxPos = 0;
			if (maxPos > 1.0)
				maxPos = 1.0;
			if (maxPos < minPos)
				minPos = maxPos;
			
			canvas.repaint();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					canvas.callDataDragged("minmax", this);
				}
			});
			return false;
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.canvas.DrawableElement#mouseMoved(java.awt.event.MouseEvent)
	 */
	public boolean mouseMoved(MouseEvent e) {
		if (e.getY() > canvas.getCanvasMinY() && e.getY() < canvas.getCanvasMaxY()) {
			if (e.getX() >= (valueToPixel(minPos) - 3) &&
					e.getX() <= (valueToPixel(minPos) + 3)) {
				canvas.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
				return false;
			}
			if (e.getX() >= (valueToPixel(maxPos) - 3) &&
					e.getX() <= (valueToPixel(maxPos) + 3)) {
				canvas.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Obtiene la distancia de la línea del mínimo al inicio del histograma. La 
	 * distancia la devuelve en tanto por cien del tamaño del gráfico
	 * @return Porcentaje de distancia entre el punto inicial del gráfico hasta la
	 * línea del mínimo.
	 */
	public double getMinDistance() {
		return minPos;
	}
	
	/**
	 * Obtiene la distancia de la línea del máximo al inicio del histograma. La 
	 * distancia la devuelve en tanto por cien del tamaño del gráfico
	 * @return Porcentaje de distancia entre el punto inicial del gráfico hasta la
	 * línea del máximo.
	 */
	public double getMaxDistance() {
		return maxPos;
	}
	
	public void firstActions() {}
	public void firstDrawActions() {}
}
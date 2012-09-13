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
package org.gvsig.raster.beans.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
/**
 * Clase base para los gráficos a dibujar sobre el canvas
 *
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public abstract class DrawableElement {
	protected Color   color     = Color.BLACK;
	private boolean   firstDraw = true;
	private boolean   drawing   = true;

	/**
	 * Entorno donde se dibuja
	 */
	protected GCanvas canvas    = null;
	
	/**
	 * Dibujado del elemento gráfico desde el GCanvas. Llamará antes de dibujar a
	 * la función firstDrawActions
	 * @param g
	 */
	public void draw(Graphics g) {
		if(!drawing)
			return;
		if (firstDraw) {
			firstDrawActions();
			firstDraw = false;
		}
		paint(g);
	}
	
	/**
	 * Asigna el flag de dibujado del elemento gráfico
	 * @param draw
	 */
	public void setDrawing(boolean drawing) {
		this.drawing = drawing;
	}
	
	/**
	 * Obtiene el flag que informa si el elemento gráfico está dibujandose
	 * o no.
	 * @return
	 */
	public boolean isDrawing() {
		return this.drawing;
	}

	/**
	 * Dibujado del elemento gráfico
	 * @param g
	 */
	protected abstract void paint(Graphics g);
	
	/**
	 * Acciones a ejecutar al asignar el canvas
	 */
	public abstract void firstActions();

	/**
	 * Acciones a ejecutar antes del primer dibujado
	 */
	public abstract void firstDrawActions();
	
	/**
	 * Asigna el objeto JComponent donde se pintan los elementos.
	 * @param canvas
	 */
	public void setCanvas(GCanvas canvas) {
		this.canvas = canvas;
	}
	
	/**
	 * Asigna el color de la línea
	 * @param c Color
	 */
	public void setColor(Color c) {
		this.color = c;
	}
	
	/**
	 * Obtiene el color de la línea
	 * @param c Color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Metodo que se ejecuta cuando se suelta el raton en el canvas
	 * @param e
	 * @return Si no se desea propagar el evento a otros drawables, devolver false.
	 */
	public boolean mouseReleased(MouseEvent e) {
		return true;
	}

	/**
	 * Metodo que se ejecuta cuando se presiona el raton en el canvas
	 * @param e
	 * @return Si no se desea propagar el evento a otros drawables, devolver false.
	 */
	public boolean mousePressed(MouseEvent e) {
		return true;
	}
	
	/**
	 * Metodo que se ejecuta cuando se esta dibujando con el raton en el canvas
	 * @param e
	 * @return Si no se desea propagar el evento a otros drawables, devolver false.
	 */
	public boolean mouseDragged(MouseEvent e) {
		return true;
	}
	
	/**
	 * Metodo que se ejecuta cuando se esta moviendo el raton sobre el canvas
	 * @param e
	 * @return Si no se desea propagar el evento a otros drawables, devolver false.
	 */
	public boolean mouseMoved(MouseEvent e) {
		return true;
	}
	
	/**
	 * Metodo que se ejecuta cuando entra el raton al canvas
	 * @param e
	 * @return
	 */
	public boolean mouseEntered(MouseEvent e) {
		return true;
	}

	/**
	 * Metodo que se ejecuta cuando sale el raton del canvas
	 * @param e
	 * @return
	 */
	public boolean mouseExited(MouseEvent e) {
		return true;
	}
}
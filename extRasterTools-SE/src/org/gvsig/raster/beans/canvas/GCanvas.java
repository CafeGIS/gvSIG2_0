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
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
/**
 * Canvas donde se pintan objetos GLine
 * 14-oct-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GCanvas  extends JPanel implements MouseListener, MouseMotionListener {
	private static final long   serialVersionUID = 5431466034535083594L;
	private Color               backgroundColor = Color.WHITE;
	private ArrayList           drawableElements = new ArrayList();
	public static final int     DEFAULT_CURSOR = Cursor.DEFAULT_CURSOR;
	
	private boolean hasMouse = false;
	
	// Tamaño de los bordes para el canvas
	private int               borderX1               = 0;
	private int               borderX2               = 0;
	private int               borderY1               = 0;
	private int               borderY2               = 0;
	
	private ArrayList           actionCommandListeners = new ArrayList();
		
	/**
	 * Contructor.Inicializa el objeto asignando color de fondo. Considera que el objeto
	 * GLine ya tiene su color asignado.
	 * @param line Objeto línea
	 * @param backgroundColor
	 */
	public GCanvas(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void addBorder(int x1, int y1, int x2, int y2) {
		borderX1 += x1;
		borderX2 += x2;
		borderY1 += y1;
		borderY2 += y2;
	}
	
	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(IGCanvasListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(IGCanvasListener listener) {
		actionCommandListeners.remove(listener);
	}
	
	/**
	 * Invocar a los eventos asociados al componente
	 */
	public void callDataChanged(String key, Object value) {
		Iterator iterator = actionCommandListeners.iterator();
		while (iterator.hasNext()) {
			IGCanvasListener listener = (IGCanvasListener) iterator.next();
			listener.actionDataChanged(new GCanvasEvent(this, key, value));
		}
	}
	
	/**
	 * Invocar a los eventos asociados al componente
	 */
	public void callDataDragged(String key, Object value) {
		Iterator iterator = actionCommandListeners.iterator();
		while (iterator.hasNext()) {
			IGCanvasListener listener = (IGCanvasListener) iterator.next();
			listener.actionDataDragged(new GCanvasEvent(this, key, value));
		}
	}
	
	/**
	 * Añade un elemento dibujable a la lista
	 * @param element
	 */
	public void addDrawableElement(DrawableElement element) {
		if (drawableElements.contains(element))
			return;
		element.setCanvas(this);
		element.firstActions();
		drawableElements.add(element);
	}
	
	/**
	 * Reemplaza un elemento dibujable si encuentra uno de su mismo tipo
	 * @param element
	 */
	public void replaceDrawableElement(DrawableElement element) {
		for (int i = 0; i < drawableElements.size(); i++) {
			if (element.getClass().isAssignableFrom(drawableElements.get(i).getClass())) {
				drawableElements.set(i, element);
				element.setCanvas(this);
				break;
			}
		}
	}

	/**
	 * Obtiene todos los elementos dibujable que sean una instancia de c1
	 * @param c1
	 * @return ArrayList de DrawableElements
	 */
	public ArrayList getDrawableElements(Class c1) {
		ArrayList elements = new ArrayList();
		for (int i = 0; i < drawableElements.size(); i++) {
			if (c1.isInstance(drawableElements.get(i)))
				elements.add(drawableElements.get(i));
		}
		return elements;
	}
	
	/**
	 * Reemplaza un elemento dibujable si encuentra uno del tipo especificado en el 
	 * parametro c1
	 * @param element
	 * @param c1
	 */
	public void replaceDrawableElement(DrawableElement element, Class cl) {
		for (int i = 0; i < drawableElements.size(); i++) {
			if (cl.isInstance(drawableElements.get(i))) {
				drawableElements.set(i, element);
				element.setCanvas(this);
				break;
			}
		}
	}
	
	/**
	 * Elimina un elemento dibujable
	 * @param Class clase del elemento a eliminar
	 */
	public void removeDrawableElement(Class cl) {
		for (int i = 0; i < drawableElements.size(); i++)
			if (cl.isInstance(drawableElements.get(i)))
				drawableElements.remove(i);
	}
	
	/**
	 * Asigna una lista de elementos dibujables
	 * @return
	 */
	public void setDrawableElements(ArrayList list) {
		drawableElements.clear();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i) instanceof DrawableElement)
				drawableElements.add(list.get(i));
		repaint();
	}
		
	/**
	 * Inicializa el fondo y dibuja el gráfico sobre el canvas.
	 */
	public void paint(Graphics g) {
		if (g == null) return;
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		for (int i = 0; i < drawableElements.size(); i++) 
			((DrawableElement)drawableElements.get(i)).draw(g);
	}
	
	/**
	 * Ejecuta las acciones antes del primer dibujado de todos
	 * los elementos dibujables
	 */
	public void execFirstDrawActions() {
		for (int i = 0; i < drawableElements.size(); i++) 
			((DrawableElement)drawableElements.get(i)).firstDrawActions();
	}
	
	/**
	 * Obtiene la posición mínima en X del canvas donde comenzar a dibujar
	 * @return
	 */
	public int getCanvasMinX() {
		return borderX1;
	}
	
	/**
	 * Obtiene la posición mínima en Y del canvas donde comenzar a dibujar
	 * @return
	 */
	public int getCanvasMinY() {
		return borderY1;
	}
	
	/**
	 * Obtiene la posición máxima en X del canvas donde terminar el dibujado
	 * @return
	 */
	public int getCanvasMaxX() {
		return getWidth() - borderX2;
	}
	
	/**
	 * Obtiene la posición máxima en Y del canvas donde terminar el dibujado
	 * @return
	 */
	public int getCanvasMaxY() {
		return getHeight() - borderY2;
	}
	
	/**
	 * Obtiene el ancho del canvas sumando a partir de getCanvasX donde termina el área de dibujo
	 * @return
	 */
	public int getCanvasWidth() {
		return getWidth() - (borderX1 + borderX2);
	}
	
	/**
	 * Obtiene el alto del canvas sumando a partir de getCanvasY donde termina el área de dibujo
	 * @return
	 */
	public int getCanvasHeight() {
		return getHeight() - (borderY1 + borderY2);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		for (int i = drawableElements.size() - 1; i >= 0 ; i--)
			if (!((DrawableElement) drawableElements.get(i)).mousePressed(e))
				return;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		for (int i = drawableElements.size() - 1; i >= 0 ; i--)
			if (!((DrawableElement) drawableElements.get(i)).mouseReleased(e))
				return;
		this.mouseMoved(e);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		for (int i = drawableElements.size() - 1; i >= 0 ; i--)
			if (!((DrawableElement) drawableElements.get(i)).mouseDragged(e))
				return;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		for (int i = drawableElements.size() - 1; i >= 0 ; i--)
			if (!((DrawableElement) drawableElements.get(i)).mouseMoved(e))
				return;
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		hasMouse = true;
		repaint();
		for (int i = drawableElements.size() - 1; i >= 0 ; i--)
			if (!((DrawableElement) drawableElements.get(i)).mouseEntered(e))
				return;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		hasMouse = false;
		repaint();
		for (int i = drawableElements.size() - 1; i >= 0 ; i--)
			if (!((DrawableElement) drawableElements.get(i)).mouseExited(e))
				return;
	}

	/**
	 * Devuelve si en ese momento el raton esta sobre el canvas
	 * @return
	 */
	public boolean isMouse() {
		return hasMouse;
	}

	public void mouseClicked(MouseEvent e) {}
}
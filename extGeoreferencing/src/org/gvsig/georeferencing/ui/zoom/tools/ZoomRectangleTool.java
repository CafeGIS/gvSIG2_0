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
package org.gvsig.georeferencing.ui.zoom.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.gvsig.georeferencing.ui.zoom.CanvasZone;

/**
 * Herramienta de selección de zoom sobre la vista.
 *
 * 17/01/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ZoomRectangleTool extends BaseViewTool implements MouseListener, MouseMotionListener {
	private Point2D                  initPoint = null;
    private double                   x = 0, y = 0, w = 0, h = 0;
    private Rectangle2D            	 result = null;


	/**
	 * Constructor. Asigna el canvas e inicializa los listeners.
	 * @param canvas
	 */
	public ZoomRectangleTool(CanvasZone canvas, ToolListener listener) {
		super(canvas, listener);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.ui.zoom.IViewTool#draw(java.awt.image.BufferedImage, java.awt.geom.Rectangle2D)
	 */
	public void draw(Graphics g) {
		if(initPoint == null || w == 0 || h == 0) {
			return;
		}
		g.setColor(Color.RED);
		g.drawRect((int)x, (int)y, (int)w, (int)h);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.ui.zoom.IViewTool#getResult()
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * Selecciona el punto inicial del cuadro del que se quiere el zoom
	 */
	public void mousePressed(MouseEvent e) {
		if(isActive()) {
			initPoint = e.getPoint();
			//Especificamos un tamaño mínimo por si no se llega a hacer dragged
			x = initPoint.getX();
			y = initPoint.getY();
			w = 30;
			h = 10;
			result = null;
		}
	}

	/**
	 * Asigna el flag que activa y desactiva la herramienta
	 * @param active true para activarla y false para desactivarla
	 */
	public void setActive(boolean active) {
		this.active = active;
		if(active) {
			onTool(new ToolEvent(this));
		} else {
			offTool(new ToolEvent(this));
		}
	}

	/**
	 * Dibujado del cuadro con el área a hacer zoom.
	 */
	public void mouseDragged(MouseEvent e) {
		if(isActive()) {
			x = initPoint.getX();
			y = initPoint.getY();
			w = Math.abs(e.getX() - x);
			h = Math.abs(e.getY() - y);
			if(e.getX() < x) {
				x = e.getX();
			}
			if(e.getY() < y) {
				y = e.getY();
			}
			Graphics g1 = canvas.getGraphics();
			g1.setColor(Color.RED);
			g1.drawRect((int)x,(int)y, (int)w, (int)h);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if(isActive()) {

			//Ajuste de la petición a la proporción del canvas. Esto se hace porque la relación entre
			//el ancho y alto del canvas es distinta a la del cuadro que hemos pintado en pantalla.
			double centerX = x + (w / 2);
			double centerY = y + (h / 2);

			if(w < h) {
				if(((double)canvas.getWidth() / (double)canvas.getHeight()) <= (w / h)) {
					h = (canvas.getHeight() * w) / canvas.getWidth();
					y = centerY - (h / 2);
				} else {
					w = (canvas.getWidth() * h) / canvas.getHeight();
					x = centerX - (w / 2);
				}
			} else {
				if(((double)canvas.getWidth() / (double)canvas.getHeight()) >= (w / h)) {
					w = (canvas.getWidth() * h) / canvas.getHeight();
					x = centerX - (w / 2);
				} else {
					h = (canvas.getHeight() * w) / canvas.getWidth();
					y = centerY - (h / 2);
				}
			}

			Point2D pInit = canvas.viewCoordsToWorld(new Point2D.Double(x, y));
			Point2D pEnd = canvas.viewCoordsToWorld(new Point2D.Double(x + w, y + h));
			if(canvas.getMinxMaxyUL())
				result = new Rectangle2D.Double(pInit.getX(), pEnd.getY(), Math.abs(pInit.getX() - pEnd.getX()), Math.abs(pInit.getY() - pEnd.getY()));
			else
				result = new Rectangle2D.Double(pInit.getX(), pInit.getY(), Math.abs(pInit.getX() - pEnd.getX()), Math.abs(pInit.getY() - pEnd.getY()));
			initPoint = null;
			x = y = w = h = 0;
			for (int i = 0; i < listeners.size(); i++) 
				((ToolListener)listeners.get(i)).endAction(new ToolEvent(this));
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}
}

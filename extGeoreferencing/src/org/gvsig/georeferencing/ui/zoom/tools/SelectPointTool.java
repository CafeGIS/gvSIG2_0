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

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import org.gvsig.georeferencing.ui.zoom.CanvasZone;

/**
 * Herramienta de selección de puntos de control sobre la vista
 * 
 * 17/01/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class SelectPointTool extends BaseViewTool implements MouseListener, MouseMotionListener {	
    private Point2D[]                 pointSelected = null;

	/**
	 * Constructor. Asigna el canvas e inicializa los listeners.
	 * @param canvas
	 */
	public SelectPointTool(CanvasZone canvas, ToolListener listener) {
		super(canvas, listener);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		pointSelected = new Point2D[2];
	}
	
	/**
	 * Asigna el flag que activa y desactiva la herramienta 
	 * @param active true para activarla y false para desactivarla
	 */
	public void setActive(boolean active) {
		this.active = active;
		if(active)
			onTool(new ToolEvent(this));
		else
			offTool(new ToolEvent(this));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.ui.zoom.IViewTool#draw(java.awt.image.BufferedImage, java.awt.geom.Rectangle2D)
	 */
	public void draw(Graphics g) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.ui.zoom.IViewTool#getResult()
	 */
	public Object getResult() {
		return pointSelected;
	}

	/**
	 * Selecciona el punto inicial del cuadro del que se quiere el zoom
	 */
	public void mousePressed(MouseEvent e) {
		if(!isActive())
			return;
		pointSelected[0] = e.getPoint();
		pointSelected[1] = canvas.viewCoordsToWorld(pointSelected[0]);
		for (int i = 0; i < listeners.size(); i++) 
			((ToolListener)listeners.get(i)).endAction(new ToolEvent(this));	
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
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

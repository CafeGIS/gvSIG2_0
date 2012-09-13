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
import java.util.ArrayList;

import org.gvsig.georeferencing.ui.zoom.CanvasZone;

/**
 * Clase base de la que deben extender las herramientas para la vista del 
 * zoom.
 * 
 * 17/01/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public abstract class BaseViewTool implements ToolListener {
	
	protected ArrayList        listeners   = new ArrayList();
	protected boolean          active      = false;
	protected CanvasZone       canvas      = null;
	
	private boolean            sleepActive = false;
	private boolean            sleep       = false;
	
	/**
	 * Constructor. Asigna el canvas y el listener para la tool.
	 * @param canvas
	 * @param listener
	 */
	public BaseViewTool(CanvasZone canvas, ToolListener listener) {
		this.canvas = canvas;
		addToolListener(listener);
	}
	
	/**
	 * Añade un listener para eventos de la tool
	 * @param listener
	 */
	public void addToolListener(ToolListener listener) {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	/**
	 * Informa de que la herramienta está activa.
	 * @param ev
	 */
	public void onTool(ToolEvent ev) {
		for (int i = 0; i < listeners.size(); i++) {
			((ToolListener)listeners.get(i)).onTool(ev);
		}
	}
	
	/**
	 * Informa de que la herramienta está activa.
	 * @param ev
	 */
	public void offTool(ToolEvent ev) {
		for (int i = 0; i < listeners.size(); i++) {
			((ToolListener)listeners.get(i)).offTool(ev);
		}
	}
	
	/**
	 * Evento de finalización de las acciones de la tool
	 * @param ev ToolEvent
	 */
	public void endAction(ToolEvent ev) {
		for (int i = 0; i < listeners.size(); i++) {
			((ToolListener)listeners.get(i)).endAction(ev);
		}
	}
	
	/**
	 * Consulta si está activo el evento de pinchado y arrastrado de los puntos de 
	 * control.
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Asigna el flag que activa y desactiva la herramienta 
	 * @param active true para activarla y false para desactivarla
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * Desactiva la herramienta temporalmente. Guarda el estado en el que estaba
	 * para restaurarlo cuando se invoque a awake
	 */
	public void sleep() {
		if(!sleep) {
			sleepActive = active;
			active = false;
			sleep = true;
		}
	}

	/**
	 * Recupera el estado de activación que tenía antes de la última invocación 
	 * de sleep
	 */
	public void awake() {
		if(sleep) {
			active = sleepActive;
			sleep = false;
		}
	}
	
	/**
	 * Parte gráfica de una tool. Una tool puede dibujar sobre una vista
	 * @param img BufferedImage
	 * @param ext Rectangle2D
	 */
	public abstract void draw(Graphics g);
	
	/**
	 * Obtiene el resultado de la aplicación de la herramienta
	 * @return Object
	 */
	public abstract Object getResult();
}

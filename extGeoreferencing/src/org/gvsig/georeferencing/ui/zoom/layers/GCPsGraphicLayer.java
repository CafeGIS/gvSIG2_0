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
package org.gvsig.georeferencing.ui.zoom.layers;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import org.gvsig.georeferencing.ui.zoom.CanvasZone;
import org.gvsig.georeferencing.ui.zoom.IGraphicLayer;
import org.gvsig.georeferencing.ui.zoom.tools.ToolEvent;
import org.gvsig.georeferencing.ui.zoom.tools.ToolListener;
import org.gvsig.raster.datastruct.GeoPoint;

/**
 * Capa gráfica que se dibuja sobre un ZoomControl y que dibuja
 * una lista de puntos de control.
 * 22/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GCPsGraphicLayer implements IGraphicLayer {
	public boolean                  active          = true;

	//Operaciones sobre los puntos de control
	private static final int        NONE            = -1;
	private static final int        MOVE            = 0;

	private ArrayList               pointList       = null;
	private int                     type            = GPGraphic.MAP;
	private Image                   icoBlue         = null;
	private Image                   icoRed          = null;
	private CanvasZone              canvas          = null;
	private int                     operation       = NONE;
	//Punto de control sobre el que se realiza la operación
	private GPGraphic               operanteGP      = null;
	private GPGraphic               lastGP          = null;
	//Listener para que objetos externos sean informados de las acciones de la capa
	private ToolListener            listener        = null;
	private boolean                 sleepActive     = false;

	/**
	 * Constructor. Asigna el tipo de punto a dibujar en la
	 * capa gráfica.
	 * @param type El valor de type viene definido por las constantes de GPGraphic
	 * @param listener Listener para acciones de finalización de la operación de mover punto
	 */
	public GCPsGraphicLayer(int type, ToolListener listener) {
		this.type = type;
		pointList = new ArrayList();
		this.listener = listener;
		try {
			icoBlue = new ImageIcon(getClass().getClassLoader().getResource("images/icoBlue.png")).getImage();
			icoRed = new ImageIcon(getClass().getClassLoader().getResource("images/icoRed.png")).getImage();
		} catch (NullPointerException e) {

		}
	}

	/**
	 * Añade un punto de control gráfico a la lista. Se hace una distinción entre MapGeoPoint
	 * y RasterGeoPoint para el calculo de las coordenadas de la vista donde se dibuja. Si es
	 * de tipo map se utilizarán la información de coordenadas reales contenidas en el objeto
	 * GeoPoint para transformarlas a coordenadas de la visualización.
	 *
	 * @param gp Punto de control
	 * @param id identificador del punto de control
	 */
	public void addMapGeoPoint(GeoPoint gp, long id) {
		GPGraphic graphicGCP = new GPGraphic(GPGraphic.MAP, gp);
		graphicGCP.setId(id);
		Point2D p = canvas.viewCoordsFromWorld(gp.mapPoint);
		graphicGCP.setDrawCoords(p);
		pointList.add(graphicGCP);
	}

	/**
	 * Añade un punto de control gráfico a la lista. Se hace una distinción entre MapGeoPoint
	 * y RasterGeoPoint para el calculo de las coordenadas de la vista donde se dibuja. Si es
	 * de tipo raster se utilizarán la información de coordenadas pixel contenidas en el objeto
	 * GeoPoint para transformarlas a coordenadas de la visualización.
	 *
	 * @param gp Punto de control
	 * @param id identificador del punto de control
	 */
	public void addPixelGeoPoint(GeoPoint gp, long id) {
		GPGraphic graphicGCP = new GPGraphic(GPGraphic.PIXEL, gp);
		graphicGCP.setId(id);
		Point2D p = canvas.viewCoordsFromWorld(gp.pixelPoint);
		graphicGCP.setDrawCoords(p);
		pointList.add(graphicGCP);
	}

	/**
	 * Elimina un punto de la lista a partir de su posición
	 * @param position
	 * @return
	 */
	public boolean removePixelGeoPoint(int position) {
		try {
			pointList.remove(position);
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * Elimina todos los puntos de la capa
	 */
	public void removeAllPoints() {
		pointList.clear();
	}

	/**
	 * Recalcula las coordenadas de dibujado
	 */
	public void recalcPixelDrawCoordinates() {
		for (int i = 0; i < pointList.size(); i++) {
			GPGraphic gp = ((GPGraphic)pointList.get(i));
			Point2D p = gp.getGeoPoint().pixelPoint;
			p = canvas.viewCoordsFromWorld(p);
			gp.setDrawCoords(p);
		}
	}

	/**
	 * Recalcula las coordenadas de dibujado
	 */
	public void recalcMapDrawCoordinates() {
		for (int i = 0; i < pointList.size(); i++) {
			GPGraphic gp = ((GPGraphic)pointList.get(i));
			Point2D p = gp.getGeoPoint().mapPoint;
			p = canvas.viewCoordsFromWorld(p);
			gp.setDrawCoords(p);
		}
	}

	/**
	 * Obtiene el identificador del punto requerido
	 * @param pos Posición del punto en la capa
	 * @return
	 */
	public long getID(int pos) {
		try {
			return ((GPGraphic)pointList.get(pos)).getId();
		}catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}

	/**
	 * Asigna el canvas
	 * @param canvas
	 */
	public void setCanvas(CanvasZone canvas) {
		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);
		this.canvas = canvas;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.ui.zoom.IGraphicLayer#draw(java.awt.Graphics2D, org.gvsig.raster.datastruct.Extent, int, int)
	 */
	public void draw(Graphics2D g, Rectangle2D ext, int w, int h) {
		for (int i = 0; i < pointList.size(); i++)
			((GPGraphic)pointList.get(i)).draw(g);
	}

	/**
	 * Asigna el flag que muestra u oculta la etiqueta del punto
	 * @param showLabel true para mostrar la etiqueta y false para ocultarla
	 */
	public void setShowLabel(boolean showLabel) {
		for (int i = 0; i < pointList.size(); i++)
			((GPGraphic)pointList.get(i)).setShowLabel(showLabel);
	}

	/**
	 * Asigna el flag que muestra u oculta el número del punto
	 * @param showLabel true para mostrar el punto y false para ocultarlo
	 */
	public void setShowNumber(boolean showNumber) {
		for (int i = 0; i < pointList.size(); i++)
			((GPGraphic)pointList.get(i)).setShowNumber(showNumber);
	}

	/**
	 * Obtiene el punto de la posición indicada por el parámetro pos
	 * @param pos Posición del punto
	 * @return GPGraphic
	 */
	public GPGraphic getPoint(int pos) {
		try {
			return ((GPGraphic)pointList.get(pos));
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Obtiene la lista de puntos
	 * @return ArrayList
	 */
	public ArrayList getPointList() {
		return pointList;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Cuando se suelta el botón del ratón después de haber arrastrado volvemos
	 * a dibujar el punto pero en la nueva ubicación. Después calculamos las nuevas
	 * coordenadas del punto y las asignamos. Finalmente desactivamos la operación de
	 * mover punto y ponemos el punto sobre el que operamos a null.
	 */
	public void mouseReleased(MouseEvent e) {
		if(!isActive() || getOperation() != MOVE)
			return;
		operanteGP.setDraw(true);
		operanteGP.setDrawCoords(e.getPoint());
		Point2D point = canvas.viewCoordsToWorld(e.getPoint());
		switch(type) {
		case GPGraphic.MAP: operanteGP.getGeoPoint().mapPoint = point; break;
		case GPGraphic.PIXEL: operanteGP.getGeoPoint().pixelPoint = point; break;
		}
		canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		operanteGP = null;
		setOperation(NONE);
		if(listener != null)
			listener.endAction(new ToolEvent(this));
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if(!isActive())
			return;
		if(getOperation() == MOVE) {
			operanteGP.setDraw(false);
			if(operanteGP.getType() == GPGraphic.PIXEL && icoRed != null)
				canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(icoRed, new Point(16, 16), ""));
			if(operanteGP.getType() == GPGraphic.MAP && icoBlue != null)
				canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(icoBlue, new Point(16, 16), ""));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		if(!isActive())
			return;
		setOperation(NONE);
		for (int i = 0; i < pointList.size(); i++) {
			GPGraphic gp = ((GPGraphic)pointList.get(i));
			int pxLeft = (int)gp.getDrawCoords().getX() - 4;
			int pxRight = (int)gp.getDrawCoords().getX() + 4;
			int pyUp = (int)gp.getDrawCoords().getY() - 4;
			int pyDown = (int)gp.getDrawCoords().getY() + 4;
			if(e.getX() >= pxLeft && e.getX() <= pxRight && e.getY() >= pyUp && e.getY() <= pyDown) {
				lastGP = operanteGP = gp;
				setOperation(MOVE);
			}

		}

	}

	/**
	 * Obtiene la operación sobre el cursor que hay seleccionada
	 * @return Entero que representa a la operación
	 */
	private int getOperation() {
		return operation;
	}

	/**
	 * Asigna la operación sobre el cursor que hay seleccionada
	 * @param op
	 */
	private void setOperation(int op) {
		operation = op;
		if(op == NONE)
			listener.offTool(new ToolEvent(this));
		else
			listener.onTool(new ToolEvent(this));
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
	 * Asigna el flag que activa y desactiva el evento de pinchado y arrastrado
	 * de los puntos de control.
	 * @param activeEvent
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Desactiva la herramienta temporalmente. Guarda el estado en el que estaba
	 * para restaurarlo cuando se invoque a awake
	 */
	public void sleep() {
		sleepActive = active;
		active = false;
	}

	/**
	 * Recupera el estado de activación que tenía antes de la última invocación
	 * de sleep
	 */
	public void awake() {
		active = sleepActive;
	}

	/**
	 * Obtiene el último punto arrastrado
	 * @return GPGraphic
	 */
	public GPGraphic getLastPoint() {
		return lastGP;
	}
}

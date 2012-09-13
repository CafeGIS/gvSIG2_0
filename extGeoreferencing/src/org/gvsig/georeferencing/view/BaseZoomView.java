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
package org.gvsig.georeferencing.view;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.gvsig.georeferencing.ui.zoom.CanvasZone;
import org.gvsig.georeferencing.ui.zoom.IGraphicLayer;
import org.gvsig.georeferencing.ui.zoom.ViewControl;

/**
 * Clase de la que heredan los dialogos de vista y zoom que utilizan
 * el ViewControl. Contiene métodos generales para el acceso a información
 * del control.
 *
 * 20/01/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public abstract class BaseZoomView extends JPanel {
	private static final long serialVersionUID = -8041978144045965049L;

	protected ViewControl zoomPixelControl = null;

	/**
	 * Obtiene el graphics del canvas
	 * @return
	 */
	public Graphics2D getCanvasGraphic() {
		return (Graphics2D) zoomPixelControl.getCanvas().getGraphics();
	}

	/**
	 * Obtiene el ancho del canvas
	 * @return
	 */
	public int getCanvasWidth() {
		return zoomPixelControl.getCanvasWith();
	}

	/**
	 * Obtiene el alto del canvas
	 * @return
	 */
	public int getCanvasHeight() {
		return zoomPixelControl.getCanvasHeight();
	}

	/**
	 * Asigna los parámetros de dibujado para el raster
	 * @param img Buffer con un área de datos
	 * @param ext Rectangle2D del área de datos dada
	 * @param pixelSize Tamaño de pixel
	 * @param center Punto del área de datos donde se quiere centrar el dibujado del buffer
	 */
	public abstract void setDrawParams(BufferedImage img, Rectangle2D ext, double pixelSize, Point2D center);

	/**
	 * Añade una capa gráfica al canvas. Esta debe heredar de IGraphicLayer
	 * para que pueda ser dibujada sobre el mismo.
	 * @param gl
	 */
	public abstract void addGraphicLayer(IGraphicLayer graphicLayer);

	/**
	 * Obtiene el panel de control de la vista
	 * @return
	 */
	public abstract ViewControl getControl();

	/**
	 * Obtiene el canvas asociado
	 * @return CanvasZone
	 */
	public CanvasZone getCanvas() {
		return zoomPixelControl.getCanvas();
	}
}

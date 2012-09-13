/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package org.gvsig.georeferencing.ui.zoom;

import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

/**
 * Interfaz  de dibujado de capas sobre ZoomControl
 * 22/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public interface IGraphicLayer extends MouseListener, MouseMotionListener {

	/**
	 * Dibujado del gr�fico
	 * @param g Graphics2D
	 * @param ext Rectangle2D del �rea de dibujado
	 * @param w Ancho en p�xeles del �rea de dibujado
	 * @param h Alto en p�xeles del �rea de dibujado
	 */
	public void draw(Graphics2D g, Rectangle2D ext, int w, int h);
}

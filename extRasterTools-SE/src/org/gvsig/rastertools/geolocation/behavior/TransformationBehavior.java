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
package org.gvsig.rastertools.geolocation.behavior;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;

/**
 * Clase base para los comportamientos de traslación, escalado, rotación y shearing de un raster.
 * Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public abstract class TransformationBehavior {

	private boolean                     activeTool = false;
	protected Image                    	defaultImage = null;
	protected GeoRasterBehavior         grBehavior = null;
	protected FLyrRasterSE              lyr = null;
	protected Color                     rectangleColor = Color.RED;
	protected ITransformIO				trIO = null;

	/**
	 * Consulta si la tool está activada o no
	 * @return
	 */
	public boolean isActiveTool() {
		return activeTool;
	}

	/**
	 * ASigna el valor de activación de la tool
	 * @param activeTool
	 */
	public void setActiveTool(boolean activeTool) {
		this.activeTool = activeTool;
	}

	/**
	 * Cambia el cursor de la vista al que le pasemos por parámetro
	 * @param c Image para el cursor
	 */
	protected void setCursor(Image c) {
		if(grBehavior != null)
			grBehavior.getMapControl().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(c, new Point(16, 16), ""));
	}

	/**
	 *  Cuando soltamos el botón del ratón desplazamos la imagen a la posición
	 * de destino calculando el extent nuevamente.
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException {
		if(trIO != null)
			trIO.applyTransformation();
	}

	/**
	 * Asigna el objeto externo al que se le asigna la transformación
	 * @param io
	 */
	public void setITransformIO(ITransformIO io) {
		this.trIO = io;
	}

	/**
	 * Método utilizado para el repintado del gráfico que sirve como apoyo para
	 * las transformaciones. Cada tipo de transformación (escalado, rotado, traslación y shearing)
	 * realiza sus acciones para redibujar este gráfico.
	 *
	 * @param g Graphics
	 */
	public abstract void paintComponent(Graphics g);

	/**
	 * Evento que se produce al pulsar el botón del ratón sobre la vista cuando la herramienta
	 * de geolocalización está activa. Esto produce la activación de una de las tranformaciones
	 * dependiendo de la posición del cursor del rastón relativa a la imagen.
	 *
	 * @param e Graphics
	 */
	public abstract void mousePressed(MouseEvent e) throws BehaviorException;

	/**
	 * Evento que se produce al mover el ratón sobre la vista cuando la herramienta
	 * de geolocalización está activa. Esto produce la variación del gráfico de apoyo para
	 * la transformación.
	 *
	 * @param e Graphics
	 */
	public abstract boolean mouseMoved(MouseEvent ev) throws BehaviorException;

	/**
	 * Evento que se produce al arrastrar el ratón sobre la vista cuando la herramienta
	 * de geolocalización está activa. Esto produce la variación del gráfico de apoyo para
	 * la transformación.
	 *
	 * @param e Graphics
	 */
	public abstract void mouseDragged(MouseEvent ev) throws BehaviorException;
}

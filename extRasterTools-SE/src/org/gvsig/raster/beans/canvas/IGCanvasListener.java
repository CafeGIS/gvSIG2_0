/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

import java.util.EventListener;
/**
 * Listener que deben implementar las clases que quieran recibir cambios
 * del GCanvas.
 * 
 * @version 28/02/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public interface IGCanvasListener extends EventListener {
	/**
	 * Evento que se dispara cuando el GCanvas cambia el valor de algún elemento
	 * @param e
	 */
	public void actionDataChanged(GCanvasEvent e);
	
	/**
	 * Evento que se dispara mientras se esta produciendo la acción
	 * @param e
	 */
	public void actionDataDragged(GCanvasEvent e);
}

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
package org.gvsig.raster.beans.previewbase;

import org.gvsig.gui.beans.imagenavigator.ImageUnavailableException;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.hierarchy.IRasterRendering;

/**
 * Interfaz para el procesado del rendering antes de dibujar la preview.
 * El cliente puede obtener la lista de filtros del rendering de la imagen 
 * que va a dibujarse como preview y modificarla para visulizar la preview 
 * modificada. En caso de no modificar la preview se ver� la imagen sin variaciones
 * con respecto a la original.
 * 
 * 19/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public interface IPreviewRenderProcess {
	
	/**
	 * Procesado del rendering
	 * @param rendering
	 * @throws ImageUnavailable Cuando se ha producido un error y hay que mostrar un mensaje en la 
	 * previsualizaci�n porque no se puede mostrar la imagen
	 */
	public void process(IRasterRendering rendering) throws FilterTypeException, ImageUnavailableException;
	
	/**
	 * Obtiene el flag que informa de si se est� mostrando la previsualizaci�n o no.
	 * En caso de no mostrarse deber�a lanzarse una excepci�n ImageUnavailableExcepcion al
	 * hacer un process.
	 * @return
	 */
	public boolean isShowPreview();
	
	/**
	 * Asigna el flag para mostrar u ocultar la preview. En caso de no mostrarse deber�a lanzarse una 
	 * excepci�n ImageUnavailableExcepcion cuando se hace un process.
	 * @param showPreview
	 */
	public void setShowPreview(boolean showPreview);
}

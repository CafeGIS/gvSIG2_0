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
package org.gvsig.raster.beans.previewbase;

import javax.swing.JPanel;


/**
 * Interfaz que implementan los paneles que se incorporan al PreviewBasePanel
 * 
 * 19/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public interface IUserPanelInterface {
	/**
	 * Obtiene el título del panel. Esta será la etiqueta que se ponga en 
	 * la pestaña del tab.
	 * @return Cadena con el título.
	 */
	public String getTitle();
	
	/**
	 * Obtiene el panel que se añade en el tab
	 * @return JPanel
	 */
	public JPanel getPanel();
}
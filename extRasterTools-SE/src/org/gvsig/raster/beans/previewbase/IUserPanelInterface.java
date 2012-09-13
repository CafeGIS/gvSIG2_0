/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
	 * Obtiene el t�tulo del panel. Esta ser� la etiqueta que se ponga en 
	 * la pesta�a del tab.
	 * @return Cadena con el t�tulo.
	 */
	public String getTitle();
	
	/**
	 * Obtiene el panel que se a�ade en el tab
	 * @return JPanel
	 */
	public JPanel getPanel();
}
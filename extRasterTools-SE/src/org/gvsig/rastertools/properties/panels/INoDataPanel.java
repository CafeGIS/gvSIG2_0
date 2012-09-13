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
package org.gvsig.rastertools.properties.panels;
/**
 * Interfaz que debe implementar el panel que quiere recibir los eventos del 
 * cambio de valores noData en el panel NoDataPanel.
 * 
 * @version 21/05/2008
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public interface INoDataPanel {
	/**
	 * Evento que se lanza cuando cambia el origen del NoDataValue.
	 * Posibles valores: General, Banda o personalizado
	 * @param noDataPanel
	 * @param newIndex
	 */
	public void sourceStateChanged(NoDataPanel noDataPanel, int newIndex);

	/**
	 * Evento que se lanza cuando cambia la seleccion de una banda en el combo
	 * @param noDataPanel
	 * @param newIndex
	 */
	public void bandStateChanged(NoDataPanel noDataPanel, int newIndex);
}
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
package org.gvsig.gui.beans.doubleslider;

import java.util.EventListener;
/**
 * Interfaz para los eventos del componente DoubleSlider. Existe ValueChanged
 * y ValueDragged, son los disparadores de los eventos, Changed para cuando
 * el evento a finalizado, y Dragged mientras esta transcurriendo.
 *
 * @version 07/08/2007
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public interface DoubleSliderListener extends EventListener {
	/**
	 * Evento que se dispara cuando cambia el valor del componente. Nunca
	 * se dispara mientras se hace un dragged con el rat�n.
	 * @param e
	 */
	public void actionValueChanged(DoubleSliderEvent e);

	/**
	 * Evento que se dispara cuando cambia el valor del componente mientras se
	 * hace un dragged con el rat�n.
	 * @param e
	 */
	public void actionValueDragged(DoubleSliderEvent e);
}
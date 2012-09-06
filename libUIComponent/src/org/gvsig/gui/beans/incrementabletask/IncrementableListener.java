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
package org.gvsig.gui.beans.incrementabletask;

import java.util.EventListener;

/**
 * Interfaz para la ventana IncrementableTask, aqui se define los
 * comportamientos de los botones de la ventana.
 *
 * @version 23/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public interface IncrementableListener extends EventListener {
	/**
	 * Invocado cuando se aprieta el boton Resumir de la ventana
	 * @param e
	 */
	public void actionResumed(IncrementableEvent e);

	/**
	 * Invocado cuando se aprieta el boton Suspender de la ventana
	 * @param e
	 */
	public void actionSuspended(IncrementableEvent e);

	/**
	 * Invocado cuando se aprieta el boton Cancelar de la ventana
	 * @param e
	 */
	public void actionCanceled(IncrementableEvent e);
}

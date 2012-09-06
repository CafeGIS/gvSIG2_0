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
package org.gvsig.gui.beans.propertiespanel;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.JPanel;
/**
 * Clase base que sirve para meter JPanels dentro de un PropertiesComponent
 *
 * @version 06/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class JPanelProperty extends JPanel {
	private static final long	serialVersionUID	= 3024638614874519828L;
	private ArrayList actionCommandListeners = new ArrayList();

	/**
	 * Añadir el disparador de cuando se pulsa un botón.
	 * @param listener
	 */
	public void addStateChangedListener(PropertiesComponentListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar el disparador de eventos de los botones.
	 * @param listener
	 */
	public void removeStateChangedListener(PropertiesComponentListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Metodo que se invoca para informar de que ha cambiado un estado
	 */
	protected void callStateChanged() {
		Iterator acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			PropertiesComponentListener listener = (PropertiesComponentListener) acIterator.next();
			listener.actionChangeProperties(new EventObject(this));
		}
	}
}
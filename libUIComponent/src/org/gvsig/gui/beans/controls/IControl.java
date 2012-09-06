/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

package org.gvsig.gui.beans.controls;

import java.awt.event.ActionListener;

/**
 * This interface models a control to be used in Andami.
 *
 * @author Cesar Martinez Izquierdo <cesar.martinez@iver.es>
 *
 */
public interface IControl {
	/**
	 *  Adds an ActionListener to the control
	 *
	 * @param listener
	 */
	public void addActionListener(ActionListener listener);

	/**
	 *  Removes an ActionListener from the control
	 *
	 * @param listener
	 */
	public void removeActionListener(ActionListener listener);

	/**
	 *  Sets the value of the control. It may mean different things for
	 *  different kinds of controls.
	 *
	 *  @return The value which was set, or null if the value was not valid
	 *  for this control
	 */
	public Object setValue(Object value);

	/**
	 * Gets the name of the control, used to identify it
	 * @return the name of the control
	 */
	public String getName();

	/**
	 * Sets the name of the control, used to identify it
	 *
	 * @param name
	 * @return
	 */
	public void setName(String name);
}

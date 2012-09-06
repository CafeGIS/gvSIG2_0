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
package org.gvsig.gui.beans.table.exceptions;
/**
 * Excepción que indica que una tabla no ha sido inicializada.
 *
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class NotInitializeException extends Exception {
	private static final long serialVersionUID = -6411813224550536884L;

	public NotInitializeException() {
		super();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param message
	 */
	public NotInitializeException(String message) {
		super(message);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param message
	 * @param cause
	 */
	public NotInitializeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param cause
	 */
	public NotInitializeException(Throwable cause) {
		super(cause);
	}
}
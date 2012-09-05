/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I   {{Task}}
*/

package org.gvsig;

import java.awt.Window;

public interface PrepareContext {
	/**
	 * Return a {@link java.awt.Dialog} or a {@link java.awt.Frame} to use like
	 * owner for any created dialogs.
	 * 
	 * @return <ul>
	 *         <li>null</li>
	 *         <li>{@link java.awt.Dialog} instance</li>
	 *         <li>{@link java.awt.Frame} instance</li>
	 *         </ul>
	 */
	Window getOwnerWindow();
}

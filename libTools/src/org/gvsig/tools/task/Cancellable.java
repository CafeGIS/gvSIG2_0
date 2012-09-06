/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 */
package org.gvsig.tools.task;

/**
 * <p><code>Cancellable</code> interface is designed for adding support to notify that a task must be cancelled.</p>
 * 
 * <p>This is useful if a process is hung up, blocking, or simply if user wants cancel it, for instance, a
 *  drawing process of a heavy layer.</p>
 * 
 * <p>The classes which implement this interface are force to share the canceling information, in order to be
 *  notified about the current status of it. This implies a synchronous canceling process, that could be implemented by
 *  another object.</p>
 */
public interface Cancellable {
	/**
	 * <p>Determines if the related task must be canceled.</p>
	 * 
	 * @return <code>true</code> if task must been canceled; otherwise <code>false</code>
	 * 
	 * @see #setCanceled(boolean)
	 */
	boolean isCanceled();

	/**
	 * <p>Sets if the related task must be canceled.</p>
	 * 
	 * @param canceled <code>true</code> if the task must be canceled; otherwise <code>false</code>
	 * 
	 * @see #isCanceled()
	 */
	void setCanceled(boolean canceled);
}

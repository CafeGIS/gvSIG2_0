package org.gvsig.tools.backup.exceptions;

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

import java.io.File;

/**
 * <p>Exception to report that a backup process has failed.</p>
 *
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class BackupException extends Exception {
	private static final long serialVersionUID = -2846421984617208883L;

	/**
	 * <p>The source file to be backup.</p>
	 */
	protected File source;

	/**
	 * <p>Constructs a new backup exception with the specified detail message and cause.</p>
	 * 
	 * @param message the detail message (which is saved for later retrieval by the <code>getMessage()</code> method).
	 * @param cause the cause (which is saved for later retrieval by the <code>getCause()</code> method). (A <code>null</code>
	 *  value is permitted, and indicates that the cause is nonexistent or unknown.)
	 * @param source the file from that was going to be done a backup
	 * 
	 * @see Exception#Exception(String, Throwable)
	 */
	public BackupException(String message, Throwable cause, File source) {
		super(message, cause);
		this.source = source;
	}

	/**
	 * <p>Gets the source file to be backup.</p> 
	 * 
	 * @return the source file
	 */
	public File getSource() {
		return source;
	}
}

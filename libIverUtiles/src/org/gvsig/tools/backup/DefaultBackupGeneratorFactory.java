package org.gvsig.tools.backup;

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

/**
 * <p>Factory that gets a {@link DefaultBackupGenerator DefaultBackupGenerator} as a particular version of
 *  backup generator.</p>
 *
 * @author Jose Ignacio Yarza (jiyarza@opensistemas.com)
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class DefaultBackupGeneratorFactory extends BackupGeneratorFactory {
	/*
	 * (non-Javadoc)
	 * @see com.iver.utiles.backup.BackupGeneratorFactory#getBackupGenerator()
	 */
	public BackupGenerator getBackupGenerator() {		
		return new DefaultBackupGenerator();
	}
}

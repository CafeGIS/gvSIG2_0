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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import org.gvsig.tools.backup.exceptions.BackupException;


/**
 * <p>Performs a backup of a file, into another file at the same path (directory), with the file extension 
 *  changed to <i>.bak</i>.</p>
 *
 * @author Jose Ignacio Yarza (jiyarza@opensistemas.com)
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class DefaultBackupGenerator implements BackupGenerator {
	/*
	 * (non-Javadoc)
	 * @see com.iver.utiles.backup.BackupGenerator#backup(java.io.File)
	 */
	public void backup(File source) throws BackupException {
		try {
			int index = source.getAbsolutePath().lastIndexOf(".");

			if (index == -1)
				return;

			File dest = new File(source.getAbsolutePath().substring(0, index) + ".bak");

	        // Create channel on the source
	        FileChannel srcChannel = new FileInputStream(source).getChannel();

	        // Create channel on the destination
	        FileChannel dstChannel = new FileOutputStream(dest).getChannel();

	        // Copy file contents from source to destination
	        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

	        // Close the channels
	        srcChannel.close();
	        dstChannel.close();
	    } catch (Exception ex) {
	    	throw new BackupException(ex.getMessage(), ex, source);
	    }
	}
}

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
* 2008 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.resource.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.MessageFormat;

import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.resource.ResourceParameters;
import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.resource.exception.PrepareResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyOpenException;
import org.gvsig.fmap.dal.resource.spi.AbstractResource;

/**
 * @author jmvivo
 *
 */
public class FileResource extends AbstractResource {

	final public static String NAME = "file";
	final public static String DESCRIPTION = "File of filesystem";

	private File file;

	public FileResource(FileResourceParameters params)
			throws InitializeException {
		super(params);
		file = null;
	}

	public Object get() throws AccessResourceException {
		try {
			prepare();
		} catch (PrepareResourceException e) {
			throw new AccessResourceException(this, e);
		}
		return ((FileResourceParameters)getParameters()).getFileName();
	}

	public String getFileName() throws AccessResourceException {
		return (String) get();
	}

	public File getFile() throws AccessResourceException {
		if (file == null) {
			file = new File((String) get());
		}
		return file;
	}

	// TODO ???
	public FileInputStream getFileInputStream() throws AccessResourceException,
			FileNotFoundException {
		FileInputStream fis = new FileInputStream(getFile());
		try {
			notifyOpen();
		} catch (ResourceNotifyOpenException e) {
			throw new AccessResourceException(this, e);
		}
		return fis;
	}

	public String getName() throws AccessResourceException {
		return MessageFormat.format("FileResource({0})",
				new Object[] { getFileName() });
	}

	public boolean isThis(ResourceParameters parameters)
			throws ResourceException {
		if (!(parameters instanceof FileResourceParameters)) {
			return false;
		}
		FileResourceParameters params;
		params = (FileResourceParameters) parameters.getCopy();
		prepare(params);
		return params.getFileName().equals(this.getFileName());

	}

}


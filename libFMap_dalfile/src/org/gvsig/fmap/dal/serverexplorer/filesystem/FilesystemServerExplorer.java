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
 * 2008 IVER T.I. S.A.   {{Task}}
 */

package org.gvsig.fmap.dal.serverexplorer.filesystem;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.FileNotFoundException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;

public interface FilesystemServerExplorer extends DataServerExplorer {

	public static final String NAME = "FilesystemExplorer";

	public abstract DataServerExplorerParameters getParameters();

	public abstract void dispose() throws DataException;

	public abstract List list() throws DataException;

	public abstract void setCurrentPath(File path) throws FileNotFoundException;

	public abstract File getCurrentPath();

	public abstract File getRoot();

	public abstract void remove(DataStoreParameters dsp) throws RemoveException;

	public abstract boolean add(NewDataStoreParameters ndsp, boolean overwrite)
			throws DataException;

	public abstract boolean canAdd();

	public abstract String getName();

	public abstract NewDataStoreParameters getAddParameters(String storeName)
			throws DataException;

	public abstract boolean canAdd(String storeName) throws DataException;

	public abstract DataStoreParameters getParametersFor(File file)
			throws DataException;

	public abstract NewDataStoreParameters getAddParameters(File file)
		throws DataException;

	public abstract Iterator getFilters();

	public abstract FilesystemFileFilter getGenericFilter();

	public abstract DataStore open(File file) throws DataException,
			ValidateDataParametersException;
}
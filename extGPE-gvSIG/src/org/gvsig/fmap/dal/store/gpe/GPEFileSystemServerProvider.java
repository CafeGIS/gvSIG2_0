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
 * 2009 {Iver T.I.}   {Task}
 */

package org.gvsig.fmap.dal.store.gpe;

import java.io.File;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.CreateException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProviderServices;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class GPEFileSystemServerProvider implements FilesystemServerExplorerProvider,
ResourceConsumer {
	protected FilesystemServerExplorerProviderServices serverExplorer;

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider#canCreate()
	 */
	public boolean canCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider#canCreate(org.gvsig.fmap.dal.NewDataStoreParameters)
	 */
	public boolean canCreate(NewDataStoreParameters parameters) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider#create(org.gvsig.fmap.dal.NewDataStoreParameters, boolean)
	 */
	public void create(NewDataStoreParameters parameters, boolean overwrite)
	throws CreateException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider#getCreateParameters()
	 */
	public NewDataStoreParameters getCreateParameters() throws DataException {
		return (NewFeatureStoreParameters) DALLocator.getDataManager()
		.createStoreParameters(this.getDataStoreProviderName());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider#initialize(org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProviderServices)
	 */
	public void initialize(
			FilesystemServerExplorerProviderServices serverExplorer) {
		this.serverExplorer = serverExplorer;		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider#remove(org.gvsig.fmap.dal.DataStoreParameters)
	 */
	public void remove(DataStoreParameters parameters) throws RemoveException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemFileFilter#getDataStoreProviderName()
	 */
	public String getDataStoreProviderName() {
		return GPEStoreProvider.NAME;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemFileFilter#getDescription()
	 */
	public String getDescription() {
		return GPEStoreProvider.DESCRIPTION;
	}

	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File pathName) {
		return pathName.getName().toLowerCase().endsWith(".gml");
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.resource.spi.ResourceConsumer#closeResourceRequested(org.gvsig.fmap.dal.resource.spi.ResourceProvider)
	 */
	public boolean closeResourceRequested(ResourceProvider resource) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.resource.spi.ResourceConsumer#resourceChanged(org.gvsig.fmap.dal.resource.spi.ResourceProvider)
	 */
	public void resourceChanged(ResourceProvider resource) {
		// TODO Auto-generated method stub

	}

}


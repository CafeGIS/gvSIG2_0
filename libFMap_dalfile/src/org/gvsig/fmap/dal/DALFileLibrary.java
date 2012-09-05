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
* 2008 {{Company}}   {{Task}}
*/


package org.gvsig.fmap.dal;

import org.gvsig.fmap.dal.resource.file.FileResource;
import org.gvsig.fmap.dal.resource.file.FileResourceParameters;
import org.gvsig.fmap.dal.resource.spi.ResourceManagerProviderServices;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.impl.DefaultFilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.impl.DefaultFilesystemServerExplorerManager;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;


public class DALFileLibrary extends BaseLibrary {

    public void initialize() throws ReferenceNotRegisteredException {
    	super.initialize();

    	DALLibrary dal = new DALLibrary();
    	dal.initialize();
    }

    public void postInitialize() throws ReferenceNotRegisteredException {     
    	DataManagerProviderServices dataman = (DataManagerProviderServices) DALLocator.getDataManager();

    	ResourceManagerProviderServices resman = (ResourceManagerProviderServices) DALLocator.getResourceManager();

    	if (!resman.getResourceProviders().contains(FileResource.NAME)) {
			resman.register(FileResource.NAME, FileResource.DESCRIPTION,
					FileResource.class, FileResourceParameters.class);
		}

    	if (!dataman.getExplorerProviders().contains(FilesystemServerExplorer.NAME)) {
			dataman.registerExplorerProvider(FilesystemServerExplorer.NAME,
					DefaultFilesystemServerExplorer.class,
					FilesystemServerExplorerParameters.class);
			DALFileLocator
					.registerFilesystemSeverExplorerManager(DefaultFilesystemServerExplorerManager.class);
    	}

    }
}


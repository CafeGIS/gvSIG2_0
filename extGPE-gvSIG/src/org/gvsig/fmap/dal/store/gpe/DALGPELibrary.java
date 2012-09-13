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

import org.gvsig.fmap.dal.DALFileLocator;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.gpe.gml.impl.DefaultGmlLibrary;
import org.gvsig.gpe.impl.DefaultGPELibrary;
import org.gvsig.gpe.kml.impl.DefaultKmlLibrary;
import org.gvsig.gpe.xml.impl.DefaultXmlLibrary;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DALGPELibrary extends BaseLibrary {
	private DefaultGmlLibrary gmlLib = null;
	private DefaultKmlLibrary kmlLib = null;
	private DefaultGPELibrary gpeLib = null;
	private DefaultXmlLibrary xmlLib = null;
	
	public void initialize() throws ReferenceNotRegisteredException {
		super.initialize();

		DALLibrary dal = new DALLibrary();
		dal.initialize();
		
		gpeLib = new DefaultGPELibrary();
		gpeLib.initialize();
		
		xmlLib = new DefaultXmlLibrary();
		xmlLib.initialize();
		
		gmlLib = new DefaultGmlLibrary();
		gmlLib.initialize();
		
		kmlLib = new DefaultKmlLibrary();
		kmlLib.initialize();
	}

	public void postInitialize() throws ReferenceNotRegisteredException {     
		super.postInitialize();

		gpeLib.postInitialize();
		xmlLib.postInitialize();
		gmlLib.postInitialize();
		kmlLib.postInitialize();
		
		GPEStoreParameters.registerDynClass();
		GPEStoreProvider.registerDynClass();

		//
		DataManagerProviderServices dataman = (DataManagerProviderServices) DALLocator
		.getDataManager();

		if (!dataman.getStoreProviders().contains(GPEStoreProvider.NAME)) {
			dataman.registerStoreProvider(GPEStoreProvider.NAME,
					GPEStoreProvider.class, GPEStoreParameters.class);
		}
		
		DALFileLocator.getFilesystemServerExplorerManager().registerProvider(
				GPEStoreProvider.NAME, GPEStoreProvider.DESCRIPTION,
				GPEFileSystemServerProvider.class);
	}
}




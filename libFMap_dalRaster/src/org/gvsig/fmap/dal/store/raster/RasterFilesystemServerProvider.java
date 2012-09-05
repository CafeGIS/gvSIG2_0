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

package org.gvsig.fmap.dal.store.raster;

import java.io.File;
import java.io.FileInputStream;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.CreateException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProvider;
import org.gvsig.fmap.dal.serverexplorer.filesystem.spi.FilesystemServerExplorerProviderServices;
import org.gvsig.raster.dataset.RasterDataset;

public class RasterFilesystemServerProvider implements
		FilesystemServerExplorerProvider {

	public boolean canCreate() {
		return false;
	}

	public boolean canCreate(NewDataStoreParameters parameters) {
		return false;
	}

	public void create(NewDataStoreParameters parameters, boolean overwrite)
			throws CreateException {
		throw new UnsupportedOperationException();
	}

	public NewDataStoreParameters getCreateParameters() throws DataException {
		return null;
	}

	public void initialize(
			FilesystemServerExplorerProviderServices serverExplorer) {
		// TODO Auto-generated method stub

	}

	public void remove(DataStoreParameters parameters) throws RemoveException {
		// FIXME
		throw new UnsupportedOperationException();

	}

	protected FilesystemServerExplorerProviderServices serverExplorer;

	public String getDataStoreProviderName() {
		return RasterStoreProvider.NAME;
	}

	public boolean accept(File pathname) {


		if (pathname.getParentFile().getName().equals("cellhd")) {
			if (pathname.getName().endsWith(".rmf")
					|| pathname.getName().endsWith(".rmf~")) {
				return false;
			}
			return true;
		}

		// Comprobamos que no sea un rmf propio, osea, que contenga xml
		if (pathname.getName().toLowerCase().endsWith(".rmf")) {
			FileInputStream reader = null;
			try {
				reader = new FileInputStream(pathname);
				String xml = "";
				for (int i = 0; i < 6; i++) {
					xml += (char) reader.read();
				}
				if (xml.equals("<?xml ")) {
					return false;
				}
			} catch (Exception e) {
			} finally {
				try {
					reader.close();
				} catch (Exception e) {
				}
			}
		}
		return RasterDataset.fileIsSupported(pathname.getName());

	}

	public String getDescription() {
		return RasterStoreProvider.DESCRIPTION;
	}

	public DataStoreParameters getParameters(File file) throws DataException {
		DataManager manager = DALLocator.getDataManager();
		RasterStoreParameters params = (RasterStoreParameters) manager
				.createStoreParameters(this.getDataStoreProviderName());
		params.setFile(file);
		return params;
	}

}

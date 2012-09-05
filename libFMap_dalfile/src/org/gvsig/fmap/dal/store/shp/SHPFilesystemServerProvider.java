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

/**
 *
 */
package org.gvsig.fmap.dal.store.shp;

import java.io.File;
import java.io.IOException;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.CreateException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.resource.exception.ResourceBeginException;
import org.gvsig.fmap.dal.resource.exception.ResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyChangesException;
import org.gvsig.fmap.dal.resource.file.FileResource;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.store.dbf.DBFFilesystemServerProvider;

/**
 * @author jmvivo
 *
 */
public class SHPFilesystemServerProvider extends DBFFilesystemServerProvider {

	public boolean accept(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".shp");
	}

	public boolean canCreate() {
		return true;
	}

	private FileResource[] createResources(File[] files)
			throws InitializeException {
		FileResource[] result = new FileResource[files.length];
		for (int i = 0; i < files.length; i++) {
			result[i] = this.createResource(files[i]);
		}

		return result;
	}



	private FileResource createResource(File file) throws InitializeException {
		FileResource resource;
		resource = (FileResource) this.serverExplorer
				.getServerExplorerProviderServices().createResource(
						FileResource.NAME,
						new Object[] { file.getAbsolutePath() });
		resource.addConsumer(this);
		return resource;
	}

	public boolean canCreate(NewDataStoreParameters parameters) {
		if (!super.canCreate(parameters)) {
			return false;
		}
		SHPNewStoreParameters params = (SHPNewStoreParameters) parameters;
		if (params.getSHPFile().getParentFile().canWrite()) {
			return false;
		}
		if (params.getSHPFile().exists()) {
			if (!params.getSHPFile().canWrite()) {
				return false;
			}
		}
		if (params.getSHXFile().getParentFile().canWrite()) {
			return false;
		}
		if (params.getSHXFile().exists()) {
			if (!params.getSHXFile().canWrite()) {
				return false;
			}
		}
		return true;
	}

	public boolean closeResourceRequested(ResourceProvider resource) {
		// while it is using a resource anyone can't close it
		return false;
	}

	public void create(NewDataStoreParameters parameters, boolean overwrite)
	throws CreateException {

		SHPNewStoreParameters params = (SHPNewStoreParameters) parameters;
		FeatureType fType = params.getDefaultFeatureType();
		// TODO Comprobar que el campo de geometria
		EditableFeatureType dbfFtype;
		if (fType instanceof EditableFeatureType){
			dbfFtype = (EditableFeatureType) fType.getCopy();
		} else{
			dbfFtype = fType.getEditable();
			params.setDefaultFeatureType(fType.getEditable());
			fType = params.getDefaultFeatureType();
		}

		SHPStoreProvider.removeGeometryColumn(dbfFtype);
		SHPStoreProvider.addGeometryColumn((EditableFeatureType) fType);
		File dbfFile = params.getDBFFile();
		File shpFile = params.getSHPFile();
		File shxFile = params.getSHXFile();

		File[] files = new File[] {
				shpFile,
				shxFile,
				dbfFile
		};
		// TODO .prj file

		FileResource[] resources;
		try {
			resources = this.createResources(files);
			closeResources(resources);
		} catch (DataException e1) {
			throw new CreateException(shpFile.getPath(), e1);
		}

		if (shpFile.exists()) {
			if (overwrite) {
			// FIXME
//				if (!shpFile.delete()) {
//					throw new CreateException(this.getDataStoreProviderName(), new IOException(
//							"cannot delete file: " + shpFile.getPath()));
//				}
//				if (shxFile.exists()) {
//					if (!shxFile.delete()) {
//						throw new CreateException(this.getDataStoreProviderName(),
//								new IOException("cannot delete file: "
//										+ shxFile.getPath()));
//					}
//
//				}
			} else {
				throw new CreateException(this.getDataStoreProviderName(),
						new IOException(
						"file already exist"));
			}
		}




		SHPFeatureWriter writer = null;
		try {
			beginResources(resources);

			writer = new SHPFeatureWriter(this.getDataStoreProviderName());

			writer.begin(params, fType, dbfFtype, 0);

			writer.end();

			notifyChangesResources(resources);
		} catch (Exception e) {
			throw new CreateException(this.getDataStoreProviderName(), e);
		} finally {
			endResources(resources);
			removeConsumer(resources);
		}
	}


	private boolean closeResources(FileResource[] resources)
			throws ResourceException {
		for (int i = 0; i < resources.length; i++) {
			// TODO
			// if (!resources[i].closeRequest()){
			// return false;
			// }
			resources[i].closeRequest();
		}
		return true;

	}

	private void removeConsumer(FileResource[] resources) {
		for (int i = 0; i < resources.length; i++) {
			resources[i].removeConsumer(this);
		}
	}

	private void endResources(FileResource[] resources) {
		for (int i = 0; i < resources.length; i++) {
			resources[i].end();
		}
	}

	private void notifyChangesResources(FileResource[] resources)
			throws ResourceNotifyChangesException {
		for (int i = 0; i < resources.length; i++) {
			resources[i].notifyChanges();
		}

	}

	private void beginResources(FileResource[] resources)
			throws ResourceBeginException {
		for (int i = 0; i < resources.length; i++) {
			resources[i].begin();
		}

	}

	public NewDataStoreParameters getCreateParameters() {
		SHPNewStoreParameters params = (SHPNewStoreParameters) super
				.getCreateParameters();

		EditableFeatureType fType = (EditableFeatureType) params
				.getDefaultFeatureType();
		//		SHPStoreProvider.addGeometryColumn(fType);
		//		params.setDefaultFeatureType(fType);
		return params;
	}

	protected NewDataStoreParameters createInstanceNewDataStoreParameters() {
		return new SHPNewStoreParameters();
	}

	public String getDataStoreProviderName() {
		return SHPStoreProvider.NAME;
	}

	public String getDescription() {
		return SHPStoreProvider.DESCRIPTION;
	}


	public DataStoreParameters getParameters(File file) throws DataException {
		DataManager manager = DALLocator.getDataManager();
		SHPStoreParameters params = (SHPStoreParameters) manager
				.createStoreParameters(this.getDataStoreProviderName());
		params.setSHPFileName(file.getPath());
		return params;

	}

	public void remove(DataStoreParameters parameters) throws RemoveException {
		SHPStoreParameters params = (SHPStoreParameters) parameters;

		File dbfFile = params.getDBFFile();
		File shpFile = params.getSHPFile();
		File shxFile = params.getSHXFile();
		// TODO .prj file

		File[] files = new File[] { shpFile, shxFile, dbfFile };

		FileResource[] resources;
		try {
			resources = this.createResources(files);
			closeResources(resources);
		} catch (DataException e1) {
			throw new RemoveException(shpFile.getPath(), e1);
		}

		try {
			beginResources(resources);
			for (int i = 0; i < files.length; i++) {
				if (!files[i].exists()) {
					continue;
				}
				if (!files[i].delete()) {
					throw new RemoveException(this.getDataStoreProviderName(),
							new IOException()); // FIXME Exception
				}
			}


			notifyChangesResources(resources);
		} catch (Exception e) {
			throw new RemoveException(this.getDataStoreProviderName(), e);
		} finally {
			endResources(resources);
			removeConsumer(resources);
		}
	}

}

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

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class GPEStoreParameters extends AbstractDataParameters implements
DataStoreParameters, FilesystemStoreParameters {
	protected static final String FIELD_FILENAME = "filename";
	public static final String DYNCLASS_NAME = "GPEStoreParameters";
	protected static DynClass DYNCLASS = null;
	
	public GPEStoreParameters() {
		super();
		initialize();
	}

	protected void initialize() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager().createDynObject(
						GPEStoreParameters.DYNCLASS);
	}
	
	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStoreParameters#getDataStoreName()
	 */
	public String getDataStoreName() {
		return GPEStoreProvider.NAME;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStoreParameters#getDescription()
	 */
	public String getDescription() {
		return GPEStoreProvider.DESCRIPTION;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStoreParameters#isValid()
	 */
	public boolean isValid() {
		return (this.getFileName() != null);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters#getFile()
	 */
	public File getFile() {
		return new File(this.getFileName());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters#setFile(java.io.File)
	 */
	public void setFile(File file) {
		this.setDynValue(FIELD_FILENAME, file.getPath());	
	}	
	
	
	public String getFileName() {
		return (String) this.getDynValue(FIELD_FILENAME);
	}
	
	public void setFileName(String aFileName) {
		this.setDynValue(FIELD_FILENAME, aFileName);
	}
	
	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME);
			field = dynClass.addDynField(FIELD_FILENAME);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("GPE file name");
			field.setType(DataTypes.STRING);
			field.setMandatory(true);
			DYNCLASS = dynClass;
		}

	}

}


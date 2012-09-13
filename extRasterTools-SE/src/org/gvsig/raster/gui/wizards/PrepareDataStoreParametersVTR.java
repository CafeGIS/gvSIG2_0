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

/**
 *
 */
package org.gvsig.raster.gui.wizards;

import java.io.File;
import java.util.Map;

import org.gvsig.PrepareContext;
import org.gvsig.PrepareDataStoreParameters;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.store.raster.RasterStoreParameters;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.rastertools.raw.ui.main.OpenRawFileDefaultView;

/**
 * @author jmvivo
 *
 */
public class PrepareDataStoreParametersVTR implements
		PrepareDataStoreParameters {

	/* (non-Javadoc)
	 * @see org.gvsig.PrepareDataStoreParameters#prepare(org.gvsig.fmap.dal.DataStoreParameters, org.gvsig.PrepareDataStoreContext)
	 */
	public DataStoreParameters prepare(DataStoreParameters storeParamters,
			PrepareContext context) {
		if (!(storeParamters instanceof RasterStoreParameters))
			return storeParamters;
		RasterStoreParameters rasterParams = (RasterStoreParameters) storeParamters;
		File file = rasterParams.getFile();
		if (RasterUtilities.getExtensionFromFileName(file.getAbsolutePath()).equals("raw")) {
			OpenRawFileDefaultView view = new OpenRawFileDefaultView(file.getAbsolutePath());
 			file = view.getImageFile();
		}
		if (file == null)
			return null;
		rasterParams.setFile(file);
		return rasterParams;
	}

	public String getDescription() {
		return "Prepare Raster VTR parameters";
	}

	public String getName() {
		return "PrepareVTR";
	}

	public Object create() {
		return this;
	}

	public Object create(Object[] args) {
		return this;
	}

	public Object create(Map args) {
		return this;
	}

}

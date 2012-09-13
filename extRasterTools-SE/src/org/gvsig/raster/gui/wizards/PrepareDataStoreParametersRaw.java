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
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.gvsig.PrepareContext;
import org.gvsig.PrepareDataStoreParameters;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.store.raster.RasterStoreParameters;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.iver.andami.PluginServices;

/**
 * @author jmvivo
 *
 */
public class PrepareDataStoreParametersRaw implements
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
		if (RasterUtilities.getExtensionFromFileName(file.getAbsolutePath())
				.equals("vrt"))
			try {
				checkFileVRT(file);
			} catch (FileOpenVRTException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_abrir_fichero") + " " + file.getName() + "\n\n" + PluginServices.getText(this, "informacion_adicional") + ":\n\n  " + e.getMessage(), this, e);
				return null;
			}

		if (file == null)
			return null;
		rasterParams.setFile(file);
		return rasterParams;
	}

	/**
	 * Comprueba si un fichero VRT esta en correcto estado, en caso contrario
	 * lanza una excepcion indicando el tipo de error en la apertura.
	 *
	 * @param file
	 * @throws FileOpenVRTException
	 */
	private void checkFileVRT(File file) throws FileOpenVRTException {
		KXmlParser parser = new KXmlParser();

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
			parser.setInput(fileReader);

			parser.nextTag();

			parser.require(XmlPullParser.START_TAG, null, "VRTDataset");

			while (parser.nextTag() != XmlPullParser.END_TAG) {
				parser.require(XmlPullParser.START_TAG, null, "VRTRasterBand");

				String name;
				while (parser.nextTag() != XmlPullParser.END_TAG) {
					parser.require(XmlPullParser.START_TAG, null, null);
					boolean relativePath = false;
					for (int i = 0; i < parser.getAttributeCount(); i++)
						if (parser.getAttributeName(i).equals("relativetoVRT")
								&& parser.getAttributeValue(i).equals("1"))
							relativePath = true;
					name = parser.getName();
					String nameFile = parser.nextText();
					if (name.equals("SourceFilename")) {
						if (relativePath)
							nameFile = file.getParent() + File.separator
									+ nameFile;
						File tryFile = new File(nameFile);
						if (!tryFile.exists())
							throw new FileOpenVRTException(PluginServices
									.getText(this, "no_existe_fichero")
									+ " " + nameFile);
					}
					parser.require(XmlPullParser.END_TAG, null, name);
				}

				parser.require(XmlPullParser.END_TAG, null, "VRTRasterBand");
			}
			parser.require(XmlPullParser.END_TAG, null, "VRTDataset");
			parser.next();
			parser.require(XmlPullParser.END_DOCUMENT, null, null);
		} catch (XmlPullParserException e) {
			throw new FileOpenVRTException(PluginServices.getText(this,
					"el_fichero")
					+ " "
					+ file.getName().toString()
					+ " "
					+ PluginServices.getText(this, "esta_formato_desconocido"));
		} catch (IOException e) {
			throw new FileOpenVRTException(PluginServices.getText(this,
					"no_puede_abrir_fichero")
					+ " " + file.getName().toString());
		} finally {
			if (fileReader != null)
				try {
					fileReader.close();
				} catch (IOException e) {
				}
		}
	}

	public String getDescription() {
		return "Prepare Raster Raw Stores";
	}

	public String getName() {
		return "PrepareRaw";
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

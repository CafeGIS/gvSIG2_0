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
 * 2009 Prodevelop S.L  vsanjaime   programador
 */

package org.gvsig.geocoding.utils;

import java.awt.Font;
import java.io.File;

import javax.swing.border.TitledBorder;

/**
 * Geocoding utilities
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class GeocodingUtils {

	/**
	 * Add to the file name the suffix .xml
	 * 
	 * @param file
	 * @return
	 */
	public static File addXMLSuffix(File file) {
		if (!(file.getPath().toLowerCase().endsWith(".xml"))) {
			return new File(file.getPath() + ".xml");
		}
		return file;
	}

	/**
	 * Add to the file name the suffix .dbf
	 * 
	 * @param file
	 * @return
	 */
	public static File addDBFSuffix(File file) {
		if (!(file.getPath().toLowerCase().endsWith(".dbf"))) {
			return new File(file.getPath() + ".dbf");
		}
		return file;
	}

	/**
	 * Add to the file name the suffix .dbf
	 * 
	 * @param file
	 * @return
	 */
	public static File addSuffixFile(File file, String suffix) {
		File nfile = new File(file.getPath() + "." + suffix);
		return nfile;
	}

	/**
	 * Add to the file name the suffix .dbf
	 * 
	 * @param file
	 * @return
	 */
	public static File addSHPSuffix(File file) {
		if (!(file.getPath().toLowerCase().endsWith(".shp"))) {
			return new File(file.getPath() + ".shp");
		}
		return file;
	}

	/**
	 * Delete to the file name the suffix .dbf
	 * 
	 * @param file
	 * @return
	 */
	public static File delSHPSuffix(File file) {
		if ((file.getPath().toLowerCase().endsWith(".shp"))) {
			String path = file.getPath();
			int npath = path.length();
			String subfile = file.getPath().substring(0, npath - 3);
			return new File(subfile);
		}
		return file;
	}

	/**
	 * @param name
	 *            of the layer
	 * @param font
	 *            for the border title
	 * @return a titled border for the layer name
	 */
	public static TitledBorder getTitledBorder(String name, Font titleFont) {
		return javax.swing.BorderFactory.createTitledBorder(null, name,
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, titleFont,
				null);
	}

	/**
	 * @param name
	 *            of the layer
	 * @return a titled border for the layer name
	 */
	public static TitledBorder getTitledBorder(String name) {
		return GeocodingUtils.getTitledBorder(name, null);
	}

	// /**
	// * Clear all rows of the TableModel
	// *
	// * @param model
	// */
	// public static void clearTableModel(DefaultTableModel model) {
	// int n = model.getRowCount();
	// for (int i = n - 1; i > -1; i--) {
	// model.removeRow(i);
	// }
	// }

}

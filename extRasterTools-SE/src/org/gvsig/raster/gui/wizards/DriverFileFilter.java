/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */
package org.gvsig.raster.gui.wizards;

import java.io.File;
import java.io.FileInputStream;

import javax.swing.filechooser.FileFilter;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
/**
 * Clase para indicar los ficheros que se verán en el JFileChooser.
 * 
 * @version 04/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class DriverFileFilter extends FileFilter {
	/*
	 * (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;

		if (f.getParentFile().getName().equals("cellhd")) {
			if (f.getName().endsWith(".rmf") || f.getName().endsWith(".rmf~"))
				return false;
			return true;
		}

		// Comprobamos que no sea un rmf propio, osea, que contenga xml
		if (f.getName().toLowerCase().endsWith(".rmf")) {
			FileInputStream reader = null;
			try {
				reader = new FileInputStream(f);
				String xml = "";
				for (int i = 0; i < 6; i++)
					xml += (char) reader.read();
				if (xml.equals("<?xml "))
					return false;
			} catch (Exception e) {
			} finally {
				try {
					reader.close();
				} catch (Exception e) {}
			}
		}

		return FLyrRasterSE.isFileAccepted(f);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return "gvSIG Raster Driver";
	}
}
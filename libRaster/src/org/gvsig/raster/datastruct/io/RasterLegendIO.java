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
package org.gvsig.raster.datastruct.io;

import java.io.File;
import java.io.IOException;

import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.io.exceptions.RasterLegendIONotFound;
import org.gvsig.raster.datastruct.io.formats.GimpGradients;
import org.gvsig.raster.datastruct.io.formats.GimpPalettes;
import org.gvsig.raster.datastruct.io.formats.LegendgvSIG;
/**
 * Clase de la que debe heredar todo aquel que quiera hacer una clase
 * de importación o exportación de una tabla de color.
 *
 * @version 20/11/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
abstract public class RasterLegendIO {
	private static String[] formats = {"rmf", "ggr", "gpl"};

	/**
	 * Lista de formatos soportados por RasterLegendIO
	 *
	 * @return
	 */
	public static String[] getFormats() {
		return formats;
	}

	/**
	 * Devuelve un RasterLegend para el formato especificado por parametro.
	 *
	 * @param formatName
	 * @return
	 * @throws RasterLegendIONotFound
	 */
	public static RasterLegendIO getRasterLegendIO(String formatName) throws RasterLegendIONotFound {
		if (formatName.equals(formats[0]))
			return new LegendgvSIG();

		if (formatName.equals(formats[1]))
			return new GimpGradients();

		if (formatName.equals(formats[2]))
			return new GimpPalettes();

		// Incluir mas formatos de la misma manera
		throw new RasterLegendIONotFound();
	}

	/**
	 * Devuelve la descripcion a mostrar en los paneles de JFileChooser.
	 * Si se devuelve null se mostrará la decripción por defecto.
	 */
	public String getDescription() {
		return null;
	}

	/**
	 * Lee una tabla de color del fichero pasado por parametro
	 *
	 * @param input
	 * @return
	 * @throws IOException cuando ha fallado la carga
	 */
	abstract public ColorTable read(File input) throws IOException;

	/**
	 * Guarda una tabla de color en el fichero pasado por parametro
	 *
	 * @param colorTable
	 * @param output
	 * @throws IOException cuando ha fallado la escritura
	 */
	abstract public void write(ColorTable colorTable, File output) throws IOException;
}
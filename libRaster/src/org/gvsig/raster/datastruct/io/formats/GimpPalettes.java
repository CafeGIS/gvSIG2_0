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
package org.gvsig.raster.datastruct.io.formats;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.io.RasterLegendIO;
/**
 * Clase GimpPalettes sirve para importar y exportar tablas de color de Gimp.
 * Los ficheros de Paletas de gimp tienen la extension .gpl
 *
 * @version 03/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class GimpPalettes extends RasterLegendIO {

	/**
	 * Devuelve el ColorItem asociado a una linea de un fichero de tablas de color
	 * de Gimp
	 * @param strings
	 * @param pos
	 * @return
	 */
	private ColorItem parseColorItem(String[] strings) {
		if (strings.length < 4)
			return null;

		ColorItem item = new ColorItem();

		item.setInterpolated(100);

		item.setColor(new Color(
				Integer.valueOf(strings[1]).intValue(),
				Integer.valueOf(strings[2]).intValue(),
				Integer.valueOf(strings[3]).intValue()
			));
		return item;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.datastruct.io.RasterLegendIO#read(java.io.File)
	 */
	public ColorTable read(File input) throws IOException {
		ArrayList colorItems = new ArrayList();
		ColorTable colorTable = new ColorTable();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(input));
			String currentLine;
			int cont = 0;
			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.charAt(0) == '#')
					continue;
				if (cont == 1)
					colorTable.setName(currentLine.substring(6));
				if (cont > 1) {
					String[] strings = (" " + currentLine).split("\\s+");

					ColorItem colorItem = parseColorItem(strings);
					if (colorItem == null)
						continue;

					strings = (" " + currentLine).split("\\s+\\d+\\s+\\d+\\s+\\d+\\s+");

					if (!strings[1].equals("Untitled"))
						colorItem.setNameClass(strings[1]);
					else
						colorItem.setNameClass("");

					colorItem.setValue(cont - 2);
					colorItems.add(colorItem);
				}
				cont++;
			}
			reader.close();
		} catch (IOException ex) {
			throw new IOException();
		}

		colorTable.createPaletteFromColorItems(colorItems, false);
		return colorTable;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.datastruct.io.RasterLegendIO#write(org.gvsig.raster.datastruct.ColorTable, java.io.File)
	 */
	public void write(ColorTable colorTable, File output) throws IOException {
		ArrayList colorItems = colorTable.getColorItems();

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			writer.write("GIMP Palette\n");
			writer.write("Name: " + colorTable.getName() + "\n");
			writer.write("#\n");

			for (int i = 0; i < colorItems.size(); i++) {
				String line = "";
				ColorItem item1 = (ColorItem) colorItems.get(i);
				Color color = item1.getColor();
				line += color.getRed() + " ";
				line += color.getGreen() + " ";
				line += color.getBlue() + "\t";
				if ((item1.getNameClass() != null) && (item1.getNameClass().length() > 0))
					line += item1.getNameClass() + "\n";
				else
					line += "Untitled\n";
				writer.write(line);
			}
			writer.close();
		} catch (IOException ex) {
			throw new IOException();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.datastruct.io.RasterLegendIO#getDescription()
	 */
	public String getDescription() {
		return "Gimp Palettes";
	}
}
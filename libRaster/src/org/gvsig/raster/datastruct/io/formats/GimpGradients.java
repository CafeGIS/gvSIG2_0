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
import java.text.NumberFormat;
import java.util.ArrayList;

import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.io.RasterLegendIO;
/**
 * Clase GimpGradients sirve para importar y exportar gradientes de Gimp
 * Los ficheros de gradientes de gimp tienen la extension .ggr
 *
 * @version 03/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class GimpGradients extends RasterLegendIO {

	/**
	 * Devuelve el ColorItem asociado a una linea de un fichero de gradientes de
	 * Gimp
	 * @param strings
	 * @param pos
	 * @return
	 */
	private ColorItem parseColorItem(String[] strings, int pos) {
		ColorItem item = new ColorItem();

		double pos1 = Double.valueOf(strings[0]).doubleValue();
		double pos2 = Double.valueOf(strings[1]).doubleValue();
		double pos3 = Double.valueOf(strings[2]).doubleValue();
		if (pos == 0)
			item.setValue(pos1 * 255);
		else
			item.setValue(pos3 * 255);
		if (pos == 0)
			item.setInterpolated((100 * (pos2 - pos1)) / (pos3 - pos1));
		if (pos == 0)
			item.setColor(new Color(
				(int) (Double.valueOf(strings[3]).doubleValue() * 255.0),
				(int) (Double.valueOf(strings[4]).doubleValue() * 255.0),
				(int) (Double.valueOf(strings[5]).doubleValue() * 255.0),
				(int) (Double.valueOf(strings[6]).doubleValue() * 255.0)
				));
		else
			item.setColor(new Color(
					(int) (Double.valueOf(strings[7]).doubleValue() * 255.0),
					(int) (Double.valueOf(strings[8]).doubleValue() * 255.0),
					(int) (Double.valueOf(strings[9]).doubleValue() * 255.0),
					(int) (Double.valueOf(strings[10]).doubleValue() * 255.0)
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
				if (cont == 1)
					colorTable.setName(currentLine.substring(6));

				if (cont > 1) {
					String[] strings = currentLine.split("\\s+");

					if (strings.length < 13)
						continue;

					colorItems.add(parseColorItem(strings, 0));
					colorItems.add(parseColorItem(strings, 1));
				}
				cont++;
			}
			reader.close();
		} catch (IOException ex) {
			throw new IOException();
		}

		colorTable.createPaletteFromColorItems(colorItems, true);
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
			writer.write("GIMP Gradient\n");
			writer.write("Name: " + colorTable.getName() + "\n");
			writer.write((colorItems.size() - 1) + "\n");
			String line;

			for (int i = 1; i < colorItems.size(); i++) {
				double min = ((ColorItem) colorItems.get(0)).getValue();
				double max = ((ColorItem) colorItems.get(colorItems.size() - 1)).getValue();

				line = "";
				ColorItem item1 = (ColorItem) colorItems.get(i - 1);
				ColorItem item2 = (ColorItem) colorItems.get(i);

				double pos1 = (item1.getValue() - min) / (max - min);
				double pos2 = (item2.getValue() - min) / (max - min);

				NumberFormat format = NumberFormat.getNumberInstance();
				format.setMaximumFractionDigits(6);
				format.setMinimumFractionDigits(6);
				line += format.format(pos1).replaceAll(",", ".") + " ";
				double interp = pos1 + (((pos2 - pos1) * item1.getInterpolated()) / 100.0);
				line += format.format(interp).replaceAll(",", ".") + " ";
				line += format.format(pos2).replaceAll(",", ".") + " ";

				Color color = item1.getColor();
				line += format.format(color.getRed() / 255.0).replaceAll(",", ".") + " ";
				line += format.format(color.getGreen() / 255.0).replaceAll(",", ".") + " ";
				line += format.format(color.getBlue() / 255.0).replaceAll(",", ".") + " ";
				line += format.format(color.getAlpha() / 255.0).replaceAll(",", ".") + " ";

				color = item2.getColor();
				line += format.format(color.getRed() / 255.0).replaceAll(",", ".") + " ";
				line += format.format(color.getGreen() / 255.0).replaceAll(",", ".") + " ";
				line += format.format(color.getBlue() / 255.0).replaceAll(",", ".") + " ";
				line += format.format(color.getAlpha() / 255.0).replaceAll(",", ".") + " ";

				line += "0 0\n";
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
		return "Gimp Gradients";
	}
}
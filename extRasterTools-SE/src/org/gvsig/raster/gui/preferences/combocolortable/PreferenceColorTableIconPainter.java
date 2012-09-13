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
package org.gvsig.raster.gui.preferences.combocolortable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.gvsig.gui.beans.listview.IIconPaint;
import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.datastruct.ColorTable;
/**
 * Clase para dibujar los iconos del ListViewComponent del panel de color. Se
 * puede indicar si la paleta esta seleccionada y si se dibuja con
 * interpolaciones.
 *
 * @version 29/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PreferenceColorTableIconPainter implements IIconPaint {
	private ColorTable colorTable;

	/**
	 * Construye un ColorTablePaint con una tabla de color pasada por parametro
	 * @param colorTable
	 */
	public PreferenceColorTableIconPainter(ColorTable colorTable) {
		this.colorTable = colorTable;
	}

	/**
	 * Define si los valores estan interpolados o no entre si
	 * @param value
	 */
	public void setInterpolated(boolean value) {
		colorTable.setInterpolated(value);
	}

	/**
	 * Obtiene el array de los colores de la paleta de color
	 * @return
	 */
	public ArrayList getColorItems() {
		return colorTable.getColorItems();
	}

	/**
	 * Obtiene el ColorTable
	 * @return
	 */
	public ColorTable getColorTable() {
		return colorTable;
	}

	/**
	 * Especificar los colores de la tabla de color, definiendo si estan los
	 * valores interpolados y si la paleta se comprimira o no.
	 * @param value
	 * @param interpolated
	 * @param compress
	 */
	public void setColorItems(ArrayList value, boolean interpolated, boolean compress) {
		colorTable.createPaletteFromColorItems(value, compress);
		setInterpolated(interpolated);
	}

	/**
	 * Metodo de pintado de la tabla de color
	 * @param g
	 * @param isSelected
	 */
	public void paint(Graphics2D g, boolean isSelected) {
		Rectangle area = g.getClipBounds();

		int x1 = area.x;
		int x2 = area.x + area.width - 1;

		Color bgColor = new Color(224, 224, 224);
		for (int i = 0; (i * 4) <= area.width; i++) {
			for (int j = 0; (j * 4) <= area.height; j++) {
				if ((i + j) % 2 == 0)
					g.setColor(Color.white);
				else
					g.setColor(bgColor);
				g.fillRect(area.x + 1 + i * 4, area.y + 1 + j * 4, 4, 4);
			}
		}

		if (colorTable.getColorItems().size() >= 1) {
			double min = ((ColorItem) colorTable.getColorItems().get(0)).getValue();
			double max = ((ColorItem) colorTable.getColorItems().get(colorTable.getColorItems().size() - 1)).getValue();
			for (int i = area.x; i < (area.x + area.width); i++) {
				double pos = min + (((max - min) * (i - area.x)) / (area.width - 2));

				byte[] col3 = colorTable.getRGBAByBand(pos);
				g.setColor(new Color(col3[0] & 0xff, col3[1] & 0xff, col3[2] & 0xff, col3[3] & 0xff));
				g.drawLine(i, area.y, i, area.y + area.height - 2);
			}
		} else {
			g.setColor(new Color(224, 224, 224));
			g.fillRect(x1, area.y, x2 - x1, area.height - 1);
		}
		if (isSelected)
			g.setColor(Color.black);
		else
			g.setColor(new Color(96, 96, 96));
		g.drawRect(x1, area.y, x2 - x1, area.height - 1);
	}
}
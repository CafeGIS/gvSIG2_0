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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
/**
 * Clase que se encarga de dibujar los elementos de una tabla de color dentro
 * de un JComboBox.
 *
 * @version 17/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PaintItem extends Component {
	private static final long serialVersionUID = -6448740563809113949L;
	private boolean isSelected = false;
	private String name = "";
	private PreferenceColorTableIconPainter colorTablePaint = null;

	public PaintItem(String name, PreferenceColorTableIconPainter colorTablePaint, boolean isSelected) {
		this.name = name;
		this.colorTablePaint = colorTablePaint;
		this.isSelected = isSelected;

		setSize(getWidth(), 19);
		setPreferredSize(new Dimension(getWidth(), 19));
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (isSelected) {
			Color color1 = new Color(89, 153, 229);
			Color color2 = new Color(31, 92, 207);
			g2.setPaint(new GradientPaint(0, 1, color1, 0, getHeight() - 1, color2, false));
			g2.fillRect(0, 1, getWidth(), getHeight() - 1);
			g2.setColor(new Color(61, 123, 218));
			g2.drawLine(0, 0, getWidth(), 0);
			g2.setColor(Color.white);
		} else {
			g2.setColor(Color.white);
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.setColor(Color.black);
		}

		Rectangle bounds = getBounds();

		FontMetrics fm = g2.getFontMetrics();
		int upper = (bounds.height + fm.getHeight()) / 2 - fm.getDescent();

		g2.setClip(bounds.x, 0, bounds.width, bounds.height);
		g2.drawString(name, bounds.width - 146, upper);

		g2.setClip(bounds.x, 0, bounds.width - 150, bounds.height);

		colorTablePaint.paint((Graphics2D) g2, isSelected);		
	}
}
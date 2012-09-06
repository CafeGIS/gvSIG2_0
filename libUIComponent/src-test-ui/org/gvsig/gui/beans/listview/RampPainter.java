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
package org.gvsig.gui.beans.listview;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
/**
 *
 * @version 29/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class RampPainter implements IIconPaint {

	public RampPainter() {
	}

	public void paint(Graphics2D g, boolean isSelected) {
		Shape clip = g.getClip();
		Rectangle area = clip.getBounds();

		int x1 = area.x;
		int x2 = area.x + area.width - 1;

		Color color1 = Color.green;
		Color color2 = Color.red;
		g.setPaint(new GradientPaint(area.x, 0, color1, area.x + area.width, 0, color2, false));
		g.fillRect(area.x, area.y, area.width, area.height);

		if (isSelected)
			g.setColor(Color.black);
		else
			g.setColor(new Color(96, 96, 96));
		g.drawRect(x1, area.y, x2 - x1, area.height - 1);
	}
}
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
package org.gvsig.gui.beans.listview.painters;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

import org.gvsig.gui.beans.listview.IListViewPainter;
import org.gvsig.gui.beans.listview.ListViewItem;
/**
 * @version 28/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PaintList implements IListViewPainter {
	ArrayList items = null;
	int iconsWidth = 35;
	int minIconsWidth = 35;
	Dimension lastDimension = new Dimension(0, 0);

	public PaintList(ArrayList items) {
		this.items = items;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.graphic.listview.IListViewPainter#getName()
	 */
	public String getName() {
		return "List";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.graphic.listview.IListViewPainter#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		return lastDimension;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.graphic.listview.IListViewPainter#paint(java.awt.Graphics2D, int, int)
	 */
	public void paint(Graphics2D g, Rectangle visibleRect) {
		FontMetrics fm = g.getFontMetrics();

		int minAux = 0;
		for (int i = 0; i < items.size(); i++) {
			int auxWidth = g.getFontMetrics().stringWidth(((ListViewItem) items.get(i)).getName()) + 8;
			if (minAux < auxWidth)
				minAux = auxWidth;
		}
		minAux = visibleRect.width - minAux;
		if (minAux < minIconsWidth)
			minAux = minIconsWidth;
		iconsWidth = minAux;

		int height2 = 0;
		int width2 = 0;
		for (int i = 0; i < items.size(); i++) {
			((ListViewItem) items.get(i)).setNameRectangle(null);

			int auxWidth = g.getFontMetrics().stringWidth(((ListViewItem) items.get(i)).getName());
			if ((minIconsWidth + 3 + auxWidth - visibleRect.x) > visibleRect.width) {
				((ListViewItem) items.get(i)).setShowTooltip(true);
			} else {
				((ListViewItem) items.get(i)).setShowTooltip(false);
			}
			if (width2 < auxWidth)
				width2 = auxWidth;

			((ListViewItem) items.get(i)).getItemRectangle().setBounds(visibleRect.x, i * 17, visibleRect.width, 17);

			if (!((ListViewItem) items.get(i)).getItemRectangle().intersects(visibleRect))
				continue;

			int upper = fm.getLeading() + fm.getAscent() + ((17 - fm.getHeight()) / 2);

			if (((ListViewItem) items.get(i)).isSelected()) {
				Color color1 = new Color(89, 153, 229);
				Color color2 = new Color(31, 92, 207);
				g.setPaint(new GradientPaint(0, i * 17 + 1, color1, 0, i * 17 + 16, color2, false));
				g.fillRect(visibleRect.x, i * 17 + 1, visibleRect.width, 16);
				g.setColor(new Color(61, 123, 218));
				g.drawLine(visibleRect.x, i * 17, visibleRect.x + visibleRect.width, i * 17);
				g.setColor(Color.white);
			} else {
				g.setColor(Color.black);
			}
			g.drawString(((ListViewItem) items.get(i)).getName(), iconsWidth + 3, (i * 17) + upper);
			// Guardar el estado de donde se visualiza el nombre y cuanto ocupa
			((ListViewItem) items.get(i)).setNameRectangle(new Rectangle(iconsWidth + 2, i * 17 - 1, visibleRect.width - (iconsWidth + 2), 20));

			Shape clip = g.getClip();
			g.translate(1, i * 17 + 1);
			g.setClip(0, 0, iconsWidth, 15);

			if (((ListViewItem) items.get(i)).getIcon() != null)
				((ListViewItem) items.get(i)).getIcon().paint(g, ((ListViewItem) items.get(i)).isSelected());

			g.setClip(clip);
			g.translate(-1, -(i * 17 + 1));
		}
		height2 = items.size() * 17;

		lastDimension = new Dimension(minIconsWidth + 3 + width2, height2);
	//lastDimension = new Dimension(0, height2);
	}
}
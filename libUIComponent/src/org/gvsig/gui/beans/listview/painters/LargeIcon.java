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
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

import org.gvsig.gui.beans.listview.IListViewPainter;
import org.gvsig.gui.beans.listview.ListViewItem;
/**
 * Iconos de 82x28
 *
 * @version 28/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class LargeIcon implements IListViewPainter {
	ArrayList items = null;
	int iconsWidth = 82;
	int minIconsWidth = 82;
	int iconsHeight = 28;
	Dimension lastDimension = new Dimension(0, 0);
	int cols = 0;

	public LargeIcon(ArrayList items) {
		this.items = items;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.graphic.listview.IListViewPainter#getName()
	 */
	public String getName() {
		return "LargeIcon";
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
		int aux = (int) Math.floor(visibleRect.getWidth() / (minIconsWidth + 2));
		if (aux > items.size())
			aux = items.size();
		iconsWidth = (int) (Math.floor(visibleRect.getWidth() / aux) - 2);

		int height2 = 0;

		int posX = 0;
		int posY = 0;
		cols = 0;
		for (int i = 0; i < items.size(); i++) {
			// Evito que se pueda editar el nombre
			((ListViewItem) items.get(i)).setNameRectangle(null);

			((ListViewItem) items.get(i)).setShowTooltip(true);
			if (posX != 0) {
				if (((posX + 1) * (iconsWidth + 2)) > visibleRect.getWidth()) {
					posX = 0;
					posY++;
				}
			}

			((ListViewItem) items.get(i)).getItemRectangle().setBounds(posX * (iconsWidth + 2), (posY * (iconsHeight + 2)), iconsWidth + 2, iconsHeight + 2);
			if (((ListViewItem) items.get(i)).getItemRectangle().intersects(visibleRect)) {
				if (((ListViewItem) items.get(i)).isSelected()) {
					g.setColor(new Color(49, 106, 197));
					g.fillRect(posX * (iconsWidth + 2), posY * (iconsHeight + 2), iconsWidth + 2, iconsHeight + 2);
				}

				Shape clip = g.getClip();
				g.translate(posX * (iconsWidth + 2) + 1, (posY * (iconsHeight + 2)) + 1);
				g.setClip(0, 0, iconsWidth, iconsHeight);

				if (((ListViewItem) items.get(i)).getIcon() != null)
					((ListViewItem) items.get(i)).getIcon().paint(g, ((ListViewItem) items.get(i)).isSelected());

				g.setClip(clip);
				g.translate(-(posX * (iconsWidth + 2) + 1), -((posY * (iconsHeight + 2)) + 1));
			}

			if (height2 < ((posY + 1) * (iconsHeight + 2)))
				height2 = (posY + 1) * (iconsHeight + 2);

			if (cols < posX)
				cols = posX;

			posX++;
		}

		lastDimension = new Dimension(minIconsWidth + 2, height2);
	}
}
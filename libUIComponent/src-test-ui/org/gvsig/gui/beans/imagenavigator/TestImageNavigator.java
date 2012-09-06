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
package org.gvsig.gui.beans.imagenavigator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import org.gvsig.gui.beans.TestUI;
/**
 * Test del ImageNavigator
 *
 * @version 08/05/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestImageNavigator implements IClientImageNavigator {
	private TestUI jFrame = null;
	private ImageNavigator imageNavigator = null;

	public TestImageNavigator() {
		initialize();
	}

	private void initialize() {
		jFrame = new TestUI("TestImageNavigator");
		jFrame.setSize(new Dimension(598, 167));
		jFrame.setContentPane(getImageNavigator());
		getImageNavigator().setViewDimensions(0.0, 0.0, 200.0, 100.0);
		getImageNavigator().updateBuffer();
		getImageNavigator().setEnabled(true);
		jFrame.setVisible(true);
	}

	private ImageNavigator getImageNavigator() {
		if (imageNavigator == null) {
			imageNavigator = new ImageNavigator(this);
		}
		return imageNavigator;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestImageNavigator();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.imagenavigator.IClientImageNavigator#drawImage(java.awt.Graphics2D, double, double, double, int, int)
	 */
	public void drawImage(Graphics2D g, double x1, double y1, double x2, double y2, double zoom, int width, int height) {
		double usex1 = (((0 - x1) * width) / (width/zoom));
		double usey1 = (((0 - y1) * height) / (height/zoom));
		double usex2 = (usex1 + (200.0 * zoom));
		double usey2 = (usey1 + (100.0 * zoom));

		g.setColor(Color.black);
		g.fillRect((int) usex1, (int) usey1, (int) (usex2 - usex1), (int) (usey2 - usey1));
		g.setColor(Color.RED);
		g.drawLine((int) usex1, (int) usey1, (int) usex2, (int) usey2);
		g.drawLine((int) usex1, (int) usey2, (int) usex2, (int) usey1);
		g.drawRect((int) usex1, (int) usey1, (int) (usex2 - usex1), (int) (usey2 - usey1));

		g.drawString("X1:" + (int) x1, 1, 20);
		g.drawString("Y1:" + (int) y1, 1, 40);
		g.drawString("X2:" + (int) x2, 1, 60);
		g.drawString("Y2:" + (int) y2, 1, 80);
		g.drawString(width + ":" + height, 1, 120);
	}
}
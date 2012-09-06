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
package org.gvsig.gui.beans.controls;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalToolTipUI;
/**
 * <p>
 * Allows using more than one line length tooltips. It also automatically
 * calculates the size of the screen rectangle used and the preferred sized up
 * on the contained text. It does NOT format the text to fit within a given
 * size. The user must provide the text as she/he wishes to show it.
 * </p>
 * <p>
 * It means that new line characters must be placed in the string as well as it
 * was a System.out.println statement.
 * </p>
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class MultiLineToolTip extends JToolTip {
	private static final long serialVersionUID = -3727623742187461577L;

	public MultiLineToolTip() {
		setUI(new MultiLineToolTipUI());
	}

	private class MultiLineToolTipUI extends MetalToolTipUI {
		private String[] strs;

		public void paint(Graphics g, JComponent c) {
			FontMetrics metrics = g.getFontMetrics();
			Dimension size = c.getSize();
			g.setColor(c.getBackground());
			g.fillRect(0, 0, size.width, size.height);
			g.setColor(c.getForeground());
			if (strs != null) {
				for (int i = 0; i < strs.length; i++) {
					g.drawString(strs[i], 3, (metrics.getHeight()) * (i + 1));
				}
			}
		}

		public Dimension getPreferredSize(JComponent c) {
			FontMetrics metrics = c.getFontMetrics(c.getFont());
			String tipText = ((JToolTip) c).getTipText();
			if (tipText == null) {
				tipText = "";
			}
			BufferedReader br = new BufferedReader(new StringReader(tipText));
			String line;
			int _maxWidth = 0;
			Vector v = new Vector();
			try {
				while ((line = br.readLine()) != null) {
					int width = SwingUtilities.computeStringWidth(metrics, line);
					_maxWidth = (_maxWidth < width) ? width : _maxWidth;
					v.addElement(line);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			int lines = v.size();
			if (lines < 1) {
				strs = null;
				lines = 1;
			} else {
				strs = new String[lines];
				int i = 0;
				for (Enumeration e = v.elements(); e.hasMoreElements(); i++) {
					strs[i] = (String) e.nextElement();
				}
			}
			int height = metrics.getHeight() * lines;
			return new Dimension(_maxWidth + 6, height + 4);
		}
	}
}
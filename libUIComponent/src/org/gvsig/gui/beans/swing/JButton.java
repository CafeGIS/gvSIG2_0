/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.gui.beans.swing;

import java.awt.Dimension;

import javax.swing.Icon;

/**
 * According to the gvSIG's GUI style sheet all the buttons in the application will
 * have a normative size. No smaller than a concrete size, and big enough to contain
 * the text and avoiding the "..." characters. The button will grow up in width by
 * a set of widths defined in this style sheet, always choosing the smallest width that
 * can contain the text. If the biggest width is not enought for this purpose then the
 * button will automatically grow up to the smallest necessary width to fit the text.<br>
 * <p>
 * The button resizing is based on the <b>setText(String txt)</b> method. However,
 * it is possible to use a custom size if you invoke one of <b>setSize(..)</b>,
 * <b>setBorders(...)</b> or <b>setPreferredSize(...)</b> after invoking the
 * <b>setText(...)<b> method.
 * <p>
 * This class is just a standard javax.swing.JButton that handles this issue.
 * </p>
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class JButton extends javax.swing.JButton {
  private static final long serialVersionUID = -1635879317292710725L;

	// TODO this should be initialized from a properties file or so.
	private static int[][] buttonSizes = new int[][] {
			new int[] { 90, 23},
			new int[] {110, 23},
			new int[] {135, 23},
			new int[] {160, 23}
	};

	private String enableText;
	private String toolTip;



	/**
	 * Creates a new empty instance of org.gvsig.gui.beans.swing.JButton.
	 */
	public JButton() {
		super();
	}

	/**
	 * Creates a new instance of org.gvsig.gui.beans.swing.JButton containing a text.
	 * @param text
	 */
	public JButton(String text) {
		super();
		setText(text);
	}

	/**
	 * Creates a new instance of org.gvsig.gui.beans.swing.JButton containing an image and
	 * a text.
	 * @param text
	 * @param icon
	 */
	public JButton(String text, Icon icon) {
		super(icon);
		setText(text);
	}

	/**
	 * Creates a new instance of org.gvsig.gui.beans.swing.JButton containing an image.
	 */
	public JButton(Icon icon) {
		super(icon);
	}


	/**
	 * Gets the text that appears in the tooltip when the button is disabled.
	 * @return String
	 */
	public String getEnableText() {
		return enableText;
	}
	/**
	 * Sets the text that appears in the tooltip when the button is disabled.
	 * @param enableText The enableText to set.
	 */
	public void setEnableText(String enableText) {
		this.enableText = enableText;
	}


	public void setEnabled(boolean aFlag) {
		super.setEnabled(aFlag);
		if (aFlag){
			setToolTipText(toolTip);
		}else{
			setToolTipText(enableText);
		}
	}

	/**
	 * Sets the text that appears in the tooltip when the button is enabled.
	 */
	public void setToolTip(String text) {
		toolTip = text;
	}

	public void setText(String text) {
		super.setText(text);
		Dimension d = getUI().getMinimumSize(this);
		int oldWidth = (int) d.getWidth(), newWidth = oldWidth;
		int oldHeight = (int) d.getHeight(), newHeight = oldHeight;

		// figure out the suitable width
		for (int i = buttonSizes.length-1; i >= 0 ; i--)
			if (oldWidth < buttonSizes[i][0])
				newWidth = buttonSizes[i][0];

		// figure out the suitable height
		for (int i = buttonSizes.length-1; i >= 0 ; i--)
			if (oldHeight < buttonSizes[i][1])
				newHeight = buttonSizes[i][1];

		Dimension sz = new Dimension(newWidth, newHeight);
		super.setSize(sz);
		super.setPreferredSize(sz);
	}

}

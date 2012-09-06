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
package org.gvsig.gui.beans.colorbutton;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDialog;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.colorchooser.ColorChooser;
import org.gvsig.gui.beans.colorchooser.ColorChooserListener;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
/**
 *
 * @version 06/08/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ColorButtonDialog extends JDialog implements ButtonsPanelListener {
	private static final long serialVersionUID = -6452724233113608938L;
	private DefaultButtonsPanel defaultButtonsPanel = null;
	private ColorChooser colorChooser = null;
	private boolean result = false;

	public ColorButtonDialog() {
		initialize();
	}

	private void initialize() {
		setResizable(false);
		setTitle("Selector de color");
		defaultButtonsPanel = new DefaultButtonsPanel(ButtonsPanel.BUTTONS_ACCEPTCANCEL);
		defaultButtonsPanel.getButtonsPanel().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 7, 0));
		add(defaultButtonsPanel);
		colorChooser = new ColorChooser();
		defaultButtonsPanel.add(colorChooser);
		defaultButtonsPanel.addButtonPressedListener(this);
		colorChooser.setPreferredSize(new Dimension(220, (int) colorChooser.getPreferredSize().getHeight()));
		pack();
	}

	/**
	 * @param listener
	 * @see org.gvsig.gui.beans.colorchooser.ColorChooser#addValueChangedListener(org.gvsig.gui.beans.colorchooser.ColorChooserListener)
	 */
	public void addValueChangedListener(ColorChooserListener listener) {
		colorChooser.addValueChangedListener(listener);
	}

	/**
	 * @return
	 * @see org.gvsig.gui.beans.colorchooser.ColorChooser#getColor()
	 */
	public Color getColor() {
		return colorChooser.getColor();
	}

	/**
	 * @param value
	 * @see org.gvsig.gui.beans.colorchooser.ColorChooser#setColor(java.awt.Color)
	 */
	public void setColor(Color value) {
		colorChooser.setColor(value);
	}

	public boolean showDialog() {
		setModal(true);
		setVisible(true);
		return result;
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
		switch (e.getButton()) {
			case ButtonsPanel.BUTTON_ACCEPT:
				result = true;
			case ButtonsPanel.BUTTON_CANCEL:
				this.setVisible(false);
				break;
		}
	}
}
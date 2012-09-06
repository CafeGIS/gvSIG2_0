/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.gui.beans.buttonspanel;

import java.awt.FlowLayout;

import org.gvsig.gui.beans.TestUI;
/**
 * <code>TestButtonsPanel</code> es un test para comprobar el funcionamiento de
 * la clase <code>ButtonsPanel</code>.
 *
 * @version 15/03/2007
 * @author BorSanZa - Borja Sanchez Zamorano (borja.sanchez@iver.es)
 */
public class TestButtonsPanel implements ButtonsPanelListener {
	private TestUI frame = new TestUI("TestButtonsPanel");

	public TestButtonsPanel(){
		frame.getContentPane().setLayout(new java.awt.GridLayout(0, 1));

		frame.setSize(320, 320);
		ButtonsPanel bp = new ButtonsPanel(ButtonsPanel.BUTTONS_ACCEPT);
		bp.addButtonPressedListener(this);
		frame.getContentPane().add(bp);
		bp = new ButtonsPanel(ButtonsPanel.BUTTONS_ACCEPTCANCEL);
		bp.addButtonPressedListener(this);
		bp.setLayout(new java.awt.FlowLayout(FlowLayout.CENTER));
		frame.getContentPane().add(bp);
		bp = new ButtonsPanel(ButtonsPanel.BUTTONS_ACCEPTCANCELAPPLY);
		bp.setLayout(new java.awt.FlowLayout(FlowLayout.LEFT));
		bp.addButtonPressedListener(this);
		frame.getContentPane().add(bp);
		bp = new ButtonsPanel(ButtonsPanel.BUTTONS_YESNO);
		bp.addButtonPressedListener(this);

		bp.getButton(ButtonsPanel.BUTTONS_YESNO).setEnabled(false);

		frame.getContentPane().add(bp);
		bp = new ButtonsPanel(ButtonsPanel.BUTTONS_CLOSE);
		bp.addButtonPressedListener(this);
		frame.getContentPane().add(bp);
		bp = new ButtonsPanel(ButtonsPanel.BUTTONS_EXIT);
		bp.addButtonPressedListener(this);
		bp.addButton("1", 55);
		bp.addButton("bp.addButtonPressedListener(this);", 56);

//		bp.getButton(ButtonsPanel.BUTTON_ACCEPT).isEnabled();

		frame.getContentPane().add(bp);
		frame.setVisible(true);
	}

	public static void main(String[] args){
		new TestButtonsPanel();
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
		System.out.println(e.getButton());
		if (ButtonsPanel.BUTTON_EXIT == e.getButton()) {
			frame.dispose();
		}
	}
}
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
package org.gvsig.gui.beans.propertiespanel.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.gui.beans.propertiespanel.JPanelProperty;
/**
 * Panel que ira dentro del panel de propiedades
 *
 * @version 06/06/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestSlider extends JPanelProperty implements ChangeListener, ActionListener {
	private static final long serialVersionUID = -4936433530376831937L;

	public TestSlider() {
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout());
		JColorChooser color = new JColorChooser();
		add(color, BorderLayout.CENTER);
		JButton boton = new JButton("OK");
		add(boton, BorderLayout.SOUTH);
		boton.addActionListener(this);
		color.getSelectionModel().addChangeListener(this);
	}

	public void stateChanged(ChangeEvent e) {
		this.callStateChanged();
	}

	public void actionPerformed(ActionEvent e) {
		this.callStateChanged();
	}
}
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
package org.gvsig.gui.beans.colorslideredition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.gvsig.gui.beans.TestUI;
/**
 *
 * @version 02/11/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestColorSliderEdition implements ActionListener {
	private TestUI jFrame = new TestUI("TestColorSliderEdition");
	private ColorSliderEdition colorSliderEdition = null;
	JCheckBox jCBInterpolated = null;
	JCheckBox jCBEnabled = null;

	public TestColorSliderEdition() {
		jCBInterpolated = new JCheckBox("Interpolated");
		jCBInterpolated.setSelected(true);
		jCBInterpolated.addActionListener(this);
		jCBEnabled = new JCheckBox("Enabled");
		jCBEnabled.addActionListener(this);
		jCBEnabled.setSelected(true);

		jFrame.setSize(new Dimension(498, 127));
		colorSliderEdition = new ColorSliderEdition();
		colorSliderEdition.setPreferredSize(new Dimension(0, 44));
		colorSliderEdition.setMinimumSize(new Dimension(0, 44));

		colorSliderEdition.addItem(new ItemColorSlider(0, Color.black), false);
		colorSliderEdition.addItem(new ItemColorSlider(25, Color.red), false);
		colorSliderEdition.addItem(new ItemColorSlider(50, Color.yellow), false);
		colorSliderEdition.addItem(new ItemColorSlider(75, Color.blue), true);
		colorSliderEdition.addItem(new ItemColorSlider(100, new Color(255, 255, 255, 0)), true);

		JPanel jPanel = new JPanel();
		jFrame.setContentPane(jPanel);

		jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		jPanel.setLayout(new java.awt.GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 2, 5);
		jPanel.add(colorSliderEdition, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
		jPanel.add(jCBInterpolated, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(2, 5, 5, 5);
		jPanel.add(jCBEnabled, gridBagConstraints);

		jFrame.setVisible(true);
	}

	public static void main(String[] args) {
		new TestColorSliderEdition();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jCBInterpolated) {
			colorSliderEdition.setInterpolated(jCBInterpolated.isSelected());
			return;
		}
		if (e.getSource() == jCBEnabled) {
			colorSliderEdition.setEnabled(jCBEnabled.isSelected());
			return;
		}
	}
}
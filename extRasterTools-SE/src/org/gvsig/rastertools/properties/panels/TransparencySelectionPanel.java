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
package org.gvsig.rastertools.properties.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
/**
 * Panel con los botones para la selección de transparencia.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TransparencySelectionPanel extends JPanel {
	private static final long serialVersionUID = -8601512178169707418L;
	private JButton areaViewSelectionButton  = null;
	private JButton pixelViewSelectionButton = null;

	/**
	 * Constructor.
	 */
	public TransparencySelectionPanel() {
		initialize();
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "transp_selection"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		setLayout(fl);
		add(getAreaViewSelectionButton(), null);
		add(getPixelViewSelectionButton(), null);
	}

	public JButton getAreaViewSelectionButton() {
		if (areaViewSelectionButton == null) {
			areaViewSelectionButton = new JButton();
			areaViewSelectionButton.setPreferredSize(new Dimension(25, 25));
		}
		return areaViewSelectionButton;
	}

	public JButton getPixelViewSelectionButton() {
		if (pixelViewSelectionButton == null) {
			pixelViewSelectionButton = new JButton();
			pixelViewSelectionButton.setPreferredSize(new Dimension(25, 25));
		}
		return pixelViewSelectionButton;
	}
}
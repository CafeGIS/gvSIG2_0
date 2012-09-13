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
package org.gvsig.rastertools.raw.ui.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
/**
 * This panel is a JButton for the "Guess image geometry" option.
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class GuessImagePanel extends JPanel {
	private static final long serialVersionUID = 6081119425437983640L;
	private final int BUTTON_WIDTH     = 250;
	private final int BUTTON_HEIGHT    = 19;

	private JButton   guessImageButton = null;

	public GuessImagePanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getGuessImageButton(), gridBagConstraints);

	}

	/**
	 * This method initializes guessImageButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getGuessImageButton() {
		if (guessImageButton == null) {
			guessImageButton = new JButton();
			guessImageButton.setPreferredSize(new java.awt.Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			guessImageButton.setText(PluginServices.getText(this, "guess_image_geometry"));

		}
		return guessImageButton;
	}
}
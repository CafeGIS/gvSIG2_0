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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.gvsig.rastertools.raw.ui.listener.OpenRawFileDefaultViewListener;

import com.iver.andami.PluginServices;
/**
 * This class contains the main buttons of the open raw file panel window.
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class OpenRawFileButtonsPanel extends JPanel {
  private static final long serialVersionUID = -2708844842333915176L;

	private final int WIDTH          = 500;
	private final int HEIGHT         = 35;
	private final int BUTTONS_WIDTH  = 80;
	private final int BUTTONS_HEIGHT = 25;

	private JButton   openButton     = null;
	private JButton   closeButton    = null;

	public OpenRawFileButtonsPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.insets = new java.awt.Insets(5, 3, 5, 5);
		gridBagConstraints3.gridy = 0;
		gridBagConstraints3.gridx = 3;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.insets = new java.awt.Insets(5, 3, 5, 2);
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.gridx = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.insets = new java.awt.Insets(5, 3, 5, 2);
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 2);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(WIDTH, HEIGHT));
		this.add(getOpenButton(), gridBagConstraints2);
		this.add(getCloseButton(), gridBagConstraints3);
	}
	
	/**
	 * This method initializes openButton	
	 * @return javax.swing.JButton	
	 */
	private JButton getOpenButton() {
		if (openButton == null) {
			openButton = new JButton();
			openButton.setPreferredSize(new Dimension(BUTTONS_WIDTH, BUTTONS_HEIGHT));
			openButton.setText(PluginServices.getText(this, "accept"));
			openButton.setActionCommand("open");
		}
		return openButton;
	}

	/**
	 * This method initializes closeButton	
	 * @return javax.swing.JButton	
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setPreferredSize(new Dimension(BUTTONS_WIDTH, BUTTONS_HEIGHT));
			closeButton.setText(PluginServices.getText(this, "close"));
			closeButton.setActionCommand("close");
		}
		return closeButton;
	}
	
	/**
	 * Sets the buttons listener
	 * @param listener Buttons listener
	 */
	public void setActionListener(OpenRawFileDefaultViewListener listener) {
		openButton.addActionListener(listener);
		closeButton.addActionListener(listener);
	}
}
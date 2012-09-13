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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class TranspByPixelAndOrSelectorPanel extends JPanel {
	final private static long	serialVersionUID = 0;
	private JPanel 				pBorder = null;
	private JRadioButton 		rbAnd = null;
	private JRadioButton 		rbOr = null;
	private ButtonGroup 		group = null;
	/**
	 * This is the default constructor
	 */
	public TranspByPixelAndOrSelectorPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		this.setLayout(flowLayout);
		this.setSize(60, 60);
		this.setPreferredSize(new java.awt.Dimension(52,50));
		this.add(getJPanel(), null);
		this.getRbAnd().setSelected(true);
		group = new ButtonGroup();
		group.add(this.getRbAnd());
		group.add(this.getRbOr());
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (pBorder == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.gridy = 1;
			pBorder = new JPanel();
			pBorder.setLayout(new GridBagLayout());
			pBorder.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray,1));
			pBorder.setPreferredSize(new java.awt.Dimension(60,60));
			pBorder.add(getRbAnd(), gridBagConstraints1);
			pBorder.add(getRbOr(), gridBagConstraints);
		}
		return pBorder;
	}

	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	public JRadioButton getRbAnd() {
		if (rbAnd == null) {
			rbAnd = new JRadioButton();
			rbAnd.setText("And");
			rbAnd.setPreferredSize(new java.awt.Dimension(60,25));
		}
		return rbAnd;
	}

	/**
	 * This method initializes jRadioButton1	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	public JRadioButton getRbOr() {
		if (rbOr == null) {
			rbOr = new JRadioButton();
			rbOr.setText("Or");
			rbOr.setPreferredSize(new java.awt.Dimension(60,25));
		}
		return rbOr;
	}
	
	/**
	 * Activa o desactiva el control
	 * @param enable True activa el control y false lo desactiva
	 */
	public void setControlEnabled(boolean enabled){
		this.getRbAnd().setEnabled(enabled);
		this.getRbOr().setEnabled(enabled);
	}

}

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
package org.gvsig.rastertools.saveraster.ui.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Panel con el selector de método de escalado. Por escala, tamaño de raster
 * de salida o tamaño de pixel.
 * 
 * @version 18/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class MethodSelectorPanel extends JPanel {
	private static final long           serialVersionUID = 1L;
	private JPanel 			            pMethodSelector = null;
	private JRadioButton 	            rbScale = null;
	private JRadioButton 	            rbMtsPixel = null;
	private JRadioButton 	            rbSize = null;
	private ButtonGroup 	            group = null;
	
	/**
	 * This is the default constructor
	 */
	public MethodSelectorPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray,1));
		
		group = new ButtonGroup();
		this.add(getPMethodSelector(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getPMethodSelector() {
		if (pMethodSelector == null) {
			pMethodSelector = new JPanel();
			pMethodSelector.setLayout(new GridBagLayout());
			pMethodSelector.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray,1));
			pMethodSelector.setPreferredSize(new java.awt.Dimension(105,77));
			group.add(getRbScale());
			group.add(getRbMtsPixel());
			group.add(getRbSize());
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			pMethodSelector.add(getRbSize(), gridBagConstraints);
			
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			pMethodSelector.add(getRbScale(), gridBagConstraints);
			
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			pMethodSelector.add(getRbMtsPixel(), gridBagConstraints);
		}
		return pMethodSelector;
	}

	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	public JRadioButton getRbScale() {
		if (rbScale == null) {
			rbScale = new JRadioButton();
			rbScale.setText("Escala");
			rbScale.setSelected(true);
		}
		return rbScale;
	}

	/**
	 * This method initializes jRadioButton1	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	public JRadioButton getRbMtsPixel() {
		if (rbMtsPixel == null) {
			rbMtsPixel = new JRadioButton();
			rbMtsPixel.setText("Mts/Pixel");
		}
		return rbMtsPixel;
	}

	/**
	 * This method initializes jRadioButton2	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	public JRadioButton getRbSize() {
		if (rbSize == null) {
			rbSize = new JRadioButton();
			rbSize.setText("Tamaño");
		}
		return rbSize;
	}

}

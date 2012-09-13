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

import javax.swing.JPanel;

import org.gvsig.rastertools.raw.ui.listener.OpenRawFileDefaultViewListener;
/**
 * This is the main open raw file panel. It is composed by all the other panels
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class OpenRawFileDefaultPanel extends JPanel{
  private static final long serialVersionUID = 6224308303746649872L;
	private OpenRawFileControlsPanel controlsPanel = null;
	private OpenRawFileButtonsPanel  buttonsPanel  = null;

	private String                   rawFileName   = null;

	public OpenRawFileDefaultPanel(String rawFileName) {
		super();
		this.rawFileName = rawFileName;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.gridx = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getControlsPanel(), gridBagConstraints);
		this.add(getButtonsPanel(), gridBagConstraints1);

	}

	/**
	 * This method initializes controlsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public OpenRawFileControlsPanel getControlsPanel() {
		if (controlsPanel == null) {
			controlsPanel = new OpenRawFileControlsPanel(rawFileName);
			controlsPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.SoftBevelBorder.RAISED));
		}
		return controlsPanel;
	}

	/**
	 * This method initializes buttonsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private OpenRawFileButtonsPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new OpenRawFileButtonsPanel();
		}
		return buttonsPanel;
	}

	/**
	 * Sets the buttons listener
	 * @param listener
	 * Buttons listener
	 */
	public void setActionListener(OpenRawFileDefaultViewListener listener) {
		getButtonsPanel().setActionListener(listener);
	}
}
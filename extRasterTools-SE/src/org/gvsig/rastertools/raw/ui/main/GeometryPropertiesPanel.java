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

import org.gvsig.rastertools.raw.tools.VRTFormatOptions;

import com.iver.andami.PluginServices;
/**
 * This panel implements all the geometry properties panel. The "guess image
 * geometry" button can be enabled or disabled changing the
 * GUESS_IMAGE_BUTTON_VISIBLE attribute value.
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class GeometryPropertiesPanel extends JPanel {
  private static final long serialVersionUID = -3099735931714632591L;
	private final int                    WIDTH                      = 510;
	private final int                    HEIGHT                     = 155;
	private boolean                      GUESS_IMAGE_BUTTON_VISIBLE = false;
	private GeometryPropertiesLeftPanel  leftPanel                  = null;
	private GeometryPropertiesRigthPanel rigthPanel                 = null;
	private GuessImagePanel              guessImagePanel            = null;

	public GeometryPropertiesPanel() {
		super();
		initialize();		
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.gridx = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.insets = new java.awt.Insets(0, 2, 0, 0);
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "select_image_geometry"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		this.add(getLeftPanel(), gridBagConstraints);
		this.add(getRigthPanel(), gridBagConstraints1);
		if (GUESS_IMAGE_BUTTON_VISIBLE) {
			this.add(getGuessImagePanel(), gridBagConstraints2);
		}
	}

	/**
	 * This method initializes leftPanel
	 * @return javax.swing.JPanel
	 */
	private GeometryPropertiesLeftPanel getLeftPanel() {
		if (leftPanel == null) {
			leftPanel = new GeometryPropertiesLeftPanel();
		}
		return leftPanel;
	}

	/**
	 * This method initializes rigthPanel
	 * @return javax.swing.JPanel
	 */
	private GeometryPropertiesRigthPanel getRigthPanel() {
		if (rigthPanel == null) {
			rigthPanel = new GeometryPropertiesRigthPanel();
		}
		return rigthPanel;
	}

	/**
	 * This method initializes guessImagePanel
	 * @return javax.swing.JPanel
	 */
	private GuessImagePanel getGuessImagePanel() {
		if (guessImagePanel == null) {
			guessImagePanel = new GuessImagePanel();
		}
		return guessImagePanel;
	}

	/**
	 * Sets the file size into the text field
	 * @param fileSize
	 */
	public void setFileSize(long fileSize) {
		getRigthPanel().setFileSize(fileSize);
	}

	/**
	 * Gets the data type
	 * @return
	 */
	public VRTFormatOptions.UIOption getDataType() {
		return getRigthPanel().getDataType();
	}

	/**
	 * Gets the byte order
	 * @return
	 */
	public String getByteOrder() {
		return getRigthPanel().getByteOrder();
	}

	/**
	 * Gets the type of interleaving
	 * @return
	 */
	public String getInterleaving() {
		return getRigthPanel().getInterleaving();
	}

	/**
	 * gets the image width
	 * @return
	 */
	public int getImageWidth() {
		return getLeftPanel().getImageWidth();
	}

	/**
	 * gets the image height
	 * @return
	 */
	public int getImageHeight() {
		return getLeftPanel().getImageHeight();
	}

	/**
	 * gets the number of bands
	 * @return
	 */
	public int getNumberOfBands() {
		return getLeftPanel().getNumberOfBands();
	}

	/**
	 * gets the header size
	 * @return
	 */
	public int getHeaderSize() {
		return getLeftPanel().getHeaderSize();
	}
}
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
import java.io.File;

import javax.swing.JPanel;

import org.gvsig.gui.beans.openfile.OpenFileContainer;
import org.gvsig.rastertools.raw.tools.VRTFormatOptions;
/**
 * This class contains all the components of a open raw file
 * panel. It doesn't has the main buttons.
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class OpenRawFileControlsPanel extends JPanel {
  private static final long serialVersionUID = 6493641545774147712L;
	private final int               PANELS_WIDTH            = 510;
	private OpenFileContainer       openFilePanel           = null;
	private GeometryPropertiesPanel geometryPropertiesPanel = null;
	private OutputHeaderFormatPanel outputHeaderFormatPanel = null;
	private boolean                 calculateFileSize       = true;

	/**
	 * Constructor
	 * @param rawFileName
	 * Raw file name
	 */
	public OpenRawFileControlsPanel(String rawFileName) {
		super();
		initialize();
		openFilePanel.getTOpen().setText(rawFileName);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.insets = new java.awt.Insets(5, 0, 5, 0);
		gridBagConstraints2.gridy = 2;
		gridBagConstraints2.gridx = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.insets = new java.awt.Insets(5, 0, 5, 0);
		gridBagConstraints1.gridy = 1;
		gridBagConstraints1.gridx = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getOpenFilePanel(), gridBagConstraints);
		this.add(getGeometryPropertiesPanel(), gridBagConstraints1);
		this.add(getOutputHeaderFormatPanel(), gridBagConstraints2);
		new CalculateFileSize();
	}

	/**
	 * This method initializes openFilePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private OpenFileContainer getOpenFilePanel() {
		if (openFilePanel == null) {
			openFilePanel = new OpenFileContainer(PANELS_WIDTH, 50, false);
			openFilePanel.setPreferredSize(new java.awt.Dimension(PANELS_WIDTH, 50));
		}
		return openFilePanel;
	}

	/**
	 * This method initializes geometryPropertiesPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private GeometryPropertiesPanel getGeometryPropertiesPanel() {
		if (geometryPropertiesPanel == null) {
			geometryPropertiesPanel = new GeometryPropertiesPanel();
		}
		return geometryPropertiesPanel;
	}

	/**
	 * This method initializes outputHeaderFormatPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private OutputHeaderFormatPanel getOutputHeaderFormatPanel() {
		if (outputHeaderFormatPanel == null) {
			outputHeaderFormatPanel = new OutputHeaderFormatPanel();
		}
		return outputHeaderFormatPanel;
	}

	/**
	 * Sets the file size into the text field
	 * @param fileSize
	 */
	public void setFileSize(int fileSize) {
		getGeometryPropertiesPanel().setFileSize(fileSize);
	}

	/**
	 * Gets the data type
	 * @return
	 */
	public VRTFormatOptions.UIOption getDataType() {
		return getGeometryPropertiesPanel().getDataType();
	}

	/**
	 * Gets the byte order
	 * @return
	 */
	public String getByteOrder() {
		return getGeometryPropertiesPanel().getByteOrder();
	}

	/**
	 * Gets the type of interleaving
	 * @return
	 */
	public String getInterleaving() {
		return getGeometryPropertiesPanel().getInterleaving();
	}

	/**
	 * gets the image width
	 * @return
	 */
	public int getImageWidth() {
		return getGeometryPropertiesPanel().getImageWidth();
	}

	/**
	 * gets the image height
	 * @return
	 */
	public int getImageHeight() {
		return getGeometryPropertiesPanel().getImageHeight();
	}

	/**
	 * gets the number of bands
	 * @return
	 */
	public int getNumberOfBands() {
		return getGeometryPropertiesPanel().getNumberOfBands();
	}

	/**
	 * gets the header size
	 * @return
	 */
	public int getHeaderSize() {
		return getGeometryPropertiesPanel().getHeaderSize();
	}

	/**
	 * gets the selected outputformat
	 * @return
	 */
	public String getOutputHeaderFormat() {
		return getOutputHeaderFormatPanel().getOutputHeaderFormat();
	}

	/**
	 * Gets the RAW file
	 * @return
	 */
	public File getFile() {
		return getOpenFilePanel().getFile();
	}

	/**
	 * This method is used by the listener to stop the
	 * thread that calculates the file size
	 *
	 */
	public void stopThread() {
		calculateFileSize = false;
	}

	/**
	 * This class is a thread that try if the textbox (where
	 * the file to open is written) contains a valid file and
	 * calculate its size.
	 * @author Jorge Piera Llodrá (piera_jor@gva.es)
	 */
	public class CalculateFileSize implements Runnable {
		volatile Thread myThread = null;

		public CalculateFileSize() {
			if (myThread == null) {
				myThread = new Thread(this);
				myThread.start();
			}
		}

		/*
		 *  (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while (calculateFileSize) {
				File file = getFile();
				if ((file.exists() && (file.isFile()))) {
					getGeometryPropertiesPanel().setFileSize(file.length());
				} else {
					getGeometryPropertiesPanel().setFileSize(0);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
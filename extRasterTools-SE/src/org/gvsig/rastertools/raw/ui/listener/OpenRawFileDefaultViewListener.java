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
package org.gvsig.rastertools.raw.ui.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.raw.tools.VRTFileCreator;
import org.gvsig.rastertools.raw.ui.main.OpenRawFileControlsPanel;
import org.gvsig.rastertools.raw.ui.main.OpenRawFileDefaultView;

import com.iver.andami.PluginServices;
/**
 * Listener for the open raw file window. It implements actions for the "open
 * file" and for the "close window" buttons.
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class OpenRawFileDefaultViewListener implements ActionListener {
	private OpenRawFileDefaultView openRawView = null;

	/**
	 * Contructor
	 * @param view Open raw file view
	 */
	public OpenRawFileDefaultViewListener(OpenRawFileDefaultView view) {
		super();
		this.openRawView = view;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand() == "close") {
			closeButtonActionPerformed();
		} else
			if (event.getActionCommand() == "open") {
				openButtonActionPerformed();
			}
	}

	/**
	 * Open raw file button action
	 */
	private void openButtonActionPerformed() {
		OpenRawFileControlsPanel controls = openRawView.getControlsPanel();
		if (!(controls.getFile().exists())) {
			JOptionPane.showMessageDialog((Component) PluginServices.getMainFrame(), PluginServices.getText(this, "file_doesn_exists"));
			return;
		}
		if (controls.getOutputHeaderFormat().equals("VRT")) {
			String vrtFileName = controls.getFile().getAbsolutePath().replaceAll("\\.raw", ".vrt");
			VRTFileCreator vrt = new VRTFileCreator(vrtFileName);
			vrt.setImageWidth(controls.getImageWidth());
			vrt.setImageHeight(controls.getImageHeight());
			vrt.setBands(controls.getNumberOfBands());
			vrt.setHeaderSize(controls.getHeaderSize());
			vrt.setDataType(controls.getDataType().getVrtOptionName());
			vrt.setDataSize(controls.getDataType().getDataSize());
			vrt.setByteOrder(controls.getByteOrder());
			vrt.setInterleaving(controls.getInterleaving());
			vrt.setRawFile(controls.getFile().getName());
			try {
				vrt.writeFile();
				openRawView.setImageFile(vrt.getM_File());
			} catch (IOException e) {
				RasterToolsUtil.messageBoxError("cant_create_vrt_file", this, e);
			}
		}
		closeButtonActionPerformed();
	}

	/**
	 * Close window
	 */
	private void closeButtonActionPerformed() {
		OpenRawFileControlsPanel controls = openRawView.getControlsPanel();
		controls.stopThread();
		PluginServices.getMDIManager().closeWindow(openRawView);
	}
}
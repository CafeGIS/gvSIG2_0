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
package org.gvsig.rastertools.saveraster.ui.info;

import java.io.File;

import javax.swing.JFrame;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.raster.util.RasterToolsUtil;
/**
 * Panel principal del dialogo de finalización del salvado a raster. En el se
 * muestra la información de nombre de fichero, tamaño de este, tiempo de la
 * operación, etc...
 * 
 * Para mostrar un fichero solo hay que usar el metodo estatico show.
 *
 * @version 19/06/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class EndInfoDialog implements ButtonsPanelListener {
	private JFrame               frame         = null;
	private EndInfoPanel         infoPanel     = null;
	static private EndInfoDialog endInfoDialog = null;

	/**
	 * Obtiene el panel con la información de finalización
	 * @return EndInfoPanel
	 */
	private EndInfoPanel getInfoPanel() {
		if (infoPanel == null) {
			infoPanel = new EndInfoPanel();
			getFrame().add(infoPanel);
			infoPanel.addButtonPressedListener(this);
		}
		return infoPanel;
	}

	private JFrame getFrame() {
		if (frame == null) {
			frame = new JFrame(RasterToolsUtil.getText(this, "stats"));
			frame.setResizable(false);
			frame.setAlwaysOnTop(true);
		}
		return frame;
	}
	
	static public void show(String fileName, long time) {
		if (!new File(fileName).exists())
			return;
		
		if ((endInfoDialog == null) || (!endInfoDialog.getFrame().isVisible()))
			endInfoDialog = new EndInfoDialog();
		endInfoDialog.getInfoPanel().addFile(fileName, time);
		endInfoDialog.getFrame().setContentPane(endInfoDialog.getInfoPanel());
		endInfoDialog.getFrame().pack();
		endInfoDialog.getFrame().setVisible(true);
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			getFrame().setVisible(false);
			endInfoDialog = null;
		}
	}
}

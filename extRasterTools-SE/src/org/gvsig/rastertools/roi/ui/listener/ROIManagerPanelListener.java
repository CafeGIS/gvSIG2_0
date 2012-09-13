/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.rastertools.roi.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.rastertools.roi.ui.ROIsManagerPanel;

public class ROIManagerPanelListener implements ButtonsPanelListener, ActionListener{
	
	private ROIsManagerPanel roiManagerPanel = null;

	public ROIManagerPanelListener(ROIsManagerPanel managerPanel) {
		this.roiManagerPanel = managerPanel;
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == roiManagerPanel.getCloseMenuItem()){
			roiManagerPanel.getRoiManagerDialog().close();
		}else if (e.getSource() == roiManagerPanel.getSaveMenuItem()){
			
		}else if (e.getSource() == roiManagerPanel.getLoadMenuItem()){
			
		}else if (e.getSource() == roiManagerPanel.getExportMenuItem()){
			
		}else if (e.getSource() == roiManagerPanel.getHelpMenuItem()){
			
		}else if (e.getSource() == roiManagerPanel.getPreferencesMenuItem()){
			
		}
	}
}

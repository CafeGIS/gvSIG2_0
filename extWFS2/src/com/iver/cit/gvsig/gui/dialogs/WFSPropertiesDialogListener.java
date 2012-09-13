package com.iver.cit.gvsig.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.gui.panels.WFSAreaPanel;
import com.iver.cit.gvsig.gui.panels.WFSFilterPanel;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 *
 */

/**
 * <p>Listener used when user presses any of the buttons of the WFS properties dialog.</p>
 * 
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class WFSPropertiesDialogListener implements ActionListener {
	public static final String CANCEL_BUTTON_ACTION_COMMAND = "CANCEL";
	public static final String APPLY_BUTTON_ACTION_COMMAND = "APPLY";
	public static final String ACCEPT_BUTTON_ACTION_COMMAND = "ACCEPT";

	private WFSPropertiesDialog dialog = null;

	/**
	 * Creates a new WFS properties dialog listener.
	 * 
	 * @param dialog reference to the WFS properties dialog
	 */
	public WFSPropertiesDialogListener(WFSPropertiesDialog dialog) {
		this.dialog = dialog;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
//		if (e.getActionCommand() == CANCEL_BUTTON_ACTION_COMMAND) {
//			dialog.close();
//			return;
//		}
//
//		// Accelerate the response: if there are not changes to apply it closes the dialog
//		if ((e.getActionCommand() == ACCEPT_BUTTON_ACTION_COMMAND) && (! dialog.isEnabledApplyButton())) {
//			dialog.close();
//			return;
//		}
//
//		//Accept or apply
//		try {
//			// We can't 'apply' if there is a filter query and it's incorrect
//			if (dialog.getFilterQuery() == null)
//				return;
//
//			// Gets a reference to the old layer
//			FLayer oldLayer = (FLayer) dialog.getReference();
//			String oldLayerName = oldLayer.getName();
//
//			// Gets the layer with the current information:
//			FLyrWFS newLayer = dialog.getFLayer(); 
//
//			// Gets the driver
//			FMapWFSDriver driver = newLayer.getWfsDriver(); //dialog.getDriver();		
//
//			// Sets the online resource
//			URL host = new URL(dialog.getServerExplorer().getHost());
//			String onlineResource = dialog.getServerExplorer().getOnlineResource();
//
//			// Tries to get the new layer
//			newLayer = new FLyrWFSFactory().getFLyrWFS(newLayer, host, onlineResource, driver, true, false);
//
//	    	// If can load the layer, update all data
//			if (newLayer != null) {
//				// Removes the last project table if exists
//				removeProjectTable((FLyrVect) oldLayer);
//
//				// Sets the new layer
//				dialog.updateReference(newLayer);
//				
//				// Replace the old layer entry at the TOC with the new one
//				BaseView activeView = (BaseView) PluginServices.getMDIManager().getActiveWindow();
//				MapControl mapCtrl = activeView.getMapControl();
//				mapCtrl.getMapContext().getLayers().replaceLayer(oldLayerName, newLayer);
//				mapCtrl.getMapContext().zoomToExtent(newLayer.getFullExtent());
//
//				// Refresh the active window where the new layer is located
//				refreshActiveWindow();
//
//				// Update data
//				if (e.getActionCommand() == APPLY_BUTTON_ACTION_COMMAND) {
//					updateFilterAndAreaPanelsData();
//
//					// Disable the apply button
//					dialog.setEnabledApplyButton(false);
//					return;
//				}
//			}
//
//			// Accept button clicked
//			if (e.getActionCommand() == ACCEPT_BUTTON_ACTION_COMMAND) {
//				dialog.close();
//				return;
//			}
//
//			return;
//		} catch (Exception e1) {
//			NotificationManager.addError(e1);
//		} 		
	}

	/**
	 * Removes the associated project table of the <code>flayer</code>.
	 * 
	 * @param flayer a vector layer
	 */
	private void removeProjectTable(FLyrVect flayer) {
		// Remove it
//		ProjectExtension ext = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
//		ArrayList<ProjectDocument> tables = ext.getProject().getDocumentsByType(ProjectTableFactory.registerName);
//
//		for (int i=0 ; i < tables.size() ; i++){
//			ProjectTable projectTable = (ProjectTable)tables.get(i);
//
//			try {
//				if (flayer.getRecordset().equals(projectTable.getAssociatedTable().getRecordset())){
//					ext.getProject().delDocument(projectTable);
//				}
//			} catch (ReadDriverException e) {
//				NotificationManager.addError(e);
//			}
//		}
	}

	/**
	 * @see WFSFilterPanel#setWFSFilterPanelIsAsTabForWFSLayersLoad(boolean)
	 */
	private void setWFSFilterPanelIsAsTabForWFSLayersLoad(boolean b) {
		dialog.setWFSFilterPanelIsAsTabForWFSLayersLoad(b);
	}

	/**
	 * <p>Refresh the active window.</p>
	 */
	private void refreshActiveWindow() {
		IWindow window = PluginServices.getMDIManager().getActiveWindow();

		if (window instanceof View)
			((View)window).invalidate();
	}

	/**
	 * Updates the information of the panels {@linkplain WFSFilterPanel WFSFilterPanel} and {@linkplain WFSAreaPanel WFSAreaPanel}.
	 */
	private void updateFilterAndAreaPanelsData() {
		// Restores the private attribute of the area panel: hasUserDefinedAnArea
		dialog.setUserHasntDefinedAnArea();

		// If we load another layer, or the same but we've selected others fields -> notify it to the WFSFilter panel
		if (dialog.getFieldsSelectedOfSameLayerHasChanged()) {
			dialog.resetFieldsSelectedOfSameLayerHasChanged(); // reset that field
		}

		// Update the data of the Filter and Area panels
		setWFSFilterPanelIsAsTabForWFSLayersLoad(false);
		dialog.updateWFSFilterFieldValues();
		dialog.updateWFSArea();
	}
}

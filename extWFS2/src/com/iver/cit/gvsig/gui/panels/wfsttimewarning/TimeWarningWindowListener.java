package com.iver.cit.gvsig.gui.panels.wfsttimewarning;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.cit.gvsig.gui.toc.WFSTStartEditionTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

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
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class TimeWarningWindowListener implements ActionListener{
	private TimeWarningWindow timeWarningWindow = null;
	private FLyrVect layer = null;
	private ITocItem item = null;
	private FLayer[] selectedItems = null;
	private WFSTStartEditionTocMenuEntry startEditionTocMenuEntry = null;

	public TimeWarningWindowListener(
			TimeWarningWindow timeWarningWindow, FLyrVect layer,
			ITocItem item, FLayer[] selectedItems,
			WFSTStartEditionTocMenuEntry startEditionTocMenuEntry) {
		super();
		this.timeWarningWindow = timeWarningWindow;
		this.layer = layer;
		this.item = item;
		this.selectedItems = selectedItems;
		this.startEditionTocMenuEntry = startEditionTocMenuEntry;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
//		layer.setWfstSrsBasedOnXML(timeWarningWindow.isSrsBasedOnXML());
//		layer.setWfstLockFeaturesEnabled(timeWarningWindow.isLockGeometries());
//		if (e.getActionCommand().compareTo(TimeWarningPanel.ACCEPTBUTTON_ACTIONCOMMAND) == 0){
//			if (timeWarningWindow.isLockGeometries()){
//				int time = timeWarningWindow.getExpiryTime();
//				//Lock the layer
//				try {
//					layer.lockCurrentFeatures(time);
//				} catch (WFSTLockFeaturesException e1) {
//					//Problem locking a feature. The WFST transaction has to be canceled
//					layer.setWfstEditing(false);
//					JOptionPane.showMessageDialog(
//							(Component) PluginServices.getMDIManager().getActiveWindow(),
//							PluginServices.getText(this, "wfst_layer_cant_be_locked"),
//							e1.getMessage(),
//							JOptionPane.WARNING_MESSAGE);
//					PluginServices.getMDIManager().closeWindow(timeWarningWindow);
//					return;
//				}	
//			}else{
//				layer.setWfstExpiryTime(-1);
//			}
//			PluginServices.getMDIManager().closeWindow(timeWarningWindow);
//			startEditionTocMenuEntry.edit(item, selectedItems);
//		}else if (e.getActionCommand().compareTo(TimeWarningPanel.CANCELBUTTON_ACTIONCOMMAND) == 0){
//			PluginServices.getMDIManager().closeWindow(timeWarningWindow);
//		}
	}	
}

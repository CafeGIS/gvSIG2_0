package com.iver.cit.gvsig.gui.panels.wfsttimewarning;

import org.gvsig.fmap.mapcontext.layers.LayerEvent;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.cit.gvsig.gui.panels.wfstclock.ClockWindow;

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
public class WFSTEditionListener {//extends WFSLayerListener {
	private ClockWindow clockWindow = null;

	public WFSTEditionListener(){
		super();
	}

	public WFSTEditionListener(FLyrVect wfsLayer){
		//super(wfsLayer);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.LayerListener#activationChanged(com.iver.cit.gvsig.fmap.layers.LayerEvent)
	 */
	public void activationChanged(LayerEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.LayerListener#editionChanged(com.iver.cit.gvsig.fmap.layers.LayerEvent)
	 */
	public void editionChanged(LayerEvent e) {
//		//If is not a WFST session
//		if (!wfsLayer.isWfstEditing()){
//			JOptionPane.showMessageDialog(
//					(Component) PluginServices.getMDIManager().getActiveWindow(),
//					PluginServices.getText(this, "this_layer_is_not_self_editable"),
//					PluginServices.getText(this, "warning_title"),
//					JOptionPane.WARNING_MESSAGE);
//		}//Is a WFST session
//		else{
//			if (wfsLayer.isEditing()){
//				//If the server don't uses the lockFeature operation
//				if (wfsLayer.getWfstExpiryTime() < 0){
//					//The LockFeature operation is not send
//				}//The clock window has to be opened
//				else{
//					clockWindow = new ClockWindow(wfsLayer);
//					clockWindow.startTime();
//					PluginServices.getMDIManager().addWindow(clockWindow);
//				}
//			}else{
//				if (clockWindow != null){
//					clockWindow.setClosed();
//					PluginServices.getMDIManager().closeWindow(clockWindow);
//				}
//				wfsLayer.setWfstEditing(false);
//			}
//		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.LayerListener#nameChanged(com.iver.cit.gvsig.fmap.layers.LayerEvent)
	 */
	public void nameChanged(LayerEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.LayerListener#visibilityChanged(com.iver.cit.gvsig.fmap.layers.LayerEvent)
	 */
	public void visibilityChanged(LayerEvent e) {

	}

	public void drawValueChanged(LayerEvent e) {
		// TODO Auto-generated method stub
		
	}
}

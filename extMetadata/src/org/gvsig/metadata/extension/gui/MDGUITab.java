/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2008 Geographic Information research group: http://www.geoinfo.uji.es
* Departamento de Lenguajes y Sistemas Informáticos (LSI)
* Universitat Jaume I   
* {{Task}}
*/
 
package org.gvsig.metadata.extension.gui;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.gui.beans.swing.JBlank;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.legend.gui.AbstractThemeManagerPage;



public class MDGUITab extends AbstractThemeManagerPage { 

	private static final long serialVersionUID = 1L;
	
	private FLayer layer;
	private MDBasicGUIPanel mdPanel; 
	private JCheckBox editCB;
	private boolean edited;
	
	
	
	/**
	 * This is the default constructor.
	 */
	public MDGUITab() {
		super();
		
		// GET ACTIVE LAYER --------------------------------
		FLayers al = this.getFLayers();
		FLayer[] ll = al.getActives();
		
		layer = al.getLayer(0);
		if(ll.length > 0)
			layer = ll[0];
		// -------------------------------------------------
		
		mdPanel = new MDBasicGUIPanel(layer);
		
		setLayout(new BorderLayout());
		add(mdPanel, BorderLayout.CENTER);
		add(new JBlank(5, 10), BorderLayout.WEST);
		add(new JBlank(5, 10), BorderLayout.EAST);
		add(getSouthPanel(), BorderLayout.SOUTH);
	}
	
	private JPanel getSouthPanel() {
		
		editCB = new JCheckBox(PluginServices.getText(this, "edit_checkbox"), false);
		editCB.addItemListener( new ItemListener() {
									public void itemStateChanged(ItemEvent e) {
										if(e.getStateChange() == ItemEvent.SELECTED) {
											mdPanel.allowEdition();
											edited = true;
										} else
											mdPanel.closeEdition();
									}
	    						}
		);
		edited = false;
		
		JPanel auxPanel = new JPanel();
		auxPanel.setLayout(new MigLayout());
		auxPanel.add(editCB);
		return auxPanel;
	}
	
	/**
	 *  (non-Javadoc)
	 * @see java.awt.Component#getName()
	 */
	public String getName() {
		return PluginServices.getText(this, "Metadata");
	}
	
	/**
	 * Sets the necessary properties in the panel.
	 * @param FLayer layer
	 */
	public void setModel(FLayer layer) {
		this.layer = layer;
	}

	public void acceptAction() {
		if(edited)
			mdPanel.saveChanges();
	}

	public void cancelAction() {
		//if(edited) -> Lanzar alert sobre guardar posibles cambios?
	}
	
	public void applyAction() {
		if(edited) {
			mdPanel.closeEdition();
			mdPanel.saveChanges();
		}
	}
	
	private FLayers getFLayers () {
		FLayers flyrs = null;
		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		
		if( !(window instanceof View) ) {
			IWindow[] windowList = PluginServices.getMDIManager().getAllWindows();
			for (int i = 0; i < windowList.length; i++) {
				if(windowList[i] instanceof View) {
					window = windowList[i];
					break;
				}
			}
		}
		
		if ( window != null && window instanceof View )
			if ( ((View) window).getMapControl() != null )
				if ( ((View) window).getMapControl().getMapContext() != null )
					flyrs = ((View) window).getMapControl().getMapContext().getLayers();
		return flyrs;
	}
	
}

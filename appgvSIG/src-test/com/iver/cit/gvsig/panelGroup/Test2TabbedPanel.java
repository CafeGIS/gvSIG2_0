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

package com.iver.cit.gvsig.panelGroup;

import javax.swing.JFrame;

import org.gvsig.gui.beans.panelGroup.PanelGroupManager;
import org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel;
import org.gvsig.tools.exception.BaseException;

import com.iver.cit.gvsig.panelGroup.loaders.PanelGroupLoaderFromExtensionPoint;
import com.iver.cit.gvsig.panelGroup.samples.Samples_ExtensionPointsOfIPanels;

/**
 * <p>Tests the creation of a {@link TabbedPanel TabbedPanel} object using {@link PanelGroupLoaderFromExtensionPoint PanelGroupLoaderFromExtensionPoint} .</p>
 * 
 * @version 16/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class Test2TabbedPanel {
	/**
	 * <p>Test method for the Test2TabbedPanel.</p>
	 * 
	 * @param args optional arguments
	 */
	public static void main(String[] args) {
		try {
			Samples_ExtensionPointsOfIPanels.loadSample();
			
			PanelGroupManager manager = PanelGroupManager.getManager();
			manager.registerPanelGroup(TabbedPanel.class);
			manager.setDefaultType(TabbedPanel.class);

			TabbedPanel panelGroup = (TabbedPanel) manager.getPanelGroup(Samples_ExtensionPointsOfIPanels.REFERENCE2);
			PanelGroupLoaderFromExtensionPoint loader = new PanelGroupLoaderFromExtensionPoint(Samples_ExtensionPointsOfIPanels.EXTENSIONPOINT2_NAME);

			// Begin: Test the normal load
			panelGroup.loadPanels(loader);
			// End: Test the normal load

			// Objects creation
			JFrame jFrame = new JFrame();
			jFrame.setTitle("Test TabbedPanel using PanelGroupLoaderFromExtensionPoint");
		    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    jFrame.setSize(panelGroup.getPreferredSize());
		    jFrame.getContentPane().add(panelGroup);
		    
			jFrame.setVisible(true);
			
		} catch (BaseException bE) {
			System.out.println(bE.getLocalizedMessageStack());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
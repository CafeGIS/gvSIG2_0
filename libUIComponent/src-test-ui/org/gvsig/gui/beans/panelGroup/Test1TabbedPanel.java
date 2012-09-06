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

package org.gvsig.gui.beans.panelGroup;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.tools.exception.BaseException;
import org.gvsig.gui.beans.panelGroup.loaders.PanelGroupLoaderFromList;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;
import org.gvsig.gui.beans.panelGroup.samples.SampleInfoPanel;
import org.gvsig.gui.beans.panelGroup.samples.Samples_Data;
import org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel;

/**
 * <p>Tests the creation of a {@link TabbedPanel TabbedPanel} object using {@link PanelGroupLoaderFromList PanelGroupLoaderFromList} .</p>
 * 
 * @version 16/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class Test1TabbedPanel {
	private static TabbedPanel panelGroup;
	
	/**
	 * <p>Test method for the Test1TabbedPanel.</p>
	 * 
	 * @param args optional arguments
	 */
	public static void main(String[] args) {
		try {
			PanelGroupManager manager = PanelGroupManager.getManager();
			manager.registerPanelGroup(TabbedPanel.class);
			manager.setDefaultType(TabbedPanel.class);

			panelGroup = (TabbedPanel) manager.getPanelGroup(Samples_Data.REFERENCE1);
			PanelGroupLoaderFromList loader = new PanelGroupLoaderFromList(Samples_Data.TEST1_CLASSES);

			// Begin: Test the normal load
			System.out.println("----------> Begin: Test the normal load");

			panelGroup.loadPanels(loader);

			System.out.println("End: Test the normal load <----------");
			// End: Test the normal load
			
			// Begin: Other tests
			ArrayList<IPanel> panels = new ArrayList<IPanel>();

			// Begin: Initialize sample classes
//			int i = 0;
//
//			for (AbstractPanel panel: panels) {
//				panel.setPreferredSize(new Dimension(Samples_Data.PANELS_DEFAULT_WIDTH, Samples_Data.PANELS_DEFAULT_HEIGHT));
//				panel.setReference(Samples_Data.REFERENCE1);
//				
//				panel.setID(Samples_Data.PANELS1_IDS[i]);
//				panel.setLabel(Samples_Data.PANELS1_LABELS[i]);
//				panel.setLabelGroup(Samples_Data.PANELS1_LABELGROUPS[i++]);
//				
//				panelGroup.addPanel(panel);
//			}
			// End: Initialize sample classes

			// Begin: Tests adding and removing a panel without "label" -> this shoudn't add that panel
			System.out.println("----------> Begin: Tests adding and removing a panel without \"label\" -> this shoudn't add that panel");
			loader = new PanelGroupLoaderFromList(new Class[]{SampleInfoPanel.class});
			loader.loadPanels(panels);

			panels.get(0).setReference(Samples_Data.REFERENCE1);
			panels.get(0).setID("ID");
			panels.get(0).setLabel(null);
			panels.get(0).setLabelGroup("LABEL_GROUP");
			panelGroup.addPanel(panels.get(0));
			panelGroup.removePanel(panels.get(0));
			System.out.println("End: Tests adding and removing a panel without \"label\" -> this shoudn't add that panel <----------");
			// End: Tests adding and removing a panel without "label" -> this shoudn't add that panel

			// Begin: Tests: has changed?
			System.out.println("----------> Begin: Tests: has changed?");
			Object[] oPanels = panelGroup.values().toArray();

			((AbstractPanel)oPanels[0]).setPreferredSize(new Dimension(700, 600));
			System.out.println("Has the new panel '" + ((AbstractPanel)oPanels[0]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[0]).hasChanged());

			String old_Label = ((AbstractPanel)oPanels[0]).getLabel();
			((AbstractPanel)oPanels[0]).setLabel(((AbstractPanel)oPanels[0]).getLabel() + " modified");
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[0]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[0]).hasChanged());

			((AbstractPanel)oPanels[0]).setLabel(old_Label);
			((AbstractPanel)oPanels[0]).resetChangedStatus();
			((AbstractPanel)oPanels[0]).setID(((AbstractPanel)oPanels[0]).getID() + " modified");
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[0]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[0]).hasChanged());

			((AbstractPanel)oPanels[0]).resetChangedStatus();
			((AbstractPanel)oPanels[0]).setLabelGroup(((AbstractPanel)oPanels[0]).getLabelGroup() + " modified");
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[0]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[0]).hasChanged());

			((AbstractPanel)oPanels[0]).resetChangedStatus();
			System.out.println("Has the panel reseted '" + ((AbstractPanel)oPanels[0]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[0]).hasChanged());
			System.out.println("End: Tests: has changed? <----------");
			// End: Tests: has changed?

			// Begin: Test visibility
			System.out.println("----------> Begin: Test visibility");
			((AbstractPanel)oPanels[0]).setID("ID");
			((AbstractPanel)oPanels[0]).setLabel("LABEL_TEST_VISIBILITY");
			((AbstractPanel)oPanels[0]).setLabelGroup("LABEL_GROUP");

			panelGroup.addPanel(((AbstractPanel)oPanels[0]));
			
			((AbstractPanel)oPanels[0]).setInGroupGUI(false);    // test change the visibility from a panel child
			if (!((AbstractPanel)oPanels[0]).isInGroupGUI()) {
				System.out.println("Test OK: the panel isn't in the GUI");
				System.out.println("Hay " + panelGroup.getPanelInGUICount() + " paneles en GUI, de los " + panelGroup.getPanelCount() + " registrados.");
			}
			else {
				System.out.println("Test FAILED: the panel is in the GUI");
			}
			
			((AbstractPanel)oPanels[0]).setInGroupGUI(true);     // test change the visibility from a panel child
			if (((AbstractPanel)oPanels[0]).isInGroupGUI()) {
				System.out.println("Test OK: the panel is in the GUI");
				System.out.println("Hay " + panelGroup.getPanelInGUICount() + " paneles en GUI, de los " + panelGroup.getPanelCount() + " registrados.");
			}
			else {
				System.out.println("Test FAILED: the panel isn't in the GUI");
			}
			
			panelGroup.setPanelInGUI(((AbstractPanel)oPanels[0]), false);
			if (!panelGroup.isPanelInGUI(((AbstractPanel)oPanels[0]))) {
				System.out.println("Test OK: the panel isn't in the GUI");
				System.out.println("Hay " + panelGroup.getPanelInGUICount() + " paneles en GUI, de los " + panelGroup.getPanelCount() + " registrados.");
			}
			else {
				System.out.println("Test FAILED: the panel is in the GUI");
			}
			
			panelGroup.setPanelInGUI(((AbstractPanel)oPanels[0]), true);
			if (panelGroup.isPanelInGUI(((AbstractPanel)oPanels[0]))) {
				System.out.println("Test OK: the panel is in the GUI");
				System.out.println("Hay " + panelGroup.getPanelInGUICount() + " paneles en GUI, de los " + panelGroup.getPanelCount() + " registrados.");
			}
			else {
				System.out.println("Test FAILED: the panel isn't in the GUI");
			}
			System.out.println("End: Test visibility <----------");
			// End: Test visibility

			// Begin: Tests: Is a panel enabled?
			System.out.println("----------> Begin: Tests: Is a panel enabled?");
			panelGroup.setEnabledAt(0, false); // test change the enable status to a panel
			if (!panelGroup.isEnabledAt(0)) {
				System.out.println("Test OK: the panel isn't enabled.");
			}
			else {
				System.out.println("Test FAILED: the panel is enabled.");
			}
			
			panelGroup.setEnabledAt(0, true); // test change the enable status to a panel
			if (panelGroup.isEnabledAt(0)) {
				System.out.println("Test OK: the panel is enabled.");
			}
			else {
				System.out.println("Test FAILED: the panel isn't enabled.");
			}
			
			panelGroup.setEnabledAt(3, false); // test change the enable status to a panel
			if (!panelGroup.isEnabledAt(3)) {
				System.out.println("Test OK: the panel isn't enabled.");
			}
			else {
				System.out.println("Test FAILED: the panel is enabled.");
			}
			
			panelGroup.setEnabledAt(3, true); // test change the enable status to a panel
			if (panelGroup.isEnabledAt(3)) {
				System.out.println("Test OK: the panel is enabled.");
			}
			else {
				System.out.println("Test FAILED: the panel isn't enabled.");
			}
			System.out.println("End: Tests: Is a panel enabled? <----------");
			// End: Tests: Is a panel enabled?
			
			// Begin: Test: accept, apply or cancel, after request if has changed?
			System.out.println("----------> Begin: Test: accept, apply or cancel, after request if has changed?");
//			Object[] obj_panels = (((Collection<IPanel>) panelGroup.values()).toArray());
			
			// First part:
			System.out.println("First part:");
			System.out.println("Is the new panel '" + ((AbstractPanel)oPanels[1]).getLabel() + "' always applicable?: " + ((AbstractPanel)oPanels[1]).isAlwaysApplicable());
			System.out.println("Has the new panel '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			((AbstractPanel)oPanels[1]).setLabel(((AbstractPanel)oPanels[1]).getLabel() + " modified");
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.accept();
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after accept?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			((AbstractPanel)oPanels[1]).setLabel(((AbstractPanel)oPanels[1]).getLabel() + " modified");
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.apply();
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after apply?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			((AbstractPanel)oPanels[1]).setLabel(((AbstractPanel)oPanels[1]).getLabel() + " modified");
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.cancel();
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after cancel?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.accept();
			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after accept?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.apply();
			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after apply?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.cancel();
			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after cancel?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			// Second part:
			System.out.println("Second part:");
			((AbstractPanel)oPanels[1]).setAlwaysApplicable(false);
			System.out.println("Is the new panel '" + ((AbstractPanel)oPanels[1]).getLabel() + "' always applicable?: " + ((AbstractPanel)oPanels[1]).isAlwaysApplicable());
			System.out.println("Has the new panel '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			((AbstractPanel)oPanels[1]).setLabel(((AbstractPanel)oPanels[1]).getLabel() + " modified");
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.accept();
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after accept?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			((AbstractPanel)oPanels[1]).setLabel(((AbstractPanel)oPanels[1]).getLabel() + " modified");
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.apply();
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after apply?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			((AbstractPanel)oPanels[1]).setLabel(((AbstractPanel)oPanels[1]).getLabel() + " modified");
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.cancel();
			System.out.println("Has the panel modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after cancel?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.accept();
			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after accept?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.apply();
			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after apply?: " + ((AbstractPanel)oPanels[1]).hasChanged());

			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			panelGroup.cancel();
			System.out.println("Has the panel not modified '" + ((AbstractPanel)oPanels[1]).getLabel() + "' changed after cancel?: " + ((AbstractPanel)oPanels[1]).hasChanged());
			System.out.println("End: Test: accept, apply or cancel, after request if has changed? <----------");
			// End: Test: accept, apply or cancel, after request if has changed?
			
			JFrame jFrame = new JFrame();
			jFrame.setTitle("Test TabbedPanel using PanelGroupLoaderFromList");
		    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    jFrame.setSize(panelGroup.getPreferredSize());
		    jFrame.getContentPane().add(panelGroup);
			jFrame.setVisible(true);
			
			// Test the methods 'getSelectedIndex()' and 'getActivePanel()'
			// Register a change listener
			panelGroup.addChangeListener(new ChangeListener() {
		        // This method is called whenever the selected tab changes
		        public void stateChanged(ChangeEvent evt) {
		        	JTabbedPane jTabbedPanel = (JTabbedPane) evt.getSource();
		        	
		        	System.out.println("Selection changed. New selection index: " + jTabbedPanel.getSelectedIndex());

		        	showActivePanelInformation();
		        }
		    });
			
			// Test the method 'getSelectedIndex()':
			panelGroup.setSelectedIndex(2);
			
			// Test the change of the reference of the group
			panelGroup.updateReference("NEW REFERENCE");

		// End: Other tests
		} catch (BaseException e) {
			System.out.println(e.getLocalizedMessageStack());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>Shows information about the current active panel using the standard output.</p>
	 */
	private static void showActivePanelInformation() {
		// Test the method 'getActivePanel()'
		IPanel panel = panelGroup.getActivePanel();
		
    	System.out.println("New active panel:\n\tID: " + panel.getID() + "\n\tLABEL_GROUP: " + panel.getLabelGroup() + "\n\tLABEL: " + panel.getLabel() + "\n\tCLASS: " + panel.getClass());
	}
}
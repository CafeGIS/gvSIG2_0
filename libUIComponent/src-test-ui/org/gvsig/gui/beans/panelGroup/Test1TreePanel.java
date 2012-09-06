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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.gvsig.tools.exception.BaseException;
import org.gvsig.gui.beans.panelGroup.loaders.PanelGroupLoaderFromList;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;
import org.gvsig.gui.beans.panelGroup.samples.SampleInfoPanel;
import org.gvsig.gui.beans.panelGroup.samples.SamplePanelWithoutGroupLabel;
import org.gvsig.gui.beans.panelGroup.samples.Samples_Data;
import org.gvsig.gui.beans.panelGroup.treePanel.TreePanel;

/**
 * <p>Tests the creation of a {@link TreePanel TreePanel} object using {@link PanelGroupLoaderFromList PanelGroupLoaderFromList} .</p>
 * 
 * @version 17/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class Test1TreePanel {
	private static JFrame jFrame;
	private static TreePanel panelGroup;
	
	/**
	 * <p>Test method for the Test1TreePanel.</p>
	 * 
	 * @param args optional arguments
	 */
	public static void main(String[] args) {
		try {
			PanelGroupManager manager = PanelGroupManager.getManager();
			manager.registerPanelGroup(TreePanel.class);
			manager.setDefaultType(TreePanel.class);

			// Use the defined  samples 
//			Class[] panelClass = new Class[Samples_Data.TEST1_CLASSES.length];
//			int i;
//			
//			for (i = 0; i < Samples_Data.TEST1_CLASSES.length; i ++) {
//				panelClass[i] = Samples_Data.TEST1_CLASSES[i];
//			}

			panelGroup = (TreePanel) manager.getPanelGroup(Samples_Data.REFERENCE1);
			PanelGroupLoaderFromList loader = new PanelGroupLoaderFromList(Samples_Data.TEST1_CLASSES);

			// Begin: Test the normal load
			panelGroup.loadPanels(loader);
			// End: Test the normal load
			
			// Begin: Other tests
			ArrayList<IPanel> panels = new ArrayList<IPanel>();

			loader.loadPanels(panels);

			// Begin: Initialise sample classes
//			i = 0;
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
			// End: Initialise sample classes
			
			// Begin: Tests adding and removing a panel without "label" -> this shoudn't add that panel
			panels = new ArrayList<IPanel>();
			loader = new PanelGroupLoaderFromList(new Class[]{SampleInfoPanel.class, SampleInfoPanel.class, SamplePanelWithoutGroupLabel.class});
			loader.loadPanels(panels);
			((AbstractPanel)panels.get(0)).setPreferredSize(new Dimension(600, 700));
			panels.get(0).setReference(Samples_Data.REFERENCE1);
			panels.get(0).setID("ID");
			panels.get(0).setLabel(null);
			panels.get(0).setLabelGroup("LABEL_GROUP");
			panelGroup.addPanel(panels.get(0));
			AbstractPanel incorrectAPanel = (AbstractPanel) panels.get(0);
			//panelGroup.removePanel(panels.get(0));
			// End: Tests adding and removing a panel without "label" -> this shoudn't add that panel

			// Begin: Tests adding and removing a panel
			((AbstractPanel)panels.get(1)).setPreferredSize(new Dimension(Samples_Data.PANELS_DEFAULT_WIDTH, Samples_Data.PANELS_DEFAULT_HEIGHT));
			panels.get(1).setReference(Samples_Data.REFERENCE1);
			panels.get(1).setID("ID");
			panels.get(1).setLabel("LABEL_TEST_REPEATED_GROUP");
			panels.get(1).setLabelGroup(Samples_Data.PANELS1_LABELGROUPS[0]);
			panelGroup.addPanel(panels.get(1));
			AbstractPanel repeatedGroupAPanel = (AbstractPanel) panels.get(1);
			//panelGroup.removePanel(panels.get(0));
			// End: Tests adding and removing a panel
			
			// Begin: Tests adding and removing a panel without "labelGroup"
			((AbstractPanel)panels.get(2)).setPreferredSize(new Dimension(Samples_Data.PANELS_DEFAULT_WIDTH, Samples_Data.PANELS_DEFAULT_HEIGHT));
			panels.get(2).setReference(Samples_Data.REFERENCE1);
			panelGroup.addPanel(panels.get(2));
			//panelGroup.removePanel(panels.get(1));
			// End: Tests adding and removing a panel without "labelGroup"
			
			// Begin: Test visibility			
			Object[] oPanels = panelGroup.values().toArray();			
			panelGroup.setPanelInGUI(repeatedGroupAPanel, false);// test set false a panel with a repeated group
			panelGroup.setPanelInGUI((IPanel)oPanels[1], false); // intermediate element
			 ((AbstractPanel)oPanels[1]).setInGroupGUI(true);    // test change the visibility from a panel child
			 ((AbstractPanel)oPanels[1]).setInGroupGUI(false);    // test change the visibility from a panel child
			 ((AbstractPanel)oPanels[1]).setInGroupGUI(true);    // test change the visibility from a panel child
			panelGroup.setPanelInGUI((IPanel)oPanels[0], false); // first element
			panelGroup.setPanelInGUI(incorrectAPanel, false);	 // incorrect element
			panelGroup.setPanelInGUI(panels.get(2), false);			 // last element (and labelGroup = null)
			panelGroup.setPanelInGUI((IPanel)oPanels[2], false); // intermediate element
			panelGroup.setPanelInGUI((IPanel)oPanels[2], false); // test set false two times the same node
			panelGroup.setPanelInGUI((IPanel)oPanels[0], true);  // first element
			panelGroup.setPanelInGUI(incorrectAPanel, true);	 // incorrect element
			panelGroup.setPanelInGUI((IPanel)oPanels[1], true);  // intermediate element
			panelGroup.setPanelInGUI((IPanel)oPanels[1], true);  // test set true two times the same node
			panelGroup.setPanelInGUI((IPanel)oPanels[2], true);  // intermediate element
			panelGroup.setPanelInGUI(panels.get(2), true); 			 // last element (and labelGroup = null)
			panelGroup.setPanelInGUI(repeatedGroupAPanel, true); // test set false a panel with a repeated group
			
			panels.get(1).setInGroupGUI(false);    // test change the visibility from a panel child
			if (!panels.get(1).isInGroupGUI()) {
				System.out.println("Test OK: the panel isn't in the GUI");
				System.out.println("Hay " + panelGroup.getPanelInGUICount() + " paneles en GUI, de los " + panelGroup.getPanelCount() + " registrados.");
			}
			else {
				System.out.println("Test FAILED: the panel is in the GUI");
			}
			
			panels.get(1).setInGroupGUI(true);     // test change the visibility from a panel child
			if (panels.get(1).isInGroupGUI()) {
				System.out.println("Test OK: the panel is in the GUI");
				System.out.println("Hay " + panelGroup.getPanelInGUICount() + " paneles en GUI, de los " + panelGroup.getPanelCount() + " registrados.");
			}
			else {
				System.out.println("Test FAILED: the panel isn't in the GUI");
			}
			
			panelGroup.setPanelInGUI(panels.get(1), false);
			if (!panelGroup.isPanelInGUI(panels.get(1))) {
				System.out.println("Test OK: the panel isn't in the GUI");
				System.out.println("Hay " + panelGroup.getPanelInGUICount() + " paneles en GUI, de los " + panelGroup.getPanelCount() + " registrados.");
			}
			else {
				System.out.println("Test FAILED: the panel is in the GUI");
			}
			
			panelGroup.setPanelInGUI(panels.get(1), true);
			if (panelGroup.isPanelInGUI(panels.get(1))) {
				System.out.println("Test OK: the panel is in the GUI");
				System.out.println("Hay " + panelGroup.getPanelInGUICount() + " paneles en GUI, de los " + panelGroup.getPanelCount() + " registrados.");
			}
			else {
				System.out.println("Test FAILED: the panel isn't in the GUI");
			}
			// End: Test visibility -> True or False
			
			jFrame = new JFrame();
			jFrame.setTitle("Test TreePanel using PanelGroupLoaderFromList");
		    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    jFrame.setSize(panelGroup.getPreferredSize());
		    jFrame.getContentPane().setLayout(new BorderLayout());
		    jFrame.getContentPane().add(panelGroup, BorderLayout.CENTER);
		    
			jFrame.setVisible(true);
			
			// Begin: Test the method 'getSelectedIndex()':
			panelGroup.addTreeSelectionListener(new TreeSelectionListener() {
				/*
				 * (non-Javadoc)
				 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
				 */
				public void valueChanged(TreeSelectionEvent e) {
		        	JTree jTree = (JTree) e.getSource();
		        	
		        	System.out.println("Selection changed. New selection path: " + jTree.getSelectionPath());

		        	showActivePanelInformation();
				}
			});
			// End: Test the method 'getSelectedIndex()':
				
			// Begin: Test the methods: 'getDividerLocation()' and 'setDividerLocation(int)'
			System.out.println("Divider location before setting it to the position 250 : " + panelGroup.getDividerLocation());
			panelGroup.setDividerLocation(250);
			System.out.println("Divider location after setting it to the position 250 : " + panelGroup.getDividerLocation());
			// End: Test the methods: 'getDividerLocation()' and 'setDividerLocation(int)'

			// Begin: Test the methods: 'getDividerSize()' and 'setDividerSize(int)'
			System.out.println("Divider size before setting it to the position 10 : " + panelGroup.getDividerSize());
			panelGroup.setDividerSize(10);
			System.out.println("Divider size after setting it to the position 10 : " + panelGroup.getDividerSize());
			// End: Test the methods: 'getDividerSize()' and 'setDividerSize(int)'
			
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

		if (panel == null) {
			System.out.println("The node selected of the JTree in the TreePanel object isn't a IPanel .");
		}
		else {
			System.out.println("New active panel:\n\tID: " + panel.getID() + "\n\tLABEL_GROUP: " + panel.getLabelGroup() + "\n\tLABEL: " + panel.getLabel() + "\n\tCLASS: " + panel.getClass());
		}
	}
}
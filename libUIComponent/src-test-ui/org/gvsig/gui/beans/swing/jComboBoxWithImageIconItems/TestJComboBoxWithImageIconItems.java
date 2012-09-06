package org.gvsig.gui.beans.swing.jComboBoxWithImageIconItems;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;

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

/**
 * Tests JPanelWithJComboBoxImageItems
 * 
 * This code has been extracted and modified from the Java Sun Microsystems's example: 'CustomComboBoxDemo.java'
 *   in http://java.sun.com/docs/books/tutorial/uiswing/components/combobox.html
 * 
 * @author Sun Microsystems
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestJComboBoxWithImageIconItems {
    /**
     * Main test method
     * 
     * @param args Arguments
     */
    public static void main(String[] args) {
    	/**
    	 * Schedule a job for the event-dispatching thread:
    	 * creating and showing this application's GUI.
    	 */
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
        	/*
        	 * (non-Javadoc)
        	 * @see java.lang.Runnable#run()
        	 */
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Test TestJComboBoxWithImageIconItems");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComboBoxWithImageIconItems component = new JComboBoxWithImageIconItems(); 
        component.setOpaque(true); //content panes must be opaque
        frame.setContentPane(component);
        component.addActionListener(new ActionListener() {
        	/*
        	 *  (non-Javadoc)
        	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
        	 */
			public void actionPerformed(ActionEvent e) {		
				JComboBoxWithImageIconItems cb = (JComboBoxWithImageIconItems)e.getSource();
				ImageIconItemInfo iiii = (ImageIconItemInfo)cb.getSelectedItem();
				if (iiii != null) {
					String value = (String)iiii.getItemValue();
					if (value != null)
						System.out.println("Seleccionado item con null: " + value);
					else
						System.out.println("Seleccionado item con con valor null");
				}
			}
        	
        });
        
        /**** METHOD TESTS ****/

        /**** Don't Remove any test! ****/
        /**** Some tests could be commented but not removed! ****/

        // Test 'setJComboBoxRendererPreferredSize' method
        component.setJComboBoxRendererPreferredSize(new Dimension(150, 80));

        // Test 'getJComboBoxRendererPreferredSize' method
        System.out.println("JComboBox Renderer Preferred Size: " + component.getJComboBoxRendererPreferredSize());

        // Data samples
        ImageIconItemInfo backward = new ImageIconItemInfo("images/backward.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "images/backward.png"), "Retroceder", null);
        ImageIconItemInfo fastbackward = new ImageIconItemInfo("images/fastbackward.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "images/fastbackward.png"), "Retroceso rápido", null);
        ImageIconItemInfo forward = new ImageIconItemInfo("images/forward.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "images/forward.png"), "Avanzar", "Avanzar");
        ImageIconItemInfo fastforward = new ImageIconItemInfo("images/fastforward.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "images/fastforward.png"), "Avance rápido", null);
        
        ImageIconItemInfo error1 = new ImageIconItemInfo("Error1.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "Error1.png"),"Error 1", "Error 1");
        ImageIconItemInfo error2 = new ImageIconItemInfo("Error2.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "Error2.png"),"Error 2", null);
        ImageIconItemInfo error3 = new ImageIconItemInfo("Error3.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "Error3.png"),"Error 3", "Error 3");
        ImageIconItemInfo error4 = new ImageIconItemInfo("Error4.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "Error4.png"),"Error 4", null);
        ImageIconItemInfo error5 = new ImageIconItemInfo("Error5.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "Error5.png"),"Error 5", "Error 5");
        ImageIconItemInfo error6 = new ImageIconItemInfo("Error6.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "Error6.png"),"Error 6", null);
        ImageIconItemInfo error7 = new ImageIconItemInfo("Error7.png", ImageIconItemInfoUtils.createImageIcon(TestJComboBoxWithImageIconItems.class, "Error7.png"),"Error 5", null);
        
        // Test 'addImageIcon' method
        component.addImageIconItem(backward);
        component.addImageIconItem(error1);
        
        // Test 'removeImageIcon' method
        component.removeImageIconItem(backward);
        component.removeImageIconItem(error2);
        component.removeImageIconItem(error1);

        // Test 'getMaximumRowCount' method
        System.out.println("Número de filas por defecto: " + component.getMaximumRowCount());

        // Test 'setMaximumRowCount' method
        component.setMaximumRowCount(4);

        // Test 'getMaximumJComboBoxRowCount' method
        System.out.println("Número de filas despues de modificarlo el atributo: " + component.getMaximumRowCount());

        // Tests 'addImageIcons(String [])' method
        ImageIconItemInfo[] icons = {backward, forward, forward, // Duplicated 'forward.png'
        		error3, fastbackward, fastforward};
        component.addImageIconItems(icons);

        // Tests 'removeAllImageIcons' method
        component.removeAllImageIconItems();

        // Tests 'removeImageIcons(String [])' method
        component.addImageIconItems(icons);
        ImageIconItemInfo[] icons2 = {error3, error4, forward};
        component.removeImageIconItems(icons2);

        // Tests 'addImageIcons(Vector)' method
        Vector icons3 = new Vector();
        icons3.add(forward); // Allow repeated items
        icons3.add(error5);
        icons3.add(backward); // Allow repeated items
        icons3.add(error6);
        component.addImageIconItems(icons3);

        // Tests 'setShowImageIconAndTextProperty' method
        component.setShowImageIconAndTextProperty(true);
        component.setShowImageIconAndTextProperty(false);

        // Tests 'removeImageIcons(Vector)' method
        Vector icons4 = new Vector();
        icons4.add(forward); // Remove a repeted item (only one time)
        icons4.add(error6);
        icons4.add(error7); // Tries to remove an item but its tool tip text it's incorrect and then it can't remove this item
        component.removeImageIconItems(icons4);

        /**** END METHOD TESTS ****/

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
package org.gvsig.crs.gui.panels;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


/**
 * Test para el interfaz de CRSs Recientes
 * @author Luisa Marina Fernández Ruiz (luisam.fernandez@uclm.es)
 *
 */
public class CrsRecentsPanelTest {

	public static void main(String[] args) {
        JFrame frame = new JFrame("CrsRecents Test");
        //cerrar el formulario
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        frame.getContentPane().add(new CrsRecentsPanel());
        frame.setLocation(300,0);
        frame.pack();
        frame.setVisible(true);
   
}
}



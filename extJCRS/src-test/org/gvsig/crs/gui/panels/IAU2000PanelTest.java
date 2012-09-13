package org.gvsig.crs.gui.panels;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
/**
 * Test para el interfaz de IAU200Panel
 * @author Luisa Marina Fernández Ruiz (luisam.fernandez@uclm.es)
 *
 */
public class IAU2000PanelTest {

	public static void main(String[] args) {

        JFrame frame = new JFrame("UAU200 Panel Test");
        //cerrar el formulario
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        frame.getContentPane().add(new IAU2000panel());
        frame.setLocation(300,0);
        frame.pack();
        frame.setVisible(true);
   
}

}

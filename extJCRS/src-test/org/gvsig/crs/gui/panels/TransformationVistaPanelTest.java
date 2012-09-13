package org.gvsig.crs.gui.panels;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 *  Test para el interfaz de transformación de la vista dentro de la transformacion compuesta
 * @author Luisa Marina Fernández Ruiz (luisam.fernandez@uclm.es)
 *
 */
public class TransformationVistaPanelTest {
	
	public static void main(String[] args) {
		 JFrame frame = new JFrame("Transformación de la Capa");
		
//	         //cerrar el formulario
	         frame.addWindowListener(new WindowAdapter() {
	             public void windowClosing(WindowEvent e) {System.exit(0);}
	         });
	 
	         frame.getContentPane().add(new TransformationVistaPanel("",null));
	         frame.setSize(300,500);
	         //colocar en la pantalla
	         frame.setLocation(300,0);
	         frame.pack();
	         frame.setVisible(true);
	    
	}
}

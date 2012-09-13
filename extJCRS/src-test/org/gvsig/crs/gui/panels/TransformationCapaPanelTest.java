package org.gvsig.crs.gui.panels;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
/**
 * Test para el interfaz de Transformaci�n de la capa dentro de la transformacion compuesta
 * @author Luisa Marina Fern�ndez Ruiz (luisam.fernandez@uclm.es)
 *
 */

public class TransformationCapaPanelTest {

	
	public static void main(String[] args) {
		
		 JFrame frame = new JFrame("Transformaci�n de la Capa");
	         //cerrar el formulario
	         frame.addWindowListener(new WindowAdapter() {
	             public void windowClosing(WindowEvent e) {System.exit(0);}
	         });
	 
	         frame.getContentPane().add(new TransformationCapaPanel("",null));
	         frame.setSize(300,500);
	         //colocar en la pantalla
	         frame.setLocation(300,0);
	         frame.pack();
	         frame.setVisible(true);
	    
	}
}

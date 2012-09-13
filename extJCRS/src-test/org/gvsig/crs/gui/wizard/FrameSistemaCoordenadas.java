package org.gvsig.crs.gui.wizard;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import org.gvsig.crs.gui.panels.wizard.DefSistCoordenadas;

/**
 * Test para el interfaz del wizard de elección del sistema de coordenadas
 * @author Luisa Marina Fernández Ruiz (luisam.fernandez@uclm.es)
 *
 */
public class FrameSistemaCoordenadas {

	public static void main(String[] args) {
		
	         JFrame frame = new JFrame("Definicion del Sistema de Coordenadas");
	 	
	         //cerrar el formulario
	         frame.addWindowListener(new WindowAdapter() {
	             public void windowClosing(WindowEvent e) {System.exit(0);}
	         });
	 
	         frame.getContentPane().add(new DefSistCoordenadas());
	         frame.setSize(300,500);
	         //colocar en la pantalla
	         frame.setLocation(300,300);
	         frame.pack();
	         frame.setVisible(true);
	    
	}

}

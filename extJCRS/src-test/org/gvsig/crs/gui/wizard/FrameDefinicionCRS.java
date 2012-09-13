package org.gvsig.crs.gui.wizard;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.gvsig.crs.gui.panels.wizard.DefCrsUsr;

public class FrameDefinicionCRS {


	/**
	 * @param args
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
			/*
			 com.sun.java.swing.plaf.motif.MotifLookAndFeel
			 com.sun.java.swing.plaf.windows.WindowsLookAndFeel
			 javax.swing.plaf.metal.MetalLookAndFeel
			 */
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	         JFrame frame = new JFrame("Definicin de CRS por el usuario");
	         //cerrar el formulario
	         frame.addWindowListener(new WindowAdapter() {
	             public void windowClosing(WindowEvent e) {System.exit(0);}
	         });
	         
	         frame.getContentPane().add(new DefCrsUsr(null));
	         frame.setSize(300,500);
	         //colocar en la pantalla
	         frame.setLocation(300,300);
	         frame.pack();
	         frame.setVisible(true);
	    
	}

}

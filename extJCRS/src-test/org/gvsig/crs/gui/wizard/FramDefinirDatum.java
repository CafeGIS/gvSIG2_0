package org.gvsig.crs.gui.wizard;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.gvsig.crs.gui.panels.wizard.DefinirDatum;

public class FramDefinirDatum {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		 JFrame frame = new JFrame("Definicin del Datum");
				/*
			 com.sun.java.swing.plaf.motif.MotifLookAndFeel
			 com.sun.java.swing.plaf.windows.WindowsLookAndFeel
			 javax.swing.plaf.metal.MetalLookAndFeel
			 */
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	         //cerrar el formulario
	         frame.addWindowListener(new WindowAdapter() {
	             public void windowClosing(WindowEvent e) {System.exit(0);}
	         });
	 
	         frame.getContentPane().add(new DefinirDatum());
	         frame.setSize(300,500);
	         //colocar en la pantalla
	         frame.setLocation(300,0);
	         frame.pack();
	         frame.setVisible(true);
	    
	}

}

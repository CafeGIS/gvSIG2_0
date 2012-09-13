package org.gvsig.rastertools.properties.panels;

import javax.swing.JFrame;

import org.gvsig.gui.beans.table.exceptions.NotInitializeException;

public class TestBrightnessAndContrastPanel{
	private int 				w = 200, h = 220;
	private JFrame 				frame=new JFrame();
	private EnhancedBrightnessContrastPanel	slider = null;
	
	public TestBrightnessAndContrastPanel() throws NotInitializeException{
		slider = new EnhancedBrightnessContrastPanel();		
		frame.getContentPane().add(slider);
		frame.setSize(w, h);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		try{
			new TestBrightnessAndContrastPanel();
		}catch(NotInitializeException ex){
			System.out.println("Tabla no inicializada");
		}
	}
}

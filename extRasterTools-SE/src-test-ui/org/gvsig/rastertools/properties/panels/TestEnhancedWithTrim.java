package org.gvsig.rastertools.properties.panels;

import javax.swing.JFrame;

import org.gvsig.gui.beans.table.exceptions.NotInitializeException;

public class TestEnhancedWithTrim{
	private int 				w = 200, h = 220;
	private JFrame 				frame=new JFrame();
	private EnhancedWithTrimPanel	slider = null;
	
	public TestEnhancedWithTrim() throws NotInitializeException{
		slider = new EnhancedWithTrimPanel();		
		frame.getContentPane().add(slider);
		frame.setSize(w, h);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		try{
			new TestEnhancedWithTrim();
		}catch(NotInitializeException ex){
			System.out.println("Tabla no inicializada");
		}
	}
}

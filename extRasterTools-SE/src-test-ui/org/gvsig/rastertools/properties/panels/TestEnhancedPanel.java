package org.gvsig.rastertools.properties.panels;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class TestEnhancedPanel {
	private int 				w = 400, h = 250;
	private JFrame 				frame=new JFrame();
	private EnhancedPanel	slider = null;
	
	public TestEnhancedPanel() {
		slider = new EnhancedPanel();		
		frame.getContentPane().add(slider);
		frame.setSize(w, h);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
    }
		new TestEnhancedPanel();
	}
}

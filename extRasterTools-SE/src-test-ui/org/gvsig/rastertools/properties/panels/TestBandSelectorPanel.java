package org.gvsig.rastertools.properties.panels;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class TestBandSelectorPanel {
	private int 				w = 450, h = 400;
	private JFrame 				frame=new JFrame();
	private BandSelectorPanel	slider = null;
	
	public TestBandSelectorPanel() {
		slider = new BandSelectorPanel();		
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
		new TestBandSelectorPanel();
	}
}

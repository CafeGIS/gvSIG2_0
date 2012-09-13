package org.gvsig.rastertools.saveraster;

import javax.swing.JFrame;

import org.gvsig.rastertools.saveraster.ui.panels.MethodSelectorPanel;

/**
 * Test para el panel de salvar a raster.
 * @version 09/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class MethodSelectorFrameTest extends JFrame {
	private static final long serialVersionUID = -6500043998556521467L;
	public MethodSelectorFrameTest(){
		this.setSize(100,100);
		MethodSelectorPanel panel = new MethodSelectorPanel();

		this.getContentPane().add(panel);
		this.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MethodSelectorFrameTest();
	}

}

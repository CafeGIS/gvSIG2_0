package org.gvsig.rastertools.saveraster;

import javax.swing.JFrame;

import org.gvsig.rastertools.saveraster.ui.panels.InputSizePanel;

/**
 * Test para el panel de salvar a raster.
 * @version 09/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class InputSizeFrameTest extends JFrame {
	private static final long serialVersionUID = -6500043998556521467L;
	public InputSizeFrameTest(){
		this.setSize(300,120);
		InputSizePanel panel = new InputSizePanel();

		this.getContentPane().add(panel);
		this.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new InputSizeFrameTest();
	}

}

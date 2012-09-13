package org.gvsig.rastertools.saveraster;

import javax.swing.JFrame;

import org.gvsig.rastertools.saveraster.ui.panels.SelectFilePanel;

/**
 * Test para el panel de selección de fichero de salvar a raster.
 * @version 09/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class SelectFileFrameTest extends JFrame {
	private static final long serialVersionUID = -6500043998556521467L;
	public SelectFileFrameTest(){
		this.setSize(300, 120);
		SelectFilePanel panel = new SelectFilePanel();

		this.getContentPane().add(panel);
		this.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SelectFileFrameTest();
	}

}

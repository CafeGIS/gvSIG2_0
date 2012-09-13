package org.gvsig.rastertools.saveraster;

import javax.swing.JFrame;

import org.gvsig.rastertools.saveraster.ui.SaveRasterPanel;

/**
 * Test para el panel de salvar a raster.
 * @version 09/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class SaveRasterFrameTest extends JFrame {
	private static final long serialVersionUID = -6500043998556521467L;
	public SaveRasterFrameTest(){
		this.setSize(380, 320);
		SaveRasterPanel panel = new SaveRasterPanel();

		this.getContentPane().add(panel);
		this.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SaveRasterFrameTest();
	}

}

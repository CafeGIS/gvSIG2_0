package org.gvsig.rastertools.saveraster;

import javax.swing.JFrame;

import org.gvsig.rastertools.saveraster.ui.panels.WCCoordsInputPanel;

/**
 * Test para el panel de salvar a raster.
 * @version 09/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class WCCoordInputFrameTest extends JFrame {
	private static final long serialVersionUID = -6500043998556521467L;
	public WCCoordInputFrameTest(){
		this.setSize(300,100);
		WCCoordsInputPanel panel = new WCCoordsInputPanel();

		this.getContentPane().add(panel);
		this.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new WCCoordInputFrameTest();
	}

}

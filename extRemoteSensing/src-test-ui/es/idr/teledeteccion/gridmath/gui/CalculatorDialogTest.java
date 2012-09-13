package es.idr.teledeteccion.gridmath.gui;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.gvsig.remotesensing.gridmath.gui.GridMathDialog;

public class CalculatorDialogTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		} catch (Exception e) {
			System.err.println("No se puede cambiar al LookAndFeel");
		}
		
		JFrame frame = new JFrame();
		GridMathDialog dialog = new GridMathDialog(null);
		frame.setSize(650,400);
		frame.getContentPane().add(dialog);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}
}
